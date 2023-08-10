package kr.carepet.service.app.navi.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kr.carepet.service.app.navi.R
import kr.carepet.service.app.navi.databinding.ActivityPetRegistBinding
import kr.carepet.model.MyPetResModel
import kr.carepet.model.PetModel
import kr.carepet.model.RefreshRes
import kr.carepet.model.RefreshToken
import kr.carepet.service.app.navi.service.ApiService
import kr.carepet.service.app.navi.singleton.G
import kr.carepet.service.app.navi.singleton.MySharedPreference
import kr.carepet.service.app.navi.singleton.RetrofitClientServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume

class PetRegistActivity : AppCompatActivity() {

    private val binding by lazy { ActivityPetRegistBinding.inflate(layoutInflater) }
    private val apiService = RetrofitClientServer.instance
    private val subApiService = RetrofitClientServer.apiInstanceForToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.petBtn.setOnClickListener {

            getEditData()

            tokenAsyncTask()
        }
    }

    private fun getEditData() {
        petBrthYmd = binding.petrgEtBirth.text.toString()
        petInfoUnqNo = binding.petrgEtUnqno.text.toString().toInt()
        petNm = binding.petrgEtNm.text.toString()
        petRelCd = binding.petrgEtRelcd.text.toString()
        petRprsYn = binding.petrgEtRqrsyn.text.toString()
        stdgCtpvCd = binding.petrgEtCypvcd.text.toString().toInt()
        stdgEmdCd = binding.petrgEtEmdcd.text.toString().toInt()
        stdgSggCd = binding.petrgEtSggcd.text.toString().toInt()

        data = PetModel(petBrthYmd, petInfoUnqNo, petNm, petRelCd, petRprsYn, stdgCtpvCd, stdgEmdCd, stdgSggCd)
        Log.d("DATA",data.toString())
    }


    private fun tokenAsyncTask()= runBlocking {
        Log.d("ASYNC","async start")
        val accessToken = async { sendWithAT() }.await()
        val requestRefresh = async { sendWithRT(accessToken)  }.await()
        val newAccessToken = async { sendWithNewAT(requestRefresh) }.await()
    }

    suspend fun sendWithAT():Boolean = suspendCancellableCoroutine { continuation ->

        //Access Token 을 담아 보내고, status code가 200이 날라오면 작업종료, 아니면 requestRefresh 실행

        val call=apiService.sendPetDataToServer(data)
        call.enqueue(object : Callback<MyPetResModel>{
            override fun onResponse(
                call: Call<MyPetResModel>,
                response: Response<MyPetResModel>
            ) {
                val body = response.body()
                if(response.isSuccessful){
                    Toast.makeText(this@PetRegistActivity, "통신성공", Toast.LENGTH_SHORT).show()
                    Log.d("PET","${body?.data},${body?.statusCode},${body?.detailMessage},${body?.data}, ${body?.resultMessage}")
                    Log.d("ASYNC","sendWithAT-response")

                    if (body?.statusCode==200) continuation.resume(true) else continuation.resume(false)
                }
            }

            override fun onFailure(call: Call<MyPetResModel>, t: Throwable) {
                Toast.makeText(this@PetRegistActivity, "통신실패", Toast.LENGTH_SHORT).show()
                continuation.resume(false)
            }

        })

        // 코루틴이 취소되면 API도 취소
        continuation.invokeOnCancellation {
            call.cancel()
        }
    }

    suspend fun sendWithRT(previous: Boolean):Boolean = suspendCancellableCoroutine { continuation ->
        if(!previous){
            val call=subApiService.sendRefreshToken(RefreshToken(G.refreshToken))
            call.enqueue(object :Callback<RefreshRes>{
                override fun onResponse(call: Call<RefreshRes>, response: Response<RefreshRes>) {
                    val body = response.body()
                    if(response.isSuccessful){
                        body?.let {
                            G.accessToken = body.data.accessToken
                            G.refreshToken = body.data.refreshToken

                            MySharedPreference.setAccessToken(body.data.accessToken)
                            MySharedPreference.setRefreshToken(body.data.refreshToken)
                            Log.d("ASYNC", "sendWithRT ${G.accessToken}, ${G.refreshToken}")
                            if (body.statusCode ==200) continuation.resume(true) else continuation.resume(false)
                        }
                    }
                }

                override fun onFailure(call: Call<RefreshRes>, t: Throwable) {
                    Log.d("ASYNC", "sendWithRT failure")
                }

            })
        }else{
            Log.d("ASYNC","sendWithRT-else")
        }

    }

    suspend fun sendWithNewAT(previous: Boolean){

        if (previous){
            val call=apiService.sendPetDataToServer(data)
            call.enqueue(object : Callback<MyPetResModel>{
                override fun onResponse(
                    call: Call<MyPetResModel>,
                    response: Response<MyPetResModel>
                ) {
                    val body = response.body()
                    if(response.isSuccessful){
                        Toast.makeText(this@PetRegistActivity, "통신성공", Toast.LENGTH_SHORT).show()
                        Log.d("PET","${body?.data},${body?.statusCode},${body?.detailMessage},${body?.data}, ${body?.resultMessage}")
                        Log.d("ASYNC","sendWithNewAT-response")
                    }
                }

                override fun onFailure(call: Call<MyPetResModel>, t: Throwable) {
                    Toast.makeText(this@PetRegistActivity, "통신실패", Toast.LENGTH_SHORT).show()
                }

            })
        }else{
            Log.d("ASYNC", "sendWithNewAT-else")
        }
    }


    private var petBrthYmd:String = ""
    private var petInfoUnqNo:Int = 0
    private var petNm:String = ""
    private var petRelCd:String = ""
    private var petRprsYn:String = ""
    private var stdgCtpvCd:Int = 0
    private var stdgEmdCd:Int = 0
    private var stdgSggCd:Int = 0

    var data:PetModel = PetModel(petBrthYmd, petInfoUnqNo, petNm, petRelCd, petRprsYn, stdgCtpvCd, stdgEmdCd, stdgSggCd)
}