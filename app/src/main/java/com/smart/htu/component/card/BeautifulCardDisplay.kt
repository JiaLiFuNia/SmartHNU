package com.smart.htu.component.card

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smart.htu.R
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun BeautifulCardDisplay(
    @DrawableRes icon: Int,
    title: String,
    description: String,
    summary: String,
    summaryDesc: String,
    rightBottomText: String,
    rightBottomEndText: String,
    containerColor: Color= MiuixTheme.colorScheme.surface,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.defaultColors(containerColor),
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = "card icon",
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color(0xFF757575),
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = summaryDesc,
                        fontSize = 12.sp,
                        color = Color(0xFF9E9E9E)
                    )
                    Text(
                        text = summary,
                        fontSize = 20.sp,
                        color = Color(0xFF43A047),
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = rightBottomText,
                        fontSize = 12.sp,
                        color = Color(0xFFBDBDBD)
                    )
                    Text(
                        text = rightBottomEndText,
                        fontSize = 12.sp,
                        color = Color(0xFFBDBDBD)
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun PreviewFoodCard() {
    BeautifulCardDisplay(
        icon = R.drawable.climate_mini_split_24px,
        title = "寝室电费",
        description = "河南师范大学寝室空调",
        summary = "￥ 1234.56",
        summaryDesc = "剩余电费",
        rightBottomText = "西区 9 号楼",
        rightBottomEndText = "312寝室",
        containerColor = MiuixTheme.colorScheme.surface
    )
}