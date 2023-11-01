package kr.carepet.app.navi.screens.myscreen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.carepet.app.navi.R
import kr.carepet.app.navi.component.BackTopBar
import kr.carepet.app.navi.component.CustomTextField
import kr.carepet.app.navi.screens.mainscreen.CircleImage
import kr.carepet.app.navi.ui.theme.design_999999
import kr.carepet.app.navi.ui.theme.design_DDDDDD
import kr.carepet.app.navi.ui.theme.design_btn_border
import kr.carepet.app.navi.ui.theme.design_button_bg
import kr.carepet.app.navi.ui.theme.design_icon_bg
import kr.carepet.app.navi.ui.theme.design_intro_bg
import kr.carepet.app.navi.ui.theme.design_login_bg
import kr.carepet.app.navi.ui.theme.design_login_text
import kr.carepet.app.navi.ui.theme.design_placeHolder
import kr.carepet.app.navi.ui.theme.design_select_btn_text
import kr.carepet.app.navi.ui.theme.design_skip
import kr.carepet.app.navi.ui.theme.design_textFieldOutLine
import kr.carepet.app.navi.ui.theme.design_white
import kr.carepet.app.navi.viewmodel.SettingViewModel
import kr.carepet.app.navi.viewmodel.SharedViewModel
import kr.carepet.data.pet.Member
import kr.carepet.data.pet.PetDetailData
import kr.carepet.singleton.G
import kr.carepet.util.Log
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetProfileScreen(navController: NavHostController, sharedViewModel: SharedViewModel, settingViewModel: SettingViewModel,index: String?){

    DisposableEffect(Unit){
        onDispose {
            Log.d("LOG","DISPOS")
            settingViewModel.updateMemberList(null)
        }
    }

    val originPetInfo by sharedViewModel.petInfo.collectAsState()
    val petInfo = originPetInfo.sortedBy {
        when (it.mngrType) {
            "M" -> 1
            "I" -> 2
            "C" -> 3
            else -> 4
        }
    }

    val indexInt = index?.toInt() ?: 0

    val originMemberList by settingViewModel.memberList.collectAsState()
    val memberList = originMemberList?.sortedBy {
        when (it.mngrType) {
            "M" -> 1
            "I" -> 2
            "C" -> 3
            else -> 4
        }
    }

    var weightRgstDialog by remember{ mutableStateOf(false) }

    LaunchedEffect(Unit){
        settingViewModel.getPetInfoDetail(petInfo[indexInt])
    }

    Scaffold (
        topBar = { BackTopBar(title = "${petInfo[indexInt].petNm} 프로필", navController = navController) }
    ) { paddingValues ->

        if (weightRgstDialog){
            WeightDialog(
                onDismiss = {newValue -> weightRgstDialog = newValue},
                viewModel = settingViewModel,
                confirm = "등록",
                dismiss = "취소",
                ownrPetUnqNo = petInfo[indexInt].ownrPetUnqNo
            )
        }


        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(design_white),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${petInfo[indexInt].stdgCtpvNm} ${petInfo[indexInt].stdgSggNm} ${petInfo[indexInt].stdgUmdNm}",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                letterSpacing = (-0.7).sp,
                color = design_skip
            )

            Spacer(modifier = Modifier.padding(top = 20.dp))

            CircleImage(
                size = 180,
                imageUri = petInfo[indexInt].petRprsImgAddr
            )

            Spacer(modifier = Modifier.padding(top = 16.dp))

            Text(
                text = petInfo[indexInt].petKindNm,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                letterSpacing = (-0.7).sp,
                color = design_skip
            )

            Text(
                text = petInfo[indexInt].petNm,
                fontSize = 30.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                letterSpacing = (-0.7).sp,
                color = design_login_text
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
                        text = if (petInfo[indexInt].petBrthYmd=="미상"){
                            "미상"
                        }else{
                            sharedViewModel.changeBirth(petInfo[indexInt].petBrthYmd)
                        },
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp,
                        color = design_login_text,
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
                        text = petInfo[indexInt].sexTypNm?:"",
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp,
                        color = design_login_text,
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
                        text = "${petInfo[indexInt].wghtVl}kg",
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp,
                        color = design_login_text,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                } // Row
            }

            Spacer(modifier = Modifier.padding(top = 20.dp))

            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Button(
                    enabled = petInfo[indexInt].mngrType == "M",
                    onClick = { navController.navigate("modifyPetInfoScreen/${indexInt}") },
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
                        text = if(petInfo[indexInt].mngrType == "M"){"정보 수정하기"}else{"관리자만 수정가능"},
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 14.sp, letterSpacing = (-0.7).sp,
                        color = design_white
                    )
                }

                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                
                Button(
                    enabled = petInfo[indexInt].mngrType != "C",
                    onClick = { weightRgstDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = design_white,
                        disabledContainerColor = design_white
                    ),
                    border = BorderStroke(width = 1.dp, color = design_btn_border),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp, pressedElevation = 0.dp),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text(
                        text = if(petInfo[indexInt].mngrType != "C"){"몸무게 등록"}else{"참여자만 등록가능"},
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 14.sp, letterSpacing = (-0.7).sp,
                        color = design_login_text
                    )
                }
            }

            Spacer(modifier = Modifier.padding(top = 40.dp))

            AnimatedVisibility(
                visible = petInfo[indexInt].mngrType != "C" && memberList?.isNotEmpty()==true,
                enter = fadeIn(tween(durationMillis = 700, delayMillis = 200)).plus(expandVertically()),
                exit = fadeOut(tween(durationMillis = 700, delayMillis = 200)).plus(shrinkVertically())
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = design_login_bg)
                ){
                    Text(
                        text = "참여중인 그룹",
                        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                        fontSize = 20.sp, letterSpacing = (-1.0).sp,
                        color = design_login_text,
                        modifier = Modifier.padding(start = 20.dp, top = 20.dp)
                    )

                    Spacer(modifier = Modifier.padding(bottom = 16.dp))

                    LazyColumn(
                        state = rememberLazyListState(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.heightIn(max = 300.dp)
                    ){
                        items(memberList?: emptyList()){ item ->
                            GroupItem(item = item, petInfo[indexInt], settingViewModel)
                        }
                    }

                    Spacer(modifier = Modifier.padding(bottom = 20.dp))
                }
            }
        }// Col
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
            color = design_login_text,
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
                    color = design_btn_border,
                    shape = RoundedCornerShape(10.dp)
                )
                .background(
                    color =
                    when (item.mngrType) {
                        "M" -> design_button_bg
                        "I" -> design_white
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
                    "C" -> "동참중단"
                    else -> "에러"
                },
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 12.sp,
                letterSpacing = (-0.6).sp,
                color =
                when(item.mngrType){
                    "M" -> design_white
                    "I" -> design_btn_border
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
                color = design_skip
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
fun WeightDialog(
    onDismiss: (Boolean) -> Unit,
    viewModel: SettingViewModel,
    confirm: String,
    dismiss: String,
    ownrPetUnqNo: String
){

    val petWeight by viewModel.petWeight.collectAsState()

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var weight by remember{ mutableStateOf("") }

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
                    .background(color = design_white, shape = RoundedCornerShape(20.dp))
            ) {
                Column {
                    DatePicker(
                        state = datePickerState,
                        colors = DatePickerDefaults.colors(
                            selectedDayContainerColor = design_intro_bg,
                            selectedDayContentColor = design_white,
                            todayDateBorderColor = design_intro_bg,
                            todayContentColor = design_intro_bg
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
                                .background(design_DDDDDD)
                                .clickable { showDatePicker = false },
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = "취소",
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                fontSize = 14.sp, letterSpacing = (-0.7).sp,
                                color = design_login_text,
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

                                    weight = date
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
                    .background(color = design_white, shape = RoundedCornerShape(20.dp))
            ){
                Column (
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(
                        text = "등록일자",
                        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                        fontSize = 16.sp, letterSpacing = (-0.8).sp,
                        color = design_login_text,
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
                            containerColor = design_white
                        )
                    ) {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart){
                            Text(
                                text =
                                if(weight ==""){ "등록일자를 입력해주세요" } else { weight },
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                fontSize = 14.sp, letterSpacing = (-0.7).sp,
                                color = if(weight ==""){ design_placeHolder } else { design_login_text },
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }

                    Text(
                        text = "몸무게",
                        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                        fontSize = 16.sp, letterSpacing = (-0.8).sp,
                        color = design_login_text,
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
                                unfocusedPlaceholderColor = design_placeHolder,
                                focusedPlaceholderColor = design_placeHolder,
                                unfocusedBorderColor = design_textFieldOutLine,
                                focusedBorderColor = design_login_text,
                                unfocusedContainerColor = design_white,
                                focusedContainerColor = design_white,
                                unfocusedLeadingIconColor = design_placeHolder,
                                focusedLeadingIconColor = design_login_text),
                            shape = RoundedCornerShape(4.dp),
                            innerPadding = PaddingValues(start = 8.dp)
                        )

                        Text(
                            text = "kg",
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = 14.sp, letterSpacing = (-0.7).sp,
                            color = design_login_text,
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
                                .background(design_DDDDDD)
                                .clickable { onDismiss(false) },
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = dismiss,
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                fontSize = 14.sp, letterSpacing = (-0.7).sp,
                                color = design_login_text,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(design_intro_bg)
                                .clickable {
                                    scope.launch {
                                        val result = viewModel.regPetWgt(ownrPetUnqNo)
                                        if (result) {
                                            onDismiss(false)
                                            Toast
                                                .makeText(context, "등록되었습니다", Toast.LENGTH_SHORT)
                                                .show()
                                        } else {
                                            Toast
                                                .makeText(context, "등록에 실패했습니다", Toast.LENGTH_SHORT)
                                                .show()
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

