package kr.carepet.service

import android.util.Log
import kr.carepet.singleton.G
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // 원래의 요청을 가져옵니다.
        val originalRequest = chain.request()

        val token = G.accessToken
        val requestWithToken = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        val response = chain.proceed(requestWithToken)
        Log.d("INTERCEPT", "기존 response 반환${response.code}//${token}")

        return response
    }
}
