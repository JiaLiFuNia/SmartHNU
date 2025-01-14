package com.smart.htu.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.smart.htu.screens.application.entity.SmallCardContent
import com.smart.htu.screens.navigateWithAuthCheck
import com.smart.htu.screens.navigation.Destinations

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SmallCardDisplay(
    enabled: Boolean,
    content: SmallCardContent,
    onLongClick: () -> Unit,
    onCLick: () -> Unit,
) {
    var showDropDownMenu by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .width(70.dp)
            .height(70.dp)
            .clip(RoundedCornerShape(10.dp))
            .combinedClickable(
                onClick = {
                    onCLick()
                },
                onLongClick = {
                    showDropDownMenu = true
                }
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = content.icon),
                    contentDescription = "",
                    modifier = Modifier.size(35.dp),
                    tint = if (enabled) colorScheme.primary
                    else colorScheme.primary.copy(0.38f)
                )
            }
            Text(
                text = stringResource(id = content.label),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 13.sp,
                modifier = Modifier
                    .basicMarquee(
                        repeatDelayMillis = 2_000,
                    ),
                color = if (enabled) colorScheme.onBackground
                else colorScheme.onBackground.copy(0.38f),
                textAlign = TextAlign.Center
            )
        }
        DropdownMenu(
            expanded = showDropDownMenu,
            onDismissRequest = { showDropDownMenu = false },
            shape = RoundedCornerShape(15.dp),
        ) {
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "remove"
                    )
                },
                text = { Text(text = "取消常用") },
                onClick = {
                    onLongClick()
                    showDropDownMenu = false
                }
            )
        }
    }
}