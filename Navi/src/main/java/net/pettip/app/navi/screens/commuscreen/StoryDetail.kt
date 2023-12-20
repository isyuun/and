package net.pettip.app.navi.screens.commuscreen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationInstance
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.component.BackTopBar
import net.pettip.app.navi.component.CircleImageTopBar
import net.pettip.app.navi.component.DclrDialog
import net.pettip.app.navi.component.ErrorScreen
import net.pettip.app.navi.component.LoadingAnimation1
import net.pettip.app.navi.component.LoadingAnimation3
import net.pettip.app.navi.component.LoadingDialog
import net.pettip.app.navi.screens.mainscreen.calculateCurrentOffsetForPage
import net.pettip.app.navi.screens.mainscreen.metersToKilometers
import net.pettip.app.navi.screens.myscreen.CustomDialogDelete
import net.pettip.app.navi.screens.myscreen.isValidFloat
import net.pettip.app.navi.screens.walkscreen.FullScreenImage
import net.pettip.app.navi.ui.theme.design_DDDDDD
import net.pettip.app.navi.ui.theme.design_btn_border
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_login_bg
import net.pettip.app.navi.ui.theme.design_login_text
import net.pettip.app.navi.ui.theme.design_placeHolder
import net.pettip.app.navi.ui.theme.design_sharp
import net.pettip.app.navi.ui.theme.design_skip
import net.pettip.app.navi.ui.theme.design_textFieldOutLine
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.CommunityViewModel
import net.pettip.app.navi.viewmodel.SharedViewModel
import net.pettip.data.daily.Cmnt
import net.pettip.data.daily.DailyDetailData
import net.pettip.singleton.G
import net.pettip.util.Log
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Properties
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun StoryDetail(viewModel: CommunityViewModel, sharedViewModel: SharedViewModel, navController:NavHostController){

    val comment by viewModel.comment.collectAsState()
    val storyDetail by viewModel.storyDetail.collectAsState()
    val cmntList by viewModel.cmntList.collectAsState()
    val storyLoading by viewModel.storyLoading.collectAsState()
    val replyCmnt by viewModel.replyCmnt.collectAsState()
    val lastSchUnqNo by viewModel.lastPstSn.collectAsState()

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
    var isLoading by remember{ mutableStateOf(false) }
    var rcmdtnLoading by remember{ mutableStateOf(false) }
    var cmntExpanded by remember{ mutableStateOf(true) }
    var onReply by remember{ mutableStateOf(false) }
    var isModify by remember{ mutableStateOf(false) }
    var dclrDialogOpen by remember{ mutableStateOf(false) }
    var expanded by remember{ mutableStateOf(false) }
    var refresh by remember{ mutableStateOf(false) }

    var replyText by remember{ mutableStateOf("") }
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(refresh){
        if (refresh){
            val result = lastSchUnqNo?.let { viewModel.getStoryDetail(it) }
            refresh = if (result == true){
                false
            }else{
                false
            }
        }
    }

    BackHandler {
        if (isModify){
            isModify = false
        }else if (showImage){
            showImage = false
        }else if (onReply){
            onReply =false
        }
        else{
            navController.popBackStack()
        }
    }

    val upCmntNo0:List<Cmnt> = cmntList?.filter { cmnt ->
        cmnt.upCmntNo == cmnt.cmntNo } ?: emptyList()

    LaunchedEffect(key1 = onReply){
        if (onReply){
            focusManager.clearFocus()
            delay(300)
            focusRequester.requestFocus()
        } else{
            delay(300)
            viewModel.updateReplyCmnt(null)
            replyText = ""
        }
    }

    DisposableEffect(Unit){
        onDispose {
            viewModel.updateStoryDetail(null)
            viewModel.updateCmntList(null)
            viewModel.updateComment("")
        }
    }

    Scaffold (
        topBar = {BackTopBar(title = stringResource(R.string.title_story), navController = navController)}
    ){ paddingValues ->

        if (dclrDialogOpen){
            DclrDialog(
                viewModel = viewModel,
                expanded = expanded,
                expandChange = {newValue -> expanded = newValue},
                onDismiss ={newValue -> dclrDialogOpen = newValue}
            )
        }

        Crossfade(
            targetState = !storyLoading && storyDetail==null,
            label = "에러페이지"
        ){
            when(it){
                true ->
                    ErrorScreen(onClick = { refresh = true })
                false ->
                    Column (
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                            .background(color = MaterialTheme.colorScheme.primary)
                            .verticalScroll(rememberScrollState())
                    ){
                        Spacer(modifier = Modifier.padding(top = 20.dp))

                        StoryDetailTopContent(story,viewModel,navController, modifyChange = {newValue -> isModify = newValue}, dclrDialogOpen = {newValue -> dclrDialogOpen = newValue})

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
                                .background(color = MaterialTheme.colorScheme.outline)
                        )

                        Crossfade(
                            targetState = storyLoading,
                            label = "",
                        ) {
                            when(!it){
                                true ->
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .animateContentSize() // 이 부분을 추가하여 크기 변화를 자연스럽게 만듭니다.
                                    ) {
                                        if (story?.dailyLifeFileList != null){
                                            PagerImage(showImage = {newValue -> showImage = newValue}, pagerState, viewModel)
                                        }

                                        AnimatedVisibility(
                                            visible = isWalk==true,
                                            enter = fadeIn(tween(durationMillis = 700, delayMillis = 200)).plus(expandVertically()),
                                            exit = fadeOut(tween(durationMillis = 700, delayMillis = 200)).plus(shrinkVertically())
                                        ) {
                                            StoryDetailTime(story)
                                        }

                                        if ( !(story?.schCn == " " || story?.schCn =="") ){
                                            Text(
                                                text = story?.schCn ?: "",
                                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                                fontSize = 14.sp,
                                                letterSpacing = (-0.7).sp,
                                                color = MaterialTheme.colorScheme.onPrimary,
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
                                    }
                                false ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(80.dp),
                                        contentAlignment = Alignment.Center
                                    ){
                                        LoadingAnimation1(circleColor = design_intro_bg)
                                    }
                            }
                        }

                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.onPrimaryContainer)
                                .height(36.dp)
                                .clickable { cmntExpanded = !cmntExpanded }
                            ,
                            verticalAlignment = Alignment.CenterVertically
                        ){

                            Icon(
                                painter = painterResource(id = R.drawable.icon_comment_line),
                                contentDescription = "", tint = Color.Unspecified,
                                modifier = Modifier.padding(start = 20.dp))

                            Text(
                                text = "댓글 ${cmntList?.size?:0}",
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                fontSize = 12.sp,
                                letterSpacing = (-0.6).sp,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }

                        AnimatedVisibility(
                            visible = cmntExpanded && upCmntNo0.isNotEmpty(),
                            enter = expandVertically(tween(durationMillis = 300)).plus(fadeIn()),
                            exit = shrinkVertically(tween(durationMillis = 300)).plus(fadeOut())
                        ) {
                            LazyColumn(
                                state = rememberLazyListState(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 1000.dp),
                                contentPadding = PaddingValues(top = 18.dp, bottom = 20.dp)
                            ){
                                itemsIndexed(upCmntNo0){ index, item ->
                                    CommentListItem(
                                        comment = item,
                                        viewModel = viewModel, onReply,
                                        onReplyChange = {newValue -> onReply = newValue},
                                        dclrDialogOpen = {newValue -> dclrDialogOpen = newValue })

                                    if(index != (upCmntNo0?.size?.minus(1) ?: 0)){
                                        Spacer(modifier = Modifier
                                            .padding(vertical = 16.dp, horizontal = 20.dp)
                                            .fillMaxWidth()
                                            .height(1.dp)
                                            .background(color = MaterialTheme.colorScheme.outline))
                                    }
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible =  onReply,
                            enter = expandVertically(expandFrom = Alignment.Top),
                            exit = shrinkVertically(shrinkTowards = Alignment.Top)
                        ) {
                            Row (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color = design_intro_bg),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ){
                                Row (
                                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                                    verticalAlignment = Alignment.Bottom
                                ){
                                    Text(
                                        text = "${replyCmnt?.petNm+"에게"} 답글 쓰는중",
                                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                        fontSize = 14.sp, letterSpacing = (-0.7).sp,
                                        color = design_white,
                                        lineHeight = 14.sp,
                                        modifier = Modifier.padding(start = 20.dp, end = 8.dp)
                                    )

                                    LoadingAnimation3(circleSize = 4.dp, circleColor = design_white, animationDelay = 600)
                                }

                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "", tint = design_white,
                                    modifier = Modifier
                                        .padding(end = 20.dp)
                                        .clickable { onReply = false })
                            }

                            Spacer(modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(MaterialTheme.colorScheme.onPrimary))
                        }

                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MaterialTheme.colorScheme.onPrimary))

                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            TextField(
                                value = if (onReply) replyText else comment,
                                onValueChange = {
                                    if (onReply){
                                        replyText = it
                                    }else{
                                        viewModel.updateComment(it)
                                    }
                                },
                                textStyle = TextStyle(
                                    color = MaterialTheme.colorScheme.onPrimary,
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
                                keyboardActions = KeyboardActions {

                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .focusRequester(focusRequester),
                                colors = TextFieldDefaults.colors(
                                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
                                    focusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                                    focusedContainerColor = MaterialTheme.colorScheme.primary,
                                    unfocusedLeadingIconColor = MaterialTheme.colorScheme.primaryContainer,
                                    focusedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
                                    cursorColor = design_intro_bg.copy(alpha = 0.5f),
                                    selectionColors = TextSelectionColors(
                                        handleColor = design_intro_bg.copy(alpha = 0.5f),
                                        backgroundColor = design_intro_bg.copy(alpha = 0.5f)
                                    ),
                                    focusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            )

                            Box(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(width = 56.dp, height = 40.dp)
                                    .background(color = MaterialTheme.colorScheme.tertiaryContainer, shape = RoundedCornerShape(12.dp))
                                    .clip(shape = RoundedCornerShape(12.dp))
                                    .clickable(
                                        enabled = if (onReply) replyText != "" && !isLoading else comment != "" && !isLoading,
                                        onClick = {
                                            viewModel.viewModelScope.launch {
                                                isLoading = true

                                                if (onReply) {
                                                    val result = viewModel.uploadComment(replyText)
                                                    if (result) {
                                                        replyText = ""
                                                        onReply = false
                                                        isLoading = false
                                                    } else {
                                                        isLoading = false
                                                        Toast
                                                            .makeText(context, "댓글 등록에 실패했습니다", Toast.LENGTH_SHORT)
                                                            .show()
                                                    }
                                                } else {
                                                    val result = viewModel.uploadComment()
                                                    if (result) {
                                                        viewModel.updateComment("")
                                                        isLoading = false
                                                    } else {
                                                        isLoading = false
                                                        Toast
                                                            .makeText(context, "댓글 등록에 실패했습니다", Toast.LENGTH_SHORT)
                                                            .show()
                                                    }
                                                }
                                            }
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ){
                                if (isLoading){
                                    LoadingAnimation3(
                                        circleColor = MaterialTheme.colorScheme.onTertiaryContainer,
                                        circleSize = 4.dp
                                    )
                                }else{
                                    Text(text = stringResource(R.string.comment_apply), style = TextStyle(
                                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                        fontSize = 14.sp,
                                        letterSpacing = (-0.7).sp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .padding(end = 4.dp)
                                    .size(40.dp)
                                    .background(color = Color.Transparent, shape = RoundedCornerShape(12.dp))
                                    .clip(shape = RoundedCornerShape(12.dp))
                                    .border(1.dp, color = MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(12.dp))
                                    .clickable(
                                        enabled = story?.myRcmdtn == null && !rcmdtnLoading
                                    ) {
                                        viewModel.viewModelScope.launch {
                                            rcmdtnLoading = true
                                            val result = viewModel.rcmdtnDaily(rcmdtnSeCd = "001", schUnqNo = story?.schUnqNo ?: 0)
                                            if (result) {
                                                rcmdtnLoading = true
                                            } else {
                                                rcmdtnLoading = true
                                                Toast
                                                    .makeText(context, "좋아요 실패", Toast.LENGTH_SHORT)
                                                    .show()
                                            }
                                        }
                                    }
                                ,
                                contentAlignment = Alignment.Center
                            ){
                                Icon(painter = painterResource(
                                    id = if (story?.myRcmdtn == "001") R.drawable.icon_like else R.drawable.icon_like_default),
                                    contentDescription = "", tint = Color.Unspecified)
                            }

                            Box(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(40.dp)
                                    .background(color = Color.Transparent, shape = RoundedCornerShape(12.dp))
                                    .clip(shape = RoundedCornerShape(12.dp))
                                    .border(1.dp, color = MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(12.dp))
                                    .clickable(
                                        enabled = story?.myRcmdtn == null && !rcmdtnLoading
                                    ) {
                                        viewModel.viewModelScope.launch {
                                            rcmdtnLoading = true
                                            val result = viewModel.rcmdtnDaily(rcmdtnSeCd = "002", schUnqNo = story?.schUnqNo ?: 0)
                                            if (result) {
                                                rcmdtnLoading = true
                                            } else {
                                                rcmdtnLoading = true
                                                Toast
                                                    .makeText(context, "싫어요 실패", Toast.LENGTH_SHORT)
                                                    .show()
                                            }
                                        }
                                    }
                                ,
                                contentAlignment = Alignment.Center
                            ){
                                Icon(painter = painterResource(
                                    id = if (story?.myRcmdtn == "002") R.drawable.icon_dislike else R.drawable.icon_dislike_default),
                                    contentDescription = "", tint = Color.Unspecified)
                            }
                        }
                    }// col
            }
        }
    }
    AnimatedVisibility(
        visible = isModify,
        enter = slideInHorizontally(initialOffsetX = {it/2})+ fadeIn(),
        exit = slideOutHorizontally(targetOffsetX = {it/2})+ fadeOut()
    ) {
        DailyModifyScreen(viewModel = viewModel, sharedViewModel = sharedViewModel, navController = navController, onBack = {newValue -> isModify = newValue})
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryDetailTopContent(
    story: DailyDetailData?,
    viewModel: CommunityViewModel,
    navcontroller:NavHostController,
    modifyChange:(Boolean) ->Unit,
    dclrDialogOpen:(Boolean) ->Unit
) {

    var deleteDialog by remember { mutableStateOf(false) }
    var storyDelete by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var rlsUpdateLoading by remember{ mutableStateOf(false) }
    val context = LocalContext.current

    var animatedValue by remember { mutableFloatStateOf(0f) }
    var booleanValue by remember{ mutableStateOf(false) }

    val targetValue = if (booleanValue) 59f else 7f
    animatedValue = animateFloatAsState(
        targetValue = targetValue,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "").value

    if (deleteDialog){
        CustomDialogDelete(
            onDismiss = { newValue -> deleteDialog = newValue },
            confirm = "삭제하기",
            dismiss = "취소",
            title = "게시글 삭제하기",
            text = "정말 삭제하시겠어요?",
            valueChange = { newValue -> storyDelete = newValue}
        )
    }

    if (loading){
        LoadingDialog(
            loadingText = "삭제중..",
            loadingState = loading
        )
    }

    LaunchedEffect(key1 = story){
        if (story != null){
            booleanValue = story.rlsYn != "Y"
        }
    }

    LaunchedEffect(key1 = storyDelete){
        if (storyDelete){
            loading = true
            Log.d("LOG","delete 진입")
            val result = viewModel.updateDaily(delYn = "Y")
            if (!result){
                Log.d("LOG","결과 진입")
                loading = false
                Toast.makeText(context, "다시 시도해주세요", Toast.LENGTH_SHORT).show()
            }else{

                viewModel.updateStoryListClear()
                viewModel.updateStoryPage(1)
                val getList = viewModel.getStoryList(1)
                if (getList){
                    loading = false
                    navcontroller.popBackStack()
                }else{
                    loading = false
                    navcontroller.popBackStack()
                }


            }
        }
        storyDelete = false
    }

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
            color = MaterialTheme.colorScheme.onPrimary
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
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Text(
                    text = story?.rlsDt ?: "",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 12.sp,
                    letterSpacing = (-0.6).sp, lineHeight = 12.sp,
                    color = MaterialTheme.colorScheme.secondary
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
                Icon(painter = painterResource(
                    id = if (story?.myRcmdtn == "001") R.drawable.icon_like else R.drawable.icon_like_default),
                    contentDescription = "", tint = Color.Unspecified)

                Text(
                    text = story?.rcmdtnCnt?.toString() ?: "0",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 12.sp,
                    letterSpacing = (-0.6).sp,
                    color = MaterialTheme.colorScheme.secondary,
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
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(start = 4.dp)
                )

                AnimatedVisibility(
                    visible = G.userId == story?.userId
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Box {
                            Row (
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .padding(start = 12.dp)
                                    .clip(shape = RoundedCornerShape(2.dp))
                                    .clickable(
                                        enabled = !rlsUpdateLoading && booleanValue,
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = CustomIndication(
                                            alpha = 0.3f,
                                            pressColor = design_textFieldOutLine
                                        )
                                    ) {
                                        rlsUpdateLoading = true
                                        viewModel.viewModelScope.launch {
                                            val result = viewModel.updateDailyRls("Y")
                                            if (result) {
                                                rlsUpdateLoading = false
                                                Toast
                                                    .makeText(context, "공개처리 되었습니다", Toast.LENGTH_SHORT)
                                                    .show()
                                            } else {
                                                rlsUpdateLoading = false
                                                Toast
                                                    .makeText(context, "다시 시도해주세요", Toast.LENGTH_SHORT)
                                                    .show()
                                            }
                                        }
                                    }

                            ){
                                Icon(
                                    painter = painterResource(id = R.drawable.icon_view),
                                    contentDescription = "", tint = Color.Unspecified,
                                    modifier = Modifier
                                        .size(14.dp))
                                Text(
                                    text = "공개",
                                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                    fontSize = 12.sp,
                                    letterSpacing = (-0.6).sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            }
                            Icon(imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "", tint = design_sharp.copy(alpha = 0.6f),
                                modifier = Modifier.offset(x = animatedValue.dp, y = (-12).dp)
                            )
                        }


                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .clickable(
                                    enabled = !rlsUpdateLoading && !booleanValue,
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = CustomIndication(
                                        alpha = 0.3f,
                                        pressColor = design_textFieldOutLine
                                    )
                                ) {
                                    rlsUpdateLoading = true
                                    viewModel.viewModelScope.launch {
                                        val result = viewModel.updateDailyRls("N")
                                        if (result) {
                                            rlsUpdateLoading = false
                                            Toast
                                                .makeText(context, "비공개처리 되었습니다", Toast.LENGTH_SHORT)
                                                .show()
                                        } else {
                                            rlsUpdateLoading = false
                                            Toast
                                                .makeText(context, "다시 시도해주세요", Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                    }
                                }
                        ){
                            Icon(
                                painter = painterResource(id = R.drawable.icon_password),
                                contentDescription = "", tint = Color.Unspecified,
                                modifier = Modifier
                                    .size(14.dp))
                            Text(
                                text = "비공개",
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                fontSize = 12.sp,
                                letterSpacing = (-0.6).sp,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }
            }

            if (story?.userId == G.userId){
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Row (
                        modifier = Modifier

                            .clickable (
                                interactionSource = remember{ MutableInteractionSource() },
                                indication = CustomIndication(
                                    alpha = 0.3f,
                                    pressColor = design_textFieldOutLine
                                )
                            ){ modifyChange(true) },
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            painter = painterResource(id = R.drawable.icon_daily),
                            contentDescription = "", tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .size(14.dp))

                        Text(
                            text = "수정하기",
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
                            text = "삭제하기",
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = 12.sp,
                            letterSpacing = (-0.6).sp,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .padding(start = 4.dp)
                        )
                    }
                }
            }else{
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable (
                            interactionSource = remember{ MutableInteractionSource() },
                            indication = CustomIndication(
                                alpha = 0.3f,
                                pressColor = design_textFieldOutLine
                            )
                        ){
                            dclrDialogOpen(true)
                        }
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.icon_report),
                        contentDescription = "", tint = Color.Unspecified,
                        modifier = Modifier.padding(start = 12.dp))

                    Text(
                        text = "신고하기",
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 12.sp,
                        letterSpacing = (-0.6).sp,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerImage(showImage: (Boolean) -> Unit, pagerState: PagerState, viewModel: CommunityViewModel) {

    val story by viewModel.storyDetail.collectAsState()
    val isLoading by viewModel.storyLoading.collectAsState()

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
                                        story?.data?.atchPath+
                                                story?.data?.dailyLifeFileList?.get(page)?.filePathNm +
                                                (story?.data?.dailyLifeFileList?.get(page)?.atchFileNm )
                                    )
                                    .crossfade(400)
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
                        repeat(story?.data?.dailyLifeFileList?.size ?: 0 ) { iteration ->
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
        .padding(bottom = 20.dp)
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
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(start = 20.dp)
            )

            Text(
                text = story?.walkDptreDt?.let { formatDateString(it) }?:"",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                letterSpacing = 0.sp,
                modifier = Modifier.padding(top = 4.dp, start = 20.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(modifier = Modifier
            .size(1.dp, 40.dp)
            .background(color = MaterialTheme.colorScheme.outline))

        Column (
            modifier= Modifier
                .weight(1f)
        ){

            Text(
                text = stringResource(R.string.story_walk_time),
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                letterSpacing = (-0.7).sp,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(start = 20.dp)
            )

            Text(
                text = story?.runTime ?: "",
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
            .background(color = MaterialTheme.colorScheme.outline))

        Column (
            modifier= Modifier
                .weight(1f)
        ){

            Text(
                text = stringResource(R.string.story_walk_dist),
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                letterSpacing = (-0.7).sp,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(start = 20.dp)
            )

            Text(
                text = metersToKilometers(story?.runDstnc?:0)+"km",
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun CommentListItem(
    comment: Cmnt,
    viewModel: CommunityViewModel,
    onReply: Boolean,
    onReplyChange: (Boolean)->Unit,
    dclrDialogOpen:(Boolean) ->Unit
){

    val cmntList by viewModel.cmntList.collectAsState()
    val step2CmntList:List<Cmnt> = cmntList?.filter { cmnt ->
        cmnt.upCmntNo == comment.cmntNo } ?: emptyList()

    val storyDetail by viewModel.storyDetail.collectAsState()
    val atchPath = storyDetail?.data?.atchPath ?: ""
    var expand by remember { mutableStateOf(false) }
    var step2Expand by remember { mutableStateOf(false) }

    var deleteDialog by remember{ mutableStateOf(false) }
    var commentDelete by remember{ mutableStateOf(false) }
    var openBottomSheet by remember{ mutableStateOf(false) }

    var updateComment by remember{ mutableStateOf(comment.cmntCn) }
    var updateLoading by remember { mutableStateOf(false) }
    var rcmdtnLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = comment.cmntCn){
        updateComment = comment.cmntCn
    }

    LaunchedEffect(key1 = cmntList){
        if (comment.cmntNo == viewModel.replyCmnt.value?.cmntNo){
            step2Expand = true
        }
    }

    if (deleteDialog){
        CustomDialogDelete(
            onDismiss = { newValue -> deleteDialog = newValue },
            confirm = "삭제하기",
            dismiss = "취소",
            title = "댓글 삭제하기",
            text = "정말 삭제하시겠어요?",
            valueChange = { newValue -> commentDelete = newValue}
        )
    }

    if (openBottomSheet){
        
        AlertDialog(
            onDismissRequest = { openBottomSheet = false},
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Column (
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
            ) {
                Spacer(modifier = Modifier.padding(top = 20.dp))

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "댓글 수정하기",
                        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                        fontSize = 20.sp,
                        letterSpacing = (-1.0).sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(start = 20.dp, bottom = 20.dp)
                    )

                    Text(
                        text = "취소",
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 14.sp,
                        letterSpacing = (-0.7).sp,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .padding(end = 30.dp, bottom = 20.dp)
                            .clickable { openBottomSheet = false }
                        ,
                        textDecoration = TextDecoration.Underline
                    )
                }

                Card (
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(3.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
                ){
                    Row (
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 10.dp)
                            .fillMaxWidth()
                    ){
                        CircleImageTopBar(size = 40, imageUri = "${atchPath}${comment.petImg}" )

                        Spacer(modifier = Modifier.padding(start = 8.dp))

                        Column (
                            modifier = Modifier.fillMaxWidth()
                        ){
                            Row (
                                verticalAlignment = Alignment.Bottom
                            ){
                                Text(
                                    text = comment.petNm,
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                    letterSpacing = (-0.7).sp,
                                    color = if (G.userId == comment.userId) design_intro_bg else design_skip
                                )

                                Text(
                                    text = comment.lastStrgDt,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                    letterSpacing = (-0.7).sp,
                                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                                    modifier = Modifier.padding(start = 5.dp)
                                )
                            }

                            Text(
                                text = comment.cmntCn,
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                fontSize = 14.sp,
                                letterSpacing = (-0.7).sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }



                Spacer(modifier = Modifier
                    .padding(top = 20.dp))

                Row (
                    modifier = Modifier
                        .padding(start = 20.dp, end = 10.dp, bottom = 20.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    TextField(
                        value = updateComment,
                        onValueChange = { updateComment = it},
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimary,
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
                        modifier = Modifier
                            .weight(1f),
                        colors = TextFieldDefaults.colors(
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
                            focusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                            focusedContainerColor = MaterialTheme.colorScheme.primary,
                            unfocusedLeadingIconColor = MaterialTheme.colorScheme.primaryContainer,
                            focusedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
                            cursorColor = design_intro_bg.copy(alpha = 0.5f),
                            selectionColors = TextSelectionColors(
                                handleColor = design_intro_bg.copy(alpha = 0.5f),
                                backgroundColor = design_intro_bg.copy(alpha = 0.5f)
                            ),
                            focusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "", tint = design_skip,
                                modifier = Modifier
                                    .size(14.dp)
                                    .clickable { updateComment = "" }
                            )
                        }
                    )

                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(width = 56.dp, height = 40.dp)
                            .background(color = MaterialTheme.colorScheme.tertiaryContainer, shape = RoundedCornerShape(12.dp))
                            .clip(shape = RoundedCornerShape(12.dp))
                            .clickable(
                                enabled = updateComment != "" && !updateLoading,
                                onClick = {
                                    viewModel.viewModelScope.launch {
                                        updateLoading = true
                                        val result = viewModel.updateComment(updateComment, comment.cmntNo)
                                        if (result) {
                                            updateComment = ""
                                            updateLoading = false
                                            Toast
                                                .makeText(context, "댓글이 수정되었습니다", Toast.LENGTH_SHORT)
                                                .show()
                                        } else {
                                            updateLoading = false
                                            Toast
                                                .makeText(context, "댓글 수정에 실패했습니다", Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                    }
                                }

                            ),
                        contentAlignment = Alignment.Center
                    ){
                        if (updateLoading){
                            LoadingAnimation3(
                                circleColor = MaterialTheme.colorScheme.primary,
                                circleSize = 4.dp
                            )
                        }else{
                            Text(text = "수정", style = TextStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                fontSize = 14.sp,
                                letterSpacing = (-0.7).sp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = commentDelete){
        if (commentDelete){
            val result = viewModel.deleteComment(comment.cmntNo)
            if (!result){
                Toast.makeText(context, "다시 시도해주세요", Toast.LENGTH_SHORT).show()
            }
        }

        commentDelete = false
    }

    Column {
        Row (
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
        ){
            CircleImageTopBar(size = 40, imageUri = "${atchPath}${comment.petImg}" )

            Spacer(modifier = Modifier.padding(start = 8.dp))

            Column (
                modifier = Modifier.fillMaxWidth()
            ){
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Row (
                        verticalAlignment = Alignment.Bottom
                    ){
                        Text(
                            text = comment.petNm,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            letterSpacing = (-0.7).sp,
                            color = if (G.userId == comment.userId) design_intro_bg else MaterialTheme.colorScheme.secondary
                        )

                        Text(
                            text = comment.lastStrgDt,
                            fontSize = 10.sp,
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            letterSpacing = (-0.7).sp,
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }


                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Box(contentAlignment = Alignment.CenterEnd){

                            androidx.compose.animation.AnimatedVisibility(
                                visible = !expand,
                                enter = fadeIn() + slideInVertically(),
                                exit = fadeOut() + slideOutVertically(targetOffsetY = {it/2})
                            ) {
                                Row (verticalAlignment = Alignment.CenterVertically){
                                    Row (verticalAlignment = Alignment.CenterVertically){
                                        Icon(
                                            painter = painterResource(id = if (comment.myCmntRcmdtn == "001" ) R.drawable.icon_like else R.drawable.icon_like_default),
                                            contentDescription = "", tint = Color.Unspecified,
                                            modifier = Modifier
                                                .padding(start = 12.dp)
                                                .clickable(
                                                    enabled = comment.myCmntRcmdtn == null && !rcmdtnLoading,
                                                    onClick = {
                                                        viewModel.viewModelScope.launch {
                                                            rcmdtnLoading = true
                                                            val result = viewModel.rcmdtnComment(cmntNo = comment.cmntNo, rcmdtnSeCd = "001", schUnqNo = storyDetail?.data?.schUnqNo ?: 0)
                                                            if (result) {
                                                                rcmdtnLoading = false
                                                            } else {
                                                                rcmdtnLoading = false
                                                                Toast
                                                                    .makeText(context, "다시 시도해주세요", Toast.LENGTH_SHORT)
                                                                    .show()
                                                            }
                                                        }
                                                    },
                                                    indication = rememberRipple(
                                                        bounded = true,
                                                        radius = 8.dp,
                                                        color = design_sharp
                                                    ),
                                                    interactionSource = remember { MutableInteractionSource() }
                                                )
                                        )

                                        Text(text = "${comment.rcmdtnCnt}",
                                            style = TextStyle(
                                                color = MaterialTheme.colorScheme.secondary,
                                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                                fontSize = 12.sp,
                                                letterSpacing = (-0.6).sp),
                                            textAlign = TextAlign.Center,
                                            lineHeight = 12.sp,
                                            modifier = Modifier.padding(start = 4.dp)
                                        )
                                    }

                                    Icon(
                                        painter = painterResource(id = R.drawable.icon_comment_line),
                                        contentDescription = "", tint = Color.Unspecified,
                                        modifier = Modifier
                                            .padding(start = 12.dp)
                                            .clickable(
                                                onClick = {
                                                    onReplyChange(true)
                                                    viewModel.updateReplyCmnt(comment)
                                                },
                                                indication = rememberRipple(
                                                    bounded = true,
                                                    radius = 8.dp,
                                                    color = design_login_text
                                                ),
                                                interactionSource = remember { MutableInteractionSource() }
                                            )
                                    )
                                }
                            }

                            androidx.compose.animation.AnimatedVisibility(
                                visible = expand,
                                enter = fadeIn() + slideInVertically(),
                                exit = fadeOut() + slideOutVertically(targetOffsetY = {it/2})
                            ){
                                if (G.userId == comment.userId){
                                    Row (verticalAlignment = Alignment.CenterVertically){
                                        Text(
                                            text = "수정",
                                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                            fontSize = 12.sp, letterSpacing = (-0.6).sp,
                                            color = MaterialTheme.colorScheme.secondary,
                                            textDecoration = if (comment.delYn == "Y" || comment.bldYn == "Y") TextDecoration.LineThrough else TextDecoration.None,
                                            modifier = Modifier
                                                .padding(start = 12.dp)
                                                .clickable(
                                                    enabled = comment.delYn == "N" && comment.bldYn == "N"
                                                ) {
                                                    focusManager.clearFocus()
                                                    openBottomSheet = true
                                                }
                                        )
                                        Text(
                                            text = "삭제",
                                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                            fontSize = 12.sp, letterSpacing = (-0.6).sp,
                                            color = MaterialTheme.colorScheme.secondary,
                                            textDecoration = if (comment.delYn == "Y" || comment.bldYn == "Y") TextDecoration.LineThrough else TextDecoration.None,
                                            modifier = Modifier
                                                .padding(start = 12.dp)
                                                .clickable(
                                                    enabled = comment.delYn == "N" && comment.bldYn == "N"
                                                ) { deleteDialog = true }
                                        )
                                    }
                                }else{
                                    Row (verticalAlignment = Alignment.CenterVertically){
                                        Row (
                                            verticalAlignment = Alignment.CenterVertically
                                        ){
                                            Icon(
                                                painter = painterResource(id = if (comment.myCmntRcmdtn == "002" ) R.drawable.icon_dislike else R.drawable.icon_dislike_default),
                                                contentDescription = "", tint = Color.Unspecified,
                                                modifier = Modifier
                                                    .padding(start = 12.dp)
                                                    .clickable(
                                                        enabled = comment.myCmntRcmdtn == null && !rcmdtnLoading,
                                                        onClick = {
                                                            viewModel.viewModelScope.launch {
                                                                rcmdtnLoading = true
                                                                val result = viewModel.rcmdtnComment(cmntNo = comment.cmntNo, rcmdtnSeCd = "002", schUnqNo = storyDetail?.data?.schUnqNo ?: 0)
                                                                if (result) {
                                                                    rcmdtnLoading = true
                                                                } else {
                                                                    rcmdtnLoading = true
                                                                    Toast
                                                                        .makeText(context, "다시 시도해주세요", Toast.LENGTH_SHORT)
                                                                        .show()
                                                                }
                                                            }
                                                        },
                                                        indication = rememberRipple(
                                                            bounded = true,
                                                            radius = 8.dp,
                                                            color = design_login_text
                                                        ),
                                                        interactionSource = remember { MutableInteractionSource() }
                                                    )
                                            )

                                            Text(text = "${comment.nrcmdtnCnt}",
                                                style = TextStyle(
                                                    color = MaterialTheme.colorScheme.secondary,
                                                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                                    fontSize = 12.sp,
                                                    letterSpacing = (-0.6).sp),
                                                textAlign = TextAlign.Center,
                                                lineHeight = 12.sp,
                                                modifier = Modifier.padding(start = 4.dp)
                                            )
                                        }

                                        Icon(
                                            painter = painterResource(id = R.drawable.icon_report),
                                            contentDescription = "", tint = Color.Unspecified,
                                            modifier = Modifier
                                                .padding(start = 12.dp)
                                                .clickable(
                                                    onClick = {
                                                        viewModel.updateSelectCmnt(comment)
                                                        dclrDialogOpen(true)
                                                    },
                                                    indication = rememberRipple(
                                                        bounded = true,
                                                        radius = 8.dp,
                                                        color = design_login_text
                                                    ),
                                                    interactionSource = remember { MutableInteractionSource() }
                                                )
                                        )
                                    }
                                }
                            }
                        }

                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "", tint = design_skip,
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .size(14.dp)
                                .clickable(
                                    onClick = { expand = !expand },
                                    indication = rememberRipple(
                                        bounded = true,
                                        radius = 8.dp,
                                        color = MaterialTheme.colorScheme.secondary
                                    ),
                                    interactionSource = remember { MutableInteractionSource() }
                                )
                        )
                    }
                }


                Text(
                    text = comment.cmntCn,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp,
                    letterSpacing = (-0.7).sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(end = 10.dp)
                )

                if (step2CmntList.isNotEmpty()){
                    Text(text = if(!step2Expand) "└ 답글 ${step2CmntList.size}개" else "└ 답글접기",
                        style = TextStyle(color = MaterialTheme.colorScheme.secondary,fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 12.sp,
                        letterSpacing = (-0.6).sp),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .clickable { step2Expand = !step2Expand }
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = step2Expand && step2CmntList.isNotEmpty()
        ) {
            LazyColumn(
                state = rememberLazyListState(),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 1000.dp),
                contentPadding = PaddingValues(top = 10.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ){
                itemsIndexed(step2CmntList){ index, item ->
                    CommentListItem2(comment = item, viewModel = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentListItem2(
    comment: Cmnt,
    viewModel: CommunityViewModel,

){
    val storyDetail by viewModel.storyDetail.collectAsState()
    val atchPath = storyDetail?.data?.atchPath ?: ""
    var expand by remember { mutableStateOf(false) }
    var commentDelete by remember{ mutableStateOf(false) }
    var openBottomSheet by remember{ mutableStateOf(false) }
    var deleteDialog by remember{ mutableStateOf(false) }
    val context = LocalContext.current
    var updateComment by remember{ mutableStateOf(comment.cmntCn) }
    var updateLoading by remember { mutableStateOf(false) }
    var rcmdtnLoading by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = commentDelete){
        if (commentDelete){
            val result = viewModel.deleteComment(comment.cmntNo)
            if (!result){
                Toast.makeText(context, "다시 시도해주세요", Toast.LENGTH_SHORT).show()
            }
        }
        commentDelete = false
    }

    if (deleteDialog){
        CustomDialogDelete(
            onDismiss = { newValue -> deleteDialog = newValue },
            confirm = "삭제하기",
            dismiss = "취소",
            title = "댓글 삭제하기",
            text = "정말 삭제하시겠어요?",
            valueChange = { newValue -> commentDelete = newValue}
        )
    }

    if (openBottomSheet){

        AlertDialog(
            onDismissRequest = { openBottomSheet = false},
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Column (
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
            ) {
                Spacer(modifier = Modifier.padding(top = 20.dp))

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "댓글 수정하기",
                        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                        fontSize = 20.sp,
                        letterSpacing = (-1.0).sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(start = 20.dp, bottom = 20.dp)
                    )

                    Text(
                        text = "취소",
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 14.sp,
                        letterSpacing = (-0.7).sp,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .padding(end = 30.dp, bottom = 20.dp)
                            .clickable { openBottomSheet = false }
                        ,
                        textDecoration = TextDecoration.Underline
                    )
                }

                Card (
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(3.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
                ){
                    Row (
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 10.dp)
                            .fillMaxWidth()
                    ){
                        CircleImageTopBar(size = 40, imageUri = "${atchPath}${comment.petImg}" )

                        Spacer(modifier = Modifier.padding(start = 8.dp))

                        Column (
                            modifier = Modifier.fillMaxWidth()
                        ){
                            Row (
                                verticalAlignment = Alignment.Bottom
                            ){
                                Text(
                                    text = comment.petNm,
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                    letterSpacing = (-0.7).sp,
                                    color = if (G.userId == comment.userId) design_intro_bg else design_skip
                                )

                                Text(
                                    text = comment.lastStrgDt,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                    letterSpacing = (-0.7).sp,
                                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                                    modifier = Modifier.padding(start = 5.dp)
                                )
                            }

                            Text(
                                text = comment.cmntCn,
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                fontSize = 14.sp,
                                letterSpacing = (-0.7).sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }



                Spacer(modifier = Modifier
                    .padding(top = 20.dp))

                Row (
                    modifier = Modifier
                        .padding(start = 20.dp, end = 10.dp, bottom = 20.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    TextField(
                        value = updateComment,
                        onValueChange = { updateComment = it},
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimary,
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
                        modifier = Modifier
                            .weight(1f),
                        colors = TextFieldDefaults.colors(
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
                            focusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                            focusedContainerColor = MaterialTheme.colorScheme.primary,
                            unfocusedLeadingIconColor = MaterialTheme.colorScheme.primaryContainer,
                            focusedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
                            cursorColor = design_intro_bg.copy(alpha = 0.5f),
                            selectionColors = TextSelectionColors(
                                handleColor = design_intro_bg.copy(alpha = 0.5f),
                                backgroundColor = design_intro_bg.copy(alpha = 0.5f)
                            ),
                            focusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "", tint = design_skip,
                                modifier = Modifier
                                    .size(14.dp)
                                    .clickable { updateComment = "" }
                            )
                        }
                    )

                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(width = 56.dp, height = 40.dp)
                            .background(color = MaterialTheme.colorScheme.tertiaryContainer, shape = RoundedCornerShape(12.dp))
                            .clip(shape = RoundedCornerShape(12.dp))
                            .clickable(
                                enabled = updateComment != "" && !updateLoading,
                                onClick = {
                                    viewModel.viewModelScope.launch {
                                        updateLoading = true
                                        val result = viewModel.updateComment(updateComment, comment.cmntNo)
                                        if (result) {
                                            updateComment = ""
                                            updateLoading = false
                                            Toast
                                                .makeText(context, "댓글이 수정되었습니다", Toast.LENGTH_SHORT)
                                                .show()
                                        } else {
                                            Toast
                                                .makeText(context, "댓글 수정에 실패했습니다", Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                    }
                                }

                            ),
                        contentAlignment = Alignment.Center
                    ){
                        if (updateLoading){
                            LoadingAnimation3(
                                circleColor = MaterialTheme.colorScheme.primary,
                                circleSize = 4.dp
                            )
                        }else{
                            Text(text = "수정", style = TextStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                fontSize = 14.sp,
                                letterSpacing = (-0.7).sp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }

    Row (
        modifier = Modifier
            .padding(start = 40.dp, end = 20.dp)
            .fillMaxWidth()
    ){
        CircleImageTopBar(size = 40, imageUri = "${atchPath}${comment.petImg}" )

        Spacer(modifier = Modifier.padding(start = 8.dp))

        Column (
            modifier = Modifier.fillMaxWidth()
        ){
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Row (
                    verticalAlignment = Alignment.Bottom
                ){
                    Text(
                        text = comment.petNm,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp,
                        color = if (G.userId == comment.userId) design_intro_bg else MaterialTheme.colorScheme.secondary
                    )

                    Text(
                        text = comment.lastStrgDt,
                        fontSize = 10.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp,
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                        modifier = Modifier.padding(start = 5.dp)
                    )
                }


                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Box(contentAlignment = Alignment.CenterEnd){

                        androidx.compose.animation.AnimatedVisibility(
                            visible = !expand,
                            enter = fadeIn() + slideInVertically(),
                            exit = fadeOut() + slideOutVertically(targetOffsetY = {it/2})
                        ) {
                            Row (verticalAlignment = Alignment.CenterVertically){
                                Icon(
                                    painter = painterResource(id = if (comment.myCmntRcmdtn == "001" ) R.drawable.icon_like else R.drawable.icon_like_default),
                                    contentDescription = "", tint = Color.Unspecified,
                                    modifier = Modifier
                                        .padding(start = 12.dp)
                                        .clickable(
                                            enabled = comment.myCmntRcmdtn == null && !rcmdtnLoading,
                                            onClick = {
                                                viewModel.viewModelScope.launch {
                                                    rcmdtnLoading = true
                                                    val result = viewModel.rcmdtnComment(cmntNo = comment.cmntNo, rcmdtnSeCd = "001", schUnqNo = storyDetail?.data?.schUnqNo ?: 0)
                                                    if (result) {
                                                        rcmdtnLoading = true
                                                    } else {
                                                        rcmdtnLoading = true
                                                        Toast
                                                            .makeText(context, "다시 시도해주세요", Toast.LENGTH_SHORT)
                                                            .show()
                                                    }
                                                }
                                            },
                                            indication = rememberRipple(
                                                bounded = true,
                                                radius = 8.dp,
                                                color = design_sharp
                                            ),
                                            interactionSource = remember { MutableInteractionSource() }
                                        )
                                )
                                Text(text = "${comment.rcmdtnCnt}",
                                    style = TextStyle(
                                        color = MaterialTheme.colorScheme.secondary,
                                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                        fontSize = 12.sp,
                                        letterSpacing = (-0.6).sp),
                                    textAlign = TextAlign.Center,
                                    lineHeight = 12.sp,
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            }
                        }

                        androidx.compose.animation.AnimatedVisibility(
                            visible = expand,
                            enter = fadeIn() + slideInVertically(),
                            exit = fadeOut() + slideOutVertically(targetOffsetY = {it/2})
                        ){
                            if (G.userId == comment.userId){
                                Row (verticalAlignment = Alignment.CenterVertically){
                                    Text(
                                        text = "수정",
                                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                        fontSize = 12.sp, letterSpacing = (-0.6).sp,
                                        color = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier
                                            .padding(start = 12.dp)
                                            .clickable {
                                                focusManager.clearFocus()
                                                openBottomSheet = true
                                            }
                                    )
                                    Text(
                                        text = "삭제",
                                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                        fontSize = 12.sp, letterSpacing = (-0.6).sp,
                                        color = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier
                                            .padding(start = 12.dp)
                                            .clickable { deleteDialog = true }
                                    )
                                }
                            }else{
                                Row (verticalAlignment = Alignment.CenterVertically){
                                    Row (
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        Icon(
                                            painter = painterResource(id = if (comment.myCmntRcmdtn == "002" ) R.drawable.icon_dislike else R.drawable.icon_dislike_default),
                                            contentDescription = "", tint = Color.Unspecified,
                                            modifier = Modifier
                                                .padding(start = 12.dp)
                                                .clickable(
                                                    enabled = comment.myCmntRcmdtn == null && !rcmdtnLoading,
                                                    onClick = {
                                                        viewModel.viewModelScope.launch {
                                                            rcmdtnLoading = true
                                                            val result = viewModel.rcmdtnComment(cmntNo = comment.cmntNo, rcmdtnSeCd = "002", schUnqNo = storyDetail?.data?.schUnqNo ?: 0)
                                                            if (result) {
                                                                rcmdtnLoading = true
                                                            } else {
                                                                rcmdtnLoading = true
                                                                Toast
                                                                    .makeText(context, "다시 시도해주세요", Toast.LENGTH_SHORT)
                                                                    .show()
                                                            }
                                                        }
                                                    },
                                                    indication = rememberRipple(
                                                        bounded = true,
                                                        radius = 8.dp,
                                                        color = design_login_text
                                                    ),
                                                    interactionSource = remember { MutableInteractionSource() }
                                                )
                                        )
                                        Text(text = "${comment.nrcmdtnCnt}",
                                            style = TextStyle(
                                                color = MaterialTheme.colorScheme.secondary,
                                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                                fontSize = 12.sp,
                                                letterSpacing = (-0.6).sp),
                                            textAlign = TextAlign.Center,
                                            lineHeight = 12.sp,
                                            modifier = Modifier.padding(start = 4.dp)
                                        )
                                    }

                                    Icon(
                                        painter = painterResource(id = R.drawable.icon_report),
                                        contentDescription = "", tint = Color.Unspecified,
                                        modifier = Modifier
                                            .padding(start = 12.dp)
                                            .clickable(
                                                onClick = { },
                                                indication = rememberRipple(
                                                    bounded = true,
                                                    radius = 8.dp,
                                                    color = design_login_text
                                                ),
                                                interactionSource = remember { MutableInteractionSource() }
                                            )
                                    )
                                }
                            }
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "", tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(14.dp)
                            .clickable(
                                onClick = { expand = !expand },
                                indication = rememberRipple(
                                    bounded = true,
                                    radius = 8.dp,
                                    color = design_login_text
                                ),
                                interactionSource = remember { MutableInteractionSource() }
                            )
                    )
                }
            }


            Text(
                text = comment.cmntCn,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 14.sp,
                letterSpacing = (-0.7).sp,
                color = MaterialTheme.colorScheme.onPrimary
            )

        }
    }
}

private class CustomIndication(
    val pressColor: Color = Color.Red,
    val cornerRadius: CornerRadius = CornerRadius(16f, 16f),
    val alpha: Float = 0.5f,
    val drawRoundedShape: Boolean = true
) : Indication {

    private inner class DefaultIndicationInstance(
        private val isPressed: State<Boolean>,
    ) : IndicationInstance {

        override fun ContentDrawScope.drawIndication() {

            drawContent()
            when {
                isPressed.value -> {
                    if (drawRoundedShape) {
                        drawRoundRect(
                            cornerRadius = cornerRadius,
                            color = pressColor.copy(
                                alpha = alpha
                            ), size = size
                        )
                    } else {

                        drawCircle(
                            radius = size.width,
                            color = pressColor.copy(
                                alpha = alpha
                            )
                        )
                    }
                }
            }
        }
    }

    @Composable
    override fun rememberUpdatedInstance(interactionSource: InteractionSource): IndicationInstance {
        val isPressed = interactionSource.collectIsPressedAsState()
        return remember(interactionSource) {
            DefaultIndicationInstance(isPressed)
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