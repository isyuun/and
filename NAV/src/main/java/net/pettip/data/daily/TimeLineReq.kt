package net.pettip.data.daily


import com.google.gson.annotations.SerializedName

data class TimeLineReq(
    @SerializedName("ownrPetUnqNo")
    var ownrPetUnqNo: List<String>,
    @SerializedName("page")
    var page: Int, // 1
    @SerializedName("pageSize")
    var pageSize: Int, // 10
    @SerializedName("recordSize")
    var recordSize: Int, // 20
    @SerializedName("searchSort")
    var searchSort: String, // 001
    @SerializedName("searchWord")
    var searchWord: String // Option
)