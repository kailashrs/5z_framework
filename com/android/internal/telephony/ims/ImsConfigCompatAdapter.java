package com.android.internal.telephony.ims;

import android.os.RemoteException;
import android.telephony.ims.stub.ImsConfigImplBase;
import android.util.Log;
import com.android.ims.internal.IImsConfig;

public class ImsConfigCompatAdapter
  extends ImsConfigImplBase
{
  public static final int FAILED = 1;
  public static final int SUCCESS = 0;
  private static final String TAG = "ImsConfigCompatAdapter";
  public static final int UNKNOWN = -1;
  private final IImsConfig mOldConfigInterface;
  
  public ImsConfigCompatAdapter(IImsConfig paramIImsConfig)
  {
    mOldConfigInterface = paramIImsConfig;
  }
  
  public int getConfigInt(int paramInt)
  {
    try
    {
      int i = mOldConfigInterface.getProvisionedValue(paramInt);
      if (i != -1) {
        return i;
      }
    }
    catch (RemoteException localRemoteException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("getConfigInt: item=");
      localStringBuilder.append(paramInt);
      localStringBuilder.append("failed: ");
      localStringBuilder.append(localRemoteException.getMessage());
      Log.w("ImsConfigCompatAdapter", localStringBuilder.toString());
    }
    return -1;
  }
  
  public String getConfigString(int paramInt)
  {
    try
    {
      String str = mOldConfigInterface.getProvisionedStringValue(paramInt);
      return str;
    }
    catch (RemoteException localRemoteException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("getConfigInt: item=");
      localStringBuilder.append(paramInt);
      localStringBuilder.append("failed: ");
      localStringBuilder.append(localRemoteException.getMessage());
      Log.w("ImsConfigCompatAdapter", localStringBuilder.toString());
    }
    return null;
  }
  
  public int setConfig(int paramInt1, int paramInt2)
  {
    try
    {
      int i = mOldConfigInterface.setProvisionedValue(paramInt1, paramInt2);
      if (i == 0) {
        return 0;
      }
    }
    catch (RemoteException localRemoteException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("setConfig: item=");
      localStringBuilder.append(paramInt1);
      localStringBuilder.append(" value=");
      localStringBuilder.append(paramInt2);
      localStringBuilder.append("failed: ");
      localStringBuilder.append(localRemoteException.getMessage());
      Log.w("ImsConfigCompatAdapter", localStringBuilder.toString());
    }
    return 1;
  }
  
  public int setConfig(int paramInt, String paramString)
  {
    try
    {
      int i = mOldConfigInterface.setProvisionedStringValue(paramInt, paramString);
      if (i == 0) {
        return 0;
      }
    }
    catch (RemoteException localRemoteException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("setConfig: item=");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(" value=");
      localStringBuilder.append(paramString);
      localStringBuilder.append("failed: ");
      localStringBuilder.append(localRemoteException.getMessage());
      Log.w("ImsConfigCompatAdapter", localStringBuilder.toString());
    }
    return 1;
  }
}
