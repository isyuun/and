package net.pettip.data.bbs


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
    @SerializedName("pstCn")
    var pstCn: String, //
    @SerializedName("pstSn")
    var pstSn: Int, // 89
    @SerializedName("pstTtl")
    var pstTtl: String, // 이미지,첨부 저장 경로 확인
    @SerializedName("rcmdtnSeCd")
    var rcmdtnSeCd: String?,
    @SerializedName("rprsImgUrl")
    var rprsImgUrl: String?,
    @SerializedName("bbsCmnts")
    var bbsCmnts: List<BbsCmnt>?
)

//data class BbsCmnt(
//    @SerializedName("bldYn")
//    var bldYn: String,
//    @SerializedName("cmntCn")
//    var cmntCn: String,
//    @SerializedName("delYn")
//    var delYn: String,
//    @SerializedName("lastStrgDt")
//    var lastStrgDt: String,
//    @SerializedName("petImgUrl")
//    var petImgUrl: String?,
//    @SerializedName("petNm")
//    var petNm: String,
//    @SerializedName("prvtYn")
//    var prvtYn: String,
//    @SerializedName("pstCmntNo")
//    var pstCmntNo: Int,
//    @SerializedName("rcmdtnSeCd")
//    var rcmdtnSeCd: String?,
//    @SerializedName("rcmdtnCnt")
//    var rcmdtnCnt: Int?,
//    @SerializedName("nrcmdtnCnt")
//    var nrcmdtnCnt: Int?,
//    @SerializedName("upCmntNo")
//    var upCmntNo: Int,
//    @SerializedName("userId")
//    var userId: String
//)