package com.qpsoft.deviceinfo.ui.main

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.CacheDiskStaticUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.qpsoft.deviceinfo.common.Keys
import com.qpsoft.deviceinfo.data.model.DeviceInfo
import com.qpsoft.deviceinfo.data.model.ResultRes
import com.qpsoft.deviceinfo.data.network.RetrofitManager
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    var loginOkLiveData = MutableLiveData<Boolean>()
    val deviceInfoLiveData = MutableLiveData<Boolean>()

    fun checkLogin() {
        val token = CacheDiskStaticUtils.getString(Keys.TOKEN)
        loginOkLiveData.value = !TextUtils.isEmpty(token)
    }

    fun submitDeviceInfo(deviceInfo: DeviceInfo) {
        viewModelScope.launch {
            val bodyMap = HashMap<String, String>()
            bodyMap["category"] = deviceInfo.category
            bodyMap["brand"] = deviceInfo.brand
            bodyMap["model"] = deviceInfo.model
            bodyMap["btMac"] = deviceInfo.btMac
            bodyMap["btName"] = deviceInfo.btName
            bodyMap["deviceSn"] = deviceInfo.deviceSn
            bodyMap["networkMac"] = deviceInfo.networkMac
            val result = RetrofitManager.apiService().deviceInfo(bodyMap)
            result.enqueue(object : Callback<ResultRes<DeviceInfo>> {
                override fun onResponse(call: Call<ResultRes<DeviceInfo>>, response: Response<ResultRes<DeviceInfo>>) {
                    if (response.isSuccessful) {
                        val deviceInfoRes = response.body()?.data
                        deviceInfoLiveData.value = deviceInfoRes != null
                    } else {
                        val resultRes = GsonUtils.fromJson<ResultRes<Any>>(response.errorBody()?.charStream()?.readText(), ResultRes::class.java)
                        ToastUtils.showShort(resultRes.message)
                    }
                }

                override fun onFailure(call: Call<ResultRes<DeviceInfo>>, t: Throwable) {
                    ToastUtils.showShort(t.message)
                }

            })
        }
    }

}