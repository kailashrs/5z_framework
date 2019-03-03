package com.android.internal.util;

import android.util.Log;
import java.util.Arrays;

public class ExponentiallyBucketedHistogram
{
  private final int[] mData;
  
  public ExponentiallyBucketedHistogram(int paramInt)
  {
    mData = new int[Preconditions.checkArgumentInRange(paramInt, 1, 31, "numBuckets")];
  }
  
  public void add(int paramInt)
  {
    int[] arrayOfInt;
    if (paramInt <= 0)
    {
      arrayOfInt = mData;
      arrayOfInt[0] += 1;
    }
    else
    {
      arrayOfInt = mData;
      paramInt = Math.min(mData.length - 1, 32 - Integer.numberOfLeadingZeros(paramInt));
      arrayOfInt[paramInt] += 1;
    }
  }
  
  public void log(String paramString, CharSequence paramCharSequence)
  {
    paramCharSequence = new StringBuilder(paramCharSequence);
    paramCharSequence.append('[');
    for (int i = 0; i < mData.length; i++)
    {
      if (i != 0) {
        paramCharSequence.append(", ");
      }
      if (i < mData.length - 1)
      {
        paramCharSequence.append("<");
        paramCharSequence.append(1 << i);
      }
      else
      {
        paramCharSequence.append(">=");
        paramCharSequence.append(1 << i - 1);
      }
      paramCharSequence.append(": ");
      paramCharSequence.append(mData[i]);
    }
    paramCharSequence.append("]");
    Log.d(paramString, paramCharSequence.toString());
  }
  
  public void reset()
  {
    Arrays.fill(mData, 0);
  }
}
