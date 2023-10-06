package kr.carepet.app.navi.screens.myscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kr.carepet.app.navi.R
import kr.carepet.app.navi.component.BackTopBar
import kr.carepet.app.navi.ui.theme.design_login_text
import kr.carepet.app.navi.ui.theme.design_skip
import kr.carepet.app.navi.ui.theme.design_textFieldOutLine
import kr.carepet.app.navi.ui.theme.design_white

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotiDetail(navController: NavHostController){

    Scaffold (
        topBar = { BackTopBar(title = "공지사항", navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = design_white)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "산책기능 이용 가능 안드로이드 버전 안내사항",
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 24.sp,
                letterSpacing = (-1.2).sp,
                color = design_login_text,
                modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)
            )

            Text(
                text = "2023.08.16",
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 14.sp,
                letterSpacing = (-0.7).sp,
                color = design_skip,
                modifier = Modifier.padding(start = 20.dp, top = 8.dp)
            )

            Spacer(modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth().height(1.dp)
                .background(color = design_textFieldOutLine)
            )

            Text(
                text = "안녕하세요 케어펫 입니다. 산책기능을 업데이트 하였습니다. 이작업에 따른 일부 안드로이드 버전에서 산책기능이용에 제한이 있을 수 있음을 안내 드립니다. 정상적인 이용을 위해 회원님의 안드로이드 버전을 확인 부탁드리며 ~~~~안녕하세요 케어펫 입니다. 산책기능을 업데이트 하였습니다 이작업에 따른 일부 안드로이드 버전에서 산책기능이용에 제한이 있을 수 있음을 안내 드립니다.",
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 14.sp, letterSpacing = (-0.7).sp,
                color = design_login_text,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp)
            )

        }
    }

}