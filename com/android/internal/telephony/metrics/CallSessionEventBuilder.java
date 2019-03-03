package com.android.internal.telephony.metrics;

import com.android.internal.telephony.nano.TelephonyProto.ImsCapabilities;
import com.android.internal.telephony.nano.TelephonyProto.ImsConnectionState;
import com.android.internal.telephony.nano.TelephonyProto.ImsReasonInfo;
import com.android.internal.telephony.nano.TelephonyProto.RilDataCall;
import com.android.internal.telephony.nano.TelephonyProto.TelephonyCallSession.Event;
import com.android.internal.telephony.nano.TelephonyProto.TelephonyCallSession.Event.RilCall;
import com.android.internal.telephony.nano.TelephonyProto.TelephonyServiceState;
import com.android.internal.telephony.nano.TelephonyProto.TelephonySettings;

public class CallSessionEventBuilder
{
  private final TelephonyProto.TelephonyCallSession.Event mEvent = new TelephonyProto.TelephonyCallSession.Event();
  
  public CallSessionEventBuilder(int paramInt)
  {
    mEvent.type = paramInt;
  }
  
  public TelephonyProto.TelephonyCallSession.Event build()
  {
    return mEvent;
  }
  
  public CallSessionEventBuilder setCallIndex(int paramInt)
  {
    mEvent.callIndex = paramInt;
    return this;
  }
  
  public CallSessionEventBuilder setCallState(int paramInt)
  {
    mEvent.callState = paramInt;
    return this;
  }
  
  public CallSessionEventBuilder setDataCalls(TelephonyProto.RilDataCall[] paramArrayOfRilDataCall)
  {
    mEvent.dataCalls = paramArrayOfRilDataCall;
    return this;
  }
  
  public CallSessionEventBuilder setDelay(int paramInt)
  {
    mEvent.delay = paramInt;
    return this;
  }
  
  public CallSessionEventBuilder setImsCapabilities(TelephonyProto.ImsCapabilities paramImsCapabilities)
  {
    mEvent.imsCapabilities = paramImsCapabilities;
    return this;
  }
  
  public CallSessionEventBuilder setImsCommand(int paramInt)
  {
    mEvent.imsCommand = paramInt;
    return this;
  }
  
  public CallSessionEventBuilder setImsConnectionState(TelephonyProto.ImsConnectionState paramImsConnectionState)
  {
    mEvent.imsConnectionState = paramImsConnectionState;
    return this;
  }
  
  public CallSessionEventBuilder setImsReasonInfo(TelephonyProto.ImsReasonInfo paramImsReasonInfo)
  {
    mEvent.reasonInfo = paramImsReasonInfo;
    return this;
  }
  
  public CallSessionEventBuilder setNITZ(long paramLong)
  {
    mEvent.nitzTimestampMillis = paramLong;
    return this;
  }
  
  public CallSessionEventBuilder setPhoneState(int paramInt)
  {
    mEvent.phoneState = paramInt;
    return this;
  }
  
  public CallSessionEventBuilder setRilCalls(TelephonyProto.TelephonyCallSession.Event.RilCall[] paramArrayOfRilCall)
  {
    mEvent.calls = paramArrayOfRilCall;
    return this;
  }
  
  public CallSessionEventBuilder setRilError(int paramInt)
  {
    mEvent.error = paramInt;
    return this;
  }
  
  public CallSessionEventBuilder setRilRequest(int paramInt)
  {
    mEvent.rilRequest = paramInt;
    return this;
  }
  
  public CallSessionEventBuilder setRilRequestId(int paramInt)
  {
    mEvent.rilRequestId = paramInt;
    return this;
  }
  
  public CallSessionEventBuilder setServiceState(TelephonyProto.TelephonyServiceState paramTelephonyServiceState)
  {
    mEvent.serviceState = paramTelephonyServiceState;
    return this;
  }
  
  public CallSessionEventBuilder setSettings(TelephonyProto.TelephonySettings paramTelephonySettings)
  {
    mEvent.settings = paramTelephonySettings;
    return this;
  }
  
  public CallSessionEventBuilder setSrcAccessTech(int paramInt)
  {
    mEvent.srcAccessTech = paramInt;
    return this;
  }
  
  public CallSessionEventBuilder setSrvccState(int paramInt)
  {
    mEvent.srvccState = paramInt;
    return this;
  }
  
  public CallSessionEventBuilder setTargetAccessTech(int paramInt)
  {
    mEvent.targetAccessTech = paramInt;
    return this;
  }
}
