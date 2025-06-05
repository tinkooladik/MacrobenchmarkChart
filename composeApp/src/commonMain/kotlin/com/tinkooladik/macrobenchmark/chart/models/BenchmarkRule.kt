package com.tinkooladik.macrobenchmark.chart.models

enum class BenchmarkRule(
    val description: String,
    val lowerBetter: Boolean,
    val priority: Int
) {
    frameOverrunMs(
        "The lower, the better. Measures frame rendering delays; high values indicate lag and jank.",
        true,
        priority = 1
    ),
    frameDurationCpuMs(
        "The lower, the better. Measures frame rendering time; lower values mean smoother performance.",
        true,
        priority = 2
    ),
    timeToInitialDisplayMs(
        "The lower, the better.",
        true,
        priority = 3
    ),
    frameCount(
        "The higher, the better. More frames indicate smoother performance.",
        false,
        priority = 4
    ),
    memoryHeapSizeMaxKb(
        "The lower, the better. Indicates peak memory usage; high values suggest memory inefficiencies.",
        true,
        priority = 5
    ),
    memoryRssAnonMaxKb(
        "The lower, the better. High peak values may lead to performance degradation due to memory pressure.",
        true,
        priority = 6
    ),
    memoryHeapSizeLastKb(
        "The lower, the better. Less heap memory usage reduces memory pressure.",
        true,
        priority = 7
    ),
    memoryRssAnonLastKb(
        "The lower, the better. Represents the actual RAM used; high values indicate more memory consumption.",
        true,
        priority = 8
    ),
    memoryRssFileMaxKb(
        "The lower, the better. High values mean more file-backed memory in RAM, which might indicate excessive caching.",
        true,
        priority = 9
    ),
    memoryRssFileLastKb(
        "The lower, the better. Measures memory-mapped file usage; reducing this can improve efficiency.",
        true,
        priority = 10
    ),
    memoryGpuMaxKb(
        "The lower, the better. Indicates peak GPU memory usage; high values may suggest graphics resource leaks or overdraw.",
        true,
        priority = 11
    ),
    memoryGpuLastKb(
        "The lower, the better. Represents current GPU memory usage; helps track ongoing graphics memory load.",
        true,
        priority = 12
    );
}