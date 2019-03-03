package android.telephony.cdma;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CdmaSmsCbProgramResults
  implements Parcelable
{
  public static final Parcelable.Creator<CdmaSmsCbProgramResults> CREATOR = new Parcelable.Creator()
  {
    public CdmaSmsCbProgramResults createFromParcel(Parcel paramAnonymousParcel)
    {
      return new CdmaSmsCbProgramResults(paramAnonymousParcel);
    }
    
    public CdmaSmsCbProgramResults[] newArray(int paramAnonymousInt)
    {
      return new CdmaSmsCbProgramResults[paramAnonymousInt];
    }
  };
  public static final int RESULT_CATEGORY_ALREADY_ADDED = 3;
  public static final int RESULT_CATEGORY_ALREADY_DELETED = 4;
  public static final int RESULT_CATEGORY_LIMIT_EXCEEDED = 2;
  public static final int RESULT_INVALID_ALERT_OPTION = 6;
  public static final int RESULT_INVALID_CATEGORY_NAME = 7;
  public static final int RESULT_INVALID_MAX_MESSAGES = 5;
  public static final int RESULT_MEMORY_LIMIT_EXCEEDED = 1;
  public static final int RESULT_SUCCESS = 0;
  public static final int RESULT_UNSPECIFIED_FAILURE = 8;
  private final int mCategory;
  private final int mCategoryResult;
  private final int mLanguage;
  
  public CdmaSmsCbProgramResults(int paramInt1, int paramInt2, int paramInt3)
  {
    mCategory = paramInt1;
    mLanguage = paramInt2;
    mCategoryResult = paramInt3;
  }
  
  CdmaSmsCbProgramResults(Parcel paramParcel)
  {
    mCategory = paramParcel.readInt();
    mLanguage = paramParcel.readInt();
    mCategoryResult = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getCategory()
  {
    return mCategory;
  }
  
  public int getCategoryResult()
  {
    return mCategoryResult;
  }
  
  public int getLanguage()
  {
    return mLanguage;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("CdmaSmsCbProgramResults{category=");
    localStringBuilder.append(mCategory);
    localStringBuilder.append(", language=");
    localStringBuilder.append(mLanguage);
    localStringBuilder.append(", result=");
    localStringBuilder.append(mCategoryResult);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mCategory);
    paramParcel.writeInt(mLanguage);
    paramParcel.writeInt(mCategoryResult);
  }
}
