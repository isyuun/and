package net.pettip.app.navi.screens.myscreen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollState
import com.patrykandpatrick.vico.compose.component.lineComponent
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.chart.DefaultPointConnector
import com.patrykandpatrick.vico.core.chart.copy
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.chart.scale.AutoScaleUp
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.dimensions.MutableDimensions
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.marker.Marker
import com.patrykandpatrick.vico.core.marker.MarkerVisibilityChangeListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.component.BackTopBar
import net.pettip.app.navi.component.CustomTextField
import net.pettip.app.navi.component.ErrorScreen
import net.pettip.app.navi.component.rememberMarker
import net.pettip.app.navi.screens.mainscreen.CircleImage
import net.pettip.app.navi.screens.mainscreen.formatWghtVl
import net.pettip.app.navi.ui.theme.design_999999
import net.pettip.app.navi.ui.theme.design_DDDDDD
import net.pettip.app.navi.ui.theme.design_btn_border
import net.pettip.app.navi.ui.theme.design_button_bg
import net.pettip.app.navi.ui.theme.design_icon_bg
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_login_bg
import net.pettip.app.navi.ui.theme.design_login_text
import net.pettip.app.navi.ui.theme.design_placeHolder
import net.pettip.app.navi.ui.theme.design_select_btn_text
import net.pettip.app.navi.ui.theme.design_sharp
import net.pettip.app.navi.ui.theme.design_skip
import net.pettip.app.navi.ui.theme.design_textFieldOutLine
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.SettingViewModel
import net.pettip.app.navi.viewmodel.SharedViewModel
import net.pettip.data.pet.Member
import net.pettip.data.pet.PetDetailData
import net.pettip.singleton.G
import net.pettip.util.Log
import java.text.SimpleDateFormat
import java.util.Date


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PetProfileScreen(navController: NavHostController, sharedViewModel: SharedViewModel, settingViewModel: SettingViewModel){

    DisposableEffect(Unit){
        onDispose {
            settingViewModel.updateMemberList(null)
        }
    }

    val petInfo by sharedViewModel.petInfo.collectAsState()
    val selectedPet by sharedViewModel.profilePet.collectAsState()

    val memberList by settingViewModel.memberList.collectAsState()

    var weightRgstDialog by remember{ mutableStateOf(false) }
    var weightCNDDialog by remember{ mutableStateOf(false) }

    val modelProducer = remember{ChartEntryModelProducer()}
    val datasetForModel = remember{ mutableStateListOf(listOf<FloatEntry>()) }
    val datasetLineSpec = remember{ arrayListOf<LineChart.LineSpec>() }

    var refreshPetList by remember{ mutableStateOf(false) }
    var updatePetWgt by remember{ mutableStateOf(false) }

    var wgtRefresh by remember{ mutableStateOf(true) }
    var wgtError by remember{ mutableStateOf(false) }

    var detailRefresh by remember{ mutableStateOf(true) }
    var detailError by remember{ mutableStateOf(false) }

    val scrollState = rememberChartScrollState()
    val petwgtList by settingViewModel.petWeightList.collectAsState()

    val defaultLines = currentChartStyle.lineChart.lines
    val pointConnector = DefaultPointConnector(cubicStrength = 0.2f)
    val circleComponent = shapeComponent(
        shape = CircleShape,
        color = design_white,
        strokeWidth = 2.5.dp,
        strokeColor = design_sharp
    )

    val markerVisibilityChangeListener = MyMarkerVisibilityChangeListener()
    val xValue by markerVisibilityChangeListener.xValue.collectAsState()
    
    val lineChart = lineChart(
        remember(defaultLines) {
            defaultLines.map { defaultLine -> defaultLine.copy(
                pointConnector = pointConnector,
                lineColor = design_sharp.toArgb(),
                lineBackgroundShader = null,
                lineThicknessDp = 2.5f,
                point = circleComponent,
                pointSizeDp = 10.0f
            ) }
        }
    )

    LaunchedEffect(key1 = updatePetWgt){
        if (updatePetWgt){
            settingViewModel.viewModelScope.launch {
                sharedViewModel.loadPetInfo()
                sharedViewModel.loadCurrentPetInfo()
            }
            val result = settingViewModel.getPetWgt(selectedPet.ownrPetUnqNo)
            if (result){
                datasetForModel.clear()
                var xPos = 0f
                val dataPoints = arrayListOf<FloatEntry>()

                for (petWgt in petwgtList?: emptyList()){
                    dataPoints.add(FloatEntry(x= xPos, y= petWgt.wghtVl.toFloat()))
                    xPos += 1f
                }

                datasetForModel.add(dataPoints)
                modelProducer.setEntries(datasetForModel)
                updatePetWgt = false
            }
        }
    }
    LaunchedEffect(key1 = detailRefresh){
        if(detailRefresh){
            val result = settingViewModel.getPetInfoDetail(selectedPet)
            detailError = !result

            detailRefresh = false
        }
    }

    LaunchedEffect(key1 = wgtRefresh){
        if (wgtRefresh){

            val result = settingViewModel.getPetWgt(selectedPet.ownrPetUnqNo)

            wgtError = !result

            datasetForModel.clear()
            datasetLineSpec.clear()
            var xPos = 0f
            val dataPoints = arrayListOf<FloatEntry>()

            datasetLineSpec.add(
                LineChart.LineSpec(
                    lineColor = design_sharp.toArgb(),
                    lineBackgroundShader = DynamicShaders.fromBrush(
                        brush = Brush.verticalGradient(
                            listOf(
                                design_sharp.copy(com.patrykandpatrick.vico.core.DefaultAlpha.LINE_BACKGROUND_SHADER_START),
                                design_sharp.copy(com.patrykandpatrick.vico.core.DefaultAlpha.LINE_BACKGROUND_SHADER_END)
                            )
                        )
                    ),
                    //lineBackgroundShader = null,
                    pointConnector = pointConnector
                )
            )
            for (petWgt in petwgtList?: emptyList()){
                dataPoints.add(FloatEntry(x= xPos, y= petWgt.wghtVl.toFloat()))
                xPos += 1f
            }
            datasetForModel.add(dataPoints)

            modelProducer.setEntries(datasetForModel)

            wgtRefresh = false
        }

    }

    Scaffold (
        topBar = { BackTopBar(title = "${selectedPet.petNm} 프로필", navController = navController) }
    ) { paddingValues ->

        if (weightRgstDialog){
            WeightDialog(
                onDismiss = {newValue -> weightRgstDialog = newValue},
                viewModel = settingViewModel,
                confirm = "등록",
                dismiss = "취소",
                ownrPetUnqNo = selectedPet.ownrPetUnqNo,
                refresh = {newValue -> updatePetWgt = newValue}
            )
        }

        if (weightCNDDialog){
            WeightCNDDialog(
                onDismiss = {newValue -> weightCNDDialog = newValue},
                viewModel = settingViewModel,
                index = xValue,
                refresh = {newValue -> updatePetWgt = newValue}
            )
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)){
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${selectedPet.stdgCtpvNm} ${selectedPet.stdgSggNm} ${selectedPet.stdgUmdNm ?: ""}",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    letterSpacing = (-0.7).sp,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.padding(top = 20.dp))

                CircleImage(
                    size = 180,
                    imageUri = selectedPet.petRprsImgAddr
                )

                Spacer(modifier = Modifier.padding(top = 16.dp))

                Text(
                    text = selectedPet.petKindNm,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    letterSpacing = (-0.7).sp,
                    color = MaterialTheme.colorScheme.secondary
                )

                Text(
                    text = selectedPet.petNm,
                    fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    letterSpacing = (-0.7).sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.padding(top = 16.dp))

                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Row (
                        modifier=Modifier.align(Alignment.Center),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ){

                        Box (
                            modifier= Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(color = design_icon_bg),
                            contentAlignment = Alignment.Center
                        ){
                            Icon(painter = painterResource(id = R.drawable.icon_age), contentDescription = "", tint = Color.Unspecified)
                        }

                        Text(
                            text = if (selectedPet.petBrthYmd=="미상"){
                                "미상"
                            }else{
                                sharedViewModel.changeBirth(selectedPet.petBrthYmd)
                            },
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            letterSpacing = (-0.7).sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(start = 8.dp)
                        )

                        Spacer(modifier = Modifier.padding(start = 20.dp))

                        Box (
                            modifier= Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(color = design_icon_bg),
                            contentAlignment = Alignment.Center
                        ){
                            Icon(painter = painterResource(id = R.drawable.icon_gender), contentDescription = "", tint = Color.Unspecified)
                        }

                        Text(
                            text = selectedPet.sexTypNm?:"",
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            letterSpacing = (-0.7).sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(start = 8.dp)
                        )

                        Spacer(modifier = Modifier.padding(start = 20.dp))

                        Box (
                            modifier= Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(color = design_icon_bg),
                            contentAlignment = Alignment.Center
                        ){
                            Icon(painter = painterResource(id = R.drawable.icon_weight), contentDescription = "", tint = Color.Unspecified)
                        }

                        Text(
                            text = "${formatWghtVl(selectedPet.wghtVl)}kg",
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            letterSpacing = (-0.7).sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    } // Row
                }


                Crossfade(
                    targetState = wgtError,
                    label = ""
                ) {
                    when(it){
                        true ->
                            Row (
                                modifier = Modifier
                                    .padding(horizontal = 20.dp, vertical = 40.dp)
                                    .fillMaxWidth()
                            ){
                                Icon(
                                    painter = painterResource(id = R.drawable.img_error_light), contentDescription = "",
                                    tint = Color.Unspecified,modifier = Modifier.weight(1f)
                                )

                                Spacer(modifier = Modifier.padding(horizontal = 5.dp))

                                Column (
                                    modifier = Modifier.weight(1f)
                                ){
                                    Text(
                                        text = "일시적인 오류입니다.",
                                        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                                        fontSize = 24.sp, letterSpacing = (-1.2).sp,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.padding(top = 20.dp)
                                    )

                                    Button(
                                        onClick = {
                                            wgtRefresh = true
                                        },
                                        modifier = Modifier
                                            .padding(top = 12.dp)
                                            .fillMaxWidth()
                                            .height(48.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonColors(
                                            containerColor = design_button_bg, disabledContainerColor = design_button_bg,
                                            contentColor = design_white, disabledContentColor = design_white
                                        ),
                                        elevation = ButtonDefaults.buttonElevation(
                                            defaultElevation = 5.dp,
                                            pressedElevation = 0.dp
                                        )
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.icon_reflesh),
                                                contentDescription = "", tint = design_white
                                            )

                                            Text(
                                                text = "체중정보 새로고침",
                                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                                fontSize = 14.sp, letterSpacing = (-0.7).sp,
                                                color = design_white,
                                                lineHeight = 16.sp,
                                                modifier = Modifier.padding(start = 8.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        false ->
                            if (datasetForModel.isNotEmpty()){
                                val marker = rememberMarker()

                                Chart(
                                    modifier = Modifier
                                        .padding(20.dp)
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.primary)
                                        .combinedClickable(
                                            onLongClick = {
                                                weightCNDDialog = true
                                            },
                                            onClick = {}
                                        ),
                                    chart = lineChart,
                                    chartModelProducer = modelProducer,
                                    startAxis = rememberStartAxis(
                                        label = textComponent(
                                            color = MaterialTheme.colorScheme.secondary,
                                            textSize = 12.sp,
                                            margins = MutableDimensions(10f, 0f),
                                        ),
                                        tickLength = 0.dp,
                                        valueFormatter = { value,_ ->
                                            String.format("%.1f", value)
                                        },
                                        itemPlacer = AxisItemPlacer.Vertical.default(
                                            maxItemCount = 6
                                        ),
                                        axis = null
                                    ),
                                    bottomAxis = rememberBottomAxis(
                                        label = textComponent(
                                            color = MaterialTheme.colorScheme.secondary,
                                            textSize = 12.sp,
                                            margins = MutableDimensions(0f, 10f),
                                        ),
                                        axis = lineComponent(color = design_textFieldOutLine, thickness = 1.dp),
                                        tickLength = 0.dp,
                                        valueFormatter = { value,_ ->
                                            if (petwgtList != null && value.toInt() in 0 until petwgtList!!.size) {
                                                petwgtList?.getOrNull(value.toInt())?.crtrYmd ?: ""
                                            } else {
                                                ""
                                            }
                                        },
                                        itemPlacer = AxisItemPlacer.Horizontal.default(

                                        ),
                                        guideline = null,
                                    ),
                                    marker = marker,
                                    chartScrollState = scrollState,
                                    markerVisibilityChangeListener = markerVisibilityChangeListener,
                                    isZoomEnabled = true,
                                    autoScaleUp = AutoScaleUp.None
                                )
                            }
                    }
                }

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Button(
                        enabled = selectedPet.mngrType == "M",
                        onClick = { navController.navigate("modifyPetInfoScreen") },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = design_select_btn_text,
                            disabledContainerColor = design_select_btn_text
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp, pressedElevation = 0.dp),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text(
                            text = if(selectedPet.mngrType == "M"){"정보 수정하기"}else{"관리자만 수정가능"},
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = 14.sp, letterSpacing = (-0.7).sp,
                            color = design_white
                        )
                    }

                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                    Button(
                        enabled = selectedPet.mngrType != "C",
                        onClick = { weightRgstDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = MaterialTheme.colorScheme.primary
                        ),
                        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp, pressedElevation = 0.dp),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .padding(end = 20.dp)
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text(
                            text = if(selectedPet.mngrType != "C"){"몸무게 등록"}else{"참여자만 등록가능"},
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = 14.sp, letterSpacing = (-0.7).sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.padding(top = 40.dp))

                AnimatedVisibility(
                    visible = selectedPet.mngrType != "C" && memberList?.isNotEmpty()==true,
                    enter = fadeIn(tween(durationMillis = 700, delayMillis = 200)).plus(expandVertically()),
                    exit = fadeOut(tween(durationMillis = 700, delayMillis = 200)).plus(shrinkVertically())
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = MaterialTheme.colorScheme.onPrimaryContainer)
                    ){
                        Text(
                            text = "참여중인 그룹",
                            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                            fontSize = 20.sp, letterSpacing = (-1.0).sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(start = 20.dp, top = 20.dp)
                        )

                        Spacer(modifier = Modifier.padding(bottom = 16.dp))

                        LazyColumn(
                            state = rememberLazyListState(),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.heightIn(max = 300.dp)
                        ){
                            items(memberList?: emptyList()){ item ->
                                GroupItem(item = item, selectedPet, settingViewModel)
                            }
                        }

                        Spacer(modifier = Modifier.padding(bottom = 20.dp))
                    }
                }

                if (detailError){
                    Row (
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 40.dp)
                            .fillMaxWidth()
                    ){
                        Icon(
                            painter = painterResource(id = R.drawable.img_error_light), contentDescription = "",
                            tint = Color.Unspecified,modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.padding(horizontal = 5.dp))

                        Column (
                            modifier = Modifier.weight(1f)
                        ){
                            Text(
                                text = "일시적인 오류입니다.",
                                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                                fontSize = 24.sp, letterSpacing = (-1.2).sp,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(top = 20.dp)
                            )

                            Button(
                                onClick = {
                                    detailRefresh = true
                                },
                                modifier = Modifier
                                    .padding(top = 12.dp)
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonColors(
                                    containerColor = design_button_bg, disabledContainerColor = design_button_bg,
                                    contentColor = design_white, disabledContentColor = design_white
                                ),
                                elevation = ButtonDefaults.buttonElevation(
                                    defaultElevation = 5.dp,
                                    pressedElevation = 0.dp
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.icon_reflesh),
                                        contentDescription = "", tint = design_white
                                    )

                                    Text(
                                        text = "참여중인 그룹 새로고침",
                                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                        fontSize = 14.sp, letterSpacing = (-0.7).sp,
                                        color = design_white,
                                        lineHeight = 16.sp,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }// Col
        }
    }
}

@Composable
fun GroupItem(item:Member,petInfo:PetDetailData, viewModel: SettingViewModel){

    var expandText by remember{ mutableStateOf(false) }
    var expandButton by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Row (
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(modifier = Modifier
            .size(40.dp)
        ){
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("")
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                placeholder = painterResource(id = R.drawable.profile_person),
                error= painterResource(id = R.drawable.profile_person),
                modifier= Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            if (item.mngrType=="M"){
                Box(modifier = Modifier
                    .size(12.dp)
                    .clip(shape = RoundedCornerShape(4.dp))
                    .background(color = design_intro_bg, shape = RoundedCornerShape(4.dp))
                    .align(Alignment.TopStart)
                ){
                    Icon(painter = painterResource(id = R.drawable.icon_admin),
                        contentDescription = "", tint = Color.Unspecified,
                        modifier = Modifier.align(Alignment.Center))
                }
            }
            
        }
        
        Text(
            text = item.nckNm,
            fontFamily = FontFamily(Font(R.font.pretendard_medium)),
            fontSize = 16.sp, letterSpacing = (-0.8).sp,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Box (
            modifier= Modifier
                .border(
                    when (item.mngrType) {
                        "M" -> 0.dp
                        "I" -> 1.dp
                        "C" -> 0.dp
                        else -> 0.dp
                    },
                    color = design_button_bg,
                    shape = RoundedCornerShape(10.dp)
                )
                .background(
                    color =
                    when (item.mngrType) {
                        "M" -> design_button_bg
                        "I" -> Color.Transparent
                        "C" -> design_DDDDDD
                        else -> design_DDDDDD
                    },
                    shape = RoundedCornerShape(10.dp)
                )
                .clip(RoundedCornerShape(10.dp))
                .clickable(
                    enabled = (item.mngrType == "I" && petInfo.petMngrYn == "Y") || (petInfo.petMngrYn == "N" && item.userId == G.userId),
                    onClick = {
                        scope.launch {
                            if (expandText) {
                                expandButton = !expandText
                                delay(450)
                                expandText = !expandText
                            } else {
                                expandButton = !expandText
                                expandText = !expandText
                            }
                        }
                    }
                )
                .animateContentSize(tween(durationMillis = 400, easing = LinearOutSlowInEasing)),
            contentAlignment = Alignment.Center
        ){
            Text(
                text =
                when(item.mngrType){
                    "M" -> "관리중"
                    "I" -> if (!expandText)"참여중" else "참여를 중단하시겠습니까?"
                    "C" -> "동행중단"
                    else -> "에러"
                },
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 12.sp,
                letterSpacing = (-0.6).sp,
                color =
                when(item.mngrType){
                    "M" -> design_white
                    "I" -> design_button_bg
                    "C" -> design_999999
                    else -> design_DDDDDD
                },
                modifier = Modifier.padding(horizontal = 9.dp, vertical = 2.dp)
            )
        }

        Spacer(modifier = Modifier.padding(start = 16.dp))

        if (item.mngrType == "C"){
            Text(
                text = item.endDt,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 12.sp, letterSpacing = (-0.6).sp,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        AnimatedVisibility(
            visible =  expandButton,
            enter = scaleIn(tween(delayMillis = 300)),
            exit = scaleOut(tween(durationMillis = 300))
        ) {
            Box (
                modifier= Modifier
                    .background(
                        color = design_button_bg,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        scope.launch {
                            val result = viewModel.relClose(petInfo.ownrPetUnqNo, item.petRelUnqNo)
                            if (result) {
                                expandButton = false
                                expandText = false
                                viewModel.getPetInfoDetail(petInfo)
                            } else {
                                Log.d("LOG", "실패")
                            }
                        }

                    },
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "네",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 12.sp,
                    letterSpacing = (-0.6).sp,
                    color = design_white,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
                )
            }
        }
    }// row
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeightCNDDialog(
    onDismiss: (Boolean) -> Unit,
    viewModel: SettingViewModel,
    index: Int,
    refresh:(Boolean) -> Unit
){

    val petWeightList by viewModel.petWeightList.collectAsState()

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val petWeight by viewModel.petWeight.collectAsState()
    val dm by viewModel.regDM.collectAsState()

    DisposableEffect(Unit){

        viewModel.updatePetWeight(petWeightList?.getOrNull(index)?.wghtVl.toString())

        onDispose {
            viewModel.updatePetWeight("")
        }
    }

    AlertDialog(
        onDismissRequest = { onDismiss(false) },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ){
        Box (
            modifier = Modifier
                .padding(horizontal = 40.dp)
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(20.dp))
        ){
            Column (
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = "등록일자",
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    fontSize = 16.sp, letterSpacing = (-0.8).sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 8.dp)
                )

                Button(
                    enabled = false,
                    onClick = {  },
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .height(48.dp),
                    border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline),
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart){
                        Text(
                            text = runCatching<String?> { petWeightList?.get(index)?.crtrYmd }.getOrElse { "" } ?: "",
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = 14.sp, letterSpacing = (-0.7).sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

                Text(
                    text = "몸무게",
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    fontSize = 16.sp, letterSpacing = (-0.8).sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 8.dp)
                )

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    CustomTextField(
                        value = petWeight,
                        onValueChange = { viewModel.updatePetWeight(it) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Done),
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .weight(1f)
                            .height(48.dp),
                        placeholder = { Text(text = "몸무게를 입력해주세요", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
                            focusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                            unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                            focusedContainerColor = MaterialTheme.colorScheme.primary,
                            unfocusedLeadingIconColor = MaterialTheme.colorScheme.primaryContainer,
                            focusedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
                            cursorColor = design_intro_bg.copy(alpha = 0.5f)
                        ),
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = 16.sp, letterSpacing = (-0.4).sp
                        ),
                        shape = RoundedCornerShape(4.dp),
                        innerPadding = PaddingValues(start = 8.dp)
                    )

                    Text(
                        text = "kg",
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 14.sp, letterSpacing = (-0.7).sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(start = 8.dp,end = 20.dp)
                    )
                }


                Row (
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                ){
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(MaterialTheme.colorScheme.onSecondary)
                            .clickable {
                                if (petWeightList?.size == 1) {
                                    Toast
                                        .makeText(context, "삭제 할 수 없습니다", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    scope.launch {
                                        val petDtlUnqNo = petWeightList?.get(index)?.petDtlUnqNo ?: 0

                                        val result = viewModel.deletePetWgt(petDtlUnqNo)
                                        if (result) {
                                            onDismiss(false)
                                            refresh(true)
                                            Toast
                                                .makeText(context, "삭제되었습니다", Toast.LENGTH_SHORT)
                                                .show()
                                        } else {
                                            Toast
                                                .makeText(context, dm, Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "삭제",
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
                                if (isValidFloat(petWeight)) {
                                    scope.launch {
                                        val crtrYmd = petWeightList?.get(index)?.crtrYmd ?: ""
                                        val petDtlUnqNo = petWeightList?.get(index)?.petDtlUnqNo ?: 0
                                        val wghtVl = petWeight.toFloat()

                                        val result = viewModel.changePetWgt(crtrYmd, petDtlUnqNo, wghtVl)
                                        if (result) {
                                            onDismiss(false)
                                            refresh(true)
                                            Toast
                                                .makeText(context, "수정되었습니다", Toast.LENGTH_SHORT)
                                                .show()
                                        } else {
                                            Toast
                                                .makeText(context, dm, Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                    }
                                } else {
                                    Toast
                                        .makeText(context, "올바른 체중을 입력해주세요", Toast.LENGTH_SHORT)
                                        .show()
                                }

                            },
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "변경",
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeightDialog(
    onDismiss: (Boolean) -> Unit,
    viewModel: SettingViewModel,
    confirm: String,
    dismiss: String,
    ownrPetUnqNo: String,
    refresh:(Boolean) -> Unit
){

    val petWeight by viewModel.petWeight.collectAsState()
    val regDM by viewModel.regDM.collectAsState()

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(selectableDates = MySelectableDates() )
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var pickDate by remember{ mutableStateOf("") }

    DisposableEffect(Unit){
        onDispose {
            viewModel.updatePetWeight("")
            viewModel.updatePetWeightRgDate("")
        }
    }

    AlertDialog(
        onDismissRequest = { onDismiss(false) },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ){
        AnimatedVisibility(
            visible = showDatePicker,
            enter = fadeIn(tween(durationMillis = 500)),
            exit = fadeOut()
        ) {
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
                                text = "취소",
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
                                    val sdfDateForSend = SimpleDateFormat("yyyyMMdd")
                                    val dateForSend = sdfDateForSend.format(Date(datePickerState.selectedDateMillis ?: Date().time))

                                    val sdfDate = SimpleDateFormat("yyyy년 MM월 dd일")
                                    val date = sdfDate.format(Date(datePickerState.selectedDateMillis ?: Date().time))

                                    pickDate = date
                                    viewModel.updatePetWeightRgDate(dateForSend)
                                    showDatePicker = false
                                },
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = "확인",
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

        AnimatedVisibility(
            visible = !showDatePicker,
            enter = fadeIn(tween(durationMillis = 500)),
            exit = fadeOut()
        ) {
            Box (
                modifier = Modifier
                    .padding(horizontal = 40.dp)
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(20.dp))
            ){
                Column (
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(
                        text = "등록일자",
                        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                        fontSize = 16.sp, letterSpacing = (-0.8).sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 8.dp)
                    )

                    Button(
                        onClick = { showDatePicker = true },
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth()
                            .height(48.dp),
                        border = BorderStroke(width = 1.dp, color = design_textFieldOutLine),
                        shape = RoundedCornerShape(4.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart){
                            Text(
                                text =
                                if(pickDate ==""){ "등록일자를 입력해주세요" } else { pickDate },
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                fontSize = 14.sp, letterSpacing = (-0.7).sp,
                                color = if(pickDate ==""){ MaterialTheme.colorScheme.primaryContainer } else { MaterialTheme.colorScheme.onPrimary },
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }

                    Text(
                        text = "몸무게",
                        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                        fontSize = 16.sp, letterSpacing = (-0.8).sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 8.dp)
                    )

                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        CustomTextField(
                            value = petWeight,
                            onValueChange = { viewModel.updatePetWeight(it) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Done),
                            modifier = Modifier
                                .padding(start = 20.dp)
                                .weight(1f)
                                .height(48.dp),
                            placeholder = { Text(text = "몸무게를 입력해주세요", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
                                focusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                                unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                                focusedContainerColor = MaterialTheme.colorScheme.primary,
                                unfocusedLeadingIconColor = MaterialTheme.colorScheme.primaryContainer,
                                focusedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
                                cursorColor = design_intro_bg.copy(alpha = 0.5f)
                            ),
                            textStyle = TextStyle(
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                fontSize = 16.sp, letterSpacing = (-0.4).sp
                            ),
                            shape = RoundedCornerShape(4.dp),
                            innerPadding = PaddingValues(start = 8.dp)
                        )

                        Text(
                            text = "kg",
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = 14.sp, letterSpacing = (-0.7).sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(start = 8.dp,end = 20.dp)
                        )
                    }


                    Row (
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                    ){
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(MaterialTheme.colorScheme.onSecondary)
                                .clickable { onDismiss(false) },
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = dismiss,
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
                                    if (pickDate == "") {
                                        Toast
                                            .makeText(context, "날짜를 입력해주세요", Toast.LENGTH_SHORT)
                                            .show()
                                    } else if (!isValidFloat(petWeight)) {
                                        Toast
                                            .makeText(context, "올바른 체중을 입력해주세요", Toast.LENGTH_SHORT)
                                            .show()
                                    } else {
                                        scope.launch {
                                            val result = viewModel.regPetWgt(ownrPetUnqNo)
                                            if (result) {
                                                onDismiss(false)
                                                refresh(true)
                                                Toast
                                                    .makeText(context, "등록되었습니다", Toast.LENGTH_SHORT)
                                                    .show()
                                            } else {
                                                Toast
                                                    .makeText(context, regDM, Toast.LENGTH_SHORT)
                                                    .show()
                                            }
                                        }
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = confirm,
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
}

class MyMarkerVisibilityChangeListener() : MarkerVisibilityChangeListener {

    private val _xValue = MutableStateFlow(0)
    val xValue: StateFlow<Int> = _xValue.asStateFlow()

    override fun onMarkerShown(marker: Marker, markerEntryModels: List<Marker.EntryModel>) {

        val firstEntryModel = markerEntryModels.firstOrNull()
        if (firstEntryModel != null) {
            val index = firstEntryModel.index
            _xValue.value = index
            
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
class MySelectableDates : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val now = System.currentTimeMillis()
        return utcTimeMillis <= now // 현재 날짜 및 이전 날짜는 선택 가능, 이후 날짜는 선택 불가능
    }
}

fun isValidFloat(input: String): Boolean {
    return try {
        input.toFloat()
        true
    } catch (e: NumberFormatException) {
        false
    }
}