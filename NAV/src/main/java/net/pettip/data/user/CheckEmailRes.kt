package net.pettip.data.user


import com.google.gson.annotations.SerializedName

data class CheckEmailRes(
    @SerializedName("data")
    var data: CheckEmailData?,
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resHeaders")
    var resHeaders: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: String?, // 정상 처리 되었습니다.
    @SerializedName("statusCode")
    var statusCode: Int? // 200
)

data class CheckEmailData(
    @SerializedName("signYn")
    var signYn: String?, // N
    @SerializedName("snsNm")
    var snsNm :String?
)