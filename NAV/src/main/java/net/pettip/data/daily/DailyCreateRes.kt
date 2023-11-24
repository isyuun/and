package net.pettip.data.daily


import com.google.gson.annotations.SerializedName

data class DailyCreateRes(
    @SerializedName("data")
    var data: DailyResData,
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: String, // 일상생활 등록 완료
    @SerializedName("statusCode")
    var statusCode: Int // 200
)

data class DailyResData(
    @SerializedName("schUnqNo")
    var schUnqNo: String // 48
)