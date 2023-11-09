/*
 *  * Copyright(c) 2023 PetTip All right reserved.
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
import kr.carepet.service.OAuthAuthenticator
import kr.carepet.service.RetryInterceptor
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
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume


object RetrofitClientServer {
    // 서버쪽 retrofit 싱글톤 객체

    private const val BASE_URL = "http://carepet.hopto.org:8020/"
    private val tokenInterceptor = TokenInterceptor()
    private val retryInterceptor = RetryInterceptor(3)

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // 로깅 레벨 설정
    }

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create()
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(tokenInterceptor)
            .addInterceptor(loggingInterceptor)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

}



