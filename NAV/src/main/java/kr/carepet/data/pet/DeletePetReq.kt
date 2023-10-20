package kr.carepet.data.pet


import com.google.gson.annotations.SerializedName

data class DeletePetReq(
    @SerializedName("delYn")
    var delYn: String, // Y
    @SerializedName("ownrPetUnqNo")
    var ownrPetUnqNo: String // P20230808000027
)