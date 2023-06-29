package com.example.emstrainer.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.emstrainer.R
import com.example.emstrainer.model.profilesFullSuit.ProfileFullSuit

@SuppressLint("NotifyDataSetChanged")
class FitProfileAdapter() : RecyclerView.Adapter<FitProfileAdapter.MyViewHolder>() {
    lateinit var profileList: List<ProfileFullSuit>

    fun setContentList(list: List<ProfileFullSuit>) {
        this.profileList = list
        notifyDataSetChanged()
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name = itemView.findViewById<TextView>(R.id.item_fit_profile_text)!!
        var line = itemView.findViewById<View>(R.id.lineInFitProfile)!!
        var count = itemView.findViewById<TextView>(R.id.TVCount)!!
        var type = itemView.findViewById<TextView>(R.id.TVtype)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fit_profile_card, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = profileList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = profileList[position].name
        holder.count.text = profileList[position].times.toString()
        if(!profileList[position].typeTimes)
            holder.type.text = "секунд"
        else
            holder.type.text = "раз"
        if(position == 0) holder.line.visibility = View.INVISIBLE
    }
}