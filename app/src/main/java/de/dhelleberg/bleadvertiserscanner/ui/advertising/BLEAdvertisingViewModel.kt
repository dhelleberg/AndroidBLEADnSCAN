package de.dhelleberg.bleadvertiserscanner.ui.advertising

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import de.dhelleberg.bleadvertiserscanner.data.BLEAdvertisingRepository

class BLEAdvertisingViewModel(val repo: BLEAdvertisingRepository): ViewModel()  {

    fun getStatus(): LiveData<String> {
        return repo.getAdvertisingStatus()
    }

}
