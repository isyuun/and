package net.pettip.data.daily


import com.google.gson.annotations.SerializedName

data class test(
    @SerializedName("cmntList")
    var cmntList: List<Cmnt>
)