package com.android.internal.util;

import android.os.Debug;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;

public final class MemInfoReader
{
  final long[] mInfos = new long[15];
  
  public MemInfoReader() {}
  
  public long getCachedSize()
  {
    return getCachedSizeKb() * 1024L;
  }
  
  public long getCachedSizeKb()
  {
    return mInfos[2] + mInfos[6] + mInfos[3] - mInfos[11];
  }
  
  public long getFreeSize()
  {
    return mInfos[1] * 1024L;
  }
  
  public long getFreeSizeKb()
  {
    return mInfos[1];
  }
  
  public long getKernelUsedSize()
  {
    return getKernelUsedSizeKb() * 1024L;
  }
  
  public long getKernelUsedSizeKb()
  {
    return mInfos[4] + mInfos[7] + mInfos[12] + mInfos[13] + mInfos[14];
  }
  
  public long[] getRawInfo()
  {
    return mInfos;
  }
  
  public long getSwapFreeSizeKb()
  {
    return mInfos[9];
  }
  
  public long getSwapTotalSizeKb()
  {
    return mInfos[8];
  }
  
  public long getTotalSize()
  {
    return mInfos[0] * 1024L;
  }
  
  public long getTotalSizeKb()
  {
    return mInfos[0];
  }
  
  public long getZramTotalSizeKb()
  {
    return mInfos[10];
  }
  
  public void readMemInfo()
  {
    StrictMode.ThreadPolicy localThreadPolicy = StrictMode.allowThreadDiskReads();
    try
    {
      Debug.getMemInfo(mInfos);
      return;
    }
    finally
    {
      StrictMode.setThreadPolicy(localThreadPolicy);
    }
  }
}
