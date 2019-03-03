package android.app.backup;

import android.annotation.SystemApi;
import java.io.FileDescriptor;
import java.io.IOException;

public class BackupDataOutput
{
  long mBackupWriter;
  private final long mQuota;
  private final int mTransportFlags;
  
  @SystemApi
  public BackupDataOutput(FileDescriptor paramFileDescriptor)
  {
    this(paramFileDescriptor, -1L, 0);
  }
  
  @SystemApi
  public BackupDataOutput(FileDescriptor paramFileDescriptor, long paramLong)
  {
    this(paramFileDescriptor, paramLong, 0);
  }
  
  public BackupDataOutput(FileDescriptor paramFileDescriptor, long paramLong, int paramInt)
  {
    if (paramFileDescriptor != null)
    {
      mQuota = paramLong;
      mTransportFlags = paramInt;
      mBackupWriter = ctor(paramFileDescriptor);
      if (mBackupWriter != 0L) {
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Native initialization failed with fd=");
      localStringBuilder.append(paramFileDescriptor);
      throw new RuntimeException(localStringBuilder.toString());
    }
    throw new NullPointerException();
  }
  
  private static native long ctor(FileDescriptor paramFileDescriptor);
  
  private static native void dtor(long paramLong);
  
  private static native void setKeyPrefix_native(long paramLong, String paramString);
  
  private static native int writeEntityData_native(long paramLong, byte[] paramArrayOfByte, int paramInt);
  
  private static native int writeEntityHeader_native(long paramLong, String paramString, int paramInt);
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      dtor(mBackupWriter);
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public long getQuota()
  {
    return mQuota;
  }
  
  public int getTransportFlags()
  {
    return mTransportFlags;
  }
  
  public void setKeyPrefix(String paramString)
  {
    setKeyPrefix_native(mBackupWriter, paramString);
  }
  
  public int writeEntityData(byte[] paramArrayOfByte, int paramInt)
    throws IOException
  {
    paramInt = writeEntityData_native(mBackupWriter, paramArrayOfByte, paramInt);
    if (paramInt >= 0) {
      return paramInt;
    }
    paramArrayOfByte = new StringBuilder();
    paramArrayOfByte.append("result=0x");
    paramArrayOfByte.append(Integer.toHexString(paramInt));
    throw new IOException(paramArrayOfByte.toString());
  }
  
  public int writeEntityHeader(String paramString, int paramInt)
    throws IOException
  {
    paramInt = writeEntityHeader_native(mBackupWriter, paramString, paramInt);
    if (paramInt >= 0) {
      return paramInt;
    }
    paramString = new StringBuilder();
    paramString.append("result=0x");
    paramString.append(Integer.toHexString(paramInt));
    throw new IOException(paramString.toString());
  }
}
