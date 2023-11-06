package kr.carepet.app.navi.viewmodel

import androidx.lifecycle.ViewModel
import com.bumptech.glide.annotation.GlideModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kr.carepet.data.bbs.EventDetailRes
import kr.carepet.data.bbs.EventListRes
import kr.carepet.data.user.BbsReq
import kr.carepet.singleton.RetrofitClientServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume


@GlideModule
class CommunityViewModel(private val sharedViewModel: SharedViewModel) :ViewModel(){

    private val _dm = MutableStateFlow<String>("")
    val dm: StateFlow<String> = _dm.asStateFlow()

    private val _comment = MutableStateFlow<String>("")
    val comment: StateFlow<String> = _comment.asStateFlow()
    fun updateComment(newValue: String) { _comment.value = newValue }

    private val _eventList = MutableStateFlow<EventListRes?>(null)
    val eventList: StateFlow<EventListRes?> = _eventList.asStateFlow()

    private val _eventDetail = MutableStateFlow<EventDetailRes?>(null)
    val eventDetail: StateFlow<EventDetailRes?> = _eventDetail.asStateFlow()

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
}