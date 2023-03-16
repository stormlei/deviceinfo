package com.qpsoft.deviceinfo.util

import android.bluetooth.BluetoothGatt
import android.text.TextUtils
import com.blankj.utilcode.util.ToastUtils
import com.clj.fastble.BleManager
import com.clj.fastble.callback.BleGattCallback
import com.clj.fastble.data.BleDevice
import com.clj.fastble.exception.BleException
import com.qpsoft.deviceinfo.DApplication

object BleDeviceOpUtil {

    fun initBle(app: DApplication) {
        BleManager.getInstance().init(app)
    }
    fun openBle() {
        if (!BleManager.getInstance().isBlueEnable) BleManager.getInstance().enableBluetooth()
    }

    fun disconnectDevice() {
        BleManager.getInstance().disconnectAllDevice()
    }

}