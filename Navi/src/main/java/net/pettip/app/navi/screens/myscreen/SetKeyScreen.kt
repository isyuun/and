package net.pettip.app.navi.screens.myscreen

import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.InternalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.component.BackTopBar
import net.pettip.app.navi.component.CustomAlertOneBtn
import net.pettip.app.navi.component.Toasty
import net.pettip.app.navi.ui.theme.design_CBE8F3
import net.pettip.app.navi.ui.theme.design_button_bg
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.SettingViewModel
import net.pettip.app.navi.viewmodel.SharedViewModel
import net.pettip.singleton.MySharedPreference
import net.pettip.util.Log

@Composable
fun SetKeyScreen(navController: NavHostController, settingViewModel: SettingViewModel, sharedViewModel: SharedViewModel){

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val dm by settingViewModel.detailMessage.collectAsState()
    val otp by settingViewModel.otpValue.collectAsState()

    var alertMsg by remember{ mutableStateOf("") }
    var alertShow by remember{ mutableStateOf(false) }
    var sendCode by remember{ mutableStateOf(false) }

    var registerSuccess by remember{ mutableStateOf(false) }

    val snackState = remember{SnackbarHostState()}

    DisposableEffect(Unit){
        onDispose {
            settingViewModel.updateDetailMessage()
        }
    }

    LaunchedEffect(key1 = dm){
        if (!dm.isNullOrEmpty()){
            alertMsg = dm?:""
            alertShow = true
            settingViewModel.updateDetailMessage()
        }
    }

    LaunchedEffect(key1 = sendCode){
        if (sendCode){
            settingViewModel.viewModelScope.launch {
                val result = settingViewModel.setInviteCode()
                if (result){
                    MySharedPreference.setLastInviteCode(otp)
                    settingViewModel.updateCurrentPetInfo()
                    settingViewModel.updatePetInfo()
                    registerSuccess = true
                    //navController.popBackStack()
                }else{
                    sendCode = false
                    registerSuccess = false
                }
            }
        }
    }

    Scaffold (
        topBar = { BackTopBar(title = stringResource(R.string.invite_code_register), navController = navController) },
        snackbarHost = { Toasty(snackState = snackState) }
    ) { paddingValues ->
        if (alertShow){
            CustomAlertOneBtn(
                onDismiss = {
                    alertShow = false
                    if (registerSuccess){
                        navController.popBackStack()
                    }
                            },
                confirm = "확인",
                title = alertMsg
            )
        }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = stringResource(R.string.invite_code_enter),
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 20.sp, letterSpacing = (-1.0).sp,
                lineHeight = 20.sp, color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(start = 20.dp, top = 20.dp)
            )

            Text(
                text = stringResource(R.string.invite_code_guide),
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 14.sp, letterSpacing = (-0.7).sp,
                lineHeight = 14.sp, color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(start = 20.dp, top = 16.dp)
            )

            Spacer(modifier = Modifier.padding(top = 40.dp))

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.onPrimaryContainer),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Spacer(modifier = Modifier.padding(top = 20.dp))

                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(color = design_CBE8F3, shape = CircleShape)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ){
                    Icon(painter = painterResource(id = R.drawable.icon_registration),
                        contentDescription = "", tint = Color.Unspecified)
                }

                Spacer(modifier = Modifier.padding(top = 20.dp))

                SetKeyTemp(
                    settingViewModel = settingViewModel,
                    sharedViewModel = sharedViewModel,
                    onSendCode = {newValue -> sendCode = newValue},
                    snackState = snackState
                )

                Spacer(modifier = Modifier.padding(top = 40.dp))

                Button(
                    onClick = {
                        settingViewModel.viewModelScope.launch {
                            val result = settingViewModel.setInviteCode()
                            if (result){
                                MySharedPreference.setLastInviteCode(otp)
                                settingViewModel.updateCurrentPetInfo()
                                settingViewModel.updatePetInfo()
                                registerSuccess = true
                                //navController.popBackStack()
                            }else{
                                registerSuccess = false
                            }
                        }

                    },
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = design_button_bg)
                )
                {
                    Text(text = stringResource(id = R.string.transfer_code),
                        color = design_white, fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        letterSpacing = (-0.7).sp
                    )
                }

                Spacer(modifier = Modifier.padding(top = 40.dp))

            }// 내부 col

        }
    }
}

@OptIn(ExperimentalFoundationApi::class, InternalTextApi::class)
@Composable
fun SetKeyTemp(settingViewModel: SettingViewModel, sharedViewModel: SharedViewModel, onSendCode: (Boolean) -> Unit, snackState: SnackbarHostState){

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    val inviCode by sharedViewModel.inviteCode.collectAsState()
    val otpValue by settingViewModel.otpValue.collectAsState()
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    val focusManager = LocalFocusManager.current
    val inputService = LocalTextInputService.current

    LaunchedEffect(Unit){
        focusRequester.requestFocus()
        settingViewModel.updateOtpValue("")

        if (inviCode != null){
            settingViewModel.updateOtpValue(inviCode?:"")
            sharedViewModel.updateInviteCode(null)
        }
    }

    BasicTextField(
        value = otpValue,
        onValueChange = {
            if (it.length <= 6) {
                settingViewModel.updateOtpValue(it)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {onSendCode(true)}
        ),
        decorationBox = {
            Row (
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                repeat(6){ index ->
                    val char = when {
                        index >= otpValue.length -> ""
                        else -> otpValue[index].toString()
                    }
                    val isFocused = otpValue.length == index

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp)
                            .border(
                                if (isFocused) 2.dp else 1.dp,
                                if (isFocused) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outline,
                                RoundedCornerShape(8.dp)
                            )
                            .background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(8.dp))
                            .combinedClickable(
                                onClick = {
                                    focusRequester.requestFocus()
                                    inputService?.startInput()
                                },
                                onLongClick = {
                                    val clipData = clipboardManager.primaryClip

                                    if (clipData != null && clipData.itemCount > 0) {
                                        val clipboardText = clipData.getItemAt(0).text.toString()

                                        if (clipboardText.length == 6) {
                                            settingViewModel.updateOtpValue(clipboardText)
                                        } else {
                                            focusManager.clearFocus()
                                            scope.launch {
                                                snackState.showSnackbar("복사된 초대 코드가 없습니다")
                                            }
                                        }
                                    }
                                },
                                onLongClickLabel = ""
                            ),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = char.uppercase(),
                            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                            fontSize = 24.sp, color = MaterialTheme.colorScheme.onPrimary,
                            lineHeight = 24.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.padding(horizontal = 2.dp))
                }
            }
        },
        modifier = Modifier
            .padding(start = 40.dp, end = 40.dp)
            .focusRequester(focusRequester)
    )

}