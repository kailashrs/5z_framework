package android.telephony.ims.compat.stub;

import android.os.Bundle;
import android.os.RemoteException;
import android.telephony.ims.ImsCallForwardInfo;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsSsData;
import android.telephony.ims.ImsSsInfo;
import com.android.ims.internal.IImsUt;
import com.android.ims.internal.IImsUtListener.Stub;

public class ImsUtListenerImplBase
  extends IImsUtListener.Stub
{
  public ImsUtListenerImplBase() {}
  
  public void onSupplementaryServiceIndication(ImsSsData paramImsSsData) {}
  
  public void utConfigurationCallBarringQueried(IImsUt paramIImsUt, int paramInt, ImsSsInfo[] paramArrayOfImsSsInfo)
    throws RemoteException
  {}
  
  public void utConfigurationCallForwardQueried(IImsUt paramIImsUt, int paramInt, ImsCallForwardInfo[] paramArrayOfImsCallForwardInfo)
    throws RemoteException
  {}
  
  public void utConfigurationCallWaitingQueried(IImsUt paramIImsUt, int paramInt, ImsSsInfo[] paramArrayOfImsSsInfo)
    throws RemoteException
  {}
  
  public void utConfigurationQueried(IImsUt paramIImsUt, int paramInt, Bundle paramBundle)
    throws RemoteException
  {}
  
  public void utConfigurationQueryFailed(IImsUt paramIImsUt, int paramInt, ImsReasonInfo paramImsReasonInfo)
    throws RemoteException
  {}
  
  public void utConfigurationUpdateFailed(IImsUt paramIImsUt, int paramInt, ImsReasonInfo paramImsReasonInfo)
    throws RemoteException
  {}
  
  public void utConfigurationUpdated(IImsUt paramIImsUt, int paramInt)
    throws RemoteException
  {}
}
