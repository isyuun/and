package net.pettip.data.user


import com.google.gson.annotations.SerializedName

data class NickNameCheckRes(
    @SerializedName("data")
    var `data`: Any?, // null
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: String, // 정상 처리 되었습니다.
    @SerializedName("statusCode")
    var statusCode: Int // 200
)