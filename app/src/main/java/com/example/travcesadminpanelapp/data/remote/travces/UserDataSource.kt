package com.example.travcesadminpanelapp.data.remote.travces

import com.example.travcesadminpanelapp.data.remote.base.ApiErrorResponse
import com.example.travcesadminpanelapp.data.remote.travces.model.data.LoginData
import com.example.travcesadminpanelapp.data.remote.travces.model.response.GetChildResponse


interface UserDataSource {

    interface LoginCallback {
        fun onLoginResponse(data: LoginData)
        fun onPayloadError(error: ApiErrorResponse)
    }

    interface GetChildrenCallback {
        fun onGetChildrenResponse(data: GetChildResponse)
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