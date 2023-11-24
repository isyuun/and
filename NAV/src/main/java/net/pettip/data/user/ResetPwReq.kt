package net.pettip.data.user


import com.google.gson.annotations.SerializedName

data class ResetPwReq(
    @SerializedName("email")
    var email: String, // dal@care-biz.co.kr
    @SerializedName("userPW")
    var userPW: String // dal
)