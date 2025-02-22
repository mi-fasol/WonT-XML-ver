package com.example.xml_ver.data.retrofit.wish

import com.google.gson.annotations.SerializedName
import java.io.Serial

data class WishListResponseModel(
    @SerializedName("wmId", alternate = ["wcId", "wid"]) val wId : Int,
    @SerializedName("pid", alternate = ["cpId", "hpId"]) val pId : Int,
    @SerializedName("uid") val uId: Int,
)