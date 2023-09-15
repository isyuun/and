package kr.carepet.service

import kr.carepet.singleton.G
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // 원래의 요청을 가져옵니다.
        val originalRequest = chain.request()

        val token = G.accessToken
        val requestWithToken = originalRequest.newBuilder()
            .header("Authorization", token)
            .build()

        val response = chain.proceed(requestWithToken)

        return response
    }
}
