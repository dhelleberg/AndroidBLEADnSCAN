package de.dhelleberg.bleadvertiserscanner

import android.app.Application
import de.dhelleberg.bleadvertiserscanner.Modules.bleModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class BLEApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@BLEApplication)
            modules(bleModules)
        }
    }
}