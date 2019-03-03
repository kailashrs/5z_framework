package com.android.internal.telephony.cat;

import java.io.ByteArrayOutputStream;

class SelectItemResponseData
  extends ResponseData
{
  private int mId;
  
  public SelectItemResponseData(int paramInt)
  {
    mId = paramInt;
  }
  
  public void format(ByteArrayOutputStream paramByteArrayOutputStream)
  {
    paramByteArrayOutputStream.write(ComprehensionTlvTag.ITEM_ID.value() | 0x80);
    paramByteArrayOutputStream.write(1);
    paramByteArrayOutputStream.write(mId);
  }
}
