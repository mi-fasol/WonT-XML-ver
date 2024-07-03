package com.example.xml_ver.data.retrofit.post

import com.google.gson.annotations.SerializedName

data class NoticeModel(
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("type") val category: String,
    @SerializedName("visible") val visibility: Boolean,
    @SerializedName("date") val date: String,
)
