package kr.carepet.service

import kotlinx.coroutines.runBlocking
import kr.carepet.singleton.G
import kr.carepet.singleton.TokenManager
import kr.carepet.singleton.isTokenExpired
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class TokenInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // 원래의 요청을 가져옵니다.
        val originalRequest = chain.request()

        val token = kr.carepet.singleton.G.accessToken
        val requestWithToken = originalRequest.newBuilder()
            .header("Authorization", token)
            .build()

        val response = chain.proceed(requestWithToken)

        return response
    }
}
