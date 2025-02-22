package com.example.xml_ver.data.retrofit.user

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("nickname") val nickname: String,
    @SerializedName("studentId") val studentId: Int,
    @SerializedName("major") val major: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("user_image") val userImage: Int
)

