package com.smart.htu.component

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.rajat.pdfviewer.PdfRendererView
import com.rajat.pdfviewer.compose.PdfRendererViewCompose
import com.rajat.pdfviewer.util.PdfSource
import com.smart.htu.R
import com.smart.htu.screens.news.newsView.DownloadDialog
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.theme.MiuixTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfReaderView(
    url: String,
    title: String,
    navController: NavController,
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current
    val pdfLoading = remember { mutableStateOf(true) }
    val pdfLoadingText = remember { mutableStateOf("正在加载PDF...") }

    val showDownloadDialog = remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MiuixTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MiuixTheme.colorScheme.background,
                    scrolledContainerColor = MiuixTheme.colorScheme.background
                ),
                title = {
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "close")
                    }
                },
                actions = {
                    IconButton(onClick = { showDownloadDialog.value = true }) {
                        Icon(
                            painterResource(R.drawable.download_24px),
                            contentDescription = "download"
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        },
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            item {
                if (pdfLoading.value) {
                    CircularProgressIndicator(loadingText = pdfLoadingText.value)
                }
                PdfRendererViewCompose(
                    source = PdfSource.Remote(url),
                    lifecycleOwner = lifecycleOwner,
                    modifier = Modifier,
                    statusCallBack = object : PdfRendererView.StatusCallBack {
                        override fun onPdfLoadStart() {
                            pdfLoading.value = true
                            pdfLoadingText.value = "正在加载PDF..."
                        }

                        override fun onPdfLoadProgress(
                            progress: Int,
                            downloadedBytes: Long,
                            totalBytes: Long?
                        ) {
                            pdfLoadingText.value = "正在加载PDF ${progress}%..."
                        }

                        override fun onPdfLoadSuccess(absolutePath: String) {
                            pdfLoading.value = true
                            pdfLoadingText.value = "正在渲染PDF..."
                        }

                        override fun onError(error: Throwable) {
                            Log.e("PDF Status", "Error loading PDF: ${error.message}")
                        }

                        override fun onPdfRenderStart() {
                            Log.i("PDF Status", "Render started")
                            pdfLoadingText.value = "正在渲染PDF..."
                        }

                        override fun onPdfRenderSuccess() {
                            Log.i("PDF Status", "Render success")
                            pdfLoading.value = false
                            pdfLoadingText.value = "正在渲染PDF..."
                        }
                    }
                )
            }
        }

        DownloadDialog(
            showDialog = showDownloadDialog,
            fileName = title,
            url = url
        )
    }
}