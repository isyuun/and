package net.pettip.app.navi.screens

import android.util.Base64
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import net.pettip.app.navi.R
import net.pettip.app.navi.Screen
import net.pettip.app.navi.ui.theme.design_intro_bg
import net.pettip.app.navi.viewmodel.SharedViewModel
import net.pettip.singleton.MySharedPreference
import net.pettip.util.Log

private const val SplashWaitTime:Long = 2000

@Composable
fun IntroScreen(modifier: Modifier = Modifier, navController: NavHostController, viewModel: SharedViewModel){

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color = design_intro_bg)

    LaunchedEffect(Unit) {
        delay(SplashWaitTime)
        if (MySharedPreference.getIsLogin()) {
            val result = viewModel.sendRFToken()
            if (result) {
                navController.navigate(Screen.MainScreen.route) {
                    popUpTo(Screen.Intro.route) { inclusive = true }
                }
            } else {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Intro.route) { inclusive = true }
                }
            }
        } else {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Intro.route) { inclusive = true }
            }
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(design_intro_bg), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
        Icon(painter = painterResource(id = R.drawable.character), contentDescription = "",
            modifier= modifier
                .offset(0.dp,(-80).dp), tint = Color.Unspecified)
        
        Icon(painter = painterResource(id = R.drawable.logo_splash_en), contentDescription = "",
            modifier=modifier.offset(0.dp, (-40).dp), tint = Color.Unspecified)
        
    }
}