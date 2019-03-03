package com.android.internal.app;

import android.animation.TimeAnimator;
import android.animation.TimeAnimator.TimeListener;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;

public class PlatLogoActivity
  extends Activity
{
  TimeAnimator anim;
  PBackground bg;
  FrameLayout layout;
  
  public PlatLogoActivity() {}
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    layout = new FrameLayout(this);
    setContentView(layout);
    bg = new PBackground();
    layout.setBackground(bg);
    layout.setOnTouchListener(new View.OnTouchListener()
    {
      final MotionEvent.PointerCoords pc0 = new MotionEvent.PointerCoords();
      final MotionEvent.PointerCoords pc1 = new MotionEvent.PointerCoords();
      
      public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
      {
        int i = paramAnonymousMotionEvent.getActionMasked();
        if (((i == 0) || (i == 2)) && (paramAnonymousMotionEvent.getPointerCount() > 1))
        {
          paramAnonymousMotionEvent.getPointerCoords(0, pc0);
          paramAnonymousMotionEvent.getPointerCoords(1, pc1);
          bg.setRadius((float)Math.hypot(pc0.x - pc1.x, pc0.y - pc1.y) / 2.0F);
        }
        return true;
      }
    });
  }
  
  public void onStart()
  {
    super.onStart();
    bg.randomizePalette();
    anim = new TimeAnimator();
    anim.setTimeListener(new TimeAnimator.TimeListener()
    {
      public void onTimeUpdate(TimeAnimator paramAnonymousTimeAnimator, long paramAnonymousLong1, long paramAnonymousLong2)
      {
        bg.setOffset((float)paramAnonymousLong1 / 60000.0F);
        bg.invalidateSelf();
      }
    });
    anim.start();
  }
  
  public void onStop()
  {
    if (anim != null)
    {
      anim.cancel();
      anim = null;
    }
    super.onStop();
  }
  
  private class PBackground
    extends Drawable
  {
    private int darkest;
    private float dp;
    private float maxRadius;
    private float offset;
    private int[] palette;
    private float radius;
    private float x;
    private float y;
    
    public PBackground()
    {
      randomizePalette();
    }
    
    public void draw(Canvas paramCanvas)
    {
      if (dp == 0.0F) {
        dp = getResources().getDisplayMetrics().density;
      }
      float f1 = paramCanvas.getWidth();
      float f2 = paramCanvas.getHeight();
      if (radius == 0.0F)
      {
        setPosition(f1 / 2.0F, f2 / 2.0F);
        setRadius(f1 / 6.0F);
      }
      float f3 = radius * 0.667F;
      Paint localPaint = new Paint();
      localPaint.setStrokeCap(Paint.Cap.BUTT);
      paramCanvas.translate(x, y);
      Path localPath = new Path();
      localPath.moveTo(-radius, f2);
      localPath.lineTo(-radius, 0.0F);
      localPath.arcTo(-radius, -radius, radius, radius, -180.0F, 270.0F, false);
      localPath.lineTo(-radius, radius);
      f1 = Math.max(paramCanvas.getWidth(), paramCanvas.getHeight());
      localPaint.setStyle(Paint.Style.FILL);
      int i = 0;
      f1 *= 1.414F;
      while (f1 > radius * 2.0F + f3 * 2.0F)
      {
        localPaint.setColor(palette[(i % palette.length)] | 0xFF000000);
        paramCanvas.drawOval(-f1 / 2.0F, -f1 / 2.0F, f1 / 2.0F, f1 / 2.0F, localPaint);
        f1 = (float)(f1 - f3 * (1.100000023841858D + Math.sin((i / 20.0F + offset) * 3.14159F)));
        i++;
      }
      localPaint.setColor(palette[((darkest + 1) % palette.length)] | 0xFF000000);
      paramCanvas.drawOval(-radius, -radius, radius, radius, localPaint);
      localPath.reset();
      localPath.moveTo(-radius, f2);
      localPath.lineTo(-radius, 0.0F);
      localPath.arcTo(-radius, -radius, radius, radius, -180.0F, 270.0F, false);
      localPath.lineTo(-radius + f3, radius);
      localPaint.setStyle(Paint.Style.STROKE);
      localPaint.setStrokeWidth(2.0F * f3);
      localPaint.setColor(palette[darkest]);
      paramCanvas.drawPath(localPath, localPaint);
      localPaint.setStrokeWidth(f3);
      localPaint.setColor(-1);
      paramCanvas.drawPath(localPath, localPaint);
    }
    
    public int getOpacity()
    {
      return 0;
    }
    
    public float lum(int paramInt)
    {
      return (Color.red(paramInt) * 299.0F + Color.green(paramInt) * 587.0F + Color.blue(paramInt) * 114.0F) / 1000.0F;
    }
    
    public void randomizePalette()
    {
      int i = (int)(Math.random() * 2.0D) + 2;
      Object localObject1 = new float[3];
      localObject1[0] = ((float)Math.random() * 360.0F);
      localObject1[1] = 1.0F;
      localObject1[2] = 1.0F;
      palette = new int[i];
      darkest = 0;
      for (int j = 0; j < i; j++)
      {
        palette[j] = Color.HSVToColor((float[])localObject1);
        localObject1[0] += 360.0F / i;
        if (lum(palette[j]) < lum(palette[darkest])) {
          darkest = j;
        }
      }
      localObject1 = new StringBuilder();
      Object localObject2 = palette;
      i = localObject2.length;
      for (j = 0; j < i; j++) {
        ((StringBuilder)localObject1).append(String.format("#%08x ", new Object[] { Integer.valueOf(localObject2[j]) }));
      }
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("color palette: ");
      ((StringBuilder)localObject2).append(localObject1);
      Log.v("PlatLogoActivity", ((StringBuilder)localObject2).toString());
    }
    
    public void setAlpha(int paramInt) {}
    
    public void setColorFilter(ColorFilter paramColorFilter) {}
    
    public void setOffset(float paramFloat)
    {
      offset = paramFloat;
    }
    
    public void setPosition(float paramFloat1, float paramFloat2)
    {
      x = paramFloat1;
      y = paramFloat2;
    }
    
    public void setRadius(float paramFloat)
    {
      radius = Math.max(48.0F * dp, paramFloat);
    }
  }
}
