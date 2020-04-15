package de.dhelleberg.bleadvertiserscanner.ui.scan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.dhelleberg.bleadvertiserscanner.R
import de.dhelleberg.bleadvertiserscanner.data.BLEDevice
import kotlinx.android.synthetic.main.ble_device_item.view.*

class BLEDeviceListAdapter(val items: ArrayList<BLEDevice>) :  RecyclerView.Adapter<BLEDeviceListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.ble_device_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(items.get(position))
    }

    fun updateData(devices: List<BLEDevice>) {
        items.clear()
        items.addAll(devices)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val rssi_view = view.findViewById<TextView>(R.id.device_rssi)
        val device_name_view = view.findViewById<TextView>(R.id.device_name)
        val device_mac_view = view.findViewById<TextView>(R.id.device_mac)

        fun bind(device: BLEDevice) {
            rssi_view.text = device.rssi.toString()
            device_name_view.text = device.name
            device_mac_view.text = device.address
        }
    }

}