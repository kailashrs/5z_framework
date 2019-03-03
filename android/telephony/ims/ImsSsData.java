package android.telephony.ims;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SystemApi
public final class ImsSsData
  implements Parcelable
{
  public static final Parcelable.Creator<ImsSsData> CREATOR = new Parcelable.Creator()
  {
    public ImsSsData createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ImsSsData(paramAnonymousParcel, null);
    }
    
    public ImsSsData[] newArray(int paramAnonymousInt)
    {
      return new ImsSsData[paramAnonymousInt];
    }
  };
  public static final int RESULT_SUCCESS = 0;
  public static final int SERVICE_CLASS_DATA = 2;
  public static final int SERVICE_CLASS_DATA_ASYNC = 32;
  public static final int SERVICE_CLASS_DATA_SYNC = 16;
  public static final int SERVICE_CLASS_FAX = 4;
  public static final int SERVICE_CLASS_NONE = 0;
  public static final int SERVICE_CLASS_PACKET = 64;
  public static final int SERVICE_CLASS_PAD = 128;
  public static final int SERVICE_CLASS_SMS = 8;
  public static final int SERVICE_CLASS_VOICE = 1;
  public static final int SS_ACTIVATION = 0;
  public static final int SS_ALL_BARRING = 18;
  public static final int SS_ALL_DATA_TELESERVICES = 3;
  public static final int SS_ALL_TELESERVICES_EXCEPT_SMS = 5;
  public static final int SS_ALL_TELESEVICES = 1;
  public static final int SS_ALL_TELE_AND_BEARER_SERVICES = 0;
  public static final int SS_BAIC = 16;
  public static final int SS_BAIC_ROAMING = 17;
  public static final int SS_BAOC = 13;
  public static final int SS_BAOIC = 14;
  public static final int SS_BAOIC_EXC_HOME = 15;
  public static final int SS_CFU = 0;
  public static final int SS_CFUT = 6;
  public static final int SS_CF_ALL = 4;
  public static final int SS_CF_ALL_CONDITIONAL = 5;
  public static final int SS_CF_BUSY = 1;
  public static final int SS_CF_NOT_REACHABLE = 3;
  public static final int SS_CF_NO_REPLY = 2;
  public static final int SS_CLIP = 7;
  public static final int SS_CLIR = 8;
  public static final int SS_CNAP = 11;
  public static final int SS_COLP = 9;
  public static final int SS_COLR = 10;
  public static final int SS_DEACTIVATION = 1;
  public static final int SS_ERASURE = 4;
  public static final int SS_INCOMING_BARRING = 20;
  public static final int SS_INCOMING_BARRING_ANONYMOUS = 22;
  public static final int SS_INCOMING_BARRING_DN = 21;
  public static final int SS_INTERROGATION = 2;
  public static final int SS_OUTGOING_BARRING = 19;
  public static final int SS_REGISTRATION = 3;
  public static final int SS_SMS_SERVICES = 4;
  public static final int SS_TELEPHONY = 2;
  public static final int SS_WAIT = 12;
  private ImsCallForwardInfo[] mCfInfo;
  private ImsSsInfo[] mImsSsInfo;
  private int[] mSsInfo;
  public int requestType;
  public final int result;
  public int serviceClass;
  public int serviceType;
  public int teleserviceType;
  
  public ImsSsData(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    serviceType = paramInt1;
    requestType = paramInt2;
    teleserviceType = paramInt3;
    serviceClass = paramInt4;
    result = paramInt5;
  }
  
  private ImsSsData(Parcel paramParcel)
  {
    serviceType = paramParcel.readInt();
    requestType = paramParcel.readInt();
    teleserviceType = paramParcel.readInt();
    serviceClass = paramParcel.readInt();
    result = paramParcel.readInt();
    mSsInfo = paramParcel.createIntArray();
    mCfInfo = ((ImsCallForwardInfo[])paramParcel.readParcelableArray(getClass().getClassLoader()));
    mImsSsInfo = ((ImsSsInfo[])paramParcel.readParcelableArray(getClass().getClassLoader()));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public ImsCallForwardInfo[] getCallForwardInfo()
  {
    return mCfInfo;
  }
  
  public ImsSsInfo[] getImsSpecificSuppServiceInfo()
  {
    return mImsSsInfo;
  }
  
  public int[] getSuppServiceInfo()
  {
    return mSsInfo;
  }
  
  public boolean isTypeBarring()
  {
    boolean bool;
    if ((serviceType != 13) && (serviceType != 14) && (serviceType != 15) && (serviceType != 16) && (serviceType != 17) && (serviceType != 18) && (serviceType != 19) && (serviceType != 20)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean isTypeCF()
  {
    int i = serviceType;
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (i != 0)
    {
      bool2 = bool1;
      if (serviceType != 1)
      {
        bool2 = bool1;
        if (serviceType != 2)
        {
          bool2 = bool1;
          if (serviceType != 3)
          {
            bool2 = bool1;
            if (serviceType != 4) {
              if (serviceType == 5) {
                bool2 = bool1;
              } else {
                bool2 = false;
              }
            }
          }
        }
      }
    }
    return bool2;
  }
  
  public boolean isTypeCW()
  {
    boolean bool;
    if (serviceType == 12) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isTypeCf()
  {
    return isTypeCF();
  }
  
  public boolean isTypeClip()
  {
    boolean bool;
    if (serviceType == 7) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isTypeClir()
  {
    boolean bool;
    if (serviceType == 8) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isTypeColp()
  {
    boolean bool;
    if (serviceType == 9) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isTypeColr()
  {
    boolean bool;
    if (serviceType == 10) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isTypeCw()
  {
    return isTypeCW();
  }
  
  public boolean isTypeIcb()
  {
    boolean bool;
    if ((serviceType != 21) && (serviceType != 22)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean isTypeInterrogation()
  {
    boolean bool;
    if (serviceType == 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isTypeUnConditional()
  {
    boolean bool;
    if ((serviceType != 0) && (serviceType != 4)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public void setCallForwardingInfo(ImsCallForwardInfo[] paramArrayOfImsCallForwardInfo)
  {
    mCfInfo = paramArrayOfImsCallForwardInfo;
  }
  
  public void setImsSpecificSuppServiceInfo(ImsSsInfo[] paramArrayOfImsSsInfo)
  {
    mImsSsInfo = paramArrayOfImsSsInfo;
  }
  
  public void setSuppServiceInfo(int[] paramArrayOfInt)
  {
    mSsInfo = paramArrayOfInt;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[ImsSsData] ServiceType: ");
    localStringBuilder.append(serviceType);
    localStringBuilder.append(" RequestType: ");
    localStringBuilder.append(requestType);
    localStringBuilder.append(" TeleserviceType: ");
    localStringBuilder.append(teleserviceType);
    localStringBuilder.append(" ServiceClass: ");
    localStringBuilder.append(serviceClass);
    localStringBuilder.append(" Result: ");
    localStringBuilder.append(result);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(serviceType);
    paramParcel.writeInt(requestType);
    paramParcel.writeInt(teleserviceType);
    paramParcel.writeInt(serviceClass);
    paramParcel.writeInt(result);
    paramParcel.writeIntArray(mSsInfo);
    paramParcel.writeParcelableArray(mCfInfo, 0);
    paramParcel.writeParcelableArray(mImsSsInfo, 0);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ServiceClass {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ServiceType {}
}
