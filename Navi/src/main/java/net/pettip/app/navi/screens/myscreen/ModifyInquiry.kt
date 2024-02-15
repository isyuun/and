package net.pettip.app.navi.screens.myscreen

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.component.CustomTextField
import net.pettip.app.navi.component.LoadingDialog
import net.pettip.app.navi.screens.commuscreen.PhotoItemInModify
import net.pettip.app.navi.screens.walkscreen.PlusBox
import net.pettip.app.navi.ui.theme.design_999EA9
import net.pettip.app.navi.ui.theme.design_button_bg
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_select_btn_text
import net.pettip.app.navi.ui.theme.design_textFieldOutLine
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.CommunityViewModel
import net.pettip.app.navi.viewmodel.SettingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyInquiryScreen(
    navController: NavHostController,
    viewModel: CommunityViewModel,
    settingViewModel: SettingViewModel,
    onDismiss: (Boolean) -> Unit
){
    val modifiedQna by viewModel.qnaDetail.collectAsState()

    var expanded by remember{ mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var loadingText by remember { mutableStateOf("문의 업로드중..") }

    var alertMsg by remember{ mutableStateOf("") }
    var alertShow by remember{ mutableStateOf(false) }

    val inquiryKindList by settingViewModel.inquiryKindList.collectAsState()
    val inquiryKind by settingViewModel.inquiryKind.collectAsState()
    val title by settingViewModel.title.collectAsState()
    val inquiryMain by settingViewModel.inquiryMain.collectAsState()
    val isCheck by settingViewModel.isCheck.collectAsState()

    val scope= rememberCoroutineScope()
    val context = LocalContext.current
    val snackState = remember { SnackbarHostState() }

    val dummyUri = Uri.parse("")
    val state = settingViewModel.state
    val galleryLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.GetMultipleContents()
        ) {
            if (state.listOfSelectedImages.isNotEmpty()){
                settingViewModel.onItemRemove(state.listOfSelectedImages.size-1)
            }
            settingViewModel.updateSelectedImageList(listOfImages = it)
            settingViewModel.updateSelectedImageList(listOf(dummyUri))
        }

    DisposableEffect(Unit){

        onDispose {
            settingViewModel.clearNewFileList()
            settingViewModel.updateTitle("")
            settingViewModel.updateInquiryKind(null)
            settingViewModel.updateInquiryMain("")
            settingViewModel.updateIsCheck(false)
            settingViewModel.clearSelectedImages()
        }
    }

    LaunchedEffect(key1 = inquiryKindList){
        val cdItem = inquiryKindList?.data?.get(0)?.cdDetailList?.filter { it.cdId == modifiedQna?.data?.get(0)?.pstSeCd }
        settingViewModel.updateInquiryKind(cdItem?.get(0))
    }

    LaunchedEffect(Unit){
        settingViewModel.getCmmList("PST")
        settingViewModel.updateTitle(modifiedQna?.data?.get(0)?.pstTtl ?: "")
        settingViewModel.updateInquiryMain(modifiedQna?.data?.get(0)?.pstCn ?: "")
        settingViewModel.updateIsCheck(true)
        if (modifiedQna?.data?.get(0)?.files?.isNotEmpty() == true){
            val atchPath = modifiedQna?.data?.get(0)?.atchPath
            val uriList: List<Uri>? = modifiedQna?.data?.get(0)?.files?.map {
                Uri.parse("$atchPath${it.filePathNm}${it.atchFileNm}")
            }

            if (uriList != null) {
                settingViewModel.updateSelectedImageList(uriList)
            }
        }
        settingViewModel.updateSelectedImageList(listOf(dummyUri))
        settingViewModel.updateUploadedFileList(modifiedQna?.data?.get(0)?.files)
    }

    Scaffold (
        snackbarHost = { SnackbarHost(hostState = snackState, Modifier) },
        modifier = Modifier.fillMaxSize(),
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
                                    onDismiss(false)
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
                            text = stringResource(R.string.edit_inquiry),
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
    ){paddingValues ->

        LoadingDialog(
            loadingText = loadingText,
            loadingState = isLoading
        )

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.primary)
                .verticalScroll(rememberScrollState())
        ) {

            Text(text = stringResource(R.string.inquiry_type), fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
            )

            Row (
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 8.dp)
                    .fillMaxWidth()
                    .height(48.dp)
                    .border(
                        width = 1.dp,
                        color = if (expanded) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clip(RoundedCornerShape(4.dp))
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = inquiryKind?.cdNm ?: stringResource(R.string.inquiry_type_select),
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp, letterSpacing = (-0.7).sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(start = 16.dp)
                )

                Icon(
                    imageVector = if(expanded){
                        Icons.Default.KeyboardArrowUp
                    }else{
                        Icons.Default.KeyboardArrowDown
                    },
                    contentDescription = "", tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(end = 16.dp))
            }


            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                LazyColumn(
                    state = rememberLazyListState(),
                    modifier = Modifier.heightIn(max = 300.dp)
                ){
                    items(items = inquiryKindList?.data?.get(0)?.cdDetailList?: emptyList()){ item ->
                        InquiryKindListItem(cmmData = item, viewModel = settingViewModel) { newValue -> expanded = newValue }
                    }
                }
            }

            Text(text = stringResource(id = R.string.title), fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
            )

            CustomTFInInquiry(
                value = title,
                onValueChange = {newValue -> settingViewModel.updateTitle(newValue)},
                placeholder = stringResource(id = R.string.place_holder_title),
                keyboardType = KeyboardType.Text
            )

            Text(text = stringResource(R.string.inquiry_content), fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
            )

            CustomTextField(
                value = inquiryMain,
                onValueChange = { settingViewModel.updateInquiryMain(it)},
                singleLine = false,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next),
                modifier = Modifier
                    .padding(start = 20.dp, top = 8.dp, end = 20.dp)
                    .fillMaxWidth()
                    .heightIn(min = 200.dp, max = 300.dp),
                placeholder = { Text(
                    text = stringResource(R.string.place_holder_inquiry_content),
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp) },
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


            Text(text = stringResource(R.string.image_regist_max), fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.padding(top = 20.dp))

            LazyRow(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp)
            ) {
                itemsIndexed(state.listOfSelectedImages){index, uri ->
                    if (index<state.listOfSelectedImages.size-1 && index<5){
                        PhotoItemInModify(uri = uri, index = index, onClick = { settingViewModel.onItemRemove(index)}, onDelete = { settingViewModel.subUploadedFileList(uri) } )
                    }else if(index==state.listOfSelectedImages.size-1 && index<5){
                        PlusBox (galleryLauncher)
                    }
                }
            }

            Row (
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 20.dp)
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(color = MaterialTheme.colorScheme.onSurfaceVariant),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "${stringResource(id = R.string.phone_type)} : ${Build.MODEL}",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp, letterSpacing = (-0.7).sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(start = 20.dp)
                )

                Spacer(modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(width = 2.dp, height = 8.dp)
                    .background(MaterialTheme.colorScheme.secondary)
                )
                Text(
                    text = "OS : ${Build.VERSION.RELEASE}",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp, letterSpacing = (-0.7).sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(width = 2.dp, height = 8.dp)
                    .background(MaterialTheme.colorScheme.secondary)
                )

                Text(
                    text = "AppVersion : ",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp, letterSpacing = (-0.7).sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )

            }

            Row (modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
            ){
                Checkbox(
                    enabled = false,
                    checked = isCheck,
                    onCheckedChange = {settingViewModel.updateIsCheck(it)},
                    colors = CheckboxDefaults.colors(
                        checkedColor = design_select_btn_text,
                        uncheckedColor = design_textFieldOutLine,
                        checkmarkColor = design_white),
                    modifier = Modifier.offset(y=(-10).dp)
                )

                Text(text = stringResource(R.string.inquiry_information_consent),
                    fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    color = MaterialTheme.colorScheme.onPrimary, modifier=Modifier.offset(x = (-8).dp), letterSpacing = (-0.7).sp
                )
            }

            Button(
                enabled = inquiryKind != null && title.isNotBlank() && inquiryMain.isNotBlank(),
                onClick = {
                    val localUriList = state.listOfSelectedImages.filter { uri ->
                        uri.scheme != "http" && uri.scheme != "https"
                    }

                    //if (settingViewModel.inquiryKind.value ==null){
                    //    Toast.makeText(context, R.string.select_inquiry_kind, Toast.LENGTH_SHORT).show()
                    //}else if(settingViewModel.title.value == ""){
                    //    Toast.makeText(context, R.string.place_holder_title, Toast.LENGTH_SHORT).show()
                    //}else if(settingViewModel.inquiryMain.value ==""){
                    //    Toast.makeText(context, R.string.enter_memo, Toast.LENGTH_SHORT).show()
                    //}else{
                    //
                    //}

                    scope.launch {
                        isLoading = true
                        if(localUriList.size<=1){
                            val result = settingViewModel.updateQna(modifiedQna!!,viewModel)
                            if (result){
                                isLoading = false
                                onDismiss(false)
                            }else{
                                isLoading=false
                                snackState.showSnackbar(
                                    message = context.getString(R.string.inquiry_modify_fail_retry),
                                    actionLabel = context.getString(R.string.confirm),
                                    duration = SnackbarDuration.Short,
                                    withDismissAction = false
                                )
                            }
                        }else{
                            val photoUpload = settingViewModel.fileUploadModify(context)
                            if(photoUpload){
                                val result = settingViewModel.updateQna(modifiedQna!!, viewModel)
                                if (result){
                                    isLoading = false
                                    onDismiss(false)
                                }else{
                                    isLoading = false
                                    snackState.showSnackbar(
                                        message = context.getString(R.string.inquiry_modify_fail_retry),
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
                },
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 20.dp)
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = design_button_bg,
                    disabledContainerColor = design_999EA9
                )
            ) {
                Text(text = stringResource(R.string.edit_inquiry_), color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
            }
        }
    }
}
