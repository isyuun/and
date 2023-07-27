package kr.carepet.service.app.navi.activity

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kr.carepet.service.app.navi.R
import kr.carepet.service.app.navi.databinding.ActivitySignUpBinding
import java.util.Calendar
import java.util.Locale

class SignUpActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }
    private val PICK_IMAGE_REQUEST = 1

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) { binding.signupIvProfile.setImageURI(uri) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.signupTvMale.setOnClickListener { genderClick(it) }
        binding.signupTvFemale.setOnClickListener { genderClick(it) }
        binding.signupFab.setOnClickListener { fabClick() }
        binding.signupTvCate.setOnClickListener { cateClick() }
        binding.signupTvAddress.setOnClickListener { addressClick() }
        binding.signupTvBirthYear.setOnClickListener { showDatePickerDialog() }
        binding.signupTvBirthMonth.setOnClickListener { showDatePickerDialog() }
        binding.signupTvBirthDay.setOnClickListener { showDatePickerDialog() }
    }

    private fun addressClick() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Alert")
            .setMessage("You clicked the text!")
            .setPositiveButton("OK") { dialog, which ->
                // OK 버튼 클릭 시 처리할 로직을 작성합니다.
            }
            .setNegativeButton("Cancel") { dialog, which ->
                // Cancel 버튼 클릭 시 처리할 로직을 작성합니다.
            }
            .create()

        alertDialog.show()
    }

    private fun cateClick() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Alert")
            .setMessage("견종클릭")
            .setPositiveButton("OK") { dialog, which ->
                // OK 버튼 클릭 시 처리할 로직을 작성합니다.
            }
            .setNegativeButton("Cancel") { dialog, which ->
                // Cancel 버튼 클릭 시 처리할 로직을 작성합니다.
            }
            .create()

        alertDialog.show()
    }

    private fun genderClick(view:View){
        if (view.id == R.id.signup_tv_male){
            view.setBackgroundResource(R.drawable.selector_signup_text_press)
            binding.signupTvFemale.setBackgroundResource(R.drawable.selector_signup_text_normal)
        }else{
            view.setBackgroundResource(R.drawable.selector_signup_text_press)
            binding.signupTvMale.setBackgroundResource(R.drawable.selector_signup_text_normal)
        }
    }

    private fun fabClick(){
        galleryLauncher.launch("image/*")
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            { view, selectedYear, selectedMonth, selectedDayOfMonth ->
                String.format(
                    //핸드폰이 한국어 설정되어있어야 locale이 동작함
                    Locale("ko", "KR"), "%d년 %02d월 %02d일",
                    selectedYear, selectedMonth + 1, selectedDayOfMonth)

                binding.signupTvBirthYear.text="${selectedYear}년"
                binding.signupTvBirthMonth.text="${selectedMonth+1}월"
                binding.signupTvBirthDay.text="${selectedDayOfMonth}일"
            }, year, month, day)

        datePickerDialog.show()
    }
}