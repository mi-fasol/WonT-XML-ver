package com.example.xml_ver.data.retrofit.wish

import com.google.gson.annotations.SerializedName

data class WishListModel(
    @SerializedName("pid") val pId: Int? = null,
    @SerializedName("cpId") val cpId: Int? = null,
    @SerializedName("hpId") val hpId: Int? = null,
    @SerializedName("uid") val uId: Int
)