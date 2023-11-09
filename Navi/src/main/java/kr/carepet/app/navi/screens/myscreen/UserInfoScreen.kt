package kr.carepet.app.navi.screens.myscreen

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import kr.carepet.app.navi.R
import kr.carepet.app.navi.Screen
import kr.carepet.app.navi.component.BackTopBar
import kr.carepet.app.navi.component.CustomTextField
import kr.carepet.app.navi.ui.theme.design_button_bg
import kr.carepet.app.navi.ui.theme.design_login_text
import kr.carepet.app.navi.ui.theme.design_placeHolder
import kr.carepet.app.navi.ui.theme.design_sharp
import kr.carepet.app.navi.ui.theme.design_skip
import kr.carepet.app.navi.ui.theme.design_textFieldOutLine
import kr.carepet.app.navi.ui.theme.design_white
import kr.carepet.app.navi.viewmodel.SettingViewModel
import kr.carepet.singleton.G
import kr.carepet.singleton.MySharedPreference

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoScreen(navController:NavHostController, settingViewModel: SettingViewModel){

    val userPhoneNum by settingViewModel.userPhoneNum.collectAsState()
    val userPw by settingViewModel.userPw.collectAsState()
    val userPwCheck by settingViewModel.userPwCheck.collectAsState()
    var nickName by remember { mutableStateOf(G.userNickName) }
    val passedNick by settingViewModel.userNickNamePass.collectAsState()

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }
    var showDialog by remember{ mutableStateOf(false) }
    var withDraw by remember { mutableStateOf(false) }
    val loginMethod = MySharedPreference.getLastLoginMethod()

    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val fcmToken by settingViewModel.appKey.collectAsState()
    LaunchedEffect(Unit){
        settingViewModel.updateAppKey()
    }

    LaunchedEffect(key1 = withDraw){
        if (withDraw){
            scope.launch {
                val result = settingViewModel.withdraw()
                if (result){

                    G.userEmail = ""
                    G.userId = ""
                    G.refreshToken = ""
                    G.accessToken = ""

                    MySharedPreference.setUserId("")
                    MySharedPreference.setRefreshToken("")
                    MySharedPreference.setAccessToken("")
                    MySharedPreference.setLastLoginMethod("")
                    MySharedPreference.setIsLogin(false)

                    snackbarHostState.showSnackbar(
                        message = "회원탈퇴를 완료했습니다",
                        actionLabel = "확인",
                        duration = SnackbarDuration.Short,
                        withDismissAction = true
                    )

                    navController.navigate(Screen.Login.route){
                        popUpTo(0)
                    }


                }else{
                    snackbarHostState.showSnackbar(
                        message = "회원탈퇴를 실패해습니다",
                        actionLabel = "확인",
                        duration = SnackbarDuration.Short,
                        withDismissAction = true
                    )
                }
                withDraw = false
            }
        }
    }

    Scaffold (
        topBar = { BackTopBar(title = "개인 정보 수정", navController = navController) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->

        if (showDialog){
            CustomDialogDelete(
                onDismiss = { newValue -> showDialog = newValue },
                confirm = "탈퇴하기",
                dismiss = "취소",
                title = "회원 탈퇴하기",
                text = "정말 탈퇴하시겠어요?",
                valueChange = { newValue -> withDraw = newValue}
            )
        }

        Column (
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(design_white)
        ){
            Spacer(modifier = Modifier.padding(top = 20.dp))
            
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
                value = nickName,
                onValueChange = { nickName = it},
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
                    focusedContainerColor = design_white),
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
                                    val result = settingViewModel.nickNameCheck(nickName)
                                    if (result){
                                        settingViewModel.updateUserNickNamePass(nickName)
                                        focusManager.clearFocus()
                                        snackbarHostState.showSnackbar(
                                            message = "사용하실 수 있는 닉네임입니다",
                                            actionLabel = "확인",
                                            duration = SnackbarDuration.Short,
                                            withDismissAction = true
                                        )
                                    }else{
                                        focusManager.clearFocus()
                                        snackbarHostState.showSnackbar(
                                            message = "이미 사용중인 닉네임입니다",
                                            actionLabel = "확인",
                                            duration = SnackbarDuration.Short,
                                            withDismissAction = true
                                        )
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = design_white),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 0.dp),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, color = design_login_text),
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(60.dp, 32.dp),
                            contentPadding = PaddingValues(horizontal = 0.dp)
                        ) {
                            Text(
                                text = "중복확인",
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                fontSize = 12.sp, letterSpacing = (-0.6).sp,
                                color = design_login_text
                            )
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.padding(top = 16.dp))

            Row (Modifier.fillMaxWidth()){
                Text(text = "휴대폰 번호", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    modifier=Modifier.padding(start = 20.dp), color = design_login_text)
                Text(
                    text = "*",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    color= design_sharp
                )
            }
            CustomTextField(
                value = userPhoneNum,
                onValueChange = {settingViewModel.updateUserPhoneNum(it)},
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next),
                modifier = Modifier
                    .padding(start = 20.dp, top = 8.dp, end = 20.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                placeholder = { Text(text = "휴대폰 번호를 입력해주세요", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedPlaceholderColor = design_placeHolder,
                    focusedPlaceholderColor = design_placeHolder,
                    unfocusedBorderColor = design_textFieldOutLine,
                    focusedBorderColor = design_login_text,
                    unfocusedContainerColor = design_white,
                    focusedContainerColor = design_white),
                shape = RoundedCornerShape(4.dp),
                innerPadding = PaddingValues(start=16.dp)
            )

            Spacer(modifier = Modifier.padding(top = 16.dp))

            Row {
                Text(text = "비밀번호 변경", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    modifier= Modifier
                        .padding(start = 20.dp)
                        .alignByBaseline(),
                    color = design_login_text)

                if (loginMethod!="EMAIL"){

                    Text(text = "(간편가입시 비밀번호 변경이 불가능합니다)",
                        fontSize = 12.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                        letterSpacing = (-0.6).sp,
                        modifier= Modifier
                            .padding(start = 8.dp)
                            .alignByBaseline(),
                        color = design_skip
                    )
                }
            }

            CustomTextField(
                enabled = loginMethod == "EMAIL",
                value = userPw,
                onValueChange = {settingViewModel.updateUserPw(it)},
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .padding(start = 20.dp, top = 8.dp, end = 20.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                placeholder = { Text(text = "8~16자 영문/숫자 조합", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedPlaceholderColor = design_placeHolder,
                    focusedPlaceholderColor = design_placeHolder,
                    unfocusedBorderColor = design_textFieldOutLine,
                    focusedBorderColor = design_login_text,
                    unfocusedContainerColor = design_white,
                    focusedContainerColor = design_white),
                shape = RoundedCornerShape(4.dp),
                innerPadding = PaddingValues(start=16.dp)
            )

            CustomTextField(
                enabled = loginMethod == "EMAIL",
                value = userPwCheck,
                onValueChange = {settingViewModel.updateUserPwCheck(it)},
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .padding(start = 20.dp, top = 8.dp, end = 20.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                placeholder = { Text(text = "비밀번호 확인", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedPlaceholderColor = design_placeHolder,
                    focusedPlaceholderColor = design_placeHolder,
                    unfocusedBorderColor = design_textFieldOutLine,
                    focusedBorderColor = design_login_text,
                    unfocusedContainerColor = design_white,
                    focusedContainerColor = design_white),
                shape = RoundedCornerShape(4.dp),
                innerPadding = PaddingValues(start=16.dp)
            )

            Button(
                onClick = {
                    scope.launch {
                        if (G.userNickName == nickName){
                            Log.d("userInfo","변경안함")
                        }else if( nickName != passedNick ){
                            snackbarHostState.showSnackbar(
                                message = "닉네임 중복확인을 해주세요",
                                actionLabel = "확인",
                                duration = SnackbarDuration.Short,
                                withDismissAction = true
                            )
                        }else if (nickName == passedNick){
                            val result = settingViewModel.resetNickName()
                            if (result){
                                snackbarHostState.showSnackbar(
                                    message = "닉네임을 변경했습니다",
                                    actionLabel = "확인",
                                    duration = SnackbarDuration.Short,
                                    withDismissAction = true
                                )
                            }else{
                                snackbarHostState.showSnackbar(
                                    message = "닉네임 변경에 실패했습니다",
                                    actionLabel = "확인",
                                    duration = SnackbarDuration.Short,
                                    withDismissAction = true
                                )
                            }
                        }

                        if (userPw == ""){
                            Log.d("userInfo","변경x")
                        }else if( userPw != userPwCheck){
                            snackbarHostState.showSnackbar(
                                message = "비밀번호가 일치하지 않습니다",
                                actionLabel = "확인",
                                duration = SnackbarDuration.Short,
                                withDismissAction = true
                            )
                        }else if (userPw == userPwCheck){
                            val result = settingViewModel.resetPw()
                            if (result){
                                snackbarHostState.showSnackbar(
                                    message = "비밀번호가 변경되었습니다",
                                    actionLabel = "확인",
                                    duration = SnackbarDuration.Short,
                                    withDismissAction = true
                                )
                            }else{
                                snackbarHostState.showSnackbar(
                                    message = "비밀번호변경에 실패했습니다",
                                    actionLabel = "확인",
                                    duration = SnackbarDuration.Short,
                                    withDismissAction = true
                                )
                            }
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
                Text(text = "수정하기",
                    color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    letterSpacing = (-0.7).sp)
            }

            Spacer(modifier = Modifier.padding(top = 20.dp))

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ){
                Button(
                    onClick = {
                              showDialog =true
                    },
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .height(30.dp)
                        .width(80.dp),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = design_sharp)
                ) {
                    Text(text = "회원탈퇴",
                        color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp)
                }
            }

            Button(
                onClick = {
                    val clipData = ClipData.newPlainText("Text", fcmToken)
                    clipboardManager.setPrimaryClip(clipData)
                },
                modifier = Modifier
                    .padding(start = 20.dp ,end = 20.dp, top = 30.dp)
                    .fillMaxWidth()
                    .height(48.dp)
                ,
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = design_sharp)
            ) {
                Text(text = "토큰복사",
                    color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    letterSpacing = (-0.7).sp)
            }
        }//col
    }
}