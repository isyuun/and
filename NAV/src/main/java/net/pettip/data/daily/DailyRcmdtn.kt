package net.pettip.data.daily


import com.google.gson.annotations.SerializedName

data class DailyRcmdtn(
    @SerializedName("rcmdtnSeCd")
    var rcmdtnSeCd: String, // 001
    @SerializedName("schUnqNo")
    var schUnqNo: Int // 26
)