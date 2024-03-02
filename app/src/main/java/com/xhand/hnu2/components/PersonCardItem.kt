package com.xhand.hnu2.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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

@Composable
fun PersonCardItem(
    ifShowCard: Boolean? = true,
    onclick: () -> Unit,
    text: String,
    imageVector: ImageVector
) {
    if (ifShowCard == true) {
        Card(
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
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
            Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(), contentAlignment = Alignment.Center) {
                Text(text = "暂无信息", color = Color.Gray)
            }
        }
    }
}

@Preview
@Composable
fun p() {
    PersonCardItem(onclick = { /*TODO*/ }, text = "消息中心", imageVector = Icons.Default.Email)
}