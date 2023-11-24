package net.pettip.data.pet


import com.google.gson.annotations.SerializedName

data class RegPetWgtReq(
    @SerializedName("crtrYmd")
    var crtrYmd: Int, // 20230825
    @SerializedName("ownrPetUnqNo")
    var ownrPetUnqNo: String, // P20230808000027
    @SerializedName("wghtVl")
    var wghtVl: Float // 5.9
)