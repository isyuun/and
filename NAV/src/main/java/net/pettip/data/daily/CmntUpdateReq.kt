package net.pettip.data.daily


import com.google.gson.annotations.SerializedName

data class CmntUpdateReq(
    @SerializedName("cmntCn")
    var cmntCn: String, // 댓글내용
    @SerializedName("cmntNo")
    var cmntNo: Int // 42
)