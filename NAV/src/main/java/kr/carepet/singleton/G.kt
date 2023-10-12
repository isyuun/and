package kr.carepet.singleton

import kr.carepet.data.pet.CurrentPetData
import java.util.Date

object G {
    var userId:String = ""
    var userNickName:String = ""
    var userEmail:String = ""

    var accessToken:String = ""
    var refreshToken:String = ""

    var mapPetInfo:List<CurrentPetData> = emptyList()

    // true면 PostScreen으로 이동
    var toPost:Boolean = false
}

