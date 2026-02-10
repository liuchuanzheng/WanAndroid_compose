

# WanAndroid Compose

本项目是一个基于 **Jetpack Compose** 实现的现代化 Android 客户端，旨在对接 [玩Android](https://www.wanandroid.com/) 开放 API。项目采用 Material Design 3 设计规范，并结合了 MVVM 架构与 Kotlin 协程，展示了当前 Android 端最主流的开发技术栈。

## ✨ 功能特点

本项目不仅涵盖了玩 Android 的基础功能，还包含了许多高级特性与自定义 UI Demo。

### 核心业务功能
*   **📱 首页 (Home)**: 文章列表展示，顶部轮播图 (Banner)。
*   **🔍 搜索 (Search)**: 支持热门搜索关键词搜索、历史搜索记录管理。
*   **📂 项目 (Project)**: 项目分类展示与列表。
*   **👤 个人中心 (Mine)**: 用户登录注册、个人积分信息展示。
*   **🪙 积分体系**: 积分排行榜、积分获取历史记录。
*   **🌐 网页 (Web)**: 内置 WebView 加载文章详情。

### 进阶 Demo 示例
项目中集成了多个功能演示模块 (`module/demo`)，方便学习和参考：
*   **🎬 抖音视频 (TickTok)**: 类似抖音的上下滑动视频播放功能，使用 ExoPlayer 实现，包含点赞、评论、分享等交互。
*   **🔔 通知与闹钟 (Alarm & Notification)**: 演示了如何创建复杂通知、设置一次性/重复闹钟、权限申请处理。
*   **📅 日历集成 (Calendar)**: 演示如何将提醒事件写入系统日历。
*   **🛒 外卖 UI (Takeout)**: 仿外卖 App 的购物车联动动画、分类标签与列表联动效果。
*   **🎨 签名绘制 (Sign)**: 使用 Canvas API 实现的手写签名功能。
*   **💾 网络缓存 (NetCache)**: 演示 OkHttp 拦截器实现的离线缓存策略。

## 🛠 技术栈

*   **UI 框架**: Jetpack Compose (Material 3)
*   **架构模式**: MVVM + Clean Architecture
*   **网络请求**: Retrofit + OkHttp + Coroutines
*   **图片加载**: Coil
*   **视频播放**: ExoPlayer
*   **状态管理**: ViewModel + StateFlow / SharedFlow
*   **导航**: Jetpack Navigation Compose
*   **依赖注入**: 简单的懒加载模式 (避免引入重型 DI 框架以保持 Demo 简洁)
*   **Lint 检查**: 自定义 Lint 规则 (`lint-rules` 模块)

## 📁 项目结构

项目结构清晰，模块化程度高。

```text
app/src/main/java/com/lcz/wanandroid_compose/
├── base/               # 基础类：BaseViewModel, BaseResponseBean
├── data/               # 数据层：Repository, DataSource, 缓存管理
├── module/             # 功能模块
│   ├── main/           # 首页、个人中心、项目Tab
│   ├── login/          # 登录模块
│   ├── search/         # 搜索模块
│   ├── coin/           # 积分相关
│   ├── ticktok/        # 抖音视频模块
│   ├── demo/           # 各类功能演示 (Alarm, Calendar, etc.)
│   └── ...
├── net/                # 网络配置：RetrofitManager, Interceptor
├── navigation/         # 导航图配置
├── theme/              # 主题配置
├── widget/             # 通用自定义组件 (Banner, BottomBar, RefreshableList)
└── util/               # 工具类 (SPUtil, LogUtil, ToastUtil)
```

## 🚀 环境配置

*   **JDK**: JDK 17 或更高版本。
*   **Gradle**: Gradle 8.x (具体版本请参考 `gradle/wrapper/gradle-wrapper.properties`)。
*   **Android Studio**: Arctic Fox (2020.3.1) 或更高版本。
*   **Android SDK**: compileSdk 35 (项目中使用了一些较新的 API)。

### 构建运行

1.  克隆项目至本地。
2.  使用 Android Studio 打开项目根目录。
3.  等待 Gradle 同步完成。
4.  连接设备或启动模拟器，点击运行 (Run)。

## 📄 License

本项目基于 MIT License 开源。

---

**感谢阅读，欢迎 Star 和 Fork！**