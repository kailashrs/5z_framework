package com.android.internal.os;

import java.util.HashMap;

public class KernelWakelockStats
  extends HashMap<String, Entry>
{
  int kernelWakelockVersion;
  
  public KernelWakelockStats() {}
  
  public static class Entry
  {
    public int mCount;
    public long mTotalTime;
    public int mVersion;
    
    Entry(int paramInt1, long paramLong, int paramInt2)
    {
      mCount = paramInt1;
      mTotalTime = paramLong;
      mVersion = paramInt2;
    }
  }
}
