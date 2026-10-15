package com.shiyu30.pixelcat;

import android.app.*; import android.content.*; import android.graphics.*; import android.graphics.drawable.ColorDrawable; import android.os.*; import android.provider.Settings; import android.view.*; import java.util.*;

public class CatService extends Service {
    static final String CHANNEL="pixel_cat"; WindowManager wm; CatView cat; WindowManager.LayoutParams lp; Handler handler=new Handler(Looper.getMainLooper());
    @Override public void onCreate(){ super.onCreate();
        if(Build.VERSION.SDK_INT>=26){ NotificationChannel ch=new NotificationChannel(CHANNEL,"像素小猫",NotificationManager.IMPORTANCE_LOW); getSystemService(NotificationManager.class).createNotificationChannel(ch); }
        Notification n=new Notification.Builder(this,CHANNEL).setContentTitle("像素小猫正在陪伴你").setContentText("点击通知回到设置").setSmallIcon(android.R.drawable.ic_menu_compass).build(); startForeground(7,n);
        wm=(WindowManager)getSystemService(WINDOW_SERVICE); cat=new CatView(this); int type=Build.VERSION.SDK_INT>=26?WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY:WindowManager.LayoutParams.TYPE_PHONE; lp=new WindowManager.LayoutParams(180,210,type,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,PixelFormat.TRANSLUCENT); lp.gravity=Gravity.TOP|Gravity.START; lp.x=80; lp.y=500; wm.addView(cat,lp); }
    void move(int x,int y){ lp.x=x; lp.y=y; wm.updateViewLayout(cat,lp); }
    @Override public void onDestroy(){ if(cat!=null) wm.removeView(cat); handler.removeCallbacksAndMessages(null); super.onDestroy(); }
    @Override public IBinder onBind(Intent i){return null;}
    class CatView extends View { Paint p=new Paint(); float downX,downY; int startX,startY; boolean drag; int dir=1,frame=0; long last=System.currentTimeMillis(); String mood=""; String[] rows={"   ##       ##   ","   #o#     #o#   ","   #op#####po#   ","   #ooooooooo#   ","  #ooooooooooo#  ","  #ooeoooooeoo#  ","  #occccpcccco#  ","   #ccccccccc#   ","    ##ooooo##    "," ## #ooooooo#    ","#oo##ooooooo#    ","#ooo#oocccoo#    "," ##oooocccoo#    ","   ##ooooooo#    ","    #oo###oo#    ","    ###   ###    "};
        CatView(Context c){super(c);setLayerType(View.LAYER_TYPE_SOFTWARE,null); p.setAntiAlias(false); setBackground(new ColorDrawable(Color.TRANSPARENT)); handler.post(tick);}
        void tick(){ frame++; invalidate(); handler.postDelayed(()->tick(),180); }
        protected void onDraw(Canvas c){super.onDraw(c); int s=8, ox=18, oy=54+(frame%2==0?3:0); String[] colors={"#583d35","#f3b56b","#ffe0a0","#ef9390","#302d39"}; for(int y=0;y<rows.length;y++)for(int x=0;x<rows[y].length();x++){char ch=rows[y].charAt(x); if(ch==' ')continue; int ix=dir>0?x:16-x; int ci=ch=='#'?0:ch=='o'?1:ch=='c'?2:ch=='p'?3:4; p.setColor(Color.parseColor(colors[ci])); c.drawRect(ox+ix*s,oy+y*s,ox+ix*s+s,oy+y*s+s,p);} if(!mood.isEmpty()){p.setTextSize(22);p.setColor(Color.rgb(121,82,61));c.drawText(mood,45,38,p);} }
        public boolean onTouchEvent(android.view.MotionEvent e){switch(e.getAction()){case 0:downX=e.getRawX();downY=e.getRawY();startX=lp.x;startY=lp.y;drag=false;return true;case 2:int dx=(int)(e.getRawX()-downX),dy=(int)(e.getRawY()-downY);if(Math.abs(dx)+Math.abs(dy)>8)drag=true;if(drag)move(startX+dx,startY+dy);return true;case 1:if(!drag){mood="♥ 喵～";invalidate();handler.postDelayed(()->{mood="";invalidate();},1500);}return true;}return true;}
    }
}
