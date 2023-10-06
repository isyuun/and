package kr.carepet.app.navi.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kr.carepet.data.daily.DailyCreateReq
import kr.carepet.data.daily.DailyCreateRes
import kr.carepet.data.daily.Pet
import kr.carepet.data.daily.PhotoData
import kr.carepet.data.daily.PhotoRes
import kr.carepet.singleton.RetrofitClientServer
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume


@GlideModule
class CommunityViewModel(private val sharedViewModel: SharedViewModel) :ViewModel(){

    private val _comment = MutableStateFlow<String>("")
    val comment: StateFlow<String> = _comment.asStateFlow()
    fun updateComment(newValue: String) { _comment.value = newValue }
}