@file:OptIn(ExperimentalMaterial3Api::class)

package kr.carepet.app.navi.screens.walkscreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import kr.carepet.app.navi.R
import kr.carepet.app.navi.component.CircleImageTopBar
import kr.carepet.app.navi.component.CustomTextField
import kr.carepet.app.navi.component.LoadingDialog
import kr.carepet.app.navi.screens.mainscreen.getFormattedDate
import kr.carepet.app.navi.ui.theme.design_DDDDDD
import kr.carepet.app.navi.ui.theme.design_alpha50_black
import kr.carepet.app.navi.ui.theme.design_button_bg
import kr.carepet.app.navi.ui.theme.design_icon_5E6D7B
import kr.carepet.app.navi.ui.theme.design_icon_bg
import kr.carepet.app.navi.ui.theme.design_intro_bg
import kr.carepet.app.navi.ui.theme.design_login_bg
import kr.carepet.app.navi.ui.theme.design_login_text
import kr.carepet.app.navi.ui.theme.design_placeHolder
import kr.carepet.app.navi.ui.theme.design_select_btn_bg
import kr.carepet.app.navi.ui.theme.design_select_btn_text
import kr.carepet.app.navi.ui.theme.design_skip
import kr.carepet.app.navi.ui.theme.design_textFieldOutLine
import kr.carepet.app.navi.ui.theme.design_white
import kr.carepet.app.navi.viewmodel.SharedViewModel
import kr.carepet.app.navi.viewmodel.WalkViewModel
import kr.carepet.data.daily.Pet
import kr.carepet.data.pet.CurrentPetData
import kr.carepet.gps.app.GPSApplication
import kr.carepet.singleton.G
import kr.carepet.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(viewModel: WalkViewModel, navController: NavHostController) {
    val application = GPSApplication.instance

    val tracks = application.tracks
    val file = application.file
    val images = application.images
    val pets = application.pets.toList()

    val walkTitle by viewModel.walkTitle.collectAsState()
    val walkMemo by viewModel.walkMemo.collectAsState()
    val postStory by viewModel.postStory.collectAsState()
    val pet by viewModel.petCount.collectAsState()

    val state = viewModel.state
    val dummyUri = Uri.parse("")

    var showDiagLog by remember { mutableStateOf(false) }

    val snackState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(false) }
    var init by rememberSaveable { mutableStateOf(true) }
    var hashString by remember { mutableStateOf("") }

    AnimatedVisibility(
        visible = showDiagLog,
        enter = scaleIn(),
        exit = scaleOut()
    ) {
        OnDialog(navController = navController, onDismiss = { showDiagLog = false })
    }

    DisposableEffect(Unit){
        onDispose {
            val updatedImageList = state.listOfSelectedImages.toMutableList()
            viewModel.viewModelScope.launch {
                updatedImageList.clear()
            }
        }
    }

    BackHandler {
        showDiagLog = true
    }

    SideEffect {
        G.toPost = false
    }

    LaunchedEffect(key1 = state.listOfSelectedImages) {
        if (state.listOfSelectedImages.size > 6) {
            Toast.makeText(context, "최대 5장까지 가능합니다", Toast.LENGTH_SHORT).show()
        }
    }



    LaunchedEffect(Unit) {
        if (init) {
            val petList: List<Pet> = pets.map { petData ->

                var pee = 0
                var poo = 0
                var mrk = 0

                tracks?.forEach { track ->
                    if (track.no==petData.ownrPetUnqNo){
                        pee += track.pee
                        poo += track.poo
                        mrk += track.mrk
                    }
                }

                Pet(
                    petNm = petData.petNm,
                    ownrPetUnqNo = petData.ownrPetUnqNo,
                    bwlMvmNmtm = poo.toString(),
                    relmIndctNmtm = mrk.toString(),
                    urineNmtm = pee.toString()
                )
            }



            Log.d("Log",petList.toString())
            viewModel.updateSelectedImageList(listOf(dummyUri))
            viewModel.updatePetCount(petList)
            init = false
        }
    }

    val galleryLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.GetMultipleContents()
        ) {
            if (state.listOfSelectedImages.isNotEmpty()) {
                viewModel.onItemRemove(state.listOfSelectedImages.size - 1)
            }
            viewModel.updateSelectedImageList(listOfImages = it)
            viewModel.updateSelectedImageList(listOf(dummyUri))
        }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackState, Modifier) }
    ) { paddingValues ->

        LoadingDialog(
            loadingText = "게시글 업로드중...",
            loadingState = isLoading
        )


        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(color = design_white)
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 20.dp, top = 20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_calendar),
                    contentDescription = "",
                    tint = Color.Unspecified
                )
                Text(
                    text = getFormattedDate(),
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp,
                    letterSpacing = (-0.7).sp,
                    color = design_login_text,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Text(
                text = "즐거운 산책 완료!",
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 24.sp,
                letterSpacing = (-1.2).sp,
                color = design_login_text,
                modifier = Modifier.padding(start = 20.dp, top = 8.dp)
            )

            WalkTimeNDisInPost(application)

            Spacer(modifier = Modifier.padding(top = 16.dp))

            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(color = design_icon_bg)
            )

            Spacer(modifier = Modifier.padding(top = 40.dp))

            BwlMvmNmtmContent(walkViewModel = viewModel, pet, pets)// 배변

            Spacer(modifier = Modifier.padding(top = 40.dp))

            Text(
                text = "산책 사진",
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 20.sp,
                letterSpacing = (-1.0).sp,
                color = design_login_text,
                modifier = Modifier.padding(start = 20.dp)
            )

            Spacer(modifier = Modifier.padding(top = 20.dp))

            LazyRow(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp)
            ) {
                itemsIndexed(state.listOfSelectedImages) { index, uri ->
                    if (index < state.listOfSelectedImages.size - 1 && index < 5) {
                        PhotoItem(
                            uri = uri,
                            index = index,
                            onClick = { viewModel.onItemRemove(index) })
                    } else if (index == state.listOfSelectedImages.size - 1 && index < 5) {
                        PlusBox(galleryLauncher)
                    }
                }
            }

            Spacer(modifier = Modifier.padding(top = 40.dp))

            Text(
                text = "제목",
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 20.sp,
                letterSpacing = (-1.0).sp,
                color = design_login_text,
                modifier = Modifier.padding(start = 20.dp)
            )

            CustomTextField(
                value = walkTitle,
                onValueChange = { viewModel.updateWalkTitle(it) },
                singleLine = false,
                maxLines = 3,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 16.dp)
                    .fillMaxWidth()
                    .heightIn(min = 40.dp),
                placeholder = {
                    Text(
                        text = "제목을 입력해주세요",
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 14.sp
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedPlaceholderColor = design_placeHolder,
                    focusedPlaceholderColor = design_placeHolder,
                    unfocusedBorderColor = design_textFieldOutLine,
                    focusedBorderColor = design_login_text,
                    unfocusedContainerColor = design_white,
                    focusedContainerColor = design_white,
                    unfocusedLeadingIconColor = design_placeHolder,
                    focusedLeadingIconColor = design_login_text
                ),
                shape = RoundedCornerShape(4.dp),
                innerPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            )

            Spacer(modifier = Modifier.padding(top = 20.dp))

            Text(
                text = "산책 메모",
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 20.sp,
                letterSpacing = (-1.0).sp,
                color = design_login_text,
                modifier = Modifier.padding(start = 20.dp)
            )

            CustomTextField(
                value = walkMemo,
                onValueChange = { viewModel.updateWalkMemo(it) },
                singleLine = false,
                maxLines = 10,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 16.dp)
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                placeholder = {
                    Text(
                        text = "오늘 산책 중 재미있던 일을 기록 해 주세요.",
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 14.sp
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedPlaceholderColor = design_placeHolder,
                    focusedPlaceholderColor = design_placeHolder,
                    unfocusedBorderColor = design_textFieldOutLine,
                    focusedBorderColor = design_login_text,
                    unfocusedContainerColor = design_white,
                    focusedContainerColor = design_white,
                    unfocusedLeadingIconColor = design_placeHolder,
                    focusedLeadingIconColor = design_login_text
                ),
                shape = RoundedCornerShape(4.dp),
                innerPadding = PaddingValues(16.dp)
            )

            Spacer(modifier = Modifier.padding(top = 20.dp))

            Text(
                text = "해시 태그",
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 20.sp,
                letterSpacing = (-1.0).sp,
                color = design_login_text,
                modifier = Modifier.padding(start = 20.dp)
            )

            CustomTextField(
                value = hashString,
                onValueChange = { hashString = it },
                singleLine = false,
                maxLines = 3,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 16.dp)
                    .fillMaxWidth()
                    .heightIn(min = 40.dp),
                placeholder = {
                    Text(
                        text = "해쉬태그를 남겨주세요. ex) #댕댕이",
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 14.sp
                    )
                },
                visualTransformation = HashTagTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedPlaceholderColor = design_placeHolder,
                    focusedPlaceholderColor = design_placeHolder,
                    unfocusedBorderColor = design_textFieldOutLine,
                    focusedBorderColor = design_login_text,
                    unfocusedContainerColor = design_white,
                    focusedContainerColor = design_white,
                    unfocusedLeadingIconColor = design_placeHolder,
                    focusedLeadingIconColor = design_login_text
                ),
                shape = RoundedCornerShape(4.dp),
                innerPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            )

            Spacer(modifier = Modifier.padding(top = 40.dp))

            Text(
                text = "오늘 산책 스토리에 올리기",
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 20.sp,
                letterSpacing = (-1.0).sp,
                color = design_login_text,
                modifier = Modifier.padding(start = 20.dp)
            )

            Row(
                modifier = Modifier
                    .padding(start = 20.dp)
                    .fillMaxWidth()
                    .offset(x = (-10).dp)
                    .clickable { viewModel.updatePostStory(!postStory) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = postStory,
                    onCheckedChange = { viewModel.updatePostStory(it) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = design_select_btn_text,
                        uncheckedColor = design_textFieldOutLine,
                        checkmarkColor = design_white
                    )
                )

                Text(
                    text = "산책기록과 사진이 스토리에 공유됩니다.",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    color = design_login_text,
                    modifier = Modifier.offset(x = (-8).dp),
                    letterSpacing = (-0.7).sp
                )
            }

            Spacer(modifier = Modifier.padding(top = 40.dp))

            Button(
                onClick = {
                    //share(context, state.listOfSelectedImages[0], "shareText")
                    viewModel.updateSchCdList(listOf("001"))

                    scope.launch {
                        isLoading = true
                        Log.d("GPX",file?.path ?: "")

                        val pattern = "#(\\S+)".toRegex() // 정규 표현식 패턴: # 다음에 공백이 아닌 문자 또는 숫자들
                        val matches = pattern.findAll(hashString)
                        val hashtagList = matches.map { it.groupValues[1] }.toList()

                        viewModel.updateHashTag(hashtagList)

                        if(state.listOfSelectedImages.size<=1 && file == null){
                            var dailyUpload = viewModel.uploadDaily()
                            if (dailyUpload){
                                navController.popBackStack()
                                isLoading = false
                            }else{
                                isLoading = false
                                snackState.showSnackbar(
                                    message = "일상생활 등록에 실패했습니다. 다시 시도해주세요",
                                    actionLabel = "확인",
                                    duration = SnackbarDuration.Short,
                                    withDismissAction = false
                                )
                            }
                        }else{
                            val photoUpload = viewModel.fileUpload(context = context, gpxFile = file)
                            if (photoUpload) {
                                var dailyUpload = viewModel.uploadDaily()
                                if (dailyUpload){
                                    navController.popBackStack()
                                    isLoading = false
                                }else{
                                    isLoading = false
                                    snackState.showSnackbar(
                                        message = "일상생활 등록에 실패했습니다. 다시 시도해주세요",
                                        actionLabel = "확인",
                                        duration = SnackbarDuration.Short,
                                        withDismissAction = false
                                    )
                                }
                            } else {
                                isLoading = false
                                snackState.showSnackbar(
                                    message = "사진전송에 실패했습니다. 다시 시도해주세요",
                                    actionLabel = "확인",
                                    duration = SnackbarDuration.Short,
                                    withDismissAction = false
                                )
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = design_button_bg)
            )
            {
                Text(
                    text = "산책 완료",
                    color = design_white,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular))
                )
            }

            Spacer(modifier = Modifier.padding(top = 20.dp))
        }// col
    }
}

@Composable
fun BwlMvmNmtmContent(walkViewModel: WalkViewModel, pet: List<Pet>, selectPet : List<CurrentPetData>) {

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "같이 산책한 아이들",
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            fontSize = 20.sp,
            letterSpacing = (-1.0).sp,
            color = design_login_text
        )
        Spacer(modifier = Modifier.padding(top = 16.dp))

        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier.heightIn(max = 300.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = pet) { petInfo ->
                BwlMvmNmtmContentItem(walkViewModel = walkViewModel, petInfo, selectPet)
            }
        }

    }
}

@Composable
fun BwlMvmNmtmContentItem(walkViewModel: WalkViewModel, petInfo: Pet, selectPet : List<CurrentPetData>) {

    var bwlCount by remember { mutableIntStateOf(petInfo.bwlMvmNmtm.toInt()) }
    var peeCount by remember { mutableIntStateOf(petInfo.urineNmtm.toInt()) }
    var markCount by remember { mutableIntStateOf(petInfo.relmIndctNmtm.toInt()) }

    val matchingSelectPet = selectPet.find { it.ownrPetUnqNo == petInfo.ownrPetUnqNo }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = design_white, shape = RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = design_textFieldOutLine,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.padding(start = 22.dp))

            CircleImageTopBar(size = 50, imageUri = matchingSelectPet?.petRprsImgAddr)

            Text(
                text = petInfo.petNm,
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                fontSize = 16.sp,
                letterSpacing = (-0.8).sp,
                color = design_login_text,
                modifier = Modifier.padding(start = 10.dp, top = 22.dp, bottom = 22.dp)
            )
        }

        Column(
            modifier = Modifier.align(Alignment.CenterEnd),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.padding(top = 4.dp))
            PlusMinusItem(
                walkViewModel = walkViewModel,
                s = "대변",
                icon = R.drawable.icon_poop, bwlCount
            ) { newValue -> bwlCount += newValue }

            PlusMinusItem(
                walkViewModel = walkViewModel,
                s = "소변",
                icon = R.drawable.icon_pee,
                peeCount
            ) { newValue -> peeCount += newValue }

            PlusMinusItem(
                walkViewModel = walkViewModel,
                s = "마킹",
                icon = R.drawable.icon_marking,
                markCount
            ) { newValue -> markCount += newValue }

            Spacer(modifier = Modifier.padding(top = 4.dp))
        }

    }

}

@Composable
fun PlusMinusItem(
    walkViewModel: WalkViewModel,
    s: String,
    icon: Int,
    count: Int,
    onClick: (Int) -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "",
            tint = Color.Unspecified
        )

        Text(
            text = s,
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = 14.sp,
            letterSpacing = (-0.7).sp,
            color = design_skip,
            modifier = Modifier.padding(start = 4.dp, end = 8.dp)
        )

        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    color =
                    if (count == 0) {
                        design_DDDDDD
                    } else {
                        design_intro_bg
                    },
                    shape = RoundedCornerShape(8.dp)
                )
                .clip(RoundedCornerShape(8.dp))
                .clickable(enabled = count != 0) { onClick(-1) },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.stepper_minus),
                contentDescription = "",
                tint = Color.Unspecified
            )
        }

        Box(
            modifier = Modifier.width(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = count.toString(),
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                fontSize = 14.sp,
                letterSpacing = (-0.7).sp,
                color = design_login_text
            )
        }

        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    color = design_intro_bg,
                    shape = RoundedCornerShape(8.dp)
                )
                .clip(RoundedCornerShape(8.dp))
                .clickable { onClick(1) },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.stepper_plus),
                contentDescription = "",
                tint = Color.Unspecified
            )
        }


        Spacer(modifier = Modifier.padding(end = 20.dp))
    }
}

@Composable
fun PhotoItem(uri: Uri, index: Int, onClick: () -> Unit) {

    Box(
        modifier = Modifier.size(105.dp)
    ) {

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(shape = RoundedCornerShape(12.dp))
                .align(Alignment.Center)
        ) {
            AsyncImage(
                model = uri,
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )



            if (index == 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .background(
                            color = design_alpha50_black,
                            shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                        )
                        .clip(shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                        .align(Alignment.BottomCenter)
                ) {
                    Text(
                        text = "대표이미지",
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.6).sp,
                        lineHeight = 12.sp,
                        modifier = Modifier.align(Alignment.Center),
                        color = design_white
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(color = design_white, shape = CircleShape)
                .border(1.dp, color = design_icon_5E6D7B, CircleShape)
                .clickable { onClick() }
                .align(Alignment.TopEnd)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.picture_delete), contentDescription = "",
                modifier = Modifier.align(Alignment.Center), tint = Color.Unspecified
            )
        }
    }

}

@Composable
fun PlusBox(galleyLauncher: ManagedActivityResultLauncher<String, List<@JvmSuppressWildcards Uri>>) {

    Box(modifier = Modifier.size(105.dp)) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(100.dp)
                .background(color = design_select_btn_bg, shape = RoundedCornerShape(12.dp))
                .clip(shape = RoundedCornerShape(12.dp))
                .border(1.dp, color = design_textFieldOutLine, shape = RoundedCornerShape(12.dp))
                .clickable { galleyLauncher.launch("image/*") }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.picture_plus),
                contentDescription = "",
                modifier = Modifier.align(
                    Alignment.Center
                ),
                tint = Color.Unspecified
            )
        }
    }
}


@Preview
@Composable
fun PostScreenPreView() {

    val sharedViewModel = SharedViewModel()
    val viewModel = WalkViewModel(sharedViewModel)

    //PostScreen(viewModel)
}

private fun share(context: Context, imageUri: Uri, shareText: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, imageUri)
        //putExtra(Intent.EXTRA_TEXT, shareText)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        type = "image/*"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)

    context.startActivity(shareIntent)
}

@Composable
fun OnDialog(onDismiss: () -> Unit, navController: NavHostController) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(
                onClick = {
                    onDismiss()
                    navController.popBackStack()
                },
            ) {
                Text(text = "나가기")
            }
        },
        title = { Text(text = "포스트 작성을 그만하시겠어요?") },
        text = { Text(text = "작성중인 글은 삭제됩니다", color = design_skip) },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text(text = "더 작성할래요")
            }
        },
        containerColor = design_white
    )
}

@Composable
fun WalkTimeNDisInPost(tracks: GPSApplication) {

    val distance by remember { mutableStateOf(tracks._distance) }
    val duration by remember { mutableStateOf(tracks.duration) }



    Row (modifier = Modifier
        .padding(top = 16.dp, start = 20.dp, end = 20.dp)
        .fillMaxWidth()
        .clip(shape = RoundedCornerShape(20.dp))
        .background(color = design_login_bg, shape = RoundedCornerShape(20.dp))
        , horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
    ){
        Column (
            modifier= Modifier
                .padding(start = 24.dp, top = 16.dp, bottom = 16.dp)
                .weight(1f)
        ){
            Text(
                text = "산책시간",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                letterSpacing = (-0.7).sp,
                color = design_skip
            )

            Text(
                text = duration.toString(),
                fontSize = 22.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                letterSpacing = 0.sp,
                modifier = Modifier.padding(top = 4.dp),
                color = design_login_text
            )
        }

        Spacer(modifier = Modifier
            .size(1.dp, 46.dp)
            .background(color = design_textFieldOutLine))

        Column (
            modifier= Modifier
                .padding(start = 24.dp, top = 16.dp, bottom = 16.dp)
                .weight(1f)
        ){

            Text(
                text = "산책거리",
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                letterSpacing = (-0.7).sp,
                color = design_skip
            )

            Row (modifier= Modifier.fillMaxWidth()){
                Text(
                    text = String.format("%.2f", distance?.div(1000) ?: 0),
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    letterSpacing = 0.sp,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .alignByBaseline(),
                    color = design_login_text
                )
                Text(
                    text = "km",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    letterSpacing = 0.sp,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .alignByBaseline(),
                    color = design_login_text
                )
            }

        }
    }
}

class HashTagTransformation(): VisualTransformation{
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            buildAnnotatedStringWithColors(text.toString()),
            OffsetMapping.Identity)
    }
}

fun buildAnnotatedStringWithColors(text:String): AnnotatedString{

    val pattern = "#\\S+".toRegex() // 정규 표현식 패턴: # 다음에 공백이 아닌 문자 또는 숫자들
    val matches = pattern.findAll(text)

    val annotatedText = buildAnnotatedString {
        var lastIndex = 0
        val highlightedTextList = mutableListOf<String>()

        matches.forEach { result ->
            val hashtag = result.value
            val startIndex = result.range.first
            val endIndex = result.range.last + 1

            // Add the text before the hashtag
            withStyle(style = SpanStyle(color = design_login_text)) {
                append(text.substring(lastIndex, startIndex))
            }

            // Add the hashtag with a different color
            withStyle(style = SpanStyle(color = design_intro_bg)) {
                append(hashtag)
            }

            highlightedTextList.add(hashtag)

            lastIndex = endIndex
        }

        // Add the text after the last hashtag
        withStyle(style = SpanStyle(color = design_login_text)) {
            append(text.substring(lastIndex))
        }
    }

    return annotatedText
}