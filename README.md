## Macrobenchmark Chart

Desktop tool to visualize [Macrobenchmark](https://developer.android.com/topic/performance/benchmarking/macrobenchmark-overview) reports.

https://github.com/user-attachments/assets/9aa975b7-41c2-4fa8-a1ee-53f4b5fca2ac

### How to run

To run it, use `./gradlew run desktopRun -DmainClass=MainKt --quiet`. Studio Run button won't work due to 

### How to analyze

<img width="600" alt="image" src="https://github.com/user-attachments/assets/805548b2-c9a5-4d02-a042-cb1de5e164a2" />

------

This is a Kotlin Multiplatform project targeting Desktop.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
