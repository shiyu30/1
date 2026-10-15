# 像素小猫桌宠

Windows 桌面上的一只橘色像素小猫。纯 Python 标准库，无需联网或第三方依赖。

## 启动

1. 安装 Windows 版 Python 3.10 或更新版本，保留安装选项 **Tcl/Tk and IDLE**，勾选 **Add Python to PATH**。
2. 将整个项目文件夹下载到本地，双击 `start.bat`。

也可以在项目目录运行 `py -3 pet.py`。这是源码版本，尚未打包为独立 exe。

## 操作

- 小猫会待机、眨眼和左右走动，到达主屏幕工作区边缘后转向。
- 按住左键拖动；松开后停留在新的高度。
- 点击小猫显示爱心。
- 右键菜单：摸摸、睡觉或叫醒、开关自由走动、开关置顶、回到底部、退出。
- 窗口获得焦点时也可按 Esc 退出。

首版按主屏幕工作区限制活动范围，避开任务栏；暂不支持跨屏。改变分辨率或任务栏位置后，可通过右键菜单“回到桌面底部”刷新范围。背景透明区域的鼠标穿透由 Windows Tk 透明色窗口实现。

## 更换角色

`character.py` 单独负责角色绘制：`CAT` 是像素图，`PALETTE` 是配色。保留 `draw(canvas, direction, frame, state)` 接口，即可替换角色，不必改动窗口和拖拽逻辑。状态包含 `idle`、`walk`、`happy`、`sleep`、`drag`。

## 开发验证

运行 `python -m unittest discover -s tests -v`。测试覆盖活动边界、转向与像素角色绘制。Windows 透明窗口、实际鼠标交互和高 DPI 显示需要在 Windows 桌面实测；Linux 上运行仅用于调试，会显示洋红色背景。
