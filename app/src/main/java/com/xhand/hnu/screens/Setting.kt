package com.xhand.hnu.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
import com.xhand.hnu.components.copyText
import com.xhand.hnu.model.entity.DarkMode
import com.xhand.hnu.model.viewWebsite
import com.xhand.hnu.screens.navigation.Destinations
import com.xhand.hnu.viewmodel.SettingsViewModel

fun DarkMode.toStringResourceId(): String {
    return when (this) {
        DarkMode.SYSTEM -> "跟随系统"
        DarkMode.ON -> "开启"
        DarkMode.OFF -> "关闭"
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
                headlineText = if (viewModel.isLoginSuccess || viewModel.stateCode == 1) "退出" else "登录",
                supportingText = if (viewModel.isLoginSuccess || viewModel.stateCode == 1) "点击退出登录" else "请先登录",
                leadingImageVector = if (viewModel.isLoginSuccess || viewModel.stateCode == 1) R.drawable.ic_outline_account_circle else R.drawable.ic_outline_no_accounts_24,
                onClick = {
                    if (viewModel.isLoginSuccess || viewModel.stateCode == 1)
                        viewModel.isShowAlert = true
                    else {
                        viewModel.isShowDialog = true
                    }
                },
                trailingContent = {
                    Row {
                        Box(
                            modifier = Modifier
                                .size(15.dp)
                                .clip(CircleShape)
                                .background(if (viewModel.isLoginSuccess) colorScheme.primary else Color.LightGray)
                        ) { }
                        Spacer(modifier = Modifier.size(10.dp))
                        Box(
                            modifier = Modifier
                                .size(15.dp)
                                .clip(CircleShape)
                                .background(if (viewModel.stateCode == 1) colorScheme.primary else Color.LightGray)
                        ) { }
                    }
                }
            )
            BasicListItem(leadingText = "显示")
            SwitchListItem(
                leadingImageVector = R.drawable.ic_palette,
                headlineText = "系统主题色",
                supportingText = "开启后将跟随系统主题色",
                value = viewModel.isDynamicColor,
                onValueChanged = {
                    viewModel.isDynamicColor = it
                }
            )
            DropdownListItem(
                leadingImageVector = R.drawable.ic_nightlight,
                headlineText = "深色模式",
                value = DarkMode.entries[viewModel.darkModeIndex],
                selections = DarkMode.entries
                    .map { SelectionItem(it.toStringResourceId(), it) },
                onValueChanged = { index, _ ->
                    viewModel.darkModeIndex = index
                }
            )
            BasicListItem(leadingText = "关于")
            BasicListItem(
                headlineText = "帮助",
                supportingText = "用户协议与使用说明",
                leadingImageVector = R.drawable.ic_outline_info,
                onClick = {
                    navController.navigate(Destinations.Guide.route)
                },
                trailingContent = {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null
                    )
                }
            )
            val currentVersion = stringResource(R.string.version)
            BasicListItem(
                headlineText = stringResource(R.string.app_name),
                supportingText = "版本号: release-${currentVersion}",
                leadingImageVector = R.drawable.ic_outline_article,
                onClick = {
                    viewWebsite("https://github.com/JiaLiFuNia/SmartHNU", context)
                }
            )
            BasicListItem(
                headlineText = "检查更新",
                supportingText = if (viewModel.ifNeedUpdate) "有新版本可用" else "点击以检查更新",
                leadingContent = {
                    if (viewModel.isGettingUpdate)
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 3.dp
                        )
                    else {
                        BadgedBox(
                            badge = {
                                if (viewModel.ifNeedUpdate) {
                                    Badge()
                                }
                            }
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                        }
                    }
                },
                onClick = {
                    viewModel.updateRes(currentVersion)
                    if (viewModel.ifNeedUpdate)
                        viewModel.isShowUpdateDialog = true
                    else
                        Toast.makeText(context, "已是最新版本", Toast.LENGTH_SHORT).show()
                }
            )
            BasicListItem(
                headlineText = "开发人员",
                supportingText = "JiaLiFuNia",
                leadingImageVector = R.drawable.ic_outline_person,
                onClick = {
                    viewWebsite("https://github.com/JiaLiFuNia", context)
                }
            )
            BasicListItem(
                headlineText = "反馈",
                supportingText = "发送邮件提供你的意见及建议",
                leadingImageVector = R.drawable.ic_outline_chat,
                onClick = {
                    copyText(cbManager, "smarthnu@proton.me")
                    Toast.makeText(context, "已复制邮件地址", Toast.LENGTH_SHORT).show()
                }
            )
            BasicListItem(leadingText = "其他")
            BasicListItem(
                headlineText = "河南师范大学",
                supportingText = "河南师范大学官网",
                leadingImageVector = R.drawable.hnu,
                leadingImageVectorModifier = Modifier.size(28.dp),
                onClick = {
                    viewWebsite("https://www.htu.edu.cn", context)
                }
            )
            BasicListItem(
                headlineText = "教务系统",
                supportingText = "河南师范大学教务管理系统",
                leadingImageVector = R.drawable.jwc,
                leadingImageVectorModifier = Modifier.size(28.dp),
                onClick = {
                    viewWebsite("https://jwc.htu.edu.cn", context)
                }
            )
            BasicListItem(
                headlineText = "Hack HTU",
                supportingText = "河南师范大学工具站",
                leadingImageVector =  R.drawable.hack_htu,
                leadingImageVectorModifier = Modifier.size(28.dp),
                onClick = {
                    viewWebsite("https://hackhtu.pages.dev", context)
                }
            )
        }
        ShowAlert(viewModel = viewModel, text = "确定要退出登录吗？")
        if (viewModel.isShowDialog) {
            ShowLoginDialog(viewModel = viewModel)
        }
        UpdateDialog(viewModel = viewModel)
    }
}