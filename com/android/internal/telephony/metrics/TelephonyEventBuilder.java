package com.android.internal.telephony.metrics;

import android.os.SystemClock;
import com.android.internal.telephony.nano.TelephonyProto.ImsCapabilities;
import com.android.internal.telephony.nano.TelephonyProto.ImsConnectionState;
import com.android.internal.telephony.nano.TelephonyProto.RilDataCall;
import com.android.internal.telephony.nano.TelephonyProto.TelephonyEvent;
import com.android.internal.telephony.nano.TelephonyProto.TelephonyEvent.CarrierIdMatching;
import com.android.internal.telephony.nano.TelephonyProto.TelephonyEvent.CarrierKeyChange;
import com.android.internal.telephony.nano.TelephonyProto.TelephonyEvent.ModemRestart;
import com.android.internal.telephony.nano.TelephonyProto.TelephonyEvent.RilDeactivateDataCall;
import com.android.internal.telephony.nano.TelephonyProto.TelephonyEvent.RilSetupDataCall;
import com.android.internal.telephony.nano.TelephonyProto.TelephonyEvent.RilSetupDataCallResponse;
import com.android.internal.telephony.nano.TelephonyProto.TelephonyServiceState;
import com.android.internal.telephony.nano.TelephonyProto.TelephonySettings;

public class TelephonyEventBuilder
{
  private final TelephonyProto.TelephonyEvent mEvent = new TelephonyProto.TelephonyEvent();
  
  public TelephonyEventBuilder(int paramInt)
  {
    this(SystemClock.elapsedRealtime(), paramInt);
  }
  
  public TelephonyEventBuilder(long paramLong, int paramInt)
  {
    mEvent.timestampMillis = paramLong;
    mEvent.phoneId = paramInt;
  }
  
  public TelephonyProto.TelephonyEvent build()
  {
    return mEvent;
  }
  
  public TelephonyEventBuilder setCarrierIdMatching(TelephonyProto.TelephonyEvent.CarrierIdMatching paramCarrierIdMatching)
  {
    mEvent.type = 13;
    mEvent.carrierIdMatching = paramCarrierIdMatching;
    return this;
  }
  
  public TelephonyEventBuilder setCarrierKeyChange(TelephonyProto.TelephonyEvent.CarrierKeyChange paramCarrierKeyChange)
  {
    mEvent.type = 14;
    mEvent.carrierKeyChange = paramCarrierKeyChange;
    return this;
  }
  
  public TelephonyEventBuilder setDataCalls(TelephonyProto.RilDataCall[] paramArrayOfRilDataCall)
  {
    mEvent.type = 7;
    mEvent.dataCalls = paramArrayOfRilDataCall;
    return this;
  }
  
  public TelephonyEventBuilder setDataStallRecoveryAction(int paramInt)
  {
    mEvent.type = 10;
    mEvent.dataStallAction = paramInt;
    return this;
  }
  
  public TelephonyEventBuilder setDeactivateDataCall(TelephonyProto.TelephonyEvent.RilDeactivateDataCall paramRilDeactivateDataCall)
  {
    mEvent.type = 8;
    mEvent.deactivateDataCall = paramRilDeactivateDataCall;
    return this;
  }
  
  public TelephonyEventBuilder setDeactivateDataCallResponse(int paramInt)
  {
    mEvent.type = 9;
    mEvent.error = paramInt;
    return this;
  }
  
  public TelephonyEventBuilder setImsCapabilities(TelephonyProto.ImsCapabilities paramImsCapabilities)
  {
    mEvent.type = 4;
    mEvent.imsCapabilities = paramImsCapabilities;
    return this;
  }
  
  public TelephonyEventBuilder setImsConnectionState(TelephonyProto.ImsConnectionState paramImsConnectionState)
  {
    mEvent.type = 3;
    mEvent.imsConnectionState = paramImsConnectionState;
    return this;
  }
  
  public TelephonyEventBuilder setModemRestart(TelephonyProto.TelephonyEvent.ModemRestart paramModemRestart)
  {
    mEvent.type = 11;
    mEvent.modemRestart = paramModemRestart;
    return this;
  }
  
  public TelephonyEventBuilder setNITZ(long paramLong)
  {
    mEvent.type = 12;
    mEvent.nitzTimestampMillis = paramLong;
    return this;
  }
  
  public TelephonyEventBuilder setServiceState(TelephonyProto.TelephonyServiceState paramTelephonyServiceState)
  {
    mEvent.type = 2;
    mEvent.serviceState = paramTelephonyServiceState;
    return this;
  }
  
  public TelephonyEventBuilder setSettings(TelephonyProto.TelephonySettings paramTelephonySettings)
  {
    mEvent.type = 1;
    mEvent.settings = paramTelephonySettings;
    return this;
  }
  
  public TelephonyEventBuilder setSetupDataCall(TelephonyProto.TelephonyEvent.RilSetupDataCall paramRilSetupDataCall)
  {
    mEvent.type = 5;
    mEvent.setupDataCall = paramRilSetupDataCall;
    return this;
  }
  
  public TelephonyEventBuilder setSetupDataCallResponse(TelephonyProto.TelephonyEvent.RilSetupDataCallResponse paramRilSetupDataCallResponse)
  {
    mEvent.type = 6;
    mEvent.setupDataCallResponse = paramRilSetupDataCallResponse;
    return this;
  }
}
