package de.dhelleberg.bleadvertiserscanner

import de.dhelleberg.bleadvertiserscanner.data.BLERepository
import de.dhelleberg.bleadvertiserscanner.data.BLERepositoryImpl
import de.dhelleberg.bleadvertiserscanner.ui.main.PageViewModel
import de.dhelleberg.bleadvertiserscanner.ui.scan.BLEDevicesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.compat.ViewModelCompat.viewModel
import org.koin.android.viewmodel.dsl.viewModel

import org.koin.dsl.module


object Modules {
    val bleModules = module {
        single<BLERepository> { BLERepositoryImpl(androidContext()) }
        viewModel {PageViewModel(get())}
        viewModel {BLEDevicesViewModel(get())}
    }
}