"""角色绘制接口：替换 draw 即可制作其他桌宠。"""
PALETTE = {'#': '#583d35', 'o': '#f3b56b', 'c': '#ffe0a0', 'p': '#ef9390', 'e': '#302d39'}
CAT = (
'   ##       ##   ',
'   #o#     #o#   ',
'   #op#####po#   ',
'   #ooooooooo#   ',
'  #ooooooooooo#  ',
'  #ooeoooooeoo#  ',
'  #occccpcccco#  ',
'   #ccccccccc#   ',
'    ##ooooo##    ',
' ## #ooooooo#    ',
'#oo##ooooooo#    ',
'#ooo#oocccoo#    ',
' ##oooocccoo#    ',
'   ##ooooooo#    ',
'    #oo###oo#    ',
'    ###   ###    ',
)

def draw(canvas, direction, frame, state):
    canvas.delete('all')
    scale = 6
    bounce = 2 if state == 'walk' and frame % 2 else 0
    for y, row in enumerate(CAT):
        for x, pixel in enumerate(row):
            if pixel == ' ':
                continue
            color = PALETTE[pixel]
            if pixel == 'e' and (state == 'sleep' or frame % 80 == 0):
                # 闭眼只画一条细线。
                height = 2
            else:
                height = scale
            px = (x if direction > 0 else 16 - x) * scale + 13
            py = y * scale + 37 + bounce
            canvas.create_rectangle(px, py, px + scale, py + height, fill=color, outline='')
    message = {'happy': '♥ 喵～', 'sleep': 'Z z z', 'drag': '带我去哪呀？'}.get(state)
    if message:
        canvas.create_text(64, 20, text=message, fill='#79523d', font=('Microsoft YaHei', 10, 'bold'))
