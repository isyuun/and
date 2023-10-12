package kr.carepet.app.navi.screens.mainscreen

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import kr.carepet.app.navi.R
import kr.carepet.app.navi.Screen
import kr.carepet.app.navi.component.CircleImageTopBar
import kr.carepet.app.navi.ui.theme.design_999999
import kr.carepet.app.navi.ui.theme.design_DDDDDD
import kr.carepet.app.navi.ui.theme.design_EFECFE
import kr.carepet.app.navi.ui.theme.design_btn_border
import kr.carepet.app.navi.ui.theme.design_button_bg
import kr.carepet.app.navi.ui.theme.design_login_bg
import kr.carepet.app.navi.ui.theme.design_login_text
import kr.carepet.app.navi.ui.theme.design_select_btn_bg
import kr.carepet.app.navi.ui.theme.design_select_btn_text
import kr.carepet.app.navi.ui.theme.design_shadow
import kr.carepet.app.navi.ui.theme.design_skip
import kr.carepet.app.navi.ui.theme.design_textFieldOutLine
import kr.carepet.app.navi.ui.theme.design_white
import kr.carepet.app.navi.viewmodel.SettingViewModel
import kr.carepet.app.navi.viewmodel.SharedViewModel
import kr.carepet.data.pet.PetDetailData
import kr.carepet.singleton.G


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MyScreen(navController: NavHostController, viewModel:SettingViewModel, sharedViewModel: SharedViewModel){

    val petInfo by sharedViewModel.petInfo.collectAsState()

    val scope = rememberCoroutineScope()

    val bottomSheetState =
        androidx.compose.material3.rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val context = LocalContext.current
    val density = LocalDensity.current.density
    val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")

    val navigationBarHeight = if (resourceId > 0) {
        (context.resources.getDimensionPixelSize(resourceId) / density).dp
    } else {
        0.dp
    }

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }

    Scaffold (
    ){ paddingValues ->
        Column (
            modifier= Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(design_white)
                .verticalScroll(rememberScrollState())
        ){
            Row (
                modifier = Modifier
                    .background(color = design_login_bg)
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
                        text = G.userNickName,
                        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                        fontSize = 20.sp, letterSpacing = (-1.0).sp,
                        color = design_login_text, modifier = Modifier.padding(start = 12.dp)
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
                        viewModel.viewModelScope.launch {
                            val result = viewModel.logOut()
                            if (result){
                                navController.navigate(Screen.Login.route){
                                    popUpTo(0)
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
                    Text(text = "로그아웃",
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
                    text = "반려동물 관리",
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    fontSize = 20.sp, letterSpacing = (-1.0).sp,
                    color = design_login_text, modifier = Modifier.padding(start = 20.dp)
                )

                Row {
                    Button(
                        onClick = { openBottomSheet = true },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = design_btn_border
                        ),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(
                            text = "초대하기",
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = 14.sp, letterSpacing = (-0.7).sp,
                            color = design_white,
                            modifier = Modifier.padding(vertical = 7.dp, horizontal = 17.dp)
                        )
                    }

                    Button(
                        onClick = {
                            navController.navigate(Screen.SetKeyScreen.route)
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = design_white
                        ),
                        border = BorderStroke(width = 1.dp, color = design_btn_border),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.padding(end = 20.dp)
                    ) {
                        Text(
                            text = "초대등록",
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = 14.sp, letterSpacing = (-0.7).sp,
                            color = design_login_text,
                            modifier = Modifier.padding(vertical = 7.dp, horizontal = 17.dp)
                        )
                    }
                }

            }

            Spacer(modifier = Modifier.padding(top = 16.dp))

            LazyColumn(
                state = rememberLazyListState(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.heightIn(max = 400.dp)
            ){
                itemsIndexed(petInfo){ index, item ->
                    MyPagePetItem(petDetailData = item,sharedViewModel, navController, index)
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

                    Text(text = "댕냥이 등록하기", color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
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
                            text = "고객센터",
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
                    
                    Text(text = "공지사항, FAQ, 1:1문의 서비스를\n이용하실 수 있습니다.",
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
                        navController = navController
                    ) { newValue -> openBottomSheet = newValue }
                    Spacer(modifier = Modifier
                        .height(navigationBarHeight)
                        .fillMaxWidth()
                        .background(color = design_white))
                }
            }
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
                color = design_textFieldOutLine,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(shape = RoundedCornerShape(12.dp))
            .clickable { navController.navigate("petProfileScreen/${index.toString()}") }
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
                CircleImageTopBar(size = 40, imageUri = petDetailData.petRprsImgAddr)

                Column (
                    modifier = Modifier.padding(start = 10.dp)
                ){
                    Text(
                        text = petDetailData.petNm,
                        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                        fontSize = 16.sp, letterSpacing = (-0.8).sp,
                        color = design_login_text
                    )

                    Text(
                        text = petDetailData.petKindNm,
                        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                        fontSize = 14.sp, letterSpacing = (-0.7).sp,
                        color = design_skip
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
                            "G" -> 0.dp
                            else -> 0.dp
                        },
                        color = design_btn_border,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .background(
                        color =
                        when (petDetailData.mngrType) {
                            "M" -> design_button_bg
                            "I" -> design_white
                            "G" -> design_DDDDDD
                            else -> design_DDDDDD
                        },
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text =
                    when(petDetailData.mngrType){
                        "M" -> "관리중"
                        "I" -> "참여중"
                        "G" -> "동참중단"
                        else -> "에러"
                    },
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 12.sp,
                    letterSpacing = (-0.6).sp,
                    color =
                    when(petDetailData.mngrType){
                        "M" -> design_white
                        "I" -> design_btn_border
                        "G" -> design_999999
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
            .background(design_textFieldOutLine)
        )

        Row (
            modifier = Modifier.padding(start = 20.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = if (petDetailData.petBrthYmd==""){
                    "미상"
                }else{
                    sharedViewModel.changeBirth(petDetailData.petBrthYmd)
                },
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                fontSize = 14.sp, letterSpacing = (-0.7).sp,
                color = design_skip
            )
            
            Spacer(modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(2.dp, 8.dp)
                .background(design_skip)
            )

            Text(
                text = "${petDetailData.wghtVl}kg",
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                fontSize = 14.sp, letterSpacing = (-0.7).sp,
                color = design_skip
            )

            Spacer(modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(2.dp, 8.dp)
                .background(design_skip)
            )

            Text(
                text = petDetailData.sexTypNm,
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                fontSize = 14.sp, letterSpacing = (-0.7).sp,
                color = design_skip,modifier = Modifier.alignByBaseline()
            )
            if (petDetailData.ntrTypCd=="001"){
                Text(
                    text = "(중성화수술 완료)",
                    fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                    fontSize = 12.sp, letterSpacing = (-0.6).sp,
                    color = design_skip, modifier = Modifier.alignByBaseline()
                )
            }
        }
    }//col
}

@Composable
fun MyBottomSheet(
    sharedViewModel: SharedViewModel,
    settingViewModel: SettingViewModel,
    navController: NavHostController,
    openBottomSheet: (Boolean) -> Unit
){

    val petList by sharedViewModel.petInfo.collectAsState()
    val endCheck by settingViewModel.endCheck.collectAsState()
    val selectedPet = settingViewModel.selectedPet
    val selectedPetSave by settingViewModel.selectedPetSave.collectAsState()

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit){
        selectedPet.clear()
    }

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(design_white)
    ) {
        Spacer(modifier = Modifier.padding(top = 20.dp))

        Text(
            text = "누구를 위해 초대를 할까요?",
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            fontSize = 20.sp,
            letterSpacing = (-1.0).sp,
            color = design_login_text,
            modifier = Modifier.padding(start = 20.dp)
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        LazyRow(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            items(petList){ petList ->
                MyBottomSheetItem(viewModel = sharedViewModel, settingViewModel= settingViewModel, petList = petList)
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

            Text(text = "종료기간이 있어요!", fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                color = design_login_text, modifier=Modifier.offset(x = (-8).dp), letterSpacing = (-0.7).sp
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
                        .background(color = design_select_btn_bg),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(painter = painterResource(id = R.drawable.input_calendar),
                        contentDescription = "", tint = Color.Unspecified,
                        modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 16.dp))

                    Text(
                        text = "종료일자",
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 14.sp, letterSpacing = (-0.7).sp,
                        color = design_skip, modifier = Modifier.padding(start = 4.dp)
                    )

                    Text(
                        text = "2023.11.12 13:00",
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 14.sp, letterSpacing = (-0.7).sp,
                        color = design_login_text, modifier = Modifier.padding(start = 12.dp)
                    )


                }
                Spacer(modifier = Modifier.padding(top = 16.dp))
            }
        }



        Button(
            onClick = {
                if (selectedPet.isNotEmpty()){
                    if (settingViewModel.updateSelectedPetSave(selectedPet)) {
                        scope.launch {
                            if (settingViewModel.getInviteCode()){
                                openBottomSheet(false)
                                navController.navigate(Screen.InviteScreen.route)
                        } }
                    }
                }else{
                    Toast.makeText(context, "펫을 선택해주세요", Toast.LENGTH_SHORT).show()
                }
                      },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = design_button_bg)
        ){
            Text(text = "초대하기", color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
        }

        Spacer(modifier = Modifier.padding(top = 20.dp))
    }
}

@Composable
fun MyBottomSheetItem(viewModel: SharedViewModel, settingViewModel: SettingViewModel, petList : PetDetailData){

    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp.dp -60.dp

    val petName:String = petList.petNm
    val imageUri:String = petList.petRprsImgAddr

    val selectedPet = settingViewModel.selectedPet

    var isSeleted by rememberSaveable { mutableStateOf(false) }

    Button(
        onClick = {
            isSeleted= !isSeleted
            if (isSeleted) {
                settingViewModel.selectedPet.add(petList)
            }else{
                settingViewModel.selectedPet.remove(petList)
            }
                  },
        modifier = Modifier
            .size(width = screenWidth / 3, height = screenWidth / 3 - 9.dp)
            .shadow(ambientColor = design_shadow, elevation = 0.dp)
        ,
        shape = RoundedCornerShape(12.dp),
        colors = if(isSeleted) {
            ButtonDefaults.buttonColors(design_select_btn_bg)
        } else {
            ButtonDefaults.buttonColors(design_white)
        },
        border = if(isSeleted) {
            BorderStroke(1.dp, color = design_select_btn_text)
        } else {
            BorderStroke(1.dp, color = design_textFieldOutLine)
        },
        contentPadding = PaddingValues(start = 14.dp,end=14.dp),
        elevation = if(isSeleted){
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
                    .border(shape = CircleShape, border = BorderStroke(3.dp, color = design_white))
                    //.shadow(elevation = 10.dp, shape = CircleShape, spotColor = Color.Gray)
                    .shadow(
                        color = design_shadow,
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
                color = design_login_text
            )
        }
    }
}