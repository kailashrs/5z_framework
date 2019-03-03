package android.app;

import android.content.Context;
import android.content.IIntentReceiver.Stub;
import android.content.IIntentSender;
import android.content.IIntentSender.Stub;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Build.FEATURES;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.AndroidException;
import android.util.ArraySet;
import android.util.proto.ProtoOutputStream;
import com.android.internal.os.IResultReceiver;
import com.android.internal.os.IResultReceiver.Stub;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class PendingIntent
  implements Parcelable
{
  public static final Parcelable.Creator<PendingIntent> CREATOR = new Parcelable.Creator()
  {
    public PendingIntent createFromParcel(Parcel paramAnonymousParcel)
    {
      IBinder localIBinder = paramAnonymousParcel.readStrongBinder();
      if (localIBinder != null) {
        paramAnonymousParcel = new PendingIntent(localIBinder, paramAnonymousParcel.getClassCookie(PendingIntent.class));
      } else {
        paramAnonymousParcel = null;
      }
      return paramAnonymousParcel;
    }
    
    public PendingIntent[] newArray(int paramAnonymousInt)
    {
      return new PendingIntent[paramAnonymousInt];
    }
  };
  public static final int FLAG_CANCEL_CURRENT = 268435456;
  public static final int FLAG_IMMUTABLE = 67108864;
  public static final int FLAG_NO_CREATE = 536870912;
  public static final int FLAG_ONE_SHOT = 1073741824;
  public static final int FLAG_UPDATE_CURRENT = 134217728;
  private static final ThreadLocal<OnMarshaledListener> sOnMarshaledListener = new ThreadLocal();
  private ArraySet<CancelListener> mCancelListeners;
  private IResultReceiver mCancelReceiver;
  private final IIntentSender mTarget;
  private IBinder mWhitelistToken;
  
  PendingIntent(IIntentSender paramIIntentSender)
  {
    mTarget = paramIIntentSender;
  }
  
  PendingIntent(IBinder paramIBinder, Object paramObject)
  {
    mTarget = IIntentSender.Stub.asInterface(paramIBinder);
    if (paramObject != null) {
      mWhitelistToken = ((IBinder)paramObject);
    }
  }
  
  private static PendingIntent buildServicePendingIntent(Context paramContext, int paramInt1, Intent paramIntent, int paramInt2, int paramInt3)
  {
    String str = paramContext.getPackageName();
    Object localObject1 = null;
    Object localObject2;
    if (paramIntent != null) {
      localObject2 = paramIntent.resolveTypeIfNeeded(paramContext.getContentResolver());
    } else {
      localObject2 = null;
    }
    try
    {
      paramIntent.prepareToLeaveProcess(paramContext);
      IActivityManager localIActivityManager = ActivityManager.getService();
      if (localObject2 != null) {
        localObject2 = new String[] { localObject2 };
      } else {
        localObject2 = null;
      }
      int i = paramContext.getUserId();
      paramIntent = localIActivityManager.getIntentSender(paramInt3, str, null, null, paramInt1, new Intent[] { paramIntent }, (String[])localObject2, paramInt2, null, i);
      paramContext = localObject1;
      if (paramIntent != null) {
        paramContext = new PendingIntent(paramIntent);
      }
      return paramContext;
    }
    catch (RemoteException paramContext)
    {
      throw paramContext.rethrowFromSystemServer();
    }
  }
  
  public static PendingIntent getActivities(Context paramContext, int paramInt1, Intent[] paramArrayOfIntent, int paramInt2)
  {
    return getActivities(paramContext, paramInt1, paramArrayOfIntent, paramInt2, null);
  }
  
  public static PendingIntent getActivities(Context paramContext, int paramInt1, Intent[] paramArrayOfIntent, int paramInt2, Bundle paramBundle)
  {
    String str = paramContext.getPackageName();
    String[] arrayOfString = new String[paramArrayOfIntent.length];
    for (int i = 0; i < paramArrayOfIntent.length; i++)
    {
      paramArrayOfIntent[i].migrateExtraStreamToClipData();
      paramArrayOfIntent[i].prepareToLeaveProcess(paramContext);
      arrayOfString[i] = paramArrayOfIntent[i].resolveTypeIfNeeded(paramContext.getContentResolver());
    }
    try
    {
      paramContext = ActivityManager.getService().getIntentSender(2, str, null, null, paramInt1, paramArrayOfIntent, arrayOfString, paramInt2, paramBundle, paramContext.getUserId());
      if (paramContext != null) {
        paramContext = new PendingIntent(paramContext);
      } else {
        paramContext = null;
      }
      return paramContext;
    }
    catch (RemoteException paramContext)
    {
      throw paramContext.rethrowFromSystemServer();
    }
  }
  
  public static PendingIntent getActivitiesAsUser(Context paramContext, int paramInt1, Intent[] paramArrayOfIntent, int paramInt2, Bundle paramBundle, UserHandle paramUserHandle)
  {
    String str = paramContext.getPackageName();
    String[] arrayOfString = new String[paramArrayOfIntent.length];
    for (int i = 0; i < paramArrayOfIntent.length; i++)
    {
      paramArrayOfIntent[i].migrateExtraStreamToClipData();
      paramArrayOfIntent[i].prepareToLeaveProcess(paramContext);
      arrayOfString[i] = paramArrayOfIntent[i].resolveTypeIfNeeded(paramContext.getContentResolver());
    }
    try
    {
      paramContext = ActivityManager.getService().getIntentSender(2, str, null, null, paramInt1, paramArrayOfIntent, arrayOfString, paramInt2, paramBundle, paramUserHandle.getIdentifier());
      if (paramContext != null) {
        paramContext = new PendingIntent(paramContext);
      } else {
        paramContext = null;
      }
      return paramContext;
    }
    catch (RemoteException paramContext)
    {
      throw paramContext.rethrowFromSystemServer();
    }
  }
  
  public static PendingIntent getActivity(Context paramContext, int paramInt1, Intent paramIntent, int paramInt2)
  {
    return getActivity(paramContext, paramInt1, paramIntent, paramInt2, null);
  }
  
  public static PendingIntent getActivity(Context paramContext, int paramInt1, Intent paramIntent, int paramInt2, Bundle paramBundle)
  {
    String str = paramContext.getPackageName();
    Object localObject1 = null;
    Object localObject2;
    if (paramIntent != null) {
      localObject2 = paramIntent.resolveTypeIfNeeded(paramContext.getContentResolver());
    } else {
      localObject2 = null;
    }
    try
    {
      paramIntent.migrateExtraStreamToClipData();
      try
      {
        paramIntent.prepareToLeaveProcess(paramContext);
        IActivityManager localIActivityManager = ActivityManager.getService();
        if (localObject2 != null) {
          localObject2 = new String[] { localObject2 };
        } else {
          localObject2 = null;
        }
        int i = paramContext.getUserId();
        paramIntent = localIActivityManager.getIntentSender(2, str, null, null, paramInt1, new Intent[] { paramIntent }, (String[])localObject2, paramInt2, paramBundle, i);
        paramContext = localObject1;
        if (paramIntent != null) {
          paramContext = new PendingIntent(paramIntent);
        }
        return paramContext;
      }
      catch (RemoteException paramContext) {}
      throw paramContext.rethrowFromSystemServer();
    }
    catch (RemoteException paramContext) {}
  }
  
  public static PendingIntent getActivityAsUser(Context paramContext, int paramInt1, Intent paramIntent, int paramInt2, Bundle paramBundle, UserHandle paramUserHandle)
  {
    String str1 = paramContext.getPackageName();
    Object localObject = null;
    String str2;
    if (paramIntent != null) {
      str2 = paramIntent.resolveTypeIfNeeded(paramContext.getContentResolver());
    } else {
      str2 = null;
    }
    try
    {
      paramIntent.migrateExtraStreamToClipData();
      try
      {
        paramIntent.prepareToLeaveProcess(paramContext);
        IActivityManager localIActivityManager = ActivityManager.getService();
        if (str2 != null) {
          paramContext = new String[] { str2 };
        } else {
          paramContext = null;
        }
        int i = paramUserHandle.getIdentifier();
        paramIntent = localIActivityManager.getIntentSender(2, str1, null, null, paramInt1, new Intent[] { paramIntent }, paramContext, paramInt2, paramBundle, i);
        paramContext = localObject;
        if (paramIntent != null) {
          paramContext = new PendingIntent(paramIntent);
        }
        return paramContext;
      }
      catch (RemoteException paramContext) {}
      throw paramContext.rethrowFromSystemServer();
    }
    catch (RemoteException paramContext) {}
  }
  
  public static PendingIntent getBroadcast(Context paramContext, int paramInt1, Intent paramIntent, int paramInt2)
  {
    return getBroadcastAsUser(paramContext, paramInt1, paramIntent, paramInt2, paramContext.getUser());
  }
  
  public static PendingIntent getBroadcastAsUser(Context paramContext, int paramInt1, Intent paramIntent, int paramInt2, UserHandle paramUserHandle)
  {
    String str1 = paramContext.getPackageName();
    Object localObject = null;
    String str2;
    if (paramIntent != null) {
      str2 = paramIntent.resolveTypeIfNeeded(paramContext.getContentResolver());
    } else {
      str2 = null;
    }
    try
    {
      paramIntent.prepareToLeaveProcess(paramContext);
      IActivityManager localIActivityManager = ActivityManager.getService();
      if (str2 != null) {
        paramContext = new String[] { str2 };
      } else {
        paramContext = null;
      }
      int i = paramUserHandle.getIdentifier();
      paramIntent = localIActivityManager.getIntentSender(1, str1, null, null, paramInt1, new Intent[] { paramIntent }, paramContext, paramInt2, null, i);
      paramContext = localObject;
      if (paramIntent != null) {
        paramContext = new PendingIntent(paramIntent);
      }
      return paramContext;
    }
    catch (RemoteException paramContext)
    {
      throw paramContext.rethrowFromSystemServer();
    }
  }
  
  public static PendingIntent getForegroundService(Context paramContext, int paramInt1, Intent paramIntent, int paramInt2)
  {
    return buildServicePendingIntent(paramContext, paramInt1, paramIntent, paramInt2, 5);
  }
  
  public static PendingIntent getService(Context paramContext, int paramInt1, Intent paramIntent, int paramInt2)
  {
    return buildServicePendingIntent(paramContext, paramInt1, paramIntent, paramInt2, 4);
  }
  
  private void notifyCancelListeners()
  {
    try
    {
      ArraySet localArraySet = new android/util/ArraySet;
      localArraySet.<init>(mCancelListeners);
      int i = localArraySet.size();
      for (int j = 0; j < i; j++) {
        ((CancelListener)localArraySet.valueAt(j)).onCancelled(this);
      }
      return;
    }
    finally {}
  }
  
  public static PendingIntent readPendingIntentOrNullFromParcel(Parcel paramParcel)
  {
    IBinder localIBinder = paramParcel.readStrongBinder();
    if (localIBinder != null) {
      paramParcel = new PendingIntent(localIBinder, paramParcel.getClassCookie(PendingIntent.class));
    } else {
      paramParcel = null;
    }
    return paramParcel;
  }
  
  public static void setOnMarshaledListener(OnMarshaledListener paramOnMarshaledListener)
  {
    sOnMarshaledListener.set(paramOnMarshaledListener);
  }
  
  public static void writePendingIntentOrNullToParcel(PendingIntent paramPendingIntent, Parcel paramParcel)
  {
    Object localObject;
    if (paramPendingIntent != null) {
      localObject = mTarget.asBinder();
    } else {
      localObject = null;
    }
    paramParcel.writeStrongBinder((IBinder)localObject);
    if (paramPendingIntent != null)
    {
      localObject = (OnMarshaledListener)sOnMarshaledListener.get();
      if (localObject != null) {
        ((OnMarshaledListener)localObject).onMarshaled(paramPendingIntent, paramParcel, 0);
      }
    }
  }
  
  public void cancel()
  {
    try
    {
      ActivityManager.getService().cancelIntentSender(mTarget);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof PendingIntent)) {
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
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getCreatorUid()
  {
    try
    {
      int i = ActivityManager.getService().getUidForIntentSender(mTarget);
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public UserHandle getCreatorUserHandle()
  {
    try
    {
      int i = ActivityManager.getService().getUidForIntentSender(mTarget);
      UserHandle localUserHandle;
      if (i > 0) {
        localUserHandle = new UserHandle(UserHandle.getUserId(i));
      } else {
        localUserHandle = null;
      }
      return localUserHandle;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public Intent getIntent()
  {
    try
    {
      Intent localIntent = ActivityManager.getService().getIntentForIntentSender(mTarget);
      return localIntent;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public IntentSender getIntentSender()
  {
    return new IntentSender(mTarget, mWhitelistToken);
  }
  
  public String getTag(String paramString)
  {
    try
    {
      paramString = ActivityManager.getService().getTagForIntentSender(mTarget, paramString);
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
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
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public IBinder getWhitelistToken()
  {
    return mWhitelistToken;
  }
  
  public int hashCode()
  {
    return mTarget.asBinder().hashCode();
  }
  
  public boolean isActivity()
  {
    try
    {
      boolean bool = ActivityManager.getService().isIntentSenderAnActivity(mTarget);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isForegroundService()
  {
    try
    {
      boolean bool = ActivityManager.getService().isIntentSenderAForegroundService(mTarget);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isTargetedToPackage()
  {
    try
    {
      boolean bool = ActivityManager.getService().isIntentSenderTargetedToPackage(mTarget);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void registerCancelListener(CancelListener paramCancelListener)
  {
    try
    {
      Object localObject;
      if (mCancelReceiver == null)
      {
        localObject = new android/app/PendingIntent$1;
        ((1)localObject).<init>(this);
        mCancelReceiver = ((IResultReceiver)localObject);
      }
      if (mCancelListeners == null)
      {
        localObject = new android/util/ArraySet;
        ((ArraySet)localObject).<init>();
        mCancelListeners = ((ArraySet)localObject);
      }
      boolean bool = mCancelListeners.isEmpty();
      mCancelListeners.add(paramCancelListener);
      if (bool) {
        try
        {
          ActivityManager.getService().registerIntentSenderCancelListener(mTarget, mCancelReceiver);
        }
        catch (RemoteException paramCancelListener)
        {
          throw paramCancelListener.rethrowFromSystemServer();
        }
      }
      return;
    }
    finally {}
  }
  
  public void send()
    throws PendingIntent.CanceledException
  {
    send(null, 0, null, null, null, null, null);
  }
  
  public void send(int paramInt)
    throws PendingIntent.CanceledException
  {
    send(null, paramInt, null, null, null, null, null);
  }
  
  public void send(int paramInt, OnFinished paramOnFinished, Handler paramHandler)
    throws PendingIntent.CanceledException
  {
    send(null, paramInt, null, paramOnFinished, paramHandler, null, null);
  }
  
  public void send(Context paramContext, int paramInt, Intent paramIntent)
    throws PendingIntent.CanceledException
  {
    send(paramContext, paramInt, paramIntent, null, null, null, null);
  }
  
  public void send(Context paramContext, int paramInt, Intent paramIntent, OnFinished paramOnFinished, Handler paramHandler)
    throws PendingIntent.CanceledException
  {
    send(paramContext, paramInt, paramIntent, paramOnFinished, paramHandler, null, null);
  }
  
  public void send(Context paramContext, int paramInt, Intent paramIntent, OnFinished paramOnFinished, Handler paramHandler, String paramString)
    throws PendingIntent.CanceledException
  {
    send(paramContext, paramInt, paramIntent, paramOnFinished, paramHandler, paramString, null);
  }
  
  public void send(Context paramContext, int paramInt, Intent paramIntent, OnFinished paramOnFinished, Handler paramHandler, String paramString, Bundle paramBundle)
    throws PendingIntent.CanceledException
  {
    if (sendAndReturnResult(paramContext, paramInt, paramIntent, paramOnFinished, paramHandler, paramString, paramBundle) >= 0) {
      return;
    }
    throw new CanceledException();
  }
  
  public int sendAndReturnResult(Context paramContext, int paramInt, Intent paramIntent, OnFinished paramOnFinished, Handler paramHandler, String paramString, Bundle paramBundle)
    throws PendingIntent.CanceledException
  {
    Object localObject1 = null;
    String str;
    if (paramIntent != null) {
      try
      {
        str = paramIntent.resolveTypeIfNeeded(paramContext.getContentResolver());
      }
      catch (RemoteException paramContext)
      {
        break label143;
      }
    } else {
      str = null;
    }
    Object localObject2 = ActivityThread.currentPackageName();
    paramContext = paramIntent;
    if (Build.FEATURES.ENABLE_AUTO_START)
    {
      paramContext = paramIntent;
      if ("com.android.systemui".equals(localObject2))
      {
        paramContext = paramIntent;
        if (paramIntent == null)
        {
          paramContext = new android/content/Intent;
          paramContext.<init>();
        }
        paramContext.addFlags(Integer.MIN_VALUE);
      }
    }
    IActivityManager localIActivityManager = ActivityManager.getService();
    IIntentSender localIIntentSender = mTarget;
    localObject2 = mWhitelistToken;
    if (paramOnFinished != null)
    {
      paramIntent = new android/app/PendingIntent$FinishedDispatcher;
      try
      {
        paramIntent.<init>(this, paramOnFinished, paramHandler);
      }
      catch (RemoteException paramContext)
      {
        break label143;
      }
    }
    else
    {
      paramIntent = localObject1;
    }
    paramInt = localIActivityManager.sendIntentSender(localIIntentSender, (IBinder)localObject2, paramInt, paramContext, str, paramIntent, paramString, paramBundle);
    return paramInt;
    label143:
    throw new CanceledException(paramContext);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(128);
    localStringBuilder.append("PendingIntent{");
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
  
  public void unregisterCancelListener(CancelListener paramCancelListener)
  {
    try
    {
      if (mCancelListeners == null) {
        return;
      }
      boolean bool1 = mCancelListeners.isEmpty();
      mCancelListeners.remove(paramCancelListener);
      boolean bool2 = mCancelListeners.isEmpty();
      if ((bool2) && (!bool1)) {
        try
        {
          ActivityManager.getService().unregisterIntentSenderCancelListener(mTarget, mCancelReceiver);
        }
        catch (RemoteException paramCancelListener)
        {
          throw paramCancelListener.rethrowFromSystemServer();
        }
      }
      return;
    }
    finally {}
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeStrongBinder(mTarget.asBinder());
    OnMarshaledListener localOnMarshaledListener = (OnMarshaledListener)sOnMarshaledListener.get();
    if (localOnMarshaledListener != null) {
      localOnMarshaledListener.onMarshaled(this, paramParcel, paramInt);
    }
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    if (mTarget != null) {
      paramProtoOutputStream.write(1138166333441L, mTarget.asBinder().toString());
    }
    paramProtoOutputStream.end(paramLong);
  }
  
  public static abstract interface CancelListener
  {
    public abstract void onCancelled(PendingIntent paramPendingIntent);
  }
  
  public static class CanceledException
    extends AndroidException
  {
    public CanceledException() {}
    
    public CanceledException(Exception paramException)
    {
      super();
    }
    
    public CanceledException(String paramString)
    {
      super();
    }
  }
  
  private static class FinishedDispatcher
    extends IIntentReceiver.Stub
    implements Runnable
  {
    private static Handler sDefaultSystemHandler;
    private final Handler mHandler;
    private Intent mIntent;
    private final PendingIntent mPendingIntent;
    private int mResultCode;
    private String mResultData;
    private Bundle mResultExtras;
    private final PendingIntent.OnFinished mWho;
    
    FinishedDispatcher(PendingIntent paramPendingIntent, PendingIntent.OnFinished paramOnFinished, Handler paramHandler)
    {
      mPendingIntent = paramPendingIntent;
      mWho = paramOnFinished;
      if ((paramHandler == null) && (ActivityThread.isSystem()))
      {
        if (sDefaultSystemHandler == null) {
          sDefaultSystemHandler = new Handler(Looper.getMainLooper());
        }
        mHandler = sDefaultSystemHandler;
      }
      else
      {
        mHandler = paramHandler;
      }
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
      mWho.onSendFinished(mPendingIntent, mIntent, mResultCode, mResultData, mResultExtras);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Flags {}
  
  public static abstract interface OnFinished
  {
    public abstract void onSendFinished(PendingIntent paramPendingIntent, Intent paramIntent, int paramInt, String paramString, Bundle paramBundle);
  }
  
  public static abstract interface OnMarshaledListener
  {
    public abstract void onMarshaled(PendingIntent paramPendingIntent, Parcel paramParcel, int paramInt);
  }
}
