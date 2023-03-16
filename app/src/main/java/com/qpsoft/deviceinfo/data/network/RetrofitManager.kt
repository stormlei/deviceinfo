package com.qpsoft.deviceinfo.data.network

import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.CacheDiskStaticUtils
import com.qpsoft.deviceinfo.BuildConfig
import com.qpsoft.deviceinfo.common.Keys
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitManager {

    fun apiService(): ApiService = retrofit.create(ApiService::class.java)


    init {
        initRetrofit()
    }

    private lateinit var retrofit: Retrofit

    private fun initRetrofit(): Retrofit {
        retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.136:8080")
            .client(okHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }

    private fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor())
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    var token = CacheDiskStaticUtils.getString(Keys.TOKEN)
                    //LogUtils.e("------$token")
                    val request = chain.request()
                        .newBuilder()
                        .addHeader("version", AppUtils.getAppVersionName())
                        .addHeader("versionCode", AppUtils.getAppVersionCode().toString())
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                    val response = chain.proceed(request)
                    return response
                }
            })
            .build()
    }

    private fun loggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
    }




}