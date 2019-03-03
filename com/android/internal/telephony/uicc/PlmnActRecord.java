package com.android.internal.telephony.uicc;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.telephony.Rlog;
import java.util.Arrays;

public class PlmnActRecord
  implements Parcelable
{
  public static final int ACCESS_TECH_CDMA2000_1XRTT = 16;
  public static final int ACCESS_TECH_CDMA2000_HRPD = 32;
  public static final int ACCESS_TECH_EUTRAN = 16384;
  public static final int ACCESS_TECH_GSM = 128;
  public static final int ACCESS_TECH_GSM_COMPACT = 64;
  public static final int ACCESS_TECH_RESERVED = 16143;
  public static final int ACCESS_TECH_UTRAN = 32768;
  public static final Parcelable.Creator<PlmnActRecord> CREATOR = new Parcelable.Creator()
  {
    public PlmnActRecord createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PlmnActRecord(paramAnonymousParcel.readString(), paramAnonymousParcel.readInt(), null);
    }
    
    public PlmnActRecord[] newArray(int paramAnonymousInt)
    {
      return new PlmnActRecord[paramAnonymousInt];
    }
  };
  public static final int ENCODED_LENGTH = 5;
  private static final String LOG_TAG = "PlmnActRecord";
  private static final boolean VDBG = false;
  public final int accessTechs;
  public final String plmn;
  
  private PlmnActRecord(String paramString, int paramInt)
  {
    plmn = paramString;
    accessTechs = paramInt;
  }
  
  public PlmnActRecord(byte[] paramArrayOfByte, int paramInt)
  {
    plmn = IccUtils.bcdPlmnToString(paramArrayOfByte, paramInt);
    accessTechs = (Byte.toUnsignedInt(paramArrayOfByte[(paramInt + 3)]) << 8 | Byte.toUnsignedInt(paramArrayOfByte[(paramInt + 4)]));
  }
  
  private String accessTechString()
  {
    if (accessTechs == 0) {
      return "NONE";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    if ((accessTechs & 0x8000) != 0) {
      localStringBuilder.append("UTRAN|");
    }
    if ((accessTechs & 0x4000) != 0) {
      localStringBuilder.append("EUTRAN|");
    }
    if ((accessTechs & 0x80) != 0) {
      localStringBuilder.append("GSM|");
    }
    if ((accessTechs & 0x40) != 0) {
      localStringBuilder.append("GSM_COMPACT|");
    }
    if ((accessTechs & 0x20) != 0) {
      localStringBuilder.append("CDMA2000_HRPD|");
    }
    if ((accessTechs & 0x10) != 0) {
      localStringBuilder.append("CDMA2000_1XRTT|");
    }
    if ((accessTechs & 0x3F0F) != 0) {
      localStringBuilder.append(String.format("UNKNOWN:%x|", new Object[] { Integer.valueOf(accessTechs & 0x3F0F) }));
    }
    return localStringBuilder.substring(0, localStringBuilder.length() - 1);
  }
  
  public static PlmnActRecord[] getRecords(byte[] paramArrayOfByte)
  {
    if ((paramArrayOfByte != null) && (paramArrayOfByte.length != 0) && (paramArrayOfByte.length % 5 == 0))
    {
      int i = paramArrayOfByte.length / 5;
      localObject = new PlmnActRecord[i];
      for (int j = 0; j < i; j++) {
        localObject[j] = new PlmnActRecord(paramArrayOfByte, j * 5);
      }
      return localObject;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Malformed PlmnActRecord, bytes: ");
    if (paramArrayOfByte != null) {
      paramArrayOfByte = Arrays.toString(paramArrayOfByte);
    } else {
      paramArrayOfByte = null;
    }
    ((StringBuilder)localObject).append(paramArrayOfByte);
    Rlog.e("PlmnActRecord", ((StringBuilder)localObject).toString());
    return null;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    return String.format("{PLMN=%s,AccessTechs=%s}", new Object[] { plmn, accessTechString() });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(plmn);
    paramParcel.writeInt(accessTechs);
  }
}
