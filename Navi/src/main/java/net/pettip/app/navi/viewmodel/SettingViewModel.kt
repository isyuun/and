package net.pettip.app.navi.viewmodel

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import net.pettip.data.CommonCodeModel
import net.pettip.data.bbs.BbsDetailRes
import net.pettip.data.bbs.File
import net.pettip.data.bbs.QnaDetailRes
import net.pettip.data.bbs.QnaReq
import net.pettip.data.bbs.QnaUpdateReq
import net.pettip.data.cmm.CdDetail
import net.pettip.data.cmm.CmmRes
import net.pettip.data.cmm.commonRes
import net.pettip.data.daily.PhotoData
import net.pettip.data.daily.PhotoRes
import net.pettip.data.pet.ChangePetWgtReq
import net.pettip.data.pet.InviteCodeReq
import net.pettip.data.pet.InviteCodeRes
import net.pettip.data.pet.Member
import net.pettip.data.pet.Pet
import net.pettip.data.pet.PetDetailData
import net.pettip.data.pet.PetDetailReq
import net.pettip.data.pet.PetDetailRes
import net.pettip.data.pet.PetWgtData
import net.pettip.data.pet.PetWgtRes
import net.pettip.data.pet.RegPetWgtReq
import net.pettip.data.pet.SetInviteCodeRes
import net.pettip.data.user.LogoutRes
import net.pettip.data.user.NickNameCheckRes
import net.pettip.data.user.RelCloseReq
import net.pettip.data.user.ResetNickNameReq
import net.pettip.data.user.ResetPwReq
import net.pettip.singleton.G
import net.pettip.singleton.MySharedPreference
import net.pettip.singleton.RetrofitClientServer
import net.pettip.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
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

    private val _profileData = MutableStateFlow<PetDetailData?>(null)
    val profileData: StateFlow<PetDetailData?> = _profileData.asStateFlow()

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

    private val _petWeightList = MutableStateFlow<List<PetWgtData>?>(emptyList())
    val petWeightList: StateFlow<List<PetWgtData>?> = _petWeightList.asStateFlow()

    private val _selectMarker = MutableStateFlow("") // Data 저장
    val selectMarker: StateFlow<String> = _selectMarker.asStateFlow() // state 노출
    fun updateSelectMarker(newValue: String) { _selectMarker.value = newValue }

    private val _regDM = MutableStateFlow<String>("")
    val regDM: StateFlow<String> = _regDM.asStateFlow()
    // -------------------PetInfo Screen---------------------

    // -----------------Setting Screen------------------------
    private val _inquiryKindList = MutableStateFlow<CmmRes?>(null)
    val inquiryKindList: StateFlow<CmmRes?> = _inquiryKindList.asStateFlow() // state 노출

    private val _inquiryKind = MutableStateFlow<CdDetail?>(null) // Data 저장
    val inquiryKind: StateFlow<CdDetail?> = _inquiryKind.asStateFlow() // state 노출
    fun updateInquiryKind(newValue: CdDetail?) { _inquiryKind.value = newValue }

    private val _title = MutableStateFlow("") // Data 저장
    val title: StateFlow<String> = _title.asStateFlow() // state 노출
    fun updateTitle(newValue: String) { _title.value = newValue }

    private val _inquiryMain = MutableStateFlow("") // Data 저장
    val inquiryMain: StateFlow<String> = _inquiryMain.asStateFlow() // state 노출
    fun updateInquiryMain(newValue: String) { _inquiryMain.value = newValue }

    private val _isCheck = MutableStateFlow(false) // Data 저장
    val isCheck: StateFlow<Boolean> = _isCheck.asStateFlow() // state 노출
    fun updateIsCheck(newValue: Boolean) { _isCheck.value = newValue }

    // 이미 등록된 사진 리스트
    private val _uploadedFileList = MutableStateFlow<List<File>?>(null)
    val uploadedFileList:StateFlow<List<File>?> = _uploadedFileList.asStateFlow()
    fun updateUploadedFileList(newValue: List<File>?){
        _uploadedFileList.value = newValue
    }
    fun subUploadedFileList(newValue:Uri){
        val fileName = getLastSegmentAfterSlash(newValue.toString())
        _uploadedFileList.value = (_uploadedFileList.value?.map { file ->
            if (file.atchFileNm == fileName) {
                file.copy(rowState = "D")
            } else {
                file
            }
        } ?: emptyList()) as MutableList<File>?

        Log.d("LOG",_uploadedFileList.value.toString())
    }

    private val _newFileList = MutableStateFlow<List<File>?>(null)
    val newFileList:StateFlow<List<File>?> = _newFileList.asStateFlow()
    fun clearNewFileList(){_newFileList.value = null}

    private val _detailMessage = MutableStateFlow<String?>(null) // Data 저장
    val detailMessage: StateFlow<String?> = _detailMessage.asStateFlow() // state 노출
    fun updateDetailMessage(){_detailMessage.value = null}

    private val _photoRes = MutableStateFlow<List<PhotoData>?>(emptyList())
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
    fun clearSelectedImages() {
        state = state.copy(listOfSelectedImages = emptyList())
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

    private val _appKey = MutableStateFlow<String>("")
    val appKey: StateFlow<String> = _appKey.asStateFlow() // state 노출
    fun updateAppKey() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                android.util.Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            _appKey.value = task.result
        })
    }

    suspend fun getCmmList(cmmCdData:String):Boolean{
        val apiService = RetrofitClientServer.instance

        val data = CommonCodeModel(cmmCdData)

        val call = apiService.getCmmList(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object :Callback<CmmRes>{
                override fun onResponse(call: Call<CmmRes>, response: Response<CmmRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            _inquiryKindList.value = it
                            continuation.resume(true)
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<CmmRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

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
                            _detailMessage.value = body.resultMessage
                            continuation.resume(true)
                        }else{
                            _detailMessage.value = body?.detailMessage ?: ""
                            continuation.resume(false)
                        }
                    }else{
                        val errorBodyString = response.errorBody()!!.string()
                        _detailMessage.value = errorBodyParse(errorBodyString)
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
                                _memberList.value = it.petDetailData.memberList
                                _profileData.value = it.petDetailData
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
                                _regDM.value = it.detailMessage
                                Log.d("LOG",_regDM.value)
                                continuation.resume(false)
                            }
                        }
                    }else{
                        val errorBodyString = response.errorBody()?.string()

                        _regDM.value = errorBodyParse(errorBodyString)

                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<commonRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

    suspend fun getPetWgt(ownrPetUnqNo:String):Boolean{
        val apiService = RetrofitClientServer.instance

        val call = apiService.getPetWgt(ownrPetUnqNo)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<PetWgtRes>{
                override fun onResponse(call: Call<PetWgtRes>, response: Response<PetWgtRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            if (body.data != null){
                                _petWeightList.value = it.data
                                continuation.resume(true)
                            }else{
                                _petWeightList.value = emptyList()
                                continuation.resume(true)
                            }
                        }
                    }else{
                        _petWeightList.value = emptyList()
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<PetWgtRes>, t: Throwable) {
                    _petWeightList.value = emptyList()
                    continuation.resume(false)
                }

            })
        }
    }

    suspend fun changePetWgt(crtrYmd:String, petDtlUnqNo:Int, wghtVl:Float):Boolean{
        val apiService = RetrofitClientServer.instance

        val cleanedString = crtrYmd.replace(".", "")
        val dateInt = cleanedString.toInt()

        val data = ChangePetWgtReq(dateInt, petDtlUnqNo, wghtVl)

        val call = apiService.changePetWgt(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<commonRes>{
                override fun onResponse(call: Call<commonRes>, response: Response<commonRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            continuation.resume(true)
                        }
                    }else{
                        val errorBodyString = response.errorBody()!!.string()

                        _regDM.value = errorBodyParse(errorBodyString)
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<commonRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

    suspend fun deletePetWgt(petDtlUnqNo:Int):Boolean{
        val apiService = RetrofitClientServer.instance

        val call = apiService.deletePetWgt(petDtlUnqNo)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<commonRes>{
                override fun onResponse(call: Call<commonRes>, response: Response<commonRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            continuation.resume(true)
                        }
                    }else{
                        val errorBodyString = response.errorBody()!!.string()

                        _regDM.value = errorBodyParse(errorBodyString)
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<commonRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

    suspend fun qnaCreate():Boolean{
        val apiService = RetrofitClientServer.instance

        val data = QnaReq(
            bbsSn = 10,
            files = _photoRes.value,
            pstCn = _inquiryMain.value,
            pstSeCd = _inquiryKind.value?.cdId ?: "",
            pstTtl = _title.value
        )

        val call = apiService.createQna(data = data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<BbsDetailRes>{
                override fun onResponse(call: Call<BbsDetailRes>, response: Response<BbsDetailRes>) {
                    if (response.isSuccessful){
                        continuation.resume(true)
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<BbsDetailRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

    suspend fun fileUpload(context: Context): Boolean {
        val apiService = RetrofitClientServer.instance

        val parts = ArrayList<MultipartBody.Part>()

        val maxImages = 5

        for (i in 0 until Integer.min(maxImages, state.listOfSelectedImages.size - 1)) {
            val fileUri = state.listOfSelectedImages[i]

            val resizedFile = resizeImage(context, fileUri , i)

            resizedFile?.let {
                val requestBody = it.asRequestBody("image/*".toMediaType())
                val part = MultipartBody.Part.createFormData("files", "image$i.jpg", requestBody)
                parts.add(part)
            }
        }

        val call = apiService.uploadPhotoInBbs(parts)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<PhotoRes> {
                override fun onResponse(call: Call<PhotoRes>, response: Response<PhotoRes>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        body?.let {
                            _photoRes.value = it.data
                            continuation.resume(true)
                        }
                    } else {
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<PhotoRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

    suspend fun updateQna(modifiedQna: QnaDetailRes, viewModel: CommunityViewModel):Boolean{
        val apiService = RetrofitClientServer.instance

        concatUploadedFileList(_newFileList.value?: emptyList())

        val data = QnaUpdateReq(
            pstSn = modifiedQna.data[0].pstSn,
            pstTtl = _title.value,
            pstCn = _inquiryMain.value,
            pstSeCd = _inquiryKind.value?.cdId ?: "",
            files = _uploadedFileList.value?: emptyList()
        )

        val call =apiService.updateQna(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<QnaDetailRes>{
                override fun onResponse(call: Call<QnaDetailRes>, response: Response<QnaDetailRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            viewModel.updateQnaDetail(it)
                            continuation.resume(true)
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<QnaDetailRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

    suspend fun fileUploadModify(context: Context): Boolean {
        val apiService = RetrofitClientServer.instance

        val parts = ArrayList<MultipartBody.Part>()

        val maxImages = 5

        val localUriList = state.listOfSelectedImages.filter { uri ->
            uri.scheme != "http" && uri.scheme != "https"
        }

        for (i in 0 until Integer.min(maxImages, localUriList.size - 1)) {
            val fileUri = localUriList[i]

            val resizedFile = resizeImage(context, fileUri , i)

            resizedFile?.let {
                val requestBody = it.asRequestBody("image/*".toMediaType())
                val part = MultipartBody.Part.createFormData("files", "image$i.jpg", requestBody)
                parts.add(part)
            }
        }

        val call = apiService.uploadPhotoInBbs(parts)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<PhotoRes> {
                override fun onResponse(call: Call<PhotoRes>, response: Response<PhotoRes>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        body?.let {
                            val qnaFileList = body.data.map { photoData ->
                                File(
                                    atchFileSn = null,
                                    atchFileNm = photoData.atchFileNm,
                                    atchFileSz = photoData.atchFileSz,
                                    orgnlAtchFileNm = photoData.orgnlAtchFileNm,
                                    fileExtnNm = photoData.fileExtnNm,
                                    filePathNm = photoData.filePathNm,
                                    rowState = "C",
                                    pstSn = null,
                                    pstRprsYn = null
                                )
                            }
                            _newFileList.value = qnaFileList
                            continuation.resume(true)
                        }
                    } else {
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<PhotoRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

    fun concatUploadedFileList(newFiles: List<File>) {
        _uploadedFileList.value?.let { currentList ->
            val combinedList = mutableListOf<File>().apply {
                addAll(currentList)
                addAll(newFiles)
            }
            _uploadedFileList.value = combinedList
        }
    }

}



fun getDateTime(date: Date): String {
    val dateFormat = SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault())
    return dateFormat.format(date)
}

data class MyScreenState(
    val listOfSelectedImages:List<Uri> = emptyList()
)

fun errorBodyParse(errorBodyString: String?):String{
    if (errorBodyString != null) {
        // errorBodyString은 JSON 형식으로 보이므로 파싱하여 detailMessage를 얻을 수 있습니다.
        val json = JSONObject(errorBodyString)
        val detailMessage = json.optString("detailMessage")

        return if (detailMessage.isNotEmpty()) {
            detailMessage
        } else {
            "통신에 실패했습니다"
        }
    }

    return "통신에 실패했습니다"
}

fun getLastSegmentAfterSlash(inputString: String): String {
    val segments = inputString.split('/')
    return segments.last()
}