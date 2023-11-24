package net.pettip.app.navi.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import net.pettip.app.navi.R
import net.pettip.app.navi.ui.theme.design_login_text
import net.pettip.app.navi.ui.theme.design_white

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackTopBar(title: String, navController: NavHostController, backVisible:Boolean=true){

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = design_white),
        modifier = Modifier.height(60.dp),
        title = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                AnimatedVisibility(
                    modifier = Modifier.align(Alignment.CenterStart),
                    visible = backVisible,
                    enter = scaleIn(),
                    exit = scaleOut()
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(shape = CircleShape)
                            .clickable {
                                navController.popBackStack()
                            }
                            .align(Alignment.CenterStart),
                        contentAlignment = Alignment.Center
                    ){
                        Icon(painter = painterResource(id = R.drawable.arrow_back),
                            contentDescription = "",
                            tint = Color.Unspecified
                        )
                    }
                }


                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                    letterSpacing = (-1.0).sp,
                    color = design_login_text,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
        }
    )
}