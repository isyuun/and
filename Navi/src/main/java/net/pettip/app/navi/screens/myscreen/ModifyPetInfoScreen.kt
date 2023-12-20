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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.focus.FocusDirection
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.Screen
import net.pettip.app.navi.component.CustomTextField
import net.pettip.app.navi.component.LoadingDialog
import net.pettip.app.navi.ui.theme.design_btn_border
import net.pettip.app.navi.ui.theme.design_button_bg
import net.pettip.app.navi.ui.theme.design_camera_bg
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_login_text
import net.pettip.app.navi.ui.theme.design_placeHolder
import net.pettip.app.navi.ui.theme.design_select_btn_bg
import net.pettip.app.navi.ui.theme.design_select_btn_text
import net.pettip.app.navi.ui.theme.design_shadow
import net.pettip.app.navi.ui.theme.design_sharp
import net.pettip.app.navi.ui.theme.design_skip
import net.pettip.app.navi.ui.theme.design_textFieldOutLine
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.ui.theme.seed
import net.pettip.app.navi.viewmodel.PickerState
import net.pettip.app.navi.viewmodel.SettingViewModel
import net.pettip.app.navi.viewmodel.SharedViewModel
import net.pettip.app.navi.viewmodel.UserCreateViewModel
import net.pettip.data.SCD
import net.pettip.data.SggList
import net.pettip.data.UmdList
import net.pettip.data.pet.PetListData
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

    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH는 0부터 시작하므로 1을 더해줍니다.
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

    val year = remember {(1980..2023).map { it.toString() }}
    val yearPickerState by viewModel.year.collectAsState()
    val month = remember {(1..12).map { it.toString() }}
    val monthPickerState by viewModel.month.collectAsState()
    val day = remember {(1..31).map { it.toString() }}
    val dayPickerState by viewModel.day.collectAsState()

    var yearIndex by rememberSaveable { mutableIntStateOf(year.indexOf(currentYear.toString())) }
    var monthIndex by rememberSaveable { mutableIntStateOf(month.indexOf(currentMonth.toString())) }
    var dayIndex by rememberSaveable { mutableIntStateOf(day.indexOf(currentDay.toString())) }

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
                    "고양이"
                }else{
                    "강아지"
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
                    viewModel.updatePetGender("남아")
                    viewModel.updatePetNtr("했어요")
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
            viewModel.updatePetGender("남아")
            viewModel.updatePetNtr("했어요")
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
            BackTopBarInModify(title = "반려동물 정보 수정", navController = navController, viewModel = viewModel, initChange = {newValue -> init = newValue})
        }
    ){ paddingValues ->

        if (showDialog){
            CustomDialogDelete(
                onDismiss = { newValue -> showDialog = newValue },
                confirm = "삭제하기",
                dismiss = "취소",
                title = "반려동물 정보 삭제하기",
                text = "정말 삭제하시겠어요?",
                valueChange = { newValue -> delete = newValue}
            )
        }

        LoadingDialog(
            loadingText = if(delete) "삭제 처리중.." else "정보 수정중..",
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

            Text(text = "반려동물", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier= Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
            )

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 8.dp)){

                Button(
                    enabled = petDorC != "강아지",
                    onClick = {
                        viewModel.updatePetDorC("강아지")
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
                    colors = if("강아지" == petDorC) {
                        ButtonDefaults.buttonColors(design_select_btn_bg)
                    } else {
                        ButtonDefaults.buttonColors(Color.Transparent)
                    },
                    border = if("강아지" == petDorC) {
                        BorderStroke(1.dp, color = design_select_btn_text)
                    } else {
                        BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline)
                    },
                    contentPadding = PaddingValues(start = 14.dp,end=14.dp),
                    elevation = if("강아지" == petDorC){
                        ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                    } else {
                        ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    }

                ) {
                    Text(
                        text = "강아지",
                        color = if("강아지" == petDorC) design_select_btn_text else MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp
                    )
                }

                Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                Button(
                    enabled = petDorC != "고양이",
                    onClick = {
                        viewModel.updatePetDorC("고양이")
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
                    colors = if("고양이" == petDorC) {
                        ButtonDefaults.buttonColors(design_select_btn_bg)
                    } else {
                        ButtonDefaults.buttonColors(Color.Transparent)
                    },
                    border = if("고양이" == petDorC) {
                        BorderStroke(1.dp, color = design_select_btn_text)
                    } else {
                        BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline)
                    },
                    contentPadding = PaddingValues(start = 14.dp,end=14.dp),
                    elevation = if("고양이" == petDorC){
                        ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                    } else {
                        ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    }
                ) {
                    Text(
                        text = "고양이",
                        color = if("고양이" == petDorC) design_select_btn_text else MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp
                    )
                }

            }

            // 사이즈 품종 선택
            Text(text = "사이즈/품종 선택", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
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
            Text(text = "주소", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
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
                        text = if(scd.cdld == "") "주소 선택" else "${scd.cdNm} ${sgg.sggNm} ${umd.umdNm}",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular))
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "", tint = MaterialTheme.colorScheme.onPrimary)
                }
            }

            Text(text = "이름", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier= Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
            )

            CustomTextField(
                value = petName,
                onValueChange = { viewModel.updatePetName(it) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next),
                modifier = Modifier
                    .padding(start = 20.dp, top = 8.dp, end = 20.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                placeholder = { Text(text = "이름을 입력해주세요", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp) },
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

            Text(text = "생일", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier= Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
            )

            Row (modifier= Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 8.dp), verticalAlignment = Alignment.CenterVertically){
                CustomTextField(
                    enabled = !petBirthUK,
                    readOnly = true,
                    value =
                    if(!yearPickerState.selectedItem.equals("")){
                        "${yearPickerState.selectedItem}-${monthPickerState.selectedItem}-${dayPickerState.selectedItem}"
                    }else{
                        formatDate(petBirth)
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
                            if (focusState.isFocused) {
                                keyboardController?.hide()
                            }
                            expanded = focusState.isFocused
                        },
                    placeholder = { Text(text = "생일을 입력해주세요", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp) },
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

                    Text(text = "나이 모름", fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        color = MaterialTheme.colorScheme.onPrimary, modifier= Modifier.offset(x = (-8).dp), letterSpacing = (-0.7).sp
                    )
                }


            }

            AnimatedVisibility(
                visible = expanded
            ) {
                Column (
                    modifier= Modifier
                        .padding(start = 20.dp, end = 20.dp)
                ){
                    Spacer(modifier = Modifier.padding(top=24.dp))
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Picker(
                            state = yearPickerState,
                            items = year,
                            visibleItemsCount = 3,
                            startIndex = yearIndex,
                            modifier = Modifier.weight(0.3f),
                            textModifier = Modifier.padding(8.dp),
                            textStyle = TextStyle(
                                fontSize = 20.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)), color = design_login_text
                            )
                        )
                        Picker(
                            state = monthPickerState,
                            items = month,
                            visibleItemsCount = 3,
                            startIndex = monthIndex,
                            modifier = Modifier.weight(0.2f),
                            textModifier = Modifier.padding(8.dp),
                            textStyle = TextStyle(
                                fontSize = 20.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)), color = design_login_text
                            )
                        )
                        Picker(
                            state = dayPickerState,
                            items = day,
                            visibleItemsCount = 3,
                            startIndex = dayIndex,
                            modifier = Modifier.weight(0.3f),
                            textModifier = Modifier.padding(8.dp),
                            textStyle = TextStyle(
                                fontSize = 20.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)), color = design_login_text
                            )
                        )
                        Button(
                            onClick = {
                                yearIndex = year.indexOf(yearPickerState.selectedItem)
                                monthIndex = month.indexOf(monthPickerState.selectedItem)
                                dayIndex = day.indexOf(dayPickerState.selectedItem)

                                val formYear = String.format("%04d", yearPickerState.selectedItem.toInt())
                                val formMonth = String.format("%02d", monthPickerState.selectedItem.toInt())
                                val formDay = String.format("%02d", dayPickerState.selectedItem.toInt())
                                val formattedDate = "$formYear$formMonth$formDay"

                                viewModel.updatePetBirth(formattedDate)
                                focusManager.moveFocus(FocusDirection.Next)
                            },
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .weight(0.2f)
                                .height(100.dp),
                            shape = RoundedCornerShape(12.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = design_button_bg)
                        )
                        {
                            Text(text = "완\n료", color = design_white, fontSize = 14.sp, fontFamily = FontFamily(
                                Font(R.font.pretendard_regular)
                            )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.padding(top=24.dp))
                }
            }


            //Text(text = "몸무게", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            //    modifier= Modifier.padding(start = 20.dp, top = 16.dp), color = design_login_text
            //)
            //
            //Row (modifier= Modifier
            //    .fillMaxWidth()
            //    .padding(start = 20.dp, end = 20.dp, top = 8.dp), verticalAlignment = Alignment.CenterVertically){
            //    CustomTextField(
            //        value = petWght,
            //        onValueChange = { viewModel.updatePetWght(it) },
            //        singleLine = true,
            //        keyboardOptions = KeyboardOptions(
            //            keyboardType = KeyboardType.Decimal,
            //            imeAction = ImeAction.Done),
            //        modifier = Modifier
            //            .weight(1f)
            //            .height(48.dp),
            //        placeholder = { Text(text = "몸무게를 입력해주세요", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp) },
            //        colors = OutlinedTextFieldDefaults.colors(
            //            unfocusedPlaceholderColor = design_placeHolder,
            //            focusedPlaceholderColor = design_placeHolder,
            //            unfocusedBorderColor = design_textFieldOutLine,
            //            focusedBorderColor = design_login_text,
            //            unfocusedContainerColor = design_white,
            //            focusedContainerColor = design_white,
            //            unfocusedLeadingIconColor = design_placeHolder,
            //            focusedLeadingIconColor = design_login_text
            //        ),
            //        shape = RoundedCornerShape(4.dp),
            //        innerPadding = PaddingValues(start=16.dp)
            //    )
            //
            //    Row (modifier = Modifier.width(100.dp),verticalAlignment = Alignment.CenterVertically){
            //        Text(text = "kg", fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            //            color = design_login_text, modifier= Modifier.padding(start = 14.dp), letterSpacing = (-0.7).sp
            //        )
            //    }
            //
            //
            //}
            //
            //Text(text = "* 1kg 미만의 경우, 600g = 0.6으로 입력",
            //    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            //    fontSize = 12.sp, color = design_skip,
            //    modifier= Modifier
            //        .padding(start = 20.dp, top = 8.dp),
            //    letterSpacing = (-0.6).sp
            //)

            Text(text = "성별", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier= Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
            )

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 8.dp)){
                Button(
                    onClick = { viewModel.updatePetGender("남아") },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .shadow(ambientColor = design_shadow, elevation = 0.dp)
                    ,
                    shape = RoundedCornerShape(12.dp),
                    colors = if("남아" == petGender) {
                        ButtonDefaults.buttonColors(design_select_btn_bg)
                    } else {
                        ButtonDefaults.buttonColors(Color.Transparent)
                    },
                    border = if("남아" == petGender) {
                        BorderStroke(1.dp, color = design_select_btn_text)
                    } else {
                        BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline)
                    },
                    contentPadding = PaddingValues(start = 14.dp,end=14.dp),
                    elevation = if("남아" == petGender){
                        ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                    } else {
                        ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    }

                ) {
                    Text(
                        text = "남아",
                        color = if("남아" == petGender) design_select_btn_text else MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp
                    )
                }

                Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                Button(
                    onClick = { viewModel.updatePetGender("여아") },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .shadow(ambientColor = MaterialTheme.colorScheme.onSurface, elevation = 0.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = if("여아" == petGender) {
                        ButtonDefaults.buttonColors(design_select_btn_bg)
                    } else {
                        ButtonDefaults.buttonColors(Color.Transparent)
                    },
                    border = if("여아" == petGender) {
                        BorderStroke(1.dp, color = design_select_btn_text)
                    } else {
                        BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline)
                    },
                    contentPadding = PaddingValues(start = 14.dp,end=14.dp),
                    elevation = if("여아" == petGender){
                        ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                    } else {
                        ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    }
                ) {
                    Text(
                        text = "여아",
                        color = if("여아" == petGender) design_select_btn_text else MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp
                    )
                }

                Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                Button(
                    onClick = { viewModel.updatePetGender("모름") },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .shadow(ambientColor = design_shadow, elevation = 0.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = if("모름" == petGender) {
                        ButtonDefaults.buttonColors(design_select_btn_bg)
                    } else {
                        ButtonDefaults.buttonColors(Color.Transparent)
                    },
                    border = if("모름" == petGender) {
                        BorderStroke(1.dp, color = design_select_btn_text)
                    } else {
                        BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline)
                    },
                    contentPadding = PaddingValues(start = 14.dp,end=14.dp),
                    elevation = if("모름" == petGender){
                        ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                    } else {
                        ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    }
                ) {
                    Text(
                        text = "모름",
                        color = if("모름" == petGender) design_select_btn_text else MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp
                    )
                }


            }

            Text(text = "중성화", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier= Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
            )

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 8.dp)) {
                Button(
                    onClick = { viewModel.updatePetNtr("했어요") },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .shadow(ambientColor = MaterialTheme.colorScheme.onSurface, elevation = 0.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = if ("했어요" == petNtr) {
                        ButtonDefaults.buttonColors(design_select_btn_bg)
                    } else {
                        ButtonDefaults.buttonColors(Color.Transparent)
                    },
                    border = if ("했어요" == petNtr) {
                        BorderStroke(1.dp, color = design_select_btn_text)
                    } else {
                        BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline)
                    },
                    contentPadding = PaddingValues(start = 14.dp, end = 14.dp),
                    elevation = if ("했어요" == petNtr) {
                        ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                    } else {
                        ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    }

                ) {
                    Text(
                        text = "했어요",
                        color = if ("했어요" == petNtr) design_select_btn_text else MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp
                    )
                }

                Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                Button(
                    onClick = { viewModel.updatePetNtr("안했어요") },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .shadow(ambientColor = MaterialTheme.colorScheme.onSurface, elevation = 0.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = if ("안했어요" == petNtr) {
                        ButtonDefaults.buttonColors(design_select_btn_bg)
                    } else {
                        ButtonDefaults.buttonColors(Color.Transparent)
                    },
                    border = if ("안했어요" == petNtr) {
                        BorderStroke(1.dp, color = design_select_btn_text)
                    } else {
                        BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline)
                    },
                    contentPadding = PaddingValues(start = 14.dp, end = 14.dp),
                    elevation = if ("안했어요" == petNtr) {
                        ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                    } else {
                        ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    }
                ) {
                    Text(
                        text = "안했어요",
                        color = if ("안했어요" == petNtr) design_select_btn_text else MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp
                    )
                }

                Spacer(modifier = Modifier.padding(horizontal = 4.dp))

                Button(
                    onClick = { viewModel.updatePetNtr("모름") },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .shadow(ambientColor = MaterialTheme.colorScheme.onSurface, elevation = 0.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = if ("모름" == petNtr) {
                        ButtonDefaults.buttonColors(design_select_btn_bg)
                    } else {
                        ButtonDefaults.buttonColors(Color.Transparent)
                    },
                    border = if ("모름" == petNtr) {
                        BorderStroke(1.dp, color = design_select_btn_text)
                    } else {
                        BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline)
                    },
                    contentPadding = PaddingValues(start = 14.dp, end = 14.dp),
                    elevation = if ("모름" == petNtr) {
                        ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                    } else {
                        ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    }
                ) {
                    Text(
                        text = "모름",
                        color = if ("모름" == petNtr) design_select_btn_text else MaterialTheme.colorScheme.onPrimary,
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
                                sharedViewModel.loadPetInfo()
                                isLoading = false
                                navController.popBackStack()
                            }else{
                                isLoading = false
                                Toast.makeText(context, "수정에 실패했습니다", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 20.dp)
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = design_button_bg)
            )
            {
                Text(text = "수정하기", color = design_white, fontSize = 14.sp, fontFamily = FontFamily(
                    Font(R.font.pretendard_regular)
                )
                )
            }

            Text(
                text = "반려동물 등록 정보 삭제",
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
                                viewModel.updatePetGender("남아")
                                viewModel.updatePetNtr("했어요")
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
        confirmButton = {
            Button(
                onClick = {
                    valueChange(true)
                    onDismiss(false)
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = design_button_bg
                )
            ) {
                Text(
                    text = confirm,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    color = design_white
                )
            } },
        title = {
            Text(
                text = title,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                color = MaterialTheme.colorScheme.onPrimary
            ) },
        text = {
            Text(
                text = text,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                color = MaterialTheme.colorScheme.secondary
            )
               },
        dismissButton = {
            Button(
                onClick = {
                    onDismiss(false)
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onSecondary
                ),
                border = BorderStroke(1.dp, color = design_login_text)
            ) {
                Text(
                    text = dismiss,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.primary
    )
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