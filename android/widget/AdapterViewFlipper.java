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
public class AdapterViewFlipper
  extends AdapterViewAnimator
{
  private static final int DEFAULT_INTERVAL = 10000;
  private static final boolean LOGD = false;
  private static final String TAG = "ViewFlipper";
  private boolean mAdvancedByHost = false;
  private boolean mAutoStart = false;
  private int mFlipInterval = 10000;
  private final Runnable mFlipRunnable = new Runnable()
  {
    public void run()
    {
      if (mRunning) {
        showNext();
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
        AdapterViewFlipper.access$002(AdapterViewFlipper.this, false);
        AdapterViewFlipper.this.updateRunning();
      }
      else if ("android.intent.action.USER_PRESENT".equals(paramAnonymousContext))
      {
        AdapterViewFlipper.access$002(AdapterViewFlipper.this, true);
        AdapterViewFlipper.this.updateRunning(false);
      }
    }
  };
  private boolean mRunning = false;
  private boolean mStarted = false;
  private boolean mUserPresent = true;
  private boolean mVisible = false;
  
  public AdapterViewFlipper(Context paramContext)
  {
    super(paramContext);
  }
  
  public AdapterViewFlipper(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public AdapterViewFlipper(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public AdapterViewFlipper(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.AdapterViewFlipper, paramInt1, paramInt2);
    mFlipInterval = paramContext.getInt(0, 10000);
    mAutoStart = paramContext.getBoolean(1, false);
    mLoopViews = true;
    paramContext.recycle();
  }
  
  private void updateRunning()
  {
    updateRunning(true);
  }
  
  private void updateRunning(boolean paramBoolean)
  {
    boolean bool;
    if ((!mAdvancedByHost) && (mVisible) && (mStarted) && (mUserPresent) && (mAdapter != null)) {
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
  
  public void fyiWillBeAdvancedByHostKThx()
  {
    mAdvancedByHost = true;
    updateRunning(false);
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return AdapterViewFlipper.class.getName();
  }
  
  public int getFlipInterval()
  {
    return mFlipInterval;
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
  
  public void setAdapter(Adapter paramAdapter)
  {
    super.setAdapter(paramAdapter);
    updateRunning();
  }
  
  public void setAutoStart(boolean paramBoolean)
  {
    mAutoStart = paramBoolean;
  }
  
  public void setFlipInterval(int paramInt)
  {
    mFlipInterval = paramInt;
  }
  
  @RemotableViewMethod
  public void showNext()
  {
    if (mRunning)
    {
      removeCallbacks(mFlipRunnable);
      postDelayed(mFlipRunnable, mFlipInterval);
    }
    super.showNext();
  }
  
  @RemotableViewMethod
  public void showPrevious()
  {
    if (mRunning)
    {
      removeCallbacks(mFlipRunnable);
      postDelayed(mFlipRunnable, mFlipInterval);
    }
    super.showPrevious();
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
