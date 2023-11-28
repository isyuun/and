package net.pettip.data.bbs


import com.google.gson.annotations.SerializedName

data class QnaListRes(
    @SerializedName("data")
    var data: QnaData,
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: String, // 1:1문의(Q&A) 기본 조회 완료
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class QnaData(
    @SerializedName("bbsQnaList")
    var bbsQnaList: List<BbsQna>?,
    @SerializedName("paginate")
    var paginate: Paginate
)

data class BbsQna(
    @SerializedName("pstAnw")
    var pstAnw: Int, // 0
    @SerializedName("pstSn")
    var pstSn: Int, // 290
    @SerializedName("pstTtl")
    var pstTtl: String // 대표여부 없이
)