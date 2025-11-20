package com.smart.htu.screens

import android.os.Environment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.smart.htu.api.module.CaptchaVersionEntity
import com.smart.htu.api.module.UpdateData
import com.smart.htu.api.module.UpdateEntity
import com.smart.htu.component.textButtonPrimaryColors
import com.smart.htu.utils.FileUtil.downloadFile
import com.smart.htu.utils.ToastUtil
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.extra.SuperDialog
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun UpdateDialog(
    showDialog: MutableState<Boolean>,
    updateInfo: UpdateEntity,
    targetDirectory: String = Environment.DIRECTORY_DOWNLOADS,
    onConfirmClick: (() -> Unit)? = null,
    contentText: String? = null,
    confirmButtonText: String = "下载并更新",
    dismissButtonText: String = "关闭",
    title: String = "发现新版本"
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    SuperDialog(
        title = title,
        show = showDialog,
        summary = "版本：${updateInfo.versionName}(${updateInfo.versionCode})",
        onDismissRequest = {
            showDialog.value = false
        }
    ) {
        Column {
            Text(
                text = contentText ?: (updateInfo.update?.content ?: "更新内容"),
                color = MiuixTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                top.yukonga.miuix.kmp.basic.TextButton(
                    text = dismissButtonText,
                    onClick = {
                        showDialog.value = false
                    },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(20.dp))
                top.yukonga.miuix.kmp.basic.TextButton(
                    text = confirmButtonText,
                    onClick = {
                        scope.launch {
                            if (onConfirmClick == null) {
                                updateInfo.update?.downloadUrl?.let { url ->
                                    downloadFile(
                                        context = context,
                                        url = url,
                                        fileName = url.substringAfterLast("/"),
                                        targetDirectory = targetDirectory
                                    )
                                }
                                showDialog.value = false
                                ToastUtil.showToast(context, "下拉通知栏，查看进度")
                            } else {
                                onConfirmClick()
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.textButtonPrimaryColors()
                )
            }
        }
    }
}

@Composable
fun CaptchaUpdateDialog(
    showDialog: MutableState<Boolean>,
    updateInfo: CaptchaVersionEntity
) {
    UpdateDialog(
        showDialog = showDialog,
        updateInfo = UpdateEntity(
            versionName = updateInfo.versionName,
            versionCode = updateInfo.versionCode,
            isNeedUpdate = true,
            update = UpdateData(
                downloadUrl = updateInfo.downloadUrl,
                content = "验证码识别模块有新版本，请下载更新。下载完整后，点击安装模型以更新。",
            ),
            isForceUpdate = false
        ),
        targetDirectory = Environment.DIRECTORY_DOWNLOADS,
        title = "模块更新"
    )
}