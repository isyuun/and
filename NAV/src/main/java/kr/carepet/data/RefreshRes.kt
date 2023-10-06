package kr.carepet.data


import com.google.gson.annotations.SerializedName

data class RefreshRes(
    @SerializedName("data")
    var data: TokenData,
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: Any?, // null
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class TokenData(
    @SerializedName("accessToken")
    var accessToken: String,
    @SerializedName("failReason")
    var failReason: Any?, // null
    @SerializedName("message")
    var message: Any?, // null
    @SerializedName("refreshToken")
    var refreshToken: String,
    @SerializedName("status")
    var status: Boolean, // true
    @SerializedName("userId")
    var userId: String //

)

data class RefreshToken(
    var refreshToken: String
)