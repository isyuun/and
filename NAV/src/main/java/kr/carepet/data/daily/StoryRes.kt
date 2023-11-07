package kr.carepet.data.daily


import com.google.gson.annotations.SerializedName

data class StoryRes(
    @SerializedName("data")
    var data: StoryData,
    @SerializedName("detailMessage")
    var detailMessage: String, // null
    @SerializedName("resultMessage")
    var resultMessage: String, // 실시간 스토리 조회 완료
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class StoryData(
    @SerializedName("paginate")
    var paginate: StoryPaginate,
    @SerializedName("storyList")
    var storyList: List<Story>
)

data class StoryPaginate(
    @SerializedName("endPage")
    var endPage: Int, // 5
    @SerializedName("existNextPage")
    var existNextPage: Boolean, // true
    @SerializedName("existPrevPage")
    var existPrevPage: Boolean, // false
    @SerializedName("startPage")
    var startPage: Int, // 1
    @SerializedName("totalPageCount")
    var totalPageCount: Int, // 5
    @SerializedName("totalRecordCount")
    var totalRecordCount: Int // 88
)

data class Story(
    @SerializedName("cmntCnt")
    var cmntCnt: String, // 0
    @SerializedName("inptDt")
    var inptDt: String, // 2023.09.20
    @SerializedName("petNm")
    var petNm: String, // Y
    @SerializedName("rcmdtnCnt")
    var rcmdtnCnt: String, // 0
    @SerializedName("schTtl")
    var schTtl: String, // String
    @SerializedName("schUnqNo")
    var schUnqNo: Int, // 7
    @SerializedName("storyFile")
    var storyFile: String? // http://carepet.hopto.org/img/dailyLife/20230920/ca3eb4c310094ceb98266afdc570b171.JPG
)