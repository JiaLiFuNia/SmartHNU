package com.xhand.hnu.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.xhand.hnu.R
import com.xhand.hnu.components.BasicListItem
import com.xhand.hnu.components.DropdownListItem
import com.xhand.hnu.components.SelectionItem
import com.xhand.hnu.components.ShowAlert
import com.xhand.hnu.components.ShowLoginDialog
import com.xhand.hnu.components.SwitchListItem
import com.xhand.hnu.components.UpdateDialog
import com.xhand.hnu.model.entity.DarkMode
import com.xhand.hnu.viewmodel.SettingsViewModel

fun DarkMode.toStringResourceId(): Int {
    return when (this) {
        DarkMode.SYSTEM -> R.string.follow_system
        DarkMode.ON -> R.string.turn_on
        DarkMode.OFF -> R.string.turn_off
    }
}

@Composable
fun NavigationSettingScreen(viewModel: SettingsViewModel) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "setting_screen",
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(500)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(500)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(500)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(500)
            )
        }
    ) {
        composable("setting_screen") {
            SettingScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable("guide_screen") {
            GuideScreen(onBack = { navController.popBackStack() })
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(viewModel: SettingsViewModel, navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val cbManager = LocalClipboardManager.current
    // 获取SystemUiController
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = isSystemInDarkTheme()
    val statueBarColor = colorScheme.surfaceContainer
    // 设置状态栏颜色
    SideEffect {
        systemUiController.setStatusBarColor(color = statueBarColor)
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.surfaceContainer
                ),
                title = {
                    Text(text = "设置")
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = it)
                .verticalScroll(scrollState)
        ) {
            BasicListItem(leadingText = "账户")
            BasicListItem(
                headlineText = if (viewModel.isLoginSuccess) {
                    "退出"
                } else {
                    "登录"
                },
                supportingText = if (viewModel.isLoginSuccess) {
                    "点击退出登录"
                } else {
                    "请先登录"
                },
                leadingImageVector = if (viewModel.isLoginSuccess) {
                    R.drawable.ic_outline_account_circle
                } else {
                    R.drawable.ic_outline_no_accounts_24
                },
                onClick = {
                    if (viewModel.isLoginSuccess) {
                        viewModel.isShowAlert = true
                    } else {
                        viewModel.isShowDialog = true
                        viewModel.username = ""
                        viewModel.password = ""
                    }
                }
            )
            BasicListItem(leadingText = "显示")
            SwitchListItem(
                leadingImageVector = R.drawable.ic_palette,
                headlineText = "系统主题色",
                supportingText = "开启后将跟随系统主题色",
                value = true,
                onValueChanged = { }
            )
            val darkModeIndex = remember {
                mutableIntStateOf(0)
            }
            DropdownListItem(
                leadingImageVector = R.drawable.ic_nightlight,
                headlineText = "深色模式",
                value = DarkMode.values()[darkModeIndex.intValue],
                selections = DarkMode.values()
                    .map { SelectionItem(stringResource(it.toStringResourceId()), it) },
                onValueChanged = { index, _ ->
                    darkModeIndex.intValue = index
                }
            )
            BasicListItem(leadingText = stringResource(R.string.about))
            BasicListItem(
                headlineText = "使用引导",
                supportingText = "功能向导与说明",
                leadingImageVector = R.drawable.ic_outline_info,
                onClick = {
                    navController.navigate("guide_screen")
                }
            )
            BasicListItem(
                headlineText = stringResource(R.string.app_name),
                supportingText = "Copyright 2023-2024 Xhand v2.0.7.9_beta.17",
                leadingImageVector = R.drawable.ic_outline_article,
                onClick = {
                    Intent(Intent.ACTION_VIEW).also {
                        it.data = Uri.parse("https://github.com/JiaLiFuNia/SmartHNU")
                        if (it.resolveActivity(context.packageManager) != null) {
                            context.startActivity(it)
                        }
                    }
                }
            )
            BasicListItem(
                headlineText = "检查更新",
                supportingText = "点击以检查更新",
                leadingImageVector = R.drawable.ic_refresh,
                onClick = {
                    viewModel.updateRes("2.0.7.9_beta.17")
                    if (viewModel.ifNeedUpdate)
                        viewModel.isShowUpdateDialog = true
                    else
                        Toast.makeText(context, "已是最新版本", Toast.LENGTH_SHORT).show()
                }
            )
            BasicListItem(
                headlineText = stringResource(R.string.dev_title),
                supportingText = stringResource(R.string.dev_name),
                leadingImageVector = R.drawable.ic_outline_person,
                onClick = {
                    Intent(Intent.ACTION_VIEW).also {
                        it.data = Uri.parse("https://xhand.fun")
                        if (it.resolveActivity(context.packageManager) != null) {
                            context.startActivity(it)
                        }
                    }
                }
            )
            BasicListItem(
                headlineText = stringResource(R.string.feedback_title),
                supportingText = stringResource(R.string.feedback_desc),
                leadingImageVector = R.drawable.ic_outline_chat,
                onClick = {
                    viewModel.copyText(cbManager, "2695520089@qq.com")
                    Toast.makeText(context, "已复制邮件地址", Toast.LENGTH_SHORT).show()
                }
            )
        }
        if (viewModel.isShowAlert) {
            ShowAlert(viewModel = viewModel, text = "确定要退出登录吗？")
        }
        if (viewModel.isShowDialog) {
            ShowLoginDialog(viewModel = viewModel)
        }
        if (viewModel.isShowUpdateDialog) {
            UpdateDialog(viewModel = viewModel)
        }
    }
}