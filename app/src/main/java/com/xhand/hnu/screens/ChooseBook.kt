package com.xhand.hnu.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.xhand.hnu.components.BookBottomSheet
import com.xhand.hnu.components.BookSelectBottomSheet
import com.xhand.hnu.components.BooksListItem
import com.xhand.hnu.viewmodel.SettingsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseBookScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val scrollState = rememberScrollState()
    var xnxqdm by remember { mutableStateOf("") }
    LaunchedEffect(viewModel.selectTerm) {
        viewModel.isGettingBook = true
        viewModel.bookService(viewModel.longGradeTerm[viewModel.selectTerm])
        viewModel.isGettingBook = false
    }
    var isRefreshing by remember { mutableStateOf(false) }
    val state = rememberPullToRefreshState()
    val coroutineScope = rememberCoroutineScope()
    val onRefresh = {
        isRefreshing = true
        coroutineScope.launch {
            delay(timeMillis = 1000)
            viewModel.bookService(viewModel.longGradeTerm[viewModel.selectTerm])
            isRefreshing = false
        }
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                title = {
                    Text(text = "选订教材")
                },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.showBookSelect = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "more"
                        )
                    }
                }
            )
        }
    ) {
        var kcrwdm by remember { mutableStateOf("") }
        var kcmc by remember {
            mutableStateOf("")
        }
        if (viewModel.isGettingBook)
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        else {
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = { onRefresh() },
                state = state,
                modifier = Modifier
                    .padding(paddingValues = it),
                contentAlignment = Alignment.TopStart
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                ) {
                    if (viewModel.booksList.isEmpty())
                        Text(text = "没有教材", color = Color.Gray)
                    else {
                        viewModel.booksList.sortedBy { it.kcdlmc }
                        viewModel.booksList.forEach { book ->
                            BooksListItem(
                                book = book,
                                modifier = Modifier.clickable {
                                    kcrwdm = book.kcrwdm
                                    xnxqdm = viewModel.longToShort(book.xnxqmc)
                                    kcmc = book.kcmc
                                    viewModel.showBookAlert = true
                                }
                            )
                        }
                    }

                }
            }
        }
        if (viewModel.showBookAlert)
            BookBottomSheet(viewModel = viewModel, kcrwdm = kcrwdm, xnxqdm = xnxqdm, kcmc = kcmc)
        if (viewModel.showBookSelect)
            BookSelectBottomSheet(viewModel = viewModel)
    }
}
