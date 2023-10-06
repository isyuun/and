package kr.carepet.app.navi.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import kr.carepet.app.navi.viewmodel.LoginViewModel

data class IDPWTabItem(
    val title: String,
    val screen: @Composable (NavHostController,LoginViewModel) -> Unit // navController를 매개변수로 추가
)

val IDPWTabItems = listOf(
    IDPWTabItem(title = "아이디 찾기", screen = { navController, viewModel -> IdFindScreen(navController, viewModel) }),
    IDPWTabItem(title = "비밀번호 찾기", screen = { navController, viewModel -> PwFindScreen(navController,viewModel) })
)