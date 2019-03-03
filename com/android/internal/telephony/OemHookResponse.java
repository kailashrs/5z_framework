package com.android.internal.telephony;

import android.hardware.radio.V1_0.RadioResponseInfo;
import android.hardware.radio.deprecated.V1_0.IOemHookResponse.Stub;
import java.util.ArrayList;

public class OemHookResponse
  extends IOemHookResponse.Stub
{
  RIL mRil;
  
  public OemHookResponse(RIL paramRIL)
  {
    mRil = paramRIL;
  }
  
  public void sendRequestRawResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<Byte> paramArrayList)
  {
    RILRequest localRILRequest = mRil.processResponse(paramRadioResponseInfo);
    if (localRILRequest != null)
    {
      byte[] arrayOfByte = null;
      if (error == 0)
      {
        arrayOfByte = RIL.arrayListToPrimitiveArray(paramArrayList);
        RadioResponse.sendMessageResponse(mResult, arrayOfByte);
      }
      mRil.processResponseDone(localRILRequest, paramRadioResponseInfo, arrayOfByte);
    }
  }
  
  public void sendRequestStringsResponse(RadioResponseInfo paramRadioResponseInfo, ArrayList<String> paramArrayList)
  {
    RadioResponse.responseStringArrayList(mRil, paramRadioResponseInfo, paramArrayList);
  }
}
