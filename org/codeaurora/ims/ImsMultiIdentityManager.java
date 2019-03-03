package org.codeaurora.ims;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import org.codeaurora.ims.internal.IImsMultiIdentityInterface;

public class ImsMultiIdentityManager
{
  private static String LOG_TAG = "ImsMultiIdentityManager";
  public static final int REGISTRATION_RESPONSE_FAILURE = 0;
  public static final int REGISTRATION_RESPONSE_SUCCESS = 1;
  private volatile IImsMultiIdentityInterface mInterface;
  private int mPhoneId;
  private QtiImsExtManager mQtiImsExtMgr;
  
  ImsMultiIdentityManager(int paramInt, QtiImsExtManager paramQtiImsExtManager)
  {
    mPhoneId = paramInt;
    mQtiImsExtMgr = paramQtiImsExtManager;
  }
  
  private IImsMultiIdentityInterface getMultiIdentityInterface()
    throws QtiImsException
  {
    IImsMultiIdentityInterface localIImsMultiIdentityInterface = mInterface;
    if (localIImsMultiIdentityInterface != null) {
      return localIImsMultiIdentityInterface;
    }
    mQtiImsExtMgr.validateInvariants(mPhoneId);
    localIImsMultiIdentityInterface = mQtiImsExtMgr.getMultiIdentityInterface(mPhoneId);
    if (localIImsMultiIdentityInterface != null)
    {
      mInterface = localIImsMultiIdentityInterface;
      Object localObject = localIImsMultiIdentityInterface.asBinder();
      try
      {
        _..Lambda.ImsMultiIdentityManager.3dvlNMVMgotYzZoawqPgTpooQ8Q local3dvlNMVMgotYzZoawqPgTpooQ8Q = new org/codeaurora/ims/_$$Lambda$ImsMultiIdentityManager$3dvlNMVMgotYzZoawqPgTpooQ8Q;
        local3dvlNMVMgotYzZoawqPgTpooQ8Q.<init>(this);
        ((IBinder)localObject).linkToDeath(local3dvlNMVMgotYzZoawqPgTpooQ8Q, 0);
        return localIImsMultiIdentityInterface;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e(LOG_TAG, "Unable to listen for Server Process death");
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Remote linkToDeath Exception : ");
        ((StringBuilder)localObject).append(localRemoteException);
        throw new QtiImsException(((StringBuilder)localObject).toString());
      }
    }
    Log.e(LOG_TAG, "mInterface is NULL");
    throw new QtiImsException("Remote Interface is NULL");
  }
  
  private void onServiceDied()
  {
    mInterface = null;
  }
  
  public void queryVirtualLineInfo(String paramString)
    throws QtiImsException
  {
    if ((paramString != null) && (!paramString.isEmpty()))
    {
      mQtiImsExtMgr.validateInvariants(mPhoneId);
      try
      {
        getMultiIdentityInterface().queryVirtualLineInfo(paramString);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        paramString = new StringBuilder();
        paramString.append("Remote ImsService queryVirtualLineInfo : ");
        paramString.append(localRemoteException);
        throw new QtiImsException(paramString.toString());
      }
    }
    Log.e(LOG_TAG, "queryVirtualLineInfo :: invalid msisdn");
    throw new QtiImsException("queryVirtualLineInfo :: invalid msisdn");
  }
  
  public void setMultiIdentityListener(ImsMultiIdentityListenerBase paramImsMultiIdentityListenerBase)
    throws QtiImsException
  {
    if (paramImsMultiIdentityListenerBase != null)
    {
      mQtiImsExtMgr.validateInvariants(mPhoneId);
      try
      {
        getMultiIdentityInterface().setMultiIdentityListener(paramImsMultiIdentityListenerBase.getListener());
        return;
      }
      catch (RemoteException paramImsMultiIdentityListenerBase)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Remote ImsService setMultiIdentityListener : ");
        localStringBuilder.append(paramImsMultiIdentityListenerBase);
        throw new QtiImsException(localStringBuilder.toString());
      }
    }
    Log.e(LOG_TAG, "setMultiIdentityListener :: listener is NULL");
    throw new QtiImsException("setMultiIdentityListener :: listener is NULL");
  }
  
  public void updateRegistrationStatus(ArrayList<MultiIdentityLineInfo> paramArrayList)
    throws QtiImsException
  {
    if (paramArrayList != null)
    {
      mQtiImsExtMgr.validateInvariants(mPhoneId);
      try
      {
        getMultiIdentityInterface().updateRegistrationStatus(paramArrayList);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        paramArrayList = new StringBuilder();
        paramArrayList.append("Remote ImsService updateRegistrationStatus : ");
        paramArrayList.append(localRemoteException);
        throw new QtiImsException(paramArrayList.toString());
      }
    }
    Log.e(LOG_TAG, "updateRegistrationStatus :: linesInfo is NULL");
    throw new QtiImsException("updateRegistrationStatus :: linesInfo is NULL");
  }
}
