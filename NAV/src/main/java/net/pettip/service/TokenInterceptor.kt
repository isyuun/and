package net.pettip.service

import android.util.Log
import com.google.gson.Gson
import net.pettip.data.RefreshRes
import net.pettip.singleton.G
import net.pettip.singleton.MySharedPreference
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class TokenInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // 원래의 요청을 가져옵니다.
        val originalRequest = chain.request()

        val token = G.accessToken
        val refreshToken = G.refreshToken

        val requestBuilder = originalRequest.newBuilder()
        if (token != null) {
            // AccessToken이 있는 경우 Authorization 헤더를 추가
            requestBuilder.header("Authorization", token)
        }
        if (refreshToken != null) {
            // RefreshToken이 있는 경우 Refresh 헤더를 추가
            requestBuilder.header("Refresh", refreshToken)
        }


        val requestWithToken = requestBuilder.build()
        val response = chain.proceed(requestWithToken)

        val responseBodyString = response.body

        if (response.code == 401) {
            Log.d("LOG", "if 실행")
            val gson = Gson()
            val tokenResponse = gson.fromJson(responseBodyString.string(), RefreshRes::class.java)

            G.accessToken = tokenResponse.data.accessToken
            G.refreshToken = tokenResponse.data.refreshToken
            MySharedPreference.setAccessToken(tokenResponse.data.accessToken)
            MySharedPreference.setRefreshToken(tokenResponse.data.refreshToken)

            if (G.accessToken != null) {
                val retryRequestBuilder = originalRequest.newBuilder()
                retryRequestBuilder.header("Authorization", G.accessToken)
                retryRequestBuilder.header("Refresh", G.refreshToken)

                val retryRequestWithToken = retryRequestBuilder.build()
                return chain.proceed(retryRequestWithToken)
            }
        }

        if (response.code == 403){
            G.dupleLogin = true
        }

        return response
    }

}

class RetryInterceptor(private val maxRetries: Int) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response: Response? = null
        var retryCount = 0

        // 최대 재시도 횟수에 도달할 때까지 재시도합니다.
        while (retryCount < maxRetries) {
            try {
                response = chain.proceed(request)
                if (response.isSuccessful) {
                    return response
                }
            } catch (e: IOException) {
                // 네트워크 오류가 발생한 경우 재시도합니다.
                if (retryCount >= maxRetries) {
                    throw e
                }
            } finally {
                retryCount++
            }
        }

        return response ?: throw IOException("Request failed after $maxRetries retries")
    }
}