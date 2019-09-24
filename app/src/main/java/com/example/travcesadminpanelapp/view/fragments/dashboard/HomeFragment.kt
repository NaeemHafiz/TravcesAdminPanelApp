package com.example.travcesadminpanelapp.view.fragments.dashboard


import android.os.Bundle
import android.view.View
import androidx.navigation.Navigation
import com.example.travcesadminpanelapp.R
import com.example.travcesadminpanelapp.view.fragments.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cvTrack_driver.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_trackDriverFragment))
        cvRent_car.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_rentCarFragment))
        cvApproval.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_approvalFragment))
        assigned_car.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_assignedDriverFragment))
    }

}
