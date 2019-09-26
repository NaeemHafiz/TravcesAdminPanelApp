package com.example.travcesadminpanelapp.view.fragments.dashboard


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travcesadminpanelapp.R
import com.example.travcesadminpanelapp.data.remote.travces.model.data.GetDriverData
import com.example.travcesadminpanelapp.view.activities.base.BaseActivity
import com.example.travcesadminpanelapp.view.adapters.DriverAdapter
import com.example.travcesadminpanelapp.view.fragments.base.BaseFragment
import com.example.travcesadminpanelapp.viewModel.UserViewModel
import kotlinx.android.synthetic.main.fragment_rent_car.*

/**
 * A simple [Fragment] subclass.
 */
class RentCarFragment : BaseFragment(), DriverAdapter.Callback {


    override fun getLayoutId(): Int = R.layout.fragment_rent_car
    lateinit var driveradapter: DriverAdapter
    var driverList = ArrayList<GetDriverData>()
    lateinit var userViewModel: UserViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachViewModel()
        initDriverAdapter()
        var driver_type = 0
        userViewModel.getDrivers(driver_type.toString())

    }

    private fun initDriverAdapter() {
        driveradapter = DriverAdapter(context as BaseActivity, driverList, this)
        rvDriverList.layoutManager = LinearLayoutManager(context)
        rvDriverList.adapter = driveradapter
    }
    private fun attachViewModel() {
        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        with(userViewModel) {
            snackbarMessage.observe(viewLifecycleOwner, Observer {
                val msg = it?.getContentIfNotHandled()
                if (!msg.isNullOrEmpty()) showToast(msg)
            })
            progressBar.observe(viewLifecycleOwner, Observer {
                val show = it?.getContentIfNotHandled()
                if (show != null)
                    showProgressDialog(show)
            })
            getDriverResponse.observe(viewLifecycleOwner, Observer {
                val show = it?.getContentIfNotHandled()
                if (show?.data != null) {
                    if (show.data.isNotEmpty()) {
                        rvDriverList.visibility = View.VISIBLE
                        tvDatanotFound.visibility = View.GONE
                    } else {
                        rvDriverList.visibility = View.GONE
                        tvDatanotFound.visibility = View.VISIBLE
                    }
                    driverList.clear()
                    for (temp in show.data.iterator()) {
                        driverList.add(temp)
                    }
                    driveradapter.notifyDataSetChanged()
                }

            })
        }
    }


    override fun onItemClicked(pos: Int) {

    }

    override fun onDeleteClicked(pos: Int) {

    }

    override fun oncvItemClicked(pos: Int) {

    }

}
