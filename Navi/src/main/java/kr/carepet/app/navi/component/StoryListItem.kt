package kr.carepet.app.navi.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kr.carepet.app.navi.R
import kr.carepet.app.navi.Screen
import kr.carepet.app.navi.screens.mainscreen.StoryList
import kr.carepet.app.navi.ui.theme.design_grad_end
import kr.carepet.app.navi.ui.theme.design_white

@Composable
fun StoryListItem(data:StoryList, navController:NavHostController){

    var sizeImage by remember { mutableStateOf(IntSize.Zero) }

    val gradient = Brush.verticalGradient(
        colors = listOf(Color.Transparent, design_grad_end),
        startY = sizeImage.height.toFloat()/5*3,
        endY = sizeImage.height.toFloat()
    )

    val imageUri= ""

    Box(
        modifier = Modifier
            .size(width = 200.dp, height = 280.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .onGloballyPositioned { sizeImage = it.size }
            .clickable { navController.navigate(Screen.StoryDetail.route) }
    ){
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUri)
                .crossfade(true)
                .build(),
            contentDescription = "",
            placeholder = painterResource(id = R.drawable.dog4),
            error= painterResource(id = R.drawable.dog4),
            modifier= Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(modifier = Modifier
            .matchParentSize()
            .background(gradient))

        Column (modifier= Modifier
            .width(160.dp)
            .wrapContentHeight()
            .align(Alignment.BottomCenter)
            .padding(bottom = 16.dp)
            .background(color = Color.Transparent)
        ){
            Text(
                text = data.title,
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                fontSize = 18.sp,
                letterSpacing = (-0.9).sp,
                color = design_white,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = data.petName,
                fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                fontSize = 14.sp,
                letterSpacing = (-0.7).sp,
                color = design_white,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.padding(bottom = 16.dp))

            Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){

                Icon(painter = painterResource(id = R.drawable.icon_like), contentDescription = "", tint = Color.Unspecified)

                Text(
                    text = data.likeCount,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 12.sp,
                    letterSpacing = (-0.6).sp,
                    color = design_white,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 4.dp)
                )

                Spacer(modifier = Modifier.padding(start = 16.dp))

                Icon(painter = painterResource(id = R.drawable.icon_comment), contentDescription = "", tint = Color.Unspecified)

                Text(
                    text = data.commentCount,
                    fontFamily = FontFamily(Font(R.font.pretendard_regular)),
                    fontSize = 12.sp,
                    letterSpacing = (-0.6).sp,
                    color = design_white,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 4.dp)
                )

            }

        }
    }

}
