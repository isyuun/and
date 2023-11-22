package kr.carepet.app.navi.viewmodel

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kr.carepet.data.RefreshRes
import kr.carepet.data.RefreshToken
import kr.carepet.data.cmm.CmmRes
import kr.carepet.data.daily.WeekData
import kr.carepet.data.daily.WeekRecordReq
import kr.carepet.data.daily.WeekRecordRes
import kr.carepet.data.pet.CurrentPetData
import kr.carepet.data.pet.CurrentPetRes
import kr.carepet.data.pet.MyPetListReq
import kr.carepet.data.pet.MyPetListRes
import kr.carepet.data.pet.PetDetailData
import kr.carepet.singleton.G
import kr.carepet.singleton.MySharedPreference
import kr.carepet.singleton.RetrofitClientServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import kotlin.coroutines.resume


class SharedViewModel:ViewModel(){

    private val _profilePet = MutableStateFlow<PetDetailData?>(null)
    val profilePet:StateFlow<PetDetailData?> = _profilePet.asStateFlow()
    fun updateProfilePet(newValue: PetDetailData?){ _profilePet.value = newValue}

    private val _pushData = MutableStateFlow<Bundle?>(null)
    val pushData:StateFlow<Bundle?> = _pushData.asStateFlow()
    fun updatePushData(newValue: Bundle?){ _pushData.value = newValue}

    private val _nickName = MutableStateFlow<String>("")
    val nickName:StateFlow<String> = _nickName.asStateFlow()
    fun updateNickName(newValue: String){ _nickName.value = newValue}

    private val _init = MutableStateFlow<Boolean>(true)
    val init:StateFlow<Boolean> = _init.asStateFlow()
    fun updateInit(newValue: Boolean){ _init.value = newValue }

    private val _moreStoryClick = MutableStateFlow<Int?>(null)
    val moreStoryClick:StateFlow<Int?> = _moreStoryClick.asStateFlow()
    fun updateMoreStoryClick(newValue: Int?){
        _moreStoryClick.value = newValue
    }

    private val _weekRecord = MutableStateFlow<WeekData?>(null)
    val weekRecord: StateFlow<WeekData?> = _weekRecord.asStateFlow()
    fun updateWeekRecord(newData: WeekData) {
        _weekRecord.value = newData
    }

    private var _petInfo = MutableStateFlow<List<PetDetailData>>(emptyList())
    var petInfo: StateFlow<List<PetDetailData>> = _petInfo.asStateFlow()
    fun updatePetInfo(newData: List<PetDetailData>) {
        _petInfo.value = newData
    }

    private var _currentPetInfo = MutableStateFlow<List<CurrentPetData>>(emptyList())
    var currentPetInfo: StateFlow<List<CurrentPetData>> = _currentPetInfo.asStateFlow()
    fun updateCurrentPetInfo(newData: List<CurrentPetData>) {
        _currentPetInfo.value = newData
    }

    private val _selectPet = MutableStateFlow<CurrentPetData?>(null)
    val selectPet: StateFlow<CurrentPetData?> = _selectPet.asStateFlow()
    fun updateSelectPet(newValue: CurrentPetData?) { _selectPet.value = newValue }

    private val _selectPetTemp = MutableStateFlow<CurrentPetData?>(null)
    val selectPetTemp: StateFlow<CurrentPetData?> = _selectPetTemp.asStateFlow()
    fun updateSelectPetTemp(newValue: CurrentPetData) { _selectPetTemp.value = newValue }

    private val _selectPetMulti = MutableStateFlow<MutableList<CurrentPetData>>(mutableListOf())
    val selectPetMulti: StateFlow<List<CurrentPetData>> = _selectPetMulti.asStateFlow()
    fun addSelectPetMulti(newValue: CurrentPetData) { _selectPetMulti.value.add(newValue) }
    fun subSelectPetMulti(newValue: CurrentPetData) { _selectPetMulti.value.remove(newValue) }

    fun parseBirthday(birthdayString: String): LocalDate? {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        return try {
            LocalDate.parse(birthdayString, formatter)
        } catch (e: Exception) {
            null // 파싱에 실패하면 null 반환 또는 에러 처리
        }
    }

    fun changeBirth(birth:String):String{
        val birthday = parseBirthday(birth)
        val currentDate = LocalDate.now() // 현재 날짜

        val period = Period.between(birthday, currentDate) // 두 날짜 간의 기간 계산

        val years = period.years // 연 차이
        val months = period.months // 월 차이

        val returnBirth = if (years > 0) {
            if (months > 0) {
                "$years 년 $months 개월"
            } else {
                "$years 년"
            }
        } else {
            "$months 개월"
        }

        return returnBirth
    }


    suspend fun loadPetInfo():Boolean{

        val apiService = RetrofitClientServer.instance

        val data = MyPetListReq(MySharedPreference.getUserId())

        val call = apiService.myPetList(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<MyPetListRes>{
                override fun onResponse(call: Call<MyPetListRes>, response: Response<MyPetListRes>) {
                    if(response.isSuccessful){
                        val body = response.body()
                        body?.petDetailData?.let {
                            if(body.petDetailData.isEmpty()){
                                _petInfo.value= arrayListOf(emptyPet)
                                //updatePetInfo(arrayListOf(emptyPet))
                            }else{
                                _petInfo.value=body.petDetailData
                                //updatePetInfo(body.petDetailData)
                            }
                            Log.d("LOG",_petInfo.value.toString())
                            continuation.resume(true)
                        }
                    }else{
                        continuation.resume(false)
                    }
                }
                override fun onFailure(call: Call<MyPetListRes>, t: Throwable) {
                    Log.d("LOG","FAIL"+t.message)
                    continuation.resume(false)
                }

            })
        }
    }

    suspend fun loadCurrentPetInfo():Boolean{

        val apiService = RetrofitClientServer.instance

        val data = MyPetListReq(MySharedPreference.getUserId())

        val call = apiService.myPetListCurrent(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object :Callback<CurrentPetRes>{
                override fun onResponse(
                    call: Call<CurrentPetRes>,
                    response: Response<CurrentPetRes>
                ) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            if(body.data.isEmpty()){
                                _currentPetInfo.value= arrayListOf(emptyCurrentPet)
                                updateCurrentPetInfo(arrayListOf(emptyCurrentPet))
                            }else{
                                _currentPetInfo.value=body.data
                                updateCurrentPetInfo(body.data)
                            }
                            continuation.resume(true)
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<CurrentPetRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

    //refresh token 던지고, 결과 Boolean으로 반환
    suspend fun sendRFToken():Boolean{
        val apiService = RetrofitClientServer.instance

        val refreshToken = kr.carepet.singleton.MySharedPreference.getRefreshToken()

        val call = apiService.sendRefreshToken(RefreshToken(refreshToken))
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<RefreshRes>{
                override fun onResponse(call: Call<RefreshRes>, response: Response<RefreshRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            if (it.statusCode==200){
                                G.accessToken = it.data.accessToken
                                G.refreshToken = it.data.refreshToken
                                G.userId = it.data.userId
                                G.userNickName = it.data.nckNm
                                G.userEmail = it.data.email

                                _nickName.value = it.data.nckNm

                                MySharedPreference.setAccessToken(it.data.accessToken)
                                MySharedPreference.setRefreshToken(it.data.refreshToken)
                                MySharedPreference.setUserId(it.data.userId)

                                continuation.resume(true)
                            }else{

                                continuation.resume(false)
                            }
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<RefreshRes>, t: Throwable) {
                    continuation.resume(false)
                }
            })
        }
    }

    suspend fun getWeekRecord(ownrPetUnqNo: String, searchDay: String):Boolean{
        val apiService = RetrofitClientServer.instance

        val data = WeekRecordReq(ownrPetUnqNo, searchDay)

        val call=apiService.getWeekRecord(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<WeekRecordRes>{
                override fun onResponse(
                    call: Call<WeekRecordRes>,
                    response: Response<WeekRecordRes>
                ) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            if (body.statusCode==200){
                                _weekRecord.value=body.data
                                continuation.resume(true)
                            }else{
                                _weekRecord.value = null
                                continuation.resume(false)
                            }
                        }
                    }else{
                        _weekRecord.value = null
                        continuation.resume(false)
                    }
                }
                override fun onFailure(call: Call<WeekRecordRes>, t: Throwable) {
                    _weekRecord.value = null
                    continuation.resume(false)
                }
            })
        }
    }



    val emptyCurrentPet = CurrentPetData(
        age = "0살",
        petNm = "펫을 등록해주세요",
        ownrPetUnqNo = "",
        petKindNm = "",
        petRprsImgAddr = "",
        sexTypNm = "모름",
        wghtVl =0.0f,
        petRelUnqNo = 0
    )

    val emptyPet = PetDetailData(
        ownrPetUnqNo = "",
        petBrthYmd = "미상",
        petInfoUnqNo = 0,
        petKindNm = "웨스트 하이랜드 화이트 테리어",
        petMngrYn = "",
        petNm = "배추",
        petRegNo = "",
        petRelCd = "",
        petRelNm = "",
        petRelUnqNo = 0,
        petRprsImgAddr = "",
        petRprsYn = "Y",
        sexTypCd = "",
        sexTypNm = "모름",
        stdgCtpvCd = "",
        stdgCtpvNm = "",
        stdgSggCd = "",
        stdgSggNm = "",
        stdgUmdCd = "",
        stdgUmdNm = "",
        wghtVl = 0.0f,
        ntrTypCd = "",
        ntrTypNm = "모름",
        endDt = "",
        mngrType = "M",
        memberList = emptyList()
    )

    fun clear(){
        _moreStoryClick.value = null
        _weekRecord.value = null
        _init.value = true
        _petInfo.value = arrayListOf(emptyPet)
        _currentPetInfo.value = arrayListOf(emptyCurrentPet)
        _selectPet.value = null
    }
}

