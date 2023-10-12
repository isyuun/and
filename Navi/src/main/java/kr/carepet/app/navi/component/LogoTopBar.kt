@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalMaterialApi::class
)

package kr.carepet.app.navi.component

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import kr.carepet.app.navi.R
import kr.carepet.app.navi.ui.theme.design_login_text
import kr.carepet.data.pet.PetDetailData
import kr.carepet.app.navi.ui.theme.design_white
import kr.carepet.app.navi.viewmodel.WalkViewModel
import kr.carepet.data.pet.CurrentPetData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogoTopBar(
    petDetailData: CurrentPetData,
    modifier:Modifier = Modifier,
    openBottomSheet:(Boolean) -> Unit,
    backBtnOn:Boolean,
    backBtnOnChange:(Boolean) -> Unit,
    walkViewModel: WalkViewModel
) {

    val scope = rememberCoroutineScope()
    val toMonthCalendar by walkViewModel.toMonthCalendar.collectAsState()
    val toDetail by walkViewModel.toDetail.collectAsState()

    TopAppBar(
        colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = design_white),
        modifier = Modifier.height(60.dp),
        title = { 
            Box(modifier = modifier
                .fillMaxSize()
                ){

                AnimatedVisibility(
                    modifier = Modifier.align(Alignment.CenterStart),
                    visible = !backBtnOn,
                    enter = scaleIn(),
                    exit = scaleOut()
                ) {
                    Icon(painter =painterResource(id = R.drawable.logo),
                        contentDescription = "",
                        modifier= modifier
                            .align(Alignment.CenterStart),
                        tint = Color.Unspecified)
                }


                AnimatedVisibility(
                    modifier = Modifier.align(Alignment.CenterStart),
                    visible = backBtnOn,
                    enter = scaleIn(),
                    exit = scaleOut()
                ) {
                    Icon(painter = painterResource(id = R.drawable.arrow_back),
                        contentDescription = "",
                        modifier= modifier
                            .clickable {
                                if (toMonthCalendar){
                                    walkViewModel.updateToMonthCalendar(false)
                                }else if (toDetail){
                                    walkViewModel.updateToDetail(false)
                                }

                                if (!(toMonthCalendar&&toDetail)){
                                    backBtnOnChange(false)
                                }
                                       },
                        tint = Color.Unspecified)
                }


                Row (
                    modifier = modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight()
                        .wrapContentWidth()
                        .clickable { openBottomSheet(true) },
                    verticalAlignment = Alignment.CenterVertically
                ){
                    CircleImageTopBar(size = 35, imageUri = petDetailData.petRprsImgAddr)
                    Spacer(modifier = Modifier.padding(end=4.dp))
                    Row (
                        modifier
                            .padding(end = 20.dp)
                            .wrapContentHeight()
                            .wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically){
                        Text(
                            text = petDetailData.petNm,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                            fontSize = 14.sp,
                            color = design_login_text
                        )
                        Icon(painter = painterResource(id = R.drawable.arrow_select), contentDescription = "",
                            modifier= Modifier
                                .padding(start = 4.dp)
                            ,
                            tint = design_login_text
                        )
                    }
                }
            }
        })
}


@Composable
fun TopbarPetData(
    petDetailData: PetDetailData,
    modifier: Modifier,
    bottomSheetState: Unit
){



}

@Composable
fun CircleImageTopBar(size: Int, imageUri:String?){

    Box(
        modifier = Modifier
            .size(size.dp)
            .border(shape = CircleShape, border = BorderStroke(3.dp, color = design_white))
            .shadow(elevation = 10.dp, shape = CircleShape, spotColor = Color.Gray)
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
}

private fun openBottomSheet(){
    Log.d("LOG","openBottomSheet 진입")
    // BottomSheet 구현
}
