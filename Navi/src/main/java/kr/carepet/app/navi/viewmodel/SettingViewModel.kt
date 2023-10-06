package kr.carepet.app.navi.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kr.carepet.data.pet.InviteCodeReq
import kr.carepet.data.pet.InviteCodeRes
import kr.carepet.data.pet.Pet
import kr.carepet.data.pet.PetDetailData
import kr.carepet.data.user.LogoutRes
import kr.carepet.singleton.MySharedPreference
import kr.carepet.singleton.RetrofitClientServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SettingViewModel(sharedViewModel: SharedViewModel) :ViewModel(){

    // -------------------My Screen--------------------------
    private val _endCheck = MutableStateFlow(false) // Data 저장
    val endCheck: StateFlow<Boolean> = _endCheck.asStateFlow() // state 노출
    fun updateEndCheck(newValue: Boolean) { _endCheck.value = newValue }

    val selectedPet = mutableListOf<kr.carepet.data.pet.PetDetailData>()

    private val _selectedPetSave = MutableStateFlow<List<kr.carepet.data.pet.PetDetailData>>(emptyList())
    val selectedPetSave: StateFlow<List<kr.carepet.data.pet.PetDetailData>> = _selectedPetSave.asStateFlow()

    fun updateSelectedPetSave(newValue: List<kr.carepet.data.pet.PetDetailData>): Boolean {
        _selectedPetSave.value = newValue
        Log.d("ViewModel", _selectedPetSave.value.size.toString())
        return true // 값을 업데이트하는 데 성공했음을 나타내는 불리언 값을 반환합니다.
    }

    private val _inviteCode = MutableStateFlow("") // Data 저장
    val inviteCode: StateFlow<String> = _inviteCode.asStateFlow() // state 노출
    fun updateInviteCode(newValue: String) { _inviteCode.value = newValue }



    // -------------------My Screen--------------------------


    // -----------------Setting Screen------------------------
    val inquiryKindList = arrayListOf(
        InquiryKindList("1"),
        InquiryKindList("2"),
        InquiryKindList("3")
    )

    private val _name = MutableStateFlow("") // Data 저장
    val name: StateFlow<String> = _name.asStateFlow() // state 노출
    fun updateName(newValue: String) { _name.value = newValue }

    private val _inquiryKind = MutableStateFlow("서비스 이용 문의") // Data 저장
    val inquiryKind: StateFlow<String> = _inquiryKind.asStateFlow() // state 노출
    fun updateInquiryKind(newValue: String) { _inquiryKind.value = newValue }

    private val _phoneNum = MutableStateFlow("") // Data 저장
    val phoneNum: StateFlow<String> = _phoneNum.asStateFlow() // state 노출
    fun updatePhoneNum(newValue: String) { _phoneNum.value = newValue }

    private val _email = MutableStateFlow("") // Data 저장
    val email: StateFlow<String> = _email.asStateFlow() // state 노출
    fun updateEmail(newValue: String) { _email.value = newValue }

    private val _title = MutableStateFlow("") // Data 저장
    val title: StateFlow<String> = _title.asStateFlow() // state 노출
    fun updateTitle(newValue: String) { _title.value = newValue }

    private val _inquiryMain = MutableStateFlow("") // Data 저장
    val inquiryMain: StateFlow<String> = _inquiryMain.asStateFlow() // state 노출
    fun updateInquiryMain(newValue: String) { _inquiryMain.value = newValue }

    private val _isCheck = MutableStateFlow(false) // Data 저장
    val isCheck: StateFlow<Boolean> = _isCheck.asStateFlow() // state 노출
    fun updateIsCheck(newValue: Boolean) { _isCheck.value = newValue }

    var state by mutableStateOf(MyScreenState())
        private set
    fun updateSelectedImageList(listOfImages: List<Uri>) {
        val updatedImageList = state.listOfSelectedImages.toMutableList()
        viewModelScope.launch {
            updatedImageList += listOfImages
            state = state.copy(
                listOfSelectedImages = updatedImageList.distinct()
            )
        }
    }

    fun onItemRemove(index: Int) {
        val updatedImageList = state.listOfSelectedImages.toMutableList()
        viewModelScope.launch {
            updatedImageList.removeAt(index)
            state = state.copy(
                listOfSelectedImages = updatedImageList.distinct()
            )
        }
    }
    // -----------------Setting Screen------------------------

    // -----------------UserInfo Screen------------------------
    private val _userName = MutableStateFlow("케어펫") // Data 저장
    val userName: StateFlow<String> = _userName.asStateFlow() // state 노출
    fun updateUserName(newValue: String) { _userName.value = newValue }

    private val _userNickName = MutableStateFlow("YJ22") // Data 저장
    val userNickName: StateFlow<String> = _userNickName.asStateFlow() // state 노출
    fun updateUserNickName(newValue: String) { _userNickName.value = newValue }

    private val _userPhoneNum = MutableStateFlow("01012345678") // Data 저장
    val userPhoneNum: StateFlow<String> = _userPhoneNum.asStateFlow() // state 노출
    fun updateUserPhoneNum(newValue: String) { _userPhoneNum.value = newValue }

    private val _userPw = MutableStateFlow("") // Data 저장
    val userPw: StateFlow<String> = _userPw.asStateFlow() // state 노출
    fun updateUserPw(newValue: String) { _userPw.value = newValue }

    private val _userPwCheck = MutableStateFlow("") // Data 저장
    val userPwCheck: StateFlow<String> = _userPwCheck.asStateFlow() // state 노출
    fun updateUserPwCheck(newValue: String) { _userPwCheck.value = newValue }
    // -----------------UserInfo Screen------------------------


    suspend fun logOut():Boolean{
        val apiService = RetrofitClientServer.instance

        val call = apiService.sendLogout()
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<kr.carepet.data.user.LogoutRes>{
                override fun onResponse(call: Call<kr.carepet.data.user.LogoutRes>, response: Response<kr.carepet.data.user.LogoutRes>) {
                    if(response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            if (body.statusCode==200){
                                Log.d("LOG", "로그아웃 성공")
                                kr.carepet.singleton.MySharedPreference.setIsLogin(false)
                                continuation.resume(true)
                            }
                        }
                    }
                }
                override fun onFailure(call: Call<kr.carepet.data.user.LogoutRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

    suspend fun handleKakaoLogout(): Boolean =
        suspendCoroutine { continuation ->
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e(LoginViewModel.TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                    continuation.resume(false)
                }
                else {
                    Log.i(LoginViewModel.TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                    continuation.resume(true)
                }
            }
        }

    suspend fun getInviteCode():Boolean{
        val apiService = RetrofitClientServer.instance

        val petList:List<kr.carepet.data.pet.Pet> = selectedPetSave.value.map { petDetailData ->
            kr.carepet.data.pet.Pet(
                ownrPetUnqNo = petDetailData.ownrPetUnqNo,
                petNm = petDetailData.petNm
            )
        }

        val data =
            kr.carepet.data.pet.InviteCodeReq(pet = petList, getCurrentDateTime(), "202510101010")

        val call = apiService.getInviteCode(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<kr.carepet.data.pet.InviteCodeRes>{
                override fun onResponse(
                    call: Call<kr.carepet.data.pet.InviteCodeRes>,
                    response: Response<kr.carepet.data.pet.InviteCodeRes>
                ) {
                    if(response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            updateInviteCode(body.data.invttKeyVl)
                            continuation.resume(true)
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<kr.carepet.data.pet.InviteCodeRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }

    }

    //suspend fun setInviteCode():Boolean{
    //    val apiService = RetrofitClientServer.instance
    //
    //    val call = apiService.getInviteCode(data)
    //    return suspendCancellableCoroutine { continuation ->
    //        call.enqueue(object : Callback<InviteCodeRes>{
    //            override fun onResponse(
    //                call: Call<InviteCodeRes>,
    //                response: Response<InviteCodeRes>
    //            ) {
    //                if(response.isSuccessful){
    //                    val body = response.body()
    //                    body?.let {
    //                        updateInviteCode(body.data.invttKeyVl)
    //                        continuation.resume(true)
    //                    }
    //                }else{
    //                    continuation.resume(false)
    //                }
    //            }
    //
    //            override fun onFailure(call: Call<InviteCodeRes>, t: Throwable) {
    //                continuation.resume(false)
    //            }
    //
    //        })
    //    }
    //
    //}
}

fun getCurrentDateTime(): String {
    val dateFormat = SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault())
    val currentTime = Date()
    return dateFormat.format(currentTime)
}
data class InquiryKindList(val kind:String)

data class MyScreenState(
    val listOfSelectedImages:List<Uri> = emptyList()
)
