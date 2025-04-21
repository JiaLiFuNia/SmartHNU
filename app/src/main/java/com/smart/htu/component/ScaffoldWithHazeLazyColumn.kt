package com.smart.htu.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.smart.htu.App.Companion.context
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import com.smart.htu.utils.sendToast
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.theme.MiuixTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldWithHazeLazyColumn(
    themeMode: Int,
    scrollBehavior: TopAppBarScrollBehavior,
    blurEnabledState: Boolean,
    title: @Composable () -> Unit,
    actions: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit,
    snackBarHost: (@Composable () -> Unit)? = null,
    itemSpacePadding: Dp = 12.dp,
    isMediumTopAppBar: Boolean = false,
    refreshState: top.yukonga.miuix.kmp.basic.PullToRefreshState,
    onRefresh: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val scope = rememberCoroutineScope()
    top.yukonga.miuix.kmp.basic.Scaffold(
        containerColor = if (themeMode == 0) MiuixTheme.colorScheme.background else colorScheme.background,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = {
            if (snackBarHost != null) {
                snackBarHost()
            }
        },
        topBar = {
            if (isMediumTopAppBar)
                MediumTopAppBar(
                    scrollBehavior = scrollBehavior,
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = if (blurEnabledState) Color.Transparent else when (themeMode) {
                            0 -> MiuixTheme.colorScheme.background
                            else -> colorScheme.surface
                        },
                        scrolledContainerColor = if (blurEnabledState) Color.Transparent else when (themeMode) {
                            0 -> MiuixTheme.colorScheme.background
                            else -> colorScheme.surfaceContainer
                        },
                    ),
                    title = { title() },
                    actions = { actions() },
                    navigationIcon = { navigationIcon() }
                )
            else
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = if (blurEnabledState) Color.Transparent else when (themeMode) {
                            0 -> MiuixTheme.colorScheme.background
                            else -> colorScheme.surface
                        },
                        scrolledContainerColor = if (blurEnabledState) Color.Transparent else when (themeMode) {
                            0 -> MiuixTheme.colorScheme.background
                            else -> colorScheme.surfaceContainer
                        },
                    ),
                    title = { title() },
                    actions = { actions() },
                    navigationIcon = { navigationIcon() }
                )
        }
    ) {
        top.yukonga.miuix.kmp.basic.PullToRefresh(
            pullToRefreshState = refreshState,
            refreshTexts = PULL_TO_REFRESH_TEXT,
            onRefresh = {
                scope.launch {
                    refreshState.completeRefreshing {
                        onRefresh()
                    }
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column {
                content()
            }
        }
    }
}