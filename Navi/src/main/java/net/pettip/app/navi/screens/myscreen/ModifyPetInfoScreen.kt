package net.pettip.app.navi.screens.myscreen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.TopAppBar
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.Screen
import net.pettip.app.navi.component.CustomAlert
import net.pettip.app.navi.component.CustomAlertOneBtn
import net.pettip.app.navi.component.CustomTextField
import net.pettip.app.navi.component.LoadingDialog
import net.pettip.app.navi.ui.theme.design_button_bg
import net.pettip.app.navi.ui.theme.design_camera_bg
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_select_btn_bg
import net.pettip.app.navi.ui.theme.design_select_btn_text
import net.pettip.app.navi.ui.theme.design_shadow
import net.pettip.app.navi.ui.theme.design_sharp
import net.pettip.app.navi.ui.theme.design_textFieldOutLine
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.PickerState
import net.pettip.app.navi.viewmodel.SettingViewModel
import net.pettip.app.navi.viewmodel.SharedViewModel
import net.pettip.app.navi.viewmodel.UserCreateViewModel
import net.pettip.data.SCD
import net.pettip.data.SggList
import net.pettip.data.UmdList
import net.pettip.data.pet.PetListData
import net.pettip.util.Log
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyPetInfoScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: UserCreateViewModel,
    sharedViewModel: SharedViewModel,
    settingViewModel: SettingViewModel
){
    val selectPet by sharedViewModel.profilePet.collectAsState()
    val profilePet by settingViewModel.profileData.collectAsState()

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val calendar = if (selectPet.petBrthYmd == "미상") {
        Calendar.getInstance()
    } else {
        val year = selectPet.petBrthYmd.substring(0, 4).toIntOrNull() ?: 0
        val month = selectPet.petBrthYmd.substring(4, 6).toIntOrNull() ?: 1
        val day = selectPet.petBrthYmd.substring(6, 8).toIntOrNull() ?: 1

        Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1) // Calendar.MONTH는 0부터 시작하므로 1을 빼줍니다.
            set(Calendar.DAY_OF_MONTH, day)
        }
    }

    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH) + 1
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

    Log.d("LOG","year:${currentYear},month:${currentMonth},day:${currentDay}")

    val datePickerState = rememberDatePickerState(selectableDates = MySelectableDates())
    var openDialog by remember{ mutableStateOf(false) }

    val alertMsg by viewModel.integrityCheckMsg.collectAsState()
    var alertShow by remember{ mutableStateOf(false) }

    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    var expanded by remember { mutableStateOf (false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val petName by viewModel.petName.collectAsState()
    val petDorC by viewModel.petDorC.collectAsState()
    val petKind by viewModel.petKind.collectAsState()
    val petBirth by viewModel.petBirth.collectAsState()
    val petBirthUK by viewModel.petBirthUnknown.collectAsState()
    val petGender by viewModel.petGender.collectAsState()
    val petNtr by viewModel.petNtr.collectAsState()
    val scd by viewModel.selectedItem1.collectAsState()
    val sgg by viewModel.selectedItem2.collectAsState()
    val umd by viewModel.selectedItem3.collectAsState()

    var showDialog by remember{ mutableStateOf(false) }
    var init by rememberSaveable { mutableStateOf(true) }
    var delete by remember{ mutableStateOf(false) }
    var isLoading by remember{ mutableStateOf(false) }

    LaunchedEffect(Unit){
        if (init){
            viewModel.updatePetName(selectPet.petNm)
            viewModel.updatePetDorC(
                if (profilePet?.petTypCd=="002"){
                    context.getString(R.string.cat)
                }else{
                    context.getString(R.string.dog)
                }
            )
            viewModel.updatePetKind(
                PetListData(
                    petDogSzCd = "",
                    petNm = selectPet.petKindNm,
                    petEnNm = "",
                    petInfoUnqNo = selectPet.petInfoUnqNo,
                    petTypCd = ""
                )
            )
            viewModel.updatePetWght(selectPet.wghtVl.toString())
            viewModel.updatePetGender(selectPet.sexTypNm?:"남아")
            viewModel.updatePetNtr(selectPet.ntrTypNm?:"했어요")
            viewModel.updateSelectedItem1(SCD(cdNm = selectPet.stdgCtpvNm , cdld = selectPet.stdgCtpvCd, upCdId = ""))
            viewModel.updateSelectedItem2(SggList(sggCd = selectPet.stdgSggCd, sggNm = selectPet.stdgSggNm))
            viewModel.updateSelectedItem3(UmdList(umdCd = selectPet.stdgUmdCd, umdNm = selectPet.stdgUmdNm?:""))
            viewModel.updatePetBirth(selectPet.petBrthYmd)
            viewModel.updateAddressPass(true)
            viewModel.updatePetBirthUnknown(selectPet.petBrthYmd=="미상")
            viewModel.setImageUri(selectPet.petRprsImgAddr?.toUri(),context)

            init = false
        }
    }

    //DisposableEffect(Unit){
    //
    //    onDispose {
    //        if (init){
    //            viewModel.updatePetName("")
    //            viewModel.updatePetKind(
    //                PetListData(
    //                    petDogSzCd = "",
    //                    petNm = "사이즈/품종 선택",
    //                    petEnNm = "",
    //                    petInfoUnqNo = 0,
    //                    petTypCd = ""
    //                )
    //            )
    //            viewModel.updatePetWght("")
    //            viewModel.updatePetGender("남아")
    //            viewModel.updatePetNtr("했어요")
    //            viewModel.updateSelectedItem1(SCD(cdNm = "", cdld = "", upCdId = ""))
    //            viewModel.updateSelectedItem2(SggList(sggCd = "", sggNm = ""))
    //            viewModel.updateSelectedItem3(UmdList(umdCd = "", umdNm = ""))
    //            viewModel.updatePetBirth("")
    //            viewModel.updateYear(PickerState())
    //            viewModel.setImageUri(null,context)
    //            viewModel.updateAddress("주소 선택")
    //        }
    //    }
    //}

    LaunchedEffect(key1 = delete){
        if (delete){
            isLoading = true
            scope.launch {
                val result = viewModel.deletePet(selectPet.ownrPetUnqNo)
                if (result) {
                    sharedViewModel.updateInit(true)
                    sharedViewModel.loadPetInfo()
                    sharedViewModel.loadCurrentPetInfo()
                    navController.popBackStack()
                    navController.popBackStack()

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
                    viewModel.updatePetGender(context.getString(R.string.male))
                    viewModel.updatePetNtr(context.getString(R.string.did))
                    viewModel.updateSelectedItem1(SCD(cdNm = "", cdld = "", upCdId = ""))
                    viewModel.updateSelectedItem2(SggList(sggCd = "", sggNm = ""))
                    viewModel.updateSelectedItem3(UmdList(umdCd = "", umdNm = ""))
                    viewModel.updatePetBirth("")
                    viewModel.updateYear(PickerState())
                    viewModel.setImageUri(null,context)
                    viewModel.updateAddressPass(false)

                    init = true
                    isLoading = false

                } else {
                    isLoading = false
                    Toast
                        .makeText(context, "삭제 실패", Toast.LENGTH_SHORT)
                        .show()
                }

                isLoading = false
                delete = false
            }
        }
    }

    BackHandler {
        scope.launch {
            navController.popBackStack()

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
            viewModel.updatePetGender(context.getString(R.string.male))
            viewModel.updatePetNtr(context.getString(R.string.did))
            viewModel.updateSelectedItem1(SCD(cdNm = "", cdld = "", upCdId = ""))
            viewModel.updateSelectedItem2(SggList(sggCd = "", sggNm = ""))
            viewModel.updateSelectedItem3(UmdList(umdCd = "", umdNm = ""))
            viewModel.updatePetBirth("")
            viewModel.updateYear(PickerState())
            viewModel.setImageUri(null,context)
            viewModel.updateAddressPass(false)

            init = true
        }
    }

    Scaffold (
        modifier = modifier.fillMaxSize(),
        topBar = {
            BackTopBarInModify(title = stringResource(R.string.modify_pet_information), navController = navController, viewModel = viewModel, initChange = { newValue -> init = newValue})
        }
    ){ paddingValues ->

        if (alertShow){
            CustomAlertOneBtn(
                onDismiss = {alertShow = false},
                confirm = "확인",
                title = alertMsg
            )
        }

        if (showDialog){
            //CustomDialogDelete(
            //    onDismiss = { newValue -> showDialog = newValue },
            //    confirm = stringResource(id = R.string.delete),
            //    dismiss = stringResource(id = R.string.cancel_kor),
            //    title = stringResource(R.string.pet_information_delete),
            //    text = stringResource(id = R.string.delete_confirm),
            //    valueChange = { newValue -> delete = newValue}
            //)
            CustomAlert(
                onDismiss = { newValue -> showDialog = newValue },
                confirm = stringResource(id = R.string.delete),
                dismiss = stringResource(id = R.string.cancel_kor),
                title = stringResource(R.string.pet_information_delete),
                text = stringResource(id = R.string.delete_confirm),
                confirmJob = { delete = true }
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
                            focusManager.clearFocus()
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

        LoadingDialog(
            loadingText = if(delete) stringResource(R.string.delete_progress) else stringResource(R.string.modify_progress),
            loadingState = isLoading)

        Column (modifier= Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
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

            Text(text = stringResource(id = R.string.pet), fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier= Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
            )

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 8.dp)){

                Button(
                    enabled = petDorC != stringResource(id = R.string.dog),
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
                    colors = if(stringResource(id = R.string.dog) == petDorC) {
                        ButtonDefaults.buttonColors(containerColor = design_select_btn_bg, disabledContainerColor = design_select_btn_bg)
                    } else {
                        ButtonDefaults.buttonColors(Color.Transparent)
                    },
                    border = if(stringResource(id = R.string.dog) == petDorC) {
                        BorderStroke(1.dp, color = design_select_btn_text)
                    } else {
                        BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline)
                    },
                    contentPadding = PaddingValues(start = 14.dp,end=14.dp),
                    elevation = if(stringResource(id = R.string.dog) == petDorC){
                        ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                    } else {
                        ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    }

                ) {
                    Text(
                        text = stringResource(id = R.string.dog),
                        color = if(stringResource(id = R.string.dog) == petDorC) design_select_btn_text else MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp
                    )
                }

                Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                Button(
                    enabled = petDorC != stringResource(id = R.string.cat),
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
                    colors = if(stringResource(id = R.string.cat) == petDorC) {
                        ButtonDefaults.buttonColors(containerColor = design_select_btn_bg, disabledContainerColor = design_select_btn_bg)
                    } else {
                        ButtonDefaults.buttonColors(Color.Transparent)
                    },
                    border = if(stringResource(id = R.string.cat) == petDorC) {
                        BorderStroke(1.dp, color = design_select_btn_text)
                    } else {
                        BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline)
                    },
                    contentPadding = PaddingValues(start = 14.dp,end=14.dp),
                    elevation = if(stringResource(id = R.string.cat) == petDorC){
                        ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                    } else {
                        ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.cat),
                        color = if(stringResource(id = R.string.cat) == petDorC) design_select_btn_text else MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp
                    )
                }

            }

            // 사이즈 품종 선택
            Text(text = stringResource(id = R.string.size_breed_selection), fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier= Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
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
                Row(modifier= Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                    Text(text = petKind.petNm, color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular))
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "", tint = MaterialTheme.colorScheme.onPrimary)
                }
            }

            //주소 선택
            Text(text = stringResource(id = R.string.activity_region), fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier= Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
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
                Row(modifier= Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                    Text(
                        text = if(scd.cdld.isNullOrBlank()) stringResource(id = R.string.address_selection) else "${scd.cdNm?:""} ${sgg.sggNm?:""} ${umd.umdNm?:""}",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular))
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "", tint = MaterialTheme.colorScheme.onPrimary)
                }
            }

            Text(text = stringResource(id = R.string.name), fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier= Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
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
                placeholder = { Text(text = stringResource(id = R.string.place_holder_pet_name), fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp) },
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

            Text(text = stringResource(id = R.string.birthday), fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier= Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
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
                    placeholder = { Text(text = stringResource(id = R.string.place_holder_birthday), fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp) },
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
                            checkmarkColor = design_white
                        )
                    )

                    Text(text = stringResource(id = R.string.age_unknown_), fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        color = MaterialTheme.colorScheme.onPrimary, modifier= Modifier.offset(x = (-8).dp), letterSpacing = (-0.7).sp
                    )
                }


            }

            Text(text = stringResource(id = R.string.gender), fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier= Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
            )

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 8.dp)){
                Button(
                    onClick = { viewModel.updatePetGender(context.getString(R.string.male)) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .shadow(ambientColor = design_shadow, elevation = 0.dp)
                    ,
                    shape = RoundedCornerShape(12.dp),
                    colors = if(stringResource(id = R.string.male) == petGender) {
                        ButtonDefaults.buttonColors(design_select_btn_bg)
                    } else {
                        ButtonDefaults.buttonColors(Color.Transparent)
                    },
                    border = if(stringResource(id = R.string.male) == petGender) {
                        BorderStroke(1.dp, color = design_select_btn_text)
                    } else {
                        BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline)
                    },
                    contentPadding = PaddingValues(start = 14.dp,end=14.dp),
                    elevation = if(stringResource(id = R.string.male) == petGender){
                        ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                    } else {
                        ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    }

                ) {
                    Text(
                        text = stringResource(id = R.string.male),
                        color = if(stringResource(id = R.string.male) == petGender) design_select_btn_text else MaterialTheme.colorScheme.onPrimary,
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
                    colors = if(stringResource(id = R.string.female) == petGender) {
                        ButtonDefaults.buttonColors(design_select_btn_bg)
                    } else {
                        ButtonDefaults.buttonColors(Color.Transparent)
                    },
                    border = if(stringResource(id = R.string.female) == petGender) {
                        BorderStroke(1.dp, color = design_select_btn_text)
                    } else {
                        BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline)
                    },
                    contentPadding = PaddingValues(start = 14.dp,end=14.dp),
                    elevation = if(stringResource(id = R.string.female) == petGender){
                        ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                    } else {
                        ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.female),
                        color = if(stringResource(id = R.string.female) == petGender) design_select_btn_text else MaterialTheme.colorScheme.onPrimary,
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
                        .shadow(ambientColor = design_shadow, elevation = 0.dp),
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

            Text(text = stringResource(id = R.string.neutering), fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
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
                    colors = if (stringResource(id = R.string.did) == petNtr) {
                        ButtonDefaults.buttonColors(design_select_btn_bg)
                    } else {
                        ButtonDefaults.buttonColors(Color.Transparent)
                    },
                    border = if (stringResource(id = R.string.did) == petNtr) {
                        BorderStroke(1.dp, color = design_select_btn_text)
                    } else {
                        BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline)
                    },
                    contentPadding = PaddingValues(start = 14.dp, end = 14.dp),
                    elevation = if (stringResource(id = R.string.did) == petNtr) {
                        ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                    } else {
                        ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    }

                ) {
                    Text(
                        text = stringResource(id = R.string.did),
                        color = if (stringResource(id = R.string.did) == petNtr) design_select_btn_text else MaterialTheme.colorScheme.onPrimary,
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
                    colors = if (stringResource(id = R.string.didnot) == petNtr) {
                        ButtonDefaults.buttonColors(design_select_btn_bg)
                    } else {
                        ButtonDefaults.buttonColors(Color.Transparent)
                    },
                    border = if (stringResource(id = R.string.didnot) == petNtr) {
                        BorderStroke(1.dp, color = design_select_btn_text)
                    } else {
                        BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline)
                    },
                    contentPadding = PaddingValues(start = 14.dp, end = 14.dp),
                    elevation = if (stringResource(id = R.string.didnot) == petNtr) {
                        ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                    } else {
                        ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.didnot),
                        color = if (stringResource(id = R.string.didnot) == petNtr) design_select_btn_text else MaterialTheme.colorScheme.onPrimary,
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
                            val result = viewModel.modifyPet(context,selectPet.ownrPetUnqNo)
                            if(result){
                                sharedViewModel.loadCurrentPetInfo()
                                val result = sharedViewModel.loadPetInfo()
                                if (result){
                                    val updatedPet = sharedViewModel.petInfo.value.find { it.ownrPetUnqNo == selectPet.ownrPetUnqNo }?.copy(
                                        mngrType = selectPet.mngrType, memberList = profilePet?.memberList ?: emptyList()
                                    )
                                    if (updatedPet != null) {
                                        sharedViewModel.updateProfilePet(updatedPet)
                                    }
                                }
                                isLoading = false
                                navController.popBackStack()
                            }else{
                                isLoading = false
                                Toast.makeText(context, R.string.petinfo_modify_fail_toast, Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            alertShow = true
                        }
                    }
                },
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 20.dp)
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = design_button_bg)
            )
            {
                Text(text = stringResource(id = R.string.modify_verb), color = design_white, fontSize = 14.sp, fontFamily = FontFamily(
                    Font(R.font.pretendard_regular)
                )
                )
            }

            Text(
                text = stringResource(R.string.pet_registinformation_delete),
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 14.sp, letterSpacing = (-0.7).sp,
                textDecoration = TextDecoration.Underline,
                color = design_sharp,
                modifier = Modifier
                    .padding(end = 20.dp, bottom = 20.dp)
                    .align(Alignment.End)
                    .clickable {
                        showDialog = true
                    }
            )
        }// Column
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackTopBarInModify(title: String, navController: NavHostController, backVisible:Boolean=true,viewModel: UserCreateViewModel,initChange:(Boolean)->Unit){

    val context = LocalContext.current

    TopAppBar(
        colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = Modifier.height(60.dp),
        title = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                AnimatedVisibility(
                    modifier = Modifier.align(Alignment.CenterStart),
                    visible = backVisible,
                    enter = scaleIn(),
                    exit = scaleOut()
                ) {
                    Icon(painter = painterResource(id = R.drawable.arrow_back),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .clickable {
                                navController.popBackStack()

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
                                viewModel.updatePetGender(context.getString(R.string.male))
                                viewModel.updatePetNtr(context.getString(R.string.did))
                                viewModel.updateSelectedItem1(SCD(cdNm = "", cdld = "", upCdId = ""))
                                viewModel.updateSelectedItem2(SggList(sggCd = "", sggNm = ""))
                                viewModel.updateSelectedItem3(UmdList(umdCd = "", umdNm = ""))
                                viewModel.updatePetBirth("")
                                viewModel.updateYear(PickerState())
                                viewModel.setImageUri(null, context)
                                viewModel.updateAddressPass(false)

                                initChange(true)
                            }
                    )
                }


                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    letterSpacing = (-1.0).sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDialogDelete(
    onDismiss:(Boolean) -> Unit,
    confirm: String,
    dismiss : String,
    title : String,
    text : String,
    valueChange : (Boolean) -> Unit
){
    AlertDialog(
        onDismissRequest = { onDismiss(false) },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ){
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
                    text = title,
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    fontSize = 18.sp, letterSpacing = (-0.8).sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(start = 20.dp, top = 30.dp)
                )

                Text(
                    text = text,
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
                                onDismiss(false)
                            },
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
                                valueChange(true)
                                onDismiss(false)
                            }
                        ,
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

    //AlertDialog(
    //    onDismissRequest = { onDismiss(false) },
    //    confirmButton = {
    //        Button(
    //            onClick = {
    //                valueChange(true)
    //                onDismiss(false)
    //            },
    //            shape = RoundedCornerShape(12.dp),
    //            colors = ButtonDefaults.buttonColors(
    //                containerColor = design_button_bg
    //            )
    //        ) {
    //            Text(
    //                text = confirm,
    //                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
    //                color = design_white
    //            )
    //        } },
    //    title = {
    //        Text(
    //            text = title,
    //            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
    //            color = MaterialTheme.colorScheme.onPrimary
    //        ) },
    //    text = {
    //        Text(
    //            text = text,
    //            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
    //            color = MaterialTheme.colorScheme.secondary
    //        )
    //           },
    //    dismissButton = {
    //        Button(
    //            onClick = {
    //                onDismiss(false)
    //            },
    //            shape = RoundedCornerShape(12.dp),
    //            colors = ButtonDefaults.buttonColors(
    //                containerColor = MaterialTheme.colorScheme.onSecondary
    //            ),
    //            border = BorderStroke(1.dp, color = design_login_text)
    //        ) {
    //            Text(
    //                text = dismiss,
    //                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
    //                color = MaterialTheme.colorScheme.onPrimary
    //            )
    //        }
    //    },
    //    containerColor = MaterialTheme.colorScheme.primary
    //)
}

@SuppressLint("SimpleDateFormat")
fun formatDate(inputDate: String): String {
    val inputFormat = SimpleDateFormat("yyyyMMdd")
    val outputFormat = SimpleDateFormat("yyyy-MM-dd")

    try {
        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        return inputDate
    }
}