package com.android.internal.telephony;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import com.android.internal.telephony.uicc.IccCardApplicationStatus.AppType;
import com.android.internal.telephony.uicc.IccRecords;

public class IccCard
{
  private IccCardConstants.State mIccCardState = IccCardConstants.State.UNKNOWN;
  
  public IccCard() {}
  
  public IccCard(IccCardConstants.State paramState)
  {
    mIccCardState = paramState;
  }
  
  private void sendMessageWithCardAbsentException(Message paramMessage)
  {
    forMessageexception = new RuntimeException("No valid IccCard");
    paramMessage.sendToTarget();
  }
  
  public void changeIccFdnPassword(String paramString1, String paramString2, Message paramMessage)
  {
    sendMessageWithCardAbsentException(paramMessage);
  }
  
  public void changeIccLockPassword(String paramString1, String paramString2, Message paramMessage)
  {
    sendMessageWithCardAbsentException(paramMessage);
  }
  
  public boolean getIccFdnEnabled()
  {
    return false;
  }
  
  public boolean getIccLockEnabled()
  {
    return false;
  }
  
  public boolean getIccPin2Blocked()
  {
    return false;
  }
  
  public boolean getIccPuk2Blocked()
  {
    return false;
  }
  
  public IccRecords getIccRecords()
  {
    return null;
  }
  
  public String getServiceProviderName()
  {
    return null;
  }
  
  public IccCardConstants.State getState()
  {
    return mIccCardState;
  }
  
  public boolean hasIccCard()
  {
    return false;
  }
  
  public boolean isApplicationOnIcc(IccCardApplicationStatus.AppType paramAppType)
  {
    return false;
  }
  
  public void registerForNetworkLocked(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void setIccFdnEnabled(boolean paramBoolean, String paramString, Message paramMessage)
  {
    sendMessageWithCardAbsentException(paramMessage);
  }
  
  public void setIccLockEnabled(boolean paramBoolean, String paramString, Message paramMessage)
  {
    sendMessageWithCardAbsentException(paramMessage);
  }
  
  public void supplyNetworkDepersonalization(String paramString, Message paramMessage)
  {
    sendMessageWithCardAbsentException(paramMessage);
  }
  
  public void supplyPin(String paramString, Message paramMessage)
  {
    sendMessageWithCardAbsentException(paramMessage);
  }
  
  public void supplyPin2(String paramString, Message paramMessage)
  {
    sendMessageWithCardAbsentException(paramMessage);
  }
  
  public void supplyPuk(String paramString1, String paramString2, Message paramMessage)
  {
    sendMessageWithCardAbsentException(paramMessage);
  }
  
  public void supplyPuk2(String paramString1, String paramString2, Message paramMessage)
  {
    sendMessageWithCardAbsentException(paramMessage);
  }
  
  public void unregisterForNetworkLocked(Handler paramHandler) {}
}
