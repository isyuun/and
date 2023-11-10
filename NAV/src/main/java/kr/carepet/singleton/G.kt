package kr.carepet.singleton

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kr.carepet.data.pet.CurrentPetData

object G {
    var userName: String = ""
    var userId: String = ""
    var userNickName: String = ""
    var userEmail: String = ""

    var accessToken: String = ""
    var refreshToken: String = ""

    var mapPetInfo: List<CurrentPetData> = emptyList()

    // true면 PostScreen으로 이동
    var toPost by mutableStateOf(false)
}
