package net.pettip.app.navi.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.pettip.app.navi.R
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_login_text
import net.pettip.app.navi.ui.theme.design_white

/**
 * @Project     : PetTip-Android
 * @FileName    : ErrorPage
 * @Date        : 2023-12-04
 * @author      : CareBiz
 * @description : net.pettip.app.navi.component
 * @see net.pettip.app.navi.component.ErrorPage
 */
@Composable
fun ErrorPage(
    isLoading:Boolean,
    onClick: (Boolean) -> Unit
){
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ){
        Text(
            text = "에러발생!\n다시 시도해주세요",
            color = MaterialTheme.colorScheme.onPrimary,
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = { onClick(true) },
            enabled = !isLoading,
            shape = CircleShape,
            modifier = Modifier
                .padding(top = 20.dp)
                .size(50.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = design_intro_bg, contentColor = design_white),
            elevation = androidx.compose.material3.ButtonDefaults.buttonElevation(defaultElevation = 5.dp, pressedElevation = 0.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = "")
        }
    }
}