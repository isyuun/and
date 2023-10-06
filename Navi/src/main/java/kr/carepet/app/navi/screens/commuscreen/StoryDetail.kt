package kr.carepet.app.navi.screens.commuscreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kr.carepet.app.navi.R
import kr.carepet.app.navi.component.BackTopBar
import kr.carepet.app.navi.component.CircleImageTopBar
import kr.carepet.app.navi.screens.mainscreen.calculateCurrentOffsetForPage
import kr.carepet.app.navi.ui.theme.design_btn_border
import kr.carepet.app.navi.ui.theme.design_login_bg
import kr.carepet.app.navi.ui.theme.design_login_text
import kr.carepet.app.navi.ui.theme.design_placeHolder
import kr.carepet.app.navi.ui.theme.design_skip
import kr.carepet.app.navi.ui.theme.design_textFieldOutLine
import kr.carepet.app.navi.ui.theme.design_white
import kr.carepet.app.navi.viewmodel.CommunityViewModel
import kr.carepet.app.navi.viewmodel.SharedViewModel
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryDetail(viewModel: CommunityViewModel, sharedViewModel: SharedViewModel, navController:NavHostController){

    val comment by viewModel.comment.collectAsState()

    val dummy = arrayListOf(
        "배추야 오늘 만나서 반가웠어~ 다음에 또 만나자!",
        "배추야 오늘 만나서 반가웠어~ 다음에 또 만나자! 배추야 오늘 만나서 반가웠어~ 다음에 또 만나자! 배추야 오늘 만나서 반가웠어~ 다음에 또 만나자!"
    )

    Scaffold (
        topBar = {BackTopBar(title = "스토리", navController = navController)}
    ){ paddingValues ->
        Column (
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = design_white)
                .verticalScroll(rememberScrollState())
        ){
            Spacer(modifier = Modifier.padding(top = 20.dp))
            
            StoryDetailTopContent()

            Spacer(
                modifier = Modifier
                    .padding(
                        top = 24.dp,
                        bottom = 20.dp,
                        start = 20.dp,
                        end = 20.dp
                    )
                    .background(color = design_textFieldOutLine)
            )

            PagerImage()

            Spacer(modifier = Modifier.padding(top = 20.dp))

            StoryDetailTime()

            Spacer(modifier = Modifier.padding(top = 20.dp))

            Text(
                text = "오늘 산책 중 너구리를 만났다. 배추도 무서워했고 나도 무서웠다. 배변은 24회 했다. 덥지만 즐거웠다. 내일은 조금 일찍 나가야겠다.",
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 14.sp,
                letterSpacing = (-0.7).sp,
                color = design_login_text,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.padding(top = 40.dp))

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
                    text = "댓글 22",
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
                        Text(text = "댓글을 남겨보세요", style = TextStyle(
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
                    Text(text = "등록", style = TextStyle(
                        color = design_white,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 14.sp,
                        letterSpacing = (-0.7).sp),
                        textAlign = TextAlign.Center
                    )
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
                    Icon(painter = painterResource(id = R.drawable.icon_like),
                        contentDescription = "", tint = Color.Unspecified)
                }
            }
        }// col
    }
}

@Composable
fun StoryDetailTopContent(){
    Column (
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
    ){
        Text(
            text = "행복한 산책 했어요",
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
            CircleImageTopBar(size = 40, imageUri = "")
            
            Spacer(modifier = Modifier.padding(start = 8.dp))

            Column (
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = "배추",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp,
                    letterSpacing = (-0.7).sp,
                    color = design_login_text
                )

                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(
                        text = "2023.08.16 16:05:00",
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 12.sp,
                        letterSpacing = (-0.6).sp,
                        color = design_skip
                    )

                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(painter = painterResource(id = R.drawable.icon_like), contentDescription = "", tint = Color.Unspecified)

                        Text(
                            text = "37",
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
                            text = "22",
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
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerImage(){

    val pagerState = rememberPagerState( pageCount = {0})

    HorizontalPager(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
        state = pagerState,
        beyondBoundsPageCount = 1
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
                        "http://carepet.hopto.org/img/"
                    )
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                placeholder = painterResource(id = R.drawable.profile_default),
                error= painterResource(id = R.drawable.profile_default),
                modifier= Modifier.fillMaxSize(),
                contentScale = ContentScale.FillHeight
            )
        }
    }
}

@Composable
fun StoryDetailTime(){

    Row (modifier = Modifier
        .fillMaxWidth()
        , horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
    ){
        Column (
            modifier= Modifier
                .weight(1f)
        ){
            Text(
                text = "산책 날짜",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                letterSpacing = (-0.7).sp,
                color = design_skip,
                modifier = Modifier.padding(start = 20.dp)
            )

            Text(
                text = "2023.08.08",
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
                text = "산책 시간",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                letterSpacing = (-0.7).sp,
                color = design_skip,
                modifier = Modifier.padding(start = 20.dp)
            )

            Text(
                text = "01:20:54",
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

            Text(
                text = "2.4km",
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
        
        Column {
            Text(
                text = "강레오",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                letterSpacing = (-0.7).sp,
                color = design_skip
            )

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