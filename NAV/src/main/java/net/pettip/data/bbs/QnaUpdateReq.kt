package net.pettip.data.bbs


import com.google.gson.annotations.SerializedName

data class QnaUpdateReq(
    @SerializedName("files")
    var files: List<File>,
    @SerializedName("pstCn")
    var pstCn: String, // String
    @SerializedName("pstSeCd")
    var pstSeCd: String, // 001
    @SerializedName("pstSn")
    var pstSn: Int, // INT
    @SerializedName("pstTtl")
    var pstTtl: String // String
)