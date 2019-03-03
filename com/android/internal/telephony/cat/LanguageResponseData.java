package com.android.internal.telephony.cat;

import com.android.internal.telephony.GsmAlphabet;
import java.io.ByteArrayOutputStream;

class LanguageResponseData
  extends ResponseData
{
  private String mLang;
  
  public LanguageResponseData(String paramString)
  {
    mLang = paramString;
  }
  
  public void format(ByteArrayOutputStream paramByteArrayOutputStream)
  {
    if (paramByteArrayOutputStream == null) {
      return;
    }
    paramByteArrayOutputStream.write(0x80 | ComprehensionTlvTag.LANGUAGE.value());
    Object localObject = mLang;
    int i = 0;
    if ((localObject != null) && (mLang.length() > 0)) {
      localObject = GsmAlphabet.stringToGsm8BitPacked(mLang);
    } else {
      localObject = new byte[0];
    }
    paramByteArrayOutputStream.write(localObject.length);
    int j = localObject.length;
    while (i < j)
    {
      paramByteArrayOutputStream.write(localObject[i]);
      i++;
    }
  }
}
