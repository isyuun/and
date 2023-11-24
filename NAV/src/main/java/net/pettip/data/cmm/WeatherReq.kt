package net.pettip.data.cmm


import com.google.gson.annotations.SerializedName

data class WeatherReq(
    @SerializedName("lat")
    var lat: Double, // 37.54670907248659
    @SerializedName("lon")
    var lon: Double // 127.06530754613844
)