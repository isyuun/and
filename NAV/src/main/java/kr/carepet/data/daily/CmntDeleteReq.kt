package kr.carepet.data.daily


import com.google.gson.annotations.SerializedName

data class CmntDeleteReq(
    @SerializedName("cmntNo")
    var cmntNo: Int // 10
)

data class BbsCmntDeleteReq(
    @SerializedName("pstCmntNo")
    var pstCmntNo: Int // 10
)