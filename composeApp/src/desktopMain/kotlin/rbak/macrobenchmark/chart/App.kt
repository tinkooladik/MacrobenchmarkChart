package rbak.macrobenchmark.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.serialization.json.Json
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.log10
import kotlin.math.max

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
    MetricBarChart(chartItems)
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

@Composable
fun MetricBarChart(items: List<MetricChartItem>) {
    val scrollState = rememberScrollState()

    Row(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f).verticalScroll(scrollState)) {
            Column(modifier = Modifier.padding(16.dp)) {
                items.forEach { item ->
                    Text(item.title, fontSize = 18.sp, modifier = Modifier.padding(bottom = 24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 48.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        listOf(
                            "Min" to item.min,
                            "Median" to item.median,
                            "Max" to item.max
                        ).forEach { (label, values) ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                Box(modifier = Modifier.height(120.dp).width(40.dp)) {
                                    Canvas(
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        fun scale(value: Double, reference: Double): Float {
                                            val ratio = if (reference == 0.0) 1.0 else value / reference
                                            return (ratio * size.height).toFloat()
                                        }

                                        val referenceValue = max(values.first, values.second) // Normalize based on the larger value
                                        val beforeHeight = scale(values.first, referenceValue)
                                        val afterHeight = scale(values.second, referenceValue)

                                        // Before Value
                                        drawRoundRect(
                                            color = ITEM_COLOR_BEFORE,
                                            topLeft = Offset(0f, size.height - beforeHeight),
                                            size = Size(size.width / 2, beforeHeight) // Use exactly half the width
                                        )

                                        // After Value
                                        drawRoundRect(
                                            color = ITEM_COLOR_AFTER,
                                            topLeft = Offset(size.width / 2, size.height - afterHeight), // No space added
                                            size = Size(size.width / 2, afterHeight) // Use exactly half the width
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(label, fontSize = 14.sp)
                                Text("Before: ${values.first.toInt()}", fontSize = 12.sp)
                                Text("After: ${values.second.toInt()}", fontSize = 12.sp)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(1.dp).fillMaxWidth().background(Color(0xFFD2D2D2)))
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        // Add a vertical scrollbar
        VerticalScrollbar(
            adapter = rememberScrollbarAdapter(scrollState),
            modifier = Modifier.fillMaxHeight().padding(end = 8.dp)
        )
    }
}
