package com.example.travcesadminpanelapp.view.fragments.dashboard


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.travcesadminpanelapp.R
import com.example.travcesadminpanelapp.view.fragments.base.BaseFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * A simple [Fragment] subclass.
 */
class DriverMapFragment : BaseFragment(), OnMapReadyCallback {

    override fun getLayoutId(): Int = R.layout.fragment_driver_map
    lateinit var mMap: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.googleMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        zoomWithAnimateCamera(sydney)

    }

    private fun zoomWithAnimateCamera(location: LatLng) {
        val cameraPosition: CameraPosition = CameraPosition.Builder()
            .target(location)
            .zoom(17f).build()
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    }
}
