package com.qpsoft.deviceinfo.data.model

data class DeviceInfo(
    val category: String,
    val brand: String,
    val model: String,
    val btMac: String,
    val btName: String,
    //视力表
    val deviceSn: String,
    val networkMac: String,
)