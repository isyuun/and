package kr.carepet.app.navi.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.annotation.GlideModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kr.carepet.data.CommonCodeModel
import kr.carepet.data.bbs.EventDetailRes
import kr.carepet.data.bbs.EventListRes
import kr.carepet.data.cmm.CdDetail
import kr.carepet.data.cmm.CmmRes
import kr.carepet.data.cmm.CommonData
import kr.carepet.data.daily.DailyCreateReq
import kr.carepet.data.daily.DailyCreateRes
import kr.carepet.data.daily.DailyDetailRes
import kr.carepet.data.daily.Pet
import kr.carepet.data.daily.PhotoData
import kr.carepet.data.daily.PhotoRes
import kr.carepet.data.daily.Story
import kr.carepet.data.daily.StoryReq
import kr.carepet.data.daily.StoryRes
import kr.carepet.data.pet.CurrentPetData
import kr.carepet.data.user.BbsReq
import kr.carepet.gps.app.GPSApplication
import kr.carepet.singleton.RetrofitClientServer
import kr.carepet.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import kotlin.coroutines.resume


@GlideModule
class CommunityViewModel(private val sharedViewModel: SharedViewModel) :ViewModel(){

    private val _storyRes = MutableStateFlow<StoryRes?>(null)
    val storyRes: StateFlow<StoryRes?> = _storyRes.asStateFlow()
    fun updateStoryRes(newValue: StoryRes?){
        _storyRes.value = newValue
    }

    private val _storyList = MutableStateFlow<List<Story>>(emptyList())
    val storyList: StateFlow<List<Story>> = _storyList.asStateFlow()
    fun updateStoryList(newValue: List<Story>){
        val currentList = _storyList.value.toMutableList()
        currentList.addAll(newValue)
        _storyList.value = currentList
    }
    fun updateStoryListClear(){
        _storyList.value = emptyList()
    }

    private val _storyPage = MutableStateFlow<Int>(1)
    val storyPage:StateFlow<Int> = _storyPage.asStateFlow()
    fun updateStoryPage(newValue: Int){
        _storyPage.value = newValue
    }

    private val _storyDetail = MutableStateFlow<DailyDetailRes?>(null)
    val storyDetail:StateFlow<DailyDetailRes?> = _storyDetail.asStateFlow()
    fun updateStoryDetail(newValue: DailyDetailRes?){
        _storyDetail.value = newValue
    }
    // ------------------------------------------------------------------
    private val _selectPetMulti = MutableStateFlow<MutableList<CurrentPetData>>(mutableListOf())
    val selectPetMulti: StateFlow<MutableList<CurrentPetData>> = _selectPetMulti.asStateFlow()
    fun addSelectPetMulti(newValue: CurrentPetData) { _selectPetMulti.value.add(newValue) }
    fun subSelectPetMulti(newValue: CurrentPetData) { _selectPetMulti.value.remove(newValue) }

    private val _selectCategory = MutableStateFlow<MutableList<CdDetail>>(mutableListOf())
    val selectCategory: StateFlow<MutableList<CdDetail>> = _selectCategory.asStateFlow()
    fun addSelectCategory(newValue: CdDetail) { _selectCategory.value.add(newValue) }
    fun subSelectCategory(newValue: CdDetail) { _selectCategory.value.remove(newValue) }

    private val _walkMemo = MutableStateFlow<String>("")
    val walkMemo: StateFlow<String> = _walkMemo.asStateFlow()
    fun updateWalkMemo(newValue: String) {
        _walkMemo.value = newValue
    }

    private val _walkTitle = MutableStateFlow<String>("")
    val walkTitle: StateFlow<String> = _walkTitle.asStateFlow()
    fun updateWalkTitle(newValue: String) {
        _walkTitle.value = newValue
    }

    private val _hashTag = MutableStateFlow<List<String>>(emptyList())
    val hashTag: StateFlow<List<String>> = _hashTag.asStateFlow()
    fun updateHashTag(newValue: List<String>) { _hashTag.value = newValue }

    private val _postStory = MutableStateFlow<Boolean>(false)
    val postStory: StateFlow<Boolean> = _postStory.asStateFlow()
    fun updatePostStory(newValue: Boolean) {
        _postStory.value = newValue
    }
    var state by mutableStateOf(MainScreenState())
        private set

    private val _schList = MutableStateFlow<List<CommonData>>(emptyList())
    val schList: StateFlow<List<CommonData>> = _schList.asStateFlow()
    fun updateSchList(newValue: List<CommonData>) {
        _schList.value = newValue
    }

    private val _photoRes = MutableStateFlow<List<PhotoData>?>(emptyList())

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
    // ------------------------------------------------------------------

    private val _dm = MutableStateFlow<String>("")
    val dm: StateFlow<String> = _dm.asStateFlow()



    private val _comment = MutableStateFlow<String>("")
    val comment: StateFlow<String> = _comment.asStateFlow()
    fun updateComment(newValue: String) { _comment.value = newValue }

    private val _eventList = MutableStateFlow<EventListRes?>(null)
    val eventList: StateFlow<EventListRes?> = _eventList.asStateFlow()

    private val _eventDetail = MutableStateFlow<EventDetailRes?>(null)
    val eventDetail: StateFlow<EventDetailRes?> = _eventDetail.asStateFlow()

    private val _orderType = MutableStateFlow<String>("최신순")
    val orderType: StateFlow<String> = _orderType.asStateFlow()
    fun updateOrderType(newValue: String) {
        _orderType.value = newValue
    }

    private val _viewType = MutableStateFlow<String>("전체")
    val viewType: StateFlow<String> = _viewType.asStateFlow()
    fun updateViewType(newValue: String) {
        _viewType.value = newValue
    }

    suspend fun getStoryList(page: Int):Boolean{
        val apiService = RetrofitClientServer.instance

        val order = if (_orderType.value == "최신순") "001" else "002"
        val view = if (_viewType.value == "전체") "001" else "002"

        val data = StoryReq(orderType = order,page = page, pageSize = 10, recordSize = 20, viewType = view)

        val call = apiService.getStoryList(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<StoryRes>{
                override fun onResponse(call: Call<StoryRes>, response: Response<StoryRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            _storyRes.value = it
                            updateStoryList(it.data.storyList)
                            continuation.resume(true)
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<StoryRes>, t: Throwable) {
                    continuation.resume(false)
                }
            })
        }
    }

    suspend fun getStoryDetail(schUnqNo: Int): Boolean {
        val apiService = RetrofitClientServer.instance

        val data = kr.carepet.data.daily.DailyDetailReq(schUnqNo = schUnqNo, cmntYn = "N")

        val call = apiService.getDailyDetail(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<DailyDetailRes> {
                override fun onResponse(
                    call: Call<DailyDetailRes>,
                    response: Response<DailyDetailRes>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        body?.let {
                            if (body.statusCode == 200) {
                                _storyDetail.value = it
                                continuation.resume(true)
                            } else {

                                continuation.resume(false)
                            }
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<DailyDetailRes>, t: Throwable) {

                    continuation.resume(false)
                }

            })
        }
    }

    suspend fun getEventList(page:Int):Boolean{
        val apiService = RetrofitClientServer.instance

        val data = BbsReq(bbsSn = 9, page = page, pageSize = 10, recordSize = 20)

        val call = apiService.getEventList(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<EventListRes>{
                override fun onResponse(call: Call<EventListRes>, response: Response<EventListRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            _eventList.value = body
                            continuation.resume(true)
                        }
                    }else{
                        val errorBodyString = response.errorBody()!!.string()

                        _dm.value = errorBodyParse(errorBodyString)

                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<EventListRes>, t: Throwable) {
                    continuation.resume(false)
                }
            })
        }
    }

    suspend fun getEventDetail(pstSn:Int):Boolean{
        val apiService = RetrofitClientServer.instance

        val call = apiService.getEventDetail(pstSn)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<EventDetailRes>{
                override fun onResponse(call: Call<EventDetailRes>, response: Response<EventDetailRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            _eventDetail.value = it
                            continuation.resume(true)
                        }
                    }else{
                        val errorBodyString = response.errorBody()!!.string()
                        _dm.value = errorBodyParse(errorBodyString)
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<EventDetailRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

    suspend fun getSchList():Boolean{
        val apiService = RetrofitClientServer.instance

        val call = apiService.getCmmList(CommonCodeModel(cmmCdData = "SCH"))
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<CmmRes>{
                override fun onResponse(call: Call<CmmRes>, response: Response<CmmRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            _schList.value = it.data
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

    suspend fun uploadDaily(): Boolean {
        val apiService = RetrofitClientServer.instance

        val petList = _selectPetMulti.value.map { currentPetData ->
            Pet(
                petNm = currentPetData.petNm,
                ownrPetUnqNo = currentPetData.ownrPetUnqNo,
                urineNmtm = "0",
                relmIndctNmtm = "0",
                bwlMvmNmtm = "0"
            )
        }

        val data = DailyCreateReq(
            cmntUseYn = "Y",// 댓글 사용 여부
            files = _photoRes.value,
            hashTag = _hashTag.value,
            pet = petList,
            rcmdtnYn = "Y", // 추천 여부
            rlsYn =  if (_postStory.value) "Y" else "N", // 공개 여부
            schCdList = _selectCategory.value.map { it.cdId },
            schTtl = _walkTitle.value,
            schCn = _walkMemo.value,
            totClr = 0f,
            totDstnc = 0f,
            walkDptreDt = "",
            walkEndDt = ""
        )

        val call = apiService.uploadDaily(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<DailyCreateRes> {
                override fun onResponse(
                    call: Call<DailyCreateRes>,
                    response: Response<DailyCreateRes>
                ) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            if (body.statusCode ==200){

                                _selectCategory.value.clear()
                                _selectPetMulti.value.clear()
                                _photoRes.value = emptyList()
                                _hashTag.value = emptyList()
                                _walkTitle.value = ""
                                _walkMemo.value = ""

                                continuation.resume(true)
                            }else{

                                continuation.resume(false)
                            }
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<DailyCreateRes>, t: Throwable) {
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

        val call = apiService.uploadPhoto(parts)
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
}