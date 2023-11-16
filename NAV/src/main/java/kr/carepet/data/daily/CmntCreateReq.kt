package kr.carepet.data.daily


import com.google.gson.annotations.SerializedName

data class CmntCreateReq(
    @SerializedName("cmntCn")
    var cmntCn: String, // 19
    @SerializedName("petRelUnqNo")
    var petRelUnqNo: Int, // 11
    @SerializedName("schUnqNo")
    var schUnqNo: Int, // 19
    @SerializedName("upCmntNo")
    var upCmntNo: Int // 19
)