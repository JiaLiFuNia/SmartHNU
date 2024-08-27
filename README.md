# 师韵
![GitHub repo file or directory count](https://img.shields.io/github/directory-file-count/JiaLiFuNia/SmartHNU)
## 简介

这是一款HNU新闻阅览与第三方教务系统app。旨在为学生浏览学校（[HNU河南师范大学](https://www.htu.edu.cn/)）发布的新闻和通知，以及收取个人教务信息提供便捷的服务。

本应用基于 Jetpack Compose 框架开发，使用 Kotlin 语言编写。

本应用会在本地存储用户敏感数据（如：学号、密码等）。这些信息仅用于获取你的教务信息，不会被用于其他用途。你可以选择不登录教务系统，只浏览新闻和通知。

## 数据来源

新闻数据：[河南师范大学官网](https://www.htu.edu.cn/) 、 [河南师范大学教务处](https://www.htu.edu.cn/teaching/main.htm)

教务数据：[河南师大智慧教务](https://jwc.htu.edu.cn/app/)

## 功能介绍（包括未开发的功能）

### 1. 新闻阅览

使用 Kotlin+Retrofit 获取了HNU官网的部分新闻。新闻详情采用了 Android 原生 WebView 直接浏览网页和 [Readability4J库](https://github.com/dankito/Readability4J) 解析获取正文文本两种方式。

### 2.成绩查询

用于查看各科的学分、成绩、绩点、排名、平时成绩、期末成绩及其占比等等

### 3.今日课表

用于查看当天课程安排

### 4.其他功能

包括上课任务、培养计划、教学评价、教室查询、课程查询、教材选订等

### 5.第二课堂

由于第二课堂和教务系统采取了不同的登录平台，以及在登录时需要使用图形验证码，因此在本 app 使用第二课堂板块时，需要重新登录以获取第二课堂的数据。

### 6.消息中心

消息中心包含了教务系统信息（如：调课申请、成绩更新等）

## 界面预览



## 下载链接

[GitHub](https://github.com/JiaLiFuNia/SmartHNU/releases/latest) 

[123云盘](https://www.123pan.com/s/uyHuVv-dTdjH.html)

## 声明

本应用仅供学习交流使用。如有侵权，请联系我删除。

## 参考项目
- AndroidX 项目
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Kotlin](https://github.com/JetBrains/kotlin)
- [Material Design](https://github.com/material-components/material-components-android)
- [Retrofit2](https://github.com/square/retrofit)
- [OkHttp](https://github.com/square/okhttp)
- [Gson](https://github.com/google/gson)
- [Accompanist](https://github.com/google/accompanist)
- [Readability4J](https://github.com/dankito/Readability4J)
- [crux](https://github.com/chimbori/crux)
- [vico](https://github.com/patrykandpatrick/vico)
- [Coil](https://github.com/coil-kt/coil)
