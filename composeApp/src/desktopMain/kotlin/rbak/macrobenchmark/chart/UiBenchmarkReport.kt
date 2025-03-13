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

// frameCount
data class MetricChartItem(
    val title: String,
    val min: Pair<Double, Double>,
    val median: Pair<Double, Double>,
    val max: Pair<Double, Double>
)

data class SampledMetricsChartItem(
    val title: String,
    val P50: Pair<Double, Double>,
    val P90: Pair<Double, Double>,
    val P95: Pair<Double, Double>,
    val P99: Pair<Double, Double>
)

fun toChart(
    before: Map<String, MetricUi>,
    after: Map<String, MetricUi>
): List<MetricChartItem> {
    return before.map { (name, metrics) ->
        MetricChartItem(
            title = name,
            min = metrics.minimum to (after[name]?.minimum ?: 0.0),
            median = metrics.median to (after[name]?.median ?: 0.0),
            max = metrics.maximum to (after[name]?.maximum ?: 0.0),
        )
    }
}

fun toSampledChart(
    before: Map<String, SampledMetricUi>,
    after: Map<String, SampledMetricUi>
): List<SampledMetricsChartItem> {
    return before.map { (name, metrics) ->
        SampledMetricsChartItem(
            title = name,
            P50 = metrics.P50 to (after[name]?.P50 ?: 0.0),
            P90 = metrics.P90 to (after[name]?.P90 ?: 0.0),
            P95 = metrics.P95 to (after[name]?.P95 ?: 0.0),
            P99 = metrics.P99 to (after[name]?.P99 ?: 0.0)
        )
    }
}
