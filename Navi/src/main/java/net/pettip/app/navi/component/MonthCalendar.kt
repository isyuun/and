package net.pettip.app.navi.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.ui.theme.design_76A1EF
import net.pettip.app.navi.ui.theme.design_C3D3EC
import net.pettip.app.navi.ui.theme.design_button_bg
import net.pettip.app.navi.ui.theme.design_deep_blue
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_sharp
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.SharedViewModel
import net.pettip.app.navi.viewmodel.WalkViewModel
import net.pettip.data.daily.MonthDay
import net.pettip.data.pet.CurrentPetData
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun MonthCalendar(walkViewModel: WalkViewModel, sharedViewModel: SharedViewModel) {

    val selectPet by sharedViewModel.selectPet.collectAsState()

    val currentMonth by walkViewModel.selectMonth.collectAsState()

    // 애니메이션 기다리기 위한 작업
    var firstLoad by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = selectPet, key2 = currentMonth){
        if (firstLoad){
            delay(160)
            firstLoad = false
        }
        walkViewModel.viewModelScope.launch {
            val date =formatKoreanDateToYearMonth(currentMonth)
            walkViewModel.getMonthData(selectPet?.ownrPetUnqNo ?: "", date)
        }
    }

    Box (
        modifier= Modifier
            .fillMaxSize()
            .background(color = design_button_bg)
    ){
        Column (
            modifier= Modifier
                .align(Alignment.TopCenter)
        ){
            Spacer(modifier = Modifier.padding(top = 20.dp))

            Row (
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "", tint = design_white,
                    modifier = Modifier
                        .clickable {
                            walkViewModel.updateSelectMonth(addOneMonth(currentMonth, -1))
                        }
                )

                Text(
                    text = currentMonth,
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)), fontSize = 20.sp,
                    letterSpacing = (-1.0).sp, color = design_white
                )

                Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "", tint = design_white,
                    modifier = Modifier
                        .clickable {
                            walkViewModel.updateSelectMonth(addOneMonth(currentMonth, 1))
                        }
                )
            }

            Spacer(modifier = Modifier.padding(top = 20.dp))

            Row (
                modifier= Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .background(design_deep_blue, shape = RoundedCornerShape(8.dp))
                    .clip(shape = RoundedCornerShape(8.dp))
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ){
                Text(text = stringResource(id = R.string.sunday), fontSize = 12.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)), color = design_sharp)
                Text(text = stringResource(id = R.string.monday), fontSize = 12.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)), color = design_white)
                Text(text = stringResource(id = R.string.tuesday), fontSize = 12.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)), color = design_white)
                Text(text = stringResource(id = R.string.wednesday), fontSize = 12.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)), color = design_white)
                Text(text = stringResource(id = R.string.thursday), fontSize = 12.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)), color = design_white)
                Text(text = stringResource(id = R.string.friday), fontSize = 12.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)), color = design_white)
                Text(text = stringResource(id = R.string.saturday), fontSize = 12.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)), color = design_white)
            }

            CalendarMonthItem(walkViewModel = walkViewModel, selectPet = selectPet)


        }// column

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Spacer(modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 16.dp)
                .height(1.dp)
                .fillMaxWidth()
                .background(color = design_white)
            )

            RunCountData(walkViewModel = walkViewModel)

            Spacer(modifier = Modifier.padding(bottom = 20.dp))
        }
    }

}

@Composable
fun RunCountData(walkViewModel: WalkViewModel){

    val runCountData by walkViewModel.dailyMonth.collectAsState()

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
                text = stringResource(R.string.times),
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                letterSpacing = (-0.7).sp,
                color = design_C3D3EC,
                modifier = Modifier.padding(start = 20.dp)
            )

            Text(
                text = (runCountData?.runCnt ?: 0).toString() + stringResource(R.string.unit),
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                letterSpacing = 0.sp,
                modifier = Modifier.padding(top = 4.dp, start = 20.dp),
                color = design_white
            )
        }

        Spacer(modifier = Modifier
            .size(1.dp, 40.dp)
            .background(color = design_76A1EF))

        Column (
            modifier= Modifier
                .weight(1f)
        ){

            Text(
                text = stringResource(id = R.string.story_walk_time),
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                letterSpacing = (-0.7).sp,
                color = design_C3D3EC,
                modifier = Modifier.padding(start = 20.dp)
            )

            Text(
                text = runCountData?.runTime ?: "00:00:00",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                letterSpacing = 0.sp,
                modifier = Modifier
                    .padding(top = 4.dp, start = 20.dp),
                color = design_white
            )

        }

        Spacer(modifier = Modifier
            .size(1.dp, 40.dp)
            .background(color = design_76A1EF))

        Column (
            modifier= Modifier
                .weight(1f)
        ){

            Text(
                text = stringResource(id = R.string.story_walk_dist),
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                letterSpacing = (-0.7).sp,
                color = design_C3D3EC,
                modifier = Modifier.padding(start = 20.dp)
            )

            Text(
                text = (runCountData?.runDstnc ?: 0).toString()+"m",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                letterSpacing = 0.sp,
                modifier = Modifier
                    .padding(top = 4.dp, start = 20.dp),
                color = design_white
            )

        }
    }
}

@Composable
fun CalendarMonthItem(walkViewModel: WalkViewModel, selectPet: CurrentPetData?){

    val days by walkViewModel.dailyMonth.collectAsState()

    LazyVerticalGrid(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize(),
        columns = GridCells.Fixed(7),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp)
    ) {
        items(days?.dayList.orEmpty()){ day ->
            CalendarDay(day = day, viewModel = walkViewModel, selectPet = selectPet)
        }
    }
}

@Composable
fun CalendarDay(day: MonthDay, viewModel: WalkViewModel, selectPet: CurrentPetData?){

    val formChangeDay = extractDayFromDate(day.date)

    if ( day.thisMonthYn == "Y" ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = if(day.runCnt==0){
                    painterResource(id = R.drawable.attendance_default)
                } else{
                    painterResource(id = R.drawable.attendance_active)
                },
                contentDescription = "", tint = Color.Unspecified,
                modifier = Modifier.clickable {
                    viewModel.viewModelScope.launch {
                        selectPet?.ownrPetUnqNo?.let {
                            val result = viewModel.getWeekRecord(ownrPetUnqNo = it, searchDay = day.date)
                            if (result){
                                viewModel.updateSelectDay(day.date)
                                viewModel.updateToMonthCalendar(false)
                            }
                        }

                    }
                }
            )

            Spacer(modifier = Modifier.padding(top = 5.dp))

            Box(
                modifier = Modifier
                    .size(17.dp)
                    .clip(CircleShape)
                    .background(
                        color = if (day.todayYn == "Y") {
                            design_white
                        } else {
                            Color.Transparent
                        }
                    ),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = formChangeDay.toString(),
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    letterSpacing = (-0.6).sp,
                    lineHeight = 12.sp,
                    color = if(day.todayYn == "Y"){
                        design_intro_bg
                    }else{
                        design_C3D3EC
                    }
                )
            }
        }
    }
}

fun formatKoreanDateToYearMonth(inputDate: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy년 M월", Locale.KOREAN)
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM")

    val date = YearMonth.parse(inputDate, inputFormatter)
    return date.format(outputFormatter)
}

fun extractDayFromDate(dateString: String): Int {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = LocalDate.parse(dateString, formatter)
    return date.dayOfMonth
}

fun addOneMonth(inputDate: String, addOrMinus:Long): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy년 M월", Locale.KOREAN)
    val date = YearMonth.parse(inputDate, formatter)
    val addedDate = date.plusMonths(addOrMinus)
    return addedDate.format(formatter)
}