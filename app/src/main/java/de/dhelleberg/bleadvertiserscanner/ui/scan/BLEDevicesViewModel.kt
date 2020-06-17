package de.dhelleberg.bleadvertiserscanner.ui.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import de.dhelleberg.bleadvertiserscanner.data.BLEDevice
import de.dhelleberg.bleadvertiserscanner.data.BLERepository
import de.dhelleberg.bleadvertiserscanner.data.Token


class BLEDevicesViewModel(val repo: BLERepository) : ViewModel() {

    fun getDevices(): LiveData<List<BLEDevice>> {
        return repo.getDeviceList()
    }

    fun getScanStatus(): LiveData<String> {
        return repo.getScanStatus()
    }

    fun getTokens(): LiveData<List<Token>> {
        return repo.getTokenList()
    }
}