package android.telephony.ims.compat.stub;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;
import com.android.ims.ImsConfigListener;
import com.android.ims.internal.IImsConfig;
import com.android.ims.internal.IImsConfig.Stub;
import com.android.internal.annotations.VisibleForTesting;
import java.lang.ref.WeakReference;
import java.util.HashMap;

public class ImsConfigImplBase
{
  private static final String TAG = "ImsConfigImplBase";
  ImsConfigStub mImsConfigStub;
  
  public ImsConfigImplBase(Context paramContext)
  {
    mImsConfigStub = new ImsConfigStub(this, paramContext);
  }
  
  public void getFeatureValue(int paramInt1, int paramInt2, ImsConfigListener paramImsConfigListener)
    throws RemoteException
  {}
  
  public IImsConfig getIImsConfig()
  {
    return mImsConfigStub;
  }
  
  public String getProvisionedStringValue(int paramInt)
    throws RemoteException
  {
    return null;
  }
  
  public int getProvisionedValue(int paramInt)
    throws RemoteException
  {
    return -1;
  }
  
  public void getVideoQuality(ImsConfigListener paramImsConfigListener)
    throws RemoteException
  {}
  
  public boolean getVolteProvisioned()
    throws RemoteException
  {
    return false;
  }
  
  public final void notifyProvisionedValueChanged(int paramInt1, int paramInt2)
  {
    mImsConfigStub.updateCachedValue(paramInt1, paramInt2, true);
  }
  
  public final void notifyProvisionedValueChanged(int paramInt, String paramString)
  {
    mImsConfigStub.updateCachedValue(paramInt, paramString, true);
  }
  
  public void setFeatureValue(int paramInt1, int paramInt2, int paramInt3, ImsConfigListener paramImsConfigListener)
    throws RemoteException
  {}
  
  public int setProvisionedStringValue(int paramInt, String paramString)
    throws RemoteException
  {
    return 1;
  }
  
  public int setProvisionedValue(int paramInt1, int paramInt2)
    throws RemoteException
  {
    return 1;
  }
  
  public void setVideoQuality(int paramInt, ImsConfigListener paramImsConfigListener)
    throws RemoteException
  {}
  
  @VisibleForTesting
  public static class ImsConfigStub
    extends IImsConfig.Stub
  {
    Context mContext;
    WeakReference<ImsConfigImplBase> mImsConfigImplBaseWeakReference;
    private HashMap<Integer, Integer> mProvisionedIntValue = new HashMap();
    private HashMap<Integer, String> mProvisionedStringValue = new HashMap();
    
    @VisibleForTesting
    public ImsConfigStub(ImsConfigImplBase paramImsConfigImplBase, Context paramContext)
    {
      mContext = paramContext;
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
    
    private void sendImsConfigChangedIntent(int paramInt1, int paramInt2)
    {
      sendImsConfigChangedIntent(paramInt1, Integer.toString(paramInt2));
    }
    
    private void sendImsConfigChangedIntent(int paramInt, String paramString)
    {
      Intent localIntent = new Intent("com.android.intent.action.IMS_CONFIG_CHANGED");
      localIntent.putExtra("item", paramInt);
      localIntent.putExtra("value", paramString);
      if (mContext != null) {
        mContext.sendBroadcast(localIntent);
      }
    }
    
    public void getFeatureValue(int paramInt1, int paramInt2, ImsConfigListener paramImsConfigListener)
      throws RemoteException
    {
      getImsConfigImpl().getFeatureValue(paramInt1, paramInt2, paramImsConfigListener);
    }
    
    public String getProvisionedStringValue(int paramInt)
      throws RemoteException
    {
      try
      {
        if (mProvisionedIntValue.containsKey(Integer.valueOf(paramInt)))
        {
          str = (String)mProvisionedStringValue.get(Integer.valueOf(paramInt));
          return str;
        }
        String str = getImsConfigImpl().getProvisionedStringValue(paramInt);
        if (str != null) {
          updateCachedValue(paramInt, str, false);
        }
        return str;
      }
      finally {}
    }
    
    public int getProvisionedValue(int paramInt)
      throws RemoteException
    {
      try
      {
        if (mProvisionedIntValue.containsKey(Integer.valueOf(paramInt)))
        {
          paramInt = ((Integer)mProvisionedIntValue.get(Integer.valueOf(paramInt))).intValue();
          return paramInt;
        }
        int i = getImsConfigImpl().getProvisionedValue(paramInt);
        if (i != -1) {
          updateCachedValue(paramInt, i, false);
        }
        return i;
      }
      finally {}
    }
    
    public void getVideoQuality(ImsConfigListener paramImsConfigListener)
      throws RemoteException
    {
      getImsConfigImpl().getVideoQuality(paramImsConfigListener);
    }
    
    public boolean getVolteProvisioned()
      throws RemoteException
    {
      return getImsConfigImpl().getVolteProvisioned();
    }
    
    public void setFeatureValue(int paramInt1, int paramInt2, int paramInt3, ImsConfigListener paramImsConfigListener)
      throws RemoteException
    {
      getImsConfigImpl().setFeatureValue(paramInt1, paramInt2, paramInt3, paramImsConfigListener);
    }
    
    public int setProvisionedStringValue(int paramInt, String paramString)
      throws RemoteException
    {
      try
      {
        mProvisionedStringValue.remove(Integer.valueOf(paramInt));
        int i = getImsConfigImpl().setProvisionedStringValue(paramInt, paramString);
        if (i == 0) {
          updateCachedValue(paramInt, paramString, true);
        }
        return i;
      }
      finally {}
    }
    
    public int setProvisionedValue(int paramInt1, int paramInt2)
      throws RemoteException
    {
      try
      {
        mProvisionedIntValue.remove(Integer.valueOf(paramInt1));
        int i = getImsConfigImpl().setProvisionedValue(paramInt1, paramInt2);
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
    
    public void setVideoQuality(int paramInt, ImsConfigListener paramImsConfigListener)
      throws RemoteException
    {
      getImsConfigImpl().setVideoQuality(paramInt, paramImsConfigListener);
    }
    
    protected void updateCachedValue(int paramInt1, int paramInt2, boolean paramBoolean)
    {
      try
      {
        mProvisionedIntValue.put(Integer.valueOf(paramInt1), Integer.valueOf(paramInt2));
        if (paramBoolean) {
          sendImsConfigChangedIntent(paramInt1, paramInt2);
        }
        return;
      }
      finally {}
    }
    
    protected void updateCachedValue(int paramInt, String paramString, boolean paramBoolean)
    {
      try
      {
        mProvisionedStringValue.put(Integer.valueOf(paramInt), paramString);
        if (paramBoolean) {
          sendImsConfigChangedIntent(paramInt, paramString);
        }
        return;
      }
      finally {}
    }
  }
}
