package kr.carepet.app.navi.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kr.carepet.data.daily.DailyDetailData
import kr.carepet.data.daily.DailyDetailRes
import kr.carepet.data.daily.DailyLifeWalk
import kr.carepet.data.daily.DailyMonthData
import kr.carepet.data.daily.DailyMonthRes
import kr.carepet.data.daily.Paginate
import kr.carepet.data.daily.WalkListRes
import kr.carepet.data.daily.WeekData
import kr.carepet.data.pet.PetDetailData
import kr.carepet.singleton.RetrofitClientServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume

class WalkViewModel(private val sharedViewModel: SharedViewModel) :ViewModel() {

    val weekRecord: StateFlow<WeekData?> = sharedViewModel.weekRecord
    fun updatePetInfo(newData: List<PetDetailData>){
        sharedViewModel.updatePetInfo(newData)
    }
    suspend fun callGetWeekRecord(ownrPetUnqNo: String, searchDay: String){
        sharedViewModel.getWeekRecord(ownrPetUnqNo, searchDay)
    }

    private val _dailyMonth = MutableStateFlow<DailyMonthData?>(null)
    val dailyMonth: StateFlow<DailyMonthData?> = _dailyMonth.asStateFlow()

    private val _toDetail = MutableStateFlow(false)
    // 검색 중인지 여부를 StateFlow로 노출
    val toDetail = _toDetail.asStateFlow()
    fun updateToDetail(newValue: Boolean){
        _toDetail.value = newValue
    }

    private val _toMonthCalendar = MutableStateFlow(false)
    // 검색 중인지 여부를 StateFlow로 노출
    val toMonthCalendar = _toMonthCalendar.asStateFlow()
    fun updateToMonthCalendar(newValue: Boolean){
        _toMonthCalendar.value = newValue
    }

    private val _walkListItem = MutableStateFlow<DailyLifeWalk?>(null)
    val walkListItem: StateFlow<DailyLifeWalk?> = _walkListItem.asStateFlow()
    fun updateWalkListItem(newValue: DailyLifeWalk){
        _walkListItem.value = newValue
    }

    val petInfo: StateFlow<List<PetDetailData>> = sharedViewModel.petInfo

    private val _walkList = MutableStateFlow<List<DailyLifeWalk>>(emptyList())
    val walkList: StateFlow<List<DailyLifeWalk>> = _walkList.asStateFlow()

    private val _page = MutableStateFlow<Paginate?>(null)
    val page: StateFlow<Paginate?> = _page.asStateFlow()

    private val _dailyDetail = MutableStateFlow<DailyDetailData?>(null)
    val dailyDetail: StateFlow<DailyDetailData?> = _dailyDetail.asStateFlow()

    private val _selectPet = MutableStateFlow<List<PetDetailData>>(emptyList())
    val selectPet: StateFlow<List<PetDetailData>> = _selectPet.asStateFlow()
    fun updateSelectPet(newValue: List<PetDetailData>) { _selectPet.value = newValue }

    private val _isLoading = MutableStateFlow(false)
    // 검색 중인지 여부를 StateFlow로 노출
    val isLoading = _isLoading.asStateFlow()
    fun updateIsLoading(newValue: Boolean){
        _isLoading.value = newValue
    }

    private val _isWalking = MutableStateFlow(false)
    // 검색 중인지 여부를 StateFlow로 노출
    val isWalking = _isWalking.asStateFlow()
    fun updateIsWalking(newValue: Boolean) { _isWalking.value = newValue }

    private val _sheetChange = MutableStateFlow("select")
    // 검색 중인지 여부를 StateFlow로 노출
    val sheetChange = _sheetChange.asStateFlow()
    fun updateSheetChange(newValue: String) { _sheetChange.value = newValue }

    //------------------------------------------------------------------------------//

    private val _walkMemo = MutableStateFlow<String>("")
    val walkMemo: StateFlow<String> = _walkMemo.asStateFlow()
    fun updateWalkMemo(newValue: String) { _walkMemo.value = newValue }

    private val _postStory = MutableStateFlow<Boolean>(false)
    val postStory: StateFlow<Boolean> = _postStory.asStateFlow()
    fun updatePostStory(newValue: Boolean) { _postStory.value = newValue }

    private val _bwlCount = MutableStateFlow<Int>(0)
    val bwlCount: StateFlow<Int> = _bwlCount.asStateFlow()
    fun updateBwlCount(newValue: Int) { _bwlCount.value += newValue }

    private val _peeCount = MutableStateFlow<Int>(0)
    val peeCount: StateFlow<Int> = _peeCount.asStateFlow()
    fun updatePeeCount(newValue: Int) { _peeCount.value = newValue }

    private val _markCount = MutableStateFlow<Int>(0)
    val markCount: StateFlow<Int> = _markCount.asStateFlow()
    fun updateMarkCount(newValue: Int) { _markCount.value = newValue }

    var state by mutableStateOf(MainScreenState())
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

    suspend fun getWalkList(page:Int,petUnqNo:String):Boolean{
        val apiService = RetrofitClientServer.instance

        val data = kr.carepet.data.daily.WalkListReq(ownrPetUnqNo = petUnqNo, page = page, 10, 20)

        val call = apiService.getWalkList(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<WalkListRes>{
                override fun onResponse(call: Call<WalkListRes>, response: Response<WalkListRes>) {
                    if(response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            if(body.statusCode==200){
                                _walkList.value = body.data.dailyLifeWalkList
                                _page.value = body.data.paginate
                                Log.d("LOG",body.data.dailyLifeWalkList.toString())
                                continuation.resume(true)
                            }else{
                                continuation.resume(false)
                            }
                        }
                    }
                }
                override fun onFailure(call: Call<WalkListRes>, t: Throwable) {
                    continuation.resume(true)
                }
            })
        }
    }

    suspend fun getDailyDetail(schUnqNo:Int):Boolean{
        val apiService = RetrofitClientServer.instance

        val data = kr.carepet.data.daily.DailyDetailReq(schUnqNo = schUnqNo, cmntYn = "N")

        val call = apiService.getDailyDetail(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object :Callback<DailyDetailRes>{
                override fun onResponse(
                    call: Call<DailyDetailRes>,
                    response: Response<DailyDetailRes>
                ) {
                    if(response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            if (body.statusCode ==200){

                                _dailyDetail.value=body.data

                                continuation.resume(true)
                            }else{
                                continuation.resume(false)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<DailyDetailRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

    suspend fun getMonthData(ownrPetUnqNo:String, searchMonth:String):Boolean{
        val apiService = RetrofitClientServer.instance

        val data = kr.carepet.data.daily.DailyMonthReq(ownrPetUnqNo, searchMonth)

        val call = apiService.getMonthData(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object :Callback<DailyMonthRes>{
                override fun onResponse(
                    call: Call<DailyMonthRes>,
                    response: Response<DailyMonthRes>
                ) {
                    if(response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            _dailyMonth.value = it.data
                            continuation.resume(true)
                        }
                    }else{
                        continuation.resume(false)
                    }
                }
                override fun onFailure(call: Call<DailyMonthRes>, t: Throwable) {
                    continuation.resume(false)
                }
            })
        }
    }
}

data class MainScreenState(
    val listOfSelectedImages:List<Uri> = emptyList()
)