package com.qpsoft.deviceinfo.data.model

data class ResultRes<T>(
    val code: Int,
    val message: String,
    val data: T,
)