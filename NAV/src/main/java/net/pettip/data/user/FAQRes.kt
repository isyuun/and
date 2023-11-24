package net.pettip.data.user


import com.google.gson.annotations.SerializedName

data class FAQRes(
    @SerializedName("data")
    var data: FAQData,
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: String, // FAQ 조회 완료
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class FAQData(
    @SerializedName("bbsFaqList")
    var bbsFaqList: List<BbsFaq>,
    @SerializedName("paginate")
    var paginate: Paginate
)

data class BbsFaq(
    @SerializedName("pstCn")
    var pstCn: String, // 회원 가입을 하세요.
    @SerializedName("pstSeCd")
    var pstSeCd: String, // 003
    @SerializedName("pstSn")
    var pstSn: Int, // 65
    @SerializedName("pstTtl")
    var pstTtl: String // 로그인 방법
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
    var totalRecordCount: Int // 1
)

data class BbsReq(
    @SerializedName("bbsSn")
    var bbsSn: Int, // 8
    @SerializedName("page")
    var page: Int, // 1
    @SerializedName("pageSize")
    var pageSize: Int, // 10
    @SerializedName("recordSize")
    var recordSize: Int // 20
)