package com.smart.htu.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.TabRowDefaults
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.G2RoundedCornerShape

@Composable
fun TabRow(
    tabs: List<String>,
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    height: Dp = 48.dp,
    onTabSelected: ((Int) -> Unit)? = null,
) {
    // TabRow()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        tabs.forEachIndexed { index, tabText ->
            Surface(
                shape = G2RoundedCornerShape(TabRowDefaults.TabRowCornerRadius),
                onClick = { onTabSelected?.invoke(index) },
                enabled = onTabSelected != null,
                color = if (selectedTabIndex == index) MiuixTheme.colorScheme.surface else MiuixTheme.colorScheme.background,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .semantics { role = Role.Tab }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tabText,
                        color = if (selectedTabIndex == index) MiuixTheme.colorScheme.onSurface else MiuixTheme.colorScheme.onSurfaceVariantSummary,
                        fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}