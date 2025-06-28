package com.smart.htu.screens.application

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.extra.CheckboxLocation
import top.yukonga.miuix.kmp.extra.SuperCheckbox
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationEdit(
    navController: NavController,
    viewModel: ApplicationViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = topAppBarColors(
                    containerColor = MiuixTheme.colorScheme.background,
                    scrolledContainerColor = MiuixTheme.colorScheme.background
                ),
                title = {
                    Text("编辑")
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back"
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp, 12.dp),
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .overScrollVertical(),
            overscrollEffect = null
        ) {
            stickyHeader {
                StickyHeader(text = "已添加应用")
            }
            items(uiState.appList.filter { it in uiState.appListIsCommonList }) {
                Card {
                    SuperCheckbox(
                        checkboxLocation = CheckboxLocation.Right,
                        title = stringResource(it.label),
                        summary = stringResource(it.category.category),
                        checked = it in uiState.appListIsCommonList,
                        onCheckedChange = { value ->
                            viewModel.changeCommonAppListState(it, value)
                        }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            stickyHeader {
                StickyHeader(text = "未添加应用")
            }
            items(uiState.appList.filter { it !in uiState.appListIsCommonList }) {
                Card {
                    SuperCheckbox(
                        checkboxLocation = CheckboxLocation.Right,
                        title = stringResource(it.label),
                        summary = stringResource(it.category.category),
                        checked = it in uiState.appListIsCommonList,
                        onCheckedChange = { value ->
                            viewModel.changeCommonAppListState(it, value)
                        }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun StickyHeader(
    text: String,
    insideMargin: PaddingValues = PaddingValues(start = 12.dp, top = 8.dp)
) {
    SmallTitle(
        text = text,
        insideMargin = insideMargin,
        modifier = Modifier
            .fillMaxSize()
            .background(MiuixTheme.colorScheme.background)
            .padding(bottom = 12.dp)
    )
}