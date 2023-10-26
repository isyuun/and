package kr.carepet.app.navi.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kr.carepet.data.daily.DailyCreateReq
import kr.carepet.data.daily.DailyCreateRes
import kr.carepet.data.daily.DailyDetailData
import kr.carepet.data.daily.DailyDetailRes
import kr.carepet.data.daily.DailyLifeWalk
import kr.carepet.data.daily.DailyMonthData
import kr.carepet.data.daily.DailyMonthRes
import kr.carepet.data.daily.Paginate
import kr.carepet.data.daily.Pet
import kr.carepet.data.daily.PhotoData
import kr.carepet.data.daily.PhotoRes
import kr.carepet.data.daily.WalkListRes
import kr.carepet.data.daily.WeekData
import kr.carepet.data.pet.PetDetailData
import kr.carepet.gps.app.GPSApplication
import kr.carepet.singleton.RetrofitClientServer
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Integer.min
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Random
import kotlin.coroutines.resume


class WalkViewModel(private val sharedViewModel: SharedViewModel) : ViewModel() {

    val weekRecord: StateFlow<WeekData?> = sharedViewModel.weekRecord
    fun updatePetInfo(newData: List<PetDetailData>) {
        sharedViewModel.updatePetInfo(newData)
    }

    suspend fun callGetWeekRecord(ownrPetUnqNo: String, searchDay: String) {
        sharedViewModel.getWeekRecord(ownrPetUnqNo, searchDay)
    }

    private val _dailyMonth = MutableStateFlow<DailyMonthData?>(null)
    val dailyMonth: StateFlow<DailyMonthData?> = _dailyMonth.asStateFlow()

    private val _toDetail = MutableStateFlow(false)

    // 검색 중인지 여부를 StateFlow로 노출
    val toDetail = _toDetail.asStateFlow()
    fun updateToDetail(newValue: Boolean) {
        _toDetail.value = newValue
    }

    private val _toMonthCalendar = MutableStateFlow(false)

    // 검색 중인지 여부를 StateFlow로 노출
    val toMonthCalendar = _toMonthCalendar.asStateFlow()
    fun updateToMonthCalendar(newValue: Boolean) {
        _toMonthCalendar.value = newValue
    }

    private val _walkListItem = MutableStateFlow<DailyLifeWalk?>(null)
    val walkListItem: StateFlow<DailyLifeWalk?> = _walkListItem.asStateFlow()
    fun updateWalkListItem(newValue: DailyLifeWalk) {
        _walkListItem.value = newValue
    }

    val petInfo: StateFlow<List<PetDetailData>> = sharedViewModel.petInfo

    private val _walkList = MutableStateFlow<List<DailyLifeWalk>>(emptyList())
    val walkList: StateFlow<List<DailyLifeWalk>> = _walkList.asStateFlow()

    private val _page = MutableStateFlow<Paginate?>(null)
    val page: StateFlow<Paginate?> = _page.asStateFlow()

    private val _dailyDetail = MutableStateFlow<DailyDetailData?>(null)
    val dailyDetail: StateFlow<DailyDetailData?> = _dailyDetail.asStateFlow()
    fun updateDailyDetail(newValue : DailyDetailData?){ _dailyDetail.value = newValue }

    private val _selectPet = MutableStateFlow<List<PetDetailData>>(emptyList())
    val selectPet: StateFlow<List<PetDetailData>> = _selectPet.asStateFlow()
    fun updateSelectPet(newValue: List<PetDetailData>) {
        _selectPet.value = newValue
    }

    private val _isLoading = MutableStateFlow(false)

    // 검색 중인지 여부를 StateFlow로 노출
    val isLoading = _isLoading.asStateFlow()
    fun updateIsLoading(newValue: Boolean) {
        _isLoading.value = newValue
    }

    private val _isWalking = MutableStateFlow(false)

    // 검색 중인지 여부를 StateFlow로 노출
    val isWalking = _isWalking.asStateFlow()
    fun updateIsWalking(newValue: Boolean) {
        _isWalking.value = newValue
    }

    private val _sheetChange = MutableStateFlow("select")

    // 검색 중인지 여부를 StateFlow로 노출
    val sheetChange = _sheetChange.asStateFlow()
    fun updateSheetChange(newValue: String) {
        _sheetChange.value = newValue
    }

    private val _photoRes = MutableStateFlow<List<PhotoData>>(emptyList())

    // 검색 중인지 여부를 StateFlow로 노출
    val photoRes = _photoRes.asStateFlow()
    fun updatePhotoRes(newValue: List<PhotoData>) {
        _photoRes.value = newValue
    }

    //------------------------------------------------------------------------------//

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

    private val _petCount = MutableStateFlow<List<Pet>>(emptyList())
    val petCount: StateFlow<List<Pet>> = _petCount.asStateFlow()
    fun updatePetCount(newValue: List<Pet>) {
        _petCount.value = newValue
    }

    private val _schCdList = MutableStateFlow<List<String>>(emptyList())
    val schCdList: StateFlow<List<String>> = _schCdList.asStateFlow()
    fun updateSchCdList(newValue: List<String>) {
        _schCdList.value = newValue
    }

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

    suspend fun getWalkList(page: Int, petUnqNo: String): Boolean {
        val apiService = RetrofitClientServer.instance

        val data = kr.carepet.data.daily.WalkListReq(ownrPetUnqNo = petUnqNo, page = page, 10, 20)

        val call = apiService.getWalkList(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<WalkListRes> {
                override fun onResponse(call: Call<WalkListRes>, response: Response<WalkListRes>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        body?.let {
                            if (body.statusCode == 200) {
                                _walkList.value = body.data.dailyLifeWalkList
                                _page.value = body.data.paginate

                                continuation.resume(true)
                            } else {
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

    suspend fun getDailyDetail(schUnqNo: Int): Boolean {
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

                                _dailyDetail.value = body.data

                                continuation.resume(true)
                            } else {
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

    suspend fun getMonthData(ownrPetUnqNo: String, searchMonth: String): Boolean {
        val apiService = RetrofitClientServer.instance

        val data = kr.carepet.data.daily.DailyMonthReq(ownrPetUnqNo, searchMonth)

        val call = apiService.getMonthData(data)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<DailyMonthRes> {
                override fun onResponse(
                    call: Call<DailyMonthRes>,
                    response: Response<DailyMonthRes>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        body?.let {
                            _dailyMonth.value = it.data
                            continuation.resume(true)
                        }
                    } else {
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<DailyMonthRes>, t: Throwable) {
                    continuation.resume(false)
                }
            })
        }
    }

    suspend fun fileUpload(context: Context, gpxFile:File?): Boolean {
        val apiService = RetrofitClientServer.instance

        val parts = ArrayList<MultipartBody.Part>()
        //var files: File?

        val maxImages = 5

        for (i in 0 until min(maxImages, state.listOfSelectedImages.size - 1)) {
            val fileUri = state.listOfSelectedImages[i]
            val resizedFile = resizeImage(context, fileUri, i)

            resizedFile?.let {
                val requestBody = it.asRequestBody("image/*".toMediaType())
                val part = MultipartBody.Part.createFormData("files", "image$i.jpg", requestBody)
                parts.add(part)
            }
        }

        gpxFile?.let { gpxFile ->
            val copyFile = copyToFile(gpxFile, context)

            copyFile?.let{
                val requestBody = copyFile.asRequestBody("application/gpx+xml".toMediaType())
                val part = MultipartBody.Part.createFormData("gpx", copyFile?.name, requestBody)
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

    // _ 타임, 거리가 실제 시간/거리
    suspend fun uploadDaily(): Boolean {
        val application = GPSApplication.instance

        val apiService = RetrofitClientServer.instance

        val data = DailyCreateReq(
            cmntUseYn = "Y",// 댓글 사용 여부
            files = _photoRes.value,
            hashTag = _hashTag.value,
            pet = _petCount.value,
            rcmdtnYn = "Y", // 추천 여부
            rlsYn =  if (_postStory.value) "Y" else "N", // 공개 여부
            schCdList = _schCdList.value,
            schTtl = _walkTitle.value,
            schCn = _walkMemo.value,
            totClr = 300f,
            totDstnc = application._distance?: 0.0f,
            walkDptreDt = formatTimestampToCustomString(application.tracks?.first()?.time?:0),
            walkEndDt = formatTimestampToCustomString(application.tracks?.last()?.time?:0)
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

                                _photoRes.value = emptyList()
                                _petCount.value = emptyList()
                                _hashTag.value = emptyList()
                                _schCdList.value = emptyList()
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


}

data class MainScreenState(
    val listOfSelectedImages: List<Uri> = emptyList()
)

fun formatTimestampToCustomString(timestamp: Long): String {
    val date = Date(timestamp)
    val sdf = SimpleDateFormat("yyyyMMddHHmmss")
    return sdf.format(date)
}

fun copyToFile(gpxFile: File?, context : Context):File?{

    val gpxFilePath = gpxFile?.path
    val uri: Uri = Uri.fromFile(File(gpxFilePath))

    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(uri)

    if (inputStream != null) {
        // 복사할 파일 경로와 이름 설정

        val randomNumber = Random().nextInt(10000)
        val fileName = gpxFile?.name ?: "${randomNumber}.gpx"
        val file = File(context.filesDir, fileName)
        val files = try {
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)

            outputStream.close()
            inputStream?.close()

            file
        } catch (e: IOException) {
            e.printStackTrace()
            null// 변환 실패 시 null로 업데이트
        }
        return files
    }
    return null

}

suspend fun resizeImage(context: Context, fileUri: Uri, index: Int): File? {
    try {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(fileUri)
        if (inputStream != null) {
            // 원본 이미지를 Bitmap으로 디코딩
            val originalBitmap = BitmapFactory.decodeStream(inputStream)

            // 원본 이미지 파일 크기가 2MB 이하면 원본 이미지 반환
            if (originalBitmap.byteCount <= 2 * 1024 * 1024) {

                val fileName = "image${index}.jpg"
                val file = File(context.filesDir, fileName)
                val files = try {
                    val outputStream = FileOutputStream(file)
                    inputStream?.copyTo(outputStream)

                    outputStream.close()
                    inputStream?.close()

                    file // 변환된 File 객체를 StateFlow에 업데이트
                } catch (e: IOException) {
                    e.printStackTrace()
                    null// 변환 실패 시 null로 업데이트
                }
                return files

            }else{

                val quality = 100 // 원하는 품질로 조절

                // 이미지의 종횡비 비율 유지하며 크기 조절
                val maxWidth = 1920 // 최대 가로 크기 (원하는 크기로 조절)
                val maxHeight = 1080 // 최대 세로 크기 (원하는 크기로 조절)
                val newWidth: Int
                val newHeight: Int
                if (originalBitmap.width > originalBitmap.height) {
                    newWidth = min(originalBitmap.width, maxWidth)
                    newHeight = (newWidth.toFloat() / originalBitmap.width * originalBitmap.height).toInt()
                } else {
                    newHeight = min(originalBitmap.height, maxHeight)
                    newWidth = (newHeight.toFloat() / originalBitmap.height * originalBitmap.width).toInt()
                }

                // 크기 조절된 이미지를 생성
                val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true)

                // 이미지 파일 저장
                val cacheDir = context.cacheDir
                val fileName = "image${index}"
                val resizedFile = File(cacheDir, fileName)
                val outputStream = FileOutputStream(resizedFile)

                // 이미지를 JPEG 형식으로 저장 (품질 설정 적용)
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                outputStream.close()

                inputStream.run { close() }
                return resizedFile
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}
