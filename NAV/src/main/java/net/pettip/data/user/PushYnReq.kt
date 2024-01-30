package net.pettip.data.user


import com.google.gson.annotations.SerializedName

data class PushYnReq(
    @SerializedName("appKey")
    var appKey: String?
)

data class PushYnRes(
    @SerializedName("data")
    var pushYnData: PushYnData?,
    @SerializedName("detailMessage")
    var detailMessage: String?, // null
    @SerializedName("resHeaders")
    var resHeaders: String?, // null
    @SerializedName("resultMessage")
    var resultMessage: String?, // 회원 알림사용여부 기록 조회 완료
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class PushYnData(
    @SerializedName("pushAdUseYn")
    var pushAdUseYn: String?, // Y
    @SerializedName("pushMdnghtUseYn")
    var pushMdnghtUseYn: String?, // Y
    @SerializedName("pushUseYn")
    var pushUseYn: String? // Y
)