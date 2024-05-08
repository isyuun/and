package net.pettip.data.pet


import com.google.gson.annotations.SerializedName

data class CurrentPetRes(
    @SerializedName("data")
    var data: List<CurrentPetData>,
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: String, // 목록 조회 완료
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class CurrentPetData(
    @SerializedName("age")
    var age: String, // 6년13일
    @SerializedName("ownrPetUnqNo")
    var ownrPetUnqNo: String, // P20230908000005
    @SerializedName("petKindNm")
    var petKindNm: String, // 말티푸
    @SerializedName("petNm")
    var petNm: String, // 이슬
    @SerializedName("petRprsImgAddr")
    var petRprsImgAddr: String,
    @SerializedName("sexTypNm")
    var sexTypNm: String?, // 남아
    @SerializedName("wghtVl")
    var wghtVl: Float, // 5.2
    @SerializedName("petRelUnqNo")
    var petRelUnqNo: Int, // 펫 관계번호
    @SerializedName("mngrType")
    var mngrType: String, // M: 관리중, I: 참여중
    @SerializedName("petTypCd")
    var petTypCd: String?, // 개 001 고양이 002
)