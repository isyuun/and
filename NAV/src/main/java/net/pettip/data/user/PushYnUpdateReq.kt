package net.pettip.data.user


import com.google.gson.annotations.SerializedName

data class PushYnUpdateReq(
    @SerializedName("appKey")
    var appKey: String?, // String
    @SerializedName("pushAdUseYn")
    var pushAdUseYn: String, // Y or N
    @SerializedName("pushMdnghtUseYn")
    var pushMdnghtUseYn: String, // Y or N
    @SerializedName("pushUseYn")
    var pushUseYn: String // Y or N
)