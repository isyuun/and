package net.pettip.app.navi.screens.commuscreen

import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.text.util.Linkify
import android.widget.TextView
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import coil.Coil
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.component.CircleImageTopBar
import net.pettip.app.navi.component.ErrorScreen
import net.pettip.app.navi.component.EventDclrDialog
import net.pettip.app.navi.component.LoadingAnimation3
import net.pettip.app.navi.screens.myscreen.CustomDialogDelete
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_login_text
import net.pettip.app.navi.ui.theme.design_sharp
import net.pettip.app.navi.ui.theme.design_skip
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.CommunityViewModel
import net.pettip.data.bbs.BbsCmnt
import net.pettip.map.app.LoadingAnimation1
import net.pettip.singleton.MySharedPreference
import net.pettip.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetail(navController: NavHostController, viewModel: CommunityViewModel) {

    val detailData by viewModel.bbsDetail.collectAsState()
    val cmntList by viewModel.eventCmntList.collectAsState()
    val comment by viewModel.bbsComment.collectAsState()
    val replyCmnt by viewModel.eventReplyCmnt.collectAsState()
    val lastPstsn by viewModel.lastPstSn.collectAsState()
    val detailLoading by viewModel.eventLoading.collectAsState()

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var cmntExpanded by remember{ mutableStateOf(true) }
    val upCmntNo0:List<BbsCmnt> = cmntList?.filter { cmnt ->
        cmnt.upCmntNo == cmnt.pstCmntNo } ?: emptyList()

    var expanded by remember{ mutableStateOf(false) }
    var dclrDialogOpen by remember{ mutableStateOf(false) }

    var refresh by remember{ mutableStateOf(false) }
    var isLoading by remember{ mutableStateOf(false) }
    var onReply by remember{ mutableStateOf(false) }
    var replyText by remember{ mutableStateOf("") }
    var rcmdtnLoading by remember { mutableStateOf(false) }
    var back by remember { mutableStateOf(false) }

    val lazyListState = rememberLazyListState()
    val verticalScrollState = rememberScrollState()

    LaunchedEffect(key1 = back){
        if (back){
            navController.popBackStack()
        }
    }

    DisposableEffect(Unit){

        onDispose {
            viewModel.updateBbsDetail(null)
            viewModel.updateLastPstSn(null)
            viewModel.updateEventReplyCmnt(null)
        }
    }

    BackHandler {
        if (onReply){
            onReply =false
        }else{
            navController.popBackStack()
        }
    }

    LaunchedEffect(key1 = refresh){
        if (refresh){
            val result = lastPstsn?.let { viewModel.getEventDetail(it) }
            refresh = if (result == true){
                false
            }else{
                false
            }
        }
    }

    LaunchedEffect(key1 = onReply){
        if (onReply){
            focusManager.clearFocus()
            delay(300)
            focusRequester.requestFocus()
        } else{
            delay(300)
            viewModel.updateEventReplyCmnt(null)
            replyText = ""
        }
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.height(60.dp),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(shape = CircleShape)
                                .clickable {
                                    back = true
                                }
                                .align(Alignment.CenterStart),
                            contentAlignment = Alignment.Center
                        ){
                            Icon(painter = painterResource(id = R.drawable.arrow_back),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        Text(
                            text = stringResource(id = R.string.title_event),
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                            letterSpacing = (-1.0).sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.align(Alignment.Center),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        if (dclrDialogOpen){
            EventDclrDialog(
                viewModel = viewModel,
                expanded = expanded,
                expandChange = {newValue -> expanded = newValue},
                onDismiss ={newValue -> dclrDialogOpen = newValue}
            )
        }

        Crossfade(
            targetState = detailLoading,
            label = "",
        ) {
            when(it){
                true ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingAnimation1(circleColor = design_intro_bg)
                    }
                false ->
                    if (detailData == null){
                        ErrorScreen(onClick = { refresh = true })
                    }else{
                        Column(
                            modifier = Modifier
                                .padding(paddingValues)
                                .fillMaxSize()
                                .background(color = MaterialTheme.colorScheme.primary)
                                .verticalScroll(verticalScrollState)
                        ) {
                            AsyncImage(
                                onLoading = { },
                                onError = { Log.d("LOG", "onError") },
                                onSuccess = { Log.d("LOG", "onSuccess") },
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(detailData?.data?.rprsImgUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "",
                                placeholder = null,
                                error = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.FillWidth
                            )

                            Text(
                                text = detailData?.data?.pstTtl ?: "",
                                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                                fontSize = 24.sp,
                                lineHeight = 32.sp,
                                letterSpacing = (-1.2).sp,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)
                            )

                            Spacer(
                                modifier = Modifier
                                    .padding(top = 20.dp, bottom = 20.dp, start = 20.dp, end = 20.dp)
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(MaterialTheme.colorScheme.outline)
                            )

                            HtmlText(
                                htmlString = detailData?.data?.pstCn?:"",
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .fillMaxWidth()
                            )

                            //Row (
                            //    modifier = Modifier
                            //        .padding(top = 40.dp)
                            //        .fillMaxWidth()
                            //        .background(MaterialTheme.colorScheme.onPrimaryContainer)
                            //        .height(36.dp)
                            //        .clickable { cmntExpanded = !cmntExpanded }
                            //    ,
                            //    verticalAlignment = Alignment.CenterVertically
                            //){
                            //
                            //    Icon(
                            //        painter = painterResource(id = R.drawable.icon_comment_line),
                            //        contentDescription = "", tint = Color.Unspecified,
                            //        modifier = Modifier.padding(start = 20.dp))
                            //
                            //    Text(
                            //        text = "${stringResource(id = R.string.comment)} ${cmntList?.size ?: 0}",
                            //        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            //        fontSize = 12.sp,
                            //        letterSpacing = (-0.6).sp,
                            //        color = MaterialTheme.colorScheme.secondary,
                            //        modifier = Modifier.padding(start = 4.dp)
                            //    )
                            //}
                            //
                            //AnimatedVisibility(
                            //    visible = cmntExpanded && upCmntNo0.isNotEmpty(),
                            //    enter = expandVertically(tween(durationMillis = 700)).plus(fadeIn(tween(durationMillis = 500, delayMillis = 200))),
                            //    exit = shrinkVertically(tween(durationMillis = 700)).plus(fadeOut())
                            //) {
                            //    LazyColumn(
                            //        state = lazyListState,
                            //        modifier = Modifier
                            //            .fillMaxWidth()
                            //            .heightIn(max = 1000.dp),
                            //        contentPadding = PaddingValues(top = 18.dp, bottom = 20.dp)
                            //    ){
                            //        itemsIndexed(upCmntNo0){ index, item ->
                            //            EventCommentListItem(
                            //                comment = item,
                            //                viewModel = viewModel,
                            //                onReply = onReply,
                            //                onReplyChange = {newValue -> onReply = newValue},
                            //                dclrDialogOpen = {newValue -> dclrDialogOpen = newValue }
                            //            )
                            //
                            //            if(index != (upCmntNo0?.size?.minus(1) ?: 0)){
                            //                Spacer(modifier = Modifier
                            //                    .padding(vertical = 16.dp, horizontal = 20.dp)
                            //                    .fillMaxWidth()
                            //                    .height(1.dp)
                            //                    .background(color = MaterialTheme.colorScheme.outline))
                            //            }
                            //        }
                            //    }
                            //}
                            //
                            //AnimatedVisibility(
                            //    visible =  onReply,
                            //    enter = expandVertically(expandFrom = Alignment.Top),
                            //    exit = shrinkVertically(shrinkTowards = Alignment.Top)
                            //) {
                            //    Row (
                            //        modifier = Modifier
                            //            .fillMaxWidth()
                            //            .background(color = design_intro_bg),
                            //        verticalAlignment = Alignment.CenterVertically,
                            //        horizontalArrangement = Arrangement.SpaceBetween
                            //    ){
                            //        Row (
                            //            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                            //            verticalAlignment = Alignment.Bottom
                            //        ){
                            //            Text(
                            //                text = "${replyCmnt?.petNm}${stringResource(id = R.string.to_comment_writing)}",
                            //                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            //                fontSize = 14.sp, letterSpacing = (-0.7).sp,
                            //                color = design_white,
                            //                lineHeight = 14.sp,
                            //                modifier = Modifier.padding(start = 20.dp, end = 8.dp)
                            //            )
                            //
                            //            LoadingAnimation3(circleSize = 4.dp, circleColor = design_white, animationDelay = 600)
                            //        }
                            //
                            //        Icon(
                            //            imageVector = Icons.Default.Clear,
                            //            contentDescription = "", tint = design_white,
                            //            modifier = Modifier
                            //                .padding(end = 20.dp)
                            //                .clickable { onReply = false })
                            //    }
                            //
                            //    Spacer(modifier = Modifier
                            //        .fillMaxWidth()
                            //        .height(1.dp)
                            //        .background(MaterialTheme.colorScheme.onPrimary))
                            //}
                            //
                            //Spacer(modifier = Modifier
                            //    .fillMaxWidth()
                            //    .height(1.dp)
                            //    .background(MaterialTheme.colorScheme.onPrimary))
                            //
                            //Row (
                            //    modifier = Modifier.fillMaxWidth(),
                            //    verticalAlignment = Alignment.CenterVertically
                            //){
                            //    TextField(
                            //        value = if (onReply) replyText else comment,
                            //        onValueChange = {
                            //            if (onReply){
                            //                replyText = it
                            //            }else{
                            //                viewModel.updateBbsComment(it)
                            //            }
                            //        },
                            //        textStyle = TextStyle(
                            //            color = MaterialTheme.colorScheme.onPrimary,
                            //            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            //            fontSize = 14.sp,
                            //            letterSpacing = (-0.7).sp
                            //        ),
                            //        placeholder = {
                            //            Text(text = stringResource(R.string.ph_comment), style = TextStyle(
                            //                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            //                fontSize = 14.sp,
                            //                letterSpacing = (-0.7).sp
                            //            )) },
                            //        keyboardOptions = KeyboardOptions(
                            //            keyboardType = KeyboardType.Text,
                            //            imeAction = ImeAction.Done),
                            //        modifier = Modifier
                            //            .weight(1f)
                            //            .focusRequester(focusRequester)
                            //        ,
                            //        colors = TextFieldDefaults.colors(
                            //            unfocusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
                            //            focusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
                            //            unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                            //            focusedContainerColor = MaterialTheme.colorScheme.primary,
                            //            unfocusedLeadingIconColor = MaterialTheme.colorScheme.primaryContainer,
                            //            focusedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
                            //            cursorColor = design_intro_bg.copy(alpha = 0.5f),
                            //            selectionColors = TextSelectionColors(
                            //                handleColor = design_intro_bg.copy(alpha = 0.5f),
                            //                backgroundColor = design_intro_bg.copy(alpha = 0.5f)
                            //            ),
                            //            focusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                            //            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSecondaryContainer
                            //        )
                            //    )
                            //
                            //    Box(
                            //        modifier = Modifier
                            //            .padding(end = 8.dp)
                            //            .size(width = 56.dp, height = 40.dp)
                            //            .background(color = MaterialTheme.colorScheme.tertiaryContainer, shape = RoundedCornerShape(12.dp))
                            //            .clip(shape = RoundedCornerShape(12.dp))
                            //            .clickable(
                            //                enabled = if (onReply) replyText != "" && !isLoading else comment != "" && !isLoading,
                            //                onClick = {
                            //                    viewModel.viewModelScope.launch {
                            //                        isLoading = true
                            //
                            //                        if (onReply) {
                            //                            val result = viewModel.bbsCmntCreate(replyText)
                            //                            if (result) {
                            //                                replyText = ""
                            //                                onReply = false
                            //                                isLoading = false
                            //                            } else {
                            //                                isLoading = false
                            //                                Toast
                            //                                    .makeText(context, "댓글 등록에 실패했습니다", Toast.LENGTH_SHORT)
                            //                                    .show()
                            //                            }
                            //                        } else {
                            //                            val result = viewModel.bbsCmntCreate()
                            //                            if (result) {
                            //                                viewModel.updateBbsComment("")
                            //                                isLoading = false
                            //                            } else {
                            //                                isLoading = false
                            //                                Toast
                            //                                    .makeText(context, "댓글 등록에 실패했습니다", Toast.LENGTH_SHORT)
                            //                                    .show()
                            //                            }
                            //                        }
                            //                    }
                            //                }
                            //            ),
                            //        contentAlignment = Alignment.Center
                            //    ){
                            //        if (isLoading){
                            //            LoadingAnimation3(
                            //                circleColor = MaterialTheme.colorScheme.onTertiaryContainer,
                            //                circleSize = 4.dp
                            //            )
                            //        }else{
                            //            Text(text = stringResource(R.string.comment_apply), style = TextStyle(
                            //                color = MaterialTheme.colorScheme.onTertiaryContainer,
                            //                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            //                fontSize = 14.sp,
                            //                letterSpacing = (-0.7).sp),
                            //                textAlign = TextAlign.Center
                            //            )
                            //        }
                            //    }
                            //
                            //    Box(
                            //        modifier = Modifier
                            //            .padding(end = 4.dp)
                            //            .size(40.dp)
                            //            .background(color = Color.Transparent, shape = RoundedCornerShape(12.dp))
                            //            .clip(shape = RoundedCornerShape(12.dp))
                            //            .border(1.dp, color = MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(12.dp))
                            //            .clickable(
                            //                enabled = detailData?.data?.rcmdtnSeCd == null && !rcmdtnLoading
                            //            ) {
                            //                scope.launch {
                            //                    rcmdtnLoading = true
                            //                    val result = viewModel.bbsRcmdtn(pstSn = detailData?.data?.pstSn ?: 0, rcmdtnSeCd = "001")
                            //                    if (result) {
                            //                        rcmdtnLoading = false
                            //                    } else {
                            //                        rcmdtnLoading = false
                            //                        Toast
                            //                            .makeText(context, "다시 시도해주세요", Toast.LENGTH_SHORT)
                            //                            .show()
                            //                    }
                            //                }
                            //            }
                            //        ,
                            //        contentAlignment = Alignment.Center
                            //    ){
                            //        Icon(painter = painterResource(id = if (detailData?.data?.rcmdtnSeCd == "001" ) R.drawable.icon_like else R.drawable.icon_like_default),
                            //            contentDescription = "", tint = Color.Unspecified)
                            //    }
                            //
                            //    Box(
                            //        modifier = Modifier
                            //            .padding(end = 8.dp)
                            //            .size(40.dp)
                            //            .background(color = Color.Transparent, shape = RoundedCornerShape(12.dp))
                            //            .clip(shape = RoundedCornerShape(12.dp))
                            //            .border(1.dp, color = MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(12.dp))
                            //            .clickable(
                            //                enabled = detailData?.data?.rcmdtnSeCd == null && !rcmdtnLoading
                            //            ) {
                            //                scope.launch {
                            //                    rcmdtnLoading = true
                            //                    val result = viewModel.bbsRcmdtn(pstSn = detailData?.data?.pstSn ?: 0, rcmdtnSeCd = "002")
                            //                    if (result) {
                            //                        rcmdtnLoading = false
                            //                    } else {
                            //                        rcmdtnLoading = false
                            //                        Toast
                            //                            .makeText(context, R.string.retry, Toast.LENGTH_SHORT)
                            //                            .show()
                            //                    }
                            //                }
                            //            }
                            //        ,
                            //        contentAlignment = Alignment.Center
                            //    ){
                            //        Icon(painter = painterResource(id = if (detailData?.data?.rcmdtnSeCd == "002" ) R.drawable.icon_dislike else R.drawable.icon_dislike_default),
                            //            contentDescription = "", tint = Color.Unspecified)
                            //    }
                            //}
                        }//col
                    }

            }
        }
    }

}

@Composable
fun HtmlText(htmlString: String, modifier: Modifier = Modifier) {

    val context = LocalContext.current

    val textColor = if (isSystemInDarkTheme()) design_white.toArgb() else design_login_text.toArgb()

    val htmlText = HtmlCompat.fromHtml(htmlString,0)

    Column {
        // 기존
        //AndroidView(
        //    modifier = modifier,
        //    factory = { context -> TextView(context) },
        //    update = { textView ->
        //        val typeface = ResourcesCompat.getFont(context, R.font.pretendard_regular)
        //        textView.text = HtmlCompat.fromHtml(
        //            htmlString, HtmlCompat.FROM_HTML_MODE_COMPACT,
        //            CoilImageGetter(textView),null
        //        )
        //        textView.setTextColor(textColor.toArgb())
        //        textView.typeface = typeface
        //    }
        //)

        // 신규 - setBackgroundColor, styledHtmlString 설정(white,dark 테마)
        //AndroidView(
        //    factory = { context ->
        //        WebView(context).apply {
        //            setBackgroundColor(color)
        //            settings.javaScriptEnabled = false
        //            settings.loadWithOverviewMode = true
        //            webViewClient = WebViewClient()
        //        }
        //    },
        //    update = { webView ->
        //        webView.loadDataWithBaseURL(null, styledHtmlString , "text/html", "UTF-8", null)
        //    },
        //    modifier = modifier
        //)

        AndroidView(
            modifier = modifier,
            factory = {
                MaterialTextView(it).apply {
                    autoLinkMask = Linkify.WEB_URLS
                    linksClickable = true
                    setLinkTextColor(Color.Blue.toArgb())
                }
            },
            update = { textView ->
                val typeface = ResourcesCompat.getFont(context, R.font.pretendard_regular)
                textView.text = HtmlCompat.fromHtml(
                    htmlText.toString(), HtmlCompat.FROM_HTML_MODE_COMPACT,
                    CoilImageGetter(textView), null
                )
                textView.typeface = typeface
                textView.setTextColor(textColor)
            }
        )
    }
}
fun String.replaceLast(oldValue: String, newValue: String): String {
    val lastIndex = this.lastIndexOf(oldValue)
    if (lastIndex == -1) {
        return this
    }
    return this.substring(0, lastIndex) + newValue + this.substring(lastIndex + oldValue.length)
}
fun String.replaceAllParagraphAndHeadingTags(textColor: String): String {
    val regex = Regex("<p>|<h1>")
    return regex.replace(this) {
        when (it.value) {
            "<p>" -> "<p style=\"color: $textColor\">"
            "<h1>" -> "<h1 style=\"color: $textColor\">"
            else -> it.value
        }
    }
}

open class CoilImageGetter(
    private val textView: TextView,
    private val imageLoader: ImageLoader = Coil.imageLoader(textView.context),
    private val sourceModifier: ((source: String) -> String)? = null
) : Html.ImageGetter {

    override fun getDrawable(source: String): Drawable {
        val finalSource = sourceModifier?.invoke(source) ?: source

        val drawablePlaceholder = DrawablePlaceHolder(textView)
        imageLoader.enqueue(ImageRequest.Builder(textView.context).data(finalSource).apply {
            target { drawable ->
                drawablePlaceholder.updateDrawable(drawable)
                textView.text = textView.text
            }
        }.build())
        return drawablePlaceholder
    }

    @Suppress("DEPRECATION")
    private class DrawablePlaceHolder(private val textView: TextView) : BitmapDrawable() {

        private var drawable: Drawable? = null

        override fun draw(canvas: Canvas) {
            drawable?.draw(canvas)
        }

        fun updateDrawable(drawable: Drawable) {
            this.drawable = drawable

            // TextView의 가로 크기를 가져와서 이미지의 가로 크기로 설정
            val textViewWidth = textView.rootView.measuredWidth

            // 이미지의 가로, 세로 크기
            val imageWidth = drawable.intrinsicWidth
            val imageHeight = drawable.intrinsicHeight

            // 이미지의 종횡비 계산
            val aspectRatio = imageWidth.toFloat() / imageHeight.toFloat()

            // TextView에 이미지를 맞추기 위해 가로 크기를 TextView의 폭과 같게 설정
            val targetWidth = textViewWidth
            val targetHeight = (targetWidth / aspectRatio).toInt()

            // 이미지 크기 설정
            drawable.setBounds(0, 0, targetWidth, targetHeight)
            setBounds(0, 0, targetWidth, targetHeight)
        }
    }
}
