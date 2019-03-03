package com.android.internal.telephony.metrics;

import com.android.internal.telephony.nano.TelephonyProto.ImsCapabilities;
import com.android.internal.telephony.nano.TelephonyProto.ImsConnectionState;
import com.android.internal.telephony.nano.TelephonyProto.RilDataCall;
import com.android.internal.telephony.nano.TelephonyProto.SmsSession.Event;
import com.android.internal.telephony.nano.TelephonyProto.SmsSession.Event.CBMessage;
import com.android.internal.telephony.nano.TelephonyProto.TelephonyServiceState;
import com.android.internal.telephony.nano.TelephonyProto.TelephonySettings;

public class SmsSessionEventBuilder
{
  TelephonyProto.SmsSession.Event mEvent = new TelephonyProto.SmsSession.Event();
  
  public SmsSessionEventBuilder(int paramInt)
  {
    mEvent.type = paramInt;
  }
  
  public TelephonyProto.SmsSession.Event build()
  {
    return mEvent;
  }
  
  public SmsSessionEventBuilder setCellBroadcastMessage(TelephonyProto.SmsSession.Event.CBMessage paramCBMessage)
  {
    mEvent.cellBroadcastMessage = paramCBMessage;
    return this;
  }
  
  public SmsSessionEventBuilder setDataCalls(TelephonyProto.RilDataCall[] paramArrayOfRilDataCall)
  {
    mEvent.dataCalls = paramArrayOfRilDataCall;
    return this;
  }
  
  public SmsSessionEventBuilder setDelay(int paramInt)
  {
    mEvent.delay = paramInt;
    return this;
  }
  
  public SmsSessionEventBuilder setErrorCode(int paramInt)
  {
    mEvent.errorCode = paramInt;
    return this;
  }
  
  public SmsSessionEventBuilder setFormat(int paramInt)
  {
    mEvent.format = paramInt;
    return this;
  }
  
  public SmsSessionEventBuilder setImsCapabilities(TelephonyProto.ImsCapabilities paramImsCapabilities)
  {
    mEvent.imsCapabilities = paramImsCapabilities;
    return this;
  }
  
  public SmsSessionEventBuilder setImsConnectionState(TelephonyProto.ImsConnectionState paramImsConnectionState)
  {
    mEvent.imsConnectionState = paramImsConnectionState;
    return this;
  }
  
  public SmsSessionEventBuilder setRilErrno(int paramInt)
  {
    mEvent.error = paramInt;
    return this;
  }
  
  public SmsSessionEventBuilder setRilRequestId(int paramInt)
  {
    mEvent.rilRequestId = paramInt;
    return this;
  }
  
  public SmsSessionEventBuilder setServiceState(TelephonyProto.TelephonyServiceState paramTelephonyServiceState)
  {
    mEvent.serviceState = paramTelephonyServiceState;
    return this;
  }
  
  public SmsSessionEventBuilder setSettings(TelephonyProto.TelephonySettings paramTelephonySettings)
  {
    mEvent.settings = paramTelephonySettings;
    return this;
  }
  
  public SmsSessionEventBuilder setTech(int paramInt)
  {
    mEvent.tech = paramInt;
    return this;
  }
}
