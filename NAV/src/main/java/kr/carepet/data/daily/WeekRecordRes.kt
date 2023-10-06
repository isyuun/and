package kr.carepet.data.daily


import com.google.gson.annotations.SerializedName

data class WeekRecordRes(
    @SerializedName("data")
    var data: kr.carepet.data.daily.WeekData,
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: String, // 해당 주별 조회 완료
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class WeekData(
    @SerializedName("dayList")
    var dayList: List<kr.carepet.data.daily.Day>,
    @SerializedName("runClr")
    var runClr: Int, // 200
    @SerializedName("runCnt")
    var runCnt: Int, // 1
    @SerializedName("runDstnc")
    var runDstnc: Int, // 1000
    @SerializedName("runTime")
    var runTime: String, // 02:07:00
    @SerializedName("dailyLifeWalkList")
    var dailyLifeWalkList: List<kr.carepet.data.daily.DailyLifeWalk>
)


data class Day(
    @SerializedName("date")
    var date: String, // 2023-09-10
    @SerializedName("dayNm")
    var dayNm: String, // 일
    @SerializedName("runCnt")
    var runCnt: Int // 1
)

data class WeekRecordReq(
    @SerializedName("ownrPetUnqNo")
    var ownrPetUnqNo: String,
    @SerializedName("searchDay")
    var searchDay: String
)