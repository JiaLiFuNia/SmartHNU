package com.smart.htu.component.chart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.component.TextComponent
import com.patrykandpatrick.vico.compose.pie.PieChart
import com.patrykandpatrick.vico.compose.pie.PieChartHost
import com.patrykandpatrick.vico.compose.pie.data.PieChartModelProducer
import com.patrykandpatrick.vico.compose.pie.data.PieValueFormatter
import com.patrykandpatrick.vico.compose.pie.data.pieSeries
import com.patrykandpatrick.vico.compose.pie.rememberPieChart
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun BasicPieChart(
    modifier: Modifier = Modifier,
    data: List<Double>,
    labels: List<String>? = null,
    colors: List<Color> = listOf(
        Color(0xFFFED032),
        Color(0xFFFF9219),
        Color(0xFFFD4135),
        Color(0xFFCF30EF),
        Color(0xFF2683ED),
        Color(0xFF2EDE8B),
        Color(0xFF8186A3),
        Color(0xFFB3BAD4)
    )
) {
    val modelProducer = remember { PieChartModelProducer() }

    LaunchedEffect(data) {
        modelProducer.runTransaction { pieSeries { series(data) } }
    }

    PieChartHost(
        chart = rememberPieChart(
            sliceProvider = PieChart.SliceProvider.series(
                colors.map {
                    PieChart.Slice(
                        fill = Fill(it),
                        label = PieChart.SliceLabel.Outside(
                            lineColor = MiuixTheme.colorScheme.onSurfaceContainer,
                            textComponent = TextComponent(
                                textStyle = TextStyle(MiuixTheme.colorScheme.onSurfaceContainer),
                                lineCount = if (labels != null) 2 else 1
                            )
                        ),
                    )
                }
            ),
            valueFormatter = PieValueFormatter { _, value, index ->
                labels.let {
                    if (it != null) {
                        "${"%.2f".format((value / data.sum()) * 100)}%\n${it[index]}"
                    } else {
                        "${"%.2f".format((value / data.sum()) * 100)}%"
                    }
                }
            },
        ),
        modelProducer = modelProducer,
        modifier = modifier,
    )
}