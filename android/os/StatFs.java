package android.os;

import android.system.ErrnoException;
import android.system.Os;
import android.system.StructStatVfs;

public class StatFs
{
  private StructStatVfs mStat;
  
  public StatFs(String paramString)
  {
    mStat = doStat(paramString);
  }
  
  private static StructStatVfs doStat(String paramString)
  {
    try
    {
      localObject = Os.statvfs(paramString);
      return localObject;
    }
    catch (ErrnoException localErrnoException)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Invalid path: ");
      ((StringBuilder)localObject).append(paramString);
      throw new IllegalArgumentException(((StringBuilder)localObject).toString(), localErrnoException);
    }
  }
  
  @Deprecated
  public int getAvailableBlocks()
  {
    return (int)mStat.f_bavail;
  }
  
  public long getAvailableBlocksLong()
  {
    return mStat.f_bavail;
  }
  
  public long getAvailableBytes()
  {
    return mStat.f_bavail * mStat.f_frsize;
  }
  
  @Deprecated
  public int getBlockCount()
  {
    return (int)mStat.f_blocks;
  }
  
  public long getBlockCountLong()
  {
    return mStat.f_blocks;
  }
  
  @Deprecated
  public int getBlockSize()
  {
    return (int)mStat.f_frsize;
  }
  
  public long getBlockSizeLong()
  {
    return mStat.f_frsize;
  }
  
  @Deprecated
  public int getFreeBlocks()
  {
    return (int)mStat.f_bfree;
  }
  
  public long getFreeBlocksLong()
  {
    return mStat.f_bfree;
  }
  
  public long getFreeBytes()
  {
    return mStat.f_bfree * mStat.f_frsize;
  }
  
  public long getTotalBytes()
  {
    return mStat.f_blocks * mStat.f_frsize;
  }
  
  public void restat(String paramString)
  {
    mStat = doStat(paramString);
  }
}
