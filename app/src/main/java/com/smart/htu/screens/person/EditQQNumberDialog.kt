package com.smart.htu.screens.person

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.smart.htu.R
import com.smart.htu.component.textButtonPrimaryColors
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.extra.SuperDialog
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun EditQQNumberDialog(
    showDialog: MutableState<Boolean>,
    onConfirmRequests: (String) -> Unit
) {
    var customizedQQNumber by remember { mutableStateOf("") }
    val qqPattern = remember { Regex("^[1-9]\\d{4,10}$") }
    var isError by remember { mutableStateOf(false) }
    var errorColor = MaterialTheme.colorScheme.error
    SuperDialog(
        show = showDialog,
        title = "修改头像",
        summary = "通过设置 QQ 号码以修改头像，暂不支持其他头像修改方式",
        onDismissRequest = {
            showDialog.value = false
        }
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = customizedQQNumber,
                onValueChange = {
                    customizedQQNumber = it
                    isError = !qqPattern.matches(customizedQQNumber)
                },
                label = "QQ 号码",
                useLabelAsPlaceholder = true,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.tag_24px),
                        contentDescription = "qq",
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                },
                trailingIcon = {
                    if (isError)
                        Icon(
                            painter = painterResource(id = R.drawable.warning_24px),
                            contentDescription = "warning",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                },
                labelColor = if (isError) errorColor else MiuixTheme.colorScheme.onSecondaryContainer,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
            if (isError) {
                Text(
                    text = "QQ 号码不合法",
                    color = errorColor,
                    style = MiuixTheme.textStyles.body2,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
            /*TextField(
                value = customizedQQNumber,
                onValueChange = {
                    onChangeQQNumber(it)
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
                isError = customizedQQNumber.length > 11,
                supportingText = {
                    if (customizedQQNumber.length > 11) Text(text = "QQ 号码不合法")
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )*/
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    text = stringResource(id = R.string.cancel),
                    onClick = {
                        showDialog.value = false
                    },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(20.dp))
                TextButton(
                    text = stringResource(id = R.string.confirm),
                    onClick = {
                        onConfirmRequests(customizedQQNumber)
                        showDialog.value = false
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.textButtonPrimaryColors()
                )
            }
        }
    }
}
