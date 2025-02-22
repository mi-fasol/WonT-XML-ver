package com.example.xml_ver.data.retrofit.comment.reply

import com.google.gson.annotations.SerializedName

data class ReplyModel(
    @SerializedName("cid") val cId: Int? = null,
    @SerializedName("ccId") val ccId: Int? = null,
    @SerializedName("hcId") val hcId: Int? = null,
    @SerializedName("content") val content: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("date") val date: String
)
