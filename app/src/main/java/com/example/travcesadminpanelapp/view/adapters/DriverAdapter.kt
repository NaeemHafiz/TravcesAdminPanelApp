package com.example.travcesadminpanelapp.view.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.travcesadminpanelapp.R
import com.example.travcesadminpanelapp.data.remote.travces.model.data.GetDriverData
import com.example.travcesadminpanelapp.view.fragments.dashboard.TrackDriverFragment


class DriverAdapter(
    var context: Context,
    var driverList: List<GetDriverData>,
    var callback: Callback
) :
    RecyclerView.Adapter<DriverAdapter.ViewHolder>() {

    override fun onCreateViewHolder(container: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.row_item_drivers_list,
                container,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) = holder.bind(i)

    override fun getItemCount(): Int = driverList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvDriverName = itemView.findViewById(R.id.tvDriverName) as TextView
        var tvAddress = itemView.findViewById(R.id.tvAddress) as TextView
        var tvPhone = itemView.findViewById(R.id.tvPhone) as TextView
        var tvStatus = itemView.findViewById(R.id.tvStatus) as TextView
        var cvItem = itemView.findViewById(R.id.cvitemClick) as CardView


        @SuppressLint("SetTextI18n")
        fun bind(pos: Int) {
            tvDriverName.text = "${driverList[pos].fname}${driverList[pos].lname}"
            tvAddress.text = driverList[pos].address
            tvPhone.text = driverList[pos].phone
            tvStatus.text = "Pending"
            initClickListeners()
        }


        private fun initClickListeners() {
            cvItem.setOnClickListener {
                callback.onItemClicked(adapterPosition)

            }
        }

    }

    interface Callback {
        fun onItemClicked(pos: Int)
        fun onDeleteClicked(pos: Int)
        fun oncvItemClicked(pos: Int)
    }
}