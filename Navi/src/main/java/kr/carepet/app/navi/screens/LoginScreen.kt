@file:OptIn(ExperimentalMaterial3Api::class)

package kr.carepet.app.navi.screens

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.launch
import kr.carepet.app.navi.R
import kr.carepet.app.navi.Screen
import kr.carepet.app.navi.component.BackTopBar
import kr.carepet.app.navi.component.CustomTextField
import kr.carepet.app.navi.ui.theme.design_btn_border
import kr.carepet.app.navi.ui.theme.design_button_bg
import kr.carepet.app.navi.ui.theme.design_intro_bg
import kr.carepet.app.navi.ui.theme.design_login_bg
import kr.carepet.app.navi.ui.theme.design_login_facebookbtn
import kr.carepet.app.navi.ui.theme.design_login_kakaobtn
import kr.carepet.app.navi.ui.theme.design_login_naverbtn
import kr.carepet.app.navi.ui.theme.design_login_text
import kr.carepet.app.navi.ui.theme.design_placeHolder
import kr.carepet.app.navi.ui.theme.design_select_btn_text
import kr.carepet.app.navi.ui.theme.design_sharp
import kr.carepet.app.navi.ui.theme.design_skip
import kr.carepet.app.navi.ui.theme.design_textFieldOutLine
import kr.carepet.app.navi.ui.theme.design_white
import kr.carepet.app.navi.viewmodel.LoginViewModel
import kr.carepet.app.navi.viewmodel.SharedViewModel
import kr.carepet.app.navi.viewmodel.UserCreateViewModel
import kr.carepet.singleton.MySharedPreference

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(navController: NavController,viewModel: LoginViewModel,sharedViewModel: SharedViewModel){

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color = design_intro_bg)
    systemUiController.setNavigationBarColor(color= design_login_bg)

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var id by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val snsEmail by viewModel.email.collectAsState()
    val snsUnqId by viewModel.unqId.collectAsState()

    var snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }
    val focusManager = LocalFocusManager.current

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("985887161836-gj9pqql898d85483bc1ik53a5t1kg6du.apps.googleusercontent.com")
        .requestServerAuthCode("985887161836-gj9pqql898d85483bc1ik53a5t1kg6du.apps.googleusercontent.com")
        .requestEmail()
        .build()

    val mGoogleSignInClient = GoogleSignIn.getClient(context,gso)
    val googleAuthLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

        try {
            val account = task.getResult(ApiException::class.java)
            viewModel.updateEmail(account?.email.toString())
            viewModel.updateUnqId(account?.id.toString())
            viewModel.updateNickName(account?.displayName.toString())

            scope.launch {
                val loginResult = viewModel.onLoginButtonClick(snsEmail, snsUnqId, "GOOGLE")
                // 가져온 정보로 로그인 시도, 성공시 메인// 실패시 가입
                if (loginResult){
                    sharedViewModel.updateInit(true)
                    navController.navigate(Screen.MainScreen.route){
                        popUpTo(0)
                    }
                }else{
                    viewModel.updateLoginMethod("GOOGLE")
                    navController.navigate(Screen.EasyRegScreen.route)
                }
            }

        } catch (e: ApiException){
            Log.e("Google account","signInResult:failed Code = " + e.statusCode)
        }
    }

    Scaffold (
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ){ paddingValues ->

        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(color = design_intro_bg)
            , horizontalAlignment = Alignment.CenterHorizontally) {

            Image(painter = painterResource(id = R.drawable.logo_login), contentDescription = "logo", modifier = Modifier
                .padding(top = 40.dp, bottom = 40.dp)
                .width(100.dp))

            Column (modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(
                    color = design_login_bg,
                    shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
                )
                ,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "로그인",modifier= Modifier
                    .padding(top = 40.dp, bottom = 20.dp)
                    , textAlign = TextAlign.Center,
                    fontSize = 24.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    color = design_login_text
                )


                CustomTextField(
                    value = id,
                    onValueChange = {id = it },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next),
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp)
                        .fillMaxWidth()
                        .height(48.dp),
                    placeholder = { Text(text = "Email", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
                    leadingIcon = { Icon(painter = painterResource(id = R.drawable.icon_email), contentDescription = "")},
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedPlaceholderColor = design_placeHolder,
                        focusedPlaceholderColor = design_placeHolder,
                        unfocusedBorderColor = design_textFieldOutLine,
                        focusedBorderColor = design_login_text,
                        unfocusedContainerColor = design_white,
                        focusedContainerColor = design_white,
                        unfocusedLeadingIconColor = design_placeHolder,
                        focusedLeadingIconColor = design_login_text),
                    shape = RoundedCornerShape(4.dp)
                )

                CustomTextField(
                    value = password,
                    onValueChange = {password = it},
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        scope.launch { viewModel.onLoginButtonClick(
                            id,
                            password,
                            "EMAIL"
                        )}}),
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 8.dp)
                        .fillMaxWidth()
                        .height(48.dp),
                    placeholder = { Text(text = "PassWord",fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
                    leadingIcon = { Icon(painter = painterResource(id = R.drawable.icon_password), contentDescription = "")},
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedPlaceholderColor = design_placeHolder,
                        focusedPlaceholderColor = design_placeHolder,
                        unfocusedBorderColor = design_textFieldOutLine,
                        focusedBorderColor = design_login_text,
                        unfocusedContainerColor = design_white,
                        focusedContainerColor = design_white,
                        unfocusedLeadingIconColor = design_placeHolder,
                        focusedLeadingIconColor = design_login_text),
                    shape = RoundedCornerShape(4.dp)
                )

                Button(
                    onClick = {
                        scope.launch {
                            val result = viewModel.onLoginButtonClick(userId = id, userPw = password, loginMethod = "EMAIL")
                            if (result){
                                sharedViewModel.updateInit(true)
                                navController.navigate(Screen.MainScreen.route){
                                    popUpTo(0)
                                }
                            }else{
                                focusManager.clearFocus()
                                snackbarHostState.showSnackbar(
                                    message = "아이디 및 패스워드를 확인해주세요.",
                                    actionLabel = "확인",
                                    duration = SnackbarDuration.Short,
                                    withDismissAction = false
                                )
                            }
                        } },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = design_button_bg)
                )
                {
                    Text(text = "로그인", color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
                }

                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                    Text(
                        text = "아이디 찾기",
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        color = design_login_text,
                        modifier = Modifier.clickable { navController.navigate(route = Screen.IdPwSearch.route+"/0") })

                    Spacer(modifier = Modifier
                        .padding(horizontal = 9.dp)
                        .size(2.dp, 8.dp)
                        .background(color = design_login_text))

                    Text(
                        text = "비밀번호 찾기",
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        color = design_login_text,
                        modifier = Modifier.clickable {  navController.navigate(route = Screen.IdPwSearch.route+"/1") })

                    Spacer(modifier = Modifier
                        .padding(horizontal = 9.dp)
                        .size(2.dp, 8.dp)
                        .background(color = design_login_text))

                    Text(
                        text = "회원가입",
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        color = design_login_text,
                        modifier = Modifier.clickable {
                            navController.navigate(Screen.UserCreate.route)
                        })
                }

                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
                    Divider(modifier=Modifier.size(92.dp,1.dp), color = design_textFieldOutLine)
                    Text(text = " SNS 계정으로 로그인 ", modifier = Modifier.padding(horizontal = 14.dp),
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)), color = design_login_text)
                    Divider(modifier=Modifier.size(92.dp,1.dp), color = design_textFieldOutLine)
                }

                // viewModel에 있는 stateFlow를 추적 시작
                val isLoggedIn = viewModel.isLoggedIn.collectAsState()
                val loginStatusInfo = if(isLoggedIn.value) "로그인 상태" else "로그아웃 상태"

                Button(onClick = {
                    scope.launch {
                        val kakaoLoginResult = viewModel.kakaoLogin(context)
                        // naver Login 성공
                        if (kakaoLoginResult){

                            val loginResult = viewModel.onLoginButtonClick(snsEmail, snsUnqId, "KAKAO")
                            // 가져온 정보로 로그인 시도, 성공시 메인// 실패시 가입
                            if (loginResult){
                                sharedViewModel.updateInit(true)
                                navController.navigate(Screen.MainScreen.route){
                                    popUpTo(0)
                                }
                            }else{
                                viewModel.updateLoginMethod("KAKAO")
                                navController.navigate(Screen.EasyRegScreen.route)
                            }

                        }else{
                            Toast.makeText(context, "Kakao 로그인 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = design_login_kakaobtn, contentColor = design_login_text)
                ) {
                    Box (modifier = Modifier.fillMaxSize()){
                        Icon(painter = painterResource(id = R.drawable.icon_kakao), contentDescription = "",
                            modifier = Modifier.align(Alignment.CenterStart), tint = Color.Unspecified)
                        Text(text = "카카오톡으로 로그인", modifier = Modifier.align(Alignment.Center),
                            fontSize = 14.sp, letterSpacing = (-0.7).sp,
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)))
                    }
                }

                Button(onClick = {
                    scope.launch {
                        val naverLoginResult = viewModel.naverLogin(context)
                        // naver Login 성공
                        if (naverLoginResult){

                            val loginResult = viewModel.onLoginButtonClick(snsEmail, snsUnqId, "NAVER")
                            // 가져온 정보로 로그인 시도, 성공시 메인// 실패시 가입
                            if (loginResult){
                                sharedViewModel.updateInit(true)
                                navController.navigate(Screen.MainScreen.route){
                                    popUpTo(0)
                                }
                            }else{
                                viewModel.updateLoginMethod("NAVER")
                                navController.navigate(Screen.EasyRegScreen.route)
                            }

                        }else{
                            Toast.makeText(context, "Naver 로그인 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = design_login_naverbtn, contentColor = design_white)
                ) {
                    Box (modifier = Modifier.fillMaxSize()){
                        Icon(painter = painterResource(id = R.drawable.icon_naver), contentDescription = "",
                            modifier = Modifier.align(Alignment.CenterStart), tint = Color.Unspecified)
                        Text(text = "네이버로 로그인", modifier = Modifier.align(Alignment.Center),
                            fontSize = 14.sp, letterSpacing = (-0.7).sp,
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)))
                    }
                }

                Button(onClick = { /*TODO 네이버*/ },
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = design_login_facebookbtn, contentColor = design_white)
                ) {
                    Box (modifier = Modifier.fillMaxSize()){
                        Icon(painter = painterResource(id = R.drawable.icon_facebook), contentDescription = "",
                            modifier = Modifier.align(Alignment.CenterStart), tint = Color.Unspecified)
                        Text(text = "페이스북으로 로그인", modifier = Modifier.align(Alignment.Center),
                            fontSize = 14.sp, letterSpacing = (-0.7).sp,
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)))
                    }
                }

                Button(onClick = {
                    scope.launch {
                        val signInIntent = mGoogleSignInClient.signInIntent
                        googleAuthLauncher.launch(signInIntent)
                    }
                },
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 8.dp)
                        .border(
                            width = 1.dp,
                            color = design_btn_border,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = design_white, contentColor = design_login_text)
                ) {
                    Box (modifier = Modifier.fillMaxSize()){
                        Icon(painter = painterResource(id = R.drawable.icon_google), contentDescription = "",
                            modifier = Modifier.align(Alignment.CenterStart), tint = Color.Unspecified)
                        Text(text = "구글로 로그인", modifier = Modifier.align(Alignment.Center),
                            fontSize = 14.sp,letterSpacing = (-0.7).sp,
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)))
                    }
                }

            }
        }
    }

}

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel,sharedViewModel: SharedViewModel) {

    Box(modifier = Modifier.fillMaxSize()){
        LoginContent(navController = navController, viewModel = viewModel, sharedViewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EasyRegScreen(navController: NavHostController, viewModel: LoginViewModel, userCreateViewModel: UserCreateViewModel){

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color = design_white)

    val allCheck by viewModel.allCheck.collectAsState()
    val memberCheck by viewModel.memberCheck.collectAsState()
    val personCheck by viewModel.personCheck.collectAsState()
    val marketingCheck by viewModel.marketingCheck.collectAsState()
    val nickname by viewModel.nickName.collectAsState()
    val snsLogin by viewModel.loginMethod.collectAsState()
    val unqId by viewModel.unqId.collectAsState()
    val email by viewModel.email.collectAsState()
    val context = LocalContext.current

    val countTrue = listOf(memberCheck, personCheck, marketingCheck).count { true }

    LaunchedEffect(key1 = memberCheck, key2 = personCheck, key3 = marketingCheck){
        if (memberCheck && personCheck && marketingCheck){
            viewModel.updateAllCheck(true)
        }else{
            viewModel.updateAllCheck(false)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            BackTopBar(title = "회원가입", navController = navController)
        },
        //snackbarHost =
    ) { paddingValues ->

        Box (modifier = Modifier
            .padding(paddingValues)
            .background(color = design_white)
        ){
            Column (
                modifier = Modifier.fillMaxSize()
            ){
                Spacer(modifier = Modifier.padding(top = 20.dp))
                
                Row (
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .height(48.dp)
                        .border(
                            width = 1.dp,
                            color =
                            if (allCheck) {
                                design_intro_bg
                            } else {
                                design_login_text
                            },
                            shape = RoundedCornerShape(12.dp)
                        )
                        .background(
                            color =
                            if (allCheck) {
                                design_login_bg
                            } else {
                                design_white
                            },
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            viewModel.updateAllCheck(!allCheck)
                            viewModel.updateMemberCheck(!allCheck)
                            viewModel.updatePersonCheck(!allCheck)
                            viewModel.updateMarketingCheck(!allCheck)
                        }
                    , verticalAlignment = Alignment.CenterVertically
                ){
                    Checkbox(
                        checked = allCheck,
                        onCheckedChange = {
                            viewModel.updateAllCheck(it)
                            viewModel.updateMemberCheck(it)
                            viewModel.updatePersonCheck(it)
                            viewModel.updateMarketingCheck(it)
                                          },
                        colors = CheckboxDefaults.colors(
                            checkedColor = design_select_btn_text,
                            uncheckedColor = design_textFieldOutLine,
                            checkmarkColor = design_white)
                    )

                    Text(text = "전체 약관에 동의합니다.",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                        color = design_login_text, modifier=Modifier.offset(x = (-8).dp),
                        letterSpacing = (-0.7).sp,
                    )
                }

                Spacer(modifier = Modifier.padding(top = 16.dp))

                AgreeComponent(
                    title = "회원가입약관 동의",
                    mainText = "마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용",
                    check = memberCheck,
                    onClick = { newValue -> viewModel.updateMemberCheck(newValue)})

                AgreeComponent(
                    title = "개인정보처리방침안내 동의",
                    mainText = "마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용",
                    check = personCheck,
                    onClick = { newValue -> viewModel.updatePersonCheck(newValue)})

                AgreeComponent(
                    title = "마케팅수신동의",
                    mainText = "마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용",
                    check = marketingCheck,
                    onClick = { newValue -> viewModel.updateMarketingCheck(newValue)})

                Spacer(modifier = Modifier.padding(top = 16.dp))

                Row (Modifier.fillMaxWidth()){
                    Text(text = "닉네임", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                        modifier=Modifier.padding(start = 20.dp), color = design_login_text)
                    Text(
                        text = "*",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                        color= design_sharp
                    )
                }

                CustomTextField(
                    value = nickname,
                    onValueChange = {viewModel.updateNickName(it)},
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next),
                    modifier = Modifier
                        .padding(start = 20.dp, top = 8.dp, end = 20.dp)
                        .fillMaxWidth()
                        .height(48.dp),
                    placeholder = { Text(text = "닉네임을 입력해주세요", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedPlaceholderColor = design_placeHolder,
                        focusedPlaceholderColor = design_placeHolder,
                        unfocusedBorderColor = design_textFieldOutLine,
                        focusedBorderColor = design_login_text,
                        unfocusedContainerColor = design_white,
                        focusedContainerColor = design_white,
                        unfocusedLeadingIconColor = design_placeHolder,
                        focusedLeadingIconColor = design_login_text),
                    shape = RoundedCornerShape(4.dp),
                    innerPadding = PaddingValues(start=16.dp)
                )

                Button(
                    onClick = {
                        if(allCheck){
                            userCreateViewModel.updateUserNickName(nickname)
                            userCreateViewModel.updateUserID(email)
                            userCreateViewModel.updateUserName(nickname)
                            userCreateViewModel.updateSnsLogin(snsLogin)
                            userCreateViewModel.updateUserPW(unqId)
                            navController.navigate(Screen.PetCreateScreen.route)
                        }else{
                            Toast.makeText(context, "약관에 동의해주세요", Toast.LENGTH_SHORT).show()
                        }
                              },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = design_button_bg)
                )
                {
                    Text(text = "다음", color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
                }

            } // column
        }

    }
}

@Composable
fun AgreeComponent(
    title: String,
    mainText : String,
    check : Boolean,
    onClick:(Boolean) -> Unit){
    Column (
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
    ){
        var expanded by remember { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxWidth()){
            Row (
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxWidth()
                    .clickable { onClick(!check) },
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    painter =
                    if(check){
                        painterResource(id = R.drawable.checkbox_blue)
                    }else{
                        painterResource(id = R.drawable.checkbox_gray)
                    },
                    contentDescription = "", tint = Color.Unspecified,
                    modifier = Modifier.padding(start = 20.dp)
                )

                Text(
                    text = title,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp,
                    letterSpacing = (-0.7).sp,
                    color = design_login_text,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Icon(
                imageVector =
                if(expanded){
                    Icons.Default.KeyboardArrowUp
                }else{
                    Icons.Default.KeyboardArrowDown
                },
                contentDescription = "",  tint = design_login_text,
                modifier = Modifier
                    .padding(end = 20.dp)
                    .align(Alignment.CenterEnd)
                    .clickable { expanded = !expanded }
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Text(
                text = mainText,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 12.sp,
                letterSpacing = (-0.6).sp,
                color = design_skip,
                modifier = Modifier.padding(start = 38.dp, end = 16.dp, top = 8.dp)
            )
        }


        Spacer(modifier = Modifier
            .padding(top = 20.dp, bottom = 8.dp)
            .fillMaxWidth()
            .height(1.dp)
            .background(design_textFieldOutLine))
    }
}

