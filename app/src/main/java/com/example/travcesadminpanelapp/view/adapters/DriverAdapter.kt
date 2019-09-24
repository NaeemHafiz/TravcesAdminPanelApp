package com.example.travcesadminpanelapp.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.travcesadminpanelapp.R
import com.example.travcesadminpanelapp.data.remote.travces.model.data.GetDriverData
import com.example.travcesadminpanelapp.view.fragments.dashboard.TrackDriverFragment


class DriverAdapter(
    var context: Context,
    var childrenList: List<GetDriverData>,
    var callback: TrackDriverFragment
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

    override fun getItemCount(): Int = 6

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //        var tvchildname = itemView.findViewById(R.id.tvchildname) as TextView
//        var tvpicklocation = itemView.findViewById(R.id.tvpicklocation) as TextView
//        var tvdroplocation = itemView.findViewById(R.id.tvdroplocation) as TextView
//        var btnpick = itemView.findViewById(R.id.btnpick) as Button
//        var btndrop = itemView.findViewById(R.id.btndrop) as Button
//        var cvItem = itemView.findViewById(R.id.cvitemClick) as CardView
//
//        @SuppressLint("SetTextI18n")
        fun bind(pos: Int) {
//            tvchildname.text = "${childrenList[pos].fname} ${childrenList[pos].lname}"
//            tvpicklocation.text = childrenList[pos].pickup_location
//            tvdroplocation.text = childrenList[pos].drop_location
        }

    }

    interface Callback {
        fun onItemClicked(pos: Int)
        fun onDeleteClicked(pos: Int)
        fun oncvItemClicked(pos: Int)
    }
}