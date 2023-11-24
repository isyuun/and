package net.pettip.data.pet


import com.google.gson.annotations.SerializedName

data class PetWgtRes(
    @SerializedName("data")
    var data: List<PetWgtData>?,
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: String, // 정상 처리 되었습니다.
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class PetWgtData(
    @SerializedName("crtrYmd")
    var crtrYmd: String, // 2023.11.01
    @SerializedName("ownrPetUnqNo")
    var ownrPetUnqNo: String, // P20231027000103
    @SerializedName("petDtlUnqNo")
    var petDtlUnqNo: Int, // 108
    @SerializedName("userId")
    var userId: String, // 16a5e09c-884e-4f96-a7ab-aebd8756549a
    @SerializedName("wghtVl")
    var wghtVl: Double // 1.5
)