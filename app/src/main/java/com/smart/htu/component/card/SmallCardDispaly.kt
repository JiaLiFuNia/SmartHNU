package com.smart.htu.component.card

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mocharealm.gaze.capsule.ContinuousRoundedRectangle
import com.smart.htu.screens.application.ApplicationEntity
import com.smart.htu.screens.application.ApplicationEntity.RouteType
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun SmallCardDisplay(
    enabled: Boolean,
    content: ApplicationEntity,
    modifier: Modifier = Modifier,
    enableContainerColor: Color = MiuixTheme.colorScheme.surface,
    disableContainerColor: Color = MiuixTheme.colorScheme.disabledSecondaryVariant,
    onClick: () -> Unit
) {
    val showDialog = remember { mutableStateOf(false) }
    Surface(
        onClick = {
            if (content.routeType == RouteType.ALIPAY) {
                showDialog.value = true
            } else {
                onClick()
            }
        },
        modifier = modifier
            .size(70.dp)
            .semantics { role = Role.Button }
            .animateContentSize(),
        shape = ContinuousRoundedRectangle(top.yukonga.miuix.kmp.basic.CardDefaults.CornerRadius),
        color = if (enabled) enableContainerColor
        else disableContainerColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = content.icon),
                    contentDescription = "icon",
                    modifier = Modifier.size(35.dp),
                    tint = if (enabled) MiuixTheme.colorScheme.primary
                    else MiuixTheme.colorScheme.primary.copy(0.38f)
                )
            }
            Text(
                text = stringResource(id = content.label),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 13.sp,
                modifier = Modifier
                    .basicMarquee(
                        repeatDelayMillis = 2_000,
                    ),
                color = if (enabled) MiuixTheme.colorScheme.onSurface
                else MiuixTheme.colorScheme.onSurfaceContainerVariant,
                textAlign = TextAlign.Center
            )
        }
    }
    JumpToAlipayDialog(
        showDialog = showDialog,
        onConfirmClick = {
            onClick()
        }
    )
}