package de.dhelleberg.bleadvertiserscanner.services

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.util.Log
import de.dhelleberg.bleadvertiserscanner.Constants
import de.dhelleberg.bleadvertiserscanner.data.BLEDevice
import de.dhelleberg.bleadvertiserscanner.data.BLERepository
import org.koin.android.ext.android.inject

class BLEScannerService : Service() {
    private val TAG by lazy { BLEScannerService::class.java.simpleName }

    private var scanning: Boolean = false
    private var bluetoothAdapter: BluetoothAdapter? = null
    private val bleRepository : BLERepository by inject()
    private lateinit var handler: Handler
    // Binder given to clients
    private val binder = LocalBinder()

    var scanEnabled : Boolean = true

    override fun onCreate() {
        super.onCreate()
        bluetoothAdapter =
            (getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
        handler = Handler()
        startScanning()
    }



    fun startScanning() {
        if(!scanning && scanEnabled) {
            scanning = true
            bleRepository.updateScanStatus("scanning...")
            bluetoothAdapter?.bluetoothLeScanner?.startScan(
                buildScanFilters(),
                buildScanSettings(),
                bleScanner
            )
            handler.postDelayed(
                Runnable { stopScanning() },
                Constants.SCAN_PERIOD
            )
        }
    }

    private fun stopScanning() {
        scanning = false
        bleRepository.updateScanStatus("paused")
        Log.d(TAG, "Scan stopped")
        // Stop the scan, wipe the callback.
        bluetoothAdapter?.bluetoothLeScanner?.stopScan(bleScanner)
        // wait until re-scanning

        handler.postDelayed(
            Runnable { startScanning() },
            Constants.SCAN_PAUSE
        )
    }
    /**
     * Return a List of [ScanFilter] objects to filter by Service UUID.
     */
    private fun buildScanFilters(): List<ScanFilter> {
        val builder = ScanFilter.Builder()//.setServiceUuid(Constants.SERVICE_UUID)
        return arrayListOf(builder.build())
    }

    /**
     * Return a [ScanSettings] object set to use low power (to preserve battery life).
     */
    private fun buildScanSettings(): ScanSettings {
        val builder = ScanSettings.Builder()
        builder.setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
        return builder.build()
    }


    private val bleScanner = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            if (result != null && result.device != null) {
                bleRepository.addDeviceToCurrentScanResult(BLEDevice(result.device.name?:"unknown",
                    result.timestampNanos,
                    result.rssi,
                    result.device.address))
                Log.d(TAG, "scan result: "+ (result.device))
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
            results?.forEach { result ->
                if( result != null && result.device != null) {
                    bleRepository.addDeviceToCurrentScanResult(BLEDevice(result.device.name?:"unknown",
                        result.timestampNanos,
                        result.rssi,
                        result.device.address))
                    Log.d(TAG, "scan result: "+ (result.device))
                }
            }
        }
    }

    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): BLEScannerService = this@BLEScannerService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }
}