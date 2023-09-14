package kr.carepet.data.pet


import com.google.gson.annotations.SerializedName

data class PetDetailRes(
    @SerializedName("data")
    var petDetailData: PetDetailData,
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: String, // 펫조회 완료
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class PetDetailData(
    @SerializedName("ntrYn")
    var ntrYn: String, // Y
    @SerializedName("ownrPetUnqNo")
    var ownrPetUnqNo: String, // P20230830000053
    @SerializedName("petBrthYmd")
    var petBrthYmd: String, // 20230404
    @SerializedName("petInfoUnqNo")
    var petInfoUnqNo: Int, // 141
    @SerializedName("petKindNm")
    var petKindNm: String, // 몰티즈
    @SerializedName("petMngrYn")
    var petMngrYn: String, // Y
    @SerializedName("petNm")
    var petNm: String, // 누렁이
    @SerializedName("petRegNo")
    var petRegNo: String, // Y
    @SerializedName("petRelCd")
    var petRelCd: String, // 001
    @SerializedName("petRelNm")
    var petRelNm: String, // 엄마
    @SerializedName("petRelUnqNo")
    var petRelUnqNo: Int, // 15
    @SerializedName("petRprsImgAddr")
    var petRprsImgAddr: String, // http://175.193.116.240/img/mypet/20230830//c317d30918704e8aacc4087eea86197f.JPG
    @SerializedName("petRprsYn")
    var petRprsYn: String, // Y
    @SerializedName("sexTypCd")
    var sexTypCd: String, // 001
    @SerializedName("sexTypNm")
    var sexTypNm: String, // 암컷
    @SerializedName("stdgCtpvCd")
    var stdgCtpvCd: String, // 11
    @SerializedName("stdgCtpvNm")
    var stdgCtpvNm: String, // 서울특별시
    @SerializedName("stdgSggCd")
    var stdgSggCd: String, // 101
    @SerializedName("stdgSggNm")
    var stdgSggNm: Any?, // null
    @SerializedName("stdgUmdCd")
    var stdgUmdCd: String, // 110
    @SerializedName("stdgUmdNm")
    var stdgUmdNm: Any?, // null
    @SerializedName("wghtVl")
    var wghtVl: Float // 6
)