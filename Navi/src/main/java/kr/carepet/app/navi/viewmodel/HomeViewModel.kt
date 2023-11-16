package kr.carepet.app.navi.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kr.carepet.data.cmm.WeatherReq
import kr.carepet.data.cmm.WeatherRes
import kr.carepet.data.daily.RTStoryListRes
import kr.carepet.data.daily.WeekData
import kr.carepet.data.pet.CurrentPetData
import kr.carepet.data.pet.PetDetailData
import kr.carepet.singleton.RetrofitClientServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume

class HomeViewModel(private val sharedViewModel: SharedViewModel):ViewModel() {

    val weekRecord: StateFlow<WeekData?> = sharedViewModel.weekRecord
    fun updatePetInfo(newData: List<PetDetailData>){
        sharedViewModel.updatePetInfo(newData)
    }

    fun updateCurrentPetInfo(newData: List<CurrentPetData>){
        sharedViewModel.updateCurrentPetInfo(newData)
    }

    fun updateSeletedPet(newData: CurrentPetData){
        sharedViewModel.updateSelectPet(newData)
    }

    suspend fun getWeekRecord(ownrPetUnqNo: String, searchDay: String):Boolean{
        return sharedViewModel.getWeekRecord(ownrPetUnqNo, searchDay)
    }

    fun changeBirth(birth:String):String{
        return sharedViewModel.changeBirth(birth)
    }

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

    val emptyCurrentPet = CurrentPetData(
        age = "0살",
        petNm = "펫을 등록해주세요",
        ownrPetUnqNo = "",
        petKindNm = "",
        petRprsImgAddr = "",
        sexTypNm = "",
        wghtVl =0.0f,
        petRelUnqNo = 0
    )

    private val _showBottomSheet = MutableStateFlow<Boolean>(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet.asStateFlow()
    fun updateShowBottomSheet(newValue: Boolean) { _showBottomSheet.value = newValue }

    val currentPetInfo = sharedViewModel.currentPetInfo

    val petInfo = sharedViewModel.petInfo

    private val _rtStoryList = MutableStateFlow<RTStoryListRes?>(null)
    val rtStoryList: StateFlow<RTStoryListRes?> = _rtStoryList.asStateFlow()

    private val _repPet = MutableStateFlow<List<PetDetailData>>(emptyList())
    val repPet: StateFlow<List<PetDetailData>> = _repPet.asStateFlow()

    val isLoading = MutableStateFlow<Boolean>(true)
    fun updateIsLoading(newValue: Boolean){isLoading.value = newValue}

    private val _selectPetManage = MutableStateFlow<PetDetailData?>(null)
    val selectPetManage: StateFlow<PetDetailData?> = _selectPetManage.asStateFlow()
    fun updateSelectPetManage(newValue: PetDetailData?) { _selectPetManage.value = newValue }

    private val _petListSelectIndex = MutableStateFlow("0")
    val petListSelectIndex: StateFlow<String> = _petListSelectIndex.asStateFlow()
    fun updatePetListSelectIndex(newValue: String) { _petListSelectIndex.value = newValue }

    private val _weatherReq= MutableStateFlow(WeatherReq(0.0,0.0))

    private val _weatherData= MutableStateFlow<WeatherRes?>(null)
    val weatherData: StateFlow<WeatherRes?> = _weatherData.asStateFlow()

    private val _weatherRefresh = MutableStateFlow(true)
    val weatherRefresh: StateFlow<Boolean> = _weatherRefresh.asStateFlow()
    fun updateWeatherRefresh(newValue: Boolean) { _weatherRefresh.value = newValue }

    suspend fun getWeather():Boolean{
        val apiService = RetrofitClientServer.instance

        val data = _weatherReq.value

        val call = apiService.getWeather(data)

        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<WeatherRes>{
                override fun onResponse(call: Call<WeatherRes>, response: Response<WeatherRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            _weatherData.value = it

                            continuation.resume(true)
                        }
                    }else{
                        _weatherData.value = response.body()
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<WeatherRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })

        }
    }

    suspend fun getLocation(context: Context):Boolean = suspendCancellableCoroutine{ continuation ->
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        // 권한 체크와 위치 업데이트
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude

                    _weatherReq.value = WeatherReq(latitude,longitude)

                    continuation.resume(true)
                }
            }
        } else {
            continuation.resume(false)
        }
    }

    suspend fun getRTStoryList():Boolean{
        val apiService = RetrofitClientServer.instance

        val call = apiService.getRealTimeList()
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<RTStoryListRes>{
                override fun onResponse(call: Call<RTStoryListRes>, response: Response<RTStoryListRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            _rtStoryList.value = it
                            continuation.resume(true)
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<RTStoryListRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }
}

