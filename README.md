# SilkNotes

Android app (`com.silknotes`, slug `silknotes`), built by Alfredo/OpenClaw.

## Download / install

- **Releases:** grab the latest debug APK from the [Releases page](../../releases) (`app-debug.apk`). Every green push to `main` publishes a build there.
- **CI artifacts:** Actions run -> artifact `apk` (kept 14 days).
- Install: unzip if needed, enable "install unknown apps", open the APK (debug-signed).

## Stack

- Kotlin + Jetpack Compose (Material3), `minSdk 26`, `targetSdk 35` / `compileSdk 35`
- AGP 8.7.3, Gradle 8.11.1 (wrapper-free in CI)

## Build

Built on GitHub Actions (x86-64); this project is not built locally on the agent server.

```bash
# CI equivalent (needs Android SDK locally)
gradle assembleDebug --no-daemon
```

## Development

Edit `app/src/main/java/.../MainActivity.kt` and push to `main`. CI builds the APK and publishes a Release.