package com.android.internal.telephony;

import android.net.LinkProperties;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.PhysicalChannelConfig;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.VoLteServiceState;
import java.util.List;

public class DefaultPhoneNotifier
  implements PhoneNotifier
{
  private static final boolean DBG = false;
  private static final String LOG_TAG = "DefaultPhoneNotifier";
  protected ITelephonyRegistry mRegistry = ITelephonyRegistry.Stub.asInterface(ServiceManager.getService("telephony.registry"));
  
  public DefaultPhoneNotifier() {}
  
  public static int convertDataActivityState(PhoneInternalInterface.DataActivityState paramDataActivityState)
  {
    switch (1.$SwitchMap$com$android$internal$telephony$PhoneInternalInterface$DataActivityState[paramDataActivityState.ordinal()])
    {
    default: 
      return 0;
    case 4: 
      return 4;
    case 3: 
      return 3;
    case 2: 
      return 2;
    }
    return 1;
  }
  
  public static int convertPreciseCallState(Call.State paramState)
  {
    switch (1.$SwitchMap$com$android$internal$telephony$Call$State[paramState.ordinal()])
    {
    default: 
      return 0;
    case 8: 
      return 8;
    case 7: 
      return 7;
    case 6: 
      return 6;
    case 5: 
      return 5;
    case 4: 
      return 4;
    case 3: 
      return 3;
    case 2: 
      return 2;
    }
    return 1;
  }
  
  private void doNotifyDataConnection(Phone paramPhone, String paramString1, String paramString2, PhoneConstants.DataState paramDataState)
  {
    int i = paramPhone.getSubId();
    long l = SubscriptionManager.getDefaultDataSubscriptionId();
    TelephonyManager localTelephonyManager = TelephonyManager.getDefault();
    LinkProperties localLinkProperties = null;
    NetworkCapabilities localNetworkCapabilities = null;
    if (paramDataState == PhoneConstants.DataState.CONNECTED)
    {
      localLinkProperties = paramPhone.getLinkProperties(paramString2);
      localNetworkCapabilities = paramPhone.getNetworkCapabilities(paramString2);
    }
    Object localObject = paramPhone.getServiceState();
    boolean bool1;
    if (localObject != null) {
      bool1 = ((ServiceState)localObject).getDataRoaming();
    } else {
      bool1 = false;
    }
    try
    {
      if (mRegistry != null)
      {
        localObject = mRegistry;
        int j = PhoneConstantConversions.convertDataState(paramDataState);
        boolean bool2 = paramPhone.isDataAllowed();
        paramPhone = paramPhone.getActiveApnHost(paramString2);
        int k;
        if (localTelephonyManager != null) {
          try
          {
            k = localTelephonyManager.getDataNetworkType(i);
          }
          catch (RemoteException paramPhone)
          {
            return;
          }
        } else {
          k = 0;
        }
        try
        {
          ((ITelephonyRegistry)localObject).notifyDataConnectionForSubscriber(i, j, bool2, paramString1, paramPhone, paramString2, localLinkProperties, localNetworkCapabilities, k, bool1);
        }
        catch (RemoteException paramPhone) {}
      }
    }
    catch (RemoteException paramPhone) {}
  }
  
  private void log(String paramString)
  {
    Rlog.d("DefaultPhoneNotifier", paramString);
  }
  
  public void notifyCallForwardingChanged(Phone paramPhone)
  {
    int i = paramPhone.getSubId();
    try
    {
      if (mRegistry != null)
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("notifyCallForwardingChanged: subId=");
        localStringBuilder.append(i);
        localStringBuilder.append(", isCFActive=");
        localStringBuilder.append(paramPhone.getCallForwardingIndicator());
        Rlog.d("DefaultPhoneNotifier", localStringBuilder.toString());
        mRegistry.notifyCallForwardingChangedForSubscriber(i, paramPhone.getCallForwardingIndicator());
      }
    }
    catch (RemoteException paramPhone) {}
  }
  
  public void notifyCellInfo(Phone paramPhone, List<CellInfo> paramList)
  {
    int i = paramPhone.getSubId();
    try
    {
      if (mRegistry != null) {
        mRegistry.notifyCellInfoForSubscriber(i, paramList);
      }
    }
    catch (RemoteException paramPhone) {}
  }
  
  public void notifyCellLocation(Phone paramPhone)
  {
    int i = paramPhone.getSubId();
    Bundle localBundle = new Bundle();
    paramPhone.getCellLocation().fillInNotifierBundle(localBundle);
    try
    {
      if (mRegistry != null) {
        mRegistry.notifyCellLocationForSubscriber(i, localBundle);
      }
    }
    catch (RemoteException paramPhone) {}
  }
  
  public void notifyDataActivationStateChanged(Phone paramPhone, int paramInt)
  {
    try
    {
      mRegistry.notifySimActivationStateChangedForPhoneId(paramPhone.getPhoneId(), paramPhone.getSubId(), 1, paramInt);
    }
    catch (RemoteException paramPhone) {}
  }
  
  public void notifyDataActivity(Phone paramPhone)
  {
    int i = paramPhone.getSubId();
    try
    {
      if (mRegistry != null) {
        mRegistry.notifyDataActivityForSubscriber(i, convertDataActivityState(paramPhone.getDataActivityState()));
      }
    }
    catch (RemoteException paramPhone) {}
  }
  
  public void notifyDataConnection(Phone paramPhone, String paramString1, String paramString2, PhoneConstants.DataState paramDataState)
  {
    doNotifyDataConnection(paramPhone, paramString1, paramString2, paramDataState);
  }
  
  public void notifyDataConnectionFailed(Phone paramPhone, String paramString1, String paramString2)
  {
    int i = paramPhone.getSubId();
    try
    {
      if (mRegistry != null) {
        mRegistry.notifyDataConnectionFailedForSubscriber(i, paramString1, paramString2);
      }
    }
    catch (RemoteException paramPhone) {}
  }
  
  public void notifyDisconnectCause(int paramInt1, int paramInt2)
  {
    try
    {
      mRegistry.notifyDisconnectCause(paramInt1, paramInt2);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void notifyMessageWaitingChanged(Phone paramPhone)
  {
    int i = paramPhone.getPhoneId();
    int j = paramPhone.getSubId();
    try
    {
      if (mRegistry != null) {
        mRegistry.notifyMessageWaitingChangedForPhoneId(i, j, paramPhone.getMessageWaitingIndicator());
      }
    }
    catch (RemoteException paramPhone) {}
  }
  
  public void notifyOemHookRawEventForSubscriber(int paramInt, byte[] paramArrayOfByte)
  {
    try
    {
      mRegistry.notifyOemHookRawEventForSubscriber(paramInt, paramArrayOfByte);
    }
    catch (RemoteException paramArrayOfByte) {}
  }
  
  public void notifyOtaspChanged(Phone paramPhone, int paramInt)
  {
    try
    {
      if (mRegistry != null) {
        mRegistry.notifyOtaspChanged(paramInt);
      }
    }
    catch (RemoteException paramPhone) {}
  }
  
  public void notifyPhoneState(Phone paramPhone)
  {
    Call localCall = paramPhone.getRingingCall();
    int i = paramPhone.getSubId();
    int j = paramPhone.getPhoneId();
    String str1 = "";
    String str2 = str1;
    if (localCall != null)
    {
      str2 = str1;
      if (localCall.getEarliestConnection() != null) {
        str2 = localCall.getEarliestConnection().getAddress();
      }
    }
    try
    {
      if (mRegistry != null) {
        mRegistry.notifyCallStateForPhoneId(j, i, PhoneConstantConversions.convertCallState(paramPhone.getState()), str2);
      }
    }
    catch (RemoteException paramPhone) {}
  }
  
  public void notifyPhysicalChannelConfiguration(Phone paramPhone, List<PhysicalChannelConfig> paramList)
  {
    int i = paramPhone.getSubId();
    try
    {
      if (mRegistry != null) {
        mRegistry.notifyPhysicalChannelConfigurationForSubscriber(i, paramList);
      }
    }
    catch (RemoteException paramPhone) {}
  }
  
  public void notifyPreciseCallState(Phone paramPhone)
  {
    Call localCall1 = paramPhone.getRingingCall();
    Call localCall2 = paramPhone.getForegroundCall();
    paramPhone = paramPhone.getBackgroundCall();
    if ((localCall1 != null) && (localCall2 != null) && (paramPhone != null)) {
      try
      {
        mRegistry.notifyPreciseCallState(convertPreciseCallState(localCall1.getState()), convertPreciseCallState(localCall2.getState()), convertPreciseCallState(paramPhone.getState()));
      }
      catch (RemoteException paramPhone) {}
    }
  }
  
  public void notifyPreciseDataConnectionFailed(Phone paramPhone, String paramString1, String paramString2, String paramString3, String paramString4)
  {
    try
    {
      mRegistry.notifyPreciseDataConnectionFailed(paramString1, paramString2, paramString3, paramString4);
    }
    catch (RemoteException paramPhone) {}
  }
  
  public void notifyServiceState(Phone paramPhone)
  {
    ServiceState localServiceState = paramPhone.getServiceState();
    int i = paramPhone.getPhoneId();
    int j = paramPhone.getSubId();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("nofityServiceState: mRegistry=");
    localStringBuilder.append(mRegistry);
    localStringBuilder.append(" ss=");
    localStringBuilder.append(localServiceState);
    localStringBuilder.append(" sender=");
    localStringBuilder.append(paramPhone);
    localStringBuilder.append(" phondId=");
    localStringBuilder.append(i);
    localStringBuilder.append(" subId=");
    localStringBuilder.append(j);
    Rlog.d("DefaultPhoneNotifier", localStringBuilder.toString());
    paramPhone = localServiceState;
    if (localServiceState == null)
    {
      paramPhone = new ServiceState();
      paramPhone.setStateOutOfService();
    }
    try
    {
      if (mRegistry != null) {
        mRegistry.notifyServiceStateForPhoneId(i, j, paramPhone);
      }
    }
    catch (RemoteException paramPhone) {}
  }
  
  public void notifySignalStrength(Phone paramPhone)
  {
    int i = paramPhone.getPhoneId();
    int j = paramPhone.getSubId();
    try
    {
      if (mRegistry != null) {
        mRegistry.notifySignalStrengthForPhoneId(i, j, paramPhone.getSignalStrength());
      }
    }
    catch (RemoteException paramPhone) {}
  }
  
  public void notifyUserMobileDataStateChanged(Phone paramPhone, boolean paramBoolean)
  {
    try
    {
      mRegistry.notifyUserMobileDataStateChangedForPhoneId(paramPhone.getPhoneId(), paramPhone.getSubId(), paramBoolean);
    }
    catch (RemoteException paramPhone) {}
  }
  
  public void notifyVoLteServiceStateChanged(Phone paramPhone, VoLteServiceState paramVoLteServiceState)
  {
    try
    {
      mRegistry.notifyVoLteServiceStateChanged(paramVoLteServiceState);
    }
    catch (RemoteException paramPhone) {}
  }
  
  public void notifyVoiceActivationStateChanged(Phone paramPhone, int paramInt)
  {
    try
    {
      mRegistry.notifySimActivationStateChangedForPhoneId(paramPhone.getPhoneId(), paramPhone.getSubId(), 0, paramInt);
    }
    catch (RemoteException paramPhone) {}
  }
}
