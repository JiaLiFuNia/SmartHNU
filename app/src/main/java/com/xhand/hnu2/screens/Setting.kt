package com.xhand.hnu2.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xhand.hnu2.R
import com.xhand.hnu2.components.BasicListItem
import com.xhand.hnu2.components.DropdownListItem
import com.xhand.hnu2.components.SelectionItem
import com.xhand.hnu2.components.SwitchListItem
import com.xhand.hnu2.components.showAlert
import com.xhand.hnu2.components.showLoginDialog
import com.xhand.hnu2.model.entity.DarkMode
import com.xhand.hnu2.model.entity.Update
import com.xhand.hnu2.network.SearchService
import com.xhand.hnu2.network.UpdateService
import com.xhand.hnu2.viewmodel.SettingsViewModel

fun DarkMode.toStringResourceId(): Int {
    return when (this) {
        DarkMode.SYSTEM -> R.string.follow_system
        DarkMode.ON -> R.string.turn_on
        DarkMode.OFF -> R.string.turn_off
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(settingsViewModel: SettingsViewModel) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val cbManager = LocalClipboardManager.current
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.primary.copy(alpha = 0.08f)
                        .compositeOver(colorScheme.surface.copy())
                ),
                title = {
                    Text(text = "设置")
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { values ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(values)
                .verticalScroll(scrollState)
        ) {
            BasicListItem(leadingText = "账户")
            BasicListItem(
                headlineText = if (settingsViewModel.isLoginSuccess) {
                    "退出"
                } else {
                    "登录"
                },
                supportingText = if (settingsViewModel.isLoginSuccess) {
                    "点击退出登录"
                } else {
                    "请先登录"
                },
                leadingImageVector = if (settingsViewModel.isLoginSuccess) {
                    R.drawable.ic_outline_account_circle
                } else {
                    R.drawable.ic_outline_no_accounts_24
                },
                onClick = {
                    if (settingsViewModel.isLoginSuccess) {
                        settingsViewModel.isShowAlert = true
                    } else {
                        settingsViewModel.isShowDialog = true
                        settingsViewModel.username = ""
                        settingsViewModel.password = ""
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
                mutableStateOf(0)
            }
            DropdownListItem(
                leadingImageVector = R.drawable.ic_nightlight,
                headlineText = "深色模式",
                value = DarkMode.values()[darkModeIndex.value],
                selections = DarkMode.values()
                    .map { SelectionItem(stringResource(it.toStringResourceId()), it) },
                onValueChanged = { index, _ ->
                    darkModeIndex.value = index
                }
            )
            BasicListItem(leadingText = stringResource(R.string.about))
            BasicListItem(
                headlineText = stringResource(R.string.app_name),
                supportingText = "Copyright 2023-2024 Xhand v2.0.3.7",
                leadingImageVector = R.drawable.ic_outline_info,
                onClick = { }
            )
            SwitchListItem(
                leadingImageVector = R.drawable.ic_refresh,
                headlineText = stringResource(R.string.update),
                supportingText = "开启以自动检测更新",
                value = settingsViewModel.ifUpdate,
                onValueChanged = { !settingsViewModel.ifUpdate }
            )
            BasicListItem(
                headlineText = stringResource(R.string.dev_title),
                supportingText = stringResource(R.string.dev_name),
                leadingImageVector = R.drawable.ic_outline_person,
                onClick = { }
            )
            BasicListItem(
                headlineText = stringResource(R.string.feedback_title),
                supportingText = stringResource(R.string.feedback_desc),
                leadingImageVector = R.drawable.ic_outline_chat,
                onClick = {
                    settingsViewModel.copyText(cbManager, context)
                    Toast.makeText(context, "已复制邮件地址", Toast.LENGTH_SHORT).show()
                }
            )
            BasicListItem(
                headlineText = stringResource(R.string.open_source_code_title),
                supportingText = stringResource(R.string.open_source_code_desc),
                leadingImageVector = R.drawable.ic_code,
                onClick = {
                    Intent(Intent.ACTION_VIEW).also {
                        it.data = Uri.parse("https://xhand.fun")
                        if (it.resolveActivity(context.packageManager) != null) {
                            context.startActivity(it)
                        }
                    }
                }
            )
        }
        if (settingsViewModel.isShowAlert) {
            showAlert(settingsViewModel = settingsViewModel, text = "确定要退出登录吗？")
        }
        if (settingsViewModel.isShowDialog) {
            showLoginDialog(settingsViewModel)
        }
    }
}