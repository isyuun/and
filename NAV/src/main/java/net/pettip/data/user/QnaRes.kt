package net.pettip.data.user


import com.google.gson.annotations.SerializedName

data class QnaRes(
    @SerializedName("data")
    var data: QnaData,
    @SerializedName("detailMessage")
    var detailMessage: String?, // null
    @SerializedName("resultMessage")
    var resultMessage: String, // 1:1문의(Q&A) 기본 조회 완료
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class QnaData(
    @SerializedName("bbsQnaBscList")
    var bbsQnaBscList: List<BbsQnaBsc>?,
    @SerializedName("paginate")
    var paginate: Paginate
)

data class BbsQnaBsc(
    @SerializedName("frstInptDt")
    var frstInptDt: String,
    @SerializedName("pstAnw")
    var pstAnw: Int,
    @SerializedName("pstSeCd")
    var pstSeCd: String,
    @SerializedName("pstSn")
    var pstSn: Int,
    @SerializedName("pstTtl")
    var pstTtl: String
)

data class QnaReq(
    @SerializedName("bbsSn")
    var bbsSn: Int, // 10
    @SerializedName("page")
    var page: Int, // 1
    @SerializedName("pageSize")
    var pageSize: Int, // 10
    @SerializedName("recordSize")
    var recordSize: Int, // 20
    @SerializedName("userId")
    var userId: String // e94da19e-cc79-4153-8f07-5ec943a39d6f
)