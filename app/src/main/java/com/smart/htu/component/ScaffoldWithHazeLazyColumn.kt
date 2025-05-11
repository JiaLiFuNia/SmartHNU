package com.smart.htu.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.smart.htu.utils.Constants.Companion.PULL_TO_REFRESH_TEXT
import top.yukonga.miuix.kmp.basic.PullToRefreshState
import top.yukonga.miuix.kmp.theme.MiuixTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldWithHazeLazyColumn(
    scrollBehavior: TopAppBarScrollBehavior,
    blurEnabledState: Boolean,
    title: @Composable () -> Unit,
    actions: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit,
    snackBarHost: (@Composable () -> Unit)? = null,
    isMediumTopAppBar: Boolean = false,
    refreshState: PullToRefreshState,
    onRefresh: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    top.yukonga.miuix.kmp.basic.Scaffold(
        containerColor = MiuixTheme.colorScheme.background,
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
                        containerColor = if (blurEnabledState) Color.Transparent else MiuixTheme.colorScheme.background,
                        scrolledContainerColor = if (blurEnabledState) Color.Transparent else MiuixTheme.colorScheme.background,
                    ),
                    title = { title() },
                    actions = { actions() },
                    navigationIcon = { navigationIcon() }
                )
            else
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = if (blurEnabledState) Color.Transparent else MiuixTheme.colorScheme.background,
                        scrolledContainerColor = if (blurEnabledState) Color.Transparent else MiuixTheme.colorScheme.background,
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
            onRefresh = onRefresh,
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