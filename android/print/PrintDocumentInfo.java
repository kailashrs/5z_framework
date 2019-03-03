package android.print;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class PrintDocumentInfo
  implements Parcelable
{
  public static final int CONTENT_TYPE_DOCUMENT = 0;
  public static final int CONTENT_TYPE_PHOTO = 1;
  public static final int CONTENT_TYPE_UNKNOWN = -1;
  public static final Parcelable.Creator<PrintDocumentInfo> CREATOR = new Parcelable.Creator()
  {
    public PrintDocumentInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PrintDocumentInfo(paramAnonymousParcel, null);
    }
    
    public PrintDocumentInfo[] newArray(int paramAnonymousInt)
    {
      return new PrintDocumentInfo[paramAnonymousInt];
    }
  };
  public static final int PAGE_COUNT_UNKNOWN = -1;
  private int mContentType;
  private long mDataSize;
  private String mName;
  private int mPageCount;
  
  private PrintDocumentInfo() {}
  
  private PrintDocumentInfo(Parcel paramParcel)
  {
    mName = ((String)Preconditions.checkStringNotEmpty(paramParcel.readString()));
    mPageCount = paramParcel.readInt();
    boolean bool;
    if ((mPageCount != -1) && (mPageCount <= 0)) {
      bool = false;
    } else {
      bool = true;
    }
    Preconditions.checkArgument(bool);
    mContentType = paramParcel.readInt();
    mDataSize = Preconditions.checkArgumentNonnegative(paramParcel.readLong());
  }
  
  private PrintDocumentInfo(PrintDocumentInfo paramPrintDocumentInfo)
  {
    mName = mName;
    mPageCount = mPageCount;
    mContentType = mContentType;
    mDataSize = mDataSize;
  }
  
  private String contentTypeToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "CONTENT_TYPE_UNKNOWN";
    case 1: 
      return "CONTENT_TYPE_PHOTO";
    }
    return "CONTENT_TYPE_DOCUMENT";
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (paramObject == null) {
      return false;
    }
    if (getClass() != paramObject.getClass()) {
      return false;
    }
    paramObject = (PrintDocumentInfo)paramObject;
    if (!TextUtils.equals(mName, mName)) {
      return false;
    }
    if (mContentType != mContentType) {
      return false;
    }
    if (mPageCount != mPageCount) {
      return false;
    }
    return mDataSize == mDataSize;
  }
  
  public int getContentType()
  {
    return mContentType;
  }
  
  public long getDataSize()
  {
    return mDataSize;
  }
  
  public String getName()
  {
    return mName;
  }
  
  public int getPageCount()
  {
    return mPageCount;
  }
  
  public int hashCode()
  {
    int i;
    if (mName != null) {
      i = mName.hashCode();
    } else {
      i = 0;
    }
    return 31 * (31 * (31 * (31 * (31 * 1 + i) + mContentType) + mPageCount) + (int)mDataSize) + (int)(mDataSize >> 32);
  }
  
  public void setDataSize(long paramLong)
  {
    mDataSize = paramLong;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("PrintDocumentInfo{");
    localStringBuilder.append("name=");
    localStringBuilder.append(mName);
    localStringBuilder.append(", pageCount=");
    localStringBuilder.append(mPageCount);
    localStringBuilder.append(", contentType=");
    localStringBuilder.append(contentTypeToString(mContentType));
    localStringBuilder.append(", dataSize=");
    localStringBuilder.append(mDataSize);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mName);
    paramParcel.writeInt(mPageCount);
    paramParcel.writeInt(mContentType);
    paramParcel.writeLong(mDataSize);
  }
  
  public static final class Builder
  {
    private final PrintDocumentInfo mPrototype;
    
    public Builder(String paramString)
    {
      if (!TextUtils.isEmpty(paramString))
      {
        mPrototype = new PrintDocumentInfo(null);
        PrintDocumentInfo.access$102(mPrototype, paramString);
        return;
      }
      throw new IllegalArgumentException("name cannot be empty");
    }
    
    public PrintDocumentInfo build()
    {
      if (mPrototype.mPageCount == 0) {
        PrintDocumentInfo.access$202(mPrototype, -1);
      }
      return new PrintDocumentInfo(mPrototype, null);
    }
    
    public Builder setContentType(int paramInt)
    {
      PrintDocumentInfo.access$302(mPrototype, paramInt);
      return this;
    }
    
    public Builder setPageCount(int paramInt)
    {
      if ((paramInt < 0) && (paramInt != -1)) {
        throw new IllegalArgumentException("pageCount must be greater than or equal to zero or DocumentInfo#PAGE_COUNT_UNKNOWN");
      }
      PrintDocumentInfo.access$202(mPrototype, paramInt);
      return this;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ContentType {}
}
