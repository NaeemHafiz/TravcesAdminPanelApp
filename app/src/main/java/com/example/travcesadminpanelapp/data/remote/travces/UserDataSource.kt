package com.example.travcesadminpanelapp.data.remote.travces

import com.example.travcesadminpanelapp.data.remote.base.ApiErrorResponse
import com.example.travcesadminpanelapp.data.remote.travces.model.data.LoginData
import com.example.travcesadminpanelapp.data.remote.travces.model.response.GetDriverResponse


interface UserDataSource {

    interface LoginCallback {
        fun onLoginResponse(data: LoginData)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface GetChildrenCallback {
        fun onGetChildrenResponse(data: GetDriverResponse)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface SendCoordinatesCallback {
        fun onSendCoordinatesResponse(message: String)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface OnlineStatusChangedCallback {
        fun onOnlineStatusChangedResponse(message: String)
        fun onPayloadError(error: ApiErrorResponse)
    }
}