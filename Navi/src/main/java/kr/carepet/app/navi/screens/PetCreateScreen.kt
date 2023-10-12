@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class
)

package kr.carepet.app.navi.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kr.carepet.app.navi.R
import kr.carepet.app.navi.Screen
import kr.carepet.app.navi.component.BackTopBar
import kr.carepet.app.navi.component.CustomTextField
import kr.carepet.app.navi.ui.theme.design_btn_border
import kr.carepet.app.navi.ui.theme.design_button_bg
import kr.carepet.app.navi.ui.theme.design_camera_bg
import kr.carepet.app.navi.ui.theme.design_login_text
import kr.carepet.app.navi.ui.theme.design_placeHolder
import kr.carepet.app.navi.ui.theme.design_select_btn_bg
import kr.carepet.app.navi.ui.theme.design_select_btn_text
import kr.carepet.app.navi.ui.theme.design_shadow
import kr.carepet.app.navi.ui.theme.design_skip
import kr.carepet.app.navi.ui.theme.design_textFieldOutLine
import kr.carepet.app.navi.ui.theme.design_white
import kr.carepet.app.navi.viewmodel.UserCreateViewModel
import kr.carepet.data.SCD
import kr.carepet.data.SggList
import kr.carepet.data.UmdList
import kr.carepet.data.pet.PetListData
import kr.carepet.singleton.MySharedPreference
import java.util.Calendar

@Composable
fun PetCreateScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: UserCreateViewModel
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

    val address by viewModel.address.collectAsState()

    Log.d("LOG","petCreate composing")

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
        ){
            Row (horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()){
                Text(text = "건너뛰기",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp, color = design_skip,
                    modifier= Modifier
                        .padding(end = 20.dp)
                        .clickable {
                            if (MySharedPreference.getIsLogin()) {
                                navController.navigate(Screen.MainScreen.route) {
                                    popUpTo(0)
                                }
                            } else {
                                scope.launch {
                                    val userCreateSuccess = viewModel.sendUserToServer()
                                    if (userCreateSuccess) {
                                        val loginSuccess = viewModel.login()
                                        if (loginSuccess) {
                                            navController.navigate(Screen.MainScreen.route) {
                                                popUpTo(0)
                                            }
                                        }
                                    } else {
                                        Toast
                                            .makeText(
                                                context,
                                                viewModel.userResponse.value.detailMessage,
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
                modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = design_login_text
            )

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 8.dp)){

                Button(
                    onClick = { viewModel.updatePetDorC("강아지") },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .shadow(ambientColor = design_shadow, elevation = 0.dp)
                    ,
                    shape = RoundedCornerShape(12.dp),
                    colors = if("강아지" == petDorC) {
                        ButtonDefaults.buttonColors(design_select_btn_bg)
                    } else {
                        ButtonDefaults.buttonColors(design_white)
                    },
                    border = if("강아지" == petDorC) {
                        BorderStroke(1.dp, color = design_select_btn_text)
                    } else {
                        BorderStroke(1.dp, color = design_textFieldOutLine)
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
                        color = if("강아지" == petDorC) design_select_btn_text else design_login_text,
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
                        .shadow(ambientColor = design_shadow, elevation = 0.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = if("고양이" == petDorC) {
                        ButtonDefaults.buttonColors(design_select_btn_bg)
                    } else {
                        ButtonDefaults.buttonColors(design_white)
                    },
                    border = if("고양이" == petDorC) {
                        BorderStroke(1.dp, color = design_select_btn_text)
                    } else {
                        BorderStroke(1.dp, color = design_textFieldOutLine)
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
                        color = if("고양이" == petDorC) design_select_btn_text else design_login_text,
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp
                    )
                }

            }

            // 사이즈 품종 선택
            Text(text = "사이즈/품종 선택", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = design_login_text
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
                colors = ButtonDefaults.buttonColors(design_white),
                border = BorderStroke(1.dp, color = design_btn_border)
            ) {
                Row(modifier=Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                    Text(text = petKind.petNm, color = design_login_text,
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
                    
                    Spacer(modifier = Modifier.weight(1f))
                    
                    Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "", tint = design_login_text)
                }
            }

            // 주소 선택
            Text(text = "주소", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = design_login_text
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
                colors = ButtonDefaults.buttonColors(design_white),
                border = BorderStroke(1.dp, color = design_btn_border)
            ) {
                Row(modifier=Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
                    Text(text = address, color = design_login_text,
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "", tint = design_login_text)
                }
            }

            Text(text = "이름", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = design_login_text
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
                    unfocusedPlaceholderColor = design_placeHolder,
                    focusedPlaceholderColor = design_placeHolder,
                    unfocusedBorderColor = design_textFieldOutLine,
                    focusedBorderColor = design_login_text,
                    unfocusedContainerColor = design_white,
                    focusedContainerColor = design_white,
                    unfocusedLeadingIconColor = design_placeHolder,
                    focusedLeadingIconColor = design_login_text),
                shape = RoundedCornerShape(4.dp),
                innerPadding = PaddingValues(start=16.dp)
            )

            Text(text = "생일", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = design_login_text
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
                        unfocusedPlaceholderColor = design_placeHolder,
                        focusedPlaceholderColor = design_placeHolder,
                        unfocusedBorderColor = design_textFieldOutLine,
                        focusedBorderColor = design_login_text,
                        unfocusedContainerColor = design_white,
                        focusedContainerColor = design_white,
                        unfocusedLeadingIconColor = design_placeHolder,
                        focusedLeadingIconColor = design_login_text),
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
                        color = design_login_text, modifier=Modifier.offset(x = (-8).dp), letterSpacing = (-0.7).sp
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
                                fontSize = 20.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)), color = design_login_text)
                        )
                        Picker(
                            state = monthPickerState,
                            items = month,
                            visibleItemsCount = 3,
                            startIndex = monthIndex,
                            modifier = Modifier.weight(0.2f),
                            textModifier = Modifier.padding(8.dp),
                            textStyle = TextStyle(
                                fontSize = 20.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)), color = design_login_text)
                        )
                        Picker(
                            state = dayPickerState,
                            items = day,
                            visibleItemsCount = 3,
                            startIndex = dayIndex,
                            modifier = Modifier.weight(0.3f),
                            textModifier = Modifier.padding(8.dp),
                            textStyle = TextStyle(
                                fontSize = 20.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)), color = design_login_text)
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
                modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = design_login_text
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
                        unfocusedPlaceholderColor = design_placeHolder,
                        focusedPlaceholderColor = design_placeHolder,
                        unfocusedBorderColor = design_textFieldOutLine,
                        focusedBorderColor = design_login_text,
                        unfocusedContainerColor = design_white,
                        focusedContainerColor = design_white,
                        unfocusedLeadingIconColor = design_placeHolder,
                        focusedLeadingIconColor = design_login_text),
                    shape = RoundedCornerShape(4.dp),
                    innerPadding = PaddingValues(start=16.dp)
                )

                Row (modifier = Modifier.width(100.dp),verticalAlignment = Alignment.CenterVertically){
                    Text(text = "kg", fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        color = design_login_text, modifier=Modifier.padding(start = 14.dp), letterSpacing = (-0.7).sp
                    )
                }


            }

            Text(text = "* 1kg 미만의 경우, 600g = 0.6으로 입력",
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 12.sp, color = design_skip,
                modifier= Modifier
                    .padding(start = 20.dp, top = 8.dp),
                letterSpacing = (-0.6).sp
            )

            Text(text = "성별", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = design_login_text
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
                        ButtonDefaults.buttonColors(design_white)
                    },
                    border = if("남아" == petGender) {
                        BorderStroke(1.dp, color = design_select_btn_text)
                    } else {
                        BorderStroke(1.dp, color = design_textFieldOutLine)
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
                        color = if("남아" == petGender) design_select_btn_text else design_login_text,
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
                        .shadow(ambientColor = design_shadow, elevation = 0.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = if("여아" == petGender) {
                        ButtonDefaults.buttonColors(design_select_btn_bg)
                    } else {
                        ButtonDefaults.buttonColors(design_white)
                    },
                    border = if("여아" == petGender) {
                        BorderStroke(1.dp, color = design_select_btn_text)
                    } else {
                        BorderStroke(1.dp, color = design_textFieldOutLine)
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
                        color = if("여아" == petGender) design_select_btn_text else design_login_text,
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
                        ButtonDefaults.buttonColors(design_white)
                    },
                    border = if("모름" == petGender) {
                        BorderStroke(1.dp, color = design_select_btn_text)
                    } else {
                        BorderStroke(1.dp, color = design_textFieldOutLine)
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
                        color = if("모름" == petGender) design_select_btn_text else design_login_text,
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp
                    )
                }


            }

            Text(text = "중성화", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = design_login_text
            )

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 8.dp)) {
                Button(
                    onClick = { viewModel.updatePetNtr("했어요") },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .shadow(ambientColor = design_shadow, elevation = 0.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = if ("했어요" == petNtr) {
                        ButtonDefaults.buttonColors(design_select_btn_bg)
                    } else {
                        ButtonDefaults.buttonColors(design_white)
                    },
                    border = if ("했어요" == petNtr) {
                        BorderStroke(1.dp, color = design_select_btn_text)
                    } else {
                        BorderStroke(1.dp, color = design_textFieldOutLine)
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
                        color = if ("했어요" == petNtr) design_select_btn_text else design_login_text,
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
                        .shadow(ambientColor = design_shadow, elevation = 0.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = if ("안했어요" == petNtr) {
                        ButtonDefaults.buttonColors(design_select_btn_bg)
                    } else {
                        ButtonDefaults.buttonColors(design_white)
                    },
                    border = if ("안했어요" == petNtr) {
                        BorderStroke(1.dp, color = design_select_btn_text)
                    } else {
                        BorderStroke(1.dp, color = design_textFieldOutLine)
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
                        color = if ("안했어요" == petNtr) design_select_btn_text else design_login_text,
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
                        .shadow(ambientColor = design_shadow, elevation = 0.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = if ("모름" == petNtr) {
                        ButtonDefaults.buttonColors(design_select_btn_bg)
                    } else {
                        ButtonDefaults.buttonColors(design_white)
                    },
                    border = if ("모름" == petNtr) {
                        BorderStroke(1.dp, color = design_select_btn_text)
                    } else {
                        BorderStroke(1.dp, color = design_textFieldOutLine)
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
                        color = if ("모름" == petNtr) design_select_btn_text else design_login_text,
                        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp
                    )
                }
            }

            Button(
                onClick = {
                    scope.launch {

                        if (IntegrityCheck(viewModel, context)){
                            // 로그인 된 상태면 펫 생성만
                            if (MySharedPreference.getIsLogin()){
                                val petCreateSuccess = viewModel.createPet()
                                if(petCreateSuccess){
                                    navController.navigate(Screen.MainScreen.route){
                                        popUpTo(0)
                                    }
                                }else{
                                    Toast.makeText(context, viewModel.myPetResModel.value?.detailMessage ?: "", Toast.LENGTH_SHORT).show()
                                }
                            }else{ // 회원가입시에 하는거면 가입, 로그인, 펫 생성까지
                                val userCreateSuccess = viewModel.sendUserToServer()
                                if (userCreateSuccess){
                                    val loginSuccess = viewModel.login()
                                    if (loginSuccess){
                                        val petCreateSuccess = viewModel.createPet()
                                        if(petCreateSuccess){
                                            navController.navigate(Screen.MainScreen.route){
                                                popUpTo(0)
                                            }
                                        }else{
                                            Toast.makeText(context, viewModel.myPetResModel.value?.detailMessage ?: "", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }else{
                                    Toast.makeText(context, viewModel.userResponse.value.detailMessage, Toast.LENGTH_SHORT).show()
                                }
                            }
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
            .border(shape = CircleShape, border = BorderStroke(5.dp, color = design_white))
            .shadow(elevation = 10.dp, shape = CircleShape, spotColor = design_shadow)
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

@Composable
fun PetKindContent(
    viewModel: UserCreateViewModel,
    navController: NavHostController
){

    LaunchedEffect(Unit){
        viewModel.onKindClick()
    }

    val sDog by viewModel.sDog.collectAsState()
    val searchText by viewModel.searchText.collectAsState()
    val pets by viewModel.pets.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    var expanded by remember { mutableStateOf (false) }

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
        ){
            Column (modifier= Modifier
                .fillMaxSize()
            ){

                Text(text = "견종", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = design_login_text
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
                    placeholder = { Text(text = "견종을 선택해주세요", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedPlaceholderColor = design_placeHolder,
                        focusedPlaceholderColor = design_placeHolder,
                        unfocusedBorderColor = design_textFieldOutLine,
                        focusedBorderColor = design_login_text,
                        unfocusedContainerColor = design_white,
                        focusedContainerColor = design_white,
                        unfocusedLeadingIconColor = design_placeHolder,
                        focusedLeadingIconColor = design_login_text),
                    shape = RoundedCornerShape(12.dp),
                    innerPadding = PaddingValues(start=16.dp),
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "")
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
                            .background(color = design_white)) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 400.dp)
                                .background(color = design_white),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            items(pets){ pets ->
                                petKindItem(viewModel = viewModel, pet = pets, focusRequester = focusRequester)
                            }
                        }
                    }
                }

            }

            Button(
                onClick = { navController.popBackStack() },
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
fun petKindItem(viewModel: UserCreateViewModel, pet: PetListData, focusRequester:FocusRequester){

    val focusManager = LocalFocusManager.current

    Column (
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(48.dp)
            .background(design_white)
            .border(
                color = design_textFieldOutLine,
                width = 1.dp,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                viewModel.onSearchTextChange(pet.petNm)
                viewModel.updatePetKind(pet)
                focusManager.clearFocus()
            },
        verticalArrangement = Arrangement.Center
    ){

        Text(
            text = pet.petNm,
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = 16.sp,
            color = design_login_text,
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

    val scdSelect by viewModel.selectedItem1.collectAsState()
    val sggSelect by viewModel.selectedItem2.collectAsState()
    val umdSelect by viewModel.selectedItem3.collectAsState()

    val isSearching by viewModel.isSearching.collectAsState()
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    var expanded1 by remember { mutableStateOf (false) }
    var expanded2 by remember { mutableStateOf (false) }
    var expanded3 by remember { mutableStateOf (false) }

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
        ){
            Column (modifier= Modifier
                .fillMaxSize()
            ){

                Text(text = "시/도", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = design_login_text
                )

                CustomTextField(
                    value = scdSelect.cdNm,
                    onValueChange = {},
                    singleLine = true,
                    readOnly = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next),
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .padding(start = 20.dp, top = 8.dp, end = 20.dp)
                        .fillMaxWidth()
                        .height(48.dp)
                        .onFocusChanged { focusState ->
                            expanded1 = focusState.isFocused
                        },
                    placeholder = { Text(text = "시/도를 선택해주세요", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedPlaceholderColor = design_placeHolder,
                        focusedPlaceholderColor = design_placeHolder,
                        unfocusedBorderColor = design_textFieldOutLine,
                        focusedBorderColor = design_login_text,
                        unfocusedContainerColor = design_white,
                        focusedContainerColor = design_white,
                        unfocusedLeadingIconColor = design_placeHolder,
                        focusedLeadingIconColor = design_login_text),
                    shape = RoundedCornerShape(12.dp),
                    innerPadding = PaddingValues(start=16.dp),
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "")
                    }
                )

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
                            .background(color = design_white),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        items(scdList){ scd ->
                            addressItem1(viewModel = viewModel, address = scd, focusRequester = focusRequester)
                        }
                    }
                }


                Text(text = "시/군/구", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = design_login_text
                )

                CustomTextField(
                    value = sggSelect.sggNm,
                    onValueChange = {},
                    singleLine = true,
                    readOnly = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next),
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .padding(start = 20.dp, top = 8.dp, end = 20.dp)
                        .fillMaxWidth()
                        .height(48.dp)
                        .onFocusChanged { focusState ->
                            expanded2 = focusState.isFocused
                        },
                    placeholder = { Text(text = "시/군/구를 선택해주세요", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedPlaceholderColor = design_placeHolder,
                        focusedPlaceholderColor = design_placeHolder,
                        unfocusedBorderColor = design_textFieldOutLine,
                        focusedBorderColor = design_login_text,
                        unfocusedContainerColor = design_white,
                        focusedContainerColor = design_white,
                        unfocusedLeadingIconColor = design_placeHolder,
                        focusedLeadingIconColor = design_login_text),
                    shape = RoundedCornerShape(12.dp),
                    innerPadding = PaddingValues(start=16.dp),
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "")
                    }
                )

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
                            .background(color = design_white),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        items(sggList){ sggList ->
                            addressItem2(viewModel = viewModel, address = sggList, focusRequester = focusRequester)
                        }
                    }
                }

                Text(text = "읍/면/동", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = design_login_text
                )

                CustomTextField(
                    value = umdSelect.umdNm,
                    onValueChange = {},
                    singleLine = true,
                    readOnly = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next),
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .padding(start = 20.dp, top = 8.dp, end = 20.dp)
                        .fillMaxWidth()
                        .height(48.dp)
                        .onFocusChanged { focusState ->
                            expanded3 = focusState.isFocused
                        },
                    placeholder = { Text(text = "읍/면/동을 선택해주세요", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedPlaceholderColor = design_placeHolder,
                        focusedPlaceholderColor = design_placeHolder,
                        unfocusedBorderColor = design_textFieldOutLine,
                        focusedBorderColor = design_login_text,
                        unfocusedContainerColor = design_white,
                        focusedContainerColor = design_white,
                        unfocusedLeadingIconColor = design_placeHolder,
                        focusedLeadingIconColor = design_login_text),
                    shape = RoundedCornerShape(12.dp),
                    innerPadding = PaddingValues(start=16.dp),
                    trailingIcon = {
                        Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "")
                    }
                )

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
                            .background(color = design_white),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        items(umdList){ umdList ->
                            addressItem3(viewModel = viewModel, address = umdList, focusRequester = focusRequester)
                        }
                    }
                }
            }

            Button(
                onClick = {
                    viewModel.updateAddress(
                        "${viewModel.selectedItem1.value.cdNm} " +
                                "${viewModel.selectedItem2.value.sggNm} " +
                                "${viewModel.selectedItem3.value.umdNm}"
                    )
                    navController.popBackStack()
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
fun addressItem1(viewModel: UserCreateViewModel, address: SCD, focusRequester:FocusRequester){

    val focusManager = LocalFocusManager.current

    Column (
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(48.dp)
            .background(design_white)
            .border(
                color = design_textFieldOutLine,
                width = 1.dp,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                viewModel.updateSelectedItem1(address)

                // 시군구, 읍면동 초기화
                viewModel.updateSelectedItem2(SggList("", ""))
                viewModel.updateSelectedItem3(UmdList("", ""))
                viewModel.updateUmdList(emptyList())

                viewModel.sggListLoad(address.cdld)
                focusManager.clearFocus()
            },
        verticalArrangement = Arrangement.Center
    ){

        Text(
            text = address.cdNm,
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = 16.sp,
            color = design_login_text,
            letterSpacing = (-0.6).sp,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun addressItem2(viewModel: UserCreateViewModel, address: SggList, focusRequester:FocusRequester){

    val focusManager = LocalFocusManager.current

    Column (
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(48.dp)
            .background(design_white)
            .border(
                color = design_textFieldOutLine,
                width = 1.dp,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                viewModel.updateSelectedItem2(address)
                viewModel.updateSelectedItem3(UmdList("", ""))
                viewModel.umdListLoad(address.sggCd)
                focusManager.clearFocus()
            },
        verticalArrangement = Arrangement.Center
    ){

        Text(
            text = address.sggNm,
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = 16.sp,
            color = design_login_text,
            letterSpacing = (-0.6).sp,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun addressItem3(viewModel: UserCreateViewModel, address: UmdList, focusRequester:FocusRequester){

    val focusManager = LocalFocusManager.current

    Column (
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(48.dp)
            .background(design_white)
            .border(
                color = design_textFieldOutLine,
                width = 1.dp,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                viewModel.updateSelectedItem3(address)
                focusManager.clearFocus()
            },
        verticalArrangement = Arrangement.Center
    ){

        Text(
            text = address.umdNm,
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = 16.sp,
            color = design_login_text,
            letterSpacing = (-0.6).sp,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Picker(
    items: List<String>,
    state: kr.carepet.app.navi.viewmodel.PickerState,
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PetScreenPreview(){
    val navController = rememberNavController()

    val scdLocalData = kr.carepet.data.SCDLocalData()
    val userCreateViewModel = UserCreateViewModel(scdLocalData)
    PetCreateScreen(navController = navController, viewModel = userCreateViewModel)
}

fun IntegrityCheck(viewModel: UserCreateViewModel,context: Context):Boolean{
    if (viewModel.petKind.value.petNm=="사이즈/품종 선택"){
        Toast.makeText(context, "펫 종류를 선택해주세요", Toast.LENGTH_SHORT).show()
        return false
    }else if(viewModel.address.value=="주소 선택"){
        Toast.makeText(context, "주소를 입력해주세요", Toast.LENGTH_SHORT).show()
        return false
    }else if(viewModel.petName.value==""){
        Toast.makeText(context, "이름을 입력해주세요", Toast.LENGTH_SHORT).show()
        return false
    }else if (viewModel.petBirth.value=="" && !viewModel.petBirthUnknown.value){
        Toast.makeText(context, "생일을 입력해주세요", Toast.LENGTH_SHORT).show()
        return false
    }else if(viewModel.petWght.value==""){
        Toast.makeText(context, "몸무게를 입력해주세요", Toast.LENGTH_SHORT).show()
        return false
    }else{
        return true
    }
}