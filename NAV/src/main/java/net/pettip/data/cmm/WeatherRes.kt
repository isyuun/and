package net.pettip.data.cmm


import com.google.gson.annotations.SerializedName

data class WeatherRes(
    @SerializedName("data")
    var data: List<WeatherData>,
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: String, // 기상정보 조회 완료
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class WeatherData(
    @SerializedName("baseDate")
    var baseDate: String, // 20231101
    @SerializedName("baseTime")
    var baseTime: String, // 0800
    @SerializedName("category")
    var category: String, // TMP
    @SerializedName("fcstDate")
    var fcstDate: String, // 20231101
    @SerializedName("fcstTime")
    var fcstTime: String, // 1000
    @SerializedName("fcstValue")
    var fcstValue: String, // 18
    @SerializedName("nx")
    var nx: String, // 61
    @SerializedName("ny")
    var ny: String // 126
)