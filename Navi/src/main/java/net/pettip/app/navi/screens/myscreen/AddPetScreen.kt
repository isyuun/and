package net.pettip.app.navi.screens.myscreen

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.Screen
import net.pettip.app.navi.component.CustomAlertOneBtn
import net.pettip.app.navi.component.CustomTextField
import net.pettip.app.navi.component.LoadingDialog
import net.pettip.app.navi.ui.theme.design_button_bg
import net.pettip.app.navi.ui.theme.design_camera_bg
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_select_btn_bg
import net.pettip.app.navi.ui.theme.design_select_btn_text
import net.pettip.app.navi.ui.theme.design_textFieldOutLine
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.SharedViewModel
import net.pettip.app.navi.viewmodel.UserCreateViewModel
import net.pettip.data.SCD
import net.pettip.data.SggList
import net.pettip.data.UmdList
import net.pettip.data.cmm.CdDetail
import net.pettip.data.pet.PetListData
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: UserCreateViewModel,
    sharedViewModel: SharedViewModel
){

    val scdList by remember { mutableStateOf(viewModel.scdList) }

    val datePickerState = rememberDatePickerState(selectableDates = MySelectableDates())
    var openDialog by remember{ mutableStateOf(false) }

    val alertMsg by viewModel.integrityCheckMsg.collectAsState()
    var alertShow by remember{ mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var isLoading by remember{ mutableStateOf(false) }
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    val focusManager = LocalFocusManager.current

    val petDorC by viewModel.petDorC.collectAsState()
    val petKind by viewModel.petKind.collectAsState()
    val petName by viewModel.petName.collectAsState()
    val petBirth by viewModel.petBirth.collectAsState()
    val petBirthUK by viewModel.petBirthUnknown.collectAsState()
    val petWght by viewModel.petWght.collectAsState()
    val petGender by viewModel.petGender.collectAsState()
    val petNtr by viewModel.petNtr.collectAsState()
    val scd by viewModel.selectedItem1.collectAsState()
    val sgg by viewModel.selectedItem2.collectAsState()
    val umd by viewModel.selectedItem3.collectAsState()

    BackHandler {
        scope.launch {
            navController.popBackStack()

            viewModel.updatePetDorC(context.getString(R.string.dog))
            viewModel.updatePetName("")
            viewModel.updatePetKind(
                PetListData(
                    petDogSzCd = "",
                    petNm = "사이즈/품종 선택",
                    petEnNm = "",
                    petInfoUnqNo = 0,
                    petTypCd = ""
                )
            )
            viewModel.updatePetWght("")
            viewModel.updatePetGender("남아")
            viewModel.updatePetNtr("했어요")
            viewModel.updateSelectedItem1(CdDetail(cdNm = "", cdId = "", upCdId = ""))
            viewModel.updateSelectedItem2(SggList(sggCd = "", sggNm = ""))
            viewModel.updateSelectedItem3(UmdList(umdCd = "", umdNm = ""))
            viewModel.updatePetBirth("")
            viewModel.updateYear(net.pettip.app.navi.viewmodel.PickerState())
            viewModel.setImageUri(null,context)
        }
    }

    Scaffold (
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.height(60.dp),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(shape = CircleShape)
                                .clickable {
                                    scope.launch {
                                        navController.popBackStack()

                                        viewModel.updatePetDorC(context.getString(R.string.dog))
                                        viewModel.updatePetName("")
                                        viewModel.updatePetKind(
                                            PetListData(
                                                petDogSzCd = "",
                                                petNm = "사이즈/품종 선택",
                                                petEnNm = "",
                                                petInfoUnqNo = 0,
                                                petTypCd = ""
                                            )
                                        )
                                        viewModel.updatePetWght("")
                                        viewModel.updatePetGender("남아")
                                        viewModel.updatePetNtr("했어요")
                                        viewModel.updateSelectedItem1(CdDetail(cdNm = "", cdId = "", upCdId = ""))
                                        viewModel.updateSelectedItem2(SggList(sggCd = "", sggNm = ""))
                                        viewModel.updateSelectedItem3(UmdList(umdCd = "", umdNm = ""))
                                        viewModel.updatePetBirth("")
                                        viewModel.updateYear(net.pettip.app.navi.viewmodel.PickerState())
                                        viewModel.setImageUri(null, context)
                                    }
                                }
                                .align(Alignment.CenterStart),
                            contentAlignment = Alignment.Center
                        ){
                            Icon(painter = painterResource(id = R.drawable.arrow_back),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        Text(
                            text = stringResource(R.string.pet_register),
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                            letterSpacing = (-1.0).sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.align(Alignment.Center),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            )
        }
    ){ paddingValues ->

        LoadingDialog(
            loadingText = stringResource(R.string.pet_register_ing),
            loadingState = isLoading
        )

        if (alertShow){
            CustomAlertOneBtn(
                onDismiss = {alertShow = false},
                confirm = "확인",
                title = alertMsg
            )
        }

        if (openDialog) {
            val confirmEnabled = remember{ derivedStateOf { datePickerState.selectedDateMillis != null } }
            DatePickerDialog(
                onDismissRequest = {
                    focusManager.clearFocus()
                    openDialog = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val sdfDate = SimpleDateFormat("yyyyMMdd")
                            val date =  sdfDate.format(Date(datePickerState.selectedDateMillis?:0))

                            viewModel.updatePetBirth(date)
                            focusManager.moveFocus(FocusDirection.Next)
                            openDialog = false
                        },
                        enabled = confirmEnabled.value
                    ) {
                        Text(text = stringResource(R.string.ok), color = design_intro_bg)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            focusManager.clearFocus()
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

        Column (modifier= Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.primary)
        ){

            Box (Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                CircleImageCreate(viewModel)
                Box (modifier= Modifier
                    .offset(50.dp, 50.dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(design_camera_bg), contentAlignment = Alignment.Center){
                    Icon(painter = painterResource(id = R.drawable.icon_camera), contentDescription = "", tint = design_white)
                }
            }

            Text(text = stringResource(R.string.pet), fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier= Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
            )

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 8.dp)){

                Button(
                    enabled = petDorC != stringResource(R.string.dog),
                    onClick = {
                        viewModel.updatePetDorC(context.getString(R.string.dog))
                        viewModel.updatePetKind(
                            PetListData(
                                petDogSzCd = "",
                                petNm = "사이즈/품종 선택",
                                petEnNm = "",
                                petInfoUnqNo = 0,
                                petTypCd = ""
                            )
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .shadow(ambientColor = MaterialTheme.colorScheme.onSurface, elevation = 0.dp)
                    ,
                    shape = RoundedCornerShape(12.dp),
                    colors = if(stringResource(R.string.dog) == petDorC) {
                        ButtonDefaults.buttonColors(containerColor = design_select_btn_bg, disabledContainerColor = design_select_btn_bg)
                    } else {
                        ButtonDefaults.buttonColors(Color.Transparent)
                    },
                    border = if(stringResource(R.string.dog) == petDorC) {
                        BorderStroke(1.dp, color = design_select_btn_text)
                    } else {
                        BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline)
                    },
                    contentPadding = PaddingValues(start = 14.dp,end=14.dp),
                    elevation = if(stringResource(R.string.dog) == petDorC){
                        ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                    } else {
                        ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    }

                ) {
                    Text(
                        text = stringResource(R.string.dog),
                        color = if(stringResource(R.string.dog) == petDorC) design_select_btn_text else MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp
                    )
                }

                Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                Button(
                    enabled = petDorC != stringResource(R.string.cat),
                    onClick = {
                        viewModel.updatePetDorC(context.getString(R.string.cat))
                        viewModel.updatePetKind(
                            PetListData(
                                petDogSzCd = "",
                                petNm = "사이즈/품종 선택",
                                petEnNm = "",
                                petInfoUnqNo = 0,
                                petTypCd = ""
                            )
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .shadow(ambientColor = MaterialTheme.colorScheme.onSurface, elevation = 0.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = if(stringResource(R.string.cat) == petDorC) {
                        ButtonDefaults.buttonColors(containerColor = design_select_btn_bg, disabledContainerColor = design_select_btn_bg)
                    } else {
                        ButtonDefaults.buttonColors(Color.Transparent)
                    },
                    border = if(stringResource(R.string.cat) == petDorC) {
                        BorderStroke(1.dp, color = design_select_btn_text)
                    } else {
                        BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline)
                    },
                    contentPadding = PaddingValues(start = 14.dp,end=14.dp),
                    elevation = if(stringResource(R.string.cat) == petDorC){
                        ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                    } else {
                        ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    }
                ) {
                    Text(
                        text = stringResource(R.string.cat),
                        color = if(stringResource(R.string.cat) == petDorC) design_select_btn_text else MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp
                    )
                }

            }

            // 사이즈 품종 선택
            Text(text = stringResource(R.string.size_breed_selection), fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
            )

            Button(
                onClick = {
                    navController.navigate(Screen.PetKindContent.route)
                    viewModel.apply {
                        onSearchTextChange("")
                    } },
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 8.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.outlineVariant)
            ) {
                Row(modifier=Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                    Text(text = petKind.petNm, color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "", tint = MaterialTheme.colorScheme.onPrimary)
                }
            }


            // 주소 선택
            Text(text = stringResource(R.string.activity_region), fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
            )


            Button(
                onClick = {
                    navController.navigate(Screen.LocationPickContent.route)
                },
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 8.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.outlineVariant)
            ) {
                Row(modifier=Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                    Text(
                        text = if(scd.cdId == "") stringResource(R.string.address_selection) else "${scd.cdNm} ${sgg.sggNm} ${umd.umdNm}",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "", tint = MaterialTheme.colorScheme.onPrimary)
                }
            }

            Text(text = stringResource(R.string.pet_name), fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
            )

            CustomTextField(
                value = petName,
                onValueChange = {
                    if (it.length <=10){
                        viewModel.updatePetName(it)
                    }
                                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next),
                modifier = Modifier
                    .padding(start = 20.dp, top = 8.dp, end = 20.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                placeholder = { Text(text = stringResource(R.string.place_holder_pet_name), fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
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
                innerPadding = PaddingValues(start=16.dp)
            )

            Text(text = stringResource(R.string.birthday), fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
            )

            Row (modifier= Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 8.dp), verticalAlignment = Alignment.CenterVertically){
                CustomTextField(
                    enabled = !petBirthUK,
                    readOnly = true,
                    value = if (petBirth.matches(Regex("\\d{8}"))) {
                        "${petBirth.substring(0, 4)}-${petBirth.substring(4, 6)}-${petBirth.substring(6, 8)}"
                    } else {
                        petBirth
                    },
                    onValueChange = {},
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next),
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .weight(1f)
                        .height(48.dp)
                        .onFocusChanged { focusState ->
                            openDialog = focusState.isFocused
                        },
                    placeholder = { Text(text = stringResource(R.string.place_holder_birthday), fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                        focusedContainerColor = MaterialTheme.colorScheme.primary,
                        unfocusedLeadingIconColor = MaterialTheme.colorScheme.primaryContainer,
                        focusedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
                        cursorColor = design_intro_bg.copy(alpha = 0.5f),
                        disabledBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 16.sp, letterSpacing = (-0.4).sp
                    ),
                    shape = RoundedCornerShape(4.dp),
                    innerPadding = PaddingValues(start=16.dp)
                )
                Row (modifier = Modifier
                    .clickable { viewModel.updatePetBirthUnknown(!petBirthUK) }
                    .wrapContentWidth(),
                    verticalAlignment = Alignment.CenterVertically){
                    Checkbox(
                        checked = petBirthUK,
                        onCheckedChange = {viewModel.updatePetBirthUnknown(it)},
                        colors = CheckboxDefaults.colors(
                            checkedColor = design_select_btn_text,
                            uncheckedColor = design_textFieldOutLine,
                            checkmarkColor = design_white)
                    )

                    Text(text = stringResource(id = R.string.age_unknown_), fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        color = MaterialTheme.colorScheme.onPrimary, modifier=Modifier.offset(x = (-8).dp), letterSpacing = (-0.7).sp
                    )
                }
            }

            Text(text = stringResource(R.string.weight), fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
            )

            Row (modifier= Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 8.dp), verticalAlignment = Alignment.CenterVertically){
                CustomTextField(
                    value = petWght,
                    onValueChange = {viewModel.updatePetWght(it)},
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    placeholder = { Text(text = stringResource(R.string.place_holder_weight), fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp) },
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
                    innerPadding = PaddingValues(start=16.dp)
                )

                Row (modifier = Modifier.width(100.dp),verticalAlignment = Alignment.CenterVertically){
                    Text(text = "kg", fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        color = MaterialTheme.colorScheme.onPrimary, modifier=Modifier.padding(start = 14.dp), letterSpacing = (-0.7).sp
                    )
                }


            }

            Text(text = stringResource(R.string.weight_guidance_message),
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary,
                modifier= Modifier
                    .padding(start = 20.dp, top = 8.dp),
                letterSpacing = (-0.6).sp
            )

            Text(text = stringResource(R.string.gender), fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
            )

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 8.dp)){
                Button(
                    onClick = { viewModel.updatePetGender(context.getString(R.string.male)) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .shadow(ambientColor = MaterialTheme.colorScheme.onSurface, elevation = 0.dp)
                    ,
                    shape = RoundedCornerShape(12.dp),
                    colors = if(stringResource(R.string.male) == petGender) {
                        ButtonDefaults.buttonColors(design_select_btn_bg)
                    } else {
                        ButtonDefaults.buttonColors(Color.Transparent)
                    },
                    border = if(stringResource(R.string.male) == petGender) {
                        BorderStroke(1.dp, color = design_select_btn_text)
                    } else {
                        BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline)
                    },
                    contentPadding = PaddingValues(start = 14.dp,end=14.dp),
                    elevation = if(stringResource(R.string.male) == petGender){
                        ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                    } else {
                        ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    }

                ) {
                    Text(
                        text = stringResource(R.string.male),
                        color = if(stringResource(R.string.male) == petGender) design_select_btn_text else MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp
                    )
                }

                Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                Button(
                    onClick = { viewModel.updatePetGender(context.getString(R.string.female)) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .shadow(ambientColor = MaterialTheme.colorScheme.onSurface, elevation = 0.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = if(stringResource(R.string.female) == petGender) {
                        ButtonDefaults.buttonColors(design_select_btn_bg)
                    } else {
                        ButtonDefaults.buttonColors(Color.Transparent)
                    },
                    border = if(stringResource(R.string.female) == petGender) {
                        BorderStroke(1.dp, color = design_select_btn_text)
                    } else {
                        BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline)
                    },
                    contentPadding = PaddingValues(start = 14.dp,end=14.dp),
                    elevation = if(stringResource(R.string.female) == petGender){
                        ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                    } else {
                        ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    }
                ) {
                    Text(
                        text = stringResource(R.string.female),
                        color = if(stringResource(R.string.female) == petGender) design_select_btn_text else MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp
                    )
                }

                Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                Button(
                    onClick = { viewModel.updatePetGender(context.getString(R.string.type_uk)) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .shadow(ambientColor = MaterialTheme.colorScheme.onSurface, elevation = 0.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = if(stringResource(id = R.string.type_uk) == petGender) {
                        ButtonDefaults.buttonColors(design_select_btn_bg)
                    } else {
                        ButtonDefaults.buttonColors(Color.Transparent)
                    },
                    border = if(stringResource(id = R.string.type_uk) == petGender) {
                        BorderStroke(1.dp, color = design_select_btn_text)
                    } else {
                        BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline)
                    },
                    contentPadding = PaddingValues(start = 14.dp,end=14.dp),
                    elevation = if(stringResource(id = R.string.type_uk) == petGender){
                        ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                    } else {
                        ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.type_uk),
                        color = if(stringResource(id = R.string.type_uk) == petGender) design_select_btn_text else MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp
                    )
                }


            }

            Text(text = stringResource(R.string.neutering), fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier= Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
            )

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 8.dp)) {
                Button(
                    onClick = { viewModel.updatePetNtr(context.getString(R.string.did)) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .shadow(ambientColor = MaterialTheme.colorScheme.onSurface, elevation = 0.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = if (stringResource(R.string.did) == petNtr) {
                        ButtonDefaults.buttonColors(design_select_btn_bg)
                    } else {
                        ButtonDefaults.buttonColors(Color.Transparent)
                    },
                    border = if (stringResource(R.string.did) == petNtr) {
                        BorderStroke(1.dp, color = design_select_btn_text)
                    } else {
                        BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline)
                    },
                    contentPadding = PaddingValues(start = 14.dp, end = 14.dp),
                    elevation = if (stringResource(R.string.did) == petNtr) {
                        ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                    } else {
                        ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    }

                ) {
                    Text(
                        text = stringResource(R.string.did),
                        color = if (stringResource(R.string.did) == petNtr) design_select_btn_text else MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp
                    )
                }

                Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                Button(
                    onClick = { viewModel.updatePetNtr(context.getString(R.string.didnot)) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .shadow(ambientColor = MaterialTheme.colorScheme.onSurface, elevation = 0.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = if (stringResource(R.string.didnot) == petNtr) {
                        ButtonDefaults.buttonColors(design_select_btn_bg)
                    } else {
                        ButtonDefaults.buttonColors(Color.Transparent)
                    },
                    border = if (stringResource(R.string.didnot) == petNtr) {
                        BorderStroke(1.dp, color = design_select_btn_text)
                    } else {
                        BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline)
                    },
                    contentPadding = PaddingValues(start = 14.dp, end = 14.dp),
                    elevation = if (stringResource(R.string.didnot) == petNtr) {
                        ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                    } else {
                        ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    }
                ) {
                    Text(
                        text = stringResource(R.string.didnot),
                        color = if (stringResource(R.string.didnot) == petNtr) design_select_btn_text else MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp
                    )
                }

                Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                Button(
                    onClick = { viewModel.updatePetNtr(context.getString(R.string.type_uk)) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .shadow(ambientColor = MaterialTheme.colorScheme.onSurface, elevation = 0.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = if (stringResource(id = R.string.type_uk) == petNtr) {
                        ButtonDefaults.buttonColors(design_select_btn_bg)
                    } else {
                        ButtonDefaults.buttonColors(Color.Transparent)
                    },
                    border = if (stringResource(id = R.string.type_uk) == petNtr) {
                        BorderStroke(1.dp, color = design_select_btn_text)
                    } else {
                        BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline)
                    },
                    contentPadding = PaddingValues(start = 14.dp, end = 14.dp),
                    elevation = if (stringResource(id = R.string.type_uk) == petNtr) {
                        ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                    } else {
                        ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.type_uk),
                        color = if (stringResource(id = R.string.type_uk) == petNtr) design_select_btn_text else MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp
                    )
                }
            }

            Button(
                onClick = {
                    scope.launch {
                        if (integrityCheck(viewModel, context)){
                            isLoading = true
                            val result = viewModel.createPet(context)

                            if(result){
                                isLoading = false
                                sharedViewModel.loadCurrentPetInfo()
                                sharedViewModel.loadPetInfo()
                                sharedViewModel.initSelectPet()
                                navController.popBackStack()
                            }else{
                                isLoading = false
                                viewModel.updateIntegrityCheckMsg("등록에 실패했습니다")
                                alertShow = true
                            }
                        }else{
                            alertShow = true
                        }
                    }
                },
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 40.dp)
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = design_button_bg)
            )
            {
                Text(text = stringResource(R.string.registration), color = design_white, fontSize = 14.sp, fontFamily = FontFamily(
                    Font(R.font.pretendard_regular)
                )
                )
            }
        }// Column
    }

}
@Composable
fun CircleImageCreate(viewModel: UserCreateViewModel){

    val imageUri by viewModel.imageUri.collectAsState()

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){ uri ->
        if (uri != null){
            viewModel.setImageUri(uri,context)
        }
    }


    Box(
        modifier = Modifier
            .size(148.dp)
            .border(shape = CircleShape, border = BorderStroke(5.dp, color = MaterialTheme.colorScheme.tertiary))
            .shadow(elevation = 10.dp, shape = CircleShape, spotColor = MaterialTheme.colorScheme.onSurface)
            .clip(CircleShape)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUri)
                .crossfade(true)
                .build(),
            contentDescription = "",
            placeholder = painterResource(id = R.drawable.profile_default),
            error = painterResource(id = R.drawable.profile_default),
            modifier= Modifier
                .fillMaxSize()
                .clickable { launcher.launch("image/*") },
            contentScale = ContentScale.Crop
        )

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Picker(
    items: List<String>,
    state: net.pettip.app.navi.viewmodel.PickerState,
    modifier: Modifier = Modifier,
    startIndex: Int = 0,
    visibleItemsCount: Int = 3,
    textModifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    dividerColor: Color = LocalContentColor.current,
) {

    val visibleItemsMiddle = visibleItemsCount / 2
    val listScrollCount = Integer.MAX_VALUE
    val listScrollMiddle = listScrollCount / 2
    val listStartIndex = listScrollMiddle - listScrollMiddle % items.size - visibleItemsMiddle + startIndex

    fun getItem(index: Int) = items[index % items.size]

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = listStartIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val itemHeightPixels = remember { mutableStateOf(0) }
    val itemHeightDp = pixelsToDp(itemHeightPixels.value)

    val fadingEdgeGradient = remember {
        Brush.verticalGradient(
            0f to Color.Transparent,
            0.5f to design_white,
            1f to Color.Transparent
        )
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index -> getItem(index + visibleItemsMiddle) }
            .distinctUntilChanged()
            .collect { item -> state.selectedItem = item }
    }

    Box(modifier = modifier) {

        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeightDp * visibleItemsCount)
                .fadingEdge(fadingEdgeGradient)
        ) {
            items(listScrollCount) { index ->
                Text(
                    text = getItem(index),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = textStyle,
                    modifier = Modifier
                        .onSizeChanged { size -> itemHeightPixels.value = size.height }
                        .then(textModifier)
                )
            }
        }

        Divider(
            color = dividerColor,
            modifier = Modifier.offset(y = itemHeightDp * visibleItemsMiddle)
        )

        Divider(
            color = dividerColor,
            modifier = Modifier.offset(y = itemHeightDp * (visibleItemsMiddle + 1))
        )

    }

}

private fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }

@Composable
private fun pixelsToDp(pixels: Int) = with(LocalDensity.current) { pixels.toDp() }

@Composable
fun rememberPickerState() = remember { PickerState() }

class PickerState {
    var selectedItem by mutableStateOf("")
}

fun integrityCheck(viewModel: UserCreateViewModel, context: Context): Boolean {
    if (viewModel.petKind.value.petNm == "사이즈/품종 선택") {
        viewModel.updateIntegrityCheckMsg("펫 종류를 선택해 주세요")
        return false
    } else if (!viewModel.addressPass.value) {
        viewModel.updateIntegrityCheckMsg("주소를 입력해 주세요")
        return false
    } else if (viewModel.petName.value == "") {
        viewModel.updateIntegrityCheckMsg("이름을 입력해 주세요")
        return false
    } else if (!viewModel.petBirthUnknown.value && viewModel.petBirth.value.length < 8) {
        viewModel.updateIntegrityCheckMsg("생일을 선택해 주세요")
        return false
    } else if (!viewModel.petBirthUnknown.value &&
        (!isDateInPastOrToday(viewModel.petBirth.value) || !isDateValid(viewModel.petBirth.value))
    ) {
        viewModel.updateIntegrityCheckMsg("올바른 날짜가 아닙니다")
        return false
    } else if (viewModel.petWght.value == "") {
        viewModel.updateIntegrityCheckMsg("몸무게를 입력해 주세요")
        return false
    } else if (!isValidFloat(viewModel.petWght.value)) {
        viewModel.updateIntegrityCheckMsg("올바른 몸무게를 입력해 주세요")
        return false
    } else {
        return true
    }
}

fun isDateInPastOrToday(dateString: String): Boolean {
    return try {
        val dateFormat = SimpleDateFormat("yyyyMMdd")
        val date = dateFormat.parse(dateString)

        val today = Calendar.getInstance().time
        !date.after(today)
    } catch (e: Exception) {
        // 날짜 파싱 오류 처리
        false
    }
}

fun isDateValid(dateString: String): Boolean {
    if (dateString.length != 8) {
        return false
    }

    val dateFormat = SimpleDateFormat("yyyyMMdd")
    dateFormat.isLenient = false

    try {
        dateFormat.parse(dateString)
        return true
    } catch (e: Exception) {
        return false
    }
}