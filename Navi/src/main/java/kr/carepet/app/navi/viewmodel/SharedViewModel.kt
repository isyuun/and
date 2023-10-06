package kr.carepet.app.navi.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kr.carepet.data.RefreshRes
import kr.carepet.data.RefreshToken
import kr.carepet.data.daily.WeekData
import kr.carepet.data.daily.WeekRecordReq
import kr.carepet.data.daily.WeekRecordRes
import kr.carepet.data.pet.MyPetListReq
import kr.carepet.data.pet.MyPetListRes
import kr.carepet.data.pet.PetDetailData
import kr.carepet.singleton.G
import kr.carepet.singleton.MySharedPreference
import kr.carepet.singleton.RetrofitClientServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Duration
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Date
import kotlin.coroutines.resume


class SharedViewModel:ViewModel(){

    private val _weekRecord = MutableStateFlow<WeekData?>(null)
    val weekRecord: StateFlow<WeekData?> = _weekRecord.asStateFlow()
    fun updateWeekRecord(newData: WeekData) {
        _weekRecord.value = newData
    }

    private val _petInfo = MutableStateFlow<List<PetDetailData>>(emptyList())
    val petInfo: StateFlow<List<PetDetailData>> = _petInfo.asStateFlow()
    fun updatePetInfo(newData: List<PetDetailData>) {
        _petInfo.value = newData
    }

    private val _selectPet = MutableStateFlow<PetDetailData?>(null)
    val selectPet: StateFlow<PetDetailData?> = _selectPet.asStateFlow()
    fun updateSelectPet(newValue: PetDetailData) { _selectPet.value = newValue }


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
                        body?.let {
                            if(body.petDetailData.isEmpty()){
                                _petInfo.value= arrayListOf(emptyPet)
                                updatePetInfo(arrayListOf(emptyPet))
                            }else{
                                _petInfo.value=body.petDetailData
                                updatePetInfo(body.petDetailData)
                            }
                            Log.d("LOG",_petInfo.value.toString())
                            continuation.resume(false)
                        }
                    }
                }
                override fun onFailure(call: Call<MyPetListRes>, t: Throwable) {
                    Log.d("LOG","FAIL"+t.message)
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
                                kr.carepet.singleton.G.accessToken = it.data.accessToken
                                kr.carepet.singleton.G.refreshToken = it.data.refreshToken
                                kr.carepet.singleton.G.userId = it.data.userId

                                kr.carepet.singleton.MySharedPreference.setAccessToken(it.data.accessToken)
                                kr.carepet.singleton.MySharedPreference.setRefreshToken(it.data.refreshToken)
                                kr.carepet.singleton.MySharedPreference.setUserId(it.data.userId)
                                Log.d(
                                    "Token",
                                    "access: ${it.data.accessToken}, refresh: ${it.data.refreshToken}"
                                )
                                continuation.resume(true)
                            }else{
                                continuation.resume(false)
                            }
                        }
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

        val data = kr.carepet.data.daily.WeekRecordReq(ownrPetUnqNo, searchDay)

        val call=apiService.getWeekRecord(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<WeekRecordRes>{
                override fun onResponse(
                    call: Call<WeekRecordRes>,
                    response: Response<WeekRecordRes>
                ) {
                    val body = response.body()
                    body?.let {
                        if (body.statusCode==200){
                            _weekRecord.value=body.data
                            continuation.resume(true)
                        }else{
                            continuation.resume(false)
                        }
                    }
                }
                override fun onFailure(call: Call<WeekRecordRes>, t: Throwable) {
                    continuation.resume(false)
                }
            })
        }
    }

    val emptyPet = kr.carepet.data.pet.PetDetailData(
        ownrPetUnqNo = "",
        petBrthYmd = "",
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
        sexTypNm = "수컷",
        stdgCtpvCd = "",
        stdgCtpvNm = "",
        stdgSggCd = "",
        stdgSggNm = "",
        stdgUmdCd = "",
        stdgUmdNm = "",
        wghtVl = 0.0f,
        ntrTypCd = "",
        ntrTypNm = "",
        endDt = "",
        mngrType = "",
        memberList = emptyList()
    )
}

