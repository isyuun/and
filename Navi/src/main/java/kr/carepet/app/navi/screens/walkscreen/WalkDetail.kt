
package kr.carepet.app.navi.screens.walkscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kr.carepet.app.navi.R
import kr.carepet.app.navi.component.CircleImageTopBar
import kr.carepet.app.navi.screens.mainscreen.CircleImage
import kr.carepet.app.navi.screens.mainscreen.calculateCurrentOffsetForPage
import kr.carepet.app.navi.ui.theme.design_DDDDDD
import kr.carepet.app.navi.ui.theme.design_intro_bg
import kr.carepet.app.navi.ui.theme.design_login_bg
import kr.carepet.app.navi.ui.theme.design_login_text
import kr.carepet.app.navi.ui.theme.design_skip
import kr.carepet.app.navi.ui.theme.design_textFieldOutLine
import kr.carepet.app.navi.ui.theme.design_white
import kr.carepet.app.navi.viewmodel.WalkViewModel
import kr.carepet.data.daily.DailyDetailData
import kr.carepet.data.daily.WalkListRes
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WalkDetailContent(walkViewModel: WalkViewModel){

    val isLoading by walkViewModel.isLoading.collectAsState()
    val dailyDetail by walkViewModel.dailyDetail.collectAsState()
    val walkListItem by walkViewModel.walkListItem.collectAsState()

    val pagerState = rememberPagerState( pageCount = {dailyDetail?.dailyLifeFileList?.size ?: 0 })

    Column (
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(color = design_white)
    ){
        Row (
            modifier= Modifier
                .padding(start = 20.dp, top = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(painter = painterResource(id = R.drawable.icon_calendar), contentDescription = "", tint = Color.Unspecified)
            Text(
                text = walkListItem?.walkDptreDt ?: "",
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 14.sp,
                letterSpacing = (-0.7).sp,
                color = design_login_text,
                modifier = Modifier.padding(start = 4.dp)
            )
        }



        AnimatedVisibility(
            visible = dailyDetail?.dailyLifeFileList?.size != null,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            Column {
                Spacer(modifier = Modifier.padding(top= 20.dp))

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
                            alpha = 1 - pageOffset.absoluteValue / 3*2
                            scaleX = 1 - pageOffset.absoluteValue / 3
                            scaleY = 1 - pageOffset.absoluteValue / 3
                        }
                        .fillMaxSize()
                        , contentAlignment = Alignment.Center) {

                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(
                                    "http://carepet.hopto.org/img/"+
                                            dailyDetail!!.dailyLifeFileList[page].filePathNm+
                                            dailyDetail!!.dailyLifeFileList[page].atchFileNm
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

                Row(
                    Modifier
                        .fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(dailyDetail?.dailyLifeFileList?.size ?: 0 ) { iteration ->
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
        }

        Spacer(modifier = Modifier.padding(top = 20.dp))

        Row (modifier = Modifier
            .padding(start = 20.dp, end = 20.dp)
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
                    color = design_skip,
                    modifier = Modifier.padding(start = 20.dp)
                )

                Text(
                    text = walkListItem?.runNcknm ?: "",
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
                    text = walkListItem?.runTime ?: "",
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
                    text = walkListItem?.runDstnc.toString()+"km",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    letterSpacing = 0.sp,
                    modifier = Modifier
                        .padding(top = 4.dp, start = 20.dp),
                    color = design_login_text
                )

            }
        }

        Spacer(modifier = Modifier.padding(top = 20.dp))

        //LazyColumn(
        //    modifier = Modifier
        //        .fillMaxWidth()
        //        .heightIn(max = 300.dp),
        //    state = rememberLazyListState(),
        //    verticalArrangement = Arrangement.spacedBy(8.dp)
        //){
        //
        //}

        DetailLazyColItem(dailyDetail = dailyDetail?: null)

        Spacer(modifier = Modifier.padding(top = 20.dp))

        Box (
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .background(color = design_login_bg, shape = RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ){
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                text = "오늘 산책 중 너구리를 만났다. 배추도 무서워했고 나도 무서웠다. 배변은 24회 했다. 덥지만 즐거웠다. 내일은 조금 일찍 나가야겠다.",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                letterSpacing = (-0.7).sp,
                color = design_login_text
            )
        }

        Spacer(modifier = Modifier.padding(top = 16.dp))
    }// column
}


@Composable
fun DetailLazyColItem(dailyDetail: kr.carepet.data.daily.DailyDetailData?){
    Box (
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .background(color = design_white, shape = RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = design_textFieldOutLine,
                shape = RoundedCornerShape(12.dp)
            )
    ){
        Row (
            modifier = Modifier.align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ){
            Spacer(modifier = Modifier.padding(start = 22.dp))

            CircleImageTopBar(size = 50, imageUri = "")

            Text(
                text = "배추",
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                fontSize = 16.sp,
                letterSpacing = (-0.8).sp,
                color = design_login_text,
                modifier = Modifier.padding(start = 10.dp, top = 22.dp, bottom = 22.dp)
            )
        }

        Column (
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 20.dp, top = 16.dp, bottom = 16.dp),
        ){
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Icon(painter = painterResource(id = R.drawable.icon_poop), contentDescription = "", tint = Color.Unspecified)

                Text(
                    text = "배변",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp,
                    letterSpacing = (-0.7).sp,
                    color = design_skip,
                    modifier = Modifier.padding(start = 4.dp, end = 8.dp)
                )

                Box (
                    modifier = Modifier.width(60.dp),
                    contentAlignment = Alignment.CenterEnd
                ){
                    Text(
                        text = if (dailyDetail?.bwlMvmNmtm ==null){
                            "0회"
                        }else{
                            dailyDetail?.bwlMvmNmtm.toString()+"회"
                        },
                        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                        fontSize = 14.sp,
                        letterSpacing = (-0.7).sp,
                        color = design_login_text
                    )
                }
            }
            Spacer(modifier = Modifier.padding(top = 8.dp))
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Icon(painter = painterResource(id = R.drawable.icon_pee), contentDescription = "", tint = Color.Unspecified)

                Text(
                    text = "소변",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp,
                    letterSpacing = (-0.7).sp,
                    color = design_skip,
                    modifier = Modifier.padding(start = 4.dp, end = 8.dp)
                )

                Box (
                    modifier = Modifier.width(60.dp),
                    contentAlignment = Alignment.CenterEnd
                ){
                    Text(
                        text = if (dailyDetail?.bwlMvmNmtm ==null){
                            "0회"
                        }else{
                            dailyDetail?.bwlMvmNmtm.toString()+"회"
                        },
                        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                        fontSize = 14.sp,
                        letterSpacing = (-0.7).sp,
                        color = design_login_text
                    )
                }
            }
            Spacer(modifier = Modifier.padding(top = 8.dp))
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Icon(painter = painterResource(id = R.drawable.icon_marking), contentDescription = "", tint = Color.Unspecified)

                Text(
                    text = "마킹",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp,
                    letterSpacing = (-0.7).sp,
                    color = design_skip,
                    modifier = Modifier.padding(start = 4.dp, end = 8.dp)
                )

                Box (
                    modifier = Modifier.width(60.dp),
                    contentAlignment = Alignment.CenterEnd
                ){
                    Text(
                        text = if (dailyDetail?.bwlMvmNmtm ==null){
                            "0회"
                        }else{
                            dailyDetail?.bwlMvmNmtm.toString()+"회"
                        },
                        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                        fontSize = 14.sp,
                        letterSpacing = (-0.7).sp,
                        color = design_login_text
                    )
                }
            }
        }
    }
}