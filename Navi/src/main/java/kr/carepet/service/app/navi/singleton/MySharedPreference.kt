package kr.carepet.service.app.navi.singleton

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
        Log.d("today",today)
        return sharedPreferences.getBoolean(today, false)
    }


    fun setTodayData() {
        //today key값을 true로 설정
        val today = LocalDate.now().toString()
        sharedPreferences.edit().putBoolean(today, true).apply()
        Log.d("today","성공")
    }
}