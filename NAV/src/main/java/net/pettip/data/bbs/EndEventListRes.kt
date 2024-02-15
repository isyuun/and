package net.pettip.data.bbs


import com.google.gson.annotations.SerializedName

data class EndEventListRes(
    @SerializedName("data")
    var data: EndEventListData,
    @SerializedName("detailMessage")
    var detailMessage: String?, // null
    @SerializedName("resultMessage")
    var resultMessage: String, // 당첨자 발표 기본 조회 완료
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class EndEventListData(
    @SerializedName("bbsAncmntWinnerList")
    var bbsAncmntWinnerList: List<BbsAncmntWinner>,
    @SerializedName("paginate")
    var paginate: Paginate? // null
)

data class BbsAncmntWinner(
    @SerializedName("pstSn")
    var pstSn: Int, // 158
    @SerializedName("pstTtl")
    var pstTtl: String, // 누가 산책 많이 했나? 당첨자 발표
    @SerializedName("pstgBgngDt")
    var pstgBgngDt: String, // 2023-11-22
    @SerializedName("pstgEndDt")
    var pstgEndDt: String, // 2023-11-30
    @SerializedName("rprsImgUrl")
    var rprsImgUrl: String?
)