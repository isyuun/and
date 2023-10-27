@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class, ExperimentalMaterialApi::class
)

package kr.carepet.app.navi.screens.mainscreen

import android.annotation.SuppressLint
import android.graphics.BlurMaskFilter
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import kr.carepet.app.navi.R
import kr.carepet.app.navi.Screen
import kr.carepet.app.navi.component.CustomBottomSheet
import kr.carepet.app.navi.component.CustomDialog
import kr.carepet.app.navi.component.LoadingAnimation1
import kr.carepet.app.navi.ui.theme.design_btn_border
import kr.carepet.app.navi.ui.theme.design_grad_end
import kr.carepet.app.navi.ui.theme.design_icon_5E6D7B
import kr.carepet.app.navi.ui.theme.design_icon_bg
import kr.carepet.app.navi.ui.theme.design_icon_distance_bg
import kr.carepet.app.navi.ui.theme.design_icon_time_bg
import kr.carepet.app.navi.ui.theme.design_intro_bg
import kr.carepet.app.navi.ui.theme.design_login_text
import kr.carepet.app.navi.ui.theme.design_select_btn_bg
import kr.carepet.app.navi.ui.theme.design_select_btn_text
import kr.carepet.app.navi.ui.theme.design_shadow
import kr.carepet.app.navi.ui.theme.design_skip
import kr.carepet.app.navi.ui.theme.design_textFieldOutLine
import kr.carepet.app.navi.ui.theme.design_weather_1
import kr.carepet.app.navi.ui.theme.design_white
import kr.carepet.app.navi.viewmodel.HomeViewModel
import kr.carepet.app.navi.viewmodel.SharedViewModel
import kr.carepet.data.pet.PetDetailData
import kr.carepet.singleton.G
import kr.carepet.util.Log
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue


@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel,
    sharedViewModel: SharedViewModel,
    backChange: (Boolean) -> Unit,
    openBottomSheet: Boolean,
    onDissMiss: (Boolean) -> Unit,
    bottomNavController: NavHostController,
    showDialog: Boolean,
    showDialogChange : (Boolean) -> Unit
){
    val currentPetInfo by viewModel.currentPetInfo.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    //petInfo.size가 갱신되기 전에, 뷰가 만들어지면서 에러발생
    var pagerState = rememberPagerState(pageCount = { currentPetInfo.size })

    val selectedPet by sharedViewModel.selectPet.collectAsState()

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val context = LocalContext.current
    val density = LocalDensity.current.density
    val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")

    val navigationBarHeight = if (resourceId > 0) {
        (context.resources.getDimensionPixelSize(resourceId) / density).dp
    } else {
        0.dp
    }

    LaunchedEffect(key1 = pagerState.currentPage, key2 = currentPetInfo){
        if (currentPetInfo.isNotEmpty()){
            viewModel.getWeekRecord(currentPetInfo[pagerState.currentPage].ownrPetUnqNo, getFormattedTodayDate())
            viewModel.updateSeletedPet(currentPetInfo[pagerState.currentPage])

            sharedViewModel.updateSelectPetTemp(currentPetInfo[pagerState.currentPage])
        }
    }

    LaunchedEffect(key1 = selectedPet){
        val index = currentPetInfo.indexOf(selectedPet)

        pagerState.animateScrollToPage(index)
    }

    LaunchedEffect(key1 = G.toPost){
        if(G.toPost){
            navController.navigate(Screen.PostScreen.route)
        }
    }

    SideEffect {
        backChange(false)
    }

    AnimatedVisibility(
        visible = showDialog,
        enter = scaleIn(tween(durationMillis = 1000)),
        exit = scaleOut(tween(durationMillis = 1000))
    ) {
        CustomDialog(
            onDismiss = { newValue -> showDialogChange(newValue) },
            navController = navController,
            confirm = stringResource(R.string.dialog_regist),
            dismiss = stringResource(R.string.dialog_later),
            title = stringResource(R.string.dialog_any_pet),
            text = stringResource(R.string.dialog_sub_regist)
        )
    }

    Column (modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .background(color = design_select_btn_bg)
    ){
        ProfileContent(viewModel = viewModel, pagerState = pagerState, sharedViewModel = sharedViewModel, navController = navController)

        Spacer(modifier = Modifier.padding(top = 40.dp))

        WalkInfoContent(viewModel, pagerState = pagerState)

        Spacer(modifier = Modifier.padding(top = 40.dp))

        StoryContent()

        Spacer(modifier = Modifier.padding(top = 16.dp))

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Button(
                modifier = Modifier
                    .width(160.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(100.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = design_btn_border),
                onClick = {
                    sharedViewModel.updateMoreStoryClick(true)
                    bottomNavController.navigate("commu") {
                        bottomNavController.graph.startDestinationRoute?.let {
                            popUpTo(it) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            ) {
                Text(
                    text = stringResource(R.string.home_story_more),
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp,
                    letterSpacing = (-0.7).sp,
                    color = design_white
                )
            }
        }

        Spacer(modifier = Modifier.padding(top = 40.dp))

        BottomInfo()

        Spacer(modifier = Modifier.padding(top = 80.dp))

        if (openBottomSheet){
            ModalBottomSheet(
                onDismissRequest = { onDissMiss(false) },
                sheetState = bottomSheetState,
                containerColor = Color.Transparent,
                dragHandle = {}
            ) {
                Column {
                    CustomBottomSheet(viewModel = sharedViewModel,  title = stringResource(R.string.select_pet), btnText = stringResource(R.string.confirm), onDismiss = { newValue -> onDissMiss(newValue)})
                    Spacer(modifier = Modifier
                        .height(navigationBarHeight)
                        .fillMaxWidth()
                        .background(color = design_white))
                }
            }
        }
    }


    //if(!isLoading){
    //
    //
    //
    //}else{
    //    CircularProgressAnimated()
    //}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileContent(
    viewModel: HomeViewModel,
    sharedViewModel: SharedViewModel,
    pagerState: PagerState,
    navController : NavHostController
){
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val isLoading by viewModel.isLoading.collectAsState()

    val context = LocalContext.current
    val density = LocalDensity.current.density
    val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")

    val navigationBarHeight = if (resourceId > 0) {
        (context.resources.getDimensionPixelSize(resourceId) / density).dp
    } else {
        0.dp
    }

    val weatherColor = design_weather_1 // 나중에 날씨받아와서 when으로 구성
    val wtCont1 = "맑음"
    val wtCont2 = "미세먼지 좋음"
    val wtCont3 = "구의동"
    val dustIcon= R.drawable.dust_good

    val currentPetInfo by viewModel.currentPetInfo.collectAsState()

    // run catching
    val petKindNm = runCatching {currentPetInfo[pagerState.currentPage].petKindNm}.getOrElse {""}
    val petNm = runCatching { currentPetInfo[pagerState.currentPage].petNm }.getOrElse { "" }
    val petAge = runCatching { currentPetInfo[pagerState.currentPage].age }.getOrElse { "" }
    val sexTypNm = runCatching { currentPetInfo[pagerState.currentPage].sexTypNm }.getOrElse { "" }
    val wghtVl = runCatching { currentPetInfo[pagerState.currentPage].wghtVl }.getOrElse { "" }

    Column (modifier = Modifier
        .fillMaxWidth()
        .shadow(
            color = design_shadow,
            offsetX = 0.dp,
            offsetY = 10.dp,
            spread = 3.dp,
            blurRadius = 5.dp,
            borderRadius = 50.dp
        )
        .clip(shape = RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp))
        .background(design_white, RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box {
            Image(
                painter = painterResource(id = R.drawable.main_pattern),
                contentDescription = "", modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds)

            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row (modifier = Modifier
                    .padding(top = 20.dp)
                    .wrapContentWidth()
                    .height(26.dp)
                    .clip(shape = RoundedCornerShape(50.dp))
                    .background(color = weatherColor, shape = RoundedCornerShape(50.dp)),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
                ){
                    Text(text = wtCont1,
                        fontSize = 12.sp, fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                        color = design_white, letterSpacing = (-0.6).sp,
                        modifier = Modifier.padding(start = 16.dp),
                        textAlign = TextAlign.Center)

                    Spacer(modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(2.dp, 8.dp)
                        .background(color = design_white))

                    Icon(painter = painterResource(id = dustIcon), contentDescription = "",
                        modifier = Modifier.padding(end = 2.dp), tint = Color.Unspecified)

                    Text(text = wtCont2,
                        fontSize = 12.sp, fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                        color = design_white, letterSpacing = (-0.6).sp,
                        textAlign = TextAlign.Center)

                    Spacer(modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(2.dp, 8.dp)
                        .background(color = design_white))

                    Text(text = wtCont3,
                        fontSize = 12.sp, fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                        color = design_white, letterSpacing = (-0.6).sp,
                        modifier = Modifier.padding(end = 16.dp),
                        textAlign = TextAlign.Center)


                }

                Spacer(modifier = Modifier.padding(top = 16.dp))

                Crossfade(
                    targetState = isLoading,
                    label = "" ,
                    animationSpec = tween(durationMillis = 700)
                ) { isLoading ->
                    when(isLoading){
                        true ->
                            Box (modifier = Modifier.fillMaxWidth().height(180.dp),
                                contentAlignment = Alignment.Center
                            ){
                                LoadingAnimation1(circleColor = design_intro_bg)
                            }
                        false ->
                            HorizontalPager(
                                modifier = Modifier
                                    .fillMaxSize(),
                                state = pagerState,
                                beyondBoundsPageCount = 1,
                                flingBehavior = PagerDefaults.flingBehavior(
                                    state = pagerState, snapVelocityThreshold = 20.dp)
                            ) { page ->
                                val isSelected = page == pagerState.currentPage // 선택된 페이지 여부를 확인

                                // 선택된 아이템의 Z-index를 높게 설정
                                val zIndexModifier = if (isSelected) Modifier.zIndex(1f) else Modifier

                                Box(Modifier
                                    .then(zIndexModifier)
                                    .graphicsLayer {
                                        val pageOffset = pagerState.calculateCurrentOffsetForPage(page)
                                        // translate the contents by the size of the page, to prevent the pages from sliding in from left or right and stays in the center
                                        translationX = pageOffset * size.width / 4 * 3
                                        // apply an alpha to fade the current page in and the old page out
                                        alpha = 1 - pageOffset.absoluteValue / 2
                                        scaleX = 1 - pageOffset.absoluteValue / 4
                                        scaleY = 1 - pageOffset.absoluteValue / 4
                                    }
                                    .fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {

                                    CircleImageHome(size = 180, imageUri = currentPetInfo[page].petRprsImgAddr, page, pagerState)
                                }
                            }
                    }
                }


                Spacer(modifier = Modifier.padding(top = 16.dp))

                Text(
                    text = petKindNm,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    letterSpacing = (-0.7).sp,
                    color = design_skip
                )

                Text(
                    text = petNm,
                    fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    letterSpacing = (-0.7).sp,
                    color = design_login_text
                )

                Spacer(modifier = Modifier.padding(top = 16.dp))


                Row (modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){

                    Box (
                        modifier= Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(color = design_icon_bg),
                        contentAlignment = Alignment.Center
                    ){
                        Icon(painter = painterResource(id = R.drawable.icon_age), contentDescription = "", tint = Color.Unspecified)
                    }

                    Text(
                        text = if (petAge==""){
                            stringResource(R.string.age_unknown)
                        }else{
                            petAge
                        },
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp,
                        color = design_login_text,
                        modifier = Modifier.padding(start = 8.dp)
                    )

                    Spacer(modifier = Modifier.padding(start = 20.dp))

                    Box (
                        modifier= Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(color = design_icon_bg),
                        contentAlignment = Alignment.Center
                    ){
                        Icon(painter = painterResource(id = R.drawable.icon_gender), contentDescription = "", tint = Color.Unspecified)
                    }

                    Text(
                        text = sexTypNm ?: stringResource(R.string.type_uk),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp,
                        color = design_login_text,
                        modifier = Modifier.padding(start = 8.dp)
                    )

                    Spacer(modifier = Modifier.padding(start = 20.dp))

                    Box (
                        modifier= Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(color = design_icon_bg),
                        contentAlignment = Alignment.Center
                    ){
                        Icon(painter = painterResource(id = R.drawable.icon_weight), contentDescription = "", tint = Color.Unspecified)
                    }

                    Text(
                        text = "${wghtVl}kg",
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp,
                        color = design_login_text,
                        modifier = Modifier.padding(start = 8.dp)
                    )

                }
            }
        }


        Divider(modifier= Modifier
            .fillMaxWidth()
            .padding(start = 40.dp, end = 40.dp, top = 16.dp, bottom = 18.dp),
            thickness = 1.dp, color = design_textFieldOutLine)


        Row (
            modifier=Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            if(currentPetInfo.size>=2){
                CircleImageOffset(imageUri = currentPetInfo[1].petRprsImgAddr, index = 1)
            }
            if(currentPetInfo.size>=1 && currentPetInfo[0].sexTypNm != ""){
                CircleImageOffset(imageUri = currentPetInfo[0].petRprsImgAddr, index = 0)
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(shape = CircleShape, border = BorderStroke(2.dp, color = design_white))
                    .shadow(elevation = 10.dp, shape = CircleShape, spotColor = Color.Gray)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(color = design_icon_5E6D7B),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(painter = painterResource(id = R.drawable.icon_animallist), contentDescription = "", tint = Color.Unspecified)
                }
            }

            Text(
                text = stringResource(R.string.pet_list),
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 12.sp, letterSpacing = (-0.6).sp,
                textDecoration = TextDecoration.Underline,
                color = design_skip,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .clickable {
                        openBottomSheet = true
                    }
            )
        }

        Spacer(modifier = Modifier.padding(bottom = 16.dp))
    }// Column

    if (openBottomSheet){
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = bottomSheetState,
            containerColor = Color.Transparent,
            dragHandle = null
        ) {
            Column {
                BottomSheetContent(viewModel = viewModel, navController = navController) { newValue ->
                    openBottomSheet = newValue
                }
                Spacer(modifier = Modifier
                    .height(navigationBarHeight)
                    .fillMaxWidth()
                    .background(color = design_white))
            }
        }
    }
}
@OptIn(ExperimentalFoundationApi::class)
fun PagerState.calculateCurrentOffsetForPage(page: Int): Float {
    return (currentPage - page) + currentPageOffsetFraction
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WalkInfoContent(viewModel: HomeViewModel, pagerState: PagerState){

    val currentPetInfo by viewModel.currentPetInfo.collectAsState()
    val weekRecord by viewModel.weekRecord.collectAsState()

    val petName = runCatching {
        currentPetInfo[pagerState.currentPage].petNm
    }.getOrElse {
        ""
    }

    Box (
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .background(color = Color.Transparent)
    ){
        Column (modifier = Modifier.fillMaxSize()){
            
            Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = petName,
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    fontSize = 20.sp,
                    letterSpacing = (-1.0).sp,
                    color = design_login_text
                    )
                Text(
                    text = stringResource(R.string.with_walk),
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 20.sp,
                    letterSpacing = (-1.0).sp,
                    color = design_login_text
                )
            }

            Row (modifier = Modifier
                .padding(top = 16.dp)
                .shadow(
                    color = design_shadow,
                    borderRadius = 20.dp,
                    offsetX = 8.dp,
                    offsetY = 8.dp,
                    spread = 3.dp,
                    blurRadius = 5.dp
                )
                .fillMaxWidth()
                .height(150.dp)
                .clip(shape = RoundedCornerShape(20.dp))
                .background(color = design_white, shape = RoundedCornerShape(20.dp))
                , horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
            ){
                Column (
                    modifier= Modifier
                        .padding(start = 24.dp)
                        .weight(1f)
                ){
                    Box (
                        modifier= Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(color = design_icon_time_bg),
                        contentAlignment = Alignment.Center
                    ){
                        Icon(painter = painterResource(id = R.drawable.icon_time), contentDescription = "", tint = Color.Unspecified)
                    }

                    Text(
                        text = stringResource(R.string.total_walk_time),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp,
                        modifier = Modifier.padding(top = 20.dp),
                        color = design_login_text
                    )

                    Text(
                        text = weekRecord?.runTime ?: stringResource(R.string.zero_min),
                        fontSize = 22.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                        letterSpacing = 0.sp,
                        modifier = Modifier.padding(top = 4.dp),
                        color = design_login_text
                    )
                }

                Spacer(modifier = Modifier
                    .size(1.dp, 110.dp)
                    .background(color = design_textFieldOutLine))

                Column (
                    modifier= Modifier
                        .padding(start = 24.dp)
                        .weight(1f)
                ){
                    Box (
                        modifier= Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(color = design_icon_distance_bg),
                        contentAlignment = Alignment.Center
                    ){
                        Icon(painter = painterResource(id = R.drawable.icon_distance), contentDescription = "", tint = Color.Unspecified)
                    }

                    Text(
                        text = stringResource(R.string.total_walk_distance),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp,
                        modifier = Modifier.padding(top = 20.dp),
                        color = design_login_text
                    )

                    Row (modifier=Modifier.fillMaxWidth()){
                        Text(
                            text = weekRecord?.runDstnc?.toString() ?: "",
                            fontSize = 22.sp,
                            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                            letterSpacing = 0.sp,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .alignByBaseline(),
                            color = design_login_text
                        )
                        Text(
                            text = "km",
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                            letterSpacing = 0.sp,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .alignByBaseline(),
                            color = design_login_text
                        )
                    }

                }
            }

        }
    }
}

@Composable
fun StoryContent(){
    val dummyList = arrayListOf(
        StoryList("","행복한 산책 했어요","핑키","37","22"),
        StoryList("","꿀잠자는 저희 강아지...","강레오","28","3"),
        StoryList("","행복한 산책 했어요","핑키","37","22"),
        StoryList("","꿀잠자는 저희 강아지...","강레오","28","3")

    )

    Box (
        modifier = Modifier
            .background(color = Color.Transparent)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.real_time),
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    fontSize = 20.sp,
                    letterSpacing = (-1.0).sp,
                    color = design_login_text,
                    modifier = Modifier.padding(start = 20.dp)
                )
                Text(
                    text = stringResource(id = R.string.title_story),
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 20.sp,
                    letterSpacing = (-1.0).sp,
                    color = design_login_text,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.padding(top = 16.dp))
            
            //StoryItem()
            LazyRow(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp)
            ) {
                items(dummyList) { dummyList ->
                    StoryItem(data = dummyList)
                }
            }
        }
    }
}

@Composable
fun StoryItem(data:StoryList){

    var sizeImage by remember { mutableStateOf(IntSize.Zero) }

    val gradient = Brush.verticalGradient(
        colors = listOf(Color.Transparent, design_grad_end),
        startY = sizeImage.height.toFloat()/5*3,
        endY = sizeImage.height.toFloat()
    )

    val imageUri= ""

    Box(
        modifier = Modifier
            .size(width = 200.dp, height = 280.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .onGloballyPositioned { sizeImage = it.size }
            .clickable { }
    ){
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUri)
                .crossfade(true)
                .build(),
            contentDescription = "",
            placeholder = painterResource(id = R.drawable.dog4),
            error= painterResource(id = R.drawable.dog4),
            modifier= Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(modifier = Modifier
            .matchParentSize()
            .background(gradient))

        Column (modifier= Modifier
            .width(160.dp)
            .wrapContentHeight()
            .align(Alignment.BottomCenter)
            .padding(bottom = 16.dp)
            .background(color = Color.Transparent)
        ){
            Text(
                text = data.title,
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                fontSize = 18.sp,
                letterSpacing = (-0.9).sp,
                color = design_white,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = data.petName,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 14.sp,
                letterSpacing = (-0.7).sp,
                color = design_white,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.padding(bottom = 16.dp))

            Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){

                Icon(painter = painterResource(id = R.drawable.icon_like), contentDescription = "", tint = Color.Unspecified)

                Text(
                    text = data.likeCount,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 12.sp,
                    letterSpacing = (-0.6).sp,
                    color = design_white,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 4.dp)
                )

                Spacer(modifier = Modifier.padding(start = 16.dp))

                Icon(painter = painterResource(id = R.drawable.icon_comment), contentDescription = "", tint = Color.Unspecified)

                Text(
                    text = data.commentCount,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 12.sp,
                    letterSpacing = (-0.6).sp,
                    color = design_white,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 4.dp)
                )

            }

        }
    }

}

@Composable
fun BottomSheetContent(
    viewModel: HomeViewModel,
    navController: NavHostController,
    onDisMiss: (Boolean) -> Unit ){
    
    val originPetList by viewModel.petInfo.collectAsState()
    val index by viewModel.petListSelectIndex.collectAsState()

    val petList = originPetList.sortedBy {
        when(it.mngrType){
            "M" -> 1
            "I" -> 2
            "C" -> 3
            else -> 4
        }
    }

    LaunchedEffect(Unit){
        viewModel.updateSelectPetManage(petList[0])
        viewModel.updatePetListSelectIndex("0")
    }



    Column (
        modifier = Modifier
            .fillMaxWidth()
            .background(design_white)
    ){
        Spacer(modifier = Modifier.padding(top = 20.dp))

        Text(
            text = stringResource(id = R.string.pet_list),
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            fontSize = 20.sp,
            letterSpacing = (-1.0).sp,
            color = design_login_text,
            modifier = Modifier.padding(start = 20.dp)
            )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        if (petList[0].petInfoUnqNo!=0){
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                itemsIndexed(petList){ index, petList ->
                    BottomSheetItem(viewModel = viewModel, petList, index)
                }
            }
        }else{
            Button(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = design_white
                ),
                onClick = { },
                border = BorderStroke(1.dp, design_textFieldOutLine)
            ) {
                Text(text = stringResource(R.string.regist_new_pet), color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.padding(top = 16.dp))

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Button(
                modifier = Modifier
                    .width(160.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(100.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = design_btn_border),
                onClick = {
                    onDisMiss(false)
                    navController.navigate(Screen.AddPetScreen.route)
                }
            ) {
                Text(
                    text = stringResource(R.string.add_pet),
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp,
                    letterSpacing = (-0.7).sp,
                    color = design_white
                )
            }
            
            Spacer(modifier = Modifier.padding(start = 8.dp))

            Button(
                modifier = Modifier
                    .width(160.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(100.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = design_white),
                border = BorderStroke(1.dp, design_btn_border),
                onClick = {
                    onDisMiss(false)
                    navController.navigate("petProfileScreen/${index.toString()}")
                }
            ) {
                Text(
                    text = stringResource(R.string.manage_pet),
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp,
                    letterSpacing = (-0.7).sp,
                    color = design_login_text
                )
            }
        }

        Spacer(modifier = Modifier.padding(top = 20.dp))
    }//col
}

@Composable
fun BottomSheetItem(viewModel : HomeViewModel ,petList: PetDetailData, index: Int){

    val petName:String = petList.petNm
    val imageUri:String? = petList.petRprsImgAddr
    val petKind:String = petList.petKindNm
    val petAge:String = if(petList.petBrthYmd == stringResource(id = R.string.age_unknown)){
        stringResource(id = R.string.age_unknown)
    }else{
        viewModel.changeBirth(petList.petBrthYmd)
    }
    val petGender:String = petList.sexTypNm?:""

    val selectPet by viewModel.selectPetManage.collectAsState()

    Button(
        onClick = {
            viewModel.updateSelectPetManage(petList)
            viewModel.updatePetListSelectIndex(index.toString())
                  },
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .shadow(ambientColor = design_shadow, elevation = 0.dp)
        ,
        shape = RoundedCornerShape(12.dp),
        colors = if(petList == selectPet) {
            ButtonDefaults.buttonColors(design_select_btn_bg)
        } else {
            ButtonDefaults.buttonColors(design_white)
        },
        border = if(petList == selectPet) {
            BorderStroke(1.dp, color = design_select_btn_text)
        } else {
            BorderStroke(1.dp, color = design_textFieldOutLine)
        },
        contentPadding = PaddingValues(start = 14.dp,end=14.dp),
        elevation = if(petList == selectPet){
            ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
        } else {
            ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
        }

    ) {
        Row (
            modifier= Modifier
                .padding(vertical = 14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .border(shape = CircleShape, border = BorderStroke(3.dp, color = design_white))
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

            Column (
                modifier = Modifier
                    .padding(start = 8.dp)
                    .background(Color.Transparent)
            ){
                Text(
                    text = petName,
                    fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                    fontSize = 16.sp,
                    letterSpacing = (-0.8).sp,
                    color = design_login_text
                )

                Row (
                    modifier = Modifier
                        .background(Color.Transparent),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = petKind,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 12.sp,
                        letterSpacing = (-0.8).sp,
                        color = design_skip
                    )

                    Spacer(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(2.dp, 8.dp)
                            .background(color = design_skip)
                    )

                    Text(
                        text = petAge,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 12.sp,
                        letterSpacing = (-0.8).sp,
                        color = design_skip
                    )

                    Spacer(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(2.dp, 8.dp)
                            .background(color = design_skip)
                    )

                    Text(
                        text = petGender,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 12.sp,
                        letterSpacing = (-0.8).sp,
                        color = design_skip
                    )

                }

            }
        }
    }
}


@Composable
fun BottomInfo(){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .background(color = Color.Transparent)
    ){
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = stringResource(R.string.terms_of_use),
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                letterSpacing = (-0.6).sp,
                color = design_skip
            )

            Spacer(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(2.dp, 8.dp)
                    .background(color = design_skip)
            )

            Text(
                text = stringResource(R.string.privacy_policy),
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                letterSpacing = (-0.6).sp,
                color = design_skip
            )

            Spacer(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(2.dp, 8.dp)
                    .background(color = design_skip)
            )

            Text(
                text = stringResource(R.string.introduc_corp),
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                letterSpacing = (-0.6).sp,
                color = design_skip
            )
        }
        
        Spacer(modifier = Modifier.padding(bottom = 16.dp))

        Text(
            text = stringResource(R.string.corp_info),
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            letterSpacing = (-0.6).sp,
            color = design_skip
        )
    }
}


@Composable
fun CircleImage(size: Int, imageUri: String?){

    Box(
        modifier = Modifier
            .size(size.dp)
            .border(shape = CircleShape, border = BorderStroke(5.dp, color = design_white))
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
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CircleImageHome(size: Int, imageUri: String?, page: Int, pagerState: PagerState){

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .size(size.dp)
            .border(shape = CircleShape, border = BorderStroke(5.dp, color = design_white))
            .shadow(
                color = design_shadow,
                offsetY = 10.dp,
                offsetX = 10.dp,
                spread = 4.dp,
                blurRadius = 3.dp,
                borderRadius = 90.dp
            )
            .clip(CircleShape)
            .clickable {
                scope.launch {
                    pagerState.animateScrollToPage(
                        page = page,
                        animationSpec = tween(durationMillis = 600)
                    )
                }
            }
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
}
@Composable
fun CircleImageOffset(imageUri: String?, index: Int){

    Box(
        modifier = Modifier
            .offset(10 * (index + 1).dp)
            .size(40.dp)
            .border(shape = CircleShape, border = BorderStroke(2.dp, color = design_white))
            .shadow(elevation = 10.dp, shape = CircleShape, spotColor = Color.Gray)
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
}


@Composable
fun CircularProgressAnimated(){

    val progressValue = 1.0f
    val infiniteTransition = rememberInfiniteTransition()

    val progressAnimationValue by infiniteTransition.animateFloat(
        initialValue = 0.0f,
        targetValue = progressValue,animationSpec = infiniteRepeatable(animation = tween(900)))

    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = design_white), contentAlignment = Alignment.Center){
        CircularProgressIndicator(progress = progressAnimationValue, color = design_intro_bg)
    }

}


fun Modifier.shadow(
    color: Color = Color.Black,
    borderRadius: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp,
    spread: Dp = 0f.dp,
    modifier: Modifier = Modifier
) = this.then(
    modifier.drawBehind {
        this.drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            val spreadPixel = spread.toPx()
            val leftPixel = (0f - spreadPixel) + offsetX.toPx()
            val topPixel = (0f - spreadPixel) + offsetY.toPx()
            val rightPixel = (this.size.width + spreadPixel)
            val bottomPixel = (this.size.height + spreadPixel)

            if (blurRadius != 0.dp) {
                frameworkPaint.maskFilter =
                    (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
            }

            frameworkPaint.color = color.toArgb()
            it.drawRoundRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                radiusX = borderRadius.toPx(),
                radiusY = borderRadius.toPx(),
                paint
            )
        }
    }
)

fun getFormattedTodayDate(): String {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return currentDate.format(formatter)
}

fun getTodayDayOfWeek(): String {
    val today = LocalDate.now()
    val dayOfWeek = today.dayOfWeek
    val dayOfWeekText = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    return dayOfWeekText
}

fun getFormattedDate(): String {
    val pattern = "yyyy.MM.dd" // 원하는 날짜 포맷 지정
    val simpleDateFormat = SimpleDateFormat(pattern)
    val currentDate = Date()

    return simpleDateFormat.format(currentDate)
}

data class StoryList(val imageUri: Any?,val title: String, val petName: String, val likeCount: String, val commentCount: String)
