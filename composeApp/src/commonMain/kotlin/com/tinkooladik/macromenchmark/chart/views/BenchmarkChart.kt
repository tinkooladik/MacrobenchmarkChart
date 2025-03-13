package com.tinkooladik.macromenchmark.chart.views

import androidx.compose.runtime.Composable
import com.tinkooladik.macromenchmark.chart.models.toChart
import com.tinkooladik.macromenchmark.chart.models.toChartSampled
import com.tinkooladik.macromenchmark.chart.readBenchmark

@Composable
fun BenchmarkChart() {
    val before = readBenchmark("before.json")
    val after = readBenchmark("after.json")

    val chartItems = before.benchmarks.map { beforeItem ->
        toChart(
            before = beforeItem.metrics,
            after = after.benchmarks.first { it.name == beforeItem.name }.metrics
        )
    }.first()

    val sampledChartItems = before.benchmarks.map { beforeItem ->
        toChartSampled(
            before = beforeItem.sampledMetrics,
            after = after.benchmarks.first { it.name == beforeItem.name }.sampledMetrics
        )
    }.first()
    MetricBarChart(chartItems + sampledChartItems)
}