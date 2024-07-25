package com.xhand.hnu.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.jeziellago.compose.markdowntext.MarkdownText

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideScreen(
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                title = {
                    Text(text = "帮助")
                },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(paddingValues = it)
                .verticalScroll(scrollState)
        ) {
            val markdownContent = """  
# 功能向导与使用说明

## 简介

这是一款第三方HNU教务系统app。为了便于学生及时获取学校（[HNU河南师范大学](https://www.htu.edu.cn/)）发布的新闻和通知，以及收取个人教务信息，本人为此开发了这款应用

本应用基于 Jetpack Compose 框架开发，使用 Kotlin 语言编写

本应用会在本地加密存储用户敏感数据（如：学号、密码等）。这些信息仅用于获取您的教务信息，不会被用于其他用途。您也可以选择不登录教务系统，只浏览新闻和通知

## 功能介绍（包括未开发的功能）

### 1. 新闻查询

使用 Kotlin+Retrofit 爬取了HNU官网的各类新闻，新闻详情采用了 Android 原生的 WebView 直接预览网页和 HTML 解析获取正文文本两种方式

### 2.成绩查询

学生可以随时查看各科学分、成绩、排名、平时成绩、期末成绩及其占比等等，并采用图表的形式展示学生的成绩变化。当教务系统更新新的成绩时，会发送通知（暂未实现）

### 3.课表查询

学生可以随时查看今天课程情况，获取当前学期课表，查询同期空闲教室等等

### 4.快捷方式

包括上课任务、学习计划、教学评价、教室查询、课程查询、教材选订等

### 5.第二课堂（未开发）

由于第二课堂和教务系统采取了不同的登录平台，以及在登录时需要使用图形验证码，因此在本 app 使用第二课堂板块时，你需要重新登录以获取第二课堂的数据

### 6.消息中心

消息中心包含了教务系统信息（如：调课申请、成绩更新等）、软件更新信息（如：最新版本、更新日志等）

## 界面预览

!["Screen"](/img/ScreenShot.png)

## 下载链接

师韵：[GitHub](https://github.com/JiaLiFuNia/SmartHNU/releases/latest)

                """
            MarkdownText(
                markdown = markdownContent.trimIndent(),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            )
        }
    }
}