package kr.carepet.data


import com.google.gson.annotations.SerializedName

data class CommonCodeResModel(
    @SerializedName("data")
    var `data`: List<CcData>,
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: Any?, // null
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class CdDetail(
    @SerializedName("cdId")
    var cdId: String, // 001
    @SerializedName("cdNm")
    var cdNm: String, // 엄마
    @SerializedName("upCdId")
    var upCdId: String // REL
)

data class CcData(
    @SerializedName("cdDetailList")
    var cdDetailList: List<CdDetail>,
    @SerializedName("cdId")
    var cdId: String, // REL
    @SerializedName("cdNm")
    var cdNm: String // 관계코드
)


class CommonCodeModel (
    @SerializedName("cmmCdData")
    var cmmCdData:String
)