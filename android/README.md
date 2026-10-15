# Android 版像素小猫

这是现有 Windows 桌宠的 Android 原生适配版。需要 Android Studio、JDK 17 和 Android SDK 35；打开 `android/` 后运行即可。首次启动时点击“开启 / 显示小猫”，在系统设置中允许“显示在其他应用上层”。

桌宠以前台服务运行，支持悬浮显示、拖动和点击互动；通知栏可看到运行状态。当前版本使用 Java 原生 View 绘制像素猫，不需要第三方依赖。Android 模拟器或真机上验证悬浮窗最可靠。
