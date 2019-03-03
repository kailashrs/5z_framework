package android.telephony.ims;

import android.annotation.SystemApi;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.ims.aidl.IImsConfig;
import android.telephony.ims.aidl.IImsMmTelFeature;
import android.telephony.ims.aidl.IImsRcsFeature;
import android.telephony.ims.aidl.IImsRegistration;
import android.telephony.ims.aidl.IImsServiceController.Stub;
import android.telephony.ims.aidl.IImsServiceControllerListener;
import android.telephony.ims.aidl.IImsServiceControllerListener.Stub;
import android.telephony.ims.feature.ImsFeature;
import android.telephony.ims.feature.MmTelFeature;
import android.telephony.ims.feature.RcsFeature;
import android.telephony.ims.stub.ImsConfigImplBase;
import android.telephony.ims.stub.ImsFeatureConfiguration;
import android.telephony.ims.stub.ImsRegistrationImplBase;
import android.util.Log;
import android.util.SparseArray;
import com.android.ims.internal.IImsFeatureStatusCallback;
import com.android.internal.annotations.VisibleForTesting;

@SystemApi
public class ImsService
  extends Service
{
  private static final String LOG_TAG = "ImsService";
  public static final String SERVICE_INTERFACE = "android.telephony.ims.ImsService";
  private final SparseArray<SparseArray<ImsFeature>> mFeaturesBySlot = new SparseArray();
  protected final IBinder mImsServiceController = new IImsServiceController.Stub()
  {
    public IImsMmTelFeature createMmTelFeature(int paramAnonymousInt, IImsFeatureStatusCallback paramAnonymousIImsFeatureStatusCallback)
    {
      return ImsService.this.createMmTelFeatureInternal(paramAnonymousInt, paramAnonymousIImsFeatureStatusCallback);
    }
    
    public IImsRcsFeature createRcsFeature(int paramAnonymousInt, IImsFeatureStatusCallback paramAnonymousIImsFeatureStatusCallback)
    {
      return ImsService.this.createRcsFeatureInternal(paramAnonymousInt, paramAnonymousIImsFeatureStatusCallback);
    }
    
    public void disableIms(int paramAnonymousInt)
    {
      ImsService.this.disableIms(paramAnonymousInt);
    }
    
    public void enableIms(int paramAnonymousInt)
    {
      ImsService.this.enableIms(paramAnonymousInt);
    }
    
    public IImsConfig getConfig(int paramAnonymousInt)
    {
      Object localObject = getConfig(paramAnonymousInt);
      if (localObject != null) {
        localObject = ((ImsConfigImplBase)localObject).getIImsConfig();
      } else {
        localObject = null;
      }
      return localObject;
    }
    
    public IImsRegistration getRegistration(int paramAnonymousInt)
    {
      Object localObject = getRegistration(paramAnonymousInt);
      if (localObject != null) {
        localObject = ((ImsRegistrationImplBase)localObject).getBinder();
      } else {
        localObject = null;
      }
      return localObject;
    }
    
    public void notifyImsServiceReadyForFeatureCreation()
    {
      readyForFeatureCreation();
    }
    
    public ImsFeatureConfiguration querySupportedImsFeatures()
    {
      return ImsService.this.querySupportedImsFeatures();
    }
    
    public void removeImsFeature(int paramAnonymousInt1, int paramAnonymousInt2, IImsFeatureStatusCallback paramAnonymousIImsFeatureStatusCallback)
    {
      ImsService.this.removeImsFeature(paramAnonymousInt1, paramAnonymousInt2, paramAnonymousIImsFeatureStatusCallback);
    }
    
    public void setListener(IImsServiceControllerListener paramAnonymousIImsServiceControllerListener)
    {
      ImsService.access$002(ImsService.this, paramAnonymousIImsServiceControllerListener);
    }
  };
  private IImsServiceControllerListener mListener;
  
  public ImsService() {}
  
  private void addImsFeature(int paramInt1, int paramInt2, ImsFeature paramImsFeature)
  {
    synchronized (mFeaturesBySlot)
    {
      SparseArray localSparseArray2 = (SparseArray)mFeaturesBySlot.get(paramInt1);
      SparseArray localSparseArray3 = localSparseArray2;
      if (localSparseArray2 == null)
      {
        localSparseArray3 = new android/util/SparseArray;
        localSparseArray3.<init>();
        mFeaturesBySlot.put(paramInt1, localSparseArray3);
      }
      localSparseArray3.put(paramInt2, paramImsFeature);
      return;
    }
  }
  
  private IImsMmTelFeature createMmTelFeatureInternal(int paramInt, IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
  {
    MmTelFeature localMmTelFeature = createMmTelFeature(paramInt);
    if (localMmTelFeature != null)
    {
      setupFeature(localMmTelFeature, paramInt, 1, paramIImsFeatureStatusCallback);
      return localMmTelFeature.getBinder();
    }
    Log.e("ImsService", "createMmTelFeatureInternal: null feature returned.");
    return null;
  }
  
  private IImsRcsFeature createRcsFeatureInternal(int paramInt, IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
  {
    RcsFeature localRcsFeature = createRcsFeature(paramInt);
    if (localRcsFeature != null)
    {
      setupFeature(localRcsFeature, paramInt, 2, paramIImsFeatureStatusCallback);
      return localRcsFeature.getBinder();
    }
    Log.e("ImsService", "createRcsFeatureInternal: null feature returned.");
    return null;
  }
  
  private void removeImsFeature(int paramInt1, int paramInt2, IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
  {
    synchronized (mFeaturesBySlot)
    {
      SparseArray localSparseArray2 = (SparseArray)mFeaturesBySlot.get(paramInt1);
      if (localSparseArray2 == null)
      {
        paramIImsFeatureStatusCallback = new java/lang/StringBuilder;
        paramIImsFeatureStatusCallback.<init>();
        paramIImsFeatureStatusCallback.append("Can not remove ImsFeature. No ImsFeatures exist on slot ");
        paramIImsFeatureStatusCallback.append(paramInt1);
        Log.w("ImsService", paramIImsFeatureStatusCallback.toString());
        return;
      }
      ImsFeature localImsFeature = (ImsFeature)localSparseArray2.get(paramInt2);
      if (localImsFeature == null)
      {
        paramIImsFeatureStatusCallback = new java/lang/StringBuilder;
        paramIImsFeatureStatusCallback.<init>();
        paramIImsFeatureStatusCallback.append("Can not remove ImsFeature. No feature with type ");
        paramIImsFeatureStatusCallback.append(paramInt2);
        paramIImsFeatureStatusCallback.append(" exists on slot ");
        paramIImsFeatureStatusCallback.append(paramInt1);
        Log.w("ImsService", paramIImsFeatureStatusCallback.toString());
        return;
      }
      localImsFeature.removeImsFeatureStatusCallback(paramIImsFeatureStatusCallback);
      localImsFeature.onFeatureRemoved();
      localSparseArray2.remove(paramInt2);
      return;
    }
  }
  
  private void setupFeature(ImsFeature paramImsFeature, int paramInt1, int paramInt2, IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
  {
    paramImsFeature.addImsFeatureStatusCallback(paramIImsFeatureStatusCallback);
    paramImsFeature.initialize(this, paramInt1);
    addImsFeature(paramInt1, paramInt2, paramImsFeature);
  }
  
  public MmTelFeature createMmTelFeature(int paramInt)
  {
    return null;
  }
  
  public RcsFeature createRcsFeature(int paramInt)
  {
    return null;
  }
  
  public void disableIms(int paramInt) {}
  
  public void enableIms(int paramInt) {}
  
  public ImsConfigImplBase getConfig(int paramInt)
  {
    return new ImsConfigImplBase();
  }
  
  @VisibleForTesting
  public SparseArray<ImsFeature> getFeatures(int paramInt)
  {
    return (SparseArray)mFeaturesBySlot.get(paramInt);
  }
  
  public ImsRegistrationImplBase getRegistration(int paramInt)
  {
    return new ImsRegistrationImplBase();
  }
  
  public IBinder onBind(Intent paramIntent)
  {
    if ("android.telephony.ims.ImsService".equals(paramIntent.getAction()))
    {
      Log.i("ImsService", "ImsService Bound.");
      return mImsServiceController;
    }
    return null;
  }
  
  public final void onUpdateSupportedImsFeatures(ImsFeatureConfiguration paramImsFeatureConfiguration)
    throws RemoteException
  {
    if (mListener != null)
    {
      mListener.onUpdateSupportedImsFeatures(paramImsFeatureConfiguration);
      return;
    }
    throw new IllegalStateException("Framework is not ready");
  }
  
  public ImsFeatureConfiguration querySupportedImsFeatures()
  {
    return new ImsFeatureConfiguration();
  }
  
  public void readyForFeatureCreation() {}
  
  public static class Listener
    extends IImsServiceControllerListener.Stub
  {
    public Listener() {}
    
    public void onUpdateSupportedImsFeatures(ImsFeatureConfiguration paramImsFeatureConfiguration) {}
  }
}
