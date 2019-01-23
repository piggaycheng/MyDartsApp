package com.andy.mydartsapp.activity

import android.app.Activity
import android.bluetooth.BluetoothAdapter
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
import com.andy.mydartsapp.R
import com.andy.mydartsapp.service.BluetoothLeService
import org.jetbrains.anko.toast

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
    }

    override fun onPause() {
        super.onPause()
        scanLeDevice(false)
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

    private val mScanCallback = object: ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            Log.d(TAG, "onScanResult: ${result?.device?.name}")
            Log.d(TAG, "onScanResult: ${result?.device?.address}")

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

    private fun makeGattUpdateIntentFilter(): IntentFilter {
        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED)
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED)
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED)
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE)
        return intentFilter
    }
}
