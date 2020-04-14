package de.dhelleberg.bleadvertiserscanner

import de.dhelleberg.bleadvertiserscanner.data.BLERepository
import de.dhelleberg.bleadvertiserscanner.data.BLERepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object Modules {
    val bleModules = module {
        single<BLERepository> { BLERepositoryImpl(androidContext()) }
    }
}