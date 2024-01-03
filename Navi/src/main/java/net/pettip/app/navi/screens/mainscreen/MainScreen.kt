@file:OptIn(ExperimentalMaterial3Api::class)

package net.pettip.app.navi.screens.mainscreen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.FloatingActionButton
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import net.pettip.app.navi.BottomNav
import net.pettip.app.navi.R
import net.pettip.app.navi.Screen
import net.pettip.app.navi.component.BackTopBar
import net.pettip.app.navi.component.LogoTopBar
import net.pettip.app.navi.component.SearchBox
import net.pettip.app.navi.ui.theme.design_bottomnav_text
import net.pettip.app.navi.ui.theme.design_button_bg
import net.pettip.app.navi.ui.theme.design_select_btn_border
import net.pettip.app.navi.ui.theme.design_sharp
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.CommunityViewModel
import net.pettip.app.navi.viewmodel.HomeViewModel
import net.pettip.app.navi.viewmodel.SettingViewModel
import net.pettip.app.navi.viewmodel.SharedViewModel
import net.pettip.app.navi.viewmodel.WalkViewModel
import net.pettip.gps.app.GPSApplication
import net.pettip.singleton.G
import net.pettip.util.Log


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    sharedViewModel: SharedViewModel,
    walkViewModel: WalkViewModel,
    communityViewModel: CommunityViewModel,
    settingViewModel: SettingViewModel,
    data: Uri?
) {

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color = MaterialTheme.colorScheme.primary)

    val bottomNavController = rememberNavController()

    var isFABVisible by rememberSaveable { mutableStateOf(true) }
    var topBarChange by rememberSaveable { mutableStateOf("") }
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var calendarMode by rememberSaveable{ mutableStateOf(false) }

    var isSearching by remember { mutableStateOf(false) }
    val searchText by walkViewModel.searchText.collectAsState()

    val init by sharedViewModel.init.collectAsState()
    val dupleLogin by sharedViewModel.dupleLogin.collectAsState()

    val selectedPet by sharedViewModel.selectPet.collectAsState()
    val currentPet by sharedViewModel.currentPetInfo.collectAsState()
    val currentTab by sharedViewModel.currentTab.collectAsState()

    // logoTopbar back on/off
    var backBtnOnLT by remember { mutableStateOf(false) }

    val context = LocalContext.current

    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
        if (!task.isSuccessful) {
            Log.d("LOG", "Fetching FCM registration token failed", task.exception)
            return@OnCompleteListener
        }

        // Get new FCM registration token
        val token = task.result

      Log.d("LOG",token)
    })

    LaunchedEffect(key1 = init){
        if (init && !dupleLogin){

            homeViewModel.updateCurrentPetLoading(true)
            val result1 = sharedViewModel.loadCurrentPetInfo()
            if (result1){
                homeViewModel.updateCurrentPetLoading(false)
            }else{
                homeViewModel.updateCurrentPetLoading(false)
            }
            homeViewModel.updatePetLoading(true)
            val result2 = sharedViewModel.loadPetInfo()
            if (result2){
                homeViewModel.updatePetLoading(false)
            }else{
                homeViewModel.updatePetLoading(false)
            }

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

                Crossfade(
                    targetState = topBarChange,
                    label = ""
                ) { topBarChange ->
                    when(topBarChange){
                        "home","timeline" ->
                            FloatingActionButton(
                                onClick = {
                                    if (currentPet.isNotEmpty()){
                                        if (currentPet[0].ownrPetUnqNo==""){
                                            showDialog = true
                                        }else{
                                            G.mapPetInfo = currentPet
                                            GPSApplication.instance.openMap()
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .padding(16.dp)
                                    .size(65.dp),
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
                                        text = stringResource(R.string.walk_go),
                                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                        fontSize = 12.sp,
                                        letterSpacing = (-0.6).sp,
                                        color = design_white
                                    )
                                }
                            }

                        "commu" ->
                            AnimatedVisibility(
                                visible = currentTab == "스토리",
                                enter = scaleIn(
                                    animationSpec = tween(200,easing = FastOutLinearInEasing)
                                ),
                                exit = scaleOut(
                                    animationSpec = tween(durationMillis = 200, easing = FastOutLinearInEasing)
                                )
                            ){
                                FloatingActionButton(
                                    onClick = {
                                        if (currentPet.isNotEmpty()) {
                                            if (currentPet[0].ownrPetUnqNo=="") {

                                            }else{
                                                navController.navigate(Screen.DailyPostScreen.route)
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .size(65.dp),
                                    backgroundColor = design_sharp,
                                    shape = CircleShape
                                ) {
                                    Column (
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ){
                                        Icon(
                                            painter = painterResource(id = R.drawable.icon_daily),
                                            contentDescription = "",
                                            tint = Color.Unspecified
                                        )

                                        Text(
                                            text = stringResource(R.string.fab_daily),
                                            fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                                            fontSize = 12.sp,
                                            letterSpacing = (-0.6).sp,
                                            color = design_white
                                        )
                                    }
                                }
                            }
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
            Crossfade(
                targetState = topBarChange,
                label = "",
                animationSpec = tween(700)
            ) { topBarChange ->
                when(topBarChange){
                    "home" ->
                        LogoTopBar(
                            petDetailData = selectedPet?:homeViewModel.emptyCurrentPet,
                            openBottomSheet = {newValue -> openBottomSheet = newValue},
                            backBtnOn = backBtnOnLT,
                            walkViewModel = walkViewModel,
                            backBtnOnChange = { newValue -> backBtnOnLT = newValue},
                            sharedViewModel = sharedViewModel
                        )
                    "timeline" ->
                        Crossfade(
                            targetState = calendarMode,
                            label = ""
                        ) {
                            when(it){
                                true ->
                                    LogoTopBar(
                                        petDetailData = selectedPet?:homeViewModel.emptyCurrentPet,
                                        openBottomSheet = {newValue -> openBottomSheet = newValue},
                                        backBtnOn = backBtnOnLT,
                                        walkViewModel = walkViewModel,
                                        backBtnOnChange = { newValue -> backBtnOnLT = newValue},
                                        sharedViewModel = sharedViewModel
                                    )
                                false ->
                                    CenterAlignedTopAppBar(
                                        colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                                        modifier = Modifier.height(60.dp),
                                        title = {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                            ) {
                                                AnimatedVisibility(
                                                    visible = !isSearching,
                                                    enter = fadeIn(tween(500)),
                                                    exit = fadeOut(tween(500)),
                                                    modifier = Modifier.align(Alignment.Center)
                                                ) {
                                                    Text(
                                                        text = "PetTimeLine",
                                                        fontSize = 20.sp,
                                                        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                                                        letterSpacing = (-1.0).sp,
                                                        color = MaterialTheme.colorScheme.onPrimary,
                                                        modifier = Modifier.align(Alignment.Center),
                                                        textAlign = TextAlign.Center
                                                    )
                                                }

                                                AnimatedVisibility(
                                                    visible = isSearching,
                                                    enter = fadeIn(tween(500)),
                                                    exit = fadeOut(tween(500)),
                                                    modifier = Modifier.align(Alignment.Center)
                                                ) {
                                                    SearchBox(
                                                        viewModel = walkViewModel,
                                                        modifier = Modifier
                                                            .align(Alignment.Center)
                                                            .padding(start = 20.dp, end = 60.dp)
                                                    )
                                                }

                                                Icon(
                                                    painter = painterResource(id = R.drawable.icon_search),
                                                    contentDescription = "", tint = MaterialTheme.colorScheme.onPrimary,
                                                    modifier = Modifier
                                                        .align(Alignment.CenterEnd)
                                                        .padding(end = 20.dp)
                                                        .clickable {
                                                            if (isSearching) {
                                                                walkViewModel.viewModelScope.launch {
                                                                    walkViewModel.dailyLifeTimeLineListClear()
                                                                    walkViewModel.getTimeLineList()
                                                                    isSearching = false
                                                                }
                                                            } else {
                                                                isSearching = true
                                                            }
                                                            //isSearching = !isSearching
                                                        }
                                                )
                                            }
                                        }
                                    )
                            }
                        }
                    "commu" ->
                        BackTopBar(title = stringResource(R.string.title_commu), navController = navController, false)
                    "my" ->
                        BackTopBar(title = stringResource(R.string.title_mypage), navController = navController, false)
                }
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
                        bottomNavController = bottomNavController,
                        showDialog = showDialog,
                        showDialogChange = {newValue -> showDialog = newValue},
                        communityViewModel = communityViewModel
                    )
                }
                composable(BottomNav.TimelineScreen.route) {
                    Crossfade(
                        targetState = calendarMode,
                        label = ""
                    ) {
                        when(it){
                            true ->
                                WalkScreen(
                                    navController = navController,
                                    walkViewModel = walkViewModel,
                                    sharedViewModel = sharedViewModel,
                                    homeViewModel = homeViewModel,
                                    backBtnOn = {newValue -> backBtnOnLT = newValue},
                                    openBottomSheet = openBottomSheet,
                                    onDissMiss = {newValue -> openBottomSheet = newValue},
                                    modeChange = {newValue -> calendarMode = newValue}
                                )
                            false ->
                                TimelineScreen(
                                    viewModel= walkViewModel,
                                    isSearching = isSearching,
                                    dismiss = {newValue -> isSearching = newValue},
                                    navController = navController,
                                    modeChange = {newValue -> calendarMode = newValue}
                                )
                        }
                    }
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
        BottomNav.TimelineScreen,
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
            BottomNav.TimelineScreen.route -> {
                onChange(true)
                onTopbarChange("timeline")
            }
            BottomNav.CommuScreen.route -> {
                onChange(true)
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
        backgroundColor = MaterialTheme.colorScheme.tertiary,
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
                        tint = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else design_bottomnav_text
                    )

                    // 레이블
                    Text(
                        text = screen.title,
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                        color = if(isSelected){MaterialTheme.colorScheme.secondaryContainer}else{
                            design_bottomnav_text}
                    )
                }

            }

        }

    }
}

@Composable
fun BackOnPressed() {
    val context = LocalContext.current
    var backPressedState by remember { mutableStateOf(true) }
    var backPressedTime = 0L
    val closeCmt = stringResource(id = R.string.app_close_cmt)

    BackHandler(enabled = backPressedState) {
        if(System.currentTimeMillis() - backPressedTime <= 1000L) {
            // 앱 종료
            (context as Activity).finish()
        } else {
            backPressedState = true
            Toast.makeText(context, closeCmt, Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}

fun clearPushDataFromIntent() {
    val intent = Intent()
    intent.data = null
}