package net.pettip.app.navi.component

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.ui.theme.design_DDDDDD
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_login_text
import net.pettip.app.navi.ui.theme.design_placeHolder
import net.pettip.app.navi.ui.theme.design_sharp
import net.pettip.app.navi.ui.theme.design_textFieldOutLine
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.CommunityViewModel
import net.pettip.data.cmm.CdDetail
import net.pettip.util.Log

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
    val dclrList by viewModel.dclrList.collectAsState()
    val selectDclr by viewModel.selectDclr.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isLoading by remember{ mutableStateOf(false) }

    LaunchedEffect(Unit){
        viewModel.getDclrList()
    }

    DisposableEffect(Unit){
        onDispose {
            viewModel.updateDclrCn("")
            viewModel.updateSelectDclr(null)
            viewModel.updateDclrList(null)
            viewModel.updateSelectCmnt(null)
        }
    }

    AlertDialog(
        onDismissRequest = { onDismiss(false) },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(modifier = Modifier
            .padding(horizontal = 40.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(20.dp))
        ){
            Column (
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = "신고사유",
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    fontSize = 16.sp, letterSpacing = (-0.8).sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(start = 20.dp, top = 20.dp)
                )

                Row (
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp, top = 8.dp)
                        .fillMaxWidth()
                        .height(48.dp)
                        .border(
                            width = 1.dp,
                            color = if (expanded) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { expandChange(!expanded) },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = selectDclr?.cdNm?:"문의유형을 선택해주세요",
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 14.sp, letterSpacing = (-0.7).sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(start = 16.dp)
                    )

                    Icon(
                        imageVector = if(expanded){
                            Icons.Default.KeyboardArrowUp
                        }else{
                            Icons.Default.KeyboardArrowDown
                        },
                        contentDescription = "", tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(end = 16.dp))
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .background(color = Color.Transparent),
                    //verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    itemsIndexed(dclrList?.data?.get(0)?.cdDetailList?: emptyList()){ index,item ->
                        AnimatedVisibility(
                            visible = expanded,
                            enter = slideInHorizontally(tween(delayMillis = 50*index))+ fadeIn(tween(delayMillis = 50*index)),
                            exit = slideOutHorizontally()+ fadeOut()
                        ) {
                            DclrItem(viewModel = viewModel, dclrData = item, onClick = expandChange)
                        }
                    }
                }

                Text(
                    text = "신고내용",
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    fontSize = 16.sp, letterSpacing = (-0.8).sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(start = 20.dp, top = 16.dp, bottom = 8.dp)
                )

                CustomTextField(
                    value = dclrCn,
                    onValueChange = { viewModel.updateDclrCn(it) },
                    singleLine = false,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done),
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp)
                        .fillMaxWidth()
                        .height(150.dp),
                    placeholder = { Text(text = "신고내용을 입력해주세요", fontFamily = FontFamily(Font(R.font.pretendard_regular)), fontSize = 14.sp)},
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.primaryContainer,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                        focusedContainerColor = MaterialTheme.colorScheme.primary,
                        unfocusedLeadingIconColor = MaterialTheme.colorScheme.primaryContainer,
                        focusedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
                        cursorColor = design_intro_bg.copy(alpha = 0.5f)
                    ),
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        fontSize = 16.sp, letterSpacing = (-0.4).sp
                    ),
                    shape = RoundedCornerShape(4.dp),
                    innerPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp)
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
                            .background(MaterialTheme.colorScheme.onSecondary)
                            .clickable { onDismiss(false) },
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "취소",
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = 14.sp, letterSpacing = (-0.7).sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(design_sharp)
                            .clickable {
                                if (selectDclr == null) {
                                    Toast
                                        .makeText(context, "신고사유를 선택해주세요", Toast.LENGTH_SHORT)
                                        .show()
                                } else if (dclrCn.isNullOrBlank()){
                                    Toast
                                        .makeText(context, "신고내용을 입력해주세요", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    scope.launch {
                                        val result = viewModel.dclrCreate()
                                        if (result) {
                                            onDismiss(false)
                                            Toast
                                                .makeText(context, "신고처리 되었습니다", Toast.LENGTH_SHORT)
                                                .show()
                                        }else{
                                            Toast
                                                .makeText(context, "다시 시도해주세요", Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                    }
                                }
                            }
                        ,
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

@Composable
fun DclrItem(viewModel: CommunityViewModel, dclrData: CdDetail, onClick: (Boolean)->Unit){

    Column (
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(48.dp)
            .background(Color.Transparent)
            .border(
                color = MaterialTheme.colorScheme.outline,
                width = 1.dp,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable {
                viewModel.updateSelectDclr(dclrData)
                onClick(false)
            },
        verticalArrangement = Arrangement.Center
    ){

        Text(
            text = dclrData.cdNm,
            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            letterSpacing = (-0.7).sp,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}