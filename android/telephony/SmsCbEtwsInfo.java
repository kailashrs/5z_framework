package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.format.Time;
import com.android.internal.telephony.uicc.IccUtils;
import java.util.Arrays;

public class SmsCbEtwsInfo
  implements Parcelable
{
  public static final Parcelable.Creator<SmsCbEtwsInfo> CREATOR = new Parcelable.Creator()
  {
    public SmsCbEtwsInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SmsCbEtwsInfo(paramAnonymousParcel);
    }
    
    public SmsCbEtwsInfo[] newArray(int paramAnonymousInt)
    {
      return new SmsCbEtwsInfo[paramAnonymousInt];
    }
  };
  public static final int ETWS_WARNING_TYPE_EARTHQUAKE = 0;
  public static final int ETWS_WARNING_TYPE_EARTHQUAKE_AND_TSUNAMI = 2;
  public static final int ETWS_WARNING_TYPE_OTHER_EMERGENCY = 4;
  public static final int ETWS_WARNING_TYPE_TEST_MESSAGE = 3;
  public static final int ETWS_WARNING_TYPE_TSUNAMI = 1;
  public static final int ETWS_WARNING_TYPE_UNKNOWN = -1;
  private final boolean mActivatePopup;
  private final boolean mEmergencyUserAlert;
  private final boolean mPrimary;
  private final byte[] mWarningSecurityInformation;
  private final int mWarningType;
  
  public SmsCbEtwsInfo(int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, byte[] paramArrayOfByte)
  {
    mWarningType = paramInt;
    mEmergencyUserAlert = paramBoolean1;
    mActivatePopup = paramBoolean2;
    mPrimary = paramBoolean3;
    mWarningSecurityInformation = paramArrayOfByte;
  }
  
  SmsCbEtwsInfo(Parcel paramParcel)
  {
    mWarningType = paramParcel.readInt();
    int i = paramParcel.readInt();
    boolean bool1 = false;
    if (i != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mEmergencyUserAlert = bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mActivatePopup = bool2;
    boolean bool2 = bool1;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    }
    mPrimary = bool2;
    mWarningSecurityInformation = paramParcel.createByteArray();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public byte[] getPrimaryNotificationSignature()
  {
    if ((mWarningSecurityInformation != null) && (mWarningSecurityInformation.length >= 50)) {
      return Arrays.copyOfRange(mWarningSecurityInformation, 7, 50);
    }
    return null;
  }
  
  public long getPrimaryNotificationTimestamp()
  {
    if ((mWarningSecurityInformation != null) && (mWarningSecurityInformation.length >= 7))
    {
      int i = IccUtils.gsmBcdByteToInt(mWarningSecurityInformation[0]);
      int j = IccUtils.gsmBcdByteToInt(mWarningSecurityInformation[1]);
      int k = IccUtils.gsmBcdByteToInt(mWarningSecurityInformation[2]);
      int m = IccUtils.gsmBcdByteToInt(mWarningSecurityInformation[3]);
      int n = IccUtils.gsmBcdByteToInt(mWarningSecurityInformation[4]);
      int i1 = IccUtils.gsmBcdByteToInt(mWarningSecurityInformation[5]);
      int i2 = mWarningSecurityInformation[6];
      int i3 = IccUtils.gsmBcdByteToInt((byte)(i2 & 0xFFFFFFF7));
      if ((i2 & 0x8) != 0) {
        i3 = -i3;
      }
      Time localTime = new Time("UTC");
      year = (i + 2000);
      month = (j - 1);
      monthDay = k;
      hour = m;
      minute = n;
      second = i1;
      return localTime.toMillis(true) - i3 * 15 * 60 * 1000;
    }
    return 0L;
  }
  
  public int getWarningType()
  {
    return mWarningType;
  }
  
  public boolean isEmergencyUserAlert()
  {
    return mEmergencyUserAlert;
  }
  
  public boolean isPopupAlert()
  {
    return mActivatePopup;
  }
  
  public boolean isPrimary()
  {
    return mPrimary;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("SmsCbEtwsInfo{warningType=");
    localStringBuilder.append(mWarningType);
    localStringBuilder.append(", emergencyUserAlert=");
    localStringBuilder.append(mEmergencyUserAlert);
    localStringBuilder.append(", activatePopup=");
    localStringBuilder.append(mActivatePopup);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mWarningType);
    paramParcel.writeInt(mEmergencyUserAlert);
    paramParcel.writeInt(mActivatePopup);
    paramParcel.writeInt(mPrimary);
    paramParcel.writeByteArray(mWarningSecurityInformation);
  }
}
