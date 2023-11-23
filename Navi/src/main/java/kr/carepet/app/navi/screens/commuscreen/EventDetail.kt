package kr.carepet.app.navi.screens.commuscreen

import android.util.Base64
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties
import androidx.core.text.HtmlCompat
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.carepet.app.navi.R
import kr.carepet.app.navi.component.BackTopBar
import kr.carepet.app.navi.component.CircleImageTopBar
import kr.carepet.app.navi.component.LoadingAnimation3
import kr.carepet.app.navi.screens.myscreen.CustomDialogDelete
import kr.carepet.app.navi.ui.theme.design_btn_border
import kr.carepet.app.navi.ui.theme.design_button_bg
import kr.carepet.app.navi.ui.theme.design_intro_bg
import kr.carepet.app.navi.ui.theme.design_login_bg
import kr.carepet.app.navi.ui.theme.design_login_text
import kr.carepet.app.navi.ui.theme.design_placeHolder
import kr.carepet.app.navi.ui.theme.design_sharp
import kr.carepet.app.navi.ui.theme.design_skip
import kr.carepet.app.navi.ui.theme.design_textFieldOutLine
import kr.carepet.app.navi.ui.theme.design_white
import kr.carepet.app.navi.viewmodel.CommunityViewModel
import kr.carepet.data.bbs.BbsCmnt
import kr.carepet.data.daily.Cmnt
import kr.carepet.singleton.G
import kr.carepet.util.Log

@Composable
fun EventDetail(navController: NavHostController, viewModel: CommunityViewModel) {

    val detailData by viewModel.eventDetail.collectAsState()
    val cmntList by viewModel.eventCmntList.collectAsState()
    val comment by viewModel.bbsComment.collectAsState()
    val replyCmnt by viewModel.eventReplyCmnt.collectAsState()

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    var cmntExpanded by remember{ mutableStateOf(true) }
    val upCmntNo0:List<BbsCmnt> = cmntList?.filter { cmnt ->
        cmnt.upCmntNo == 0 } ?: emptyList()

    var isLoading by remember{ mutableStateOf(false) }
    var onReply by remember{ mutableStateOf(false) }
    var replyText by remember{ mutableStateOf("") }

    BackHandler {
        if (onReply){
            onReply =false
        }else{
            navController.popBackStack()
        }
    }


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


    Scaffold(
        topBar = { BackTopBar(title = stringResource(R.string.title_event), navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = design_white)
                .verticalScroll(rememberScrollState())
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
                placeholder = painterResource(id = R.drawable.profile_default),
                error = painterResource(id = R.drawable.profile_default),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillWidth
            )

            Text(
                text = detailData?.data?.pstTtl ?: "",
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 24.sp,
                letterSpacing = (-1.2).sp,
                color = design_login_text,
                modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)
            )

            Spacer(
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 20.dp, start = 20.dp, end = 20.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(design_textFieldOutLine)
            )


            // ------------------------ html 문 들어갈 자리 --------------------------
            val pstCn = detailData?.data?.pstCn
            pstCn?.let { WebViewHtml(html = it, modifier = Modifier.fillMaxSize()) }
            // ------------------------ html 문 들어갈 자리 --------------------------

            Spacer(modifier = Modifier.padding(top = 40.dp))

            Button(
                onClick = {
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = design_button_bg)
            )
            {
                Text(text = stringResource(R.string.event_apply), color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
            }

            Row (
                modifier = Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth()
                    .background(design_login_bg)
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
                    text = "댓글 ${detailData?.data?.bbsCmnts?.size ?: 0}",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 12.sp,
                    letterSpacing = (-0.6).sp,
                    color = design_skip,
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
                        EventCommentListItem(comment = item, viewModel = viewModel, onReply, onReplyChange = {newValue -> onReply = newValue})

                        if(index != (upCmntNo0?.size?.minus(1) ?: 0)){
                            Spacer(modifier = Modifier
                                .padding(vertical = 16.dp, horizontal = 20.dp)
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(color = design_textFieldOutLine))
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
                    .background(design_login_text))
            }

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(design_login_text))

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
                            viewModel.updateBbsComment(it)
                        }
                    },
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
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
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
                        .clip(shape = RoundedCornerShape(12.dp))
                        .clickable(
                            enabled = if (onReply) replyText != "" && !isLoading else comment != "" && !isLoading,
                            onClick = {
                                viewModel.viewModelScope.launch {
                                    isLoading = true

                                    if (onReply) {
                                        val result = viewModel.bbsCmntCreate(replyText)
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
                                        val result = viewModel.bbsCmntCreate()
                                        if (result) {
                                            viewModel.updateBbsComment("")
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
                            circleColor = design_white,
                            circleSize = 4.dp
                        )
                    }else{
                        Text(text = stringResource(R.string.comment_apply), style = TextStyle(
                            color = design_white,
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
                        .background(color = design_white, shape = RoundedCornerShape(12.dp))
                        .clip(shape = RoundedCornerShape(12.dp))
                        .border(1.dp, color = design_textFieldOutLine, shape = RoundedCornerShape(12.dp))
                        .clickable {

                        }
                    ,
                    contentAlignment = Alignment.Center
                ){
                    Icon(painter = painterResource(
                        id = R.drawable.icon_like_default),
                        contentDescription = "", tint = Color.Unspecified)
                }

                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(40.dp)
                        .background(color = design_white, shape = RoundedCornerShape(12.dp))
                        .clip(shape = RoundedCornerShape(12.dp))
                        .border(1.dp, color = design_textFieldOutLine, shape = RoundedCornerShape(12.dp))
                        .clickable {

                        }
                    ,
                    contentAlignment = Alignment.Center
                ){
                    Icon(painter = painterResource(
                        id = R.drawable.icon_dislike_default),
                        contentDescription = "", tint = Color.Unspecified)
                }
            }
        }//col

    }

}

@Composable
fun WebViewHtml(html: String, modifier: Modifier) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                loadData(html, "text/html; charset=utf-8", "UTF-8")
            }
        },
        modifier = modifier
    )
}
@Composable
fun WebViewUrl(url: String, data: String, modifier: Modifier) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                postUrl(url, Base64.encode(data.toByteArray(), Base64.DEFAULT))
            }
        },
        modifier = modifier
    )
}
@Composable
fun EventDetailMainText(text: String, bottomPadding: Int) {
    Text(
        text = text,
        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
        fontSize = 14.sp, letterSpacing = (-0.7).sp,
        color = design_skip,
        modifier = Modifier.padding(bottom = bottomPadding.dp),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}
@Composable
fun EventDetailSubText(text: String, bottomPadding: Int) {
    Text(
        text = text,
        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
        fontSize = 14.sp, letterSpacing = (-0.7).sp,
        color = design_login_text,
        modifier = Modifier.padding(bottom = bottomPadding.dp),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}
@Composable
fun HtmlText(html: String, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context -> TextView(context) },
        update = { it.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT) }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun EventCommentListItem(comment: BbsCmnt, viewModel: CommunityViewModel, onReply: Boolean, onReplyChange: (Boolean)->Unit){

    val cmntList by viewModel.eventCmntList.collectAsState()
    val step2CmntList:List<BbsCmnt> = cmntList?.filter { cmnt ->
        cmnt.upCmntNo == comment.pstCmntNo } ?: emptyList()

    val eventDetail by viewModel.eventDetail.collectAsState()
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
                    .background(design_white, RoundedCornerShape(20.dp))
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
                        color = design_login_text,
                        modifier = Modifier.padding(start = 20.dp, bottom = 20.dp)
                    )

                    Text(
                        text = "취소",
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 14.sp,
                        letterSpacing = (-0.7).sp,
                        color = design_skip,
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
                    colors = CardDefaults.cardColors(containerColor = design_white)
                ){
                    Row (
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 10.dp)
                            .fillMaxWidth()
                    ){
                        CircleImageTopBar(size = 40, imageUri = comment.petImgUrl )

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
                                    //color = if (G.userId == comment.userId) design_intro_bg else design_skip
                                )

                                Text(
                                    text = comment.lastStrgDt,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                    letterSpacing = (-0.7).sp,
                                    color = design_skip.copy(alpha = 0.7f),
                                    modifier = Modifier.padding(start = 5.dp)
                                )
                            }

                            Text(
                                text = comment.cmntCn,
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                fontSize = 14.sp,
                                letterSpacing = (-0.7).sp,
                                color = design_login_text
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
                            )
                            ) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done),
                        modifier = Modifier
                            .weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedPlaceholderColor = design_placeHolder,
                            focusedPlaceholderColor = design_placeHolder,
                            unfocusedBorderColor = design_textFieldOutLine,
                            focusedBorderColor = design_login_text,
                            unfocusedContainerColor = design_white,
                            focusedContainerColor = design_white,
                            unfocusedLeadingIconColor = design_placeHolder,
                            focusedLeadingIconColor = design_login_text),
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
                            .background(color = design_btn_border, shape = RoundedCornerShape(12.dp))
                            .clip(shape = RoundedCornerShape(12.dp))
                            .clickable(
                                enabled = updateComment != "" && !updateLoading,
                                onClick = {
                                    viewModel.viewModelScope.launch {
                                        updateLoading = true
                                        val result = viewModel.bbsUpdateComment(updateComment, comment.pstCmntNo)
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
                                circleColor = design_white,
                                circleSize = 4.dp
                            )
                        }else{
                            Text(text = "수정", style = TextStyle(
                                color = design_white,
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
            val result = viewModel.bbsDeleteComment(comment.pstCmntNo)
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
            CircleImageTopBar(size = 40, imageUri = comment.petImgUrl)

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
                            color = if (G.userId == comment.userId) design_intro_bg else design_skip
                        )

                        Text(
                            text = comment.lastStrgDt,
                            fontSize = 10.sp,
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            letterSpacing = (-0.7).sp,
                            color = design_skip.copy(alpha = 0.7f),
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
                                            painter = painterResource(id = if (comment.rcmdtnSeCd == "001" ) R.drawable.icon_like else R.drawable.icon_like_default),
                                            contentDescription = "", tint = Color.Unspecified,
                                            modifier = Modifier
                                                .padding(start = 12.dp)
                                                .clickable(
                                                    enabled = comment.rcmdtnSeCd == null && !rcmdtnLoading,
                                                    onClick = {
                                                        viewModel.viewModelScope.launch {
                                                            rcmdtnLoading = true
                                                            val result = viewModel.bbsRcmdtnComment(pstCmntNo = comment.pstCmntNo, rcmdtnSeCd = "001", pstSn = eventDetail?.data?.pstSn ?: 0)
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

                                        //Text(text = "${comment.rcmdtnCnt}",
                                        //    style = TextStyle(
                                        //        color = design_skip,
                                        //        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                        //        fontSize = 12.sp,
                                        //        letterSpacing = (-0.6).sp),
                                        //    textAlign = TextAlign.Center,
                                        //    lineHeight = 12.sp,
                                        //    modifier = Modifier.padding(start = 4.dp)
                                        //)
                                    }

                                    Icon(
                                        painter = painterResource(id = R.drawable.icon_comment_line),
                                        contentDescription = "", tint = Color.Unspecified,
                                        modifier = Modifier
                                            .padding(start = 12.dp)
                                            .clickable(
                                                onClick = {
                                                    onReplyChange(true)
                                                    viewModel.updateEventReplyCmnt(comment)
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
                                            color = design_skip,
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
                                            color = design_skip,
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
                                                painter = painterResource(id = if (comment.rcmdtnSeCd == "002" ) R.drawable.icon_dislike else R.drawable.icon_dislike_default),
                                                contentDescription = "", tint = Color.Unspecified,
                                                modifier = Modifier
                                                    .padding(start = 12.dp)
                                                    .clickable(
                                                        enabled = comment.rcmdtnSeCd == null && !rcmdtnLoading,
                                                        onClick = {
                                                            viewModel.viewModelScope.launch {
                                                                rcmdtnLoading = true
                                                                val result = viewModel.bbsRcmdtnComment(pstCmntNo = comment.pstCmntNo, rcmdtnSeCd = "002", pstSn = eventDetail?.data?.pstSn ?: 0)
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
                                                            color = design_login_text
                                                        ),
                                                        interactionSource = remember { MutableInteractionSource() }
                                                    )
                                            )

                                            //Text(text = "${comment.nrcmdtnCnt}",
                                            //    style = TextStyle(
                                            //        color = design_skip,
                                            //        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                            //        fontSize = 12.sp,
                                            //        letterSpacing = (-0.6).sp),
                                            //    textAlign = TextAlign.Center,
                                            //    lineHeight = 12.sp,
                                            //    modifier = Modifier.padding(start = 4.dp)
                                            //)
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
                            contentDescription = "", tint = design_skip,
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
                    color = design_login_text,
                    modifier = Modifier.padding(end = 10.dp)
                )

                if (step2CmntList.isNotEmpty()){
                    Text(text = if(!step2Expand) "└ 답글 ${step2CmntList.size}개" else "└ 답글접기",
                        style = TextStyle(color = design_skip,fontFamily = FontFamily(Font(R.font.pretendard_regular)),
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
                    BbsCommentListItem2(comment = item, viewModel = viewModel)
                }
            }
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BbsCommentListItem2(
    comment: BbsCmnt,
    viewModel: CommunityViewModel,
    ){
    val eventDetail by viewModel.eventDetail.collectAsState()
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
            val result = viewModel.bbsDeleteComment(comment.pstCmntNo)
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
                    .background(design_white, RoundedCornerShape(20.dp))
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
                        color = design_login_text,
                        modifier = Modifier.padding(start = 20.dp, bottom = 20.dp)
                    )

                    Text(
                        text = "취소",
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 14.sp,
                        letterSpacing = (-0.7).sp,
                        color = design_skip,
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
                    colors = CardDefaults.cardColors(containerColor = design_white)
                ){
                    Row (
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 10.dp)
                            .fillMaxWidth()
                    ){
                        CircleImageTopBar(size = 40, imageUri = comment.petImgUrl )

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
                                    color = design_skip.copy(alpha = 0.7f),
                                    modifier = Modifier.padding(start = 5.dp)
                                )
                            }

                            Text(
                                text = comment.cmntCn,
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                fontSize = 14.sp,
                                letterSpacing = (-0.7).sp,
                                color = design_login_text
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
                        modifier = Modifier
                            .weight(1f),
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
                            .clip(shape = RoundedCornerShape(12.dp))
                            .clickable(
                                enabled = updateComment != "" && !updateLoading,
                                onClick = {
                                    viewModel.viewModelScope.launch {
                                        updateLoading = true
                                        val result = viewModel.updateComment(updateComment, comment.pstCmntNo)
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
                                circleColor = design_white,
                                circleSize = 4.dp
                            )
                        }else{
                            Text(text = "수정", style = TextStyle(
                                color = design_white,
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
        CircleImageTopBar(size = 40, imageUri = comment.petImgUrl )

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
                        color = if (G.userId == comment.userId) design_intro_bg else design_skip
                    )

                    Text(
                        text = comment.lastStrgDt,
                        fontSize = 10.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp,
                        color = design_skip.copy(alpha = 0.7f),
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
                                    painter = painterResource(id = if (comment.rcmdtnSeCd == "001" ) R.drawable.icon_like else R.drawable.icon_like_default),
                                    contentDescription = "", tint = Color.Unspecified,
                                    modifier = Modifier
                                        .padding(start = 12.dp)
                                        .clickable(
                                            enabled = comment.rcmdtnSeCd == null && !rcmdtnLoading,
                                            onClick = {
                                                viewModel.viewModelScope.launch {
                                                    rcmdtnLoading = true
                                                    val result = viewModel.bbsRcmdtnComment(pstCmntNo = comment.pstCmntNo, rcmdtnSeCd = "001", pstSn = eventDetail?.data?.pstSn ?: 0)
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
                                //Text(text = "${comment.rcmdtnCnt}",
                                //    style = TextStyle(
                                //        color = design_skip,
                                //        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                //        fontSize = 12.sp,
                                //        letterSpacing = (-0.6).sp),
                                //    textAlign = TextAlign.Center,
                                //    lineHeight = 12.sp,
                                //    modifier = Modifier.padding(start = 4.dp)
                                //)
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
                                        color = design_skip,
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
                                        color = design_skip,
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
                                            painter = painterResource(id = if (comment.rcmdtnSeCd == "002" ) R.drawable.icon_dislike else R.drawable.icon_dislike_default),
                                            contentDescription = "", tint = Color.Unspecified,
                                            modifier = Modifier
                                                .padding(start = 12.dp)
                                                .clickable(
                                                    enabled = comment.rcmdtnSeCd == null && !rcmdtnLoading,
                                                    onClick = {
                                                        viewModel.viewModelScope.launch {
                                                            rcmdtnLoading = true
                                                            val result = viewModel.bbsRcmdtnComment(pstCmntNo = comment.pstCmntNo, rcmdtnSeCd = "002", pstSn = eventDetail?.data?.pstSn ?: 0)
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
                                        //Text(text = "${comment.nrcmdtnCnt}",
                                        //    style = TextStyle(
                                        //        color = design_skip,
                                        //        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                        //        fontSize = 12.sp,
                                        //        letterSpacing = (-0.6).sp),
                                        //    textAlign = TextAlign.Center,
                                        //    lineHeight = 12.sp,
                                        //    modifier = Modifier.padding(start = 4.dp)
                                        //)
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
                        contentDescription = "", tint = design_skip,
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
                color = design_login_text
            )

        }
    }
}