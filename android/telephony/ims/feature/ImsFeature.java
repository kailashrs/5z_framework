package android.telephony.ims.feature;

import android.annotation.SystemApi;
import android.content.Context;
import android.content.Intent;
import android.os.IInterface;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.telephony.ims.aidl.IImsCapabilityCallback;
import android.telephony.ims.aidl.IImsCapabilityCallback.Stub;
import android.util.Log;
import com.android.ims.internal.IImsFeatureStatusCallback;
import com.android.internal.annotations.VisibleForTesting;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;

@SystemApi
public abstract class ImsFeature
{
  public static final String ACTION_IMS_SERVICE_DOWN = "com.android.ims.IMS_SERVICE_DOWN";
  public static final String ACTION_IMS_SERVICE_UP = "com.android.ims.IMS_SERVICE_UP";
  public static final int CAPABILITY_ERROR_GENERIC = -1;
  public static final int CAPABILITY_SUCCESS = 0;
  public static final String EXTRA_PHONE_ID = "android:phone_id";
  public static final int FEATURE_EMERGENCY_MMTEL = 0;
  public static final int FEATURE_INVALID = -1;
  public static final int FEATURE_MAX = 3;
  public static final int FEATURE_MMTEL = 1;
  public static final int FEATURE_RCS = 2;
  private static final String LOG_TAG = "ImsFeature";
  public static final int STATE_INITIALIZING = 1;
  public static final int STATE_READY = 2;
  public static final int STATE_UNAVAILABLE = 0;
  private final RemoteCallbackList<IImsCapabilityCallback> mCapabilityCallbacks = new RemoteCallbackList();
  private Capabilities mCapabilityStatus = new Capabilities();
  protected Context mContext;
  protected final Object mLock = new Object();
  private int mSlotId = -1;
  private int mState = 0;
  private final Set<IImsFeatureStatusCallback> mStatusCallbacks = Collections.newSetFromMap(new WeakHashMap());
  
  public ImsFeature() {}
  
  private void notifyFeatureState(int paramInt)
  {
    synchronized (mLock)
    {
      Iterator localIterator = mStatusCallbacks.iterator();
      while (localIterator.hasNext())
      {
        Object localObject3 = (IImsFeatureStatusCallback)localIterator.next();
        try
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("notifying ImsFeatureState=");
          localStringBuilder.append(paramInt);
          Log.i("ImsFeature", localStringBuilder.toString());
          ((IImsFeatureStatusCallback)localObject3).notifyImsFeatureStatus(paramInt);
        }
        catch (RemoteException localRemoteException)
        {
          localIterator.remove();
          localObject3 = new java/lang/StringBuilder;
          ((StringBuilder)localObject3).<init>();
          ((StringBuilder)localObject3).append("Couldn't notify feature state: ");
          ((StringBuilder)localObject3).append(localRemoteException.getMessage());
          Log.w("ImsFeature", ((StringBuilder)localObject3).toString());
        }
      }
      sendImsServiceIntent(paramInt);
      return;
    }
  }
  
  private void sendImsServiceIntent(int paramInt)
  {
    if ((mContext != null) && (mSlotId != -1))
    {
      Intent localIntent;
      switch (paramInt)
      {
      default: 
        localIntent = new Intent("com.android.ims.IMS_SERVICE_DOWN");
        break;
      case 2: 
        localIntent = new Intent("com.android.ims.IMS_SERVICE_UP");
        break;
      case 0: 
      case 1: 
        localIntent = new Intent("com.android.ims.IMS_SERVICE_DOWN");
      }
      localIntent.putExtra("android:phone_id", mSlotId);
      mContext.sendBroadcast(localIntent);
      return;
    }
  }
  
  public final void addCapabilityCallback(IImsCapabilityCallback paramIImsCapabilityCallback)
  {
    mCapabilityCallbacks.register(paramIImsCapabilityCallback);
  }
  
  @VisibleForTesting
  public void addImsFeatureStatusCallback(IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
  {
    try
    {
      paramIImsFeatureStatusCallback.notifyImsFeatureStatus(getFeatureState());
      synchronized (mLock)
      {
        mStatusCallbacks.add(paramIImsFeatureStatusCallback);
      }
      return;
    }
    catch (RemoteException paramIImsFeatureStatusCallback)
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("Couldn't notify feature state: ");
      ((StringBuilder)???).append(paramIImsFeatureStatusCallback.getMessage());
      Log.w("ImsFeature", ((StringBuilder)???).toString());
    }
  }
  
  public abstract void changeEnabledCapabilities(CapabilityChangeRequest paramCapabilityChangeRequest, CapabilityCallbackProxy paramCapabilityCallbackProxy);
  
  protected abstract IInterface getBinder();
  
  public int getFeatureState()
  {
    synchronized (mLock)
    {
      int i = mState;
      return i;
    }
  }
  
  public final void initialize(Context paramContext, int paramInt)
  {
    mContext = paramContext;
    mSlotId = paramInt;
  }
  
  /* Error */
  protected final void notifyCapabilitiesStatusChanged(Capabilities paramCapabilities)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 72	android/telephony/ims/feature/ImsFeature:mLock	Ljava/lang/Object;
    //   4: astore_2
    //   5: aload_2
    //   6: monitorenter
    //   7: aload_0
    //   8: aload_1
    //   9: invokevirtual 204	android/telephony/ims/feature/ImsFeature$Capabilities:copy	()Landroid/telephony/ims/feature/ImsFeature$Capabilities;
    //   12: putfield 95	android/telephony/ims/feature/ImsFeature:mCapabilityStatus	Landroid/telephony/ims/feature/ImsFeature$Capabilities;
    //   15: aload_2
    //   16: monitorexit
    //   17: aload_0
    //   18: getfield 92	android/telephony/ims/feature/ImsFeature:mCapabilityCallbacks	Landroid/os/RemoteCallbackList;
    //   21: invokevirtual 207	android/os/RemoteCallbackList:beginBroadcast	()I
    //   24: istore_3
    //   25: iconst_0
    //   26: istore 4
    //   28: iload 4
    //   30: iload_3
    //   31: if_icmpge +83 -> 114
    //   34: aload_0
    //   35: getfield 92	android/telephony/ims/feature/ImsFeature:mCapabilityCallbacks	Landroid/os/RemoteCallbackList;
    //   38: iload 4
    //   40: invokevirtual 211	android/os/RemoteCallbackList:getBroadcastItem	(I)Landroid/os/IInterface;
    //   43: checkcast 213	android/telephony/ims/aidl/IImsCapabilityCallback
    //   46: aload_1
    //   47: getfield 216	android/telephony/ims/feature/ImsFeature$Capabilities:mCapabilities	I
    //   50: invokeinterface 219 2 0
    //   55: goto +44 -> 99
    //   58: astore_1
    //   59: goto +46 -> 105
    //   62: astore_2
    //   63: new 120	java/lang/StringBuilder
    //   66: astore 5
    //   68: aload 5
    //   70: invokespecial 121	java/lang/StringBuilder:<init>	()V
    //   73: aload 5
    //   75: aload_2
    //   76: invokevirtual 222	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   79: pop
    //   80: aload 5
    //   82: ldc -32
    //   84: invokevirtual 127	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   87: pop
    //   88: ldc 49
    //   90: aload 5
    //   92: invokevirtual 134	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   95: invokestatic 154	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   98: pop
    //   99: iinc 4 1
    //   102: goto -74 -> 28
    //   105: aload_0
    //   106: getfield 92	android/telephony/ims/feature/ImsFeature:mCapabilityCallbacks	Landroid/os/RemoteCallbackList;
    //   109: invokevirtual 227	android/os/RemoteCallbackList:finishBroadcast	()V
    //   112: aload_1
    //   113: athrow
    //   114: aload_0
    //   115: getfield 92	android/telephony/ims/feature/ImsFeature:mCapabilityCallbacks	Landroid/os/RemoteCallbackList;
    //   118: invokevirtual 227	android/os/RemoteCallbackList:finishBroadcast	()V
    //   121: return
    //   122: astore_1
    //   123: aload_2
    //   124: monitorexit
    //   125: aload_1
    //   126: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	127	0	this	ImsFeature
    //   0	127	1	paramCapabilities	Capabilities
    //   4	12	2	localObject	Object
    //   62	62	2	localRemoteException	RemoteException
    //   24	8	3	i	int
    //   26	74	4	j	int
    //   66	25	5	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   34	55	58	finally
    //   63	99	58	finally
    //   34	55	62	android/os/RemoteException
    //   7	17	122	finally
    //   123	125	122	finally
  }
  
  public abstract void onFeatureReady();
  
  public abstract void onFeatureRemoved();
  
  @VisibleForTesting
  public Capabilities queryCapabilityStatus()
  {
    synchronized (mLock)
    {
      Capabilities localCapabilities = mCapabilityStatus.copy();
      return localCapabilities;
    }
  }
  
  public final void removeCapabilityCallback(IImsCapabilityCallback paramIImsCapabilityCallback)
  {
    mCapabilityCallbacks.unregister(paramIImsCapabilityCallback);
  }
  
  @VisibleForTesting
  public void removeImsFeatureStatusCallback(IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
  {
    synchronized (mLock)
    {
      mStatusCallbacks.remove(paramIImsFeatureStatusCallback);
      return;
    }
  }
  
  @VisibleForTesting
  public final void requestChangeEnabledCapabilities(CapabilityChangeRequest paramCapabilityChangeRequest, IImsCapabilityCallback paramIImsCapabilityCallback)
  {
    if (paramCapabilityChangeRequest != null)
    {
      changeEnabledCapabilities(paramCapabilityChangeRequest, new CapabilityCallbackProxy(paramIImsCapabilityCallback));
      return;
    }
    throw new IllegalArgumentException("ImsFeature#requestChangeEnabledCapabilities called with invalid params.");
  }
  
  public final void setFeatureState(int paramInt)
  {
    synchronized (mLock)
    {
      if (mState != paramInt)
      {
        mState = paramInt;
        notifyFeatureState(paramInt);
      }
      return;
    }
  }
  
  public static class Capabilities
  {
    protected int mCapabilities = 0;
    
    public Capabilities() {}
    
    protected Capabilities(int paramInt)
    {
      mCapabilities = paramInt;
    }
    
    public void addCapabilities(int paramInt)
    {
      mCapabilities |= paramInt;
    }
    
    public Capabilities copy()
    {
      return new Capabilities(mCapabilities);
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool = true;
      if (this == paramObject) {
        return true;
      }
      if (!(paramObject instanceof Capabilities)) {
        return false;
      }
      paramObject = (Capabilities)paramObject;
      if (mCapabilities != mCapabilities) {
        bool = false;
      }
      return bool;
    }
    
    public int getMask()
    {
      return mCapabilities;
    }
    
    public int hashCode()
    {
      return mCapabilities;
    }
    
    public boolean isCapable(int paramInt)
    {
      boolean bool;
      if ((mCapabilities & paramInt) == paramInt) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void removeCapabilities(int paramInt)
    {
      mCapabilities &= paramInt;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Capabilities: ");
      localStringBuilder.append(Integer.toBinaryString(mCapabilities));
      return localStringBuilder.toString();
    }
  }
  
  public static class CapabilityCallback
    extends IImsCapabilityCallback.Stub
  {
    public CapabilityCallback() {}
    
    public final void onCapabilitiesStatusChanged(int paramInt)
      throws RemoteException
    {
      onCapabilitiesStatusChanged(new ImsFeature.Capabilities(paramInt));
    }
    
    public void onCapabilitiesStatusChanged(ImsFeature.Capabilities paramCapabilities) {}
    
    public void onChangeCapabilityConfigurationError(int paramInt1, int paramInt2, int paramInt3) {}
    
    public void onQueryCapabilityConfiguration(int paramInt1, int paramInt2, boolean paramBoolean) {}
  }
  
  protected static class CapabilityCallbackProxy
  {
    private final IImsCapabilityCallback mCallback;
    
    public CapabilityCallbackProxy(IImsCapabilityCallback paramIImsCapabilityCallback)
    {
      mCallback = paramIImsCapabilityCallback;
    }
    
    public void onChangeCapabilityConfigurationError(int paramInt1, int paramInt2, int paramInt3)
    {
      if (mCallback == null) {
        return;
      }
      try
      {
        mCallback.onChangeCapabilityConfigurationError(paramInt1, paramInt2, paramInt3);
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("ImsFeature", "onChangeCapabilityConfigurationError called on dead binder.");
      }
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface FeatureType {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ImsCapabilityError {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ImsState {}
}
