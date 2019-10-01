package com.example.travcesadminpanelapp.viewModel

import android.app.Application
import android.location.Location
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import com.example.travcesadminpanelapp.data.remote.base.ApiErrorResponse
import com.example.travcesadminpanelapp.data.remote.travces.UserDataSource
import com.example.travcesadminpanelapp.data.remote.travces.model.data.LoginData
import com.example.travcesadminpanelapp.data.remote.travces.model.response.GetDriverResponse
import com.example.travcesadminpanelapp.repository.UserRepository
import com.example.travcesadminpanelapp.utils.extensions.ERROR_CODE_EMPTY_PASSWORD
import com.example.travcesadminpanelapp.utils.extensions.ERROR_CODE_EMPTY_PHONE_FIELD
import com.example.travcesadminpanelapp.utils.extensions.OneShotEvent
import com.example.travcesadminpanelapp.view.activities.base.BaseActivity
import com.google.android.gms.location.*

class UserViewModel(context: Application) : BaseAndroidViewModel(context) {

    private val UPDATE_INTERVAL = (10 * 1000).toLong()  /* 10 secs */
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */
    var userRepository: UserRepository = UserRepository(context)
    var locationResponse: MutableLiveData<OneShotEvent<Location>> = MutableLiveData()
    var loginResponse: MutableLiveData<OneShotEvent<LoginData>> = MutableLiveData()
    var updateLocationResponse: MutableLiveData<OneShotEvent<Location>> = MutableLiveData()
    var getDriverResponse: MutableLiveData<OneShotEvent<GetDriverResponse>> = MutableLiveData()


    fun getCurrentLocation(activity: BaseActivity) {
        val mFusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient((activity))
        mFusedLocationClient.lastLocation.addOnSuccessListener((activity)) { location ->
            locationResponse.value = OneShotEvent(location)
        }
    }

    fun login(phone: String, password: String) {
        showProgressBar(true)
        var canProceed = true
        if (phone.isEmpty()) {
            handleErrorType(ERROR_CODE_EMPTY_PHONE_FIELD, "Enter Phone Number")
            canProceed = false
        }
        if (password.isEmpty()) {
            handleErrorType(ERROR_CODE_EMPTY_PASSWORD, "Enter Password")
            canProceed = false
        }
        if (!canProceed) return
        userRepository.login(phone, password, object : UserDataSource.LoginCallback {
            override fun onLoginResponse(data: LoginData) {
                showProgressBar(false)
                loginResponse.value = OneShotEvent(data)
            }

            override fun onPayloadError(error: ApiErrorResponse) {
                showProgressBar(false)
                showSnackbarMessage(error.message)
            }
        })
    }

    fun sendCoordinates(userId: String, latitude: String, longitude: String) {
        showProgressBar(false)
        userRepository.sendCoordinates(
            userId,
            latitude,
            longitude,
            object : UserDataSource.SendCoordinatesCallback {
                override fun onSendCoordinatesResponse(message: String) {

                }

                override fun onPayloadError(error: ApiErrorResponse) {
                    showProgressBar(false)
                    showSnackbarMessage(error.message)
                }
            })
    }

    private var mFusedLocationClient: FusedLocationProviderClient? = null

    fun startLocationUpdates(activity: BaseActivity) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient((activity))
        // Create the location request to start receiving updates
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = UPDATE_INTERVAL
        mLocationRequest.fastestInterval = FASTEST_INTERVAL
        mLocationRequest.smallestDisplacement = 100.00f

        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(activity)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    fun stopLocationUpdates() {
        mFusedLocationClient!!.removeLocationUpdates(mLocationCallback)
    }

    fun getDrivers(driver_type: String) {
        showProgressBar(true)
        userRepository.getDrivers(driver_type,
            object : UserDataSource.GetChildrenCallback {
                override fun onGetChildrenResponse(data: GetDriverResponse) {
                    showProgressBar(false)
                    getDriverResponse.value = OneShotEvent(data)
                }

                override fun onPayloadError(error: ApiErrorResponse) {
                    showProgressBar(false)
                    showSnackbarMessage(error.message)
                }
            })
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            updateLocationResponse.value = OneShotEvent(locationResult.lastLocation)
        }
    }

}