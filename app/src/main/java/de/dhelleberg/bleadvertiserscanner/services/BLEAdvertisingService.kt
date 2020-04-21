package de.dhelleberg.bleadvertiserscanner.services

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.util.Log
import de.dhelleberg.bleadvertiserscanner.Constants
import de.dhelleberg.bleadvertiserscanner.data.BLEAdvertisingRepository
import org.koin.android.ext.android.inject

class BLEAdvertisingService : Service() {

    private val TAG by lazy { BLEAdvertisingService::class.java.simpleName }

    private val bleAdvertisingRepository : BLEAdvertisingRepository by inject()

    private var bluetoothAdapter: BluetoothAdapter? = null
    var advertising: Boolean = false

    private lateinit var handler: Handler
    // Binder given to clients
    private val binder = LocalBinder()
    private var eid_counter = 1234567890000000

    override fun onCreate() {
        super.onCreate()
        handler = Handler()
        bluetoothAdapter =
            (getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter

    }



    /**
     * Starts BLE Advertising.
     */
    fun startAdvertising() {
        //goForeground()
        Log.d(TAG,"Service: Starting Advertising")
        val settings = buildAdvertiseSettings()
        val data = buildAdvertiseData()
        bluetoothAdapter?.bluetoothLeAdvertiser?.startAdvertising(settings, data, advertiseCallback)

    }

    fun stopAdvertising() {
        Log.d(TAG,"Service: stopped Advertising")
        bluetoothAdapter?.bluetoothLeAdvertiser?.stopAdvertising(advertiseCallback)
        bleAdvertisingRepository.setAdAdvertisingStatus("stopped")
        advertising = false
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
        eid_counter = eid_counter.inc()
        /* For example - this will cause advertising to fail (exceeds size limit) */
        val serviceData = eid_counter.toString() //the 16byte identifier
        bleAdvertisingRepository.setCurrentEID( serviceData )
        //val seviceData = "1" //the 16byte identifier
        dataBuilder.addManufacturerData(Constants.MANUFACTURER_ID, serviceData.toByteArray(Charsets.UTF_8))
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
            advertising = false

            val message = when(errorCode) {
                ADVERTISE_FAILED_ALREADY_STARTED -> "error: advertising already started"
                ADVERTISE_FAILED_DATA_TOO_LARGE -> "error: data too large"
                ADVERTISE_FAILED_FEATURE_UNSUPPORTED -> "error: feature unsupported"
                ADVERTISE_FAILED_INTERNAL_ERROR -> "error: internal error"
                ADVERTISE_FAILED_TOO_MANY_ADVERTISERS -> "error: too many advertisers"
                    else -> "error: unknown"
            }
            bleAdvertisingRepository.setAdAdvertisingStatus(message)
        }

        override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
            Log.d(TAG,"onStartSuccess $settingsInEffect")
            advertising = true
            bleAdvertisingRepository.setAdAdvertisingStatus("started advertising")
            handler.postDelayed(
                Runnable {
                    if(advertising) {
                        stopAdvertising()
                        startAdvertising()
                    }
                },
                Constants.ADV_PERIOD
            )
        }
    }

    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): BLEAdvertisingService = this@BLEAdvertisingService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }
}
