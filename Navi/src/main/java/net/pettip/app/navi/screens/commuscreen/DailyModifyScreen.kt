@file:OptIn(ExperimentalMaterial3Api::class)

package net.pettip.app.navi.screens.commuscreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.component.CustomAlertOneBtn
import net.pettip.app.navi.component.CustomTextField
import net.pettip.app.navi.component.LoadingDialog
import net.pettip.app.navi.component.Toasty
import net.pettip.app.navi.screens.mainscreen.getFormattedDate
import net.pettip.app.navi.screens.mainscreen.shadow
import net.pettip.app.navi.screens.walkscreen.HashTagTransformation
import net.pettip.app.navi.screens.walkscreen.HashTagTransformationForDark
import net.pettip.app.navi.ui.theme.design_alpha50_black
import net.pettip.app.navi.ui.theme.design_button_bg
import net.pettip.app.navi.ui.theme.design_icon_5E6D7B
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_login_text
import net.pettip.app.navi.ui.theme.design_select_btn_bg
import net.pettip.app.navi.ui.theme.design_select_btn_text
import net.pettip.app.navi.ui.theme.design_shadow
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.CommunityViewModel
import net.pettip.app.navi.viewmodel.SharedViewModel
import net.pettip.data.cmm.CdDetail
import net.pettip.data.daily.DailyLifePet
import net.pettip.data.daily.DailyLifeSchSe
import net.pettip.data.daily.DailyLifeUpdatePet
import net.pettip.data.pet.CurrentPetData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyModifyScreen(viewModel: CommunityViewModel, sharedViewModel: SharedViewModel,navController: NavHostController, onBack:(Boolean)->Unit) {

    val storyDetail by viewModel.storyDetail.collectAsState()

    val uploadedFile by viewModel.uploadedFileList.collectAsState()
    val uploadedPet by viewModel.uploadedPetMulti.collectAsState()
    val newPetList by viewModel.newSelectPet.collectAsState()
    val uploadSchSeList by viewModel.uploadSchSeList.collectAsState()
    val petList by sharedViewModel.currentPetInfo.collectAsState()
    val uploadSchTtl by viewModel.uploadSchTtl.collectAsState()
    val uploadSchCn by viewModel.uploadSchCn.collectAsState()
    val uploadPostStory by viewModel.uploadPostStory.collectAsState()
    val hashTagList = storyDetail?.data?.dailyLifeSchHashTagList
    val hashtagString = hashTagList?.joinToString(separator = " ") { "#${it.hashTagNm}" } ?: ""

    val schList by viewModel.schList.collectAsState()
    val cdDetailList = if (schList.isNotEmpty()){
        schList[0].cdDetailList.toMutableList().filter { it.cdId != "001" }
    }else{
        emptyList<CdDetail>().toMutableList()
    }
    
    val state = viewModel.state
    val dummyUri = Uri.parse("")
    var expanded by remember { mutableStateOf(false) }
    val items = petList.map { "${it.petNm}와 즐거운 산책~!" }
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    val scrollState = rememberScrollState()

    var showDiagLog by remember { mutableStateOf(false) }
    var alertShow by remember{ mutableStateOf(false) }
    var alertMsg by remember{ mutableStateOf("") }

    val snackState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(false) }
    var hashString by remember { mutableStateOf(hashtagString) }

    val uploadedPetList = storyDetail?.data?.dailyLifePetList // 이미 업로드된 펫 목록
    val filteredCurrentPetList = petList.filter { currentPet ->
        uploadedPetList?.none { uploadedPet ->
            uploadedPet.ownrPetUnqNo == currentPet.ownrPetUnqNo
        } ?: false
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearSelectedImages()
            viewModel.clearNewFileList()
            viewModel.clearUploadedPetMulti()
            viewModel.clearNewSelectPet()
            viewModel.clearUploadSchSeList()
            viewModel.clearUploadHashTag()
            viewModel.updateUploadedFileList(null)
            //viewModel.updatePostStory(false)
            //viewModel.updateHashTag(emptyList())
        }
    }

    LaunchedEffect(Unit){
        viewModel.getSchList()
        viewModel.initUploadedPetMulti(storyDetail?.data?.dailyLifePetList?: emptyList())

        if (storyDetail?.data?.dailyLifeFileList?.isNotEmpty()==true){

            val atchPath = storyDetail?.data?.atchPath
            val uriList: List<Uri>? = storyDetail!!.data.dailyLifeFileList?.map {
                Uri.parse("$atchPath${it.filePathNm}${it.atchFileNm}")
            }

            if (uriList != null) {
                viewModel.updateSelectedImageList(uriList)
            }
        }
        viewModel.updateSelectedImageList(listOf(dummyUri))
        viewModel.updateUploadedFileList(storyDetail?.data?.dailyLifeFileList)
        viewModel.initUploadSchSeList(storyDetail?.data?.dailyLifeSchSeList?: emptyList())
        viewModel.updateUploadSchTtl(storyDetail?.data?.schTtl?:"")
        viewModel.updateUploadSchCn(storyDetail?.data?.schCn?:"")
        viewModel.updateUploadPostStory(storyDetail?.data?.rlsYn=="Y")
    }

    LaunchedEffect(key1 = state.listOfSelectedImages) {
        if (state.listOfSelectedImages.size > 6) {
            alertMsg = "사진은 5장까지만 등록이 가능해요\n확인해주세요"
            alertShow = true
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
        snackbarHost = { Toasty(snackState = snackState) }
    ) { paddingValues ->

        LoadingDialog(
            loadingText = stringResource(R.string.daily_uploading),
            loadingState = isLoading
        )

        if (alertShow){
            CustomAlertOneBtn(
                onDismiss = {alertShow = false},
                confirm = "확인",
                title = alertMsg
            )
        }

        if (showDiagLog) {
            CustomDialogInPost(
                onDismiss = { showDiagLog = false},
                navController = navController,
                title = stringResource(R.string.daily_dialog_title),
                text = stringResource(R.string.daily_dialog_text),
                dismiss = stringResource(R.string.daily_dialog_dismiss), confirm = stringResource(R.string.daily_dialog_confirm)
                )
        }


        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
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

            Spacer(modifier = Modifier.padding(top = 20.dp))

            Text(
                text = stringResource(R.string.daily_pet_title),
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 20.sp,
                letterSpacing = (-1.0).sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(start = 20.dp)
            )

            Text(
                text = stringResource(R.string.daily_pet_current),
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 16.sp,
                letterSpacing = (-0.8).sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(start = 20.dp, top = 8.dp)
            )

            LazyRow(
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp)
            ) {
                items(uploadedPetList?: emptyList()){ petList ->
                    Box (modifier = Modifier.padding(horizontal = 4.dp)){
                        AlreadyUploadedPetItem(viewModel = viewModel, petList = petList)
                    }
                }
            }

            Text(
                text = stringResource(R.string.daily_pet_add),
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 16.sp,
                letterSpacing = (-0.8).sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(start = 20.dp)
            )

            LazyRow(
                modifier = Modifier.padding(top = 8.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp)
            ) {
                items(filteredCurrentPetList){ petList ->
                    Box (modifier = Modifier.padding(horizontal = 4.dp)){
                        SelectedPetMultiItemInModify(viewModel = viewModel, petList = petList)
                    }
                }
            }

            Text(
                text = stringResource(R.string.attatched_image),
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 20.sp,
                letterSpacing = (-1.0).sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(start = 20.dp)
            )

            LazyRow(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp)
            ) {
                itemsIndexed(state.listOfSelectedImages) { index, uri ->
                    if (index < state.listOfSelectedImages.size - 1 && index < 5) {
                        PhotoItemInModify(
                            uri = uri,
                            index = index,
                            onClick = { viewModel.onItemRemove(index) },
                            onDelete = { viewModel.subUploadedFileList(uri) }
                        )
                    } else if (index == state.listOfSelectedImages.size - 1 && index < 5) {
                        PlusBox(galleryLauncher)
                    }
                }
            }

            Spacer(modifier = Modifier.padding(top = 20.dp))

            Text(
                text = stringResource(R.string.daily_category),
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 20.sp,
                letterSpacing = (-1.0).sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(start = 20.dp)
            )

            AnimatedVisibility(
                visible = cdDetailList.isNotEmpty(),
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                LazyRow(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp)
                ) {
                    items(cdDetailList){ item ->
                        CategoryBoxInModify(viewModel = viewModel, item = item)
                    }
                }
            }

            Spacer(modifier = Modifier.padding(top = 20.dp))

            Text(
                text = stringResource(R.string.title),
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 20.sp,
                letterSpacing = (-1.0).sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(start = 20.dp)
            )

            CustomTextField(
                value = uploadSchTtl?:"",
                onValueChange = { viewModel.updateUploadSchTtl(it) },
                singleLine = true,
                maxLines = 3,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 16.dp)
                    .imePadding()
                    .fillMaxWidth()
                    .heightIn(min = 40.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            expanded = focusState.isFocused
                            scope.launch {
                                scrollState.animateScrollTo(scrollState.maxValue, tween(500))
                            }
                        }
                    },
                placeholder = {
                    Text(
                        text = stringResource(R.string.place_holder_title),
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 14.sp
                    )
                },
                trailingIcon = {
                    if (expanded){
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "", tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.clickable { expanded = false }
                        )
                    } else{
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "", tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.clickable { expanded = true }
                        )
                    }
                } ,
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
                ){
                    itemsIndexed(items){ index, item ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.updateUploadSchTtl(item)
                                    expanded = false
                                },
                            contentAlignment = Alignment.CenterStart
                        ){
                            Text(
                                text = item,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                fontSize = 14.sp,
                                letterSpacing = (-0.7).sp,
                                modifier = Modifier
                                    .padding(start = 16.dp, top = 3.dp, bottom = 3.dp)
                            )
                        }

                        if (index < items.size-1){
                            Spacer(modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(MaterialTheme.colorScheme.outline))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.padding(top = 20.dp))

            Text(
                text = stringResource(R.string.memo),
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 20.sp,
                letterSpacing = (-1.0).sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(start = 20.dp)
            )

            CustomTextField(
                value = uploadSchCn?:"",
                onValueChange = { viewModel.updateUploadSchCn(it) },
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
                        text = stringResource(R.string.place_holder_daily),
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
                text = stringResource(R.string.hashtag),
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 20.sp,
                letterSpacing = (-1.0).sp,
                color = MaterialTheme.colorScheme.onPrimary,
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
                        text = stringResource(R.string.place_holder_hashtag),
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

            //Text(
            //    text = "오늘 일상 스토리에 올리기",
            //    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            //    fontSize = 20.sp,
            //    letterSpacing = (-1.0).sp,
            //    color = design_login_text,
            //    modifier = Modifier.padding(start = 20.dp)
            //)
            //
            //Row(
            //    modifier = Modifier
            //        .padding(start = 20.dp)
            //        .fillMaxWidth()
            //        .offset(x = (-10).dp)
            //        .clickable { viewModel.updateUploadPostStory(!uploadPostStory) },
            //    verticalAlignment = Alignment.CenterVertically
            //) {
            //    Checkbox(
            //        checked = uploadPostStory,
            //        onCheckedChange = { viewModel.updateUploadPostStory(it) },
            //        colors = CheckboxDefaults.colors(
            //            checkedColor = design_select_btn_text,
            //            uncheckedColor = design_textFieldOutLine,
            //            checkmarkColor = design_white
            //        )
            //    )
            //
            //    Text(
            //        text = "사진이 스토리에 공유됩니다.",
            //        fontSize = 14.sp,
            //        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            //        color = design_login_text,
            //        modifier = Modifier.offset(x = (-8).dp),
            //        letterSpacing = (-0.7).sp
            //    )
            //}

            Spacer(modifier = Modifier.padding(top = 20.dp))

            Button(
                onClick = {
                    val localUriList = state.listOfSelectedImages.filter { uri ->
                        uri.scheme != "http" && uri.scheme != "https"
                    }

                    if (((uploadedPet.count { it.rowState == null })?.plus(newPetList.size))==0){
                        scope.launch { snackState.showSnackbar("반려동물을 선택해주세요") }
                    }else if(uploadSchSeList.count { it.rowState==null }.plus(uploadSchSeList.count{it.rowState == "C"})==0){
                        scope.launch { snackState.showSnackbar("일상구분을 선택해주세요") }
                    }else if(((uploadedFile?.count { it.rowState == null } ?: 0) + (localUriList.size-1)) >5){
                        scope.launch { snackState.showSnackbar("최대 5장까지 업로드 가능합니다") }
                    }else{
                        scope.launch {
                            isLoading = true

                            val pattern = "#(\\S+)".toRegex() // 정규 표현식 패턴: # 다음에 공백이 아닌 문자 또는 숫자들
                            val matches = pattern.findAll(hashString)
                            val hashtagList = matches.map { it.groupValues[1] }.toList()

                            viewModel.updateUploadHashTag(hashtagList)

                            if (localUriList.size <= 1){
                                val result = viewModel.updateDaily()
                                if(result){
                                    isLoading = false
                                    onBack(false)
                                }else{
                                    isLoading = false
                                    snackState.showSnackbar(
                                        message = context.getString(R.string.daily_modify_fail_retry),
                                        actionLabel = context.getString(R.string.confirm),
                                        duration = SnackbarDuration.Short,
                                        withDismissAction = false
                                    )
                                }
                            }else{
                                val photoUpload = viewModel.fileUploadModify(context)
                                if (photoUpload){
                                    val result = viewModel.updateDaily()
                                    if(result){
                                        isLoading = false
                                        onBack(false)
                                    }else{
                                        isLoading = false
                                        snackState.showSnackbar(
                                            message = context.getString(R.string.daily_modify_fail_retry),
                                            actionLabel = context.getString(R.string.confirm),
                                            duration = SnackbarDuration.Short,
                                            withDismissAction = false
                                        )
                                    }
                                }else{
                                    isLoading = false
                                    snackState.showSnackbar(
                                        message = context.getString(R.string.upload_photo_fail_retry),
                                        actionLabel = context.getString(R.string.confirm),
                                        duration = SnackbarDuration.Short,
                                        withDismissAction = false
                                    )
                                }
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
                    text = stringResource(R.string.modify_complete),
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
fun AlreadyUploadedPetItem(viewModel: CommunityViewModel, petList : DailyLifePet){

    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp.dp -60.dp

    val petName:String = petList.petNm
    val imageUri:String? = petList.petImg

    var isSelected by rememberSaveable { mutableStateOf(true) }

    Button(
        onClick = {
            isSelected= !isSelected
            if (isSelected){
                viewModel.addUploadedPetMulti(petList)
            }else{
                viewModel.subUploadedPetMulti(petList)
            } },
        modifier = Modifier
            .size(width = screenWidth * 0.29f, height = screenWidth * 0.29f - 9.dp)
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
                    .border(shape = CircleShape, border = BorderStroke(3.dp, color = MaterialTheme.colorScheme.tertiary))
                    //.shadow(elevation = 10.dp, shape = CircleShape, spotColor = Color.Gray)
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
                color = if (isSelected) design_login_text else MaterialTheme.colorScheme.onPrimary,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun CategoryBoxInModify(viewModel: CommunityViewModel, item: CdDetail){

    val storyDetail by viewModel.storyDetail.collectAsState()
    val schList = storyDetail?.data?.dailyLifeSchSeList
    var isSelected by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = schList){
        if (schList!=null){
            isSelected = schList.any{it.cdId == item.cdId}
        }
    }

    Button(
        onClick = {
            isSelected= !isSelected
            if (isSelected){
                viewModel.addUploadSchSeList(
                    DailyLifeSchSe(
                        cdId = item.cdId,
                        cdNm = item.cdNm,
                        schUnqNo = storyDetail?.data?.schUnqNo ?: 0,
                        rowState = null,
                    )
                )
            }else{
                viewModel.subUploadSchSeList(
                    DailyLifeSchSe(
                        cdId = item.cdId,
                        cdNm = item.cdNm,
                        schUnqNo = storyDetail?.data?.schUnqNo ?: 0,
                        rowState = null,
                    )
                )
            } },
        modifier = Modifier
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
        Text(
            text = item.cdNm,
            fontFamily = FontFamily(Font(R.font.pretendard_medium)),
            fontSize = 16.sp,
            letterSpacing = (-0.8).sp,
            color = if (isSelected) design_login_text else MaterialTheme.colorScheme.onPrimary,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun SelectedPetMultiItemInModify(viewModel: CommunityViewModel, petList : CurrentPetData){

    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp.dp -60.dp

    val petName:String = petList.petNm
    val imageUri:String? = petList.petRprsImgAddr

    var isSelected by rememberSaveable { mutableStateOf(false) }

    Button(
        onClick = {
            isSelected= !isSelected
            if (isSelected){
                viewModel.addNewSelectPet(
                    DailyLifeUpdatePet.SimplePet(
                        ownrPetUnqNo = petList.ownrPetUnqNo,
                        petNm = petList.petNm,
                        rowState = "C"
                    )
                )
            }else{
                viewModel.subNewSelectPet(
                    DailyLifeUpdatePet.SimplePet(
                    ownrPetUnqNo = petList.ownrPetUnqNo,
                    petNm = petList.petNm,
                    rowState = "C"
                ))
            } },
        modifier = Modifier
            .size(width = screenWidth * 0.29f, height = screenWidth * 0.29f - 9.dp)
            .shadow(ambientColor = design_shadow, elevation = 0.dp)
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
                    .border(shape = CircleShape, border = BorderStroke(3.dp, color = MaterialTheme.colorScheme.tertiary))
                    //.shadow(elevation = 10.dp, shape = CircleShape, spotColor = Color.Gray)
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
                color = if (isSelected) design_login_text else MaterialTheme.colorScheme.onPrimary,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun PhotoItemInModify(uri: Uri, index: Int, onClick: () -> Unit, onDelete:()->Unit) {

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
                contentScale = ContentScale.Crop,
                filterQuality = FilterQuality.Low
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
                        text = stringResource(R.string.main_image),
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
                .clickable {
                    onClick()
                    onDelete()
                }
                .align(Alignment.TopEnd)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.picture_delete), contentDescription = "",
                modifier = Modifier.align(Alignment.Center), tint = Color.Unspecified
            )
        }
    }

}
