package com.shiyu30.pixelcat;

import android.app.*;
import android.content.*;
import android.graphics.Color;
import android.net.Uri;
import android.os.*;
import android.provider.Settings;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity {
    private static final int OVERLAY = 9;
    @Override public void onCreate(Bundle state) { super.onCreate(state);
        LinearLayout box = new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(48,64,48,32);
        TextView title = new TextView(this); title.setText("像素小猫"); title.setTextSize(28); title.setTextColor(Color.rgb(80,55,45));
        TextView info = new TextView(this); info.setText("开启悬浮窗权限后，小猫会出现在其他应用上方。\n点击小猫互动，拖动它到喜欢的位置。\n再次打开此页面可停止桌宠。\n\n这是个人同人原型，不包含官方素材。"); info.setTextSize(16); info.setPadding(0,24,0,24);
        Button start = new Button(this); start.setText("开启 / 显示小猫"); Button stop = new Button(this); stop.setText("隐藏小猫");
        box.addView(title); box.addView(info); box.addView(start); box.addView(stop); setContentView(box);
        start.setOnClickListener(v -> { if (!Settings.canDrawOverlays(this)) { startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+getPackageName())), OVERLAY); } else startCat(); });
        stop.setOnClickListener(v -> stopService(new Intent(this, CatService.class)));
    }
    private void startCat() { Intent i = new Intent(this, CatService.class); if (Build.VERSION.SDK_INT >= 26) startForegroundService(i); else startService(i); }
    @Override protected void onActivityResult(int r,int c,Intent d) { super.onActivityResult(r,c,d); if (r==OVERLAY && Settings.canDrawOverlays(this)) startCat(); }
}
