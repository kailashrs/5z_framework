package com.android.internal.telephony;

import android.hardware.radio.deprecated.V1_0.IOemHookIndication.Stub;
import android.os.AsyncResult;
import android.os.Registrant;
import com.android.internal.telephony.uicc.IccUtils;
import java.util.ArrayList;

public class OemHookIndication
  extends IOemHookIndication.Stub
{
  RIL mRil;
  
  public OemHookIndication(RIL paramRIL)
  {
    mRil = paramRIL;
  }
  
  public void oemHookRaw(int paramInt, ArrayList<Byte> paramArrayList)
  {
    mRil.processIndication(paramInt);
    paramArrayList = RIL.arrayListToPrimitiveArray(paramArrayList);
    mRil.unsljLogvRet(1028, IccUtils.bytesToHexString(paramArrayList));
    if (mRil.mUnsolOemHookRawRegistrant != null) {
      mRil.mUnsolOemHookRawRegistrant.notifyRegistrant(new AsyncResult(null, paramArrayList, null));
    }
  }
}
