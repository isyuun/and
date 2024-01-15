package net.pettip.app.navi.screens

import android.util.Patterns
import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
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
import net.pettip.app.navi.viewmodel.LoginViewModel
import net.pettip.util.Log

/**
 * @Project     : PetTip-Android
 * @FileName    : PwSearchScreen
 * @Date        : 2024-01-15
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screens
 * @see net.pettip.app.navi.screens.PwSearchScreen
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PwSearchScreen(
    navController: NavHostController,
    viewModel: LoginViewModel
){
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color = MaterialTheme.colorScheme.primary)
    systemUiController.setNavigationBarColor(color= MaterialTheme.colorScheme.primary)

    val email by viewModel.pwSearchEmail.collectAsState()
    val certiNum by viewModel.certiNum.collectAsState()
    val countTime by viewModel.countTime.collectAsState()
    val timer by viewModel.timer.collectAsState()
    val toPwChange by viewModel.toPwChange.collectAsState()

    var originPw by remember{ mutableStateOf("") }
    var newPw by remember{ mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    var snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }

    LaunchedEffect(key1 = timer, key2 = countTime) {
        if (timer > 0 && countTime) {
            delay(1000)
            viewModel.updateTimer(timer - 1)
        }else if( timer==0){
            viewModel.updateCountTime(false)
            viewModel.updateTimer(5*60)
        }
    }

    DisposableEffect(Unit){
        onDispose {
            viewModel.updateToPwChange(false)
        }
    }

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            BackTopBar(title = "비밀번호 변경", navController = navController)
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Crossfade(
            targetState = toPwChange,
            label = "",
            animationSpec = tween(1000)
        ) {
            when(it){
                true ->
                    Column (
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.primary)
                    ){
                        Text(
                            text = "새 비밀번호를 입력해 주세요",
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                            modifier = Modifier.padding(start = 20.dp,top=20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )

                        CustomTextField(
                            value = originPw,
                            onValueChange = {originPw = it},
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Next),
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier
                                .padding(start = 20.dp, top = 8.dp, end = 20.dp)
                                .fillMaxWidth()
                                .height(48.dp),
                            placeholder = { Text(text = stringResource(R.string.place_holder_password), fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
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
                            value = newPw,
                            onValueChange = {newPw = it},
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Next),
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier
                                .padding(start = 20.dp, top = 8.dp, end = 20.dp)
                                .fillMaxWidth()
                                .height(48.dp),
                            placeholder = { Text(text = stringResource(R.string.place_holder_password_confirm), fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
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

                        Button(
                            onClick = {
                                scope.launch {
                                    focusManager.clearFocus()
                                    if (originPw.isBlank() || newPw.isBlank()){
                                        snackbarHostState.showSnackbar(
                                            message = "비밀번호를 입력해주세요",
                                            actionLabel = "확인",
                                            duration = SnackbarDuration.Short
                                        )
                                    }else if (originPw != newPw){
                                        snackbarHostState.showSnackbar(
                                            message = "비밀번호가 일치하지 않습니다.",
                                            actionLabel = "확인",
                                            duration = SnackbarDuration.Short
                                        )
                                    }else if (!isAlphaNumeric(newPw)){
                                        snackbarHostState.showSnackbar(
                                            message = "올바른 비밀번호 형식이 아닙니다.",
                                            actionLabel = "확인",
                                            duration = SnackbarDuration.Short
                                        )
                                    }else{
                                        // 여기서 비밀번호 변경 로직 수행
                                    }
                                }
                            },
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .fillMaxWidth()
                                .height(48.dp)
                                .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp,
                                disabledElevation = 4.dp,
                                pressedElevation = 0.dp,
                                hoveredElevation = 0.dp,
                                focusedElevation = 0.dp
                            ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = design_button_bg,
                                disabledContainerColor = design_button_bg
                            )
                        )
                        {
                            Text(text = "비밀번호 변경하기", color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
                        }
                    }
                false ->
                    Column (
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.primary)
                    ){
                        Text(
                            text = "이메일",
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                            modifier = Modifier.padding(start = 20.dp,top=20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )

                        Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                            CustomTextField(
                                value = email,
                                onValueChange = {viewModel.updatePwSearchEmail(it)},
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Next),
                                modifier = Modifier
                                    .padding(start = 20.dp, top = 8.dp, end = 8.dp)
                                    .weight(1f)
                                    .height(48.dp),
                                placeholder = { Text(text = "이메일을 입력해주세요", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp) },
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

                            Button(
                                enabled = !countTime,
                                onClick = {
                                    scope.launch {
                                        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                                            viewModel.updateCountTime(true)
                                            focusManager.moveFocus(FocusDirection.Next)
                                        }else{
                                            snackbarHostState.showSnackbar(
                                                message = "올바른 이메일형식이 아닙니다",
                                                actionLabel = "확인",
                                                duration = SnackbarDuration.Short
                                            )
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .padding(end = 20.dp, top = 8.dp)
                                    .wrapContentWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = design_white,
                                    disabledContainerColor = design_white
                                ),
                                border = BorderStroke(1.dp, color = design_btn_border),
                                contentPadding = PaddingValues(start = 14.dp,end=14.dp)
                            ) {
                                Text(text = "인증번호 발송", color = design_login_text,
                                    fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                    letterSpacing = (-0.7).sp)
                            }

                        }

                        Spacer(modifier = Modifier.padding(top = 8.dp))

                        Box{
                            CustomTextField(
                                enabled = countTime,
                                value = certiNum,
                                onValueChange = {viewModel.updateCertiNum(it)},
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next),
                                modifier = Modifier
                                    .padding(start = 20.dp, end = 20.dp)
                                    .fillMaxWidth()
                                    .height(48.dp),
                                placeholder = { Text(text = "인증번호 입력", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
                                    focusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
                                    unfocusedBorderColor = if (countTime) design_sharp else MaterialTheme.colorScheme.outline,
                                    focusedBorderColor = if (countTime) design_sharp else MaterialTheme.colorScheme.onPrimary,
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

                            if(countTime){
                                Text(
                                    text = "${String.format("%02d", timer / 60)}:${String.format("%02d", timer % 60)}",
                                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                    fontSize = 14.sp,
                                    letterSpacing = (-0.7).sp,
                                    color = design_weather_4,
                                    modifier = Modifier
                                        .padding(end = 36.dp)
                                        .align(Alignment.CenterEnd)
                                )
                            }
                        }

                        Button(
                            enabled = countTime,
                            onClick = {
                                viewModel.updateToPwChange(true)
                            },
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .fillMaxWidth()
                                .height(48.dp)
                                .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 4.dp,
                                disabledElevation = 4.dp,
                                pressedElevation = 0.dp,
                                hoveredElevation = 0.dp,
                                focusedElevation = 0.dp
                            ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = design_button_bg,
                                disabledContainerColor = design_button_bg
                            )
                        )
                        {
                            Text(text = "인증번호 확인", color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
                        }
                    }
            }
        }
    }
}