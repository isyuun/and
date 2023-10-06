package kr.carepet.app.navi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kr.carepet.app.navi.screens.EasyRegScreen
import kr.carepet.app.navi.screens.IdFindScreen
import kr.carepet.app.navi.screens.IdPwSearchScreen
import kr.carepet.app.navi.screens.IntroScreen
import kr.carepet.app.navi.screens.LocationPickContent
import kr.carepet.app.navi.screens.LoginScreen
import kr.carepet.app.navi.screens.PetCreateScreen
import kr.carepet.app.navi.screens.PetKindContent
import kr.carepet.app.navi.screens.PwFindScreen
import kr.carepet.app.navi.screens.UserCreateScreen
import kr.carepet.app.navi.screens.commuscreen.EventDetail
import kr.carepet.app.navi.screens.commuscreen.EventEndDetail
import kr.carepet.app.navi.screens.commuscreen.StoryDetail
import kr.carepet.app.navi.screens.mainscreen.MainScreen
import kr.carepet.app.navi.screens.mainscreen.SettingScreen
import kr.carepet.app.navi.screens.mainscreen.WalkWithMap
import kr.carepet.app.navi.screens.myscreen.InquiryDetail
import kr.carepet.app.navi.screens.myscreen.InviteScreen
import kr.carepet.app.navi.screens.myscreen.NotiDetail
import kr.carepet.app.navi.screens.myscreen.OneNOneScreen
import kr.carepet.app.navi.screens.myscreen.PetProfileScreen
import kr.carepet.app.navi.screens.myscreen.SetKeyScreen
import kr.carepet.app.navi.screens.myscreen.UserInfoScreen
import kr.carepet.app.navi.screens.walkscreen.PostScreen
import kr.carepet.app.navi.ui.theme.AppTheme
import kr.carepet.app.navi.viewmodel.CommunityViewModel
import kr.carepet.app.navi.viewmodel.HomeViewModel
import kr.carepet.app.navi.viewmodel.LoginViewModel
import kr.carepet.app.navi.viewmodel.SettingViewModel
import kr.carepet.app.navi.viewmodel.SharedViewModel
import kr.carepet.app.navi.viewmodel.UserCreateViewModel
import kr.carepet.app.navi.viewmodel.WalkViewModel
import kr.carepet.data.SCDLocalData

class MainActivity : ComponentActivity() {

    private val loginViewModel:LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface {
                    MyApp(loginViewModel)
                }
            }
        }
    }
}


@Composable
fun MyApp(viewModel: LoginViewModel){
    val navController = rememberNavController()

    AppNavigation(navController = navController, viewModel)
}


@Composable
fun AppNavigation(navController: NavHostController, viewModel: LoginViewModel){

    val scdLocalData = remember { kr.carepet.data.SCDLocalData() }

    val userCreateViewModel = remember{UserCreateViewModel(scdLocalData)}
    val sharedViewModel = remember{ SharedViewModel() }
    val homeViewModel = remember { HomeViewModel(sharedViewModel) }
    val walkViewModel = remember{WalkViewModel(sharedViewModel)}
    val communityViewModel = remember{CommunityViewModel(sharedViewModel)}
    val settingViewModel = remember{SettingViewModel(sharedViewModel)}

    NavHost(navController = navController, startDestination = "intro"){
        composable("intro"){

            IntroScreen(navController = navController, viewModel = sharedViewModel, loginViewModel = viewModel)
        }
        composable("login"){
            LoginScreen(navController = navController, viewModel)
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

            PetCreateScreen(navController = navController, viewModel = userCreateViewModel)
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

        composable("walkWithMap"){

            WalkWithMap(walkViewModel, navController)
        }

        composable("postScreen"){
            val context = LocalContext.current
            PostScreen(walkViewModel, navController)
        }

        composable("easyRegScreen"){
            EasyRegScreen(navController = navController, viewModel = viewModel, userCreateViewModel = userCreateViewModel)
        }

        composable("storyDetail"){
            StoryDetail(viewModel = communityViewModel, sharedViewModel = sharedViewModel, navController = navController)
        }
        composable("eventDetail"){
            EventDetail(navController = navController)
        }
        composable("eventEndDetail"){
            EventEndDetail(navController = navController)
        }
        composable("notiDetail"){
            NotiDetail(navController = navController)
        }
        composable("inquiryDetail"){
            InquiryDetail(navController = navController)
        }
        composable("oneNOneScreen"){
            OneNOneScreen(navController = navController, settingViewModel = settingViewModel)
        }
        composable("settingScreen"){
            SettingScreen(navController = navController, viewModel = settingViewModel)
        }
        composable("userInfoScreen"){
            UserInfoScreen(navController = navController, settingViewModel = settingViewModel)
        }
        composable("petProfileScreen/{index}"){ backStackEntry ->
            PetProfileScreen(navController = navController, sharedViewModel = sharedViewModel, backStackEntry.arguments?.getString("index"))
        }
        composable("inviteScreen"){
            InviteScreen(navController = navController, settingViewModel = settingViewModel)
        }
        composable("setKeyScreen"){
            SetKeyScreen(navController = navController)
        }
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
    object PetProfileScreen : Screen("petProfileScreen/{index}")
}

sealed class BottomNav(val route: String, val title: String, val unSelectedIcon: Int, val selectedIcon: Int,){
    object HomeScreen : BottomNav("home", "홈", R.drawable.home, R.drawable.home_active)
    object WalkScreen : BottomNav("walk", "산책", R.drawable.walk, R.drawable.walk_active)
    object CommuScreen : BottomNav("commu", "커뮤니티", R.drawable.community, R.drawable.community_active)
    object MyScreen : BottomNav("my", "MY", R.drawable.mypage, R.drawable.mypage_active)
}


private fun share(context: Context, title: String, summary: String) {
    // Create an ACTION_SEND implicit intent with order details in the intent extras
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TITLE, title)
        putExtra(Intent.EXTRA_TEXT, summary)
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            "walk send"
        )
    )
}
