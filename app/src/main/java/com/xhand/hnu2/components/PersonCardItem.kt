package com.xhand.hnu2.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xhand.hnu2.model.entity.KbList
import com.xhand.hnu2.viewmodel.SettingsViewModel

@Composable
fun PersonCardItem(
    onclick: () -> Unit,
    text: String,
    imageVector: ImageVector,
    schedule: MutableList<KbList>
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onclick,
    ) {
        Row(
            modifier = Modifier
                .padding(start = 8.dp, top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                modifier = Modifier.padding(end = 3.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        if (schedule.size != 0)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, top = 8.dp),
            ) {
                schedule.forEach { schedule ->
                    Column {
                        Text(text = "课程名称 ${schedule.kcmc}")
                        Text(text = "授课教师 ${schedule.teaxms}")
                        Text(text = "上课地点 ${schedule.jxcdmc}")
                        Text(text = "上课时间 ${schedule.qssj} - ${schedule.jssj}")
                        Text(text = "评价方式 ${schedule.khfsmc}")
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        else
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "暂无信息", color = Color.Gray)
            }
    }

}