package kr.carepet.data.bbs


import com.google.gson.annotations.SerializedName

data class EventDetailRes(
    @SerializedName("data")
    var data: EventDetailData,
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: String, // 이벤트 상세 조회 완료
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class EventDetailData(
    @SerializedName("bbsEvntDtl")
    var bbsEvntDtl: List<BbsEvntDtl>
)

data class BbsEvntDtl(
    @SerializedName("pstCn")
    var pstCn: String, // ![](http://carepet.hopto.org/img/bbs/img/20231019/642c9f02735e464d9e1f8a195dedf461.JPG)
    @SerializedName("pstSn")
    var pstSn: Int, // 89
    @SerializedName("pstTtl")
    var pstTtl: String, // 이미지,첨부 저장 경로 확인
    @SerializedName("rprsImgUrl")
    var rprsImgUrl: String // http://carepet.hopto.org/img/bbs/img/20231019/642c9f02735e464d9e1f8a195dedf461.JPG
)