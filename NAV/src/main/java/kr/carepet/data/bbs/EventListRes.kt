package kr.carepet.data.bbs


import com.google.gson.annotations.SerializedName

data class EventListRes(
    @SerializedName("data")
    var data: EventListData,
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: String, // 이벤트 기본 조회 완료
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class EventListData(
    @SerializedName("bbsEvntList")
    var bbsEvntList: List<BbsEvnt>,
    @SerializedName("paginate")
    var paginate: Paginate
)

data class BbsEvnt(
    @SerializedName("pstSn")
    var pstSn: Int, // 89
    @SerializedName("pstTtl")
    var pstTtl: String, // 이미지,첨부 저장 경로 확인
    @SerializedName("pstgBgngDt")
    var pstgBgngDt: String?, // 2023-10-01 17:01:00
    @SerializedName("pstgEndDt")
    var pstgEndDt: String?, // 2023-10-31 17:01:00
    @SerializedName("rprsImgUrl")
    var rprsImgUrl: String // http://carepet.hopto.org/img/bbs/img/20231019/642c9f02735e464d9e1f8a195dedf461.JPG
)