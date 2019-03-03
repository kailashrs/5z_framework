package android.telephony;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

@SystemApi
public class UiccSlotInfo
  implements Parcelable
{
  public static final int CARD_STATE_INFO_ABSENT = 1;
  public static final int CARD_STATE_INFO_ERROR = 3;
  public static final int CARD_STATE_INFO_PRESENT = 2;
  public static final int CARD_STATE_INFO_RESTRICTED = 4;
  public static final Parcelable.Creator<UiccSlotInfo> CREATOR = new Parcelable.Creator()
  {
    public UiccSlotInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new UiccSlotInfo(paramAnonymousParcel, null);
    }
    
    public UiccSlotInfo[] newArray(int paramAnonymousInt)
    {
      return new UiccSlotInfo[paramAnonymousInt];
    }
  };
  private final String mCardId;
  private final int mCardStateInfo;
  private final boolean mIsActive;
  private final boolean mIsEuicc;
  private final boolean mIsExtendedApduSupported;
  private final int mLogicalSlotIdx;
  
  private UiccSlotInfo(Parcel paramParcel)
  {
    int i = paramParcel.readByte();
    boolean bool1 = false;
    if (i != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mIsActive = bool2;
    if (paramParcel.readByte() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mIsEuicc = bool2;
    mCardId = paramParcel.readString();
    mCardStateInfo = paramParcel.readInt();
    mLogicalSlotIdx = paramParcel.readInt();
    boolean bool2 = bool1;
    if (paramParcel.readByte() != 0) {
      bool2 = true;
    }
    mIsExtendedApduSupported = bool2;
  }
  
  public UiccSlotInfo(boolean paramBoolean1, boolean paramBoolean2, String paramString, int paramInt1, int paramInt2, boolean paramBoolean3)
  {
    mIsActive = paramBoolean1;
    mIsEuicc = paramBoolean2;
    mCardId = paramString;
    mCardStateInfo = paramInt1;
    mLogicalSlotIdx = paramInt2;
    mIsExtendedApduSupported = paramBoolean3;
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
      paramObject = (UiccSlotInfo)paramObject;
      if ((mIsActive != mIsActive) || (mIsEuicc != mIsEuicc) || (!Objects.equals(mCardId, mCardId)) || (mCardStateInfo != mCardStateInfo) || (mLogicalSlotIdx != mLogicalSlotIdx) || (mIsExtendedApduSupported != mIsExtendedApduSupported)) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public String getCardId()
  {
    return mCardId;
  }
  
  public int getCardStateInfo()
  {
    return mCardStateInfo;
  }
  
  public boolean getIsActive()
  {
    return mIsActive;
  }
  
  public boolean getIsEuicc()
  {
    return mIsEuicc;
  }
  
  public boolean getIsExtendedApduSupported()
  {
    return mIsExtendedApduSupported;
  }
  
  public int getLogicalSlotIdx()
  {
    return mLogicalSlotIdx;
  }
  
  public int hashCode()
  {
    return 31 * (31 * (31 * (31 * (31 * (31 * 1 + mIsActive) + mIsEuicc) + Objects.hashCode(mCardId)) + mCardStateInfo) + mLogicalSlotIdx) + mIsExtendedApduSupported;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("UiccSlotInfo (mIsActive=");
    localStringBuilder.append(mIsActive);
    localStringBuilder.append(", mIsEuicc=");
    localStringBuilder.append(mIsEuicc);
    localStringBuilder.append(", mCardId=");
    localStringBuilder.append(mCardId);
    localStringBuilder.append(", cardState=");
    localStringBuilder.append(mCardStateInfo);
    localStringBuilder.append(", phoneId=");
    localStringBuilder.append(mLogicalSlotIdx);
    localStringBuilder.append(", mIsExtendedApduSupported=");
    localStringBuilder.append(mIsExtendedApduSupported);
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeByte((byte)mIsActive);
    paramParcel.writeByte((byte)mIsEuicc);
    paramParcel.writeString(mCardId);
    paramParcel.writeInt(mCardStateInfo);
    paramParcel.writeInt(mLogicalSlotIdx);
    paramParcel.writeByte((byte)mIsExtendedApduSupported);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface CardStateInfo {}
}
