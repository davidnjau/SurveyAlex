package com.dave.survey

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.util.HashMap

class BeaconsAdapter(

        private var beaconDetailsList: List<BeaconData>,
        private val context: Context

) :
    RecyclerView.Adapter<BeaconsAdapter.Pager2ViewHolder>() {

    inner class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val tvControlName : TextView = itemView.findViewById(R.id.tvControlName)
        val tvControlDetails : TextView = itemView.findViewById(R.id.tvControlDetails)


        init {

            itemView.setOnClickListener(this)


        }

        override fun onClick(v: View?) {

            val position = adapterPosition

            val key = beaconDetailsList[position].key

            val sharedPreferenceStorage = SharedPreferenceStorage(
                context, "beacons"
            )
            val hashMap1 = HashMap<String, String>()
            hashMap1["firebase_key"] = key
            sharedPreferenceStorage.saveData(hashMap1, "beacons")

            val intent = Intent(context, EditDetails::class.java)
            context.startActivity(intent)

        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BeaconsAdapter.Pager2ViewHolder {
        return Pager2ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.beacon_data,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BeaconsAdapter.Pager2ViewHolder, position: Int) {

        val controlName = beaconDetailsList[position].beaconName
        val controlDetails = beaconDetailsList[position].beaconDetails

        holder.tvControlName.text = controlName
        holder.tvControlDetails.text = controlDetails


    }

    override fun getItemCount(): Int {
        return beaconDetailsList.size
    }

}