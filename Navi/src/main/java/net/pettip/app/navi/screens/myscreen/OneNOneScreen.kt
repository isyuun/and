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
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.component.BackTopBar
import net.pettip.app.navi.component.CustomAlertOneBtn
import net.pettip.app.navi.component.CustomTextField
import net.pettip.app.navi.component.LoadingDialog
import net.pettip.app.navi.screens.walkscreen.PhotoItem
import net.pettip.app.navi.screens.walkscreen.PlusBox
import net.pettip.app.navi.ui.theme.design_999EA9
import net.pettip.app.navi.ui.theme.design_button_bg
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_select_btn_text
import net.pettip.app.navi.ui.theme.design_textFieldOutLine
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.CommunityViewModel
import net.pettip.app.navi.viewmodel.SettingViewModel
import net.pettip.app.navi.viewmodel.SharedViewModel
import net.pettip.data.cmm.CdDetail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OneNOneScreen(navController:NavHostController, sharedViewModel: SharedViewModel,settingViewModel: SettingViewModel, communityViewModel: CommunityViewModel){

    val inquiryKindList by settingViewModel.inquiryKindList.collectAsState()
    var expanded by remember{ mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var loadingText by remember { mutableStateOf("문의 업로드중..") }

    val inquiryKind by settingViewModel.inquiryKind.collectAsState()
    val title by settingViewModel.title.collectAsState()
    val inquiryMain by settingViewModel.inquiryMain.collectAsState()
    val isCheck by settingViewModel.isCheck.collectAsState()
    var alertMsg by remember{ mutableStateOf("") }
    var alertShow by remember{ mutableStateOf(false) }

    val scope= rememberCoroutineScope()
    val context = LocalContext.current

    val state = settingViewModel.state
    val dummyUri = Uri.parse("")

    DisposableEffect(Unit){

        onDispose {
            settingViewModel.clearSelectedImagesList()
            settingViewModel.updateTitle("")
            settingViewModel.updateInquiryKind(null)
            settingViewModel.updateInquiryMain("")
            settingViewModel.updateIsCheck(false)
        }
    }

    LaunchedEffect(Unit){
        settingViewModel.clearSelectedImagesList()
        settingViewModel.updateSelectedImageList(listOf(dummyUri))
        settingViewModel.getCmmList("PST")
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
            if (state.listOfSelectedImages.isNotEmpty()){
                settingViewModel.onItemRemove(state.listOfSelectedImages.size-1)
            }
            settingViewModel.updateSelectedImageList(listOfImages = it)
            settingViewModel.updateSelectedImageList(listOf(dummyUri))
        }

    Scaffold (
        topBar = { BackTopBar(title = stringResource(id = R.string.inquiry), navController = navController) }
    ) { paddingValues ->

        LoadingDialog(
            loadingText = loadingText,
            loadingState = isLoading
        )

        if (alertShow){
            CustomAlertOneBtn(
                onDismiss = {alertShow = false},
                confirm = "확인",
                title = alertMsg
            )
        }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.primary)
                .verticalScroll(rememberScrollState())
        ) {
            //Text(text = "이름", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            //    modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = design_login_text
            //)
            //
            //CustomTFInInquiry(
            //    value = name,
            //    onValueChange = {newValue -> settingViewModel.updateName(newValue)},
            //    placeholder = "이름을 입력해주세요",
            //    keyboardType = KeyboardType.Text
            //)

            Text(text = stringResource(id = R.string.inquiry_type), fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
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
                    text = inquiryKind?.cdNm ?: stringResource(id = R.string.inquiry_type_select),
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

            Text(text = stringResource(id = R.string.inquiry_content), fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                modifier=Modifier.padding(start = 20.dp, top = 16.dp), color = MaterialTheme.colorScheme.onPrimary
            )

            CustomTextField(
                value = inquiryMain,
                onValueChange = { settingViewModel.updateInquiryMain(it)},
                singleLine = false,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Default),
                modifier = Modifier
                    .padding(start = 20.dp, top = 8.dp, end = 20.dp)
                    .fillMaxWidth()
                    .heightIn(min = 200.dp, max = 300.dp),
                placeholder = { Text(
                    text = stringResource(id = R.string.place_holder_inquiry_content),
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

            Text(text = stringResource(id = R.string.image_regist_max), fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
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
                        PhotoItem(
                            uri = uri,
                            index = index,
                            onClick = { settingViewModel.onItemRemove(index)},
                            changeMainImage = {
                                if (index > 0 && index < state.listOfSelectedImages.size) {
                                    settingViewModel.updateChangeMainImage(index)
                                }
                            }
                        )
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
                    text = "${stringResource(id = R.string.phone_type)} :${Build.MODEL}",
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
                    checked = isCheck,
                    onCheckedChange = {settingViewModel.updateIsCheck(it)},
                    colors = CheckboxDefaults.colors(
                        checkedColor = design_select_btn_text,
                        uncheckedColor = design_textFieldOutLine,
                        checkmarkColor = design_white),
                    modifier = Modifier.offset(y=(-10).dp)
                )

                Text(text = stringResource(id = R.string.inquiry_information_consent),
                    fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    color = MaterialTheme.colorScheme.onPrimary, modifier=Modifier.offset(x = (-8).dp), letterSpacing = (-0.7).sp
                )
            }

            Button(
                enabled = inquiryKind != null && title.isNotBlank() && inquiryMain.isNotBlank() ,
                onClick = {
                          if (!settingViewModel.isCheck.value){
                              alertMsg = "개인정보 수집 이용에 동의해주세요"
                              alertShow = true
                          }else{
                              scope.launch {
                                  isLoading = true
                                  if (state.listOfSelectedImages.size > 1){
                                      val photoUpload = settingViewModel.fileUpload(context = context)
                                      if(photoUpload){
                                          val qnaUpload = settingViewModel.qnaCreate()
                                          if (qnaUpload){
                                              communityViewModel.updateQnaListInit(true)
                                              loadingText = context.getString(R.string.registration_successful)
                                              delay(1000)
                                              isLoading = false
                                              navController.popBackStack()
                                          }else{
                                              loadingText = context.getString(R.string.registration_failed)
                                              delay(1000)
                                              isLoading = false
                                          }
                                      }else{
                                          loadingText = context.getString(R.string.registration_failed)
                                          delay(1000)
                                          isLoading = false
                                      }
                                  }else{
                                      val qnaUpload = settingViewModel.qnaCreate()
                                      if (qnaUpload){
                                          communityViewModel.updateQnaListInit(true)
                                          loadingText = context.getString(R.string.registration_successful)
                                          delay(1000)
                                          isLoading = false
                                          navController.popBackStack()
                                      }else{
                                          loadingText = context.getString(R.string.registration_failed)
                                          delay(1000)
                                          isLoading = false
                                      }
                                  }
                                  sharedViewModel.deleteTempFilesStartingWithName(context = context)
                              }

                          }
                },
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 20.dp)
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
                Text(text = stringResource(R.string.inquiry_complete), color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
            }
        }//col
    }
}

@Composable
fun InquiryKindListItem(cmmData: CdDetail, viewModel: SettingViewModel, onChange:(Boolean)->Unit){
    Row (
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxWidth()
            .height(48.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(4.dp)
            )
            .clip(RoundedCornerShape(4.dp))
            .clickable {
                viewModel.updateInquiryKind(cmmData)
                onChange(false)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = cmmData.cdNm,
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = 14.sp, letterSpacing = (-0.7).sp,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTFInInquiry(
    value:String,
    onValueChange:(String) -> Unit,
    placeholder:String,
    keyboardType: KeyboardType
){
    CustomTextField(
        value = value,
        onValueChange = {onValueChange(it)},
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Next),
        modifier = Modifier
            .padding(start = 20.dp, top = 8.dp, end = 20.dp)
            .fillMaxWidth()
            .height(48.dp),
        placeholder = { Text(text = placeholder, fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp) },
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
}