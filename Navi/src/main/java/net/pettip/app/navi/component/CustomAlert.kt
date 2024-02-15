package net.pettip.app.navi.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import net.pettip.app.navi.R
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_white

/**
 * @Project     : PetTip-Android
 * @FileName    : CustomAlert
 * @Date        : 2024-02-02
 * @author      : CareBiz
 * @description : net.pettip.app.navi.component
 * @see net.pettip.app.navi.component.CustomAlert
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAlert(
    onDismiss: (Boolean) -> Unit,
    confirm: String,
    dismiss: String,
    title: String,
    text: String,
    dismissJob : () ->Unit = {},
    confirmJob : () ->Unit = {},
){
    val widthTitle = measureTextWidth(text = title, style = TextStyle(fontSize = 16.sp, letterSpacing = (-0.4).sp, fontFamily = FontFamily(Font(R.font.pretendard_bold))))
    val widthText = measureTextWidth(text = text, style = TextStyle(fontSize = 14.sp, letterSpacing = (-0.4).sp, fontFamily = FontFamily(Font(R.font.pretendard_regular))))
    val width = maxOf(widthTitle,widthText)

    AlertDialog(
        onDismissRequest = { onDismiss(false) },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
        modifier = Modifier
            .padding(30.dp)
            .widthIn(
                min = 240.dp,
                max = 600.dp
            )
            .width(width+40.dp)
    ){
        Box(modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(20.dp)
            )
        ){
            Column (
                modifier = Modifier.wrapContentWidth()
            ){
                Text(
                    text = title,
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    fontSize = 16.sp, letterSpacing = (-0.4).sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 30.dp)
                )

                Text(
                    text = text,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp, letterSpacing = (-0.4).sp,
                    lineHeight = 20.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 8.dp)
                )

                Row (
                    modifier = Modifier
                        .padding(top = 50.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                ){
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(MaterialTheme.colorScheme.onSecondary)
                            .clickable {
                                onDismiss(false)
                                dismissJob()
                            },
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = dismiss,
                            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                            fontSize = 14.sp, letterSpacing = (-0.7).sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(design_intro_bg)
                            .clickable {
                                onDismiss(false)
                                confirmJob()
                            }
                        ,
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = confirm,
                            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                            fontSize = 14.sp, letterSpacing = (-0.7).sp,
                            color = design_white,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }
                }
            }
        }
    }
}
