@file:OptIn(ExperimentalMaterial3Api::class)

package net.pettip.app.navi.screens.walkscreen

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.component.CustomTextField
import net.pettip.app.navi.component.LoadingDialog
import net.pettip.app.navi.component.Toasty
import net.pettip.app.navi.screens.commuscreen.CustomDialogInPost
import net.pettip.app.navi.screens.commuscreen.isAllHashtagsUnder30Characters
import net.pettip.app.navi.screens.mainscreen.getFormattedDate
import net.pettip.app.navi.ui.theme.design_999EA9
import net.pettip.app.navi.ui.theme.design_button_bg
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_select_btn_text
import net.pettip.app.navi.ui.theme.design_textFieldOutLine
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.SharedViewModel
import net.pettip.app.navi.viewmodel.WalkViewModel
import net.pettip.data.daily.Pet
import net.pettip.data.pet.CurrentPetData
import net.pettip.gpx.Track
import net.pettip.map.app.naver.GpxMap
import net.pettip.singleton.G
import net.pettip.singleton.MySharedPreference
import net.pettip.util.Log
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TempPostScreen(viewModel: WalkViewModel, sharedViewModel: SharedViewModel,navController: NavHostController) {
    //val application = GPSApplication.instance

    var expanded by remember { mutableStateOf(false) }
    val items = listOf(
        "댕댕이와 함께하는 행복한 산책",
        "댕댕이와 걷기 완료",
        "댕댕이 응아를 위한 산책"
    )

    val focusManager = LocalFocusManager.current
    val focusRequester by remember { mutableStateOf(FocusRequester()) }

    var tracks by remember{ mutableStateOf<MutableList<Track>>(mutableListOf()) }
    var file by remember{ mutableStateOf<File?>(null) }
    var images by remember{ mutableStateOf<List<Uri>>(emptyList()) }
    var pets by remember{ mutableStateOf<List<CurrentPetData>>(emptyList()) }

    val walkTitle by viewModel.walkTitle.collectAsState()
    val walkMemo by viewModel.walkMemo.collectAsState()
    val postStory by viewModel.postStory.collectAsState()
    val pet by viewModel.petCount.collectAsState()

    val state = viewModel.state
    val dummyUri = Uri.parse("")

    val uploadInfo = MySharedPreference.getTempWalkInfo()

    var showDiagLog by remember { mutableStateOf(false) }

    val snackState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(false) }
    var init by rememberSaveable { mutableStateOf(true) }
    var hashString by remember { mutableStateOf("") }
    var loadingMsg by remember { mutableStateOf("게시글 업로드중..") }

    val scrollState = rememberScrollState()

    if (showDiagLog) {
        CustomDialogInPost(
            onDismiss = { showDiagLog = false },
            navController = navController,
            title = stringResource(id = R.string.daily_dialog_title),
            text = stringResource(id = R.string.daily_dialog_text),
            dismiss = stringResource(id = R.string.daily_dialog_dismiss),
            confirm = stringResource(id = R.string.daily_dialog_confirm)
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            sharedViewModel.setTempWalkDelete(context)
            viewModel.clearSelectedImages()
            G.posting = false
        }
    }

    BackHandler {
        showDiagLog = true
    }

    LaunchedEffect(key1 = state.listOfSelectedImages) {
        if (state.listOfSelectedImages.size > 6) {
            //Toast.makeText(context, R.string.photo_upload_toast_msg, Toast.LENGTH_SHORT).show()
            snackState.showSnackbar("사진은 최대 5장까지 가능합니다")
        }
    }

    LaunchedEffect(key1 = G.trackChange){
        if (G.trackChange){

            val petList: List<Pet> = pets.map { petData ->

                var pee = 0
                var poo = 0
                var mrk = 0

                tracks?.forEach { track ->
                    if (track.no == petData.ownrPetUnqNo) {
                        when (track.event) {
                            Track.EVENT.NNN -> {}
                            Track.EVENT.IMG -> {}
                            Track.EVENT.PEE -> pee++
                            Track.EVENT.POO -> poo++
                            Track.EVENT.MRK -> mrk++
                        }
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

            viewModel.updatePetCount(petList)

            Log.d("PETLIST",petList.toString())

            G.trackChange = false
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // 권한이 허용된 경우 이미지 URI 리스트를 ViewModel에 업데이트
            if (images != null) {
                viewModel.updateSelectedImageList(images)
            }
        } else {
            // 권한이 거부된 경우 처리 로직 추가
            // 사용자에게 권한이 필요하다는 메시지 표시 등
            // 예를 들어, Toast 메시지로 권한 요청 안내
            Toast.makeText(context, "외부 저장소 읽기 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {

        // 바인딩

        if (!G.posting){
            G.posting = true
        }

        val track = uploadInfo?.tracks?.map {
            Track(
                it.location, it.no, it.event, Uri.parse("")
            )
        } as MutableList

        file = uploadInfo?.file?.let { File(it) }
        images = uploadInfo?.uriList?.map { uri ->
            Uri.parse(uri)
        } as List<Uri>
        pets = uploadInfo.pets?: emptyList()
        tracks = track

        Log.d("LOG",images.toString())

        viewModel.updateWalkTitle(uploadInfo.schTtl ?:"")
        viewModel.updateWalkMemo(uploadInfo.schCn ?: "")
        viewModel.updatePostStory(uploadInfo.rlsYn == "Y")

        hashString = uploadInfo.hashTag

        if (init) {
            val petList: List<Pet> = pets.map { petData ->

                var pee = 0
                var poo = 0
                var mrk = 0

                tracks?.forEach { track ->
                    if (track.no == petData.ownrPetUnqNo) {
                        when (track.event) {
                            Track.EVENT.NNN -> {}
                            Track.EVENT.IMG -> {}
                            Track.EVENT.PEE -> pee++
                            Track.EVENT.POO -> poo++
                            Track.EVENT.MRK -> mrk++
                        }
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

            if (images != null) {
                viewModel.updateSelectedImageList(images)
            }

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
            Log.d("URI",it.toString())
            viewModel.updateSelectedImageList(listOfImages = it)
            viewModel.updateSelectedImageList(listOf(dummyUri))
        }

    LaunchedEffect(snackState){
        delay(300)
        snackState.currentSnackbarData?.dismiss()
    }

    Scaffold(
        snackbarHost = { Toasty(snackState = snackState, bottomPadding = 20.dp) }
    ) { paddingValues ->

        LoadingDialog(
            loadingText = loadingMsg,
            loadingState = isLoading
        )


        var isTouch by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(
                    scrollState,
                    enabled = !isTouch,
                )
                .background(color = MaterialTheme.colorScheme.primary)
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
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Text(
                text = stringResource(R.string.phost_screen_top_title),
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 24.sp,
                letterSpacing = (-1.2).sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(start = 20.dp, top = 8.dp)
            )

            WalkTimeNDisInPostInTemp(uploadInfo?.totDstnc, uploadInfo?.totDuration)

            Spacer(modifier = Modifier.padding(top = 16.dp))

            file?.let {
                GpxMap(it) { _, event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> isTouch = true
                        MotionEvent.ACTION_UP -> isTouch = false
                        else -> isTouch = true
                    }
                    //Log.v(__CLASSNAME__, "pointerInteropFilter()${getMethodName()}[isTouch:$isTouch][MotionEvent:$event]")
                    false
                }
            }

            Spacer(modifier = Modifier.padding(top = 40.dp))

            BwlMvmNmtmContent(walkViewModel = viewModel, pet, pets)// 배변

            Spacer(modifier = Modifier.padding(top = 40.dp))

            Text(
                text = stringResource(R.string.walk_image),
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 20.sp,
                letterSpacing = (-1.0).sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(start = 20.dp)
            )

            Spacer(modifier = Modifier.padding(top = 20.dp))

            LazyRow(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp)
            ) {
                itemsIndexed(state.listOfSelectedImages) { index, uri ->
                    if (index < state.listOfSelectedImages.size - 1) {
                        PhotoItem(
                            uri = uri,
                            index = index,
                            onClick = { viewModel.onItemRemove(index) },
                            changeMainImage = {
                                if (index > 0 && index < state.listOfSelectedImages.size) {
                                    viewModel.updateChangeMainImage(index)
                                }
                            }
                        )
                    } else if (index == state.listOfSelectedImages.size - 1) {
                        PlusBox(galleryLauncher)
                    }
                }
            }

            Spacer(modifier = Modifier.padding(top = 40.dp))

            Text(
                text = stringResource(id = R.string.title),
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 20.sp,
                letterSpacing = (-1.0).sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(start = 20.dp)
            )

            CustomTextField(
                value = walkTitle,
                onValueChange = {
                    if (it.length<=40){
                        viewModel.updateWalkTitle(it)
                    }
                                },
                singleLine = false,
                maxLines = 3,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 16.dp)
                    .fillMaxWidth()
                    .heightIn(min = 40.dp, max = 80.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            expanded = focusState.isFocused
                            scope.launch { scrollState.animateScrollTo(scrollState.maxValue, tween(500)) }
                        }
                    },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.place_holder_title),
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 14.sp
                    )
                },
                trailingIcon = {
                    if (expanded) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "", tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.clickable { expanded = false }
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "", tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.clickable { expanded = true }
                        )
                    }
                },
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
                    fontSize = 16.sp, letterSpacing = (-0.4).sp,
                    lineHeight = 20.sp
                ),
                shape = RoundedCornerShape(4.dp),
                innerPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 10.dp)
            )

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically().plus(fadeIn()),
                exit = shrinkVertically().plus(fadeOut())
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .heightIn(max = 150.dp)
                        .background(color = Color.Transparent),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    itemsIndexed(items) { index, item ->
                        Text(
                            text = item,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = 14.sp,
                            letterSpacing = (-0.7).sp,
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .clickable {
                                    viewModel.updateWalkTitle(item)
                                    expanded = false
                                }
                        )

                        if (index < items.size - 1) {
                            Spacer(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(MaterialTheme.colorScheme.onSecondaryContainer)
                            )
                        }
                    }
                }
            }

            //Spacer(modifier = Modifier.padding(top = 20.dp))

            Text(
                text = stringResource(R.string.walk_memo),
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 20.sp,
                letterSpacing = (-1.0).sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(start = 20.dp, top = 20.dp)
            )

            CustomTextField(
                value = walkMemo,
                onValueChange = {
                    if (it.length<=400){
                        viewModel.updateWalkMemo(it)
                    }
                                },
                singleLine = false,
                maxLines = 10,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Default
                ),
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 16.dp)
                    .fillMaxWidth()
                    .heightIn(min = 120.dp, max = 500.dp),
                placeholder = {
                    Text(
                        text = stringResource(R.string.place_holder_walk_memo),
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 14.sp
                    )
                },
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
                innerPadding = PaddingValues(16.dp)
            )

            Spacer(modifier = Modifier.padding(top = 20.dp))

            Text(
                text = stringResource(id = R.string.hashtag),
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 20.sp,
                letterSpacing = (-1.0).sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(start = 20.dp)
            )

            CustomTextField(
                value = hashString,
                onValueChange = {
                    hashString = it
                                },
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
                        text = stringResource(id = R.string.place_holder_hashtag),
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 14.sp
                    )
                },
                visualTransformation = if (isSystemInDarkTheme()) HashTagTransformationForDark() else HashTagTransformation(),
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
                innerPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            )

            Spacer(modifier = Modifier.padding(top = 40.dp))

            Text(
                text = stringResource(R.string.upload_walk_to_story),
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 20.sp,
                letterSpacing = (-1.0).sp,
                color = MaterialTheme.colorScheme.onPrimary,
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
                    text = stringResource(R.string.share_to_story),
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.offset(x = (-8).dp),
                    letterSpacing = (-0.7).sp
                )
            }

            Spacer(modifier = Modifier.padding(top = 40.dp))

            Button(
                enabled = !isLoading && walkTitle.isNotBlank(),
                onClick = {
                    isLoading = true
                    //share(context, state.listOfSelectedImages[0], "shareText")
                    viewModel.updateSchCdList(listOf("001"))

                    val pattern = "#(\\S+)".toRegex() // 정규 표현식 패턴: # 다음에 공백이 아닌 문자 또는 숫자들
                    val matches = pattern.findAll(hashString)
                    val hashtagList = matches.map { it.groupValues[1] }.toList()

                    val isValidHash = isAllHashtagsUnder30Characters(hashtagList)

                    if (isValidHash){
                        scope.launch {

                            delay(500)

                            viewModel.updateHashTag(hashtagList)

                            MySharedPreference.setTempWalkTF(true)

                            if (state.listOfSelectedImages.size <= 1 && file == null) {
                                var dailyUpload = viewModel.uploadDaily(uploadInfo?.walkDptreDt, uploadInfo?.walkEndDt)
                                if (dailyUpload) {
                                    /** 성공 */
                                    //sharedViewModel.setTempWalkDelete(context)// 임시 파일 삭제
                                    MySharedPreference.setTempWalkTF(false)

                                    sharedViewModel.updateWalkUpload(true)
                                    isLoading = false
                                    navController.popBackStack()
                                } else {
                                    /** 실패 */
                                    viewModel.setTempWalk(context, file, hashString, tracks)

                                    isLoading = false
                                    snackState.showSnackbar(
                                        message = context.getString(R.string.daily_create_fail_retry),
                                        actionLabel = context.getString(R.string.confirm),
                                        duration = SnackbarDuration.Short,
                                        withDismissAction = false
                                    )
                                }
                            } else {
                                val photoUpload = viewModel.fileUpload(context = context, gpxFile = file)
                                if (photoUpload) {
                                    var dailyUpload = viewModel.uploadDaily(uploadInfo?.walkDptreDt, uploadInfo?.walkEndDt)
                                    if (dailyUpload) {
                                        /** 성공 */
                                        //sharedViewModel.setTempWalkDelete(context)// 임시 파일 삭제
                                        MySharedPreference.setTempWalkTF(false)

                                        sharedViewModel.updateWalkUpload(true)
                                        viewModel.updateSelectedImageList(emptyList())
                                        uploadInfo?.uriList?.map { Uri.parse(it) }?.let { viewModel.deleteTemporaryFiles(it) }
                                        isLoading = false
                                        navController.popBackStack()
                                    } else {
                                        /** 실패 */
                                        viewModel.setTempWalk(context, file, hashString, tracks)

                                        isLoading = false
                                        snackState.showSnackbar(
                                            message = context.getString(R.string.daily_create_fail_retry),
                                            actionLabel = context.getString(R.string.confirm),
                                            duration = SnackbarDuration.Short,
                                            withDismissAction = false
                                        )
                                    }
                                } else {
                                    /** 실패 */
                                    viewModel.setTempWalk(context, file, hashString, tracks)

                                    isLoading = false
                                    snackState.showSnackbar(
                                        message = context.getString(R.string.upload_photo_fail_retry),
                                        actionLabel = context.getString(R.string.confirm),
                                        duration = SnackbarDuration.Short,
                                        withDismissAction = false
                                    )
                                }
                                sharedViewModel.deleteTempFilesStartingWithName(context = context)
                            }
                        }
                    }else{
                        scope.launch {
                            isLoading = false
                            snackState.showSnackbar("해시 태그는 30자까지 가능해요")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = design_button_bg,
                    disabledContainerColor = design_999EA9
                )
            )
            {
                Text(
                    text = stringResource(id = R.string.walk_complete),
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
fun WalkTimeNDisInPostInTemp(distance : Float? = 0.0f, duration : String? = "") {

    Row(
        modifier = Modifier
            .padding(top = 16.dp, start = 20.dp, end = 20.dp)
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(20.dp))
            .background(
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                shape = RoundedCornerShape(20.dp)
            ), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(start = 24.dp, top = 16.dp, bottom = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = stringResource(id = R.string.walk_time),
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                letterSpacing = (-0.7).sp,
                color = MaterialTheme.colorScheme.secondary
            )

            Text(
                text = duration.toString(),
                fontSize = 22.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                letterSpacing = 0.sp,
                modifier = Modifier.padding(top = 4.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(
            modifier = Modifier
                .size(1.dp, 46.dp)
                .background(color = MaterialTheme.colorScheme.onSecondaryContainer)
        )

        Column(
            modifier = Modifier
                .padding(start = 24.dp, top = 16.dp, bottom = 16.dp)
                .weight(1f)
        ) {

            Text(
                text = stringResource(id = R.string.walk_distance),
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                letterSpacing = (-0.7).sp,
                color = MaterialTheme.colorScheme.secondary
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = String.format("%.2f", distance?.div(1000) ?: 0),
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    letterSpacing = 0.sp,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .alignByBaseline(),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = "km",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    letterSpacing = 0.sp,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .alignByBaseline(),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

        }
    }
}