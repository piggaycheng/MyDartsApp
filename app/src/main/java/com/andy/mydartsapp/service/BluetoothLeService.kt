package com.andy.mydartsapp.service

import android.app.Service
import android.bluetooth.*
import android.bluetooth.BluetoothAdapter.STATE_CONNECTING
import android.bluetooth.BluetoothAdapter.STATE_DISCONNECTED
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log

class BluetoothLeService: Service() {
    companion object {
        private val TAG: String = BluetoothLeService::class.java.simpleName
        val ACTION_GATT_CONNECTED: String = "com.andy.bluetooth.le.ACTION_GATT_CONNECTED"
        val ACTION_GATT_DISCONNECTED: String = "com.andy.bluetooth.le.ACTION_GATT_DISCONNECTED"
        val ACTION_GATT_SERVICES_DISCOVERED: String = "com.andy.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED"
        val ACTION_DATA_AVAILABLE: String = "com.andy.bluetooth.le.ACTION_DATA_AVAILABLE"
    }

    private val mBinder: IBinder = LocalBinder()
    private var mBluetoothDeviceAddress: String? = null
    private var mBluetoothGatt: BluetoothGatt? = null
    private var mBluetoothManager: BluetoothManager? = null
    private var mBluetoothAdapter: BluetoothAdapter? =null

    private var mConnectionState = STATE_DISCONNECTED

    inner class LocalBinder: Binder(){
        fun getService(): BluetoothLeService {
            return this@BluetoothLeService
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close()
        return super.onUnbind(intent)
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    private fun close() {
        if(mBluetoothGatt == null) {
            return
        }

        mBluetoothGatt!!.close()
        mBluetoothGatt = null
    }

    fun initialize(): Boolean {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        if(mBluetoothManager == null) {
            mBluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager

            if(mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.")
                return false
            }
        }

        mBluetoothAdapter = mBluetoothManager!!.adapter
        if(mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.")
            return false
        }

        return true
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     *
     * @return Return true if the connection is initiated successfully. The connection result
     *         is reported asynchronously through the
     *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     *         callback.
     */
    fun connect(address: String?): Boolean {
        if(address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.")
            return false
        }

        if(address == mBluetoothDeviceAddress && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.")
            return if (mBluetoothGatt!!.connect()) {
                mConnectionState = STATE_CONNECTING
                true
            } else {
                false
            }
        }

        var device: BluetoothDevice? = mBluetoothAdapter!!.getRemoteDevice(address)
        if(device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.")
            return false
        }

        mBluetoothGatt = device.connectGatt(this, true, mGattCallback)
        Log.d(TAG, "Trying to create a new connection.")
        mBluetoothDeviceAddress = address
        mConnectionState = STATE_CONNECTING

        return true
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    fun disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized")
            return
        }
        mBluetoothGatt!!.disconnect()
    }

    private val mGattCallback = object: BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, status)
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
            super.onCharacteristicChanged(gatt, characteristic)
        }
    }
}
