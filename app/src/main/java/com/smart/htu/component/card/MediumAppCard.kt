package com.smart.htu.component.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mocharealm.gaze.capsule.ContinuousRoundedRectangle
import com.smart.htu.R
import com.smart.htu.screens.application.ApplicationEntity
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.BasicComponentDefaults
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.extra.SuperDialog
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun MediumAppCard(
    enabled: Boolean,
    content: ApplicationEntity,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = {
            onClick()
        },
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth(),
        shape = ContinuousRoundedRectangle(CardDefaults.CornerRadius),
        color = if (enabled) MiuixTheme.colorScheme.surfaceContainer
        else MiuixTheme.colorScheme.disabledSecondaryVariant
    ) {
        BasicComponent(
            startAction = {
                Icon(
                    painter = painterResource(id = content.icon),
                    contentDescription = "icon",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(24.dp),
                    tint = if (enabled) MiuixTheme.colorScheme.primary
                    else MiuixTheme.colorScheme.primary.copy(0.38f)
                )
            },
            enabled = enabled,
            title = stringResource(content.label),
            titleColor = BasicComponentDefaults.titleColor(),
            endActions = {
                if (content.trailingIcon != null) {
                    Image(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = content.trailingIcon),
                        contentDescription = "add"
                    )
                }
            }
        )
    }
}

@Composable
fun JumpToAlipayDialog(
    showDialog: MutableState<Boolean>,
    onConfirmClick: () -> Unit
) {
    SuperDialog(
        title = "提示",
        summary = "是否跳转到支付宝小程序？",
        show = showDialog,
        onDismissRequest = {
            showDialog.value = false
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            top.yukonga.miuix.kmp.basic.TextButton(
                text = stringResource(id = R.string.cancel),
                onClick = {
                    showDialog.value = false
                },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(20.dp))
            top.yukonga.miuix.kmp.basic.TextButton(
                text = stringResource(id = R.string.confirm),
                onClick = {
                    onConfirmClick()
                    showDialog.value = false
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.textButtonColorsPrimary()
            )
        }
    }
}