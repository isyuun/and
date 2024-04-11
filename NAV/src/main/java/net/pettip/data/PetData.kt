package net.pettip.data

import com.google.gson.annotations.SerializedName
import java.io.Serial
import java.io.Serializable

/**
 * @Project     : PetTip-Android
 * @FileName    : PetData
 * @Date        : 2024-04-09
 * @author      : CareBiz
 * @description : net.pettip.data
 * @see net.pettip.data.PetData
 */
data class PetData(
 @SerializedName("age")
 var age: String, // 6년13일
 @SerializedName("ownrPetUnqNo")
 var ownrPetUnqNo: String, // P20230908000005
 @SerializedName("petKindNm")
 var petKindNm: String, // 말티푸
 @SerializedName("petNm")
 var petNm: String, // 이슬
 @SerializedName("petRprsImgAddr")
 var petRprsImgAddr: String,
 @SerializedName("sexTypNm")
 var sexTypNm: String?, // 남아
 @SerializedName("wghtVl")
 var wghtVl: Float, // 5.2
 @SerializedName("petRelUnqNo")
 var petRelUnqNo: Int, // 펫 관계번호
 @SerializedName("mngrType")
 var mngrType: String // M: 관리중, I: 참여중
) : Serializable