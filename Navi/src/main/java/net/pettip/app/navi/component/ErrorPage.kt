package net.pettip.app.navi.component

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_login_text

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
    isLoading : Boolean,
    onClick: (Boolean) -> Unit
){
    val scope = rememberCoroutineScope()

    Box (
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){

        Text(
            text = "불러오기에 실패했습니다.\n다시 시도해주세요",
            textAlign = TextAlign.Center,
            fontSize = 20.sp, color = design_login_text,
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            letterSpacing = (-1.0).sp
        )

        Crossfade(
            targetState = isLoading, label = ""
        ) {
            when(it){
                true ->
                    Box(modifier = Modifier.offset(y = 70.dp).size(50.dp)){
                        LoadingAnimation1(circleColor = design_intro_bg)
                    }
                false ->
                    IconButton(
                        onClick = {
                            onClick(true)
                        },
                        modifier = Modifier.offset(y = 70.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "",
                            tint = design_login_text,
                            modifier = Modifier.size(30.dp)
                        )
                    }
            }

        }



    }
}