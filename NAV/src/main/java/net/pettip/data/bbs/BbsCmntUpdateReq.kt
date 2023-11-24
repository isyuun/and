package net.pettip.data.bbs


import com.google.gson.annotations.SerializedName

data class BbsCmntUpdateReq(
    @SerializedName("cmntCn")
    var cmntCn: String, // 댓글내용
    @SerializedName("pstCmntNo")
    var pstCmntNo: Int // INT
)