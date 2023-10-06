package kr.carepet.app.navi.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kr.carepet.data.daily.WeekData
import kr.carepet.data.pet.MyPetListReq
import kr.carepet.data.pet.MyPetListRes
import kr.carepet.data.pet.PetDetailData
import kr.carepet.singleton.G
import kr.carepet.singleton.MySharedPreference
import kr.carepet.singleton.RetrofitClientServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume

class HomeViewModel(private val sharedViewModel: SharedViewModel):ViewModel() {

    val weekRecord: StateFlow<kr.carepet.data.daily.WeekData?> = sharedViewModel.weekRecord
    fun updatePetInfo(newData: List<kr.carepet.data.pet.PetDetailData>){
        sharedViewModel.updatePetInfo(newData)
    }

    fun updateSeletedPet(newData: kr.carepet.data.pet.PetDetailData){
        sharedViewModel.updateSelectPet(newData)
    }

    suspend fun callGetWeekRecord(ownrPetUnqNo: String, searchDay: String){
        sharedViewModel.getWeekRecord(ownrPetUnqNo, searchDay)
    }

    fun changeBirth(birth:String):String{
        return sharedViewModel.changeBirth(birth)
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

    private val _showBottomSheet = MutableStateFlow<Boolean>(false)
    val showBottomSheet: StateFlow<Boolean> = _showBottomSheet.asStateFlow()
    fun updateShowBottomSheet(newValue: Boolean) { _showBottomSheet.value = newValue }

    private val _petInfo = MutableStateFlow<List<kr.carepet.data.pet.PetDetailData>>(emptyList())
    val petInfo: StateFlow<List<kr.carepet.data.pet.PetDetailData>> = _petInfo.asStateFlow()

    private val _repPet = MutableStateFlow<List<kr.carepet.data.pet.PetDetailData>>(emptyList())
    val repPet: StateFlow<List<kr.carepet.data.pet.PetDetailData>> = _repPet.asStateFlow()

    //private val _isLoading = MutableStateFlow<Boolean>(true)
    //val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    //suspend fun updateIsLoading(newValue: Boolean){
    //    _isLoading.value = newValue
    //}

    val isLoading = MutableStateFlow<Boolean>(true)

    private val _petListSelect = MutableStateFlow("")
    val petListSelect: StateFlow<String> = _petListSelect.asStateFlow()
    fun updatePetListSelect(newValue: String) { _petListSelect.value = newValue }

    fun isLoadingSuccess() {
        viewModelScope.launch {
            // 결과물을 날리기
            isLoading.emit(loadPetInfo())
        }
    }

    suspend fun loadPetInfo():Boolean{

            val apiService = RetrofitClientServer.instance

            val data =
                kr.carepet.data.pet.MyPetListReq(kr.carepet.singleton.MySharedPreference.getUserId())

            val call = apiService.myPetList(data)
            return suspendCancellableCoroutine { continuation ->
                call.enqueue(object : Callback<kr.carepet.data.pet.MyPetListRes>{
                    override fun onResponse(call: Call<kr.carepet.data.pet.MyPetListRes>, response: Response<kr.carepet.data.pet.MyPetListRes>) {
                        if(response.isSuccessful){
                            val body = response.body()
                            body?.let {
                                if(body.petDetailData.isEmpty()){
                                    _petInfo.value= arrayListOf(emptyPet)
                                    updatePetInfo(arrayListOf(emptyPet))
                                }else{
                                    _petInfo.value=body.petDetailData
                                    _repPet.value=body.petDetailData.filter { petDetailData -> petDetailData.petRprsYn=="Y" }
                                    updatePetInfo(body.petDetailData)
                                }
                                Log.d("LOG",_petInfo.value.toString())
                                continuation.resume(false)
                            }
                        }
                    }
                    override fun onFailure(call: Call<kr.carepet.data.pet.MyPetListRes>, t: Throwable) {
                        Log.d("LOG","FAIL"+t.message)
                    }

                })
            }
    }
}