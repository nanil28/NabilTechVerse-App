package com.nabiltechverse.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat

private const val SITE_URL = "https://nabiltechverse.blogspot.com/"

class MainActivity : ComponentActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Keep Android system bars outside the webpage instead of drawing over its header.
        WindowCompat.setDecorFitsSystemWindows(window, true)
        window.statusBarColor = android.graphics.Color.rgb(5, 12, 9)
        window.navigationBarColor = android.graphics.Color.rgb(5, 12, 9)
        window.decorView.systemUiVisibility = 0

        setContent {
            var webView by remember { mutableStateOf<WebView?>(null) }
            var progress by remember { mutableIntStateOf(0) }
            var pageLoading by remember { mutableStateOf(true) }

            BackHandler(enabled = webView?.canGoBack() == true) {
                webView?.goBack()
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF050C09))
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        WebView(context).apply {
                            webView = this
                            setBackgroundColor(android.graphics.Color.rgb(5, 12, 9))
                            isVerticalScrollBarEnabled = false
                            isHorizontalScrollBarEnabled = false

                            settings.apply {
                                javaScriptEnabled = true
                                domStorageEnabled = true
                                databaseEnabled = true
                                loadsImagesAutomatically = true
                                javaScriptCanOpenWindowsAutomatically = true
                                setSupportMultipleWindows(false)
                                allowFileAccess = false
                                allowContentAccess = true
                                useWideViewPort = true
                                loadWithOverviewMode = true
                                builtInZoomControls = false
                                displayZoomControls = false
                                cacheMode = WebSettings.LOAD_DEFAULT
                                mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
                            }

                            webViewClient = WebViewClient()
                            webChromeClient = object : WebChromeClient() {
                                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                    progress = newProgress
                                    pageLoading = newProgress < 100
                                }
                            }
                            loadUrl(SITE_URL)
                        }
                    },
                    update = { webView = it }
                )

                if (pageLoading) {
                    LinearProgressIndicator(
                        progress = { progress / 100f },
                        modifier = Modifier.fillMaxSize().then(Modifier),
                        color = Color(0xFF39FF14),
                        trackColor = Color.Transparent
                    )
                }
            }
        }
    }
}
