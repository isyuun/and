package kr.carepet.service.app.navi.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kr.carepet.service.app.navi.R
import kr.carepet.service.app.navi.databinding.ActivityPetRegistBinding
import kr.carepet.service.app.navi.model.MyPetResModel
import kr.carepet.service.app.navi.model.PetModel
import kr.carepet.service.app.navi.service.ApiService
import kr.carepet.service.app.navi.singleton.RetrofitClientServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PetRegistActivity : AppCompatActivity() {

    val binding by lazy { ActivityPetRegistBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.petBtn.setOnClickListener {
            val apiService = RetrofitClientServer.instance

            val petBrthYmd:String = binding.petrgEtBirth.text.toString()
            val petInfoUnqNo:Int = binding.petrgEtUnqno.text.toString().toInt()
            val petNm:String = binding.petrgEtNm.text.toString()
            val petRelCd:String = binding.petrgEtRelcd.text.toString()
            val petRprsYn:String = binding.petrgEtRqrsyn.text.toString()
            val stdgCtpvCd:Int = binding.petrgEtCypvcd.text.toString().toInt()
            val stdgEmdCd:Int = binding.petrgEtEmdcd.text.toString().toInt()
            val stdgSggCd:Int = binding.petrgEtSggcd.text.toString().toInt()

            val data = PetModel(petBrthYmd, petInfoUnqNo, petNm, petRelCd, petRprsYn, stdgCtpvCd, stdgEmdCd, stdgSggCd)

            val call=apiService.sendPetDataToServer(data)
            call.enqueue(object : Callback<MyPetResModel>{
                override fun onResponse(
                    call: Call<MyPetResModel>,
                    response: Response<MyPetResModel>
                ) {
                    val body = response.body()
                    if(response.isSuccessful){
                        Toast.makeText(this@PetRegistActivity, "통신성공", Toast.LENGTH_SHORT).show()
                        Log.d("PET","${body?.data},${body?.statusCode},${body?.detailMessage},${body?.data}, ${body?.resultMessage}")

                    }
                }

                override fun onFailure(call: Call<MyPetResModel>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}