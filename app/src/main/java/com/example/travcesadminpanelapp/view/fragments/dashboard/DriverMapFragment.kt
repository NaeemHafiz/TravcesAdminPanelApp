package com.example.travcesadminpanelapp.view.fragments.dashboard


import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.travcesadminpanelapp.R
import com.example.travcesadminpanelapp.data.remote.puhser.EVENT_NAME_LOCATION_UPDATE
import com.example.travcesadminpanelapp.data.remote.puhser.MyPusherManager
import com.example.travcesadminpanelapp.data.remote.puhser.model.PusherEvent
import com.example.travcesadminpanelapp.data.remote.travces.model.data.GetDriverData
import com.example.travcesadminpanelapp.utils.extensions.isLocationEnabled
import com.example.travcesadminpanelapp.utils.rxBus.RxBus
import com.example.travcesadminpanelapp.view.activities.GlobalNavigationActivity
import com.example.travcesadminpanelapp.view.activities.base.BaseActivity
import com.example.travcesadminpanelapp.view.fragments.base.BaseFragment
import com.example.travcesadminpanelapp.viewModel.UserViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

class DriverMapFragment : BaseFragment(), OnMapReadyCallback {

    override fun getLayoutId(): Int = R.layout.fragment_driver_map
    lateinit var mMap: GoogleMap
    lateinit var userViewModel: UserViewModel
    lateinit var driverObj: GetDriverData

    private fun getMyArguments() {
        val args = arguments
        if (args != null) {
            if (args.containsKey(Companion.KEY_DRIVER)) driverObj =
                (args.getSerializable(Companion.KEY_DRIVER)!! as GetDriverData)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.googleMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
        getMyArguments()
        registerWithRXBus()
        attachViewModel()

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (isLocationEnabled((activity as BaseActivity))) {
            userViewModel.getCurrentLocation(activity as BaseActivity)
        }

    }

    private fun setupMap(location: Location) {
        mMap.isMyLocationEnabled = true
        val currentLatLng = LatLng(location.latitude, location.longitude)
        zoomWithAnimateCamera(currentLatLng)
    }

    private fun zoomWithAnimateCamera(location: LatLng) {
        val cameraPosition: CameraPosition = CameraPosition.Builder()
            .target(location)
            .zoom(17f).build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    }

    private fun attachViewModel() {
        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        with(userViewModel) {
            snackbarMessage.observe(viewLifecycleOwner, Observer {
                val msg = it?.getContentIfNotHandled()
                if (!msg.isNullOrEmpty()) showErrorToast(msg)
            })
            progressBar.observe(viewLifecycleOwner, Observer {
                val show = it?.getContentIfNotHandled()
                if (show != null)
                    showProgressDialog(show)
            })

            locationResponse.observe(viewLifecycleOwner, Observer {
                val show = it?.getContentIfNotHandled()
                if (show != null) setupMap(show)
            })
        }
    }


    @SuppressLint("CheckResult", "LogNotTimber")
    private fun registerWithRXBus() {
        RxBus.defaultInstance().toObservable()
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it is PusherEvent) {
                    when (it.eventName) {
                        EVENT_NAME_LOCATION_UPDATE -> {
                            simulateMap(it.data)
                            Log.d("EVENT_TRIGGER", it.data)
                        }
                    }
                }
            }, Throwable::printStackTrace)
    }

    private fun simulateMap(data: String) {
        val jsonObject = JSONObject(data)
        val lat: Double = jsonObject.getString("latitude").toDouble()
        val lon: Double = jsonObject.getString("longitude").toDouble()
        Log.d("EVENT_TRIGGER", "Latitude:" + lat)
        Log.d("EVENT_TRIGGER", "Longitude:" + lon)
        (activity as GlobalNavigationActivity).runOnUiThread {
            val driverPos = LatLng(lat, lon)
            val marker = mMap.addMarker(
                MarkerOptions().position(driverPos)
                    .title("Driver")
                    .icon(
                        BitmapDescriptorFactory
                            .fromResource(R.drawable.taxi)
                    )
            )
            zoomWithAnimateCamera(driverPos)
        }
    }


    object Companion {
        @JvmStatic
        val TAG: String = DriverMapFragment::class.java.simpleName
        @JvmStatic
        val KEY_DRIVER = "driver"
    }


    override fun onResume() {
        super.onResume()
        MyPusherManager.instance.subscribeToPrivateLocationUpdateChannelPusher(
            driverObj.id
        )
    }

    override fun onPause() {
        super.onPause()
        MyPusherManager.instance.unsubscribeToPrivateLocationUpdateChannelPusher()
    }
}
