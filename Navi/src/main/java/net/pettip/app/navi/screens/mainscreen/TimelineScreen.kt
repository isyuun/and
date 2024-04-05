package net.pettip.app.navi.screens.mainscreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.Screen
import net.pettip.app.navi.component.CircleImageTopBar
import net.pettip.app.navi.component.ErrorScreen
import net.pettip.app.navi.component.LoadingAnimation1
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_login_text
import net.pettip.app.navi.ui.theme.design_select_btn_bg
import net.pettip.app.navi.ui.theme.design_select_btn_text
import net.pettip.app.navi.ui.theme.design_shadow
import net.pettip.app.navi.viewmodel.SharedViewModel
import net.pettip.app.navi.viewmodel.WalkViewModel
import net.pettip.data.daily.LifeTimeLineItem
import net.pettip.data.pet.PetDetailData
import net.pettip.singleton.G
import net.pettip.singleton.MySharedPreference

/**
 * @Project     : PetTip-Android
 * @FileName    : TimelineScreen
 * @Date        : 2023-12-13
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screens.mainscreen
 * @see net.pettip.app.navi.screens.mainscreen.TimelineScreen
 */

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun TimelineScreen(
    viewModel: WalkViewModel,
    isSearching: Boolean,
    dismiss: (Boolean) -> Unit,
    navController: NavHostController,
    modeChange: (Boolean)->Unit,
    sharedViewModel: SharedViewModel
) {

    val petList by viewModel.petInfo.collectAsState()
    val selectedPet by viewModel.selectedPet.collectAsState()
    val timeLineList by viewModel.timeLineList.collectAsState()
    val state = rememberLazyListState()
    val dateLazyState = rememberLazyListState()
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f,10f),0f)
    val sortType by viewModel.sortType.collectAsState()
    val refresh by viewModel.timeLineRefresh.collectAsState()
    val preUserId by viewModel.preUserId.collectAsState()
    val dailyLifeTimeLineList by viewModel.dailyLifeTimeLineList.collectAsState()
    val walkUpload by sharedViewModel.walkUpload.collectAsState()


    var isLoading by rememberSaveable { mutableStateOf(false) }
    var typeChange by rememberSaveable{ mutableStateOf(false) }
    var isError by rememberSaveable{ mutableStateOf(false) }
    var isLoadingNextPage by remember { mutableStateOf(false) }

    var previousSortType: String? by remember { mutableStateOf(null) }
    var previousSelectedPet: MutableList<PetDetailData>? by remember { mutableStateOf(null) }

    LaunchedEffect(key1= walkUpload){
        if (walkUpload){
            sharedViewModel.updateWalkUpload(false)
            viewModel.updateTimeLineRefresh(true)
        }
    }

    LaunchedEffect(dateLazyState.canScrollForward) {
        if (!dateLazyState.canScrollForward && !isLoadingNextPage && dailyLifeTimeLineList != null){
            if (timeLineList?.data?.paginate?.existNextPage == true){
                isLoadingNextPage = true
                viewModel.addTimeLinePage()
                viewModel.viewModelScope.launch {
                    val result = viewModel.getTimeLineList()
                    if (result){
                        isLoadingNextPage = false
                    }else{
                        isLoadingNextPage = false
                        viewModel.subTimeLinePage()
                    }
                }
            }
        }
    }

    SideEffect {
        if (preUserId != MySharedPreference.getUserId() && preUserId != ""){
            viewModel.updateTimeLineRefresh(true)
            viewModel.updatePreUserId(MySharedPreference.getUserId())
        }else{
            viewModel.updatePreUserId(MySharedPreference.getUserId())
        }
    }

    LaunchedEffect(key1 = G.toPost){
        if(G.toPost){
            navController.navigate(Screen.PostScreen.route)
        }
    }

    LaunchedEffect(key1 = refresh){
        if (refresh) {
            viewModel.clearSelectPet()
            viewModel.addAllSelectedPet(petList)
            delay(100)
            if (selectedPet.isNotEmpty()) {
                isLoading = true
                viewModel.viewModelScope.launch {
                    viewModel.dailyLifeTimeLineListClear()
                    val result = viewModel.getTimeLineList()
                    isLoading = false
                    isError = !result
                    viewModel.updateTimeLineRefresh(false)
                }
            }
        }
    }

    LaunchedEffect(key1 = sortType){
        if ((sortType  != previousSortType) && (previousSortType !=null)){
            viewModel.viewModelScope.launch {
                viewModel.dailyLifeTimeLineListClear()
                val result = viewModel.getTimeLineList()
                isError = !result
                typeChange = false
            }
        }
        previousSortType = sortType
    }

    LaunchedEffect(key1 = selectedPet){
        if (selectedPet != previousSelectedPet && previousSelectedPet != null && !refresh){
            viewModel.dailyLifeTimeLineListClear()
            if (selectedPet.size != 0){
                viewModel.viewModelScope.launch {
                    val result = viewModel.getTimeLineList()
                    isError = !result
                }
            }
        }
        previousSelectedPet = selectedPet
    }

    BackHandler(
        enabled = isSearching
    ) {
        dismiss(false)
    }

    Scaffold { paddingValues ->
        if (isLoading){
            Box(modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary), contentAlignment = Alignment.Center){ LoadingAnimation1(circleColor = design_intro_bg) }
        }else if (isError || timeLineList == null){
            ErrorScreen(onClick = { viewModel.updateTimeLineRefresh(true) })
        }else{
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.onPrimaryContainer)
                    .fillMaxSize()
            ) {
                Canvas(
                    modifier = Modifier
                        .padding(start = 27.dp)
                        .fillMaxHeight()
                        .width(1.dp),
                ){
                    drawLine(
                        color = Color.Gray,
                        start = Offset(0f,0f),
                        end = Offset(0f,size.height),
                        pathEffect = pathEffect
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    state = dateLazyState,
                    contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(40.dp)
                ){
                    dailyLifeTimeLineList?.let { dailyLifeTimeLineList ->
                        for ((dateKey, itemList) in dailyLifeTimeLineList) {
                            item {
                                DateItem(viewModel = viewModel, dateKey = dateKey, dailyLifeTimeLineList = itemList, navController = navController)
                            }
                        }
                    }
                }


                Row (
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 20.dp, top = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    AnimatedVisibility(
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(
                                    color = Color.Black
                                ),
                                enabled = !typeChange
                            ) {
                                if (sortType == "오름차순") {
                                    typeChange = true
                                    viewModel.updateSortType("내림차순")
                                } else {
                                    typeChange = true
                                    viewModel.updateSortType("오름차순")
                                }
                            },
                        visible = !dateLazyState.canScrollBackward,
                        enter = fadeIn(), exit = fadeOut()
                    ) {
                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                        ){
                            Crossfade(
                                targetState = sortType, label = ""
                            ) {
                                when(it){
                                    "오름차순" ->
                                        Icon(
                                            painter = painterResource(id = R.drawable.icon_sort_up),
                                            contentDescription = "",
                                            tint = MaterialTheme.colorScheme.secondary
                                        )
                                    "내림차순" ->
                                        Icon(
                                            painter = painterResource(id = R.drawable.icon_sort_down),
                                            contentDescription = "",
                                            tint = MaterialTheme.colorScheme.secondary
                                        )
                                }

                            }
                            Text(
                                text = sortType,
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                fontSize = 14.sp, letterSpacing = (-0.7).sp,
                                lineHeight = 16.sp, color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }

                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(
                                    color = Color.Black
                                ),
                                enabled = !(petList.isNotEmpty() && petList[0].ownrPetUnqNo == "")
                            ) {
                                modeChange(true)
                                viewModel.updateToMonthCalendar(true)
                            }
                    ){
                        Icon(
                            painter = painterResource(id = R.drawable.icon_calendar),
                            contentDescription = "", tint = MaterialTheme.colorScheme.secondary
                        )

                        Text(
                            text = stringResource(R.string.monthly_journal),
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = 14.sp, letterSpacing = (-0.7).sp,
                            lineHeight = 16.sp, color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }


                AnimatedVisibility(
                    visible = isSearching,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(
                                    color = Color.Black
                                ),
                                enabled = isSearching
                            ) { dismiss(false) }
                    )
                }

                AnimatedVisibility(
                    visible = isSearching,
                    enter = expandVertically(tween(easing = EaseInOutCubic)) + fadeIn(),
                    exit = shrinkVertically(tween(easing = EaseInOutCubic)) + fadeOut()
                ) {
                    // 나타나는 Composable
                    Column(
                        modifier = Modifier
                            .shadow(
                                color = MaterialTheme.colorScheme.onSurface,
                                offsetY = 4.dp,
                                offsetX = 4.dp,
                                spread = 4.dp,
                                blurRadius = 4.dp,
                                borderRadius = 20.dp
                            )
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp), color = MaterialTheme.colorScheme.primary)
                            .align(Alignment.TopCenter)
                            .clip(shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                    ){
                        Spacer(modifier = Modifier.padding(top = 20.dp))

                        Text(
                            text = stringResource(R.string.pet_select),
                            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                            fontSize = 20.sp,
                            letterSpacing = (-1.0).sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(start = 20.dp)
                        )

                        Spacer(modifier = Modifier.padding(top = 16.dp))

                        Box {
                            LazyRow(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .align(Alignment.Center)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                state = state
                                //contentPadding = PaddingValues(horizontal = 20.dp)
                            ){
                                items(petList){ petList ->
                                    TopPetItem(petList = petList, viewModel = viewModel)
                                }
                            }

                            if (state.canScrollForward){
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "",
                                    modifier = Modifier.align(Alignment.CenterEnd), tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                                )
                            }

                            if (state.canScrollBackward){
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "",
                                    modifier = Modifier.align(Alignment.CenterStart), tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.padding(top = 20.dp))
                    }
                }
            }//Box
        }
    }
}

@Composable
fun DateItem(viewModel: WalkViewModel, dateKey: String, dailyLifeTimeLineList: List<LifeTimeLineItem>?, navController:NavHostController) {

    Box{

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.offset(y = (-7).dp)
            ){
                Icon(painter = painterResource(id = R.drawable.timeline_date),
                    contentDescription = "", tint = Color.Unspecified,
                    modifier = Modifier.padding(start = 20.dp)
                )

                Text(
                    text = dateKey,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 12.sp, letterSpacing = (-0.6).sp,
                    color = design_intro_bg,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 5000.dp),
                state = rememberLazyListState(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ){
                items(dailyLifeTimeLineList?: emptyList()){ item ->
                    TimeItem(timeData = item, viewModel = viewModel, navController = navController)
                }
            }

        }
    }


}

@Composable
fun TimeItem(timeData: LifeTimeLineItem,viewModel:WalkViewModel,navController:NavHostController) {

    var lastClickTime by remember { mutableStateOf(System.currentTimeMillis()) }
    val searchText by viewModel.searchText.collectAsState()

    Column (
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clickable(
                enabled = timeData.bldYn != "Y"
            ) {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastClickTime >= 300) {
                    lastClickTime = currentTime
                    viewModel.viewModelScope.launch {
                        viewModel.getDailyDetail(timeData.schUnqNo)
                        viewModel.updateLastDaily(timeData.schUnqNo)
                    }
                    navController.navigate(Screen.WalkDetailContent.route)
                }
            }
    ){
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(timeData.runFile)
                .crossfade(400)
                .build(),
            filterQuality = FilterQuality.Low
        )

        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ){
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    painter = painterResource(id = R.drawable.timeline_time),
                    contentDescription = "", tint = Color.Unspecified
                )

                Text(
                    text = timeData.walkDptreTime,
                    fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                    fontSize = 16.sp, letterSpacing = (-0.8).sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }

            //Row (
            //    verticalAlignment = Alignment.CenterVertically
            //){
            //    Icon(
            //        painter = painterResource(id = R.drawable.icon_delete),
            //        contentDescription = "", tint = MaterialTheme.colorScheme.secondary
            //    )
            //
            //    Text(
            //        text = "삭제",
            //        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            //        fontSize = 12.sp, letterSpacing = (-0.6).sp,
            //        color = MaterialTheme.colorScheme.secondary,
            //        modifier = Modifier.padding(start = 4.dp)
            //    )
            //}


        }

        Column (
            modifier = Modifier
                .padding(start = 20.dp)
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(12.dp))
                .border(width = 1.dp, color = MaterialTheme.colorScheme.onSecondaryContainer, shape = RoundedCornerShape(12.dp))
                .clip(shape = RoundedCornerShape(12.dp))
        ){
            LazyRow(
                modifier = Modifier.padding(start = 20.dp, top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy((-10).dp)
            ){
                timeData.petList?.let {
                    items(timeData.petList!!){ item ->
                        CircleImageTopBar(size = 42, imageUri = item.petImg)
                    }
                }

            }

            Row (
                modifier = Modifier
                    .padding(start = 20.dp, top = 12.dp)
                    .fillMaxWidth()
            ){
                Column (
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .weight(1f)
                ){
                    Text(
                        text =
                        buildAnnotatedString {
                            val str = timeData.runTit
                            val startIndex = str.indexOf(searchText)
                            val endIndex = str.indexOf(searchText)+searchText.length
                            append(str)
                            if (searchText != "") addStyle(style = SpanStyle(color = design_intro_bg), start = startIndex, end = endIndex)
                        },
                        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                        fontSize = 16.sp, letterSpacing = (-0.8).sp, lineHeight = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = timeData.runCmn?: "",
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 14.sp, letterSpacing = (-0.7).sp, lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Image(
                    painter = painter,
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.onSecondaryContainer))

            Row (modifier = Modifier
                .padding(top = 16.dp, start = 20.dp, end = 20.dp, bottom = 16.dp)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
            ){
                Column (
                    modifier= Modifier
                        .weight(1f)
                ){
                    Text(
                        text = stringResource(R.string.walker),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        lineHeight = 14.sp,
                        letterSpacing = (-0.7).sp,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Text(
                        text = timeData.runNcknm,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                        lineHeight = 14.sp,
                        letterSpacing = 0.sp,
                        modifier = Modifier.padding(top = 4.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Spacer(modifier = Modifier
                    .padding(end = 10.dp)
                    .size(1.dp, 40.dp)
                    .background(color = MaterialTheme.colorScheme.onSecondaryContainer))

                Column (
                    modifier= Modifier
                        .weight(1f)
                ){
                    Text(
                        text = stringResource(R.string.walk_time),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        lineHeight = 14.sp,
                        letterSpacing = (-0.7).sp,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Row (modifier= Modifier.fillMaxWidth()){
                        Text(
                            text = timeData.runTime,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                            lineHeight = 14.sp,
                            letterSpacing = 0.sp,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .alignByBaseline(),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier
                    .padding(end = 10.dp)
                    .size(1.dp, 40.dp)
                    .background(color = MaterialTheme.colorScheme.onSecondaryContainer))

                Column (
                    modifier= Modifier
                        .weight(1f)
                ){

                    Text(
                        text = stringResource(R.string.walk_distance),
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        lineHeight = 14.sp,
                        letterSpacing = (-0.7).sp,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Row (modifier= Modifier.fillMaxWidth()){
                        Text(
                            text = timeData.runDstnc.toString(),
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                            lineHeight = 14.sp,
                            letterSpacing = 0.sp,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .alignByBaseline(),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = "m",
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                            lineHeight = 14.sp,
                            letterSpacing = 0.sp,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .alignByBaseline(),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                }
            }
        } // inner col

    } // col
}

@Composable
fun TopPetItem(petList: PetDetailData, viewModel: WalkViewModel){
    
    
    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp.dp -60.dp

    val petName:String = petList.petNm
    val imageUri:String? = petList.petRprsImgAddr
    val selectedPet by viewModel.selectedPet.collectAsState()

    var isSelected by remember{ mutableStateOf(selectedPet.contains(petList)) }

    Button(
        onClick = {
            isSelected = if (isSelected){
                viewModel.removeSelectedPet(petList)
                !isSelected
            }else{
                viewModel.addSelectedPet(petList)
                !isSelected
            }
        },
        modifier = Modifier
            .size(width = screenWidth * 0.33f, height = screenWidth * 0.33f - 9.dp)
            .shadow(ambientColor = design_shadow, elevation = 0.dp)
        ,
        shape = RoundedCornerShape(12.dp),
        colors = if(isSelected) {
            ButtonDefaults.buttonColors(design_select_btn_bg)
        } else {
            ButtonDefaults.buttonColors(Color.Transparent)
        },
        border = if(isSelected) {
            BorderStroke(1.dp, color = design_select_btn_text)
        } else {
            BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline)
        },
        contentPadding = PaddingValues(start = 14.dp,end=14.dp),
        elevation = if(isSelected){
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
                    .border(shape = CircleShape, border = BorderStroke(3.dp, color = MaterialTheme.colorScheme.tertiary))
                    //.shadow(elevation = 10.dp, shape = CircleShape, spotColor = Color.Gray)
                    .shadow(
                        color = MaterialTheme.colorScheme.onSurface,
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
                color = if (isSelected) design_login_text else MaterialTheme.colorScheme.onPrimary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}


@Stable
fun Modifier.mirror(): Modifier = composed {
    if (LocalLayoutDirection.current == LayoutDirection.Rtl)
        this.scale(scaleX = -1f, scaleY = 1f)
    else
        this
}