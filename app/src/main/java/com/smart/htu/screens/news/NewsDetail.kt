package com.smart.htu.screens.news

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.screens.webview.WebViewContent
import top.yukonga.miuix.kmp.basic.FloatingToolbar
import top.yukonga.miuix.kmp.basic.ToolbarPosition

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NewsDetail(
    url: String,
    title: String,
    newsViewModel: NewsViewModel,
    navController: NavController
) {
    WebViewContent(
        url = url,
        title = title,
        navController = navController,
        floatingToolbar = {
            FloatingToolbar {
                Column {
                    IconButton(onClick = { navController.navigate(Destinations.AIConfiguration.route) }) {
                        Icon(
                            painter = painterResource(R.drawable.wand_stars_24px),
                            contentDescription = "ai"
                        )
                    }
                    IconButton(onClick = { /* 操作 2 */ }) {
                        Icon(painterResource(R.drawable.star_24px), contentDescription = "star")
                    }
                }
            }
        },
        floatingToolbarPosition = ToolbarPosition.CenterEnd
    )
}