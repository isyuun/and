package kr.carepet.service.app.navi.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.carepet.service.app.navi.singleton.G
import kr.carepet.service.app.navi.databinding.ActivityMainBinding
import kr.carepet.service.app.navi.singleton.MySharedPreference

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.mainTvId.text = G.userId
        binding.mainTvEmail.text = G.userEmail


        weatherCheck()

    }

    private fun weatherCheck() {
        // 당일 처음 접속하면 false를 반환, 이후에는 true를 반환
        var isWeatherCheck = MySharedPreference.getTodayData()

        if(!isWeatherCheck){
            //isWeatherCheck가 false면 실행
            //즉, 하루에 한번만 실행하는 메소드

            //isWeather값을 true로 바꿔줌
            MySharedPreference.setTodayData()

            // 이후 날씨정보를 가져오는 코드 구현
        }

    }
}