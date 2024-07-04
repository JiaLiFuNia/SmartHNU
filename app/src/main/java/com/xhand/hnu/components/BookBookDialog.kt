package com.xhand.hnu.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.xhand.hnu.viewmodel.SettingsViewModel

@Composable
fun BookBookDialog(viewModel: SettingsViewModel, kcrwdm: String, xnxqdm: String) {
    LaunchedEffect(kcrwdm.isNotEmpty()) {
        viewModel.bookDetailService(kcrwdm, xnxqdm)
    }
    AlertDialog(
        title = {
            Text(text = "提示")
        },
        text = {
            if (viewModel.isGettingBookDetail) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CircularProgressIndicator()
                }
            } else {
                if (viewModel.bookAbleList.isEmpty())
                    Text(text = "该门课程无可选教材")
                else{
                    val book = viewModel.bookAbleList[0]
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row {
                            Text(
                                text = "学期",
                                modifier = Modifier.weight(0.3f),
                                textAlign = TextAlign.Center
                            )
                            Text(text = book.pcmc, Modifier.weight(0.7f))
                        }
                        Row {
                            Text(
                                text = "书名",
                                modifier = Modifier.weight(0.3f),
                                textAlign = TextAlign.Center
                            )
                            Text(text = book.jcmc, Modifier.weight(0.7f))
                        }
                        Row {
                            Text(
                                text = "编著",
                                modifier = Modifier.weight(0.3f),
                                textAlign = TextAlign.Center
                            )
                            Text(text = book.zb, Modifier.weight(0.7f))
                        }
                        Row {
                            Text(
                                text = "出版社",
                                modifier = Modifier.weight(0.3f),
                                textAlign = TextAlign.Center
                            )
                            Text(text = book.cbs, Modifier.weight(0.7f))
                        }
                        Row {
                            Text(
                                text = "ISBN",
                                modifier = Modifier.weight(0.3f),
                                textAlign = TextAlign.Center
                            )
                            Text(text = book.isbn, Modifier.weight(0.7f))
                        }
                    }
                }
            }
        },
        onDismissRequest = {  },
        confirmButton = {
            TextButton(onClick = { }, enabled = viewModel.bookAbleList.isNotEmpty()) {
                    Text(text = "选订")
                }
        },
        dismissButton = {
            TextButton(onClick = { viewModel.showBookAlert = false }) {
                Text(text = "取消")
            }
        }
    )
}