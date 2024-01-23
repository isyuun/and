package net.pettip.data.bbs


import com.google.gson.annotations.SerializedName

data class EventDclrCreateReq(
    @SerializedName("pstCmntNo")
    var pstCmntNo: Int, // INT
    @SerializedName("dclrCn")
    var dclrCn: String, // String
    @SerializedName("dclrRsnCd")
    var dclrRsnCd: String, // 001
    @SerializedName("dclrSeCd")
    var dclrSeCd: String, // 001
    @SerializedName("pstSn")
    var pstSn: Int, // INT
)