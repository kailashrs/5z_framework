package android.mtp;

public final class MtpStorageInfo
{
  private String mDescription;
  private long mFreeSpace;
  private long mMaxCapacity;
  private int mStorageId;
  private String mVolumeIdentifier;
  
  private MtpStorageInfo() {}
  
  public final String getDescription()
  {
    return mDescription;
  }
  
  public final long getFreeSpace()
  {
    return mFreeSpace;
  }
  
  public final long getMaxCapacity()
  {
    return mMaxCapacity;
  }
  
  public final int getStorageId()
  {
    return mStorageId;
  }
  
  public final String getVolumeIdentifier()
  {
    return mVolumeIdentifier;
  }
}
