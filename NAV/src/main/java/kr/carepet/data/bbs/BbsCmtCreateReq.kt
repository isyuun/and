package kr.carepet.data.bbs


import com.google.gson.annotations.SerializedName

data class BbsCmtCreateReq(
    @SerializedName("cmntCn")
    var cmntCn: String, // String
    @SerializedName("petRelUnqNo")
    var petRelUnqNo: Int, // INT
    @SerializedName("pstSn")
    var pstSn: Int, // INT
    @SerializedName("upCmntNo")
    var upCmntNo: Int, // INT
    @SerializedName("userId")
    var userId: String // d8a80b0e-333b-4bba-94f7-fe89749a7eea
)