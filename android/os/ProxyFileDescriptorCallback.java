package android.os;

import android.system.ErrnoException;
import android.system.OsConstants;

public abstract class ProxyFileDescriptorCallback
{
  public ProxyFileDescriptorCallback() {}
  
  public void onFsync()
    throws ErrnoException
  {
    throw new ErrnoException("onFsync", OsConstants.EINVAL);
  }
  
  public long onGetSize()
    throws ErrnoException
  {
    throw new ErrnoException("onGetSize", OsConstants.EBADF);
  }
  
  public int onRead(long paramLong, int paramInt, byte[] paramArrayOfByte)
    throws ErrnoException
  {
    throw new ErrnoException("onRead", OsConstants.EBADF);
  }
  
  public abstract void onRelease();
  
  public int onWrite(long paramLong, int paramInt, byte[] paramArrayOfByte)
    throws ErrnoException
  {
    throw new ErrnoException("onWrite", OsConstants.EBADF);
  }
}
