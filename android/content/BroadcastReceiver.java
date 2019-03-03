package android.content;

import android.app.ActivityManager;
import android.app.IActivityManager;
import android.app.QueuedWork;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public abstract class BroadcastReceiver
{
  private boolean mDebugUnregister;
  private PendingResult mPendingResult;
  
  public BroadcastReceiver() {}
  
  public final void abortBroadcast()
  {
    checkSynchronousHint();
    mPendingResult.mAbortBroadcast = true;
  }
  
  void checkSynchronousHint()
  {
    if (mPendingResult != null)
    {
      if ((!mPendingResult.mOrderedHint) && (!mPendingResult.mInitialStickyHint))
      {
        RuntimeException localRuntimeException = new RuntimeException("BroadcastReceiver trying to return result during a non-ordered broadcast");
        localRuntimeException.fillInStackTrace();
        Log.e("BroadcastReceiver", localRuntimeException.getMessage(), localRuntimeException);
        return;
      }
      return;
    }
    throw new IllegalStateException("Call while result is not pending");
  }
  
  public final void clearAbortBroadcast()
  {
    if (mPendingResult != null) {
      mPendingResult.mAbortBroadcast = false;
    }
  }
  
  public final boolean getAbortBroadcast()
  {
    boolean bool;
    if (mPendingResult != null) {
      bool = mPendingResult.mAbortBroadcast;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final boolean getDebugUnregister()
  {
    return mDebugUnregister;
  }
  
  public final PendingResult getPendingResult()
  {
    return mPendingResult;
  }
  
  public final int getResultCode()
  {
    int i;
    if (mPendingResult != null) {
      i = mPendingResult.mResultCode;
    } else {
      i = 0;
    }
    return i;
  }
  
  public final String getResultData()
  {
    String str;
    if (mPendingResult != null) {
      str = mPendingResult.mResultData;
    } else {
      str = null;
    }
    return str;
  }
  
  public final Bundle getResultExtras(boolean paramBoolean)
  {
    if (mPendingResult == null) {
      return null;
    }
    Bundle localBundle1 = mPendingResult.mResultExtras;
    if (!paramBoolean) {
      return localBundle1;
    }
    Bundle localBundle2 = localBundle1;
    if (localBundle1 == null)
    {
      PendingResult localPendingResult = mPendingResult;
      localBundle1 = new Bundle();
      localBundle2 = localBundle1;
      mResultExtras = localBundle1;
    }
    return localBundle2;
  }
  
  public int getSendingUserId()
  {
    return mPendingResult.mSendingUser;
  }
  
  public final PendingResult goAsync()
  {
    PendingResult localPendingResult = mPendingResult;
    mPendingResult = null;
    return localPendingResult;
  }
  
  public final boolean isInitialStickyBroadcast()
  {
    boolean bool;
    if (mPendingResult != null) {
      bool = mPendingResult.mInitialStickyHint;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final boolean isOrderedBroadcast()
  {
    boolean bool;
    if (mPendingResult != null) {
      bool = mPendingResult.mOrderedHint;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public abstract void onReceive(Context paramContext, Intent paramIntent);
  
  public IBinder peekService(Context paramContext, Intent paramIntent)
  {
    IActivityManager localIActivityManager = ActivityManager.getService();
    Object localObject = null;
    try
    {
      paramIntent.prepareToLeaveProcess(paramContext);
      paramContext = localIActivityManager.peekService(paramIntent, paramIntent.resolveTypeIfNeeded(paramContext.getContentResolver()), paramContext.getOpPackageName());
    }
    catch (RemoteException paramContext)
    {
      paramContext = localObject;
    }
    return paramContext;
  }
  
  public final void setDebugUnregister(boolean paramBoolean)
  {
    mDebugUnregister = paramBoolean;
  }
  
  public final void setOrderedHint(boolean paramBoolean) {}
  
  public final void setPendingResult(PendingResult paramPendingResult)
  {
    mPendingResult = paramPendingResult;
  }
  
  public final void setResult(int paramInt, String paramString, Bundle paramBundle)
  {
    checkSynchronousHint();
    mPendingResult.mResultCode = paramInt;
    mPendingResult.mResultData = paramString;
    mPendingResult.mResultExtras = paramBundle;
  }
  
  public final void setResultCode(int paramInt)
  {
    checkSynchronousHint();
    mPendingResult.mResultCode = paramInt;
  }
  
  public final void setResultData(String paramString)
  {
    checkSynchronousHint();
    mPendingResult.mResultData = paramString;
  }
  
  public final void setResultExtras(Bundle paramBundle)
  {
    checkSynchronousHint();
    mPendingResult.mResultExtras = paramBundle;
  }
  
  public static class PendingResult
  {
    public static final int TYPE_COMPONENT = 0;
    public static final int TYPE_REGISTERED = 1;
    public static final int TYPE_UNREGISTERED = 2;
    boolean mAbortBroadcast;
    boolean mFinished;
    final int mFlags;
    final boolean mInitialStickyHint;
    final boolean mOrderedHint;
    int mResultCode;
    String mResultData;
    Bundle mResultExtras;
    final int mSendingUser;
    final IBinder mToken;
    final int mType;
    
    public PendingResult(int paramInt1, String paramString, Bundle paramBundle, int paramInt2, boolean paramBoolean1, boolean paramBoolean2, IBinder paramIBinder, int paramInt3, int paramInt4)
    {
      mResultCode = paramInt1;
      mResultData = paramString;
      mResultExtras = paramBundle;
      mType = paramInt2;
      mOrderedHint = paramBoolean1;
      mInitialStickyHint = paramBoolean2;
      mToken = paramIBinder;
      mSendingUser = paramInt3;
      mFlags = paramInt4;
    }
    
    public final void abortBroadcast()
    {
      checkSynchronousHint();
      mAbortBroadcast = true;
    }
    
    void checkSynchronousHint()
    {
      if ((!mOrderedHint) && (!mInitialStickyHint))
      {
        RuntimeException localRuntimeException = new RuntimeException("BroadcastReceiver trying to return result during a non-ordered broadcast");
        localRuntimeException.fillInStackTrace();
        Log.e("BroadcastReceiver", localRuntimeException.getMessage(), localRuntimeException);
        return;
      }
    }
    
    public final void clearAbortBroadcast()
    {
      mAbortBroadcast = false;
    }
    
    public final void finish()
    {
      if (mType == 0)
      {
        final IActivityManager localIActivityManager = ActivityManager.getService();
        if (QueuedWork.hasPendingWork()) {
          QueuedWork.queue(new Runnable()
          {
            public void run()
            {
              sendFinished(localIActivityManager);
            }
          }, false);
        } else {
          sendFinished(localIActivityManager);
        }
      }
      else if ((mOrderedHint) && (mType != 2))
      {
        sendFinished(ActivityManager.getService());
      }
    }
    
    public final boolean getAbortBroadcast()
    {
      return mAbortBroadcast;
    }
    
    public final int getResultCode()
    {
      return mResultCode;
    }
    
    public final String getResultData()
    {
      return mResultData;
    }
    
    public final Bundle getResultExtras(boolean paramBoolean)
    {
      Bundle localBundle1 = mResultExtras;
      if (!paramBoolean) {
        return localBundle1;
      }
      Bundle localBundle2 = localBundle1;
      if (localBundle1 == null)
      {
        localBundle1 = new Bundle();
        localBundle2 = localBundle1;
        mResultExtras = localBundle1;
      }
      return localBundle2;
    }
    
    public int getSendingUserId()
    {
      return mSendingUser;
    }
    
    public void sendFinished(IActivityManager paramIActivityManager)
    {
      try
      {
        if (!mFinished)
        {
          mFinished = true;
          try
          {
            if (mResultExtras != null) {
              mResultExtras.setAllowFds(false);
            }
            if (mOrderedHint) {
              paramIActivityManager.finishReceiver(mToken, mResultCode, mResultData, mResultExtras, mAbortBroadcast, mFlags);
            } else {
              paramIActivityManager.finishReceiver(mToken, 0, null, null, false, mFlags);
            }
          }
          catch (RemoteException paramIActivityManager) {}
          return;
        }
        paramIActivityManager = new java/lang/IllegalStateException;
        paramIActivityManager.<init>("Broadcast already finished");
        throw paramIActivityManager;
      }
      finally {}
    }
    
    public void setExtrasClassLoader(ClassLoader paramClassLoader)
    {
      if (mResultExtras != null) {
        mResultExtras.setClassLoader(paramClassLoader);
      }
    }
    
    public final void setResult(int paramInt, String paramString, Bundle paramBundle)
    {
      checkSynchronousHint();
      mResultCode = paramInt;
      mResultData = paramString;
      mResultExtras = paramBundle;
    }
    
    public final void setResultCode(int paramInt)
    {
      checkSynchronousHint();
      mResultCode = paramInt;
    }
    
    public final void setResultData(String paramString)
    {
      checkSynchronousHint();
      mResultData = paramString;
    }
    
    public final void setResultExtras(Bundle paramBundle)
    {
      checkSynchronousHint();
      mResultExtras = paramBundle;
    }
  }
}
