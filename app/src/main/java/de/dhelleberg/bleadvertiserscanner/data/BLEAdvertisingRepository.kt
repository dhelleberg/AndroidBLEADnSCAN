package de.dhelleberg.bleadvertiserscanner.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface BLEAdvertisingRepository {
    fun getAdvertisingStatus(): LiveData<String>
    fun getCurrentEID(): LiveData<String>
    fun setAdAdvertisingStatus(status: String)
    fun setCurrentEID(serviceData: String)
}

class BLEAdvertisingRepositoryImpl : BLEAdvertisingRepository {

    private val scanStatus = MutableLiveData<String>()
    private val eID = MutableLiveData<String>()

    override fun getAdvertisingStatus(): LiveData<String> {
        return scanStatus
    }

    override fun setAdAdvertisingStatus(status: String) {
        scanStatus.value = status
    }

    override fun setCurrentEID(serviceData: String) {
        eID.value = serviceData
    }

    override fun getCurrentEID(): LiveData<String> {
        return eID
    }


}