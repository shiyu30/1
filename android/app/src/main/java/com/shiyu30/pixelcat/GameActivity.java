package com.shiyu30.pixelcat;

import android.app.*; import android.os.*; import android.graphics.*; import android.graphics.drawable.ColorDrawable; import android.view.*; import android.content.*; import java.util.*;

/** 一键闪避：拖动蓝色玩家接星星、躲红块。 */
public class GameActivity extends Activity {
 @Override public void onCreate(Bundle b){super.onCreate(b); getWindow().setStatusBarColor(Color.rgb(15,18,35)); setContentView(new GameView(this));}
 static class GameView extends View {
  Paint p=new Paint(3); Random rng=new Random(); ArrayList<Item> items=new ArrayList<>(); float px,py,downX; int score,best,combo; long last,spawn; boolean playing;
  int W,H;
  GameView(Context c){super(c); p.setTypeface(Typeface.create("sans",Typeface.BOLD)); setBackgroundColor(Color.rgb(15,18,35)); best=c.getSharedPreferences("game",0).getInt("best",0);}
  protected void onSizeChanged(int w,int h,int ow,int oh){W=w;H=h; px=w/2f;py=h-180;}
  protected void onDraw(Canvas c){super.onDraw(c); long now=System.currentTimeMillis(); if(playing){float dt=Math.min(.04f,(now-last)/1000f); update(dt,now);} last=now;
   p.setTextAlign(Paint.Align.CENTER); p.setTextSize(22); p.setColor(Color.WHITE); c.drawText("分数  "+score+"     最高  "+best, W/2,55,p);
   for(Item i:items){p.setColor(i.star?Color.rgb(255,211,74):Color.rgb(255,78,91)); if(i.star){Path q=new Path();for(int n=0;n<10;n++){double a=-Math.PI/2+n*Math.PI/5;float r=n%2==0?i.r:i.r*.45f;float x=i.x+(float)Math.cos(a)*r,y=i.y+(float)Math.sin(a)*r;if(n==0)q.moveTo(x,y);else q.lineTo(x,y);}q.close();c.drawPath(q,p);}else c.drawRoundRect(i.x-i.r,i.y-i.r,i.x+i.r,i.y+i.r,8,8,p);}
   p.setColor(Color.rgb(79,184,255)); c.drawCircle(px,py,22,p); p.setColor(Color.rgb(180,235,255)); c.drawCircle(px-7,py-7,6,p);
   p.setTextSize(18); if(!playing){p.setColor(Color.WHITE); c.drawText(score==0?"拖动蓝点接星星，躲开红块":"撞到了！",W/2,H/2-35,p);p.setTextSize(15);p.setColor(Color.rgb(180,190,215));c.drawText(score==0?"按住屏幕移动 · 点击开始":"点击屏幕再来一局",W/2,H/2+5,p);}
   if(playing){postInvalidateDelayed(16);} }
  void start(){playing=true;score=0;combo=0;items.clear();last=System.currentTimeMillis();spawn=0;}
  void update(float dt,long now){float speed=180+score*7;for(Item i:items)i.y+=i.star?speed*.7f*dt:speed*dt; if(now-spawn>Math.max(260,720-score*8)){spawn=now;boolean star=rng.nextFloat()<.28f;items.add(new Item(35+rng.nextInt(Math.max(1,W-70)), -30,star?14:20,star));}
   for(int n=items.size()-1;n>=0;n--){Item i=items.get(n);float d=(float)Math.hypot(i.x-px,i.y-py);if(d<i.r+22){if(i.star){score+=1+combo/5;combo++;items.remove(n);}else{playing=false;best=Math.max(best,score);getContext().getSharedPreferences("game",0).edit().putInt("best",best).apply();} }else if(i.y>H+50)items.remove(n);}
  }
  public boolean onTouchEvent(MotionEvent e){if(e.getAction()==MotionEvent.ACTION_DOWN){downX=e.getX();if(!playing){start();invalidate();return true;}} if(e.getAction()==MotionEvent.ACTION_MOVE||e.getAction()==MotionEvent.ACTION_DOWN){px=Math.max(25,Math.min(W-25,e.getX()));py=Math.max(100,Math.min(H-45,e.getY()));invalidate();return true;}return true;}
  static class Item{float x,y,r;boolean star;Item(float x,float y,float r,boolean s){this.x=x;this.y=y;this.r=r;star=s;}}
 }
}
