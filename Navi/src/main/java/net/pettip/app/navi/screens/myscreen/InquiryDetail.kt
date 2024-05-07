package net.pettip.app.navi.screens.myscreen

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import net.pettip.app.navi.R
import net.pettip.app.navi.component.BackTopBar
import net.pettip.app.navi.component.CustomAlert
import net.pettip.app.navi.component.Toasty
import net.pettip.app.navi.screens.commuscreen.HtmlText
import net.pettip.app.navi.screens.walkscreen.FullScreenImage
import net.pettip.app.navi.screens.walkscreen.ZoomablePagerImage
import net.pettip.app.navi.ui.theme.design_DDDDDD
import net.pettip.app.navi.ui.theme.design_button_bg
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.CommunityViewModel
import net.pettip.app.navi.viewmodel.SettingViewModel
import net.pettip.app.navi.viewmodel.SharedViewModel
import net.pettip.data.bbs.QnaDetailData
import net.pettip.data.bbs.QnaDetailRes
import net.pettip.data.daily.DailyDetailData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InquiryDetail(navController: NavHostController, sharedViewModel: SharedViewModel,viewModel: CommunityViewModel, settingViewModel: SettingViewModel){

    val qnaDetail by viewModel.qnaDetail.collectAsState()
    var isModify by remember{ mutableStateOf(false) }
    var deleteDialog by remember{ mutableStateOf(false) }
    var qnaDelete by remember{ mutableStateOf(false) }
    var showImage by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val snackState = remember{SnackbarHostState()}

    val uriList: List<Uri>? = qnaDetail?.data?.get(0)?.files?.map {
        Uri.parse("${qnaDetail?.data?.get(0)?.atchPath}${it.filePathNm}${it.atchFileNm}")
    }
    var selectImage by remember{ mutableStateOf<Int>(0) }

    BackHandler {
        if (isModify){
            isModify = false
        }else if (showImage){
            showImage = false
        }else{
            navController.popBackStack()
        }
    }

    DisposableEffect(Unit){
        onDispose {
            viewModel.updateQnaDetail(null)
        }
    }

    LaunchedEffect(key1 = qnaDelete){
        if (qnaDelete){
            val result = viewModel.deleteQna()
            if (result){
                viewModel.updateQnaListInit(true)
                navController.popBackStack()
            }else{
                snackState.showSnackbar("다시 시도 해주세요")
                qnaDelete = false
            }
        }
    }

    Scaffold (
        topBar = { BackTopBar(title = stringResource(R.string.inquiry), navController = navController) },
        snackbarHost = { Toasty(snackState = snackState) }
    ) { paddingValues ->

        if (deleteDialog){
            CustomAlert(
                onDismiss = { newValue -> deleteDialog = newValue },
                confirm = stringResource(id = R.string.delete),
                dismiss = stringResource(id = R.string.cancel_kor),
                title = stringResource(R.string.inquiry_delete),
                text = stringResource(id = R.string.delete_confirm),
                confirmJob = { qnaDelete = true }
            )
        }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.primary)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = qnaDetail?.data?.get(0)?.pstTtl ?: "",
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 24.sp,
                letterSpacing = (-1.2).sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)
            )

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = qnaDetail?.data?.get(0)?.frstInptDt?: "",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp,
                    letterSpacing = (-0.7).sp,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(start = 20.dp, top = 8.dp)
                )

                if (qnaDetail?.data?.size !=2){
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 20.dp)
                    ){
                        Row (
                            modifier = Modifier.clickable {
                                isModify = true
                            },
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(
                                painter = painterResource(id = R.drawable.icon_daily),
                                contentDescription = "", tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .padding(start = 12.dp)
                                    .size(14.dp))

                            Text(
                                text = stringResource(id = R.string.modify_verb),
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                fontSize = 12.sp,
                                letterSpacing = (-0.6).sp,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                        Row (
                            modifier = Modifier.clickable {
                                deleteDialog = true
                            },
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "", tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .padding(start = 12.dp)
                                    .size(14.dp))

                            Text(
                                text = stringResource(id = R.string.delete),
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                fontSize = 12.sp,
                                letterSpacing = (-0.6).sp,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .padding(start = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(color = MaterialTheme.colorScheme.outline)
            )

            Text(
                text = qnaDetail?.data?.get(0)?.pstCn?: "",
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 14.sp, letterSpacing = (-0.7).sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp)
            )

            if (!uriList.isNullOrEmpty()){

                Text(
                    text = stringResource(R.string.attached_file),
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    fontSize = 14.sp, letterSpacing = (-0.7).sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 8.dp)
                )

                LazyRow(
                    state = rememberLazyListState(),
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    itemsIndexed(uriList){index,item ->
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clickable {
                                    selectImage = index
                                    showImage = true
                                }
                        ){
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(item)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "",
                                placeholder = null,
                                error= null,
                                modifier= Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                filterQuality = FilterQuality.None
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier
                .padding(top = 40.dp)
                .fillMaxWidth()
                .height(8.dp)
                .background(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )

            if (qnaDetail?.data?.size == 2){
                InquiryDetailAnswer(qnaDetail?.data?.get(1))
            }else{
                InquiryDetailNoAnswer()
            }
        }
    }

    AnimatedVisibility(
        visible = showImage,
        enter = scaleIn(transformOrigin = TransformOrigin(pivotFractionX = 0.5f, pivotFractionY = 0.5f)).plus(fadeIn()),
        exit = scaleOut(transformOrigin = TransformOrigin(pivotFractionX = 0.5f, pivotFractionY = 0.5f)).plus(fadeOut())
    ) {
        selectImage?.let {
            FullScreenImageInquiry(
                uriList = uriList,
                page = selectImage,
                onDismiss = { newValue -> showImage = newValue })
        }
    }


    AnimatedVisibility(
        visible = isModify,
        enter = slideInHorizontally(initialOffsetX = {it/2}) + fadeIn(),
        exit = slideOutHorizontally(targetOffsetX = {it/2}) + fadeOut()
    ) {
        ModifyInquiryScreen(
            navController = navController,
            sharedViewModel = sharedViewModel,
            viewModel = viewModel,
            settingViewModel = settingViewModel,
            onDismiss = {newValue -> isModify = newValue}
        )
    }
}

@Composable
fun InquiryDetailNoAnswer(){
    Column(
        modifier = Modifier
            .padding(top = 40.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(painter = painterResource(id = R.drawable.icon_bubble),
            contentDescription = "", tint = Color.Unspecified)

        Text(
            text = stringResource(R.string.preparing_response),
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = 14.sp, letterSpacing = (-0.7).sp,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(top = 12.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun InquiryDetailAnswer(answer: QnaDetailData?) {

    Column (
        modifier = Modifier.fillMaxWidth()
    ){
        Row (
            modifier = Modifier
                .padding(start = 20.dp, top = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(painter = painterResource(id = R.drawable.line_answer),
                contentDescription = "", tint = Color.Unspecified)

            Box (
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(36.dp)
                    .background(color = design_button_bg, shape = CircleShape)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "A",
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    fontSize = 20.sp,
                    color = design_white,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.padding(top = 16.dp))

        Text(
            text = answer?.pstTtl?: "",
            fontFamily = FontFamily(Font(R.font.pretendard_medium)),
            fontSize = 16.sp, letterSpacing = (-0.8).sp,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(start = 40.dp, end = 20.dp)
        )
        //
        //Spacer(modifier = Modifier.padding(top = 16.dp))
        //
        //Text(
        //    text = "안녕하세요. 케어펫입니다.\n\n문의주신 휴대폰 번호는 홈 > 마이페이지 > 설정 > 개인정보수정에서 변경 가능합니다.\n\n감사합니다!",
        //    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
        //    fontSize = 14.sp, letterSpacing = (-0.7).sp,
        //    color = design_login_text,
        //    modifier = Modifier.padding(start = 40.dp, end = 20.dp)
        //)

        HtmlText(htmlString = answer?.pstCn?:"", modifier = Modifier.padding(top = 20.dp, start = 40.dp, end = 40.dp))
    }// col
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FullScreenImageInquiry(
    uriList:List<Uri>?,
    page:Int,
    onDismiss: (Boolean) -> Unit
) {
    //var systemBarColor by remember { mutableStateOf(Color.Black) }
    //val color = MaterialTheme.colorScheme.primary
    //
    //val systemUiController = rememberSystemUiController()
    //systemUiController.setSystemBarsColor(color = systemBarColor)
    //
    //var rotate by remember { mutableStateOf(false) }
    //var scale by remember { mutableFloatStateOf(1f) }
    //val rotation: Float by animateFloatAsState(if (rotate) 90f else 0f, label = "")
    //var offset by remember { mutableStateOf(Offset.Zero) }
    //val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
    //    scale *= zoomChange
    //    //rotation += rotationChange
    //    offset = if (scale <= 1f) {
    //        Offset.Zero
    //    } else {
    //        offset + offsetChange
    //    }
    //}
    //
    //BackHandler {
    //    systemBarColor = color
    //    onDismiss(false)
    //}
    //
    //Box(
    //    modifier = Modifier
    //        .fillMaxSize()
    //        .background(Color.Black)
    //        .clickable(
    //            interactionSource = remember { MutableInteractionSource() },
    //            indication = null,
    //            onClick = {
    //                systemBarColor = color
    //                onDismiss(false)
    //            }
    //        ),
    //    contentAlignment = Alignment.Center
    //) {
    //    AsyncImage(
    //        model = ImageRequest.Builder(LocalContext.current)
    //            .data(uri)
    //            .crossfade(true)
    //            .build(),
    //        contentDescription = "",
    //        modifier = Modifier
    //            .graphicsLayer(
    //                scaleX = scale,
    //                scaleY = scale,
    //                rotationZ = rotation,
    //                translationX = if (rotate) -offset.y * scale else offset.x * scale,
    //                translationY = if (rotate) offset.x * scale else offset.y * scale
    //            )
    //            // add transformable to listen to multitouch transformation events
    //            // after offset
    //            .transformable(state = state)
    //            .fillMaxSize(),
    //        contentScale = ContentScale.Fit
    //    )
    //
    //    Box(
    //        modifier = Modifier
    //            .padding(bottom = 20.dp)
    //            .clickable {
    //                rotate = !rotate
    //                offset = Offset.Zero
    //            }
    //            .align(Alignment.BottomCenter),
    //        contentAlignment = Alignment.Center
    //    ) {
    //        Text(
    //            text = "사진 회전",
    //            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
    //            fontSize = 12.sp, color = design_white.copy(alpha = 0.5f),
    //            modifier = Modifier.padding(horizontal = 8.dp)
    //        )
    //    }
    //
    //}
    var systemBarColor by remember { mutableStateOf(Color.Black) }
    val color = MaterialTheme.colorScheme.primary

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color = systemBarColor)

    val pagerState = rememberPagerState(pageCount = { uriList?.size ?:0 },initialPage = page)
    val scrollEnabled = remember { mutableStateOf(true) }
    var lastClickTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    BackHandler {
        systemBarColor = color
        onDismiss(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
        //.clickable(
        //    interactionSource = remember { MutableInteractionSource() },
        //    indication = null,
        //    onClick = {
        //        systemBarColor = color
        //        onDismiss(false)
        //    }
        //)
        ,
        contentAlignment = Alignment.Center
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize(),
            beyondBoundsPageCount = 0,
            userScrollEnabled = scrollEnabled.value
        ) {
            ZoomablePagerImage(
                imageUri = uriList?.get(it)?.toString() ?:"",
                scrollEnabled = scrollEnabled
            )
        }

        Box(
            modifier = Modifier
                .padding(bottom = 20.dp)
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.Center
        ) {
            Row(
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(uriList?.size ?: 0 ) { iteration ->
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
        }

        Box(
            modifier = Modifier
                .padding(top = 12.dp, start = 8.dp)
                .size(30.dp)
                .clip(shape = CircleShape)
                .align(Alignment.TopStart)
                .clickable {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastClickTime >= 500) {
                        systemBarColor = color
                        onDismiss(false)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_back),
                contentDescription = "",
                tint = design_white
            )
        }
    }
}