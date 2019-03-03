package com.android.internal.telephony.cat;

import java.io.ByteArrayOutputStream;

abstract class ResponseData
{
  ResponseData() {}
  
  public static void writeLength(ByteArrayOutputStream paramByteArrayOutputStream, int paramInt)
  {
    if (paramInt > 127) {
      paramByteArrayOutputStream.write(129);
    }
    paramByteArrayOutputStream.write(paramInt);
  }
  
  public abstract void format(ByteArrayOutputStream paramByteArrayOutputStream);
}
