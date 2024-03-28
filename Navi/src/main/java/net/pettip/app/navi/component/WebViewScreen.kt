package net.pettip.app.navi.component

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import net.pettip.singleton.BASE_URL

/**
 * @Project     : PetTip-Android
 * @FileName    : WebViewScreen
 * @Date        : 2024-01-15
 * @author      : CareBiz
 * @description : net.pettip.app.navi.component
 * @see net.pettip.app.navi.component.WebViewScreen
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(page:Int) {
    val url1 = "${BASE_URL}terms"
    val url2 = "${BASE_URL}privacy_policy"
    val url3 = "${BASE_URL}loc-based-terms"
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.setSupportZoom(true)

                // WebView hardware 가속 비활성화
                //setLayerType(View.LAYER_TYPE_SOFTWARE,null)
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