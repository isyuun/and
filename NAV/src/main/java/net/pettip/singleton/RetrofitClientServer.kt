/*
 *  * Copyright(c) 2023 PetTip All right reserved.
 *  * This software is the proprietary information of PetTip.
 *  *
 *  * Revision History
 *  * Author                         Date             Description
 *  * --------------------------    -------------    ----------------------------------------
 *  * sanghun.lee@care-biz.co.kr     2023. 7. 28.    PetTip Development...
 *  *
 */

@file:OptIn(ExperimentalCoroutinesApi::class)

package net.pettip.singleton

import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.pettip.DEBUG
import net.pettip.service.ApiService
import net.pettip.service.RetryInterceptor
import net.pettip.service.TokenInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object RetrofitClientServer {
    // 서버쪽 retrofit 싱글톤 객체

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
        if (DEBUG){
            OkHttpClient.Builder()
                .addInterceptor(tokenInterceptor)
                .addInterceptor(loggingInterceptor)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
        }else{
            OkHttpClient.Builder()
                .addInterceptor(tokenInterceptor)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
        }
    }

}



