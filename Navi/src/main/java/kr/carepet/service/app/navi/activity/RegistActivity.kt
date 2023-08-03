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

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kr.carepet.service.app.navi.databinding.ActivityRegistBinding
import kr.carepet.service.app.navi.model.UserDataModel
import kr.carepet.service.app.navi.model.UserDataResponse
import kr.carepet.service.app.navi.singleton.MySharedPreference
import kr.carepet.service.app.navi.singleton.RetrofitClientServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistActivity : AppCompatActivity() {

    val binding by lazy { ActivityRegistBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.regTvToken.text = MySharedPreference.getFcmToken()

        binding.regBtnSend.setOnClickListener { sendUserToServer() }

    }

    private fun sendUserToServer() {
        val apiService = RetrofitClientServer.instance

        val appKey = MySharedPreference.getFcmToken()
        val appOs = "001"
        val appTypNm = Build.MODEL.toString()
        val snsLogin = ""
        val userID = binding.regEtUserid.text.toString()
        val userName = binding.regEtUsername.text.toString()
        val userPW = binding.regEtUserpw.text.toString()


        if (userID.isNotEmpty() && userName.isNotEmpty() && userPW.isNotEmpty()){

            //이메일 형식이 아니라면 메소드를 종료
            if(!Patterns.EMAIL_ADDRESS.matcher(userID).matches()) {
                Toast.makeText(this, "올바른 이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show()
                binding.regEtUserid.requestFocus()
                return
            }

            var userData=UserDataModel(appKey,appOs,appTypNm,snsLogin,userID,userName,userPW)

            val call= apiService.sendUserToServer(userData)
            call.enqueue(object : Callback<UserDataResponse>{
                override fun onResponse(
                    call: Call<UserDataResponse>,
                    response: Response<UserDataResponse>
                ) {
                    if(response.isSuccessful){

                        // 통신이 성공하면 다 여기로 들어옴
                        // 200을 제외한 code 는 detail message 를 노출하는 식으로 처리
                        val userRegistResponse =response.body()
                        userRegistResponse?.let {
                            val dataSet = it.data
                            binding.regTvResult.text ="statusCode : ${it.statusCode} // ${it.resultMessage}"
                            binding.regTvDetail.text ="${it.detailMessage}"
                            binding.regTvDataMessage.text = dataSet?.message
                            binding.regTvDataStatus.text = dataSet?.status.toString()
                            Log.d("USER",userData.toString())
                        }
                    }else{
                        Log.d("USER","else로 진입")
                    }
                    Log.d("USER",response.toString())
                }

                override fun onFailure(call: Call<UserDataResponse>, t: Throwable) {
                    Log.d("USER", "onFailure : ${t.toString()}")
                }

            })



        }else{
            Toast.makeText(this, "모든 항목을 입력하세요", Toast.LENGTH_SHORT).show()
        }

    }

}