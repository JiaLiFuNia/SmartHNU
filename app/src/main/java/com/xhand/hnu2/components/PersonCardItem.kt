package com.xhand.hnu2.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xhand.hnu2.R

@Composable
fun PersonCardItem(
    isChecked: Boolean,
    onclick: () -> Unit,
    text: String,
    rightText: String?,
    imageVector: ImageVector,
    content: @Composable () -> Unit = {}
) {
    if (isChecked)
        Card(
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier
                .fillMaxWidth(),
            onClick = onclick,
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 8.dp, top = 8.dp, end = 8.dp)
                    .fillMaxWidth(),
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
                if (rightText != null) {
                    Text(text = rightText, textAlign = TextAlign.Right, color = Color.Gray, modifier = Modifier.fillMaxWidth())
                }
            }
            content()
        }
}

@Composable
fun PersonFunctionCardItem(
    modifier: Modifier,
    title: String,
    painterResource: Int,
    onClick: () -> Unit
) {
    Box(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    colorScheme.primary.copy(alpha = 0.08f)
                        .compositeOver(colorScheme.surface.copy())
                ),
                modifier = Modifier
                    .height(55.dp)
                    .width(55.dp),
                onClick = onClick
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        painterResource(id = painterResource),
                        contentDescription = null,
                        modifier = Modifier
                            .size(35.dp)
                    )
                }
            }
            Text(text = title, modifier = Modifier.padding(top = 4.dp), fontSize = 13.sp)
        }
    }
}