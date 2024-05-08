package net.pettip.data.daily


import com.google.gson.annotations.SerializedName

data class DailyDetailRes(
    @SerializedName("data")
    var data: DailyDetailData?,
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: String?, // 일상생활 조회 완료
    @SerializedName("statusCode")
    var statusCode: Int? // 200
)

data class DailyDetailData(
    @SerializedName("atchPath")
    var atchPath: String?, //
    @SerializedName("cmntCnt")
    var cmntCnt: Int?, // 0
    @SerializedName("cmntList")
    var cmntList: List<Cmnt>?, // null
    @SerializedName("cmntUseYn")
    var cmntUseYn: String?, // Y
    @SerializedName("dailyLifeFileList")
    var dailyLifeFileList: List<DailyLifeFile>?,
    @SerializedName("dailyLifePetList")
    var dailyLifePetList: List<DailyLifePet>?,
    @SerializedName("dailyLifeSchHashTagList")
    var dailyLifeSchHashTagList: List<DailyLifeSchHashTag>?,
    @SerializedName("dailyLifeSchSeList")
    var dailyLifeSchSeList: List<DailyLifeSchSe>?,
    @SerializedName("delYn")
    var delYn: String?, // N
    @SerializedName("nrcmdtnCnt")
    var nrcmdtnCnt: Int?, // 0
    @SerializedName("rcmdtnCnt")
    var rcmdtnCnt: Int?, // 0
    @SerializedName("rcmdtnYn")
    var rcmdtnYn: String?, // Y
    @SerializedName("myRcmdtn")
    var myRcmdtn: String?,
    @SerializedName("rgtrIpAddr")
    var rgtrIpAddr: Any?, // null
    @SerializedName("rlsDt")
    var rlsDt: String?, // 2023.10.20 01:10
    @SerializedName("rlsYn")
    var rlsYn: String?, // Y
    @SerializedName("schCn")
    var schCn: String?, // 001 추가
    @SerializedName("schTtl")
    var schTtl: String?, // 코드리스트 추가
    @SerializedName("schUnqNo")
    var schUnqNo: Int?, // 53
    @SerializedName("totClr")
    var totClr: Int?, // 300
    @SerializedName("totDstnc")
    var totDstnc: Int?, // 0
    @SerializedName("totMvmnPathFile")
    var totMvmnPathFile: String?, // null
    @SerializedName("totMvmnPathFileSn")
    var totMvmnPathFileSn: String?, // null
    @SerializedName("userId")
    var userId: String?, // 9d93728e-c71e-41ee-ad8d-6ee741201921
    @SerializedName("walkDptreDt")
    var walkDptreDt: String?, // 2023-10-20 13:35:46
    @SerializedName("walkEndDt")
    var walkEndDt: String?, // 2023-10-20 13:35:46
    @SerializedName("runTime")
    var runTime: String?,
    @SerializedName("runDstnc")
    var runDstnc: Int?,
    @SerializedName("runClr")
    var runClr: Int?,
    @SerializedName("runNcknm")
    var runNcknm: String?
)

data class DailyLifeFile(
    @SerializedName("atchFileNm")
    var atchFileNm: String?, // 958984f26b154024a9f2bbf78be1235d.JPG
    @SerializedName("atchFileSn")
    var atchFileSn: String?, // 44
    @SerializedName("atchFileSz")
    var atchFileSz: Int?, // 2859770
    @SerializedName("fileExtnNm")
    var fileExtnNm: String?, // JPG
    @SerializedName("filePathNm")
    var filePathNm: String?, // /dailyLife/20231020/
    @SerializedName("flmPstnLat")
    var flmPstnLat: Any?, // null
    @SerializedName("flmPstnLot")
    var flmPstnLot: Any?, // null
    @SerializedName("orgnlAtchFileNm")
    var orgnlAtchFileNm: String?, // image_file_name.jpg
    @SerializedName("rowState")
    var rowState: String?, // null
    @SerializedName("schUnqNo")
    var schUnqNo: Int? // 53
)

data class DailyLifePet(
    @SerializedName("age")
    var age: String?, // 15년2일
    @SerializedName("bwlMvmNmtm")
    var bwlMvmNmtm: Int?, // 1
    @SerializedName("ownrPetUnqNo")
    var ownrPetUnqNo: String?, // P20231018000071
    @SerializedName("petNm")
    var petNm: String?, // 첫째
    @SerializedName("petImg")
    var petImg: String?,
    @SerializedName("relmIndctNmtm")
    var relmIndctNmtm: Int?, // 0
    @SerializedName("rowState")
    var rowState: String?, // null
    @SerializedName("schUnqNo")
    var schUnqNo: Int?, // 53
    @SerializedName("stdgUmdNm")
    var stdgUmdNm: String?, // null
    @SerializedName("urineNmtm")
    var urineNmtm: Int?, // 1
    @SerializedName("petTypCd")
    var petTypCd: String? // 1
)

data class DailyLifeSchHashTag(
    @SerializedName("hashTagNm")
    var hashTagNm: String?, // 해시
    @SerializedName("hashTagNo")
    var hashTagNo: String?, // 21
    @SerializedName("rowState")
    var rowState: String?, // null
    @SerializedName("schUnqNo")
    var schUnqNo: Int? // 53
)

data class DailyLifeSchSe(
    @SerializedName("cdId")
    var cdId: String?, // 001
    @SerializedName("cdNm")
    var cdNm: String?, // 산책
    @SerializedName("rowState")
    var rowState: String?, // null
    @SerializedName("schUnqNo")
    var schUnqNo: Int? // 53
)

data class DailyDetailReq(
    @SerializedName("cmntYn")
    var cmntYn: String?, // Y
    @SerializedName("schUnqNo")
    var schUnqNo: Int? // 3
)

data class Cmnt(
    @SerializedName("bldYn")
    var bldYn: String?, // N
    @SerializedName("delYn")
    var delYn: String?, // N
    @SerializedName("cmntCn")
    var cmntCn: String?, // 댓글 테스트
    @SerializedName("cmntNo")
    var cmntNo: Int?, // 10
    @SerializedName("lastStrgDt")
    var lastStrgDt: String?, // 23.11.14 03:22
    @SerializedName("nrcmdtnCnt")
    var nrcmdtnCnt: Int?, // 0
    @SerializedName("petImg")
    var petImg: String?, // /mypet/20231103/74d93117e6cb4950b2b6cb44706ada6e.JPG
    @SerializedName("petNm")
    var petNm: String?, // 수정
    @SerializedName("petRelUnqNo")
    var petRelUnqNo: Int?, // 157
    @SerializedName("rcmdtnCnt")
    var rcmdtnCnt: Int?, // 0
    @SerializedName("schUnqNo")
    var schUnqNo: Int?, // 214
    @SerializedName("stdgUmdNm")
    var stdgUmdNm: String?, // null
    @SerializedName("stepLevel")
    var stepLevel: Int?, // 1
    @SerializedName("upCmntNo")
    var upCmntNo: Int?, // 0
    @SerializedName("userId")
    var userId: String?, // 16a5e09c-884e-4f96-a7ab-aebd8756549a
    @SerializedName("myCmntRcmdtn")
    var myCmntRcmdtn: String?, //
    @SerializedName("petTypCd")
    var petTypCd: String?, //
)