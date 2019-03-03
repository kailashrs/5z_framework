package android.telephony.ims.compat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.ims.compat.feature.ImsFeature;
import android.telephony.ims.compat.feature.MMTelFeature;
import android.telephony.ims.compat.feature.RcsFeature;
import android.util.Log;
import android.util.SparseArray;
import com.android.ims.internal.IImsFeatureStatusCallback;
import com.android.ims.internal.IImsMMTelFeature;
import com.android.ims.internal.IImsRcsFeature;
import com.android.ims.internal.IImsServiceController.Stub;
import com.android.internal.annotations.VisibleForTesting;

public class ImsService
  extends Service
{
  private static final String LOG_TAG = "ImsService(Compat)";
  public static final String SERVICE_INTERFACE = "android.telephony.ims.compat.ImsService";
  private final SparseArray<SparseArray<ImsFeature>> mFeaturesBySlot = new SparseArray();
  protected final IBinder mImsServiceController = new IImsServiceController.Stub()
  {
    public IImsMMTelFeature createEmergencyMMTelFeature(int paramAnonymousInt, IImsFeatureStatusCallback paramAnonymousIImsFeatureStatusCallback)
    {
      return ImsService.this.createEmergencyMMTelFeatureInternal(paramAnonymousInt, paramAnonymousIImsFeatureStatusCallback);
    }
    
    public IImsMMTelFeature createMMTelFeature(int paramAnonymousInt, IImsFeatureStatusCallback paramAnonymousIImsFeatureStatusCallback)
    {
      return ImsService.this.createMMTelFeatureInternal(paramAnonymousInt, paramAnonymousIImsFeatureStatusCallback);
    }
    
    public IImsRcsFeature createRcsFeature(int paramAnonymousInt, IImsFeatureStatusCallback paramAnonymousIImsFeatureStatusCallback)
    {
      return ImsService.this.createRcsFeatureInternal(paramAnonymousInt, paramAnonymousIImsFeatureStatusCallback);
    }
    
    public void removeImsFeature(int paramAnonymousInt1, int paramAnonymousInt2, IImsFeatureStatusCallback paramAnonymousIImsFeatureStatusCallback)
      throws RemoteException
    {
      ImsService.this.removeImsFeature(paramAnonymousInt1, paramAnonymousInt2, paramAnonymousIImsFeatureStatusCallback);
    }
  };
  
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
  
  private IImsMMTelFeature createEmergencyMMTelFeatureInternal(int paramInt, IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
  {
    MMTelFeature localMMTelFeature = onCreateEmergencyMMTelImsFeature(paramInt);
    if (localMMTelFeature != null)
    {
      setupFeature(localMMTelFeature, paramInt, 0, paramIImsFeatureStatusCallback);
      return localMMTelFeature.getBinder();
    }
    return null;
  }
  
  private IImsMMTelFeature createMMTelFeatureInternal(int paramInt, IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
  {
    MMTelFeature localMMTelFeature = onCreateMMTelImsFeature(paramInt);
    if (localMMTelFeature != null)
    {
      setupFeature(localMMTelFeature, paramInt, 1, paramIImsFeatureStatusCallback);
      return localMMTelFeature.getBinder();
    }
    return null;
  }
  
  private IImsRcsFeature createRcsFeatureInternal(int paramInt, IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
  {
    RcsFeature localRcsFeature = onCreateRcsFeature(paramInt);
    if (localRcsFeature != null)
    {
      setupFeature(localRcsFeature, paramInt, 2, paramIImsFeatureStatusCallback);
      return localRcsFeature.getBinder();
    }
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
        Log.w("ImsService(Compat)", paramIImsFeatureStatusCallback.toString());
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
        Log.w("ImsService(Compat)", paramIImsFeatureStatusCallback.toString());
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
    paramImsFeature.setContext(this);
    paramImsFeature.setSlotId(paramInt1);
    paramImsFeature.addImsFeatureStatusCallback(paramIImsFeatureStatusCallback);
    addImsFeature(paramInt1, paramInt2, paramImsFeature);
    paramImsFeature.onFeatureReady();
  }
  
  @VisibleForTesting
  public SparseArray<ImsFeature> getFeatures(int paramInt)
  {
    return (SparseArray)mFeaturesBySlot.get(paramInt);
  }
  
  public IBinder onBind(Intent paramIntent)
  {
    if ("android.telephony.ims.compat.ImsService".equals(paramIntent.getAction()))
    {
      Log.i("ImsService(Compat)", "ImsService(Compat) Bound.");
      return mImsServiceController;
    }
    return null;
  }
  
  public MMTelFeature onCreateEmergencyMMTelImsFeature(int paramInt)
  {
    return null;
  }
  
  public MMTelFeature onCreateMMTelImsFeature(int paramInt)
  {
    return null;
  }
  
  public RcsFeature onCreateRcsFeature(int paramInt)
  {
    return null;
  }
}
