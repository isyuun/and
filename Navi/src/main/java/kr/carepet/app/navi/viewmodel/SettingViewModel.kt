package kr.carepet.app.navi.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kr.carepet.data.cmm.commonRes
import kr.carepet.data.pet.InviteCodeReq
import kr.carepet.data.pet.InviteCodeRes
import kr.carepet.data.pet.Member
import kr.carepet.data.pet.Pet
import kr.carepet.data.pet.PetDetailData
import kr.carepet.data.pet.PetDetailReq
import kr.carepet.data.pet.PetDetailRes
import kr.carepet.data.pet.RegPetWgtReq
import kr.carepet.data.pet.SetInviteCodeRes
import kr.carepet.data.user.BbsFaq
import kr.carepet.data.user.BbsReq
import kr.carepet.data.user.FAQData
import kr.carepet.data.user.FAQRes
import kr.carepet.data.user.LogoutRes
import kr.carepet.data.user.NickNameCheckRes
import kr.carepet.data.user.QnaData
import kr.carepet.data.user.QnaReq
import kr.carepet.data.user.QnaRes
import kr.carepet.data.user.RelCloseReq
import kr.carepet.data.user.ResetNickNameReq
import kr.carepet.data.user.ResetPwReq
import kr.carepet.singleton.G
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

class SettingViewModel(private val sharedViewModel: SharedViewModel) :ViewModel(){

    fun updatePetInfo(){
        sharedViewModel.viewModelScope.launch { sharedViewModel.loadPetInfo() }
    }

    fun updateCurrentPetInfo(){
        sharedViewModel.viewModelScope.launch { sharedViewModel.loadCurrentPetInfo() }
    }

    // -------------------My Screen--------------------------
    private val _endCheck = MutableStateFlow(false) // Data 저장
    val endCheck: StateFlow<Boolean> = _endCheck.asStateFlow() // state 노출
    fun updateEndCheck(newValue: Boolean) { _endCheck.value = newValue }

    val selectedPet = mutableListOf<PetDetailData>()

    private val _selectedPetSave = MutableStateFlow<List<PetDetailData>>(emptyList())
    val selectedPetSave: StateFlow<List<PetDetailData>> = _selectedPetSave.asStateFlow()
    fun updateSelectedPetSave(newValue: List<PetDetailData>): Boolean {
        _selectedPetSave.value = newValue
        return true // 값을 업데이트하는 데 성공했음을 나타내는 불리언 값을 반환합니다.
    }

    private val _otpValue = MutableStateFlow("") // Data 저장
    val otpValue: StateFlow<String> = _otpValue.asStateFlow() // state 노출
    fun updateOtpValue(newValue: String) { _otpValue.value = newValue }

    private val _inviteCode = MutableStateFlow("") // Data 저장
    val inviteCode: StateFlow<String> = _inviteCode.asStateFlow() // state 노출
    fun updateInviteCode(newValue: String) { _inviteCode.value = newValue }

    private val _selectedDate = MutableStateFlow("") // Data 저장
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow() // state 노출
    fun updateSelectedDate(newValue: String) { _selectedDate.value = newValue }

    private val _selectedTime = MutableStateFlow("") // Data 저장
    val selectedTime: StateFlow<String> = _selectedTime.asStateFlow() // state 노출
    fun updateSelectedTime(newValue: String) { _selectedTime.value = newValue }
    // -------------------My Screen--------------------------

    // -------------------PetInfo Screen---------------------

    private val _memberList = MutableStateFlow<List<Member>?>(null)
    val memberList: StateFlow<List<Member>?> = _memberList.asStateFlow()
    fun updateMemberList(newValue: List<Member>?){
        _memberList.value = newValue
    }

    private val _petWeight = MutableStateFlow<String>("")
    val petWeight: StateFlow<String> = _petWeight.asStateFlow()
    fun updatePetWeight(newValue: String){_petWeight.value = newValue}

    private val _petWeightRgDate = MutableStateFlow<String>("")
    val petWeightRgDate: StateFlow<String> = _petWeightRgDate.asStateFlow()
    fun updatePetWeightRgDate(newValue: String){_petWeightRgDate.value = newValue}


    // -------------------PetInfo Screen---------------------

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

    private val _detailMessage = MutableStateFlow("") // Data 저장
    val detailMessage: StateFlow<String> = _detailMessage.asStateFlow() // state 노출

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
    private val _userNickNamePass = MutableStateFlow("") // Data 저장
    val userNickNamePass: StateFlow<String> = _userNickNamePass.asStateFlow() // state 노출
    fun updateUserNickNamePass(newValue: String) { _userNickNamePass.value = newValue }

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


    // ----------------- 게시판 ------------------------
    private val _faqData = MutableStateFlow<FAQData?>(null)
    val faqData: StateFlow<FAQData?> = _faqData.asStateFlow()

    private val _qnaData = MutableStateFlow<QnaRes?>(null)
    val qnaData: StateFlow<QnaRes?> = _qnaData.asStateFlow()
    // ----------------- 게시판 ------------------------
    suspend fun logOut():Boolean{
        val apiService = RetrofitClientServer.instance

        val call = apiService.sendLogout()
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<LogoutRes>{
                override fun onResponse(call: Call<LogoutRes>, response: Response<LogoutRes>) {
                    if(response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            if (body.statusCode==200){
                                sharedViewModel.clear()
                                MySharedPreference.setIsLogin(false)
                                continuation.resume(true)
                            }else{
                                continuation.resume(false)
                            }
                        }
                    }else{
                        sharedViewModel.clear()
                        MySharedPreference.setIsLogin(false)
                        continuation.resume(true)
                    }
                }
                override fun onFailure(call: Call<LogoutRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

    suspend fun withdraw():Boolean{
        val apiService = RetrofitClientServer.instance

        val call = apiService.withdraw()
        return suspendCancellableCoroutine {  continuation ->
            call.enqueue(object : Callback<commonRes>{
                override fun onResponse(call: Call<commonRes>, response: Response<commonRes>) {
                    if(response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            if (body.statusCode == 200){
                                sharedViewModel.clear()
                                MySharedPreference.setIsLogin(false)
                                continuation.resume(true)
                            }else{
                                continuation.resume(false)
                            }
                        }
                    }else{
                        continuation.resume(false)
                    }
                }
                override fun onFailure(call: Call<commonRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

    suspend fun handleKakaoLogout(): Boolean =
        suspendCoroutine { continuation ->
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    //Log.e(LoginViewModel.TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                    continuation.resume(false)
                }
                else {
                    //Log.i(LoginViewModel.TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                    continuation.resume(true)
                }
            }
        }

    suspend fun getInviteCode():Boolean{
        val apiService = RetrofitClientServer.instance

        val petList:List<Pet> = selectedPetSave.value.map { currentPetData ->
            Pet(
                ownrPetUnqNo = currentPetData.ownrPetUnqNo,
                petNm = currentPetData.petNm
            )
        }

        val relEndDt = if (_endCheck.value){
            _selectedDate.value+_selectedTime.value
        }else{
            "299912311159"
        }

        val data = InviteCodeReq(pet = petList, getDateTime(Date()), relEndDt)

        val call = apiService.getInviteCode(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<InviteCodeRes>{
                override fun onResponse(
                    call: Call<InviteCodeRes>,
                    response: Response<InviteCodeRes>
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

                override fun onFailure(call: Call<InviteCodeRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }

    }

    suspend fun setInviteCode():Boolean{
        val apiService = RetrofitClientServer.instance

        val call = apiService.setInviteCode(_otpValue.value.uppercase())
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<SetInviteCodeRes>{
                override fun onResponse(
                    call: Call<SetInviteCodeRes>,
                    response: Response<SetInviteCodeRes>
                ) {
                    if(response.isSuccessful){
                        val body = response.body()
                        if (body?.statusCode == 200){
                            _detailMessage.value = body.detailMessage
                            continuation.resume(true)
                        }else{
                            _detailMessage.value = body?.detailMessage.toString()
                            continuation.resume(false)
                        }
                    }else{
                        _detailMessage.value = response.body()?.detailMessage.toString()
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<SetInviteCodeRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }

    }

    suspend fun nickNameCheck(nickName : String):Boolean{
        val apiService = RetrofitClientServer.instance

        val call = apiService.nickNameCheck(nickName)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<NickNameCheckRes>{
                override fun onResponse(
                    call: Call<NickNameCheckRes>,
                    response: Response<NickNameCheckRes>
                ) {
                    if (response.isSuccessful){
                        val body = response.body()
                        if (body?.statusCode == 200){
                            continuation.resume(true)
                        }else{
                            continuation.resume(false)
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<NickNameCheckRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

    suspend fun resetNickName():Boolean{
        val apiService = RetrofitClientServer.instance

        val data = ResetNickNameReq(_userNickNamePass.value, G.userId)

        val call = apiService.resetNickName(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object :Callback<commonRes>{
                override fun onResponse(call: Call<commonRes>, response: Response<commonRes>) {
                    if(response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            if (body.statusCode == 200){
                                G.userNickName = _userNickNamePass.value
                                continuation.resume(true)
                            }else{
                                continuation.resume(false)
                            }
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<commonRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

    suspend fun resetPw():Boolean{
        val apiService = RetrofitClientServer.instance

        val data = ResetPwReq(G.userEmail, _userPw.value)

        val call = apiService.resetPw(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object :Callback<commonRes>{
                override fun onResponse(call: Call<commonRes>, response: Response<commonRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            if (body.statusCode == 200){
                                continuation.resume(true)
                            }else{
                                continuation.resume(false)
                            }
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<commonRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

    suspend fun getPetInfoDetail(petDetailData: PetDetailData):Boolean{
        val apiService = RetrofitClientServer.instance

        val data = PetDetailReq(
            ownrPetUnqNo = petDetailData.ownrPetUnqNo,
            petRprsYn = petDetailData.petRprsYn,
            userId = G.userId
        )

        val call = apiService.myPetDetail(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<PetDetailRes>{
                override fun onResponse(
                    call: Call<PetDetailRes>,
                    response: Response<PetDetailRes>
                ) {
                    if(response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            if (body.petDetailData != null){
                                _memberList.value = body.petDetailData.memberList
                                continuation.resume(true)
                            }else{
                                _memberList.value = emptyList()
                                continuation.resume(true)
                            }
                        }
                    }else{
                        _memberList.value = emptyList()
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<PetDetailRes>, t: Throwable) {
                    _memberList.value = emptyList()
                    continuation.resume(false)
                }

            })

        }
    }

    suspend fun relClose(ownrPetUnqNo:String, petRelUnqNo:Int):Boolean{
        val apiService = RetrofitClientServer.instance

        val data = RelCloseReq(ownrPetUnqNo, petRelUnqNo)

        val call = apiService.relClose(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<commonRes>{
                override fun onResponse(call: Call<commonRes>, response: Response<commonRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            if (body.statusCode == 200){
                                updatePetInfo()
                                updateCurrentPetInfo()
                                continuation.resume(true)
                            }else{
                                continuation.resume(false)
                            }
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<commonRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

    // 게시판
    suspend fun getFaqList(page:Int):Boolean{
        val apiService = RetrofitClientServer.instance

        val data = BbsReq(8, page, 10, 20)

        val call = apiService.getFaqList(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<FAQRes>{
                override fun onResponse(call: Call<FAQRes>, response: Response<FAQRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            _faqData.value = body.data
                            continuation.resume(true)
                        }
                    }else{
                        _faqData.value = null
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<FAQRes>, t: Throwable) {
                    _faqData.value = null
                    continuation.resume(false)
                }
            })
        }
    }

    suspend fun getQnaList(page: Int):Boolean{
        val apiService = RetrofitClientServer.instance

        val data = QnaReq(10, page, 10, 20, G.userId)

        val call = apiService.getQnaList(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<QnaRes>{
                override fun onResponse(call: Call<QnaRes>, response: Response<QnaRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            _qnaData.value = it
                            continuation.resume(true)
                        }
                    }else{
                        _qnaData.value = response.body()
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<QnaRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

    suspend fun regPetWgt(ownrPetUnqNo: String):Boolean{
        val apiService = RetrofitClientServer.instance

        val data = RegPetWgtReq(_petWeightRgDate.value.toInt(), ownrPetUnqNo, _petWeight.value.toFloat())

        val call = apiService.regPetWgt(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object :Callback<commonRes>{
                override fun onResponse(call: Call<commonRes>, response: Response<commonRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            if (body.statusCode ==200){
                                continuation.resume(true)
                            }else{
                                continuation.resume(false)
                            }
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<commonRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

}

fun getDateTime(date: Date): String {
    val dateFormat = SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault())
    return dateFormat.format(date)
}
data class InquiryKindList(val kind:String)

data class MyScreenState(
    val listOfSelectedImages:List<Uri> = emptyList()
)
