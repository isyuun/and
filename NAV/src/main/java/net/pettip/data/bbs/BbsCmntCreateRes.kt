package net.pettip.data.bbs

import com.google.gson.annotations.SerializedName

/**
 * @Project     : PetTip-Android
 * @FileName    : BbsCmntCreateRes
 * @Date        : 2023-11-23
 * @author      : CareBiz
 * @description : net.pettip.data.bbs
 * @see net.pettip.data.bbs.BbsCmntCreateRes
 */

data class BbsCmntCreateRes(
    @SerializedName("data")
    var data: List<BbsCmnt>?,
    @SerializedName("detailMessage")
    var detailMessage: Any?, // null
    @SerializedName("resultMessage")
    var resultMessage: String, // 일상생활 댓글 등록 완료
    @SerializedName("statusCode")
    var statusCode: Int // 200
)