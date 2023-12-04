package net.pettip.data.bbs


import com.google.gson.annotations.SerializedName

data class QnaDetailRes(
    @SerializedName("data")
    var data: List<QnaDetailData>,
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: String, // 1:1문의(Q&A) 상세 조회 완료
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class QnaDetailData(
    @SerializedName("files")
    var files: List<File>,
    @SerializedName("frstInptDt")
    var frstInptDt: String, // 2023-11-30
    @SerializedName("pstCn")
    var pstCn: String, // 문의내용
    @SerializedName("pstSeCd")
    var pstSeCd: String, // 003
    @SerializedName("pstSn")
    var pstSn: Int, // 308
    @SerializedName("pstTtl")
    var pstTtl: String, // 제목
    @SerializedName("pstType")
    var pstType: String, // Org
    @SerializedName("atchPath")
    var atchPath: String
)

data class File(
    @SerializedName("atchFileNm")
    var atchFileNm: String, // f7aa5eab6d254bf990050203ed6152ef.JPG
    @SerializedName("atchFileSn")
    var atchFileSn: Int?, // 254
    @SerializedName("atchFileSz")
    var atchFileSz: Int, // 889033
    @SerializedName("fileExtnNm")
    var fileExtnNm: String, // JPG
    @SerializedName("filePathNm")
    var filePathNm: String, // /bbs/qna/atch/20231130/
    @SerializedName("orgnlAtchFileNm")
    var orgnlAtchFileNm: String, // image0.jpg
    @SerializedName("pstRprsYn")
    var pstRprsYn: String?, // N
    @SerializedName("pstSn")
    var pstSn: Int?, // 308
    @SerializedName("rowState")
    var rowState: String? = null // 308
)