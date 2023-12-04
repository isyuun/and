package net.pettip.app.navi.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import net.pettip.app.navi.R
import net.pettip.app.navi.ui.theme.design_DDDDDD
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_login_text
import net.pettip.app.navi.ui.theme.design_placeHolder
import net.pettip.app.navi.ui.theme.design_sharp
import net.pettip.app.navi.ui.theme.design_textFieldOutLine
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.CommunityViewModel

/**
 * @Project     : PetTip-Android
 * @FileName    : DclrDialog
 * @Date        : 2023-12-01
 * @author      : CareBiz
 * @description : net.pettip.app.navi.component
 * @see net.pettip.app.navi.component.DclrDialog
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DclrDialog(
    viewModel : CommunityViewModel,
    expanded : Boolean,
    expandChange: (Boolean) -> Unit,
    onDismiss: (Boolean) -> Unit,
){
    val dclrCn by viewModel.dclrCn.collectAsState()

    AlertDialog(
        onDismissRequest = { onDismiss(false) },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(modifier = Modifier
            .padding(horizontal = 40.dp)
            .fillMaxWidth()
            .background(color = design_white, shape = RoundedCornerShape(20.dp))
        ){
            Column (
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = "신고사유",
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    fontSize = 16.sp, letterSpacing = (-0.8).sp,
                    color = design_login_text,
                    modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 8.dp)
                )

                Row (
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 8.dp)
                        .fillMaxWidth()
                        .height(48.dp)
                        .border(
                            width = 1.dp,
                            color = design_textFieldOutLine,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { expandChange(!expanded) },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = "문의유형을 선택해주세요",
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 14.sp, letterSpacing = (-0.7).sp,
                        color = design_login_text,
                        modifier = Modifier.padding(start = 16.dp)
                    )

                    Icon(
                        imageVector = if(expanded){
                            Icons.Default.KeyboardArrowUp
                        }else{
                            Icons.Default.KeyboardArrowDown
                        },
                        contentDescription = "", tint = design_login_text,
                        modifier = Modifier.padding(end = 16.dp))
                }

                Text(
                    text = "신고내용",
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    fontSize = 16.sp, letterSpacing = (-0.8).sp,
                    color = design_login_text,
                    modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 8.dp)
                )

                CustomTextField(
                    value = dclrCn,
                    onValueChange = { viewModel.updateDclrCn(it) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done),
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp)
                        .fillMaxWidth()
                        .height(150.dp),
                    placeholder = { Text(text = "신고내용을 입력해주세요", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedPlaceholderColor = design_placeHolder,
                        focusedPlaceholderColor = design_placeHolder,
                        unfocusedBorderColor = design_textFieldOutLine,
                        focusedBorderColor = design_login_text,
                        unfocusedContainerColor = design_white,
                        focusedContainerColor = design_white,
                        unfocusedLeadingIconColor = design_placeHolder,
                        focusedLeadingIconColor = design_login_text),
                    shape = RoundedCornerShape(4.dp),
                    innerPadding = PaddingValues(start = 8.dp)
                )

                Row (
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                ){
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(design_DDDDDD)
                            .clickable { onDismiss(false) },
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "취소",
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = 14.sp, letterSpacing = (-0.7).sp,
                            color = design_login_text,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(design_sharp),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "신고",
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = 14.sp, letterSpacing = (-0.7).sp,
                            color = design_white,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                }
            }
        }
    }
}