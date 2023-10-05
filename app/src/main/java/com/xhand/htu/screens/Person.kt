package com.xhand.htu.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xhand.htu.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonScreen() {
    Column(modifier = Modifier) {
        TopAppBar(
            title = { Text("我的") },
            modifier = Modifier
        )
        LazyColumn(
            modifier = Modifier.padding(15.dp)
        ) {
            item { 
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.headimage2),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                    )
                    Column(
                        verticalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .height(75.dp)
                    ) {
                        Text(text = "许博涵", fontSize = 19.sp)
                        Text(text = "2201214001", fontSize = 15.sp)
                        Text(text = "数学与信息科学学院", fontSize = 15.sp)
                    }
                }
            }
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 14.dp)
                ) {
                    Text(
                        text = "今日课程",
                        modifier = Modifier
                            .padding(8.dp),
                        fontSize = 17.sp
                    )
                    Row {
                        Column(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(text = "时间", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text(text = "08:00-09:50")
                            Text(text = "10:10-11:50")
                            Text(text = "15:00-16:40")
                            Text(text = "17:10-18:50")
                            Text(text = "20:00-21:40")
                        }
                        Column(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(text = "课程", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text(text = "现代教育技术实验")
                            Text(text = "数学分析 Ⅲ")
                            Text(text = "中外基础教育比较")
                            Text(text = "常微分方程")
                            Text(text = "教育学")
                        }
                        Column(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(text = "地点", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text(text = "田家炳502")
                            Text(text = "文昌楼102")
                            Text(text = "东教2_1031")
                            Text(text = "启智楼406")
                            Text(text = "启智楼304")
                        }
                    }
                }
            }
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(vertical = 14.dp)
                ) {
                    Text(
                        text = "第二课堂",
                        modifier = Modifier
                            .padding(8.dp),
                        fontSize = 17.sp
                    )
                    Text(
                        text = "请先登录",
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    )
                }
            }
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(vertical = 14.dp)
                ) {
                    Text(
                        text = "课程成绩",
                        modifier = Modifier
                            .padding(8.dp),
                        fontSize = 17.sp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PersonScreenPreview() {
    PersonScreen()
}