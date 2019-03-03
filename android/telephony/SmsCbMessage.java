package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SmsCbMessage
  implements Parcelable
{
  public static final Parcelable.Creator<SmsCbMessage> CREATOR = new Parcelable.Creator()
  {
    public SmsCbMessage createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SmsCbMessage(paramAnonymousParcel);
    }
    
    public SmsCbMessage[] newArray(int paramAnonymousInt)
    {
      return new SmsCbMessage[paramAnonymousInt];
    }
  };
  public static final int GEOGRAPHICAL_SCOPE_CELL_WIDE = 3;
  public static final int GEOGRAPHICAL_SCOPE_CELL_WIDE_IMMEDIATE = 0;
  public static final int GEOGRAPHICAL_SCOPE_LA_WIDE = 2;
  public static final int GEOGRAPHICAL_SCOPE_PLMN_WIDE = 1;
  protected static final String LOG_TAG = "SMSCB";
  public static final int MESSAGE_FORMAT_3GPP = 1;
  public static final int MESSAGE_FORMAT_3GPP2 = 2;
  public static final int MESSAGE_PRIORITY_EMERGENCY = 3;
  public static final int MESSAGE_PRIORITY_INTERACTIVE = 1;
  public static final int MESSAGE_PRIORITY_NORMAL = 0;
  public static final int MESSAGE_PRIORITY_URGENT = 2;
  private final String mBody;
  private final SmsCbCmasInfo mCmasWarningInfo;
  private final SmsCbEtwsInfo mEtwsWarningInfo;
  private final int mGeographicalScope;
  private final String mLanguage;
  private final SmsCbLocation mLocation;
  private final int mMessageFormat;
  private final int mPriority;
  private final int mSerialNumber;
  private final int mServiceCategory;
  
  public SmsCbMessage(int paramInt1, int paramInt2, int paramInt3, SmsCbLocation paramSmsCbLocation, int paramInt4, String paramString1, String paramString2, int paramInt5, SmsCbEtwsInfo paramSmsCbEtwsInfo, SmsCbCmasInfo paramSmsCbCmasInfo)
  {
    mMessageFormat = paramInt1;
    mGeographicalScope = paramInt2;
    mSerialNumber = paramInt3;
    mLocation = paramSmsCbLocation;
    mServiceCategory = paramInt4;
    mLanguage = paramString1;
    mBody = paramString2;
    mPriority = paramInt5;
    mEtwsWarningInfo = paramSmsCbEtwsInfo;
    mCmasWarningInfo = paramSmsCbCmasInfo;
  }
  
  public SmsCbMessage(Parcel paramParcel)
  {
    mMessageFormat = paramParcel.readInt();
    mGeographicalScope = paramParcel.readInt();
    mSerialNumber = paramParcel.readInt();
    mLocation = new SmsCbLocation(paramParcel);
    mServiceCategory = paramParcel.readInt();
    mLanguage = paramParcel.readString();
    mBody = paramParcel.readString();
    mPriority = paramParcel.readInt();
    int i = paramParcel.readInt();
    if (i != 67)
    {
      if (i != 69)
      {
        mEtwsWarningInfo = null;
        mCmasWarningInfo = null;
      }
      else
      {
        mEtwsWarningInfo = new SmsCbEtwsInfo(paramParcel);
        mCmasWarningInfo = null;
      }
    }
    else
    {
      mEtwsWarningInfo = null;
      mCmasWarningInfo = new SmsCbCmasInfo(paramParcel);
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public SmsCbCmasInfo getCmasWarningInfo()
  {
    return mCmasWarningInfo;
  }
  
  public SmsCbEtwsInfo getEtwsWarningInfo()
  {
    return mEtwsWarningInfo;
  }
  
  public int getGeographicalScope()
  {
    return mGeographicalScope;
  }
  
  public String getLanguageCode()
  {
    return mLanguage;
  }
  
  public SmsCbLocation getLocation()
  {
    return mLocation;
  }
  
  public String getMessageBody()
  {
    return mBody;
  }
  
  public int getMessageFormat()
  {
    return mMessageFormat;
  }
  
  public int getMessagePriority()
  {
    return mPriority;
  }
  
  public int getSerialNumber()
  {
    return mSerialNumber;
  }
  
  public int getServiceCategory()
  {
    return mServiceCategory;
  }
  
  public boolean isCmasMessage()
  {
    boolean bool;
    if (mCmasWarningInfo != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isEmergencyMessage()
  {
    boolean bool;
    if (mPriority == 3) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isEtwsMessage()
  {
    boolean bool;
    if (mEtwsWarningInfo != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("SmsCbMessage{geographicalScope=");
    localStringBuilder.append(mGeographicalScope);
    localStringBuilder.append(", serialNumber=");
    localStringBuilder.append(mSerialNumber);
    localStringBuilder.append(", location=");
    localStringBuilder.append(mLocation);
    localStringBuilder.append(", serviceCategory=");
    localStringBuilder.append(mServiceCategory);
    localStringBuilder.append(", language=");
    localStringBuilder.append(mLanguage);
    localStringBuilder.append(", body=");
    localStringBuilder.append(mBody);
    localStringBuilder.append(", priority=");
    localStringBuilder.append(mPriority);
    Object localObject;
    if (mEtwsWarningInfo != null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(", ");
      ((StringBuilder)localObject).append(mEtwsWarningInfo.toString());
      localObject = ((StringBuilder)localObject).toString();
    }
    else
    {
      localObject = "";
    }
    localStringBuilder.append((String)localObject);
    if (mCmasWarningInfo != null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(", ");
      ((StringBuilder)localObject).append(mCmasWarningInfo.toString());
      localObject = ((StringBuilder)localObject).toString();
    }
    else
    {
      localObject = "";
    }
    localStringBuilder.append((String)localObject);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mMessageFormat);
    paramParcel.writeInt(mGeographicalScope);
    paramParcel.writeInt(mSerialNumber);
    mLocation.writeToParcel(paramParcel, paramInt);
    paramParcel.writeInt(mServiceCategory);
    paramParcel.writeString(mLanguage);
    paramParcel.writeString(mBody);
    paramParcel.writeInt(mPriority);
    if (mEtwsWarningInfo != null)
    {
      paramParcel.writeInt(69);
      mEtwsWarningInfo.writeToParcel(paramParcel, paramInt);
    }
    else if (mCmasWarningInfo != null)
    {
      paramParcel.writeInt(67);
      mCmasWarningInfo.writeToParcel(paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeInt(48);
    }
  }
}
