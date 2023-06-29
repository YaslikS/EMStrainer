package com.example.emstrainer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.emstrainer.R
import com.example.emstrainer.model.profilesFullSuit.ProfileFullSuit

class ProfileAdapter() : RecyclerView.Adapter<ProfileAdapter.MyViewHolder>() {

    lateinit var profileList: List<ProfileFullSuit>

    fun setContentList(list: List<ProfileFullSuit>) {
        this.profileList = list
        notifyDataSetChanged()
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name = itemView.findViewById<TextView>(R.id.item_profile_text)!!
        var power1 = itemView.findViewById<TextView>(R.id.power1Value)!!
        var power2 = itemView.findViewById<TextView>(R.id.power2Value)!!
        var power3 = itemView.findViewById<TextView>(R.id.power3Value)!!
        var power4 = itemView.findViewById<TextView>(R.id.power4Value)!!
        var power5 = itemView.findViewById<TextView>(R.id.power5Value)!!
        var power6 = itemView.findViewById<TextView>(R.id.power6Value)!!
        var power7 = itemView.findViewById<TextView>(R.id.power7Value)!!
        var power8 = itemView.findViewById<TextView>(R.id.power8Value)!!
        var power9 = itemView.findViewById<TextView>(R.id.power9Value)!!
        var power10 = itemView.findViewById<TextView>(R.id.power10Value)!!
        var power11 = itemView.findViewById<TextView>(R.id.power11Value)!!
        var power12 = itemView.findViewById<TextView>(R.id.power12Value)!!
        var modulationFm = itemView.findViewById<TextView>(R.id.modulationFmValue)!!
        var modulationAm = itemView.findViewById<TextView>(R.id.modulationAmValue)!!
        var radioId = itemView.findViewById<TextView>(R.id.frequencyValue)!!
        var line = itemView.findViewById<View>(R.id.lineInProfile)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_profile_card, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = profileList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = profileList[position].name
        holder.power1.text = profileList[position].power1.toString()
        holder.power2.text = profileList[position].power2.toString()
        holder.power3.text = profileList[position].power3.toString()
        holder.power4.text = profileList[position].power4.toString()
        holder.power5.text = profileList[position].power5.toString()
        holder.power6.text = profileList[position].power6.toString()
        holder.power7.text = profileList[position].power7.toString()
        holder.power8.text = profileList[position].power8.toString()
        holder.power9.text = profileList[position].power9.toString()
        holder.power10.text = profileList[position].power10.toString()
        holder.power11.text = profileList[position].power11.toString()
        holder.power12.text = profileList[position].power12.toString()

        if(position == 0) holder.line.visibility = View.INVISIBLE

        if(profileList[position].modulationAm) holder.modulationAm.text = "ВКЛ"
        else holder.modulationAm.text = "ВЫКЛ"

        if(profileList[position].modulationFm) holder.modulationFm.text = "ВКЛ"
        else holder.modulationFm.text = "ВЫКЛ"

        when(profileList[position].radioId) {
            R.id.radioFreq16 -> holder.radioId.text = "16гц"
            R.id.radioFreq32 -> holder.radioId.text = "32гц"
            R.id.radioFreq64 -> holder.radioId.text = "64гц"
            R.id.radioFreq128 -> holder.radioId.text = "128гц"
        }
    }
}