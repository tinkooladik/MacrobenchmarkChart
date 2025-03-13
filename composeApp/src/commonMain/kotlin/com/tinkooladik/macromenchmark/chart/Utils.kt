package com.tinkooladik.macromenchmark.chart

import com.tinkooladik.macromenchmark.chart.models.BenchmarkReport
import com.tinkooladik.macromenchmark.chart.models.BenchmarkReportUi
import com.tinkooladik.macromenchmark.chart.models.toUi
import kotlinx.serialization.json.Json

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