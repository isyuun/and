package kr.carepet.app.navi.screens.commuscreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import kr.carepet.app.navi.R
import kr.carepet.app.navi.component.BackTopBar
import kr.carepet.app.navi.component.CircleImageTopBar
import kr.carepet.app.navi.component.LoadingAnimation1
import kr.carepet.app.navi.screens.mainscreen.calculateCurrentOffsetForPage
import kr.carepet.app.navi.screens.mainscreen.metersToKilometers
import kr.carepet.app.navi.screens.walkscreen.FullScreenImage
import kr.carepet.app.navi.ui.theme.design_DDDDDD
import kr.carepet.app.navi.ui.theme.design_btn_border
import kr.carepet.app.navi.ui.theme.design_intro_bg
import kr.carepet.app.navi.ui.theme.design_login_bg
import kr.carepet.app.navi.ui.theme.design_login_text
import kr.carepet.app.navi.ui.theme.design_placeHolder
import kr.carepet.app.navi.ui.theme.design_skip
import kr.carepet.app.navi.ui.theme.design_textFieldOutLine
import kr.carepet.app.navi.ui.theme.design_white
import kr.carepet.app.navi.viewmodel.CommunityViewModel
import kr.carepet.app.navi.viewmodel.SharedViewModel
import kr.carepet.data.daily.DailyDetailData
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StoryDetail(viewModel: CommunityViewModel, sharedViewModel: SharedViewModel, navController:NavHostController){

    val comment by viewModel.comment.collectAsState()
    val storyDetail by viewModel.storyDetail.collectAsState()
    val story = storyDetail?.data
    val isWalk = story?.dailyLifeSchSeList?.any{ it.cdId == "001" }

    val annotatedString = buildAnnotatedString {
        val hashTagList = story?.dailyLifeSchHashTagList ?: emptyList()

        hashTagList.forEach {
            withStyle(style = SpanStyle(
                color = design_intro_bg, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular))
            )
            ){
                append("#${it.hashTagNm} ")
            }
        }
    }

    val pagerState = rememberPagerState( pageCount = {story?.dailyLifeFileList?.size ?: 0 })
    var showImage by remember{ mutableStateOf(false) }


    BackHandler {
        if (showImage){
            showImage = false
        }else{
            navController.popBackStack()
        }
    }
    val dummy = arrayListOf(
        "배추야 오늘 만나서 반가웠어~ 다음에 또 만나자!",
        "배추야 오늘 만나서 반가웠어~ 다음에 또 만나자! 배추야 오늘 만나서 반가웠어~ 다음에 또 만나자! 배추야 오늘 만나서 반가웠어~ 다음에 또 만나자!"
    )

    LaunchedEffect(Unit){
        sharedViewModel.updatePushData(null)
    }

    DisposableEffect(Unit){
        onDispose {
            viewModel.updateStoryDetail(null)
        }
    }

    Scaffold (
        topBar = {BackTopBar(title = stringResource(R.string.title_story), navController = navController)}
    ){ paddingValues ->
        Column (
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = design_white)
                .verticalScroll(rememberScrollState())
        ){
            Spacer(modifier = Modifier.padding(top = 20.dp))
            
            StoryDetailTopContent(story)

            Spacer(
                modifier = Modifier
                    .padding(
                        top = 16.dp,
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 20.dp
                    )
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = design_textFieldOutLine)
            )

            if (story?.dailyLifeFileList != null){
                PagerImage(story, showImage = {newValue -> showImage = newValue}, pagerState)
            }

            if (isWalk == true){
                StoryDetailTime(story)

                Spacer(modifier = Modifier.padding(top = 20.dp))
            }

            if ( !(story?.schCn == " " || story?.schCn =="") ){
                Text(
                    text = story?.schCn ?: "",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp,
                    letterSpacing = (-0.7).sp,
                    color = design_login_text,
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                )
            }

            if (story?.dailyLifeSchHashTagList?.isNotEmpty() == true){
                Text(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                        .fillMaxWidth(),
                    text = annotatedString,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    letterSpacing = (-0.7).sp,
                    color = design_intro_bg
                )
            }

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(design_login_bg)
                    .height(36.dp),
                verticalAlignment = Alignment.CenterVertically
            ){

                Icon(
                    painter = painterResource(id = R.drawable.icon_comment_line),
                    contentDescription = "", tint = Color.Unspecified,
                    modifier = Modifier.padding(start = 20.dp))

                Text(
                    text = "댓글 " + story?.cmntCnt,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 12.sp,
                    letterSpacing = (-0.6).sp,
                    color = design_skip,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.padding(top = 18.dp))
            
            LazyColumn(
                state = rememberLazyListState(),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp)
            ){
                itemsIndexed(dummy){ index, item ->
                    CommentListItem(comment = item)

                    if(index!=dummy.size-1){
                        Spacer(modifier = Modifier
                            .padding(vertical = 16.dp, horizontal = 20.dp)
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(color = design_textFieldOutLine))
                    }
                }
            }

            Spacer(modifier = Modifier
                .padding(top = 40.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(design_login_text))

            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                TextField(
                    value = comment,
                    onValueChange = {viewModel.updateComment(it)},
                    textStyle = TextStyle(
                        color = design_login_text,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 14.sp,
                        letterSpacing = (-0.7).sp
                    ),
                    placeholder = {
                        Text(text = stringResource(R.string.ph_comment), style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = 14.sp,
                            letterSpacing = (-0.7).sp
                        )) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done),
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedPlaceholderColor = design_placeHolder,
                        focusedPlaceholderColor = design_placeHolder,
                        unfocusedBorderColor = design_textFieldOutLine,
                        focusedBorderColor = design_login_text,
                        unfocusedContainerColor = design_white,
                        focusedContainerColor = design_white,
                        unfocusedLeadingIconColor = design_placeHolder,
                        focusedLeadingIconColor = design_login_text)
                )
                
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(width = 56.dp, height = 40.dp)
                        .background(color = design_btn_border, shape = RoundedCornerShape(12.dp))
                        .clip(shape = RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ){
                    Text(text = stringResource(R.string.comment_apply), style = TextStyle(
                        color = design_white,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 14.sp,
                        letterSpacing = (-0.7).sp),
                        textAlign = TextAlign.Center
                    )
                }

                Box(
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(40.dp)
                        .background(color = design_white, shape = RoundedCornerShape(12.dp))
                        .clip(shape = RoundedCornerShape(12.dp))
                        .border(1.dp, color = design_textFieldOutLine, shape = RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ){
                    Icon(painter = painterResource(id = R.drawable.icon_like),
                        contentDescription = "", tint = Color.Unspecified)
                }

                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(40.dp)
                        .background(color = design_white, shape = RoundedCornerShape(12.dp))
                        .clip(shape = RoundedCornerShape(12.dp))
                        .border(1.dp, color = design_textFieldOutLine, shape = RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ){
                    Icon(painter = painterResource(id = R.drawable.icon_dislike),
                        contentDescription = "", tint = Color.Unspecified)
                }
            }
        }// col
    }

    AnimatedVisibility(
        visible = showImage,
        enter = scaleIn(transformOrigin = TransformOrigin(pivotFractionX = 0.5f, pivotFractionY = 0.3f)).plus(fadeIn()),
        exit = scaleOut(transformOrigin = TransformOrigin(pivotFractionX = 0.5f, pivotFractionY = 0.3f)).plus(fadeOut())
    ) {
        FullScreenImage(
            dailyDetail = story!!,
            page = pagerState.currentPage,
            onDismiss = { newValue -> showImage = newValue })
    }
}

@Composable
fun StoryDetailTopContent(story: DailyDetailData?) {
    Column (
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
    ){
        Text(
            text = story?.schTtl ?: "",
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            fontSize = 24.sp,
            letterSpacing = (-1.2).sp,
            color = design_login_text
        )

        Row (
            modifier= Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
            
        ){
            CircleImageTopBar(size = 40, imageUri = story?.dailyLifePetList?.get(0)?.petImg ?: "")
            
            Spacer(modifier = Modifier.padding(start = 8.dp))

            Column (
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = story?.dailyLifePetList?.get(0)?.petNm ?: "",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp,
                    letterSpacing = (-0.7).sp,
                    color = design_login_text
                )

                Text(
                    text = story?.rlsDt ?: "",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 12.sp,
                    letterSpacing = (-0.6).sp, lineHeight = 12.sp,
                    color = design_skip
                )
            }
        }

        Row (
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Row (verticalAlignment = Alignment.CenterVertically){
                Icon(painter = painterResource(id = R.drawable.icon_like), contentDescription = "", tint = Color.Unspecified)

                Text(
                    text = story?.rcmdtnCnt?.toString() ?: "0",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 12.sp,
                    letterSpacing = (-0.6).sp,
                    color = design_skip,
                    modifier = Modifier.padding(start = 4.dp)
                )

                Icon(
                    painter = painterResource(id = R.drawable.icon_comment_line),
                    contentDescription = "", tint = Color.Unspecified,
                    modifier = Modifier.padding(start = 12.dp))

                Text(
                    text = story?.cmntCnt?.toString() ?:"0",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 12.sp,
                    letterSpacing = (-0.6).sp,
                    color = design_skip,
                    modifier = Modifier.padding(start = 4.dp)
                )

                Icon(
                    painter = painterResource(id = R.drawable.icon_view),
                    contentDescription = "", tint = Color.Unspecified,
                    modifier = Modifier.padding(start = 12.dp))

                Text(
                    text = "218",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 12.sp,
                    letterSpacing = (-0.6).sp,
                    color = design_skip,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Row (verticalAlignment = Alignment.CenterVertically){
                Icon(
                    painter = painterResource(id = R.drawable.icon_report),
                    contentDescription = "", tint = Color.Unspecified,
                    modifier = Modifier.padding(start = 12.dp))

                Text(
                    text = "신고하기",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 12.sp,
                    letterSpacing = (-0.6).sp,
                    color = design_skip,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerImage(story: DailyDetailData?, showImage: (Boolean) -> Unit, pagerState: PagerState) {

    val isLoading by remember{ mutableStateOf(story == null) }

    Crossfade(
        targetState = isLoading,
        label = "",
        animationSpec = tween(700),
        modifier = Modifier.animateContentSize()
    ) { isLoading ->
        when(isLoading){
            true ->
                Box (modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .height(258.dp),
                    contentAlignment = Alignment.Center
                ){
                    LoadingAnimation1(circleColor = design_intro_bg)
                }
            false ->
                Column {
                    HorizontalPager(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth()
                            .heightIn(max = 300.dp),
                        state = pagerState,
                        beyondBoundsPageCount = 1,
                        flingBehavior = PagerDefaults.flingBehavior(
                            state = pagerState, snapVelocityThreshold = 100.dp)
                    ) { page ->
                        val isSelected = page == pagerState.currentPage // 선택된 페이지 여부를 확인

                        // 선택된 아이템의 Z-index를 높게 설정
                        val zIndexModifier = if (isSelected) Modifier.zIndex(1f) else Modifier

                        Box(Modifier
                            .then(zIndexModifier)
                            .graphicsLayer {
                                val pageOffset = pagerState.calculateCurrentOffsetForPage(page)
                                // translate the contents by the size of the page, to prevent the pages from sliding in from left or right and stays in the center
                                //translationX = pageOffset * size.width/4
                                // apply an alpha to fade the current page in and the old page out
                                alpha = 1 - pageOffset.absoluteValue / 3 * 2
                                scaleX = 1 - pageOffset.absoluteValue / 3
                                scaleY = 1 - pageOffset.absoluteValue / 3
                            }
                            .fillMaxSize()
                            , contentAlignment = Alignment.Center) {

                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(
                                        story?.atchPath+
                                                story!!.dailyLifeFileList[page].filePathNm+
                                                story!!.dailyLifeFileList[page].atchFileNm
                                    )
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "",
                                //placeholder = painterResource(id = R.drawable.profile_default),
                                //error= painterResource(id = R.drawable.profile_default),
                                modifier= Modifier
                                    .fillMaxSize()
                                    .clickable { showImage(true) },
                                contentScale = ContentScale.Fit,
                                filterQuality = FilterQuality.Low
                            )
                        }
                    }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(story?.dailyLifeFileList?.size ?: 0 ) { iteration ->
                            val color = if (pagerState.currentPage == iteration) design_intro_bg else design_DDDDDD
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .size(10.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.padding(top= 20.dp))
                }
        }
    }
}

@Composable
fun StoryDetailTime(story: DailyDetailData?) {

    Row (modifier = Modifier
        .fillMaxWidth()
        , horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
    ){
        Column (
            modifier= Modifier
                .weight(1f)
        ){
            Text(
                text = stringResource(R.string.story_walk_date),
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                letterSpacing = (-0.7).sp,
                color = design_skip,
                modifier = Modifier.padding(start = 20.dp)
            )

            Text(
                text = story?.walkDptreDt?.let { formatDateString(it) }?:"",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                letterSpacing = 0.sp,
                modifier = Modifier.padding(top = 4.dp, start = 20.dp),
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
                text = stringResource(R.string.story_walk_time),
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                letterSpacing = (-0.7).sp,
                color = design_skip,
                modifier = Modifier.padding(start = 20.dp)
            )

            Text(
                text = story?.runTime ?: "",
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
                text = stringResource(R.string.story_walk_dist),
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                letterSpacing = (-0.7).sp,
                color = design_skip,
                modifier = Modifier.padding(start = 20.dp)
            )

            Text(
                text = metersToKilometers(story?.runDstnc?:0)+"km",
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

@Composable
fun CommentListItem(comment:String){

    Row (
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
    ){
        CircleImageTopBar(size = 40, imageUri = "")
        
        Spacer(modifier = Modifier.padding(start = 8.dp))
        
        Column (
            modifier = Modifier.fillMaxWidth()
        ){
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = "강레오",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    letterSpacing = (-0.7).sp,
                    color = design_skip
                )

                Row (verticalAlignment = Alignment.CenterVertically){
                    Icon(
                        painter = painterResource(id = R.drawable.icon_like_default),
                        contentDescription = "", tint = Color.Unspecified,
                        modifier = Modifier.padding(start = 12.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.icon_dislike_default),
                        contentDescription = "", tint = Color.Unspecified,
                        modifier = Modifier.padding(start = 12.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.icon_report),
                        contentDescription = "", tint = Color.Unspecified,
                        modifier = Modifier.padding(start = 12.dp))
                }
            }


            Text(
                text = comment,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 14.sp,
                letterSpacing = (-0.7).sp,
                color = design_login_text
            )
        }
    }
}

private fun formatDateString(input: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())

    return try {
        val date = inputFormat.parse(input)
        outputFormat.format(date)
    } catch (e: Exception) {
        // 날짜 변환 중 에러가 발생할 경우 예외 처리
        e.printStackTrace()
        "날짜 변환 오류"
    }
}