package android.telephony.cdma;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CdmaSmsCbProgramData
  implements Parcelable
{
  public static final int ALERT_OPTION_DEFAULT_ALERT = 1;
  public static final int ALERT_OPTION_HIGH_PRIORITY_ONCE = 10;
  public static final int ALERT_OPTION_HIGH_PRIORITY_REPEAT = 11;
  public static final int ALERT_OPTION_LOW_PRIORITY_ONCE = 6;
  public static final int ALERT_OPTION_LOW_PRIORITY_REPEAT = 7;
  public static final int ALERT_OPTION_MED_PRIORITY_ONCE = 8;
  public static final int ALERT_OPTION_MED_PRIORITY_REPEAT = 9;
  public static final int ALERT_OPTION_NO_ALERT = 0;
  public static final int ALERT_OPTION_VIBRATE_ONCE = 2;
  public static final int ALERT_OPTION_VIBRATE_REPEAT = 3;
  public static final int ALERT_OPTION_VISUAL_ONCE = 4;
  public static final int ALERT_OPTION_VISUAL_REPEAT = 5;
  public static final Parcelable.Creator<CdmaSmsCbProgramData> CREATOR = new Parcelable.Creator()
  {
    public CdmaSmsCbProgramData createFromParcel(Parcel paramAnonymousParcel)
    {
      return new CdmaSmsCbProgramData(paramAnonymousParcel);
    }
    
    public CdmaSmsCbProgramData[] newArray(int paramAnonymousInt)
    {
      return new CdmaSmsCbProgramData[paramAnonymousInt];
    }
  };
  public static final int OPERATION_ADD_CATEGORY = 1;
  public static final int OPERATION_CLEAR_CATEGORIES = 2;
  public static final int OPERATION_DELETE_CATEGORY = 0;
  private final int mAlertOption;
  private final int mCategory;
  private final String mCategoryName;
  private final int mLanguage;
  private final int mMaxMessages;
  private final int mOperation;
  
  public CdmaSmsCbProgramData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, String paramString)
  {
    mOperation = paramInt1;
    mCategory = paramInt2;
    mLanguage = paramInt3;
    mMaxMessages = paramInt4;
    mAlertOption = paramInt5;
    mCategoryName = paramString;
  }
  
  CdmaSmsCbProgramData(Parcel paramParcel)
  {
    mOperation = paramParcel.readInt();
    mCategory = paramParcel.readInt();
    mLanguage = paramParcel.readInt();
    mMaxMessages = paramParcel.readInt();
    mAlertOption = paramParcel.readInt();
    mCategoryName = paramParcel.readString();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getAlertOption()
  {
    return mAlertOption;
  }
  
  public int getCategory()
  {
    return mCategory;
  }
  
  public String getCategoryName()
  {
    return mCategoryName;
  }
  
  public int getLanguage()
  {
    return mLanguage;
  }
  
  public int getMaxMessages()
  {
    return mMaxMessages;
  }
  
  public int getOperation()
  {
    return mOperation;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("CdmaSmsCbProgramData{operation=");
    localStringBuilder.append(mOperation);
    localStringBuilder.append(", category=");
    localStringBuilder.append(mCategory);
    localStringBuilder.append(", language=");
    localStringBuilder.append(mLanguage);
    localStringBuilder.append(", max messages=");
    localStringBuilder.append(mMaxMessages);
    localStringBuilder.append(", alert option=");
    localStringBuilder.append(mAlertOption);
    localStringBuilder.append(", category name=");
    localStringBuilder.append(mCategoryName);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mOperation);
    paramParcel.writeInt(mCategory);
    paramParcel.writeInt(mLanguage);
    paramParcel.writeInt(mMaxMessages);
    paramParcel.writeInt(mAlertOption);
    paramParcel.writeString(mCategoryName);
  }
}
