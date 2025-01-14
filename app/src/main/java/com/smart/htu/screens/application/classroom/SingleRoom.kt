package com.smart.htu.screens.application.classroom

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SingleRoom(
    label: String,
    state: Boolean,
    timeRange: String,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Card(
        modifier = modifier
            .height(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (state) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer
        ),
        onClick = {
            onClick()
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.basicMarquee(
                    repeatDelayMillis = 2_000,
                )
            )
        }
    }
}

@Preview
@Composable
fun SingleRoomStatePreview() {
    SingleRoom(
        label = "新五五四888",
        state = true,
        timeRange = "8:00-10:00",
        onClick = {},
        modifier = Modifier
    )
}