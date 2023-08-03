/*
 *  * Copyright(c) 2023 CarePat All right reserved.
 *  * This software is the proprietary information of CarePet.
 *  *
 *  * Revision History
 *  * Author                         Date             Description
 *  * --------------------------    -------------    ----------------------------------------
 *  * sanghun.lee@care-biz.co.kr     2023. 7. 28.    CarePet Development...
 *  *
 */

package kr.carepet.service.app.navi.activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import kr.carepet.service.app.navi.R
import kr.carepet.service.app.navi.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {

    private val splashDelay: Long = 3000
    private val binding by lazy { ActivityIntroBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        startTextviewAnimaiton()

        timeStart()

    }

    private fun startTextviewAnimaiton() {

        val targetX = 0
        val targetY = -300

        val translateAnimation = TranslateAnimation(0f, targetX.toFloat(), 0f, targetY.toFloat())
        translateAnimation.duration = 1500 // 애니메이션 지속 시간 (밀리초)

        translateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                // 애니메이션이 시작될 때 호출되는 콜백 메서드
            }

            override fun onAnimationEnd(animation: Animation) {
                // 애니메이션이 종료될 때 호출되는 콜백 메서드
                binding.introTv.apply {
                    clearAnimation()  // 애니메이션 효과를 제거하여 제자리에 고정
                    translationX = targetX.toFloat() // x 좌표를 이동한 위치로 설정
                    translationY = targetY.toFloat() // y 좌표를 이동한 위치로 설정
                }
            }

            override fun onAnimationRepeat(animation: Animation) {
                // 애니메이션이 반복될 때 호출되는 콜백 메서드
            }
        })

        binding.introTv.startAnimation(translateAnimation)

    }


    private fun timeStart(){
        val timer = object : CountDownTimer(splashDelay, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // 타이머가 틱마다 수행할 작업 (여기서는 필요 없음)
            }

            override fun onFinish() {
                val intent = Intent(this@IntroActivity, LoginActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
            }
        }

        timer.start() // 타이머 시작
    }

}