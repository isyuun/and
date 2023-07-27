package kr.carepet.service.app.navi.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import kr.carepet.service.app.navi.R
import kr.carepet.service.app.navi.singleton.G
import kr.carepet.service.app.navi.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)




        binding.loginTvSignup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.loginBtnKakao.setOnClickListener { kakaoClick() }

        binding.loginBtnNaver.setOnClickListener { naverClick() }

        binding.loginBtnGoogle.setOnClickListener { googleClick() }

        binding.loginTvGo.setOnClickListener { startActivity(Intent(this,TimerActivity::class.java)) }

    }

    private fun kakaoClick() {
        // Kakao Login API를 이용하여 사용자 정보 취득

        // 로그인 시도한 결과를 받았을때 발동하는 콜백함수를 별도로 만들기
        val callback:(OAuthToken?, Throwable?)->Unit = { token, error ->
            if( error != null ) {
                Log.d("kakao","로그인 실패")
            }else{
                //사용자 정보 요청
                UserApiClient.instance.me { user, error ->
                    if(user!=null){
                        var id:String = user.id.toString()
                        var email:String = user.kakaoAccount?.email ?: ""  //혹시 null이면 이메일의 기본값 ""

                        Log.d("userId",id)
                        G.userId =id
                        G.userEmail =email

                        //로그인이 성공했으니.. Main 화면으로 전환
                        startActivity( Intent(this, MainActivity::class.java) )
                        finish()
                    }
                }
            }
        }

        // 카카오톡 로그인을 권장하지만 설치가 되어 있지 않다면 카카오계정으로 로그인 시도.
        if(UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
            UserApiClient.instance.loginWithKakaoTalk(this, callback= callback )
        }else{
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback )
        }
    }

    private fun naverClick() {
        var naverToken : String = ""

        NaverIdLoginSDK.initialize(this, "J_pbuqdm8q9u37YZOMFI", "gOYGlN__Wh", "carepet")

        val profileCallback = object : NidProfileCallback<NidProfileResponse>{
            // 유저 정보를 가져오는 콜백 메소드
            override fun onError(errorCode: Int, message: String) {
                Log.d("naver","회원가져오기 실패"+message.toString())
            }

            override fun onFailure(httpStatus: Int, message: String) {
                Log.d("naver","회원가져오기 실패"+message.toString())
            }

            override fun onSuccess(result: NidProfileResponse) {
                val body= result.profile
                G.userId = body?.id.toString()
                G.userEmail = naverToken
                Log.d("userId",body?.id.toString())
                startActivity( Intent(this@LoginActivity, MainActivity::class.java) )
                finish()
            }

        }

        val oauthLoginCallback = object : OAuthLoginCallback {
            // 로그인 시도시 발동하는 콜백 메소드
            override fun onSuccess() {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                naverToken = NaverIdLoginSDK.getAccessToken().toString()
                NidOAuthLogin().callProfileApi(profileCallback)
                Log.d("naver","콜백발동")
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Log.d("naver","인증 실패"+errorCode+errorDescription)
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        NaverIdLoginSDK.authenticate(this, oauthLoginCallback)
    }

    private fun googleClick() {

        val signInOptions : GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val intent:Intent = GoogleSignIn.getClient(this, signInOptions).signInIntent

        googleResultLauncher.launch(intent)

    }

    val googleResultLauncher : ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), object : ActivityResultCallback<ActivityResult>{
        override fun onActivityResult(result: ActivityResult?) {
            val intent:Intent? = result?.data

            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(intent)

            val account: GoogleSignInAccount = task.result

            var id:String= account.id.toString()
            var email:String= account.email ?: ""

            Toast.makeText(this@LoginActivity, "Google 로그인 성공 : $email", Toast.LENGTH_SHORT).show()
            G.userId = id
            G.userEmail = email

            startActivity( Intent(this@LoginActivity, MainActivity::class.java) )
            finish()
        }

    })
}