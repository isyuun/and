package net.pettip.app.navi.screens.mainscreen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.Screen
import net.pettip.app.navi.component.CircleImageTopBar
import net.pettip.app.navi.component.CustomAlertOneBtn
import net.pettip.app.navi.component.LoadingDialog
import net.pettip.app.navi.component.Toasty
import net.pettip.app.navi.ui.theme.design_999999
import net.pettip.app.navi.ui.theme.design_DDDDDD
import net.pettip.app.navi.ui.theme.design_EFECFE
import net.pettip.app.navi.ui.theme.design_button_bg
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_login_text
import net.pettip.app.navi.ui.theme.design_select_btn_bg
import net.pettip.app.navi.ui.theme.design_select_btn_text
import net.pettip.app.navi.ui.theme.design_skip
import net.pettip.app.navi.ui.theme.design_textFieldOutLine
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.SettingViewModel
import net.pettip.app.navi.viewmodel.SharedViewModel
import net.pettip.data.pet.PetDetailData
import net.pettip.gps.app.GPSApplication
import net.pettip.singleton.MySharedPreference
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MyScreen(navController: NavHostController, viewModel:SettingViewModel, sharedViewModel: SharedViewModel){

    val petInfo by sharedViewModel.petInfo.collectAsState()

    val bottomSheetState =
        androidx.compose.material3.rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val application = GPSApplication.instance
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val density = LocalDensity.current.density
    val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")

    val navigationBarHeight = if (resourceId > 0) {
        (context.resources.getDimensionPixelSize(resourceId) / density).dp
    } else {
        0.dp
    }

    var openLogoutDialog by remember{ mutableStateOf(false) }
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val timePickerState = rememberTimePickerState()
    val datePickerState = rememberDatePickerState(selectableDates = MySelectableDates2())
    val snackState = remember { SnackbarHostState() }
    var openDialog by remember { mutableStateOf(false) }
    var openTimePicker by remember { mutableStateOf(false) }
    var logout by remember{ mutableStateOf(false) }

    LaunchedEffect(key1 = logout){
        if (logout){
            application.stop()
            openLogoutDialog = false
            isLoading = true
            viewModel.viewModelScope.launch {
                val result = viewModel.logOut()
                if (result){
                    isLoading = false
                    navController.navigate(Screen.Login.route){
                        popUpTo(0)
                    }
                }else{
                    isLoading = false
                    logout = false
                    Toast.makeText(context, R.string.retry, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold (
        snackbarHost = { Toasty(snackState = snackState) }
    ){ paddingValues ->

        LoadingDialog(
            loadingText = stringResource(R.string.logout_ing),
            loadingState = isLoading
        )

        LogoutDialog(
            dialogState = openLogoutDialog,
            onDismiss = {newValue -> openLogoutDialog = newValue},
            onLogoutState = {newValue -> logout = newValue}
        )

        Column (
            modifier= Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .verticalScroll(rememberScrollState())
        ){

            Row (
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.onPrimaryContainer)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(painter = painterResource(id = R.drawable.profile_person),
                        contentDescription = "", tint = Color.Unspecified,
                        modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 20.dp))

                    Text(
                        text = MySharedPreference.getUserNickName(),
                        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                        fontSize = 20.sp, letterSpacing = (-1.0).sp,
                        color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.padding(start = 12.dp)
                    )

                    Icon(painter = painterResource(id = R.drawable.icon_modify),
                        contentDescription = "", tint = Color.Unspecified,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .clickable {
                                navController.navigate(Screen.UserInfoScreen.route)
                            })
                }

                Button(
                    onClick = {
                        // 산책중인지 아닌지 여부체크
                        if (application.start){
                            openLogoutDialog = true
                        }else{
                            isLoading = true
                            viewModel.viewModelScope.launch {
                                val result = viewModel.logOut()
                                if (result){
                                    isLoading = false
                                    navController.navigate(Screen.Login.route){
                                        popUpTo(0)
                                    }
                                }else{
                                    isLoading = false
                                    Toast.makeText(context, R.string.retry, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                    },
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .width(80.dp)
                        .height(30.dp),
                    shape = RoundedCornerShape(10.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = design_button_bg),
                    contentPadding = PaddingValues(0.dp)
                )
                {
                    Text(
                        text = stringResource(R.string.logout),
                        color = design_white,
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp,
                    )
                }
            }

            Spacer(modifier = Modifier.padding(top = 20.dp))

            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = stringResource(R.string.management_pet),
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    fontSize = 20.sp, letterSpacing = (-1.0).sp,
                    color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.padding(start = 20.dp)
                )

                Row {
                    Button(
                        onClick = {
                            if (petInfo[0].ownrPetUnqNo!=""){
                                openBottomSheet = true
                            }
                                  },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        ),
                        contentPadding = PaddingValues(0.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.invite),
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = 14.sp, letterSpacing = (-0.7).sp,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.padding(vertical = 7.dp, horizontal = 17.dp)
                        )
                    }

                    Button(
                        onClick = {
                            navController.navigate(Screen.SetKeyScreen.route)
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant),
                        contentPadding = PaddingValues(0.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp
                        ),
                        modifier = Modifier.padding(end = 20.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.ivt_code_reg),
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = 14.sp, letterSpacing = (-0.7).sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(vertical = 7.dp, horizontal = 17.dp)
                        )
                    }
                }

            }

            Spacer(modifier = Modifier.padding(top = 16.dp))

            if (petInfo.isNotEmpty() && petInfo[0].ownrPetUnqNo == ""){
                Box(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .height(80.dp)
                        .border(
                            1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(12.dp)
                        ), contentAlignment = Alignment.Center
                ){
                    Text(
                        text = stringResource(R.string.no_manage_pet),
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 14.sp, color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }else{
                LazyColumn(
                    state = rememberLazyListState(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.heightIn(max = 400.dp)
                ){
                    itemsIndexed(petInfo){ index, item ->
                        MyPagePetItem(petDetailData = item,sharedViewModel, navController, index)
                    }
                }
            }

            Button(
                onClick = { navController.navigate(Screen.AddPetScreen.route) },
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = design_button_bg)
            )
            {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(painter = painterResource(id = R.drawable.plus_white),
                        contentDescription = "", tint = Color.Unspecified,
                        modifier = Modifier.padding(end = 8.dp))

                    Text(text = stringResource(R.string.dog_cat_reg), color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
                }
            }

            Spacer(modifier = Modifier.padding(top = 20.dp))

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = design_EFECFE),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Column (
                    modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 16.dp)
                ){
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = stringResource(R.string.service_center),
                            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                            fontSize = 16.sp, letterSpacing = (-0.8).sp,
                            color = design_login_text
                        )

                        Box (
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .background(design_white, CircleShape)
                                .clip(CircleShape)
                                .size(20.dp)
                                .clickable { navController.navigate(Screen.SettingScreen.route) },
                            contentAlignment = Alignment.Center
                        ){
                            Icon(
                                painter = painterResource(id = R.drawable.arrow_diagonal),
                                contentDescription = "", tint = Color.Unspecified)
                        }
                    }
                    
                    Spacer(modifier = Modifier.padding(top = 8.dp))
                    
                    Text(text = stringResource(R.string.noti_faq_inqu_info),
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 14.sp, letterSpacing = (-0.7).sp,
                        lineHeight = 20.sp,
                        color = design_skip
                    )
                }
                
                Icon(
                    painter = painterResource(id = R.drawable.icon_service),
                    contentDescription = "", tint = Color.Unspecified,
                    modifier = Modifier.padding(end = 20.dp))
            }
            
            Spacer(modifier = Modifier.padding(bottom = 30.dp))
        }// col

        if (openBottomSheet){
            ModalBottomSheet(
                onDismissRequest = { openBottomSheet = false },
                sheetState = bottomSheetState,
                containerColor = Color.Transparent,
                dragHandle = {}
            ) {
                Column {
                    MyBottomSheet(
                        sharedViewModel = sharedViewModel,
                        settingViewModel = viewModel,
                        navController = navController,
                        openDatePicker = {newValue -> openDialog = newValue},
                        openBottomSheet = { newValue -> openBottomSheet = newValue },
                        dateState = datePickerState,
                        timeState = timePickerState,
                        snackState = snackState
                    )
                    Spacer(modifier = Modifier
                        .height(navigationBarHeight)
                        .fillMaxWidth()
                        .background(color = design_white))
                }
            }
        }

        if (openDialog) {
            val confirmEnabled = remember{derivedStateOf { datePickerState.selectedDateMillis != null }}
            DatePickerDialog(
                onDismissRequest = {
                    openDialog = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val sdfDate = SimpleDateFormat("yyyyMMdd")
                            val date =  sdfDate.format(Date(datePickerState.selectedDateMillis?:0))

                            viewModel.updateSelectedDate(date)
                            openDialog = false
                            openTimePicker = true
                        },
                        enabled = confirmEnabled.value
                    ) {
                        Text(text = stringResource(R.string.ok), color = design_intro_bg)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            openDialog = false
                        }
                    ) {
                        Text(text = stringResource(R.string.cancel), color = design_intro_bg)
                    }
                },
                colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.primary

                )
            ) {
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
            }
        }

        if (openTimePicker){
            TimePickerDialog(
                title = stringResource(id = R.string.select_time),
                onCancel = { newValue -> openTimePicker =newValue},
                state = timePickerState,
                onConfirm = {newValue -> viewModel.updateSelectedTime(newValue)}
            )
        }
    }
}

@Composable
fun MyPagePetItem(petDetailData: PetDetailData, sharedViewModel: SharedViewModel, navController: NavHostController, index:Int){
    Column (
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(shape = RoundedCornerShape(12.dp))
            .clickable {
                sharedViewModel.updateProfilePet(petDetailData)
                navController.navigate("petProfileScreen")
            }
    ){
        Spacer(modifier = Modifier.padding(top = 12.dp))
        
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 20.dp)
            ){
                CircleImageTopBar(size = 40, petDetailData = petDetailData)

                Column (
                    modifier = Modifier.padding(start = 10.dp)
                ){
                    Text(
                        text = petDetailData.petNm,
                        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                        fontSize = 16.sp, letterSpacing = (-0.8).sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = petDetailData.petKindNm,
                        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                        fontSize = 14.sp, letterSpacing = (-0.7).sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Box (
                modifier= Modifier
                    .padding(end = 12.dp)
                    .border(
                        when (petDetailData.mngrType) {
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
                        when (petDetailData.mngrType) {
                            "M" -> design_button_bg
                            "I" -> Color.Transparent
                            "C" -> design_DDDDDD
                            else -> design_DDDDDD
                        },
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text =
                    when(petDetailData.mngrType){
                        "M" -> stringResource(R.string.ing_manage)
                        "I" -> stringResource(R.string.ing_join)
                        "C" -> petDetailData.endDt?: ""
                        else -> stringResource(R.string.error)
                    },
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 12.sp,
                    letterSpacing = (-0.6).sp,
                    color =
                    when(petDetailData.mngrType){
                        "M" -> design_white
                        "I" -> design_button_bg
                        "C" -> design_999999
                        else -> design_DDDDDD
                    },
                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 12.dp)
            .fillMaxWidth()
            .height(1.dp)
            .background(MaterialTheme.colorScheme.outline)
        )

        Row (
            modifier = Modifier.padding(start = 20.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = if (petDetailData.petBrthYmd== stringResource(id = R.string.age_unknown)){
                    stringResource(id = R.string.age_unknown)
                }else{
                    sharedViewModel.changeBirth(petDetailData.petBrthYmd)
                },
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                fontSize = 14.sp, letterSpacing = (-0.7).sp,
                color = MaterialTheme.colorScheme.secondary
            )
            
            Spacer(modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(2.dp, 8.dp)
                .background(MaterialTheme.colorScheme.secondary)
            )

            Text(
                text = "${formatWghtVl(petDetailData.wghtVl)}kg",
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                fontSize = 14.sp, letterSpacing = (-0.7).sp,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(2.dp, 8.dp)
                .background(MaterialTheme.colorScheme.secondary)
            )

            Text(
                text = petDetailData.sexTypNm?:"",
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                fontSize = 14.sp, letterSpacing = (-0.7).sp,
                color = MaterialTheme.colorScheme.secondary,modifier = Modifier.alignByBaseline()
            )
            if (petDetailData.ntrTypCd=="001"){
                Text(
                    text = stringResource(R.string.neutering_comp),
                    fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                    fontSize = 12.sp, letterSpacing = (-0.6).sp,
                    color = MaterialTheme.colorScheme.secondary, modifier = Modifier.alignByBaseline()
                )
            }
        }
    }//col
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBottomSheet(
    sharedViewModel: SharedViewModel,
    settingViewModel: SettingViewModel,
    navController: NavHostController,
    openBottomSheet: (Boolean) -> Unit,
    openDatePicker: (Boolean) -> Unit,
    dateState: DatePickerState,
    timeState: TimePickerState,
    snackState : SnackbarHostState
){

    val petList by sharedViewModel.petInfo.collectAsState()
    val endCheck by settingViewModel.endCheck.collectAsState()
    val selectedPet = settingViewModel.selectedPet

    val managedPet:List<PetDetailData> = petList.filter { it.mngrType == "M" }

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    var alertMsg by remember{ mutableStateOf("") }
    var alertShow by remember{ mutableStateOf(false) }

    val sdfDate = SimpleDateFormat("yyyy.MM.dd")
    val formattedDate = sdfDate.format(Date(dateState.selectedDateMillis?:Date().time))

    val selectedTime by settingViewModel.selectedTime.collectAsState()
    
    LaunchedEffect(Unit){
        selectedPet.clear()
        settingViewModel.updateSelectedTime("")
    }

    if (alertShow){
        CustomAlertOneBtn(
            onDismiss = {alertShow = false},
            confirm = "확인",
            title = alertMsg
        )
    }

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Spacer(modifier = Modifier.padding(top = 20.dp))

        Text(
            text = stringResource(R.string.invite_cmt),
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            fontSize = 20.sp,
            letterSpacing = (-1.0).sp,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(start = 20.dp)
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        if (managedPet.isNotEmpty()){
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                contentPadding = PaddingValues(horizontal = 20.dp)
            ){
                items(managedPet){ petList ->
                    MyBottomSheetItem(viewModel = sharedViewModel, settingViewModel= settingViewModel, petList = petList)
                }
            }
        }

        Row (modifier = Modifier
            .padding(end = 20.dp)
            .wrapContentWidth()
            .align(Alignment.End),
            verticalAlignment = Alignment.CenterVertically){
            Checkbox(
                checked = endCheck,
                onCheckedChange = {settingViewModel.updateEndCheck(it)},
                colors = CheckboxDefaults.colors(
                    checkedColor = design_select_btn_text,
                    uncheckedColor = design_textFieldOutLine,
                    checkmarkColor = design_white)
            )

            Text(text = stringResource(R.string.end_cmt), fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                color = MaterialTheme.colorScheme.onPrimary, modifier=Modifier.offset(x = (-8).dp), letterSpacing = (-0.7).sp
            )
        }

        AnimatedVisibility(
            visible = endCheck,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column {
                Row (
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.onPrimaryContainer)
                        .clickable { openDatePicker(true) },
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(painter = painterResource(id = R.drawable.input_calendar),
                        contentDescription = "", tint = Color.Unspecified,
                        modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 16.dp))

                    Text(
                        text = stringResource(R.string.end_date),
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 14.sp, letterSpacing = (-0.7).sp,
                        color = MaterialTheme.colorScheme.secondary, modifier = Modifier.padding(start = 4.dp)
                    )

                    Text(
                        text =
                        if (selectedTime == ""){
                            "날짜를 선택해주세요"
                        }else{
                            "$formattedDate ${if(timeState.hour<10) "0"+timeState.hour else timeState.hour}:${if(timeState.minute<10) "0"+timeState.minute else timeState.minute}"
                        },
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 14.sp, letterSpacing = (-0.7).sp,
                        color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.padding(start = 12.dp)
                    )


                }
                Spacer(modifier = Modifier.padding(top = 16.dp))
            }
        }



        Button(
            onClick = {
                if (selectedPet.isNotEmpty()){
                    if (!endCheck){
                        if (settingViewModel.updateSelectedPetSave(selectedPet)) {
                            //scope.launch {
                            //    if (settingViewModel.getInviteCode()){
                            //        openBottomSheet(false)
                            //        navController.navigate(Screen.InviteScreen.route)
                            //    } }
                            openBottomSheet(false)
                            navController.navigate(Screen.InviteScreen.route)
                        }
                    }else if(dateState.selectedDateMillis != null && selectedTime !=""){
                        val calendar = Calendar.getInstance()
                        calendar.set(Calendar.HOUR_OF_DAY, timeState.hour)
                        calendar.set(Calendar.MINUTE, timeState.minute)

                        val sdfDate = SimpleDateFormat("yyyyMMdd")
                        val sdfCurrentDate = SimpleDateFormat("yyyyMMddHHmm")

                        val selectedDate = sdfDate.format(dateState.selectedDateMillis)

                        val currentDate = sdfCurrentDate.format(Date().time)

                        if(currentDate > selectedDate+selectedTime){
                            alertMsg = "이미 만료된 시간입니다"
                            alertShow =true
                        }else{
                            if (settingViewModel.updateSelectedPetSave(selectedPet)) {
                                openBottomSheet(false)
                                navController.navigate(Screen.InviteScreen.route)
                            }
                        }
                    }else{
                        alertMsg = "날짜를 선택해주세요"
                        alertShow =true
                    }
                }else{
                    alertMsg = "펫을 선택해주세요"
                    alertShow =true
                }
                      },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = design_button_bg)
        ){
            Text(text = stringResource(id = R.string.invite), color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
        }

        Spacer(modifier = Modifier.padding(top = 20.dp))
    }
}

@Composable
fun MyBottomSheetItem(viewModel: SharedViewModel, settingViewModel: SettingViewModel, petList : PetDetailData){

    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp.dp -60.dp

    val petName:String = petList.petNm
    val imageUri:String = petList.petRprsImgAddr?: ""

    var isSelected by rememberSaveable { mutableStateOf(false) }

    Button(
        onClick = {
            isSelected= !isSelected
            if (isSelected) {
                settingViewModel.selectedPet.add(petList)
            }else{
                settingViewModel.selectedPet.remove(petList)
            }
                  },
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .size(width = screenWidth / 3, height = screenWidth / 3 - 9.dp)
            .shadow(ambientColor = MaterialTheme.colorScheme.onSurface, elevation = 0.dp)
        ,
        shape = RoundedCornerShape(12.dp),
        colors = if(isSelected) {
            ButtonDefaults.buttonColors(design_select_btn_bg)
        } else {
            ButtonDefaults.buttonColors(Color.Transparent)
        },
        border = if(isSelected) {
            BorderStroke(1.dp, color = design_select_btn_text)
        } else {
            BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline)
        },
        contentPadding = PaddingValues(start = 14.dp,end=14.dp),
        elevation = if(isSelected){
            ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
        } else {
            ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
        }

    ) {
        Column (
            modifier= Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .border(shape = CircleShape, border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.tertiary))
                    .shadow(
                        color = MaterialTheme.colorScheme.onSurface,
                        offsetY = 10.dp,
                        offsetX = 10.dp,
                        spread = 4.dp,
                        blurRadius = 3.dp,
                        borderRadius = 90.dp
                    )
                    .clip(CircleShape)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = "",
                    placeholder = painterResource(id = R.drawable.profile_default),
                    error= painterResource(id = R.drawable.profile_default),
                    modifier= Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

            }

            Spacer(modifier = Modifier.padding(top = 8.dp))

            Text(
                text = petName,
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                fontSize = 16.sp,
                letterSpacing = (-0.8).sp,
                color = if (isSelected) design_login_text else MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    title: String,
    onCancel: (Boolean) -> Unit,
    onConfirm: (String) -> Unit,
    toggle: @Composable () -> Unit = {},
    state: TimePickerState
) {
    Dialog(
        onDismissRequest = {onCancel(false)},
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.primary
                ),
            color = MaterialTheme.colorScheme.primary
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    fontSize = 16.sp, letterSpacing = (-0.8).sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                TimePicker(
                    state = state,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        clockDialSelectedContentColor = design_white,
                        clockDialUnselectedContentColor = MaterialTheme.colorScheme.onPrimary,
                        selectorColor = design_intro_bg,
                        containerColor = Color.Unspecified,
                        timeSelectorSelectedContainerColor = design_intro_bg,
                        timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        timeSelectorSelectedContentColor = design_white,
                        timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.onPrimary,
                        periodSelectorBorderColor = MaterialTheme.colorScheme.outline,
                        periodSelectorSelectedContainerColor = design_intro_bg,
                        periodSelectorSelectedContentColor = design_white,
                        periodSelectorUnselectedContainerColor = design_white,
                        periodSelectorUnselectedContentColor = design_login_text

                    )
                )
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    toggle()
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = {onCancel(false)}
                    ) {
                        Text(
                            text = stringResource(id = R.string.cancel),
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = 16.sp,color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    TextButton(
                        onClick = {
                            val calendar = Calendar.getInstance()
                            calendar.set(Calendar.HOUR_OF_DAY, state.hour)
                            calendar.set(Calendar.MINUTE, state.minute)

                            val sdfTime = SimpleDateFormat("HHmm")
                            val time = sdfTime.format(calendar.time)

                            onConfirm(time)
                            onCancel(false)
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.ok),
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = 16.sp,color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogoutDialog(
    dialogState: Boolean,
    onDismiss: (Boolean) -> Unit,
    onLogoutState: (Boolean) -> Unit,
) {
    if (dialogState) {
        AlertDialog(
            onDismissRequest = { onDismiss(false) },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            Box(modifier = Modifier
                .padding(horizontal = 60.dp)
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(20.dp)
                )
            ){
                Column (
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(
                        text = stringResource(R.string.logout_walking_title),
                        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                        fontSize = 18.sp, letterSpacing = (-0.8).sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(start = 20.dp, top = 30.dp)
                    )

                    Text(
                        text = stringResource(R.string.logout_walking_memo),
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 14.sp, letterSpacing = (-0.8).sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(start = 20.dp, top = 30.dp)
                    )

                    Row (
                        modifier = Modifier
                            .padding(top = 30.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                    ){
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(MaterialTheme.colorScheme.onSecondary)
                                .clickable {
                                    onLogoutState(true)
                                },
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = stringResource(id = R.string.logout),
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
                                    onDismiss(false)
                                }
                            ,
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = stringResource(R.string.keep_walk),
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

@OptIn(ExperimentalMaterial3Api::class)
class MySelectableDates2 : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val now = System.currentTimeMillis()
        val yesterday = now - 24 * 60 * 60 * 1000
        return utcTimeMillis > yesterday
    }
}