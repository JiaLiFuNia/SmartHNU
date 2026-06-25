package com.smart.htu.component.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smart.htu.screens.application.ApplicationEntity
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun SmallAppCard(
    enabled: Boolean,
    content: ApplicationEntity,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = {
            onClick()
        },
        shape = RoundedCornerShape(CardDefaults.CornerRadius),
        modifier = modifier
            .aspectRatio(1f),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .size(58.dp)
                    .aspectRatio(1f),
                colors = CardDefaults.defaultColors(
                    if (enabled) MiuixTheme.colorScheme.surfaceContainer
                    else MiuixTheme.colorScheme.disabledSecondaryVariant,
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = content.outlinedIcon),
                        contentDescription = "icon",
                        modifier = Modifier.size(32.dp),
                        tint = if (enabled) MiuixTheme.colorScheme.primary
                        else MiuixTheme.colorScheme.primary.copy(0.38f)
                    )
                }
            }
            Text(
                text = stringResource(content.label),
                fontSize = 15.sp,
                lineHeight = 18.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}