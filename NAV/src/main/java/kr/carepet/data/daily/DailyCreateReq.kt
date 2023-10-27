package kr.carepet.data.daily


import com.google.gson.annotations.SerializedName

data class DailyCreateReq(
    @SerializedName("cmntUseYn")
    var cmntUseYn: String, // Y
    @SerializedName("files")
    var files: List<PhotoData>?,
    @SerializedName("hashTag")
    var hashTag: List<String>,
    @SerializedName("pet")
    var pet: List<Pet>,
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
    var totClr: Float, // 340
    @SerializedName("totDstnc")
    var totDstnc: Float, // 1500
    @SerializedName("walkDptreDt")
    var walkDptreDt: String, // YYYYMMDDHH24MISS
    @SerializedName("walkEndDt")
    var walkEndDt: String // YYYYMMDDHH24MISS
)

data class Pet(
    @SerializedName("bwlMvmNmtm")
    var bwlMvmNmtm: String, // 0
    @SerializedName("ownrPetUnqNo")
    var ownrPetUnqNo: String, // P20230822000046
    @SerializedName("petNm")
    var petNm: String, // 이슬
    @SerializedName("relmIndctNmtm")
    var relmIndctNmtm: String, // 0
    @SerializedName("urineNmtm")
    var urineNmtm: String // 0
)