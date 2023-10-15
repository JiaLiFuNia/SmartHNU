package com.xhand.htu.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.xhand.htu.components.ArticleItem
import com.xhand.htu.viewmodel.ActiveArticleViewModel
import com.xhand.htu.viewmodel.ArticleViewModel
import com.xhand.htu.viewmodel.NewsViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NewsScreen(
    vm: NewsViewModel = viewModel(),
    articleViewModel: ArticleViewModel = viewModel(),
    activeArticleViewModel: ActiveArticleViewModel = viewModel(),
    onNavigateToArticle: () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        vm.categoryData()
    }
    Column(modifier = Modifier) {
        //标题栏
        TopAppBar(
            title = { Text("新闻") }
        )
        //搜索框
        /*var text by remember{ mutableStateOf("")}
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = text,
                onValueChange = {
                    text = it
                },
                leadingIcon = {
                    Row {
                        Icon(Icons.Filled.Search, null)
                    }

                },
            )
        }*/
        Row{
            //切换标签
            TabRow(
                selectedTabIndex = vm.categoryIndex,
                modifier = Modifier
                    .padding(horizontal = 15.dp,vertical = 8.dp)
            ) {
                vm.categories.forEachIndexed{index, category ->
                    Tab(
                        selected = vm.categoryIndex == index,
                        onClick = {
                            vm.updateCategoryIndex(index)
                        }
                    ) {
                        Text(
                            text = category.title,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                        )
                    }
                }
            }
            //搜索按钮
            /*IconButton(onClick = { ) {
                Icon(imageVector = Icons.Filled.Search,contentDescription = null)
            }*/
        }


        if (vm.categoryIndex == 0) {
            //图片
            HorizontalPager(
                pageCount = vm.swiperData.size,
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) { index ->
                AsyncImage(
                    model = vm.swiperData[index].imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16 / 9f), contentScale = ContentScale.Crop
                )
            }
            //通知通告
            LazyColumn() {
                items(articleViewModel.list) { article ->
                    ArticleItem(
                        article,
                        modifier = Modifier
                            .clickable{
                                onNavigateToArticle()
                            }
                    )
                }
            }
        }

        if (vm.categoryIndex == 1) {
            //活动通知
            LazyColumn() {
                items(activeArticleViewModel.list){ article ->
                    ArticleItem(
                        article,
                        modifier = Modifier
                            .clickable{
                                onNavigateToArticle()
                            }
                    )
                }
            }
        }


    }
}

@Preview
@Composable
fun NewsScreenPreview() {
    NewsScreen()
}