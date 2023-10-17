package kr.carepet.app.navi.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kr.carepet.app.navi.R
import kr.carepet.app.navi.Screen
import kr.carepet.app.navi.ui.theme.design_button_bg
import kr.carepet.app.navi.ui.theme.design_login_text
import kr.carepet.app.navi.ui.theme.design_skip
import kr.carepet.app.navi.ui.theme.design_white

@Composable
fun CustomDialog(onDismiss:(Boolean) -> Unit, navController: NavHostController){
    AlertDialog(
        onDismissRequest = { onDismiss(false) },
        confirmButton = {
            Button(
                onClick = {
                    onDismiss(false)
                    navController.navigate(Screen.AddPetScreen.route)
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = design_button_bg
                )
            ) {
                Text(
                    text = "등록하기",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    color = design_white
                )
            } },
        title = { Text(text = "같이 산책할 펫이 없어요") },
        text = { Text(text = "펫을 등록하시겠어요?", color = design_skip) },
        dismissButton = {
            Button(
                onClick = {
                    onDismiss(false)
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = design_white
                ),
                border = BorderStroke(1.dp, color = design_login_text)
            ) {
                Text(
                    text = "나중에",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    color = design_login_text
                )
            }
        },
        containerColor = design_white
    )
}