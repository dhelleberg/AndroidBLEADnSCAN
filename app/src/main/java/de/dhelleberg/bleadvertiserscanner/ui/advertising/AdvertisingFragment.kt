package de.dhelleberg.bleadvertiserscanner.ui.advertising

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import de.dhelleberg.bleadvertiserscanner.R
import de.dhelleberg.bleadvertiserscanner.services.BLEAdvertisingService
import de.dhelleberg.bleadvertiserscanner.services.BLEScannerService
import de.dhelleberg.bleadvertiserscanner.ui.scan.BLEDevicesViewModel
import kotlinx.android.synthetic.main.advertising_fragment.*
import kotlinx.android.synthetic.main.scan_result_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 * Use the [AdvertisingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdvertisingFragment : Fragment() {

    private var bound = false
    private val bleAdvertisingViewModel: BLEAdvertisingViewModel by viewModel()

    private lateinit var bleAdvertisingService: BLEAdvertisingService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.advertising_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        bleAdvertisingViewModel.getStatus().observe(this, Observer { adv_status.text = it })
        bleAdvertisingViewModel.getEID().observe(this, Observer { t_current_eid.text = it })

        // Bind to LocalService
        Intent(activity, BLEAdvertisingService::class.java).also { intent ->
            activity?.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
        adv_switch.setOnCheckedChangeListener { _, state ->
            when(state) {
                true -> bleAdvertisingService.startAdvertising()
                false-> bleAdvertisingService.stopAdvertising()
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AdvertisingFragment()
    }

    /** Defines callbacks for service binding, passed to bindService()  */
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as BLEAdvertisingService.LocalBinder
            bleAdvertisingService = binder.getService()
            adv_switch.isChecked = bleAdvertisingService.advertising
            bound = true
        }

        override fun onServiceDisconnected(className: ComponentName) {
            bound = false
        }
    }




}
