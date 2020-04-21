package de.dhelleberg.bleadvertiserscanner.ui.advertising

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import de.dhelleberg.bleadvertiserscanner.R
import de.dhelleberg.bleadvertiserscanner.ui.scan.BLEDevicesViewModel
import kotlinx.android.synthetic.main.advertising_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AdvertisingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdvertisingFragment : Fragment() {

    private val bleAdvertisingViewModel: BLEAdvertisingViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AdvertisingFragment()
    }


}
