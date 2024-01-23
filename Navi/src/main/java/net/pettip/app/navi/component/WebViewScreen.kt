package net.pettip.app.navi.component

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

/**
 * @Project     : PetTip-Android
 * @FileName    : WebViewScreen
 * @Date        : 2024-01-15
 * @author      : CareBiz
 * @description : net.pettip.app.navi.component
 * @see net.pettip.app.navi.component.WebViewScreen
 */
@Composable
fun WebViewScreen(page:Int){
    val url1 = "http://carepet.hopto.org/terms"
    val url2 = "https://www.google.co.kr/?hl=ko"
    val url3 = "http://carepet.hopto.org"
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true

                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.setSupportZoom(true)

            }
        },
        update = { webView ->
            when(page){
                1 -> webView.loadUrl(url1)
                2 -> webView.loadUrl(url2)
                3 -> webView.loadUrl(url3)
            }
        }
    )
}