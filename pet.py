"""无第三方依赖的 Windows 像素桌宠。"""
import ctypes
import random
import sys
import time
import tkinter as tk
from dataclasses import dataclass

from character import draw

WIDTH, HEIGHT = 128, 140
TRANSPARENT = '#ff00ff'


@dataclass
class Motion:
    x: float
    y: float
    direction: int = 1

    def clamp(self, bounds):
        left, top, right, bottom = bounds
        self.x = min(max(self.x, left), max(left, right - WIDTH))
        self.y = min(max(self.y, top), max(top, bottom - HEIGHT))

    def step(self, dt, bounds):
        self.x += self.direction * 32 * dt
        left, _, right, _ = bounds
        if self.x <= left:
            self.direction = 1
        elif self.x >= right - WIDTH:
            self.direction = -1
        self.clamp(bounds)


def work_area(root):
    if sys.platform == 'win32':
        from ctypes import wintypes
        rect = wintypes.RECT()
        if ctypes.windll.user32.SystemParametersInfoW(0x0030, 0, ctypes.byref(rect), 0):
            return rect.left, rect.top, rect.right, rect.bottom
    return 0, 0, root.winfo_screenwidth(), root.winfo_screenheight()


class DesktopPet:
    def __init__(self, root):
        self.root = root
        root.title('像素小猫')
        root.overrideredirect(True)
        root.attributes('-topmost', True)
        root.configure(bg=TRANSPARENT)
        if sys.platform == 'win32':
            root.wm_attributes('-transparentcolor', TRANSPARENT)
        self.canvas = tk.Canvas(root, width=WIDTH, height=HEIGHT, bg=TRANSPARENT,
                                highlightthickness=0, cursor='hand2')
        self.canvas.pack()
        self.bounds = work_area(root)
        self.motion = Motion(self.bounds[2] - WIDTH - 80, self.bounds[3] - HEIGHT)
        self.motion.clamp(self.bounds)
        self.state = 'idle'
        self.deadline = time.monotonic() + 2
        self.last = time.monotonic()
        self.frame = 0
        self.drag = None
        self.moved = False
        self.menu_open = False
        self.roaming = tk.BooleanVar(value=True)
        self.topmost = tk.BooleanVar(value=True)
        self.menu = tk.Menu(root, tearoff=False)
        self.menu.add_command(label='摸摸小猫', command=self.pet)
        self.menu.add_command(label='睡觉 / 叫醒', command=self.sleep)
        self.menu.add_checkbutton(label='自由走动', variable=self.roaming)
        self.menu.add_checkbutton(label='总在最前', variable=self.topmost,
                                  command=lambda: root.attributes('-topmost', self.topmost.get()))
        self.menu.add_command(label='回到桌面底部', command=self.reset)
        self.menu.add_separator()
        self.menu.add_command(label='退出', command=root.destroy)
        self.canvas.bind('<ButtonPress-1>', self.press)
        self.canvas.bind('<B1-Motion>', self.move)
        self.canvas.bind('<ButtonRelease-1>', self.release)
        self.canvas.bind('<Button-3>', self.popup)
        root.bind('<Escape>', lambda event: root.destroy())
        self.tick()

    def place(self):
        self.root.geometry(f'{WIDTH}x{HEIGHT}{round(self.motion.x):+d}{round(self.motion.y):+d}')

    def pet(self):
        self.state = 'happy'
        self.deadline = time.monotonic() + 2

    def sleep(self):
        self.state = 'idle' if self.state == 'sleep' else 'sleep'
        self.deadline = time.monotonic() + 2

    def reset(self):
        self.bounds = work_area(self.root)
        self.motion.y = self.bounds[3] - HEIGHT
        self.motion.clamp(self.bounds)
        self.place()

    def press(self, event):
        self.drag = (event.x_root, event.y_root, self.motion.x, self.motion.y)
        self.moved = False

    def move(self, event):
        if self.drag is None:
            return
        x, y, start_x, start_y = self.drag
        dx, dy = event.x_root - x, event.y_root - y
        if abs(dx) + abs(dy) > 4:
            self.moved = True
        if self.moved:
            self.state = 'drag'
            self.motion.x, self.motion.y = start_x + dx, start_y + dy
            self.motion.clamp(self.bounds)
            self.place()

    def release(self, event):
        self.drag = None
        self.pet()

    def popup(self, event):
        self.menu_open = True
        try:
            self.menu.tk_popup(event.x_root, event.y_root)
        finally:
            self.menu.grab_release()
            self.menu_open = False

    def tick(self):
        now = time.monotonic()
        dt = min(now - self.last, 0.1)
        self.last = now
        if self.drag is None and not self.menu_open and self.state != 'sleep':
            if now >= self.deadline:
                self.state = random.choice(('idle', 'walk')) if self.roaming.get() else 'idle'
                self.deadline = now + random.uniform(2, 5)
            if self.state == 'walk' and self.roaming.get():
                self.motion.step(dt, self.bounds)
        self.place()
        draw(self.canvas, self.motion.direction, self.frame, self.state)
        self.frame += 1
        self.root.after(50, self.tick)


def main():
    if sys.platform == 'win32':
        ctypes.windll.user32.SetProcessDPIAware()
    root = tk.Tk()
    DesktopPet(root)
    root.mainloop()


if __name__ == '__main__':
    main()
