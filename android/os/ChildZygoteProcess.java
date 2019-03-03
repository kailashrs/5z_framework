package android.os;

import android.net.LocalSocketAddress;

public class ChildZygoteProcess
  extends ZygoteProcess
{
  private final int mPid;
  
  ChildZygoteProcess(LocalSocketAddress paramLocalSocketAddress, int paramInt)
  {
    super(paramLocalSocketAddress, null);
    mPid = paramInt;
  }
  
  public int getPid()
  {
    return mPid;
  }
}
