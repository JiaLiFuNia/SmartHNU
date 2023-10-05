package com.xhand.htu.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.xhand.htu.DropdownMenuPreference
import com.xhand.htu.PreferenceHeader
import com.xhand.htu.SwitchPreference
import com.xhand.htu.TextPreference
import com.xhand.htu.rememberChat
import com.xhand.htu.rememberClearNight
import com.xhand.htu.rememberCode
import com.xhand.htu.rememberInfo
import com.xhand.htu.rememberNoAccounts
import com.xhand.htu.rememberPalette

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SettingScreen() {
    Column {
        TopAppBar(
            title = { Text("设置") },
            modifier = Modifier
        )
        PreferenceHeader(text = "账户")
        TextPreference(
            title = "登录",
            description = "点击登录教务系统",
            imageVector = rememberNoAccounts(),
            onClick = {  }
        )
        PreferenceHeader(text = "主题")
        SwitchPreference(
            imageVector = rememberPalette(),
            title = "系统主题色",
            description = "开启后将跟随系统主题色",
            value = true,
            onValueChanged = {  }
        )
        DropdownMenuPreference(
            imageVector = rememberClearNight(),
            title = "深色主题",
            value = true,
            selections = listOf(
                "跟随系统",
                "开启",
                "关闭"
            ),
            onValueChanged = {  }
        )
        PreferenceHeader(text = "关于")
        TextPreference(
            title = "智慧师大",
            description = "Copyright@2023 Xhand",
            imageVector = rememberInfo()
        ) {
        }
        TextPreference(
            title = "反馈",
            description = "加入群聊提供你的意见及建议",
            imageVector = rememberChat()
        ) { }
        TextPreference(
            title = "开源代码",
            description = "你的开发将为此应用贡献一份力量",
            imageVector = rememberCode()
        ) { }
    }
}
// 提示框
@Composable
fun AddTextAlertDialog(
    isShowDialog: Boolean,
    actionAfterConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
    titleText: String = "提示",
    contentText: String
) {
    if (isShowDialog) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(
                    text = titleText,
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            text = {
                Text(
                    text = contentText,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    actionAfterConfirm()
                }) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text("取消")
                }
            }
        )
    }
}
