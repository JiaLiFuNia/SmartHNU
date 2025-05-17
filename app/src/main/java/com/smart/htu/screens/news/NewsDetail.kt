package com.smart.htu.screens.news

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.navigation.NavController
import com.smart.htu.R
import com.smart.htu.screens.webview.WebViewContent

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NewsDetail(
    url: String,
    title: String,
    newsViewModel: NewsViewModel,
    navController: NavController
) {
    WebViewContent(
        url = url,
        title = title,
        navController = navController,
        content = {
            var fabMenuExpanded by rememberSaveable { mutableStateOf(false) }
            FloatingActionButtonMenu(
                modifier = Modifier.align(Alignment.BottomEnd),
                expanded = fabMenuExpanded,
                button = {
                    FloatingActionButton(
                        onClick = {
                            fabMenuExpanded = !fabMenuExpanded
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Home, contentDescription = "")
                    }
                }
            ) {
                val items = listOf(
                    R.drawable.star_24px to "标星",
                    R.drawable.wand_stars_24px to "AI 总结"
                )

                items.forEachIndexed { i, item ->
                    FloatingActionButtonMenuItem(
                        modifier =
                            Modifier.semantics {
                                isTraversalGroup = true
                                if (i == items.size - 1) {
                                    customActions =
                                        listOf(
                                            CustomAccessibilityAction(
                                                label = "Close menu",
                                                action = {
                                                    fabMenuExpanded = false
                                                    true
                                                }
                                            )
                                        )
                                }
                            },
                        onClick = {
                        },
                        icon = { Icon(painterResource(item.first), contentDescription = null) },
                        text = { Text(text = item.second) },
                    )
                }
            }
        }
    )
}