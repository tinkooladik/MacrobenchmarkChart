package rbak.macrobenchmark.chart

data class BenchmarkReportUi(
    val device: String,
    val iterations: Int,
    val benchmarks: List<BenchmarkUi>
)

data class BenchmarkUi(
    val name: String,
    val metrics: Map<String, MetricUi>,
    val sampledMetrics: Map<String, SampledMetricUi>
)

data class MetricUi(
    val minimum: Double,
    val maximum: Double,
    val median: Double
)

data class SampledMetricUi(
    val P50: Double,
    val P90: Double,
    val P95: Double,
    val P99: Double
)

fun BenchmarkReport.toUi() = BenchmarkReportUi(
    device = context.run {
        "${build.brand} $${build.model} (${build.version} $osCodenameAbbreviated)"
    },
    iterations = benchmarks.sumOf { it.repeatIterations },
    benchmarks = benchmarks.map { it.toUi() }
)

fun Benchmark.toUi() = BenchmarkUi(
    name = name,
    metrics = metrics.mapValues { it.value.toUi() },
    sampledMetrics = sampledMetrics.mapValues { it.value.toUi() }
)

fun Metric.toUi() = MetricUi(
    minimum = minimum, maximum = maximum, median = median
)

fun SampledMetric.toUi() = SampledMetricUi(
    P50 = P50, P90 = P90, P95 = P95, P99 = P99
)

data class MetricValue(
    val label: String,
    val before: Double,
    val after: Double
)

// frameCount
data class MetricChartItem(
    val title: String,
    val values: List<MetricValue>
)

fun toChart(
    before: Map<String, MetricUi>,
    after: Map<String, MetricUi>
): List<MetricChartItem> {
    return before.map { (name, metrics) ->
        MetricChartItem(
            title = name,
            values = listOf(
                MetricValue("Min", metrics.minimum, (after[name]?.minimum ?: 0.0)),
                MetricValue("Median", metrics.median, (after[name]?.median ?: 0.0)),
                MetricValue("Max", metrics.maximum, (after[name]?.maximum ?: 0.0))
            )
        )
    }
}

fun toChartSampled(
    before: Map<String, SampledMetricUi>,
    after: Map<String, SampledMetricUi>
): List<MetricChartItem> {
    return before.map { (name, metrics) ->
        MetricChartItem(
            title = name,
            values = listOf(
                MetricValue("P50", metrics.P50, (after[name]?.P50 ?: 0.0)),
                MetricValue("P90", metrics.P90, (after[name]?.P90 ?: 0.0)),
                MetricValue("P95", metrics.P95, (after[name]?.P95 ?: 0.0)),
                MetricValue("P99", metrics.P99, (after[name]?.P99 ?: 0.0))
            )
        )
    }
}
