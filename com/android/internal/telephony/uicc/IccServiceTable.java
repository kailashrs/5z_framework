package com.android.internal.telephony.uicc;

import android.telephony.Rlog;

public abstract class IccServiceTable
{
  protected final byte[] mServiceTable;
  
  protected IccServiceTable(byte[] paramArrayOfByte)
  {
    mServiceTable = paramArrayOfByte;
  }
  
  protected abstract String getTag();
  
  protected abstract Object[] getValues();
  
  protected boolean isAvailable(int paramInt)
  {
    int i = paramInt / 8;
    int j = mServiceTable.length;
    boolean bool = false;
    if (i >= j)
    {
      String str = getTag();
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("isAvailable for service ");
      localStringBuilder.append(paramInt + 1);
      localStringBuilder.append(" fails, max service is ");
      localStringBuilder.append(mServiceTable.length * 8);
      Rlog.e(str, localStringBuilder.toString());
      return false;
    }
    if ((mServiceTable[i] & 1 << paramInt % 8) != 0) {
      bool = true;
    }
    return bool;
  }
  
  public String toString()
  {
    Object[] arrayOfObject = getValues();
    int i = mServiceTable.length;
    StringBuilder localStringBuilder = new StringBuilder(getTag());
    localStringBuilder.append('[');
    localStringBuilder.append(i * 8);
    localStringBuilder = localStringBuilder.append("]={ ");
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      int m = mServiceTable[k];
      int n = 0;
      while (n < 8)
      {
        int i1 = j;
        if ((1 << n & m) != 0)
        {
          if (j != 0) {
            localStringBuilder.append(", ");
          } else {
            j = 1;
          }
          i1 = k * 8 + n;
          if (i1 < arrayOfObject.length)
          {
            localStringBuilder.append(arrayOfObject[i1]);
            i1 = j;
          }
          else
          {
            localStringBuilder.append('#');
            localStringBuilder.append(i1 + 1);
            i1 = j;
          }
        }
        n++;
        j = i1;
      }
    }
    localStringBuilder.append(" }");
    return localStringBuilder.toString();
  }
}
