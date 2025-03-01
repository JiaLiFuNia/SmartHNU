package com.smart.htu.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.smart.htu.MainActivity.Companion.snackBarHostState
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import top.yukonga.miuix.kmp.theme.MiuixTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun ScaffoldWithHazeLazyColumn(
    themeMode: Int,
    scrollBehavior: TopAppBarScrollBehavior,
    blurEnabledState: Boolean,
    title: @Composable () -> Unit,
    actions: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit,
    itemSpacePadding: Dp = 20.dp,
    isMediumTopAppBar: Boolean = false,
    isRefreshing: Boolean,
    refreshState: PullToRefreshState,
    onRefresh: () -> Unit,
    content: LazyListScope.() -> Unit
) {
    val hazeState = remember { HazeState() }
    Scaffold(
        containerColor = if (themeMode == 0) MiuixTheme.colorScheme.background else colorScheme.background,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
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
                    navigationIcon = { navigationIcon() },
                    modifier = Modifier.hazeEffect(
                        state = hazeState,
                        style = HazeMaterials.regular()
                    ) {
                        blurRadius = 30.dp
                        blurEnabled = blurEnabledState
                    }
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
                    navigationIcon = { navigationIcon() },
                    modifier = Modifier.hazeEffect(
                        state = hazeState,
                        style = HazeMaterials.regular()
                    ) {
                        blurRadius = 30.dp
                        blurEnabled = blurEnabledState
                    }
                )
        }
    ) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { onRefresh() },
            state = refreshState,
            indicator = {
                Indicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = it.calculateTopPadding()),
                    isRefreshing = isRefreshing,
                    state = refreshState
                )
            },
            modifier = Modifier
                .fillMaxSize()
        ) {
            top.yukonga.miuix.kmp.basic.LazyColumn(
                contentPadding = PaddingValues(
                    top = it.calculateTopPadding() + 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
                modifier = Modifier
                    .hazeSource(state = hazeState)
                    .fillMaxSize()
            ) {
                content()
            }
        }
    }
}