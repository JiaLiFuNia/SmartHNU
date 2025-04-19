package com.smart.htu.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.smart.htu.R
import com.smart.htu.ui.theme.onPrimaryLight
import com.smart.htu.ui.theme.primaryLight
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.extra.SuperDialog
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.MiuixPopupUtils.Companion.dismissDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicBottomSheet(
    showDialog: MutableState<Boolean>,
    title: String,
    summary: String? = null,
    onConfirmClick: (() -> Unit)? = null,
    content: @Composable (ColumnScope.() -> Unit)
) {
    SuperDialog(
        title = title,
        summary = summary,
        show = showDialog,
        onDismissRequest = {
            dismissDialog(showDialog)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
        ) {
            Card(modifier = Modifier) {
                content()
            }

            if (onConfirmClick != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        text = stringResource(id = R.string.cancel),
                        onClick = {
                            dismissDialog(showDialog)
                        },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(20.dp))
                    TextButton(
                        text = stringResource(id = R.string.confirm),
                        onClick = {
                            onConfirmClick()
                            dismissDialog(showDialog)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.textButtonPrimaryColors()
                    )
                }
            }
        }
    }
}

@Composable
fun ButtonDefaults.textButtonPrimaryColors() = textButtonColors(
    color = MiuixTheme.colorScheme.primaryContainer,
    disabledColor = MiuixTheme.colorScheme.disabledPrimaryButton,
    textColor = primaryLight,
    disabledTextColor = MiuixTheme.colorScheme.disabledOnPrimaryButton
)