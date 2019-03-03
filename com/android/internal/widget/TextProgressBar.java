package com.android.internal.widget;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.RemoteViews.RemoteView;

@RemoteViews.RemoteView
public class TextProgressBar
  extends RelativeLayout
  implements Chronometer.OnChronometerTickListener
{
  static final int CHRONOMETER_ID = 16908308;
  static final int PROGRESSBAR_ID = 16908301;
  public static final String TAG = "TextProgressBar";
  Chronometer mChronometer = null;
  boolean mChronometerFollow = false;
  int mChronometerGravity = 0;
  int mDuration = -1;
  long mDurationBase = -1L;
  ProgressBar mProgressBar = null;
  
  public TextProgressBar(Context paramContext)
  {
    super(paramContext);
  }
  
  public TextProgressBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public TextProgressBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public TextProgressBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams)
  {
    super.addView(paramView, paramInt, paramLayoutParams);
    paramInt = paramView.getId();
    if ((paramInt == 16908308) && ((paramView instanceof Chronometer)))
    {
      mChronometer = ((Chronometer)paramView);
      mChronometer.setOnChronometerTickListener(this);
      boolean bool;
      if (width == -2) {
        bool = true;
      } else {
        bool = false;
      }
      mChronometerFollow = bool;
      mChronometerGravity = (mChronometer.getGravity() & 0x800007);
    }
    else if ((paramInt == 16908301) && ((paramView instanceof ProgressBar)))
    {
      mProgressBar = ((ProgressBar)paramView);
    }
  }
  
  public void onChronometerTick(Chronometer paramChronometer)
  {
    if (mProgressBar != null)
    {
      long l = SystemClock.elapsedRealtime();
      if (l >= mDurationBase) {
        mChronometer.stop();
      }
      int i = (int)(mDurationBase - l);
      mProgressBar.setProgress(mDuration - i);
      if (mChronometerFollow)
      {
        paramChronometer = (RelativeLayout.LayoutParams)mProgressBar.getLayoutParams();
        int j = mProgressBar.getWidth() - (leftMargin + rightMargin);
        int k = mProgressBar.getProgress() * j / mProgressBar.getMax();
        int m = leftMargin;
        i = 0;
        int n = mChronometer.getWidth();
        if (mChronometerGravity == 8388613) {
          i = -n;
        } else if (mChronometerGravity == 1) {
          i = -(n / 2);
        }
        k = k + m + i;
        j = j - rightMargin - n;
        if (k < leftMargin)
        {
          i = leftMargin;
        }
        else
        {
          i = k;
          if (k > j) {
            i = j;
          }
        }
        mChronometer.getLayoutParams()).leftMargin = i;
        mChronometer.requestLayout();
      }
      return;
    }
    throw new RuntimeException("Expecting child ProgressBar with id 'android.R.id.progress'");
  }
  
  @RemotableViewMethod
  public void setDurationBase(long paramLong)
  {
    mDurationBase = paramLong;
    if ((mProgressBar != null) && (mChronometer != null))
    {
      mDuration = ((int)(paramLong - mChronometer.getBase()));
      if (mDuration <= 0) {
        mDuration = 1;
      }
      mProgressBar.setMax(mDuration);
      return;
    }
    throw new RuntimeException("Expecting child ProgressBar with id 'android.R.id.progress' and Chronometer id 'android.R.id.text1'");
  }
}
