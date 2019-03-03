package android.widget;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings.System;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import java.util.Calendar;

@Deprecated
public class DigitalClock
  extends TextView
{
  Calendar mCalendar;
  String mFormat;
  private FormatChangeObserver mFormatChangeObserver;
  private Handler mHandler;
  private Runnable mTicker;
  private boolean mTickerStopped = false;
  
  public DigitalClock(Context paramContext)
  {
    super(paramContext);
    initClock();
  }
  
  public DigitalClock(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    initClock();
  }
  
  private void initClock()
  {
    if (mCalendar == null) {
      mCalendar = Calendar.getInstance();
    }
  }
  
  private void setFormat()
  {
    mFormat = DateFormat.getTimeFormatString(getContext());
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return DigitalClock.class.getName();
  }
  
  protected void onAttachedToWindow()
  {
    mTickerStopped = false;
    super.onAttachedToWindow();
    mFormatChangeObserver = new FormatChangeObserver();
    getContext().getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, mFormatChangeObserver);
    setFormat();
    mHandler = new Handler();
    mTicker = new Runnable()
    {
      public void run()
      {
        if (mTickerStopped) {
          return;
        }
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        setText(DateFormat.format(mFormat, mCalendar));
        invalidate();
        long l = SystemClock.uptimeMillis();
        mHandler.postAtTime(mTicker, 1000L - l % 1000L + l);
      }
    };
    mTicker.run();
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    mTickerStopped = true;
    getContext().getContentResolver().unregisterContentObserver(mFormatChangeObserver);
  }
  
  private class FormatChangeObserver
    extends ContentObserver
  {
    public FormatChangeObserver()
    {
      super();
    }
    
    public void onChange(boolean paramBoolean)
    {
      DigitalClock.this.setFormat();
    }
  }
}
