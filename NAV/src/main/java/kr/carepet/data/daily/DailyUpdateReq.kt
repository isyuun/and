package kr.carepet.data.daily


import com.google.gson.annotations.SerializedName

data class DailyUpdateReq(
    @SerializedName("cmntUseYn")
    var cmntUseYn: String, // Y
    @SerializedName("dailyLifeFileList")
    var dailyLifeFileList: List<DailyLifeFile>?,
    @SerializedName("dailyLifePetList")
    var dailyLifePetList: List<DailyLifePet>?,
    @SerializedName("dailyLifeSchHashTagList")
    var dailyLifeSchHashTagList: List<DailyLifeSchHashTag>?,
    @SerializedName("dailyLifeSchSeList")
    var dailyLifeSchSeList: List<DailyLifeSchSe>?,
    @SerializedName("delYn")
    var delYn: String, // N
    @SerializedName("rcmdtnYn")
    var rcmdtnYn: String, // Y
    @SerializedName("rlsYn")
    var rlsYn: String, // Y
    @SerializedName("schCn")
    var schCn: String, // String
    @SerializedName("schTtl")
    var schTtl: String, // String
    @SerializedName("schUnqNo")
    var schUnqNo: Int // 43
)

