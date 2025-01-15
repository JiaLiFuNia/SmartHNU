package com.smart.htu.component

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun MediumCardDisplay(
    onClick: () -> Unit,
    navigateTo: (() -> Unit)? = null,
    modifier: Modifier,
    title: String,
    leadingContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(110.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    leadingContent()
                    if (navigateTo != null) {
                        IconButton(
                            onClick = navigateTo,
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.TopEnd)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "navigate",
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }
                }
            }
            Text(
                text = title,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                lineHeight = 20.sp,
                maxLines = 1,
                modifier = Modifier.basicMarquee(
                    repeatDelayMillis = 2_000,
                )
            )
            Box(modifier = Modifier.fillMaxSize()) {
                content()
            }
        }
    }
}

@Preview
@Composable
fun MediumCardDisplayPreview() {
    val today = LocalDate.now()
    val context = LocalContext.current
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.width(750.dp)
    ) {
        MediumCardDisplay(
            onClick = {
            },
            modifier = Modifier.weight(3 / 10f),
            title = "${today.year}/${today.dayOfMonth}",
            leadingContent = {
                Text(
                    text = "${today.dayOfMonth}",
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            content = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(text = "冬月初九 星期一", color = Color.Gray)
                }
            }
        )
        /*Spacer(modifier = Modifier.width(20.dp))
        MediumCardDisplay(
            onClick = {
            },
            modifier = Modifier.weight(3 / 10f),
            title = "空调电费",
            leadingContent = {

            },
            content = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        text = "总剩余电费 53 度", color = Color.Gray,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee(
                            repeatDelayMillis = 2_000,
                        ),
                    )
                }
            },
            navigateTo = {
                openInBrowser(
                    url = "https://houqin.htu.edu.cn/one/plan"
                )
            }
        )*/
        Spacer(modifier = Modifier.width(20.dp))
        MediumCardDisplay(
            onClick = {
            },
            modifier = Modifier.weight(3 / 10f),
            title = "多云转晴",
            leadingContent = {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "11",
                        fontSize = 30.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "℃",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            },
            content = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        text = "-1~11 ℃ | 1 级风",
                        color = Color.Gray,
                        maxLines = 1
                    )
                }
            }
        )
    }
}