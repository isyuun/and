package net.pettip.app.navi.screens.mainscreen

import android.graphics.BlurMaskFilter
import android.graphics.Paint.Style
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.core.provider.FontRequest
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Scaffold
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.component.measureTextWidth
import net.pettip.app.navi.screens.myscreen.MySelectableDates
import net.pettip.app.navi.ui.theme.design_002D83
import net.pettip.app.navi.ui.theme.design_4783F5
import net.pettip.app.navi.ui.theme.design_666666
import net.pettip.app.navi.ui.theme.design_777777
import net.pettip.app.navi.ui.theme.design_999EA9
import net.pettip.app.navi.ui.theme.design_ACC1E9
import net.pettip.app.navi.ui.theme.design_B4CDFF
import net.pettip.app.navi.ui.theme.design_DEDEDE
import net.pettip.app.navi.ui.theme.design_FFF2EB
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_login_text
import net.pettip.app.navi.ui.theme.design_shadow
import net.pettip.app.navi.ui.theme.design_sharp
import net.pettip.app.navi.ui.theme.design_skip
import net.pettip.app.navi.ui.theme.design_textFieldOutLine
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.SharedViewModel
import net.pettip.app.navi.viewmodel.WalkViewModel
import net.pettip.data.pet.CurrentPetData
import net.pettip.util.Log
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

/**
 * @Project     : PetTip-Android
 * @FileName    : WalkScreenV2
 * @Date        : 2024-02-07
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screens.mainscreen
 * @see net.pettip.app.navi.screens.mainscreen.WalkScreenV2
 */
 @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
 @Composable
fun WalkScreenV2(
    viewModel: WalkViewModel,
    navController: NavHostController,
    sharedViewModel: SharedViewModel
){
    val days = getDaysOfWeekAndYear(2024)
    val currentDateTime = LocalDateTime.now()
    val todayIndex = days.indexOfFirst { (dayInfo, yearMonthInfo) ->
        dayInfo.day == currentDateTime.dayOfMonth && yearMonthInfo.month == currentDateTime.monthValue-1 && yearMonthInfo.year == currentDateTime.year
    }

    val dateLazyState = rememberLazyListState()
    val pagerState = rememberPagerState(pageCount = { days.size }, initialPage = todayIndex-3)
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f,10f),0f)

    val datePickerState = rememberDatePickerState(selectableDates = MySelectableDates())

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val itemWidth = (screenWidth-100.dp)/7
    val remainingSpace = (screenWidth - itemWidth*7 - 60.dp) / 2

    var showDatePicker by remember{ mutableStateOf(false) }
    var selectDayIndex by remember{ mutableIntStateOf( todayIndex ) }

    val rank by remember{ mutableStateOf("상위 25%") }

    var myTime by remember { mutableStateOf(30f) }
    var recommendTime by remember { mutableStateOf(30f) }

    val dailyLifeTimeLineList by viewModel.dailyLifeTimeLineList.collectAsState()

    val scope = rememberCoroutineScope()

    Scaffold (
    ){
        if (showDatePicker){
            AlertDialog(
                onDismissRequest = { showDatePicker = false },
                properties = DialogProperties(
                    usePlatformDefaultWidth = false
                )
            ){
                Box (
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(20.dp))
                ) {
                    Column {
                        DatePicker(
                            state = datePickerState,
                            colors = DatePickerDefaults.colors(
                                selectedDayContainerColor = design_intro_bg,
                                selectedDayContentColor = design_white,
                                todayDateBorderColor = design_intro_bg,
                                todayContentColor = design_intro_bg,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                headlineContentColor = MaterialTheme.colorScheme.onPrimary,
                                weekdayContentColor = MaterialTheme.colorScheme.onPrimary,
                                subheadContentColor = MaterialTheme.colorScheme.onPrimary,
                                navigationContentColor = MaterialTheme.colorScheme.onPrimary,
                                yearContentColor = MaterialTheme.colorScheme.onPrimary,
                                dayContentColor = MaterialTheme.colorScheme.onPrimary,
                                currentYearContentColor = MaterialTheme.colorScheme.onPrimary,
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        )

                        Row (
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                        ){
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(MaterialTheme.colorScheme.onSecondary)
                                    .clickable { showDatePicker = false },
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    text = stringResource(id = R.string.cancel_kor),
                                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                    fontSize = 14.sp, letterSpacing = (-0.7).sp,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(design_intro_bg)
                                    .clickable {
                                        val instant = Instant.ofEpochMilli(datePickerState.selectedDateMillis ?: Date().time)
                                        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

                                        val year = localDateTime.year
                                        val month = localDateTime.monthValue
                                        val day = localDateTime.dayOfMonth

                                        val index = days.indexOfFirst { (dayInfo, yearMonthInfo) ->
                                            dayInfo.day == day && yearMonthInfo.month == month - 1 && yearMonthInfo.year == year
                                        }

                                        scope.launch {
                                            selectDayIndex = index
                                            pagerState.animateScrollToPage(index - 3)
                                        }
                                        showDatePicker = false
                                    },
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    text = stringResource(id = R.string.confirm),
                                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                    fontSize = 14.sp, letterSpacing = (-0.7).sp,
                                    color = design_white,
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onPrimaryContainer)
                .fillMaxSize()
        ) {
            Canvas(
                modifier = Modifier
                    .padding(start = 27.dp)
                    .fillMaxHeight()
                    .width(1.dp),
            ) {
                drawLine(
                    color = Color.Gray,
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height),
                    pathEffect = pathEffect
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                state = dateLazyState,
                contentPadding = PaddingValues(top = itemWidth * 1.6f + 96.dp ,bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(40.dp)
            ){
                item {
                    Crossfade(
                        targetState = selectDayIndex == todayIndex, label = ""
                    ) {
                        when(it){
                            true -> TodayRating(rank = rank, myTimeData = myTime, recommendTimeData = recommendTime)
                            false -> OtherDayRating(rank = rank, count = "00", time = "00:00:00", distance = "000.00",
                                day = "${days[selectDayIndex].second.year}.${String.format("%02d",days[selectDayIndex].second.month+1)}.${days[selectDayIndex].first.day}")
                        }
                    }

                    Spacer(modifier = Modifier.padding(vertical = 8.dp))

                    PetSelectItem(
                        sharedViewModel = sharedViewModel,
                        pointText = "10분이상 산책시 +500p",
                        buttonText = "산책하기",
                        buttonJob = {}
                    )

                    Spacer(modifier = Modifier.padding(vertical = 8.dp))

                    PetSelectItem(
                        sharedViewModel = sharedViewModel,
                        pointText = "몸무게등록시 +50p",
                        buttonText = "몸무게 등록하기",
                        buttonJob = {}
                    )

                    Spacer(modifier = Modifier.padding(vertical = 8.dp))

                    AddPetNotification(
                        buttonJob = {}
                    )

                    Spacer(modifier = Modifier.padding(vertical = 8.dp))

                    WalkItem(sharedViewModel = sharedViewModel)

                    Spacer(modifier = Modifier.padding(vertical = 8.dp))

                    WeightItem(sharedViewModel = sharedViewModel)

                    Spacer(modifier = Modifier.padding(vertical = 8.dp))

                    EmptyWalkBox()
                }

                dailyLifeTimeLineList?.let { dailyLifeTimeLineList ->
                    for ((dateKey, itemList) in dailyLifeTimeLineList) {
                        item {
                            DateItem(viewModel = viewModel, dateKey = dateKey, dailyLifeTimeLineList = itemList, navController = navController)
                        }
                    }
                }
            }

            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemWidth * 1.6f + 96.dp)
                    .background(design_white)
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(design_4783F5)
                ) {
                    Row (
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                            .fillMaxWidth()
                    ){
                        Row (
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "", tint = design_white,
                                modifier = Modifier
                                    .clickable {
                                        scope.launch {
                                            pagerState.animateScrollToPage(
                                                page = pagerState.currentPage-7,
                                                animationSpec = spring(
                                                    dampingRatio = Spring.DampingRatioNoBouncy,
                                                    stiffness = Spring.StiffnessMedium
                                                )
                                            )
                                        }
                                    }
                            )

                            Text(
                                text = "${days[pagerState.currentPage].second.year}.${String.format("%02d",days[pagerState.currentPage].second.month+1)}",
                                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                                fontSize = 20.sp,
                                letterSpacing = (-1.0).sp,
                                color = design_white,
                                modifier = Modifier.clickable {
                                    showDatePicker = true
                                }
                            )

                            Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "", tint = design_white,
                                modifier = Modifier
                                    .clickable {
                                        scope.launch {
                                            pagerState.animateScrollToPage(
                                                page = pagerState.currentPage+7,
                                                animationSpec = spring(
                                                    dampingRatio = Spring.DampingRatioNoBouncy,
                                                    stiffness = Spring.StiffnessMedium
                                                )
                                            )
                                        }
                                    }
                            )
                        }

                        Button(
                            onClick = {
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                            border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.outlineVariant),
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier
                                .height(36.dp)
                                .width(72.dp)
                        ) {
                            Text(
                                text = "월 기록", color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                lineHeight = 14.sp
                            )
                        }

                    }

                    HorizontalPager(
                        modifier = Modifier
                            //.padding(vertical = 20.dp)
                            .fillMaxWidth()
                            .height(itemWidth * 1.6f + 40.dp)
                            .align(Alignment.CenterHorizontally),
                        state = pagerState,
                        pageSize = PageSize.Fixed(itemWidth),
                        pageSpacing = 10.dp,
                        contentPadding = PaddingValues(horizontal = remainingSpace),
                        beyondBoundsPageCount = 0,
                        flingBehavior = PagerDefaults.flingBehavior(
                            state = pagerState,
                            snapVelocityThreshold = 8.dp,
                            pagerSnapDistance = PagerSnapDistance.atMost(30)
                        )
                    ) { page ->
                        Box (
                            modifier = Modifier
                                .graphicsLayer {
                                    val pageOffset = pagerState.calculateCurrentOffsetForPage(page)

                                    if (pageOffset<=-6f){
                                        alpha = (7f + pageOffset)
                                    }else{
                                        alpha = (1f - pageOffset)
                                    }
                                }
                        ){
                            DayItem(day = days[page], page = page, selectDayIndex = selectDayIndex, onSelect = {selectDayIndex = page}, itemWidth = itemWidth, selectable = page<=todayIndex)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WalkItem(
    sharedViewModel: SharedViewModel
){
    val currentPetData by sharedViewModel.currentPetInfo.collectAsState()
    val pagerState = rememberPagerState(pageCount = { currentPetData.size })

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val itemWidth = (screenWidth-120.dp)/3

    Box(
        modifier = Modifier.fillMaxWidth()
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(painter = painterResource(id = R.drawable.timeline_date),
                contentDescription = "", tint = Color.Unspecified,
                modifier = Modifier.padding(start = 20.dp)
            )

            Text(
                text = "00:00",
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 12.sp,
                color = design_intro_bg,
                modifier = Modifier.padding(start = 6.dp)
            )
        }

        Column (
            modifier = Modifier
                .padding(start = 40.dp, end = 20.dp, top = 32.dp)
                .fillMaxWidth()
                .shadowWithBottom()
                .clip(shape = RoundedCornerShape(12.dp))
                .border(width = 1.dp, color = design_textFieldOutLine, shape = RoundedCornerShape(12.dp))
                .background(color = design_white, shape = RoundedCornerShape(12.dp))
        ){
            HorizontalPager(
                modifier = Modifier
                    .padding(vertical = 28.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                state = pagerState,
                pageSize = PageSize.Fixed(itemWidth),
                pageSpacing = 10.dp,
                contentPadding = PaddingValues(horizontal = 20.dp),
                beyondBoundsPageCount = 0,
                flingBehavior = PagerDefaults.flingBehavior(
                    state = pagerState,
                    snapVelocityThreshold = 3.dp,
                    pagerSnapDistance = PagerSnapDistance.atMost(30)
                )
            ) {page->
                CirclePetItem(petData = currentPetData[page], size = itemWidth)
            }


            Text(
                text = "산책자 닉네임명 초과시 말줄임 테스트 길게",
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 20.sp,
                color = design_login_text,
                modifier = Modifier.padding(start = 30.dp, end = 30.dp),
                lineHeight = 20.sp,
                maxLines = 1, overflow = TextOverflow.Ellipsis
            )

            Row (
                modifier = Modifier.padding(start = 30.dp, end = 30.dp, top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ){
                Text(
                    text = "산책시간",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp,
                    color = design_skip,
                    lineHeight = 14.sp,
                )

                Text(
                    text = "00 : 00 : 00",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp,
                    color = design_login_text,
                    lineHeight = 14.sp, letterSpacing = (-0.7).sp
                )
            }


            Row (
                modifier = Modifier.padding(start = 30.dp, end = 30.dp, top = 4.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ){
                Text(
                    text = "산책거리",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp,
                    color = design_skip,
                    lineHeight = 14.sp,
                )

                Text(
                    text = "0.00km",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp,
                    color = design_login_text,
                    lineHeight = 14.sp,
                )
            }

            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(start = 30.dp, end = 30.dp, bottom = 30.dp)
                    .fillMaxWidth()
            ){
                Icon(painter = painterResource(id = R.drawable.icon_pee), contentDescription = "", tint = Color.Unspecified)
                Text(
                    text = "12회",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp,
                    color = design_login_text,
                    lineHeight = 14.sp,
                    modifier = Modifier.padding(start = 4.dp, end = 16.dp)
                )

                Icon(painter = painterResource(id = R.drawable.icon_poop), contentDescription = "", tint = Color.Unspecified)
                Text(
                    text = "12회",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp,
                    color = design_login_text,
                    lineHeight = 14.sp,
                    modifier = Modifier.padding(start = 4.dp, end = 16.dp)
                )

                Icon(painter = painterResource(id = R.drawable.icon_marking), contentDescription = "", tint = Color.Unspecified)
                Text(
                    text = "12회",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp,
                    color = design_login_text,
                    lineHeight = 14.sp,
                    modifier = Modifier.padding(start = 4.dp, end = 16.dp)
                )
            }

        }//col

        Box (
            modifier = Modifier
                .padding(end = 30.dp, top = 12.dp)
                .clip(shape = RoundedCornerShape(20.dp))
                .size(width = 80.dp, height = 40.dp)
                .background(color = design_sharp, shape = RoundedCornerShape(20.dp))
                .align(Alignment.TopEnd),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "+ 500p",
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 14.sp, lineHeight = 14.sp,
                color = design_white
            )
        }
    }//Box
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeightItem(
    sharedViewModel: SharedViewModel
){
    val currentPetData by sharedViewModel.currentPetInfo.collectAsState()
    val pagerState = rememberPagerState(pageCount = { currentPetData.size })

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val itemWidth = (screenWidth-120.dp)/3

    Box(
        modifier = Modifier.fillMaxWidth()
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(painter = painterResource(id = R.drawable.timeline_date),
                contentDescription = "", tint = Color.Unspecified,
                modifier = Modifier.padding(start = 20.dp)
            )

            Text(
                text = "00:00",
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 12.sp,
                color = design_intro_bg,
                modifier = Modifier.padding(start = 6.dp)
            )
        }

        Column (
            modifier = Modifier
                .padding(start = 40.dp, end = 20.dp, top = 32.dp)
                .fillMaxWidth()
                .shadowWithBottom()
                .clip(shape = RoundedCornerShape(12.dp))
                .border(width = 1.dp, color = design_textFieldOutLine, shape = RoundedCornerShape(12.dp))
                .background(color = design_white, shape = RoundedCornerShape(12.dp))
        ){
            HorizontalPager(
                modifier = Modifier
                    .padding(vertical = 28.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                state = pagerState,
                pageSize = PageSize.Fixed(itemWidth),
                pageSpacing = 10.dp,
                contentPadding = PaddingValues(horizontal = 20.dp),
                beyondBoundsPageCount = 0,
                flingBehavior = PagerDefaults.flingBehavior(
                    state = pagerState,
                    snapVelocityThreshold = 3.dp,
                    pagerSnapDistance = PagerSnapDistance.atMost(30)
                )
            ) {page->
                CirclePetItem(petData = currentPetData[page], size = itemWidth)
            }


            Text(
                text = "산책자 닉네임명 초과시 말줄임 테스트 길게",
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 20.sp,
                color = design_login_text,
                modifier = Modifier.padding(start = 30.dp, end = 30.dp),
                lineHeight = 20.sp,
                maxLines = 1, overflow = TextOverflow.Ellipsis
            )

            Text(
                text = buildAnnotatedString {
                    append("몸무게 등록 ")
                    withStyle(style = SpanStyle(fontFamily = FontFamily(Font(R.font.pretendard_bold)))){
                        append("3")
                    }
                    append("건")
                },
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 12.sp,
                color = design_777777,
                lineHeight = 14.sp,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 30.dp, top = 20.dp)
            )

            LazyColumn(
                state = rememberLazyListState(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .padding(start = 30.dp, end = 30.dp, top = 16.dp, bottom = 30.dp)
                    .fillMaxWidth()
                    .heightIn(max = 5000.dp)
            ){
                item {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = "텍스트 열자 길이 테스트",
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = 14.sp, lineHeight = 14.sp,
                            overflow = TextOverflow.Ellipsis,
                            color = design_skip,
                            maxLines = 1,
                            modifier = Modifier.width(measureTextWidth(text = "텍스트 열자 길이 테스트", style = TextStyle(fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular))))+20.dp)
                        )

                        Text(
                            text = "12.12kg",
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = 14.sp, lineHeight = 14.sp,
                            overflow = TextOverflow.Ellipsis,
                            color = design_login_text,
                        )
                    }
                }
            }

        }//col

        Box (
            modifier = Modifier
                .padding(end = 30.dp, top = 12.dp)
                .clip(shape = RoundedCornerShape(20.dp))
                .size(width = 80.dp, height = 40.dp)
                .background(color = design_sharp, shape = RoundedCornerShape(20.dp))
                .align(Alignment.TopEnd),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "+ 500p",
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 14.sp, lineHeight = 14.sp,
                color = design_white
            )
        }
    }//Box
}


@Composable
fun TodayRating(
    rank:String,
    myTimeData:Float,
    recommendTimeData:Float
){
    var myTime by remember { mutableStateOf(0f) }
    var recommendTime by remember { mutableStateOf(0f) }
    var myTimeToFloat by remember { mutableStateOf(0f) }
    var recommendTimeToFloat by remember { mutableStateOf(0f) }

    val myBarSize by animateFloatAsState(
        targetValue = myTimeToFloat,
        tween(
            durationMillis = 1000,
            delayMillis = 200,
            easing = LinearOutSlowInEasing
        ),
        label = ""
    )

    LaunchedEffect(Unit){
        myTime = myTimeData
        recommendTime = recommendTimeData
        if (myTime == recommendTime){
            myTimeToFloat = 0.8f
            recommendTimeToFloat = 0.8f
        } else if (myTime>recommendTime){
            myTimeToFloat = 0.8f
            recommendTimeToFloat = (recommendTime/myTime) * 0.8f
        }else{
            recommendTimeToFloat = 0.8f
            myTimeToFloat = (myTime/recommendTime) * 0.8f
        }
    }

    Column (
        modifier = Modifier.padding(start = 40.dp, end = 20.dp, top = 16.dp)
    ){
        Text(
            text = buildAnnotatedString{
                append("오늘 현재까지 산책시간은\n")
                withStyle(
                    style = SpanStyle(fontFamily = FontFamily(Font(R.font.pretendard_bold)))
                ){
                    append("$rank")
                }
                append("예요")
            },
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = 24.sp, letterSpacing = -(0).sp,
            lineHeight = 32.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Row (
                modifier = Modifier.fillMaxWidth(0.8f),
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth(myBarSize)
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(design_intro_bg)
                        .animateContentSize()
                )

                Text(
                    text = "${myTime.toInt()}분",
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    fontSize = 10.sp, color = design_intro_bg,
                    modifier = Modifier.padding(start = 8.dp),
                    lineHeight = 10.sp
                )
            }

            Text(
                text = "나",
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 10.sp, color = design_login_text,
                lineHeight = 10.sp
            )
        }


        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Row (
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth(recommendTimeToFloat)
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(design_skip)
                )

                Text(
                    text = "${recommendTime.toInt()}분",
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    fontSize = 10.sp, color = design_skip,
                    modifier = Modifier.padding(start = 8.dp),
                    lineHeight = 10.sp
                )
            }

            Text(
                text = "권장평균",
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 10.sp, color = design_login_text,
                lineHeight = 10.sp
            )
        }
    }
}

@Composable
fun OtherDayRating(
    rank:String,
    count:String,
    time:String,
    distance: String,
    day:String
){
    Column (
        modifier = Modifier.padding(start = 40.dp, end = 20.dp, top = 16.dp)
    ){
        Text(
            text = buildAnnotatedString{
                append("$day 산책시간은\n")
                withStyle(
                    style = SpanStyle(fontFamily = FontFamily(Font(R.font.pretendard_bold)))
                ){
                    append("$rank")
                }
                append("예요")
            },
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = 24.sp, letterSpacing = -(0).sp,
            lineHeight = 32.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row (
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(28.dp)
        ){
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = count,
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    fontSize = 20.sp, color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(bottom = 4.dp),
                    lineHeight = 20.sp
                )

                Text(
                    text = "총횟수",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp, color = MaterialTheme.colorScheme.onPrimary,
                    lineHeight = 14.sp
                )
            }

            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = time,
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    fontSize = 20.sp, color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(bottom = 4.dp),
                    lineHeight = 20.sp
                )

                Text(
                    text = "총시간",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp, color = MaterialTheme.colorScheme.onPrimary,
                    lineHeight = 14.sp
                )
            }

            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Text(
                    text = distance,
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    fontSize = 20.sp, color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(bottom = 4.dp),
                    lineHeight = 20.sp
                )

                Text(
                    text = "총거리(km)",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp, color = MaterialTheme.colorScheme.onPrimary,
                    lineHeight = 14.sp
                )
            }
        }
    }
}

@Composable
fun DayItem(
    day: Pair<DayInfo, YearMonthInfo>,
    page: Int,
    selectDayIndex: Int,
    onSelect: () -> Unit,
    itemWidth: Dp,
    selectable:Boolean,
) {
    val backgroundColor by animateColorAsState(
        targetValue = when {
            page == selectDayIndex -> design_002D83
            !selectable -> design_B4CDFF
            else -> design_white
        }, label = "",
        animationSpec = tween(durationMillis = 350)
    )

    val textColor by animateColorAsState(
        targetValue = when {
            page == selectDayIndex -> design_white
            !selectable -> design_777777
            else -> design_login_text
        }, label = "",
        animationSpec = tween(durationMillis = 350)
    )

    Column (
        modifier = Modifier
            .width(itemWidth)
            .height(itemWidth * 1.6f)
            .clip(RoundedCornerShape(itemWidth / 2))
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(
                enabled = selectable
            ) {
                onSelect()
            }
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        Text(
            text = day.first.dayOfWeek,
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = 14.sp,
            color = textColor,
            lineHeight = 14.sp,
            modifier = Modifier.padding(bottom = 2.dp)
        )

        Text(
            text = day.first.day.toString(),
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            fontSize = 14.sp,
            color = textColor,
            lineHeight = 14.sp,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PetSelectItem(
    sharedViewModel: SharedViewModel,
    pointText:String,
    buttonText:String,
    buttonJob:()->Unit
){
    val currentPetData by sharedViewModel.currentPetInfo.collectAsState()
    val pagerState = rememberPagerState(pageCount = { currentPetData.size })

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val itemWidth = (screenWidth-120.dp)/3

    Column (
        modifier = Modifier
            .padding(start = 40.dp, end = 20.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(12.dp))
            .border(width = 1.dp, shape = RoundedCornerShape(12.dp), color = design_textFieldOutLine)
            .background(design_white)
    ){
        Box (
            modifier = Modifier
                .padding(end = 20.dp, top = 20.dp)
                .clip(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp, bottomStart = 0.dp, bottomEnd = 15.dp))
                .background(design_FFF2EB)
                .align(Alignment.End),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = pointText,
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                lineHeight = 12.sp
            )
        }

        HorizontalPager(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            state = pagerState,
            pageSize = PageSize.Fixed(itemWidth),
            pageSpacing = 10.dp,
            contentPadding = PaddingValues(horizontal = 20.dp),
            beyondBoundsPageCount = 0,
            flingBehavior = PagerDefaults.flingBehavior(
                state = pagerState,
                snapVelocityThreshold = 3.dp,
                pagerSnapDistance = PagerSnapDistance.atMost(30)
            )
        ) {page->
            SelectablePetItem(petData = currentPetData[page], size = itemWidth)
        }

        Button(
            onClick = { buttonJob() },
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 30.dp)
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = design_intro_bg
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 6.dp,
                pressedElevation = 0.dp,
                hoveredElevation = 0.dp,
                focusedElevation = 0.dp,
                disabledElevation = 0.dp
            )
        ) {
            Text(
                text = buttonText,
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                fontSize = 14.sp, lineHeight = 14.sp,
                color = design_white
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddPetNotification(
    buttonJob:()->Unit
){

    Column (
        modifier = Modifier
            .padding(start = 40.dp, end = 20.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(12.dp))
            .border(width = 1.dp, shape = RoundedCornerShape(12.dp), color = design_textFieldOutLine)
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Text(
            text = "반려견이 등록되어 있지 않아요",
            fontFamily = FontFamily(Font(R.font.pretendard_medium)),
            fontSize = 18.sp, color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(top = 60.dp, bottom = 4.dp),
            lineHeight = 18.sp, letterSpacing = (-0.6).sp
        )

        Text(
            text =
            buildAnnotatedString {
                                 withStyle(
                                     style = SpanStyle(
                                         fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                                         fontSize = 22.sp
                                     )
                                 ){
                                     append("반려견을 등록")
                                 }
                append("해 주세요!!")
            },
            fontFamily = FontFamily(Font(R.font.pretendard_medium)),
            fontSize = 20.sp, color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(bottom = 40.dp),
            lineHeight = 22.sp, letterSpacing = (-0.6).sp
        )

        Button(
            onClick = { buttonJob() },
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 30.dp)
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = design_intro_bg
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 6.dp,
                pressedElevation = 0.dp,
                hoveredElevation = 0.dp,
                focusedElevation = 0.dp,
                disabledElevation = 0.dp
            )
        ) {
            Text(
                text = "반려동물 등록하기",
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                fontSize = 14.sp, lineHeight = 14.sp,
                color = design_white
            )
        }
    }
}

@Composable
fun SelectablePetItem(
    petData: CurrentPetData,
    size: Dp
){
    var check by remember { mutableStateOf(false) }

    Box (
        modifier = Modifier.size(size)
    ){
        Box(
            modifier = Modifier
                .size(size)
                .sizeIn(minWidth = 80.dp, minHeight = 80.dp, maxWidth = 120.dp, maxHeight = 120.dp)
                .border(shape = CircleShape, border = BorderStroke(3.dp, color = MaterialTheme.colorScheme.tertiary))
                .shadow(elevation = 4.dp, shape = CircleShape, spotColor = Color.Gray)
                .clip(CircleShape)
                .align(Alignment.Center)
                .clickable { check = !check }
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(petData.petRprsImgAddr)
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                placeholder = painterResource(id = R.drawable.profile_default),
                error= painterResource(id = R.drawable.profile_default),
                modifier= Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Box (
            modifier = Modifier
                .padding(top = 2.dp, end = 2.dp)
                .size(20.dp)
                .clip(RoundedCornerShape(6.dp))
                .border(
                    width = if (check) 0.dp else (1.5).dp,
                    color = design_textFieldOutLine,
                    shape = RoundedCornerShape(6.dp)
                )
                .background(
                    color = if (check) design_intro_bg else design_white,
                    shape = RoundedCornerShape(6.dp)
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    check = !check
                }
                .align(Alignment.TopEnd),
            contentAlignment = Alignment.Center
        ){
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = "", tint = design_white,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun EmptyWalkBox(){
    Column (
        modifier = Modifier
            .padding(start = 40.dp, end = 20.dp)
            .fillMaxWidth()
            .height(200.dp)
            .clip(shape = RoundedCornerShape(12.dp))
            .border(width = 1.dp, shape = RoundedCornerShape(12.dp), color = design_textFieldOutLine)
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Box (
            modifier = Modifier
                .padding(bottom = 16.dp)
                .clip(CircleShape)
                .background(design_DEDEDE)
                .border(color = design_textFieldOutLine, shape = CircleShape, width = 1.dp)
                .size(48.dp),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "!",
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 24.sp, lineHeight = 24.sp,
                color = design_777777
            )
        }

        Text(
            text = "산책기록이 없어요",
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = 18.sp, lineHeight = 20.sp,
            color = design_999EA9, letterSpacing = (-0.6).sp
        )
    }
}

@Composable
fun CirclePetItem(
    petData: CurrentPetData,
    size: Dp
){
    Box (
        modifier = Modifier.size(size)
    ){
        Box(
            modifier = Modifier
                .size(size)
                .sizeIn(minWidth = 80.dp, minHeight = 80.dp, maxWidth = 120.dp, maxHeight = 120.dp)
                .border(shape = CircleShape, border = BorderStroke(3.dp, color = MaterialTheme.colorScheme.tertiary))
                .shadow(elevation = 4.dp, shape = CircleShape, spotColor = Color.Gray)
                .clip(CircleShape)
                .align(Alignment.Center)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(petData.petRprsImgAddr)
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                placeholder = painterResource(id = R.drawable.profile_default),
                error= painterResource(id = R.drawable.profile_default),
                modifier= Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}


//data class DayInfo(
//    val dayOfWeek: String,
//    val day: Int
//)
//
//data class YearMonthInfo(
//    val year: Int,
//    val month: Int
//)
//
//fun getDaysOfWeekAndYear(year: Int): MutableList<Pair<DayInfo, YearMonthInfo>> {
//    val calendar = Calendar.getInstance().apply {
//        set(Calendar.YEAR, year)
//        set(Calendar.MONTH, Calendar.JANUARY) // 1월로 설정
//        set(Calendar.DAY_OF_MONTH, 1)
//    }
//
//    val dayList = mutableListOf<Pair<DayInfo, YearMonthInfo>>()
//
//    for (yearIn in 2000..year){
//        calendar.set(Calendar.YEAR, yearIn)
//        for (month in Calendar.JANUARY..Calendar.DECEMBER) {
//            calendar.set(Calendar.MONTH, month)
//
//            // Get the number of days in the current month
//            val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
//
//            // Get year and month information
//            val yearMonthInfo = YearMonthInfo(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH))
//
//            // Add days of the month with corresponding days of the week
//            for (i in 1..daysInMonth) {
//                val dayOfWeek = SimpleDateFormat("EE", Locale.KOREA).format(calendar.time)
//                val dayInfo = DayInfo(dayOfWeek, i)
//                dayList.add(dayInfo to yearMonthInfo)
//
//                // Move to the next day
//                calendar.add(Calendar.DAY_OF_MONTH, 1)
//            }
//        }
//    }
//    // Loop through each month of the year
//
//
//    return dayList
//}