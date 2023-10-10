package kr.carepet.singleton

import java.util.Date

object G {
    var userId:String = ""
    var userNickName:String = ""

    var accessToken:String = ""
    var refreshToken:String = ""

    var tokenReceivedTime: Date? = null

}

