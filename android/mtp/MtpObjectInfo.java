package android.mtp;

import com.android.internal.util.Preconditions;
import dalvik.system.VMRuntime;

public final class MtpObjectInfo
{
  private int mAssociationDesc;
  private int mAssociationType;
  private int mCompressedSize;
  private long mDateCreated;
  private long mDateModified;
  private int mFormat;
  private int mHandle;
  private int mImagePixDepth;
  private int mImagePixHeight;
  private int mImagePixWidth;
  private String mKeywords = "";
  private String mName = "";
  private int mParent;
  private int mProtectionStatus;
  private int mSequenceNumber;
  private int mStorageId;
  private int mThumbCompressedSize;
  private int mThumbFormat;
  private int mThumbPixHeight;
  private int mThumbPixWidth;
  
  private MtpObjectInfo() {}
  
  private static int longToUint32(long paramLong, String paramString)
  {
    Preconditions.checkArgumentInRange(paramLong, 0L, 4294967295L, paramString);
    return (int)paramLong;
  }
  
  private static long uint32ToLong(int paramInt)
  {
    long l;
    if (paramInt < 0) {
      l = 4294967296L + paramInt;
    } else {
      l = paramInt;
    }
    return l;
  }
  
  public final int getAssociationDesc()
  {
    return mAssociationDesc;
  }
  
  public final int getAssociationType()
  {
    return mAssociationType;
  }
  
  public final int getCompressedSize()
  {
    boolean bool;
    if (mCompressedSize >= 0) {
      bool = true;
    } else {
      bool = false;
    }
    Preconditions.checkState(bool);
    return mCompressedSize;
  }
  
  public final long getCompressedSizeLong()
  {
    return uint32ToLong(mCompressedSize);
  }
  
  public final long getDateCreated()
  {
    return mDateCreated;
  }
  
  public final long getDateModified()
  {
    return mDateModified;
  }
  
  public final int getFormat()
  {
    return mFormat;
  }
  
  public final int getImagePixDepth()
  {
    boolean bool;
    if (mImagePixDepth >= 0) {
      bool = true;
    } else {
      bool = false;
    }
    Preconditions.checkState(bool);
    return mImagePixDepth;
  }
  
  public final long getImagePixDepthLong()
  {
    return uint32ToLong(mImagePixDepth);
  }
  
  public final int getImagePixHeight()
  {
    boolean bool;
    if (mImagePixHeight >= 0) {
      bool = true;
    } else {
      bool = false;
    }
    Preconditions.checkState(bool);
    return mImagePixHeight;
  }
  
  public final long getImagePixHeightLong()
  {
    return uint32ToLong(mImagePixHeight);
  }
  
  public final int getImagePixWidth()
  {
    boolean bool;
    if (mImagePixWidth >= 0) {
      bool = true;
    } else {
      bool = false;
    }
    Preconditions.checkState(bool);
    return mImagePixWidth;
  }
  
  public final long getImagePixWidthLong()
  {
    return uint32ToLong(mImagePixWidth);
  }
  
  public final String getKeywords()
  {
    return mKeywords;
  }
  
  public final String getName()
  {
    return mName;
  }
  
  public final int getObjectHandle()
  {
    return mHandle;
  }
  
  public final int getParent()
  {
    return mParent;
  }
  
  public final int getProtectionStatus()
  {
    return mProtectionStatus;
  }
  
  public final int getSequenceNumber()
  {
    boolean bool;
    if (mSequenceNumber >= 0) {
      bool = true;
    } else {
      bool = false;
    }
    Preconditions.checkState(bool);
    return mSequenceNumber;
  }
  
  public final long getSequenceNumberLong()
  {
    return uint32ToLong(mSequenceNumber);
  }
  
  public final int getStorageId()
  {
    return mStorageId;
  }
  
  public final int getThumbCompressedSize()
  {
    boolean bool;
    if (mThumbCompressedSize >= 0) {
      bool = true;
    } else {
      bool = false;
    }
    Preconditions.checkState(bool);
    return mThumbCompressedSize;
  }
  
  public final long getThumbCompressedSizeLong()
  {
    return uint32ToLong(mThumbCompressedSize);
  }
  
  public final int getThumbFormat()
  {
    return mThumbFormat;
  }
  
  public final int getThumbPixHeight()
  {
    boolean bool;
    if (mThumbPixHeight >= 0) {
      bool = true;
    } else {
      bool = false;
    }
    Preconditions.checkState(bool);
    return mThumbPixHeight;
  }
  
  public final long getThumbPixHeightLong()
  {
    return uint32ToLong(mThumbPixHeight);
  }
  
  public final int getThumbPixWidth()
  {
    boolean bool;
    if (mThumbPixWidth >= 0) {
      bool = true;
    } else {
      bool = false;
    }
    Preconditions.checkState(bool);
    return mThumbPixWidth;
  }
  
  public final long getThumbPixWidthLong()
  {
    return uint32ToLong(mThumbPixWidth);
  }
  
  public static class Builder
  {
    private MtpObjectInfo mObjectInfo = new MtpObjectInfo(null);
    
    public Builder()
    {
      MtpObjectInfo.access$102(mObjectInfo, -1);
    }
    
    public Builder(MtpObjectInfo paramMtpObjectInfo)
    {
      MtpObjectInfo.access$102(mObjectInfo, -1);
      MtpObjectInfo.access$202(mObjectInfo, mAssociationDesc);
      MtpObjectInfo.access$302(mObjectInfo, mAssociationType);
      MtpObjectInfo.access$402(mObjectInfo, mCompressedSize);
      MtpObjectInfo.access$502(mObjectInfo, mDateCreated);
      MtpObjectInfo.access$602(mObjectInfo, mDateModified);
      MtpObjectInfo.access$702(mObjectInfo, mFormat);
      MtpObjectInfo.access$802(mObjectInfo, mImagePixDepth);
      MtpObjectInfo.access$902(mObjectInfo, mImagePixHeight);
      MtpObjectInfo.access$1002(mObjectInfo, mImagePixWidth);
      MtpObjectInfo.access$1102(mObjectInfo, mKeywords);
      MtpObjectInfo.access$1202(mObjectInfo, mName);
      MtpObjectInfo.access$1302(mObjectInfo, mParent);
      MtpObjectInfo.access$1402(mObjectInfo, mProtectionStatus);
      MtpObjectInfo.access$1502(mObjectInfo, mSequenceNumber);
      MtpObjectInfo.access$1602(mObjectInfo, mStorageId);
      MtpObjectInfo.access$1702(mObjectInfo, mThumbCompressedSize);
      MtpObjectInfo.access$1802(mObjectInfo, mThumbFormat);
      MtpObjectInfo.access$1902(mObjectInfo, mThumbPixHeight);
      MtpObjectInfo.access$2002(mObjectInfo, mThumbPixWidth);
    }
    
    public MtpObjectInfo build()
    {
      MtpObjectInfo localMtpObjectInfo = mObjectInfo;
      mObjectInfo = null;
      return localMtpObjectInfo;
    }
    
    public Builder setAssociationDesc(int paramInt)
    {
      MtpObjectInfo.access$202(mObjectInfo, paramInt);
      return this;
    }
    
    public Builder setAssociationType(int paramInt)
    {
      MtpObjectInfo.access$302(mObjectInfo, paramInt);
      return this;
    }
    
    public Builder setCompressedSize(long paramLong)
    {
      MtpObjectInfo.access$402(mObjectInfo, MtpObjectInfo.longToUint32(paramLong, "value"));
      return this;
    }
    
    public Builder setDateCreated(long paramLong)
    {
      MtpObjectInfo.access$502(mObjectInfo, paramLong);
      return this;
    }
    
    public Builder setDateModified(long paramLong)
    {
      MtpObjectInfo.access$602(mObjectInfo, paramLong);
      return this;
    }
    
    public Builder setFormat(int paramInt)
    {
      MtpObjectInfo.access$702(mObjectInfo, paramInt);
      return this;
    }
    
    public Builder setImagePixDepth(long paramLong)
    {
      MtpObjectInfo.access$802(mObjectInfo, MtpObjectInfo.longToUint32(paramLong, "value"));
      return this;
    }
    
    public Builder setImagePixHeight(long paramLong)
    {
      MtpObjectInfo.access$902(mObjectInfo, MtpObjectInfo.longToUint32(paramLong, "value"));
      return this;
    }
    
    public Builder setImagePixWidth(long paramLong)
    {
      MtpObjectInfo.access$1002(mObjectInfo, MtpObjectInfo.longToUint32(paramLong, "value"));
      return this;
    }
    
    public Builder setKeywords(String paramString)
    {
      String str;
      if (VMRuntime.getRuntime().getTargetSdkVersion() > 25)
      {
        Preconditions.checkNotNull(paramString);
        str = paramString;
      }
      else
      {
        str = paramString;
        if (paramString == null) {
          str = "";
        }
      }
      MtpObjectInfo.access$1102(mObjectInfo, str);
      return this;
    }
    
    public Builder setName(String paramString)
    {
      Preconditions.checkNotNull(paramString);
      MtpObjectInfo.access$1202(mObjectInfo, paramString);
      return this;
    }
    
    public Builder setObjectHandle(int paramInt)
    {
      MtpObjectInfo.access$102(mObjectInfo, paramInt);
      return this;
    }
    
    public Builder setParent(int paramInt)
    {
      MtpObjectInfo.access$1302(mObjectInfo, paramInt);
      return this;
    }
    
    public Builder setProtectionStatus(int paramInt)
    {
      MtpObjectInfo.access$1402(mObjectInfo, paramInt);
      return this;
    }
    
    public Builder setSequenceNumber(long paramLong)
    {
      MtpObjectInfo.access$1502(mObjectInfo, MtpObjectInfo.longToUint32(paramLong, "value"));
      return this;
    }
    
    public Builder setStorageId(int paramInt)
    {
      MtpObjectInfo.access$1602(mObjectInfo, paramInt);
      return this;
    }
    
    public Builder setThumbCompressedSize(long paramLong)
    {
      MtpObjectInfo.access$1702(mObjectInfo, MtpObjectInfo.longToUint32(paramLong, "value"));
      return this;
    }
    
    public Builder setThumbFormat(int paramInt)
    {
      MtpObjectInfo.access$1802(mObjectInfo, paramInt);
      return this;
    }
    
    public Builder setThumbPixHeight(long paramLong)
    {
      MtpObjectInfo.access$1902(mObjectInfo, MtpObjectInfo.longToUint32(paramLong, "value"));
      return this;
    }
    
    public Builder setThumbPixWidth(long paramLong)
    {
      MtpObjectInfo.access$2002(mObjectInfo, MtpObjectInfo.longToUint32(paramLong, "value"));
      return this;
    }
  }
}
