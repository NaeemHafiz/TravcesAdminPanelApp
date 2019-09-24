package com.example.travcesadminpanelapp.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.travcesadminpanelapp.data.remote.base.ApiErrorResponse
import com.example.travcesadminpanelapp.data.remote.travces.UserDataSource
import com.example.travcesadminpanelapp.repository.UserRepository
import com.example.travcesadminpanelapp.utils.extensions.OneShotEvent

class HomeViewModel(context: Application) : BaseAndroidViewModel(context) {

    var userRepository: UserRepository = UserRepository(
        context
    )
    var updateOnlineStatusResponse: MutableLiveData<OneShotEvent<String>> = MutableLiveData()
    var getChildrenResponse: MutableLiveData<OneShotEvent<String>> = MutableLiveData()


    fun changeOnlineStatus(userId: String, status: String) {
        showProgressBar(true)
        userRepository.changeOnlineStatus(
            userId,
            status,
            object : UserDataSource.OnlineStatusChangedCallback {
                override fun onOnlineStatusChangedResponse(message: String) {
                    showProgressBar(false)
                    updateOnlineStatusResponse.value = OneShotEvent(message)
                }

                override fun onPayloadError(error: ApiErrorResponse) {
                    showProgressBar(false)
                    showSnackbarMessage(error.message)
                }
            })
    }




}


