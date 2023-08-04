package kr.carepet.service.app.navi.service

import android.util.Log
import kr.carepet.service.app.navi.singleton.G
import kr.carepet.service.app.navi.singleton.MySharedPreference
import kr.carepet.service.app.navi.singleton.RetrofitClientServer
import okhttp3.*

class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // 원래의 요청을 가져옵니다.
        val originalRequest = chain.request()

        val token = G.accessToken
        val requestWithToken = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        val response = chain.proceed(requestWithToken)

        if (response.code == 401) {
            val refreshedToken = G.refreshToken
            val requestAccesToken = RetrofitClientServer.apiInstanceForToken

            val subCall= requestAccesToken.sendRefreshToken(refreshedToken)
            if(subCall.execute().isSuccessful){
                val subCallBody = subCall.execute().body()
                G.accessToken = subCallBody?.data?.accessToken.toString()
                G.refreshToken = subCallBody?.data?.refreshToken.toString()

                MySharedPreference.setAccessToken(subCallBody?.data?.accessToken.toString())
                MySharedPreference.setRefreshToken(subCallBody?.data?.refreshToken.toString())

                Log.d("RETROFIT","${G.accessToken}///${G.refreshToken}")
            }

            if (G.accessToken != null) {
                // 새로운 토큰을 사용하여 요청을 다시 보냅니다.
                val newRequestWithToken = originalRequest.newBuilder()
                    .header("Authorization", "Bearer ${G.accessToken}")
                    .build()
                return chain.proceed(newRequestWithToken)
            }
        }

        return response
    }
}
