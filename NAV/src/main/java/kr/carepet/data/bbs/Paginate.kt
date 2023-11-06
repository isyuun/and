package kr.carepet.data.bbs


import com.google.gson.annotations.SerializedName

data class Paginate(
    @SerializedName("endPage")
    var endPage: Int, // 1
    @SerializedName("existNextPage")
    var existNextPage: Boolean, // false
    @SerializedName("existPrevPage")
    var existPrevPage: Boolean, // false
    @SerializedName("startPage")
    var startPage: Int, // 1
    @SerializedName("totalPageCount")
    var totalPageCount: Int, // 1
    @SerializedName("totalRecordCount")
    var totalRecordCount: Int // 6
)