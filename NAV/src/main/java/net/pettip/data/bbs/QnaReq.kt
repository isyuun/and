package net.pettip.data.bbs


import com.google.gson.annotations.SerializedName
import net.pettip.data.daily.PhotoData

data class QnaReq(
    @SerializedName("bbsSn")
    var bbsSn: Int, // 10
    @SerializedName("files")
    var files: List<PhotoData>?,
    @SerializedName("pstCn")
    var pstCn: String, // String
    @SerializedName("pstSeCd")
    var pstSeCd: String, // 001
    @SerializedName("pstTtl")
    var pstTtl: String, // String
)
