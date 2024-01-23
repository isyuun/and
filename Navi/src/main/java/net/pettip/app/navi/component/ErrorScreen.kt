package net.pettip.app.navi.component

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import net.pettip.app.navi.R
import net.pettip.app.navi.ui.theme.design_button_bg
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
fun ErrorScreen(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    var isButtonEnabled by remember { mutableStateOf(true) }

    LaunchedEffect(isButtonEnabled) {
        if (!isButtonEnabled) {
            delay(1000)
            isButtonEnabled = true
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = if (isSystemInDarkTheme()) R.drawable.img_error_dark else R.drawable.img_error_light),
            tint = Color.Unspecified, contentDescription = "",
            modifier = Modifier.padding(top = 20.dp)
        )

        Text(
            text = stringResource(id = R.string.error_msg_temp_error),
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            fontSize = 24.sp, letterSpacing = (-1.2).sp,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(top = 20.dp)
        )

        Text(
            text = stringResource(id = R.string.error_msg_refresh),
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = 14.sp, letterSpacing = (-0.7).sp,
            color = MaterialTheme.colorScheme.onPrimary,
            lineHeight = 20.sp,
            modifier = Modifier.padding(top = 20.dp)
        )

        Spacer(modifier = Modifier.padding(top = 20.dp))

        Button(
            onClick = {
                isButtonEnabled = false
                onClick()
            },
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonColors(
                containerColor = design_button_bg, disabledContainerColor = design_button_bg,
                contentColor = design_white, disabledContentColor = design_white
            ),
            enabled = isButtonEnabled,
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 5.dp,
                pressedElevation = 0.dp
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_reflesh),
                    contentDescription = "", tint = design_white
                )

                Text(
                    text = stringResource(R.string.page_refresh),
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp, letterSpacing = (-0.7).sp,
                    color = design_white,
                    lineHeight = 16.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}