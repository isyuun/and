@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package net.pettip.app.navi.screens

import android.util.Patterns
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.Screen
import net.pettip.app.navi.component.BackTopBar
import net.pettip.app.navi.component.CustomTextField
import net.pettip.app.navi.ui.theme.design_btn_border
import net.pettip.app.navi.ui.theme.design_button_bg
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_login_text
import net.pettip.app.navi.ui.theme.design_placeHolder
import net.pettip.app.navi.ui.theme.design_sharp
import net.pettip.app.navi.ui.theme.design_textFieldOutLine
import net.pettip.app.navi.ui.theme.design_weather_4
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.UserCreateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserCreateScreen(modifier:Modifier=Modifier, navController: NavHostController, viewModel: UserCreateViewModel){

    val scope = rememberCoroutineScope()

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color = MaterialTheme.colorScheme.primary)

    val id by viewModel.userID.collectAsState()
    val pw by viewModel.userPW.collectAsState()
    val pwCheck by viewModel.userPWCheck.collectAsState()
    val nickName by viewModel.userNickName.collectAsState()
    val nickNamePass by viewModel.userNickNamePass.collectAsState()

    // -- 추가 예정 --
    val phoneNum by viewModel.userPhone.collectAsState()
    val certiNum by viewModel.certiNum.collectAsState()
    // -- 추가 예정 --

    var snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }
    var countTime by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    var timer by remember { mutableStateOf(5*60) }

    LaunchedEffect(key1 = timer, key2 = countTime) {
        if (timer > 0 && countTime) {
            delay(1000)
            timer -= 1
        }else if( timer==0){
            countTime = false
            timer = 5*60
        }
    }

    LaunchedEffect(Unit){
        viewModel.updateAppKey()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            BackTopBar(title = "회원가입", navController = navController)
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)}
    ) {paddingValues ->
        Column(
            modifier= modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Spacer(modifier = Modifier.padding(vertical = 10.dp))

            Row (Modifier.fillMaxWidth()){
                Text(text = "아이디", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    modifier=Modifier.padding(start = 20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = "*",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    color= design_sharp
                )
            }
            CustomTextField(
                value = id,
                onValueChange = {viewModel.updateUserID(it)},
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next),
                modifier = Modifier
                    .padding(start = 20.dp, top = 8.dp, end = 20.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                placeholder = { Text(text = "이메일 주소를 입력해주세요", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
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
                innerPadding = PaddingValues(start=16.dp)
            )

            Spacer(modifier = Modifier.padding(top = 16.dp))
            
            Row (Modifier.fillMaxWidth()){
                Text(text = "비밀번호", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    modifier=Modifier.padding(start = 20.dp),
                    color = MaterialTheme.colorScheme.onPrimary)
                Text(
                    text = "*",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    color= design_sharp
                )
            }
            CustomTextField(
                value = pw,
                onValueChange = {viewModel.updateUserPW(it)},
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next),
                modifier = Modifier
                    .padding(start = 20.dp, top = 8.dp, end = 20.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                visualTransformation = PasswordVisualTransformation(),
                placeholder = { Text(text = "8~16자 영문/숫자 조합", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
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
                innerPadding = PaddingValues(start=16.dp)
            )

            CustomTextField(
                value = pwCheck,
                onValueChange = {viewModel.updateUserPWCheck(it)},
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next),
                modifier = Modifier
                    .padding(start = 20.dp, top = 8.dp, end = 20.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                visualTransformation = PasswordVisualTransformation(),
                placeholder = { Text(text = "비밀번호 확인", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
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
                innerPadding = PaddingValues(start=16.dp)
            )

            Spacer(modifier = Modifier.padding(top = 16.dp))

            Row (Modifier.fillMaxWidth()){
                Text(text = "닉네임", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    modifier=Modifier.padding(start = 20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = "*",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    color= design_sharp
                )
            }
            CustomTextField(
                value = nickName,
                onValueChange = {viewModel.updateUserNickName(it)},
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
                                    val result = viewModel.nickNameCheck()
                                    if (result){
                                        focusManager.clearFocus()
                                        viewModel.updateUserNickNamePass(nickName)
                                        snackbarHostState.showSnackbar(
                                            message = "사용하실 수 있는 닉네임입니다",
                                            actionLabel = "확인",
                                            duration = SnackbarDuration.Short,
                                            withDismissAction = false
                                        )
                                    }else{
                                        focusManager.clearFocus()
                                        snackbarHostState.showSnackbar(
                                            message = "이미 사용중인 닉네임입니다",
                                            actionLabel = "확인",
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
                                text = "중복확인",
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                fontSize = 12.sp, letterSpacing = (-0.6).sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.padding(top = 16.dp))

            // ------------------ 휴대폰 인증 ---------------------

            //Row (Modifier.fillMaxWidth()){
            //    Text(text = "휴대폰 번호", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            //        modifier=Modifier.padding(start = 20.dp), color = MaterialTheme.colorScheme.onPrimary)
            //    Text(
            //        text = "*",
            //        fontSize = 16.sp,
            //        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            //        color= design_sharp
            //    )
            //}
            //
            //Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
            //    CustomTextField(
            //        value = phoneNum,
            //        onValueChange = {viewModel.updateUserPhone(it)},
            //        singleLine = true,
            //        keyboardOptions = KeyboardOptions(
            //            keyboardType = KeyboardType.Number,
            //            imeAction = ImeAction.Next),
            //        modifier = Modifier
            //            .padding(start = 20.dp, top = 8.dp, end = 8.dp)
            //            .weight(1f)
            //            .height(48.dp),
            //        placeholder = { Text(text = "“-” 없이 숫자만", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
            //        colors = OutlinedTextFieldDefaults.colors(
            //            unfocusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
            //            focusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
            //            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            //            focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
            //            unfocusedContainerColor = MaterialTheme.colorScheme.primary,
            //            focusedContainerColor = MaterialTheme.colorScheme.primary,
            //            unfocusedLeadingIconColor = MaterialTheme.colorScheme.primaryContainer,
            //            focusedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
            //            cursorColor = design_intro_bg.copy(alpha = 0.5f)
            //        ),
            //        textStyle = TextStyle(
            //            color = MaterialTheme.colorScheme.onPrimary,
            //            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            //            fontSize = 16.sp, letterSpacing = (-0.4).sp
            //        ),
            //        shape = RoundedCornerShape(4.dp),
            //        innerPadding = PaddingValues(start=16.dp)
            //    )
            //
            //    Button(
            //        onClick = {
            //            countTime = true
            //            focusManager.moveFocus(FocusDirection.Next)
            //        },
            //        modifier = Modifier
            //            .padding(end = 20.dp, top = 8.dp)
            //            .wrapContentWidth()
            //            .height(48.dp),
            //        shape = RoundedCornerShape(12.dp),
            //        colors = ButtonDefaults.buttonColors(
            //            containerColor = MaterialTheme.colorScheme.primary,
            //            contentColor = MaterialTheme.colorScheme.onPrimary
            //        ),
            //        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.outlineVariant),
            //        contentPadding = PaddingValues(start = 14.dp,end=14.dp)
            //    ) {
            //        Text(text = "인증번호 발송", color = MaterialTheme.colorScheme.onPrimary,
            //            fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            //            letterSpacing = (-0.7).sp)
            //    }
            //
            //}
            //
            //Box (
            //    modifier = Modifier
            //        .padding(start = 20.dp, top = 8.dp, end = 20.dp)
            //        .fillMaxWidth()
            //        .height(48.dp)
            //){
            //    CustomTextField(
            //        value = certiNum,
            //        onValueChange = {viewModel.updateCertiNum(it)},
            //        singleLine = true,
            //        modifier = Modifier.fillMaxSize(),
            //        keyboardOptions = KeyboardOptions(
            //            keyboardType = KeyboardType.Number,
            //            imeAction = ImeAction.Done),
            //        placeholder = { Text(text = "인증번호 입력", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
            //        colors = OutlinedTextFieldDefaults.colors(
            //            unfocusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
            //            focusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
            //            unfocusedBorderColor = if (countTime) design_sharp else MaterialTheme.colorScheme.outline,
            //            focusedBorderColor = if (countTime) design_sharp else MaterialTheme.colorScheme.onPrimary,
            //            unfocusedContainerColor = MaterialTheme.colorScheme.primary,
            //            focusedContainerColor = MaterialTheme.colorScheme.primary,
            //            unfocusedLeadingIconColor = MaterialTheme.colorScheme.primaryContainer,
            //            focusedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
            //            cursorColor = design_intro_bg.copy(alpha = 0.5f)
            //        ),
            //        textStyle = TextStyle(
            //            color = MaterialTheme.colorScheme.onPrimary,
            //            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            //            fontSize = 16.sp, letterSpacing = (-0.4).sp
            //        ),
            //        shape = RoundedCornerShape(4.dp),
            //        innerPadding = PaddingValues(start=16.dp)
            //    )
            //
            //    if(countTime){
            //        Text(
            //            text = "${String.format("%02d", timer / 60)}:${String.format("%02d", timer % 60)}",
            //            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            //            fontSize = 14.sp,
            //            letterSpacing = (-0.7).sp,
            //            color = design_weather_4,
            //            modifier = Modifier
            //                .padding(end = 16.dp)
            //                .align(Alignment.CenterEnd)
            //        )
            //    }
            //}

            Button(
                onClick = {
                    scope.launch {
                        if (!Patterns.EMAIL_ADDRESS.matcher(id).matches()) {
                            snackbarHostState.showSnackbar(
                                message = "올바른 이메일형식이 아닙니다",
                                actionLabel = "확인",
                                duration = SnackbarDuration.Short,
                                withDismissAction = true
                            )
                        } else if (pw.isEmpty()) {
                            snackbarHostState.showSnackbar(
                                message = "비밀번호를 입력해주세요",
                                actionLabel = "확인",
                                duration = SnackbarDuration.Short,
                                withDismissAction = true
                            )
                        }else if (!isAlphaNumeric(pw)) {
                            snackbarHostState.showSnackbar(
                                message = "올바른 비밀번호 형식이 아닙니다",
                                actionLabel = "확인",
                                duration = SnackbarDuration.Short,
                                withDismissAction = true
                            )
                        }else if (pw != pwCheck) {
                            snackbarHostState.showSnackbar(
                                message = "비밀번호가 일치하지 않습니다",
                                actionLabel = "확인",
                                duration = SnackbarDuration.Short,
                                withDismissAction = true
                            )
                        } else if (nickName.isEmpty()) {
                            snackbarHostState.showSnackbar(
                                message = "닉네임을 입력해주세요",
                                actionLabel = "확인",
                                duration = SnackbarDuration.Short,
                                withDismissAction = true
                            )
                        } else if ( nickName != nickNamePass ) {
                            snackbarHostState.showSnackbar(
                                message = "닉네임 중복확인을 해주세요",
                                actionLabel = "확인",
                                duration = SnackbarDuration.Short,
                                withDismissAction = true
                            )
                        } else {
                            viewModel.updateSnsLogin("EMAIL")
                            navController.navigate(Screen.PetCreateScreen.route)
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
                Text(text = "다음", color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
            }

        }
    }
}

fun isAlphaNumeric(input: String): Boolean {
    val pattern = Regex("^[a-zA-Z0-9]{8,16}\$")
    return pattern.matches(input)
}