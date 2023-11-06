package kr.carepet.data.pet


import com.google.gson.annotations.SerializedName

data class ChangePetWgtReq(
    @SerializedName("crtrYmd")
    var crtrYmd: Int, // 20230825
    @SerializedName("petDtlUnqNo")
    var petDtlUnqNo: Int, // 100
    @SerializedName("wghtVl")
    var wghtVl: Float // 5.9
)