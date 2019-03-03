package android.telephony.ims;

import android.annotation.SystemApi;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import com.android.ims.internal.IImsUtListener;

@SystemApi
public class ImsUtListener
{
  private static final String LOG_TAG = "ImsUtListener";
  private IImsUtListener mServiceInterface;
  
  public ImsUtListener(IImsUtListener paramIImsUtListener)
  {
    mServiceInterface = paramIImsUtListener;
  }
  
  public void onSupplementaryServiceIndication(ImsSsData paramImsSsData)
  {
    try
    {
      mServiceInterface.onSupplementaryServiceIndication(paramImsSsData);
    }
    catch (RemoteException paramImsSsData)
    {
      Log.w("ImsUtListener", "onSupplementaryServiceIndication: remote exception");
    }
  }
  
  public void onUtConfigurationCallBarringQueried(int paramInt, ImsSsInfo[] paramArrayOfImsSsInfo)
  {
    try
    {
      mServiceInterface.utConfigurationCallBarringQueried(null, paramInt, paramArrayOfImsSsInfo);
    }
    catch (RemoteException paramArrayOfImsSsInfo)
    {
      Log.w("ImsUtListener", "utConfigurationCallBarringQueried: remote exception");
    }
  }
  
  public void onUtConfigurationCallForwardQueried(int paramInt, ImsCallForwardInfo[] paramArrayOfImsCallForwardInfo)
  {
    try
    {
      mServiceInterface.utConfigurationCallForwardQueried(null, paramInt, paramArrayOfImsCallForwardInfo);
    }
    catch (RemoteException paramArrayOfImsCallForwardInfo)
    {
      Log.w("ImsUtListener", "utConfigurationCallForwardQueried: remote exception");
    }
  }
  
  public void onUtConfigurationCallWaitingQueried(int paramInt, ImsSsInfo[] paramArrayOfImsSsInfo)
  {
    try
    {
      mServiceInterface.utConfigurationCallWaitingQueried(null, paramInt, paramArrayOfImsSsInfo);
    }
    catch (RemoteException paramArrayOfImsSsInfo)
    {
      Log.w("ImsUtListener", "utConfigurationCallWaitingQueried: remote exception");
    }
  }
  
  public void onUtConfigurationQueried(int paramInt, Bundle paramBundle)
  {
    try
    {
      mServiceInterface.utConfigurationQueried(null, paramInt, paramBundle);
    }
    catch (RemoteException paramBundle)
    {
      Log.w("ImsUtListener", "utConfigurationQueried: remote exception");
    }
  }
  
  public void onUtConfigurationQueryFailed(int paramInt, ImsReasonInfo paramImsReasonInfo)
  {
    try
    {
      mServiceInterface.utConfigurationQueryFailed(null, paramInt, paramImsReasonInfo);
    }
    catch (RemoteException paramImsReasonInfo)
    {
      Log.w("ImsUtListener", "utConfigurationQueryFailed: remote exception");
    }
  }
  
  public void onUtConfigurationUpdateFailed(int paramInt, ImsReasonInfo paramImsReasonInfo)
  {
    try
    {
      mServiceInterface.utConfigurationUpdateFailed(null, paramInt, paramImsReasonInfo);
    }
    catch (RemoteException paramImsReasonInfo)
    {
      Log.w("ImsUtListener", "utConfigurationUpdateFailed: remote exception");
    }
  }
  
  public void onUtConfigurationUpdated(int paramInt)
  {
    try
    {
      mServiceInterface.utConfigurationUpdated(null, paramInt);
    }
    catch (RemoteException localRemoteException)
    {
      Log.w("ImsUtListener", "utConfigurationUpdated: remote exception");
    }
  }
}
