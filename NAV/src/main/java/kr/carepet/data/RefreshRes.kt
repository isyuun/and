package kr.carepet.data


import com.google.gson.annotations.SerializedName

data class RefreshRes(
    @SerializedName("data")
    var data: TokenData,
    @SerializedName("detailMessage")
    var detailMessage: String, // null
    @SerializedName("resultMessage")
    var resultMessage: String, // null
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class TokenData(
    @SerializedName("accessToken")
    var accessToken: String,
    @SerializedName("failReason")
    var failReason: String, // null
    @SerializedName("message")
    var message: String, // null
    @SerializedName("refreshToken")
    var refreshToken: String,
    @SerializedName("status")
    var status: Boolean, // true
    @SerializedName("userId")
    var userId: String, //
    @SerializedName("nckNm")
    var nckNm: String, //
    @SerializedName("email")
    var email: String, //
)

data class RefreshToken(
    var refreshToken: String
)