package kr.carepet.data.daily


import com.google.gson.annotations.SerializedName

data class DailyDetailRes(
    @SerializedName("data")
    var data: kr.carepet.data.daily.DailyDetailData,
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: String, // 일상생활 조회 완료
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class DailyDetailData(
    @SerializedName("atchPath")
    var atchPath: String, // http://carepet.hopto.org/img
    @SerializedName("bwlMvmNmtm")
    var bwlMvmNmtm: Int, // 0
    @SerializedName("cmntCnt")
    var cmntCnt: Int, // 0
    @SerializedName("cmntList")
    var cmntList: Any?, // null
    @SerializedName("cmntUseYn")
    var cmntUseYn: String, // Y
    @SerializedName("dailyLifeFileList")
    var dailyLifeFileList: List<kr.carepet.data.daily.DailyLifeFile>,
    @SerializedName("dailyLifePetList")
    var dailyLifePetList: List<kr.carepet.data.daily.DailyLifePet>,
    @SerializedName("dailyLifeSchHashTagList")
    var dailyLifeSchHashTagList: List<kr.carepet.data.daily.DailyLifeSchHashTag>,
    @SerializedName("dailyLifeSchSeList")
    var dailyLifeSchSeList: List<kr.carepet.data.daily.DailyLifeSchSe>,
    @SerializedName("delYn")
    var delYn: String, // N
    @SerializedName("nrcmdtnCnt")
    var nrcmdtnCnt: Int, // 0
    @SerializedName("rcmdtnCnt")
    var rcmdtnCnt: Int, // 0
    @SerializedName("rcmdtnYn")
    var rcmdtnYn: String, // Y
    @SerializedName("rgtrIpAddr")
    var rgtrIpAddr: Any?, // null
    @SerializedName("rlsDt")
    var rlsDt: String, // 2023.09.08 01:09
    @SerializedName("rlsYn")
    var rlsYn: String, // Y
    @SerializedName("schCn")
    var schCn: String,
    @SerializedName("schTtl")
    var schTtl: String, // 이슬 산책
    @SerializedName("schUnqNo")
    var schUnqNo: Int, // 3
    @SerializedName("totClr")
    var totClr: Int, // 340
    @SerializedName("totDstnc")
    var totDstnc: Int, // 1500
    @SerializedName("totMvmnPathFile")
    var totMvmnPathFile: Any?, // null
    @SerializedName("userId")
    var userId: String, // e94da19e-cc79-4153-8f07-5ec943a39d6f
    @SerializedName("walkDptreDt")
    var walkDptreDt: String, // 2023-09-08 10:27:21
    @SerializedName("walkEndDt")
    var walkEndDt: String // 2023-09-08 13:27:11
)

data class DailyLifeFile(
    @SerializedName("atchFileNm")
    var atchFileNm: String, // f98642708ab44adebc49800192773cbf.JPEG
    @SerializedName("atchFileSn")
    var atchFileSn: String, // 3
    @SerializedName("atchFileSz")
    var atchFileSz: Int, // 85917
    @SerializedName("fileExtnNm")
    var fileExtnNm: String, // JPEG
    @SerializedName("filePathNm")
    var filePathNm: String, // /dailyLife/20230908/
    @SerializedName("flmPstnLat")
    var flmPstnLat: Any?, // null
    @SerializedName("flmPstnLot")
    var flmPstnLot: Any?, // null
    @SerializedName("orgnlAtchFileNm")
    var orgnlAtchFileNm: String, // KakaoTalk_Photo_2023-08-08-15-11-27.jpeg
    @SerializedName("rowState")
    var rowState: Any?, // null
    @SerializedName("schUnqNo")
    var schUnqNo: Int // 3
)

data class DailyLifePet(
    @SerializedName("age")
    var age: String, // 6년9개월12일
    @SerializedName("ownrPetUnqNo")
    var ownrPetUnqNo: String, // P20230908000005
    @SerializedName("petNm")
    var petNm: String, // 이슬
    @SerializedName("rowState")
    var rowState: Any?, // null
    @SerializedName("schUnqNo")
    var schUnqNo: Int, // 3
    @SerializedName("stdgUmdNm")
    var stdgUmdNm: String // 공릉동
)

data class DailyLifeSchHashTag(
    @SerializedName("hashTagNm")
    var hashTagNm: String, // 댕댕이
    @SerializedName("hashTagNo")
    var hashTagNo: String, // 3
    @SerializedName("rowState")
    var rowState: Any?, // null
    @SerializedName("schUnqNo")
    var schUnqNo: Int // 3
)

data class DailyLifeSchSe(
    @SerializedName("cdId")
    var cdId: String, // 002
    @SerializedName("cdNm")
    var cdNm: String, // 일상
    @SerializedName("rowState")
    var rowState: Any?, // null
    @SerializedName("schUnqNo")
    var schUnqNo: Int // 3
)

data class DailyDetailReq(
    @SerializedName("cmntYn")
    var cmntYn: String, // Y
    @SerializedName("schUnqNo")
    var schUnqNo: Int // 3
)