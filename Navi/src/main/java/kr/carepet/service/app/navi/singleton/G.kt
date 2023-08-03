package kr.carepet.service.app.navi.singleton

object G {
    var userId:String = ""
    var userEmail:String = ""

    // 나중에 loginActivity 에서 처리하기
    var accessToken:String = ""
    var refreshToken:String = ""
}