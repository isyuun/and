package net.pettip.app.navi.screens.mainscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Scaffold
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_login_text
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.SharedViewModel
import net.pettip.app.navi.viewmodel.WalkViewModel
import net.pettip.util.Log
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

/**
 * @Project     : PetTip-Android
 * @FileName    : WalkScreenV2
 * @Date        : 2024-02-07
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screens.mainscreen
 * @see net.pettip.app.navi.screens.mainscreen.WalkScreenV2
 */
 @OptIn(ExperimentalFoundationApi::class)
 @Composable
fun WalkScreenV2(
    viewModel: WalkViewModel,
    navController: NavHostController,
    sharedViewModel: SharedViewModel
){
    //val days = getDaysOfWeekAndMonth(2024, 2) // January 2022
    val days = getDaysOfWeekAndYear(2024)
    val pagerState = rememberPagerState(pageCount = { days.size })

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val remainingSpace = (screenWidth - 352.dp) / 2

    val scope = rememberCoroutineScope()

    Scaffold (

    ){
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onPrimaryContainer)
                .fillMaxSize()
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(design_intro_bg)
            ) {
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                        .fillMaxWidth()
                ){
                    Row (
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "", tint = design_white,
                            modifier = Modifier
                                .clickable {
                                    val currentYearMonthInfo = days[pagerState.currentPage].second
                                    val resultIndex = days.indexOfFirst { (dayInfo, yearMonthInfo) ->
                                        dayInfo.day == 1 && yearMonthInfo.month == currentYearMonthInfo.month-1
                                    }
                                    if (resultIndex != -1) {
                                        scope.launch {
                                            pagerState.animateScrollToPage(resultIndex)
                                        }
                                    }
                                }
                        )

                        Text(
                            text = "${days[pagerState.currentPage].second.year}.${String.format("%02d",days[pagerState.currentPage].second.month+1)}",
                            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                            fontSize = 20.sp,
                            letterSpacing = (-1.0).sp,
                            color = design_white,
                            //modifier = Modifier.padding(top = 20.dp, start = 20.dp)
                        )

                        Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "", tint = design_white,
                            modifier = Modifier
                                .clickable {
                                    val currentYearMonthInfo = days[pagerState.currentPage].second
                                    val resultIndex = days.indexOfFirst { (dayInfo, yearMonthInfo) ->
                                        dayInfo.day == 1 && yearMonthInfo.month == currentYearMonthInfo.month+1
                                    }
                                    if (resultIndex != -1) {
                                        scope.launch {
                                            pagerState.animateScrollToPage(resultIndex)
                                        }
                                    }
                                }
                        )
                    }

                    Button(
                        onClick = {
                        },
                        modifier = Modifier
                            .padding(end = 16.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.outlineVariant),
                        contentPadding = PaddingValues(vertical = 0.dp, horizontal = 12.dp)
                    ) {
                        Text(
                            text = "월 기록", color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            lineHeight = 14.sp
                        )
                    }

                }

                //LazyRow(
                //    modifier = Modifier.padding(top = 8.dp, bottom = 20.dp),
                //    horizontalArrangement = Arrangement.spacedBy(12.dp),
                //    contentPadding = PaddingValues(start = 20.dp, end = 20.dp)
                //) {
                //    items(days){ day ->
                //        Log.d("LOG",day.toString())
                //        DayItem(day = day, page = page)
                //    }
                //}


                HorizontalPager(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    state = pagerState,
                    pageSize = PageSize.Fixed(40.dp),
                    pageSpacing = 12.dp,
                    contentPadding = PaddingValues(horizontal = remainingSpace),
                    beyondBoundsPageCount = 0,
                    flingBehavior = PagerDefaults.flingBehavior(
                        state = pagerState,
                        snapVelocityThreshold = 3.dp,
                        pagerSnapDistance = PagerSnapDistance.atMost(30)
                    )
                ) { page ->
                    Box (
                        modifier = Modifier
                            .graphicsLayer {
                                val pageOffset = pagerState.calculateCurrentOffsetForPage(page)

                                if (page >= pagerState.currentPage+6){
                                    alpha = (7f + pageOffset)
                                }else{
                                    alpha = (1f - pageOffset)
                                }

                            }
                    ){
                        DayItem(day = days[page], page)
                    }
                }
            }//col
        }
    }
}
@Composable
fun DayItem(
    day: Pair<DayInfo, YearMonthInfo>,
    page: Int
) {
    Column (
        modifier = Modifier
            .width(40.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(color = design_white, shape = RoundedCornerShape(20.dp))
            .clickable { Log.d("LOG", page.toString()) }
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        Text(
            text = day.first.dayOfWeek,
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = 12.sp, color = design_login_text,
            lineHeight = 12.sp,
            modifier = Modifier.padding(top = 6.dp, bottom = 3.dp)
        )

        Text(
            text = day.first.day.toString(),
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = 12.sp, color = design_login_text,
            lineHeight = 12.sp,
            modifier = Modifier.padding(top = 3.dp, bottom = 6.dp)
        )

    }
}

data class DayInfo(
    val dayOfWeek: String,
    val day: Int
)

data class YearMonthInfo(
    val year: Int,
    val month: Int
)

fun getDaysOfWeekAndYear(year: Int): MutableList<Pair<DayInfo, YearMonthInfo>> {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, Calendar.JANUARY) // 1월로 설정
        set(Calendar.DAY_OF_MONTH, 1)
    }

    val dayList = mutableListOf<Pair<DayInfo, YearMonthInfo>>()

    // Loop through each month of the year
    for (month in Calendar.JANUARY..Calendar.DECEMBER) {
        calendar.set(Calendar.MONTH, month)

        // Get the number of days in the current month
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        // Get year and month information
        val yearMonthInfo = YearMonthInfo(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH))

        // Add days of the month with corresponding days of the week
        for (i in 1..daysInMonth) {
            val dayOfWeek = SimpleDateFormat("EE", Locale.KOREA).format(calendar.time)
            val dayInfo = DayInfo(dayOfWeek, i)
            dayList.add(dayInfo to yearMonthInfo)

            // Move to the next day
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
    }

    return dayList
}