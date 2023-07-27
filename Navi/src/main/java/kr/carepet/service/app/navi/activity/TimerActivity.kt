package kr.carepet.service.app.navi.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.carepet.service.app.navi.R
import kr.carepet.service.app.navi.databinding.ActivityTimerBinding
import java.util.Timer
import kotlin.concurrent.timer

class TimerActivity : AppCompatActivity() {

    private val binding by lazy { ActivityTimerBinding.inflate(layoutInflater) }

    private var isRunning = false
    private var timer = Timer()
    private var time = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.timerStart.setOnClickListener { btnStart() }
        binding.timerStop.setOnClickListener { btnStop() }
    }

    private fun btnStart() {

        if(isRunning){
            btnPause()
            // pause 작업 실행
        }else{
            binding.timerStart.text = getString(R.string.timer_pause)
            binding.timerStart.backgroundTintList = getColorStateList(R.color.pause)
            isRunning = true

            // 타이머 시작
            timer=timer(period = 1000){
                // 1초마다 갱신
                time++

                val second = time%60
                val minute = (time/60)%60
                val hour = (time/3600)

                runOnUiThread {
                    if(isRunning){
                        binding.timerTvSecond.text= if(second < 10) ": 0${second}" else ": ${second}"
                        binding.timerTvMinute.text= if(minute < 10) ": 0${minute} " else ": ${minute} "
                        binding.timerTvHour.text= if(hour < 10) "0${hour} " else "${hour} "
                    }
                }
            }
        }
    }

    private fun btnPause() {
        binding.timerStart.text = getString(R.string.timer_start)
        binding.timerStart.backgroundTintList = getColorStateList(R.color.start)
        isRunning = false

        timer.cancel()
    }

    private fun btnStop(){
        timer.cancel()
        isRunning=false
        time = 0

        binding.timerTvSecond.text= ": 00"
        binding.timerTvMinute.text= ": 00 "
        binding.timerTvHour.text= "00 "
    }
}