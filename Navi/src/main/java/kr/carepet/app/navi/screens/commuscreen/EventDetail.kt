package kr.carepet.app.navi.screens.commuscreen

import android.util.Base64
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kr.carepet.app.navi.R
import kr.carepet.app.navi.component.BackTopBar
import kr.carepet.app.navi.ui.theme.design_button_bg
import kr.carepet.app.navi.ui.theme.design_login_text
import kr.carepet.app.navi.ui.theme.design_skip
import kr.carepet.app.navi.ui.theme.design_textFieldOutLine
import kr.carepet.app.navi.ui.theme.design_white
import kr.carepet.app.navi.viewmodel.CommunityViewModel
import kr.carepet.util.Log

@Composable
fun EventDetail(navController: NavHostController, viewModel: CommunityViewModel) {

    val detailData by viewModel.eventDetail.collectAsState()

    Scaffold(
        topBar = { BackTopBar(title = stringResource(R.string.title_event), navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = design_white)
                .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                onLoading = { },
                onError = { Log.d("LOG", "onError") },
                onSuccess = { Log.d("LOG", "onSuccess") },
                model = ImageRequest.Builder(LocalContext.current)
                    .data(detailData?.data?.bbsEvntDtl?.get(0)?.rprsImgUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                placeholder = painterResource(id = R.drawable.profile_default),
                error = painterResource(id = R.drawable.profile_default),
                modifier = Modifier.fillMaxSize(),
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

            Spacer(
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 20.dp, start = 20.dp, end = 20.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(design_textFieldOutLine)
            )


            // ------------------------ html 문 들어갈 자리 --------------------------
            val pstCn = detailData?.data?.bbsEvntDtl?.get(0)?.pstCn
            pstCn?.let { WebViewHtml(html = it, modifier = Modifier.fillMaxSize()) }
            // ------------------------ html 문 들어갈 자리 --------------------------


            Spacer(modifier = Modifier.padding(top = 40.dp))

            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 20.dp), shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = design_button_bg)
            )
            {
                Text(text = stringResource(R.string.event_apply), color = design_white, fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.pretendard_regular)))
            }
        }//col

    }

}

@Composable
fun WebViewUrl(url: String, data: String, modifier: Modifier) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                postUrl(url, Base64.encode(data.toByteArray(), Base64.DEFAULT))
            }
        },
        modifier = modifier
    )
}

@Composable
fun WebViewHtml(html: String, modifier: Modifier) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                loadData(html, "text/html; charset=utf-8", "UTF-8")
            }
        },
        modifier = modifier
    )
}

@Composable
fun EventDetailMainText(text: String, bottomPadding: Int) {
    Text(
        text = text,
        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
        fontSize = 14.sp, letterSpacing = (-0.7).sp,
        color = design_skip,
        modifier = Modifier.padding(bottom = bottomPadding.dp),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun EventDetailSubText(text: String, bottomPadding: Int) {
    Text(
        text = text,
        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
        fontSize = 14.sp, letterSpacing = (-0.7).sp,
        color = design_login_text,
        modifier = Modifier.padding(bottom = bottomPadding.dp),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun HtmlText(html: String, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context -> TextView(context) },
        update = { it.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT) }
    )
}