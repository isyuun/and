@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package net.pettip.app.navi.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.component.BackTopBar
import net.pettip.app.navi.component.CustomTextField
import net.pettip.app.navi.ui.theme.design_btn_border
import net.pettip.app.navi.ui.theme.design_button_bg
import net.pettip.app.navi.ui.theme.design_login_text
import net.pettip.app.navi.ui.theme.design_placeHolder
import net.pettip.app.navi.ui.theme.design_textFieldOutLine
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun IdPwSearchScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    initialTab: Int = 0,
    viewModel: LoginViewModel
){


    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color = design_white)
    systemUiController.setNavigationBarColor(color= design_white)

    val pagerState = rememberPagerState(initialPage = initialTab)
    val coroutineScope = rememberCoroutineScope()
    var tabVisible by remember { mutableFloatStateOf(1f) }

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
                BackTopBar(title = "아이디/비밀번호 찾기", navController = navController)
            }
    ) { paddingValues ->
        Column(modifier= Modifier
            .padding(paddingValues)
            .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            TabRow(
                modifier = Modifier.alpha(tabVisible),
                selectedTabIndex = pagerState.currentPage,
                indicator = { tabPositions -> TabRowDefaults.Indicator(Modifier.pagerTabIndicatorOffset(pagerState, tabPositions), color = design_login_text, height = 2.dp) },
                backgroundColor = design_white
            ) {
                IDPWTabItems.forEachIndexed { index, idpwTabItem ->
                    Tab(
                        text = { Text(text = idpwTabItem.title, fontSize = 16.sp,
                            fontFamily = if(index == pagerState.currentPage) FontFamily(Font(R.font.pretendard_bold)) else FontFamily(Font(R.font.pretendard_regular))
                        )},
                        selected = index == pagerState.currentPage,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) }}
                        )
                }
            }

            HorizontalPager(
                count = IDPWTabItems.size,
                state = pagerState
            ) { page ->
                when(page){
                    0 -> IdFindScreen(navController, viewModel)
                    1 -> PwFindScreen(navController, viewModel)
                }
            }
        }

    }

}



@Composable
fun IdFindScreen(navController: NavHostController, viewModel: LoginViewModel) {

    val cpChange by viewModel.cpChange.collectAsState()
    val scope = rememberCoroutineScope()

    Log.d("LOG", cpChange)

    when(cpChange){

        "main" -> IFMain(navController = navController, viewModel = viewModel)
        "phone" -> IFbyPhone(navController = navController, viewModel = viewModel)
    }


}

@Composable
fun IFMain(navController: NavHostController, viewModel: LoginViewModel) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(design_white), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { viewModel.updateCpChange("phone") },
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 20.dp)
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(design_white),
            border = BorderStroke(1.dp, color = design_btn_border)
        ) {
            Text(text = "본인명의 휴대폰 번호로 찾기", color = design_login_text,
                fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
        }

        Button(
            onClick = {  },
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 8.dp)
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(design_white),
            border = BorderStroke(1.dp, color = design_btn_border)
        ) {
            Text(text = "이메일로 찾기", color = design_login_text,
                fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
        }
    }

}

@Composable
fun IFbyPhone(navController: NavHostController, viewModel: LoginViewModel){

    val phoneNum by viewModel.phoneNum.collectAsState()
    val certiNum by viewModel.certiNum.collectAsState()

    Column (modifier = Modifier
        .fillMaxSize()
        .background(design_white)){
        
        Text(
            text = "휴대폰 번호",
            fontSize = 16.sp,
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            modifier = Modifier.padding(start = 20.dp,top=20.dp),
            color = design_login_text
        )

        Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
            CustomTextField(
                value = phoneNum,
                onValueChange = {viewModel.updatePhoneNum(it)},
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
                onClick = { },
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

        CustomTextField(
            value = certiNum,
            onValueChange = {viewModel.updateCertiNum(it)},
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next),
            modifier = Modifier
                .padding(start = 20.dp, top = 8.dp, end = 20.dp)
                .fillMaxWidth()
                .height(48.dp),
            placeholder = { Text(text = "인증번호 입력", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
            colors = TextFieldDefaults.outlinedTextFieldColors(
                //placeholderColor = design_placeHolder,
                containerColor = design_white,
                unfocusedBorderColor = design_textFieldOutLine,
                focusedBorderColor = design_login_text,
                unfocusedLeadingIconColor = design_placeHolder,
                focusedLeadingIconColor = design_login_text),
            shape = RoundedCornerShape(4.dp),
            innerPadding = PaddingValues(start=16.dp)
        )

        Button(
            onClick = { },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = design_button_bg)
        )
        {
            Text(text = "아이디 찾기", color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
        }
    }

}


@Composable
fun PwFindScreen(navController: NavHostController, viewModel: LoginViewModel) {

    Box (modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Text(text = "비밀번호")
    }

}



