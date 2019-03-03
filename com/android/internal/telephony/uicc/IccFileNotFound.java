package com.android.internal.telephony.uicc;

public class IccFileNotFound
  extends IccException
{
  IccFileNotFound() {}
  
  IccFileNotFound(int paramInt)
  {
    super(localStringBuilder.toString());
  }
  
  IccFileNotFound(String paramString)
  {
    super(paramString);
  }
}
