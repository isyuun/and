package net.pettip.data.user


import com.google.gson.annotations.SerializedName

data class TrmnlMngReq(
    @SerializedName("appKey")
    var appKey: String, // string
    @SerializedName("appOs")
    var appOs: String, // 001
    @SerializedName("appTypNm")
    var appTypNm: String // SM-T975N
)