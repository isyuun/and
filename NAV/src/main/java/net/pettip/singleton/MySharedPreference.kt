package net.pettip.singleton

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.pettip.data.pet.CurrentPetData
import net.pettip.util.Log
import java.time.LocalDate

object MySharedPreference {
    private const val DATA = "data"
    private const val KEY_WEATHER = "weather"
    private const val KEY_USERID = "userId"
    private const val KEY_EMAIL = "email"
    private const val DEFAULT_VALUE = ""

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(DATA, Context.MODE_PRIVATE)
    }

    fun getTodayData(): Boolean {
        //today key값의 데이터를 호출, 없으면 false 반환
        val today = LocalDate.now().toString()
        Log.d("today", today)
        return sharedPreferences.getBoolean(today, false)
    }


    fun setTodayData() {
        //today key값을 true로 설정
        val today = LocalDate.now().toString()
        sharedPreferences.edit().putBoolean(today, true).apply()
        Log.d("today", "성공")
    }

    fun setFcmToken(fcmToken: String) {
        sharedPreferences.edit().putString("FcmToken", fcmToken).apply()
    }

    fun getFcmToken(): String {
        return sharedPreferences.getString("FcmToken", "").toString()
    }

    fun setAccessToken(accessToken: String?) {
        sharedPreferences.edit().putString("AccessToken", accessToken).apply()
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString("AccessToken", null)
    }

    fun setRefreshToken(refreshToken: String?) {
        sharedPreferences.edit().putString("RefreshToken", refreshToken).apply()
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString("RefreshToken", null)
    }

    fun setFcmDataPage(page: String) {
        sharedPreferences.edit().putString("page", page).apply()
    }

    fun getFcmDataPage(): String {
        return sharedPreferences.getString("page", "").toString()
    }

    fun setFcmDataSchUnqNo(schUnqNo: String) {
        sharedPreferences.edit().putString("schUnqNo", schUnqNo).apply()
    }

    fun getFcmDataSchUnqNo(): String {
        return sharedPreferences.getString("schUnqNo", "").toString()
    }

    fun setIsLogin(loggined: Boolean) {
        sharedPreferences.edit().putBoolean("isLogin", loggined).apply()
    }

    fun getIsLogin(): Boolean {
        return sharedPreferences.getBoolean("isLogin", false)
    }

    fun setLastLoginMethod(method: String) {
        sharedPreferences.edit().putString("lastLoginMethod", method).apply()
    }

    fun getLastLoginMethod(): String {
        return sharedPreferences.getString("lastLoginMethod", "").toString()
    }

    fun setUserId(userId: String) {
        sharedPreferences.edit().putString("userId", userId).apply()
    }

    fun getUserId(): String {
        return sharedPreferences.getString("userId", "").toString()
    }

    fun setUserEmail(userEmail: String) {
        sharedPreferences.edit().putString("userEmail", userEmail).apply()
    }

    fun getUserEmail(): String {
        return sharedPreferences.getString("userEmail", "").toString()
    }

    fun setUserNickName(newValue: String?) {
        sharedPreferences.edit().putString("userNickName", newValue).apply()
    }

    fun getUserNickName(): String {
        return sharedPreferences.getString("userNickName", "").toString()
    }

    fun setLastInviteCode(inviteCode: String) {
        sharedPreferences.edit().putString("inviteCode", inviteCode).apply()
    }

    fun getLastInviteCode(): String {
        return sharedPreferences.getString("inviteCode", "").toString()
    }

    fun setCurrentPetData(newValue: List<CurrentPetData>) {
        val gson = Gson()
        val json = gson.toJson(newValue)
        sharedPreferences.edit().putString("currentPetData", json).apply()
    }

    fun getCurrentPetData(): List<CurrentPetData>? {
        val json = sharedPreferences.getString("currentPetData", null)
        val gson = Gson()
        return gson.fromJson(json, object : TypeToken<List<CurrentPetData>>() {}.type)
    }
}