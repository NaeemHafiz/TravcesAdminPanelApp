package com.example.travcesadminpanelapp.data.remote.travces.model.response

import com.example.travcesadminpanelapp.data.remote.travces.model.data.GetDriverData
import com.example.travcesadminpanelapp.data.remote.travces.model.response.base.BaseResponse
import java.io.Serializable

class GetDriverResponse(
    val data:List<GetDriverData>
) : BaseResponse(), Serializable