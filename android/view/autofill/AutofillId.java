package android.view.autofill;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class AutofillId
  implements Parcelable
{
  public static final Parcelable.Creator<AutofillId> CREATOR = new Parcelable.Creator()
  {
    public AutofillId createFromParcel(Parcel paramAnonymousParcel)
    {
      return new AutofillId(paramAnonymousParcel, null);
    }
    
    public AutofillId[] newArray(int paramAnonymousInt)
    {
      return new AutofillId[paramAnonymousInt];
    }
  };
  private final int mViewId;
  private final boolean mVirtual;
  private final int mVirtualId;
  
  public AutofillId(int paramInt)
  {
    mVirtual = false;
    mViewId = paramInt;
    mVirtualId = -1;
  }
  
  public AutofillId(int paramInt1, int paramInt2)
  {
    mVirtual = true;
    mViewId = paramInt1;
    mVirtualId = paramInt2;
  }
  
  private AutofillId(Parcel paramParcel)
  {
    mViewId = paramParcel.readInt();
    int i = paramParcel.readInt();
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    mVirtual = bool;
    mVirtualId = paramParcel.readInt();
  }
  
  public AutofillId(AutofillId paramAutofillId, int paramInt)
  {
    mVirtual = true;
    mViewId = mViewId;
    mVirtualId = paramInt;
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
    paramObject = (AutofillId)paramObject;
    if (mViewId != mViewId) {
      return false;
    }
    return mVirtualId == mVirtualId;
  }
  
  public int getViewId()
  {
    return mViewId;
  }
  
  public int getVirtualChildId()
  {
    return mVirtualId;
  }
  
  public int hashCode()
  {
    return 31 * (31 * 1 + mViewId) + mVirtualId;
  }
  
  public boolean isVirtual()
  {
    return mVirtual;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder().append(mViewId);
    if (mVirtual)
    {
      localStringBuilder.append(':');
      localStringBuilder.append(mVirtualId);
    }
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mViewId);
    paramParcel.writeInt(mVirtual);
    paramParcel.writeInt(mVirtualId);
  }
}
