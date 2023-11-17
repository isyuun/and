package kr.carepet.data.daily


import com.google.gson.annotations.SerializedName

data class CmntDeleteReq(
    @SerializedName("cmntNo")
    var cmntNo: Int // 10
)