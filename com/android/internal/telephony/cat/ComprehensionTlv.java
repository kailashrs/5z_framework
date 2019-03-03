package com.android.internal.telephony.cat;

import android.telephony.Rlog;
import java.util.ArrayList;
import java.util.List;

public class ComprehensionTlv
{
  private static final String LOG_TAG = "ComprehensionTlv";
  private boolean mCr;
  private int mLength;
  private byte[] mRawValue;
  private int mTag;
  private int mValueIndex;
  
  protected ComprehensionTlv(int paramInt1, boolean paramBoolean, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
  {
    mTag = paramInt1;
    mCr = paramBoolean;
    mLength = paramInt2;
    mValueIndex = paramInt3;
    mRawValue = paramArrayOfByte;
  }
  
  public static ComprehensionTlv decode(byte[] paramArrayOfByte, int paramInt)
    throws ResultException
  {
    int i = paramArrayOfByte.length;
    int j = paramInt + 1;
    int k = paramArrayOfByte[paramInt] & 0xFF;
    boolean bool1;
    boolean bool2;
    if ((k != 0) && (k != 255))
    {
      bool1 = false;
      bool2 = false;
    }
    int m;
    switch (k)
    {
    default: 
      if ((k & 0x80) != 0) {
        bool2 = true;
      }
      m = k & 0xFF7F;
    case 127: 
      for (k = j;; k = j + 2)
      {
        break;
        n = paramArrayOfByte[j];
        k = paramArrayOfByte[(j + 1)];
        k = (n & 0xFF) << 8 | k & 0xFF;
        bool2 = bool1;
        if ((0x8000 & k) != 0) {
          bool2 = true;
        }
        m = 0xFFFF7FFF & k;
      }
      j = k + 1;
      k = paramArrayOfByte[k];
      int n = k & 0xFF;
      if (n < 128)
      {
        k = j;
      }
      else if (n == 129)
      {
        k = j + 1;
        n = 0xFF & paramArrayOfByte[j];
        if (n < 128) {
          try
          {
            localObject1 = new com/android/internal/telephony/cat/ResultException;
            paramArrayOfByte = ResultCode.CMD_DATA_NOT_UNDERSTOOD;
            localObject2 = new java/lang/StringBuilder;
            ((StringBuilder)localObject2).<init>();
            ((StringBuilder)localObject2).append("length < 0x80 length=");
            ((StringBuilder)localObject2).append(Integer.toHexString(n));
            ((StringBuilder)localObject2).append(" startIndex=");
            ((StringBuilder)localObject2).append(paramInt);
            ((StringBuilder)localObject2).append(" curIndex=");
            ((StringBuilder)localObject2).append(k);
            ((StringBuilder)localObject2).append(" endIndex=");
            ((StringBuilder)localObject2).append(i);
            ((ResultException)localObject1).<init>(paramArrayOfByte, ((StringBuilder)localObject2).toString());
            throw ((Throwable)localObject1);
          }
          catch (IndexOutOfBoundsException paramArrayOfByte)
          {
            j = k;
          }
        }
      }
      else
      {
        if (n == 130)
        {
          k = paramArrayOfByte[j];
          n = paramArrayOfByte[(j + 1)];
          n = (k & 0xFF) << 8 | 0xFF & n;
          k = j + 2;
          if (n < 256) {}
        }
        for (j = n;; j = n)
        {
          n = j;
          break;
          j = k;
          try
          {
            localObject1 = new com/android/internal/telephony/cat/ResultException;
            j = k;
            paramArrayOfByte = ResultCode.CMD_DATA_NOT_UNDERSTOOD;
            j = k;
            localObject2 = new java/lang/StringBuilder;
            j = k;
            ((StringBuilder)localObject2).<init>();
            j = k;
            ((StringBuilder)localObject2).append("two byte length < 0x100 length=");
            j = k;
            ((StringBuilder)localObject2).append(Integer.toHexString(n));
            j = k;
            ((StringBuilder)localObject2).append(" startIndex=");
            j = k;
            ((StringBuilder)localObject2).append(paramInt);
            j = k;
            ((StringBuilder)localObject2).append(" curIndex=");
            j = k;
            ((StringBuilder)localObject2).append(k);
            j = k;
            ((StringBuilder)localObject2).append(" endIndex=");
            j = k;
            ((StringBuilder)localObject2).append(i);
            j = k;
            ((ResultException)localObject1).<init>(paramArrayOfByte, ((StringBuilder)localObject2).toString());
            j = k;
            throw ((Throwable)localObject1);
          }
          catch (IndexOutOfBoundsException paramArrayOfByte) {}
          if (n != 131) {
            break label754;
          }
          k = paramArrayOfByte[j];
          int i1 = paramArrayOfByte[(j + 1)];
          n = paramArrayOfByte[(j + 2)];
          n = (k & 0xFF) << 16 | (i1 & 0xFF) << 8 | 0xFF & n;
          k = j + 3;
          if (n < 65536) {
            break label614;
          }
        }
      }
      j = k;
      return new ComprehensionTlv(m, bool2, n, paramArrayOfByte, k);
      j = k;
      localObject1 = new com/android/internal/telephony/cat/ResultException;
      j = k;
      paramArrayOfByte = ResultCode.CMD_DATA_NOT_UNDERSTOOD;
      j = k;
      Object localObject2 = new java/lang/StringBuilder;
      j = k;
      ((StringBuilder)localObject2).<init>();
      j = k;
      ((StringBuilder)localObject2).append("three byte length < 0x10000 length=0x");
      j = k;
      ((StringBuilder)localObject2).append(Integer.toHexString(n));
      j = k;
      ((StringBuilder)localObject2).append(" startIndex=");
      j = k;
      ((StringBuilder)localObject2).append(paramInt);
      j = k;
      ((StringBuilder)localObject2).append(" curIndex=");
      j = k;
      ((StringBuilder)localObject2).append(k);
      j = k;
      ((StringBuilder)localObject2).append(" endIndex=");
      j = k;
      ((StringBuilder)localObject2).append(i);
      j = k;
      ((ResultException)localObject1).<init>(paramArrayOfByte, ((StringBuilder)localObject2).toString());
      j = k;
      throw ((Throwable)localObject1);
      try
      {
        paramArrayOfByte = new com/android/internal/telephony/cat/ResultException;
        localObject2 = ResultCode.CMD_DATA_NOT_UNDERSTOOD;
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        ((StringBuilder)localObject1).append("Bad length modifer=");
        ((StringBuilder)localObject1).append(n);
        ((StringBuilder)localObject1).append(" startIndex=");
        ((StringBuilder)localObject1).append(paramInt);
        ((StringBuilder)localObject1).append(" curIndex=");
        ((StringBuilder)localObject1).append(j);
        ((StringBuilder)localObject1).append(" endIndex=");
        ((StringBuilder)localObject1).append(i);
        paramArrayOfByte.<init>((ResultCode)localObject2, ((StringBuilder)localObject1).toString());
        throw paramArrayOfByte;
      }
      catch (IndexOutOfBoundsException paramArrayOfByte) {}
    case 128: 
      try
      {
        label614:
        label754:
        paramArrayOfByte = new java/lang/StringBuilder;
        paramArrayOfByte.<init>();
        paramArrayOfByte.append("decode: unexpected first tag byte=");
        paramArrayOfByte.append(Integer.toHexString(k));
        paramArrayOfByte.append(", startIndex=");
        paramArrayOfByte.append(paramInt);
        paramArrayOfByte.append(" curIndex=");
        paramArrayOfByte.append(j);
        paramArrayOfByte.append(" endIndex=");
        paramArrayOfByte.append(i);
        Rlog.d("CAT     ", paramArrayOfByte.toString());
        return null;
      }
      catch (IndexOutOfBoundsException paramArrayOfByte) {}
    }
    paramArrayOfByte = ResultCode.CMD_DATA_NOT_UNDERSTOOD;
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("IndexOutOfBoundsException startIndex=");
    ((StringBuilder)localObject1).append(paramInt);
    ((StringBuilder)localObject1).append(" curIndex=");
    ((StringBuilder)localObject1).append(j);
    ((StringBuilder)localObject1).append(" endIndex=");
    ((StringBuilder)localObject1).append(i);
    throw new ResultException(paramArrayOfByte, ((StringBuilder)localObject1).toString());
  }
  
  public static List<ComprehensionTlv> decodeMany(byte[] paramArrayOfByte, int paramInt)
    throws ResultException
  {
    ArrayList localArrayList = new ArrayList();
    int i = paramArrayOfByte.length;
    while (paramInt < i)
    {
      ComprehensionTlv localComprehensionTlv = decode(paramArrayOfByte, paramInt);
      if (localComprehensionTlv != null)
      {
        localArrayList.add(localComprehensionTlv);
        paramInt = mValueIndex + mLength;
      }
      else
      {
        CatLog.d("ComprehensionTlv", "decodeMany: ctlv is null, stop decoding");
      }
    }
    return localArrayList;
  }
  
  public int getLength()
  {
    return mLength;
  }
  
  public byte[] getRawValue()
  {
    return mRawValue;
  }
  
  public int getTag()
  {
    return mTag;
  }
  
  public int getValueIndex()
  {
    return mValueIndex;
  }
  
  public boolean isComprehensionRequired()
  {
    return mCr;
  }
}
