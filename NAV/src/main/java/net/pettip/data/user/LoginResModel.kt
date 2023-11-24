package net.pettip.data.user


import com.google.gson.annotations.SerializedName

data class LoginResModel(
    @SerializedName("data")
    var `data`: Data,
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: Any?, // null
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class Data(
    @SerializedName("accessToken")
    var accessToken: String,
    @SerializedName("failReason")
    var failReason: Any?, // null
    @SerializedName("message")
    var message: Any?, // null
    @SerializedName("refreshToken")
    var refreshToken: String,
    @SerializedName("scope")
    var scope: String, // profile email
    @SerializedName("status")
    var status: Boolean, // true
    @SerializedName("tokenType")
    var tokenType: String, // Bearer
    @SerializedName("userId")
    var userId: String, //
    @SerializedName("nckNm")
    var nckNm: String, //
    @SerializedName("email")
    var email: String, //
    @SerializedName("appKeyVl")
    var appKeyVl: String?,
)

data class LoginData(
    var appTypNm : String,
    var userID: String,
    var userPW: String
)