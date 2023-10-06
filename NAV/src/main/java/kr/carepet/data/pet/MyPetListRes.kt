package kr.carepet.data.pet


import com.google.gson.annotations.SerializedName

data class MyPetListRes(
    @SerializedName("data")
    var petDetailData: List<PetDetailData>,
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: String, // 펫조회 완료
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class MyPetListReq(
    @SerializedName("userId")
    var userId:String
)