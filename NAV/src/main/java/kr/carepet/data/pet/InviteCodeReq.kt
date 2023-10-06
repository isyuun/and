package kr.carepet.data.pet


import com.google.gson.annotations.SerializedName

data class InviteCodeReq(
    @SerializedName("pet")
    var pet: List<kr.carepet.data.pet.Pet>,
    @SerializedName("relBgngDt")
    var relBgngDt: String, // 202310050918
    @SerializedName("relEndDt")
    var relEndDt: String // 299912311159
)

data class Pet(
    @SerializedName("ownrPetUnqNo")
    var ownrPetUnqNo: String, // P20230822000046
    @SerializedName("petNm")
    var petNm: String // 이슬
)