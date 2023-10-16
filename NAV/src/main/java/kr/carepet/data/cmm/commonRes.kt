package kr.carepet.data.cmm


import com.google.gson.annotations.SerializedName

data class commonRes(
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("detailMessage")
    var detailMessage: String, // 처리중 오류 발생시 상세 메세지 노출.
    @SerializedName("resultMessage")
    var resultMessage: String, // 정상처리 되었습니다.
    @SerializedName("statusCode")
    var statusCode: Int // 200
)