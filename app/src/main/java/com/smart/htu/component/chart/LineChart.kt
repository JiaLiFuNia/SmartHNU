package com.smart.htu.component.chart

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.cartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.point
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.component.shapeComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.insets
import com.patrykandpatrick.vico.compose.common.rememberVerticalLegend
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.core.common.LegendItem
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import top.yukonga.miuix.kmp.theme.MiuixTheme
import java.text.DecimalFormat


private val RangeProvider = CartesianLayerRangeProvider.fixed(maxY = 5.0)
private val YDecimalFormat = DecimalFormat("#.##")
private val StartAxisValueFormatter = CartesianValueFormatter.decimal(YDecimalFormat)
private val MarkerValueFormatter = DefaultCartesianMarker.ValueFormatter.default(YDecimalFormat)

private val LegendLabelKey = ExtraStore.Key<Set<String>>()
private val BottomAxisLabelKey = ExtraStore.Key<List<String>>()
private val BottomAxisValueFormatter = CartesianValueFormatter { context, x, _ ->
    context.model.extraStore[BottomAxisLabelKey][x.toInt()]
}

@Composable
fun LineChart(
    data: MutableState<Map<String, Pair<List<String>, List<Double>>>>
) {
    val modelProducer = remember { CartesianChartModelProducer() }
    val legendItemLabelComponent = rememberTextComponent(color = MiuixTheme.colorScheme.onSurface)
    val lineColor = listOf(Color(0xff916cda), Color(0xffd877d8))
    LaunchedEffect(data) {
        modelProducer.runTransaction {
            lineSeries { data.value.forEach { (_, map) -> series(map.second) } }
            extras { data.value.forEach { (_, map) -> it[BottomAxisLabelKey] = map.first } }
            extras { extraStore -> extraStore[LegendLabelKey] = data.value.keys }
        }
    }

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                lineProvider =
                    LineCartesianLayer.LineProvider.series(
                        lineColor.map {
                            LineCartesianLayer.rememberLine(
                                fill = LineCartesianLayer.LineFill.single(fill(it)),
                                pointProvider = LineCartesianLayer.PointProvider.single(
                                    LineCartesianLayer.point(
                                        rememberShapeComponent(
                                            fill(it),
                                            CorneredShape.Pill
                                        )
                                    )
                                )
                            )
                        }
                    ),
                rangeProvider = RangeProvider,
            ),
            startAxis = VerticalAxis.rememberStart(valueFormatter = StartAxisValueFormatter), // 纵轴
            bottomAxis = HorizontalAxis.rememberBottom(
                valueFormatter = BottomAxisValueFormatter,
            ), // 横轴
            layerPadding = { cartesianLayerPadding(scalableStart = 8.dp, scalableEnd = 8.dp) },
            marker = rememberMarker(MarkerValueFormatter),
            legend =
                rememberVerticalLegend(
                    items = { extraStore ->
                        extraStore[LegendLabelKey].forEachIndexed { index, label ->
                            add(
                                LegendItem(
                                    icon = shapeComponent(
                                        fill(lineColor[index]),
                                        CorneredShape.Pill
                                    ),
                                    labelComponent = legendItemLabelComponent,
                                    label = label
                                )
                            )
                        }
                    },
                    padding = insets(start = 12.dp, top = 12.dp),
                )
        ),
        modifier = Modifier.height(240.dp),
        modelProducer = modelProducer,
        scrollState = rememberVicoScrollState(scrollEnabled = false),
    )
}
