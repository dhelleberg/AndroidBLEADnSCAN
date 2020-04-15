package de.dhelleberg.bleadvertiserscanner.ui.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import de.dhelleberg.bleadvertiserscanner.data.BLEDevice
import de.dhelleberg.bleadvertiserscanner.data.BLERepository


class BLEDevicesViewModel(val repo: BLERepository) : ViewModel() {

    fun getDevices(): LiveData<List<BLEDevice>> {
        return repo.getDeviceList()
    }
}