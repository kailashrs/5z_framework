package android.view;

import android.hardware.input.InputManager;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

public class CombineKeyDetector
{
  private static final int COMBINE_KEY_TIME_OUT = 250;
  public static final int HAS_HANDLED = 1;
  public static final int NEED_RETURN_EVENT = 100;
  private static final int POWER_KEY_TIME_OUT = 500;
  private static final String TAG = "CombineKeyDetector";
  private OnCombineKeyListener mCombineKeyListener;
  private Handler mHandler;
  private InjectKeyDownRunnable mInjectKeyDownRunnable = new InjectKeyDownRunnable(null);
  private InjectKeyUpRunnable mInjectKeyUpRunnable = new InjectKeyUpRunnable(null);
  private boolean mIsBackWithVolDown = false;
  private boolean mIsDoubleTapPower = false;
  private boolean mIsEnterWithVolUp = false;
  private boolean mIsInjectKey = false;
  private boolean mIsReturnDown = false;
  private KeyEvent mPreDownEvent = null;
  
  public CombineKeyDetector(Handler paramHandler)
  {
    mHandler = paramHandler;
  }
  
  private boolean injectKeyEvent(int paramInt1, int paramInt2)
  {
    long l = SystemClock.uptimeMillis();
    KeyEvent localKeyEvent = new KeyEvent(l, l, paramInt1, paramInt2, 0);
    if (paramInt1 == 0)
    {
      mHandler.removeCallbacks(mInjectKeyDownRunnable);
      mInjectKeyDownRunnable.set(localKeyEvent);
      mHandler.postDelayed(mInjectKeyDownRunnable, 250L);
    }
    else
    {
      mHandler.removeCallbacks(mInjectKeyUpRunnable);
      mInjectKeyUpRunnable.set(localKeyEvent);
      mHandler.postDelayed(mInjectKeyUpRunnable, 250L);
    }
    return true;
  }
  
  private boolean isConsideredBackWithVolumeDown(KeyEvent paramKeyEvent1, KeyEvent paramKeyEvent2)
  {
    long l1 = paramKeyEvent1.getDownTime();
    long l2 = paramKeyEvent2.getDownTime();
    int i;
    if (paramKeyEvent2.getKeyCode() == 4) {
      i = 1;
    } else {
      i = 0;
    }
    return (i != 0) && (l1 - l2 <= 250L);
  }
  
  private boolean isConsideredEnterWithVolumeUp(KeyEvent paramKeyEvent1, KeyEvent paramKeyEvent2)
  {
    long l1 = paramKeyEvent1.getDownTime();
    long l2 = paramKeyEvent2.getDownTime();
    int i;
    if (paramKeyEvent2.getKeyCode() == 66) {
      i = 1;
    } else {
      i = 0;
    }
    return (i != 0) && (l1 - l2 <= 250L);
  }
  
  private boolean isConsideredTripleTapPowerKey(KeyEvent paramKeyEvent1, KeyEvent paramKeyEvent2)
  {
    long l1 = paramKeyEvent1.getDownTime();
    long l2 = paramKeyEvent2.getDownTime();
    if ((paramKeyEvent2.getKeyCode() == paramKeyEvent1.getKeyCode()) && (l1 - l2 <= 500L))
    {
      if (!mIsDoubleTapPower)
      {
        mIsDoubleTapPower = true;
      }
      else
      {
        mIsDoubleTapPower = false;
        return true;
      }
    }
    else {
      mIsDoubleTapPower = false;
    }
    return false;
  }
  
  public void cancelInjectEvent()
  {
    mHandler.removeCallbacks(mInjectKeyDownRunnable);
    mHandler.removeCallbacks(mInjectKeyUpRunnable);
  }
  
  public int onKeyEvent(KeyEvent paramKeyEvent, boolean paramBoolean)
  {
    int i = paramKeyEvent.getAction();
    int j = paramKeyEvent.getKeyCode();
    int k = 0;
    int n = 0;
    int i1 = 0;
    switch (i)
    {
    default: 
      i1 = n;
      break;
    case 1: 
      if (mPreDownEvent == null)
      {
        i1 = n;
      }
      else
      {
        k = i1;
        if (mIsReturnDown)
        {
          mIsReturnDown = false;
          k = 0x0 | 0x64;
        }
        if (mIsEnterWithVolUp) {
          mIsEnterWithVolUp = false;
        }
        i1 = k;
        if (mIsBackWithVolDown)
        {
          mIsBackWithVolDown = false;
          i1 = k;
        }
      }
      break;
    case 0: 
      if (j == 66)
      {
        i1 = n;
        if (paramKeyEvent.getSource() == 0) {
          break;
        }
      }
      else
      {
        i1 = k;
        if (j == 24)
        {
          i1 = k;
          if (mPreDownEvent != null)
          {
            i1 = k;
            if (isConsideredEnterWithVolumeUp(paramKeyEvent, mPreDownEvent))
            {
              mIsEnterWithVolUp = true;
              m = false | mCombineKeyListener.onHandleEnterWithVolumeUp(paramBoolean);
              i1 = m;
              if ((m & 0x1) != 0)
              {
                i1 = m | 0x64;
                mIsReturnDown = true;
                cancelInjectEvent();
              }
            }
          }
        }
        int m = i1;
        if (j == 25)
        {
          m = i1;
          if (mPreDownEvent != null)
          {
            m = i1;
            if (paramBoolean)
            {
              m = i1;
              if (isConsideredBackWithVolumeDown(paramKeyEvent, mPreDownEvent))
              {
                mIsBackWithVolDown = true;
                m = i1 | mCombineKeyListener.onHandleBackWithVolumeDown(paramBoolean);
              }
            }
          }
        }
        n = m;
        if (j == 26)
        {
          n = m;
          if (mPreDownEvent != null)
          {
            n = m;
            if (isConsideredTripleTapPowerKey(paramKeyEvent, mPreDownEvent)) {
              n = m | mCombineKeyListener.onHandleTripleTapPowerKey();
            }
          }
        }
        i1 = n;
        if (j != 837)
        {
          mPreDownEvent = new KeyEvent(paramKeyEvent);
          i1 = n;
        }
      }
      break;
    }
    return i1;
  }
  
  public void setOnCombineKeyListener(OnCombineKeyListener paramOnCombineKeyListener)
  {
    if ((paramOnCombineKeyListener instanceof OnCombineKeyListener)) {
      mCombineKeyListener = paramOnCombineKeyListener;
    }
  }
  
  private static class InjectKeyDownRunnable
    implements Runnable
  {
    private KeyEvent mKeyEvent;
    
    private InjectKeyDownRunnable() {}
    
    public void run()
    {
      boolean bool = InputManager.getInstance().injectInputEvent(mKeyEvent, 0);
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("InjectKeyDown mPreDownEvent is clear, isSuccess: ");
      localStringBuilder.append(bool);
      Log.i("CombineKeyDetector", localStringBuilder.toString());
    }
    
    public void set(KeyEvent paramKeyEvent)
    {
      mKeyEvent = paramKeyEvent;
    }
  }
  
  private static class InjectKeyUpRunnable
    implements Runnable
  {
    private KeyEvent mKeyEvent;
    
    private InjectKeyUpRunnable() {}
    
    public void run()
    {
      boolean bool = InputManager.getInstance().injectInputEvent(mKeyEvent, 0);
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("InjectKeyUp isSuccess: ");
      localStringBuilder.append(bool);
      Log.i("CombineKeyDetector", localStringBuilder.toString());
    }
    
    public void set(KeyEvent paramKeyEvent)
    {
      mKeyEvent = paramKeyEvent;
    }
  }
  
  public static abstract interface OnCombineKeyListener
  {
    public abstract boolean onHandleBackWithVolumeDown(boolean paramBoolean);
    
    public abstract boolean onHandleEnterWithVolumeUp(boolean paramBoolean);
    
    public abstract boolean onHandleTripleTapPowerKey();
  }
  
  public static class SimpleOnCombineKeyListener
    implements CombineKeyDetector.OnCombineKeyListener
  {
    public SimpleOnCombineKeyListener() {}
    
    public boolean onHandleBackWithVolumeDown(boolean paramBoolean)
    {
      return false;
    }
    
    public boolean onHandleEnterWithVolumeUp(boolean paramBoolean)
    {
      return false;
    }
    
    public boolean onHandleTripleTapPowerKey()
    {
      return false;
    }
  }
}
