package com.andy.mydartsapp.activity

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.*
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.andy.mydartsapp.GattAttributes
import com.andy.mydartsapp.R
import com.andy.mydartsapp.service.BluetoothLeService
import org.jetbrains.anko.toast
import java.util.*

class GB2Activity : AppCompatActivity() {
    companion object {
        const val PERMISSION_REQUEST_CODE_ALL = 1
        const val REQUEST_ENABLE_BT = 2
        val PERMISSIONS = arrayOf(
            android.Manifest.permission.BLUETOOTH,
            android.Manifest.permission.BLUETOOTH_ADMIN,
            android.Manifest.permission.ACCESS_COARSE_LOCATION)
        private val TAG: String = GB2Activity::class.java.simpleName
    }

    private var mScanning: Boolean? = null
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var mBluetoothLeScanner: BluetoothLeScanner? = null
    private var mBluetoothLeService: BluetoothLeService? = null
    private var gb2Address: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gb2)

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE_ALL)
        }

        val gattServiceIntent = Intent(this@GB2Activity, BluetoothLeService::class.java)
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE)
    }

    override fun onResume() {
        super.onResume()
        initBluetoothAdapter()
        scanLeDevice(true)

        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter())
        if (mBluetoothLeService != null) {
            val result = mBluetoothLeService!!.connect(gb2Address)
            Log.d(TAG, "Connect request result=$result")
        }
    }

    override fun onPause() {
        super.onPause()
        scanLeDevice(false)
        unregisterReceiver(mGattUpdateReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(mServiceConnection)
        mBluetoothLeService = null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            PERMISSION_REQUEST_CODE_ALL -> {
                for(grantResult: Int in grantResults) {
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        finish()
                        return
                    }
                }
            }
        }
    }

    private fun initBluetoothAdapter() {
        mBluetoothAdapter = (getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter

//        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled){
//            bluetoothAdapter.enable()
//        }

        if(mBluetoothAdapter == null) {
            toast(R.string.error_bluetooth_not_supported)
        }
        if(!mBluetoothAdapter!!.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish()
            return
        }
    }

    private fun scanLeDevice(enable: Boolean) {
        mBluetoothLeScanner = mBluetoothAdapter!!.bluetoothLeScanner
        if(enable) {
            mScanning = true
            mBluetoothLeScanner!!.startScan(mScanCallback)
        }else {
            mScanning = false
            mBluetoothLeScanner!!.stopScan(mScanCallback)
        }
    }

    private fun makeGattUpdateIntentFilter(): IntentFilter {
        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED)
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED)
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED)
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE)
        return intentFilter
    }

    private val mServiceConnection = object: ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName?, service: IBinder?) {
            mBluetoothLeService = (service as BluetoothLeService.LocalBinder).getService()

            if(!mBluetoothLeService!!.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth")
                finish()
            }

            mBluetoothLeService!!.connect(gb2Address)
        }

        override fun onServiceDisconnected(componentName: ComponentName?) {
            mBluetoothLeService = null
        }
    }

    private val mScanCallback = object: ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
//            Log.d(TAG, "onScanResult: ${result?.device?.name}")
//            Log.d(TAG, "onScanResult: ${result?.device?.address}")

            if(result?.device?.name == "GRANBOARD") {
                gb2Address = "E4:71:AB:4E:18:E6"
                if(mBluetoothLeService != null) {
                    mBluetoothLeService?.connect(gb2Address)
                }
            }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            Log.d(TAG, "onScanFailed: $errorCode")
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
            Log.d(TAG,"onBatchScanResults:${results.toString()}")
        }
    }

    private val mGattUpdateReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            when(action) {
                BluetoothLeService.ACTION_GATT_CONNECTED -> {

                }

                BluetoothLeService.ACTION_GATT_DISCONNECTED -> {

                }

                BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED -> {
                    Log.d(TAG, "mGattUpdateReceiver: onReceive")
                    val mGattService = mBluetoothLeService!!.getSupportedGattServices(UUID.fromString(GattAttributes.GRAN_BOARD_2_DARTS_TARGET_READ_SERVICE))
                    if(mGattService != null) {
                        val mGattCharacteristic = mGattService.getCharacteristic(UUID.fromString(GattAttributes.GRAN_BOARD_2_DARTS_TARGET_READ_CHARACTERISTIC))
                        if(mGattCharacteristic != null) {
                            val charaProp = mGattCharacteristic.properties
                            if((charaProp or BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                                mBluetoothLeService!!.setCharacteristicNotification(mGattCharacteristic, true)
                            }
                        }
                    }
                }

                BluetoothLeService.ACTION_DATA_AVAILABLE -> {

                }
            }
        }
    }
}
