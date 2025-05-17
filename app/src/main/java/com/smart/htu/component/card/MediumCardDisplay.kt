package com.smart.htu.component.card

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.smart.htu.R
import com.smart.htu.component.textButtonPrimaryColors
import com.smart.htu.screens.application.entity.ApplicationEntity
import com.smart.htu.screens.application.entity.RouteType
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.extra.SuperDialog
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.SmoothRoundedCornerShape

@Composable
fun MediumCardDisplay(
    enabled: Boolean,
    content: ApplicationEntity,
    modifier: Modifier,
    onCLick: () -> Unit
) {
    val showDialog = remember { mutableStateOf(false) }
    Surface(
        onClick = {
            if (content.routeType == RouteType.ALIPAY) {
                showDialog.value = true
            } else {
                onCLick()
            }
        },
        modifier = modifier
            .semantics { role = Role.Button }
            .fillMaxWidth()
            .animateContentSize(),
        shape = SmoothRoundedCornerShape(ButtonDefaults.CornerRadius),
        color = if (enabled) MiuixTheme.colorScheme.surface
        else MiuixTheme.colorScheme.disabledSecondaryVariant
    ) {
        ListItem(
            leadingContent = {
                Icon(
                    painter = painterResource(id = content.icon),
                    contentDescription = "icon",
                    modifier = Modifier.size(24.dp),
                    tint = if (enabled) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.primary.copy(0.38f)
                )
            },
            headlineContent = {
                Text(
                    text = stringResource(id = content.label),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .basicMarquee(
                            repeatDelayMillis = 2_000,
                        ),
                    color = if (enabled) MaterialTheme.colorScheme.onBackground
                    else MaterialTheme.colorScheme.onBackground.copy(0.38f)
                )
            },
            trailingContent = {
                if (content.trailingIcon != null) {
                    Image(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = content.trailingIcon),
                        contentDescription = "add"
                    )
                }
            },
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            )
        )
    }

    JumpToAlipayDialog(
        showDialog = showDialog,
        onConfirmClick = {
            onCLick()
        }
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
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
                colors = ButtonDefaults.textButtonPrimaryColors()
            )
        }
    }
}