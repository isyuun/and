package kr.carepet.app.navi.screens.myscreen

import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.InternalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import kr.carepet.app.navi.R
import kr.carepet.app.navi.component.BackTopBar
import kr.carepet.app.navi.ui.theme.design_CBE8F3
import kr.carepet.app.navi.ui.theme.design_button_bg
import kr.carepet.app.navi.ui.theme.design_login_bg
import kr.carepet.app.navi.ui.theme.design_login_text
import kr.carepet.app.navi.ui.theme.design_textFieldOutLine
import kr.carepet.app.navi.ui.theme.design_white
import kr.carepet.app.navi.viewmodel.SettingViewModel

@Composable
fun SetKeyScreen(navController:NavHostController, settingViewModel: SettingViewModel){

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val dm by settingViewModel.detailMessage.collectAsState()

    Scaffold (
        topBar = { BackTopBar(title = "초대코드 등록하기", navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(design_white)
        ) {
            Text(
                text = "초대 코드 입력",
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 20.sp, letterSpacing = (-1.0).sp,
                lineHeight = 20.sp, color = design_login_text,
                modifier = Modifier.padding(start = 20.dp, top = 20.dp)
            )

            Text(
                text = "전달받은 코드를 입력후 [등록하기]버튼을 클릭해 주세요.",
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 14.sp, letterSpacing = (-0.7).sp,
                lineHeight = 14.sp, color = design_login_text,
                modifier = Modifier.padding(start = 20.dp, top = 16.dp)
            )

            Spacer(modifier = Modifier.padding(top = 40.dp))

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = design_login_bg),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Spacer(modifier = Modifier.padding(top = 20.dp))

                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(color = design_CBE8F3, shape = CircleShape)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ){
                    Icon(painter = painterResource(id = R.drawable.icon_registration),
                        contentDescription = "", tint = Color.Unspecified)
                }

                Spacer(modifier = Modifier.padding(top = 20.dp))

                SetKeyTemp(settingViewModel = settingViewModel)

                Spacer(modifier = Modifier.padding(top = 40.dp))

                Button(
                    onClick = {
                        settingViewModel.viewModelScope.launch {
                            val result = settingViewModel.setInviteCode()
                            if (result){
                                scope.launch { Toast.makeText(context, dm , Toast.LENGTH_SHORT).show() }
                                settingViewModel.updateCurrentPetInfo()
                                settingViewModel.updatePetInfo()
                                navController.popBackStack()
                            }else{
                                scope.launch { Toast.makeText(context, dm , Toast.LENGTH_SHORT).show() }
                            }
                        }

                    },
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = design_button_bg)
                )
                {
                    Text(text = "코드 전송",
                        color = design_white, fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp
                    )
                }

                Spacer(modifier = Modifier.padding(top = 40.dp))

            }// 내부 col

        }
    }
}

@OptIn(ExperimentalFoundationApi::class, InternalTextApi::class)
@Composable
fun SetKeyTemp(settingViewModel: SettingViewModel){

    val context = LocalContext.current
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    val otpValue by settingViewModel.otpValue.collectAsState()
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    val inputService = LocalTextInputService.current

    LaunchedEffect(Unit){
        focusRequester.requestFocus()
        settingViewModel.updateOtpValue("")
    }

    BasicTextField(
        value = otpValue,
        onValueChange = {
            if (it.length <= 6) {
                settingViewModel.updateOtpValue(it)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ),
        decorationBox = {
            Row (
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                repeat(6){ index ->
                    val char = when {
                        index >= otpValue.length -> ""
                        else -> otpValue[index].toString()
                    }
                    var isFocused = otpValue.length == index

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp)
                            .border(
                                if (isFocused) 2.dp else 1.dp,
                                if (isFocused) design_login_text else design_textFieldOutLine,
                                RoundedCornerShape(8.dp)
                            )
                            .background(color = design_white, shape = RoundedCornerShape(8.dp))
                            .combinedClickable(
                                onClick = {
                                    focusRequester.requestFocus()
                                    inputService?.startInput()
                                },
                                onLongClick = {
                                    val clipData = clipboardManager.primaryClip

                                    if (clipData != null && clipData.itemCount > 0) {
                                        val clipboardText = clipData.getItemAt(0).text.toString()

                                        if (clipboardText.length == 6) {
                                            settingViewModel.updateOtpValue(clipboardText)
                                        } else {
                                            Toast.makeText(context,"복사된 초대코드가 없습니다",Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                },
                                onLongClickLabel = ""
                            ),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = char.uppercase(),
                            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                            fontSize = 24.sp, color = design_login_text,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                }
            }
        },
        modifier = Modifier
            .padding(start = 40.dp, end = 40.dp)
            .focusRequester(focusRequester)
    )

}