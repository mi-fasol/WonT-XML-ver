package com.example.xml_ver.data.retrofit.user

import com.google.gson.annotations.SerializedName

data class MailModel(
    @SerializedName("body") val content: String,
    @SerializedName("subject") val title: String
)

