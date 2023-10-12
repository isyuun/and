package kr.carepet.app.navi

import android.content.ContentValues.TAG
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility

class MyApplication : kr.carepet.map.app.MapApplication() {
    override fun onCreate() {
        super.onCreate()
        Log.d("Log", "application 진입")

        KakaoSdk.init(this, "55dcd3a241d864c2399f6ca1e466d57d")
        kr.carepet.singleton.MySharedPreference.init(this)

        Log.d(TAG, "keyhash : ${Utility.getKeyHash(this)}")
    }
}