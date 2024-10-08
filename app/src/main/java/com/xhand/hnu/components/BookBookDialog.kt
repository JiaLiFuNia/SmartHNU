package com.xhand.hnu.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
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
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.xhand.hnu.repository.Repository
import com.xhand.hnu.viewmodel.BookUiState
import com.xhand.hnu.viewmodel.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookBottomSheet(
    viewModel: BookViewModel,
    kcrwdm: String,
    xnxqdm: String,
    kcmc: String,
    uiState: BookUiState
) {
    val bottomSheetState = rememberModalBottomSheetState()
    val tabs = listOf("可选教材", "已选教材")
    val cbManager = LocalClipboardManager.current
    val scrollState = rememberScrollState()
    val context = LocalContext.current
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
                containerColor = Color.Transparent,
                indicator = {
                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(
                            selectedTabIndex,
                            matchContentSize = true
                        ),
                        width = 40.dp,
                        shape = RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp)
                    )
                },
                divider = {}
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(text = tab) },
                        selectedContentColor = colorScheme.primary,
                        unselectedContentColor = colorScheme.onSurface,
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
            ) {
                if (selectedTabIndex == 0) {
                    if (uiState.isGettingBookDetail)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    else
                        if (uiState.bookAbleList.isEmpty())
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "没有可选教材", color = Color.Gray)
                            }
                        else
                            uiState.bookAbleList.forEach { book ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp),
                                    onClick = {
                                        copyText(
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
                                                    copyText(
                                                        cbManager,
                                                        book.isbn
                                                    )
                                                    Toast.makeText(
                                                        context,
                                                        "已复制",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            ) {
                                                Text(text = "复制")
                                            }
                                            TextButton(
                                                onClick = {
                                                    Toast.makeText(
                                                        context,
                                                        "暂未实现该功能",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            ) {
                                                Text(text = "选订")
                                            }
                                        }
                                    }
                                }
                            }
                } else {
                    BookList(
                        cbManager = cbManager,
                        uiState = uiState
                    )
                }
            }

        }
    }
}


@Composable
fun BookList(cbManager: ClipboardManager, uiState: BookUiState) {
    val context = LocalContext.current
    if (uiState.isGettingBookSelected)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    else
        if (uiState.bookSelectedList.isEmpty())
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "没有已选教材", color = Color.Gray)
            }
        else
            uiState.bookSelectedList.forEach { book ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    onClick = {
                        copyText(
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
                            TextButton(
                                onClick = {
                                    copyText(
                                        cbManager,
                                        book.isbn
                                    )
                                    Toast.makeText(
                                        context,
                                        "已复制",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            ) {
                                Text(text = "复制")
                            }
                            TextButton(onClick = {
                                Toast.makeText(
                                    context,
                                    "暂未实现该功能",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }) {
                                Text(text = "退订")
                            }
                        }
                    }
                }
            }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookSelectBottomSheet(
    viewModel: BookViewModel,
    uiState: BookUiState
) {
    val bottomSheetState = rememberModalBottomSheetState()
    val term = Repository.getCurrentTerm()
    val longGradeTerm = term.longGradeTerm
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
                uiState.term!!.longGradeTerm.forEachIndexed { index, term ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = longGradeTerm[viewModel.selectTerm] == term,
                                onClick = { viewModel.selectTerm = index }
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = longGradeTerm[viewModel.selectTerm] == term,
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