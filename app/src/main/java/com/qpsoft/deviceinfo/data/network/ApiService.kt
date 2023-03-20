package com.qpsoft.deviceinfo.data.network

import com.qpsoft.deviceinfo.data.model.DeviceInfo
import com.qpsoft.deviceinfo.data.model.LoginRes
import com.qpsoft.deviceinfo.data.model.ResultRes
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("public/user/v1/login")
    fun login(@Body bodyMap: MutableMap<String, String>): Call<ResultRes<LoginRes>>

    @POST("info-device/v1/info-device")
    fun deviceInfo(@Body bodyMap: MutableMap<String, String>): Call<ResultRes<DeviceInfo>>
}