package android.print;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class PageRange
  implements Parcelable
{
  public static final PageRange ALL_PAGES = new PageRange(0, Integer.MAX_VALUE);
  public static final PageRange[] ALL_PAGES_ARRAY = { ALL_PAGES };
  public static final Parcelable.Creator<PageRange> CREATOR = new Parcelable.Creator()
  {
    public PageRange createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PageRange(paramAnonymousParcel, null);
    }
    
    public PageRange[] newArray(int paramAnonymousInt)
    {
      return new PageRange[paramAnonymousInt];
    }
  };
  private final int mEnd;
  private final int mStart;
  
  public PageRange(int paramInt1, int paramInt2)
  {
    if (paramInt1 >= 0)
    {
      if (paramInt2 >= 0)
      {
        if (paramInt1 <= paramInt2)
        {
          mStart = paramInt1;
          mEnd = paramInt2;
          return;
        }
        throw new IllegalArgumentException("start must be lesser than end.");
      }
      throw new IllegalArgumentException("end cannot be less than zero.");
    }
    throw new IllegalArgumentException("start cannot be less than zero.");
  }
  
  private PageRange(Parcel paramParcel)
  {
    this(paramParcel.readInt(), paramParcel.readInt());
  }
  
  public boolean contains(int paramInt)
  {
    boolean bool;
    if ((paramInt >= mStart) && (paramInt <= mEnd)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
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
    paramObject = (PageRange)paramObject;
    if (mEnd != mEnd) {
      return false;
    }
    return mStart == mStart;
  }
  
  public int getEnd()
  {
    return mEnd;
  }
  
  public int getSize()
  {
    return mEnd - mStart + 1;
  }
  
  public int getStart()
  {
    return mStart;
  }
  
  public int hashCode()
  {
    return 31 * (31 * 1 + mEnd) + mStart;
  }
  
  public String toString()
  {
    if ((mStart == 0) && (mEnd == Integer.MAX_VALUE)) {
      return "PageRange[<all pages>]";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("PageRange[");
    localStringBuilder.append(mStart);
    localStringBuilder.append(" - ");
    localStringBuilder.append(mEnd);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mStart);
    paramParcel.writeInt(mEnd);
  }
}
