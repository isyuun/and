package kr.carepet.singleton

import kr.carepet.data.pet.CurrentPetData
import java.util.Date

object G {
    var userId:String = ""
    var userNickName:String = ""

    var accessToken:String = ""
    var refreshToken:String = ""

    var tokenReceivedTime: Date? = null

    var mapPetInfo:List<CurrentPetData> = emptyList()

}

