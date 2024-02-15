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
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Scaffold
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.screens.myscreen.MySelectableDates
import net.pettip.app.navi.ui.theme.design_4783F5
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.SharedViewModel
import net.pettip.app.navi.viewmodel.WalkViewModel
import net.pettip.util.Log
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * @Project     : PetTip-Android
 * @FileName    : WalkScreenV2
 * @Date        : 2024-02-07
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screens.mainscreen
 * @see net.pettip.app.navi.screens.mainscreen.WalkScreenV2
 */
 @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
 @Composable
fun WalkScreenV3(
    viewModel: WalkViewModel,
    navController: NavHostController,
    sharedViewModel: SharedViewModel
){
    val days = getDaysOfWeekAndYear(2024)
    val currentDateTime = LocalDateTime.now()
    val todayIndex = days.indexOfFirst { (dayInfo, yearMonthInfo) ->
        dayInfo.day == currentDateTime.dayOfMonth && yearMonthInfo.month == currentDateTime.monthValue-1 && yearMonthInfo.year == currentDateTime.year
    }

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val itemWidth = (screenWidth-100.dp)/7

    val datePickerState = rememberDatePickerState(selectableDates = MySelectableDates())
    val pagerState = rememberPagerState(pageCount = { days.size }, initialPage = todayIndex-3)

    var showDatePicker by remember{ mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Log.d("LOG",days.toString())

    Scaffold {

        if (showDatePicker){
            AlertDialog(
                onDismissRequest = { showDatePicker = false },
                properties = DialogProperties(
                    usePlatformDefaultWidth = false
                )
            ){
                Box (
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(20.dp))
                ) {
                    Column {
                        DatePicker(
                            state = datePickerState,
                            colors = DatePickerDefaults.colors(
                                selectedDayContainerColor = design_intro_bg,
                                selectedDayContentColor = design_white,
                                todayDateBorderColor = design_intro_bg,
                                todayContentColor = design_intro_bg,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                headlineContentColor = MaterialTheme.colorScheme.onPrimary,
                                weekdayContentColor = MaterialTheme.colorScheme.onPrimary,
                                subheadContentColor = MaterialTheme.colorScheme.onPrimary,
                                navigationContentColor = MaterialTheme.colorScheme.onPrimary,
                                yearContentColor = MaterialTheme.colorScheme.onPrimary,
                                dayContentColor = MaterialTheme.colorScheme.onPrimary,
                                currentYearContentColor = MaterialTheme.colorScheme.onPrimary,
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        )

                        Row (
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                        ){
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(MaterialTheme.colorScheme.onSecondary)
                                    .clickable { showDatePicker = false },
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    text = stringResource(id = R.string.cancel_kor),
                                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                    fontSize = 14.sp, letterSpacing = (-0.7).sp,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(design_intro_bg)
                                    .clickable {
                                        val instant = Instant.ofEpochMilli(datePickerState.selectedDateMillis ?: Date().time)
                                        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

                                        val year = localDateTime.year
                                        val month = localDateTime.monthValue
                                        val day = localDateTime.dayOfMonth

                                        val index = days.indexOfFirst { (dayInfo, yearMonthInfo) ->
                                            dayInfo.day == day && yearMonthInfo.month == month - 1 && yearMonthInfo.year == year
                                        }

                                        scope.launch {
                                            pagerState.animateScrollToPage(index - 3)
                                        }
                                        showDatePicker = false
                                    },
                                contentAlignment = Alignment.Center
                            ){
                                Text(
                                    text = stringResource(id = R.string.confirm),
                                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                    fontSize = 14.sp, letterSpacing = (-0.7).sp,
                                    color = design_white,
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }


        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onPrimaryContainer)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemWidth * 1.6f + 96.dp)
                    .background(design_white)
            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemWidth * 1.6f + 96.dp)
                        .background(design_4783F5)
                ){

                    Row (
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            text = "${days[pagerState.currentPage].second.year}",
                            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                            fontSize = 20.sp,
                            letterSpacing = (-1.0).sp,
                            color = design_intro_bg,
                            modifier = Modifier
                                .padding(start = 20.dp)
                                .clickable { showDatePicker = true }
                        )

                        Button(
                            onClick = {
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                            border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.outlineVariant),
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier
                                .padding(end = 20.dp)
                                .height(36.dp)
                                .width(72.dp)
                        ) {
                            Text(
                                text = "월 기록", color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                lineHeight = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

data class DayInfo(val dayOfWeek: String, val day: Int, val weekOfMonth: Int)
data class YearMonthInfo(val year: Int, val month: Int, val weeksInMonth: Int)

fun getDaysOfWeekAndYear(year: Int): MutableList<Pair<DayInfo, YearMonthInfo>> {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, Calendar.JANUARY) // 1월로 설정
        set(Calendar.DAY_OF_MONTH, 1)
    }

    val dayList = mutableListOf<Pair<DayInfo, YearMonthInfo>>()

    for (yearIn in 2000..year) {
        calendar.set(Calendar.YEAR, yearIn)
        for (month in Calendar.JANUARY..Calendar.DECEMBER) {
            calendar.set(Calendar.MONTH, month)

            // Get the number of weeks in the current month
            val weeksInMonth = calculateWeeksInMonth(calendar)

            // Get year and month information
            val yearMonthInfo = YearMonthInfo(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                weeksInMonth
            )

            // Add days of the month with corresponding days of the week
            for (i in 1..calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                val dayOfWeek = SimpleDateFormat("EE", Locale.KOREA).format(calendar.time)
                val weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH)
                val dayInfo = DayInfo(dayOfWeek, i, weekOfMonth)
                dayList.add(dayInfo to yearMonthInfo)

                // Move to the next day
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
        }
    }
    return dayList
}

fun calculateWeeksInMonth(calendar: Calendar): Int {
    val firstDayOfMonth = Calendar.getInstance().apply {
        time = calendar.time
        set(Calendar.DAY_OF_MONTH, 1)
    }

    val lastDayOfMonth = Calendar.getInstance().apply {
        time = calendar.time
        set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
    }

    val firstDayOfWeek = firstDayOfMonth.get(Calendar.DAY_OF_WEEK)
    val lastDayOfWeek = lastDayOfMonth.get(Calendar.DAY_OF_WEEK)

    // Calculate the number of days in the first and last week
    val daysInFirstWeek = 7 - (firstDayOfWeek - 1)
    val daysInLastWeek = lastDayOfWeek

    // Calculate the number of full weeks in the month
    val fullWeeks = (calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - daysInFirstWeek - lastDayOfWeek) / 7

    // Total weeks in the month
    return fullWeeks + 2 // Add 2 for the first and last weeks
}

