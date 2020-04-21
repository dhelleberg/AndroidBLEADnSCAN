package de.dhelleberg.bleadvertiserscanner.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.koin.android.ext.android.inject

interface BLERepository {

    fun addDeviceToCurrentScanResult(device: BLEDevice)
    fun getDeviceList(): LiveData<List<BLEDevice>>
    fun getScanStatus(): LiveData<String>
    fun updateScanStatus(status: String)
}

class BLERepositoryImpl (private val context : Context) : BLERepository {
    private val TAG by lazy { BLERepositoryImpl::class.java.simpleName }

    private val deviceList = ArrayList<BLEDevice>()
    private val result = MutableLiveData<List<BLEDevice>>()
    private val scanStatus = MutableLiveData<String>()
    init {
        result.value = ArrayList()
    }



    override fun addDeviceToCurrentScanResult(device: BLEDevice) {
        Log.d(TAG, "add device $device")
        if(deviceList.contains(device))
            deviceList.set(deviceList.indexOf(device),device)
        else
            deviceList.add(device)
        result.value = deviceList
    }

    override fun getDeviceList(): LiveData<List<BLEDevice>> {
        return result
    }

    override fun getScanStatus(): LiveData<String> {
        return scanStatus
    }

    override fun updateScanStatus(status: String) {
        scanStatus.value = status
    }



}
