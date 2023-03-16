package com.qpsoft.deviceinfo

import android.app.Application
import com.blankj.utilcode.util.LogUtils
import com.qpsoft.deviceinfo.util.BleDeviceOpUtil

class DApplication : Application() {

    companion object {

        lateinit var instance: DApplication
            private set

        init {

        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        // utilcode
        LogUtils.getConfig().isLogSwitch = BuildConfig.DEBUG

        //ble
        BleDeviceOpUtil.initBle(this)
    }
}