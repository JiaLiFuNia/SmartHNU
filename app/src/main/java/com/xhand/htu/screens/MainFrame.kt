package com.xhand.htu.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.xhand.htu.model.entity.NavigationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainFrame(
    onNavigateToArticle: () -> Unit = {}
) {

    val navigationItems = listOf(
        NavigationItem(title = "新闻", icon = Icons.Filled.Home),
        //NavigationItem(title = "教务", icon = Icons.Filled.DateRange),
        NavigationItem(title = "我的", icon = Icons.Filled.Person),
        NavigationItem(title = "设置", icon = Icons.Filled.Settings)
    )

    var currentNavigationIndex by remember {
        mutableStateOf(0)
    }

    Scaffold(
        bottomBar = {
        NavigationBar {
            navigationItems.forEachIndexed{index, navigationItem ->
                NavigationBarItem(
                    selected = currentNavigationIndex == index,
                    onClick = {
                        currentNavigationIndex = index
                    },
                    icon = {
                        Icon(
                            imageVector = navigationItem.icon,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = navigationItem.title)
                    },
                    alwaysShowLabel = false
                )
            }
        }
    }) {
        Box(modifier = Modifier.padding(it)) {
            when(currentNavigationIndex){
                0 -> NewsScreen(onNavigateToArticle = onNavigateToArticle)
                1 -> PersonScreen()
                2 -> SettingScreen()
            }
        }

    }

}

@Preview
@Composable
fun MainFramePreview() {
    MainFrame()
}