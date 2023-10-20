@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalPagerApi::class, ExperimentalFoundationApi::class
)

package kr.carepet.app.navi.screens.mainscreen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.carepet.app.navi.R
import kr.carepet.app.navi.Screen
import kr.carepet.app.navi.component.CircleImageTopBar
import kr.carepet.app.navi.component.CustomBottomSheet
import kr.carepet.app.navi.component.MonthCalendar
import kr.carepet.app.navi.component.WalkTimeNDis
import kr.carepet.app.navi.screens.walkscreen.WalkDetailContent
import kr.carepet.app.navi.ui.theme.design_76A1EF
import kr.carepet.app.navi.ui.theme.design_C3D3EC
import kr.carepet.app.navi.ui.theme.design_btn_border
import kr.carepet.app.navi.ui.theme.design_button_bg
import kr.carepet.app.navi.ui.theme.design_home_bg
import kr.carepet.app.navi.ui.theme.design_intro_bg
import kr.carepet.app.navi.ui.theme.design_login_text
import kr.carepet.app.navi.ui.theme.design_select_btn_bg
import kr.carepet.app.navi.ui.theme.design_select_btn_border
import kr.carepet.app.navi.ui.theme.design_select_btn_text
import kr.carepet.app.navi.ui.theme.design_shadow
import kr.carepet.app.navi.ui.theme.design_skip
import kr.carepet.app.navi.ui.theme.design_textFieldOutLine
import kr.carepet.app.navi.ui.theme.design_white
import kr.carepet.app.navi.viewmodel.HomeViewModel
import kr.carepet.app.navi.viewmodel.SharedViewModel
import kr.carepet.app.navi.viewmodel.WalkViewModel
import kr.carepet.data.daily.DailyLifeWalk
import kr.carepet.data.daily.Day
import kr.carepet.data.pet.PetDetailData


@Composable
fun WalkScreen(
    navController: NavHostController,
    walkViewModel: WalkViewModel,
    sharedViewModel: SharedViewModel,
    homeViewModel: HomeViewModel,
    backBtnOn: (Boolean) -> Unit,
    openBottomSheet: Boolean,
    onDissMiss:(Boolean) -> Unit
){

    val toDetail by walkViewModel.toDetail.collectAsState()
    val toMonthCalendar by walkViewModel.toMonthCalendar.collectAsState()
    val selectPet by sharedViewModel.selectPet.collectAsState()


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

    LaunchedEffect(key1 = selectPet){
        homeViewModel.getWeekRecord(selectPet?.ownrPetUnqNo ?: "",  getFormattedTodayDate())
    }

    SideEffect {
        if(toMonthCalendar){
            backBtnOn(true)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column(modifier = Modifier
            .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
        ){
            BackHandler (enabled = toDetail){
                walkViewModel.updateToDetail(false)
                backBtnOn(false)
            }

            BackHandler (enabled = toMonthCalendar){
                walkViewModel.updateToMonthCalendar(false)
                if (!toDetail){
                    backBtnOn(false)
                }
            }

            WeekContent(walkViewModel,navController,backBtnOn)

            WalkListContent(walkViewModel, navController)

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
            MonthCalendar(walkViewModel = walkViewModel, backBtnOn = backBtnOn)
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

@Composable
fun WalkListContent(walkViewModel: WalkViewModel, navController: NavHostController) {

    val weekRecord by walkViewModel.weekRecord.collectAsState()

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(color = design_home_bg)
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

    Column (
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .background(color = design_white, shape = RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = design_textFieldOutLine,
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
                    color = design_login_text
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
            Icon(painter = painterResource(id = R.drawable.arrow_next), contentDescription = "", tint = Color.Unspecified,
                modifier = Modifier.clickable {
                    walkViewModel.viewModelScope.launch{
                        walkViewModel.updateIsLoading(true)
                        walkViewModel.getDailyDetail(walk.schUnqNo)
                        walkViewModel.updateWalkListItem(walk)
                    }
                    navController.navigate(Screen.WalkDetailContent.route)
                })
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
                    color = design_skip
                )

                Text(
                    text = walk.runNcknm,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    letterSpacing = 0.sp,
                    modifier = Modifier.padding(top = 4.dp),
                    color = design_login_text
                )
            }

            Spacer(modifier = Modifier
                .size(1.dp, 40.dp)
                .background(color = design_textFieldOutLine))

            Column (
                modifier= Modifier
                    .weight(1f)
            ){

                Text(
                    text = "산책 시간",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    letterSpacing = (-0.7).sp,
                    color = design_skip,
                    modifier = Modifier.padding(start = 20.dp)
                )

                Text(
                    text = walk.runTime,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    letterSpacing = 0.sp,
                    modifier = Modifier
                        .padding(top = 4.dp, start = 20.dp),
                    color = design_login_text
                )

            }

            Spacer(modifier = Modifier
                .size(1.dp, 40.dp)
                .background(color = design_textFieldOutLine))

            Column (
                modifier= Modifier
                    .weight(1f)
            ){

                Text(
                    text = "산책 거리",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    letterSpacing = (-0.7).sp,
                    color = design_skip,
                    modifier = Modifier.padding(start = 20.dp)
                )

                Row (modifier= Modifier.fillMaxWidth()){
                    Text(
                        text = walk.runDstnc.toString()+"km",
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                        letterSpacing = 0.sp,
                        modifier = Modifier
                            .padding(top = 4.dp, start = 20.dp),
                        color = design_login_text
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
    backBtnOn: (Boolean) -> Unit
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

            Column {
                Text(
                    text = "이번주 산책 기록",
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    fontSize = 20.sp,
                    letterSpacing = (-1.0).sp,
                    color = design_white,
                    modifier = Modifier.padding(top = 20.dp, start = 20.dp)
                )

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
                                text = weekRecord?.runDstnc?.toString() ?: "",
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
                        WeekItem(data)
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
fun WeekItem(day : Day){

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
                    color = if (day.dayNm == currentDate) {
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
                color = if(day.dayNm == currentDate){
                    design_intro_bg
                }else{
                    design_C3D3EC
                }
            )
        }
    }
}

@Composable
fun WalkWithMap(viewModel: WalkViewModel, navController: NavHostController){

    var tipVisible by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = false
    )

    val petList by viewModel.petInfo.collectAsState()

    val isWalking by viewModel.isWalking.collectAsState()

    val sheetChange by viewModel.sheetChange.collectAsState()

    LaunchedEffect(Unit){
        delay(1000)
        tipVisible=true
    }

    BackHandler (enabled = bottomSheetState.isVisible){
        scope.launch { bottomSheetState.hide() }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ){

        ModalBottomSheetLayout(
            sheetContent = {
                when(sheetChange){
                    "select" ->
                        WalkBottomSheet(
                            title = "누구랑 산책할까요?",
                            btnText = "산책하기",
                            viewModel = viewModel,
                            bottomSheetState = bottomSheetState
                        )

                    "change" ->
                        WalkBottomSheet(
                            title =  "반려동물 변경",
                            btnText = "반려동물 변경",
                            viewModel = viewModel,
                            bottomSheetState = bottomSheetState
                        )

                    "end" ->
                        WalkBottomSheetEnd(viewModel= viewModel, bottomSheetState = bottomSheetState, navController = navController)

                    else -> null
                }
 },
            sheetState = bottomSheetState,
            sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            sheetGesturesEnabled = false
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(design_skip)
            ){
                //Text(text = "Map 들어갈 자리", modifier = Modifier.align(Alignment.Center))

            }

            Column (modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
            ){
                AnimatedVisibility(
                    visible =  tipVisible&&!isWalking,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                color = design_shadow,
                                offsetX = 20.dp,
                                offsetY = 20.dp,
                                spread = 3.dp,
                                blurRadius = 5.dp,
                                borderRadius = 20.dp
                            )
                            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                            .background(
                                color = design_white,
                                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                            )
                    ) {
                        Spacer(modifier = Modifier.padding(top = 16.dp))
                        Row (
                            modifier = Modifier
                                .padding(start = 20.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(painter = painterResource(id = R.drawable.icon_bulb), contentDescription = "", tint = Color.Unspecified)
                            Text(
                                text = "소소한 산책 TIP",
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                fontSize = 12.sp,
                                letterSpacing = (-0.6).sp,
                                color = design_skip,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.padding(top = 4.dp))
                        Text(
                            text = "슬개골 건강에는 비탈길, 계단보다 평지가 좋아요~",
                            fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                            fontSize = 14.sp,
                            letterSpacing = (-0.7).sp,
                            color = design_login_text,
                            modifier = Modifier.padding(start = 20.dp)
                        )
                        Spacer(modifier = Modifier.padding(top = 16.dp))
                    }
                }

                AnimatedVisibility(
                    visible =  isWalking,
                    enter = expandVertically(
                        animationSpec = tween(delayMillis = 500)
                    ),
                    exit = shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                color = design_shadow,
                                offsetX = 20.dp,
                                offsetY = 20.dp,
                                spread = 3.dp,
                                blurRadius = 5.dp,
                                borderRadius = 20.dp
                            )
                            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                            .background(
                                color = design_white,
                                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                            )
                    ){
                        Spacer(modifier = Modifier.padding(top = 16.dp))

                        Row (
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Row (
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                CircleImageTopBar(size = 40, imageUri = petList[0].petRprsImgAddr)

                                Column (
                                    modifier = Modifier
                                        .padding(start = 12.dp)
                                ){
                                    Text(
                                        text = "행복한 산책중" ,
                                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                        fontSize = 12.sp,
                                        letterSpacing = (-0.6).sp,
                                        color = design_skip
                                    )

                                    Text(
                                        text = "01:20:54" ,
                                        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                                        fontSize = 22.sp,
                                        letterSpacing = (-0.0).sp,
                                        color = design_login_text
                                    )
                                }
                            }

                            Text(
                                text = "반려동물 변경" ,
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                fontSize = 14.sp,
                                letterSpacing = (-0.6).sp,
                                textDecoration = TextDecoration.Underline,
                                color = design_skip,
                                modifier = Modifier.clickable {
                                    viewModel.updateSheetChange("change")
                                    scope.launch { bottomSheetState.show() }
                                }
                            )
                        }
                        Spacer(modifier = Modifier.padding(top = 16.dp))
                    }
                }

                Spacer(modifier = Modifier.padding(top = 16.dp))

                Box(
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .size(40.dp)
                        .background(color = design_white, shape = CircleShape)
                        .clip(shape = CircleShape)
                ){
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "", tint = design_login_text,
                        modifier = Modifier.align(Alignment.Center))
                }
            }

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ){
                Box(
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .size(40.dp)
                        .background(design_white, shape = RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ){
                    Icon(imageVector = Icons.Default.Place, contentDescription = "", tint = Color.Transparent)
                }

                Spacer(modifier = Modifier.padding(top = 8.dp))

                Row (
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(design_white, shape = RoundedCornerShape(10.dp))
                            .clip(RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ){
                        Icon(imageVector = Icons.Default.Email, contentDescription = "", tint = Color.Transparent)
                    }

                    Box(
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .size(40.dp)
                            .background(design_white, shape = RoundedCornerShape(10.dp))
                            .clip(RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ){
                        Icon(imageVector = Icons.Default.List, contentDescription = "", tint = Color.Transparent)
                    }
                }

                Spacer(modifier = Modifier.padding(top = 16.dp))

                Button(
                    onClick = {
                        if (!isWalking){
                            viewModel.updateSheetChange("select")
                            scope.launch { bottomSheetState.show() }
                        }else{
                            viewModel.updateSheetChange("end")
                            scope.launch { bottomSheetState.show() }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    colors = if (!isWalking){
                        ButtonDefaults.buttonColors(containerColor = design_button_bg)
                    }else{
                        ButtonDefaults.buttonColors(containerColor = design_btn_border)
                    }
                ){
                    Text(
                        text = if (!isWalking){
                            "산책하기"
                        }else{
                            "산책 종료"
                        },
                        color = design_white,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)))
                }

                Spacer(modifier = Modifier.padding(bottom = 20.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WalkBottomSheet(title:String, btnText:String, viewModel: WalkViewModel, bottomSheetState: ModalBottomSheetState){

    val petList by viewModel.petInfo.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    Log.d("LOG","왜 안나옴"+petList.toString())

    var isCheck by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    val isWalking by viewModel.isWalking.collectAsState()

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(design_white)
    ) {
        Spacer(modifier = Modifier.padding(top = 20.dp))

        Text(
            text = title,
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            fontSize = 20.sp,
            letterSpacing = (-1.0).sp,
            color = design_login_text,
            modifier = Modifier.padding(start = 20.dp)
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            contentPadding = PaddingValues(horizontal = 20.dp)
        ){
            items(petList){ petList ->
                Box (modifier = Modifier.padding(horizontal = 4.dp)){
                    WalkBottomSheetItem(viewModel = viewModel, petList = petList)
                }
            }
        }

        Spacer(modifier = Modifier.padding(top = 4.dp))

        Row (
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ){
            Row (modifier = Modifier
                .clickable { isCheck = !isCheck },
                verticalAlignment = Alignment.CenterVertically
            ){
                Checkbox(
                    checked = isCheck,
                    onCheckedChange = {isCheck = it},
                    colors = CheckboxDefaults.colors(
                        checkedColor = design_select_btn_text,
                        uncheckedColor = design_textFieldOutLine)
                )

                Text(
                    text = "계속 이 아이와 산책할게요",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    color = design_login_text,
                    letterSpacing = (-0.7).sp
                )
            }
        }

        Spacer(modifier = Modifier.padding(top = 4.dp))

        Button(
            onClick = {
                if(!isWalking){
                    scope.launch { bottomSheetState.hide() }
                    viewModel.updateIsWalking(true)
                }else{
                    scope.launch { bottomSheetState.hide() }
                }
                      },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = design_button_bg)
        ){
            Text(text = btnText, color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
        }

        Spacer(modifier = Modifier.padding(top = 20.dp))
    }
}

@Composable
fun WalkBottomSheetEnd(viewModel: WalkViewModel, bottomSheetState: ModalBottomSheetState, navController: NavHostController) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(design_white)
    ) {
        Spacer(modifier = Modifier.padding(top = 20.dp))

        Text(
            text = "산책을 종료할까요?",
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            fontSize = 20.sp,
            letterSpacing = (-1.0).sp,
            color = design_login_text,
            modifier = Modifier.padding(start = 20.dp)
        )

        // 데이터만 넘겨주기
        WalkTimeNDis()

        Spacer(modifier = Modifier.padding(top = 20.dp))

        Row (
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
        ){
            Button(
                onClick = {
                          navController.navigate(Screen.PostScreen.route){
                              popUpTo(Screen.WalkWithMap.route){inclusive=true}
                          }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(design_white),
                border = BorderStroke(1.dp, color = design_btn_border)
            ) {
                Text(text = "네,종료할게요", color = design_login_text,
                    fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
            }

            Spacer(modifier = Modifier.padding(horizontal = 4.dp))

            Button(
                onClick = { },
                modifier = Modifier
                    .height(48.dp)
                    .weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = design_button_bg)
            )
            {
                Text(text = "조금 더 할게요", color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
            }
        }

        Spacer(modifier = Modifier.padding(top = 20.dp))
    }
}

@Composable
fun WalkBottomSheetItem(viewModel: WalkViewModel, petList : PetDetailData){

    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp.dp -60.dp

    val petName:String = petList.petNm
    val imageUri:String? = petList.petRprsImgAddr

    val selectedPet by viewModel.selectPet.collectAsState()
    var isSeleted by remember { mutableStateOf(false) }

    Button(
        onClick = { isSeleted= !isSeleted },
        modifier = Modifier
            .size(width = screenWidth / 3, height = screenWidth / 3 - 9.dp)
            .shadow(ambientColor = design_shadow, elevation = 0.dp)
        ,
        shape = RoundedCornerShape(12.dp),
        colors = if(isSeleted) {
            ButtonDefaults.buttonColors(design_select_btn_bg)
        } else {
            ButtonDefaults.buttonColors(design_white)
        },
        border = if(isSeleted) {
            BorderStroke(1.dp, color = design_select_btn_text)
        } else {
            BorderStroke(1.dp, color = design_textFieldOutLine)
        },
        contentPadding = PaddingValues(start = 14.dp,end=14.dp),
        elevation = if(isSeleted){
            ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
        } else {
            ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
        }

    ) {
        Column (
            modifier= Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .border(shape = CircleShape, border = BorderStroke(3.dp, color = design_white))
                    //.shadow(elevation = 10.dp, shape = CircleShape, spotColor = Color.Gray)
                    .shadow(
                        color = design_shadow,
                        offsetY = 10.dp,
                        offsetX = 10.dp,
                        spread = 4.dp,
                        blurRadius = 3.dp,
                        borderRadius = 90.dp
                    )
                    .clip(CircleShape)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = "",
                    placeholder = painterResource(id = R.drawable.profile_default),
                    error= painterResource(id = R.drawable.profile_default),
                    modifier= Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

            }

            Spacer(modifier = Modifier.padding(top = 8.dp))

            Text(
                text = petName,
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                fontSize = 16.sp,
                letterSpacing = (-0.8).sp,
                color = design_login_text
            )
        }
    }
}


data class week(val string: String, val int: Int)

