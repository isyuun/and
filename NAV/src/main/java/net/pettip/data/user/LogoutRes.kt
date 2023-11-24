package net.pettip.data.user


import com.google.gson.annotations.SerializedName

data class LogoutRes(
    @SerializedName("data")
    var `data`: Any?, // null
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: String, // 로그아웃처리가 완료되었습니다.
    @SerializedName("statusCode")
    var statusCode: Int // 200
)