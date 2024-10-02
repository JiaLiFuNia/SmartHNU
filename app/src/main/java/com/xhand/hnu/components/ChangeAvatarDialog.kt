package com.xhand.hnu.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xhand.hnu.R
import com.xhand.hnu.viewmodel.SettingsViewModel

@Composable
fun ChangeAvatarDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: (Int) -> Unit
) {
    var selectedIndex by remember { mutableIntStateOf(-1) }

    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(onClick = { onConfirmation(selectedIndex) }) {
                Text(text = "确认")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(text = "取消")
            }
        },
        title = {
            Column {
                Text(text = "选择头像", style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "请从以下列表中选择一个作为头像，暂不支持自定义",
                    style = MaterialTheme.typography.titleSmall
                )
            }
        },
        text = {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4)
            ) {
                items(ic_ids.size) { index ->
                    Image(
                        painter = painterResource(id = ic_ids[index]),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(5.dp)// 图片内边距
                            .clip(CircleShape)
                            .clickable {
                                selectedIndex = index
                            }
                            .border(
                                width = if (selectedIndex == index) 3.dp else 0.dp,
                                color = if (selectedIndex == index) MaterialTheme.colorScheme.primary else Color.Transparent,
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    )
}

val ic_ids = listOf(
    R.drawable.ic_1,
    R.drawable.ic_2,
    R.drawable.ic_3,
    R.drawable.ic_4,
    R.drawable.ic_5,
    R.drawable.ic_6,
    R.drawable.ic_7,
    R.drawable.ic_8,
    R.drawable.ic_9,
    R.drawable.ic_10,
    R.drawable.ic_11,
    R.drawable.ic_12
)


@Preview
@Composable
fun ChangeAvatarDialogPreview() {
    ChangeAvatarDialog(onDismissRequest = {}, onConfirmation = {})
}