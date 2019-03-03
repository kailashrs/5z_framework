package android.telephony.ims.compat.feature;

import android.content.Context;
import android.content.Intent;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.Log;
import com.android.ims.internal.IImsFeatureStatusCallback;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;

public abstract class ImsFeature
{
  public static final String ACTION_IMS_SERVICE_DOWN = "com.android.ims.IMS_SERVICE_DOWN";
  public static final String ACTION_IMS_SERVICE_UP = "com.android.ims.IMS_SERVICE_UP";
  public static final int EMERGENCY_MMTEL = 0;
  public static final String EXTRA_PHONE_ID = "android:phone_id";
  public static final int INVALID = -1;
  private static final String LOG_TAG = "ImsFeature";
  public static final int MAX = 3;
  public static final int MMTEL = 1;
  public static final int RCS = 2;
  public static final int STATE_INITIALIZING = 1;
  public static final int STATE_NOT_AVAILABLE = 0;
  public static final int STATE_READY = 2;
  protected Context mContext;
  private int mSlotId = -1;
  private int mState = 0;
  private final Set<IImsFeatureStatusCallback> mStatusCallbacks = Collections.newSetFromMap(new WeakHashMap());
  
  public ImsFeature() {}
  
  private void notifyFeatureState(int paramInt)
  {
    synchronized (mStatusCallbacks)
    {
      Iterator localIterator = mStatusCallbacks.iterator();
      while (localIterator.hasNext())
      {
        IImsFeatureStatusCallback localIImsFeatureStatusCallback = (IImsFeatureStatusCallback)localIterator.next();
        try
        {
          localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("notifying ImsFeatureState=");
          localStringBuilder.append(paramInt);
          Log.i("ImsFeature", localStringBuilder.toString());
          localIImsFeatureStatusCallback.notifyImsFeatureStatus(paramInt);
        }
        catch (RemoteException localRemoteException)
        {
          localIterator.remove();
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Couldn't notify feature state: ");
          localStringBuilder.append(localRemoteException.getMessage());
          Log.w("ImsFeature", localStringBuilder.toString());
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
  
  public void addImsFeatureStatusCallback(IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
  {
    if (paramIImsFeatureStatusCallback == null) {
      return;
    }
    try
    {
      paramIImsFeatureStatusCallback.notifyImsFeatureStatus(mState);
      synchronized (mStatusCallbacks)
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
  
  public abstract IInterface getBinder();
  
  public int getFeatureState()
  {
    return mState;
  }
  
  public abstract void onFeatureReady();
  
  public abstract void onFeatureRemoved();
  
  public void removeImsFeatureStatusCallback(IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
  {
    if (paramIImsFeatureStatusCallback == null) {
      return;
    }
    synchronized (mStatusCallbacks)
    {
      mStatusCallbacks.remove(paramIImsFeatureStatusCallback);
      return;
    }
  }
  
  public void setContext(Context paramContext)
  {
    mContext = paramContext;
  }
  
  protected final void setFeatureState(int paramInt)
  {
    if (mState != paramInt)
    {
      mState = paramInt;
      notifyFeatureState(paramInt);
    }
  }
  
  public void setSlotId(int paramInt)
  {
    mSlotId = paramInt;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ImsState {}
}
