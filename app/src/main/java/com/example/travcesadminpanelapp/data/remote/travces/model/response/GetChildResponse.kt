package com.example.travcesadminpanelapp.data.remote.travces.model.response

import com.example.travcesadminpanelapp.data.remote.travces.model.data.GetChildrenData
import com.example.travcesadminpanelapp.data.remote.travces.model.response.base.BaseResponse
import java.io.Serializable

class GetChildResponse(
    val data:List<List<GetChildrenData>>
) : BaseResponse(), Serializable