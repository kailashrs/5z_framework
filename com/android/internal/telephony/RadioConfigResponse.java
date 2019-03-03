package com.android.internal.telephony;

import android.hardware.radio.V1_0.RadioResponseInfo;
import android.hardware.radio.config.V1_0.IRadioConfigResponse.Stub;
import android.hardware.radio.config.V1_0.SimSlotStatus;
import android.telephony.Rlog;
import java.util.ArrayList;

public class RadioConfigResponse
  extends IRadioConfigResponse.Stub
{
  private static final String TAG = "RadioConfigResponse";
  private final RadioConfig mRadioConfig;
  
  public RadioConfigResponse(RadioConfig paramRadioConfig)
  {
    mRadioConfig = paramRadioConfig;
  }
  
  public void getSimSlotsStatusResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<SimSlotStatus> paramArrayList)
  {
    RILRequest localRILRequest = mRadioConfig.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      paramArrayList = RadioConfig.convertHalSlotStatus(paramArrayList);
      RadioConfig localRadioConfig;
      if (error == 0)
      {
        RadioResponse.sendMessageResponse(mResult, paramArrayList);
        paramRadioResponseInfo = new StringBuilder();
        paramRadioResponseInfo.append(localRILRequest.serialString());
        paramRadioResponseInfo.append("< ");
        localRadioConfig = mRadioConfig;
        paramRadioResponseInfo.append(RadioConfig.requestToString(mRequest));
        paramRadioResponseInfo.append(" ");
        paramRadioResponseInfo.append(paramArrayList.toString());
        Rlog.d("RadioConfigResponse", paramRadioResponseInfo.toString());
      }
      else
      {
        localRILRequest.onError(error, paramArrayList);
        paramArrayList = new StringBuilder();
        paramArrayList.append(localRILRequest.serialString());
        paramArrayList.append("< ");
        localRadioConfig = mRadioConfig;
        paramArrayList.append(RadioConfig.requestToString(mRequest));
        paramArrayList.append(" error ");
        paramArrayList.append(error);
        Rlog.e("RadioConfigResponse", paramArrayList.toString());
      }
    }
    else
    {
      paramArrayList = new StringBuilder();
      paramArrayList.append("getSimSlotsStatusResponse: Error ");
      paramArrayList.append(paramRadioResponseInfo.toString());
      Rlog.e("RadioConfigResponse", paramArrayList.toString());
    }
  }
  
  public void setSimSlotsMappingResponse(RadioResponseInfo paramRadioResponseInfo)
  {
    Object localObject = mRadioConfig.processResponse(paramRadioResponseInfo);
    if (localObject != null)
    {
      RadioConfig localRadioConfig;
      if (error == 0)
      {
        RadioResponse.sendMessageResponse(mResult, null);
        paramRadioResponseInfo = new StringBuilder();
        paramRadioResponseInfo.append(((RILRequest)localObject).serialString());
        paramRadioResponseInfo.append("< ");
        localRadioConfig = mRadioConfig;
        paramRadioResponseInfo.append(RadioConfig.requestToString(mRequest));
        Rlog.d("RadioConfigResponse", paramRadioResponseInfo.toString());
      }
      else
      {
        ((RILRequest)localObject).onError(error, null);
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(((RILRequest)localObject).serialString());
        localStringBuilder.append("< ");
        localRadioConfig = mRadioConfig;
        localStringBuilder.append(RadioConfig.requestToString(mRequest));
        localStringBuilder.append(" error ");
        localStringBuilder.append(error);
        Rlog.e("RadioConfigResponse", localStringBuilder.toString());
      }
    }
    else
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("setSimSlotsMappingResponse: Error ");
      ((StringBuilder)localObject).append(paramRadioResponseInfo.toString());
      Rlog.e("RadioConfigResponse", ((StringBuilder)localObject).toString());
    }
  }
}
