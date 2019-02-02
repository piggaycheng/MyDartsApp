package com.andy.mydartsapp.service

import android.app.Service
import android.bluetooth.*
import android.bluetooth.BluetoothAdapter.*
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.andy.mydartsapp.GattAttributes
import java.util.*

class BluetoothLeService: Service() {
    companion object {
        private val TAG: String = BluetoothLeService::class.java.simpleName
        const val ACTION_GATT_CONNECTED: String = "com.andy.bluetooth.le.ACTION_GATT_CONNECTED"
        const val ACTION_GATT_DISCONNECTED: String = "com.andy.bluetooth.le.ACTION_GATT_DISCONNECTED"
        const val ACTION_GATT_SERVICES_DISCOVERED: String = "com.andy.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED"
        const val ACTION_DATA_AVAILABLE: String = "com.andy.bluetooth.le.ACTION_DATA_AVAILABLE"
        const val ACTION_SCORE_RETRIEVE: String = "com.andy.bluetooth.le.ACTION_SCORE_RETRIEVE"
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

    private fun broadcastUpdate(action: String) {
        val intent = Intent(action)
        sendBroadcast(intent)
    }

    private fun broadcastUpdate(action: String, score: String) {
        val intent = Intent(action)
        intent.putExtra("data", score)
        sendBroadcast(intent)
    }

    fun getSupportedGattServices(): List<BluetoothGattService>? {
        return if (mBluetoothGatt == null) {
            null
        }else {
            mBluetoothGatt!!.services
        }
    }

    fun getSupportedGattServices(uuid: UUID): BluetoothGattService? {
        return if (mBluetoothGatt == null) {
            null
        }else {
            mBluetoothGatt!!.getService(uuid)
        }

    }

    fun setCharacteristicNotification(
        characteristic: BluetoothGattCharacteristic,
        enabled: Boolean
    ) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized")
            return
        }
        mBluetoothGatt!!.setCharacteristicNotification(characteristic, enabled)

        // This is specific to Heart Rate Measurement.
        //*重要
        if (GattAttributes.GRAN_BOARD_2_DARTS_TARGET_READ_CHARACTERISTIC == characteristic.uuid.toString()) {
            val descriptor = characteristic.getDescriptor(
                UUID.fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG)
            )
            descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            mBluetoothGatt!!.writeDescriptor(descriptor)
        }
    }

    private val mGattCallback = object: BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            if(newState == STATE_CONNECTED) {
                mConnectionState = STATE_CONNECTED
                broadcastUpdate(ACTION_GATT_CONNECTED)
                Log.i(TAG, "Connected to GATT server.")
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:" + mBluetoothGatt!!.discoverServices())

            } else if(newState == STATE_DISCONNECTED) {
                mConnectionState = STATE_DISCONNECTED
                Log.i(TAG, "Disconnected from GATT server.")
                broadcastUpdate(ACTION_GATT_DISCONNECTED)
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)

            if(status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED)
            }else {
                Log.w(TAG, "onServicesDiscovered received: $status")
            }

        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, status)
        }

        //讀資料
        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
            super.onCharacteristicChanged(gatt, characteristic)
            val data = characteristic!!.value
            if (data != null && data.isNotEmpty()) {
                val stringBuilder = StringBuilder(data.size)
                for (byteChar in data) {
                    stringBuilder.append(String.format("%02X ", byteChar))
                }
                Log.i(TAG, "onCharacteristicChanged: " + String(data) + "  " + stringBuilder.toString())
                broadcastUpdate(ACTION_SCORE_RETRIEVE, String(data))
            }
        }
    }
}
