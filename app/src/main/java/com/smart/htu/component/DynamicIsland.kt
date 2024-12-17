package com.smart.htu.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smart.htu.R

@Composable
fun DynamicIsland(text: String, onClick: () -> Unit, clickAble: Boolean = true) {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .height(35.dp)
            .clickable(
                onClick = onClick,
                enabled = clickAble
            )
            .animateContentSize(), // Add this line to animate size changes,
        onClick = onClick,
        enabled = clickAble,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Box(
            modifier = Modifier.fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 17.sp,
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
    }
}