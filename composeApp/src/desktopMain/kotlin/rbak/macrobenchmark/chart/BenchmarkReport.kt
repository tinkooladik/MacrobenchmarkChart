package rbak.macrobenchmark.chart

import kotlinx.serialization.Serializable

@Serializable
data class BenchmarkReport(
    val context: Context,
    val benchmarks: List<Benchmark>
)

@Serializable
data class Context(
    val build: BuildInfo,
    val cpuCoreCount: Int,
    val cpuLocked: Boolean,
    val cpuMaxFreqHz: Long,
    val memTotalBytes: Long,
    val sustainedPerformanceModeEnabled: Boolean,
    val artMainlineVersion: Int,
    val osCodenameAbbreviated: String,
    val compilationMode: String
)

@Serializable
data class BuildInfo(
    val brand: String,
    val device: String,
    val id: String,
    val model: String,
    val type: String,
    val version: Version
)

@Serializable
data class Version(
    val codename: String,
    val sdk: Int
)

@Serializable
data class Benchmark(
    val name: String,
    val params: Map<String, String>,
    val className: String,
    val totalRunTimeNs: Long,
    val metrics: Map<String, Metric>,
    val sampledMetrics: Map<String, SampledMetric>,
    val warmupIterations: Int,
    val repeatIterations: Int,
    val thermalThrottleSleepSeconds: Int,
    val profilerOutputs: List<ProfilerOutput>
)

@Serializable
data class Metric(
    val minimum: Double,
    val maximum: Double,
    val median: Double,
    val runs: List<Double>
)

@Serializable
data class SampledMetric(
    val P50: Double,
    val P90: Double,
    val P95: Double,
    val P99: Double,
    val runs: List<List<Double>>
)

@Serializable
data class ProfilerOutput(
    val type: String,
    val label: String,
    val filename: String
)