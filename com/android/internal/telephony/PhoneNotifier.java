package com.android.internal.telephony;

import android.telephony.CellInfo;
import android.telephony.PhysicalChannelConfig;
import android.telephony.VoLteServiceState;
import java.util.List;

public abstract interface PhoneNotifier
{
  public abstract void notifyCallForwardingChanged(Phone paramPhone);
  
  public abstract void notifyCellInfo(Phone paramPhone, List<CellInfo> paramList);
  
  public abstract void notifyCellLocation(Phone paramPhone);
  
  public abstract void notifyDataActivationStateChanged(Phone paramPhone, int paramInt);
  
  public abstract void notifyDataActivity(Phone paramPhone);
  
  public abstract void notifyDataConnection(Phone paramPhone, String paramString1, String paramString2, PhoneConstants.DataState paramDataState);
  
  public abstract void notifyDataConnectionFailed(Phone paramPhone, String paramString1, String paramString2);
  
  public abstract void notifyDisconnectCause(int paramInt1, int paramInt2);
  
  public abstract void notifyMessageWaitingChanged(Phone paramPhone);
  
  public abstract void notifyOemHookRawEventForSubscriber(int paramInt, byte[] paramArrayOfByte);
  
  public abstract void notifyOtaspChanged(Phone paramPhone, int paramInt);
  
  public abstract void notifyPhoneState(Phone paramPhone);
  
  public abstract void notifyPhysicalChannelConfiguration(Phone paramPhone, List<PhysicalChannelConfig> paramList);
  
  public abstract void notifyPreciseCallState(Phone paramPhone);
  
  public abstract void notifyPreciseDataConnectionFailed(Phone paramPhone, String paramString1, String paramString2, String paramString3, String paramString4);
  
  public abstract void notifyServiceState(Phone paramPhone);
  
  public abstract void notifySignalStrength(Phone paramPhone);
  
  public abstract void notifyUserMobileDataStateChanged(Phone paramPhone, boolean paramBoolean);
  
  public abstract void notifyVoLteServiceStateChanged(Phone paramPhone, VoLteServiceState paramVoLteServiceState);
  
  public abstract void notifyVoiceActivationStateChanged(Phone paramPhone, int paramInt);
}
