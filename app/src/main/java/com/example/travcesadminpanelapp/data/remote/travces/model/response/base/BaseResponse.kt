package com.example.travcesadminpanelapp.data.remote.travces.model.response.base

import java.io.Serializable

open class BaseResponse: Serializable {

    var status: Int = -1
    var message: String = ""
}