package com.android.internal.util;

import android.os.Bundle;
import android.os.IProgressListener;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.MathUtils;
import com.android.internal.annotations.GuardedBy;

public class ProgressReporter
{
  private static final int STATE_FINISHED = 2;
  private static final int STATE_INIT = 0;
  private static final int STATE_STARTED = 1;
  @GuardedBy("this")
  private Bundle mExtras = new Bundle();
  private final int mId;
  @GuardedBy("this")
  private final RemoteCallbackList<IProgressListener> mListeners = new RemoteCallbackList();
  @GuardedBy("this")
  private int mProgress = 0;
  @GuardedBy("this")
  private int[] mSegmentRange = { 0, 100 };
  @GuardedBy("this")
  private int mState = 0;
  
  public ProgressReporter(int paramInt)
  {
    mId = paramInt;
  }
  
  private void notifyFinished(int paramInt, Bundle paramBundle)
  {
    for (int i = mListeners.beginBroadcast() - 1; i >= 0; i--) {
      try
      {
        ((IProgressListener)mListeners.getBroadcastItem(i)).onFinished(paramInt, paramBundle);
      }
      catch (RemoteException localRemoteException) {}
    }
    mListeners.finishBroadcast();
  }
  
  private void notifyProgress(int paramInt1, int paramInt2, Bundle paramBundle)
  {
    for (int i = mListeners.beginBroadcast() - 1; i >= 0; i--) {
      try
      {
        ((IProgressListener)mListeners.getBroadcastItem(i)).onProgress(paramInt1, paramInt2, paramBundle);
      }
      catch (RemoteException localRemoteException) {}
    }
    mListeners.finishBroadcast();
  }
  
  private void notifyStarted(int paramInt, Bundle paramBundle)
  {
    for (int i = mListeners.beginBroadcast() - 1; i >= 0; i--) {
      try
      {
        ((IProgressListener)mListeners.getBroadcastItem(i)).onStarted(paramInt, paramBundle);
      }
      catch (RemoteException localRemoteException) {}
    }
    mListeners.finishBroadcast();
  }
  
  public void addListener(IProgressListener paramIProgressListener)
  {
    if (paramIProgressListener == null) {
      return;
    }
    try
    {
      mListeners.register(paramIProgressListener);
      int i = mState;
      switch (i)
      {
      default: 
        break;
      case 2: 
        try
        {
          paramIProgressListener.onFinished(mId, null);
        }
        catch (RemoteException paramIProgressListener) {}
      case 1: 
        try
        {
          paramIProgressListener.onStarted(mId, null);
          paramIProgressListener.onProgress(mId, mProgress, mExtras);
        }
        catch (RemoteException paramIProgressListener) {}
      }
      return;
    }
    finally {}
  }
  
  public void endSegment(int[] paramArrayOfInt)
  {
    try
    {
      mProgress = (mSegmentRange[0] + mSegmentRange[1]);
      mSegmentRange = paramArrayOfInt;
      return;
    }
    finally {}
  }
  
  public void finish()
  {
    try
    {
      mState = 2;
      notifyFinished(mId, null);
      mListeners.kill();
      return;
    }
    finally {}
  }
  
  int getProgress()
  {
    return mProgress;
  }
  
  int[] getSegmentRange()
  {
    return mSegmentRange;
  }
  
  public void setProgress(int paramInt)
  {
    setProgress(paramInt, 100, null);
  }
  
  public void setProgress(int paramInt1, int paramInt2)
  {
    setProgress(paramInt1, paramInt2, null);
  }
  
  public void setProgress(int paramInt1, int paramInt2, CharSequence paramCharSequence)
  {
    try
    {
      if (mState == 1)
      {
        mProgress = (mSegmentRange[0] + MathUtils.constrain(mSegmentRange[1] * paramInt1 / paramInt2, 0, mSegmentRange[1]));
        if (paramCharSequence != null) {
          mExtras.putCharSequence("android.intent.extra.TITLE", paramCharSequence);
        }
        notifyProgress(mId, mProgress, mExtras);
        return;
      }
      paramCharSequence = new java/lang/IllegalStateException;
      paramCharSequence.<init>("Must be started to change progress");
      throw paramCharSequence;
    }
    finally {}
  }
  
  public void setProgress(int paramInt, CharSequence paramCharSequence)
  {
    setProgress(paramInt, 100, paramCharSequence);
  }
  
  public void start()
  {
    try
    {
      mState = 1;
      notifyStarted(mId, null);
      notifyProgress(mId, mProgress, mExtras);
      return;
    }
    finally {}
  }
  
  public int[] startSegment(int paramInt)
  {
    try
    {
      int[] arrayOfInt = mSegmentRange;
      mSegmentRange = new int[] { mProgress, mSegmentRange[1] * paramInt / 100 };
      return arrayOfInt;
    }
    finally {}
  }
}
