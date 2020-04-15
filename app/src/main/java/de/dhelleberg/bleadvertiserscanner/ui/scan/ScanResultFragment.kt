package de.dhelleberg.bleadvertiserscanner.ui.scan

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import de.dhelleberg.bleadvertiserscanner.R
import de.dhelleberg.bleadvertiserscanner.data.BLEDevice
import de.dhelleberg.bleadvertiserscanner.ui.main.PageViewModel
import kotlinx.android.synthetic.main.scan_result_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

class ScanResultFragment : Fragment() {
    private val TAG by lazy { ScanResultFragment::class.java.simpleName }
    companion object {
        fun newInstance() = ScanResultFragment()
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
        device_list.adapter = bleAddapter
    }
    private fun showDevices(devices: List<BLEDevice>) {
        devices.forEach { device ->
            Log.d(TAG, "device: $device")
        }
        bleAddapter.updateData(devices)
    }

}
