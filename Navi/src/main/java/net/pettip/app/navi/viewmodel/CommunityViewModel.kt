package net.pettip.app.navi.viewmodel

import android.content.Context
import android.net.Uri
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
import net.pettip.data.CommonCodeModel
import net.pettip.data.bbs.BbsCmnt
import net.pettip.data.bbs.BbsCmntCreateRes
import net.pettip.data.bbs.BbsCmntRcmdtnReq
import net.pettip.data.bbs.BbsCmntUpdateReq
import net.pettip.data.bbs.BbsCmtCreateReq
import net.pettip.data.bbs.BbsDetailRes
import net.pettip.data.bbs.BbsRcmdtnReq
import net.pettip.data.bbs.EndEventListRes
import net.pettip.data.bbs.EventListRes
import net.pettip.data.bbs.NtcListRes
import net.pettip.data.bbs.QnaDetailRes
import net.pettip.data.bbs.QnaListRes
import net.pettip.data.cmm.CdDetail
import net.pettip.data.cmm.CmmRes
import net.pettip.data.cmm.CommonData
import net.pettip.data.cmm.commonRes
import net.pettip.data.daily.BbsCmntDeleteReq
import net.pettip.data.daily.Cmnt
import net.pettip.data.daily.CmntCreateReq
import net.pettip.data.daily.CmntCreateRes
import net.pettip.data.daily.CmntDeleteReq
import net.pettip.data.daily.CmntRcmdtnReq
import net.pettip.data.daily.CmntUpdateReq
import net.pettip.data.daily.DailyCreateReq
import net.pettip.data.daily.DailyCreateRes
import net.pettip.data.daily.DailyDetailRes
import net.pettip.data.daily.DailyLifeFile
import net.pettip.data.daily.DailyLifePet
import net.pettip.data.daily.DailyLifeSchHashTag
import net.pettip.data.daily.DailyLifeSchSe
import net.pettip.data.daily.DailyLifeUpdatePet
import net.pettip.data.daily.DailyRcmdtn
import net.pettip.data.daily.DailyRlsYnReq
import net.pettip.data.daily.DailyUpdateReq
import net.pettip.data.daily.DclrCreateReq
import net.pettip.data.daily.Pet
import net.pettip.data.daily.PhotoData
import net.pettip.data.daily.PhotoRes
import net.pettip.data.daily.Story
import net.pettip.data.daily.StoryReq
import net.pettip.data.daily.StoryRes
import net.pettip.data.pet.CurrentPetData
import net.pettip.data.user.BbsReq
import net.pettip.data.user.FAQData
import net.pettip.data.user.FAQRes
import net.pettip.singleton.RetrofitClientServer
import net.pettip.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume


@GlideModule
class CommunityViewModel(private val sharedViewModel: SharedViewModel) :ViewModel(){

    var moreStoryClick = sharedViewModel.moreStoryClick
    fun updateMoreStoryClick(newValue : Int?){
        sharedViewModel.updateMoreStoryClick(newValue)
    }

    fun updateToStory(newValue: Boolean){
        sharedViewModel.updateToStory(newValue)
    }

    val currentTab = sharedViewModel.currentTab

    val selectedPet = sharedViewModel.selectPet

    private val _preUserId = MutableStateFlow<String>("")
    val preUserId : StateFlow<String> = _preUserId.asStateFlow()
    fun updatePreUserId(newValue: String){
        _preUserId.value = newValue
    }

    private val _storyRefresh = MutableStateFlow<Boolean>(true)
    val storyRefresh: StateFlow<Boolean> = _storyRefresh.asStateFlow()
    fun updateStoryRefresh(newValue: Boolean){
        _storyRefresh.value = newValue
    }

    private val _eventRefresh = MutableStateFlow<Boolean>(true)
    val eventRefresh: StateFlow<Boolean> = _eventRefresh.asStateFlow()
    fun updateEventRefresh(newValue: Boolean){
        _eventRefresh.value = newValue
    }

    private val _endEventRefresh = MutableStateFlow<Boolean>(true)
    val endEventRefresh: StateFlow<Boolean> = _endEventRefresh.asStateFlow()
    fun updateEndEventRefresh(newValue: Boolean){
        _endEventRefresh.value = newValue
    }

    private val _lastPstSn = MutableStateFlow<Int?>(null)
    val lastPstSn: StateFlow<Int?> = _lastPstSn.asStateFlow()
    fun updateLastPstSn(newValue: Int?){
        _lastPstSn.value = newValue
    }

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

    private val _storyUpdate = MutableStateFlow<DailyUpdateReq?>(null)
    val storyUpdate:StateFlow<DailyUpdateReq?> = _storyUpdate.asStateFlow()
    fun updateStoryUpdate(newValue: DailyUpdateReq?){
        _storyUpdate.value = newValue
    }

    private val _cmntList = MutableStateFlow<List<Cmnt>?>(null)
    val cmntList:StateFlow<List<Cmnt>?> = _cmntList.asStateFlow()
    fun updateCmntList(newValue: List<Cmnt>?){
        _cmntList.value = newValue
    }

    private val _commentRes = MutableStateFlow<CmntCreateRes?>(null)
    val commentRes:StateFlow<CmntCreateRes?> = _commentRes.asStateFlow()

    private val _storyLoading = MutableStateFlow<Boolean>(false)
    val storyLoading:StateFlow<Boolean> = _storyLoading.asStateFlow()

    private val _eventLoading = MutableStateFlow<Boolean>(false)
    val eventLoading:StateFlow<Boolean> = _eventLoading.asStateFlow()

    private val _replyCmnt = MutableStateFlow<Cmnt?>(null)
    val replyCmnt:StateFlow<Cmnt?> = _replyCmnt.asStateFlow()
    fun updateReplyCmnt(newValue: Cmnt?){
        _replyCmnt.value = newValue
    }

    // ------------------------------------------------------------------
    private val _selectPetMulti = MutableStateFlow<MutableList<CurrentPetData>>(mutableListOf())
    val selectPetMulti: StateFlow<MutableList<CurrentPetData>> = _selectPetMulti.asStateFlow()
    fun addSelectPetMulti(newValue: CurrentPetData) { _selectPetMulti.value.add(newValue) }
    fun subSelectPetMulti(newValue: CurrentPetData) { _selectPetMulti.value.remove(newValue) }
    fun clearSelectPetMulti() { _selectPetMulti.value.clear() }

    private val _selectCategory = MutableStateFlow<MutableList<CdDetail>>(mutableListOf())
    val selectCategory: StateFlow<MutableList<CdDetail>> = _selectCategory.asStateFlow()
    fun addSelectCategory(newValue: CdDetail) { _selectCategory.value.add(newValue) }
    fun subSelectCategory(newValue: CdDetail) { _selectCategory.value.remove(newValue) }
    fun clearSelectCategory() { _selectCategory.value.clear() }

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

    private val _updateComment = MutableStateFlow<String>("")
    val updateComment: StateFlow<String> = _updateComment.asStateFlow()
    fun updateUpdateComment(newValue: String) { _updateComment.value = newValue }

    private val _eventList = MutableStateFlow<EventListRes?>(null)
    val eventList: StateFlow<EventListRes?> = _eventList.asStateFlow()
    fun updateEventListClear(){_eventList.value = null}

    private val _bbsDetail = MutableStateFlow<BbsDetailRes?>(null)
    val bbsDetail: StateFlow<BbsDetailRes?> = _bbsDetail.asStateFlow()
    fun updateBbsDetail(newValue: BbsDetailRes?){
        _bbsDetail.value = newValue
    }

    private val _eventCmntList = MutableStateFlow<List<BbsCmnt>?>(null)
    val eventCmntList:StateFlow<List<BbsCmnt>?> = _eventCmntList.asStateFlow()
    fun updateEventCmntList(newValue: List<BbsCmnt>?){
        _eventCmntList.value = newValue
    }

    private val _endEventList = MutableStateFlow<EndEventListRes?>(null)
    val endEventList: StateFlow<EndEventListRes?> = _endEventList.asStateFlow()
    fun updateEndEventListClear(){_endEventList.value = null}

    private val _eventReplyCmnt = MutableStateFlow<BbsCmnt?>(null)
    val eventReplyCmnt:StateFlow<BbsCmnt?> = _eventReplyCmnt.asStateFlow()
    fun updateEventReplyCmnt(newValue: BbsCmnt?){
        _eventReplyCmnt.value = newValue
    }

    private val _bbsComment = MutableStateFlow<String>("")
    val bbsComment: StateFlow<String> = _bbsComment.asStateFlow()
    fun updateBbsComment(newValue: String) { _bbsComment.value = newValue }



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
    // ----------------- 게시판 ------------------------
    private val _ntcList = MutableStateFlow<NtcListRes?>(null)
    val ntcList: StateFlow<NtcListRes?> = _ntcList.asStateFlow()
    fun updateNtcListClear(){_ntcList.value = null}

    private val _faqList = MutableStateFlow<FAQData?>(null)
    val faqList: StateFlow<FAQData?> = _faqList.asStateFlow()
    fun updateFaqListClear(){_faqList.value = null}

    private val _qnaList = MutableStateFlow<QnaListRes?>(null)
    val qnaList: StateFlow<QnaListRes?> = _qnaList.asStateFlow()
    fun updateQnaListClear(){_qnaList.value = null}

    private val _qnaDetail = MutableStateFlow<QnaDetailRes?>(null)
    val qnaDetail: StateFlow<QnaDetailRes?> = _qnaDetail.asStateFlow()
    fun updateQnaDetail(newValue: QnaDetailRes){
        _qnaDetail.value = newValue
    }

    // ----------------- 게시판 ------------------------

    // ---------------------------Modify------------------------------

    // 이미 등록된 펫 리스트
    private val _uploadedPetMulti = MutableStateFlow<MutableList<DailyLifePet>>(mutableListOf())
    val uploadedPetMulti: StateFlow<MutableList<DailyLifePet>> = _uploadedPetMulti.asStateFlow()
    fun addUploadedPetMulti(newValue: DailyLifePet) {
        _uploadedPetMulti.value = _uploadedPetMulti.value.map { pet ->
            if (pet.ownrPetUnqNo == newValue.ownrPetUnqNo) {
                pet.copy(rowState = null)
            } else {
                pet
            }
        }.toMutableList()
    }
    fun subUploadedPetMulti(newValue: DailyLifePet) {
        _uploadedPetMulti.value = _uploadedPetMulti.value.map { pet ->
            if (pet.ownrPetUnqNo == newValue.ownrPetUnqNo) {
                pet.copy(rowState = "D")
            } else {
                pet
            }
        }.toMutableList()
    }
    fun clearUploadedPetMulti() { _uploadedPetMulti.value.clear() }
    fun initUploadedPetMulti(newValue: List<DailyLifePet>){
        _uploadedPetMulti.value.addAll(newValue)
    }

    // 새로 추가한 펫
    private val _newSelectPet = MutableStateFlow<MutableList<DailyLifeUpdatePet.SimplePet>>(mutableListOf())
    val newSelectPet: StateFlow<MutableList<DailyLifeUpdatePet.SimplePet>> = _newSelectPet.asStateFlow()
    fun addNewSelectPet(newValue: DailyLifeUpdatePet.SimplePet) {
        _newSelectPet.value.add(newValue)
    }
    fun subNewSelectPet(newValue: DailyLifeUpdatePet.SimplePet) {
        _newSelectPet.value.remove(newValue)
    }
    fun clearNewSelectPet() { _newSelectPet.value.clear() }

    private val _uploadSchTtl = MutableStateFlow<String?>(null)
    val uploadSchTtl = _uploadSchTtl.asStateFlow()
    fun updateUploadSchTtl(newValue: String){
        _uploadSchTtl.value = newValue
    }

    // 이미 등록된 사진 리스트
    private val _uploadedFileList = MutableStateFlow<List<DailyLifeFile>?>(null)
    val uploadedFileList:StateFlow<List<DailyLifeFile>?> = _uploadedFileList.asStateFlow()
    fun updateUploadedFileList(newValue: List<DailyLifeFile>?){
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
        } ?: emptyList()) as MutableList<DailyLifeFile>?
    }

    // 새로 등록된 사진 리스트
    private val _newFileList = MutableStateFlow<List<DailyLifeFile>?>(null)
    val newFileList:StateFlow<List<DailyLifeFile>?> = _newFileList.asStateFlow()
    fun clearNewFileList(){_newFileList.value = null}


    fun getLastSegmentAfterSlash(inputString: String): String {
        val segments = inputString.split('/')
        return segments.last()
    }

    private val _uploadSchCn = MutableStateFlow<String?>(null)
    val uploadSchCn = _uploadSchCn.asStateFlow()
    fun updateUploadSchCn(newValue: String){
        _uploadSchCn.value = newValue
    }

    private val _uploadSchSeList = MutableStateFlow<MutableList<DailyLifeSchSe>>(mutableListOf())
    val uploadSchSeList = _uploadSchSeList.asStateFlow()
    fun addUploadSchSeList(newValue: DailyLifeSchSe){
        val isAlreadyUploadedSch = _storyDetail.value?.data?.dailyLifeSchSeList?.any{ it.cdId == newValue.cdId}
        if (isAlreadyUploadedSch == true){
            _uploadSchSeList.value = _uploadSchSeList.value.map { sch ->
                if (sch.cdId == newValue.cdId) {
                    sch.copy(rowState = null)
                } else {
                    sch
                }
            }.toMutableList()
        }else{
            _uploadSchSeList.value.add(newValue.copy(rowState = "C"))
        }
        Log.d("LOG",_uploadSchSeList.value.toString())
    }
    fun subUploadSchSeList(newValue: DailyLifeSchSe){
        val isAlreadyUploadedSch = _storyDetail.value?.data?.dailyLifeSchSeList?.any { it.cdId == newValue.cdId }

        if (isAlreadyUploadedSch == true) {
            _uploadSchSeList.value = _uploadSchSeList.value.map { sch ->
                if (sch.cdId == newValue.cdId) {
                    sch.copy(rowState = "D")
                } else {
                    sch
                }
            }.toMutableList()
        }else{
            _uploadSchSeList.value.remove(newValue.copy(rowState = "C"))
        }
        Log.d("LOG",_uploadSchSeList.value.toString())
    }
    fun initUploadSchSeList(newValue: List<DailyLifeSchSe>){
        _uploadSchSeList.value.addAll(newValue)
    }
    fun clearUploadSchSeList(){ _uploadSchSeList.value.clear()}

    private val _uploadHashTag = MutableStateFlow<List<DailyLifeSchHashTag>>(emptyList())
    val uploadHashTag: StateFlow<List<DailyLifeSchHashTag>> = _uploadHashTag.asStateFlow()
    fun updateUploadHashTag(newValue: List<String>) {

        val currentHashTagList = _storyDetail.value?.data?.dailyLifeSchHashTagList ?: emptyList()

        _uploadHashTag.value = currentHashTagList.map { currentHashTag ->
            if (newValue.contains(currentHashTag.hashTagNm)) {
                currentHashTag.copy(rowState = null)
            } else {
                currentHashTag.copy(rowState = "D")
            }
        } + newValue.filter { newHashTag ->
            !currentHashTagList.any { it.hashTagNm == newHashTag }
        }.map {
            DailyLifeSchHashTag(hashTagNm = it, hashTagNo = "", rowState = "C", schUnqNo = _storyDetail.value?.data?.schUnqNo?:0)
        }
    }
    fun clearUploadHashTag(){_uploadHashTag.value = emptyList() }

    private val _uploadPostStory = MutableStateFlow<Boolean>(false)
    val uploadPostStory: StateFlow<Boolean> = _uploadPostStory.asStateFlow()
    fun updateUploadPostStory(newValue: Boolean) {
        _uploadPostStory.value = newValue
    }
    // ---------------------------Modify------------------------------

    // ---------------------------신고------------------------------
    private val _dclrCn = MutableStateFlow<String>("")
    val dclrCn: StateFlow<String> = _dclrCn.asStateFlow()
    fun updateDclrCn(newValue: String) {
        _dclrCn.value = newValue
    }

    private val _selectCmnt = MutableStateFlow<Cmnt?>(null)
    val selectCmnt: StateFlow<Cmnt?> = _selectCmnt.asStateFlow()
    fun updateSelectCmnt(newValue: Cmnt?) {
        _selectCmnt.value = newValue
    }

    private val _selectDclr = MutableStateFlow<CdDetail?>(null)
    val selectDclr: StateFlow<CdDetail?> = _selectDclr.asStateFlow()
    fun updateSelectDclr(newValue: CdDetail?) {
        _selectDclr.value = newValue
    }

    private val _dclrList = MutableStateFlow<CmmRes?>(null)
    val dclrList: StateFlow<CmmRes?> = _dclrList.asStateFlow()
    fun updateDclrList(newValue: CmmRes?) {
        _dclrList.value = newValue
    }
    suspend fun getDclrList():Boolean{
        val apiService = RetrofitClientServer.instance

        val call = apiService.getCmmList(CommonCodeModel("RSN"))
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<CmmRes>{
                override fun onResponse(call: Call<CmmRes>, response: Response<CmmRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            _dclrList.value = it
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

    suspend fun dclrCreate():Boolean{
        val apiService = RetrofitClientServer.instance

        val data = DclrCreateReq(
            cmntNo = _selectCmnt.value?.cmntNo ?: 0,
            dclrCn = _dclrCn.value,
            dclrRsnCd = _selectDclr.value?.cdId?: "001",
            dclrSeCd = if (_selectCmnt.value==null) "001" else "002",
            schUnqNo = _storyDetail.value?.data?.schUnqNo ?: 0
        )

        val call = apiService.dclrCreate(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<commonRes>{
                override fun onResponse(call: Call<commonRes>, response: Response<commonRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            Log.d("LOG","1")
                            continuation.resume(true)
                        }
                    }else{
                        Log.d("LOG","2")
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<commonRes>, t: Throwable) {
                    Log.d("LOG",t.message.toString())
                    continuation.resume(false)
                }

            })
        }
    }
    // ---------------------------신고------------------------------

    suspend fun bbsRcmdtn(pstSn:Int, rcmdtnSeCd: String):Boolean{
        val apiService = RetrofitClientServer.instance

        val data = BbsRcmdtnReq(
            pstSn = pstSn,
            rcmdtnSeCd = rcmdtnSeCd
        )

        val call = apiService.bbsRcmdtn(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<BbsDetailRes>{
                override fun onResponse(call: Call<BbsDetailRes>, response: Response<BbsDetailRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            _bbsDetail.value = it
                            continuation.resume(true)
                        }
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

        val data = net.pettip.data.daily.DailyDetailReq(schUnqNo = schUnqNo, cmntYn = "Y")

        _storyLoading.value = true
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
                                _cmntList.value = body.data.cmntList
                                _storyLoading.value = false

                                continuation.resume(true)
                            } else {

                                _storyLoading.value = false
                                continuation.resume(false)
                            }
                        }
                    }else{

                        _storyLoading.value = false
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<DailyDetailRes>, t: Throwable) {
                    _storyLoading.value = false
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

        _eventLoading.value = true
        val call = apiService.getEventDetail(pstSn)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<BbsDetailRes>{
                override fun onResponse(call: Call<BbsDetailRes>, response: Response<BbsDetailRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            _bbsDetail.value = it
                            _eventCmntList.value = it.data.bbsCmnts
                            _eventLoading.value = false
                            continuation.resume(true)
                        }
                    }else{
                        val errorBodyString = response.errorBody()!!.string()
                        _dm.value = errorBodyParse(errorBodyString)
                        _eventLoading.value = false
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<BbsDetailRes>, t: Throwable) {
                    _eventLoading.value = false
                    continuation.resume(false)
                }

            })
        }
    }

    suspend fun getEndEventList(page:Int):Boolean{
        val apiService = RetrofitClientServer.instance

        val data = BbsReq(bbsSn = 11, page = page, pageSize = 10, recordSize = 20)

        val call = apiService.getEndEventList(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<EndEventListRes>{
                override fun onResponse(call: Call<EndEventListRes>, response: Response<EndEventListRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            _endEventList.value = body
                            continuation.resume(true)
                        }
                    }else{
                        val errorBodyString = response.errorBody()!!.string()

                        _dm.value = errorBodyParse(errorBodyString)

                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<EndEventListRes>, t: Throwable) {
                    continuation.resume(false)
                }
            })
        }
    }

    suspend fun getEndEventDetail(pstSn:Int):Boolean{
        val apiService = RetrofitClientServer.instance

        _eventLoading.value = true
        val call = apiService.getEndEventDetail(pstSn)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<BbsDetailRes>{
                override fun onResponse(call: Call<BbsDetailRes>, response: Response<BbsDetailRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            _bbsDetail.value = it
                            _eventCmntList.value = it.data.bbsCmnts
                            _eventLoading.value = false
                            continuation.resume(true)
                        }
                    }else{
                        val errorBodyString = response.errorBody()!!.string()
                        _dm.value = errorBodyParse(errorBodyString)
                        _eventLoading.value = false
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<BbsDetailRes>, t: Throwable) {
                    _eventLoading.value = false
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

    suspend fun uploadComment():Boolean{
        val apiService = RetrofitClientServer.instance

        val data = CmntCreateReq(
            schUnqNo = _storyDetail.value?.data?.schUnqNo ?: 0,
            cmntCn = _comment.value,
            petRelUnqNo = selectedPet.value?.petRelUnqNo?:0,
            upCmntNo = 0
        )

        val call = apiService.cmntCreate(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<CmntCreateRes>{
                override fun onResponse(call: Call<CmntCreateRes>, response: Response<CmntCreateRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            _cmntList.value = it.data
                            continuation.resume(true)
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<CmntCreateRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

    suspend fun uploadComment(cmntCn: String):Boolean{
        val apiService = RetrofitClientServer.instance

        val data = CmntCreateReq(
            schUnqNo = _storyDetail.value?.data?.schUnqNo ?: 0,
            cmntCn = cmntCn,
            petRelUnqNo = selectedPet.value?.petRelUnqNo?:0,
            upCmntNo = _replyCmnt.value?.cmntNo ?: 0
        )

        val call = apiService.cmntCreate(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<CmntCreateRes>{
                override fun onResponse(call: Call<CmntCreateRes>, response: Response<CmntCreateRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            _cmntList.value = it.data
                            continuation.resume(true)
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<CmntCreateRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }
    suspend fun getNtcList(page:Int):Boolean{
        val apiService = RetrofitClientServer.instance

        val data = BbsReq(bbsSn = 7, page = page, pageSize = 30, recordSize = 30)

        val call = apiService.getNtcList(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<NtcListRes>{
                override fun onResponse(call: Call<NtcListRes>, response: Response<NtcListRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            _ntcList.value = it
                            continuation.resume(true)
                        }
                    }else{
                        val errorBodyString = response.errorBody()!!.string()

                        _dm.value = errorBodyParse(errorBodyString)

                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<NtcListRes>, t: Throwable) {
                    continuation.resume(false)
                }
            })
        }
    }
    suspend fun getNctDetail(pstSn:Int):Boolean{
        val apiService = RetrofitClientServer.instance

        val call = apiService.getNtcDetail(pstSn)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<BbsDetailRes>{
                override fun onResponse(call: Call<BbsDetailRes>, response: Response<BbsDetailRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            _bbsDetail.value = it
                            _eventCmntList.value = it.data.bbsCmnts
                            continuation.resume(true)
                        }
                    }else{
                        val errorBodyString = response.errorBody()!!.string()
                        _dm.value = errorBodyParse(errorBodyString)
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<BbsDetailRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

    suspend fun getFaqList(page:Int):Boolean{
        val apiService = RetrofitClientServer.instance

        val data = BbsReq(8, page, 100, 100)

        val call = apiService.getFaqList(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<FAQRes>{
                override fun onResponse(call: Call<FAQRes>, response: Response<FAQRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            _faqList.value = body.data
                            continuation.resume(true)
                        }
                    }else{
                        _faqList.value = null
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<FAQRes>, t: Throwable) {
                    _faqList.value = null
                    continuation.resume(false)
                }
            })
        }
    }

    suspend fun getQnaList(page:Int):Boolean{
        val apiService = RetrofitClientServer.instance

        val data = BbsReq(10, page, 100, 100)

        val call = apiService.getQnaList(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<QnaListRes>{
                override fun onResponse(call: Call<QnaListRes>, response: Response<QnaListRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            _qnaList.value = it
                            continuation.resume(true)
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<QnaListRes>, t: Throwable) {
                    continuation.resume(false)
                }
            })
        }
    }

    suspend fun getQnaDetail(pstSn:Int):Boolean{
        val apiService = RetrofitClientServer.instance

        val call = apiService.getQnaDetail(pstSn)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<QnaDetailRes>{
                override fun onResponse(call: Call<QnaDetailRes>, response: Response<QnaDetailRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            _qnaDetail.value = it
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

    suspend fun deleteQna():Boolean{
        val apiService = RetrofitClientServer.instance

        val call = apiService.deleteQna(_qnaDetail.value?.data?.get(0)?.pstSn?:0)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<CmmRes>{
                override fun onResponse(call: Call<CmmRes>, response: Response<CmmRes>) {
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

                override fun onFailure(call: Call<CmmRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

    suspend fun deleteComment(cmntNo: Int):Boolean{
        val apiService = RetrofitClientServer.instance

        val data = CmntDeleteReq(cmntNo)

        val call = apiService.cmntDelete(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<CmntCreateRes>{
                override fun onResponse(call: Call<CmntCreateRes>, response: Response<CmntCreateRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let { it ->
                            if (it.statusCode == 200){
                                _cmntList.value = it.data
                                continuation.resume(true)
                            }else{

                                continuation.resume(false)
                            }
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<CmntCreateRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

    suspend fun updateComment(cmntCn:String, cmntNo:Int):Boolean{
        val apiService = RetrofitClientServer.instance

        val data = CmntUpdateReq(
            cmntCn = cmntCn,
            cmntNo = cmntNo
        )

        val call = apiService.cmntUpdate(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<CmntCreateRes>{
                override fun onResponse(call: Call<CmntCreateRes>, response: Response<CmntCreateRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let { it ->
                            if (it.statusCode == 200){
                                _cmntList.value = it.data
                                continuation.resume(true)
                            }else{

                                continuation.resume(false)
                            }
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<CmntCreateRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

    suspend fun rcmdtnComment(cmntNo:Int ,rcmdtnSeCd:String, schUnqNo:Int):Boolean{
        val apiService = RetrofitClientServer.instance

        val data = CmntRcmdtnReq(
            cmntNo = cmntNo,
            rcmdtnSeCd = rcmdtnSeCd,
            schUnqNo = schUnqNo
        )

        val call = apiService.cmntRcmdtn(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<CmntCreateRes>{
                override fun onResponse(call: Call<CmntCreateRes>, response: Response<CmntCreateRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let { it ->
                            if (it.statusCode == 200){
                                _cmntList.value = it.data
                                continuation.resume(true)
                            }else{

                                continuation.resume(false)
                            }
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<CmntCreateRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

    suspend fun rcmdtnDaily(rcmdtnSeCd:String, schUnqNo:Int):Boolean{
        val apiService = RetrofitClientServer.instance

        val data = DailyRcmdtn(rcmdtnSeCd, schUnqNo)

        val call = apiService.rcmdtnDaily(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<DailyDetailRes>{
                override fun onResponse(call: Call<DailyDetailRes>, response: Response<DailyDetailRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            _storyDetail.value = it

                            continuation.resume(true)
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

    suspend fun updateDaily(delYn:String):Boolean{
        val apiService = RetrofitClientServer.instance

        val data = DailyUpdateReq(
            cmntUseYn = _storyDetail.value?.data?.cmntUseYn?:"Y",
            dailyLifeFileList = _storyDetail.value?.data?.dailyLifeFileList,
            dailyLifePetList = _storyDetail.value?.data?.dailyLifePetList?: emptyList(),
            dailyLifeSchHashTagList = _storyDetail.value?.data?.dailyLifeSchHashTagList,
            dailyLifeSchSeList = _storyDetail.value?.data?.dailyLifeSchSeList,
            delYn = delYn,
            rcmdtnYn = _storyDetail.value?.data?.rcmdtnYn?:"Y",
            rlsYn = _storyDetail.value?.data?.rlsYn?:"Y",
            schCn = _storyDetail.value?.data?.schCn?:"",
            schTtl = _storyDetail.value?.data?.schTtl?:"",
            schUnqNo = _storyDetail.value?.data?.schUnqNo ?: 0,
        )

        val call = data?.let { apiService.updateDaily(it) }

        return suspendCancellableCoroutine { continuation ->
            call?.enqueue(object : Callback<DailyDetailRes>{
                override fun onResponse(call: Call<DailyDetailRes>, response: Response<DailyDetailRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {

                            continuation.resume(true)
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

    suspend fun updateDaily():Boolean{
        val apiService = RetrofitClientServer.instance

        concatUploadedFileList(_newFileList.value?: emptyList())

        val data = DailyUpdateReq(
            cmntUseYn = "Y",
            dailyLifeFileList = _uploadedFileList.value,
            dailyLifePetList = _uploadedPetMulti.value+_newSelectPet.value,
            dailyLifeSchHashTagList = _uploadHashTag.value,
            dailyLifeSchSeList = _uploadSchSeList.value,
            delYn = "N",
            rcmdtnYn = "Y",
            rlsYn = if (_uploadPostStory.value) "Y" else "N",
            schCn = _uploadSchCn.value?:"",
            schTtl = _uploadSchTtl.value?:"",
            schUnqNo = _storyDetail.value?.data?.schUnqNo ?: 0,
        )

        val call = data.let { apiService.updateDaily(it) }

        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<DailyDetailRes>{
                override fun onResponse(call: Call<DailyDetailRes>, response: Response<DailyDetailRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            _storyDetail.value = it
                            continuation.resume(true)
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

    suspend fun updateDailyRls(rlsYn : String):Boolean{
        val apiService = RetrofitClientServer.instance

       // concatUploadedFileList(_newFileList.value?: emptyList())

        val data = DailyRlsYnReq(
            rlsYn = rlsYn,
            schUnqNo = _storyDetail.value?.data?.schUnqNo ?: 0
        )

        val call = data.let { apiService.dailyRlsUpdate(it) }

        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<DailyDetailRes>{
                override fun onResponse(call: Call<DailyDetailRes>, response: Response<DailyDetailRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            _storyDetail.value = it
                            continuation.resume(true)
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

    suspend fun bbsDeleteComment(cmntNo: Int):Boolean{
        val apiService = RetrofitClientServer.instance

        val data = BbsCmntDeleteReq(cmntNo)

        val call = apiService.bbsCmntDelete(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<BbsCmntCreateRes>{
                override fun onResponse(call: Call<BbsCmntCreateRes>, response: Response<BbsCmntCreateRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let { it ->
                            if (it.statusCode == 200){
                                _eventCmntList.value = it.data
                                continuation.resume(true)
                            }else{

                                continuation.resume(false)
                            }
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<BbsCmntCreateRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

    suspend fun bbsCmntCreate(cmntCn: String):Boolean{
        val apiService = RetrofitClientServer.instance

        val data = BbsCmtCreateReq(
            cmntCn = cmntCn,
            petRelUnqNo = selectedPet.value?.petRelUnqNo?:0,
            pstSn = _bbsDetail.value?.data?.pstSn?:0,
            upCmntNo = _eventReplyCmnt.value?.pstCmntNo ?: 0
        )

        val call = apiService.bbsCmntCreate(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<BbsCmntCreateRes>{
                override fun onResponse(call: Call<BbsCmntCreateRes>, response: Response<BbsCmntCreateRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            _eventCmntList.value = it.data
                            continuation.resume(true)
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<BbsCmntCreateRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

    suspend fun bbsCmntCreate():Boolean{
        val apiService = RetrofitClientServer.instance

        val data = BbsCmtCreateReq(
            cmntCn = _bbsComment.value,
            petRelUnqNo = selectedPet.value?.petRelUnqNo?:0,
            pstSn = _bbsDetail.value?.data?.pstSn?:0,
            upCmntNo = 0
        )

        val call = apiService.bbsCmntCreate(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<BbsCmntCreateRes>{
                override fun onResponse(call: Call<BbsCmntCreateRes>, response: Response<BbsCmntCreateRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            _eventCmntList.value = it.data
                            continuation.resume(true)
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<BbsCmntCreateRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }

    suspend fun bbsUpdateComment(cmntCn: String, pstCmntNo:Int):Boolean{
        val apiService = RetrofitClientServer.instance

        val data = BbsCmntUpdateReq(cmntCn = cmntCn, pstCmntNo = pstCmntNo)

        val call = apiService.bbsCmntUpdate(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<BbsCmntCreateRes>{
                override fun onResponse(call: Call<BbsCmntCreateRes>, response: Response<BbsCmntCreateRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let { it ->
                            if (it.statusCode == 200){
                                _eventCmntList.value = it.data
                                continuation.resume(true)
                            }else{

                                continuation.resume(false)
                            }
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<BbsCmntCreateRes>, t: Throwable) {
                    continuation.resume(false)
                }

            })
        }
    }
    suspend fun bbsRcmdtnComment(pstCmntNo:Int ,rcmdtnSeCd:String, pstSn:Int):Boolean{
        val apiService = RetrofitClientServer.instance

        val data = BbsCmntRcmdtnReq(
            pstCmntNo = pstCmntNo,
            pstSn = pstSn,
            rcmdtnSeCd = rcmdtnSeCd
        )

        val call = apiService.bbsCmntRcmdtn(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<BbsCmntCreateRes>{
                override fun onResponse(call: Call<BbsCmntCreateRes>, response: Response<BbsCmntCreateRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let { it ->
                            if (it.statusCode == 200){
                                _eventCmntList.value = it.data
                                continuation.resume(true)
                            }else{

                                continuation.resume(false)
                            }
                        }
                    }else{
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<BbsCmntCreateRes>, t: Throwable) {
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

        val call = apiService.uploadPhoto(parts)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<PhotoRes> {
                override fun onResponse(call: Call<PhotoRes>, response: Response<PhotoRes>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        body?.let {
                            val dailyLifeFiles = body.data.map { photoData ->
                                DailyLifeFile(
                                    atchFileNm = photoData.atchFileNm,
                                    atchFileSz = photoData.atchFileSz,
                                    atchFileSn = null,
                                    fileExtnNm = photoData.fileExtnNm,
                                    filePathNm = photoData.filePathNm,
                                    flmPstnLat = photoData.flmPstnLat,
                                    flmPstnLot = photoData.flmPstnLot,
                                    orgnlAtchFileNm = photoData.orgnlAtchFileNm,
                                    rowState = "C",
                                    schUnqNo = null
                                )
                            }
                            _newFileList.value = dailyLifeFiles
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

    fun concatUploadedFileList(newFiles: List<DailyLifeFile>) {
        val currentList = _uploadedFileList.value ?: emptyList()
        val combinedList = mutableListOf<DailyLifeFile>().apply {
            addAll(currentList)
            addAll(newFiles)
        }
        _uploadedFileList.value = combinedList
    }
}
