package android.service.carrier;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.telephony.uicc.IccUtils;
import java.util.Objects;

public class CarrierIdentifier
  implements Parcelable
{
  public static final Parcelable.Creator<CarrierIdentifier> CREATOR = new Parcelable.Creator()
  {
    public CarrierIdentifier createFromParcel(Parcel paramAnonymousParcel)
    {
      return new CarrierIdentifier(paramAnonymousParcel);
    }
    
    public CarrierIdentifier[] newArray(int paramAnonymousInt)
    {
      return new CarrierIdentifier[paramAnonymousInt];
    }
  };
  private String mGid1;
  private String mGid2;
  private String mIccid;
  private String mImsi;
  private String mMcc;
  private String mMnc;
  private String mSpn;
  
  public CarrierIdentifier(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public CarrierIdentifier(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6)
  {
    mMcc = paramString1;
    mMnc = paramString2;
    mSpn = paramString3;
    mImsi = paramString4;
    mGid1 = paramString5;
    mGid2 = paramString6;
    mIccid = null;
  }
  
  public CarrierIdentifier(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7)
  {
    this(paramString1, paramString2, paramString3, paramString4, paramString5, paramString6);
    mIccid = paramString7;
  }
  
  public CarrierIdentifier(byte[] paramArrayOfByte, String paramString1, String paramString2)
  {
    if (paramArrayOfByte.length == 3)
    {
      paramArrayOfByte = IccUtils.bytesToHexString(paramArrayOfByte);
      mMcc = new String(new char[] { paramArrayOfByte.charAt(1), paramArrayOfByte.charAt(0), paramArrayOfByte.charAt(3) });
      if (paramArrayOfByte.charAt(2) == 'F') {
        mMnc = new String(new char[] { paramArrayOfByte.charAt(5), paramArrayOfByte.charAt(4) });
      } else {
        mMnc = new String(new char[] { paramArrayOfByte.charAt(5), paramArrayOfByte.charAt(4), paramArrayOfByte.charAt(2) });
      }
      mGid1 = paramString1;
      mGid2 = paramString2;
      mSpn = null;
      mImsi = null;
      mIccid = null;
      return;
    }
    paramString1 = new StringBuilder();
    paramString1.append("MCC & MNC must be set by a 3-byte array: byte[");
    paramString1.append(paramArrayOfByte.length);
    paramString1.append("]");
    throw new IllegalArgumentException(paramString1.toString());
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
      paramObject = (CarrierIdentifier)paramObject;
      if ((!Objects.equals(mMcc, mMcc)) || (!Objects.equals(mMnc, mMnc)) || (!Objects.equals(mSpn, mSpn)) || (!Objects.equals(mImsi, mImsi)) || (!Objects.equals(mGid1, mGid1)) || (!Objects.equals(mGid2, mGid2)) || (!Objects.equals(mIccid, mIccid))) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public String getGid1()
  {
    return mGid1;
  }
  
  public String getGid2()
  {
    return mGid2;
  }
  
  public String getIccid()
  {
    return mIccid;
  }
  
  public String getImsi()
  {
    return mImsi;
  }
  
  public String getMcc()
  {
    return mMcc;
  }
  
  public String getMnc()
  {
    return mMnc;
  }
  
  public String getSpn()
  {
    return mSpn;
  }
  
  public int hashCode()
  {
    return 31 * (31 * (31 * (31 * (31 * (31 * (31 * 1 + Objects.hashCode(mMcc)) + Objects.hashCode(mMnc)) + Objects.hashCode(mSpn)) + Objects.hashCode(mImsi)) + Objects.hashCode(mGid1)) + Objects.hashCode(mGid2)) + Objects.hashCode(mIccid);
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    mMcc = paramParcel.readString();
    mMnc = paramParcel.readString();
    mSpn = paramParcel.readString();
    mImsi = paramParcel.readString();
    mGid1 = paramParcel.readString();
    mGid2 = paramParcel.readString();
    mIccid = paramParcel.readString();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("CarrierIdentifier{mcc=");
    localStringBuilder.append(mMcc);
    localStringBuilder.append(",mnc=");
    localStringBuilder.append(mMnc);
    localStringBuilder.append(",spn=");
    localStringBuilder.append(mSpn);
    localStringBuilder.append(",imsi=");
    localStringBuilder.append(mImsi);
    localStringBuilder.append(",gid1=");
    localStringBuilder.append(mGid1);
    localStringBuilder.append(",gid2=");
    localStringBuilder.append(mGid2);
    localStringBuilder.append(",iccid=");
    localStringBuilder.append(mIccid);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mMcc);
    paramParcel.writeString(mMnc);
    paramParcel.writeString(mSpn);
    paramParcel.writeString(mImsi);
    paramParcel.writeString(mGid1);
    paramParcel.writeString(mGid2);
    paramParcel.writeString(mIccid);
  }
  
  public static abstract interface MatchType
  {
    public static final int ALL = 0;
    public static final int GID1 = 3;
    public static final int GID2 = 4;
    public static final int ICCID = 5;
    public static final int IMSI_PREFIX = 2;
    public static final int SPN = 1;
  }
}
