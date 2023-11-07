package kr.carepet.app.navi.component

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
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.carepet.app.navi.R
import kr.carepet.app.navi.ui.theme.design_76A1EF
import kr.carepet.app.navi.ui.theme.design_C3D3EC
import kr.carepet.app.navi.ui.theme.design_button_bg
import kr.carepet.app.navi.ui.theme.design_deep_blue
import kr.carepet.app.navi.ui.theme.design_intro_bg
import kr.carepet.app.navi.ui.theme.design_sharp
import kr.carepet.app.navi.ui.theme.design_white
import kr.carepet.app.navi.viewmodel.SharedViewModel
import kr.carepet.app.navi.viewmodel.WalkViewModel
import kr.carepet.data.daily.MonthDay
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun MonthCalendar(walkViewModel: WalkViewModel, sharedViewModel: SharedViewModel) {

    val selectPet by sharedViewModel.selectPet.collectAsState()

    var currentMonth by remember { mutableStateOf(getCurrentYearMonthKr()) }

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
                Icon(imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "", tint = design_white,
                    modifier = Modifier.clickable { currentMonth = addOneMonth(currentMonth, -1) })

                Text(
                    text = currentMonth,
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)), fontSize = 20.sp,
                    letterSpacing = (-1.0).sp, color = design_white
                )

                Icon(imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "", tint = design_white,
                    modifier = Modifier.clickable { currentMonth = addOneMonth(currentMonth, 1) })
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
                Text(text = "일", fontSize = 12.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)), color = design_sharp)
                Text(text = "월", fontSize = 12.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)), color = design_white)
                Text(text = "화", fontSize = 12.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)), color = design_white)
                Text(text = "수", fontSize = 12.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)), color = design_white)
                Text(text = "목", fontSize = 12.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)), color = design_white)
                Text(text = "금", fontSize = 12.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)), color = design_white)
                Text(text = "토", fontSize = 12.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)), color = design_white)
            }

            CalendarMonthItem(walkViewModel = walkViewModel)


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
                text = "횟수",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                letterSpacing = (-0.7).sp,
                color = design_C3D3EC,
                modifier = Modifier.padding(start = 20.dp)
            )

            Text(
                text = (runCountData?.runCnt ?: 0).toString() + "회",
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
                text = "산책 시간",
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
                text = "산책 거리",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                letterSpacing = (-0.7).sp,
                color = design_C3D3EC,
                modifier = Modifier.padding(start = 20.dp)
            )

            Text(
                text = (runCountData?.runDstnc ?: 0).toString()+"km",
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
fun CalendarMonthItem(walkViewModel: WalkViewModel){

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
            CalendarDay(day = day)
        }
    }
}

@Composable
fun CalendarDay(day: MonthDay){

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
                contentDescription = "", tint = Color.Unspecified)

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

fun getCurrentYearMonthKr(): String {
    val currentYearMonth = YearMonth.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy년 M월", Locale.getDefault())
    return currentYearMonth.format(formatter)
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