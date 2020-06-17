package de.dhelleberg.bleadvertiserscanner.ui.scan

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import de.dhelleberg.bleadvertiserscanner.R
import de.dhelleberg.bleadvertiserscanner.data.BLEDevice
import de.dhelleberg.bleadvertiserscanner.services.BLEScannerService
import kotlinx.android.synthetic.main.scan_result_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

class ScanResultFragment : Fragment() {
    private val TAG by lazy { ScanResultFragment::class.java.simpleName }
    companion object {
        fun newInstance() = ScanResultFragment()
    }


    private lateinit var bleScannerService: BLEScannerService
    private var bound: Boolean = false

    /** Defines callbacks for service binding, passed to bindService()  */
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as BLEScannerService.LocalBinder
            bleScannerService = binder.getService()
            scan_switch.isChecked = bleScannerService.scanEnabled
            bound = true
        }

        override fun onServiceDisconnected(className: ComponentName) {
            bound = false
        }
    }

    private val bleDevicesViewModel: BLEDevicesViewModel by viewModel()

    private val bleAddapter = BLEDeviceListAdapter(ArrayList())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.scan_result_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        device_list.layoutManager = LinearLayoutManager(context)
    }


    override fun onStart() {
        super.onStart()
        bleDevicesViewModel.getDevices().observe(this, Observer { showDevices(it) })
        bleDevicesViewModel.getScanStatus().observe(this, Observer { tv_scanStatus.text = it })
        bleDevicesViewModel.getTokens().observe(this, Observer { tv_nrTokens.text = it.size.toString() })
        device_list.adapter = bleAddapter
        // Bind to LocalService
        Intent(activity, BLEScannerService::class.java).also { intent ->
            activity?.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

        scan_switch.setOnCheckedChangeListener { _, state ->
            bleScannerService.scanEnabled = state
            bleScannerService.startScanning()
        }
    }

    override fun onStop() {
        super.onStop()
        activity?.unbindService(connection)
        bound = false
    }
    private fun showDevices(devices: List<BLEDevice>) {
        devices.forEach { device ->
            Log.d(TAG, "device: $device")
        }
        bleAddapter.updateData(devices)
    }

}
