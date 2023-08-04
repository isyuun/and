package kr.carepet.service.app.navi.model


import com.google.gson.annotations.SerializedName

data class PetModel(
    @SerializedName("petBrthYmd")
    var petBrthYmd: String, // 20230731
    @SerializedName("petInfoUnqNo")
    var petInfoUnqNo: Int, // 141
    @SerializedName("petNm")
    var petNm: String, // 강아지
    @SerializedName("petRelCd")
    var petRelCd: String, // 001
    @SerializedName("petRprsYn")
    var petRprsYn: String, // Y
    @SerializedName("stdgCtpvCd")
    var stdgCtpvCd: Int, // 11
    @SerializedName("stdgEmdCd")
    var stdgEmdCd: Int, // 110
    @SerializedName("stdgSggCd")
    var stdgSggCd: Int // 101
)