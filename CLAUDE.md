# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK (requires signing config)
./gradlew assembleRelease

# Run unit tests
./gradlew test

# Run a single test class
./gradlew testDebugUnitTest --tests "com.smart.htu.YourTestClass"

# Clean build
./gradlew clean assembleDebug
```

## Architecture

**师韵 SmartHNU** — Android campus life assistant for Henan Normal University (河师大).

### Tech Stack
- **Language**: Kotlin, Java 17 target
- **UI**: Jetpack Compose + Miuix (Xiaomi HyperOS style components from `top.yukonga.miuix.kmp`)
- **Navigation**: AndroidX Navigation3 (type-safe `NavKey` routes, NOT the older NavController)
- **DI**: Hilt (`@HiltAndroidApp`, `@HiltViewModel`, `@AndroidEntryPoint`)
- **Networking**: Retrofit + OkHttp + Gson, with custom `NetworkCookieJar` for session persistence
- **Data persistence**: DataStore Preferences (not Room/SQLite)
- **Image loading**: Coil 3
- **Serialization**: kotlinx.serialization (for navigation routes) + Gson (for API responses)

### Module Structure (single `:app` module)

```
com.smart.htu/
├── api/
│   ├── module/      # Data entities / DTOs (@Serializable or Gson)
│   └── network/     # Retrofit service interfaces
├── component/       # Reusable Compose UI components (cards, charts, animations)
├── di/              # Hilt modules (NetworkModule, SharedDataModule, SecurityModule)
├── repo/            # Repositories (network calls + DataStore persistence)
├── screens/
│   ├── navigation/  # Route definitions (sealed interface Route : NavKey) + Navigator
│   ├── main/        # Home screen & MainViewModel
│   ├── login/       # Auth screens
│   ├── news/        # News feed & detail
│   ├── application/ # Feature screens (courseTable, grade, classroom, library, etc.)
│   ├── message/     # Notification/messages
│   ├── person/      # User profile
│   └── setting/     # Settings, about, feedback
├── ui/theme/        # Material3 theme, colors, typography
└── utils/           # Utility classes (crypto, date, course colors, etc.)
```

### Key Architectural Patterns

- **Navigation**: All routes are `@Serializable` data objects/classes implementing `sealed interface Route : NavKey` in `screens/navigation/Route.kt`. Navigation uses `Navigator` class with a `mutableStateListOf<NavKey>` backstack, provided via `CompositionLocalProvider(LocalNavigator)`.
- **ViewModels**: Hilt-injected (`@HiltViewModel`), expose `StateFlow<UiState>` with `MutableStateFlow` + `_uiState.update {}` pattern.
- **Network layer**: Multiple Retrofit service interfaces per backend (JWCService for 教务, AuthLoginService for 统一认证, EHallService for 一网通办, etc.). Each has a corresponding `*Repo` or `*NetworkRepo` class.
- **Cookie management**: `NetworkCookieJar` persists cookies to DataStore for session continuity across app restarts.
- **Shared data**: `SharedDataRepository` (singleton) manages term calendar, week index, and student grade — shared across ViewModels.
- **Multiple auth systems**: The app authenticates against separate backends (JWC 教务, 统一认证, 图书馆, 第二课堂) with independent token/cookie flows.

### External APIs
All API base URLs are defined in `di/NetworkModule.kt` → `ApiConstants`. The app scrapes/parses HTML from some university pages (using Jsoup) rather than using official APIs.

## Version Catalog

Dependencies are managed via `gradle/libs.versions.toml`. Use `libs.xxx.yyy` references in `build.gradle.kts`.
