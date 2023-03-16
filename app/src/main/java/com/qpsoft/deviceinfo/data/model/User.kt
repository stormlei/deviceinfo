package com.qpsoft.deviceinfo.data.model

import java.io.Serializable

data class User(
    val ID: String,
    val account: String,
    val name: String,
    val gender: String,
    val mobile: String
    ): Serializable