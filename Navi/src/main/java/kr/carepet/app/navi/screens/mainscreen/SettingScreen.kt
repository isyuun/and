package kr.carepet.app.navi.screens.mainscreen

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import kr.carepet.app.navi.R
import kr.carepet.app.navi.Screen
import kr.carepet.app.navi.component.BackTopBar
import kr.carepet.app.navi.ui.theme.design_btn_border
import kr.carepet.app.navi.ui.theme.design_button_bg
import kr.carepet.app.navi.ui.theme.design_f1f1f1
import kr.carepet.app.navi.ui.theme.design_intro_bg
import kr.carepet.app.navi.ui.theme.design_login_bg
import kr.carepet.app.navi.ui.theme.design_login_text
import kr.carepet.app.navi.ui.theme.design_skip
import kr.carepet.app.navi.ui.theme.design_textFieldOutLine
import kr.carepet.app.navi.ui.theme.design_white
import kr.carepet.app.navi.viewmodel.SettingViewModel
import java.time.DayOfWeek
import java.time.Instant
import java.util.Calendar
import java.util.TimeZone

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(navController: NavHostController, viewModel:SettingViewModel){

    val pagerState = rememberPagerState(initialPage = 0)
    val coroutineScope = rememberCoroutineScope()
    var tabVisible by remember { mutableFloatStateOf(1f) }

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {BackTopBar(title = "고객센터", navController = navController, backVisible = true)}
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
                MyScreenTabItems.forEachIndexed { index, item ->
                    Tab(
                        text = { Text(text = item.title, fontSize = 16.sp,
                            fontFamily =
                            if(index == pagerState.currentPage) FontFamily(Font(R.font.pretendard_bold))
                            else FontFamily(Font(R.font.pretendard_regular))
                        )},
                        selected = index == pagerState.currentPage,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) }}
                    )
                }
            }

            HorizontalPager(
                count = MyScreenTabItems.size,
                state = pagerState
            ) { page ->
                when(page){
                    0 -> NotiScreen(navController = navController, viewModel = viewModel)
                    1 -> FAQScreen(navController = navController, viewModel = viewModel)
                    2 -> InquiryScreen(navController = navController, viewModel = viewModel)
                }
            }
        }

    }
}


@Composable
fun NotiScreen(navController: NavHostController, viewModel: SettingViewModel){

    val dummy = arrayListOf(
        NotiItemData(1),
        NotiItemData(2),
        NotiItemData(3),
        NotiItemData(4),
        NotiItemData(5),
        NotiItemData(6),
        NotiItemData(7),
        NotiItemData(8),
        NotiItemData(9),
    )

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(design_white)
    ){
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            state = rememberLazyListState(),
            contentPadding = PaddingValues(vertical = 20.dp)
        ){
            items(dummy){ item->
                NotiItem(item, navController)
            }
        }
    }

}

@Composable
fun FAQScreen(navController: NavHostController, viewModel: SettingViewModel){

    val dummy = arrayListOf(
        FAQItemData("1","1"),
        FAQItemData("1","1"),
        FAQItemData("1","1"),
        FAQItemData("1","1"),
        FAQItemData("1","1"),
        FAQItemData("1","1"),
        FAQItemData("1","1"),
        FAQItemData("1","1"),
        FAQItemData("1","1"),
        FAQItemData("1","1"),
        FAQItemData("1","1"),
        FAQItemData("1","1"),
    )

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(design_white)
    ){
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            state = rememberLazyListState()
        ){
            items(dummy){ item->
                FAQItem(faqItemData = item)
            }
        }
    }
}

@Composable
fun InquiryScreen(navController: NavHostController, viewModel: SettingViewModel){

    val dummy = arrayListOf(
        InquiryItemData(false),
        InquiryItemData(true),
        InquiryItemData(false),
        InquiryItemData(true),
        InquiryItemData(false),
        InquiryItemData(false),
        InquiryItemData(false),
        InquiryItemData(false),
        InquiryItemData(false),
        InquiryItemData(false),
    )

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(color = design_white)
    ){
        Button(
            onClick = { navController.navigate(Screen.OneNOneScreen.route) },
            modifier = Modifier
                .padding(top = 20.dp, bottom = 20.dp)
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = design_button_bg)
        )
        {
            Text(
                text = "1:1 문의하기",
                color = design_white,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                letterSpacing = (-0.7).sp
            )
        }

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .background(color = design_f1f1f1))

        Text(
            text = "나의 문의 내역",
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            fontSize = 20.sp,
            letterSpacing = (-1.0).sp,
            color = design_login_text,
            modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 16.dp)
        )

        Spacer(modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(1.dp)
            .background(color = design_textFieldOutLine))

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
            state = rememberLazyListState(),
            //verticalArrangement = Arrangement.spacedBy(20.dp)
        ){
            items(dummy){ item ->
                InquiryItem(inquiryItemData = item, navController)
            }
        }
    }// col
}

@Composable
fun NotiItem(notiItemData: NotiItemData, navController: NavHostController){

    Column (
        modifier= Modifier
            .fillMaxWidth()
            .clickable { navController.navigate(Screen.NotiDetail.route) }
    ){
        Text(
            text = "산책기능 이용 가능 안드로이드 버전 안내사항",
            fontFamily = FontFamily(Font(R.font.pretendard_medium)),
            fontSize = 16.sp,
            letterSpacing = (-0.8).sp,
            color = design_login_text,
            modifier = Modifier.padding(top = 20.dp)
        )

        Text(
            text = "2023.08.16",
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = 14.sp,
            letterSpacing = (-0.7).sp,
            color = design_skip,
            modifier = Modifier.padding(top = 8.dp, bottom = 20.dp)
        )

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(color = design_textFieldOutLine))
    }
}

@Composable
fun FAQItem(faqItemData: FAQItemData){

    var expanded by remember {
        mutableStateOf(false)
    }

    Column (
        modifier= Modifier
            .fillMaxWidth()
            .background(
                color =
                if (expanded) {
                    design_login_bg
                } else {
                    design_white
                }
            )
    ){

        Row (
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "산책기능 이용 가능 안드로이드 버전 안내사항",
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                fontSize = 16.sp,
                letterSpacing = (-0.8).sp,
                color = if (expanded){
                    design_intro_bg
                }else{
                    design_login_text
                },
                modifier = Modifier.padding(start = 20.dp)
            )

            Box (
                modifier = Modifier
                    .padding(end = 20.dp)
                    .clip(CircleShape)
                    .border(width = 1.dp, color = design_skip, shape = CircleShape)
                    .clickable { expanded = !expanded },
                contentAlignment = Alignment.Center
            ){
                Icon(
                    imageVector =
                    if (expanded){
                        Icons.Default.KeyboardArrowUp
                    }else{
                        Icons.Default.KeyboardArrowDown
                    }
                    ,
                    contentDescription = "",
                    tint = design_login_text)
            }

        }

        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column {
                Spacer(modifier = Modifier.padding(top = 16.dp))

                Text(
                    text = "현재 케어펫에서 제공되는 품종의 경우 표준품종데이터를 기준으로 제공해 드리고 있으며 당 품종의 정확은 품종명을 검색 부탁드립니다. 믹스견일 경우 품종명 (mixed)로 등록 부탁드립니다.",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp,
                    letterSpacing = (-0.7).sp,
                    color = design_login_text,
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp)
                )
            }
        }

        Spacer(modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth()
            .height(1.dp)
            .background(color = design_textFieldOutLine))
    }
}

@Composable
fun InquiryItem(inquiryItemData: InquiryItemData, navController: NavHostController){
    Column (
        modifier = Modifier.clickable { navController.navigate(Screen.InquiryDetail.route) }
    ){
        Box (
            modifier= Modifier
                .padding(top = 20.dp)
                .border(
                    width = if (inquiryItemData.complete) {
                        0.dp
                    } else {
                        1.dp
                    },
                    color = design_btn_border,
                    shape = RoundedCornerShape(10.dp)
                )
                .background(
                    color = if (inquiryItemData.complete) {
                        design_button_bg
                    } else {
                        design_white
                    }, shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = if (inquiryItemData.complete){
                    "답변 완료"
                }else{
                    "문의 접수"
                },
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 12.sp,
                letterSpacing = (-0.6).sp,
                color = if(inquiryItemData.complete){
                    design_white
                }else{
                    design_login_text
                },
                modifier = Modifier.padding(horizontal = 9.dp, vertical = 2.dp)
            )
        }

        Text(
            text = "휴대폰 번호 수정하는 방법이 있나요?",
            fontFamily = FontFamily(Font(R.font.pretendard_medium)),
            fontSize = 16.sp,
            letterSpacing = (-0.8).sp,
            color = design_login_text,
        )

        Spacer(modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth()
            .height(1.dp)
            .background(color = design_textFieldOutLine)
        )
    }
}

data class NotiItemData(val num:Int)
data class FAQItemData(val title:String, val sub:String)
data class InquiryItemData(val complete:Boolean)

data class MyScreenTabItem(
    val title: String,
    val screen: @Composable (NavHostController, SettingViewModel) -> Unit // navController를 매개변수로 추가
)

val MyScreenTabItems = listOf(
    MyScreenTabItem(
        title = "공지사항",
        screen = { navController, viewModel ->
            NotiScreen(
                navController = navController,
                viewModel = viewModel
            ) }),
    MyScreenTabItem(
        title = "FAQ",
        screen = { navController, viewModel ->
            FAQScreen(
                navController = navController,
                viewModel = viewModel
            ) }),
    MyScreenTabItem(
        title = "1:1문의",
        screen = { navController, viewModel ->
            InquiryScreen(
                navController = navController,
                viewModel = viewModel
            ) })
)

