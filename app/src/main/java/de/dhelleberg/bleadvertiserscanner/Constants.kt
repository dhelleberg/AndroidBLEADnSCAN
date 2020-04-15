package de.dhelleberg.bleadvertiserscanner

import android.os.ParcelUuid

class Constants {
    companion object {
        @JvmField val SCAN_PERIOD :Long =  5000
        @JvmField val PERMISSIONS_REQUEST_LOCATION = 1
        @JvmField val SERVICE_UUID =  ParcelUuid
            .fromString("0000b81d-0000-1000-8000-00805f9b34fa")
    }
}