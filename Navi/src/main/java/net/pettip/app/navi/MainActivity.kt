package net.pettip.app.navi

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.AlertDialog
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.delay
import net.pettip.app.navi.screens.EasyRegScreen
import net.pettip.app.navi.screens.IdFindScreen
import net.pettip.app.navi.screens.IdPwSearchScreen
import net.pettip.app.navi.screens.IntroScreen
import net.pettip.app.navi.screens.LocationPickContent
import net.pettip.app.navi.screens.LoginScreen
import net.pettip.app.navi.screens.PermissionScreen
import net.pettip.app.navi.screens.PetCreateScreen
import net.pettip.app.navi.screens.PetKindContent
import net.pettip.app.navi.screens.PwFindScreen
import net.pettip.app.navi.screens.UserCreateScreen
import net.pettip.app.navi.screens.commuscreen.DailyPostScreen
import net.pettip.app.navi.screens.commuscreen.EventDetail
import net.pettip.app.navi.screens.commuscreen.EventEndDetail
import net.pettip.app.navi.screens.commuscreen.StoryDetail
import net.pettip.app.navi.screens.mainscreen.MainScreen
import net.pettip.app.navi.screens.mainscreen.SettingScreen
import net.pettip.app.navi.screens.mainscreen.WalkScreen
import net.pettip.app.navi.screens.myscreen.AddPetScreen
import net.pettip.app.navi.screens.myscreen.InquiryDetail
import net.pettip.app.navi.screens.myscreen.InviteScreen
import net.pettip.app.navi.screens.myscreen.ModifyInquiryScreen
import net.pettip.app.navi.screens.myscreen.ModifyPetInfoScreen
import net.pettip.app.navi.screens.myscreen.NotiDetail
import net.pettip.app.navi.screens.myscreen.OneNOneScreen
import net.pettip.app.navi.screens.myscreen.PetProfileScreen
import net.pettip.app.navi.screens.myscreen.SetKeyScreen
import net.pettip.app.navi.screens.myscreen.UserInfoScreen
import net.pettip.app.navi.screens.walkscreen.PostScreen
import net.pettip.app.navi.screens.walkscreen.WalkDetailContent
import net.pettip.app.navi.ui.theme.AppTheme
import net.pettip.app.navi.ui.theme.design_login_text
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.CommunityViewModel
import net.pettip.app.navi.viewmodel.HomeViewModel
import net.pettip.app.navi.viewmodel.LoginViewModel
import net.pettip.app.navi.viewmodel.SettingViewModel
import net.pettip.app.navi.viewmodel.SharedViewModel
import net.pettip.app.navi.viewmodel.UserCreateViewModel
import net.pettip.app.navi.viewmodel.WalkViewModel
import net.pettip.data.SCDLocalData
import net.pettip.singleton.G
import java.io.File

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //WindowCompat.setDecorFitsSystemWindows(window, true)


        val data = intent.extras;
        setContent {
            AppTheme {
                Surface {
                    MyApp(data)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        val filesDir = this.filesDir
        val files = filesDir.listFiles()

        files?.forEach { file ->
            file.delete()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MyApp(data: Bundle?) {
    val navController = rememberNavController()

    AppNavigation(navController = navController, data = data)
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun AppNavigation(navController: NavHostController, data: Bundle?){

    val scdLocalData = remember { SCDLocalData() }

    val viewModel = remember{ LoginViewModel()}
    val userCreateViewModel = remember{UserCreateViewModel(scdLocalData)}
    val sharedViewModel = remember{ SharedViewModel() }
    val homeViewModel = remember { HomeViewModel(sharedViewModel) }
    val walkViewModel = remember{WalkViewModel(sharedViewModel)}
    val communityViewModel = remember{CommunityViewModel(sharedViewModel)}
    val settingViewModel = remember{SettingViewModel(sharedViewModel)}

    var count by remember { mutableIntStateOf(3) }

    LaunchedEffect(key1 = G.dupleLogin) {
        if (G.dupleLogin) {
            sharedViewModel.updateDupleLogin(true)
            while (count > 0) {
                delay(1000) // 1초 딜레이
                count--
            }
            navController.navigate(Screen.Login.route) {
                popUpTo(0)
            }
            G.dupleLogin = false
            count = 3
        }
    }
    sharedViewModel.updatePushData(data)

    NavHost(
        navController = navController,
        startDestination = "intro",
        enterTransition = { fadeIn(tween(700)) },
        exitTransition = { fadeOut(tween(700)) }
    ){
        composable(
            route = "detail",
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "http://pettip.kr/{seqNo}"
                    action = Intent.ACTION_VIEW
                }
            ),
            arguments = listOf(
                navArgument("schUnqNo"){
                    type = NavType.IntType
                    defaultValue = 1
                }
            )
        ){entry ->
            val id = entry.arguments?.getInt("schUnqNo")

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Text(text = id.toString())
            }
        }

        composable("intro"){

            IntroScreen(navController = navController, viewModel = sharedViewModel)
        }
        composable("login"){
            LoginScreen(navController = navController, viewModel, sharedViewModel)
        }
        composable("idFindScreen"){

            IdFindScreen(navController = navController, viewModel = viewModel)
        }
        composable("pwFindScreen"){

            PwFindScreen(navController = navController, viewModel = viewModel)
        }
        composable("idPwSearch/{initialTab}", arguments = listOf(navArgument("initialTab"){type = NavType.IntType})){backStackEntry ->

            IdPwSearchScreen(navController= navController, initialTab = backStackEntry.arguments?.getInt("initialTab")?:0, viewModel = viewModel)
        }
        composable("userCreate"){

            UserCreateScreen(navController = navController, viewModel = userCreateViewModel)
        }
        composable("petCreateScreen"){

            PetCreateScreen(navController = navController, viewModel = userCreateViewModel, loginViewModel = viewModel, sharedViewModel = sharedViewModel)
        }
        composable("petKindContent"){

            PetKindContent(navController = navController, viewModel = userCreateViewModel)
        }
        composable("locationPickContent"){

            LocationPickContent(viewModel = userCreateViewModel, navController = navController)
        }
        composable("mainScreen"){

            MainScreen(
                navController = navController,
                homeViewModel = homeViewModel,
                sharedViewModel = sharedViewModel,
                walkViewModel = walkViewModel,
                communityViewModel = communityViewModel,
                settingViewModel = settingViewModel)
        }

        composable("postScreen"){
            PostScreen(walkViewModel, navController)
        }

        composable("easyRegScreen"){
            EasyRegScreen(navController = navController, viewModel = viewModel, userCreateViewModel = userCreateViewModel)
        }

        composable(
            route = "storyDetail",
            enterTransition = {
            fadeIn(
                animationSpec = tween(
                    300, easing = LinearEasing
                )
            )+ slideInHorizontally(
                tween(300, easing = LinearEasing),
                initialOffsetX = { it/3*2}
            )
        },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutHorizontally(
                    tween(300, easing = LinearEasing),
                    targetOffsetX = { it/3*2}
                )
            }
        ){
            StoryDetail(viewModel = communityViewModel, sharedViewModel = sharedViewModel, navController = navController)
        }
        composable("eventDetail"){
            EventDetail(navController = navController, viewModel = communityViewModel)
        }
        composable("eventEndDetail"){
            EventEndDetail(navController = navController)
        }
        composable(
            route = "notiDetail",
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ){
            NotiDetail(navController = navController, viewModel = communityViewModel)
        }
        composable(
            route = "inquiryDetail",
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ){
            InquiryDetail(navController = navController, viewModel = communityViewModel, settingViewModel = settingViewModel)
        }
        //composable(
        //    route = "modifyInquiryScreen",
        //    enterTransition = {
        //        fadeIn(
        //            animationSpec = tween(
        //                300, easing = LinearEasing
        //            )
        //        ) + slideIntoContainer(
        //            animationSpec = tween(300, easing = EaseIn),
        //            towards = AnimatedContentTransitionScope.SlideDirection.Start
        //        )
        //    },
        //    exitTransition = {
        //        fadeOut(
        //            animationSpec = tween(
        //                300, easing = LinearEasing
        //            )
        //        ) + slideOutOfContainer(
        //            animationSpec = tween(300, easing = EaseOut),
        //            towards = AnimatedContentTransitionScope.SlideDirection.End
        //        )
        //    }
        //){
        //    ModifyInquiryScreen(navController = navController, viewModel = communityViewModel, settingViewModel = settingViewModel)
        //}
        composable("oneNOneScreen"){
            OneNOneScreen(navController = navController, settingViewModel = settingViewModel)
        }
        composable("settingScreen"){
            SettingScreen(navController = navController, viewModel = communityViewModel)
        }
        composable("userInfoScreen"){
            UserInfoScreen(navController = navController, settingViewModel = settingViewModel)
        }
        composable("petProfileScreen"){ backStackEntry ->
            PetProfileScreen(navController = navController, sharedViewModel = sharedViewModel, settingViewModel)
        }
        composable("inviteScreen"){
            InviteScreen(navController = navController, settingViewModel = settingViewModel)
        }
        composable("setKeyScreen"){
            SetKeyScreen(navController = navController, settingViewModel = settingViewModel)
        }
        composable("addPetScreen"){
            AddPetScreen(navController = navController, viewModel = userCreateViewModel, sharedViewModel = sharedViewModel)
        }
        composable("modifyPetInfoScreen"){
            ModifyPetInfoScreen(navController = navController, viewModel = userCreateViewModel, sharedViewModel = sharedViewModel)
        }
        composable(
            "walkDetailContent",
            //enterTransition = {
            //    fadeIn(
            //        animationSpec = tween(
            //            300, easing = LinearEasing
            //        )
            //    ) + slideIntoContainer(
            //        animationSpec = tween(300, easing = EaseIn),
            //        towards = AnimatedContentTransitionScope.SlideDirection.Start
            //    )
            //},
            //exitTransition = {
            //    fadeOut(
            //        animationSpec = tween(
            //            300, easing = LinearEasing
            //        )
            //    ) + slideOutOfContainer(
            //        animationSpec = tween(300, easing = EaseOut),
            //        towards = AnimatedContentTransitionScope.SlideDirection.End
            //    )
            //}
        ){
            WalkDetailContent(walkViewModel = walkViewModel, navController)
        }
        composable("dailyPostScreen"){
            DailyPostScreen(viewModel = communityViewModel, sharedViewModel = sharedViewModel, navController = navController)
        }
    }

    if (G.dupleLogin){
        AlertDialog(
            onDismissRequest = { },
            buttons = { },
            title = {
                Text(
                    text = "다른 기기에서 로그인",
                    fontFamily = FontFamily(Font(R.font.pretendard_bold))
                ) },
            text = {
                Text(
                    text = "잠시 후 로그아웃 됩니다...${count}",
                    fontFamily = FontFamily(Font(R.font.pretendard_regular))
                )},
            backgroundColor = design_white,
            contentColor = design_login_text
        )
    }
}

sealed class Screen(val route: String) {
    object Intro : Screen("intro")
    object Login : Screen("login")
    object IdFindScreen : Screen("idFindScreen")
    object PwFindScreen : Screen("pwFindScreen")
    object IdPwSearch : Screen("idPwSearch")
    object UserCreate : Screen("userCreate")
    object PetCreateScreen : Screen("petCreateScreen")
    object PetKindContent : Screen("petKindContent")
    object LocationPickContent : Screen("locationPickContent")
    object MainScreen : Screen("mainScreen")
    object WalkWithMap : Screen("walkWithMap")
    object PostScreen : Screen("postScreen")
    object EasyRegScreen : Screen("easyRegScreen")
    object StoryDetail : Screen("storyDetail")
    object EventDetail : Screen("eventDetail")
    object EventEndDetail : Screen("eventEndDetail")
    object NotiDetail : Screen("notiDetail")
    object InquiryDetail : Screen("inquiryDetail")
    object OneNOneScreen : Screen("oneNOneScreen")
    object SettingScreen : Screen("settingScreen")
    object UserInfoScreen : Screen("userInfoScreen")
    object InviteScreen : Screen("inviteScreen")
    object SetKeyScreen : Screen("setKeyScreen")
    object AddPetScreen : Screen("addPetScreen")
    object WalkDetailContent : Screen("walkDetailContent")
    object DailyPostScreen : Screen("dailyPostScreen")
    object WalkScreen : Screen("walkScreen")

}

sealed class BottomNav(val route: String, val title: String, val unSelectedIcon: Int, val selectedIcon: Int,){
    object HomeScreen : BottomNav("home", "홈", R.drawable.home, R.drawable.home_active)
    object TimelineScreen : BottomNav("timeline", "산책", R.drawable.walk, R.drawable.walk_active)
    object CommuScreen : BottomNav("commu", "커뮤니티", R.drawable.community, R.drawable.community_active)
    object MyScreen : BottomNav("my", "MY", R.drawable.mypage, R.drawable.mypage_active)
}
