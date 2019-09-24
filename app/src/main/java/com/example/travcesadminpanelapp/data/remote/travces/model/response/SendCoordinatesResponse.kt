package com.example.travcesadminpanelapp.data.remote.travces.model.response

import com.example.travcesadminpanelapp.data.remote.travces.model.response.base.BaseResponse
import java.io.Serializable

class SendCoordinatesResponse(
    val data: Data
) : BaseResponse(), Serializable{

    inner class Data{

    }
}

