package kr.carepet.app.navi.screens.myscreen

import android.content.ClipData
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kr.carepet.app.navi.R
import kr.carepet.app.navi.component.BackTopBar
import kr.carepet.app.navi.ui.theme.design_CBE8F3
import kr.carepet.app.navi.ui.theme.design_FCE9B3
import kr.carepet.app.navi.ui.theme.design_button_bg
import kr.carepet.app.navi.ui.theme.design_login_bg
import kr.carepet.app.navi.ui.theme.design_login_text
import kr.carepet.app.navi.ui.theme.design_select_btn_text
import kr.carepet.app.navi.ui.theme.design_sharp
import kr.carepet.app.navi.ui.theme.design_skip
import kr.carepet.app.navi.ui.theme.design_textFieldOutLine
import kr.carepet.app.navi.ui.theme.design_white

@Composable
fun SetKeyScreen(navController:NavHostController){

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

                SetKey()

                Spacer(modifier = Modifier.padding(top = 40.dp))

                Button(
                    onClick = {  },
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

@Composable
fun SetKey(){

    var combinedText by remember { mutableStateOf("") }

    var text1 by remember { mutableStateOf("") }
    var text2 by remember { mutableStateOf("") }
    var text3 by remember { mutableStateOf("") }
    var text4 by remember { mutableStateOf("") }
    var text5 by remember { mutableStateOf("") }
    var text6 by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(60.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){

        Box(
            modifier = Modifier
                .padding(horizontal = 3.dp)
                .fillMaxHeight()
                .weight(1f)
                .background(color = design_white, shape = RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, design_textFieldOutLine, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ){
            BasicTextField(
                value = text1,
                onValueChange = {
                    if (it.length <= 1){
                        text1 = it
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                    .plus(TextStyle(color = design_login_text, fontSize = 24.sp, lineHeight = 24.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold))))
            )
        }

        Box(
            modifier = Modifier
                .padding(horizontal = 3.dp)
                .fillMaxHeight()
                .weight(1f)
                .background(color = design_white, shape = RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, design_textFieldOutLine, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ){
            BasicTextField(
                value = text2,
                onValueChange = {
                    if (it.length <= 1){
                        text2 = it
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                    .plus(TextStyle(color = design_login_text, fontSize = 24.sp, lineHeight = 24.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold))))
            )
        }

        Box(
            modifier = Modifier
                .padding(horizontal = 3.dp)
                .fillMaxHeight()
                .weight(1f)
                .background(color = design_white, shape = RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, design_textFieldOutLine, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ){
            BasicTextField(
                value = text3,
                onValueChange = {
                    if (it.length <= 1){
                        text3 = it
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                    .plus(TextStyle(color = design_login_text, fontSize = 24.sp, lineHeight = 24.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold))))
            )
        }

        Spacer(modifier = Modifier
            .padding(horizontal = 2.dp)
            .size(10.dp, 2.dp)
            .background(design_login_text))

        Box(
            modifier = Modifier
                .padding(horizontal = 3.dp)
                .fillMaxHeight()
                .weight(1f)
                .background(color = design_white, shape = RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, design_textFieldOutLine, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ){
            BasicTextField(
                value = text4,
                onValueChange = {
                    if (it.length <= 1){
                        text4 = it
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                    .plus(TextStyle(color = design_login_text, fontSize = 24.sp, lineHeight = 24.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold))))
            )
        }

        Box(
            modifier = Modifier
                .padding(horizontal = 3.dp)
                .fillMaxHeight()
                .weight(1f)
                .background(color = design_white, shape = RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, design_textFieldOutLine, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ){
            BasicTextField(
                value = text5,
                onValueChange = {
                    if (it.length <= 1){
                        text5 = it
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                    .plus(TextStyle(color = design_login_text, fontSize = 24.sp, lineHeight = 24.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold))))
            )
        }

        Box(
            modifier = Modifier
                .padding(horizontal = 3.dp)
                .fillMaxHeight()
                .weight(1f)
                .background(color = design_white, shape = RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, design_textFieldOutLine, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ){
            BasicTextField(
                value = text6,
                onValueChange = {
                    if (it.length <= 1){
                        text6 = it
                    }
                },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                    .plus(TextStyle(color = design_login_text, fontSize = 24.sp, lineHeight = 24.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)))),
            )
        }


    }
}