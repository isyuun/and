package net.pettip.app.navi.screens.mainscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.material.pullrefresh.PullRefreshIndicator
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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
import net.pettip.app.navi.R
import net.pettip.app.navi.Screen
import net.pettip.app.navi.component.BackTopBar
import net.pettip.app.navi.component.CustomIndicator
import net.pettip.app.navi.component.ErrorScreen
import net.pettip.app.navi.component.LoadingAnimation1
import net.pettip.app.navi.ui.theme.design_button_bg
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_login_bg
import net.pettip.app.navi.ui.theme.design_login_text
import net.pettip.app.navi.ui.theme.design_textFieldOutLine
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.CommunityViewModel
import net.pettip.data.bbs.BbsNtc
import net.pettip.data.bbs.BbsQna
import net.pettip.data.user.BbsFaq
import net.pettip.singleton.G

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(navController: NavHostController, viewModel:CommunityViewModel){

    val pagerState = rememberPagerState(initialPage = 0)
    val coroutineScope = rememberCoroutineScope()
    var tabVisible by remember { mutableFloatStateOf(1f) }
    val preUserId by viewModel.settingPreUserId.collectAsState()

    LaunchedEffect(key1 = pagerState.currentPage){
        when(pagerState.currentPage){
            0 -> viewModel.updateSettingCurrentTab("공지사항")
            1 -> viewModel.updateSettingCurrentTab("FAQ")
            2 -> viewModel.updateSettingCurrentTab("1:1문의")
        }
    }

    LaunchedEffect(Unit){
        if (preUserId != G.userId && preUserId != ""){
            viewModel.updateNtcListInit(true)
            viewModel.updateFaqListInit(true)
            viewModel.updateQnaListInit(true)
            viewModel.updateSettingPreUserId(G.userId)
        }else{
            viewModel.updateSettingPreUserId(G.userId)
        }
    }

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {BackTopBar(title = stringResource(id = R.string.service_center), navController = navController, backVisible = true)}
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
                        text = { Text(text = stringResource(id = item.title), fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimary,
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

    val ntcRes by viewModel.ntcRes.collectAsState()
    val ntcList by viewModel.ntcList.collectAsState()
    val currentTab by viewModel.settingCurrentTab.collectAsState()
    val page by viewModel.ntcListPage.collectAsState()
    val init by viewModel.ntcListInit.collectAsState()

    var isLoading by remember{ mutableStateOf(false) }
    var isError by rememberSaveable{ mutableStateOf(false) }
    var refreshing by remember{ mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = {
            refreshing = true
        })

    val lazyListState = rememberLazyListState()

    LaunchedEffect(key1 = refreshing) {
        if (refreshing) {

            viewModel.updateNtcListClear()
            viewModel.updateNtcListPage(1)
            val result = viewModel.getNtcList(1)
            isLoading = false
            isError = !result
            refreshing = false
        }
    }

    LaunchedEffect(Unit) {
        isLoading = true

        viewModel.updateNtcListClear()
        viewModel.updateNtcListPage(1)
        val result = viewModel.getNtcList(1)
        isLoading = false
        isError = !result
        //viewModel.updateNtcListInit(false)
    }


    LaunchedEffect(key1 = lazyListState.canScrollForward){
        if (!lazyListState.canScrollForward && !refreshing && currentTab=="공지사항"){
            if (ntcRes?.data?.paginate?.existNextPage == true){
                if (!isLoading){
                    isLoading = true
                    val result = viewModel.getNtcList(page + 1)
                    isLoading = if (result){
                        viewModel.updateNtcListPage(page + 1)
                        false
                    }else{
                        viewModel.updateNtcListPage(page - 1)
                        false
                    }
                }
            }
        }
    }

    Box (
        Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
    ){

        if (isLoading && ntcList.isEmpty()){
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                LoadingAnimation1()
            }
        }else if (isError){
            ErrorScreen(onClick = { refreshing = true })
        }else{
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .pullRefresh(pullRefreshState)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                if (!ntcList.isNullOrEmpty()){
                    items(ntcList){ item ->
                        NotiItem(notiItemData = item, navController, viewModel)
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                PullRefreshIndicator(refreshing = refreshing, state = pullRefreshState)
            }
        }
    }
}

@Composable
fun FAQScreen(navController: NavHostController, viewModel: CommunityViewModel){

    val faqRes by viewModel.faqRes.collectAsState()
    val faqList by viewModel.faqList.collectAsState()
    val init by viewModel.faqListInit.collectAsState()
    val currentTab by viewModel.settingCurrentTab.collectAsState()
    val page by viewModel.faqListPage.collectAsState()

    var isLoading by remember{ mutableStateOf(false) }
    var isError by rememberSaveable{ mutableStateOf(false) }
    var refreshing by remember{ mutableStateOf(false) }
    val lazyListState = rememberLazyListState()

    //LaunchedEffect(key1 = init){
    //    if (init){
    //        isLoading = true
    //
    //        viewModel.updateFaqListClear()
    //        val result = viewModel.getFaqList(1)
    //        isLoading = false
    //        isError = !result
    //        viewModel.updateFaqListInit(false)
    //    }
    //}

    LaunchedEffect(Unit){
        isLoading = true

        viewModel.updateFaqListClear()
        val result = viewModel.getFaqList(1)
        isLoading = false
        isError = !result
    }

    LaunchedEffect(key1 = refreshing){
        if (refreshing){
            isLoading = true

            viewModel.updateFaqListClear()
            val result = viewModel.getFaqList(1)
            isLoading = false
            isError = !result
            refreshing = false
        }
    }

    LaunchedEffect(key1 = lazyListState.canScrollForward){
        if (!lazyListState.canScrollForward && !refreshing && currentTab=="FAQ"){
            if (faqRes?.data?.paginate?.existNextPage == true){
                if (!isLoading){
                    isLoading = true
                    val result = viewModel.getFaqList(page + 1)
                    isLoading = if (result){
                        viewModel.updateFaqListPage(page + 1)
                        false
                    }else{
                        viewModel.updateFaqListPage(page - 1)
                        false
                    }
                }
            }
        }
    }

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ){
        if (isLoading && faqList.isEmpty()){
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                LoadingAnimation1()
            }
        } else if (isError){
            ErrorScreen(onClick = { refreshing = true })
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                state = lazyListState
            ){
                if (!faqList.isNullOrEmpty()){
                    items(faqList){ item ->
                        FAQItem(faqItemData = item)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InquiryScreen(navController: NavHostController, viewModel: CommunityViewModel){

    val qnaRes by viewModel.qnaRes.collectAsState()
    val qnaList by viewModel.qnaList.collectAsState()
    val init by viewModel.qnaListInit.collectAsState()
    val currentTab by viewModel.settingCurrentTab.collectAsState()
    val page by viewModel.qnaListPage.collectAsState()

    var isLoading by remember{ mutableStateOf(false) }
    var isError by rememberSaveable{ mutableStateOf(false) }
    var refreshing by remember{ mutableStateOf(false) }
    val lazyListState = rememberLazyListState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = {
            refreshing = true
        })

    //LaunchedEffect(key1 = init){
    //    if (init){
    //        isLoading = true
    //
    //        viewModel.updateQnaListClear()
    //        val result = viewModel.getQnaList(1)
    //        isLoading = false
    //        isError = !result
    //        viewModel.updateQnaListInit(false)
    //    }
    //}

    LaunchedEffect(Unit){
        isLoading = true

        viewModel.updateQnaListClear()
        val result = viewModel.getQnaList(1)
        isLoading = false
        isError = !result
        //viewModel.updateQnaListInit(false)
    }

    LaunchedEffect(key1 = refreshing){
        if (refreshing){
            isLoading = true

            viewModel.updateQnaListClear()
            val result = viewModel.getQnaList(1)
            isLoading = false
            isError = !result
            refreshing = false
        }
    }

    LaunchedEffect(key1 = lazyListState.canScrollForward){
        if (!lazyListState.canScrollForward && !refreshing && currentTab=="1:1문의"){
            if (qnaRes?.data?.paginate?.existNextPage == true){
                if (!isLoading){
                    isLoading = true
                    val result = viewModel.getQnaList(page + 1)
                    isLoading = if (result){
                        viewModel.updateQnaListPage(page + 1)
                        false
                    }else{
                        viewModel.updateQnaListPage(page - 1)
                        false
                    }
                }
            }
        }
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
                text = stringResource(R.string.one_one_inquiry),
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
            text = stringResource(R.string.my_inquiry),
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

        Box(
            modifier = Modifier.fillMaxSize()
        ){
            if (isLoading && qnaList.isEmpty()){
                Box(modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    LoadingAnimation1()
                }
            } else if (isError){
                ErrorScreen(onClick = { refreshing = true })
            } else {
                if (qnaRes?.data?.bbsQnaList?.isEmpty() == true){
                    Box (
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = stringResource(R.string.no_my_inquiry),
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = 16.sp, letterSpacing = (-0.8).sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }else{
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        state = lazyListState,
                        modifier = Modifier
                            .pullRefresh(pullRefreshState)
                        //verticalArrangement = Arrangement.spacedBy(20.dp)
                    ){
                        items(qnaList){ item ->
                            InquiryItem(inquiryItemData = item, navController, viewModel)
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                        PullRefreshIndicator(refreshing = refreshing, state = pullRefreshState)
                    }
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
        Box (
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
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
                modifier = Modifier
                    .padding(start = 20.dp, end = 68.dp)
                    .align(Alignment.CenterStart)
            )

            Box (
                modifier = Modifier
                    .padding(end = 20.dp)
                    .size(28.dp)
                    .align(Alignment.CenterEnd)
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
                    },
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
                        navController.navigate(Screen.InquiryDetail.route)
                        viewModel.getQnaDetail(inquiryItemData.pstSn)
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
                    stringResource(R.string.answer_complete)
                }else{
                    stringResource(R.string.inquiry_received)
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
            modifier = Modifier.padding(top = 8.dp)
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
    val title: Int,
    val screen: @Composable (NavHostController, CommunityViewModel) -> Unit // navController를 매개변수로 추가
)

val MyScreenTabItems = listOf(
    MyScreenTabItem(
        title = R.string.announcement,
        screen = { navController, viewModel ->
            NotiScreen(
                navController = navController,
                viewModel = viewModel

            ) }),
    MyScreenTabItem(
        title = R.string.faq_title,
        screen = { navController, viewModel ->
            FAQScreen(
                navController = navController,
                viewModel = viewModel
            ) }),
    MyScreenTabItem(
        title = R.string.qna_title,
        screen = { navController, viewModel ->
            InquiryScreen(
                navController = navController,
                viewModel = viewModel
            ) })
)

