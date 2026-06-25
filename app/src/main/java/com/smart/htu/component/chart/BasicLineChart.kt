package com.smart.htu.component.chart

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.data.lineModel
import com.patrykandpatrick.vico.compose.cartesian.layer.CartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.Insets
import com.patrykandpatrick.vico.compose.common.LegendItem
import com.patrykandpatrick.vico.compose.common.component.ShapeComponent
import com.patrykandpatrick.vico.compose.common.component.TextComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.data.ExtraStore
import com.patrykandpatrick.vico.compose.common.rememberVerticalLegend
import top.yukonga.miuix.kmp.theme.MiuixTheme


private val RangeProvider = CartesianLayerRangeProvider.fixed(maxY = 5.0)
private val StartAxisValueFormatter = CartesianValueFormatter.decimal()
private val MarkerValueFormatter = DefaultCartesianMarker.ValueFormatter.default()

private val LegendLabelKey = ExtraStore.Key<Set<String>>()
private val BottomAxisLabelKey = ExtraStore.Key<List<String>>()
private val BottomAxisValueFormatter = CartesianValueFormatter { context, x, _ ->
    context.model.extraStore[BottomAxisLabelKey][x.toInt()]
}

@Composable
fun BasicLineChart(
    data: MutableState<Map<String, Pair<List<String>, List<Double>>>>,
    rangeProvider: CartesianLayerRangeProvider = RangeProvider,
    startAxisValueFormatter: CartesianValueFormatter = StartAxisValueFormatter,
    markerValueFormatter: DefaultCartesianMarker.ValueFormatter = MarkerValueFormatter,
    legendLabelKey: ExtraStore.Key<Set<String>> = LegendLabelKey,
    bottomAxisLabelKey: ExtraStore.Key<List<String>> = BottomAxisLabelKey,
    bottomAxisValueFormatter: CartesianValueFormatter = BottomAxisValueFormatter
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    val legendItemLabelComponent =
        rememberTextComponent(style = TextStyle(color = MiuixTheme.colorScheme.onSurface))
    val lineColor = listOf(Color(0xff916cda), Color(0xffd877d8))
    LaunchedEffect(data) {
        modelProducer.runTransaction {
            lineModel { data.value.forEach { (_, map) -> series(map.second) } }
            extras { data.value.forEach { (_, map) -> it[bottomAxisLabelKey] = map.first } }
            extras { extraStore -> extraStore[legendLabelKey] = data.value.keys }
        }
    }

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                lineProvider =
                    LineCartesianLayer.LineProvider.series(
                        lineColor.map {
                            LineCartesianLayer.rememberLine(
                                fill = LineCartesianLayer.LineFill.single(Fill(it)),
                                pointProvider = LineCartesianLayer.PointProvider.single(
                                    LineCartesianLayer.Point(
                                        rememberShapeComponent(
                                            Fill(it),
                                            CircleShape
                                        )
                                    )
                                )
                            )
                        }
                    ),
                rangeProvider = rangeProvider,
            ),
            startAxis = VerticalAxis.rememberStart(
                valueFormatter = startAxisValueFormatter,
                label = TextComponent(
                    TextStyle(
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                        fontSize = MiuixTheme.textStyles.footnote1.fontSize
                    )
                )
            ), // 纵轴
            bottomAxis = HorizontalAxis.rememberBottom(
                valueFormatter = bottomAxisValueFormatter,
                label = TextComponent(
                    TextStyle(
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                        fontSize = MiuixTheme.textStyles.footnote1.fontSize
                    )
                )
            ), // 横轴
            layerPadding = { CartesianLayerPadding(scalableStart = 8.dp, scalableEnd = 8.dp) },
            marker = rememberMarker(markerValueFormatter),
            legend = rememberVerticalLegend(
                items = { extraStore ->
                    extraStore[legendLabelKey].forEachIndexed { index, label ->
                        add(
                            LegendItem(
                                icon = ShapeComponent(
                                    Fill(lineColor[index]),
                                    CircleShape
                                ),
                                labelComponent = legendItemLabelComponent,
                                label = label
                            )
                        )
                    }
                },
                padding = Insets(start = 12.dp, top = 12.dp),
            )
        ),
        modifier = Modifier.height(240.dp),
        modelProducer = modelProducer,
        scrollState = rememberVicoScrollState(scrollEnabled = false),
    )
}
