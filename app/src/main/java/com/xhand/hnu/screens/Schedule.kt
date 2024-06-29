package com.xhand.hnu.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    onBack: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
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
                    Text(text = "大二 秋季学期")
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
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(paddingValues = it)
                .padding(start = 5.dp, end = 5.dp)
        ) {
            Column {
                Row {
                    Text(
                        text = "6\n月",
                        modifier = Modifier.weight(0.1f),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "一\n6/17",
                        modifier = Modifier.weight((0.9 / 7.0).toFloat()),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "二\n6/18",
                        modifier = Modifier.weight((0.9 / 7.0).toFloat()),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "三\n6/19",
                        modifier = Modifier.weight((0.9 / 7.0).toFloat()),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "四\n6/20",
                        modifier = Modifier.weight((0.9 / 7.0).toFloat()),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "五\n6/21",
                        modifier = Modifier.weight((0.9 / 7.0).toFloat()),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "六\n6/22",
                        modifier = Modifier.weight((0.9 / 7.0).toFloat()),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "日\n6/23",
                        modifier = Modifier.weight((0.9 / 7.0).toFloat()),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                }
            }
            Column(Modifier.padding(top = 5.dp)) {
                Row {
                    Column(
                        Modifier.weight(0.1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "1\n08:00\n08:45\n", textAlign = TextAlign.Center,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "2\n08:55\n09:40", textAlign = TextAlign.Center,
                            fontSize = 12.sp
                        )
                    }
                    Card(
                        Modifier
                            .weight((0.9 / 7.0).toFloat())
                            .padding(2.dp)
                    ) {
                        Text("数值逼近")
                    }
                    Card(
                        Modifier
                            .weight((0.9 / 7.0).toFloat())
                            .padding(2.dp)
                    ) {
                        Text("数值逼近")
                    }
                    Card(
                        Modifier
                            .weight((0.9 / 7.0).toFloat())
                            .padding(2.dp)
                    ) {
                        Text("数值逼近")
                    }
                    Card(
                        Modifier
                            .weight((0.9 / 7.0).toFloat())
                            .padding(2.dp)
                    ) {
                        Text("数值逼近")
                    }
                    Card(
                        Modifier
                            .weight((0.9 / 7.0).toFloat())
                            .padding(2.dp)
                    ) {
                        Text("数值逼近")
                    }
                    Card(
                        Modifier
                            .weight((0.9 / 7.0).toFloat())
                            .padding(2.dp)
                    ) {
                        Text("数值逼近")
                    }
                    Card(
                        Modifier
                            .weight((0.9 / 7.0).toFloat())
                            .padding(2.dp)
                    ) {
                        Text("数值逼近")
                    }

                }
            }
        }
    }
}


@Composable
@Preview
fun p() {
    ScheduleScreen(onBack = {})
}