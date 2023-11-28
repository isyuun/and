package net.pettip.data.bbs


import com.google.gson.annotations.SerializedName

data class QnaDetailRes(
    @SerializedName("data")
    var data: List<QnaDetailData>?,
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: String, // 1:1문의(Q&A) 상세 조회 완료
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class QnaDetailData(
    @SerializedName("frstInptDt")
    var frstInptDt: String, // 2023-11-28
    @SerializedName("pstCn")
    var pstCn: String, // ㅁㅁ
    @SerializedName("pstSn")
    var pstSn: Int, // 292
    @SerializedName("pstTtl")
    var pstTtl: String, // ㅁ
    @SerializedName("pstType")
    var pstType: String // Org
)