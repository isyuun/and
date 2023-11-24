@file:OptIn(ExperimentalMaterial3Api::class)

package net.pettip.app.navi.screens.commuscreen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.component.CustomTextField
import net.pettip.app.navi.component.LoadingDialog
import net.pettip.app.navi.screens.mainscreen.getFormattedDate
import net.pettip.app.navi.screens.mainscreen.shadow
import net.pettip.app.navi.ui.theme.design_alpha50_black
import net.pettip.app.navi.ui.theme.design_button_bg
import net.pettip.app.navi.ui.theme.design_icon_5E6D7B
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_login_text
import net.pettip.app.navi.ui.theme.design_placeHolder
import net.pettip.app.navi.ui.theme.design_select_btn_bg
import net.pettip.app.navi.ui.theme.design_select_btn_text
import net.pettip.app.navi.ui.theme.design_shadow
import net.pettip.app.navi.ui.theme.design_skip
import net.pettip.app.navi.ui.theme.design_textFieldOutLine
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.CommunityViewModel
import net.pettip.app.navi.viewmodel.SharedViewModel
import net.pettip.data.cmm.CdDetail
import net.pettip.data.pet.CurrentPetData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyPostScreen(viewModel: CommunityViewModel, sharedViewModel: SharedViewModel,navController: NavHostController) {

    val walkTitle by viewModel.walkTitle.collectAsState()
    val walkMemo by viewModel.walkMemo.collectAsState()
    val postStory by viewModel.postStory.collectAsState()
    val selectedPet by viewModel.selectPetMulti.collectAsState()
    val selectedCategory by viewModel.selectCategory.collectAsState()
    val schList by viewModel.schList.collectAsState()
    val cdDetailList = if (schList.isNotEmpty()){
        schList[0].cdDetailList.toMutableList().filter { it.cdId != "001" }
    }else{
        emptyList<CdDetail>().toMutableList()
    }
    
    val state = viewModel.state
    val dummyUri = Uri.parse("")
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("Option 1", "Option 2", "Option 3")
    val focusManager = LocalFocusManager.current
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    val scrollState = rememberScrollState()

    var showDiagLog by remember { mutableStateOf(false) }

    val snackState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(false) }
    var hashString by remember { mutableStateOf("") }

    val petList by sharedViewModel.currentPetInfo.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearSelectedImages()
            viewModel.clearSelectPetMulti()
            viewModel.clearSelectCategory()
            viewModel.updateWalkTitle("")
            viewModel.updateWalkMemo("")
            viewModel.updatePostStory(false)
            viewModel.updateHashTag(emptyList())
        }
    }

    BackHandler {
        showDiagLog = true
    }

    LaunchedEffect(Unit){
        viewModel.getSchList()
        viewModel.updateSelectedImageList(listOf(dummyUri))
    }

    LaunchedEffect(key1 = state.listOfSelectedImages) {
        if (state.listOfSelectedImages.size > 6) {
            Toast.makeText(context, "최대 5장까지 가능합니다", Toast.LENGTH_SHORT).show()
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

        if (showDiagLog) {
            CustomDialogInPost(
                onDismiss = { showDiagLog = false},
                navController = navController,
                title =  "글 작성을 그만하시겠습니까?",
                text = "작성중인 글은 삭제됩니다",
                dismiss = "나가기", confirm = "더 작성할래요"
                )
        }


        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
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

            //Text(
            //    text = "일상 생활 작성!",
            //    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            //    fontSize = 24.sp,
            //    letterSpacing = (-1.2).sp,
            //    color = design_login_text,
            //    modifier = Modifier.padding(start = 20.dp, top = 8.dp)
            //)

            Spacer(modifier = Modifier.padding(top = 20.dp))

            Text(
                text = "어느 아이의 글인가요?",
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 20.sp,
                letterSpacing = (-1.0).sp,
                color = design_login_text,
                modifier = Modifier.padding(start = 20.dp)
            )

            LazyRow(
                modifier = Modifier.padding(top = 8.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp)
            ) {
                items(petList){ petList ->
                    Box (modifier = Modifier.padding(horizontal = 4.dp)){
                        SelectedPetMultiItem(viewModel = viewModel, petList = petList)
                    }
                }
            }

            Text(
                text = "첨부 사진",
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 20.sp,
                letterSpacing = (-1.0).sp,
                color = design_login_text,
                modifier = Modifier.padding(start = 20.dp)
            )

            LazyRow(
                modifier = Modifier.padding(top = 8.dp),
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

            Spacer(modifier = Modifier.padding(top = 20.dp))

            Text(
                text = "일상 구분",
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 20.sp,
                letterSpacing = (-1.0).sp,
                color = design_login_text,
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
                        CategoryBox(viewModel = viewModel, item = item)
                    }
                }
            }

            Spacer(modifier = Modifier.padding(top = 20.dp))

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
                        text = "제목을 입력해주세요",
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 14.sp
                    )
                },
                trailingIcon = {
                    if (expanded){
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "", tint = design_login_text,
                            modifier = Modifier.clickable { expanded = false }
                        )
                    } else{
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "", tint = design_login_text,
                            modifier = Modifier.clickable { expanded = true }
                        )
                    }
                } ,
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
                        .background(color = design_white),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ){
                    itemsIndexed(items){ index, item ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.updateWalkTitle(item)
                                    expanded = false
                                },
                            contentAlignment = Alignment.CenterStart
                        ){
                            Text(
                                text = item,
                                color = design_login_text.copy(alpha = 0.8f),
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
                                .background(design_textFieldOutLine))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.padding(top = 20.dp))

            Text(
                text = "메모",
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
                        text = "일상을 기록해주세요.",
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
                text = "오늘 일상 스토리에 올리기",
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
                    text = "사진이 스토리에 공유됩니다.",
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
                    if (selectedPet.size == 0){
                        Toast.makeText(context, "반려동물을 선택해주세요", Toast.LENGTH_SHORT).show()
                    }else if (selectedCategory.size == 0 ){
                        Toast.makeText(context, "일상 구분을 선택해주세요", Toast.LENGTH_SHORT).show()
                    }else{
                        scope.launch {
                            isLoading = true

                            val pattern = "#(\\S+)".toRegex() // 정규 표현식 패턴: # 다음에 공백이 아닌 문자 또는 숫자들
                            val matches = pattern.findAll(hashString)
                            val hashtagList = matches.map { it.groupValues[1] }.toList()

                            viewModel.updateHashTag(hashtagList)

                            if (state.listOfSelectedImages.size <= 1) {
                                var dailyUpload = viewModel.uploadDaily()
                                if (dailyUpload) {
                                    navController.popBackStack()
                                    isLoading = false
                                } else {
                                    isLoading = false
                                    snackState.showSnackbar(
                                        message = "일상생활 등록에 실패했습니다. 다시 시도해주세요",
                                        actionLabel = "확인",
                                        duration = SnackbarDuration.Short,
                                        withDismissAction = false
                                    )
                                }
                            } else {
                                val photoUpload = viewModel.fileUpload(context = context)
                                if (photoUpload) {
                                    val dailyUpload = viewModel.uploadDaily()
                                    if (dailyUpload) {
                                        viewModel.updateStoryRes(null)
                                        viewModel.updateStoryList(emptyList())
                                        viewModel.updateStoryPage(1)
                                        viewModel.updateSelectedImageList(emptyList())
                                        navController.popBackStack()
                                        isLoading = false
                                    } else {
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

class HashTagTransformation() : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            buildAnnotatedStringWithColors(text.toString()),
            OffsetMapping.Identity
        )
    }
}

fun buildAnnotatedStringWithColors(text: String): AnnotatedString {

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

@Composable
fun CustomDialogInPost(
    onDismiss:(Boolean) -> Unit,
    navController: NavHostController,
    confirm: String,
    dismiss : String,
    title : String,
    text : String
){
    AlertDialog(
        onDismissRequest = { onDismiss(false) },
        confirmButton = {
            Button(
                onClick = {
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
        title = { Text(text = title) },
        text = { Text(text = text, color = design_skip) },
        dismissButton = {
            Button(
                onClick = {
                    onDismiss(false)
                    navController.popBackStack()
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = design_white
                ),
                border = BorderStroke(1.dp, color = design_login_text)
            ) {
                Text(
                    text = dismiss,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    color = design_login_text
                )
            }
        },
        containerColor = design_white
    )
}

@Composable
fun SelectedPetMultiItem(viewModel: CommunityViewModel, petList : CurrentPetData){

    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp.dp -60.dp

    val petName:String = petList.petNm
    val imageUri:String? = petList.petRprsImgAddr

    var isSelected by rememberSaveable { mutableStateOf(false) }

    Button(
        onClick = {
            isSelected= !isSelected
            if (isSelected){
                viewModel.addSelectPetMulti(petList)
            }else{
                viewModel.subSelectPetMulti(petList)
            } },
        modifier = Modifier
            .size(width = screenWidth * 0.29f, height = screenWidth * 0.29f - 9.dp)
            .shadow(ambientColor = design_shadow, elevation = 0.dp)
        ,
        shape = RoundedCornerShape(12.dp),
        colors = if(isSelected) {
            ButtonDefaults.buttonColors(design_select_btn_bg)
        } else {
            ButtonDefaults.buttonColors(design_white)
        },
        border = if(isSelected) {
            BorderStroke(1.dp, color = design_select_btn_text)
        } else {
            BorderStroke(1.dp, color = design_textFieldOutLine)
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
                color = design_login_text,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun CategoryBox(viewModel: CommunityViewModel, item: CdDetail){

    var isSelected by rememberSaveable { mutableStateOf(false) }

    Button(
        onClick = {
            isSelected= !isSelected
            if (isSelected){
                viewModel.addSelectCategory(item)
            }else{
                viewModel.subSelectCategory(item)
            } },
        modifier = Modifier
            .shadow(ambientColor = design_shadow, elevation = 0.dp)
        ,
        shape = RoundedCornerShape(12.dp),
        colors = if(isSelected) {
            ButtonDefaults.buttonColors(design_select_btn_bg)
        } else {
            ButtonDefaults.buttonColors(design_white)
        },
        border = if(isSelected) {
            BorderStroke(1.dp, color = design_select_btn_text)
        } else {
            BorderStroke(1.dp, color = design_textFieldOutLine)
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
            color = design_login_text,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }


}
