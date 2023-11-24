package net.pettip.data.user


import com.google.gson.annotations.SerializedName

data class ResetNickNameReq(
    @SerializedName("ncknm")
    var ncknm: String, // dall
    @SerializedName("userID")
    var userID: String // 9d70ff46-c9e6-4b29-b69a-f9f3829c7eb0
)