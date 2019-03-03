package com.android.internal.telephony.gsm;

import android.telephony.SmsCbCmasInfo;
import android.telephony.SmsCbEtwsInfo;
import java.util.Arrays;

public class SmsCbHeader
{
  static final int FORMAT_ETWS_PRIMARY = 3;
  static final int FORMAT_GSM = 1;
  static final int FORMAT_UMTS = 2;
  private static final int MESSAGE_TYPE_CBS_MESSAGE = 1;
  static final int PDU_HEADER_LENGTH = 6;
  private static final int PDU_LENGTH_ETWS = 56;
  private static final int PDU_LENGTH_GSM = 88;
  private final SmsCbCmasInfo mCmasInfo;
  private final int mDataCodingScheme;
  private final SmsCbEtwsInfo mEtwsInfo;
  private final int mFormat;
  private final int mGeographicalScope;
  private boolean mIsEtws;
  private final int mMessageIdentifier;
  private final int mNrOfPages;
  private final int mPageIndex;
  private final int mSerialNumber;
  
  public SmsCbHeader(byte[] paramArrayOfByte)
    throws IllegalArgumentException
  {
    if ((paramArrayOfByte != null) && (paramArrayOfByte.length >= 6))
    {
      mIsEtws = false;
      int i = paramArrayOfByte.length;
      if (paramArrayOfByte[(i - 1)] == 1) {
        mIsEtws = true;
      }
      paramArrayOfByte = Arrays.copyOf(paramArrayOfByte, i - 1);
      boolean bool1;
      boolean bool2;
      int j;
      int k;
      int m;
      if (paramArrayOfByte.length <= 88)
      {
        mGeographicalScope = ((paramArrayOfByte[0] & 0xC0) >>> 6);
        mSerialNumber = ((paramArrayOfByte[0] & 0xFF) << 8 | paramArrayOfByte[1] & 0xFF);
        mMessageIdentifier = ((paramArrayOfByte[2] & 0xFF) << 8 | paramArrayOfByte[3] & 0xFF);
        if ((isEtwsMessage()) && (paramArrayOfByte.length <= 56))
        {
          mFormat = 3;
          mDataCodingScheme = -1;
          mPageIndex = -1;
          mNrOfPages = -1;
          if ((paramArrayOfByte[4] & 0x1) != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          if ((paramArrayOfByte[5] & 0x80) != 0) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          i = paramArrayOfByte[4];
          if (paramArrayOfByte.length > 6) {
            paramArrayOfByte = Arrays.copyOfRange(paramArrayOfByte, 6, paramArrayOfByte.length);
          } else {
            paramArrayOfByte = null;
          }
          mEtwsInfo = new SmsCbEtwsInfo((i & 0xFE) >>> 1, bool1, bool2, true, paramArrayOfByte);
          mCmasInfo = null;
          return;
        }
        mFormat = 1;
        mDataCodingScheme = (paramArrayOfByte[4] & 0xFF);
        j = (paramArrayOfByte[5] & 0xF0) >>> 4;
        k = paramArrayOfByte[5] & 0xF;
        if ((j != 0) && (k != 0))
        {
          m = j;
          i = k;
          if (j <= k) {}
        }
        else
        {
          m = 1;
          i = 1;
        }
        mPageIndex = m;
        mNrOfPages = i;
      }
      else
      {
        mFormat = 2;
        i = paramArrayOfByte[0];
        if (i != 1) {
          break label518;
        }
        mMessageIdentifier = ((paramArrayOfByte[1] & 0xFF) << 8 | paramArrayOfByte[2] & 0xFF);
        mGeographicalScope = ((paramArrayOfByte[3] & 0xC0) >>> 6);
        mSerialNumber = ((paramArrayOfByte[3] & 0xFF) << 8 | paramArrayOfByte[4] & 0xFF);
        mDataCodingScheme = (paramArrayOfByte[5] & 0xFF);
        mPageIndex = 1;
        mNrOfPages = 1;
      }
      if (isEtwsMessage())
      {
        bool2 = isEtwsEmergencyUserAlert();
        bool1 = isEtwsPopupAlert();
        mEtwsInfo = new SmsCbEtwsInfo(getEtwsWarningType(), bool2, bool1, false, null);
        mCmasInfo = null;
      }
      else if (isCmasMessage())
      {
        k = getCmasMessageClass();
        j = getCmasSeverity();
        i = getCmasUrgency();
        m = getCmasCertainty();
        mEtwsInfo = null;
        mCmasInfo = new SmsCbCmasInfo(k, -1, -1, j, i, m);
      }
      else
      {
        mEtwsInfo = null;
        mCmasInfo = null;
      }
      return;
      label518:
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("Unsupported message type ");
      paramArrayOfByte.append(i);
      throw new IllegalArgumentException(paramArrayOfByte.toString());
    }
    throw new IllegalArgumentException("Illegal PDU");
  }
  
  private int getCmasCertainty()
  {
    int i = mMessageIdentifier;
    switch (i)
    {
    default: 
      switch (i)
      {
      default: 
        return -1;
      }
    case 4372: 
    case 4374: 
    case 4376: 
    case 4378: 
      return 1;
    }
    return 0;
  }
  
  private int getCmasMessageClass()
  {
    switch (mMessageIdentifier)
    {
    default: 
      return -1;
    case 4382: 
    case 4395: 
      return 6;
    case 4381: 
    case 4394: 
      return 5;
    case 4380: 
    case 4393: 
      return 4;
    case 4379: 
    case 4392: 
      return 3;
    case 4373: 
    case 4374: 
    case 4375: 
    case 4376: 
    case 4377: 
    case 4378: 
    case 4386: 
    case 4387: 
    case 4388: 
    case 4389: 
    case 4390: 
    case 4391: 
      return 2;
    case 4371: 
    case 4372: 
    case 4384: 
    case 4385: 
      return 1;
    }
    return 0;
  }
  
  private int getCmasSeverity()
  {
    int i = mMessageIdentifier;
    switch (i)
    {
    default: 
      switch (i)
      {
      default: 
        return -1;
      }
    case 4375: 
    case 4376: 
    case 4377: 
    case 4378: 
      return 1;
    }
    return 0;
  }
  
  private int getCmasUrgency()
  {
    int i = mMessageIdentifier;
    switch (i)
    {
    default: 
      switch (i)
      {
      default: 
        return -1;
      }
    case 4373: 
    case 4374: 
    case 4377: 
    case 4378: 
      return 1;
    }
    return 0;
  }
  
  private int getEtwsWarningType()
  {
    return mMessageIdentifier - 4352;
  }
  
  private boolean isCmasMessage()
  {
    boolean bool;
    if ((mMessageIdentifier >= 4370) && (mMessageIdentifier <= 4399)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isEtwsEmergencyUserAlert()
  {
    boolean bool;
    if ((mSerialNumber & 0x2000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isEtwsMessage()
  {
    boolean bool;
    if ((!mIsEtws) && ((mMessageIdentifier & 0xFFF8) != 4352)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private boolean isEtwsPopupAlert()
  {
    boolean bool;
    if ((mSerialNumber & 0x1000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  SmsCbCmasInfo getCmasInfo()
  {
    return mCmasInfo;
  }
  
  int getDataCodingScheme()
  {
    return mDataCodingScheme;
  }
  
  SmsCbEtwsInfo getEtwsInfo()
  {
    return mEtwsInfo;
  }
  
  int getGeographicalScope()
  {
    return mGeographicalScope;
  }
  
  int getNumberOfPages()
  {
    return mNrOfPages;
  }
  
  int getPageIndex()
  {
    return mPageIndex;
  }
  
  int getSerialNumber()
  {
    return mSerialNumber;
  }
  
  int getServiceCategory()
  {
    return mMessageIdentifier;
  }
  
  boolean isEmergencyMessage()
  {
    boolean bool;
    if ((mMessageIdentifier >= 4352) && (mMessageIdentifier <= 6399)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  boolean isEtwsPrimaryNotification()
  {
    boolean bool;
    if (mFormat == 3) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  boolean isUmtsFormat()
  {
    boolean bool;
    if (mFormat == 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("SmsCbHeader{GS=");
    localStringBuilder.append(mGeographicalScope);
    localStringBuilder.append(", serialNumber=0x");
    localStringBuilder.append(Integer.toHexString(mSerialNumber));
    localStringBuilder.append(", messageIdentifier=0x");
    localStringBuilder.append(Integer.toHexString(mMessageIdentifier));
    localStringBuilder.append(", format=");
    localStringBuilder.append(mFormat);
    localStringBuilder.append(", DCS=0x");
    localStringBuilder.append(Integer.toHexString(mDataCodingScheme));
    localStringBuilder.append(", page ");
    localStringBuilder.append(mPageIndex);
    localStringBuilder.append(" of ");
    localStringBuilder.append(mNrOfPages);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
}
