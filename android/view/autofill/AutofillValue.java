package android.view.autofill;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.android.internal.util.Preconditions;
import java.util.Objects;

public final class AutofillValue
  implements Parcelable
{
  public static final Parcelable.Creator<AutofillValue> CREATOR = new Parcelable.Creator()
  {
    public AutofillValue createFromParcel(Parcel paramAnonymousParcel)
    {
      return new AutofillValue(paramAnonymousParcel, null);
    }
    
    public AutofillValue[] newArray(int paramAnonymousInt)
    {
      return new AutofillValue[paramAnonymousInt];
    }
  };
  private final int mType;
  private final Object mValue;
  
  private AutofillValue(int paramInt, Object paramObject)
  {
    mType = paramInt;
    mValue = paramObject;
  }
  
  private AutofillValue(Parcel paramParcel)
  {
    mType = paramParcel.readInt();
    switch (mType)
    {
    default: 
      paramParcel = new StringBuilder();
      paramParcel.append("type=");
      paramParcel.append(mType);
      paramParcel.append(" not valid");
      throw new IllegalArgumentException(paramParcel.toString());
    case 4: 
      mValue = Long.valueOf(paramParcel.readLong());
      break;
    case 3: 
      mValue = Integer.valueOf(paramParcel.readInt());
      break;
    case 2: 
      boolean bool;
      if (paramParcel.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      mValue = Boolean.valueOf(bool);
      break;
    case 1: 
      mValue = paramParcel.readCharSequence();
    }
  }
  
  public static AutofillValue forDate(long paramLong)
  {
    return new AutofillValue(4, Long.valueOf(paramLong));
  }
  
  public static AutofillValue forList(int paramInt)
  {
    return new AutofillValue(3, Integer.valueOf(paramInt));
  }
  
  public static AutofillValue forText(CharSequence paramCharSequence)
  {
    if (paramCharSequence == null) {
      paramCharSequence = null;
    } else {
      paramCharSequence = new AutofillValue(1, TextUtils.trimNoCopySpans(paramCharSequence));
    }
    return paramCharSequence;
  }
  
  public static AutofillValue forToggle(boolean paramBoolean)
  {
    return new AutofillValue(2, Boolean.valueOf(paramBoolean));
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
    paramObject = (AutofillValue)paramObject;
    if (mType != mType) {
      return false;
    }
    if (isText()) {
      return mValue.toString().equals(mValue.toString());
    }
    return Objects.equals(mValue, mValue);
  }
  
  public long getDateValue()
  {
    boolean bool = isDate();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("value must be a date value, not type=");
    localStringBuilder.append(mType);
    Preconditions.checkState(bool, localStringBuilder.toString());
    return ((Long)mValue).longValue();
  }
  
  public int getListValue()
  {
    boolean bool = isList();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("value must be a list value, not type=");
    localStringBuilder.append(mType);
    Preconditions.checkState(bool, localStringBuilder.toString());
    return ((Integer)mValue).intValue();
  }
  
  public CharSequence getTextValue()
  {
    boolean bool = isText();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("value must be a text value, not type=");
    localStringBuilder.append(mType);
    Preconditions.checkState(bool, localStringBuilder.toString());
    return (CharSequence)mValue;
  }
  
  public boolean getToggleValue()
  {
    boolean bool = isToggle();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("value must be a toggle value, not type=");
    localStringBuilder.append(mType);
    Preconditions.checkState(bool, localStringBuilder.toString());
    return ((Boolean)mValue).booleanValue();
  }
  
  public int hashCode()
  {
    return mType + mValue.hashCode();
  }
  
  public boolean isDate()
  {
    boolean bool;
    if (mType == 4) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isEmpty()
  {
    boolean bool;
    if ((isText()) && (((CharSequence)mValue).length() == 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isList()
  {
    boolean bool;
    if (mType == 3) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isText()
  {
    int i = mType;
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isToggle()
  {
    boolean bool;
    if (mType == 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String toString()
  {
    if (!Helper.sDebug) {
      return super.toString();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[type=");
    localStringBuilder.append(mType);
    localStringBuilder = localStringBuilder.append(", value=");
    if (isText()) {
      Helper.appendRedacted(localStringBuilder, (CharSequence)mValue);
    } else {
      localStringBuilder.append(mValue);
    }
    localStringBuilder.append(']');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mType);
    switch (mType)
    {
    default: 
      break;
    case 4: 
      paramParcel.writeLong(((Long)mValue).longValue());
      break;
    case 3: 
      paramParcel.writeInt(((Integer)mValue).intValue());
      break;
    case 2: 
      paramParcel.writeInt(((Boolean)mValue).booleanValue());
      break;
    case 1: 
      paramParcel.writeCharSequence((CharSequence)mValue);
    }
  }
}
