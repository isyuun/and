package net.pettip.data.daily


import com.google.gson.annotations.SerializedName

data class CmntRcmdtnReq(
    @SerializedName("cmntNo")
    var cmntNo: Int, // 16
    @SerializedName("rcmdtnSeCd")
    var rcmdtnSeCd: String, // 001
    @SerializedName("schUnqNo")
    var schUnqNo: Int // 43
)