package org.codeaurora.ims;

import org.codeaurora.ims.internal.IImsMultiIdentityInterface;
import org.codeaurora.ims.internal.IQtiImsExt.Stub;
import org.codeaurora.ims.internal.IQtiImsExtListener;

public abstract class QtiImsExtBase
{
  private QtiImsExtBinder mQtiImsExtBinder;
  
  public QtiImsExtBase() {}
  
  public QtiImsExtBinder getBinder()
  {
    if (mQtiImsExtBinder == null) {
      mQtiImsExtBinder = new QtiImsExtBinder();
    }
    return mQtiImsExtBinder;
  }
  
  protected void onGetCallForwardUncondTimer(int paramInt1, int paramInt2, int paramInt3, IQtiImsExtListener paramIQtiImsExtListener) {}
  
  protected void onGetHandoverConfig(int paramInt, IQtiImsExtListener paramIQtiImsExtListener) {}
  
  protected IImsMultiIdentityInterface onGetMultiIdentityInterface(int paramInt)
  {
    return null;
  }
  
  protected void onGetPacketCount(int paramInt, IQtiImsExtListener paramIQtiImsExtListener) {}
  
  protected void onGetPacketErrorCount(int paramInt, IQtiImsExtListener paramIQtiImsExtListener) {}
  
  protected int onGetRcsAppConfig(int paramInt)
  {
    return 0;
  }
  
  protected int onGetVvmAppConfig(int paramInt)
  {
    return 0;
  }
  
  protected void onQuerySsacStatus(int paramInt, IQtiImsExtListener paramIQtiImsExtListener) {}
  
  protected void onQueryVoltePreference(int paramInt, IQtiImsExtListener paramIQtiImsExtListener) {}
  
  protected void onQueryVopsStatus(int paramInt, IQtiImsExtListener paramIQtiImsExtListener) {}
  
  protected void onRegisterForParticipantStatusInfo(int paramInt, IQtiImsExtListener paramIQtiImsExtListener) {}
  
  protected void onRegisterForViceRefreshInfo(int paramInt, IQtiImsExtListener paramIQtiImsExtListener) {}
  
  protected void onResumePendingCall(int paramInt1, int paramInt2) {}
  
  protected void onSendCallTransferRequest(int paramInt1, int paramInt2, String paramString, IQtiImsExtListener paramIQtiImsExtListener) {}
  
  protected void onSendCancelModifyCall(int paramInt, IQtiImsExtListener paramIQtiImsExtListener) {}
  
  protected void onSetCallForwardUncondTimer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, String paramString, IQtiImsExtListener paramIQtiImsExtListener) {}
  
  protected void onSetHandoverConfig(int paramInt1, int paramInt2, IQtiImsExtListener paramIQtiImsExtListener) {}
  
  protected int onSetRcsAppConfig(int paramInt1, int paramInt2)
  {
    return 0;
  }
  
  protected int onSetVvmAppConfig(int paramInt1, int paramInt2)
  {
    return 0;
  }
  
  protected void onUpdateVoltePreference(int paramInt1, int paramInt2, IQtiImsExtListener paramIQtiImsExtListener) {}
  
  public final class QtiImsExtBinder
    extends IQtiImsExt.Stub
  {
    public QtiImsExtBinder() {}
    
    public void getCallForwardUncondTimer(int paramInt1, int paramInt2, int paramInt3, IQtiImsExtListener paramIQtiImsExtListener)
    {
      onGetCallForwardUncondTimer(paramInt1, paramInt2, paramInt3, paramIQtiImsExtListener);
    }
    
    public void getHandoverConfig(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
    {
      onGetHandoverConfig(paramInt, paramIQtiImsExtListener);
    }
    
    public IImsMultiIdentityInterface getMultiIdentityInterface(int paramInt)
    {
      return onGetMultiIdentityInterface(paramInt);
    }
    
    public void getPacketCount(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
    {
      onGetPacketCount(paramInt, paramIQtiImsExtListener);
    }
    
    public void getPacketErrorCount(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
    {
      onGetPacketErrorCount(paramInt, paramIQtiImsExtListener);
    }
    
    public int getRcsAppConfig(int paramInt)
    {
      return onGetRcsAppConfig(paramInt);
    }
    
    public int getVvmAppConfig(int paramInt)
    {
      return onGetVvmAppConfig(paramInt);
    }
    
    public void querySsacStatus(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
    {
      onQuerySsacStatus(paramInt, paramIQtiImsExtListener);
    }
    
    public void queryVoltePreference(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
    {
      onQueryVoltePreference(paramInt, paramIQtiImsExtListener);
    }
    
    public void queryVopsStatus(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
    {
      onQueryVopsStatus(paramInt, paramIQtiImsExtListener);
    }
    
    public void registerForParticipantStatusInfo(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
    {
      onRegisterForParticipantStatusInfo(paramInt, paramIQtiImsExtListener);
    }
    
    public void resumePendingCall(int paramInt1, int paramInt2)
    {
      onResumePendingCall(paramInt1, paramInt2);
    }
    
    public void sendCallTransferRequest(int paramInt1, int paramInt2, String paramString, IQtiImsExtListener paramIQtiImsExtListener)
    {
      onSendCallTransferRequest(paramInt1, paramInt2, paramString, paramIQtiImsExtListener);
    }
    
    public void sendCancelModifyCall(int paramInt, IQtiImsExtListener paramIQtiImsExtListener)
    {
      onSendCancelModifyCall(paramInt, paramIQtiImsExtListener);
    }
    
    public void setCallForwardUncondTimer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, String paramString, IQtiImsExtListener paramIQtiImsExtListener)
    {
      onSetCallForwardUncondTimer(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramString, paramIQtiImsExtListener);
    }
    
    public void setHandoverConfig(int paramInt1, int paramInt2, IQtiImsExtListener paramIQtiImsExtListener)
    {
      onSetHandoverConfig(paramInt1, paramInt2, paramIQtiImsExtListener);
    }
    
    public int setRcsAppConfig(int paramInt1, int paramInt2)
    {
      return onSetRcsAppConfig(paramInt1, paramInt2);
    }
    
    public int setVvmAppConfig(int paramInt1, int paramInt2)
    {
      return onSetVvmAppConfig(paramInt1, paramInt2);
    }
    
    public void updateVoltePreference(int paramInt1, int paramInt2, IQtiImsExtListener paramIQtiImsExtListener)
    {
      onUpdateVoltePreference(paramInt1, paramInt2, paramIQtiImsExtListener);
    }
  }
}
