package com.example.travcesadminpanelapp.view.fragments.dashboard


import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travcesadminpanelapp.R
import com.example.travcesadminpanelapp.data.remote.travces.model.data.GetDriverData
import com.example.travcesadminpanelapp.view.activities.base.BaseActivity
import com.example.travcesadminpanelapp.view.adapters.DriverAdapter
import com.example.travcesadminpanelapp.view.fragments.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_track_driver.*

class TrackDriverFragment : BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_track_driver
    lateinit var driveradapter: DriverAdapter
    var childrenList = ArrayList<GetDriverData>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDriverAdapter()
    }

    private fun initDriverAdapter() {
        driveradapter = DriverAdapter(context as BaseActivity, childrenList, this)
        rvTrackDrivers.layoutManager = LinearLayoutManager(context)
        rvTrackDrivers.adapter = driveradapter
    }


}
