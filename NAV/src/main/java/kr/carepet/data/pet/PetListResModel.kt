package kr.carepet.data.pet


import com.google.gson.annotations.SerializedName

data class PetListResModel(
    @SerializedName("data")
    var `data`: List<kr.carepet.data.pet.PetListData>,
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: Any?, // null
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class PetListData(
    @SerializedName("petDogSzCd")
    var petDogSzCd: String, // 001
    @SerializedName("petEnNm")
    var petEnNm: String, // NORWICH TERRIER
    @SerializedName("petInfoUnqNo")
    var petInfoUnqNo: Int, // 127
    @SerializedName("petNm")
    var petNm: String, // 노르위치 테리어
    @SerializedName("petTypCd")
    var petTypCd: String // 001
)

data class PetListModel(
    @SerializedName("petDogSzCd")
    var petDogSzCd: String, // 001
    @SerializedName("petTypCd")
    var petTypCd: String // 001
)