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
        toChartSampled(
            before = beforeItem.sampledMetrics,
            after = after.benchmarks.first { it.name == beforeItem.name }.sampledMetrics
        )
    }.first()
    MetricBarChart(chartItems + sampledChartItems)
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
                    Text(item.title, fontSize = 18.sp, modifier = Modifier.padding(bottom = 4.dp))
                    Text(
                        benchmarkComparisonRules[item.title] ?: "",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 36.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 48.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        item.values.forEach { (label, before, after) ->
                            MetricValueBars(label, before, after)
                        }
                    }
                    Spacer(
                        modifier = Modifier.height(1.dp).fillMaxWidth()
                            .background(Color(0xFFD2D2D2))
                    )
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

@Composable
fun MetricValueBars(label: String, before: Double, after: Double) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Box(modifier = Modifier.height(120.dp).width(40.dp)) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                fun scale(value: Double, reference: Double): Float {
                    val ratio =
                        if (reference == 0.0) 1.0 else value / reference
                    return (ratio * size.height).toFloat()
                }

                val referenceValue = max(before, after) // Normalize based on the larger value
                val beforeHeight = scale(before, referenceValue)
                val afterHeight = scale(after, referenceValue)

                // Before Value
                drawRoundRect(
                    color = ITEM_COLOR_BEFORE,
                    topLeft = Offset(0f, size.height - beforeHeight),
                    size = Size(
                        size.width / 2,
                        beforeHeight
                    ) // Use exactly half the width
                )

                // After Value
                drawRoundRect(
                    color = ITEM_COLOR_AFTER,
                    topLeft = Offset(
                        size.width / 2,
                        size.height - afterHeight
                    ), // No space added
                    size = Size(
                        size.width / 2,
                        afterHeight
                    ) // Use exactly half the width
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(label, fontSize = 14.sp)
        Text("Before: ${String.format("%.2f", before)}", fontSize = 12.sp)
        Text("After: ${String.format("%.2f", after)}", fontSize = 12.sp)
        Spacer(modifier = Modifier.height(8.dp))
        val difference = (after - before).let {
            if (it > 0) "+${String.format("%.2f", it)}" else String.format("%.2f", it)
        }
        Spacer(
            modifier = Modifier.height(1.dp).width(200.dp)
                .background(Color(0xFFDEDCDC))
        )
        Text("Difference: $difference", fontSize = 12.sp)
    }
}

val benchmarkComparisonRules: Map<String, String> = mapOf(
    "frameCount" to "The higher, the better. More frames indicate smoother performance.",
    "memoryHeapSizeLastKb" to "The lower, the better. Less heap memory usage reduces memory pressure.",
    "memoryHeapSizeMaxKb" to "The lower, the better. Indicates peak memory usage; high values suggest memory inefficiencies.",
    "memoryRssAnonLastKb" to "The lower, the better. Represents the actual RAM used; high values indicate more memory consumption.",
    "memoryRssAnonMaxKb" to "The lower, the better. High peak values may lead to performance degradation due to memory pressure.",
    "memoryRssFileLastKb" to "The lower, the better. Measures memory-mapped file usage; reducing this can improve efficiency.",
    "memoryRssFileMaxKb" to "The lower, the better. High values mean more file-backed memory in RAM, which might indicate excessive caching.",
    "frameDurationCpuMs" to "The lower, the better. Measures frame rendering time; lower values mean smoother performance.",
    "frameOverrunMs" to "The lower, the better. Measures frame rendering delays; high values indicate lag and jank."
)
