package kr.carepet.service.app.navi

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import kr.carepet.service.app.navi.singleton.MySharedPreference

class KakaoApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "64420f8a9770f177aa4f87153c4f6f5c")
        MySharedPreference.init(this)
    }
}