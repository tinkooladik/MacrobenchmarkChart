package rbak.macrobenchmark.chart

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.Json
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val scrollState = rememberScrollState()
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            BenchmarkChart()

            VerticalScrollbar(
                adapter = rememberScrollbarAdapter(scrollState),
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}

val ITEM_WIDTH = 50.dp
val ITEM_COLOR_BEFORE = Color(0xFF5C8CBE)
val ITEM_COLOR_AFTER = Color(0xFF3D7BB9)

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
        toSampledChart(
            before = beforeItem.sampledMetrics,
            after = after.benchmarks.first { it.name == beforeItem.name }.sampledMetrics
        )
    }.first()

    LazyColumn {
        itemsIndexed(chartItems) { index, item ->
            Row {
                Box(
                    modifier = Modifier.width(ITEM_WIDTH).height(item.min.first.dp)
                        .background(ITEM_COLOR_BEFORE)
                )
                Box(
                    modifier = Modifier.width(ITEM_WIDTH).height(item.min.second.dp)
                        .background(ITEM_COLOR_AFTER)
                )
            }
        }
    }
}

val json = Json {
    ignoreUnknownKeys = true
}


fun readBenchmark(fileName: String): BenchmarkReportUi {
    val classLoader = Thread.currentThread().contextClassLoader
    val resource = classLoader.getResource(fileName)
    requireNotNull(resource) { "Resource not found: $fileName" }
    val file = resource.readText()
    val benchmark = json.decodeFromString<BenchmarkReport>(file)
    return benchmark.toUi()
}