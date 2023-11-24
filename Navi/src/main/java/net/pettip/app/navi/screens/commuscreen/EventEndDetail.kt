package net.pettip.app.navi.screens.commuscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import net.pettip.app.navi.R
import net.pettip.app.navi.component.BackTopBar
import net.pettip.app.navi.ui.theme.design_login_text
import net.pettip.app.navi.ui.theme.design_textFieldOutLine
import net.pettip.app.navi.ui.theme.design_white

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventEndDetail(navController: NavHostController){

    val dummy = arrayListOf(
        "권 * 주 2881",
        "권 * 주 2881",
        "권 * 주 2881",
        "권 * 주 2881",
        "권 * 주 2881",
        "권 * 주 2881",
        "권 * 주 2881",
        "권 * 주 2881",
        "권 * 주 2881",
        "권 * 주 2881",
        "권 * 주 2881",
        "권 * 주 2881",
        "권 * 주 2881",
        "권 * 주 2881"
    )

    Scaffold (
        topBar = { BackTopBar(title = stringResource(R.string.title_event), navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = design_white)
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(id = R.drawable.event_thumb1),
                contentDescription = "",
                modifier= Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            
            Text(
                text = "본아페티 관절 영양제 무료 체험단",
                fontFamily = FontFamily(Font(R.font.pretendard_bold)),
                fontSize = 24.sp,
                letterSpacing = (-1.2).sp,
                color = design_login_text,
                modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp)
            )
            
            Spacer(modifier = Modifier
                .padding(top = 20.dp, bottom = 20.dp, start = 20.dp, end = 20.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(design_textFieldOutLine))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = rememberLazyGridState(),
                contentPadding = PaddingValues(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ){
                items(dummy){ item ->  
                    EventEndDetailSubText(text = item)
                }
            }
        }//col

    }

}

@Composable
fun EventEndDetailSubText(text:String){
    Text(
        text = text,
        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
        fontSize = 14.sp, letterSpacing = (-0.7).sp,
        color = design_login_text,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}