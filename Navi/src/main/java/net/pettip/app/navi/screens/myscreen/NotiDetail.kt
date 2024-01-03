package net.pettip.app.navi.screens.myscreen

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import net.pettip.app.navi.R
import net.pettip.app.navi.component.BackTopBar
import net.pettip.app.navi.screens.commuscreen.HtmlText
import net.pettip.app.navi.ui.theme.design_login_text
import net.pettip.app.navi.ui.theme.design_skip
import net.pettip.app.navi.ui.theme.design_textFieldOutLine
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.CommunityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotiDetail(navController: NavHostController, viewModel:CommunityViewModel){

    val notiDetail by viewModel.bbsDetail.collectAsState()

    DisposableEffect(Unit){
        onDispose {
            viewModel.updateBbsDetail(null)
        }
    }

    Scaffold (
        topBar = { BackTopBar(title = "공지사항", navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.primary)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = notiDetail?.data?.pstTtl?:"",
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 24.sp,
                letterSpacing = (-1.2).sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)
            )

            Text(
                text = notiDetail?.data?.pstgBgngDt?:"",
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 14.sp,
                letterSpacing = (-0.7).sp,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(start = 20.dp, top = 8.dp)
            )

            Spacer(modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(color = MaterialTheme.colorScheme.outline)
            )

            HtmlText(htmlString = notiDetail?.data?.pstCn?:"", modifier = Modifier.padding(horizontal = 20.dp))
        }
    }

}