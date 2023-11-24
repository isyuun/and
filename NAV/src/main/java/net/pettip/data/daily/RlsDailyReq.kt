package net.pettip.data.daily


import com.google.gson.annotations.SerializedName

data class RlsDailyReq(
    @SerializedName("rlsYn")
    var rlsYn: String, // N
    @SerializedName("schUnqNo")
    var schUnqNo: Int // 228
)