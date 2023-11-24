package net.pettip.data.daily


import com.google.gson.annotations.SerializedName

data class DailyMonthRes(
    @SerializedName("data")
    var data: DailyMonthData,
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: String, // 해당 월별 조회 완료
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class DailyMonthData(
    @SerializedName("dayList")
    var dayList: List<MonthDay>,
    @SerializedName("runClr")
    var runClr: Int, // 1040
    @SerializedName("runCnt")
    var runCnt: Int, // 2
    @SerializedName("runDstnc")
    var runDstnc: Int, // 4000
    @SerializedName("runTime")
    var runTime: String // 00:00:20
)

data class MonthDay(
    @SerializedName("date")
    var date: String, // 2023-08-27
    @SerializedName("dayNm")
    var dayNm: String, // 일
    @SerializedName("runCnt")
    var runCnt: Int, // 0
    @SerializedName("thisMonthYn")
    var thisMonthYn: Any?, // null
    @SerializedName("todayYn")
    var todayYn: Any? // null
)

data class DailyMonthReq(
    @SerializedName("ownrPetUnqNo")
    var ownrPetUnqNo: String, // P20230914000008
    @SerializedName("searchMonth")
    var searchMonth: String // 2023-09
)