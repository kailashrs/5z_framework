package android.app.slice;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class SliceSpec
  implements Parcelable
{
  public static final Parcelable.Creator<SliceSpec> CREATOR = new Parcelable.Creator()
  {
    public SliceSpec createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SliceSpec(paramAnonymousParcel);
    }
    
    public SliceSpec[] newArray(int paramAnonymousInt)
    {
      return new SliceSpec[paramAnonymousInt];
    }
  };
  private final int mRevision;
  private final String mType;
  
  public SliceSpec(Parcel paramParcel)
  {
    mType = paramParcel.readString();
    mRevision = paramParcel.readInt();
  }
  
  public SliceSpec(String paramString, int paramInt)
  {
    mType = paramString;
    mRevision = paramInt;
  }
  
  public boolean canRender(SliceSpec paramSliceSpec)
  {
    boolean bool1 = mType.equals(mType);
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    if (mRevision >= mRevision) {
      bool2 = true;
    }
    return bool2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof SliceSpec;
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    paramObject = (SliceSpec)paramObject;
    bool1 = bool2;
    if (mType.equals(mType))
    {
      bool1 = bool2;
      if (mRevision == mRevision) {
        bool1 = true;
      }
    }
    return bool1;
  }
  
  public int getRevision()
  {
    return mRevision;
  }
  
  public String getType()
  {
    return mType;
  }
  
  public String toString()
  {
    return String.format("SliceSpec{%s,%d}", new Object[] { mType, Integer.valueOf(mRevision) });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mType);
    paramParcel.writeInt(mRevision);
  }
}
