package com.example.xml_ver.data.retrofit.post

import com.google.gson.annotations.SerializedName

data class NoticeResponseModel(
    @SerializedName("nid") val nId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("type") val category: String,
    @SerializedName("visible") val visibility: Boolean,
    @SerializedName("date") val date: String,
)
