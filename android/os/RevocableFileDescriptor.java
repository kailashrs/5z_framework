package android.os;

import android.content.Context;
import android.os.storage.StorageManager;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructStat;
import android.util.Slog;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InterruptedIOException;
import libcore.io.IoUtils;

public class RevocableFileDescriptor
{
  private static final boolean DEBUG = true;
  private static final String TAG = "RevocableFileDescriptor";
  private final ProxyFileDescriptorCallback mCallback = new ProxyFileDescriptorCallback()
  {
    private void checkRevoked()
      throws ErrnoException
    {
      if (!mRevoked) {
        return;
      }
      throw new ErrnoException("RevocableFileDescriptor", OsConstants.EPERM);
    }
    
    public void onFsync()
      throws ErrnoException
    {
      Slog.v("RevocableFileDescriptor", "onFsync()");
      checkRevoked();
      Os.fsync(mInner);
    }
    
    public long onGetSize()
      throws ErrnoException
    {
      checkRevoked();
      return fstatmInner).st_size;
    }
    
    public int onRead(long paramAnonymousLong, int paramAnonymousInt, byte[] paramAnonymousArrayOfByte)
      throws ErrnoException
    {
      checkRevoked();
      int i = 0;
      int j;
      for (;;)
      {
        j = i;
        if (i >= paramAnonymousInt) {
          break;
        }
        try
        {
          j = Os.pread(mInner, paramAnonymousArrayOfByte, i, paramAnonymousInt - i, paramAnonymousLong + i);
          j = i + j;
        }
        catch (InterruptedIOException localInterruptedIOException)
        {
          i += bytesTransferred;
        }
      }
      return j;
    }
    
    public void onRelease()
    {
      Slog.v("RevocableFileDescriptor", "onRelease()");
      RevocableFileDescriptor.access$002(RevocableFileDescriptor.this, true);
      IoUtils.closeQuietly(mInner);
    }
    
    public int onWrite(long paramAnonymousLong, int paramAnonymousInt, byte[] paramAnonymousArrayOfByte)
      throws ErrnoException
    {
      checkRevoked();
      int i = 0;
      int j;
      for (;;)
      {
        j = i;
        if (i >= paramAnonymousInt) {
          break;
        }
        try
        {
          j = Os.pwrite(mInner, paramAnonymousArrayOfByte, i, paramAnonymousInt - i, paramAnonymousLong + i);
          j = i + j;
        }
        catch (InterruptedIOException localInterruptedIOException)
        {
          i += bytesTransferred;
        }
      }
      return j;
    }
  };
  private FileDescriptor mInner;
  private ParcelFileDescriptor mOuter;
  private volatile boolean mRevoked;
  
  public RevocableFileDescriptor() {}
  
  public RevocableFileDescriptor(Context paramContext, File paramFile)
    throws IOException
  {
    try
    {
      init(paramContext, Os.open(paramFile.getAbsolutePath(), OsConstants.O_CREAT | OsConstants.O_RDWR, 448));
      return;
    }
    catch (ErrnoException paramContext)
    {
      throw paramContext.rethrowAsIOException();
    }
  }
  
  public RevocableFileDescriptor(Context paramContext, FileDescriptor paramFileDescriptor)
    throws IOException
  {
    init(paramContext, paramFileDescriptor);
  }
  
  public ParcelFileDescriptor getRevocableFileDescriptor()
  {
    return mOuter;
  }
  
  public void init(Context paramContext, FileDescriptor paramFileDescriptor)
    throws IOException
  {
    mInner = paramFileDescriptor;
    mOuter = ((StorageManager)paramContext.getSystemService(StorageManager.class)).openProxyFileDescriptor(805306368, mCallback);
  }
  
  public boolean isRevoked()
  {
    return mRevoked;
  }
  
  public void revoke()
  {
    mRevoked = true;
    IoUtils.closeQuietly(mInner);
  }
}
