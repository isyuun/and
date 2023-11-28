package net.pettip.data.bbs


import com.google.gson.annotations.SerializedName

data class NtcListRes(
    @SerializedName("data")
    var data: NtcData,
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: String, // 공지사항 기본 조회 완료
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class NtcData(
    @SerializedName("bbsNtcList")
    var bbsNtcList: List<BbsNtc>,
    @SerializedName("paginate")
    var paginate: Paginate
)

data class BbsNtc(
    @SerializedName("pstSn")
    var pstSn: Int, // 201
    @SerializedName("pstTtl")
    var pstTtl: String, // 서버 확인2
    @SerializedName("pstgBgngDt")
    var pstgBgngDt: String? // 2023-10-20
)