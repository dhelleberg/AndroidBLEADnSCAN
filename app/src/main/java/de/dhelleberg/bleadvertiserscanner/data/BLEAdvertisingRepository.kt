package de.dhelleberg.bleadvertiserscanner.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface BLEAdvertisingRepository {
    fun getAdvertisingStatus(): LiveData<String>
    fun setAdAdvertisingStatus(status: String)
}

class BLEAdvertisingRepositoryImpl : BLEAdvertisingRepository {

    private val scanStatus = MutableLiveData<String>()

    override fun getAdvertisingStatus(): LiveData<String> {
        return scanStatus
    }

    override fun setAdAdvertisingStatus(status: String) {
        scanStatus.value = status
    }

}