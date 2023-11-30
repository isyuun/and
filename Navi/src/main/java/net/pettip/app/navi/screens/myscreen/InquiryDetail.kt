package net.pettip.app.navi.screens.myscreen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import net.pettip.app.navi.R
import net.pettip.app.navi.component.BackTopBar
import net.pettip.app.navi.ui.theme.design_button_bg
import net.pettip.app.navi.ui.theme.design_f1f1f1
import net.pettip.app.navi.ui.theme.design_login_text
import net.pettip.app.navi.ui.theme.design_skip
import net.pettip.app.navi.ui.theme.design_textFieldOutLine
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.CommunityViewModel
import net.pettip.app.navi.viewmodel.SettingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InquiryDetail(navController: NavHostController, viewModel: CommunityViewModel, settingViewModel: SettingViewModel){

    val qnaDetail by viewModel.qnaDetail.collectAsState()
    var isModify by remember{ mutableStateOf(false) }
    var deleteDialog by remember{ mutableStateOf(false) }
    var qnaDelete by remember{ mutableStateOf(false) }
    val context = LocalContext.current

    BackHandler {
        if (isModify){
            isModify = false
        }else{
            navController.popBackStack()
        }
    }

    LaunchedEffect(key1 = qnaDelete){
        if (qnaDelete){
            val result = viewModel.deleteQna()
            if (result){
                navController.popBackStack()
            }else{
                Toast.makeText(context, "다시 시도해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold (
        topBar = { BackTopBar(title = "1:1 문의", navController = navController) }
    ) { paddingValues ->

        if (deleteDialog){
            CustomDialogDelete(
                onDismiss = { newValue -> deleteDialog = newValue },
                confirm = "삭제하기",
                dismiss = "취소",
                title = "문의 삭제하기",
                text = "정말 삭제하시겠어요?",
                valueChange = { newValue -> qnaDelete = newValue}
            )
        }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = design_white)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = qnaDetail?.data?.get(0)?.pstTtl ?: "",
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 24.sp,
                letterSpacing = (-1.2).sp,
                color = design_login_text,
                modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)
            )

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = qnaDetail?.data?.get(0)?.frstInptDt?: "",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp,
                    letterSpacing = (-0.7).sp,
                    color = design_skip,
                    modifier = Modifier.padding(start = 20.dp, top = 8.dp)
                )

                if (qnaDetail?.data?.size !=2){
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 20.dp)
                    ){
                        Row (
                            modifier = Modifier.clickable {
                                isModify = true
                            },
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(
                                painter = painterResource(id = R.drawable.icon_daily),
                                contentDescription = "", tint = design_skip,
                                modifier = Modifier
                                    .padding(start = 12.dp)
                                    .size(14.dp))

                            Text(
                                text = "수정하기",
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                fontSize = 12.sp,
                                letterSpacing = (-0.6).sp,
                                color = design_skip,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                        Row (
                            modifier = Modifier.clickable {
                                deleteDialog = true
                            },
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "", tint = design_skip,
                                modifier = Modifier
                                    .padding(start = 12.dp)
                                    .size(14.dp))

                            Text(
                                text = "삭제하기",
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                fontSize = 12.sp,
                                letterSpacing = (-0.6).sp,
                                color = design_skip,
                                modifier = Modifier
                                    .padding(start = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(color = design_textFieldOutLine)
            )

            Text(
                text = qnaDetail?.data?.get(0)?.pstCn?: "",
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 14.sp, letterSpacing = (-0.7).sp,
                color = design_login_text,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp)
            )

            Spacer(modifier = Modifier
                .padding(top = 40.dp)
                .fillMaxWidth()
                .height(8.dp)
                .background(color = design_f1f1f1)
            )

            if (qnaDetail?.data?.size == 2){
                InquiryDetailAnswer()
            }else{
                InquiryDetailNoAnswer()
            }
        }
    }

    AnimatedVisibility(
        visible = isModify,
        enter = slideInHorizontally(initialOffsetX = {it/2}) + fadeIn(),
        exit = slideOutHorizontally(targetOffsetX = {it/2}) + fadeOut()
    ) {
        ModifyInquiryScreen(
            navController = navController,
            viewModel = viewModel,
            settingViewModel = settingViewModel,
            onDismiss = {newValue -> isModify = newValue}
        )
    }
}

@Composable
fun InquiryDetailNoAnswer(){
    Column(
        modifier = Modifier
            .padding(top = 40.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(painter = painterResource(id = R.drawable.icon_bubble),
            contentDescription = "", tint = Color.Unspecified)

        Text(
            text = "조금만 기다려 주세요.\n답변 준비 중입니다.",
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = 14.sp, letterSpacing = (-0.7).sp,
            color = design_skip,
            modifier = Modifier.padding(top = 12.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun InquiryDetailAnswer(){

    Column (
        modifier = Modifier.fillMaxWidth()
    ){
        Row (
            modifier = Modifier
                .padding(start = 20.dp, top = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(painter = painterResource(id = R.drawable.line_answer),
                contentDescription = "", tint = Color.Unspecified)

            Box (
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(36.dp)
                    .background(color = design_button_bg, shape = CircleShape)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "A",
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    fontSize = 20.sp,
                    color = design_white,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.padding(top = 16.dp))

        Text(
            text = "[답변] 휴대폰 번호 수정하는 방법이 있나요?",
            fontFamily = FontFamily(Font(R.font.pretendard_medium)),
            fontSize = 16.sp, letterSpacing = (-0.8).sp,
            color = design_login_text,
            modifier = Modifier.padding(start = 40.dp, end = 20.dp)
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        Text(
            text = "안녕하세요. 케어펫입니다.\n\n문의주신 휴대폰 번호는 홈 > 마이페이지 > 설정 > 개인정보수정에서 변경 가능합니다.\n\n감사합니다!",
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = 14.sp, letterSpacing = (-0.7).sp,
            color = design_login_text,
            modifier = Modifier.padding(start = 40.dp, end = 20.dp)
        )
    }// col
}