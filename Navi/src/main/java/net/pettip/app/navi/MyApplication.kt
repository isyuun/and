package net.pettip.app.navi

import net.pettip.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility

class MyApplication : net.pettip.map.app.MapApplication() {
    override fun onCreate() {
        super.onCreate()
        Log.d("Log", "application 진입")

        KakaoSdk.init(this, "226344419c2ba87b4309b7d42ac22ae0")
        net.pettip.singleton.MySharedPreference.init(this)
    }
}