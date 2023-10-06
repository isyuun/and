/*
 *  * Copyright(c) 2023 CarePat All right reserved.
 *  * This software is the proprietary information of CarePet.
 *  *
 *  * Revision History
 *  * Author                         Date             Description
 *  * --------------------------    -------------    ----------------------------------------
 *  * sanghun.lee@care-biz.co.kr     2023. 7. 28.    CarePet Development...
 *  *
 */

@file:OptIn(ExperimentalCoroutinesApi::class)

package kr.carepet.singleton

import android.util.Log
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kr.carepet.data.RefreshRes
import kr.carepet.data.RefreshToken
import kr.carepet.service.ApiService
import kr.carepet.service.TokenInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.time.Duration
import java.util.Date
import kotlin.coroutines.resume


object RetrofitClientServer {
    // 서버쪽 retrofit 싱글톤 객체

    private const val BASE_URL = "http://carepet.hopto.org:8020/"
    private val tokenInterceptor = TokenInterceptor()

    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // 로깅 레벨 설정
    }

    val instance : ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create()
    }

    val okHttpClient : OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(tokenInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    val apiInstanceForToken : ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create()
    }

}

fun setTokenReceivedTime() {
    kr.carepet.singleton.G.tokenReceivedTime = Date()
}

fun isTokenExpired(): Boolean {
    if (G.tokenReceivedTime == null) {
        // 토큰을 아직 받지 않았거나 시간을 초기화하지 않은 경우
        return true
    }

    val currentTime = Date()
    val elapsedMinutes = Duration.between(G.tokenReceivedTime!!.toInstant(), currentTime.toInstant()).toMinutes()

    return elapsedMinutes >= 30
}

class TokenManager {
    suspend fun refreshAccessToken():String{
        val apiService = RetrofitClientServer.instance

        val refreshToken = kr.carepet.singleton.G.refreshToken

        val call = apiService.sendRefreshToken(RefreshToken(refreshToken))
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback<RefreshRes> {
                override fun onResponse(call: Call<RefreshRes>, response: Response<RefreshRes>) {
                    if (response.isSuccessful){
                        val body = response.body()
                        body?.let {
                            if (it.statusCode==200){
                                kr.carepet.singleton.G.accessToken = it.data.accessToken
                                kr.carepet.singleton.G.refreshToken = it.data.refreshToken
                                kr.carepet.singleton.G.userId = it.data.userId
                                setTokenReceivedTime()

                                kr.carepet.singleton.MySharedPreference.setAccessToken(it.data.accessToken)
                                kr.carepet.singleton.MySharedPreference.setRefreshToken(it.data.refreshToken)
                                kr.carepet.singleton.MySharedPreference.setUserId(it.data.userId)
                                Log.d(
                                    "Token",
                                    "access: ${it.data.accessToken}, refresh: ${it.data.refreshToken}"
                                )

                                continuation.resume(it.data.accessToken)
                            }else{
                                continuation.resume("")
                            }
                        }
                    }
                }
                override fun onFailure(call: Call<RefreshRes>, t: Throwable) {
                    continuation.resume("")
                }
            })
        }
    }
}