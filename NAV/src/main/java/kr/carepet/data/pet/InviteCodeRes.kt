package kr.carepet.data.pet


import com.google.gson.annotations.SerializedName

data class InviteCodeRes(
    @SerializedName("data")
    var data: InviteData,
    @SerializedName("detailMessage")
    var detailMessage: String, // null
    @SerializedName("resultMessage")
    var resultMessage: String, // 초대 키값 생성 완료
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class InviteData(
    @SerializedName("invttKeyVl")
    var invttKeyVl: String, // PDQL0LYDV1
    @SerializedName("regPsbltyBgngDt")
    var regPsbltyBgngDt: String, // 20231005000000
    @SerializedName("regPsbltyEndDt")
    var regPsbltyEndDt: String // 20231006000000
)