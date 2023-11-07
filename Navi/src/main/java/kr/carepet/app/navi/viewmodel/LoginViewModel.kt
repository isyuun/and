package kr.carepet.app.navi.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kr.carepet.data.user.LoginResModel
import kr.carepet.singleton.G
import kr.carepet.singleton.MySharedPreference
import kr.carepet.singleton.RetrofitClientServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.resume

class LoginViewModel() : ViewModel() {

    companion object {
        const val TAG = "KakaoViewModel"
    }

    //private val context = application.applicationContext

    // 간편 로그인 Data
    private val _email = MutableStateFlow<String>("")
    val email: StateFlow<String> = _email.asStateFlow()
    fun updateEmail(newValue: String) { _email.value = newValue }

    private val _unqId = MutableStateFlow<String>("")
    val unqId: StateFlow<String> = _unqId.asStateFlow()
    fun updateUnqId(newValue: String) { _unqId.value = newValue }

    private val _nickName = MutableStateFlow<String>("")
    val nickName: StateFlow<String> = _nickName.asStateFlow()
    fun updateNickName(newValue: String) { _nickName.value = newValue }

    private val _loginMethod = MutableStateFlow<String>("")
    val loginMethod: StateFlow<String> = _loginMethod.asStateFlow()
    fun updateLoginMethod(newValue: String) { _loginMethod.value = newValue }
    // 간편 로그인 Data

    // 아이디, 비밀번호 찾기
    private val _cpChange = MutableStateFlow<String>("main")
    val cpChange: StateFlow<String> = _cpChange.asStateFlow()
    fun updateCpChange(newValue: String) { _cpChange.value = newValue }

    private val _phoneNum = MutableStateFlow<String>("")
    val phoneNum: StateFlow<String> = _phoneNum.asStateFlow()
    fun updatePhoneNum(newValue: String) { _phoneNum.value = newValue }

    private val _certiNum = MutableStateFlow<String>("")
    val certiNum: StateFlow<String> = _certiNum.asStateFlow()
    fun updateCertiNum(newValue: String) { _certiNum.value = newValue }
    // 아이디, 비밀번호 찾기

    private val _allCheck = MutableStateFlow<Boolean>(false)
    val allCheck: StateFlow<Boolean> = _allCheck.asStateFlow()
    fun updateAllCheck(newValue: Boolean) { _allCheck.value = newValue }

    private val _memberCheck = MutableStateFlow<Boolean>(false)
    val memberCheck: StateFlow<Boolean> = _memberCheck.asStateFlow()
    fun updateMemberCheck(newValue: Boolean) { _memberCheck.value = newValue }

    private val _personCheck = MutableStateFlow<Boolean>(false)
    val personCheck: StateFlow<Boolean> = _personCheck.asStateFlow()
    fun updatePersonCheck(newValue: Boolean) { _personCheck.value = newValue }

    private val _marketingCheck = MutableStateFlow<Boolean>(false)
    val marketingCheck: StateFlow<Boolean> = _marketingCheck.asStateFlow()
    fun updateMarketingCheck(newValue: Boolean) { _marketingCheck.value = newValue }


    // 이메일 로그인
    suspend fun onLoginButtonClick(userId: String, userPw: String, loginMethod: String): Boolean {
        if (userId.isNotEmpty() && userPw.isNotEmpty()) {
            // 서버로 전송
            val apiService = RetrofitClientServer.instance

            val loginData = kr.carepet.data.user.LoginData(userId, userPw)

            val call = apiService.sendLoginToServer(loginData)
            return suspendCancellableCoroutine { continuation ->
                call.enqueue(object : Callback<LoginResModel> {
                    override fun onResponse(
                        call: Call<LoginResModel>,
                        response: Response<LoginResModel>
                    ) {
                        // Response 결과
                        if (response.isSuccessful) {
                            val body = response.body()
                            body?.let {
                                if (it.statusCode == 200) { // status code 200 검증
                                    // 200이면 login 처리, access/refresh token G 및 shared 에 저장

                                    G.accessToken = it.data.accessToken
                                    G.refreshToken = it.data.refreshToken
                                    G.userId = it.data.userId
                                    G.userNickName = it.data.nckNm
                                    G.userEmail = it.data.email

                                    // shared에 저장
                                    MySharedPreference.setAccessToken(it.data.accessToken)
                                    MySharedPreference.setRefreshToken(it.data.refreshToken)
                                    MySharedPreference.setUserId(it.data.userId)
                                    MySharedPreference.setLastLoginMethod(loginMethod)
                                    MySharedPreference.setIsLogin(true)


                                    continuation.resume(true)
                                } else {
                                    continuation.resume(false)
                                }
                            }
                        }else{
                            continuation.resume(false)
                        }
                    }

                    override fun onFailure(call: Call<LoginResModel>, t: Throwable) {
                        Log.d("LOG", "userid : ${userId}, userpw: ${userPw}, " + t.message)
                        continuation.resume(false)
                    }
                })
            }
        } else {
            return false
        }
    }



    val isLoggedIn = MutableStateFlow<Boolean>(false)

    suspend fun kakaoLogin(context: Context):Boolean=
        suspendCancellableCoroutine{ continuation ->
        // 카카오 계정으로 로그인을 위한 콜백
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                continuation.resume(false)
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")

                UserApiClient.instance.me { user, error ->
                    if(user!=null){
                        _email.value = user.kakaoAccount?.email ?: ""
                        _unqId.value = user.id.toString()
                        _nickName.value = user.kakaoAccount?.profile?.nickname ?: ""

                        continuation.resume(true)
                    }
                }
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")

                    UserApiClient.instance.me { user, error ->
                        if(user!=null){
                            _email.value = user.kakaoAccount?.email ?: ""
                            _unqId.value = user.id.toString()
                            _nickName.value = user.kakaoAccount?.profile?.nickname ?: ""

                            continuation.resume(true)
                        }
                    }
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }
    suspend fun naverLogin(context: Context):Boolean=
        suspendCancellableCoroutine { continuation ->

            NaverIdLoginSDK.initialize(context, "fk5tuUBi3UzTVQRcBMGK", "kbSHyo7KeQ", "CarePet")

            val oAuthLoginCallback = object : OAuthLoginCallback{
                override fun onError(errorCode: Int, message: String) {
                    Log.d("NAVER","error : ${message}")
                    continuation.resume(false)
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    Log.d("NAVER","failure : ${message}")
                    continuation.resume(false)
                }

                override fun onSuccess() {
                    NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse>{
                        override fun onError(errorCode: Int, message: String) {
                            Log.d("NAVER","suc -> error : ${message}")
                            continuation.resume(false)
                        }

                        override fun onFailure(httpStatus: Int, message: String) {
                            Log.d("NAVER","suc -> failure : ${message}")
                            continuation.resume(false)
                        }

                        override fun onSuccess(result: NidProfileResponse) {
                            _email.value = result.profile?.email?: ""
                            _unqId.value = result.profile?.id?: ""
                            _nickName.value = result.profile?.nickname?: ""

                            Log.d("NAVER","${_email.value} ${_unqId.value} ${_nickName.value}")
                            continuation.resume(true)
                        }

                    })
                }

            }

            NaverIdLoginSDK.authenticate(context,oAuthLoginCallback)
    }

    suspend fun googleLogin(){


    }
}

class RetrofitHelper {
    companion object{
        fun getRetrofitInstance(baseUrl:String): Retrofit {
            val retrofit= Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit
        }
    }
}