package net.pettip.app.navi.component

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import net.pettip.app.navi.R
import net.pettip.app.navi.ui.theme.design_login_text
import net.pettip.app.navi.ui.theme.design_white

/**
 * @Project     : PetTip-Android
 * @FileName    : Toasty
 * @Date        : 2024-02-02
 * @author      : CareBiz
 * @description : net.pettip.app.navi.component
 * @see net.pettip.app.navi.component.Toasty
 */
@Composable
fun Toasty(
    snackState: SnackbarHostState,
    text : String = snackState.currentSnackbarData?.visuals?.message?:"",
){
    SnackbarHost(
        hostState = snackState,
        modifier = Modifier,
        snackbar = {
            androidx.compose.material3.Snackbar(
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .fillMaxWidth()
                    .height(46.dp)
                    .padding(horizontal = 20.dp),
                action = {},
                dismissAction = {},
                shape = RoundedCornerShape(23.dp),
                containerColor = design_login_text.copy(alpha = 0.7f),
                contentColor = Color.Transparent,
                content = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Box (
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .align(Alignment.CenterStart)
                                .size(25.dp)
                                .background(color = design_white, shape = CircleShape)
                                .clip(CircleShape)
                        ){
                            Text(
                                text = "!",
                                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                                fontSize = 14.sp, letterSpacing = (-0.7).sp,
                                color = design_login_text,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }

                        Text(
                            text = text,
                            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                            fontSize = 14.sp, letterSpacing = (-0.7).sp,
                            color = design_white,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            )
        }
    )
}
