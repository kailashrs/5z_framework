package android.telephony.data;

import android.app.Service;
import android.content.Intent;
import android.net.LinkProperties;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.Rlog;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class DataService
  extends Service
{
  private static final int DATA_SERVICE_CREATE_DATA_SERVICE_PROVIDER = 1;
  public static final String DATA_SERVICE_EXTRA_SLOT_ID = "android.telephony.data.extra.SLOT_ID";
  private static final int DATA_SERVICE_INDICATION_DATA_CALL_LIST_CHANGED = 11;
  public static final String DATA_SERVICE_INTERFACE = "android.telephony.data.DataService";
  private static final int DATA_SERVICE_REMOVE_ALL_DATA_SERVICE_PROVIDERS = 3;
  private static final int DATA_SERVICE_REMOVE_DATA_SERVICE_PROVIDER = 2;
  private static final int DATA_SERVICE_REQUEST_DEACTIVATE_DATA_CALL = 5;
  private static final int DATA_SERVICE_REQUEST_GET_DATA_CALL_LIST = 8;
  private static final int DATA_SERVICE_REQUEST_REGISTER_DATA_CALL_LIST_CHANGED = 9;
  private static final int DATA_SERVICE_REQUEST_SETUP_DATA_CALL = 4;
  private static final int DATA_SERVICE_REQUEST_SET_DATA_PROFILE = 7;
  private static final int DATA_SERVICE_REQUEST_SET_INITIAL_ATTACH_APN = 6;
  private static final int DATA_SERVICE_REQUEST_UNREGISTER_DATA_CALL_LIST_CHANGED = 10;
  public static final int REQUEST_REASON_HANDOVER = 3;
  public static final int REQUEST_REASON_NORMAL = 1;
  public static final int REQUEST_REASON_SHUTDOWN = 2;
  private static final String TAG = DataService.class.getSimpleName();
  @VisibleForTesting
  public final IDataServiceWrapper mBinder = new IDataServiceWrapper(null);
  private final DataServiceHandler mHandler;
  private final HandlerThread mHandlerThread = new HandlerThread(TAG);
  private final SparseArray<DataServiceProvider> mServiceMap = new SparseArray();
  
  public DataService()
  {
    mHandlerThread.start();
    mHandler = new DataServiceHandler(mHandlerThread.getLooper());
    log("Data service created");
  }
  
  private void log(String paramString)
  {
    Rlog.d(TAG, paramString);
  }
  
  private void loge(String paramString)
  {
    Rlog.e(TAG, paramString);
  }
  
  public abstract DataServiceProvider createDataServiceProvider(int paramInt);
  
  public IBinder onBind(Intent paramIntent)
  {
    if ((paramIntent != null) && ("android.telephony.data.DataService".equals(paramIntent.getAction()))) {
      return mBinder;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unexpected intent ");
    localStringBuilder.append(paramIntent);
    loge(localStringBuilder.toString());
    return null;
  }
  
  public void onDestroy()
  {
    mHandlerThread.quit();
  }
  
  public boolean onUnbind(Intent paramIntent)
  {
    mHandler.obtainMessage(3).sendToTarget();
    return false;
  }
  
  private static final class DataCallListChangedIndication
  {
    public final IDataServiceCallback callback;
    public final List<DataCallResponse> dataCallList;
    
    DataCallListChangedIndication(List<DataCallResponse> paramList, IDataServiceCallback paramIDataServiceCallback)
    {
      dataCallList = paramList;
      callback = paramIDataServiceCallback;
    }
  }
  
  private class DataServiceHandler
    extends Handler
  {
    DataServiceHandler(Looper paramLooper)
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      int i = arg1;
      DataService.DataServiceProvider localDataServiceProvider = (DataService.DataServiceProvider)mServiceMap.get(i);
      int j = what;
      Object localObject1 = null;
      Object localObject2 = null;
      Object localObject3 = null;
      boolean bool1;
      DataProfile localDataProfile;
      switch (j)
      {
      default: 
        break;
      case 11: 
        if (localDataServiceProvider != null)
        {
          paramMessage = (DataService.DataCallListChangedIndication)obj;
          try
          {
            callback.onDataCallListChanged(dataCallList);
          }
          catch (RemoteException localRemoteException)
          {
            localObject1 = DataService.this;
            paramMessage = new StringBuilder();
            paramMessage.append("Failed to call onDataCallListChanged. ");
            paramMessage.append(localRemoteException);
            ((DataService)localObject1).loge(paramMessage.toString());
          }
        }
        break;
      case 10: 
        if (localDataServiceProvider != null) {
          DataService.DataServiceProvider.access$400(localDataServiceProvider, (IDataServiceCallback)obj);
        }
        break;
      case 9: 
        if (localDataServiceProvider != null) {
          DataService.DataServiceProvider.access$300(localDataServiceProvider, (IDataServiceCallback)obj);
        }
        break;
      case 8: 
        if (localDataServiceProvider != null) {
          localDataServiceProvider.getDataCallList(new DataServiceCallback((IDataServiceCallback)obj));
        }
        break;
      case 7: 
        if (localDataServiceProvider != null)
        {
          paramMessage = (DataService.SetDataProfileRequest)obj;
          localObject1 = dps;
          bool1 = isRoaming;
          if (callback != null) {
            paramMessage = new DataServiceCallback(callback);
          } else {
            paramMessage = localRemoteException;
          }
          localDataServiceProvider.setDataProfile((List)localObject1, bool1, paramMessage);
        }
        break;
      case 6: 
        if (localDataServiceProvider != null)
        {
          paramMessage = (DataService.SetInitialAttachApnRequest)obj;
          localDataProfile = dataProfile;
          bool1 = isRoaming;
          if (callback != null) {
            paramMessage = new DataServiceCallback(callback);
          } else {
            paramMessage = (Message)localObject1;
          }
          localDataServiceProvider.setInitialAttachApn(localDataProfile, bool1, paramMessage);
        }
        break;
      case 5: 
        if (localDataServiceProvider != null)
        {
          paramMessage = (DataService.DeactivateDataCallRequest)obj;
          j = cid;
          i = reason;
          if (callback != null) {
            paramMessage = new DataServiceCallback(callback);
          } else {
            paramMessage = localObject2;
          }
          localDataServiceProvider.deactivateDataCall(j, i, paramMessage);
        }
        break;
      case 4: 
        if (localDataServiceProvider != null)
        {
          paramMessage = (DataService.SetupDataCallRequest)obj;
          i = accessNetworkType;
          localDataProfile = dataProfile;
          bool1 = isRoaming;
          boolean bool2 = allowRoaming;
          j = reason;
          localObject1 = linkProperties;
          if (callback != null) {
            paramMessage = new DataServiceCallback(callback);
          } else {
            paramMessage = null;
          }
          localDataServiceProvider.setupDataCall(i, localDataProfile, bool1, bool2, j, (LinkProperties)localObject1, paramMessage);
        }
        break;
      case 3: 
        for (i = 0; i < mServiceMap.size(); i++)
        {
          paramMessage = (DataService.DataServiceProvider)mServiceMap.get(i);
          if (paramMessage != null) {
            paramMessage.onDestroy();
          }
        }
        mServiceMap.clear();
        break;
      case 2: 
        if (localDataServiceProvider != null)
        {
          localDataServiceProvider.onDestroy();
          mServiceMap.remove(i);
        }
        break;
      case 1: 
        paramMessage = createDataServiceProvider(arg1);
        if (paramMessage != null) {
          mServiceMap.put(i, paramMessage);
        }
        break;
      }
    }
  }
  
  public class DataServiceProvider
  {
    private final List<IDataServiceCallback> mDataCallListChangedCallbacks = new ArrayList();
    private final int mSlotId;
    
    public DataServiceProvider(int paramInt)
    {
      mSlotId = paramInt;
    }
    
    private void registerForDataCallListChanged(IDataServiceCallback paramIDataServiceCallback)
    {
      synchronized (mDataCallListChangedCallbacks)
      {
        mDataCallListChangedCallbacks.add(paramIDataServiceCallback);
        return;
      }
    }
    
    private void unregisterForDataCallListChanged(IDataServiceCallback paramIDataServiceCallback)
    {
      synchronized (mDataCallListChangedCallbacks)
      {
        mDataCallListChangedCallbacks.remove(paramIDataServiceCallback);
        return;
      }
    }
    
    public void deactivateDataCall(int paramInt1, int paramInt2, DataServiceCallback paramDataServiceCallback)
    {
      paramDataServiceCallback.onDeactivateDataCallComplete(1);
    }
    
    public void getDataCallList(DataServiceCallback paramDataServiceCallback)
    {
      paramDataServiceCallback.onGetDataCallListComplete(1, null);
    }
    
    public final int getSlotId()
    {
      return mSlotId;
    }
    
    public final void notifyDataCallListChanged(List<DataCallResponse> paramList)
    {
      synchronized (mDataCallListChangedCallbacks)
      {
        Iterator localIterator = mDataCallListChangedCallbacks.iterator();
        while (localIterator.hasNext())
        {
          IDataServiceCallback localIDataServiceCallback = (IDataServiceCallback)localIterator.next();
          DataService.DataServiceHandler localDataServiceHandler = mHandler;
          int i = mSlotId;
          DataService.DataCallListChangedIndication localDataCallListChangedIndication = new android/telephony/data/DataService$DataCallListChangedIndication;
          localDataCallListChangedIndication.<init>(paramList, localIDataServiceCallback);
          localDataServiceHandler.obtainMessage(11, i, 0, localDataCallListChangedIndication).sendToTarget();
        }
        return;
      }
    }
    
    protected void onDestroy()
    {
      mDataCallListChangedCallbacks.clear();
    }
    
    public void setDataProfile(List<DataProfile> paramList, boolean paramBoolean, DataServiceCallback paramDataServiceCallback)
    {
      paramDataServiceCallback.onSetDataProfileComplete(1);
    }
    
    public void setInitialAttachApn(DataProfile paramDataProfile, boolean paramBoolean, DataServiceCallback paramDataServiceCallback)
    {
      paramDataServiceCallback.onSetInitialAttachApnComplete(1);
    }
    
    public void setupDataCall(int paramInt1, DataProfile paramDataProfile, boolean paramBoolean1, boolean paramBoolean2, int paramInt2, LinkProperties paramLinkProperties, DataServiceCallback paramDataServiceCallback)
    {
      paramDataServiceCallback.onSetupDataCallComplete(1, null);
    }
  }
  
  private static final class DeactivateDataCallRequest
  {
    public final IDataServiceCallback callback;
    public final int cid;
    public final int reason;
    
    DeactivateDataCallRequest(int paramInt1, int paramInt2, IDataServiceCallback paramIDataServiceCallback)
    {
      cid = paramInt1;
      reason = paramInt2;
      callback = paramIDataServiceCallback;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface DeactivateDataReason {}
  
  private class IDataServiceWrapper
    extends IDataService.Stub
  {
    private IDataServiceWrapper() {}
    
    public void createDataServiceProvider(int paramInt)
    {
      mHandler.obtainMessage(1, paramInt, 0).sendToTarget();
    }
    
    public void deactivateDataCall(int paramInt1, int paramInt2, int paramInt3, IDataServiceCallback paramIDataServiceCallback)
    {
      mHandler.obtainMessage(5, paramInt1, 0, new DataService.DeactivateDataCallRequest(paramInt2, paramInt3, paramIDataServiceCallback)).sendToTarget();
    }
    
    public void getDataCallList(int paramInt, IDataServiceCallback paramIDataServiceCallback)
    {
      if (paramIDataServiceCallback == null)
      {
        DataService.this.loge("getDataCallList: callback is null");
        return;
      }
      mHandler.obtainMessage(8, paramInt, 0, paramIDataServiceCallback).sendToTarget();
    }
    
    public void registerForDataCallListChanged(int paramInt, IDataServiceCallback paramIDataServiceCallback)
    {
      if (paramIDataServiceCallback == null)
      {
        DataService.this.loge("registerForDataCallListChanged: callback is null");
        return;
      }
      mHandler.obtainMessage(9, paramInt, 0, paramIDataServiceCallback).sendToTarget();
    }
    
    public void removeDataServiceProvider(int paramInt)
    {
      mHandler.obtainMessage(2, paramInt, 0).sendToTarget();
    }
    
    public void setDataProfile(int paramInt, List<DataProfile> paramList, boolean paramBoolean, IDataServiceCallback paramIDataServiceCallback)
    {
      mHandler.obtainMessage(7, paramInt, 0, new DataService.SetDataProfileRequest(paramList, paramBoolean, paramIDataServiceCallback)).sendToTarget();
    }
    
    public void setInitialAttachApn(int paramInt, DataProfile paramDataProfile, boolean paramBoolean, IDataServiceCallback paramIDataServiceCallback)
    {
      mHandler.obtainMessage(6, paramInt, 0, new DataService.SetInitialAttachApnRequest(paramDataProfile, paramBoolean, paramIDataServiceCallback)).sendToTarget();
    }
    
    public void setupDataCall(int paramInt1, int paramInt2, DataProfile paramDataProfile, boolean paramBoolean1, boolean paramBoolean2, int paramInt3, LinkProperties paramLinkProperties, IDataServiceCallback paramIDataServiceCallback)
    {
      mHandler.obtainMessage(4, paramInt1, 0, new DataService.SetupDataCallRequest(paramInt2, paramDataProfile, paramBoolean1, paramBoolean2, paramInt3, paramLinkProperties, paramIDataServiceCallback)).sendToTarget();
    }
    
    public void unregisterForDataCallListChanged(int paramInt, IDataServiceCallback paramIDataServiceCallback)
    {
      if (paramIDataServiceCallback == null)
      {
        DataService.this.loge("unregisterForDataCallListChanged: callback is null");
        return;
      }
      mHandler.obtainMessage(10, paramInt, 0, paramIDataServiceCallback).sendToTarget();
    }
  }
  
  private static final class SetDataProfileRequest
  {
    public final IDataServiceCallback callback;
    public final List<DataProfile> dps;
    public final boolean isRoaming;
    
    SetDataProfileRequest(List<DataProfile> paramList, boolean paramBoolean, IDataServiceCallback paramIDataServiceCallback)
    {
      dps = paramList;
      isRoaming = paramBoolean;
      callback = paramIDataServiceCallback;
    }
  }
  
  private static final class SetInitialAttachApnRequest
  {
    public final IDataServiceCallback callback;
    public final DataProfile dataProfile;
    public final boolean isRoaming;
    
    SetInitialAttachApnRequest(DataProfile paramDataProfile, boolean paramBoolean, IDataServiceCallback paramIDataServiceCallback)
    {
      dataProfile = paramDataProfile;
      isRoaming = paramBoolean;
      callback = paramIDataServiceCallback;
    }
  }
  
  private static final class SetupDataCallRequest
  {
    public final int accessNetworkType;
    public final boolean allowRoaming;
    public final IDataServiceCallback callback;
    public final DataProfile dataProfile;
    public final boolean isRoaming;
    public final LinkProperties linkProperties;
    public final int reason;
    
    SetupDataCallRequest(int paramInt1, DataProfile paramDataProfile, boolean paramBoolean1, boolean paramBoolean2, int paramInt2, LinkProperties paramLinkProperties, IDataServiceCallback paramIDataServiceCallback)
    {
      accessNetworkType = paramInt1;
      dataProfile = paramDataProfile;
      isRoaming = paramBoolean1;
      allowRoaming = paramBoolean2;
      linkProperties = paramLinkProperties;
      reason = paramInt2;
      callback = paramIDataServiceCallback;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SetupDataReason {}
}
