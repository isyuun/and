package net.pettip.data.bbs


import com.google.gson.annotations.SerializedName

data class BbsDetailRes(
    @SerializedName("data")
    var data: BbsData,
    @SerializedName("detailMessage")
    var detailMessage: String?, // null
    @SerializedName("resultMessage")
    var resultMessage: String?, // 이벤트 상세 조회 완료
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class BbsData(
    @SerializedName("bbsAtch")
    var bbsAtch: List<BbsAtch>?,
    @SerializedName("bbsCmnts")
    var bbsCmnts: List<BbsCmnt>?,
    @SerializedName("bbsSn")
    var bbsSn: Int?, // 0
    @SerializedName("bldYn")
    var bldYn: String?, // null
    @SerializedName("cmntCnt")
    var cmntCnt: Int?, // 0
    @SerializedName("frstInptDt")
    var frstInptDt: String?, // 2023-11-22 15:44
    @SerializedName("frstKbrdrId")
    var frstKbrdrId: String?, // null
    @SerializedName("inqCnt")
    var inqCnt: Int?, // 0
    @SerializedName("lastMdfrId")
    var lastMdfrId: String?, // null
    @SerializedName("lastStrgDt")
    var lastStrgDt: String?, // 2023-11-25 21:03
    @SerializedName("nrcmdtnCnt")
    var nrcmdtnCnt: Int?, // 0
    @SerializedName("pblrNm")
    var pblrNm: String?, // null
    @SerializedName("pstCn")
    var pstCn: String?, // <p>이벤트 진행중</p>
    @SerializedName("pstSeCd")
    var pstSeCd: String?, // null
    @SerializedName("pstSn")
    var pstSn: Int?, // 284
    @SerializedName("pstTtl")
    var pstTtl: String?, // 이벤트 진행중
    @SerializedName("pstType")
    var pstType: String?, // null
    @SerializedName("pstgBgngDt")
    var pstgBgngDt: String?, // null
    @SerializedName("pstgEndDt")
    var pstgEndDt: String?, // null
    @SerializedName("pstgYn")
    var pstgYn: String?, // null
    @SerializedName("rcmdtnCnt")
    var rcmdtnCnt: Int?, // 0
    @SerializedName("rcmdtnSeCd")
    var rcmdtnSeCd: String?, // null
    @SerializedName("relPstSn")
    var relPstSn: Int?, // 0
    @SerializedName("rprsImgUrl")
    var rprsImgUrl: String?,
    @SerializedName("upPstNo")
    var upPstNo: Int?, // 0
    @SerializedName("userId")
    var userId: String? // null
)

data class BbsAtch(
    @SerializedName("atchFileNm")
    var atchFileNm: String, // f14992aba44f46938d4b231be0b75bc0.JPG
    @SerializedName("atchFileSn")
    var atchFileSn: Int, // 244
    @SerializedName("atchFileSz")
    var atchFileSz: Int, // 51140
    @SerializedName("fileExtnNm")
    var fileExtnNm: String, // JPG
    @SerializedName("filePathNm")
    var filePathNm: String, // /bbs/event/atch/20231122/
    @SerializedName("orgnlAtchFileNm")
    var orgnlAtchFileNm: String, // 진행이벤트.jpg
    @SerializedName("pstRprsYn")
    var pstRprsYn: String, // Y
    @SerializedName("pstSn")
    var pstSn: Int // 284
)

data class BbsCmnt(
    @SerializedName("bldYn")
    var bldYn: String?, // N
    @SerializedName("cmntCn")
    var cmntCn: String?, // 댓글
    @SerializedName("delYn")
    var delYn: String?, // N
    @SerializedName("lastStrgDt")
    var lastStrgDt: String?, // 2023-11-23 11:30
    @SerializedName("nrcmdtnCnt")
    var nrcmdtnCnt: Int?, // 0
    @SerializedName("petImgUrl")
    var petImgUrl: String?,
    @SerializedName("petNm")
    var petNm: String?, // 수정
    @SerializedName("prvtYn")
    var prvtYn: String?, // N
    @SerializedName("pstCmntNo")
    var pstCmntNo: Int?, // 27
    @SerializedName("rcmdtnCnt")
    var rcmdtnCnt: Int?, // 1
    @SerializedName("rcmdtnSeCd")
    var rcmdtnSeCd: String?, // 001
    @SerializedName("upCmntNo")
    var upCmntNo: Int?, // 0
    @SerializedName("userId")
    var userId: String? // 16a5e09c-884e-4f96-a7ab-aebd8756549a
)