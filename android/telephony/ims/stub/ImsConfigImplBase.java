package android.telephony.ims.stub;

import android.annotation.SystemApi;
import android.content.Context;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.telephony.ims.aidl.IImsConfig;
import android.telephony.ims.aidl.IImsConfig.Stub;
import android.telephony.ims.aidl.IImsConfigCallback;
import android.telephony.ims.aidl.IImsConfigCallback.Stub;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.lang.ref.WeakReference;
import java.util.HashMap;

@SystemApi
public class ImsConfigImplBase
{
  public static final int CONFIG_RESULT_FAILED = 1;
  public static final int CONFIG_RESULT_SUCCESS = 0;
  public static final int CONFIG_RESULT_UNKNOWN = -1;
  private static final String TAG = "ImsConfigImplBase";
  private final RemoteCallbackList<IImsConfigCallback> mCallbacks = new RemoteCallbackList();
  ImsConfigStub mImsConfigStub = new ImsConfigStub(this);
  
  public ImsConfigImplBase() {}
  
  public ImsConfigImplBase(Context paramContext) {}
  
  private void addImsConfigCallback(IImsConfigCallback paramIImsConfigCallback)
  {
    mCallbacks.register(paramIImsConfigCallback);
  }
  
  private final void notifyConfigChanged(int paramInt1, int paramInt2)
  {
    if (mCallbacks == null) {
      return;
    }
    mCallbacks.broadcast(new _..Lambda.ImsConfigImplBase.yL4863k_FoQyqg_FX2mWsLMqbyA(paramInt1, paramInt2));
  }
  
  private void notifyConfigChanged(int paramInt, String paramString)
  {
    if (mCallbacks == null) {
      return;
    }
    mCallbacks.broadcast(new _..Lambda.ImsConfigImplBase.GAuYvQ8qBc7KgCJhNp4Pt4j5t_0(paramInt, paramString));
  }
  
  private void removeImsConfigCallback(IImsConfigCallback paramIImsConfigCallback)
  {
    mCallbacks.unregister(paramIImsConfigCallback);
  }
  
  public int getConfigInt(int paramInt)
  {
    return -1;
  }
  
  public String getConfigString(int paramInt)
  {
    return null;
  }
  
  public IImsConfig getIImsConfig()
  {
    return mImsConfigStub;
  }
  
  public final void notifyProvisionedValueChanged(int paramInt1, int paramInt2)
  {
    try
    {
      mImsConfigStub.updateCachedValue(paramInt1, paramInt2, true);
    }
    catch (RemoteException localRemoteException)
    {
      Log.w("ImsConfigImplBase", "notifyProvisionedValueChanged(int): Framework connection is dead.");
    }
  }
  
  public final void notifyProvisionedValueChanged(int paramInt, String paramString)
  {
    try
    {
      mImsConfigStub.updateCachedValue(paramInt, paramString, true);
    }
    catch (RemoteException paramString)
    {
      Log.w("ImsConfigImplBase", "notifyProvisionedValueChanged(string): Framework connection is dead.");
    }
  }
  
  public int setConfig(int paramInt1, int paramInt2)
  {
    return 1;
  }
  
  public int setConfig(int paramInt, String paramString)
  {
    return 1;
  }
  
  public static class Callback
    extends IImsConfigCallback.Stub
  {
    public Callback() {}
    
    public void onConfigChanged(int paramInt1, int paramInt2) {}
    
    public void onConfigChanged(int paramInt, String paramString) {}
    
    public final void onIntConfigChanged(int paramInt1, int paramInt2)
      throws RemoteException
    {
      onConfigChanged(paramInt1, paramInt2);
    }
    
    public final void onStringConfigChanged(int paramInt, String paramString)
      throws RemoteException
    {
      onConfigChanged(paramInt, paramString);
    }
  }
  
  @VisibleForTesting
  public static class ImsConfigStub
    extends IImsConfig.Stub
  {
    WeakReference<ImsConfigImplBase> mImsConfigImplBaseWeakReference;
    private HashMap<Integer, Integer> mProvisionedIntValue = new HashMap();
    private HashMap<Integer, String> mProvisionedStringValue = new HashMap();
    
    @VisibleForTesting
    public ImsConfigStub(ImsConfigImplBase paramImsConfigImplBase)
    {
      mImsConfigImplBaseWeakReference = new WeakReference(paramImsConfigImplBase);
    }
    
    private ImsConfigImplBase getImsConfigImpl()
      throws RemoteException
    {
      ImsConfigImplBase localImsConfigImplBase = (ImsConfigImplBase)mImsConfigImplBaseWeakReference.get();
      if (localImsConfigImplBase != null) {
        return localImsConfigImplBase;
      }
      throw new RemoteException("Fail to get ImsConfigImpl");
    }
    
    private void notifyImsConfigChanged(int paramInt1, int paramInt2)
      throws RemoteException
    {
      getImsConfigImpl().notifyConfigChanged(paramInt1, paramInt2);
    }
    
    private void notifyImsConfigChanged(int paramInt, String paramString)
      throws RemoteException
    {
      getImsConfigImpl().notifyConfigChanged(paramInt, paramString);
    }
    
    public void addImsConfigCallback(IImsConfigCallback paramIImsConfigCallback)
      throws RemoteException
    {
      getImsConfigImpl().addImsConfigCallback(paramIImsConfigCallback);
    }
    
    public int getConfigInt(int paramInt)
      throws RemoteException
    {
      try
      {
        if (mProvisionedIntValue.containsKey(Integer.valueOf(paramInt)))
        {
          paramInt = ((Integer)mProvisionedIntValue.get(Integer.valueOf(paramInt))).intValue();
          return paramInt;
        }
        int i = getImsConfigImpl().getConfigInt(paramInt);
        if (i != -1) {
          updateCachedValue(paramInt, i, false);
        }
        return i;
      }
      finally {}
    }
    
    public String getConfigString(int paramInt)
      throws RemoteException
    {
      try
      {
        if (mProvisionedIntValue.containsKey(Integer.valueOf(paramInt)))
        {
          str = (String)mProvisionedStringValue.get(Integer.valueOf(paramInt));
          return str;
        }
        String str = getImsConfigImpl().getConfigString(paramInt);
        if (str != null) {
          updateCachedValue(paramInt, str, false);
        }
        return str;
      }
      finally {}
    }
    
    public void removeImsConfigCallback(IImsConfigCallback paramIImsConfigCallback)
      throws RemoteException
    {
      getImsConfigImpl().removeImsConfigCallback(paramIImsConfigCallback);
    }
    
    public int setConfigInt(int paramInt1, int paramInt2)
      throws RemoteException
    {
      try
      {
        mProvisionedIntValue.remove(Integer.valueOf(paramInt1));
        int i = getImsConfigImpl().setConfig(paramInt1, paramInt2);
        if (i == 0)
        {
          updateCachedValue(paramInt1, paramInt2, true);
        }
        else
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Set provision value of ");
          localStringBuilder.append(paramInt1);
          localStringBuilder.append(" to ");
          localStringBuilder.append(paramInt2);
          localStringBuilder.append(" failed with error code ");
          localStringBuilder.append(i);
          Log.d("ImsConfigImplBase", localStringBuilder.toString());
        }
        return i;
      }
      finally {}
    }
    
    public int setConfigString(int paramInt, String paramString)
      throws RemoteException
    {
      try
      {
        mProvisionedStringValue.remove(Integer.valueOf(paramInt));
        int i = getImsConfigImpl().setConfig(paramInt, paramString);
        if (i == 0) {
          updateCachedValue(paramInt, paramString, true);
        }
        return i;
      }
      finally {}
    }
    
    protected void updateCachedValue(int paramInt1, int paramInt2, boolean paramBoolean)
      throws RemoteException
    {
      try
      {
        mProvisionedIntValue.put(Integer.valueOf(paramInt1), Integer.valueOf(paramInt2));
        if (paramBoolean) {
          notifyImsConfigChanged(paramInt1, paramInt2);
        }
        return;
      }
      finally {}
    }
    
    protected void updateCachedValue(int paramInt, String paramString, boolean paramBoolean)
      throws RemoteException
    {
      try
      {
        mProvisionedStringValue.put(Integer.valueOf(paramInt), paramString);
        if (paramBoolean) {
          notifyImsConfigChanged(paramInt, paramString);
        }
        return;
      }
      finally {}
    }
  }
}
