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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import net.pettip.app.navi.ui.theme.design_login_text
import net.pettip.app.navi.ui.theme.design_skip
import net.pettip.app.navi.ui.theme.design_textFieldOutLine
import net.pettip.app.navi.ui.theme.design_white

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
fun PermissionScreen(permissionState: MultiplePermissionsState, onCheck:(Boolean) -> Unit) {

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
                text = "펫팁 권한 안내",
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 24.sp, color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(top = 40.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            )

            Text(
                text = "펫팁은 아래 권한들을 필요로 합니다.\n서비스 사용 중 앱에서 요청 시 허용해주세요.",
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
                text = "필수 접근 권한",
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 16.sp, letterSpacing = (-0.8).sp,
                color = design_intro_bg
            )

            MediumText(text = "위치")

            RegularText(text = "우리 앱은 현재 위치 기반 서비스를 제공합니다. 정확한 서비스를 위해 위치에 접근할 필요가 있습니다.")

            MediumText(text = "저장용량")

            RegularText(text = "앱은 사진, 동영상 등의 미디어를 저장하고 관리합니다. 저장소에 접근하여 파일을 저장하고 불러오기 위해 권한이 필요합니다.")

            MediumText(text = "알림")

            RegularText(text = "우리 앱은 중요한 알림을 통해 사용자에게 정보를 전달합니다. 알림을 표시하기 위해 권한이 필요합니다.")

            Spacer(modifier = Modifier
                .padding(top = 20.dp, bottom = 20.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(design_textFieldOutLine))

            Text(
                text = "선택 접근 권한",
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 16.sp, letterSpacing = (-0.8).sp,
                color = MaterialTheme.colorScheme.onPrimary
            )

            MediumText(text = "신체활동")

            RegularText(text = "당사 앱은 건강 및 신체활동을 기록하는 서비스를 제공합니다. 신체활동 데이터를 수집하기 위해 신체활동 권한이 필요합니다.")

            MediumText(text = "카메라")

            RegularText(text = "당사 앱은 사진 및 비디오 촬영 기능을 제공합니다. 사진 및 비디오를 촬영하고 저장하기 위해 카메라에 접근하는 권한이 필요합니다.")
        }

        Button(
            onClick = { permissionState.launchMultiplePermissionRequest() },
            modifier = Modifier
                .padding(top = 20.dp,bottom = 20.dp)
                .fillMaxWidth()
                .height(48.dp), shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = design_button_bg)
        )
        {
            Text(text = "확인", color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
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
