package de.dhelleberg.bleadvertiserscanner.data

import android.bluetooth.le.ScanRecord

data class BLEDevice(
    val name: String,
    val discovered: Long,
    val rssi: Int,
    val address: String,
    val scanRecord: ScanRecord?
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as BLEDevice
        return address == other.address
    }
    override fun hashCode(): Int {
        return address.hashCode()
    }
}