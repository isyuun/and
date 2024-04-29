package net.pettip.data.daily


import com.google.gson.annotations.SerializedName

data class DailyUpdateReq(
    @SerializedName("cmntUseYn")
    var cmntUseYn: String, // Y
    @SerializedName("dailyLifeFileList")
    var dailyLifeFileList: List<Any?>?,
    @SerializedName("dailyLifePetList")
    var dailyLifePetList: List<Any>,
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

sealed class DailyLifeUpdatePet {
    data class PetWithDetails(
        val ownrPetUnqNo: String,
        val petNm: String,
        val stdgUmdNm: String,
        val age: String,
        val schUnqNo: Int,
        val bwlMvmNmtm: Int,
        val urineNmtm: Int,
        val relmIndctNmtm: Int,
        val rowState: String? // 또는 다른 타입으로 변경 가능
    ) : DailyLifeUpdatePet()

    data class SimplePet(
        val ownrPetUnqNo: String,
        val petNm: String,
        val rowState: String
    ) : DailyLifeUpdatePet()

}