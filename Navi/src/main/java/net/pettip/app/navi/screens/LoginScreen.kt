@file:OptIn(ExperimentalMaterial3Api::class)

package net.pettip.app.navi.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import net.pettip.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.Screen
import net.pettip.app.navi.component.BackTopBar
import net.pettip.app.navi.component.CustomTextField
import net.pettip.app.navi.component.LoadingDialog
import net.pettip.app.navi.ui.theme.design_btn_border
import net.pettip.app.navi.ui.theme.design_button_bg
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_login_bg
import net.pettip.app.navi.ui.theme.design_login_kakaobtn
import net.pettip.app.navi.ui.theme.design_login_naverbtn
import net.pettip.app.navi.ui.theme.design_login_text
import net.pettip.app.navi.ui.theme.design_placeHolder
import net.pettip.app.navi.ui.theme.design_select_btn_text
import net.pettip.app.navi.ui.theme.design_sharp
import net.pettip.app.navi.ui.theme.design_skip
import net.pettip.app.navi.ui.theme.design_textFieldOutLine
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.LoginViewModel
import net.pettip.app.navi.viewmodel.SharedViewModel
import net.pettip.app.navi.viewmodel.UserCreateViewModel
import net.pettip.singleton.MySharedPreference

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun LoginContent(navController: NavController,viewModel: LoginViewModel,sharedViewModel: SharedViewModel){

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color = design_intro_bg)
    systemUiController.setNavigationBarColor(color= MaterialTheme.colorScheme.tertiary)

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var id by remember { mutableStateOf(MySharedPreference.getUserEmail()) }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var requirePermission by remember { mutableStateOf(false) }

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
                if (loginResult == 0){
                    sharedViewModel.updateInit(true)
                    sharedViewModel.updateDupleLogin(false)
                    navController.navigate(Screen.MainScreen.route){
                        popUpTo(0)
                    }
                }else if (loginResult == 1){
                    viewModel.updateLoginMethod("GOOGLE")
                    navController.navigate(Screen.EasyRegScreen.route)
                }else{
                    Toast.makeText(context, R.string.retry, Toast.LENGTH_SHORT).show()
                }
            }

        } catch (e: ApiException){
            Log.e("Google account","signInResult:failed Code = " + e.statusCode)
        }
    }

    val permissionState = rememberMultiplePermissionsState(
        permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
            )
        } else {
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
            )
        }
    )

    LaunchedEffect(Unit){
        if (!permissionState.allPermissionsGranted){
            requirePermission = true
        }
    }

    LaunchedEffect(Unit){
        viewModel.updateAppKey()

        sharedViewModel.clear()
    }

    Scaffold (
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ){ paddingValues ->

        LoadingDialog(
            loadingText = stringResource(R.string.login_),
            loadingState = isLoading
        )

        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(color = design_intro_bg)
            , horizontalAlignment = Alignment.CenterHorizontally) {

            Image(painter = painterResource(id = R.drawable.logo_login_pettip), contentDescription = "logo", modifier = Modifier
                .padding(top = 60.dp, bottom = 60.dp)
                .width(100.dp))

            Column (modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
                )
                ,
                horizontalAlignment = Alignment.CenterHorizontally) {

                // -------------- 일반 사용자 로그인 --------------

                //Text(text = "로그인",modifier= Modifier
                //    .padding(top = 40.dp, bottom = 20.dp)
                //    , textAlign = TextAlign.Center,
                //    fontSize = 24.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                //    color = MaterialTheme.colorScheme.onPrimary
                //)
                //
                //
                //CustomTextField(
                //    value = id,
                //    onValueChange = {id = it },
                //    singleLine = true,
                //    keyboardOptions = KeyboardOptions(
                //        keyboardType = KeyboardType.Email,
                //        imeAction = ImeAction.Next),
                //    modifier = Modifier
                //        .padding(start = 20.dp, end = 20.dp)
                //        .fillMaxWidth()
                //        .height(48.dp),
                //    placeholder = { Text(text = "Email", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
                //    leadingIcon = { Icon(painter = painterResource(id = R.drawable.icon_email), contentDescription = "")},
                //    colors = OutlinedTextFieldDefaults.colors(
                //        unfocusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
                //        focusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
                //        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                //        focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                //        unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                //        focusedContainerColor = MaterialTheme.colorScheme.primary,
                //        unfocusedLeadingIconColor = MaterialTheme.colorScheme.primaryContainer,
                //        focusedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
                //        cursorColor = design_intro_bg.copy(alpha = 0.5f)
                //    ),
                //    textStyle = TextStyle(
                //        color = MaterialTheme.colorScheme.onPrimary,
                //        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                //        fontSize = 16.sp, letterSpacing = (-0.4).sp
                //    ),
                //    shape = RoundedCornerShape(4.dp)
                //)
                //
                //CustomTextField(
                //    value = password,
                //    onValueChange = {password = it},
                //    singleLine = true,
                //    keyboardOptions = KeyboardOptions(
                //        keyboardType = KeyboardType.Password,
                //        imeAction = ImeAction.Done),
                //    keyboardActions = KeyboardActions(onDone = {
                //        scope.launch {
                //            isLoading = true
                //            val result = viewModel.onLoginButtonClick(userId = id, userPw = password, loginMethod = "EMAIL")
                //            if (result==0){
                //                isLoading = false
                //                MySharedPreference.setUserEmail(id)
                //                sharedViewModel.updateInit(true)
                //                sharedViewModel.updateDupleLogin(false)
                //                navController.navigate(Screen.MainScreen.route){
                //                    popUpTo(0)
                //                }
                //            }else if (result == 1){
                //                isLoading = false
                //                focusManager.clearFocus()
                //                snackbarHostState.showSnackbar(
                //                    message = "아이디 및 패스워드를 확인해주세요.",
                //                    actionLabel = "확인",
                //                    duration = SnackbarDuration.Short,
                //                    withDismissAction = false
                //                )
                //            }else{
                //                isLoading = false
                //                Toast.makeText(context, "다시 시도해주세요", Toast.LENGTH_SHORT).show()
                //            }
                //        }
                //    }),
                //    visualTransformation = PasswordVisualTransformation(),
                //    modifier = Modifier
                //        .padding(start = 20.dp, end = 20.dp, top = 8.dp)
                //        .fillMaxWidth()
                //        .height(48.dp),
                //    placeholder = { Text(text = "PassWord",fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
                //    leadingIcon = { Icon(painter = painterResource(id = R.drawable.icon_password), contentDescription = "")},
                //    colors = OutlinedTextFieldDefaults.colors(
                //        unfocusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
                //        focusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
                //        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                //        focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                //        unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                //        focusedContainerColor = MaterialTheme.colorScheme.primary,
                //        unfocusedLeadingIconColor = MaterialTheme.colorScheme.primaryContainer,
                //        focusedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
                //        cursorColor = design_intro_bg.copy(alpha = 0.5f)
                //    ),
                //    textStyle = TextStyle(
                //        color = MaterialTheme.colorScheme.onPrimary,
                //        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                //        fontSize = 16.sp, letterSpacing = (-0.4).sp
                //    ),
                //    shape = RoundedCornerShape(4.dp)
                //)
                //
                //Button(
                //    onClick = {
                //        scope.launch {
                //            isLoading = true
                //            val result = viewModel.onLoginButtonClick(userId = id, userPw = password, loginMethod = "EMAIL")
                //            if (result==0){
                //                isLoading = false
                //                MySharedPreference.setUserEmail(id)
                //                sharedViewModel.updateInit(true)
                //                sharedViewModel.updateDupleLogin(false)
                //                navController.navigate(Screen.MainScreen.route){
                //                    popUpTo(0)
                //                }
                //            }else if (result == 1){
                //                isLoading = false
                //                focusManager.clearFocus()
                //                snackbarHostState.showSnackbar(
                //                    message = "아이디 및 패스워드를 확인해주세요.",
                //                    actionLabel = "확인",
                //                    duration = SnackbarDuration.Short,
                //                    withDismissAction = false
                //                )
                //            }else{
                //                isLoading = false
                //                Toast.makeText(context, "다시 시도해주세요", Toast.LENGTH_SHORT).show()
                //            }
                //        } },
                //    modifier = Modifier
                //        .padding(top = 16.dp)
                //        .fillMaxWidth()
                //        .height(48.dp)
                //        .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
                //    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                //    colors = ButtonDefaults.buttonColors(containerColor = design_button_bg)
                //)
                //{
                //    Text(text = "로그인", color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
                //}
                //
                //Row (modifier = Modifier
                //    .fillMaxWidth()
                //    .padding(top = 16.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                //    //Text(
                //    //    text = "아이디 찾기",
                //    //    fontSize = 14.sp,
                //    //    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                //    //    color = MaterialTheme.colorScheme.onPrimary,
                //    //    modifier = Modifier.clickable { navController.navigate(route = Screen.IdPwSearch.route+"/0") })
                //
                //    //Spacer(modifier = Modifier
                //    //    .padding(horizontal = 9.dp)
                //    //    .size(2.dp, 8.dp)
                //    //    .background(color = design_login_text))
                //
                //    Text(
                //        text = "비밀번호 찾기",
                //        fontSize = 14.sp,
                //        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                //        color = MaterialTheme.colorScheme.onPrimary,
                //        modifier = Modifier.clickable {  navController.navigate(route = Screen.PwSearchScreen.route) })
                //
                //    Spacer(modifier = Modifier
                //        .padding(horizontal = 9.dp)
                //        .size(2.dp, 8.dp)
                //        .background(color = design_login_text))
                //
                //    Text(
                //        text = "회원가입",
                //        fontSize = 14.sp,
                //        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                //        color = MaterialTheme.colorScheme.onPrimary,
                //        modifier = Modifier.clickable {
                //            navController.navigate(Screen.UserCreate.route)
                //        })
                //}

                // ------------- 일반 사용자 로그인 ---------------------

                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 80.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center){
                    Divider(modifier=Modifier.size(92.dp,1.dp), color = design_textFieldOutLine)
                    Text(text = stringResource(R.string.login_with_sns), modifier = Modifier.padding(horizontal = 14.dp),
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        color = MaterialTheme.colorScheme.onPrimary)
                    Divider(modifier=Modifier.size(92.dp,1.dp), color = design_textFieldOutLine)
                }

                // viewModel에 있는 stateFlow를 추적 시작
                val isLoggedIn = viewModel.isLoggedIn.collectAsState()
                val loginStatusInfo = if(isLoggedIn.value) "로그인 상태" else "로그아웃 상태"

                Box{
                    Button(onClick = {
                        scope.launch {
                            val kakaoLoginResult = viewModel.kakaoLogin(context)
                            if (kakaoLoginResult){

                                val loginResult = viewModel.onLoginButtonClick(snsEmail, snsUnqId, "KAKAO")
                                if (loginResult == 0){
                                    sharedViewModel.updateInit(true)
                                    sharedViewModel.updateDupleLogin(false)
                                    navController.navigate(Screen.MainScreen.route){
                                        popUpTo(0)
                                    }
                                }else if (loginResult == 1){
                                    viewModel.updateLoginMethod("KAKAO")
                                    navController.navigate(Screen.EasyRegScreen.route)
                                }else{
                                    // 실패시의 상황을 하나로 상정하면 안됨.
                                    // 통신실패의 경우에는 토스트를 띄어 다시시도하기 유도
                                    Toast.makeText(context, R.string.retry, Toast.LENGTH_SHORT).show()
                                }
                            }else{
                                Toast.makeText(context, "Kakao 로그인 실패", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                        modifier = Modifier
                            .padding(top = 32.dp)
                            .fillMaxWidth()
                            .height(48.dp)
                            .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = design_login_kakaobtn, contentColor = design_login_text)
                    ) {
                        Box (modifier = Modifier.fillMaxSize()){
                            Icon(painter = painterResource(id = R.drawable.icon_kakao), contentDescription = "",
                                modifier = Modifier.align(Alignment.CenterStart), tint = Color.Unspecified)
                            Text(text = stringResource(R.string.login_with_kakao), modifier = Modifier.align(Alignment.Center),
                                fontSize = 14.sp, letterSpacing = (-0.7).sp,
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)))
                        }
                    }

                    if (MySharedPreference.getLastLoginMethod() == "KAKAO"){
                        Column(
                            modifier = Modifier
                                .width(IntrinsicSize.Max)
                                .align(Alignment.TopEnd)
                                .padding(end = 40.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .background(
                                        color = design_sharp.copy(alpha = 0.8f),
                                        shape = RoundedCornerShape(8.dp, 8.dp, 8.dp, 8.dp)
                                    )
                                    .wrapContentWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    stringResource(R.string.last_login_method),
                                    fontSize = 14.sp, letterSpacing = (-0.7).sp,
                                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                    color = design_white,
                                    modifier = Modifier.padding(horizontal = 8.dp))
                            }
                            Column(
                                modifier = Modifier
                                    .background(
                                        color = design_sharp.copy(alpha = 1.0f),
                                        shape = TriangleEdgeShape(30)
                                    )
                                    .width(8.dp)
                                    .align(Alignment.CenterHorizontally)
                            ) {
                            }
                        }
                    }
                }


                Box {
                    Button(onClick = {
                        scope.launch {
                            val naverLoginResult = viewModel.naverLogin(context)
                            if (naverLoginResult){

                                val loginResult = viewModel.onLoginButtonClick(snsEmail, snsUnqId, "NAVER")
                                // 가져온 정보로 로그인 시도, 성공시 메인// 실패시 가입
                                if (loginResult == 0){
                                    sharedViewModel.updateInit(true)
                                    sharedViewModel.updateDupleLogin(false)
                                    navController.navigate(Screen.MainScreen.route){
                                        popUpTo(0)
                                    }
                                }else if(loginResult == 1){
                                    viewModel.updateLoginMethod("NAVER")
                                    navController.navigate(Screen.EasyRegScreen.route)
                                }else{
                                    Toast.makeText(context, R.string.retry, Toast.LENGTH_SHORT).show()
                                }
                            }else{
                                Toast.makeText(context, "Naver 로그인 실패", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth()
                            .height(48.dp)
                            .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = design_login_naverbtn, contentColor = design_white)
                    ) {
                        Box (modifier = Modifier.fillMaxSize()){
                            Icon(painter = painterResource(id = R.drawable.icon_naver), contentDescription = "",
                                modifier = Modifier.align(Alignment.CenterStart), tint = Color.Unspecified)
                            Text(text = stringResource(R.string.login_with_naver), modifier = Modifier.align(Alignment.Center),
                                fontSize = 14.sp, letterSpacing = (-0.7).sp,
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)))
                        }
                    }

                    if (MySharedPreference.getLastLoginMethod() == "NAVER"){
                        Column(
                            modifier = Modifier
                                .width(IntrinsicSize.Max)
                                .align(Alignment.TopEnd)
                                .padding(end = 40.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .background(
                                        color = design_sharp.copy(alpha = 1.0f),
                                        shape = RoundedCornerShape(8.dp, 8.dp, 8.dp, 8.dp)
                                    )
                                    .wrapContentWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = stringResource(id = R.string.last_login_method),
                                    fontSize = 14.sp, letterSpacing = (-0.7).sp,
                                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                    color = design_white,
                                    modifier = Modifier.padding(horizontal = 8.dp))
                            }
                            Column(
                                modifier = Modifier
                                    .background(
                                        color = design_sharp.copy(alpha = 1.0f),
                                        shape = TriangleEdgeShape(30)
                                    )
                                    .width(8.dp)
                                    .align(Alignment.CenterHorizontally)
                            ) {
                            }
                        }
                    }
                }

                Box {
                    Button(
                        onClick = {
                            scope.launch {
                                val signInIntent = mGoogleSignInClient.signInIntent
                                googleAuthLauncher.launch(signInIntent)
                            } },
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(top = 16.dp)
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
                            Text(text = stringResource(R.string.login_with_google), modifier = Modifier.align(Alignment.Center),
                                fontSize = 14.sp,letterSpacing = (-0.7).sp,
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)))
                        }
                    }

                    if (MySharedPreference.getLastLoginMethod() == "GOOGLE"){
                        Column(
                            modifier = Modifier
                                .width(IntrinsicSize.Max)
                                .align(Alignment.TopEnd)
                                .padding(end = 40.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .background(
                                        color = design_sharp.copy(alpha = 1.0f),
                                        shape = RoundedCornerShape(8.dp, 8.dp, 8.dp, 8.dp)
                                    )
                                    .wrapContentWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    stringResource(id = R.string.last_login_method),
                                    fontSize = 14.sp, letterSpacing = (-0.7).sp,
                                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                    color = design_white,
                                    modifier = Modifier.padding(horizontal = 8.dp))
                            }
                            Column(
                                modifier = Modifier
                                    .background(
                                        color = design_sharp.copy(alpha = 1.0f),
                                        shape = TriangleEdgeShape(30)
                                    )
                                    .width(8.dp)
                                    .align(Alignment.CenterHorizontally)
                            ) {
                            }
                        }
                    }
                }
            }// col
        }
    }

    AnimatedVisibility(
        visible = requirePermission ,
        enter = slideInVertically(initialOffsetY = {it/2}) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = {it/2}) + fadeOut()
    ) {
        PermissionScreen(permissionState = permissionState, onCheck = {newValue -> requirePermission = newValue})
    }

}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
    systemUiController.setSystemBarsColor(color = MaterialTheme.colorScheme.primary)

    val allCheck by viewModel.allCheck.collectAsState()
    val memberCheck by viewModel.memberCheck.collectAsState()
    val personCheck by viewModel.personCheck.collectAsState()
    val marketingCheck by viewModel.marketingCheck.collectAsState()
    val snsLogin by viewModel.loginMethod.collectAsState()
    val unqId by viewModel.unqId.collectAsState()
    val email by viewModel.email.collectAsState()
    val context = LocalContext.current

    //val nickName by viewModel.nickName.collectAsState()
    val nickName by userCreateViewModel.userNickName.collectAsState()
    val nickNamePass by userCreateViewModel.userNickNamePass.collectAsState()

    val focusManager = LocalFocusManager.current
    val snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }
    val scope = rememberCoroutineScope()

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
            BackTopBar(title = stringResource(R.string.sign_up_), navController = navController)
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)}
    ) { paddingValues ->

        Box (modifier = Modifier
            .padding(paddingValues)
            .background(color = MaterialTheme.colorScheme.primary)
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
                                MaterialTheme.colorScheme.onPrimary
                            },
                            shape = RoundedCornerShape(12.dp)
                        )
                        .background(
                            color =
                            if (allCheck) {
                                design_login_bg
                            } else {
                                Color.Transparent
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

                    Text(
                        text = stringResource(R.string.agree_all_term),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                        color = if (allCheck) design_intro_bg else MaterialTheme.colorScheme.onPrimary, modifier = Modifier.offset(x = (-8).dp),
                        letterSpacing = (-0.7).sp,
                    )
                }

                Spacer(modifier = Modifier.padding(top = 16.dp))

                AgreeComponent(
                    title = stringResource(R.string.agree_terms_service),
                    mainText = stringResource(R.string.agree_terms_service_main_text),
                    check = memberCheck,
                    onClick = { newValue -> viewModel.updateMemberCheck(newValue)})

                AgreeComponent(
                    title = stringResource(R.string.agree_privacy_policy),
                    mainText = "마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용",
                    check = personCheck,
                    onClick = { newValue -> viewModel.updatePersonCheck(newValue)})

                AgreeComponent(
                    title = stringResource(R.string.agree_marketing),
                    mainText = "마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용 마케팅수신동의 내용",
                    check = marketingCheck,
                    onClick = { newValue -> viewModel.updateMarketingCheck(newValue)})

                Spacer(modifier = Modifier.padding(top = 16.dp))

                Row (Modifier.fillMaxWidth()){
                    Text(text = stringResource(id = R.string.nickname), fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                        modifier=Modifier.padding(start = 20.dp), color = MaterialTheme.colorScheme.onPrimary)
                    Text(
                        text = "*",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                        color= design_sharp
                    )
                }

                CustomTextField(
                    value = nickName,
                    onValueChange = {userCreateViewModel.updateUserNickName(it)},
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next),
                    modifier = Modifier
                        .padding(start = 20.dp, top = 8.dp, end = 20.dp)
                        .fillMaxWidth()
                        .height(48.dp),
                    placeholder = { Text(text = stringResource(id = R.string.place_holder_nickname), fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                        focusedContainerColor = MaterialTheme.colorScheme.primary,
                        unfocusedLeadingIconColor = MaterialTheme.colorScheme.primaryContainer,
                        focusedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
                        cursorColor = design_intro_bg.copy(alpha = 0.5f)
                    ),
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 16.sp, letterSpacing = (-0.4).sp
                    ),
                    shape = RoundedCornerShape(4.dp),
                    innerPadding = PaddingValues(start=16.dp),
                    trailingIcon = {
                        AnimatedVisibility(
                            visible = nickName.isNotEmpty(),
                            enter = scaleIn(),
                            exit = scaleOut()
                        ) {
                            Button(
                                onClick = {
                                    scope.launch {
                                        val result = userCreateViewModel.nickNameCheck()
                                        if (result){
                                            focusManager.clearFocus()
                                            userCreateViewModel.updateUserNickNamePass(nickName)
                                            snackbarHostState.showSnackbar(
                                                message = context.getString(R.string.available_nickname),
                                                actionLabel = context.getString(R.string.confirm),
                                                duration = SnackbarDuration.Short,
                                                withDismissAction = false
                                            )
                                        }else{
                                            focusManager.clearFocus()
                                            snackbarHostState.showSnackbar(
                                                message = context.getString(R.string.already_use_nickname),
                                                actionLabel = context.getString(R.string.confirm),
                                                duration = SnackbarDuration.Short,
                                                withDismissAction = false
                                            )
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 0.dp),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.onPrimary),
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(60.dp, 32.dp),
                                contentPadding = PaddingValues(horizontal = 0.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.duplicate_check),
                                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                    fontSize = 12.sp, letterSpacing = (-0.6).sp,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                )

                Button(
                    onClick = {
                        scope.launch {
                            if(allCheck){
                                if (nickName.isEmpty()) {
                                    snackbarHostState.showSnackbar(
                                        message = context.getString(R.string.place_holder_nickname),
                                        actionLabel = context.getString(R.string.confirm),
                                        duration = SnackbarDuration.Short,
                                        withDismissAction = true
                                    )
                                } else if ( nickName != nickNamePass ) {
                                    snackbarHostState.showSnackbar(
                                        message = context.getString(R.string.check_duplicate_nickname),
                                        actionLabel = context.getString(R.string.confirm),
                                        duration = SnackbarDuration.Short,
                                        withDismissAction = true
                                    )
                                }else{
                                    userCreateViewModel.updateUserNickName(nickName)
                                    userCreateViewModel.updateUserID(email)
                                    userCreateViewModel.updateUserName(nickName)
                                    userCreateViewModel.updateSnsLogin(snsLogin)
                                    userCreateViewModel.updateUserPW(unqId)
                                    navController.navigate(Screen.PetCreateScreen.route)
                                }
                            }else{
                                Toast.makeText(context, R.string.please_agree_term, Toast.LENGTH_SHORT).show()
                            }
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
                    Text(text = stringResource(R.string.next), color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
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
                    color = MaterialTheme.colorScheme.onPrimary,
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
                contentDescription = "",  tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(end = 20.dp)
                    .align(Alignment.CenterEnd)
                    .clickable { expanded = !expanded }
            )
        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically(),
        ) {
            Column (
                modifier = Modifier
                    .heightIn(max = 300.dp)
                    .verticalScroll(rememberScrollState())
            ){
                Text(
                    text = mainText,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 12.sp,
                    letterSpacing = (-0.6).sp,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(start = 38.dp, end = 16.dp, top = 8.dp)
                )
            }
        }


        Spacer(modifier = Modifier
            .padding(top = 20.dp, bottom = 8.dp)
            .fillMaxWidth()
            .height(1.dp)
            .background(MaterialTheme.colorScheme.outline))
    }
}

fun checkAndRequestPermissions(
    context: Context,
    permissions: Array<String>,
    launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
) {

    /** 권한이 이미 있는 경우 **/
    if (permissions.all {
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }) {
        Log.d("test5", "권한이 이미 존재합니다.")
    }

    /** 권한이 없는 경우 **/
    else {
        launcher.launch(permissions)
        Log.d("test5", "권한을 요청하였습니다.")
    }
}

class TriangleEdgeShape(val offset: Int) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val trianglePath = Path().apply {
            moveTo(x = size.width/2 - offset/2, y = size.height)
            lineTo(x = size.width/2, y = size.height + offset)
            lineTo(x = size.width/2 + offset/2, y = size.height)
        }
        return Outline.Generic(path = trianglePath)
    }
}