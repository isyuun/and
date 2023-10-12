package kr.carepet.service

import android.util.Log
import com.google.gson.Gson
import kr.carepet.data.RefreshRes
import kr.carepet.singleton.G
import kr.carepet.singleton.MySharedPreference
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.internal.commonNewBuilder
import okhttp3.internal.stripBody
import org.json.JSONObject
import kotlin.reflect.typeOf

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

        if (response.code == 401){
            Log.d("LOG","if 실행")
            val gson = Gson()
            val tokenResponse = gson.fromJson(responseBodyString.string(), RefreshRes::class.java)

            G.accessToken = tokenResponse.data.accessToken
            G.refreshToken = tokenResponse.data.refreshToken
            MySharedPreference.setAccessToken(tokenResponse.data.accessToken)
            MySharedPreference.setRefreshToken(tokenResponse.data.refreshToken)

            if (G.accessToken != null){
                val retryRequestBuilder = originalRequest.newBuilder()
                retryRequestBuilder.header("Authorization", G.accessToken)
                retryRequestBuilder.header("Refresh", G.refreshToken)

                val retryRequestWithToken = retryRequestBuilder.build()
                return chain.proceed(retryRequestWithToken)
            }
        }


        return response
    }

}
