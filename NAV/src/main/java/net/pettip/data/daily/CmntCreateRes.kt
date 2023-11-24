package net.pettip.data.daily


import com.google.gson.annotations.SerializedName

data class CmntCreateRes(
    @SerializedName("data")
    var data: List<Cmnt>?,
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: String, // 일상생활 댓글 등록 완료
    @SerializedName("statusCode")
    var statusCode: Int // 200
)
