package com.xhand.hnu.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.xhand.hnu.model.entity.MessageDetail
import com.xhand.hnu.viewmodel.SettingsViewModel

@Composable
fun MessageListItem(messageDetail: MessageDetail, modifier: Modifier) {
    ListItem(
        headlineContent = {
            Text(
                text = messageDetail.subject
            )
        },
        supportingContent = {
            Text(
                text = messageDetail.feedback,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        modifier = modifier
    )
}

@Composable
fun MessageDetailDialog(messageDetail: MessageDetail, viewModel: SettingsViewModel) {
    if (viewModel.showMessageDetail)
        AlertDialog(
            title = {
                Text(text = "详情")
            },
            text = {
                Text(text = messageDetail.feedback)
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
}