package net.pettip.app.navi.screens.walkscreen

import android.view.MotionEvent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.component.BackTopBar
import net.pettip.app.navi.component.CircleImageTopBar
import net.pettip.app.navi.component.ErrorScreen
import net.pettip.app.navi.component.Toasty
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_login_bg
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.WalkViewModel
import net.pettip.data.daily.DailyDetailData
import net.pettip.data.daily.DailyLifePet
import net.pettip.map.app.naver.GpxMap
import net.pettip.util.Log
import net.pettip.util.getMethodName

private val __CLASSNAME__ = Exception().stackTrace[0].fileName

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun WalkDetailContent(walkViewModel: WalkViewModel, navController: NavHostController) {

    val isLoading by walkViewModel.isLoading.collectAsState()
    val dailyDetail by walkViewModel.dailyDetail.collectAsState()
    val lastDaily by walkViewModel.lastDaily.collectAsState()
    val gpxInputStream by walkViewModel.gpxInputStream.collectAsState()


    val context = LocalContext.current

    var gpxDownload by remember { mutableStateOf(false) }
    var gpxDownloading by remember { mutableStateOf(false) }

    var imageLoading by remember { mutableStateOf(false) }
    var showImage by remember { mutableStateOf(false) }
    var refresh by remember { mutableStateOf(false) }
    var curruntImage by remember { mutableIntStateOf(0) }

    val snackState = remember { SnackbarHostState() }
    val pagerState = rememberPagerState(pageCount = { dailyDetail?.dailyLifeFileList?.size ?: 0 })

    val annotatedString = buildAnnotatedString {
        val hashTagList = dailyDetail?.dailyLifeSchHashTagList ?: emptyList()

        append("\n\n")

        hashTagList.forEach {
            withStyle(
                style = SpanStyle(
                    color = design_intro_bg, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular))
                )
            ) {
                append("#${it.hashTagNm} ")
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            walkViewModel.updateDailyDetail(null)
            walkViewModel.updateGpxInputStream(null)
        }
    }

    BackHandler {
        if (showImage) {
            showImage = false
        } else {
            navController.popBackStack()
        }
    }


    LaunchedEffect(key1 = dailyDetail) {
        if (!dailyDetail?.totMvmnPathFileSn.isNullOrEmpty() && !gpxDownload && !gpxDownloading) {
            dailyDetail?.totMvmnPathFileSn?.let {
                gpxDownloading = true
                walkViewModel.viewModelScope.launch {
                    val result = walkViewModel.saveGpxFileToStream(totMvmnPathFileSn = it, context = context, fileName = "${dailyDetail?.schUnqNo}.GPX")
                    if (result) {
                        gpxDownload = true
                        gpxDownloading = false
                    } else {
                        gpxDownloading = false
                    }
                }
            }
        }
    }

    LaunchedEffect(refresh) {
        if (refresh) {
            val result = lastDaily?.let { walkViewModel.getDailyDetail(it) }
            refresh = if (result == true) {
                false
            } else {
                false
            }
        }
    }


    Scaffold(
        topBar = { BackTopBar(title = dailyDetail?.schTtl ?: "", navController = navController) },
        snackbarHost = { Toasty(snackState = snackState) }
    ) { paddingValue ->

        Crossfade(
            targetState = !isLoading && dailyDetail == null,
            label = ""
        ) {
            var isTouch by remember { mutableStateOf(false) }
            when (it) {
                true -> ErrorScreen(onClick = { refresh = true })
                false ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
                        Column(
                            modifier = Modifier
                                .verticalScroll(
                                    rememberScrollState(),
                                    enabled = !isTouch,
                                )
                                .padding(paddingValues = paddingValue)
                                .fillMaxSize()
                                .background(color = MaterialTheme.colorScheme.primary)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(start = 20.dp, top = 20.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(painter = painterResource(id = R.drawable.icon_calendar), contentDescription = "", tint = Color.Unspecified)
                                Text(
                                    text = dailyDetail?.walkDptreDt ?: "",
                                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                    fontSize = 14.sp,
                                    letterSpacing = (-0.7).sp,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            }

                            // ----------- 맵 들어갈 자리 -------------------
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(360.dp)
                            ) {
                                if (dailyDetail != null && gpxDownload) {
                                    val `in` = gpxInputStream
                                    `in`?.use { i ->
                                        Log.v(__CLASSNAME__, "GpxMap()${getMethodName()}[${i.equals(`in`)}][${`in`.available()}][${i.available()}]")
                                        GpxMap(i) { _, event ->
                                            isTouch = when (event.action) {
                                                MotionEvent.ACTION_DOWN -> true
                                                MotionEvent.ACTION_UP -> false
                                                else -> true
                                            }
                                            false
                                        }
                                    }
                                }
                            }
                            // ----------- 맵 들어갈 자리 -------------------

                            //Column {
                            //    Spacer(modifier = Modifier.padding(top= 20.dp))
                            //
                            //    HorizontalPager(
                            //        modifier = Modifier
                            //            .padding(horizontal = 10.dp)
                            //            .fillMaxWidth()
                            //            .heightIn(max = 240.dp),
                            //        state = pagerState,
                            //        beyondBoundsPageCount = 1,
                            //        flingBehavior = PagerDefaults.flingBehavior(
                            //            state = pagerState, snapVelocityThreshold = 100.dp)
                            //    ) { page ->
                            //        val isSelected = page == pagerState.currentPage // 선택된 페이지 여부를 확인
                            //
                            //        // 선택된 아이템의 Z-index를 높게 설정
                            //        val zIndexModifier = if (isSelected) Modifier.zIndex(1f) else Modifier
                            //
                            //        Box(Modifier
                            //            .then(zIndexModifier)
                            //            .graphicsLayer {
                            //                val pageOffset = pagerState.calculateCurrentOffsetForPage(page)
                            //                // translate the contents by the size of the page, to prevent the pages from sliding in from left or right and stays in the center
                            //                //translationX = pageOffset * size.width/4
                            //                // apply an alpha to fade the current page in and the old page out
                            //                alpha = 1 - pageOffset.absoluteValue / 3 * 2
                            //                scaleX = 1 - pageOffset.absoluteValue / 3
                            //                scaleY = 1 - pageOffset.absoluteValue / 3
                            //            }
                            //            .fillMaxSize()
                            //            , contentAlignment = Alignment.Center) {
                            //
                            //            AsyncImage(
                            //                onLoading = { imageLoading = true },
                            //                onError = {Log.d("LOG", "onError")},
                            //                onSuccess = { imageLoading = false},
                            //                model = ImageRequest.Builder(LocalContext.current)
                            //                    .data(
                            //                        "http://carepet.hopto.org/img/"+
                            //                                (dailyDetail!!.dailyLifeFileList?.get(page)?.filePathNm ?: "") +
                            //                                (dailyDetail!!.dailyLifeFileList?.get(page)?.atchFileNm ?: "")
                            //                    )
                            //                    .crossfade(true)
                            //                    .build(),
                            //                contentDescription = "",
                            //                //placeholder = painterResource(id = R.drawable.profile_default),
                            //                //error= painterResource(id = R.drawable.profile_default),
                            //                modifier= Modifier
                            //                    .fillMaxSize()
                            //                    .clickable { showImage = true },
                            //                contentScale = ContentScale.Fit
                            //            )
                            //        }
                            //    }
                            //
                            //    Row(
                            //        Modifier
                            //            .fillMaxWidth()
                            //            .padding(top = 8.dp),
                            //        horizontalArrangement = Arrangement.Center
                            //    ) {
                            //        repeat(dailyDetail?.dailyLifeFileList?.size ?: 0 ) { iteration ->
                            //            val color = if (pagerState.currentPage == iteration) design_intro_bg else design_DDDDDD
                            //            Box(
                            //                modifier = Modifier
                            //                    .padding(horizontal = 4.dp)
                            //                    .clip(CircleShape)
                            //                    .background(color)
                            //                    .size(10.dp)
                            //            )
                            //        }
                            //    }
                            //}

                            Spacer(modifier = Modifier.padding(top = 20.dp))

                            Row(
                                modifier = Modifier
                                    .padding(start = 20.dp, end = 20.dp)
                                    .fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                ) {
                                    Text(
                                        text = "산책자",
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                        letterSpacing = (-0.7).sp,
                                        color = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.padding(start = 20.dp)
                                    )

                                    Text(
                                        text = dailyDetail?.runNcknm ?: "",
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                                        letterSpacing = 0.sp,
                                        modifier = Modifier.padding(top = 4.dp, start = 20.dp),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }

                                Spacer(
                                    modifier = Modifier
                                        .size(1.dp, 40.dp)
                                        .background(color = MaterialTheme.colorScheme.onSecondaryContainer)
                                )

                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                ) {

                                    Text(
                                        text = "산책 시간",
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                        letterSpacing = (-0.7).sp,
                                        color = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.padding(start = 20.dp)
                                    )

                                    Text(
                                        text = dailyDetail?.runTime ?: "",
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                                        letterSpacing = 0.sp,
                                        modifier = Modifier
                                            .padding(top = 4.dp, start = 20.dp),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )

                                }

                                Spacer(
                                    modifier = Modifier
                                        .size(1.dp, 40.dp)
                                        .background(color = MaterialTheme.colorScheme.onSecondaryContainer)
                                )

                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                ) {

                                    Text(
                                        text = "산책 거리",
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                        letterSpacing = (-0.7).sp,
                                        color = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.padding(start = 20.dp)
                                    )

                                    Text(
                                        text = (dailyDetail?.runDstnc ?: 0).toString() + "m",
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                                        letterSpacing = 0.sp,
                                        modifier = Modifier
                                            .padding(top = 4.dp, start = 20.dp),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )

                                }
                            }

                            Spacer(modifier = Modifier.padding(top = 20.dp))

                            AnimatedVisibility(
                                visible = !isLoading && dailyDetail?.dailyLifePetList?.isNotEmpty() == true,
                                enter = fadeIn(tween(durationMillis = 700, delayMillis = 200)).plus(expandVertically()),
                                exit = fadeOut(tween(durationMillis = 700, delayMillis = 200)).plus(shrinkVertically())
                            ) {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(max = 500.dp),
                                    state = rememberLazyListState(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(dailyDetail?.dailyLifePetList ?: emptyList()) { item ->
                                        DetailLazyColItem(item)
                                    }
                                }
                            }



                            Spacer(modifier = Modifier.padding(top = 20.dp))


                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .fillMaxWidth()
                                    .background(color = MaterialTheme.colorScheme.onPrimaryContainer, shape = RoundedCornerShape(12.dp))
                                    .clip(RoundedCornerShape(12.dp))
                            ) {
                                if (!(dailyDetail?.schCn == " " || dailyDetail?.schCn == "")) {
                                    Text(
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth()
                                            .align(Alignment.TopStart),
                                        text = dailyDetail?.schCn ?: "",
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                        letterSpacing = (-0.7).sp,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }

                                if (dailyDetail?.dailyLifeSchHashTagList?.isNotEmpty() == true) {
                                    Text(
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth()
                                            .align(Alignment.BottomStart),
                                        text = annotatedString,
                                        fontSize = 14.sp,
                                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                        letterSpacing = (-0.7).sp,
                                        color = design_intro_bg
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.padding(top = 16.dp))

                            LazyRow(
                                state = rememberLazyListState(),
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .fillMaxWidth(),
                                contentPadding = PaddingValues(horizontal = 10.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                itemsIndexed(dailyDetail?.dailyLifeFileList ?: emptyList()) { index, item ->
                                    Box(
                                        modifier = Modifier
                                            .size(100.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable {
                                                curruntImage = index
                                                showImage = true
                                            }
                                    ) {
                                        AsyncImage(
                                            model = ImageRequest.Builder(LocalContext.current)
                                                .data(
                                                    dailyDetail?.atchPath +
                                                            item.filePathNm +
                                                            item.atchFileNm
                                                )
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = "",
                                            placeholder = painterResource(id = R.drawable.img_blank),
                                            error = null,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop,
                                            filterQuality = FilterQuality.Low
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.padding(top = 16.dp))
                        }// column
                    }
            }
        }
    }

    AnimatedVisibility(
        visible = showImage,
        enter = scaleIn(transformOrigin = TransformOrigin(pivotFractionX = 0.5f, pivotFractionY = 0.9f)).plus(fadeIn()),
        exit = scaleOut(transformOrigin = TransformOrigin(pivotFractionX = 0.5f, pivotFractionY = 0.9f)).plus(fadeOut())
    ) {
        FullScreenImage(
            dailyDetail = dailyDetail!!,
            page = curruntImage,
            onDismiss = { newValue -> showImage = newValue })
    }

}


@Composable
fun DetailLazyColItem(dailyDetail: DailyLifePet) {

    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .background(color = Color.Transparent, shape = RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.padding(start = 22.dp))

            CircleImageTopBar(size = 50, imageUri = dailyDetail.petImg)

            Text(
                text = dailyDetail.petNm,
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                fontSize = 16.sp,
                letterSpacing = (-0.8).sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(start = 10.dp, top = 22.dp, bottom = 22.dp)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 20.dp, top = 16.dp, bottom = 16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(painter = painterResource(id = R.drawable.icon_poop), contentDescription = "", tint = Color.Unspecified)

                Text(
                    text = "배변",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp,
                    letterSpacing = (-0.7).sp,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(start = 4.dp, end = 8.dp)
                )

                Box(
                    modifier = Modifier.width(60.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = dailyDetail.bwlMvmNmtm.toString() + "회",
                        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                        fontSize = 14.sp,
                        letterSpacing = (-0.7).sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            Spacer(modifier = Modifier.padding(top = 8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(painter = painterResource(id = R.drawable.icon_pee), contentDescription = "", tint = Color.Unspecified)

                Text(
                    text = "소변",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp,
                    letterSpacing = (-0.7).sp,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(start = 4.dp, end = 8.dp)
                )

                Box(
                    modifier = Modifier.width(60.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = dailyDetail.urineNmtm.toString() + "회",
                        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                        fontSize = 14.sp,
                        letterSpacing = (-0.7).sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            Spacer(modifier = Modifier.padding(top = 8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(painter = painterResource(id = R.drawable.icon_marking), contentDescription = "", tint = Color.Unspecified)

                Text(
                    text = "마킹",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp,
                    letterSpacing = (-0.7).sp,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(start = 4.dp, end = 8.dp)
                )

                Box(
                    modifier = Modifier.width(60.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = dailyDetail.relmIndctNmtm.toString() + "회",
                        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                        fontSize = 14.sp,
                        letterSpacing = (-0.7).sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingComposable() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                0.7f at 500
            },
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .background(design_login_bg.copy(alpha))
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenImage(
    dailyDetail: DailyDetailData,
    page: Int,
    onDismiss: (Boolean) -> Unit
) {
    var systemBarColor by remember { mutableStateOf(Color.Black) }
    val color = MaterialTheme.colorScheme.primary

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color = systemBarColor)

    var rotate by remember { mutableStateOf(false) }
    var scale by remember { mutableFloatStateOf(1f) }
    val rotation: Float by animateFloatAsState(if (rotate) 90f else 0f, label = "")
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        //rotation += rotationChange
        offset = if (scale <= 1f) {
            Offset.Zero
        } else {
            offset + offsetChange
        }
    }

    BackHandler {
        systemBarColor = color
        onDismiss(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    systemBarColor = color
                    onDismiss(false)
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(
                    (dailyDetail.atchPath) +
                            (dailyDetail.dailyLifeFileList?.get(page)?.filePathNm ?: "") +
                            (dailyDetail.dailyLifeFileList?.get(page)?.atchFileNm ?: "")
                )
                .crossfade(true)
                .build(),
            contentDescription = "",
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    rotationZ = rotation,
                    translationX = if (rotate) -offset.y * scale else offset.x * scale,
                    translationY = if (rotate) offset.x * scale else offset.y * scale
                )
                // add transformable to listen to multitouch transformation events
                // after offset
                .transformable(state = state)
                .fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        Box(
            modifier = Modifier
                .padding(bottom = 20.dp)
                .clickable {
                    rotate = !rotate
                    offset = Offset.Zero
                }
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "사진 회전",
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 12.sp, color = design_white.copy(alpha = 0.5f),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

    }
}