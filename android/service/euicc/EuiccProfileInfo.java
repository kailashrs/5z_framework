package android.service.euicc;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.service.carrier.CarrierIdentifier;
import android.telephony.UiccAccessRule;
import android.text.TextUtils;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SystemApi
public final class EuiccProfileInfo
  implements Parcelable
{
  public static final Parcelable.Creator<EuiccProfileInfo> CREATOR = new Parcelable.Creator()
  {
    public EuiccProfileInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new EuiccProfileInfo(paramAnonymousParcel, null);
    }
    
    public EuiccProfileInfo[] newArray(int paramAnonymousInt)
    {
      return new EuiccProfileInfo[paramAnonymousInt];
    }
  };
  public static final int POLICY_RULE_DELETE_AFTER_DISABLING = 4;
  public static final int POLICY_RULE_DO_NOT_DELETE = 2;
  public static final int POLICY_RULE_DO_NOT_DISABLE = 1;
  public static final int PROFILE_CLASS_OPERATIONAL = 2;
  public static final int PROFILE_CLASS_PROVISIONING = 1;
  public static final int PROFILE_CLASS_TESTING = 0;
  public static final int PROFILE_CLASS_UNSET = -1;
  public static final int PROFILE_STATE_DISABLED = 0;
  public static final int PROFILE_STATE_ENABLED = 1;
  public static final int PROFILE_STATE_UNSET = -1;
  private final UiccAccessRule[] mAccessRules;
  private final CarrierIdentifier mCarrierIdentifier;
  private final String mIccid;
  private final String mNickname;
  private final int mPolicyRules;
  private final int mProfileClass;
  private final String mProfileName;
  private final String mServiceProviderName;
  private final int mState;
  
  private EuiccProfileInfo(Parcel paramParcel)
  {
    mIccid = paramParcel.readString();
    mNickname = paramParcel.readString();
    mServiceProviderName = paramParcel.readString();
    mProfileName = paramParcel.readString();
    mProfileClass = paramParcel.readInt();
    mState = paramParcel.readInt();
    if (paramParcel.readByte() == 1) {
      mCarrierIdentifier = ((CarrierIdentifier)CarrierIdentifier.CREATOR.createFromParcel(paramParcel));
    } else {
      mCarrierIdentifier = null;
    }
    mPolicyRules = paramParcel.readInt();
    mAccessRules = ((UiccAccessRule[])paramParcel.createTypedArray(UiccAccessRule.CREATOR));
  }
  
  private EuiccProfileInfo(String paramString1, String paramString2, String paramString3, String paramString4, int paramInt1, int paramInt2, CarrierIdentifier paramCarrierIdentifier, int paramInt3, List<UiccAccessRule> paramList)
  {
    mIccid = paramString1;
    mNickname = paramString2;
    mServiceProviderName = paramString3;
    mProfileName = paramString4;
    mProfileClass = paramInt1;
    mState = paramInt2;
    mCarrierIdentifier = paramCarrierIdentifier;
    mPolicyRules = paramInt3;
    if ((paramList != null) && (paramList.size() > 0)) {
      mAccessRules = ((UiccAccessRule[])paramList.toArray(new UiccAccessRule[paramList.size()]));
    } else {
      mAccessRules = null;
    }
  }
  
  @Deprecated
  public EuiccProfileInfo(String paramString1, UiccAccessRule[] paramArrayOfUiccAccessRule, String paramString2)
  {
    if (TextUtils.isDigitsOnly(paramString1))
    {
      mIccid = paramString1;
      mAccessRules = paramArrayOfUiccAccessRule;
      mNickname = paramString2;
      mServiceProviderName = null;
      mProfileName = null;
      mProfileClass = -1;
      mState = -1;
      mCarrierIdentifier = null;
      mPolicyRules = 0;
      return;
    }
    paramArrayOfUiccAccessRule = new StringBuilder();
    paramArrayOfUiccAccessRule.append("iccid contains invalid characters: ");
    paramArrayOfUiccAccessRule.append(paramString1);
    throw new IllegalArgumentException(paramArrayOfUiccAccessRule.toString());
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
      paramObject = (EuiccProfileInfo)paramObject;
      if ((!Objects.equals(mIccid, mIccid)) || (!Objects.equals(mNickname, mNickname)) || (!Objects.equals(mServiceProviderName, mServiceProviderName)) || (!Objects.equals(mProfileName, mProfileName)) || (mProfileClass != mProfileClass) || (mState != mState) || (!Objects.equals(mCarrierIdentifier, mCarrierIdentifier)) || (mPolicyRules != mPolicyRules) || (!Arrays.equals(mAccessRules, mAccessRules))) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public CarrierIdentifier getCarrierIdentifier()
  {
    return mCarrierIdentifier;
  }
  
  public String getIccid()
  {
    return mIccid;
  }
  
  public String getNickname()
  {
    return mNickname;
  }
  
  public int getPolicyRules()
  {
    return mPolicyRules;
  }
  
  public int getProfileClass()
  {
    return mProfileClass;
  }
  
  public String getProfileName()
  {
    return mProfileName;
  }
  
  public String getServiceProviderName()
  {
    return mServiceProviderName;
  }
  
  public int getState()
  {
    return mState;
  }
  
  public List<UiccAccessRule> getUiccAccessRules()
  {
    if (mAccessRules == null) {
      return null;
    }
    return Arrays.asList(mAccessRules);
  }
  
  public boolean hasPolicyRule(int paramInt)
  {
    boolean bool;
    if ((mPolicyRules & paramInt) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasPolicyRules()
  {
    boolean bool;
    if (mPolicyRules != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public int hashCode()
  {
    return 31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * 1 + Objects.hashCode(mIccid)) + Objects.hashCode(mNickname)) + Objects.hashCode(mServiceProviderName)) + Objects.hashCode(mProfileName)) + mProfileClass) + mState) + Objects.hashCode(mCarrierIdentifier)) + mPolicyRules) + Arrays.hashCode(mAccessRules);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("EuiccProfileInfo (nickname=");
    localStringBuilder.append(mNickname);
    localStringBuilder.append(", serviceProviderName=");
    localStringBuilder.append(mServiceProviderName);
    localStringBuilder.append(", profileName=");
    localStringBuilder.append(mProfileName);
    localStringBuilder.append(", profileClass=");
    localStringBuilder.append(mProfileClass);
    localStringBuilder.append(", state=");
    localStringBuilder.append(mState);
    localStringBuilder.append(", CarrierIdentifier=");
    localStringBuilder.append(mCarrierIdentifier);
    localStringBuilder.append(", policyRules=");
    localStringBuilder.append(mPolicyRules);
    localStringBuilder.append(", accessRules=");
    localStringBuilder.append(Arrays.toString(mAccessRules));
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mIccid);
    paramParcel.writeString(mNickname);
    paramParcel.writeString(mServiceProviderName);
    paramParcel.writeString(mProfileName);
    paramParcel.writeInt(mProfileClass);
    paramParcel.writeInt(mState);
    if (mCarrierIdentifier != null)
    {
      paramParcel.writeByte((byte)1);
      mCarrierIdentifier.writeToParcel(paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeByte((byte)0);
    }
    paramParcel.writeInt(mPolicyRules);
    paramParcel.writeTypedArray(mAccessRules, paramInt);
  }
  
  public static final class Builder
  {
    private List<UiccAccessRule> mAccessRules;
    private CarrierIdentifier mCarrierIdentifier;
    private String mIccid;
    private String mNickname;
    private int mPolicyRules;
    private int mProfileClass;
    private String mProfileName;
    private String mServiceProviderName;
    private int mState;
    
    public Builder(EuiccProfileInfo paramEuiccProfileInfo)
    {
      mIccid = mIccid;
      mNickname = mNickname;
      mServiceProviderName = mServiceProviderName;
      mProfileName = mProfileName;
      mProfileClass = mProfileClass;
      mState = mState;
      mCarrierIdentifier = mCarrierIdentifier;
      mPolicyRules = mPolicyRules;
      mAccessRules = Arrays.asList(mAccessRules);
    }
    
    public Builder(String paramString)
    {
      if (TextUtils.isDigitsOnly(paramString))
      {
        mIccid = paramString;
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("iccid contains invalid characters: ");
      localStringBuilder.append(paramString);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public EuiccProfileInfo build()
    {
      if (mIccid != null) {
        return new EuiccProfileInfo(mIccid, mNickname, mServiceProviderName, mProfileName, mProfileClass, mState, mCarrierIdentifier, mPolicyRules, mAccessRules, null);
      }
      throw new IllegalStateException("ICCID must be set for a profile.");
    }
    
    public Builder setCarrierIdentifier(CarrierIdentifier paramCarrierIdentifier)
    {
      mCarrierIdentifier = paramCarrierIdentifier;
      return this;
    }
    
    public Builder setIccid(String paramString)
    {
      if (TextUtils.isDigitsOnly(paramString))
      {
        mIccid = paramString;
        return this;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("iccid contains invalid characters: ");
      localStringBuilder.append(paramString);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public Builder setNickname(String paramString)
    {
      mNickname = paramString;
      return this;
    }
    
    public Builder setPolicyRules(int paramInt)
    {
      mPolicyRules = paramInt;
      return this;
    }
    
    public Builder setProfileClass(int paramInt)
    {
      mProfileClass = paramInt;
      return this;
    }
    
    public Builder setProfileName(String paramString)
    {
      mProfileName = paramString;
      return this;
    }
    
    public Builder setServiceProviderName(String paramString)
    {
      mServiceProviderName = paramString;
      return this;
    }
    
    public Builder setState(int paramInt)
    {
      mState = paramInt;
      return this;
    }
    
    public Builder setUiccAccessRule(List<UiccAccessRule> paramList)
    {
      mAccessRules = paramList;
      return this;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface PolicyRule {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ProfileClass {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ProfileState {}
}
