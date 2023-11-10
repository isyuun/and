package kr.carepet.service

import android.util.Log
import kr.carepet.data.RefreshToken
import kr.carepet.singleton.G
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class OAuthAuthenticator(private val apiService: ApiService) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.code == 999) {
            Log.d("Authenticator", "Authenticator is called for route: $route")
            // The access token is expired. Refresh the token.
            val newAccessToken = refreshAccessToken()
            if (newAccessToken != null) {
                // Update the access token in your storage.
                updateAccessToken(newAccessToken)
                // Retry the request with the new access token.
                Log.d("AUTH", "999")
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $newAccessToken")
                    .build()
            }
        }

        Log.d("AUTH", "null")
        // If the request can't be retried, return null to indicate failure.
        return null
    }

    private fun refreshAccessToken(): String? {
        // Use your API service to send a request to refresh the access token.
        val refreshToken = getRefreshToken() // Retrieve the refresh token from local storage.
        val response = apiService.sendRefreshToken(refreshToken).execute()

        if (response.isSuccessful) {
            // Extract the new access token from the response.
            val newAccessToken = response.body()?.data?.accessToken
            G.refreshToken = response.body()?.data?.refreshToken ?: ""
            return newAccessToken
        }

        // The refresh token request failed, handle it accordingly.
        return null
    }

    private fun getRefreshToken(): RefreshToken {
        // Retrieve the refresh token from local storage.
        return RefreshToken(G.refreshToken)
    }

    private fun updateAccessToken(newAccessToken: String) {
        // Update the access token in your storage.
        G.accessToken = newAccessToken
    }
}