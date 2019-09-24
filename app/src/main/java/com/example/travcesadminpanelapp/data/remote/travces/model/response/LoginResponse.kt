package com.example.travcesadminpanelapp.data.remote.travces.model.response

import com.example.travcesadminpanelapp.data.remote.travces.model.data.LoginData
import com.example.travcesadminpanelapp.data.remote.travces.model.response.base.BaseResponse
import java.io.Serializable

class LoginResponse(
    val data: LoginData
) : BaseResponse(), Serializable