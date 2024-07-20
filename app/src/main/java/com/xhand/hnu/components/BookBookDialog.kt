package com.xhand.hnu.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.xhand.hnu.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookBottomSheet(viewModel: SettingsViewModel, kcrwdm: String, xnxqdm: String, kcmc: String) {
    val bottomSheetState = rememberModalBottomSheetState()
    val tabs = listOf("可选教材", "已选教材")
    val cbManager = LocalClipboardManager.current
    val scrollState = rememberScrollState()
    LaunchedEffect(kcrwdm.isNotEmpty()) {
        viewModel.bookDetailService(kcrwdm, xnxqdm)
        viewModel.bookSelectedService(kcrwdm, xnxqdm)
    }
    ModalBottomSheet(
        onDismissRequest = { viewModel.showBookAlert = false },
        sheetState = bottomSheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = kcmc,
                style = MaterialTheme.typography.headlineSmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            var selectedTabIndex by remember { mutableIntStateOf(0) }
            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.Transparent
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(text = tab) }
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
            ) {
                if (selectedTabIndex == 0) {
                    if (viewModel.isGettingBookDetail)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    else
                        if (viewModel.bookAbleList.isEmpty())
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "没有可选教材", color = Color.Gray)
                            }
                        else
                            viewModel.bookAbleList.forEach { book ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp),
                                    onClick = {
                                        viewModel.copyText(
                                            cbManager,
                                            book.isbn
                                        )
                                    },
                                    colors = CardDefaults.cardColors(Color.Transparent)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp)
                                    ) {
                                        Row {
                                            Text(
                                                text = "学期",
                                                modifier = Modifier.weight(0.25f),
                                                textAlign = TextAlign.Left,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(text = book.pcmc, Modifier.weight(0.75f))
                                        }
                                        Row {
                                            Text(
                                                text = "书名",
                                                modifier = Modifier.weight(0.25f),
                                                textAlign = TextAlign.Left,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(text = book.jcmc, Modifier.weight(0.75f))
                                        }
                                        Row {
                                            Text(
                                                text = "编著",
                                                modifier = Modifier.weight(0.25f),
                                                textAlign = TextAlign.Left,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(text = book.zb, Modifier.weight(0.75f))
                                        }
                                        Row {
                                            Text(
                                                text = "出版社",
                                                modifier = Modifier.weight(0.25f),
                                                textAlign = TextAlign.Left,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(text = book.cbs, Modifier.weight(0.75f))
                                        }
                                        Row {
                                            Text(
                                                text = "ISBN",
                                                modifier = Modifier.weight(0.25f),
                                                textAlign = TextAlign.Left,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(text = book.isbn, Modifier.weight(0.75f))
                                        }
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            TextButton(
                                                onClick = {
                                                    viewModel.copyText(
                                                        cbManager,
                                                        book.isbn
                                                    )
                                                }
                                            ) {
                                                Text(text = "复制")
                                            }
                                            TextButton(onClick = { /*TODO*/ }) {
                                                Text(text = "选订")
                                            }
                                        }
                                    }
                                }
                            }
                } else {
                    if (viewModel.isGettingBookSelected)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    else
                        if (viewModel.bookSelectedList.isEmpty())
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "没有已选教材", color = Color.Gray)
                            }
                        else
                            viewModel.bookSelectedList.forEach { book ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp),
                                    onClick = {
                                        viewModel.copyText(
                                            cbManager,
                                            book.isbn
                                        )
                                    },
                                    colors = CardDefaults.cardColors(Color.Transparent)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp)
                                    ) {
                                        Row {
                                            Text(
                                                text = "学期",
                                                modifier = Modifier.weight(0.25f),
                                                textAlign = TextAlign.Left,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(text = book.pcmc, Modifier.weight(0.75f))
                                        }
                                        Row {
                                            Text(
                                                text = "课程名称",
                                                modifier = Modifier.weight(0.25f),
                                                textAlign = TextAlign.Left,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(text = book.kcmc, Modifier.weight(0.75f))
                                        }
                                        Row {
                                            Text(
                                                text = "书名",
                                                modifier = Modifier.weight(0.25f),
                                                textAlign = TextAlign.Left,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(text = book.jcmc, Modifier.weight(0.75f))
                                        }
                                        Row {
                                            Text(
                                                text = "编著",
                                                modifier = Modifier.weight(0.25f),
                                                textAlign = TextAlign.Left,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(text = book.zb, Modifier.weight(0.75f))
                                        }
                                        Row {
                                            Text(
                                                text = "出版社",
                                                modifier = Modifier.weight(0.25f),
                                                textAlign = TextAlign.Left,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(text = book.cbs, Modifier.weight(0.75f))
                                        }
                                        Row {
                                            Text(
                                                text = "ISBN",
                                                modifier = Modifier.weight(0.25f),
                                                textAlign = TextAlign.Left,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(text = book.isbn, Modifier.weight(0.75f))
                                        }
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            TextButton(onClick = {
                                                viewModel.copyText(
                                                    cbManager,
                                                    book.isbn
                                                )
                                            }) {
                                                Text(text = "复制")
                                            }
                                            TextButton(onClick = { /*TODO*/ }) {
                                                Text(text = "退订")
                                            }
                                        }
                                    }
                                }
                            }
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookSelectBottomSheet(viewModel: SettingsViewModel) {
    val bottomSheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = { viewModel.showBookSelect = false },
        sheetState = bottomSheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "选择学期",
                style = MaterialTheme.typography.headlineSmall
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                viewModel.longGradeTerm.forEachIndexed { index, term ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = viewModel.longGradeTerm[viewModel.selectTerm] == term,
                                onClick = { viewModel.selectTerm = index }
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = viewModel.longGradeTerm[viewModel.selectTerm] == term,
                            onClick = null
                        )
                        Text(
                            text = term,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
    }
}