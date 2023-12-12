package net.pettip.data.daily


import com.google.gson.annotations.SerializedName

data class DclrCreateReq(
    @SerializedName("cmntNo")
    var cmntNo: Int, // INT
    @SerializedName("dclrCn")
    var dclrCn: String, // String
    @SerializedName("dclrRsnCd")
    var dclrRsnCd: String, // 001
    @SerializedName("dclrSeCd")
    var dclrSeCd: String, // 001
    @SerializedName("schUnqNo")
    var schUnqNo: Int // INT
)