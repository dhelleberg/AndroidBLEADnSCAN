package de.dhelleberg.bleadvertiserscanner

import android.os.ParcelUuid

class Constants {
    companion object {
        @JvmField val MANUFACTURER_ID: Int = 0xabba
        @JvmField val ADV_PERIOD: Long = 15000
        @JvmField val SCAN_PAUSE: Long = 2000
        @JvmField val SCAN_PERIOD: Long =  30000
        @JvmField val PERMISSIONS_REQUEST_LOCATION = 1
        @JvmField val SERVICE_UUID =  ParcelUuid
            .fromString("00002234-0000-1000-8000-00805F9B34FB")
    }
}