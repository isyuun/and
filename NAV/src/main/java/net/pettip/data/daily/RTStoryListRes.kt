package net.pettip.data.daily


import com.google.gson.annotations.SerializedName

data class RTStoryListRes(
    @SerializedName("data")
    var data: List<RTStoryData>,
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: String, // 실시간 스토리 조회 완료
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class RTStoryData(
    @SerializedName("cmntCnt")
    var cmntCnt: String, // 0
    @SerializedName("petNm")
    var petNm: String, // 코코
    @SerializedName("rcmdtnCnt")
    var rcmdtnCnt: String, // 0
    @SerializedName("schTtl")
    var schTtl: String, // 코코 산책
    @SerializedName("schUnqNo")
    var schUnqNo: Int, // 173
    @SerializedName("storyFile")
    var storyFile: String? //
)