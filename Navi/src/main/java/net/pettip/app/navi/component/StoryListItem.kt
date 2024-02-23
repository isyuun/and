package net.pettip.app.navi.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
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
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.Screen
import net.pettip.app.navi.ui.theme.design_grad_end
import net.pettip.app.navi.ui.theme.design_white
import net.pettip.app.navi.viewmodel.CommunityViewModel
import net.pettip.data.daily.Story

@Composable
fun StoryListItem(data: Story, navController:NavHostController, viewModel:CommunityViewModel){

    var sizeImage by remember { mutableStateOf(IntSize.Zero) }
    val scope = rememberCoroutineScope()
    var lastClickTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    val context = LocalContext.current
    val density = context.resources.displayMetrics.density
    val width = (200 * density).toInt()
    val height = (280 * density).toInt()

    val gradient = Brush.verticalGradient(
        colors = listOf(Color.Transparent, design_grad_end),
        startY = sizeImage.height.toFloat()/5*3,
        endY = sizeImage.height.toFloat()
    )

    Box(
        modifier = Modifier
            .size(width = 200.dp, height = 280.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .onGloballyPositioned { sizeImage = it.size }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false),
                enabled = data.bldYn != "Y"
            ) {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastClickTime >= 500) {
                    lastClickTime = currentTime
                    scope.launch {

                        navController.navigate(Screen.StoryDetail.route)
                        viewModel.updateLastPstSn(data.schUnqNo)
                        viewModel.getStoryDetail(data.schUnqNo)
                    }
                }
            }
    ){
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(data.storyFile?:R.drawable.img_blank)
                .crossfade(400)
                .size(width,height) // Set the target size to load the image at.
                .build(),
            filterQuality = FilterQuality.Low
        )
        Image(
            painter = painter,
            modifier = Modifier.fillMaxSize(),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )

        Box(modifier = Modifier
            .matchParentSize()
            .background(gradient))

        Column (modifier= Modifier
            .padding(horizontal = 8.dp)
            .width(160.dp)
            .wrapContentHeight()
            .align(Alignment.BottomCenter)
            .padding(bottom = 16.dp)
            .background(color = Color.Transparent)
        ){
            Text(
                text = data.schTtl,
                fontFamily = FontFamily(Font(R.font.pretendard_medium)),
                fontSize = 18.sp,
                letterSpacing = (-0.9).sp,
                color = design_white,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = data.petNm,
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
                    text = data.rcmdtnCnt,
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
                    text = data.cmntCnt,
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
