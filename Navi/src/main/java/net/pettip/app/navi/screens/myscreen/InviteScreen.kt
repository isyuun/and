package net.pettip.app.navi.screens.myscreen

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import net.pettip.app.navi.R
import net.pettip.app.navi.component.BackTopBar
import net.pettip.app.navi.ui.theme.design_FCE9B3
import net.pettip.app.navi.ui.theme.design_button_bg
import net.pettip.app.navi.ui.theme.design_login_bg
import net.pettip.app.navi.ui.theme.design_login_text
import net.pettip.app.navi.ui.theme.design_select_btn_text
import net.pettip.app.navi.ui.theme.design_sharp
import net.pettip.app.navi.ui.theme.design_skip
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.SettingViewModel
import net.pettip.data.pet.PetDetailData
import net.pettip.singleton.G

@Composable
fun InviteScreen(navController: NavHostController, settingViewModel: SettingViewModel){

    val context = LocalContext.current
    val inviteCode by settingViewModel.inviteCode.collectAsState()
    val selectPet by settingViewModel.selectedPetSave.collectAsState()
    val name by settingViewModel.name.collectAsState()
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    Scaffold (
        topBar = { BackTopBar(title = "초대하기", navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(design_white)
        ) {
            Text(
                text = "회원 초대",
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 20.sp, letterSpacing = (-1.0).sp,
                lineHeight = 20.sp, color = design_login_text,
                modifier = Modifier.padding(start = 20.dp, top = 20.dp)
            )

            Text(
                text = "코드를 직접 말해주거나 메시지로 전송하세요.",
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 14.sp, letterSpacing = (-0.7).sp,
                lineHeight = 14.sp, color = design_login_text,
                modifier = Modifier.padding(start = 20.dp, top = 16.dp)
            )

            Spacer(modifier = Modifier.padding(top = 40.dp))
            
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = design_login_bg),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Spacer(modifier = Modifier.padding(top = 20.dp))
                
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(color = design_FCE9B3, shape = CircleShape)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ){
                    Icon(painter = painterResource(id = R.drawable.icon_invite),
                        contentDescription = "", tint = Color.Unspecified)
                }

                Box(
                    modifier = Modifier
                        .padding(horizontal = 40.dp)
                        .fillMaxWidth()
                        .height(60.dp)
                ){
                    Text(
                        text = inviteCode,
                        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                        fontSize = 24.sp, color = design_select_btn_text,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    
                    Icon(painter = painterResource(id = R.drawable.icon_copy),
                        contentDescription = "", tint = Color.Unspecified,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .align(Alignment.CenterEnd)
                            .clickable {
                                val clipData = ClipData.newPlainText("Text", inviteCode)
                                clipboardManager.setPrimaryClip(clipData)
                            }
                    )
                    
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(design_skip)
                        .align(Alignment.BottomCenter)
                    )
                }

                Spacer(modifier = Modifier.padding(top = 12.dp))
                
                Text(
                    text = buildAnnotatedString {

                        append("이 코드는 ")
                        withStyle(
                            style = SpanStyle(
                                design_sharp
                            ),
                        ) {
                            append("1일 동안 유효")
                        }
                        append("합니다 ")
                    },
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp, letterSpacing = (-0.7).sp,
                    color = design_skip,
                )

                Spacer(modifier = Modifier.padding(top = 40.dp))

                Button(
                    onClick = { share(context, inviteCode = inviteCode, selectPet = selectPet) },
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = design_button_bg)
                )
                {
                    Text(text = "코드 전송",
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

private fun share(context: Context, inviteCode:String, selectPet:List<PetDetailData>){
    val sendIntent: Intent = Intent().apply {
        var petNameList:List<String> = selectPet.map { it.petNm }
        val formattedString : String = petNameList.joinToString(separator = ", "){ "[${it}]" }
        action = Intent.ACTION_SEND
        val inviteString =
            "${G.userNickName}님이 Pet Tip으로 초대했어요!" +
                    "\n${formattedString} 관리에 동참하시겠어요?"+
                    "\n초대코드등록란에 [${inviteCode}]를 입력해주세요. \n https://www.care-biz.co.kr/ "
        putExtra(Intent.EXTRA_TEXT, inviteString)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, "코드 전송")

    context.startActivity(shareIntent)
}