package kr.carepet.data.daily


import com.google.gson.annotations.SerializedName

data class WalkListRes(
    @SerializedName("data")
    var data: kr.carepet.data.daily.WalkListResData,
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: String, // 목록 조회 완료
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class WalkListResData(
    @SerializedName("dailyLifeWalkList")
    var dailyLifeWalkList: List<kr.carepet.data.daily.DailyLifeWalk>,
    @SerializedName("paginate")
    var paginate: kr.carepet.data.daily.Paginate
)

data class DailyLifeWalk(
    @SerializedName("petNm")
    var petNm: String, // 이슬
    @SerializedName("runClr")
    var runClr: Int, // 200
    @SerializedName("runDstnc")
    var runDstnc: Int, // 1000
    @SerializedName("runNcknm")
    var runNcknm: String, // 슬아빠
    @SerializedName("runTime")
    var runTime: String, // 02:07:00
    @SerializedName("schUnqNo")
    var schUnqNo: Int, // 5
    @SerializedName("walkDptreDt")
    var walkDptreDt: String

)

data class Paginate(
    @SerializedName("endPage")
    var endPage: Int, // 1
    @SerializedName("existNextPage")
    var existNextPage: Boolean, // false
    @SerializedName("existPrevPage")
    var existPrevPage: Boolean, // false
    @SerializedName("startPage")
    var startPage: Int, // 1
    @SerializedName("totalPageCount")
    var totalPageCount: Int, // 1
    @SerializedName("totalRecordCount")
    var totalRecordCount: Int // 3
)

data class WalkListReq(
    @SerializedName("ownrPetUnqNo")
    var ownrPetUnqNo: String, // P20230908000005
    @SerializedName("page")
    var page: Int, // 1
    @SerializedName("pageSize")
    var pageSize: Int, // 10
    @SerializedName("recordSize")
    var recordSize: Int // 20
)