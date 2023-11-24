package net.pettip.app.navi.component

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
import net.pettip.app.navi.R
import net.pettip.app.navi.Screen
import net.pettip.app.navi.ui.theme.design_button_bg
import net.pettip.app.navi.ui.theme.design_login_text
import net.pettip.app.navi.ui.theme.design_skip
import net.pettip.app.navi.ui.theme.design_white

@Composable
fun CustomDialog(
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
                    navController.navigate(Screen.AddPetScreen.route)
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