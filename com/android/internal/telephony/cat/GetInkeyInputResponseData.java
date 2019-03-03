package com.android.internal.telephony.cat;

import com.android.internal.telephony.EncodeException;
import com.android.internal.telephony.GsmAlphabet;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

class GetInkeyInputResponseData
  extends ResponseData
{
  protected static final byte GET_INKEY_NO = 0;
  protected static final byte GET_INKEY_YES = 1;
  public String mInData;
  private boolean mIsPacked;
  private boolean mIsUcs2;
  private boolean mIsYesNo;
  private boolean mYesNoResponse;
  
  public GetInkeyInputResponseData(String paramString, boolean paramBoolean1, boolean paramBoolean2)
  {
    mIsUcs2 = paramBoolean1;
    mIsPacked = paramBoolean2;
    mInData = paramString;
    mIsYesNo = false;
  }
  
  public GetInkeyInputResponseData(boolean paramBoolean)
  {
    mIsUcs2 = false;
    mIsPacked = false;
    mInData = "";
    mIsYesNo = true;
    mYesNoResponse = paramBoolean;
  }
  
  public void format(ByteArrayOutputStream paramByteArrayOutputStream)
  {
    if (paramByteArrayOutputStream == null) {
      return;
    }
    paramByteArrayOutputStream.write(0x80 | ComprehensionTlvTag.TEXT_STRING.value());
    boolean bool = mIsYesNo;
    int i = 0;
    byte[] arrayOfByte1;
    byte[] arrayOfByte3;
    if (bool)
    {
      arrayOfByte1 = new byte[1];
      arrayOfByte1[0] = ((byte)mYesNoResponse);
    }
    else if ((mInData != null) && (mInData.length() > 0))
    {
      try
      {
        if (mIsUcs2)
        {
          arrayOfByte1 = mInData.getBytes("UTF-16BE");
        }
        else if (mIsPacked)
        {
          byte[] arrayOfByte4 = GsmAlphabet.stringToGsm7BitPacked(mInData, 0, 0);
          arrayOfByte1 = new byte[arrayOfByte4.length - 1];
          System.arraycopy(arrayOfByte4, 1, arrayOfByte1, 0, arrayOfByte4.length - 1);
        }
        else
        {
          arrayOfByte1 = GsmAlphabet.stringToGsm8BitPacked(mInData);
        }
      }
      catch (EncodeException localEncodeException)
      {
        byte[] arrayOfByte2 = new byte[0];
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        arrayOfByte3 = new byte[0];
      }
    }
    else
    {
      arrayOfByte3 = new byte[0];
    }
    if (arrayOfByte3.length + 1 <= 255) {
      writeLength(paramByteArrayOutputStream, arrayOfByte3.length + 1);
    } else {
      arrayOfByte3 = new byte[0];
    }
    if (mIsUcs2) {
      paramByteArrayOutputStream.write(8);
    } else if (mIsPacked) {
      paramByteArrayOutputStream.write(0);
    } else {
      paramByteArrayOutputStream.write(4);
    }
    int j = arrayOfByte3.length;
    while (i < j)
    {
      paramByteArrayOutputStream.write(arrayOfByte3[i]);
      i++;
    }
  }
}
