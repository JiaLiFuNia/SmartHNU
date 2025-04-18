package com.smart.htu.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.smart.htu.R
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.extra.SuperDialog
import top.yukonga.miuix.kmp.utils.MiuixPopupUtils.Companion.dismissDialog

@Composable
fun EditQQNumberDialog(
    showDialog: MutableState<Boolean>,
    onConfirmRequests: (String) -> Unit
) {
    val (customizedQQNumber, onChangeQQNumber) = remember { mutableStateOf("") }
    SuperDialog(
        show = showDialog,
        title = "修改头像",
        summary = "通过设置 QQ 号码以修改头像，暂不支持其他头像修改方式",
        onDismissRequest = {
            dismissDialog(showDialog)
        }
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            top.yukonga.miuix.kmp.basic.TextField(
                value = customizedQQNumber,
                onValueChange = { onChangeQQNumber(it) },
                label = "QQ 号码",
                useLabelAsPlaceholder = true,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.tag_24px),
                        contentDescription = "qq",
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
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
                top.yukonga.miuix.kmp.basic.TextButton(
                    text = stringResource(id = R.string.cancel),
                    onClick = {
                        dismissDialog(showDialog)
                    },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(20.dp))
                top.yukonga.miuix.kmp.basic.TextButton(
                    text = stringResource(id = R.string.confirm),
                    onClick = {
                        onConfirmRequests(customizedQQNumber)
                        dismissDialog(showDialog)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.textButtonPrimaryColors()
                )
            }
        }
    }
}
