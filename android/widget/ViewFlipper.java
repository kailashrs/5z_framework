package android.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.Process;
import android.util.AttributeSet;
import android.view.RemotableViewMethod;
import com.android.internal.R.styleable;

@RemoteViews.RemoteView
public class ViewFlipper
  extends ViewAnimator
{
  private static final int DEFAULT_INTERVAL = 3000;
  private static final boolean LOGD = false;
  private static final String TAG = "ViewFlipper";
  private boolean mAutoStart = false;
  private int mFlipInterval = 3000;
  private final Runnable mFlipRunnable = new Runnable()
  {
    public void run()
    {
      if (mRunning)
      {
        showNext();
        postDelayed(mFlipRunnable, mFlipInterval);
      }
    }
  };
  private final BroadcastReceiver mReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      paramAnonymousContext = paramAnonymousIntent.getAction();
      if ("android.intent.action.SCREEN_OFF".equals(paramAnonymousContext))
      {
        ViewFlipper.access$002(ViewFlipper.this, false);
        ViewFlipper.this.updateRunning();
      }
      else if ("android.intent.action.USER_PRESENT".equals(paramAnonymousContext))
      {
        ViewFlipper.access$002(ViewFlipper.this, true);
        ViewFlipper.this.updateRunning(false);
      }
    }
  };
  private boolean mRunning = false;
  private boolean mStarted = false;
  private boolean mUserPresent = true;
  private boolean mVisible = false;
  
  public ViewFlipper(Context paramContext)
  {
    super(paramContext);
  }
  
  public ViewFlipper(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ViewFlipper);
    mFlipInterval = paramContext.getInt(0, 3000);
    mAutoStart = paramContext.getBoolean(1, false);
    paramContext.recycle();
  }
  
  private void updateRunning()
  {
    updateRunning(true);
  }
  
  private void updateRunning(boolean paramBoolean)
  {
    boolean bool;
    if ((mVisible) && (mStarted) && (mUserPresent)) {
      bool = true;
    } else {
      bool = false;
    }
    if (bool != mRunning)
    {
      if (bool)
      {
        showOnly(mWhichChild, paramBoolean);
        postDelayed(mFlipRunnable, mFlipInterval);
      }
      else
      {
        removeCallbacks(mFlipRunnable);
      }
      mRunning = bool;
    }
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return ViewFlipper.class.getName();
  }
  
  public boolean isAutoStart()
  {
    return mAutoStart;
  }
  
  public boolean isFlipping()
  {
    return mStarted;
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("android.intent.action.SCREEN_OFF");
    localIntentFilter.addAction("android.intent.action.USER_PRESENT");
    getContext().registerReceiverAsUser(mReceiver, Process.myUserHandle(), localIntentFilter, null, getHandler());
    if (mAutoStart) {
      startFlipping();
    }
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    mVisible = false;
    getContext().unregisterReceiver(mReceiver);
    updateRunning();
  }
  
  protected void onWindowVisibilityChanged(int paramInt)
  {
    super.onWindowVisibilityChanged(paramInt);
    boolean bool;
    if (paramInt == 0) {
      bool = true;
    } else {
      bool = false;
    }
    mVisible = bool;
    updateRunning(false);
  }
  
  public void setAutoStart(boolean paramBoolean)
  {
    mAutoStart = paramBoolean;
  }
  
  @RemotableViewMethod
  public void setFlipInterval(int paramInt)
  {
    mFlipInterval = paramInt;
  }
  
  public void startFlipping()
  {
    mStarted = true;
    updateRunning();
  }
  
  public void stopFlipping()
  {
    mStarted = false;
    updateRunning();
  }
}
