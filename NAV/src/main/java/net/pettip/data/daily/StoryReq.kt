package net.pettip.data.daily

import com.google.gson.annotations.SerializedName

data class StoryReq(
    @SerializedName("orderType")
    var orderType: String,
    @SerializedName("page")
    var page: Int, // null
    @SerializedName("pageSize")
    var pageSize: Int, // 실시간 스토리 조회 완료
    @SerializedName("recordSize")
    var recordSize: Int, // 200
    @SerializedName("viewType")
    var viewType: String // 200
)