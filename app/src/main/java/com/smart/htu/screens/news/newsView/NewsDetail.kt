package com.smart.htu.screens.news.newsView

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kevinnzou.web.rememberWebViewNavigator
import com.kevinnzou.web.rememberWebViewState
import com.kevinnzou.web.rememberWebViewStateWithHTMLData
import com.smart.htu.R
import com.smart.htu.api.module.AttachmentEntity
import com.smart.htu.api.module.NewsMarkEntity
import com.smart.htu.component.BasicDialog
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.DownloadDialog
import com.smart.htu.component.ImagePreviewDialog
import com.smart.htu.component.WebView
import com.smart.htu.screens.navigation.Destinations
import com.smart.htu.screens.news.NewsViewModel
import com.smart.htu.screens.news.newsView.NewsStyle.HORIZONTAL_MARGIN
import com.smart.htu.utils.DateUtil.getCurrentDate
import com.smart.htu.utils.FileUtil.downloadFile
import com.smart.htu.utils.ToastUtil
import com.smart.htu.utils.copyContent
import com.smart.htu.utils.startWebUrl
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.FloatingToolbar
import top.yukonga.miuix.kmp.basic.ListPopup
import top.yukonga.miuix.kmp.basic.ListPopupColumn
import top.yukonga.miuix.kmp.basic.ListPopupDefaults
import top.yukonga.miuix.kmp.basic.PopupPositionProvider
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.ToolbarPosition
import top.yukonga.miuix.kmp.extra.DropdownImpl
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalHazeMaterialsApi::class)
@Composable
fun NewsDetail(
    url: String,
    title: String,
    source: String,
    newsViewModel: NewsViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by newsViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val navigator = rememberWebViewNavigator()

    val hazeState = rememberHazeState()

    val listState = rememberLazyListState()
    val snackBarHostState = remember { SnackbarHostState() }

    val showDropDownMenu = remember { mutableStateOf(false) }
    val showFloatingToolbar = remember { mutableStateOf(true) }

    val isHTUNews = remember { mutableStateOf(url.toUri().host == "www.htu.edu.cn") }

    val newsViewMode = remember { mutableIntStateOf(0) }
    val newsLoading = remember { mutableStateOf(true) }
    val errorMessage = remember { mutableStateOf("") }
    val showErrorMessageDialog = remember { mutableStateOf(false) }
    val showHtml = remember { mutableStateOf(false) }
    val showImagePreview = remember { mutableStateOf(false) }
    val selectedImageData = remember { mutableStateOf("") }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex > 1 || listState.firstVisibleItemScrollOffset > 200 }
            .collect { hasScrolled ->
                showFloatingToolbar.value = !hasScrolled
            }
    }

    LaunchedEffect(url) {
        if (isHTUNews.value) {
            newsViewMode.intValue = 0
            delay(1000)
            newsViewModel.getNewsDetail(url)
        } else {
            newsViewMode.intValue = 1
        }
    }

    LaunchedEffect(errorMessage) {
        showErrorMessageDialog.value = errorMessage.value.isNotEmpty()
    }

    Scaffold(
        containerColor = MiuixTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (uiState.blurEffect) Color.Transparent else MiuixTheme.colorScheme.background,
                    scrolledContainerColor = if (uiState.blurEffect) Color.Transparent else MiuixTheme.colorScheme.background
                ),
                title = {
                    Text(
                        text = source,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                actions = {
                    val dropdownOptions = listOf(
                        stringResource(R.string.share),
                        stringResource(R.string.copy_url),
                        stringResource(R.string.open_outside),
                        stringResource(R.string.forward)
                    )
                    ListPopup(
                        show = showDropDownMenu,
                        popupPositionProvider = ListPopupDefaults.ContextMenuPositionProvider,
                        alignment = PopupPositionProvider.Align.TopRight,
                        onDismissRequest = {
                            showDropDownMenu.value = false
                        }
                    ) {
                        ListPopupColumn {
                            dropdownOptions.forEachIndexed { index, item ->
                                DropdownImpl(
                                    text = item,
                                    isSelected = false,
                                    optionSize = dropdownOptions.size,
                                    onSelectedIndexChange = {
                                        showDropDownMenu.value = false
                                        when (index) {
                                            0 -> {
                                                Intent(Intent.ACTION_SEND).also {
                                                    it.putExtra(Intent.EXTRA_TEXT, url)
                                                    it.type = "text/plain"
                                                    if (it.resolveActivity(context.packageManager) != null) {
                                                        context.startActivity(it)
                                                    }
                                                }
                                            }

                                            1 -> {
                                                scope.launch {
                                                    copyContent(url)
                                                    snackBarHostState.showSnackbar(
                                                        message = context.getString(R.string.copied_to_clipboard)
                                                    )
                                                }
                                            }

                                            2 -> {
                                                startWebUrl(url)
                                            }

                                            3 -> {
                                                if (navigator.canGoForward) navigator.navigateForward()
                                            }
                                        }
                                    },
                                    index = index
                                )
                            }
                        }
                    }
                    IconButton(onClick = { showDropDownMenu.value = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "more")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "close")
                    }
                },
                modifier = Modifier.hazeEffect(
                    state = hazeState,
                    style = HazeMaterials.thick()
                ) {
                    blurRadius = 40.dp
                    blurEnabled = uiState.blurEffect
                }
            )
        },
        floatingToolbar = {
            AnimatedVisibility(
                enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
                exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top),
                visible = showFloatingToolbar.value
            ) {
                FloatingToolbar(
                    modifier = Modifier
                ) {
                    Row(
                        modifier = Modifier
                            .background(MiuixTheme.colorScheme.surfaceContainer)
                        // .hazeEffect(state = hazeState)
                    ) {
                        IconButton(
                            onClick = {
                                if (uiState.aiModelKey.isEmpty()) navController.navigate(
                                    Destinations.AIConfiguration.route
                                )
                                else newsViewModel.aiNewsSummaryService(
                                    {},
                                    uiState.newsArticle?.articleContent ?: ""
                                )
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.wand_stars_24px),
                                contentDescription = "ai",
                                tint = MiuixTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(
                            onClick = {
                                scope.launch {
                                    newsViewModel.addNewsFavorite(
                                        NewsMarkEntity(
                                            title = title,
                                            url = url,
                                            time = LocalDate.now().toString(),
                                            source = source
                                        )
                                    )
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = if (uiState.newsFavoriteList.map {
                                        it.title
                                    }
                                        .contains(title)) R.drawable.star_24px_filled else R.drawable.star_24px),
                                contentDescription = "star",
                                tint = MiuixTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(
                            enabled = isHTUNews.value,
                            onClick = {
                                newsViewMode.intValue = if (newsViewMode.intValue == 0) 1 else 0
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_outline_article),
                                contentDescription = "news",
                                tint = MiuixTheme.colorScheme.onSurface
                            )
                        }
                        IconButton(
                            onClick = {
                                // newsViewModel.changeBionicReadingEnabled(!uiState.bionicReadingEnabled)
                                navController.navigate(Destinations.ArticleStyle.route)
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.format_bold_24px),
                                contentDescription = "news",
                                tint = MiuixTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        },
        floatingToolbarPosition = ToolbarPosition.BottomCenter,
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        },
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(state = hazeState),
            contentPadding = PaddingValues(top = it.calculateTopPadding())
        ) {
            if (newsViewMode.intValue == 0 && uiState.newsArticle == null && newsLoading.value) {
                item { CircularProgressIndicator() }
            } else {
                item {
                    LaunchedEffect(uiState.newsArticle) {
                        if (uiState.newsArticle?.articleContent == null || uiState.newsArticle?.articleContent == null) {
                            newsViewMode.intValue = 1
                        }
                    }
                }
                if (newsViewMode.intValue == 0) {
                    item {
                        TittleContent(
                            title = uiState.newsArticle?.title ?: "无标题",
                            publishDate = uiState.newsArticle?.publishDate ?: getCurrentDate(),
                            visitCount = uiState.newsArticle?.visitCount ?: "10",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                        if (showHtml.value) Text(text = uiState.newsArticle?.articleContent.toString())
                    }
                }

                item {
                    when (newsViewMode.intValue) {
                        0 -> WebView(
                            url = url,
                            webViewState = rememberWebViewStateWithHTMLData(
                                data = NewsHTML.HTML.format(
                                    NewsStyle.get(
                                        fontSize = uiState.newsFontSize,
                                        lineHeight = 1.0F,
                                        letterSpacing = 0.5F,
                                        textMargin = HORIZONTAL_MARGIN,
                                        textColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb(),
                                        textBold = false,
                                        textAlign = "start",
                                        boldTextColor = MaterialTheme.colorScheme.onSurface.toArgb(),
                                        subheadBold = false,
                                        subheadUpperCase = false,
                                        imgMargin = HORIZONTAL_MARGIN,
                                        imgBorderRadius = 4,
                                        imgDisplayMode = if (uiState.loadImgEnabled) "block" else "none",
                                        linkTextColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb(),
                                        codeTextColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb(),
                                        codeBgColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb(),
                                        tableMargin = 0,
                                        selectionTextColor = MaterialTheme.colorScheme.onSurface.toArgb(),
                                        selectionBgColor = MaterialTheme.colorScheme.primaryContainer.toArgb(),
                                        signatureColor = Color.Gray.toArgb()
                                    ),
                                    url,
                                    uiState.newsArticle?.articleContent,
                                    WebViewScript.get(uiState.bionicReadingEnabled)
                                ),
                                baseUrl = url
                            ),
                            onError = {
                                errorMessage.value = it
                            },
                            onFinished = {
                                newsLoading.value = !it
                            },
                            onImageClick = {
                                selectedImageData.value = it
                                showImagePreview.value = true
                            },
                            isShowLinearProgressIndicator = false,
                            navigator = navigator,
                            snackBarHostState = snackBarHostState
                        )

                        else -> WebView(
                            url = url,
                            webViewState = rememberWebViewState(url),
                            navigator = navigator,
                            snackBarHostState = snackBarHostState
                        )
                    }
                }

                if (newsViewMode.intValue == 0) {
                    item {
                        uiState.newsArticle?.attachment
                            .let { attachments ->
                                if (attachments?.isNotEmpty() == true) {
                                    AttachmentContent(
                                        attachments = attachments,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp),
                                        onClick = { url, title ->
                                            navController.navigate("${Destinations.PdfReaderView.route}/${url}/${title}")
                                        }
                                    )
                                }
                            }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    if (showImagePreview.value) {
        ImagePreviewDialog(
            imageUrl = selectedImageData.value,
            onDismiss = { showImagePreview.value = false },
            onDownload = {
                scope.launch {
                    ToastUtil.showToast(context, "正在下载图片：$title.jpg")
                    downloadFile(context, selectedImageData.value, "$title.jpg")
                    ToastUtil.showToast(context, "下载成功")
                }
            }
        )
    }

    BasicDialog(
        showDialog = showErrorMessageDialog,
        title = "提示",
        summary = errorMessage.value,
    ) { }
}

@Composable
fun TittleContent(
    title: String,
    publishDate: String,
    visitCount: String,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.headlineSmall.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "发布时间：${publishDate}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray
                )
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "浏览次数：${visitCount}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray
                )
            )
        }
    }
}

@Composable
fun AttachmentContent(
    attachments: List<AttachmentEntity>,
    modifier: Modifier,
    onClick: (String, String) -> Unit = { _, _ -> }
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        attachments.forEach { attachment ->
            if (attachment.fileName.isNotEmpty() || attachment.url.isNotEmpty()) {
                Card {
                    val showDownloadDialog = remember { mutableStateOf(false) }
                    BasicComponent(
                        leftAction = {
                            Image(
                                painter = painterResource(
                                    id = when (attachment.fileType) {
                                        "pdf" -> R.drawable.ic_pdf
                                        "doc", "docx" -> R.drawable.ic_doc
                                        "xls", "xlsx" -> R.drawable.ic_xls
                                        "ppt", "pptx" -> R.drawable.ic_ppt
                                        "mp3", "wav" -> R.drawable.ic_music
                                        "mp4", "avi", "mkv" -> R.drawable.ic_video
                                        "zip", "rar", "7z" -> R.drawable.ic_zip
                                        "jpg", "jpeg", "png", "gif" -> R.drawable.ic_img
                                        "csv" -> R.drawable.ic_csv
                                        "psd" -> R.drawable.ic_psd
                                        else -> R.drawable.folder_24px
                                    }
                                ),
                                contentDescription = "file",
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .size(36.dp)
                            )
                        },
                        title = attachment.fileName,
                        onClick = {
                            if (attachment.fileType == "pdf" || attachment.isNeedOnlineView)
                                onClick(Uri.encode(attachment.url), attachment.fileName)
                            else
                                showDownloadDialog.value = true
                        }
                    )
                    DownloadDialog(
                        showDialog = showDownloadDialog,
                        fileName = attachment.fileName,
                        url = attachment.url
                    )
                }
            }
        }
    }
}