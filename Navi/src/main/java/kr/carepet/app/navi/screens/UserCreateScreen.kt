@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package kr.carepet.app.navi.screens

import android.util.Patterns
import android.widget.Toast
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.carepet.app.navi.R
import kr.carepet.app.navi.Screen
import kr.carepet.app.navi.component.BackTopBar
import kr.carepet.app.navi.component.CustomTextField
import kr.carepet.app.navi.ui.theme.design_btn_border
import kr.carepet.app.navi.ui.theme.design_button_bg
import kr.carepet.app.navi.ui.theme.design_login_text
import kr.carepet.app.navi.ui.theme.design_placeHolder
import kr.carepet.app.navi.ui.theme.design_sharp
import kr.carepet.app.navi.ui.theme.design_textFieldOutLine
import kr.carepet.app.navi.ui.theme.design_weather_4
import kr.carepet.app.navi.ui.theme.design_white
import kr.carepet.app.navi.viewmodel.UserCreateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserCreateScreen(modifier:Modifier=Modifier, navController: NavHostController, viewModel: UserCreateViewModel){

    val scope = rememberCoroutineScope()

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color = design_white)

    val id by viewModel.userID.collectAsState()
    val pw by viewModel.userPW.collectAsState()
    val pwCheck by viewModel.userPWCheck.collectAsState()
    val nickName by viewModel.userNickName.collectAsState()
    val phoneNum by viewModel.userPhone.collectAsState()
    val certiNum by viewModel.certiNum.collectAsState()

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

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            BackTopBar(title = "회원가입", navController = navController)
        }
    ) {paddingValues ->
        Column(
            modifier= modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(design_white)
        ) {
            Spacer(modifier = Modifier.padding(vertical = 10.dp))

            Row (Modifier.fillMaxWidth()){
                Text(text = "아이디", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    modifier=Modifier.padding(start = 20.dp), color = design_login_text)
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
                placeholder = { Text(text = "3~15자 영문/숫자 조합", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
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

            Spacer(modifier = Modifier.padding(top = 16.dp))
            
            Row (Modifier.fillMaxWidth()){
                Text(text = "비밀번호", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    modifier=Modifier.padding(start = 20.dp), color = design_login_text)
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

            Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                CustomTextField(
                    value = phoneNum,
                    onValueChange = {viewModel.updateUserPhone(it)},
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next),
                    modifier = Modifier
                        .padding(start = 20.dp, top = 8.dp, end = 8.dp)
                        .weight(1f)
                        .height(48.dp),
                    placeholder = { Text(text = "“-” 없이 숫자만", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
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
                        countTime = true
                        focusManager.moveFocus(FocusDirection.Next)
                    },
                    modifier = Modifier
                        .padding(end = 20.dp, top = 8.dp)
                        .wrapContentWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(design_white),
                    border = BorderStroke(1.dp, color = design_btn_border),
                    contentPadding = PaddingValues(start = 14.dp,end=14.dp)
                ) {
                    Text(text = "인증번호 발송", color = design_login_text,
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp)
                }

            }

            Box (
                modifier = Modifier
                    .padding(start = 20.dp, top = 8.dp, end = 20.dp)
                    .fillMaxWidth()
                    .height(48.dp)
            ){
                CustomTextField(
                    value = certiNum,
                    onValueChange = {viewModel.updateCertiNum(it)},
                    singleLine = true,
                    modifier = Modifier.fillMaxSize(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done),
                    placeholder = { Text(text = "인증번호 입력", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedPlaceholderColor = design_placeHolder,
                        focusedPlaceholderColor = design_placeHolder,
                        unfocusedBorderColor = design_textFieldOutLine,
                        focusedBorderColor = if(countTime) design_weather_4 else design_login_text,
                        unfocusedContainerColor = design_white,
                        focusedContainerColor = design_white,
                        unfocusedLeadingIconColor = design_placeHolder,
                        focusedLeadingIconColor = design_login_text),
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
                            .padding(end = 16.dp)
                            .align(Alignment.CenterEnd)
                    )
                }
            }

            Button(
                onClick = {
                    if (!Patterns.EMAIL_ADDRESS.matcher(id).matches()) {
                        Toast.makeText(context, "올바른 이메일 형식이 아닙니다", Toast.LENGTH_SHORT).show()
                    } else if (pw.isEmpty()) {
                        Toast.makeText(context, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                    } else if (pw != pwCheck) {
                        Toast.makeText(context, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                    } else if (nickName.isEmpty()) {
                        Toast.makeText(context, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show()
                    } else {
                        scope.launch {
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



            //Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            //    Row(
            //        modifier = Modifier
            //            .fillMaxWidth()
            //            .padding(start = 50.dp, top = 5.dp, end = 50.dp, bottom = 3.dp),
            //        horizontalArrangement = Arrangement.Start
            //    ) {
            //        Text(text = "생년월일")
            //    }
            //    Text(
            //        text = mDate.value,
            //        modifier = Modifier
            //            .fillMaxWidth()
            //            .padding(top = 3.dp, bottom = 2.dp, start = 50.dp, end = 50.dp)
            //            .border(
            //                width = 1.dp,
            //                color = Color(R.color.hint_text),
            //                shape = RoundedCornerShape(8.dp)
            //            )
            //            .padding(horizontal = 30.dp, vertical = 20.dp)
            //            .clickable { mDatePickerDialog.show() })
            //}
        }
    }
}
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun UserCreatePreview(){
    val navController = rememberNavController()
    val scdLocalData = kr.carepet.data.SCDLocalData()
    val viewModel = UserCreateViewModel(scdLocalData)
    UserCreateScreen(navController = navController, viewModel = viewModel)
}



