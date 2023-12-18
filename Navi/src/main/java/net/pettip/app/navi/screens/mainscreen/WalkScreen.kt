@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalPagerApi::class, ExperimentalFoundationApi::class
)

package net.pettip.app.navi.screens.mainscreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.Screen
import net.pettip.app.navi.component.CustomBottomSheet
import net.pettip.app.navi.component.ErrorScreen
import net.pettip.app.navi.component.LoadingAnimation1
import net.pettip.app.navi.component.MonthCalendar
import net.pettip.app.navi.ui.theme.design_76A1EF
import net.pettip.app.navi.ui.theme.design_C3D3EC
import net.pettip.app.navi.ui.theme.design_button_bg
import net.pettip.app.navi.ui.theme.design_home_bg
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_select_btn_border
import net.pettip.app.navi.ui.theme.design_textFieldOutLine
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.HomeViewModel
import net.pettip.app.navi.viewmodel.SharedViewModel
import net.pettip.app.navi.viewmodel.WalkViewModel
import net.pettip.data.daily.DailyLifeWalk
import net.pettip.data.daily.Day
import net.pettip.singleton.G
import net.pettip.util.Log
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalkScreen(
    navController: NavHostController,
    walkViewModel: WalkViewModel,
    sharedViewModel: SharedViewModel,
    homeViewModel: HomeViewModel,
    backBtnOn: (Boolean) -> Unit,
    openBottomSheet: Boolean,
    onDissMiss: (Boolean) -> Unit,
    modeChange: (Boolean) -> Unit
){

    val toDetail by walkViewModel.toDetail.collectAsState()
    val toMonthCalendar by walkViewModel.toMonthCalendar.collectAsState()
    val selectPet by sharedViewModel.selectPet.collectAsState()
    var isLoading by rememberSaveable{ mutableStateOf(false) }
    var isError by rememberSaveable{ mutableStateOf(false) }
    val refresh by walkViewModel.weekRecordRefresh.collectAsState()


    val bottomSheetState =
        androidx.compose.material3.rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val context = LocalContext.current
    val density = LocalDensity.current.density
    val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")

    val navigationBarHeight = if (resourceId > 0) {
        (context.resources.getDimensionPixelSize(resourceId) / density).dp
    } else {
        0.dp
    }

    var selectPetPre:String? by remember { mutableStateOf(null) }
    var selectDayPre:String? by remember { mutableStateOf(null) }
    val selectDay by walkViewModel.selectDay.collectAsState()

    LaunchedEffect(key1 = G.toPost){
        if(G.toPost){
            navController.navigate(Screen.PostScreen.route)
        }
    }

    LaunchedEffect(key1 = refresh){
        if (refresh){
            isLoading = true
            homeViewModel.viewModelScope.launch {
                val result = homeViewModel.getWeekRecord(selectPet?.ownrPetUnqNo ?: "",  selectDay)
                isLoading = false
                isError = !result
                walkViewModel.updateWeekRecordRefresh(false)
            }
        }
    }

    LaunchedEffect(key1 = selectDay){
        if (selectDay != selectDayPre && selectDayPre != null && !refresh){
            isLoading = true
            homeViewModel.viewModelScope.launch {
                val result = homeViewModel.getWeekRecord(selectPet?.ownrPetUnqNo ?: "",  selectDay)
                isLoading = false
                isError = !result
            }
        }
        selectDayPre = selectDay
    }

    LaunchedEffect(key1 = selectPet){
        if (selectPet?.ownrPetUnqNo != selectPetPre && selectPetPre != null && !refresh){
            isLoading = true
            homeViewModel.viewModelScope.launch {
                val result = homeViewModel.getWeekRecord(selectPet?.ownrPetUnqNo ?: "",  selectDay)
                isLoading = false
                isError = !result
            }
        }
        selectPetPre = selectPet?.ownrPetUnqNo
    }

    LaunchedEffect(key1 = toMonthCalendar){
        if (!toMonthCalendar){
            backBtnOn(false)
        }
    }

    SideEffect {
        if(toMonthCalendar){
            backBtnOn(true)
        }
    }

    BackHandler (enabled = toMonthCalendar){
        walkViewModel.updateToMonthCalendar(false)
        if (!toDetail){
            backBtnOn(false)
        }
    }

    Crossfade(
        targetState = !isLoading && isError,
        label = ""
    ) {
        when(it){
            true ->
                ErrorScreen(onClick = { walkViewModel.updateWeekRecordRefresh(true) })
            false ->
                Box(
                    modifier = Modifier.fillMaxSize()
                ){
                    Column(modifier = Modifier
                        .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        WeekContent(walkViewModel,navController,backBtnOn,modeChange)

                        Crossfade(
                            targetState = isLoading,
                            label = "",
                        ) { isLoading ->
                            when(isLoading){
                                true ->
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(design_home_bg),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ){
                                        Spacer(modifier = Modifier.padding(top = 60.dp))
                                        LoadingAnimation1(circleColor = design_intro_bg)
                                    }

                                else -> WalkListContent(walkViewModel, navController)
                            }
                        }
                    }

                    AnimatedVisibility(
                        visible =  toMonthCalendar,
                        enter = slideInVertically(
                            initialOffsetY = {-it}
                        ),
                        exit = slideOutVertically(
                            targetOffsetY = {-it}
                        )
                    ) {
                        MonthCalendar(walkViewModel = walkViewModel, sharedViewModel = sharedViewModel)
                    }

                    if (openBottomSheet){
                        ModalBottomSheet(
                            onDismissRequest = { onDissMiss(false) },
                            sheetState = bottomSheetState,
                            containerColor = Color.Transparent,
                            dragHandle = {}
                        ) {
                            Column {
                                CustomBottomSheet(viewModel = sharedViewModel,  title = "나의 반려동물을 선택하여 주세요.", btnText = "확인", onDismiss = { newValue -> onDissMiss(newValue)})
                                Spacer(modifier = Modifier
                                    .height(navigationBarHeight)
                                    .fillMaxWidth()
                                    .background(color = design_white))
                            }
                        }
                    }
                }
        }
    }
}

@Composable
fun WalkListContent(walkViewModel: WalkViewModel, navController: NavHostController) {

    val weekRecord by walkViewModel.weekRecord.collectAsState()

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.onPrimaryContainer)
    ){
        LazyColumn(
            state = rememberLazyListState(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 20.dp)
        ){
            items(weekRecord?.dailyLifeWalkList.orEmpty()) { walk ->
                WalkListContentItem(walk, walkViewModel, navController = navController)
            }
        }
    }
}

@Composable
fun WalkListContentItem(walk: DailyLifeWalk, walkViewModel: WalkViewModel, navController: NavHostController) {

    var lastClickTime by remember { mutableStateOf(System.currentTimeMillis()) }

    Column (
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
    ){
        Spacer(modifier = Modifier.padding(top = 20.dp))

        Row (
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = walk.petNm,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                    letterSpacing = (-0.8).sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Box (
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .border(1.dp, design_button_bg, RoundedCornerShape(10.dp))
                ){
                    Text(
                        text = walk.walkDptreDt,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 12.sp,
                        letterSpacing = (-0.6).sp,
                        color = design_button_bg,
                        modifier = Modifier.padding(vertical = 2.dp, horizontal = 7.dp)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(shape = CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = false)
                    ) {
                        val currentTime = System.currentTimeMillis()
                        if (currentTime - lastClickTime >= 300) {
                            lastClickTime = currentTime
                            walkViewModel.viewModelScope.launch {
                                walkViewModel.getDailyDetail(walk.schUnqNo)
                                walkViewModel.updateLastDaily(walk.schUnqNo)
                            }
                            navController.navigate(Screen.WalkDetailContent.route)
                        }
                    },
                contentAlignment = Alignment.Center
            ){
                Icon(painter = painterResource(id = R.drawable.arrow_next), contentDescription = "", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }

        Row (modifier = Modifier
            .padding(top = 16.dp, start = 20.dp, end = 20.dp, bottom = 20.dp)
            .fillMaxWidth()
            , horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
        ){
            Column (
                modifier= Modifier
                    .weight(1f)
            ){
                Text(
                    text = "산책자",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    letterSpacing = (-0.7).sp,
                    color = MaterialTheme.colorScheme.secondary
                )

                Text(
                    text = walk.runNcknm,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    letterSpacing = 0.sp,
                    modifier = Modifier.padding(top = 4.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier
                .size(1.dp, 40.dp)
                .background(color = MaterialTheme.colorScheme.onSecondaryContainer))

            Column (
                modifier= Modifier
                    .weight(1f)
            ){

                Text(
                    text = "산책 시간",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    letterSpacing = (-0.7).sp,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(start = 20.dp)
                )

                Text(
                    text = walk.runTime,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    letterSpacing = 0.sp,
                    modifier = Modifier
                        .padding(top = 4.dp, start = 20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )

            }

            Spacer(modifier = Modifier
                .size(1.dp, 40.dp)
                .background(color = MaterialTheme.colorScheme.onSecondaryContainer))

            Column (
                modifier= Modifier
                    .weight(1f)
            ){

                Text(
                    text = "산책 거리",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    letterSpacing = (-0.7).sp,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(start = 20.dp)
                )

                Row (modifier= Modifier.fillMaxWidth()){
                    Text(
                        text = walk.runDstnc.toString()+"m",
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                        letterSpacing = 0.sp,
                        modifier = Modifier
                            .padding(top = 4.dp, start = 20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

            }
        }
    }
}

@Composable
fun WeekContent(
    viewModel: WalkViewModel,
    navController: NavHostController,
    backBtnOn: (Boolean) -> Unit,
    modeChange: (Boolean) -> Unit
){

    val data = arrayListOf(
        Day(date = "", dayNm = "일", runCnt = 0),
        Day(date = "", dayNm = "월", runCnt = 0),
        Day(date = "", dayNm = "화", runCnt = 0),
        Day(date = "", dayNm = "수", runCnt = 0),
        Day(date = "", dayNm = "목", runCnt = 0),
        Day(date = "", dayNm = "금", runCnt = 0),
        Day(date = "", dayNm = "토", runCnt = 0),
    )

    val weekRecord by viewModel.weekRecord.collectAsState()
    val toDetail by viewModel.toDetail.collectAsState()

    val selectDay by viewModel.selectDay.collectAsState()
    val isThisWeek = getMonthAndWeek(getFormattedTodayDate()) == getMonthAndWeek(selectDay)
    val (month, week) = getMonthAndWeek(selectDay) ?: Pair(0,0)

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .background(color = design_select_btn_border)
    ){
        AnimatedVisibility(
            visible =  !toDetail,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {

            Column (Modifier.fillMaxWidth()){
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
                                    viewModel.updateSelectDay(subtract7Days(selectDay))
                                }
                        )

                        Text(
                            text = if (!isThisWeek){
                                "${month}월 ${week}주차 산책기록"
                            } else {
                                "이번주 산책기록"
                            },
                            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                            fontSize = 20.sp,
                            letterSpacing = (-1.0).sp,
                            color = design_white,
                            //modifier = Modifier.padding(top = 20.dp, start = 20.dp)
                        )

                        Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "", tint = design_white,
                            modifier = Modifier
                                .clickable {
                                    viewModel.updateSelectDay(add7Days(selectDay))
                                }
                        )
                    }

                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            modeChange(false)
                        }
                    ){
                        Icon(painter = painterResource(id = R.drawable.icon_sort_down),
                            contentDescription = "", tint = design_white
                        )

                        Text(
                            text = "타임라인", fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = 14.sp, letterSpacing = (-0.7).sp, color = design_white,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }


                Row (modifier = Modifier
                    .padding(top = 16.dp, start = 20.dp, end = 20.dp)
                    .fillMaxWidth()
                    , horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
                ){
                    Column (
                        modifier= Modifier
                            .weight(1f)
                    ){
                        Text(
                            text = "산책시간",
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            letterSpacing = (-0.7).sp,
                            color = design_C3D3EC
                        )

                        Text(
                            text = weekRecord?.runTime?:"00:00:00",
                            fontSize = 22.sp,
                            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                            letterSpacing = 0.sp,
                            modifier = Modifier.padding(top = 4.dp),
                            color = design_white
                        )
                    }

                    Spacer(modifier = Modifier
                        .size(1.dp, 46.dp)
                        .background(color = design_textFieldOutLine))

                    Column (
                        modifier= Modifier
                            .padding(start = 24.dp)
                            .weight(1f)
                    ){

                        Text(
                            text = "산책거리",
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            letterSpacing = (-0.7).sp,
                            color = design_C3D3EC
                        )

                        Row (modifier= Modifier.fillMaxWidth()){
                            Text(
                                text = metersToKilometers(weekRecord?.runDstnc?:0),
                                fontSize = 22.sp,
                                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                                letterSpacing = 0.sp,
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .alignByBaseline(),
                                color = design_white
                            )
                            Text(
                                text = "km",
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                                letterSpacing = 0.sp,
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .alignByBaseline(),
                                color = design_white
                            )
                        }

                    }
                }

                Spacer(modifier = Modifier.padding(top = 16.dp))

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    items(weekRecord?.dayList?: data){ data ->
                        WeekItem(data,isThisWeek)
                    }
                }

                Spacer(modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = design_76A1EF)
                )
            }

        }

        Text(
            text = "월간기록 보기",
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = 14.sp,
            letterSpacing = (-0.7).sp,
            color = design_white,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
                .clickable {
                    viewModel.updateToMonthCalendar(true)
                    backBtnOn(true)
                }
        )

        Spacer(modifier = Modifier.padding(bottom = 16.dp))
    }
}

@Composable
fun WeekItem(day : Day, isThisWeek:Boolean){

    val currentDate = getTodayDayOfWeek()

    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Icon(
            painter = if(day.runCnt==0){
                painterResource(id = R.drawable.attendance_default)
            } else{
                painterResource(id = R.drawable.attendance_active)
                  },
            contentDescription = "", tint = Color.Unspecified)
        Spacer(modifier = Modifier.padding(top = 5.dp))

        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(
                    color = if ((day.dayNm == currentDate) && isThisWeek) {
                        design_white
                    } else {
                        Color.Transparent
                    }
                ),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = day.dayNm,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                letterSpacing = (-0.6).sp,
                lineHeight = 12.sp,
                color = if( (day.dayNm == currentDate) && isThisWeek){
                    design_intro_bg
                }else{
                    design_C3D3EC
                }
            )
        }
    }
}

fun getMonthAndWeek(date: String): Pair<Int, Int>? {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    return try {
        val localDate = LocalDate.parse(date, dateFormatter)
        val weekFields = WeekFields.of(Locale.getDefault())
        Pair(localDate.monthValue, localDate.get(weekFields.weekOfMonth()))
    } catch (e: Exception) {
        null
    }
}

fun add7Days(dateString: String): String {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(dateString, dateFormatter)
    val addedDate = date.plusDays(7)
    return addedDate.format(dateFormatter)
}

fun subtract7Days(dateString: String): String {
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(dateString, dateFormatter)
    val subtractedDate = date.minusDays(7)
    return subtractedDate.format(dateFormatter)
}
