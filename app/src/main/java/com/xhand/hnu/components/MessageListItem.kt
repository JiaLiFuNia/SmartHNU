package com.xhand.hnu.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.google.gson.Gson
import com.xhand.hnu.model.entity.MessageDetail
import com.xhand.hnu.model.entity.MyClass
import com.xhand.hnu.viewmodel.SettingsViewModel

@Composable
fun MessageListItem(messageDetail: MessageDetail, modifier: Modifier, viewModel: SettingsViewModel) {
    if (messageDetail.type == "tksh")
        ListItem(
            headlineContent = {
                Text(
                    text = messageDetail.subject ?: ""
                )
            },
            supportingContent = {
                Text(
                    text = messageDetail.feedback ?: "",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            modifier = modifier
        )
    if (messageDetail.type == "cycjtz" || messageDetail.type == "cjtz") {
        val jsonString = messageDetail.msg
        val gson = Gson()
        val myObject = gson.fromJson(jsonString, MyClass::class.java)
        val className = myObject.kcmc.split('[')[0]
        val xxids = messageDetail.xxid
        ListItem(
            headlineContent = {
                Text(
                    text = "$className 成绩已公布"
                )
            },
            supportingContent = {
                Text(
                    text = "成绩：${myObject.zcj}，绩点：${myObject.jd}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            trailingContent = {
                TextButton(onClick = { viewModel.readMessage(xxids) }) {
                    Text(text = "已读")
                }
            },
            modifier = modifier
        )
    }

}

@Composable
fun MessageDetailDialog(messageDetail: MessageDetail, viewModel: SettingsViewModel) {
    if (viewModel.showMessageDetail) {
        if (messageDetail.type == "tksh")
            AlertDialog(
                title = {
                    Text(text = "详情")
                },
                text = {
                    Text(text = messageDetail.feedback ?: "")
                },
                onDismissRequest = {
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.showMessageDetail = false
                        }
                    ) {
                        Text("已读")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { viewModel.showMessageDetail = false }
                    ) {
                        Text("取消")
                    }
                }
            )
        /*if (messageDetail.type == "cycjtz" || messageDetail.type == "cjtz") {
            val jsonString = messageDetail.msg
            val gson = Gson()
            val myObject = gson.fromJson(jsonString, MyClass::class.java)
            AlertDialog(
                title = {
                    Text(text = "详情")
                },
                text = {
                    Text(text = "${myObject.kcmc} 的成绩为：${myObject.zcj}")
                },
                onDismissRequest = {
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.showMessageDetail = false
                        }
                    ) {
                        Text("已读")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { viewModel.showMessageDetail = false }
                    ) {
                        Text("取消")
                    }
                }
            )
        }*/
    }
}


@Composable
fun AllMessageRead(viewModel: SettingsViewModel) {
    if (viewModel.showHaveReadAlert)
        AlertDialog(
            title = {
                Text(text = "提示")
            },
            text = {
                Text(text = "是否将所有消息标记为已读？")
            },
            onDismissRequest = {
                viewModel.showHaveReadAlert = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.readMessage(viewModel.hasMessage.joinToString(",") { it.xxid })
                        viewModel.showHaveReadAlert = false
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.showHaveReadAlert = false }
                ) {
                    Text("取消")
                }
            }
        )
}