package net.pettip.data.pet


import com.google.gson.annotations.SerializedName

data class PetDetailRes(
    @SerializedName("data")
    var petDetailData: PetDetailData,
    @SerializedName("detailMessage")
    var detailMessage: String?, // null
    @SerializedName("resultMessage")
    var resultMessage: String, // 펫조회 완료
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class PetDetailData(
    @SerializedName("endDt")
    var endDt: String?,
    @SerializedName("memberList")
    var memberList: List<Member>,
    @SerializedName("mngrType")
    var mngrType: String?, // M
    @SerializedName("ntrTypCd")
    var ntrTypCd: String, // 001
    @SerializedName("ntrTypNm")
    var ntrTypNm: String?, // 했어요
    @SerializedName("ownrPetUnqNo")
    var ownrPetUnqNo: String, // P20230922000033
    @SerializedName("petBrthYmd")
    var petBrthYmd: String, // 20150101
    @SerializedName("petInfoUnqNo")
    var petInfoUnqNo: Int, // 140
    @SerializedName("petKindNm")
    var petKindNm: String, // 맨체스터 테리어
    @SerializedName("petMngrYn")
    var petMngrYn: String, // Y
    @SerializedName("petNm")
    var petNm: String, // 태백
    @SerializedName("petRegNo")
    var petRegNo: String, // Y
    @SerializedName("petRelCd")
    var petRelCd: String, // 001
    @SerializedName("petRelNm")
    var petRelNm: String?, // 엄마
    @SerializedName("petRelUnqNo")
    var petRelUnqNo: Int, // 30
    @SerializedName("petRprsImgAddr")
    var petRprsImgAddr: String?, // http://carepet.hopto.org/img/mypet/20230920/46bb3b500643478e8c5a5b14975fe983.JPG
    @SerializedName("petRprsYn")
    var petRprsYn: String, // Y
    @SerializedName("sexTypCd")
    var sexTypCd: String, // 002
    @SerializedName("sexTypNm")
    var sexTypNm: String?, // 남아
    @SerializedName("stdgCtpvCd")
    var stdgCtpvCd: String, // 11
    @SerializedName("stdgCtpvNm")
    var stdgCtpvNm: String, // 서울특별시
    @SerializedName("stdgSggCd")
    var stdgSggCd: String, // 350
    @SerializedName("stdgSggNm")
    var stdgSggNm: String, // 노원구
    @SerializedName("stdgUmdCd")
    var stdgUmdCd: String, // 103
    @SerializedName("stdgUmdNm")
    var stdgUmdNm: String?, // 공릉동
    @SerializedName("wghtVl")
    var wghtVl: Float, // 4.1
    @SerializedName("petTypCd")
    var petTypCd: String? // 개, 고양이 분류 코드
)

data class Member(
    @SerializedName("userId")
    var userId: String,
    @SerializedName("endDt")
    var endDt: String,
    @SerializedName("mngrType")
    var mngrType: String, // M
    @SerializedName("nckNm")
    var nckNm: String, // dal
    @SerializedName("ownrPetUnqNo")
    var ownrPetUnqNo: String,
    @SerializedName("petRelUnqNo")
    var petRelUnqNo: Int
)

data class PetDetailReq(
    @SerializedName("ownrPetUnqNo")
    var ownrPetUnqNo: String, // P20231010000039
    @SerializedName("petRprsYn")
    var petRprsYn: String, // Y
    @SerializedName("userId")
    var userId: String // 9d70ff46-c9e6-4b29-b69a-f9f3829c7eb0
)