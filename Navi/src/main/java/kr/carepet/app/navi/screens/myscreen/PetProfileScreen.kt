package kr.carepet.app.navi.screens.myscreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kr.carepet.app.navi.R
import kr.carepet.app.navi.component.BackTopBar
import kr.carepet.app.navi.screens.mainscreen.CircleImage
import kr.carepet.app.navi.ui.theme.design_999999
import kr.carepet.app.navi.ui.theme.design_DDDDDD
import kr.carepet.app.navi.ui.theme.design_btn_border
import kr.carepet.app.navi.ui.theme.design_button_bg
import kr.carepet.app.navi.ui.theme.design_icon_bg
import kr.carepet.app.navi.ui.theme.design_intro_bg
import kr.carepet.app.navi.ui.theme.design_login_bg
import kr.carepet.app.navi.ui.theme.design_login_text
import kr.carepet.app.navi.ui.theme.design_skip
import kr.carepet.app.navi.ui.theme.design_white
import kr.carepet.app.navi.viewmodel.SettingViewModel
import kr.carepet.app.navi.viewmodel.SharedViewModel
import kr.carepet.data.pet.Member

@Composable
fun PetProfileScreen(navController: NavHostController, sharedViewModel: SharedViewModel, settingViewModel: SettingViewModel,index: String?){

    val petInfo by sharedViewModel.petInfo.collectAsState()
    val index = index?.toInt() ?: 0

    val memberList by settingViewModel.memberList.collectAsState()

    LaunchedEffect(Unit){
        settingViewModel.getPetInfoDetail(petInfo[index])
    }

    Scaffold (
        topBar = { BackTopBar(title = "${petInfo[index].petNm} 프로필", navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(design_white),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(top = 20.dp))

            CircleImage(
                size = 180,
                imageUri = petInfo[index].petRprsImgAddr
            )

            Spacer(modifier = Modifier.padding(top = 16.dp))

            Text(
                text = petInfo[index].petKindNm,
                fontSize = 14.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                letterSpacing = (-0.7).sp,
                color = design_skip
            )

            Text(
                text = petInfo[index].petNm,
                fontSize = 30.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                letterSpacing = (-0.7).sp,
                color = design_login_text
            )

            Spacer(modifier = Modifier.padding(top = 16.dp))

            Row (modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){

                Box (
                    modifier= Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(color = design_icon_bg),
                    contentAlignment = Alignment.Center
                ){
                    Icon(painter = painterResource(id = R.drawable.icon_age), contentDescription = "", tint = Color.Unspecified)
                }

                Text(
                    text = if (petInfo[index].petBrthYmd=="미상"){
                        "미상"
                    }else{
                        sharedViewModel.changeBirth(petInfo[index].petBrthYmd)
                    },
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    letterSpacing = (-0.7).sp,
                    color = design_login_text,
                    modifier = Modifier.padding(start = 8.dp)
                )

                Spacer(modifier = Modifier.padding(start = 20.dp))

                Box (
                    modifier= Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(color = design_icon_bg),
                    contentAlignment = Alignment.Center
                ){
                    Icon(painter = painterResource(id = R.drawable.icon_gender), contentDescription = "", tint = Color.Unspecified)
                }

                Text(
                    text = petInfo[index].sexTypNm?:"",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    letterSpacing = (-0.7).sp,
                    color = design_login_text,
                    modifier = Modifier.padding(start = 8.dp)
                )

                Spacer(modifier = Modifier.padding(start = 20.dp))

                Box (
                    modifier= Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(color = design_icon_bg),
                    contentAlignment = Alignment.Center
                ){
                    Icon(painter = painterResource(id = R.drawable.icon_weight), contentDescription = "", tint = Color.Unspecified)
                }

                Text(
                    text = "${petInfo[index].wghtVl}kg",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    letterSpacing = (-0.7).sp,
                    color = design_login_text,
                    modifier = Modifier.padding(start = 8.dp)
                )

            } // Row

            Spacer(modifier = Modifier.padding(top = 20.dp))

            Button(
                enabled = petInfo[index].mngrType == "M",
                onClick = { navController.navigate("modifyPetInfoScreen/${index}") },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = design_white,
                    disabledContainerColor = design_white
                ),
                border = BorderStroke(width = 1.dp, color = design_btn_border),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp, pressedElevation = 0.dp),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = if(petInfo[index].mngrType == "M"){"정보 수정하기"}else{"관리중인 반려동물만 수정 가능합니다"},
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 14.sp, letterSpacing = (-0.7).sp,
                    color = design_login_text
                )
            }

            Spacer(modifier = Modifier.padding(top = 40.dp))

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = design_login_bg)
            ){
                Text(
                    text = "참여중인 그룹",
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    fontSize = 20.sp, letterSpacing = (-1.0).sp,
                    color = design_login_text,
                    modifier = Modifier.padding(start = 20.dp, top = 20.dp)
                )

                Spacer(modifier = Modifier.padding(bottom = 16.dp))

                if (memberList?.isNotEmpty()==true){
                    LazyColumn(
                        state = rememberLazyListState(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.heightIn(max = 300.dp)
                    ){
                        items(memberList!!){ item ->
                            GroupItem(item = item)
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(bottom = 20.dp))
            }
        }// Col
    }
}

@Composable
fun GroupItem(item:Member){
    Row (
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(modifier = Modifier
            .size(40.dp)
        ){
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("")
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                placeholder = painterResource(id = R.drawable.profile_person),
                error= painterResource(id = R.drawable.profile_person),
                modifier= Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            if (item.mngrType=="M"){
                Box(modifier = Modifier
                    .size(12.dp)
                    .clip(shape = RoundedCornerShape(4.dp))
                    .background(color = design_intro_bg, shape = RoundedCornerShape(4.dp))
                    .align(Alignment.TopStart)
                ){
                    Icon(painter = painterResource(id = R.drawable.icon_admin),
                        contentDescription = "", tint = Color.Unspecified,
                        modifier = Modifier.align(Alignment.Center))
                }
            }
            
        }
        
        Text(
            text = item.nckNm,
            fontFamily = FontFamily(Font(R.font.pretendard_medium)),
            fontSize = 16.sp, letterSpacing = (-0.8).sp,
            color = design_login_text,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Box (
            modifier= Modifier
                .padding(end = 12.dp)
                .border(
                    when (item.mngrType) {
                        "M" -> 0.dp
                        "I" -> 1.dp
                        "C" -> 0.dp
                        else -> 0.dp
                    },
                    color = design_btn_border,
                    shape = RoundedCornerShape(10.dp)
                )
                .background(
                    color =
                    when (item.mngrType) {
                        "M" -> design_button_bg
                        "I" -> design_white
                        "C" -> design_DDDDDD
                        else -> design_DDDDDD
                    },
                    shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ){
            Text(
                text =
                when(item.mngrType){
                    "M" -> "관리중"
                    "I" -> "참여중"
                    "C" -> "동참중단"
                    else -> "에러"
                },
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 12.sp,
                letterSpacing = (-0.6).sp,
                color =
                when(item.mngrType){
                    "M" -> design_white
                    "I" -> design_btn_border
                    "C" -> design_999999
                    else -> design_DDDDDD
                },
                modifier = Modifier.padding(horizontal = 9.dp, vertical = 2.dp)
            )
        }
    }// row
}

