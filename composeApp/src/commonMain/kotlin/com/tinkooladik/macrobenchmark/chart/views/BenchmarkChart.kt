package com.tinkooladik.macrobenchmark.chart.views

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.tinkooladik.macrobenchmark.chart.json
import com.tinkooladik.macrobenchmark.chart.models.BenchmarkReport
import com.tinkooladik.macrobenchmark.chart.models.toChart
import com.tinkooladik.macrobenchmark.chart.models.toChartSampled
import com.tinkooladik.macrobenchmark.chart.models.toUi
import java.io.File

@Composable
fun BenchmarkChart(beforeFile: File?, afterFile: File?) {
    if (beforeFile == null || afterFile == null) return
    val before = try {
        json.decodeFromString<BenchmarkReport>(beforeFile.readText()).toUi()
    } catch (e: Exception) {
        Text("Error parsing `Before` file: e")
        return
    }
    val after = try {
        json.decodeFromString<BenchmarkReport>(afterFile.readText()).toUi()
    } catch (e: Exception) {
        Text("Error parsing `After` file: e")
        return
    }

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