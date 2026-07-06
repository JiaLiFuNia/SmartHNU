package com.smart.htu.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.kevinnzou.web.rememberWebViewNavigator
import com.smart.htu.screens.LocalNavigator
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTopAppBar
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.icon.extended.Download

@Composable
fun PDFViewer(
    url: String,
    title: String
) {
    val navigator = LocalNavigator.current
    val showDownloadDialog = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = title,
                navigationIcon = {
                    IconButton(
                        onClick = { navigator.pop() }
                    ) {
                        Icon(imageVector = MiuixIcons.Regular.Back, contentDescription = "close")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showDownloadDialog.value = true }
                    ) {
                        Icon(
                            imageVector = MiuixIcons.Download,
                            contentDescription = "download"
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())
        ) {
            item {
                WebView(
                    url = "https://www.htu.edu.cn/_js/_portletPlugs/swfPlayer/pdfjs22228/web/viewer.html?file=${url}",
                    navigator = rememberWebViewNavigator(),
                    headers = mapOf("user-agent" to "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Mobile Safari/537.36 Edg/145.0.0.0"),
                    modifier = Modifier.fillParentMaxSize(),
                    onError = {}
                )
            }
        }

        DownloadDialog(
            showDialog = showDownloadDialog.value,
            fileName = title,
            url = url,
            onDismissRequest = { showDownloadDialog.value = false }
        )
    }
}