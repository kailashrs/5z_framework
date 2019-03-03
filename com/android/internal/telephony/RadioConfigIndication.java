package com.android.internal.telephony;

import android.hardware.radio.config.V1_0.IRadioConfigIndication.Stub;
import android.hardware.radio.config.V1_0.SimSlotStatus;
import android.os.AsyncResult;
import android.os.Registrant;
import android.telephony.Rlog;
import java.util.ArrayList;

public class RadioConfigIndication
  extends IRadioConfigIndication.Stub
{
  private static final String TAG = "RadioConfigIndication";
  private final RadioConfig mRadioConfig;
  
  public RadioConfigIndication(RadioConfig paramRadioConfig)
  {
    mRadioConfig = paramRadioConfig;
  }
  
  public void simSlotsStatusChanged(int paramInt, ArrayList<SimSlotStatus> paramArrayList)
  {
    ArrayList localArrayList = RadioConfig.convertHalSlotStatus(paramArrayList);
    paramArrayList = new StringBuilder();
    paramArrayList.append("[UNSL]<  UNSOL_SIM_SLOT_STATUS_CHANGED ");
    paramArrayList.append(localArrayList.toString());
    Rlog.d("RadioConfigIndication", paramArrayList.toString());
    if (mRadioConfig.mSimSlotStatusRegistrant != null) {
      mRadioConfig.mSimSlotStatusRegistrant.notifyRegistrant(new AsyncResult(null, localArrayList, null));
    }
  }
}
