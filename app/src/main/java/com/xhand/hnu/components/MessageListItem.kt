package com.xhand.hnu.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.datastore.preferences.protobuf.Internal.BooleanList
import com.xhand.hnu.R
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