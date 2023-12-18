package net.pettip.data.daily

/**
 * @Project     : PetTip-Android
 * @FileName    : TimeLineRes
 * @Date        : 2023-12-14
 * @author      : CareBiz
 * @description : net.pettip.data.daily
 * @see net.pettip.data.daily.TimeLineRes
 */


import com.google.gson.annotations.SerializedName

data class TimeLineRes(
    @SerializedName("data")
    val data: Data,
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("resultMessage")
    val resultMessage: String?,
    @SerializedName("detailMessage")
    val detailMessage: String?
)

data class Data(
    @SerializedName("dailyLifeTimeLineList")
    val dailyLifeTimeLineList: Map<String, List<LifeTimeLineItem>?>?,
    @SerializedName("paginate")
    val paginate: Paginate
)

data class LifeTimeLineItem(
    @SerializedName("schUnqNo")
    val schUnqNo: Int,
    @SerializedName("runTit")
    val runTit: String,
    @SerializedName("runCmn")
    val runCmn: String,
    @SerializedName("walkDptreDt")
    val walkDptreDt: String,
    @SerializedName("walkDptreTime")
    val walkDptreTime: String,
    @SerializedName("petList")
    val petList: List<PetItem>?,
    @SerializedName("runTime")
    val runTime: String,
    @SerializedName("runDstnc")
    val runDstnc: Int,
    @SerializedName("runClr")
    val runClr: Int,
    @SerializedName("runNcknm")
    val runNcknm: String,
    @SerializedName("runFile")
    val runFile: String?
)

data class PetItem(
    @SerializedName("ownrPetUnqNo")
    val ownrPetUnqNo: String,
    @SerializedName("petNm")
    val petNm: String,
    @SerializedName("petImg")
    val petImg: String
)
