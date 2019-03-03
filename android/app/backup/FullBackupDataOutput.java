package android.app.backup;

import android.os.ParcelFileDescriptor;

public class FullBackupDataOutput
{
  private final BackupDataOutput mData;
  private final long mQuota;
  private long mSize;
  private final int mTransportFlags;
  
  public FullBackupDataOutput(long paramLong)
  {
    mData = null;
    mQuota = paramLong;
    mSize = 0L;
    mTransportFlags = 0;
  }
  
  public FullBackupDataOutput(long paramLong, int paramInt)
  {
    mData = null;
    mQuota = paramLong;
    mSize = 0L;
    mTransportFlags = paramInt;
  }
  
  public FullBackupDataOutput(ParcelFileDescriptor paramParcelFileDescriptor)
  {
    this(paramParcelFileDescriptor, -1L, 0);
  }
  
  public FullBackupDataOutput(ParcelFileDescriptor paramParcelFileDescriptor, long paramLong)
  {
    mData = new BackupDataOutput(paramParcelFileDescriptor.getFileDescriptor(), paramLong, 0);
    mQuota = paramLong;
    mTransportFlags = 0;
  }
  
  public FullBackupDataOutput(ParcelFileDescriptor paramParcelFileDescriptor, long paramLong, int paramInt)
  {
    mData = new BackupDataOutput(paramParcelFileDescriptor.getFileDescriptor(), paramLong, paramInt);
    mQuota = paramLong;
    mTransportFlags = paramInt;
  }
  
  public void addSize(long paramLong)
  {
    if (paramLong > 0L) {
      mSize += paramLong;
    }
  }
  
  public BackupDataOutput getData()
  {
    return mData;
  }
  
  public long getQuota()
  {
    return mQuota;
  }
  
  public long getSize()
  {
    return mSize;
  }
  
  public int getTransportFlags()
  {
    return mTransportFlags;
  }
}
