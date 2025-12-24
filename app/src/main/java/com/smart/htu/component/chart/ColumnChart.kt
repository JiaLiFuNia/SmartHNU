package com.smart.htu.component.chart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.cartesianLayerPadding
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.ColumnCartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.common.component.LineComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import top.yukonga.miuix.kmp.theme.MiuixTheme

private val BottomAxisLabelKey = ExtraStore.Key<List<String>>()

private val BottomAxisValueFormatter = CartesianValueFormatter { context, x, _ ->
    context.model.extraStore[BottomAxisLabelKey][x.toInt()]
}

private fun getColumnProvider(positive: LineComponent) =
    object : ColumnCartesianLayer.ColumnProvider {
        override fun getColumn(
            entry: ColumnCartesianLayerModel.Entry,
            seriesIndex: Int,
            extraStore: ExtraStore,
        ) = positive

        override fun getWidestSeriesColumn(seriesIndex: Int, extraStore: ExtraStore) = positive
    }

@Composable
fun ColumnChart(
    xData: MutableState<List<String>>,
    yData: MutableState<List<Double>>,
    verticalAxisItemPlacerStep: Double, // 纵轴数据间距
    columnCollectionSpacing: Dp, // 每一列的间隔
    columnWidth: Dp = 16.dp
) {
    val columnStyle = rememberLineComponent(
        fill = fill(MiuixTheme.colorScheme.primary),
        thickness = columnWidth,
        shape = CorneredShape.rounded(topLeftPercent = 30, topRightPercent = 30),
    )

    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(xData, yData) {
        modelProducer.runTransaction {
            columnSeries { series(yData.value) }
            extras { it[BottomAxisLabelKey] = xData.value }
        }
    }

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberColumnCartesianLayer(
                columnProvider = remember(columnStyle) {
                    getColumnProvider(columnStyle)
                },
                columnCollectionSpacing = columnCollectionSpacing
            ),
            startAxis = VerticalAxis.rememberStart(
                itemPlacer = VerticalAxis.ItemPlacer.step({ verticalAxisItemPlacerStep })
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
                valueFormatter = BottomAxisValueFormatter,
            ),
            layerPadding = { cartesianLayerPadding(scalableStart = 16.dp, scalableEnd = 16.dp) },
        ),
        modelProducer = modelProducer,
    )
}
