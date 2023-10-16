@file:OptIn(ExperimentalMaterial3Api::class)

package kr.carepet.app.navi.screens.mainscreen

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kr.carepet.app.navi.BottomNav
import kr.carepet.app.navi.MapActivity
import kr.carepet.app.navi.R
import kr.carepet.app.navi.component.BackTopBar
import kr.carepet.app.navi.component.LogoTopBar
import kr.carepet.app.navi.ui.theme.design_bottomnav_text
import kr.carepet.app.navi.ui.theme.design_button_bg
import kr.carepet.app.navi.ui.theme.design_select_btn_border
import kr.carepet.app.navi.ui.theme.design_white
import kr.carepet.app.navi.viewmodel.CommunityViewModel
import kr.carepet.app.navi.viewmodel.HomeViewModel
import kr.carepet.app.navi.viewmodel.SettingViewModel
import kr.carepet.app.navi.viewmodel.SharedViewModel
import kr.carepet.app.navi.viewmodel.WalkViewModel
import kr.carepet.singleton.G
import kr.carepet.util.Log


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    sharedViewModel:SharedViewModel,
    walkViewModel: WalkViewModel,
    communityViewModel: CommunityViewModel,
    settingViewModel: SettingViewModel) {

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color = design_white)

    val bottomNavController = rememberNavController()

    var isFABVisible by rememberSaveable { mutableStateOf(true) }
    var topBarChange by rememberSaveable { mutableStateOf("") }
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }

    val init by sharedViewModel.init.collectAsState()

    val selectedPet by sharedViewModel.selectPet.collectAsState()
    val currentPet by sharedViewModel.currentPetInfo.collectAsState()

    // logoTopbar back on/off
    var backBtnOnLT by remember { mutableStateOf(false) }

    // backTopbar back on/off
    var backBtnOnBT by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(key1 = init){
        if (init){
            homeViewModel.updateIsLoading(true)
            delay(500)
            val result = sharedViewModel.loadCurrentPetInfo()
            sharedViewModel.loadPetInfo()
            homeViewModel.updateIsLoading(!result)
            sharedViewModel.updateInit(false)
        }
    }

    BackOnPressed()
    Scaffold(
        floatingActionButton = {

            AnimatedVisibility(
                visible = isFABVisible,
                enter = scaleIn(
                    animationSpec = tween(200,easing = FastOutLinearInEasing)
                ),
                exit = scaleOut(
                    animationSpec = tween(durationMillis = 200, easing = FastOutLinearInEasing)
                )
            ) {
                FloatingActionButton(
                    onClick = {
                        //navController.navigate(Screen.WalkWithMap.route)
                        G.mapPetInfo = currentPet
                        val intent = Intent(context,MapActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .size(60.dp),
                    backgroundColor = design_button_bg,
                    shape = CircleShape
                ) {
                    Column (
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ){
                        Icon(
                            painter = painterResource(id = R.drawable.icon_sole),
                            contentDescription = "",
                            tint = Color.Unspecified
                        )

                        Text(
                            text = "산책 GO",
                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                            fontSize = 12.sp,
                            letterSpacing = (-0.6).sp,
                            color = design_white
                        )
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            BottomNavigationComponent(
                navController = bottomNavController,
                onChange = {newValue -> isFABVisible=newValue},
                isFABVisible = isFABVisible,
                onTopbarChange = { newValue -> topBarChange = newValue }
            )
        },
        topBar = {
            when(topBarChange){
                "home","walk" ->
                    LogoTopBar(
                        petDetailData = selectedPet?:homeViewModel.emptyCurrentPet,
                        openBottomSheet = {newValue -> openBottomSheet = newValue},
                        backBtnOn = backBtnOnLT,
                        walkViewModel = walkViewModel,
                        backBtnOnChange = { newValue -> backBtnOnLT = newValue}
                    )
                "commu" ->
                    BackTopBar(title = "커뮤니티", navController = navController, false)
                "my" ->
                    BackTopBar(title = "마이페이지", navController = navController, false)
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // 여기에 해당 탭에 맞는 컴포저블을 띄우는 코드 작성
            NavHost(
                navController = bottomNavController,
                startDestination = BottomNav.HomeScreen.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(BottomNav.HomeScreen.route) {
                    HomeScreen(
                        navController = navController,
                        viewModel = homeViewModel,
                        sharedViewModel = sharedViewModel,
                        backChange = { newValue -> backBtnOnLT = newValue },
                        openBottomSheet = openBottomSheet,
                        onDissMiss = {newValue -> openBottomSheet = newValue},
                        bottomNavController = bottomNavController
                    )
                }
                composable(BottomNav.WalkScreen.route) {
                    WalkScreen(
                        navController = navController,
                        walkViewModel = walkViewModel,
                        sharedViewModel = sharedViewModel,
                        backBtnOn = {newValue -> backBtnOnLT = newValue},
                        openBottomSheet = openBottomSheet,
                        onDissMiss = {newValue -> openBottomSheet = newValue}
                    )
                }
                composable(BottomNav.CommuScreen.route) {
                    CommuScreen(
                        navController = navController,
                        communityViewModel = communityViewModel,
                        sharedViewModel = sharedViewModel
                    )
                }
                composable(BottomNav.MyScreen.route) {
                    MyScreen(
                        navController = navController,
                        viewModel = settingViewModel,
                        sharedViewModel = sharedViewModel
                    )
                }

            }
        }
    }
}

@Composable
fun BottomNavigationComponent(
    navController: NavHostController,
    onChange: (Boolean) -> Unit,
    isFABVisible: Boolean,
    onTopbarChange: (String) -> Unit
) {
    val items = listOf(
        BottomNav.HomeScreen,
        BottomNav.WalkScreen,
        BottomNav.CommuScreen,
        BottomNav.MyScreen
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    currentDestination?.route.let { it ->
        when(it){
            BottomNav.HomeScreen.route -> {
                onChange(true)
                onTopbarChange("home")
            }
            BottomNav.WalkScreen.route -> {
                onChange(false)
                onTopbarChange("walk")
            }
            BottomNav.CommuScreen.route -> {
                onChange(false)
                onTopbarChange("commu")
            }
            BottomNav.MyScreen.route -> {
                onChange(false)
                onTopbarChange("my")
            }
        }
    }

    val selectedIndex = items.indexOfFirst { screen -> currentDestination?.hierarchy?.any { it.route == screen.route } == true }

    BottomNavigation(
        backgroundColor = design_white,
        elevation = 8.dp,
        modifier = Modifier.height(60.dp)
    ) {
        items.forEachIndexed { index, screen ->
            val isSelected = currentDestination?.hierarchy?.any {
                it.route == screen.route
            } == true

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
                    .clickable {
                        navController.navigate(screen.route) {
                            navController.graph.startDestinationRoute?.let {
                                popUpTo(it) { saveState = true }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
            ) {
                if (isSelected){
                    Box(
                        modifier = Modifier
                            .height(4.dp)
                            .width(40.dp)
                            .background(design_select_btn_border, shape = RoundedCornerShape(5.dp))
                            .clip(RoundedCornerShape(5.dp))
                            .align(Alignment.TopCenter))
                }

                Column (modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){
                    val iconPainter = if (isSelected) {
                        // 선택된 상태의 아이콘
                        screen.selectedIcon
                    } else {
                        // 선택되지 않은 상태의 아이콘
                        screen.unSelectedIcon
                    }
                    // 아이콘
                    Icon(
                        painter = painterResource(id = iconPainter),
                        contentDescription = "Navigation Icon",
                        modifier = Modifier.padding(top = 10.dp),
                        tint = Color.Unspecified
                    )

                    // 레이블
                    Text(
                        text = screen.title,
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        color = if(isSelected){design_select_btn_border}else{
                            design_bottomnav_text}
                    )
                }

            }

        }

    }
}

// 일단 사용안하는걸로
@Composable
fun RowScope.AddItem(
    screen: BottomNav,
    currentDestination: NavDestination?,
    navController: NavHostController,
    selectedIndex: Int,
    itemIndex: Int
) {
    val isSelected = currentDestination?.hierarchy?.any {
        it.route == screen.route
    } == true

    BottomNavigationItem(
        label = {
            Text(
                text = screen.title,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                color = if(isSelected){design_select_btn_border}else{
                    design_bottomnav_text}
            )
        },
        icon = {
            val iconPainter = if (isSelected) {
                // 선택된 상태의 아이콘
                screen.selectedIcon
            } else {
                // 선택되지 않은 상태의 아이콘
                screen.unSelectedIcon
            }
            androidx.compose.material.Icon(
                painter = painterResource(id = iconPainter),
                contentDescription = "Navigation Icon",
                modifier = Modifier.padding(bottom = 4.dp)
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        onClick = {
            navController.navigate(screen.route) {
                navController.graph.startDestinationRoute?.let {
                    popUpTo(it) { saveState = true }
                }
                launchSingleTop = true
                restoreState = true
            }
        },
        alwaysShowLabel = true,
        unselectedContentColor = Color.Unspecified,
        selectedContentColor = Color.Unspecified
    )

    // Indicator
    val indicatorWidth = 40.dp // Indicator width
    val indicatorHeight = 2.dp // Indicator height
    val indicatorOffset = selectedIndex * indicatorWidth // Calculate the offset based on the selected index

    Box(
        modifier = Modifier
            .width(indicatorWidth)
            .height(indicatorHeight)
    ) {
        Spacer(
            modifier = Modifier
                .width(indicatorWidth)
                .height(indicatorHeight)
                .background(Color.Blue) // Customize the indicator color here
                .offset(x = indicatorOffset) // Offset based on the selected index
        )
    }

}

@Composable
fun BackOnPressed() {
    val context = LocalContext.current
    var backPressedState by remember { mutableStateOf(true) }
    var backPressedTime = 0L

    BackHandler(enabled = backPressedState) {
        if(System.currentTimeMillis() - backPressedTime <= 1000L) {
            // 앱 종료
            (context as Activity).finish()
        } else {
            backPressedState = true
            Toast.makeText(context, "한 번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}

//@Composable
//public fun NavController.currentBackStackEntryAsState():State<NavBackStackEntry?>{
//    return currentBackStackEntryFlow.collectAsState(null)
//}