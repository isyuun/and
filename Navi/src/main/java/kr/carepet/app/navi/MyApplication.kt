package kr.carepet.app.navi

import android.util.Log
import com.kakao.sdk.common.KakaoSdk

class MyApplication : kr.carepet.map.app.MapApplication() {
    override fun onCreate() {
        super.onCreate()
        Log.d("Log", "application 진입")

        KakaoSdk.init(this, "55dcd3a241d864c2399f6ca1e466d57d")
        kr.carepet.singleton.MySharedPreference.init(this)
    }
}