package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Objects;

public class NetworkRegistrationState
  implements Parcelable
{
  public static final Parcelable.Creator<NetworkRegistrationState> CREATOR = new Parcelable.Creator()
  {
    public NetworkRegistrationState createFromParcel(Parcel paramAnonymousParcel)
    {
      return new NetworkRegistrationState(paramAnonymousParcel);
    }
    
    public NetworkRegistrationState[] newArray(int paramAnonymousInt)
    {
      return new NetworkRegistrationState[paramAnonymousInt];
    }
  };
  public static final int DOMAIN_CS = 1;
  public static final int DOMAIN_PS = 2;
  public static final int REG_STATE_DENIED = 3;
  public static final int REG_STATE_HOME = 1;
  public static final int REG_STATE_NOT_REG_NOT_SEARCHING = 0;
  public static final int REG_STATE_NOT_REG_SEARCHING = 2;
  public static final int REG_STATE_ROAMING = 5;
  public static final int REG_STATE_UNKNOWN = 4;
  public static final int SERVICE_TYPE_DATA = 2;
  public static final int SERVICE_TYPE_EMERGENCY = 5;
  public static final int SERVICE_TYPE_SMS = 3;
  public static final int SERVICE_TYPE_VIDEO = 4;
  public static final int SERVICE_TYPE_VOICE = 1;
  private final int mAccessNetworkTechnology;
  private final int[] mAvailableServices;
  private final CellIdentity mCellIdentity;
  private DataSpecificRegistrationStates mDataSpecificStates;
  private final int mDomain;
  private final boolean mEmergencyOnly;
  private final int mReasonForDenial;
  private final int mRegState;
  private final int mTransportType;
  private VoiceSpecificRegistrationStates mVoiceSpecificStates;
  
  public NetworkRegistrationState(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean, int[] paramArrayOfInt, CellIdentity paramCellIdentity)
  {
    mTransportType = paramInt1;
    mDomain = paramInt2;
    mRegState = paramInt3;
    mAccessNetworkTechnology = paramInt4;
    mReasonForDenial = paramInt5;
    mAvailableServices = paramArrayOfInt;
    mCellIdentity = paramCellIdentity;
    mEmergencyOnly = paramBoolean;
  }
  
  public NetworkRegistrationState(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean, int[] paramArrayOfInt, CellIdentity paramCellIdentity, int paramInt6)
  {
    this(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramBoolean, paramArrayOfInt, paramCellIdentity);
    mDataSpecificStates = new DataSpecificRegistrationStates(paramInt6);
  }
  
  public NetworkRegistrationState(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean1, int[] paramArrayOfInt, CellIdentity paramCellIdentity, boolean paramBoolean2, int paramInt6, int paramInt7, int paramInt8)
  {
    this(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramBoolean1, paramArrayOfInt, paramCellIdentity);
    mVoiceSpecificStates = new VoiceSpecificRegistrationStates(paramBoolean2, paramInt6, paramInt7, paramInt8);
  }
  
  protected NetworkRegistrationState(Parcel paramParcel)
  {
    mTransportType = paramParcel.readInt();
    mDomain = paramParcel.readInt();
    mRegState = paramParcel.readInt();
    mAccessNetworkTechnology = paramParcel.readInt();
    mReasonForDenial = paramParcel.readInt();
    mEmergencyOnly = paramParcel.readBoolean();
    mAvailableServices = paramParcel.createIntArray();
    mCellIdentity = ((CellIdentity)paramParcel.readParcelable(CellIdentity.class.getClassLoader()));
    mVoiceSpecificStates = ((VoiceSpecificRegistrationStates)paramParcel.readParcelable(VoiceSpecificRegistrationStates.class.getClassLoader()));
    mDataSpecificStates = ((DataSpecificRegistrationStates)paramParcel.readParcelable(DataSpecificRegistrationStates.class.getClassLoader()));
  }
  
  private static boolean equals(Object paramObject1, Object paramObject2)
  {
    if (paramObject1 == paramObject2) {
      return true;
    }
    if (paramObject1 == null) {
      return false;
    }
    return paramObject1.equals(paramObject2);
  }
  
  private static String regStateToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown reg state ");
      localStringBuilder.append(paramInt);
      return localStringBuilder.toString();
    case 5: 
      return "ROAMING";
    case 4: 
      return "UNKNOWN";
    case 3: 
      return "DENIED";
    case 2: 
      return "NOT_REG_SEARCHING";
    case 1: 
      return "HOME";
    }
    return "NOT_REG_NOT_SEARCHING";
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
    if ((paramObject != null) && ((paramObject instanceof NetworkRegistrationState)))
    {
      paramObject = (NetworkRegistrationState)paramObject;
      if ((mTransportType != mTransportType) || (mDomain != mDomain) || (mRegState != mRegState) || (mAccessNetworkTechnology != mAccessNetworkTechnology) || (mReasonForDenial != mReasonForDenial) || (mEmergencyOnly != mEmergencyOnly) || ((mAvailableServices != mAvailableServices) && (!Arrays.equals(mAvailableServices, mAvailableServices))) || (!equals(mCellIdentity, mCellIdentity)) || (!equals(mVoiceSpecificStates, mVoiceSpecificStates)) || (!equals(mDataSpecificStates, mDataSpecificStates))) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public int getAccessNetworkTechnology()
  {
    return mAccessNetworkTechnology;
  }
  
  public int[] getAvailableServices()
  {
    return mAvailableServices;
  }
  
  public CellIdentity getCellIdentity()
  {
    return mCellIdentity;
  }
  
  public DataSpecificRegistrationStates getDataSpecificStates()
  {
    return mDataSpecificStates;
  }
  
  public int getDomain()
  {
    return mDomain;
  }
  
  public int getReasonForDenial()
  {
    return mReasonForDenial;
  }
  
  public int getRegState()
  {
    return mRegState;
  }
  
  public int getTransportType()
  {
    return mTransportType;
  }
  
  public VoiceSpecificRegistrationStates getVoiceSpecificStates()
  {
    return mVoiceSpecificStates;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(mTransportType), Integer.valueOf(mDomain), Integer.valueOf(mRegState), Integer.valueOf(mAccessNetworkTechnology), Integer.valueOf(mReasonForDenial), Boolean.valueOf(mEmergencyOnly), mAvailableServices, mCellIdentity, mVoiceSpecificStates, mDataSpecificStates });
  }
  
  public boolean isEmergencyEnabled()
  {
    return mEmergencyOnly;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("NetworkRegistrationState{");
    localStringBuilder.append("transportType=");
    localStringBuilder.append(mTransportType);
    localStringBuilder.append(" domain=");
    String str;
    if (mDomain == 1) {
      str = "CS";
    } else {
      str = "PS";
    }
    localStringBuilder.append(str);
    localStringBuilder.append(" regState=");
    localStringBuilder.append(regStateToString(mRegState));
    localStringBuilder.append(" accessNetworkTechnology=");
    localStringBuilder.append(TelephonyManager.getNetworkTypeName(mAccessNetworkTechnology));
    localStringBuilder.append(" reasonForDenial=");
    localStringBuilder.append(mReasonForDenial);
    localStringBuilder.append(" emergencyEnabled=");
    localStringBuilder.append(mEmergencyOnly);
    localStringBuilder.append(" supportedServices=");
    localStringBuilder.append(mAvailableServices);
    localStringBuilder.append(" cellIdentity=");
    localStringBuilder.append(mCellIdentity);
    localStringBuilder.append(" voiceSpecificStates=");
    localStringBuilder.append(mVoiceSpecificStates);
    localStringBuilder.append(" dataSpecificStates=");
    localStringBuilder.append(mDataSpecificStates);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mTransportType);
    paramParcel.writeInt(mDomain);
    paramParcel.writeInt(mRegState);
    paramParcel.writeInt(mAccessNetworkTechnology);
    paramParcel.writeInt(mReasonForDenial);
    paramParcel.writeBoolean(mEmergencyOnly);
    paramParcel.writeIntArray(mAvailableServices);
    paramParcel.writeParcelable(mCellIdentity, 0);
    paramParcel.writeParcelable(mVoiceSpecificStates, 0);
    paramParcel.writeParcelable(mDataSpecificStates, 0);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Domain {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface RegState {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ServiceType {}
}
