package net.pettip.singleton

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import net.pettip.BuildConfig
import net.pettip.data.pet.CurrentPetData

//const val BASE_URL = "http://carepet.hopto.org/'
//const val BASE_URL = "http://dev.pettip.net:8020/"
const val BASE_URL = "https://pettip.net/"

object G {
    var userName: String = ""
    var userId: String = ""
    var userNickName: String = ""
    var userEmail: String = ""

    var accessToken: String = ""
    var refreshToken: String = ""
    var inviteCode by mutableStateOf<String?>(null)

    var mapPetInfo: List<CurrentPetData> = emptyList()

    // true면 PostScreen으로 이동
    var toPost by mutableStateOf(false)
    var dupleLogin by mutableStateOf(false)

    // 푸쉬로 받은 데이터
    var pushPage by mutableStateOf<String?>(null)
    var pushSeqNo by mutableStateOf<String?>(null)
}
