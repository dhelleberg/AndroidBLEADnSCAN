package de.dhelleberg.bleadvertiserscanner.ui.scan

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.dhelleberg.bleadvertiserscanner.Constants
import de.dhelleberg.bleadvertiserscanner.R
import de.dhelleberg.bleadvertiserscanner.data.BLEDevice
import java.nio.charset.Charset

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
        val device_add_data = view.findViewById<TextView>(R.id.device_add_data)

        fun bind(device: BLEDevice) {
            rssi_view.text = device.rssi.toString()
            rssi_view.setBackgroundColor(Color.WHITE)
            device_name_view.text = device.name
            device_mac_view.text = device.address
            device_add_data.visibility = View.INVISIBLE
            device.scanRecord?.serviceUuids?.forEach {
                uuid -> if(uuid == Constants.SERVICE_UUID) {
                    rssi_view.setBackgroundColor(Color.RED)
                    device_add_data.visibility = View.VISIBLE
                    device_add_data.text = device.scanRecord.getManufacturerSpecificData(Constants.MANUFACTURER_ID)?.toString(
                        Charsets.UTF_8)
                }
            }
        }
    }

}