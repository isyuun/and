@file:OptIn(ExperimentalMaterial3Api::class)

package kr.carepet.app.navi.screens.mainscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.DropdownMenu
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.carepet.app.navi.R
import kr.carepet.app.navi.Screen
import kr.carepet.app.navi.component.CustomIndicator
import kr.carepet.app.navi.component.LoadingAnimation1
import kr.carepet.app.navi.component.StoryListItem
import kr.carepet.app.navi.ui.theme.design_B5B9BE
import kr.carepet.app.navi.ui.theme.design_alpha60_black
import kr.carepet.app.navi.ui.theme.design_login_text
import kr.carepet.app.navi.ui.theme.design_skip
import kr.carepet.app.navi.ui.theme.design_white
import kr.carepet.app.navi.viewmodel.CommunityViewModel
import kr.carepet.app.navi.viewmodel.SharedViewModel
import kr.carepet.data.bbs.BbsEvnt
import kr.carepet.data.daily.Story
import kr.carepet.util.Log
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CommuScreen(navController: NavHostController, communityViewModel: CommunityViewModel, sharedViewModel: SharedViewModel){

    val pagerState = rememberPagerState(initialPage = 0)
    val coroutineScope = rememberCoroutineScope()
    var tabVisible by remember { mutableFloatStateOf(1f) }

    var init by rememberSaveable{ mutableStateOf(true) }

    LaunchedEffect(init){
        if (init){
            if (communityViewModel.storyRes.value == null){
                communityViewModel.updateStoryListClear()
                communityViewModel.getStoryList(1)
            }
            if (communityViewModel.eventList.value == null){
                communityViewModel.getEventList(1)
            }
            init = false
        }
    }

    Scaffold (
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(modifier= Modifier
            .padding(paddingValues)
            .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            TabRow(
                modifier = Modifier.alpha(tabVisible),
                selectedTabIndex = pagerState.currentPage,
                indicator = { tabPositions -> TabRowDefaults.Indicator(Modifier.pagerTabIndicatorOffset(pagerState, tabPositions), color = design_login_text, height = 2.dp) },
                backgroundColor = design_white,
                contentColor = design_login_text
            ) {
                CommunityTabItems.forEachIndexed { index, idpwTabItem ->
                    Tab(
                        text = { Text(text = idpwTabItem.title, fontSize = 16.sp,color = design_login_text,
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
                count = CommunityTabItems.size,
                state = pagerState
            ) { page ->
                when(page){
                    0 -> StoryScreen(navController = navController, viewModel = communityViewModel)
                    1 -> EventScreen(navController = navController, viewModel = communityViewModel)
                    2 -> EventEndScreen(navController = navController, viewModel = communityViewModel)
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StoryScreen(navController: NavHostController, viewModel: CommunityViewModel){


    val storyListRes by viewModel.storyRes.collectAsState()
    val storyList by viewModel.storyList.collectAsState()
    val lazyGridState = rememberLazyGridState()
    val page by viewModel.storyPage.collectAsState()
    var isLoading by remember{ mutableStateOf(false) }
    val orderType by viewModel.orderType.collectAsState()
    val viewType by viewModel.viewType.collectAsState()

    var refreshing by remember{ mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = {
            refreshing = true
        })

    var oTDropDownShow by remember{ mutableStateOf(false) }
    var vTDropDownShow by remember{ mutableStateOf(false) }

    var typeChange by remember{ mutableStateOf(false) }

    val oTItems = listOf("최신순", "인기순")
    val vTItems = listOf("전체", "내 스토리")

    LaunchedEffect(key1 = typeChange){
        if (typeChange){
            isLoading = true

            viewModel.updateStoryListClear()
            viewModel.updateStoryPage(1)
            viewModel.getStoryList(1)

            isLoading = false
            typeChange = false
        }
    }

    LaunchedEffect(key1 = refreshing){
        if (refreshing){

            viewModel.updateStoryListClear()
            viewModel.updateStoryPage(1)
            viewModel.getStoryList(1)

            delay(300)
            refreshing = false
        }
    }

    LaunchedEffect(key1 = lazyGridState.canScrollForward){
        Log.d("LOG", lazyGridState.canScrollForward.toString())
        if (!lazyGridState.canScrollForward && !refreshing){
            if (storyListRes?.data?.paginate?.existNextPage == true){
                if (!isLoading){
                    isLoading = true

                    val result = viewModel.getStoryList(page + 1)
                    isLoading = if (result){
                        viewModel.updateStoryPage(page + 1)
                        false
                    }else{
                        viewModel.updateStoryPage(page - 1)
                        false
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = viewModel.moreStoryClick){
        if (viewModel.moreStoryClick.value != null){
            navController.navigate(Screen.StoryDetail.route)
            viewModel.getStoryDetail(viewModel.moreStoryClick.value!!)
            viewModel.updateMoreStoryClick(null)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = design_white)
    ){
        Column {
            Box{
                Row (
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp)
                ){

                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { oTDropDownShow = true }
                    ){
                        Text(
                            text = orderType,
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = 14.sp,
                            letterSpacing = (-0.7).sp,
                            color = design_login_text
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "",
                            tint = design_login_text
                        )
                    }



                    Spacer(modifier = Modifier.padding(start = 20.dp))

                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { vTDropDownShow = true }
                    ){
                        Text(
                            text = viewType,
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = 14.sp,
                            letterSpacing = (-0.7).sp,
                            color = design_login_text
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "",
                            tint = design_login_text
                        )
                    }
                }

                DropdownMenu(
                    expanded = oTDropDownShow,
                    onDismissRequest = { oTDropDownShow = false },
                    offset = DpOffset(x = 10.dp, y = 5.dp)
                ) {
                    oTItems.forEach { s ->
                        DropdownMenuItem(
                            onClick = {
                                if(s != orderType){
                                    viewModel.updateOrderType(s)
                                    typeChange = true
                                }
                                oTDropDownShow = false
                            },
                            text = {
                                Text(
                                    text = s,
                                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                    color = design_login_text,
                                    fontSize = 14.sp,letterSpacing = (-0.7).sp
                                )
                            },
                            contentPadding = PaddingValues(start = 10.dp)
                        )
                    }
                }

                DropdownMenu(
                    expanded = vTDropDownShow,
                    onDismissRequest = { vTDropDownShow = false },
                    offset = DpOffset(x = 90.dp, y = 5.dp)
                ) {
                    vTItems.forEach { s ->
                        DropdownMenuItem(
                            onClick = {
                                if(s != viewType){
                                    viewModel.updateViewType(s)
                                    typeChange = true
                                }
                                vTDropDownShow = false
                            },
                            text = {
                                Text(
                                    text = s,
                                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                    color = design_login_text,
                                    fontSize = 14.sp,letterSpacing = (-0.7).sp
                                )
                            },
                            contentPadding = PaddingValues(start = 10.dp)
                        )
                    }
                }
            }

            Crossfade(
                targetState = storyList.isEmpty(),
                label = "",
            ) { storyList.isEmpty()
                when(it){
                    true ->
                        Box(modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ){
                            LoadingAnimation1()
                        }
                    false ->
                        LazyVerticalGrid(
                            modifier = Modifier
                                .padding(top = 10.dp, start = 20.dp, end = 20.dp)
                                .pullRefresh(pullRefreshState)
                                .fillMaxSize(),
                            columns = GridCells.Fixed(2),
                            state = lazyGridState,
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(storyList?: emptyList()) { item ->
                                StoryListItem(data = item, navController = navController, viewModel = viewModel)
                            }
                        }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                    CustomIndicator(state = pullRefreshState, refreshing = refreshing)
                }
            }


        }// col
    }
}

@Composable
fun EventScreen(navController: NavHostController, viewModel: CommunityViewModel){

    val eventList by viewModel.eventList.collectAsState()

    Box (
        Modifier
            .fillMaxSize()
            .background(color = design_white)
    ){
        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(40.dp)
            ){
            if (eventList?.data?.bbsEvntList != null){
                items(eventList?.data?.bbsEvntList ?: emptyList()){ item ->
                    EventItem(eventItemData = item, navController, viewModel)
                }
            }
        }
    }
}

@Composable
fun EventEndScreen(navController: NavHostController, viewModel: CommunityViewModel){

    val eventList by viewModel.eventList.collectAsState()

    Box (
        Modifier
            .fillMaxSize()
            .background(color = design_white)
    ){
        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(40.dp)
        ){
            if (eventList?.data?.bbsEvntList != null){
                items(eventList?.data?.bbsEvntList ?: emptyList()){ item ->
                    EventEndItem(eventItemData = item, navController)
                }
            }
        }
    }
}

@Composable
fun EventItem(eventItemData: BbsEvnt, navController: NavHostController, viewModel: CommunityViewModel){

    Column (
        modifier= Modifier
            .wrapContentWidth()
            .fillMaxWidth()
            .clickable(
                enabled = !compareTimes(eventItemData.pstgEndDt)
            ) {
                viewModel.viewModelScope.launch {
                    viewModel.getEventDetail(eventItemData.pstSn)
                    navController.navigate(Screen.EventDetail.route)
                }
            }
    ) {
        var sizeImage by remember { mutableStateOf(IntSize.Zero) }

        Box(
            modifier = Modifier
                .onGloballyPositioned { sizeImage = it.size }
        ){
            AsyncImage(
                onLoading = {  },
                onError = { Log.d("LOG", "onError")},
                onSuccess = { Log.d("LOG", "onSuccess")},
                model = ImageRequest.Builder(LocalContext.current)
                    .data( eventItemData.rprsImgUrl )
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                placeholder = painterResource(id = R.drawable.profile_default),
                error= painterResource(id = R.drawable.profile_default),
                modifier= Modifier.fillMaxSize(),
                contentScale = ContentScale.FillWidth
            )

            if (compareTimes(eventItemData.pstgEndDt?: "")){
                Box(modifier = Modifier
                    .matchParentSize()
                    .background(color = design_alpha60_black),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = stringResource(R.string.commu_ended_event),
                        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                        fontSize = 16.sp,
                        letterSpacing = (-0.8).sp,
                        color = design_white
                    )
                }
            }
        }


        Text(
            text =  eventItemData.pstTtl,
            fontFamily = FontFamily(Font(R.font.pretendard_medium)),
            fontSize = 16.sp,
            letterSpacing = (-0.8).sp,
            color = if(compareTimes(eventItemData.pstgEndDt?: "")){
                design_B5B9BE
            }else{
                design_login_text
                 },
            modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
        )

        Text(
            text =  "${eventItemData.pstgBgngDt} ~ ${eventItemData.pstgEndDt}",
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = 14.sp,
            letterSpacing = (-0.7).sp,
            color = if(compareTimes(eventItemData.pstgEndDt?: "")){
                design_B5B9BE
            }else{
                design_skip
            }
        )
    }
}

@Composable
fun EventEndItem(eventItemData: BbsEvnt,navController: NavHostController){
    Column (
        modifier= Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .clickable(
                enabled = compareTimes(eventItemData.pstgEndDt ?: "")
            ) {
                navController.navigate(Screen.EventEndDetail.route)
            }
    ) {
        var sizeImage by remember { mutableStateOf(IntSize.Zero) }

        Box(
            modifier = Modifier
                .onGloballyPositioned { sizeImage = it.size }
        ){
            Image(
                painter = painterResource(id = R.drawable.event_thumb1),
                contentDescription = "",
                modifier= Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )

            if (compareTimes(eventItemData.pstgEndDt?: "")){
                Box(modifier = Modifier
                    .matchParentSize()
                    .background(color = design_alpha60_black),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = stringResource(id = R.string.commu_ended_event),
                        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                        fontSize = 16.sp,
                        letterSpacing = (-0.8).sp,
                        color = design_white
                    )
                }
            }
        }


        Text(
            text =  "본아페티 관절 영양제 무료 체험단",
            fontFamily = FontFamily(Font(R.font.pretendard_medium)),
            fontSize = 16.sp,
            letterSpacing = (-0.8).sp,
            color = if(compareTimes(eventItemData.pstgEndDt?: "")){
                design_B5B9BE
            }else{
                design_login_text
            },
            modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
        )

        Text(
            text =  "2023.08.16(월) ~ 2023.08.19(수)",
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = 14.sp,
            letterSpacing = (-0.7).sp,
            color = if(compareTimes(eventItemData.pstgEndDt?: "")){
                design_B5B9BE
            }else{
                design_skip
            }
        )
    }
}

data class CommunityTabItem(
    val title: String,
    val screen: @Composable (NavHostController, CommunityViewModel) -> Unit // navController를 매개변수로 추가
)

val CommunityTabItems = listOf(
    CommunityTabItem(
        title = "스토리",
        screen = { navController, viewModel ->
            StoryScreen(
                navController = navController,
                viewModel = viewModel
            ) }),
    CommunityTabItem(
        title = "이벤트",
        screen = { navController, viewModel ->
            EventScreen(
                navController = navController,
                viewModel = viewModel
            ) }),
    CommunityTabItem(
        title = "당첨자 발표",
        screen = { navController, viewModel ->
            EventEndScreen(
                navController = navController,
                viewModel = viewModel
            ) })
)

fun compareTimes(inputTime: String?) : Boolean {

    if (inputTime == "" || inputTime == null){
        return false
    }else{
        // 현재 시간 가져오기
        val currentTime = LocalDateTime.now()
        // 입력된 문자열을 LocalDateTime으로 파싱
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val parsedTime = LocalDateTime.parse(inputTime, inputFormatter)

        // 현재 시간과 비교
        return if (currentTime.isBefore(parsedTime)) {
            false
        } else if (currentTime.isEqual(parsedTime)) {
            true
        } else {
            true
        }
    }
}
