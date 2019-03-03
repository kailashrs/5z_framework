package org.codeaurora.ims;

import org.codeaurora.ims.internal.IQtiImsExtListener.Stub;

public class QtiImsExtListenerBaseImpl
  extends IQtiImsExtListener.Stub
{
  public QtiImsExtListenerBaseImpl() {}
  
  public void notifyParticipantStatusInfo(int paramInt1, int paramInt2, int paramInt3, String paramString, boolean paramBoolean) {}
  
  public void notifySsacStatus(int paramInt, boolean paramBoolean) {}
  
  public void notifyVopsStatus(int paramInt, boolean paramBoolean) {}
  
  public void onGetCallForwardUncondTimer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, String paramString, int paramInt8) {}
  
  public void onGetHandoverConfig(int paramInt1, int paramInt2, int paramInt3) {}
  
  public void onGetPacketCount(int paramInt1, int paramInt2, long paramLong) {}
  
  public void onGetPacketErrorCount(int paramInt1, int paramInt2, long paramLong) {}
  
  public void onSetCallForwardUncondTimer(int paramInt1, int paramInt2) {}
  
  public void onSetHandoverConfig(int paramInt1, int paramInt2) {}
  
  public void onUTReqFailed(int paramInt1, int paramInt2, String paramString) {}
  
  public void onVoltePreferenceQueried(int paramInt1, int paramInt2, int paramInt3) {}
  
  public void onVoltePreferenceUpdated(int paramInt1, int paramInt2) {}
  
  public void receiveCallTransferResponse(int paramInt1, int paramInt2) {}
  
  public void receiveCancelModifyCallResponse(int paramInt1, int paramInt2) {}
}
