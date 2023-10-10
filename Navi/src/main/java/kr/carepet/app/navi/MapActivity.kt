/*
 *  Copyright 2011 The Android Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 * Copyright (c) 2023. CarePat All right reserved.
 * This software is the proprietary information of CarePet Co.,Ltd.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-pet.kr             2023. 10. 6.   description...
 */

package kr.carepet.app.navi

import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.carepet.app.navi.component.CircleImageTopBar
import kr.carepet.app.navi.component.WalkTimeNDis
import kr.carepet.app.navi.screens.mainscreen.MyBottomSheet
import kr.carepet.app.navi.screens.mainscreen.WalkBottomSheet
import kr.carepet.app.navi.screens.mainscreen.WalkBottomSheetEnd
import kr.carepet.app.navi.screens.mainscreen.shadow
import kr.carepet.app.navi.ui.theme.design_btn_border
import kr.carepet.app.navi.ui.theme.design_button_bg
import kr.carepet.app.navi.ui.theme.design_login_text
import kr.carepet.app.navi.ui.theme.design_select_btn_bg
import kr.carepet.app.navi.ui.theme.design_select_btn_text
import kr.carepet.app.navi.ui.theme.design_shadow
import kr.carepet.app.navi.ui.theme.design_skip
import kr.carepet.app.navi.ui.theme.design_textFieldOutLine
import kr.carepet.app.navi.ui.theme.design_white
import kr.carepet.app.navi.viewmodel.SettingViewModel
import kr.carepet.app.navi.viewmodel.SharedViewModel
import kr.carepet.app.navi.viewmodel.WalkViewModel
import kr.carepet.data.pet.CurrentPetData
import kr.carepet.data.pet.PetDetailData
import kr.carepet.singleton.G
import kr.carepet.util.Log

/**
 * @Project     : carepet-android
 * @FileName    : MapActivity.kt
 * @Date        : 2023. 10. 06.
 * @author      : isyuun@care-pet.kr
 * @description :
 */
class MapActivity : kr.carepet.map.app.MapActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun setContent() {
        setContent {

            var tipVisible by remember { mutableStateOf(false) }

            val scope = rememberCoroutineScope()

            var openBottomSheet by rememberSaveable { mutableStateOf(true) }

            val bottomSheetState =
                androidx.compose.material3.rememberModalBottomSheetState(skipPartiallyExpanded = true)

            val context = LocalContext.current
            val density = LocalDensity.current.density
            val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")

            val navigationBarHeight = if (resourceId > 0) {
                (context.resources.getDimensionPixelSize(resourceId) / density).dp
            } else {
                0.dp
            }

            var isWalking by remember{ mutableStateOf(false) }

            val petInfo = G.mapPetInfo
            Log.d("MAP",petInfo.toString())

            val navController = rememberNavController()

            LaunchedEffect(Unit){
                delay(1000)
                tipVisible=true
            }

            Box(
                modifier = Modifier.fillMaxSize()
            ){

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(design_skip)
                ){
                    NaverMapApp()
                }

                Column (modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                ){
                    AnimatedVisibility(
                        visible =  tipVisible&&!isWalking,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    color = design_shadow,
                                    offsetX = 20.dp,
                                    offsetY = 20.dp,
                                    spread = 3.dp,
                                    blurRadius = 5.dp,
                                    borderRadius = 20.dp
                                )
                                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                                .background(
                                    color = design_white,
                                    shape = RoundedCornerShape(
                                        bottomStart = 20.dp,
                                        bottomEnd = 20.dp
                                    )
                                )
                        ) {
                            Spacer(modifier = Modifier.padding(top = 16.dp))
                            Row (
                                modifier = Modifier
                                    .padding(start = 20.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Icon(painter = painterResource(id = R.drawable.icon_bulb), contentDescription = "", tint = Color.Unspecified)
                                Text(
                                    text = "소소한 산책 TIP",
                                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                    fontSize = 12.sp,
                                    letterSpacing = (-0.6).sp,
                                    color = design_skip,
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.padding(top = 4.dp))
                            Text(
                                text = "슬개골 건강에는 비탈길, 계단보다 평지가 좋아요~",
                                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                                fontSize = 14.sp,
                                letterSpacing = (-0.7).sp,
                                color = design_login_text,
                                modifier = Modifier.padding(start = 20.dp)
                            )
                            Spacer(modifier = Modifier.padding(top = 16.dp))
                        }
                    }

                    AnimatedVisibility(
                        visible =  isWalking,
                        enter = expandVertically(
                            animationSpec = tween(delayMillis = 500)
                        ),
                        exit = shrinkVertically()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(
                                    color = design_shadow,
                                    offsetX = 20.dp,
                                    offsetY = 20.dp,
                                    spread = 3.dp,
                                    blurRadius = 5.dp,
                                    borderRadius = 20.dp
                                )
                                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                                .background(
                                    color = design_white,
                                    shape = RoundedCornerShape(
                                        bottomStart = 20.dp,
                                        bottomEnd = 20.dp
                                    )
                                )
                        ){
                            Spacer(modifier = Modifier.padding(top = 16.dp))

                            Row (
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ){
                                Row (
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    CircleImageTopBar(size = 40, imageUri = petInfo[0].petRprsImgAddr)

                                    Column (
                                        modifier = Modifier
                                            .padding(start = 12.dp)
                                    ){
                                        Text(
                                            text = "행복한 산책중" ,
                                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                            fontSize = 12.sp,
                                            letterSpacing = (-0.6).sp,
                                            color = design_skip
                                        )

                                        Text(
                                            text = "01:20:54" ,
                                            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                                            fontSize = 22.sp,
                                            letterSpacing = (-0.0).sp,
                                            color = design_login_text
                                        )
                                    }
                                }

                            }
                            Spacer(modifier = Modifier.padding(top = 16.dp))
                        }
                    }

                    Spacer(modifier = Modifier.padding(top = 16.dp))

                    Box(
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .size(40.dp)
                            .background(color = design_white, shape = CircleShape)
                            .clip(shape = CircleShape)
                    ){
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "", tint = design_login_text,
                            modifier = Modifier.align(Alignment.Center))
                    }
                }

                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                ){

                    Button(
                        onClick = {
                                  openBottomSheet = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                        colors = if (!isWalking){
                            ButtonDefaults.buttonColors(containerColor = design_button_bg)
                        }else{
                            ButtonDefaults.buttonColors(containerColor = design_btn_border)
                        }
                    ){
                        Text(
                            text = if (!isWalking){
                                "산책하기"
                            }else{
                                "산책 종료"
                            },
                            color = design_white,
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.pretendard_regular))
                        )
                    }

                    Spacer(modifier = Modifier.padding(bottom = 20.dp))
                }

                if (openBottomSheet){
                    ModalBottomSheet(
                        onDismissRequest = { openBottomSheet = false },
                        sheetState = bottomSheetState,
                        containerColor = Color.Transparent,
                        dragHandle = {}
                    ) {
                        Column {
                            WalkBottomSheet(isWalking,{newValue -> isWalking = newValue})
                            Spacer(modifier = Modifier
                                .height(navigationBarHeight)
                                .fillMaxWidth()
                                .background(color = design_white))
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun WalkBottomSheet(isWalking:Boolean,onChange:(Boolean)->Unit){

    var isCheck by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(design_white)
    ) {
        Spacer(modifier = Modifier.padding(top = 20.dp))

        Text(
            text = "누구랑 산책할까요?",
            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
            fontSize = 20.sp,
            letterSpacing = (-1.0).sp,
            color = design_login_text,
            modifier = Modifier.padding(start = 20.dp)
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            contentPadding = PaddingValues(horizontal = 20.dp)
        ){
            items(G.mapPetInfo){ petList ->
                Box (modifier = Modifier.padding(horizontal = 4.dp)){
                    WalkBottomSheetItem(petList = petList)
                }
            }
        }

        Spacer(modifier = Modifier.padding(top = 4.dp))

        Row (
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ){
            Row (modifier = Modifier
                .clickable { isCheck = !isCheck },
                verticalAlignment = Alignment.CenterVertically
            ){
                Checkbox(
                    checked = isCheck,
                    onCheckedChange = {isCheck = it},
                    colors = CheckboxDefaults.colors(
                        checkedColor = design_select_btn_text,
                        uncheckedColor = design_textFieldOutLine
                    )
                )

                Text(
                    text = "계속 이 아이와 산책할게요",
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    color = design_login_text,
                    letterSpacing = (-0.7).sp
                )
            }
        }

        Spacer(modifier = Modifier.padding(top = 4.dp))

        Button(
            onClick = {onChange(true)},
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = design_button_bg)
        ){
            Text(text = "산책하기", color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
        }

        Spacer(modifier = Modifier.padding(top = 20.dp))
    }
}

//@Composable
//fun WalkBottomSheetEnd(viewModel: WalkViewModel, bottomSheetState: ModalBottomSheetState, navController: NavHostController) {
//    Column (
//        modifier = Modifier
//            .fillMaxWidth()
//            .wrapContentHeight()
//            .background(design_white)
//    ) {
//        Spacer(modifier = Modifier.padding(top = 20.dp))
//
//        Text(
//            text = "산책을 종료할까요?",
//            fontFamily = FontFamily(Font(R.font.pretendard_bold)),
//            fontSize = 20.sp,
//            letterSpacing = (-1.0).sp,
//            color = design_login_text,
//            modifier = Modifier.padding(start = 20.dp)
//        )
//
//        // 데이터만 넘겨주기
//        WalkTimeNDis()
//
//        Spacer(modifier = Modifier.padding(top = 20.dp))
//
//        Row (
//            modifier = Modifier
//                .padding(horizontal = 20.dp)
//                .fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
//        ){
//            Button(
//                onClick = {
//                    navController.navigate(Screen.PostScreen.route){
//                        popUpTo(Screen.WalkWithMap.route){inclusive=true}
//                    }
//                },
//                modifier = Modifier
//                    .weight(1f)
//                    .height(48.dp),
//                shape = RoundedCornerShape(12.dp),
//                colors = ButtonDefaults.buttonColors(design_white),
//                border = BorderStroke(1.dp, color = design_btn_border)
//            ) {
//                Text(text = "네,종료할게요", color = design_login_text,
//                    fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
//            }
//
//            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
//
//            Button(
//                onClick = { },
//                modifier = Modifier
//                    .height(48.dp)
//                    .weight(1f),
//                shape = RoundedCornerShape(12.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = design_button_bg)
//            )
//            {
//                Text(text = "조금 더 할게요", color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
//            }
//        }
//
//        Spacer(modifier = Modifier.padding(top = 20.dp))
//    }
//}

@Composable
fun WalkBottomSheetItem(petList : CurrentPetData){

    val configuration = LocalConfiguration.current

    val screenWidth = configuration.screenWidthDp.dp -60.dp

    val petName:String = petList.petNm
    val imageUri:String = petList.petRprsImgAddr

    var isSeleted by remember { mutableStateOf(false) }

    Button(
        onClick = { isSeleted= !isSeleted },
        modifier = Modifier
            .size(width = screenWidth / 3, height = screenWidth / 3 - 9.dp)
            .shadow(ambientColor = design_shadow, elevation = 0.dp)
        ,
        shape = RoundedCornerShape(12.dp),
        colors = if(isSeleted) {
            ButtonDefaults.buttonColors(design_select_btn_bg)
        } else {
            ButtonDefaults.buttonColors(design_white)
        },
        border = if(isSeleted) {
            BorderStroke(1.dp, color = design_select_btn_text)
        } else {
            BorderStroke(1.dp, color = design_textFieldOutLine)
        },
        contentPadding = PaddingValues(start = 14.dp,end=14.dp),
        elevation = if(isSeleted){
            ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
        } else {
            ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
        }

    ) {
        Column (
            modifier= Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .border(shape = CircleShape, border = BorderStroke(3.dp, color = design_white))
                    //.shadow(elevation = 10.dp, shape = CircleShape, spotColor = Color.Gray)
                    .shadow(
                        color = design_shadow,
                        offsetY = 10.dp,
                        offsetX = 10.dp,
                        spread = 4.dp,
                        blurRadius = 3.dp,
                        borderRadius = 90.dp
                    )
                    .clip(CircleShape)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = "",
                    placeholder = painterResource(id = R.drawable.profile_default),
                    error= painterResource(id = R.drawable.profile_default),
                    modifier= Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

            }

            Spacer(modifier = Modifier.padding(top = 8.dp))

            Text(
                text = petName,
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                fontSize = 16.sp,
                letterSpacing = (-0.8).sp,
                color = design_login_text
            )
        }
    }
}