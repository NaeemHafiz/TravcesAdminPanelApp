package com.example.travcesadminpanelapp.data.remote.travces

import com.example.travcesadminpanelapp.data.remote.travces.model.params.LoginParams
import com.example.travcesadminpanelapp.data.remote.travces.model.params.SendCoordinatesParams
import com.example.travcesadminpanelapp.data.remote.travces.model.response.GetDriverResponse
import com.example.travcesadminpanelapp.data.remote.travces.model.response.LoginResponse
import com.example.travcesadminpanelapp.data.remote.travces.model.response.SendCoordinatesResponse
import com.example.travcesadminpanelapp.data.remote.travces.model.response.base.BaseResponse
import io.reactivex.Observable
import retrofit2.http.*

interface ApiService {

    @POST("login")
    fun login(@Body body: LoginParams): Observable<LoginResponse>

    @POST("send_coordinates")
    fun sendCoordinates(@Body body: SendCoordinatesParams): Observable<SendCoordinatesResponse>

    @FormUrlEncoded
    @PUT("is_online")
    fun changeOnlineStatus(@Field("driver_id") driverId: String, @Field("status") status: String): Observable<BaseResponse>

    @GET("driver_list")
    fun getDriver(@Query("driver_type") driver_type: String): Observable<GetDriverResponse>
}
