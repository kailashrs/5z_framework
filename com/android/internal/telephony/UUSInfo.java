package com.android.internal.telephony;

public class UUSInfo
{
  public static final int UUS_DCS_IA5c = 4;
  public static final int UUS_DCS_OSIHLP = 1;
  public static final int UUS_DCS_RMCF = 3;
  public static final int UUS_DCS_USP = 0;
  public static final int UUS_DCS_X244 = 2;
  public static final int UUS_TYPE1_IMPLICIT = 0;
  public static final int UUS_TYPE1_NOT_REQUIRED = 2;
  public static final int UUS_TYPE1_REQUIRED = 1;
  public static final int UUS_TYPE2_NOT_REQUIRED = 4;
  public static final int UUS_TYPE2_REQUIRED = 3;
  public static final int UUS_TYPE3_NOT_REQUIRED = 6;
  public static final int UUS_TYPE3_REQUIRED = 5;
  private byte[] mUusData;
  private int mUusDcs;
  private int mUusType;
  
  public UUSInfo()
  {
    mUusType = 0;
    mUusDcs = 4;
    mUusData = null;
  }
  
  public UUSInfo(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    mUusType = paramInt1;
    mUusDcs = paramInt2;
    mUusData = paramArrayOfByte;
  }
  
  public int getDcs()
  {
    return mUusDcs;
  }
  
  public int getType()
  {
    return mUusType;
  }
  
  public byte[] getUserData()
  {
    return mUusData;
  }
  
  public void setDcs(int paramInt)
  {
    mUusDcs = paramInt;
  }
  
  public void setType(int paramInt)
  {
    mUusType = paramInt;
  }
  
  public void setUserData(byte[] paramArrayOfByte)
  {
    mUusData = paramArrayOfByte;
  }
}
