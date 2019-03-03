package android.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Process;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import com.android.internal.R.styleable;
import java.util.TimeZone;

@RemoteViews.RemoteView
@Deprecated
public class AnalogClock
  extends View
{
  private boolean mAttached;
  private Time mCalendar;
  private boolean mChanged;
  private Drawable mDial;
  private int mDialHeight;
  private int mDialWidth;
  private float mHour;
  private Drawable mHourHand;
  private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      if (paramAnonymousIntent.getAction().equals("android.intent.action.TIMEZONE_CHANGED"))
      {
        paramAnonymousContext = paramAnonymousIntent.getStringExtra("time-zone");
        AnalogClock.access$002(AnalogClock.this, new Time(TimeZone.getTimeZone(paramAnonymousContext).getID()));
      }
      AnalogClock.this.onTimeChanged();
      invalidate();
    }
  };
  private Drawable mMinuteHand;
  private float mMinutes;
  
  public AnalogClock(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public AnalogClock(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public AnalogClock(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public AnalogClock(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramContext.getResources();
    paramAttributeSet = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.AnalogClock, paramInt1, paramInt2);
    mDial = paramAttributeSet.getDrawable(0);
    if (mDial == null) {
      mDial = paramContext.getDrawable(17302360);
    }
    mHourHand = paramAttributeSet.getDrawable(1);
    if (mHourHand == null) {
      mHourHand = paramContext.getDrawable(17302361);
    }
    mMinuteHand = paramAttributeSet.getDrawable(2);
    if (mMinuteHand == null) {
      mMinuteHand = paramContext.getDrawable(17302362);
    }
    mCalendar = new Time();
    mDialWidth = mDial.getIntrinsicWidth();
    mDialHeight = mDial.getIntrinsicHeight();
  }
  
  private void onTimeChanged()
  {
    mCalendar.setToNow();
    int i = mCalendar.hour;
    int j = mCalendar.minute;
    int k = mCalendar.second;
    mMinutes = (j + k / 60.0F);
    mHour = (i + mMinutes / 60.0F);
    mChanged = true;
    updateContentDescription(mCalendar);
  }
  
  private void updateContentDescription(Time paramTime)
  {
    setContentDescription(DateUtils.formatDateTime(mContext, paramTime.toMillis(false), 129));
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    if (!mAttached)
    {
      mAttached = true;
      IntentFilter localIntentFilter = new IntentFilter();
      localIntentFilter.addAction("android.intent.action.TIME_TICK");
      localIntentFilter.addAction("android.intent.action.TIME_SET");
      localIntentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
      getContext().registerReceiverAsUser(mIntentReceiver, Process.myUserHandle(), localIntentFilter, null, getHandler());
    }
    mCalendar = new Time();
    onTimeChanged();
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    if (mAttached)
    {
      getContext().unregisterReceiver(mIntentReceiver);
      mAttached = false;
    }
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    boolean bool = mChanged;
    if (bool) {
      mChanged = false;
    }
    int i = mRight - mLeft;
    int j = mBottom - mTop;
    int k = i / 2;
    int m = j / 2;
    Drawable localDrawable = mDial;
    int n = localDrawable.getIntrinsicWidth();
    int i1 = localDrawable.getIntrinsicHeight();
    int i2 = 0;
    if ((i < n) || (j < i1))
    {
      i2 = 1;
      float f = Math.min(i / n, j / i1);
      paramCanvas.save();
      paramCanvas.scale(f, f, k, m);
    }
    if (bool) {
      localDrawable.setBounds(k - n / 2, m - i1 / 2, n / 2 + k, i1 / 2 + m);
    }
    localDrawable.draw(paramCanvas);
    paramCanvas.save();
    paramCanvas.rotate(mHour / 12.0F * 360.0F, k, m);
    localDrawable = mHourHand;
    if (bool)
    {
      j = localDrawable.getIntrinsicWidth();
      i = localDrawable.getIntrinsicHeight();
      localDrawable.setBounds(k - j / 2, m - i / 2, j / 2 + k, m + i / 2);
    }
    localDrawable.draw(paramCanvas);
    paramCanvas.restore();
    paramCanvas.save();
    paramCanvas.rotate(mMinutes / 60.0F * 360.0F, k, m);
    localDrawable = mMinuteHand;
    if (bool)
    {
      i = localDrawable.getIntrinsicWidth();
      j = localDrawable.getIntrinsicHeight();
      localDrawable.setBounds(k - i / 2, m - j / 2, i / 2 + k, m + j / 2);
    }
    localDrawable.draw(paramCanvas);
    paramCanvas.restore();
    if (i2 != 0) {
      paramCanvas.restore();
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = View.MeasureSpec.getSize(paramInt1);
    int k = View.MeasureSpec.getMode(paramInt2);
    int m = View.MeasureSpec.getSize(paramInt2);
    float f1 = 1.0F;
    float f2 = 1.0F;
    float f3 = f1;
    if (i != 0)
    {
      f3 = f1;
      if (j < mDialWidth) {
        f3 = j / mDialWidth;
      }
    }
    f1 = f2;
    if (k != 0)
    {
      f1 = f2;
      if (m < mDialHeight) {
        f1 = m / mDialHeight;
      }
    }
    f3 = Math.min(f3, f1);
    setMeasuredDimension(resolveSizeAndState((int)(mDialWidth * f3), paramInt1, 0), resolveSizeAndState((int)(mDialHeight * f3), paramInt2, 0));
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    mChanged = true;
  }
}
