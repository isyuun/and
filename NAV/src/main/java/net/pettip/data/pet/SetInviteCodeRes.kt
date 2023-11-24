package net.pettip.data.pet


import com.google.gson.annotations.SerializedName

data class SetInviteCodeRes(
    @SerializedName("data")
    var data: String, // null
    @SerializedName("detailMessage")
    var detailMessage: String, // null
    @SerializedName("resultMessage")
    var resultMessage: String, // 초대 키값 등록 완료
    @SerializedName("statusCode")
    var statusCode: Int // 200
)