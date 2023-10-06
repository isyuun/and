package kr.carepet.data


import com.google.gson.annotations.SerializedName

data class SggListRes(
    @SerializedName("data")
    var data: List<SggList>,
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: Any?, // null
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class SggList(
    @SerializedName("sggCd")
    var sggCd: String, // 110
    @SerializedName("sggNm")
    var sggNm: String // 종로구
)