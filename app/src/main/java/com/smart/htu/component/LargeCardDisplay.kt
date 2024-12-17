package com.smart.htu.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smart.htu.R

@Composable
fun LargeCardDisplay(
    onClick: (() -> Unit)? = null,
    navigateTo: (() -> Unit)? = null,
    modifier: Modifier,
    title: String,
    @DrawableRes leadingIconPainting: Int,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 5.dp)
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = leadingIconPainting),
                    contentDescription = "icon"
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = title, fontSize = 17.sp)
            }
            if (navigateTo != null)
                Row(
                    modifier = Modifier
                        .clickable { navigateTo() },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(id = R.string.all),
                        fontSize = 15.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }
        }
        if (onClick != null) {
            Card(
                modifier = modifier.fillMaxWidth(),
                onClick = onClick,
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                content()
            }
        } else {
            Card(
                modifier = modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                content()
            }
        }
    }
}

@Preview
@Composable
fun LargeCardDisplayPreview() {
    LargeCardDisplay(
        onClick = {},
        modifier = Modifier.height(200.dp),
        title = "Medium Card",
        leadingIconPainting = R.drawable.mode_fan_24px,
        content = {},
        navigateTo = {}
    )
}