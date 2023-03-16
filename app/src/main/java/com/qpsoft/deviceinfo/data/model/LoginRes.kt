package com.qpsoft.deviceinfo.data.model


data class LoginRes(val user: User, val token: String, val refreshToken: String)