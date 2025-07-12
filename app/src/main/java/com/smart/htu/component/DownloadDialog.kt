package com.smart.htu.component

import android.os.Environment
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.smart.htu.utils.FileUtil.downloadFile
import kotlinx.coroutines.launch

@Composable
fun DownloadDialog(
    showDialog: MutableState<Boolean>,
    url: String,
    fileName: String
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    BasicDialog(
        title = "下载附件",
        showDialog = showDialog,
        summary = "是否下载 $fileName",
        confirmRequestText = "下载",
        onConfirmClick = {
            scope.launch {
                downloadFile(
                    context = context,
                    url = url,
                    fileName = fileName,
                    targetDirectory = Environment.DIRECTORY_DOCUMENTS
                )
            }
        },
        content = {}
    )
}