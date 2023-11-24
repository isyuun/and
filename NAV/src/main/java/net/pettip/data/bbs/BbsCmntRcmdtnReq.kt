package net.pettip.data.bbs


import com.google.gson.annotations.SerializedName

data class BbsCmntRcmdtnReq(
    @SerializedName("pstCmntNo")
    var pstCmntNo: Int, // 16
    @SerializedName("pstSn")
    var pstSn: Int, // 001
    @SerializedName("rcmdtnSeCd")
    var rcmdtnSeCd: String // 43
)