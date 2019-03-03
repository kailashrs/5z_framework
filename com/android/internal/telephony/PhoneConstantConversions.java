package com.android.internal.telephony;

public class PhoneConstantConversions
{
  public PhoneConstantConversions() {}
  
  public static int convertCallState(PhoneConstants.State paramState)
  {
    switch (1.$SwitchMap$com$android$internal$telephony$PhoneConstants$State[paramState.ordinal()])
    {
    default: 
      return 0;
    case 2: 
      return 2;
    }
    return 1;
  }
  
  public static PhoneConstants.State convertCallState(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return PhoneConstants.State.IDLE;
    case 2: 
      return PhoneConstants.State.OFFHOOK;
    }
    return PhoneConstants.State.RINGING;
  }
  
  public static int convertDataState(PhoneConstants.DataState paramDataState)
  {
    switch (1.$SwitchMap$com$android$internal$telephony$PhoneConstants$DataState[paramDataState.ordinal()])
    {
    default: 
      return 0;
    case 3: 
      return 3;
    case 2: 
      return 2;
    }
    return 1;
  }
  
  public static PhoneConstants.DataState convertDataState(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return PhoneConstants.DataState.DISCONNECTED;
    case 3: 
      return PhoneConstants.DataState.SUSPENDED;
    case 2: 
      return PhoneConstants.DataState.CONNECTED;
    }
    return PhoneConstants.DataState.CONNECTING;
  }
}
