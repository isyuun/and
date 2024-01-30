package net.pettip.data.bbs


import com.google.gson.annotations.SerializedName

data class BbsCmtCreateReq(
    @SerializedName("cmntCn")
    var cmntCn: String?, // String
    @SerializedName("petRelUnqNo")
    var petRelUnqNo: Int?, // INT
    @SerializedName("pstSn")
    var pstSn: Int?, // INT
    @SerializedName("upCmntNo")
    var upCmntNo: Int? // INT
)