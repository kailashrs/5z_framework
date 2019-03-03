package com.google.android.mms.pdu;

import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class EncodedStringValue
  implements Cloneable
{
  private static final boolean DEBUG = false;
  private static final boolean LOCAL_LOGV = false;
  private static final String TAG = "EncodedStringValue";
  private int mCharacterSet;
  private byte[] mData;
  
  public EncodedStringValue(int paramInt, byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte != null)
    {
      mCharacterSet = paramInt;
      mData = new byte[paramArrayOfByte.length];
      System.arraycopy(paramArrayOfByte, 0, mData, 0, paramArrayOfByte.length);
      return;
    }
    throw new NullPointerException("EncodedStringValue: Text-string is null.");
  }
  
  public EncodedStringValue(String paramString)
  {
    try
    {
      mData = paramString.getBytes("utf-8");
      mCharacterSet = 106;
    }
    catch (UnsupportedEncodingException paramString)
    {
      Log.e("EncodedStringValue", "Default encoding must be supported.", paramString);
    }
  }
  
  public EncodedStringValue(byte[] paramArrayOfByte)
  {
    this(106, paramArrayOfByte);
  }
  
  public static String concat(EncodedStringValue[] paramArrayOfEncodedStringValue)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int i = paramArrayOfEncodedStringValue.length - 1;
    for (int j = 0; j <= i; j++)
    {
      localStringBuilder.append(paramArrayOfEncodedStringValue[j].getString());
      if (j < i) {
        localStringBuilder.append(";");
      }
    }
    return localStringBuilder.toString();
  }
  
  public static EncodedStringValue copy(EncodedStringValue paramEncodedStringValue)
  {
    if (paramEncodedStringValue == null) {
      return null;
    }
    return new EncodedStringValue(mCharacterSet, mData);
  }
  
  public static EncodedStringValue[] encodeStrings(String[] paramArrayOfString)
  {
    int i = paramArrayOfString.length;
    if (i > 0)
    {
      EncodedStringValue[] arrayOfEncodedStringValue = new EncodedStringValue[i];
      for (int j = 0; j < i; j++) {
        arrayOfEncodedStringValue[j] = new EncodedStringValue(paramArrayOfString[j]);
      }
      return arrayOfEncodedStringValue;
    }
    return null;
  }
  
  public static EncodedStringValue[] extract(String paramString)
  {
    String[] arrayOfString = paramString.split(";");
    paramString = new ArrayList();
    for (int i = 0; i < arrayOfString.length; i++) {
      if (arrayOfString[i].length() > 0) {
        paramString.add(new EncodedStringValue(arrayOfString[i]));
      }
    }
    i = paramString.size();
    if (i > 0) {
      return (EncodedStringValue[])paramString.toArray(new EncodedStringValue[i]);
    }
    return null;
  }
  
  public void appendTextString(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte != null)
    {
      ByteArrayOutputStream localByteArrayOutputStream;
      if (mData == null)
      {
        mData = new byte[paramArrayOfByte.length];
        System.arraycopy(paramArrayOfByte, 0, mData, 0, paramArrayOfByte.length);
      }
      else
      {
        localByteArrayOutputStream = new ByteArrayOutputStream();
      }
      try
      {
        localByteArrayOutputStream.write(mData);
        localByteArrayOutputStream.write(paramArrayOfByte);
        mData = localByteArrayOutputStream.toByteArray();
        return;
      }
      catch (IOException paramArrayOfByte)
      {
        paramArrayOfByte.printStackTrace();
        throw new NullPointerException("appendTextString: failed when write a new Text-string");
      }
    }
    throw new NullPointerException("Text-string is null.");
  }
  
  public Object clone()
    throws CloneNotSupportedException
  {
    super.clone();
    int i = mData.length;
    Object localObject = new byte[i];
    System.arraycopy(mData, 0, (byte[])localObject, 0, i);
    try
    {
      localObject = new EncodedStringValue(mCharacterSet, (byte[])localObject);
      return localObject;
    }
    catch (Exception localException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("failed to clone an EncodedStringValue: ");
      localStringBuilder.append(this);
      Log.e("EncodedStringValue", localStringBuilder.toString());
      localException.printStackTrace();
      throw new CloneNotSupportedException(localException.getMessage());
    }
  }
  
  public int getCharacterSet()
  {
    return mCharacterSet;
  }
  
  public String getString()
  {
    if (mCharacterSet == 0) {
      return new String(mData);
    }
    try
    {
      String str1 = CharacterSets.getMimeName(mCharacterSet);
      str1 = new String(mData, str1);
      return str1;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException1)
    {
      try
      {
        String str2 = new String(mData, "iso-8859-1");
        return str2;
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException2) {}
    }
    return new String(mData);
  }
  
  public byte[] getTextString()
  {
    byte[] arrayOfByte = new byte[mData.length];
    System.arraycopy(mData, 0, arrayOfByte, 0, mData.length);
    return arrayOfByte;
  }
  
  public void setCharacterSet(int paramInt)
  {
    mCharacterSet = paramInt;
  }
  
  public void setTextString(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte != null)
    {
      mData = new byte[paramArrayOfByte.length];
      System.arraycopy(paramArrayOfByte, 0, mData, 0, paramArrayOfByte.length);
      return;
    }
    throw new NullPointerException("EncodedStringValue: Text-string is null.");
  }
  
  public EncodedStringValue[] split(String paramString)
  {
    paramString = getString().split(paramString);
    EncodedStringValue[] arrayOfEncodedStringValue = new EncodedStringValue[paramString.length];
    int i = 0;
    while (i < arrayOfEncodedStringValue.length) {
      try
      {
        arrayOfEncodedStringValue[i] = new EncodedStringValue(mCharacterSet, paramString[i].getBytes());
        i++;
      }
      catch (NullPointerException paramString)
      {
        return null;
      }
    }
    return arrayOfEncodedStringValue;
  }
}
