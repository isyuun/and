package kr.carepet.app.navi.screens.commuscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kr.carepet.app.navi.R
import kr.carepet.app.navi.component.BackTopBar
import kr.carepet.app.navi.ui.theme.design_button_bg
import kr.carepet.app.navi.ui.theme.design_login_text
import kr.carepet.app.navi.ui.theme.design_skip
import kr.carepet.app.navi.ui.theme.design_textFieldOutLine
import kr.carepet.app.navi.ui.theme.design_white

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetail(navController: NavHostController){

    Scaffold (
        topBar = { BackTopBar(title = stringResource(R.string.title_event), navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = design_white)
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(id = R.drawable.event_thumb1),
                contentDescription = "",
                modifier= Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            
            Text(
                text = "본아페티 관절 영양제 무료 체험단",
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 24.sp,
                letterSpacing = (-1.2).sp,
                color = design_login_text,
                modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)
            )
            
            Spacer(modifier = Modifier
                .padding(top = 20.dp, bottom = 20.dp, start = 20.dp, end = 20.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(design_textFieldOutLine))

            Row {
                Column (
                    modifier= Modifier
                        .padding(start = 20.dp)
                        .width(60.dp)
                ){
                    EventDetailMainText(text = stringResource(R.string.event_period), bottomPadding = 12)
                    EventDetailMainText(text = stringResource(R.string.event_people), bottomPadding = 12)
                    EventDetailMainText(text = stringResource(R.string.event_condition), bottomPadding = 12)
                    EventDetailMainText(text = stringResource(R.string.event_benefit), bottomPadding = 12)
                    EventDetailMainText(text = stringResource(R.string.event_announce), bottomPadding = 12)
                    EventDetailMainText(text = stringResource(R.string.event_method), bottomPadding = 12)
                    EventDetailMainText(text = stringResource(R.string.event_deadline), bottomPadding = 0)
                }

                Column (
                    modifier= Modifier
                        .padding(start = 20.dp)
                ){
                    EventDetailSubText(text = "2023.07.31(월) ~ 2023.08.11(금)", bottomPadding = 12)
                    EventDetailSubText(text = "10명", bottomPadding = 12)
                    EventDetailSubText(text = "3시간 산책시 응모가능", bottomPadding = 12)
                    EventDetailSubText(text = "강아지 아이스크림", bottomPadding = 12)
                    EventDetailSubText(text = "2023.08.18(금)", bottomPadding = 12)
                    EventDetailSubText(text = "무작위 추첨", bottomPadding = 12)
                    EventDetailSubText(text = "2023.07.31(월) ~ 2023.08.11(금)", bottomPadding = 0)
                }
            }

            Spacer(modifier = Modifier.padding(top = 40.dp))

            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = design_button_bg)
            )
            {
                Text(text = stringResource(R.string.event_apply), color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
            }
        }//col

    }

}

@Composable
fun EventDetailMainText(text:String, bottomPadding:Int){
    Text(
        text = text,
        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
        fontSize = 14.sp, letterSpacing = (-0.7).sp,
        color = design_skip,
        modifier = Modifier.padding(bottom = bottomPadding.dp),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun EventDetailSubText(text:String, bottomPadding:Int){
    Text(
        text = text,
        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
        fontSize = 14.sp, letterSpacing = (-0.7).sp,
        color = design_login_text,
        modifier = Modifier.padding(bottom = bottomPadding.dp),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}