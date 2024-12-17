package com.smart.htu.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.smart.htu.R
import com.smart.htu.screens.login.LoginViewModel

@Composable
fun EditMessageDialog(
    showDialog: Boolean,
    onDismissRequests: () -> Unit,
    viewModel: LoginViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    var customUsername by remember { mutableStateOf("") }
    var customQQ by remember { mutableStateOf("") }
    if (showDialog) {
        AlertDialog(
            title = {
                Text(text = "编辑信息")
            },
            icon = {
                Icon(imageVector = Icons.Outlined.Edit, contentDescription = "edit")
            },
            onDismissRequest = { },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.changeEditableMessage(
                            if (customUsername == "") uiState.uneditableMessage.username else customUsername,
                            customQQ
                        )
                        onDismissRequests()
                        customUsername = ""
                    },
                    enabled = customUsername.isNotEmpty() || (customQQ.isNotEmpty() && customQQ.length <= 11)
                ) {
                    Text(text = "确定")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismissRequests()
                        customUsername = ""
                    }
                ) {
                    Text(text = "取消")
                }
            },
            text = {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "当前用户名为：${uiState.editableMessage.customUsername}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 5.dp),
                        textAlign = TextAlign.Start
                    )
                    TextField(
                        value = customUsername,
                        onValueChange = { customUsername = it },
                        label = {
                            Text(text = "请输入新的用户名")
                        },
                        maxLines = 1,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.AccountCircle,
                                contentDescription = "username"
                            )
                        },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TextField(
                        value = customQQ,
                        onValueChange = {
                            customQQ = it
                        },
                        label = {
                            Text(text = "请输入新的 QQ 号")
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.tag_24px),
                                contentDescription = "qq"
                            )
                        },
                        maxLines = 1,
                        singleLine = true,
                        isError = customQQ.length > 11,
                        supportingText = {
                            if (customQQ.length > 11) Text(text = "QQ号码不合法")
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )
                    Text(
                        text = "Tip：你可以修改你的用户名和 QQ 号，QQ 号仅用于获取你的 QQ 头像。",
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Gray
                    )
                }
            }
        )
    }
}
