package net.pettip.data.daily


import com.google.gson.annotations.SerializedName

data class DailyRlsYnReq(
    @SerializedName("rlsYn")
    var rlsYn: String, // Y or N
    @SerializedName("schUnqNo")
    var schUnqNo: Int // 26
)