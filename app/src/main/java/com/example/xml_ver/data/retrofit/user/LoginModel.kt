package com.example.xml_ver.data.retrofit.user

import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("studentId") val studentId: String,
    @SerializedName("pwd") val password: String
)
