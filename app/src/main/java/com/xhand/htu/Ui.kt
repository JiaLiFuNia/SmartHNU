package com.xhand.htu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }

@Composable
fun <T> DropdownMenuPreference(
    value: T,
    imageVector: ImageVector,
    title: String,
    selections: List<T>,
    onValueChanged: (value: T) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .clickable {
                expanded.value = true
            }
            .padding(vertical = 15.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(start = 25.dp)
                .align(Alignment.CenterVertically)
        ) {
            Icon(imageVector = imageVector, contentDescription = null)
        }
        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 25.dp)
                .fillMaxWidth()
        ) {
            Text(text = title, fontSize = 16.sp)
            Text(
                text = value.toString(),
                color = MaterialTheme.colorScheme.outline,
                fontSize = 14.sp
            )
        }
        Box(modifier = Modifier.weight(1f, true)) {
            DropdownMenu(
                offset = DpOffset((-20).dp, 0.dp),
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
            ) {
                selections.forEach { selection ->
                    DropdownMenuItem(
                        modifier = Modifier.background(
                            if (selection == value) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent
                        ),
                        text = { Text(selection.toString()) },
                        onClick = {
                            expanded.value = false
                            onValueChanged(selection)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SwitchPreference(
    value: Boolean,
    imageVector: ImageVector,
    title: String,
    description: String? = null,
    onValueChanged: (value: Boolean) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .clickable {
                onValueChanged(!value)
            }
            .padding(vertical = 15.dp)
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier.align(Alignment.CenterVertically)) {
            Box(
                modifier = Modifier
                    .padding(start = 25.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Icon(imageVector = imageVector, contentDescription = null)
            }
            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 25.dp)
            ) {
                Text(text = title, fontSize = 16.sp)
                if (description != null) {
                    Text(
                        text = description,
                        color = MaterialTheme.colorScheme.outline,
                        fontSize = 14.sp
                    )
                }
            }
        }
        Switch(
            checked = value,
            onCheckedChange = {
                onValueChanged(it)
            },
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = 20.dp)
        )
    }
}

@Composable
fun TextPreference(
    title: String,
    description: String,
    imageVector: ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable {
                onClick()
            }
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 15.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.align(Alignment.CenterVertically)) {
                Box(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Icon(imageVector = imageVector, contentDescription = null)
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 25.dp)
                ) {
                    Text(text = title, fontSize = 16.sp)
                    Text(
                        text = description,
                        color = MaterialTheme.colorScheme.outline,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PreferenceHeader(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 25.dp, top = 10.dp, bottom = 10.dp),
        fontSize = 14.sp
    )
}

@Composable
fun NoLoginPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Icon(
                imageVector = rememberNoAccounts(),
                contentDescription = null,
                modifier = Modifier.size(150.dp)
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = "您未登录",
                style = MaterialTheme.typography.headlineSmall,
            )
        }
    }
}

@Composable
fun AddAnimatedCircularProgressIndicator(value: Boolean) {
    AnimatedVisibility(
        visible = value,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator(modifier = Modifier)
        }
    }
}

@Composable
fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}
