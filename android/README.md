# Android 版：一键闪避 + 像素小猫

默认启动的是小游戏 **一键闪避**：拖动蓝色小点接黄色星星、躲红色方块。规则只需几秒就能明白；星星会增加分数和连击，分数越高下落越快。撞到红块后点屏幕重开，最高分会保存在手机上。

打开 `android/` 到 Android Studio，使用 JDK 17、Android SDK 35，运行到 Android 手机或模拟器即可。游戏不需要网络和第三方库。

原来的桌宠仍保留在 `MainActivity` 与 `CatService` 中；若要从设置页启动桌宠，可将 `MainActivity` 设为启动 Activity，或在 Android Studio 中直接运行它。
