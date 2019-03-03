package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.telephony.RILConstants;

public class RadioAccessFamily
  implements Parcelable
{
  private static final int CDMA = 112;
  public static final Parcelable.Creator<RadioAccessFamily> CREATOR = new Parcelable.Creator()
  {
    public RadioAccessFamily createFromParcel(Parcel paramAnonymousParcel)
    {
      return new RadioAccessFamily(paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt());
    }
    
    public RadioAccessFamily[] newArray(int paramAnonymousInt)
    {
      return new RadioAccessFamily[paramAnonymousInt];
    }
  };
  private static final int EVDO = 12672;
  private static final int GSM = 65542;
  private static final int HS = 36352;
  private static final int LTE = 540672;
  public static final int RAF_1xRTT = 64;
  public static final int RAF_EDGE = 4;
  public static final int RAF_EHRPD = 8192;
  public static final int RAF_EVDO_0 = 128;
  public static final int RAF_EVDO_A = 256;
  public static final int RAF_EVDO_B = 4096;
  public static final int RAF_GPRS = 2;
  public static final int RAF_GSM = 65536;
  public static final int RAF_HSDPA = 512;
  public static final int RAF_HSPA = 2048;
  public static final int RAF_HSPAP = 32768;
  public static final int RAF_HSUPA = 1024;
  public static final int RAF_IS95A = 16;
  public static final int RAF_IS95B = 32;
  public static final int RAF_LTE = 16384;
  public static final int RAF_LTE_CA = 524288;
  public static final int RAF_TD_SCDMA = 131072;
  public static final int RAF_UMTS = 8;
  public static final int RAF_UNKNOWN = 1;
  private static final int WCDMA = 36360;
  private int mPhoneId;
  private int mRadioAccessFamily;
  
  public RadioAccessFamily(int paramInt1, int paramInt2)
  {
    mPhoneId = paramInt1;
    mRadioAccessFamily = paramInt2;
  }
  
  private static int getAdjustedRaf(int paramInt)
  {
    if ((0x10006 & paramInt) > 0) {
      paramInt = 0x10006 | paramInt;
    }
    if ((0x8E08 & paramInt) > 0) {
      paramInt = 0x8E08 | paramInt;
    }
    if ((0x70 & paramInt) > 0) {
      paramInt = 0x70 | paramInt;
    }
    if ((0x3180 & paramInt) > 0) {
      paramInt = 0x3180 | paramInt;
    }
    if ((0x84000 & paramInt) > 0) {
      paramInt = 0x84000 | paramInt;
    }
    return paramInt;
  }
  
  public static int getHighestRafCapability(int paramInt)
  {
    if ((0x84000 & paramInt) > 0) {
      return 3;
    }
    if ((0xBF80 | 0x8E08 & paramInt) > 0) {
      return 2;
    }
    if ((0x10006 | 0x70 & paramInt) > 0) {
      return 1;
    }
    return 0;
  }
  
  public static int getNetworkTypeFromRaf(int paramInt)
  {
    switch (getAdjustedRaf(paramInt))
    {
    default: 
      paramInt = RILConstants.PREFERRED_NETWORK_MODE;
      break;
    case 786430: 
      paramInt = 22;
      break;
    case 773646: 
      paramInt = 20;
      break;
    case 737286: 
      paramInt = 17;
      break;
    case 708104: 
      paramInt = 19;
      break;
    case 671744: 
      paramInt = 15;
      break;
    case 655358: 
      paramInt = 10;
      break;
    case 642574: 
      paramInt = 9;
      break;
    case 577032: 
      paramInt = 12;
      break;
    case 553456: 
      paramInt = 8;
      break;
    case 540672: 
      paramInt = 11;
      break;
    case 245758: 
      paramInt = 21;
      break;
    case 232974: 
      paramInt = 18;
      break;
    case 196614: 
      paramInt = 16;
      break;
    case 167432: 
      paramInt = 14;
      break;
    case 131072: 
      paramInt = 13;
      break;
    case 114686: 
      paramInt = 7;
      break;
    case 101902: 
      paramInt = 3;
      break;
    case 65542: 
      paramInt = 1;
      break;
    case 36360: 
      paramInt = 2;
      break;
    case 12784: 
      paramInt = 4;
      break;
    case 12672: 
      paramInt = 6;
      break;
    case 112: 
      paramInt = 5;
    }
    return paramInt;
  }
  
  public static int getRafFromNetworkType(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      paramInt = 1;
      break;
    case 22: 
      paramInt = 786430;
      break;
    case 21: 
      paramInt = 245758;
      break;
    case 20: 
      paramInt = 773646;
      break;
    case 19: 
      paramInt = 708104;
      break;
    case 18: 
      paramInt = 232974;
      break;
    case 17: 
      paramInt = 737286;
      break;
    case 16: 
      paramInt = 196614;
      break;
    case 15: 
      paramInt = 671744;
      break;
    case 14: 
      paramInt = 167432;
      break;
    case 13: 
      paramInt = 131072;
      break;
    case 12: 
      paramInt = 577032;
      break;
    case 11: 
      paramInt = 540672;
      break;
    case 10: 
      paramInt = 655358;
      break;
    case 9: 
      paramInt = 642574;
      break;
    case 8: 
      paramInt = 553456;
      break;
    case 7: 
      paramInt = 114686;
      break;
    case 6: 
      paramInt = 12672;
      break;
    case 5: 
      paramInt = 112;
      break;
    case 4: 
      paramInt = 12784;
      break;
    case 3: 
      paramInt = 101902;
      break;
    case 2: 
      paramInt = 36360;
      break;
    case 1: 
      paramInt = 65542;
      break;
    case 0: 
      paramInt = 101902;
    }
    return paramInt;
  }
  
  public static int rafTypeFromString(String paramString)
  {
    paramString = paramString.toUpperCase().split("\\|");
    int i = 0;
    int j = paramString.length;
    for (int k = 0; k < j; k++)
    {
      int m = singleRafTypeFromString(paramString[k].trim());
      if (m == 1) {
        return m;
      }
      i |= m;
    }
    return i;
  }
  
  public static int singleRafTypeFromString(String paramString)
  {
    switch (paramString.hashCode())
    {
    default: 
      break;
    case 2056938943: 
      if (paramString.equals("EVDO_B")) {
        i = 11;
      }
      break;
    case 2056938942: 
      if (paramString.equals("EVDO_A")) {
        i = 7;
      }
      break;
    case 2056938925: 
      if (paramString.equals("EVDO_0")) {
        i = 6;
      }
      break;
    case 82410124: 
      if (paramString.equals("WCDMA")) {
        i = 20;
      }
      break;
    case 69946172: 
      if (paramString.equals("IS95B")) {
        i = 4;
      }
      break;
    case 69946171: 
      if (paramString.equals("IS95A")) {
        i = 3;
      }
      break;
    case 69050395: 
      if (paramString.equals("HSUPA")) {
        i = 9;
      }
      break;
    case 69045140: 
      if (paramString.equals("HSPAP")) {
        i = 14;
      }
      break;
    case 69034058: 
      if (paramString.equals("HSDPA")) {
        i = 8;
      }
      break;
    case 65949251: 
      if (paramString.equals("EHRPD")) {
        i = 12;
      }
      break;
    case 47955627: 
      if (paramString.equals("1XRTT")) {
        i = 5;
      }
      break;
    case 2608919: 
      if (paramString.equals("UMTS")) {
        i = 2;
      }
      break;
    case 2227260: 
      if (paramString.equals("HSPA")) {
        i = 10;
      }
      break;
    case 2194666: 
      if (paramString.equals("GPRS")) {
        i = 0;
      }
      break;
    case 2140412: 
      if (paramString.equals("EVDO")) {
        i = 19;
      }
      break;
    case 2123197: 
      if (paramString.equals("EDGE")) {
        i = 1;
      }
      break;
    case 2063797: 
      if (paramString.equals("CDMA")) {
        i = 18;
      }
      break;
    case 75709: 
      if (paramString.equals("LTE")) {
        i = 13;
      }
      break;
    case 70881: 
      if (paramString.equals("GSM")) {
        i = 15;
      }
      break;
    case 2315: 
      if (paramString.equals("HS")) {
        i = 17;
      }
      break;
    case -908593671: 
      if (paramString.equals("TD_SCDMA")) {
        i = 16;
      }
      break;
    case -2039427040: 
      if (paramString.equals("LTE_CA")) {
        i = 21;
      }
      break;
    }
    int i = -1;
    switch (i)
    {
    default: 
      return 1;
    case 21: 
      return 524288;
    case 20: 
      return 36360;
    case 19: 
      return 12672;
    case 18: 
      return 112;
    case 17: 
      return 36352;
    case 16: 
      return 131072;
    case 15: 
      return 65536;
    case 14: 
      return 32768;
    case 13: 
      return 16384;
    case 12: 
      return 8192;
    case 11: 
      return 4096;
    case 10: 
      return 2048;
    case 9: 
      return 1024;
    case 8: 
      return 512;
    case 7: 
      return 256;
    case 6: 
      return 128;
    case 5: 
      return 64;
    case 4: 
      return 32;
    case 3: 
      return 16;
    case 2: 
      return 8;
    case 1: 
      return 4;
    }
    return 2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getPhoneId()
  {
    return mPhoneId;
  }
  
  public int getRadioAccessFamily()
  {
    return mRadioAccessFamily;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{ mPhoneId = ");
    localStringBuilder.append(mPhoneId);
    localStringBuilder.append(", mRadioAccessFamily = ");
    localStringBuilder.append(mRadioAccessFamily);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mPhoneId);
    paramParcel.writeInt(mRadioAccessFamily);
  }
}
