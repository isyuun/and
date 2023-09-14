package kr.carepet.data


import com.google.gson.annotations.SerializedName

data class UmdListRes(
    @SerializedName("data")
    var data: List<UmdList>,
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: Any?, // null
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class UmdList(
    @SerializedName("umdCd")
    var umdCd: String, // 101
    @SerializedName("umdNm")
    var umdNm: String // 상왕십리동
)

data class UmdListReq(
    @SerializedName("sggCd")
    var sggCd: String,
    @SerializedName("sidoCd")
    var sidoCd: String
)