package com.smart.htu.component.card

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.smart.htu.screens.main.FocusCardItem

@Composable
fun MessageCardDisplay(
    modifier: Modifier,
    message: List<SingleInfo>,
) {
    top.yukonga.miuix.kmp.basic.Card(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(0.5f)
            ) {
                message.forEachIndexed { index, it ->
                    if (index < message.size / 2)
                        FocusCardItem(
                            title = it.label,
                            content = it.content,
                            leadingContent = {
                                Icon(
                                    painter = painterResource(it.leadingIcon),
                                    contentDescription = null
                                )
                            },
                            onClick = { },
                            modifier = Modifier
                        )
                }
            }
            Column(
                modifier = Modifier.weight(0.5f)
            ) {
                message.forEachIndexed { index, it ->
                    if (index >= message.size / 2)
                        FocusCardItem(
                            title = it.label,
                            content = it.content,
                            leadingContent = {
                                Icon(
                                    painter = painterResource(it.leadingIcon),
                                    contentDescription = null
                                )
                            },
                            onClick = { },
                            modifier = Modifier
                        )
                }
            }
        }
    }
}

data class SingleInfo(
    val label: String,
    val content: String,
    @DrawableRes val leadingIcon: Int
)