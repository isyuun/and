package net.pettip.app.navi.screens.mainscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.Screen
import net.pettip.app.navi.component.BackTopBar
import net.pettip.app.navi.component.CustomIndicator
import net.pettip.app.navi.component.LoadingAnimation1
import net.pettip.app.navi.ui.theme.design_btn_border
import net.pettip.app.navi.ui.theme.design_button_bg
import net.pettip.app.navi.ui.theme.design_f1f1f1
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_login_bg
import net.pettip.app.navi.ui.theme.design_login_text
import net.pettip.app.navi.ui.theme.design_skip
import net.pettip.app.navi.ui.theme.design_textFieldOutLine
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.CommunityViewModel
import net.pettip.data.bbs.BbsNtc
import net.pettip.data.bbs.BbsQna
import net.pettip.data.user.BbsFaq

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(navController: NavHostController, viewModel:CommunityViewModel){

    val pagerState = rememberPagerState(initialPage = 0)
    val coroutineScope = rememberCoroutineScope()
    var tabVisible by remember { mutableFloatStateOf(1f) }

    LaunchedEffect(Unit){

        if (viewModel.ntcList.value == null){
            viewModel.updateNtcListClear()
            viewModel.getNtcList(1)
        }
        if (viewModel.faqList.value ==null){
            viewModel.updateFaqListClear()
            viewModel.getFaqList(1)
        }
    }

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {BackTopBar(title = "고객센터", navController = navController, backVisible = true)}
    ) { paddingValues ->
        Column(modifier= Modifier
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            TabRow(
                modifier = Modifier.alpha(tabVisible),
                selectedTabIndex = pagerState.currentPage,
                indicator = { tabPositions -> TabRowDefaults.Indicator(Modifier.pagerTabIndicatorOffset(pagerState, tabPositions), color = MaterialTheme.colorScheme.onPrimary, height = 2.dp) },
                backgroundColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                MyScreenTabItems.forEachIndexed { index, item ->
                    Tab(
                        text = { Text(text = item.title, fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimary,
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


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NotiScreen(navController: NavHostController, viewModel: CommunityViewModel){

    val ntcList by viewModel.ntcList.collectAsState()

    var refreshing by remember{ mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = {
            refreshing = true
        })

    LaunchedEffect(key1 = refreshing) {
        if (refreshing) {

            viewModel.updateNtcListClear()
            //viewModel.updateNtcyPage(1)
            viewModel.getNtcList(1)

            delay(300)
            refreshing = false
        }
    }


    Box (
        Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
    ){

        Crossfade(
            targetState = ntcList?.data?.bbsNtcList?.isEmpty(),
            label = "",
        ) { ntcList?.data?.bbsNtcList?.isEmpty()
            when(it){
                true ->
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        LoadingAnimation1()
                    }
                false ->
                    LazyColumn(
                        state = rememberLazyListState(),
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .pullRefresh(pullRefreshState)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ){
                        if (ntcList?.data?.bbsNtcList != null){
                            items(ntcList?.data?.bbsNtcList ?: emptyList()){ item ->
                                NotiItem(notiItemData = item, navController, viewModel)
                            }
                        }
                    }

                else ->
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        LoadingAnimation1()
                    }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                CustomIndicator(state = pullRefreshState, refreshing = refreshing)
            }
        }

    }

}

@Composable
fun FAQScreen(navController: NavHostController, viewModel: CommunityViewModel){

    val faqData by viewModel.faqList.collectAsState()

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ){
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            state = rememberLazyListState()
        ){
            items(faqData?.bbsFaqList?: emptyList()){ item ->
                FAQItem(faqItemData = item)
            }
        }
    }
}

@Composable
fun InquiryScreen(navController: NavHostController, viewModel: CommunityViewModel){

    val qnaRes by viewModel.qnaList.collectAsState()

    LaunchedEffect(Unit){
        viewModel.updateQnaListClear()
        viewModel.getQnaList(1)
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
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
            .background(color = MaterialTheme.colorScheme.onSurfaceVariant))

        Text(
            text = "나의 문의 내역",
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            fontSize = 20.sp,
            letterSpacing = (-1.0).sp,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 16.dp)
        )

        Spacer(modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(1.dp)
            .background(color = design_textFieldOutLine))

        if (qnaRes?.data?.bbsQnaList?.isEmpty() == true){
            Box (
                modifier = Modifier.padding(top = 20.dp).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "나의 문의 내역이 없습니다",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 16.sp, letterSpacing = (-0.8).sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }else{
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp),
                state = rememberLazyListState(),
                //verticalArrangement = Arrangement.spacedBy(20.dp)
            ){
                items(qnaRes?.data?.bbsQnaList ?: emptyList()){ item ->
                    InquiryItem(inquiryItemData = item, navController, viewModel)
                }
            }
        }
    }// col
}

@Composable
fun NotiItem(notiItemData: BbsNtc, navController: NavHostController, viewModel: CommunityViewModel){

    val scope = rememberCoroutineScope()
    var lastClickTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    Column (
        modifier= Modifier
            .fillMaxWidth()
            .clickable {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastClickTime >= 500) {
                    lastClickTime = currentTime
                    scope.launch {
                        navController.navigate(Screen.NotiDetail.route)
                        viewModel.getNctDetail(notiItemData.pstSn)
                    }
                }
            }
    ){
        Text(
            text = notiItemData.pstTtl,
            fontFamily = FontFamily(Font(R.font.pretendard_medium)),
            fontSize = 16.sp,
            letterSpacing = (-0.8).sp,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(top = 20.dp)
        )

        Text(
            text = notiItemData.pstgBgngDt?:"",
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = 14.sp,
            letterSpacing = (-0.7).sp,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(top = 8.dp, bottom = 20.dp)
        )

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(color = design_textFieldOutLine))
    }
}

@Composable
fun FAQItem(faqItemData: BbsFaq){

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
                    MaterialTheme.colorScheme.primary
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
                text = faqItemData.pstTtl,
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                fontSize = 16.sp,
                letterSpacing = (-0.8).sp,
                color = if (expanded){
                    design_intro_bg
                }else{
                    MaterialTheme.colorScheme.onPrimary
                },
                modifier = Modifier.padding(start = 20.dp)
            )

            Box (
                modifier = Modifier
                    .padding(end = 20.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .border(width = 1.dp, color = MaterialTheme.colorScheme.outline, shape = CircleShape)
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
                    tint = MaterialTheme.colorScheme.onPrimary)
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
                    text = faqItemData.pstCn,
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
fun InquiryItem(inquiryItemData: BbsQna, navController: NavHostController, viewModel: CommunityViewModel){

    val scope = rememberCoroutineScope()
    var lastClickTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    Column (
        modifier = Modifier
            .clickable {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastClickTime >= 500) {
                    lastClickTime = currentTime
                    scope.launch {
                        viewModel.getQnaDetail(inquiryItemData.pstSn)
                        navController.navigate(Screen.InquiryDetail.route)
                    }
                }
            }
    ){
        Box (
            modifier= Modifier
                .padding(top = 20.dp)
                .border(
                    width = if (inquiryItemData.pstAnw != 0) {
                        0.dp
                    } else {
                        1.dp
                    },
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(10.dp)
                )
                .background(
                    color = if (inquiryItemData.pstAnw != 0) {
                        design_button_bg
                    } else {
                        Color.Transparent
                    }, shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = if (inquiryItemData.pstAnw!=0){
                    "답변 완료"
                }else{
                    "문의 접수"
                },
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 12.sp,
                letterSpacing = (-0.6).sp,
                color = if(inquiryItemData.pstAnw!=0){
                    design_white
                }else{
                    MaterialTheme.colorScheme.onPrimary
                },
                modifier = Modifier.padding(horizontal = 9.dp, vertical = 2.dp)
            )
        }

        Text(
            text = inquiryItemData.pstTtl?:"",
            fontFamily = FontFamily(Font(R.font.pretendard_medium)),
            fontSize = 16.sp,
            letterSpacing = (-0.8).sp,
            color = MaterialTheme.colorScheme.onPrimary,
        )

        Spacer(modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth()
            .height(1.dp)
            .background(color = MaterialTheme.colorScheme.outline)
        )
    }
}

data class NotiItemData(val num:Int)
data class FAQItemData(val title:String, val sub:String)
data class InquiryItemData(val complete:Boolean)

data class MyScreenTabItem(
    val title: String,
    val screen: @Composable (NavHostController, CommunityViewModel) -> Unit // navController를 매개변수로 추가
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

