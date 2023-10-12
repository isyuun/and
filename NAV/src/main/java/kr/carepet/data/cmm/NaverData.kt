package kr.carepet.data.cmm

import com.google.gson.annotations.SerializedName

data class NidUserInfoResponse(
    @SerializedName("resultcode")
    var resultcode:String,
    @SerializedName("message")
    var message:String,
    @SerializedName("response")
    var response: NidUser
)


data class NidUser(
    @SerializedName("id")
    var id:String,
    @SerializedName("email")
    var email:String,
    @SerializedName("nickName")
    var nickName:String
)