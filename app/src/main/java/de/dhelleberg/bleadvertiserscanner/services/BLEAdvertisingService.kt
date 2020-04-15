package de.dhelleberg.bleadvertiserscanner.services

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import de.dhelleberg.bleadvertiserscanner.Constants

class BLEAdvertisingService : Service() {

    private val TAG by lazy { BLEAdvertisingService::class.java.simpleName }


    private var bluetoothAdapter: BluetoothAdapter? = null


    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        bluetoothAdapter =
            (getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
        startAdvertising()
    }



    /**
     * Starts BLE Advertising.
     */
    private fun startAdvertising() {
        //goForeground()
        Log.d(TAG,"Service: Starting Advertising")
        val settings = buildAdvertiseSettings()
        val data = buildAdvertiseData()
        bluetoothAdapter?.bluetoothLeAdvertiser?.startAdvertising(settings, data, advertiseCallback)

    }
    private fun buildAdvertiseData(): AdvertiseData? {
        /**
         * Note: There is a strict limit of 31 Bytes on packets sent over BLE Advertisements.
         * This includes everything put into AdvertiseData including UUIDs, device info, &
         * arbitrary service or manufacturer data.
         * Attempting to send packets over this limit will result in a failure with error code
         * AdvertiseCallback.ADVERTISE_FAILED_DATA_TOO_LARGE. Catch this error in the
         * onStartFailure() method of an AdvertiseCallback implementation.
         */
        val dataBuilder = AdvertiseData.Builder()
        dataBuilder.addServiceUuid(Constants.SERVICE_UUID)
        dataBuilder.setIncludeTxPowerLevel(false)
        dataBuilder.setIncludeDeviceName(false)

        /* For example - this will cause advertising to fail (exceeds size limit) */
        val seviceData = "1234567890123456" //the 16byte identifier
        //val seviceData = "1" //the 16byte identifier
        dataBuilder.addServiceData(Constants.SERVICE_UUID, seviceData.toByteArray())
        return dataBuilder.build()
    }

    /**
     * Returns an AdvertiseSettings object set to use low power (to help preserve battery life)
     * and disable the built-in timeout since this code uses its own timeout runnable.
     */
    private fun buildAdvertiseSettings(): AdvertiseSettings? {
        val settingsBuilder = AdvertiseSettings.Builder()
        settingsBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_POWER)
        settingsBuilder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_LOW)
        settingsBuilder.setConnectable(false)
        settingsBuilder.setTimeout(0)
        return settingsBuilder.build()
    }

    private val advertiseCallback = object: AdvertiseCallback() {
        override fun onStartFailure(errorCode: Int) {
            Log.d(TAG,"onStartFailure $errorCode")
        }

        override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
            Log.d(TAG,"onStartSuccess $settingsInEffect")

        }

    }
}
