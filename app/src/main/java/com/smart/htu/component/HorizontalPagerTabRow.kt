package com.smart.htu.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun <T> HorizontalPagerTabRow(
    isHeaderVisible: Boolean = true,
    tabs: List<String>?,
    pagerState: PagerState = rememberPagerState { tabs?.size ?: 0 },
    dataSource: List<T>?,
    pageContent: @Composable (List<T>, String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    if (!tabs.isNullOrEmpty()) {
        Column {
            if (isHeaderVisible) {
                top.yukonga.miuix.kmp.basic.TabRow(
                    tabs = tabs,
                    selectedTabIndex = pagerState.currentPage,
                    onTabSelected = {
                        scope.launch {
                            pagerState.animateScrollToPage(it)
                        }
                    }
                )
            }
            HorizontalPager(
                state = pagerState,
                verticalAlignment = Alignment.Top
            ) { pageIndex ->
                if (dataSource != null) {
                    pageContent(dataSource, tabs[pageIndex])
                }
            }
        }
    }
}