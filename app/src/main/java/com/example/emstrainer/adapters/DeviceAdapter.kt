package com.example.emstrainer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.emstrainer.R
import com.example.emstrainer.model.devices.Devices

class DeviceAdapter() : RecyclerView.Adapter<DeviceAdapter.MyViewHolder>() {

    lateinit var deviceList: List<Devices>

    fun setContentList(list: List<Devices>) {
        this.deviceList = list
        notifyDataSetChanged()
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nameDeviceItem = itemView.findViewById<TextView>(R.id.nameDeviceItem)!!
        var addressDeviceItem = itemView.findViewById<TextView>(R.id.addressDeviceItem)!!
        val line = itemView.findViewById<View>(R.id.lineInFitDevice)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_device_card, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = deviceList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.nameDeviceItem.text = deviceList[position].name
        holder.addressDeviceItem.text = deviceList[position].numberDevice

        if(position == 0) holder.line.visibility = View.INVISIBLE
    }
}