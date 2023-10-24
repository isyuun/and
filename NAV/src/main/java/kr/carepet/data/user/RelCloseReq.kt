package kr.carepet.data.user


import com.google.gson.annotations.SerializedName

data class RelCloseReq(
    @SerializedName("ownrPetUnqNo")
    var ownrPetUnqNo: String, // P20230808000027
    @SerializedName("petRelUnqNo")
    var petRelUnqNo: Int // 245
)