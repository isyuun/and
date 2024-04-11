package net.pettip.data.cmm


import com.google.gson.annotations.SerializedName

data class CmmRes(
    @SerializedName("data")
    var data: List<CommonData>,
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: Any?, // null
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class CommonData(
    @SerializedName("cdDetailList")
    var cdDetailList: List<CdDetail>,
    @SerializedName("cdId")
    var cdId: String, // SCH
    @SerializedName("cdNm")
    var cdNm: String // 일정구분코드
)

data class CdDetail(
    @SerializedName("cdId")
    var cdId: String, // 001
    @SerializedName("cdNm")
    var cdNm: String, // 산책
    @SerializedName("upCdId")
    var upCdId: String // SCH
)

data class CmmReq(
    @SerializedName("cmmCdData")
    var cmmCdData: List<String?>
)