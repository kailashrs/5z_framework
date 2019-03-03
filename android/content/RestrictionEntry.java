package android.content;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;
import java.util.Objects;

public class RestrictionEntry
  implements Parcelable
{
  public static final Parcelable.Creator<RestrictionEntry> CREATOR = new Parcelable.Creator()
  {
    public RestrictionEntry createFromParcel(Parcel paramAnonymousParcel)
    {
      return new RestrictionEntry(paramAnonymousParcel);
    }
    
    public RestrictionEntry[] newArray(int paramAnonymousInt)
    {
      return new RestrictionEntry[paramAnonymousInt];
    }
  };
  public static final int TYPE_BOOLEAN = 1;
  public static final int TYPE_BUNDLE = 7;
  public static final int TYPE_BUNDLE_ARRAY = 8;
  public static final int TYPE_CHOICE = 2;
  public static final int TYPE_CHOICE_LEVEL = 3;
  public static final int TYPE_INTEGER = 5;
  public static final int TYPE_MULTI_SELECT = 4;
  public static final int TYPE_NULL = 0;
  public static final int TYPE_STRING = 6;
  private String[] mChoiceEntries;
  private String[] mChoiceValues;
  private String mCurrentValue;
  private String[] mCurrentValues;
  private String mDescription;
  private String mKey;
  private RestrictionEntry[] mRestrictions;
  private String mTitle;
  private int mType;
  
  public RestrictionEntry(int paramInt, String paramString)
  {
    mType = paramInt;
    mKey = paramString;
  }
  
  public RestrictionEntry(Parcel paramParcel)
  {
    mType = paramParcel.readInt();
    mKey = paramParcel.readString();
    mTitle = paramParcel.readString();
    mDescription = paramParcel.readString();
    mChoiceEntries = paramParcel.readStringArray();
    mChoiceValues = paramParcel.readStringArray();
    mCurrentValue = paramParcel.readString();
    mCurrentValues = paramParcel.readStringArray();
    paramParcel = paramParcel.readParcelableArray(null);
    if (paramParcel != null)
    {
      mRestrictions = new RestrictionEntry[paramParcel.length];
      for (int i = 0; i < paramParcel.length; i++) {
        mRestrictions[i] = ((RestrictionEntry)paramParcel[i]);
      }
    }
  }
  
  public RestrictionEntry(String paramString, int paramInt)
  {
    mKey = paramString;
    mType = 5;
    setIntValue(paramInt);
  }
  
  public RestrictionEntry(String paramString1, String paramString2)
  {
    mKey = paramString1;
    mType = 2;
    mCurrentValue = paramString2;
  }
  
  public RestrictionEntry(String paramString, boolean paramBoolean)
  {
    mKey = paramString;
    mType = 1;
    setSelectedState(paramBoolean);
  }
  
  private RestrictionEntry(String paramString, RestrictionEntry[] paramArrayOfRestrictionEntry, boolean paramBoolean)
  {
    mKey = paramString;
    if (paramBoolean)
    {
      mType = 8;
      if (paramArrayOfRestrictionEntry != null)
      {
        int i = paramArrayOfRestrictionEntry.length;
        for (int j = 0;; j++)
        {
          if (j >= i) {
            break label71;
          }
          if (paramArrayOfRestrictionEntry[j].getType() != 7) {
            break;
          }
        }
        throw new IllegalArgumentException("bundle_array restriction can only have nested restriction entries of type bundle");
      }
    }
    else
    {
      mType = 7;
    }
    label71:
    setRestrictions(paramArrayOfRestrictionEntry);
  }
  
  public RestrictionEntry(String paramString, String[] paramArrayOfString)
  {
    mKey = paramString;
    mType = 4;
    mCurrentValues = paramArrayOfString;
  }
  
  public static RestrictionEntry createBundleArrayEntry(String paramString, RestrictionEntry[] paramArrayOfRestrictionEntry)
  {
    return new RestrictionEntry(paramString, paramArrayOfRestrictionEntry, true);
  }
  
  public static RestrictionEntry createBundleEntry(String paramString, RestrictionEntry[] paramArrayOfRestrictionEntry)
  {
    return new RestrictionEntry(paramString, paramArrayOfRestrictionEntry, false);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == this) {
      return true;
    }
    if (!(paramObject instanceof RestrictionEntry)) {
      return false;
    }
    paramObject = (RestrictionEntry)paramObject;
    if ((mType == mType) && (mKey.equals(mKey)))
    {
      if ((mCurrentValues == null) && (mCurrentValues == null) && (mRestrictions == null) && (mRestrictions == null) && (Objects.equals(mCurrentValue, mCurrentValue))) {
        return true;
      }
      if ((mCurrentValue == null) && (mCurrentValue == null) && (mRestrictions == null) && (mRestrictions == null) && (Arrays.equals(mCurrentValues, mCurrentValues))) {
        return true;
      }
      return (mCurrentValue == null) && (mCurrentValue == null) && (mCurrentValue == null) && (mCurrentValue == null) && (Arrays.equals(mRestrictions, mRestrictions));
    }
    return false;
  }
  
  public String[] getAllSelectedStrings()
  {
    return mCurrentValues;
  }
  
  public String[] getChoiceEntries()
  {
    return mChoiceEntries;
  }
  
  public String[] getChoiceValues()
  {
    return mChoiceValues;
  }
  
  public String getDescription()
  {
    return mDescription;
  }
  
  public int getIntValue()
  {
    return Integer.parseInt(mCurrentValue);
  }
  
  public String getKey()
  {
    return mKey;
  }
  
  public RestrictionEntry[] getRestrictions()
  {
    return mRestrictions;
  }
  
  public boolean getSelectedState()
  {
    return Boolean.parseBoolean(mCurrentValue);
  }
  
  public String getSelectedString()
  {
    return mCurrentValue;
  }
  
  public String getTitle()
  {
    return mTitle;
  }
  
  public int getType()
  {
    return mType;
  }
  
  public int hashCode()
  {
    int i = 31 * 17 + mKey.hashCode();
    int j;
    if (mCurrentValue != null)
    {
      j = 31 * i + mCurrentValue.hashCode();
    }
    else
    {
      if (mCurrentValues != null)
      {
        String[] arrayOfString = mCurrentValues;
        int k = arrayOfString.length;
        int m = 0;
        for (;;)
        {
          j = i;
          if (m >= k) {
            break;
          }
          String str = arrayOfString[m];
          j = i;
          if (str != null) {
            j = 31 * i + str.hashCode();
          }
          m++;
          i = j;
        }
      }
      j = i;
      if (mRestrictions != null) {
        j = 31 * i + Arrays.hashCode(mRestrictions);
      }
    }
    return j;
  }
  
  public void setAllSelectedStrings(String[] paramArrayOfString)
  {
    mCurrentValues = paramArrayOfString;
  }
  
  public void setChoiceEntries(Context paramContext, int paramInt)
  {
    mChoiceEntries = paramContext.getResources().getStringArray(paramInt);
  }
  
  public void setChoiceEntries(String[] paramArrayOfString)
  {
    mChoiceEntries = paramArrayOfString;
  }
  
  public void setChoiceValues(Context paramContext, int paramInt)
  {
    mChoiceValues = paramContext.getResources().getStringArray(paramInt);
  }
  
  public void setChoiceValues(String[] paramArrayOfString)
  {
    mChoiceValues = paramArrayOfString;
  }
  
  public void setDescription(String paramString)
  {
    mDescription = paramString;
  }
  
  public void setIntValue(int paramInt)
  {
    mCurrentValue = Integer.toString(paramInt);
  }
  
  public void setRestrictions(RestrictionEntry[] paramArrayOfRestrictionEntry)
  {
    mRestrictions = paramArrayOfRestrictionEntry;
  }
  
  public void setSelectedState(boolean paramBoolean)
  {
    mCurrentValue = Boolean.toString(paramBoolean);
  }
  
  public void setSelectedString(String paramString)
  {
    mCurrentValue = paramString;
  }
  
  public void setTitle(String paramString)
  {
    mTitle = paramString;
  }
  
  public void setType(int paramInt)
  {
    mType = paramInt;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("RestrictionEntry{mType=");
    localStringBuilder.append(mType);
    localStringBuilder.append(", mKey='");
    localStringBuilder.append(mKey);
    localStringBuilder.append('\'');
    localStringBuilder.append(", mTitle='");
    localStringBuilder.append(mTitle);
    localStringBuilder.append('\'');
    localStringBuilder.append(", mDescription='");
    localStringBuilder.append(mDescription);
    localStringBuilder.append('\'');
    localStringBuilder.append(", mChoiceEntries=");
    localStringBuilder.append(Arrays.toString(mChoiceEntries));
    localStringBuilder.append(", mChoiceValues=");
    localStringBuilder.append(Arrays.toString(mChoiceValues));
    localStringBuilder.append(", mCurrentValue='");
    localStringBuilder.append(mCurrentValue);
    localStringBuilder.append('\'');
    localStringBuilder.append(", mCurrentValues=");
    localStringBuilder.append(Arrays.toString(mCurrentValues));
    localStringBuilder.append(", mRestrictions=");
    localStringBuilder.append(Arrays.toString(mRestrictions));
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mType);
    paramParcel.writeString(mKey);
    paramParcel.writeString(mTitle);
    paramParcel.writeString(mDescription);
    paramParcel.writeStringArray(mChoiceEntries);
    paramParcel.writeStringArray(mChoiceValues);
    paramParcel.writeString(mCurrentValue);
    paramParcel.writeStringArray(mCurrentValues);
    paramParcel.writeParcelableArray(mRestrictions, 0);
  }
}
