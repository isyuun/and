package net.pettip.app.navi.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import net.pettip.app.navi.R
import net.pettip.app.navi.ui.theme.design_button_bg
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.ui.theme.design_textFieldOutLine
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.LoginViewModel

/**
 * @Project     : PetTip-Android
 * @FileName    : PermissionScreen
 * @Date        : 2023-12-07
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screens
 * @see net.pettip.app.navi.screens.PermissionScreen
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionScreen(viewModel: LoginViewModel, permissionState: MultiplePermissionsState, onCheck:(Boolean) -> Unit) {

    val permissionCheck by viewModel.permissionCheck.collectAsState()

    LaunchedEffect(permissionState.allPermissionsGranted){
        if (permissionState.allPermissionsGranted){
            onCheck(false)
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ){
        Column {
            Text(
                text = stringResource(R.string.pettip_permissions_notice),
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 24.sp, color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(top = 40.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            )

            Text(
                text = stringResource(R.string.pettip_permissions_sub_notice),
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 14.sp, color = MaterialTheme.colorScheme.onPrimary,
                lineHeight = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier
                .padding(top = 20.dp, bottom = 20.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.outline))

            Text(
                text = stringResource(R.string.required_access_permissons),
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 16.sp, letterSpacing = (-0.8).sp,
                color = design_intro_bg
            )

            MediumText(text = stringResource(R.string.location))

            RegularText(text = stringResource(R.string.location_text))

            MediumText(text = stringResource(R.string.storage))

            RegularText(text = stringResource(R.string.storage_text))

            MediumText(text = stringResource(R.string.push))

            RegularText(text = stringResource(R.string.push_text))

            Spacer(modifier = Modifier
                .padding(top = 20.dp, bottom = 20.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(design_textFieldOutLine))

            Text(
                text = stringResource(R.string.optional_access_permissons),
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 16.sp, letterSpacing = (-0.8).sp,
                color = MaterialTheme.colorScheme.onPrimary
            )

            MediumText(text = stringResource(R.string.physical_activity))

            RegularText(text = stringResource(R.string.physical_activty_text))

            MediumText(text = stringResource(R.string.camera))

            RegularText(text = stringResource(R.string.camera_text))
        }

        Button(
            onClick = {
                if (!permissionCheck){
                    viewModel.updatePermissionCheck(true)
                    permissionState.launchMultiplePermissionRequest()
                }else{
                    onCheck(false)
                }
                      },
            modifier = Modifier
                .padding(top = 20.dp, bottom = 20.dp)
                .fillMaxWidth()
                .height(56.dp), shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = design_button_bg)
        )
        {
            Text(
                text = if (!permissionCheck) stringResource(id = R.string.confirm) else "뒤로가기",
                color = design_white, fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular))
            )
        }

    }

}

@Composable
private fun MediumText(
    text:String
){
    Text(
        text = text,
        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
        fontSize = 16.sp, letterSpacing = (-0.8).sp,
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.padding(top = 16.dp)
    )
}

@Composable
private fun RegularText(
    text:String
){
    Text(
        text = text,
        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
        fontSize = 14.sp, letterSpacing = (-0.7).sp,
        modifier = Modifier.padding(top = 4.dp),
        color = MaterialTheme.colorScheme.secondary,
        lineHeight = 20.sp
    )
}
