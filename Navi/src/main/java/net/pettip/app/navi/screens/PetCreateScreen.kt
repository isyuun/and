@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class
)

package net.pettip.app.navi.screens

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
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
import net.pettip.app.navi.component.BackTopBar
import net.pettip.app.navi.component.CustomTextField
import net.pettip.app.navi.component.ErrorScreen
import net.pettip.app.navi.component.LoadingDialog
import net.pettip.app.navi.screens.myscreen.integrityCheck
import net.pettip.app.navi.ui.theme.design_btn_border
import net.pettip.app.navi.ui.theme.design_button_bg
import net.pettip.app.navi.ui.theme.design_camera_bg
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_login_text
import net.pettip.app.navi.ui.theme.design_placeHolder
import net.pettip.app.navi.ui.theme.design_select_btn_bg
import net.pettip.app.navi.ui.theme.design_select_btn_text
import net.pettip.app.navi.ui.theme.design_shadow
import net.pettip.app.navi.ui.theme.design_skip
import net.pettip.app.navi.ui.theme.design_textFieldOutLine
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.LoginViewModel
import net.pettip.app.navi.viewmodel.SharedViewModel
import net.pettip.app.navi.viewmodel.UserCreateViewModel
import net.pettip.data.SCD
import net.pettip.data.SggList
import net.pettip.data.UmdList
import net.pettip.data.pet.PetListData
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetCreateScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: UserCreateViewModel,
    loginViewModel: LoginViewModel,
    sharedViewModel: SharedViewModel
){

    val scdList by remember { mutableStateOf(viewModel.scdList) }
    val petCreateSuccess by viewModel.petCreateSuccess.collectAsState()
    if(petCreateSuccess){
        navController.navigate(Screen.MainScreen.route){
            popUpTo(0)
        }
    }

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

    val userId by viewModel.userID.collectAsState()
    val userPw by viewModel.userPW.collectAsState()
    val snsLogin by viewModel.snsLogin.collectAsState()
    var isLoading by remember{ mutableStateOf(false) }
    val dm by viewModel.dm.collectAsState()

    Log.d("LOG",scd.cdNm+":"+sgg.sggNm)

    Scaffold (
        modifier = modifier.fillMaxSize(),
        topBar = {
            BackTopBar(title = "반려동물 정보 입력", navController = navController)
        }
    ){ paddingValues ->
        Column (modifier= Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.primary)
        ){
            LoadingDialog(
                loadingText = "펫 등록중...",
                loadingState = isLoading
            )

            Row (horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()){
                Text(text = "건너뛰기",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp, color = MaterialTheme.colorScheme.secondary,
                    modifier= Modifier
                        .padding(end = 20.dp)
                        .clickable {
                            scope.launch {
                                val userCreateSuccess = viewModel.sendUserToServer()
                                if (userCreateSuccess) {
                                    val loginSuccess =
                                        loginViewModel.onLoginButtonClick(
                                            userId,
                                            userPw,
                                            snsLogin
                                        )
                                    if (loginSuccess) {
                                        sharedViewModel.updateInit(true)
                                        navController.navigate(Screen.MainScreen.route) {
                                            popUpTo(0)
                                        }
                                    }
                                } else {
                                    scope.launch {
                                        Toast
                                            .makeText(
                                                context,
                                                dm,
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                    }
                                }
                            }
                        },
                    textDecoration = TextDecoration.Underline
                )
            }
            
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
                modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
            )

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 8.dp)){

                Button(
                    onClick = { viewModel.updatePetDorC("강아지") },
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
                    onClick = { viewModel.updatePetDorC("고양이") },
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
            Text(text = "주소", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
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
                        text = if(scd.cdld == "") "주소 선택" else "${scd.cdNm} ${sgg.sggNm} ${umd.umdNm}",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "", tint = MaterialTheme.colorScheme.onPrimary)
                }
            }

            Text(text = "이름", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
            )

            CustomTextField(
                value = petName,
                onValueChange = {viewModel.updatePetName(it)},
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next),
                modifier = Modifier
                    .padding(start = 20.dp, top = 8.dp, end = 20.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                placeholder = { Text(text = "이름을 입력해주세요", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
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
                modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
            )

            Row (modifier= Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 8.dp), verticalAlignment = Alignment.CenterVertically){
                CustomTextField(
                    enabled = !petBirthUK,
                    readOnly = true,
                    value = if(!yearPickerState.selectedItem.equals("")){
                        "${yearPickerState.selectedItem}-${monthPickerState.selectedItem}-${dayPickerState.selectedItem}"
                    }else{
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
                            if (focusState.isFocused) {
                                keyboardController?.hide()
                            }
                            expanded = focusState.isFocused
                        },
                    placeholder = { Text(text = "생일을 입력해주세요", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
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

                    Text(text = "나이 모름", fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        color = MaterialTheme.colorScheme.onPrimary, modifier=Modifier.offset(x = (-8).dp), letterSpacing = (-0.7).sp
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
                                fontSize = 20.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)), color = MaterialTheme.colorScheme.onPrimary)
                        )
                        Picker(
                            state = monthPickerState,
                            items = month,
                            visibleItemsCount = 3,
                            startIndex = monthIndex,
                            modifier = Modifier.weight(0.2f),
                            textModifier = Modifier.padding(8.dp),
                            textStyle = TextStyle(
                                fontSize = 20.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)), color = MaterialTheme.colorScheme.onPrimary)
                        )
                        Picker(
                            state = dayPickerState,
                            items = day,
                            visibleItemsCount = 3,
                            startIndex = dayIndex,
                            modifier = Modifier.weight(0.3f),
                            textModifier = Modifier.padding(8.dp),
                            textStyle = TextStyle(
                                fontSize = 20.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)), color = MaterialTheme.colorScheme.onPrimary)
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
                            Text(text = "완\n료", color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
                        }
                    }
                    Spacer(modifier = Modifier.padding(top=24.dp))
                }
            }


            Text(text = "몸무게", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
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
                    innerPadding = PaddingValues(start=16.dp)
                )

                Row (modifier = Modifier.width(100.dp),verticalAlignment = Alignment.CenterVertically){
                    Text(text = "kg", fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        color = MaterialTheme.colorScheme.onPrimary, modifier=Modifier.padding(start = 14.dp), letterSpacing = (-0.7).sp
                    )
                }


            }

            Text(text = "* 1kg 미만의 경우, 600g = 0.6으로 입력",
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary,
                modifier= Modifier
                    .padding(start = 20.dp, top = 8.dp),
                letterSpacing = (-0.6).sp
            )

            Text(text = "성별", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
            )

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 8.dp)){
                Button(
                    onClick = { viewModel.updatePetGender("남아") },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .shadow(ambientColor = MaterialTheme.colorScheme.onSurface, elevation = 0.dp)
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
                        .shadow(ambientColor = MaterialTheme.colorScheme.onSurface, elevation = 0.dp),
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
                modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
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
                            val userCreateSuccess = viewModel.sendUserToServer()
                            if (userCreateSuccess){
                                val loginSuccess = loginViewModel.onLoginButtonClick(userId, userPw, snsLogin)
                                if (loginSuccess){
                                    val petCreateSuccess = viewModel.createPet(context)
                                    if(petCreateSuccess){
                                        isLoading = false
                                        sharedViewModel.updateInit(true)
                                        navController.navigate(Screen.MainScreen.route){
                                            popUpTo(0)
                                        }
                                    }else{
                                        isLoading = false
                                        Toast.makeText(context, viewModel.myPetResModel.value?.detailMessage ?: "", Toast.LENGTH_SHORT).show()
                                    }
                                }else{
                                    isLoading = false
                                }
                            }else{
                                isLoading = false
                                Toast.makeText(context, viewModel.userResponse.value.detailMessage, Toast.LENGTH_SHORT).show()
                            }

                            isLoading = false
                        }
                    }
                },
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 40.dp)
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = design_button_bg)
            )
            {
                Text(text = "가입하기", color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
            }
        }// Column
    }

}
@Composable
fun CircleImageCreate(viewModel: UserCreateViewModel){

    val imageUri by viewModel.imageUri.collectAsState()

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){ uri ->
        viewModel.setImageUri(uri,context)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetKindContent(
    viewModel: UserCreateViewModel,
    navController: NavHostController
){

    val context = LocalContext.current
    val searchText by viewModel.searchText.collectAsState()
    val pets by viewModel.pets.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    var expanded by remember { mutableStateOf (false) }

    var selectPet by remember{ mutableStateOf<PetListData?>(null) }

    LaunchedEffect(Unit){
        viewModel.getPetType()
    }

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            BackTopBar(title = "사이즈/품종 선택", navController = navController)
        }
    ) { paddingValues ->
        Box (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.primary)
        ){
            Column (modifier= Modifier
                .fillMaxSize()
            ){

                Text(text = "펫종", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
                )

                CustomTextField(
                    value = searchText,
                    onValueChange = viewModel::onSearchTextChange,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next),
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .padding(start = 20.dp, top = 8.dp, end = 20.dp)
                        .fillMaxWidth()
                        .height(48.dp)
                        .onFocusChanged { focusState ->
                            expanded = focusState.isFocused
                        },
                    placeholder = { Text(text = "펫종을 선택해주세요", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
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
                    shape = RoundedCornerShape(12.dp),
                    innerPadding = PaddingValues(start=16.dp),
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                )

                Spacer(modifier = Modifier.padding(top= 4.dp))

                AnimatedVisibility(
                    visible = expanded,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    if(isSearching) {
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .background(color = MaterialTheme.colorScheme.primary)) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 400.dp)
                                .background(color = MaterialTheme.colorScheme.primary),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            items(pets){ pets ->
                                petKindItem(viewModel = viewModel, pet = pets, focusRequester = focusRequester, onSelect = {newValue -> selectPet = newValue})
                            }
                        }
                    }
                }

            }

            Button(
                onClick = {
                    if (selectPet == null){
                        Toast.makeText(context, "품종을 선택해주세요", Toast.LENGTH_SHORT).show()
                    }else{
                        viewModel.updatePetKind(selectPet!!)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 40.dp)
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = design_button_bg)
            )
            {
                Text(text = "선택완료", color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
            }
        }
    }
}

@Composable
fun petKindItem(viewModel: UserCreateViewModel, pet: PetListData, focusRequester: FocusRequester, onSelect: (PetListData)->Unit){

    val focusManager = LocalFocusManager.current

    Column (
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(48.dp)
            .background(MaterialTheme.colorScheme.primary)
            .border(
                color = MaterialTheme.colorScheme.onSurface,
                width = 1.dp,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                viewModel.onSearchTextChange(pet.petNm)
                //viewModel.updatePetKind(pet)
                onSelect(pet)
                focusManager.clearFocus()
            },
        verticalArrangement = Arrangement.Center
    ){

        Text(
            text = pet.petNm,
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            letterSpacing = (-0.6).sp,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun LocationPickContent(
    viewModel: UserCreateViewModel,
    navController: NavHostController
){

    val scdList = viewModel.scdList
    val sggList by viewModel.sggList.collectAsState()
    val umdList by viewModel.umdList.collectAsState()

    var expanded1 by remember { mutableStateOf (false) }
    var expanded2 by remember { mutableStateOf (false) }
    var expanded3 by remember { mutableStateOf (false) }

    var selectSCD by remember{ mutableStateOf<SCD?>(null) }
    var selectSGG by remember{ mutableStateOf<SggList?>(null) }
    var selectUMD by remember{ mutableStateOf<UmdList?>(null) }

    val context = LocalContext.current

    DisposableEffect(Unit){
        onDispose {
            viewModel.updateSggList(emptyList())
            viewModel.updateUmdList(emptyList())
        }
    }

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            BackTopBar(title = "주소 선택", navController = navController)
        }
    ) { paddingValues ->
        Box (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.primary)
        ){
            Column (modifier= Modifier
                .fillMaxSize()
            ){

                Text(text = "시/도", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
                )

                Row (
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 8.dp)
                        .fillMaxWidth()
                        .height(48.dp)
                        .border(
                            width = 1.dp,
                            color = if (expanded1) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { expanded1 = !expanded1 },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = selectSCD?.cdNm ?: "시/도를 선택해주세요",
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = if(selectSCD == null) 14.sp else 16.sp,
                        letterSpacing = if(selectSCD == null) (-0.7).sp else (-0.8).sp,
                        color = if(selectSCD == null) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(start = 16.dp)
                    )

                    Icon(
                        imageVector = if(expanded1){
                            Icons.Default.KeyboardArrowUp
                        }else{
                            Icons.Default.KeyboardArrowDown
                        },
                        contentDescription = "", tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(end = 16.dp))
                }

                Spacer(modifier = Modifier.padding(top= 4.dp))

                AnimatedVisibility(
                    visible = expanded1,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp)
                            .background(color = Color.Transparent),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        items(scdList){ scd ->
                            addressItem1(
                                viewModel = viewModel,
                                address = scd,
                                onClick = {newValue -> expanded1 = newValue},
                                onSelect = {newValue -> selectSCD = newValue},
                                sggClear = { selectSGG = null },
                                umdClear = { selectUMD = null }
                            )
                        }
                    }
                }

                AnimatedVisibility(
                    visible = sggList.isNotEmpty(),
                    enter = slideInHorizontally(initialOffsetX = {-it})+ fadeIn(),
                    exit = slideOutHorizontally(targetOffsetX = {-it})+ fadeOut()
                ) {
                    Column {
                        Text(text = "시/군/구", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                            modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
                        )

                        Row (
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp, top = 8.dp)
                                .fillMaxWidth()
                                .height(48.dp)
                                .border(
                                    width = 1.dp,
                                    color = if (expanded2) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outline,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { expanded2 = !expanded2 },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text(
                                text = selectSGG?.sggNm ?: "시/군/구를 선택해주세요",
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                fontSize = if(selectSGG == null) 14.sp else 16.sp,
                                letterSpacing = if(selectSGG == null) (-0.7).sp else (-0.8).sp,
                                color = if(selectSGG == null) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(start = 16.dp)
                            )

                            Icon(
                                imageVector = if(expanded2){
                                    Icons.Default.KeyboardArrowUp
                                }else{
                                    Icons.Default.KeyboardArrowDown
                                },
                                contentDescription = "", tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(end = 16.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(top= 4.dp))

                AnimatedVisibility(
                    visible = expanded2,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp)
                            .background(color = Color.Transparent),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        items(sggList){ sggList ->
                            addressItem2(
                                viewModel = viewModel,
                                address = sggList,
                                sidoCd = selectSCD?.cdld?:"",
                                onClick = {newValue -> expanded2 = newValue},
                                onSelect = {newValue -> selectSGG = newValue},
                                umdClear = { selectUMD = null }
                            )
                        }
                    }
                }


                AnimatedVisibility(
                    visible = umdList.isNotEmpty(),
                    enter = slideInHorizontally(initialOffsetX = {-it})+ fadeIn(),
                    exit = slideOutHorizontally(targetOffsetX = {-it})+ fadeOut()
                ) {
                    Column {
                        Text(text = "읍/면/동", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                            modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
                        )

                        Row (
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp, top = 8.dp)
                                .fillMaxWidth()
                                .height(48.dp)
                                .border(
                                    width = 1.dp,
                                    color = if (expanded3) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outline,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { expanded3 = !expanded3 },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text(
                                text = selectUMD?.umdNm ?: "읍/면/동을 선택해주세요",
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                fontSize = if(selectUMD == null) 14.sp else 16.sp,
                                letterSpacing = if(selectUMD == null) (-0.7).sp else (-0.8).sp,
                                color = if(selectUMD == null) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(start = 16.dp)
                            )

                            Icon(
                                imageVector = if(expanded3){
                                    Icons.Default.KeyboardArrowUp
                                }else{
                                    Icons.Default.KeyboardArrowDown
                                },
                                contentDescription = "", tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(end = 16.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(top= 4.dp))

                AnimatedVisibility(
                    visible = expanded3,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.dp)
                            .background(color = Color.Transparent),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        items(umdList){ umdList ->
                            addressItem3(viewModel = viewModel, address = umdList, onClick = {newValue -> expanded3 = newValue},  onSelect = {newValue -> selectUMD = newValue})
                        }
                    }
                }
            }

            Button(
                onClick = {
                    if (selectSCD == null){
                        Toast.makeText(context, "시/도를 선택해주세요", Toast.LENGTH_SHORT).show()
                    }else if (selectSGG == null){
                        Toast.makeText(context, "시/군/구를 선택해주세요", Toast.LENGTH_SHORT).show()
                    }else if (umdList.isNotEmpty() && selectUMD == null){
                        Toast.makeText(context, "읍/면/동을 선택해주세요", Toast.LENGTH_SHORT).show()
                    }else{

                        viewModel.updateSelectedItem1(selectSCD!!)
                        viewModel.updateSelectedItem2(selectSGG!!)
                        if (umdList.isNotEmpty()) viewModel.updateSelectedItem3(selectUMD!!) else viewModel.updateSelectedItem3(UmdList("",""))

                        viewModel.updateAddressPass(true)
                        navController.popBackStack()
                    }

                          },
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 40.dp)
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = design_button_bg)
            )
            {
                Text(text = "선택완료", color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
            }
        }

    }
}

@Composable
fun addressItem1(viewModel: UserCreateViewModel, address: SCD, onClick: (Boolean) -> Unit, onSelect: (SCD)->Unit, sggClear:()->Unit, umdClear:()->Unit){

    Column (
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(48.dp)
            .background(Color.Transparent)
            .border(
                color = MaterialTheme.colorScheme.outline,
                width = 1.dp,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                onSelect(address)

                // 시군구, 읍면동 초기화
                sggClear()
                umdClear()

                viewModel.updateUmdList(emptyList())
                viewModel.sggListLoad(address.cdld)

                onClick(false)
            },
        verticalArrangement = Arrangement.Center
    ){

        Text(
            text = address.cdNm,
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            letterSpacing = (-0.6).sp,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun addressItem2(viewModel: UserCreateViewModel, address: SggList, sidoCd: String, onClick: (Boolean) -> Unit, onSelect: (SggList) -> Unit, umdClear: () -> Unit){

    Column (
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(48.dp)
            .background(Color.Transparent)
            .border(
                color = MaterialTheme.colorScheme.outline,
                width = 1.dp,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                onSelect(address)
                umdClear()
                viewModel.umdListLoad(address.sggCd, sidoCd)
                onClick(false)
            },
        verticalArrangement = Arrangement.Center
    ){

        Text(
            text = address.sggNm,
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            letterSpacing = (-0.6).sp,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun addressItem3(viewModel: UserCreateViewModel, address: UmdList, onClick: (Boolean) -> Unit, onSelect: (UmdList)->Unit){

    Column (
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(48.dp)
            .background(Color.Transparent)
            .border(
                color = MaterialTheme.colorScheme.outline,
                width = 1.dp,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                onSelect(address)
                onClick(false)
            },
        verticalArrangement = Arrangement.Center
    ){

        Text(
            text = address.umdNm,
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            letterSpacing = (-0.6).sp,
            modifier = Modifier.padding(start = 16.dp)
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