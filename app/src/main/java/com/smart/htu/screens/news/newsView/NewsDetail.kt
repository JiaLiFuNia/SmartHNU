package com.smart.htu.screens.news.newsView

import android.content.Intent
import android.os.Environment
import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.FormatBold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kevinnzou.web.WebViewNavigator
import com.kevinnzou.web.rememberWebViewNavigator
import com.kevinnzou.web.rememberWebViewState
import com.kevinnzou.web.rememberWebViewStateWithHTMLData
import com.smart.htu.R
import com.smart.htu.api.module.AttachmentEntity
import com.smart.htu.api.module.NewsArticleEntity
import com.smart.htu.api.module.NewsMarkEntity
import com.smart.htu.component.BlurredBar
import com.smart.htu.component.CircularProgressIndicator
import com.smart.htu.component.DownloadDialog
import com.smart.htu.component.ImagePreviewDialog
import com.smart.htu.component.WebView
import com.smart.htu.component.rememberBlurBackdrop
import com.smart.htu.screens.LocalNavigator
import com.smart.htu.screens.navigation.Route
import com.smart.htu.screens.news.NewsViewModel
import com.smart.htu.screens.news.newsView.NewsStyle.HORIZONTAL_MARGIN
import com.smart.htu.screens.news.newsView.NewsStyle.fontList
import com.smart.htu.utils.DateUtil.getCurrentDate
import com.smart.htu.utils.FileUtil.downloadFile
import com.smart.htu.utils.ToastUtil.showToast
import com.smart.htu.utils.copyContent
import com.smart.htu.utils.startWebUrl
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.DropdownImpl
import top.yukonga.miuix.kmp.basic.FloatingToolbar
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.ListPopupColumn
import top.yukonga.miuix.kmp.basic.ListPopupDefaults
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.PopupPositionProvider
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.SmallTopAppBar
import top.yukonga.miuix.kmp.basic.SnackbarHostState
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.ToolbarPosition
import top.yukonga.miuix.kmp.basic.VerticalScrollBar
import top.yukonga.miuix.kmp.basic.rememberScrollBarAdapter
import top.yukonga.miuix.kmp.blur.LayerBackdrop
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back
import top.yukonga.miuix.kmp.icon.extended.Copy
import top.yukonga.miuix.kmp.icon.extended.More
import top.yukonga.miuix.kmp.icon.extended.Share
import top.yukonga.miuix.kmp.interfaces.ExperimentalScrollBarApi
import top.yukonga.miuix.kmp.overlay.OverlayBottomSheet
import top.yukonga.miuix.kmp.overlay.OverlayDialog
import top.yukonga.miuix.kmp.overlay.OverlayListPopup
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic
import java.time.LocalDate
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun NewsDetail(
    url: String,
    title: String,
    source: String,
    newsViewModel: NewsViewModel = hiltViewModel()
) {
    val navigator = LocalNavigator.current
    val uiState by newsViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val webViewNavigator = rememberWebViewNavigator()
    val listState = rememberLazyListState()
    val scrollState = rememberScrollState()
    val snackBarHostState = remember { SnackbarHostState() }

    val showDropDownMenu = remember { mutableStateOf(false) }

    val isHTUNews = remember { derivedStateOf { url.toUri().host == "www.htu.edu.cn" } }
    val isLoadingContent = rememberSaveable { mutableStateOf(true) }
    val newsViewMode = remember { mutableIntStateOf(0) }

    var errorMessage by remember { mutableStateOf("") }
    val showErrorMessageDialog = remember(errorMessage) {
        mutableStateOf(errorMessage.isNotEmpty())
    }

    val showImagePreview = remember { mutableStateOf(false) }
    val selectedImageData = remember { mutableStateOf("") }

    var downloadedFileUrl by remember { mutableStateOf("") }
    var downloadFileName by remember { mutableStateOf("") }
    val showDownloadDialog = remember(downloadFileName, downloadedFileUrl) {
        mutableStateOf(downloadedFileUrl.isNotEmpty() && downloadFileName.isNotEmpty())
    }

    val showAISummaryBottomSheet = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (isHTUNews.value) {
            newsViewModel.fetchNewsDetail(url) {
                errorMessage = it
                newsViewMode.intValue = 1
            }
            delay(500.milliseconds)
            isLoadingContent.value = false
        } else {
            newsViewMode.intValue = 1
        }
    }

    val scrollBehavior = MiuixScrollBehavior()
    val backdrop = rememberBlurBackdrop(uiState.blurEffect)
    val blurActive = backdrop != null
    val barColor = if (blurActive) Color.Transparent else MiuixTheme.colorScheme.surface

    Scaffold(
        topBar = {
            BlurredBar(backdrop = backdrop, blurEnabled = blurActive) {
                SmallTopAppBar(
                    title = source,
                    scrollBehavior = scrollBehavior,
                    color = barColor,
                    navigationIcon = {
                        IconButton(
                            onClick = { navigator.pop() },

                            ) {
                            Icon(
                                imageVector = MiuixIcons.Regular.Back,
                                contentDescription = "close",
                                tint = MiuixTheme.colorScheme.onBackground
                            )
                        }
                    },
                    actions = {
                        /*IconButton(onClick = {
                            webViewNavigator.reload()
                        }) {
                            Icon(
                                imageVector = MiuixIcons.Refresh,
                                contentDescription = "refresh",
                            )
                        }*/
                        IconButton(
                            onClick = {
                                Intent(Intent.ACTION_SEND).also {
                                    it.putExtra(
                                        Intent.EXTRA_TEXT,
                                        "$title $url"
                                    )
                                    it.type = "text/plain"
                                    if (it.resolveActivity(context.packageManager) != null) {
                                        context.startActivity(it)
                                    }
                                }
                            }
                        ) {
                            Icon(MiuixIcons.Share, contentDescription = "share")
                        }
                        IconButton(
                            onClick = { showDropDownMenu.value = true },
                            holdDownState = showDropDownMenu.value
                        ) {
                            Icon(
                                MiuixIcons.Regular.More, contentDescription = "more",
                                tint = MiuixTheme.colorScheme.onBackground
                            )
                        }
                        val dropdownOptions = listOf(
                            stringResource(R.string.copy_url),
                            stringResource(R.string.open_outside),
                            stringResource(R.string.forward),
                            "复制 HTML 文本"
                        )
                        OverlayListPopup(
                            show = showDropDownMenu.value,
                            popupPositionProvider = ListPopupDefaults.ContextMenuPositionProvider,
                            alignment = PopupPositionProvider.Align.TopEnd,
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
                                                    scope.launch {
                                                        copyContent(url)
                                                        showToast(context, "已复制到剪贴板")
                                                    }
                                                }

                                                1 -> {
                                                    startWebUrl(url)
                                                }

                                                2 -> {
                                                    if (webViewNavigator.canGoForward) webViewNavigator.navigateForward()
                                                }

                                                3 -> {
                                                    scope.launch {
                                                        copyContent(uiState.newsContent?.articleContent.toString())
                                                        showToast(context, "已复制到剪贴板")
                                                    }
                                                }
                                            }
                                        },
                                        index = index
                                    )
                                }
                            }
                        }
                    }
                )
            }
        },
        floatingToolbar = {
            FloatingToolbar(
                modifier = Modifier,
                cornerRadius = 20.dp
            ) {
                Row(
                    modifier = Modifier
                        .background(Color.Transparent)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    /*IconButton(
                        onClick = {
                            if (uiState.aiModelKey.isEmpty()) {
                                navigator.push(Route.AIConfiguration)
                            } else {
                                showAISummaryBottomSheet.value = true
                                if (!uiState.isAISummaryReasoning)
                                    newsViewModel.aiNewsSummaryService(
                                        articleTitle = uiState.newsContent?.title ?: "",
                                        articleContent = uiState.newsContent?.articleContent
                                            ?: "",
                                        publishDate = uiState.newsContent?.publishDate
                                            ?: getCurrentDate()
                                    )
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.wand_stars_24px),
                            contentDescription = "ai",
                            tint = MiuixTheme.colorScheme.onSurface
                        )
                    }*/
                    IconButton(
                        onClick = {
                            scope.launch {
                                newsViewModel.addNewsFavorite(
                                    NewsMarkEntity(
                                        title = title,
                                        url = url,
                                        time = LocalDate.now().toString(),
                                        source = source
                                    ),
                                    onResult = {
                                        if (it) {
                                            showToast(context, "已添加到收藏")
                                        } else {
                                            showToast(context, "已从收藏夹移除")
                                        }
                                    }
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
                            imageVector = Icons.AutoMirrored.Outlined.Article,
                            contentDescription = "news",
                            tint = MiuixTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(
                        onClick = {
                            navigator.push(Route.ArticleStyle)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FormatBold,
                            contentDescription = "news",
                            tint = MiuixTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        },
        floatingToolbarPosition = ToolbarPosition.BottomEnd,
        modifier = Modifier.fillMaxSize()
    ) {
        if (newsViewMode.intValue == 0) {
            ParsedArticleView(
                padding = it,
                isLoadingContent = isLoadingContent,
                newsContent = uiState.newsContent,
                url = url,
                fontSize = uiState.newsFontSize,
                fontFamilyIndex = uiState.newsFontFamilyIndex,
                loadImgEnabled = uiState.loadImgEnabled,
                onImgClick = {
                    selectedImageData.value = it
                    showImagePreview.value = true
                },
                onError = {
                    errorMessage = it
                },
                onDownloadClick = { fileUrl, fileName ->
                    downloadedFileUrl = fileUrl
                    downloadFileName = fileName
                },
                webViewNavigator = webViewNavigator,
                snackBarHostState = snackBarHostState,
                backdrop = backdrop
            )
        } else {
            RawArticleView(
                modifier = Modifier.padding(top = it.calculateTopPadding()),
                url = url,
                onError = {
                    errorMessage = it
                },
                onDownloadClick = { fileUrl, fileName ->
                    downloadedFileUrl = fileUrl
                    downloadFileName = fileName
                },
                webViewNavigator = webViewNavigator,
                snackBarHostState = snackBarHostState
            )
        }

        DownloadDialog(
            showDialog = showDownloadDialog.value,
            fileName = downloadFileName,
            url = downloadedFileUrl,
            onDismissRequest = {
                showDownloadDialog.value = false
                downloadFileName = ""
                downloadedFileUrl = ""
            }
        )

        AISummaryBottomSheet(
            showDialog = showAISummaryBottomSheet.value,
            aiSummaryContent = uiState.aiSummaryContent,
            aiSummaryReasoningContent = uiState.aiSummaryReasoningContent,
            onDismissRequest = { showAISummaryBottomSheet.value = false }
        )

        ErrorMessageDialog(
            showDialog = showErrorMessageDialog.value,
            errorMessage = errorMessage,
            onDismissRequest = {
                showErrorMessageDialog.value = false
                navigator.pop()
            },
            onReload = {
                errorMessage = ""
                webViewNavigator.reload()
            }
        )

    }

    if (showImagePreview.value) {
        ImagePreviewDialog(
            imageUrl = selectedImageData.value,
            onDismiss = { showImagePreview.value = false },
            onDownload = {
                scope.launch {
                    showToast(context, "正在下载图片：$title.jpg")
                    downloadFile(
                        context,
                        selectedImageData.value,
                        "$title.jpg",
                        Environment.DIRECTORY_PICTURES
                    )
                    showToast(context, "下载成功")
                }
            }
        )
    }
}

@OptIn(ExperimentalScrollBarApi::class)
@Composable
private fun ParsedArticleView(
    padding: PaddingValues,
    isLoadingContent: MutableState<Boolean>,
    newsContent: NewsArticleEntity? = null,
    url: String,
    fontSize: Int,
    fontFamilyIndex: Int = 0,
    loadImgEnabled: Boolean,
    onImgClick: (imgUrl: String) -> Unit,
    onError: (String) -> Unit,
    onDownloadClick: (fileUrl: String, fileName: String) -> Unit,
    webViewNavigator: WebViewNavigator,
    snackBarHostState: SnackbarHostState,
    backdrop: LayerBackdrop? = null
) {
    val lazyListState = rememberLazyListState()
    val navigator = LocalNavigator.current

    Box(
        modifier = if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .overScrollVertical()
                .scrollEndHaptic(),
            state = lazyListState,
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 12.dp,
                bottom = padding.calculateBottomPadding() + 84.dp
            )
        ) {
            if (isLoadingContent.value) {
                item { CircularProgressIndicator() }
            } else {
                item {
                    TittleContent(
                        title = newsContent?.title ?: "无标题",
                        publishDate = newsContent?.publishDate ?: getCurrentDate(),
                        visitCount = newsContent?.visitCount ?: "10",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                    WebView(
                        modifier = Modifier
                            .fillMaxWidth(),
                        url = url,
                        webViewState = rememberWebViewStateWithHTMLData(
                            data = NewsHTML.HTML.format(
                                NewsStyle.get(
                                    fontSize = fontSize,
                                    fontFamily = fontList[fontFamilyIndex],
                                    lineHeight = 1.0F,
                                    letterSpacing = 0.5F,
                                    textMargin = HORIZONTAL_MARGIN,
                                    textColor = MiuixTheme.colorScheme.onBackground.copy(0.8f)
                                        .toArgb(),
                                    textBold = false,
                                    textAlign = "start",
                                    boldTextColor = MiuixTheme.colorScheme.onBackground.copy(
                                        0.8f
                                    ).toArgb(),
                                    subheadBold = false,
                                    subheadUpperCase = false,
                                    imgMargin = HORIZONTAL_MARGIN,
                                    imgBorderRadius = 4,
                                    imgDisplayMode = if (loadImgEnabled) "block" else "none",
                                    linkTextColor = MiuixTheme.colorScheme.onBackground.copy(
                                        0.8f
                                    ).toArgb(),
                                    codeTextColor = MiuixTheme.colorScheme.onBackground.copy(
                                        0.8f
                                    ).toArgb(),
                                    codeBgColor = MiuixTheme.colorScheme.onBackground.copy(0.8f)
                                        .toArgb(),
                                    tableMargin = 0,
                                    selectionTextColor = MiuixTheme.colorScheme.onBackground.toArgb(),
                                    selectionBgColor = MiuixTheme.colorScheme.tertiaryContainer.toArgb()
                                ),
                                url,
                                newsContent?.articleContent,
                                WebViewScript.get()
                            ),
                            baseUrl = url
                        ),
                        onError = { onError(it) },
                        onFinished = {
                        },
                        onImageClick = { onImgClick(it) },
                        onDownloadClick = { fileUrl, fileName ->
                            onDownloadClick(
                                fileUrl,
                                fileName
                            )
                        },
                        isShowLinearProgressIndicator = false,
                        navigator = webViewNavigator,
                        snackBarHostState = snackBarHostState
                    )
                    newsContent?.attachment
                        .let { attachments ->
                            if (attachments?.isNotEmpty() == true) {
                                AttachmentContent(
                                    attachments = attachments,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 12.dp)
                                        .padding(horizontal = 16.dp),
                                    onPDFClick = { url, title ->
                                        navigator.push(
                                            Route.PdfReaderView(
                                                url = url,
                                                title = title
                                            )
                                        )
                                    },
                                    onUrlClick = {
                                        navigator.pushWebView(it, "附件")
                                    }
                                )
                            }
                        }
                }
            }
        }
        VerticalScrollBar(
            adapter = rememberScrollBarAdapter(lazyListState),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight(),
            trackPadding = padding,
        )
    }
}

@Composable
private fun RawArticleView(
    modifier: Modifier = Modifier,
    url: String,
    onError: (String) -> Unit,
    onDownloadClick: (fileUrl: String, fileName: String) -> Unit,
    webViewNavigator: WebViewNavigator,
    snackBarHostState: SnackbarHostState
) {
    WebView(
        modifier = modifier,
        url = url,
        webViewState = rememberWebViewState(url),
        navigator = webViewNavigator,
        snackBarHostState = snackBarHostState,
        onError = { onError(it) },
        onDownloadClick = { fileUrl, fileName -> onDownloadClick(fileUrl, fileName) }
    )
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
            style = MiuixTheme.textStyles.title2,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "发布时间：${publishDate}",
                fontSize = MiuixTheme.textStyles.body2.fontSize,
                color = MiuixTheme.colorScheme.onBackground.copy(0.6f)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "浏览次数：${visitCount}",
                fontSize = MiuixTheme.textStyles.body2.fontSize,
                color = MiuixTheme.colorScheme.onBackground.copy(0.6f)
            )
        }
    }
}

private val attachmentIconMap: Map<String, Int> = mapOf(
    // 文档
    "pdf" to R.drawable.ic_pdf_file,
    "doc" to R.drawable.ic_doc_file,
    "docx" to R.drawable.ic_docx_file,
    "txt" to R.drawable.ic_txt_file,
    "md" to R.drawable.ic_md_file,
    "csv" to R.drawable.ic_csv_file,
    "html" to R.drawable.ic_html_file,
    "xml" to R.drawable.ic_xml_file,
    "log" to R.drawable.ic_log_file,
    "caj" to R.drawable.ic_caj_file,
    "ofd" to R.drawable.ic_ofd_file,
    "epub" to R.drawable.ic_epub_file,
    "azw3" to R.drawable.ic_azw3_file,
    "mobi" to R.drawable.ic_mobi_file,
    // 表格
    "xls" to R.drawable.ic_xls_file,
    "xlsx" to R.drawable.ic_xlsx_file,
    "ods" to R.drawable.ic_ods_file,
    "numbers" to R.drawable.ic_numbers_file,
    // 演示
    "ppt" to R.drawable.ic_ppt_file,
    "pptx" to R.drawable.ic_pptx_file,
    "odp" to R.drawable.ic_odp_file,
    "wps" to R.drawable.ic_wps_file,
    "pages" to R.drawable.ic_pages_file,
    // 图片
    "jpg" to R.drawable.ic_jpg_file,
    "jpeg" to R.drawable.ic_jpeg_file,
    "png" to R.drawable.ic_png_file,
    "gif" to R.drawable.ic_gif_file,
    "bmp" to R.drawable.ic_bmp_file,
    "svg" to R.drawable.ic_svg_file,
    "webp" to R.drawable.ic_webp_file,
    "tif" to R.drawable.ic_tif_file,
    "tiff" to R.drawable.ic_tiff_file,
    // 音频
    "mp3" to R.drawable.ic_mp3_file,
    "wav" to R.drawable.ic_wav_file,
    "aac" to R.drawable.ic_aac_file,
    "flac" to R.drawable.ic_flac_file,
    "ogg" to R.drawable.ic_ogg_file,
    "m4a" to R.drawable.ic_m4a_file,
    "amr" to R.drawable.ic_amr_file,
    "wma" to R.drawable.ic_wma_file,
    // 视频
    "mp4" to R.drawable.ic_mp4_file,
    "avi" to R.drawable.ic_avi_file,
    "mkv" to R.drawable.ic_mkv_file,
    "mov" to R.drawable.ic_mov_file,
    "flv" to R.drawable.ic_flv_file,
    "m4v" to R.drawable.ic_m4v_file,
    "wmv" to R.drawable.ic_wmv_file,
    "mpeg" to R.drawable.ic_mpeg_file,
    "mpg" to R.drawable.ic_mpg_file,
    "3gp" to R.drawable.ic_3gp_file,
    // 压缩
    "zip" to R.drawable.ic_zip_file,
    "rar" to R.drawable.ic_rar_file,
    "7z" to R.drawable.ic_7z_file,
    "gz" to R.drawable.ic_gz_file,
    "tar" to R.drawable.ic_tar_file,
    "tgz" to R.drawable.ic_tgz_file,
    // 代码/脚本/数据
    "js" to R.drawable.ic_js_file,
    "py" to R.drawable.ic_py_file,
    "sh" to R.drawable.ic_sh_file,
    "bat" to R.drawable.ic_bat_file,
    "jar" to R.drawable.ic_jar_file,
    "dat" to R.drawable.ic_dat_file,
    "db" to R.drawable.ic_db_file,
    "bin" to R.drawable.ic_bin_file,
    // 应用/其他
    "apk" to R.drawable.ic_apk_file,
    "exe" to R.drawable.ic_exe_file,
    "ics" to R.drawable.ic_ics_file,
    "vcf" to R.drawable.ic_vcf_file,
    "key" to R.drawable.ic_key_file,
    "odt" to R.drawable.ic_odt_file
)

@Composable
fun AttachmentContent(
    attachments: List<AttachmentEntity>,
    modifier: Modifier,
    onPDFClick: (String, String) -> Unit = { _, _ -> },
    onUrlClick: (String) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        attachments.forEach { attachment ->
            Log.i("TAG666", attachment.toString())
            if (attachment.url.isNotEmpty()) {
                Card {
                    val showDownloadDialog = remember { mutableStateOf(false) }
                    val iconRes = remember(attachment.fileType) {
                        attachmentIconMap[attachment.fileType.lowercase()]
                            ?: R.drawable.ic_html_file
                    }
                    BasicComponent(
                        startAction = {
                            Image(
                                painter = painterResource(id = iconRes),
                                contentDescription = "file",
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(36.dp)
                            )
                        },
                        title = attachment.fileName,
                        onClick = {
                            when {
                                !attachment.isFile -> {
                                    onUrlClick(attachment.url)
                                }

                                attachment.fileType == "pdf" -> {
                                    onPDFClick(attachment.url, attachment.fileName)
                                }

                                else -> {
                                    showDownloadDialog.value = true
                                }
                            }
                        }
                    )
                    DownloadDialog(
                        showDialog = showDownloadDialog.value,
                        fileName = attachment.fileName,
                        url = attachment.url,
                        onDismissRequest = {
                            showDownloadDialog.value = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AISummaryBottomSheet(
    showDialog: Boolean,
    aiSummaryContent: String? = null,
    aiSummaryReasoningContent: String? = null,
    onDismissRequest: () -> Unit
) {
    OverlayBottomSheet(
        title = "YunAI 智能摘要",
        show = showDialog,
        onDismissRequest = {
            onDismissRequest()
        },
        startAction = {
            IconButton(
                onClick = {
                    copyContent(aiSummaryContent.toString())
                },
                modifier = Modifier
            ) {
                Icon(
                    imageVector = MiuixIcons.Copy,
                    contentDescription = null
                )
            }
        },
        // backgroundColor = Color(0xFF4b6ed4),
        // insideMargin = DpSize(0.dp, 0.dp)
    ) {
        val aiSummaryContentScrollState = rememberScrollState()
        val aiSummaryReasoningContentScrollState = rememberScrollState()

        LaunchedEffect(aiSummaryContent) {
            if (!aiSummaryContent.isNullOrEmpty()) {
                aiSummaryContentScrollState.animateScrollTo(aiSummaryContentScrollState.maxValue)
            }
        }

        LaunchedEffect(aiSummaryReasoningContent) {
            if (!aiSummaryReasoningContent.isNullOrEmpty()) {
                aiSummaryReasoningContentScrollState.animateScrollTo(
                    aiSummaryReasoningContentScrollState.maxValue
                )
            }
        }

        if (aiSummaryContent.isNullOrEmpty() && aiSummaryReasoningContent.isNullOrEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(aiSummaryContentScrollState)
            ) {
                aiSummaryReasoningContent?.let {
                    val expandState = remember { mutableStateOf(true) }
                    val targetMax = if (expandState.value) 150.dp else 500.dp
                    val animatedMax by animateDpAsState(
                        targetValue = targetMax,
                        animationSpec = tween(durationMillis = 300)
                    )
                    val iconRotate by animateFloatAsState(if (expandState.value) 0f else -180f)
                    Card(
                        colors = CardDefaults.defaultColors(color = MiuixTheme.colorScheme.secondaryVariant),
                        modifier = Modifier
                            .heightIn(max = animatedMax)
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        expandState.value = !expandState.value
                                    }
                                    .semantics {
                                        role = Role.Button
                                    },
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.deepthink),
                                    contentDescription = null,
                                    tint = MiuixTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(18.dp),
                                )
                                Text(
                                    text = "深度思考",
                                    color = MiuixTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = MiuixTheme.colorScheme.onSurface,
                                    modifier = Modifier.rotate(iconRotate),
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .graphicsLayer { alpha = 0.99f } // 触发离屏渲染，保证蒙版生效
                                    .drawWithCache {
                                        // 创建顶部和底部的渐变蒙版
                                        val brush = Brush.verticalGradient(
                                            startY = 0f,
                                            endY = size.height,
                                            colorStops = arrayOf(
                                                0.0f to Color.Transparent,
                                                (64f / size.height) to Color.Black,
                                                (1 - 64f / size.height) to Color.Black,
                                                1.0f to Color.Transparent
                                            )
                                        )
                                        onDrawWithContent {
                                            drawContent()
                                            drawRect(
                                                brush = brush,
                                                size = Size(size.width, size.height),
                                                blendMode = BlendMode.DstIn // 用蒙版做透明渐变
                                            )
                                        }
                                    }
                                    .heightIn(max = animatedMax)
                                    .verticalScroll(aiSummaryReasoningContentScrollState)
                            ) {
                                Text(
                                    text = it,
                                    color = MiuixTheme.colorScheme.onSurface,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
                aiSummaryContent?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(it)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun ErrorMessageDialog(
    showDialog: Boolean,
    errorMessage: String,
    onDismissRequest: () -> Unit,
    onReload: () -> Unit
) {
    OverlayDialog(
        title = "提示信息",
        summary = errorMessage,
        show = showDialog,
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    text = "返回",
                    onClick = {
                        onDismissRequest()
                    },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(20.dp))
                TextButton(
                    text = "刷新",
                    onClick = {
                        onReload()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.textButtonColorsPrimary()
                )
            }
        }
    }
}
