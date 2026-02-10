# WanAndroid Compose

This project is a modern Android client implemented using **Jetpack Compose**, designed to integrate with the [WanAndroid](https://www.wanandroid.com/) open API. It follows the Material Design 3 design guidelines and incorporates the MVVM architecture along with Kotlin Coroutines, showcasing the most mainstream Android development stack today.

## ✨ Features

This project not only covers the core functionalities of WanAndroid but also includes numerous advanced features and custom UI demos.

### Core Business Features
*   **📱 Home**: Displays article lists with a top carousel banner.
*   **🔍 Search**: Supports searching via popular keywords and managing search history.
*   **📂 Project**: Displays categorized project lists.
*   **👤 Mine**: User login/register functionality and display of personal points information.
*   **🪙 Points System**: Points leaderboard and history of points earned.
*   **🌐 Web**: Built-in WebView to load article details.

### Advanced Demo Examples
The project integrates multiple demonstration modules (`module/demo`) for learning and reference:
*   **🎬 TikTok Video**: Implements a TikTok-like vertical scrolling video playback feature using ExoPlayer, with interactions such as likes, comments, and sharing.
*   **🔔 Alarm & Notification**: Demonstrates creating complex notifications, setting one-time/repeating alarms, and handling permission requests.
*   **📅 Calendar Integration**: Shows how to write reminder events into the system calendar.
*   **🛒 Takeout UI**: Mimics food delivery app behaviors such as synchronized cart animations, category tags, and linked lists.
*   **🎨 Signature Drawing**: Implements a handwritten signature feature using the Canvas API.
*   **💾 Network Cache**: Demonstrates an offline caching strategy using OkHttp interceptors.

## 🛠 Technology Stack

*   **UI Framework**: Jetpack Compose (Material 3)
*   **Architecture Pattern**: MVVM + Clean Architecture
*   **Network Requests**: Retrofit + OkHttp + Coroutines
*   **Image Loading**: Coil
*   **Video Playback**: ExoPlayer
*   **State Management**: ViewModel + StateFlow / SharedFlow
*   **Navigation**: Jetpack Navigation Compose
*   **Dependency Injection**: Simple lazy loading (avoiding heavy DI frameworks to keep the demo lightweight)
*   **Lint Checks**: Custom Lint rules (`lint-rules` module)

## 📁 Project Structure

The project structure is clear and highly modular.

```text
app/src/main/java/com/lcz/wanandroid_compose/
├── base/               # Base classes: BaseViewModel, BaseResponseBean
├── data/               # Data layer: Repository, DataSource, cache management
├── module/             # Feature modules
│   ├── main/           # Home, Mine, Project tabs
│   ├── login/          # Login module
│   ├── search/         # Search module
│   ├── coin/           # Points-related features
│   ├── ticktok/        # TikTok video module
│   ├── demo/           # Various feature demos (Alarm, Calendar, etc.)
│   └── ...
├── net/                # Network configuration: RetrofitManager, Interceptor
├── navigation/         # Navigation graph configuration
├── theme/              # Theme configuration
├── widget/             # Reusable custom components (Banner, BottomBar, RefreshableList)
└── util/               # Utility classes (SPUtil, LogUtil, ToastUtil)
```

## 🚀 Environment Setup

*   **JDK**: JDK 17 or higher.
*   **Gradle**: Gradle 8.x (refer to `gradle/wrapper/gradle-wrapper.properties` for the exact version).
*   **Android Studio**: Arctic Fox (2020.3.1) or higher.
*   **Android SDK**: compileSdk 35 (uses newer APIs).

### Build and Run

1. Clone the project to your local machine.
2. Open the project root directory in Android Studio.
3. Wait for Gradle synchronization to complete.
4. Connect a device or launch an emulator, then click Run.

## 📄 License

This project is open-sourced under the MIT License.

---

**Thank you for reading! Welcome to Star and Fork!**