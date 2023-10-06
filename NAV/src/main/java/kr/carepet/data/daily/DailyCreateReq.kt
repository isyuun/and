package kr.carepet.data.daily


import com.google.gson.annotations.SerializedName

data class DailyCreateReq(
    @SerializedName("bwlMvmNmtm")
    var bwlMvmNmtm: Int, // 1
    @SerializedName("cmntUseYn")
    var cmntUseYn: String, // Y
    @SerializedName("files")
    var files: List<kr.carepet.data.daily.PhotoData>,
    @SerializedName("hashTag")
    var hashTag: List<String>,
    @SerializedName("pet")
    var pet: List<kr.carepet.data.daily.Pet>,
    @SerializedName("rcmdtnYn")
    var rcmdtnYn: String, // Y
    @SerializedName("rlsYn")
    var rlsYn: String, // Y
    @SerializedName("schCdList")
    var schCdList: List<String>,
    @SerializedName("schCn")
    var schCn: String, // String
    @SerializedName("schTtl")
    var schTtl: String, // String
    @SerializedName("totClr")
    var totClr: Int, // 340
    @SerializedName("totDstnc")
    var totDstnc: Int, // 1500
    @SerializedName("walkDptreDt")
    var walkDptreDt: String, // YYYYMMDDHH24MISS
    @SerializedName("walkEndDt")
    var walkEndDt: String // YYYYMMDDHH24MISS
)

data class File(
    @SerializedName("atchFileNm")
    var atchFileNm: String, // d3923ffef36d4cdeb06ffbf7c60bd21d.JPEG
    @SerializedName("atchFileSz")
    var atchFileSz: String, // 85917
    @SerializedName("fileExtnNm")
    var fileExtnNm: String, // JPEG
    @SerializedName("filePathNm")
    var filePathNm: String, // /dailyLife/20230822/
    @SerializedName("flmPstnLat")
    var flmPstnLat: String, // 0.0
    @SerializedName("flmPstnLot")
    var flmPstnLot: String, // 0.0
    @SerializedName("orgnlAtchFileNm")
    var orgnlAtchFileNm: String // KakaoTalk_Photo_2023-08-08-15-11-27.jpeg
)

data class Pet(
    @SerializedName("ownrPetUnqNo")
    var ownrPetUnqNo: String, // P20230822000046
    @SerializedName("petNm")
    var petNm: String // 이슬
)