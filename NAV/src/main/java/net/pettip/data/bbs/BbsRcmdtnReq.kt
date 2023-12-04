package net.pettip.data.bbs


import com.google.gson.annotations.SerializedName

data class BbsRcmdtnReq(
    @SerializedName("pstSn")
    var pstSn: Int, // INT
    @SerializedName("rcmdtnSeCd")
    var rcmdtnSeCd: String // 001
)