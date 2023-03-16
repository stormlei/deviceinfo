package com.qpsoft.deviceinfo.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.*
import com.qpsoft.deviceinfo.common.Keys
import com.qpsoft.deviceinfo.data.model.LoginRes
import com.qpsoft.deviceinfo.data.model.ResultRes
import com.qpsoft.deviceinfo.data.network.RetrofitManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    val loginOkLiveData = MutableLiveData<Any>()

    fun login(account: String, password: String) {
        val bodyMap = HashMap<String, String>()
        bodyMap["account"] = account
        bodyMap["password"] = password
        bodyMap["device"] = DeviceUtils.getModel()
        bodyMap["client"] = "apad"
        val result = RetrofitManager.apiService().login(bodyMap)
        result.enqueue(object : Callback<ResultRes<LoginRes>> {
            override fun onResponse(call: Call<ResultRes<LoginRes>>, response: Response<ResultRes<LoginRes>>) {
                if (response.isSuccessful) {
                    LogUtils.e("-----"+response.body()?.data)
                    val loginRes = response.body()?.data
                    CacheDiskStaticUtils.put(Keys.TOKEN, loginRes?.token)
                    //CacheDiskStaticUtils.put(Keys.REFRESH_TOKEN, loginRes?.refreshToken)
                    //CacheDiskStaticUtils.put(Keys.USER, loginRes?.user)
                    loginOkLiveData.value = loginRes != null
                } else {
                    val resultRes = GsonUtils.fromJson<ResultRes<Any>>(response.errorBody()?.charStream()?.readText(), ResultRes::class.java)
                    ToastUtils.showShort(resultRes.message)
                }
            }

            override fun onFailure(call: Call<ResultRes<LoginRes>>, t: Throwable) {
                ToastUtils.showShort(t.message)
            }

        })

    }

}