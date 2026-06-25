package com.smart.htu.component.chart

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.columnModel
import com.patrykandpatrick.vico.compose.cartesian.layer.CartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.CartesianMarkerVisibilityListener
import com.patrykandpatrick.vico.compose.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.component.TextComponent
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.data.ExtraStore
import top.yukonga.miuix.kmp.theme.MiuixTheme

private val BottomAxisLabelKey = ExtraStore.Key<List<String>>()

private val BottomAxisValueFormatter = CartesianValueFormatter { context, x, _ ->
    context.model.extraStore[BottomAxisLabelKey][x.toInt()]
}

@Composable
fun BasicColumnChart(
    xData: MutableState<List<String>>,
    yData: MutableState<List<Double>>,
    verticalAxisItemPlacerStep: Double, // 纵轴数据间距
    columnCollectionSpacing: Dp, // 每一列的间隔
    columnWidth: Dp = 16.dp,
    maxY: Double? = null,
    isShowMaker: Boolean = false,
    onMakerShow: (Boolean) -> Unit = {},
    markerValueFormatter: DefaultCartesianMarker.ValueFormatter = DefaultCartesianMarker.ValueFormatter.default()
) {
    val columnStyle = rememberLineComponent(
        fill = Fill(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF8383FF), Color(0xFF3482FF)),
                startY = 0.0f,
                endY = Float.POSITIVE_INFINITY
            )
        ),
        thickness = columnWidth,
        shape = RoundedCornerShape(topStartPercent = 30, topEndPercent = 30),
    )

    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(xData, yData) {
        modelProducer.runTransaction {
            columnModel { series(yData.value) }
            extras { it[BottomAxisLabelKey] = xData.value }
        }
    }

    val marker = rememberMarker(markerValueFormatter)
    val markerVisibilityListener = remember {
        object : CartesianMarkerVisibilityListener {
            override fun onShown(
                marker: CartesianMarker,
                targets: List<CartesianMarker.Target>
            ) {
                onMakerShow(true)
            }

            override fun onHidden(marker: CartesianMarker) {
                onMakerShow(false)
            }
        }
    }

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberColumnCartesianLayer(
                columnProvider = remember(columnStyle) {
                    ColumnCartesianLayer.ColumnProvider.series(columnStyle)
                },
                columnCollectionSpacing = columnCollectionSpacing,
                rangeProvider = remember(maxY) {
                    if (maxY != null) CartesianLayerRangeProvider.fixed(maxY = maxY)
                    else CartesianLayerRangeProvider.auto()
                }
            ),
            startAxis = VerticalAxis.rememberStart(
                itemPlacer = VerticalAxis.ItemPlacer.step({ verticalAxisItemPlacerStep }),
                label = TextComponent(
                    TextStyle(
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                        fontSize = MiuixTheme.textStyles.footnote1.fontSize
                    )
                )
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                label = TextComponent(
                    TextStyle(
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                        fontSize = MiuixTheme.textStyles.footnote1.fontSize
                    )
                ),
                itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
                valueFormatter = BottomAxisValueFormatter,
            ),
            layerPadding = { CartesianLayerPadding(scalableStart = 16.dp, scalableEnd = 16.dp) },
            marker = if (isShowMaker) marker else null,
            markerVisibilityListener = markerVisibilityListener
        ),
        modelProducer = modelProducer,
    )
}
