package android.mtp;

import android.os.storage.StorageVolume;

public class MtpStorage
{
  private final String mDescription;
  private final long mMaxFileSize;
  private final String mPath;
  private final boolean mRemovable;
  private final int mStorageId;
  
  public MtpStorage(StorageVolume paramStorageVolume, int paramInt)
  {
    mStorageId = paramInt;
    mPath = paramStorageVolume.getInternalPath();
    mDescription = paramStorageVolume.getDescription(null);
    mRemovable = paramStorageVolume.isRemovable();
    mMaxFileSize = paramStorageVolume.getMaxFileSize();
  }
  
  public final String getDescription()
  {
    return mDescription;
  }
  
  public long getMaxFileSize()
  {
    return mMaxFileSize;
  }
  
  public final String getPath()
  {
    return mPath;
  }
  
  public final int getStorageId()
  {
    return mStorageId;
  }
  
  public final boolean isRemovable()
  {
    return mRemovable;
  }
}
