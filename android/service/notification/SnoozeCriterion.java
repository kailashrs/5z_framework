package android.service.notification;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public final class SnoozeCriterion
  implements Parcelable
{
  public static final Parcelable.Creator<SnoozeCriterion> CREATOR = new Parcelable.Creator()
  {
    public SnoozeCriterion createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SnoozeCriterion(paramAnonymousParcel);
    }
    
    public SnoozeCriterion[] newArray(int paramAnonymousInt)
    {
      return new SnoozeCriterion[paramAnonymousInt];
    }
  };
  private final CharSequence mConfirmation;
  private final CharSequence mExplanation;
  private final String mId;
  
  protected SnoozeCriterion(Parcel paramParcel)
  {
    if (paramParcel.readByte() != 0) {
      mId = paramParcel.readString();
    } else {
      mId = null;
    }
    if (paramParcel.readByte() != 0) {
      mExplanation = paramParcel.readCharSequence();
    } else {
      mExplanation = null;
    }
    if (paramParcel.readByte() != 0) {
      mConfirmation = paramParcel.readCharSequence();
    } else {
      mConfirmation = null;
    }
  }
  
  public SnoozeCriterion(String paramString, CharSequence paramCharSequence1, CharSequence paramCharSequence2)
  {
    mId = paramString;
    mExplanation = paramCharSequence1;
    mConfirmation = paramCharSequence2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (SnoozeCriterion)paramObject;
      if (mId != null ? !mId.equals(mId) : mId != null) {
        return false;
      }
      if (mExplanation != null ? !mExplanation.equals(mExplanation) : mExplanation != null) {
        return false;
      }
      if (mConfirmation != null) {
        bool = mConfirmation.equals(mConfirmation);
      } else if (mConfirmation != null) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public CharSequence getConfirmation()
  {
    return mConfirmation;
  }
  
  public CharSequence getExplanation()
  {
    return mExplanation;
  }
  
  public String getId()
  {
    return mId;
  }
  
  public int hashCode()
  {
    String str = mId;
    int i = 0;
    int j;
    if (str != null) {
      j = mId.hashCode();
    } else {
      j = 0;
    }
    int k;
    if (mExplanation != null) {
      k = mExplanation.hashCode();
    } else {
      k = 0;
    }
    if (mConfirmation != null) {
      i = mConfirmation.hashCode();
    }
    return 31 * (31 * j + k) + i;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (mId != null)
    {
      paramParcel.writeByte((byte)1);
      paramParcel.writeString(mId);
    }
    else
    {
      paramParcel.writeByte((byte)0);
    }
    if (mExplanation != null)
    {
      paramParcel.writeByte((byte)1);
      paramParcel.writeCharSequence(mExplanation);
    }
    else
    {
      paramParcel.writeByte((byte)0);
    }
    if (mConfirmation != null)
    {
      paramParcel.writeByte((byte)1);
      paramParcel.writeCharSequence(mConfirmation);
    }
    else
    {
      paramParcel.writeByte((byte)0);
    }
  }
}
