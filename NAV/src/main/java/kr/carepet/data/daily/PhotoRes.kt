package kr.carepet.data.daily


import com.google.gson.annotations.SerializedName

data class PhotoRes(
    @SerializedName("data")
    var data: List<kr.carepet.data.daily.PhotoData>,
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: Any?, // null
    @SerializedName("statusCode")
    var statusCode: Int // 0
)

data class PhotoData(
    @SerializedName("atchFileNm")
    var atchFileNm: String, // 52366e71453548578f64e05436d79560.JPG
    @SerializedName("atchFileSz")
    var atchFileSz: Int, // 197840
    @SerializedName("fileExtnNm")
    var fileExtnNm: String, // JPG
    @SerializedName("filePathNm")
    var filePathNm: String, // /dailyLife/20230904/
    @SerializedName("flmPstnLat")
    var flmPstnLat: Any?, // null
    @SerializedName("flmPstnLot")
    var flmPstnLot: Any?, // null
    @SerializedName("orgnlAtchFileNm")
    var orgnlAtchFileNm: String // dog1.jpg
)