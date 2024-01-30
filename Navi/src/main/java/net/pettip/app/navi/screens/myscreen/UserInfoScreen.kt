package net.pettip.app.navi.screens.myscreen

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import net.pettip.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.Screen
import net.pettip.app.navi.component.BackTopBar
import net.pettip.app.navi.component.CustomTextField
import net.pettip.app.navi.component.ErrorScreen
import net.pettip.app.navi.screens.isAlphaNumeric
import net.pettip.app.navi.ui.theme.design_button_bg
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_login_text
import net.pettip.app.navi.ui.theme.design_placeHolder
import net.pettip.app.navi.ui.theme.design_sharp
import net.pettip.app.navi.ui.theme.design_skip
import net.pettip.app.navi.ui.theme.design_textFieldOutLine
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.SettingViewModel
import net.pettip.singleton.G
import net.pettip.singleton.MySharedPreference

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoScreen(navController:NavHostController, settingViewModel: SettingViewModel) {

    val userPw by settingViewModel.userPw.collectAsState()
    val userPwCheck by settingViewModel.userPwCheck.collectAsState()
    var nickName by remember { mutableStateOf(G.userNickName) }
    val passedNick by settingViewModel.userNickNamePass.collectAsState()

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }
    var showDialog by remember { mutableStateOf(false) }
    var withDraw by remember { mutableStateOf(false) }
    val loginMethod = MySharedPreference.getLastLoginMethod()

    var pushYnError by remember { mutableStateOf(false) }
    var refresh by remember { mutableStateOf(true) }

    val defaultCheck by settingViewModel.pushUseYn.collectAsState()
    val marketingCheck by settingViewModel.pushAdUseYn.collectAsState()
    val dawnCheck by settingViewModel.pushMdnghtUseYn.collectAsState()

    LaunchedEffect(key1 = refresh) {
        if (refresh) {
            val result = settingViewModel.getPushYn()
            pushYnError = !result
            refresh = false
        }
    }

    LaunchedEffect(key1 = withDraw) {
        if (withDraw) {
            scope.launch {
                val result = settingViewModel.withdraw()
                if (result) {

                    G.userEmail = ""
                    G.userId = ""
                    G.refreshToken = ""
                    G.accessToken = ""

                    MySharedPreference.setUserId("")
                    MySharedPreference.setRefreshToken("")
                    MySharedPreference.setAccessToken("")
                    MySharedPreference.setLastLoginMethod("")
                    MySharedPreference.setIsLogin(false)

                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.withdraw_success),
                        actionLabel = context.getString(R.string.confirm),
                        duration = SnackbarDuration.Short
                    )

                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }


                } else {
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.withdraw_fail),
                        actionLabel = context.getString(R.string.confirm),
                        duration = SnackbarDuration.Short
                    )
                }
                withDraw = false
            }
        }
    }

    Scaffold(
        topBar = { BackTopBar(title = stringResource(R.string.userinfo_modify), navController = navController) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->

        if (showDialog) {
            CustomDialogDelete(
                onDismiss = { newValue -> showDialog = newValue },
                confirm = stringResource(R.string.withdraw),
                dismiss = stringResource(R.string.cancel_kor),
                title = stringResource(R.string.member_withdraw),
                text = stringResource(R.string.member_withdraw_confirm),
                valueChange = { newValue -> withDraw = newValue }
            )
        }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Spacer(modifier = Modifier.padding(top = 20.dp))

            Row(Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.nickname), fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    modifier = Modifier.padding(start = 20.dp), color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = "*",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    color = design_sharp
                )
            }
            CustomTextField(
                value = nickName,
                onValueChange = { nickName = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .padding(start = 20.dp, top = 8.dp, end = 20.dp)
                    .fillMaxWidth()
                    .height(48.dp),
                placeholder = { Text(text = stringResource(R.string.place_holder_nickname), fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp) },
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
                innerPadding = PaddingValues(start = 16.dp),
                trailingIcon = {
                    AnimatedVisibility(
                        visible = nickName.isNotEmpty(),
                        enter = scaleIn(),
                        exit = scaleOut()
                    ) {
                        Button(
                            onClick = {
                                scope.launch {
                                    val result = settingViewModel.nickNameCheck(nickName)
                                    if (result) {
                                        settingViewModel.updateUserNickNamePass(nickName)
                                        focusManager.clearFocus()
                                        snackbarHostState.showSnackbar(
                                            message = context.getString(R.string.available_nickname),
                                            actionLabel = context.getString(R.string.confirm),
                                            duration = SnackbarDuration.Short,
                                            withDismissAction = true
                                        )
                                    } else {
                                        focusManager.clearFocus()
                                        snackbarHostState.showSnackbar(
                                            message = context.getString(R.string.already_use_nickname),
                                            actionLabel = context.getString(R.string.confirm),
                                            duration = SnackbarDuration.Short,
                                            withDismissAction = true
                                        )
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 0.dp),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.onPrimary),
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(60.dp, 32.dp),
                            contentPadding = PaddingValues(horizontal = 0.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.duplicate_check),
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                fontSize = 12.sp, letterSpacing = (-0.6).sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            )

            Spacer(
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 30.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.outline)
            )

            Row(Modifier.fillMaxWidth()) {
                Text(
                    text = "알림 설정", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    modifier = Modifier.padding(start = 20.dp), color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = "*",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    color = design_sharp
                )
            }

            Crossfade(
                targetState = pushYnError,
                label = ""
            ) {
                if (it) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "알림 설정을 불러오지 못했습니다.\n다시 시도해주세요",
                            fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                            modifier = Modifier.padding(start = 20.dp), color = MaterialTheme.colorScheme.onPrimary
                        )

                        Box(
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(design_intro_bg)
                                .clickable { refresh = true }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_reflesh),
                                contentDescription = "", tint = design_white,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(20.dp)
                            )
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "",
                                modifier = Modifier
                                    .padding(start = 20.dp)
                                    .size(14.dp),
                                tint = design_skip.copy(alpha = 0.7f)
                            )

                            Text(
                                text = "마케팅 및 이벤트 알림",
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "동의",
                                        fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                        modifier = Modifier.padding(start = 30.dp),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )

                                    Text(
                                        text = "푸시를 이용해서 마케팅 및 이벤트 정보를 알려드려요",
                                        fontSize = 12.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                        modifier = Modifier.padding(start = 30.dp), letterSpacing = (-0.6).sp,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }

                            Switch(
                                checked = marketingCheck,
                                onCheckedChange = { newValue ->
                                    settingViewModel.updatePushAdUseYn(newValue)
                                    settingViewModel.viewModelScope.launch {
                                        val result = settingViewModel.setPushYn()
                                        if (result) {
                                            if (marketingCheck) {
                                                snackbarHostState.showSnackbar(
                                                    message = "마케팅 및 이벤트 알림 동의",
                                                    actionLabel = context.getString(R.string.confirm),
                                                    duration = SnackbarDuration.Short
                                                )
                                            } else {
                                                snackbarHostState.showSnackbar(
                                                    message = "마케팅 및 이벤트 알림 거절",
                                                    actionLabel = context.getString(R.string.confirm),
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                        } else {
                                            settingViewModel.updatePushAdUseYn(!newValue)
                                            snackbarHostState.showSnackbar(
                                                message = "변경에 실패했습니다. 다시 시도해주세요",
                                                actionLabel = context.getString(R.string.confirm),
                                                duration = SnackbarDuration.Short
                                            )
                                        }
                                    }
                                },
                                modifier = Modifier.padding(end = 30.dp),
                                thumbContent = if (marketingCheck) {
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(SwitchDefaults.IconSize),
                                        )
                                    }
                                } else {
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.Close,
                                            contentDescription = null,
                                            modifier = Modifier.size(SwitchDefaults.IconSize),
                                        )
                                    }
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = design_white,
                                    checkedTrackColor = design_intro_bg,
                                    checkedBorderColor = design_intro_bg,
                                    checkedIconColor = design_login_text,
                                    uncheckedThumbColor = design_skip,
                                    uncheckedTrackColor = design_textFieldOutLine,
                                    uncheckedBorderColor = design_skip,
                                    uncheckedIconColor = design_white,
                                )
                            )
                        }

                        Spacer(
                            modifier = Modifier
                                .padding(horizontal = 30.dp, vertical = 8.dp)
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(MaterialTheme.colorScheme.outline)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "",
                                modifier = Modifier
                                    .padding(start = 20.dp)
                                    .size(14.dp),
                                tint = design_skip.copy(alpha = 0.7f)
                            )

                            Text(
                                text = "기본 알림 설정",
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "푸시 허용",
                                        fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                        modifier = Modifier.padding(start = 30.dp),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )

                                    Text(
                                        text = "우리 아이의 새로운 산책 정보, 댓글 등을 알려드려요.",
                                        fontSize = 12.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                        modifier = Modifier.padding(start = 30.dp), letterSpacing = (-0.6).sp,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }

                            Switch(
                                checked = defaultCheck,
                                onCheckedChange = { newValue ->
                                    settingViewModel.updatePushUseYn(newValue)
                                    settingViewModel.viewModelScope.launch {
                                        val result = settingViewModel.setPushYn()
                                        if (result) {
                                            if (defaultCheck) {
                                                snackbarHostState.showSnackbar(
                                                    message = "푸시 허용",
                                                    actionLabel = context.getString(R.string.confirm),
                                                    duration = SnackbarDuration.Short
                                                )
                                            } else {
                                                snackbarHostState.showSnackbar(
                                                    message = "푸시 거절",
                                                    actionLabel = context.getString(R.string.confirm),
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                        } else {
                                            settingViewModel.updatePushUseYn(!newValue)
                                            snackbarHostState.showSnackbar(
                                                message = "변경에 실패했습니다. 다시 시도해주세요",
                                                actionLabel = context.getString(R.string.confirm),
                                                duration = SnackbarDuration.Short
                                            )
                                        }
                                    }
                                },
                                modifier = Modifier.padding(end = 30.dp),
                                thumbContent = if (defaultCheck) {
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(SwitchDefaults.IconSize),
                                        )
                                    }
                                } else {
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.Close,
                                            contentDescription = null,
                                            modifier = Modifier.size(SwitchDefaults.IconSize),
                                        )
                                    }
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = design_white,
                                    checkedTrackColor = design_intro_bg,
                                    checkedBorderColor = design_intro_bg,
                                    checkedIconColor = design_login_text,
                                    uncheckedThumbColor = design_skip,
                                    uncheckedTrackColor = design_textFieldOutLine,
                                    uncheckedBorderColor = design_skip,
                                    uncheckedIconColor = design_white,
                                )
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "야간 무음 모드",
                                        fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                        modifier = Modifier.padding(start = 30.dp),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )

                                    Text(
                                        text = "0시부터 아침 8시까지는 알림을 제한합니다.",
                                        fontSize = 12.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                        modifier = Modifier.padding(start = 30.dp), letterSpacing = (-0.6).sp,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }

                            Switch(
                                checked = dawnCheck,
                                onCheckedChange = { newValue ->
                                    settingViewModel.updatePushMdnghtUseYn(newValue)
                                    settingViewModel.viewModelScope.launch {
                                        val result = settingViewModel.setPushYn()
                                        if (result) {
                                            if (dawnCheck) {
                                                snackbarHostState.showSnackbar(
                                                    message = "야간 무음 모드 허용",
                                                    actionLabel = context.getString(R.string.confirm),
                                                    duration = SnackbarDuration.Short
                                                )
                                            } else {
                                                snackbarHostState.showSnackbar(
                                                    message = "야간 무음 모드 거절",
                                                    actionLabel = context.getString(R.string.confirm),
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                        } else {
                                            settingViewModel.updatePushMdnghtUseYn(!newValue)
                                            snackbarHostState.showSnackbar(
                                                message = "변경에 실패했습니다. 다시 시도해주세요",
                                                actionLabel = context.getString(R.string.confirm),
                                                duration = SnackbarDuration.Short
                                            )
                                        }
                                    }
                                },
                                modifier = Modifier.padding(end = 30.dp),
                                thumbContent = if (dawnCheck) {
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(SwitchDefaults.IconSize),
                                        )
                                    }
                                } else {
                                    {
                                        Icon(
                                            imageVector = Icons.Filled.Close,
                                            contentDescription = null,
                                            modifier = Modifier.size(SwitchDefaults.IconSize),
                                        )
                                    }
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = design_white,
                                    checkedTrackColor = design_intro_bg,
                                    checkedBorderColor = design_intro_bg,
                                    checkedIconColor = design_login_text,
                                    uncheckedThumbColor = design_skip,
                                    uncheckedTrackColor = design_textFieldOutLine,
                                    uncheckedBorderColor = design_skip,
                                    uncheckedIconColor = design_white,
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.padding(top = 16.dp))


            //Row (Modifier.fillMaxWidth()){
            //    Text(text = "휴대폰 번호", fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            //        modifier=Modifier.padding(start = 20.dp), color = MaterialTheme.colorScheme.onPrimary)
            //    Text(
            //        text = "*",
            //        fontSize = 16.sp,
            //        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            //        color= design_sharp
            //    )
            //}
            //CustomTextField(
            //    value = userPhoneNum,
            //    onValueChange = {settingViewModel.updateUserPhoneNum(it)},
            //    singleLine = true,
            //    keyboardOptions = KeyboardOptions(
            //        keyboardType = KeyboardType.Text,
            //        imeAction = ImeAction.Next),
            //    modifier = Modifier
            //        .padding(start = 20.dp, top = 8.dp, end = 20.dp)
            //        .fillMaxWidth()
            //        .height(48.dp),
            //    placeholder = { Text(text = "휴대폰 번호를 입력해주세요", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
            //    colors = OutlinedTextFieldDefaults.colors(
            //        unfocusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
            //        focusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
            //        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            //        focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
            //        unfocusedContainerColor = MaterialTheme.colorScheme.primary,
            //        focusedContainerColor = MaterialTheme.colorScheme.primary,
            //        unfocusedLeadingIconColor = MaterialTheme.colorScheme.primaryContainer,
            //        focusedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
            //        cursorColor = design_intro_bg.copy(alpha = 0.5f)
            //    ),
            //    textStyle = TextStyle(
            //        color = MaterialTheme.colorScheme.onPrimary,
            //        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            //        fontSize = 16.sp, letterSpacing = (-0.4).sp
            //    ),
            //    shape = RoundedCornerShape(4.dp),
            //    innerPadding = PaddingValues(start=16.dp)
            //)
            //
            //Spacer(modifier = Modifier.padding(top = 16.dp))

            //Row {
            //    Text(text = stringResource(R.string.password_change), fontSize = 16.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            //        modifier= Modifier
            //            .padding(start = 20.dp)
            //            .alignByBaseline(),
            //        color = MaterialTheme.colorScheme.onPrimary)
            //
            //    if (loginMethod!="EMAIL"){
            //
            //        Text(text = stringResource(R.string.pw_change_easylogin_guide),
            //            fontSize = 12.sp, fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            //            letterSpacing = (-0.6).sp,
            //            modifier= Modifier
            //                .padding(start = 8.dp)
            //                .alignByBaseline(),
            //            color = MaterialTheme.colorScheme.secondary
            //        )
            //    }
            //}
            //
            //CustomTextField(
            //    enabled = loginMethod == "EMAIL",
            //    value = userPw,
            //    onValueChange = {settingViewModel.updateUserPw(it)},
            //    singleLine = true,
            //    keyboardOptions = KeyboardOptions(
            //        keyboardType = KeyboardType.Password,
            //        imeAction = ImeAction.Next),
            //    visualTransformation = PasswordVisualTransformation(),
            //    modifier = Modifier
            //        .padding(start = 20.dp, top = 8.dp, end = 20.dp)
            //        .fillMaxWidth()
            //        .height(48.dp),
            //    placeholder = { Text(text = stringResource(R.string.place_holder_password), fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
            //    colors = OutlinedTextFieldDefaults.colors(
            //        unfocusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
            //        focusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
            //        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            //        focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
            //        unfocusedContainerColor = MaterialTheme.colorScheme.primary,
            //        focusedContainerColor = MaterialTheme.colorScheme.primary,
            //        unfocusedLeadingIconColor = MaterialTheme.colorScheme.primaryContainer,
            //        focusedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
            //        cursorColor = design_intro_bg.copy(alpha = 0.5f)
            //    ),
            //    textStyle = TextStyle(
            //        color = MaterialTheme.colorScheme.onPrimary,
            //        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            //        fontSize = 16.sp, letterSpacing = (-0.4).sp
            //    ),
            //    shape = RoundedCornerShape(4.dp),
            //    innerPadding = PaddingValues(start=16.dp)
            //)
            //
            //CustomTextField(
            //    enabled = loginMethod == "EMAIL",
            //    value = userPwCheck,
            //    onValueChange = {settingViewModel.updateUserPwCheck(it)},
            //    singleLine = true,
            //    keyboardOptions = KeyboardOptions(
            //        keyboardType = KeyboardType.Password,
            //        imeAction = ImeAction.Next),
            //    visualTransformation = PasswordVisualTransformation(),
            //    modifier = Modifier
            //        .padding(start = 20.dp, top = 8.dp, end = 20.dp)
            //        .fillMaxWidth()
            //        .height(48.dp),
            //    placeholder = { Text(text = stringResource(R.string.place_holder_password_confirm), fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
            //    colors = OutlinedTextFieldDefaults.colors(
            //        unfocusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
            //        focusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
            //        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            //        focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
            //        unfocusedContainerColor = MaterialTheme.colorScheme.primary,
            //        focusedContainerColor = MaterialTheme.colorScheme.primary,
            //        unfocusedLeadingIconColor = MaterialTheme.colorScheme.primaryContainer,
            //        focusedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
            //        cursorColor = design_intro_bg.copy(alpha = 0.5f)
            //    ),
            //    textStyle = TextStyle(
            //        color = MaterialTheme.colorScheme.onPrimary,
            //        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            //        fontSize = 16.sp, letterSpacing = (-0.4).sp
            //    ),
            //    shape = RoundedCornerShape(4.dp),
            //    innerPadding = PaddingValues(start=16.dp)
            //)

            Button(
                onClick = {
                    scope.launch {
                        focusManager.clearFocus()
                        if (G.userNickName == nickName) {
                            Log.d("userInfo", "변경안함")
                        } else if (nickName != passedNick) {
                            snackbarHostState.showSnackbar(
                                message = context.getString(R.string.check_duplicate_nickname),
                                actionLabel = context.getString(R.string.confirm),
                                duration = SnackbarDuration.Short
                            )
                        } else {
                            val result = settingViewModel.resetNickName()
                            if (result) {
                                snackbarHostState.showSnackbar(
                                    message = context.getString(R.string.change_nickname),
                                    actionLabel = context.getString(R.string.confirm),
                                    duration = SnackbarDuration.Short
                                )
                            } else {
                                snackbarHostState.showSnackbar(
                                    message = context.getString(R.string.change_nickname_fail),
                                    actionLabel = context.getString(R.string.confirm),
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }

                        if (userPw == "") {
                            Log.d("userInfo", "변경x")
                        } else if (userPw != userPwCheck) {
                            snackbarHostState.showSnackbar(
                                message = "비밀번호가 일치하지 않습니다",
                                actionLabel = "확인",
                                duration = SnackbarDuration.Short
                            )
                        } else if (userPw == userPwCheck) {
                            if (!isAlphaNumeric(userPw)) {
                                snackbarHostState.showSnackbar(
                                    message = "올바른 비밀번호 형식이 아닙니다",
                                    actionLabel = "확인",
                                    duration = SnackbarDuration.Short
                                )
                            } else {
                                val result = settingViewModel.resetPw()
                                if (result) {
                                    snackbarHostState.showSnackbar(
                                        message = "비밀번호가 변경되었습니다",
                                        actionLabel = "확인",
                                        duration = SnackbarDuration.Short
                                    )
                                } else {
                                    snackbarHostState.showSnackbar(
                                        message = "비밀번호변경에 실패했습니다",
                                        actionLabel = "확인",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = design_button_bg)
            )
            {
                Text(
                    text = stringResource(id = R.string.modify_verb),
                    color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    letterSpacing = (-0.7).sp
                )
            }

            Spacer(modifier = Modifier.padding(top = 20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        showDialog = true
                    },
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .height(30.dp)
                        .width(80.dp),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = design_sharp)
                ) {
                    Text(
                        text = stringResource(R.string.member_withdraw_),
                        color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp
                    )
                }
            }

            //Button(
            //    onClick = {
            //        val clipData = ClipData.newPlainText("Text", fcmToken)
            //        clipboardManager.setPrimaryClip(clipData)
            //    },
            //    modifier = Modifier
            //        .padding(start = 20.dp, end = 20.dp, top = 30.dp)
            //        .fillMaxWidth()
            //        .height(48.dp)
            //    ,
            //    contentPadding = PaddingValues(0.dp),
            //    shape = RoundedCornerShape(10.dp),
            //    colors = ButtonDefaults.buttonColors(containerColor = design_sharp)
            //) {
            //    Text(text = "토큰복사",
            //        color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            //        letterSpacing = (-0.7).sp)
            //}
        }//col
    }
}
