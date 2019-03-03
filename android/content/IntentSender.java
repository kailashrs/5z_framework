package android.content;

import android.app.ActivityManager;
import android.app.IActivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.AndroidException;

public class IntentSender
  implements Parcelable
{
  public static final Parcelable.Creator<IntentSender> CREATOR = new Parcelable.Creator()
  {
    public IntentSender createFromParcel(Parcel paramAnonymousParcel)
    {
      paramAnonymousParcel = paramAnonymousParcel.readStrongBinder();
      if (paramAnonymousParcel != null) {
        paramAnonymousParcel = new IntentSender(paramAnonymousParcel);
      } else {
        paramAnonymousParcel = null;
      }
      return paramAnonymousParcel;
    }
    
    public IntentSender[] newArray(int paramAnonymousInt)
    {
      return new IntentSender[paramAnonymousInt];
    }
  };
  private final IIntentSender mTarget;
  IBinder mWhitelistToken;
  
  public IntentSender(IIntentSender paramIIntentSender)
  {
    mTarget = paramIIntentSender;
  }
  
  public IntentSender(IIntentSender paramIIntentSender, IBinder paramIBinder)
  {
    mTarget = paramIIntentSender;
    mWhitelistToken = paramIBinder;
  }
  
  public IntentSender(IBinder paramIBinder)
  {
    mTarget = IIntentSender.Stub.asInterface(paramIBinder);
  }
  
  public static IntentSender readIntentSenderOrNullFromParcel(Parcel paramParcel)
  {
    paramParcel = paramParcel.readStrongBinder();
    if (paramParcel != null) {
      paramParcel = new IntentSender(paramParcel);
    } else {
      paramParcel = null;
    }
    return paramParcel;
  }
  
  public static void writeIntentSenderOrNullToParcel(IntentSender paramIntentSender, Parcel paramParcel)
  {
    if (paramIntentSender != null) {
      paramIntentSender = mTarget.asBinder();
    } else {
      paramIntentSender = null;
    }
    paramParcel.writeStrongBinder(paramIntentSender);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof IntentSender)) {
      return mTarget.asBinder().equals(mTarget.asBinder());
    }
    return false;
  }
  
  public String getCreatorPackage()
  {
    try
    {
      String str = ActivityManager.getService().getPackageForIntentSender(mTarget);
      return str;
    }
    catch (RemoteException localRemoteException) {}
    return null;
  }
  
  public int getCreatorUid()
  {
    try
    {
      int i = ActivityManager.getService().getUidForIntentSender(mTarget);
      return i;
    }
    catch (RemoteException localRemoteException) {}
    return -1;
  }
  
  public UserHandle getCreatorUserHandle()
  {
    UserHandle localUserHandle = null;
    try
    {
      int i = ActivityManager.getService().getUidForIntentSender(mTarget);
      if (i > 0) {
        localUserHandle = new UserHandle(UserHandle.getUserId(i));
      }
      return localUserHandle;
    }
    catch (RemoteException localRemoteException) {}
    return null;
  }
  
  public IIntentSender getTarget()
  {
    return mTarget;
  }
  
  @Deprecated
  public String getTargetPackage()
  {
    try
    {
      String str = ActivityManager.getService().getPackageForIntentSender(mTarget);
      return str;
    }
    catch (RemoteException localRemoteException) {}
    return null;
  }
  
  public IBinder getWhitelistToken()
  {
    return mWhitelistToken;
  }
  
  public int hashCode()
  {
    return mTarget.asBinder().hashCode();
  }
  
  public void sendIntent(Context paramContext, int paramInt, Intent paramIntent, OnFinished paramOnFinished, Handler paramHandler)
    throws IntentSender.SendIntentException
  {
    sendIntent(paramContext, paramInt, paramIntent, paramOnFinished, paramHandler, null);
  }
  
  public void sendIntent(Context paramContext, int paramInt, Intent paramIntent, OnFinished paramOnFinished, Handler paramHandler, String paramString)
    throws IntentSender.SendIntentException
  {
    FinishedDispatcher localFinishedDispatcher = null;
    if (paramIntent != null) {
      try
      {
        paramContext = paramIntent.resolveTypeIfNeeded(paramContext.getContentResolver());
      }
      catch (RemoteException paramContext)
      {
        break label110;
      }
    } else {
      paramContext = null;
    }
    IActivityManager localIActivityManager = ActivityManager.getService();
    IIntentSender localIIntentSender = mTarget;
    IBinder localIBinder = mWhitelistToken;
    if (paramOnFinished != null)
    {
      localFinishedDispatcher = new android/content/IntentSender$FinishedDispatcher;
      try
      {
        localFinishedDispatcher.<init>(this, paramOnFinished, paramHandler);
        paramOnFinished = localFinishedDispatcher;
      }
      catch (RemoteException paramContext)
      {
        break label110;
      }
    }
    else
    {
      paramOnFinished = localFinishedDispatcher;
    }
    if (localIActivityManager.sendIntentSender(localIIntentSender, localIBinder, paramInt, paramIntent, paramContext, paramOnFinished, paramString, null) >= 0) {
      return;
    }
    paramContext = new android/content/IntentSender$SendIntentException;
    paramContext.<init>();
    throw paramContext;
    label110:
    throw new SendIntentException();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(128);
    localStringBuilder.append("IntentSender{");
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    localStringBuilder.append(": ");
    IBinder localIBinder;
    if (mTarget != null) {
      localIBinder = mTarget.asBinder();
    } else {
      localIBinder = null;
    }
    localStringBuilder.append(localIBinder);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeStrongBinder(mTarget.asBinder());
  }
  
  private static class FinishedDispatcher
    extends IIntentReceiver.Stub
    implements Runnable
  {
    private final Handler mHandler;
    private Intent mIntent;
    private final IntentSender mIntentSender;
    private int mResultCode;
    private String mResultData;
    private Bundle mResultExtras;
    private final IntentSender.OnFinished mWho;
    
    FinishedDispatcher(IntentSender paramIntentSender, IntentSender.OnFinished paramOnFinished, Handler paramHandler)
    {
      mIntentSender = paramIntentSender;
      mWho = paramOnFinished;
      mHandler = paramHandler;
    }
    
    public void performReceive(Intent paramIntent, int paramInt1, String paramString, Bundle paramBundle, boolean paramBoolean1, boolean paramBoolean2, int paramInt2)
    {
      mIntent = paramIntent;
      mResultCode = paramInt1;
      mResultData = paramString;
      mResultExtras = paramBundle;
      if (mHandler == null) {
        run();
      } else {
        mHandler.post(this);
      }
    }
    
    public void run()
    {
      mWho.onSendFinished(mIntentSender, mIntent, mResultCode, mResultData, mResultExtras);
    }
  }
  
  public static abstract interface OnFinished
  {
    public abstract void onSendFinished(IntentSender paramIntentSender, Intent paramIntent, int paramInt, String paramString, Bundle paramBundle);
  }
  
  public static class SendIntentException
    extends AndroidException
  {
    public SendIntentException() {}
    
    public SendIntentException(Exception paramException)
    {
      super();
    }
    
    public SendIntentException(String paramString)
    {
      super();
    }
  }
}
