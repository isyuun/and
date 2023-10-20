package kr.carepet.app.navi.viewmodel

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kr.carepet.data.SCD
import kr.carepet.data.SCDLocalData
import kr.carepet.data.SggList
import kr.carepet.data.SggListRes
import kr.carepet.data.UmdList
import kr.carepet.data.UmdListRes
import kr.carepet.data.cmm.commonRes
import kr.carepet.data.pet.DeletePetReq
import kr.carepet.data.pet.MyPetResModel
import kr.carepet.data.pet.PetListData
import kr.carepet.data.pet.PetListResModel
import kr.carepet.data.user.LoginResModel
import kr.carepet.data.user.NickNameCheckRes
import kr.carepet.data.user.UserDataResponse
import kr.carepet.singleton.RetrofitClientServer
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserCreateViewModel @Inject constructor(private val scdLocalData: SCDLocalData): ViewModel() {

    val scdList : List<SCD> = scdLocalData.scd

    private val _sggList = MutableStateFlow<List<SggList>>(emptyList()) // 시군구
    val sggList: StateFlow<List<SggList>> = _sggList.asStateFlow()

    private val _umdList = MutableStateFlow<List<UmdList>>(emptyList()) // 읍면동
    val umdList: StateFlow<List<UmdList>> = _umdList.asStateFlow()
    fun updateUmdList(newValue: List<UmdList>) { _umdList.value = newValue }

    private val _selectedItem1 = MutableStateFlow(SCD("", "", "")) // 시도
    val selectedItem1: StateFlow<SCD> = _selectedItem1.asStateFlow()
    fun updateSelectedItem1(newValue: SCD) { _selectedItem1.value = newValue }

    private val _selectedItem2 = MutableStateFlow(SggList("", "")) // 시군구
    val selectedItem2: StateFlow<SggList> = _selectedItem2.asStateFlow() // state 노출
    fun updateSelectedItem2(newValue: SggList) { _selectedItem2.value = newValue }

    private val _selectedItem3 = MutableStateFlow(UmdList("", "")) // 읍면동
    val selectedItem3: StateFlow<UmdList> = _selectedItem3.asStateFlow() // state 노출
    fun updateSelectedItem3(newValue: UmdList) { _selectedItem3.value = newValue }

    // --------------   User   -----------------------
    private val _userID = MutableStateFlow<String>("")
    val userID: StateFlow<String> = _userID.asStateFlow()
    fun updateUserID(newUserID: String) { _userID.value = newUserID }

    private val _userName = MutableStateFlow<String>("")
    val userName: StateFlow<String> = _userName.asStateFlow()
    fun updateUserName(newUserName: String) { _userName.value = newUserName }

    private val _userNickName = MutableStateFlow<String>("")
    val userNickName: StateFlow<String> = _userNickName.asStateFlow()
    fun updateUserNickName(newValue: String) { _userNickName.value = newValue }

    private val _userNickNamePass = MutableStateFlow<String>("")
    val userNickNamePass: StateFlow<String> = _userNickNamePass.asStateFlow()
    fun updateUserNickNamePass(newValue: String) { _userNickNamePass.value = newValue }

    private val _userPW = MutableStateFlow<String>("")
    val userPW: StateFlow<String> = _userPW.asStateFlow()
    fun updateUserPW(newUserPW: String) { _userPW.value = newUserPW }

    private val _userPWCheck = MutableStateFlow<String>("")
    val userPWCheck: StateFlow<String> = _userPWCheck.asStateFlow()
    fun updateUserPWCheck(newUserPWCheck: String) { _userPWCheck.value = newUserPWCheck }

    private val _userPhone = MutableStateFlow<String>("")
    val userPhone: StateFlow<String> = _userPhone.asStateFlow()
    fun updateUserPhone(newUserPhone: String) { _userPhone.value = newUserPhone }

    private val _certiNum = MutableStateFlow<String>("")
    val certiNum: StateFlow<String> = _certiNum.asStateFlow()
    fun updateCertiNum(newValue: String) { _certiNum.value = newValue }

    private val _snsLogin = MutableStateFlow<String>("")
    val snsLogin: StateFlow<String> = _snsLogin.asStateFlow()
    fun updateSnsLogin(newValue: String) { _snsLogin.value = newValue }
    // --------------   User   -----------------------

    // --------------   pet    -----------------------
    private val _petDorC = MutableStateFlow("강아지") // Data 저장
    val petDorC: StateFlow<String> = _petDorC.asStateFlow() // state 노출
    fun updatePetDorC(newPetDorC: String) { _petDorC.value = newPetDorC }

    private val _petName = MutableStateFlow("") // Data 저장
    val petName: StateFlow<String> = _petName.asStateFlow() // state 노출
    fun updatePetName(newPetName: String) { _petName.value = newPetName }
    // Pet Birth
    private val _petBirth = MutableStateFlow<String>("")
    val petBirth: StateFlow<String> = _petBirth.asStateFlow()
    fun updatePetBirth(newPetBirth: String) { _petBirth.value = newPetBirth }
    // Pet BirthUnknown
    private val _petBirthUnknown = MutableStateFlow<Boolean>(false)
    val petBirthUnknown: StateFlow<Boolean> = _petBirthUnknown.asStateFlow()
    fun updatePetBirthUnknown(newPetBirthUnknown: Boolean) { _petBirthUnknown.value = newPetBirthUnknown }
    // Pet Weight
    private val _petWght = MutableStateFlow<String>("")
    val petWght: StateFlow<String> = _petWght.asStateFlow()
    fun updatePetWght(newPetWght: String) { _petWght.value = newPetWght }
    // Pet Gender
    private val _petGender = MutableStateFlow<String>("남아")
    val petGender: StateFlow<String> = _petGender.asStateFlow()
    fun updatePetGender(newPetGender: String) { _petGender.value = newPetGender }
    // pet Ntr(중성화)
    private val _petNtr = MutableStateFlow<String>("했어요")
    val petNtr: StateFlow<String> = _petNtr.asStateFlow()
    fun updatePetNtr(newPetNtr: String) { _petNtr.value = newPetNtr }

    private val _petRprsYn = MutableStateFlow<String>("Y")
    val petRprsYn:StateFlow<String> = _petRprsYn.asStateFlow()
    fun updatePetRprsYn(newValue: String){ _petRprsYn.value = newValue}

    // Pet image
    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri.asStateFlow()

    private val _imageFile = MutableStateFlow<File?>(null)
    val imageFile: StateFlow<File?> = _imageFile.asStateFlow()

    private val _sDog = MutableStateFlow<List<PetListData>>(emptyList())
    val sDog: StateFlow<List<PetListData>> = _sDog.asStateFlow()

    private val _mDog = MutableStateFlow<List<PetListData>>(emptyList())
    val mDog: StateFlow<List<PetListData>> = _mDog.asStateFlow()

    private val _lDog = MutableStateFlow<List<PetListData>>(emptyList())
    val lDog: StateFlow<List<PetListData>> = _lDog.asStateFlow()

    private val _petKind = MutableStateFlow<PetListData>(
        kr.carepet.data.pet.PetListData(
            "",
            "",
            0,
            "사이즈/품종 선택",
            ""
        )
    )
    val petKind: StateFlow<PetListData> = _petKind.asStateFlow()

    private val _year = MutableStateFlow<PickerState>(PickerState())
    val year: StateFlow<PickerState> = _year.asStateFlow()
    fun updateYear(newValue: PickerState) { _year.value = newValue }

    private val _month = MutableStateFlow<PickerState>(PickerState())
    val month: StateFlow<PickerState> = _month.asStateFlow()
    fun updateMonth(newValue: PickerState) { _month.value = newValue }

    private val _day = MutableStateFlow<PickerState>(PickerState())
    val day: StateFlow<PickerState> = _day.asStateFlow()
    fun updateDay(newValue: PickerState) { _day.value = newValue }

    private val _address = MutableStateFlow<String>("주소 선택")
    val address: StateFlow<String> = _address.asStateFlow()
    fun updateAddress(newValue: String) { _address.value = newValue }


    private val _userResponse = MutableStateFlow<UserDataResponse>(
        kr.carepet.data.user.UserDataResponse(
            null,
            null,
            null,
            null
        )
    )
    val userResponse: StateFlow<UserDataResponse> = _userResponse.asStateFlow()


    private val _myPetResModel = MutableStateFlow<MyPetResModel?>(null)
    val myPetResModel: StateFlow<MyPetResModel?> = _myPetResModel.asStateFlow()

    // 검색어를 담는 MutableStateFlow를 생성
    private val _searchText = MutableStateFlow("")
    // 검색어를 StateFlow로 노출
    val searchText = _searchText.asStateFlow()
    // --------------   pet    -----------------------


    // 검색 중인지 여부를 담는 MutableStateFlow를 생성
    private val _isSearching = MutableStateFlow(false)
    // 검색 중인지 여부를 StateFlow로 노출
    val isSearching = _isSearching.asStateFlow()




    // 전체 pets 리스트를 MutableStateFlow로 생성
    private val _pets = MutableStateFlow<List<PetListData>>(emptyList())
    // 검색어, pets 리스트의 조합을 통해 검색 결과를 생성하는 로직
    val pets = searchText
        .debounce(0L) // 검색어 입력 후 1초 동안 대기
        .onEach { _isSearching.update { true } } // 검색 중 상태로 변경
        .combine(_pets) { text, pets ->
            if(text.isBlank()) { // 검색어가 없을 경우 전체 pets를 반환
                pets
            } else {
                delay(0L) //
                // 검색어에 따라 pets를 필터링하여 반환
                val searchResult = pets.filter { doesPetMatchSearchQuery(it,text) }
                if(searchResult.isEmpty()){
                    pets
                }else{
                    searchResult
                } // 검색결과가 없을시 전체 리스트 반환(검색결과가 없을시 빈 리스트를 줄려면)
                // pets.filter { doesPetMatchSearchQuery(it,text) } if else 지우고
            }
        }
        .onEach { _isSearching.update { false } } // 검색 종료 상태로 변경
        .stateIn(
            viewModelScope, // viewModelScope를 사용하여 상태를 관리
            SharingStarted.WhileSubscribed(5000), // 5초 동안 상태를 유지
            _pets.value // 초기값으로 _pets의 현재 값을 사용
        )

    fun doesPetMatchSearchQuery(pet: PetListData, query: String): Boolean {
        return pet.petNm.contains(query, ignoreCase = true)
    }

    // 검색어가 변경될 때 호출되는 함수입니다.
    fun onSearchTextChange(text: String) {
        _searchText.value = text // 검색어를 업데이트
    }

    fun onKindClick(){
        val apiService = RetrofitClientServer.instance

        fun callPetListApi(petSize:String){
            val petData = kr.carepet.data.pet.PetListModel(petSize, "001")

            val call=apiService.petList(petData)
            call.enqueue(object : Callback<PetListResModel>{
                override fun onResponse(
                    call: Call<PetListResModel>,
                    response: Response<PetListResModel>
                ) {
                    if(response.isSuccessful){
                        var petListBody = response.body()
                        petListBody?.let {
                            // 각 통신에 대한 처리, 받아온 리스트 처리하기
                            when(petSize){
                                "001" -> _sDog.value = petListBody.data
                                "002" -> _mDog.value = petListBody.data
                                "003" -> _lDog.value = petListBody.data

                                else -> return
                            }
                            _pets.value = (_sDog.value + _mDog.value + _lDog.value).sortedBy { it.petNm }
                            Log.d("LOG", petSize+"통신완료")
                        }
                    }
                }

                override fun onFailure(call: Call<PetListResModel>, t: Throwable) {
                    Log.d("LOG", "통신실패")
                }

            })
        }

        // 소,중,대 통신 모두 진행
        callPetListApi("001")
        callPetListApi("002")
        callPetListApi("003")
    }

    fun updatePetKind(newPetKind: PetListData){
        _petKind.value = newPetKind
    }

    fun setImageUri(uri: Uri?, context: Context) {

        _imageUri.value = uri

        viewModelScope.launch {
            val contentResolver = context.contentResolver
            val fileName = "image_file_name.jpg"
            val file = File(context.filesDir, fileName)

            try {
                val inputStream = uri?.let { contentResolver.openInputStream(it) }
                val outputStream = FileOutputStream(file)
                inputStream?.copyTo(outputStream)

                outputStream.close()
                inputStream?.close()

                _imageFile.emit(file) // 변환된 File 객체를 StateFlow에 업데이트
            } catch (e: IOException) {
                e.printStackTrace()
                _imageFile.emit(null) // 변환 실패 시 null로 업데이트
            }

        }
    }


    suspend fun sendUserToServer():Boolean{
        val apiService = RetrofitClientServer.instance

        val appKey = kr.carepet.singleton.MySharedPreference.getFcmToken()
        val appOs = "001"
        val appTypNm = Build.MODEL.toString()

        val userData= kr.carepet.data.user.UserDataModel(
            appKey = appKey,
            appOs = appOs,
            appTypNm = appTypNm,
            ncknm = _userNickName.value,
            snsLogin = _snsLogin.value,
            userID = _userID.value,
            userName = _userNickName.value,
            userPW = _userPW.value
        )

        val call= apiService.sendUserToServer(userData)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<UserDataResponse>{
                override fun onResponse(
                    call: Call<UserDataResponse>,
                    response: Response<UserDataResponse>
                ) {
                    if(response.isSuccessful){
                        // 200을 제외한 code 는 detail message 를 노출하는 식으로 처리
                        val userRegistResponse =response.body()
                        userRegistResponse?.let {
                            if(it.statusCode.toString() == "200") {
                                _userResponse.value = it
                                continuation.resume(true) // 이곳만 성공(true 반환시 navigate 실행)
                            }else{
                                _userResponse.value = it
                                continuation.resume(false)
                            }
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<UserDataResponse>, t: Throwable) {

                    continuation.resume(false)
                }
            })
        }
    }

    val petCreateSuccess = MutableStateFlow<Boolean>(false)
    fun petCreate() {
        viewModelScope.launch {
            // 결과물을 날리기
            petCreateSuccess.emit(createPet())
        }
    }

    fun sggListLoad(sidoCd:String){
        val apiService = RetrofitClientServer.instance

        val call = apiService.getSggList(sidoCd)

        viewModelScope.launch {
            call.enqueue(object: Callback<SggListRes>{
                override fun onResponse(call: Call<SggListRes>, response: Response<SggListRes>) {
                    if(response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            _sggList.value = body.data
                        }
                    }
                }

                override fun onFailure(call: Call<SggListRes>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    fun umdListLoad(sggCd:String){
        val apiService = RetrofitClientServer.instance

        val data = kr.carepet.data.UmdListReq(sggCd = sggCd, sidoCd = _selectedItem1.value.cdld)

        val call = apiService.getUmdList(data)

        viewModelScope.launch {
            call.enqueue(object: Callback<UmdListRes>{
                override fun onResponse(call: Call<UmdListRes>, response: Response<UmdListRes>) {
                    if(response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            _umdList.value = body.data
                        }
                    }
                }
                override fun onFailure(call: Call<UmdListRes>, t: Throwable) {

                }

            })
        }
    }

    suspend fun nickNameCheck():Boolean{
        val apiService = RetrofitClientServer.instance

        val call = apiService.nickNameCheck(_userNickName.value)
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

    suspend fun createPet():Boolean=
        suspendCoroutine<Boolean> { continuation ->
            val apiService = RetrofitClientServer.instance

            val fileRequestBody = _imageFile.value?.asRequestBody("image/*".toMediaType()) ?: RequestBody.create("image/*".toMediaType(), ByteArray(0))
            val filePart = MultipartBody.Part.createFormData("file", _imageFile.value?.name, fileRequestBody)

            // 일단 받아올수있는 데이터, 이름,생일,종,성별,중성화,몸무게 정도 받아오기
            val petRelCd = "001".toRequestBody("text/plain".toMediaType()) // 펫 관계코드
            val petNm = petName.value.toRequestBody("text/plain".toMediaType()) //펫 이름
            val petRegNo = "Y".toRequestBody("text/plain".toMediaType()) //펫 등록번호
            val petInfoUnqNo = petKind.value.petInfoUnqNo.toString().toRequestBody("text/plain".toMediaType()) //펫 종 고유번호
            val stdgSggCd = selectedItem2.value.sggCd.toRequestBody("text/plain".toMediaType()) //시군구 코드
            val petBrthYmd = petBirth.value.toRequestBody("text/plain".toMediaType()) // 생일
            val stdgUmdCd =
                if(selectedItem3.value.umdCd!=""){
                    selectedItem3.value.umdCd.toRequestBody("text/plain".toMediaType())
                }else{
                    "1".toRequestBody("text/plain".toMediaType())
                } // 읍면동 코드
            val delYn = "N".toRequestBody("text/plain".toMediaType()) // 삭제 여부
            val petRprsYn = petRprsYn.value.toRequestBody("text/plain".toMediaType()) // 펫 대표여부
            val ntrTypCd = when(petNtr.value){
                "했어요" -> "001".toRequestBody("text/plain".toMediaType()) //중성화 코드
                "안했어요" -> "002".toRequestBody("text/plain".toMediaType()) //중성화 코드
                "모름" -> "003".toRequestBody("text/plain".toMediaType()) //중성화 코드
                else -> "003".toRequestBody("text/plain".toMediaType()) //중성화 코드
            }
            val sexTypCd = when(petGender.value){
                "남아" -> "002".toRequestBody("text/plain".toMediaType()) //중성화 코드
                "여아" -> "001".toRequestBody("text/plain".toMediaType()) //중성화 코드
                "모름" -> "003".toRequestBody("text/plain".toMediaType()) //중성화 코드
                else -> "003".toRequestBody("text/plain".toMediaType()) //중성화 코드
            }
            val petMngrYn = "Y".toRequestBody("text/plain".toMediaType()) // 펫 관리자여부
            val wghtVl = petWght.value.toFloat() // 몸무게
            val stdgCtpvCd = selectedItem1.value.cdld.toRequestBody("text/plain".toMediaType()) //시도 코드

            val response = apiService.createPet(petRelCd, petNm, petRegNo, stdgSggCd, petInfoUnqNo,petBrthYmd, delYn, stdgUmdCd, filePart, petRprsYn, ntrTypCd, sexTypCd, petMngrYn, stdgCtpvCd, wghtVl)
            response.enqueue(object : Callback<MyPetResModel>{
                override fun onResponse(call: Call<MyPetResModel>, response: Response<MyPetResModel>) {
                    if(response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            if (body.statusCode==200){
                                _myPetResModel.value = it

                                //---- 초기화 ----
                                _petKind.value = PetListData("", "", 0, "사이즈/품종 선택", "")
                                _selectedItem1.value = SCD("", "", "")
                                _selectedItem2.value = SggList("", "") // 시군구
                                _selectedItem3.value = UmdList("", "") // 읍면동
                                _sggList.value = emptyList()
                                _umdList.value = emptyList()
                                _address.value = "주소 선택"
                                _petName.value = ""
                                _year.value =PickerState()
                                _petBirth.value = ""
                                _petBirthUnknown.value = false
                                _petWght.value = ""
                                _petGender.value = "남아"
                                _petNtr.value = "했어요"
                                _imageUri.value = null
                                //---- 초기화 ----

                                continuation.resume(true)
                            }else{
                                _myPetResModel.value = it
                                continuation.resume(false)
                            }
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<MyPetResModel>, t: Throwable) {
                    continuation.resume(false)
                }
            })
        }

    suspend fun modifyPet(ownrPetUnqNo:String):Boolean{
        val apiService = RetrofitClientServer.instance

        val fileRequestBody = _imageFile.value?.asRequestBody("image/*".toMediaType()) ?: RequestBody.create("image/*".toMediaType(), ByteArray(0))
        val filePart = MultipartBody.Part.createFormData("file", _imageFile.value?.name, fileRequestBody)

        // 일단 받아올수있는 데이터, 이름,생일,종,성별,중성화,몸무게 정도 받아오기
        val ownrPetUnqNo = ownrPetUnqNo.toRequestBody("text/plain".toMediaType())
        val petRelCd = "001".toRequestBody("text/plain".toMediaType()) // 펫 관계코드
        val petNm = petName.value.toRequestBody("text/plain".toMediaType()) //펫 이름
        val petRegNo = "Y".toRequestBody("text/plain".toMediaType()) //펫 등록번호
        val petInfoUnqNo = petKind.value.petInfoUnqNo.toString().toRequestBody("text/plain".toMediaType()) //펫 종 고유번호
        val stdgSggCd = selectedItem2.value.sggCd.toRequestBody("text/plain".toMediaType()) //시군구 코드
        val petBrthYmd = petBirth.value.toRequestBody("text/plain".toMediaType()) // 생일
        val stdgUmdCd =
            if(selectedItem3.value.umdCd!=""){
                selectedItem3.value.umdCd.toRequestBody("text/plain".toMediaType())
            }else{
                "1".toRequestBody("text/plain".toMediaType())
            } // 읍면동 코드
        val delYn = "N".toRequestBody("text/plain".toMediaType()) // 삭제 여부
        val petRprsYn = petRprsYn.value.toRequestBody("text/plain".toMediaType()) // 펫 대표여부
        val ntrTypCd = when(petNtr.value){
            "했어요" -> "001".toRequestBody("text/plain".toMediaType()) //중성화 코드
            "안했어요" -> "002".toRequestBody("text/plain".toMediaType()) //중성화 코드
            "모름" -> "003".toRequestBody("text/plain".toMediaType()) //중성화 코드
            else -> "003".toRequestBody("text/plain".toMediaType()) //중성화 코드
        }
        val sexTypCd = when(petGender.value){
            "남아" -> "002".toRequestBody("text/plain".toMediaType()) //중성화 코드
            "여아" -> "001".toRequestBody("text/plain".toMediaType()) //중성화 코드
            "모름" -> "003".toRequestBody("text/plain".toMediaType()) //중성화 코드
            else -> "003".toRequestBody("text/plain".toMediaType()) //중성화 코드
        }
        val petMngrYn = "Y".toRequestBody("text/plain".toMediaType()) // 펫 관리자여부
        //val wghtVl = petWght.value.toFloat() // 몸무게
        val stdgCtpvCd = selectedItem1.value.cdld.toRequestBody("text/plain".toMediaType()) //시도 코드

        Log.d("IMAGE",filePart.toString())
        val call = apiService.modifyPet(ownrPetUnqNo, petRelCd, petNm, petRegNo, stdgSggCd, petInfoUnqNo,petBrthYmd, delYn, stdgUmdCd, filePart, petRprsYn, ntrTypCd, sexTypCd, petMngrYn, stdgCtpvCd)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object :Callback<MyPetResModel>{
                override fun onResponse(
                    call: Call<MyPetResModel>,
                    response: Response<MyPetResModel>
                ) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            if (body.statusCode==200){
                                _myPetResModel.value = it

                                //---- 초기화 ----
                                _petKind.value = PetListData("", "", 0, "사이즈/품종 선택", "")
                                _selectedItem1.value = SCD("", "", "")
                                _selectedItem2.value = SggList("", "") // 시군구
                                _selectedItem3.value = UmdList("", "") // 읍면동
                                _sggList.value = emptyList()
                                _umdList.value = emptyList()
                                _address.value = "주소 선택"
                                _petName.value = ""
                                _year.value =PickerState()
                                _petBirth.value = ""
                                _petBirthUnknown.value = false
                                _petWght.value = ""
                                _petGender.value = "남아"
                                _petNtr.value = "했어요"
                                _imageUri.value = null
                                //---- 초기화 ----

                                continuation.resume(true)
                            }else{
                                _myPetResModel.value = it
                                continuation.resume(false)
                            }
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<MyPetResModel>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

    suspend fun deletePet(ownrPetUnqNo:String):Boolean{
        val apiService = RetrofitClientServer.instance

        val data = DeletePetReq("Y",ownrPetUnqNo)

        val call = apiService.deletePet(data)
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

    suspend fun login(): Boolean {
        val apiService = RetrofitClientServer.instance

        val loginData = kr.carepet.data.user.LoginData(_userID.value, _userPW.value)

        val call = apiService.sendLoginToServer(loginData)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<LoginResModel> {
                override fun onResponse(
                    call: Call<LoginResModel>,
                    response: Response<LoginResModel>
                ) {
                    // Response 결과
                    if (response.isSuccessful) {
                        val body = response.body()
                        body?.let {
                            if (it.statusCode == 200) { // status code 200 검증
                                // 200이면 login 처리, access/refresh token G 및 shared 에 저장

                                kr.carepet.singleton.G.accessToken = it.data.accessToken
                                kr.carepet.singleton.G.refreshToken = it.data.refreshToken
                                kr.carepet.singleton.G.userId = it.data.userId
                                // shared에 저장
                                kr.carepet.singleton.MySharedPreference.setAccessToken(it.data.accessToken)
                                kr.carepet.singleton.MySharedPreference.setRefreshToken(it.data.refreshToken)
                                kr.carepet.singleton.MySharedPreference.setUserId(it.data.userId)
                                kr.carepet.singleton.MySharedPreference.setLastLoginMethod(_snsLogin.value)
                                kr.carepet.singleton.MySharedPreference.setIsLogin(true)

                                Log.d(
                                    "Token",
                                    "access: ${it.data.accessToken}, refresh: ${it.data.refreshToken}"
                                )

                                continuation.resume(true)
                            } else {
                                Log.d(
                                    "LOG",
                                    "" + body.statusCode + body.detailMessage + body.resultMessage
                                )
                                continuation.resume(false)
                            }
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<LoginResModel>, t: Throwable) {
                    continuation.resume(false)
                }
            })
        }

    }
}

class PickerState {
    var selectedItem by mutableStateOf("")
}