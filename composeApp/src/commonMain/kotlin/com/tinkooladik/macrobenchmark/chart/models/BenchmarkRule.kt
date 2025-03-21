package com.tinkooladik.macrobenchmark.chart.models

enum class BenchmarkRule(val description: String, val lowerBetter: Boolean) {
    frameCount("The higher, the better. More frames indicate smoother performance.", false),
    memoryHeapSizeLastKb(
        "The lower, the better. Less heap memory usage reduces memory pressure.",
        true
    ),
    memoryHeapSizeMaxKb(
        "The lower, the better. Indicates peak memory usage; high values suggest memory inefficiencies.",
        true
    ),
    memoryRssAnonLastKb(
        "The lower, the better. Represents the actual RAM used; high values indicate more memory consumption.",
        true
    ),
    memoryRssAnonMaxKb(
        "The lower, the better. High peak values may lead to performance degradation due to memory pressure.",
        true
    ),
    memoryRssFileLastKb(
        "The lower, the better. Measures memory-mapped file usage; reducing this can improve efficiency.",
        true
    ),
    memoryRssFileMaxKb(
        "The lower, the better. High values mean more file-backed memory in RAM, which might indicate excessive caching.",
        true
    ),
    frameDurationCpuMs(
        "The lower, the better. Measures frame rendering time; lower values mean smoother performance.",
        true
    ),
    frameOverrunMs(
        "The lower, the better. Measures frame rendering delays; high values indicate lag and jank.",
        true
    ),
    timeToInitialDisplayMs(
        "The lower, the better.",
        true
    );
}