package com.nabiltechverse.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView

private const val SITE_URL = "https://nabiltechverse.blogspot.com/"

class MainActivity : ComponentActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.rgb(5, 12, 9)
        window.navigationBarColor = android.graphics.Color.rgb(5, 12, 9)
        setContent {
            var webView by remember { mutableStateOf<WebView?>(null) }
            var loading by remember { mutableStateOf(true) }
            var currentUrl by remember { mutableStateOf(SITE_URL) }
            val dark = Color(0xFF050C09)
            val neon = Color(0xFF39FF14)

            BackHandler(enabled = webView?.canGoBack() == true) {
                webView?.goBack()
            }

            MaterialTheme(colorScheme = darkColorScheme(
                primary = neon,
                background = dark,
                surface = Color(0xFF0B1710),
                onPrimary = Color.Black,
                onBackground = Color.White,
                onSurface = Color.White
            )) {
                Scaffold(
                    containerColor = dark,
                    topBar = {
                        Surface(color = dark, shadowElevation = 4.dp) {
                            Row(
                                modifier = Modifier.fillMaxWidth().height(54.dp).padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("NabilTechVerse", color = neon, style = MaterialTheme.typography.titleLarge)
                                Spacer(Modifier.weight(1f))
                                if (loading) CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = neon,
                                    strokeWidth = 2.dp
                                )
                            }
                        }
                    },
                    bottomBar = {
                        NavigationBar(containerColor = Color(0xFF0B1710), contentColor = neon) {
                            NavigationBarItem(
                                selected = true,
                                onClick = { webView?.loadUrl(SITE_URL) },
                                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                label = { Text("Home") }
                            )
                            NavigationBarItem(
                                selected = false,
                                onClick = {
                                    webView?.evaluateJavascript(
                                        "document.querySelector('input[type=search],input[placeholder*=Search],input[aria-label*=Search]')?.focus();",
                                        null
                                    )
                                },
                                icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                                label = { Text("Search") }
                            )
                            NavigationBarItem(
                                selected = false,
                                onClick = { webView?.reload() },
                                icon = { Icon(Icons.Default.Refresh, contentDescription = "Refresh") },
                                label = { Text("Refresh") }
                            )
                        }
                    }
                ) { padding ->
                    Box(Modifier.fillMaxSize().padding(padding)) {
                        AndroidView(
                            modifier = Modifier.fillMaxSize(),
                            factory = { context ->
                                WebView(context).apply {
                                    webView = this
                                    settings.javaScriptEnabled = true
                                    settings.domStorageEnabled = true
                                    settings.loadsImagesAutomatically = true
                                    settings.javaScriptCanOpenWindowsAutomatically = true
                                    settings.setSupportMultipleWindows(false)
                                    settings.allowFileAccess = false
                                    settings.allowContentAccess = true
                                    webViewClient = object : WebViewClient() {
                                        override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                                            loading = true
                                            if (url != null) currentUrl = url
                                        }
                                        override fun onPageFinished(view: WebView?, url: String?) {
                                            loading = false
                                        }
                                    }
                                    webChromeClient = WebChromeClient()
                                    loadUrl(SITE_URL)
                                }
                            },
                            update = { webView = it }
                        )
                    }
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Compose BackHandler handles WebView navigation.
        super.onBackPressed()
    }
}