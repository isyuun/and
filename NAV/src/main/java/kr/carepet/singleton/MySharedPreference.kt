package kr.carepet.singleton

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import java.time.LocalDate

object MySharedPreference {
    private const val DATA = "data"
    private const val KEY_WEATHER = "weather"
    private const val KEY_USERID = "userId"
    private const val KEY_EMAIL = "email"
    private const val DEFAULT_VALUE = ""

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        kr.carepet.singleton.MySharedPreference.sharedPreferences = context.getSharedPreferences(kr.carepet.singleton.MySharedPreference.DATA, Context.MODE_PRIVATE)
    }

    fun getTodayData(): Boolean {
        //today key값의 데이터를 호출, 없으면 false 반환
        val today = LocalDate.now().toString()
        Log.d("today",today)
        return kr.carepet.singleton.MySharedPreference.sharedPreferences.getBoolean(today, false)
    }


    fun setTodayData() {
        //today key값을 true로 설정
        val today = LocalDate.now().toString()
        kr.carepet.singleton.MySharedPreference.sharedPreferences.edit().putBoolean(today, true).apply()
        Log.d("today","성공")
    }

    fun setFcmToken(fcmToken:String){
        kr.carepet.singleton.MySharedPreference.sharedPreferences.edit().putString("FcmToken", fcmToken).apply()
    }

    fun getFcmToken():String{
        return kr.carepet.singleton.MySharedPreference.sharedPreferences.getString("FcmToken","").toString()
    }

    fun setAccessToken(accessToken :String){
        kr.carepet.singleton.MySharedPreference.sharedPreferences.edit().putString("AccessToken",accessToken).apply()
    }

    fun getAccessToken():String{
        return kr.carepet.singleton.MySharedPreference.sharedPreferences.getString("AccessToken","").toString()
    }

    fun setRefreshToken(refreshToken:String){
        kr.carepet.singleton.MySharedPreference.sharedPreferences.edit().putString("RefreshToken",refreshToken).apply()
    }

    fun getRefreshToken():String{
        return kr.carepet.singleton.MySharedPreference.sharedPreferences.getString("RefreshToken"," ").toString()
    }

    fun setIsLogin(loggined:Boolean){
        kr.carepet.singleton.MySharedPreference.sharedPreferences.edit().putBoolean("isLogin",loggined).apply()
    }
    fun getIsLogin():Boolean{
        return kr.carepet.singleton.MySharedPreference.sharedPreferences.getBoolean("isLogin",false)
    }

    fun setLastLoginMethod(method:String){
        kr.carepet.singleton.MySharedPreference.sharedPreferences.edit().putString("lastLoginMethod",method).apply()
    }
    fun getLastLoginMethod():String{
        return kr.carepet.singleton.MySharedPreference.sharedPreferences.getString("lastLoginMethod","").toString()
    }


    fun setProfileImage(imageUri:Uri?){
        kr.carepet.singleton.MySharedPreference.sharedPreferences.edit().putString("imageUri",imageUri.toString()).apply()
    }

    fun getProfileImage():String?{
        return kr.carepet.singleton.MySharedPreference.sharedPreferences.getString("imageUri","")
    }

    fun setUserId(userId:String){
        kr.carepet.singleton.MySharedPreference.sharedPreferences.edit().putString("userId",userId).apply()
    }

    fun getUserId():String{
        return kr.carepet.singleton.MySharedPreference.sharedPreferences.getString("userId","").toString()
    }
}