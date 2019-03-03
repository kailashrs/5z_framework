package android.telephony.data;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;

public final class DataProfile
  implements Parcelable
{
  public static final Parcelable.Creator<DataProfile> CREATOR = new Parcelable.Creator()
  {
    public DataProfile createFromParcel(Parcel paramAnonymousParcel)
    {
      return new DataProfile(paramAnonymousParcel);
    }
    
    public DataProfile[] newArray(int paramAnonymousInt)
    {
      return new DataProfile[paramAnonymousInt];
    }
  };
  public static final int TYPE_3GPP = 1;
  public static final int TYPE_3GPP2 = 2;
  public static final int TYPE_COMMON = 0;
  private final String mApn;
  private final int mAuthType;
  private final int mBearerBitmap;
  private final boolean mEnabled;
  private final int mMaxConns;
  private final int mMaxConnsTime;
  private final boolean mModemCognitive;
  private final int mMtu;
  private final String mMvnoMatchData;
  private final String mMvnoType;
  private final String mPassword;
  private final int mProfileId;
  private final String mProtocol;
  private final String mRoamingProtocol;
  private final int mSupportedApnTypesBitmap;
  private final int mType;
  private final String mUserName;
  private final int mWaitTime;
  
  public DataProfile(int paramInt1, String paramString1, String paramString2, int paramInt2, String paramString3, String paramString4, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean1, int paramInt7, String paramString5, int paramInt8, int paramInt9, String paramString6, String paramString7, boolean paramBoolean2)
  {
    mProfileId = paramInt1;
    mApn = paramString1;
    mProtocol = paramString2;
    if (paramInt2 == -1)
    {
      if (TextUtils.isEmpty(paramString3)) {
        paramInt1 = 0;
      } else {
        paramInt1 = 3;
      }
    }
    else {
      paramInt1 = paramInt2;
    }
    mAuthType = paramInt1;
    mUserName = paramString3;
    mPassword = paramString4;
    mType = paramInt3;
    mMaxConnsTime = paramInt4;
    mMaxConns = paramInt5;
    mWaitTime = paramInt6;
    mEnabled = paramBoolean1;
    mSupportedApnTypesBitmap = paramInt7;
    mRoamingProtocol = paramString5;
    mBearerBitmap = paramInt8;
    mMtu = paramInt9;
    mMvnoType = paramString6;
    mMvnoMatchData = paramString7;
    mModemCognitive = paramBoolean2;
  }
  
  public DataProfile(Parcel paramParcel)
  {
    mProfileId = paramParcel.readInt();
    mApn = paramParcel.readString();
    mProtocol = paramParcel.readString();
    mAuthType = paramParcel.readInt();
    mUserName = paramParcel.readString();
    mPassword = paramParcel.readString();
    mType = paramParcel.readInt();
    mMaxConnsTime = paramParcel.readInt();
    mMaxConns = paramParcel.readInt();
    mWaitTime = paramParcel.readInt();
    mEnabled = paramParcel.readBoolean();
    mSupportedApnTypesBitmap = paramParcel.readInt();
    mRoamingProtocol = paramParcel.readString();
    mBearerBitmap = paramParcel.readInt();
    mMtu = paramParcel.readInt();
    mMvnoType = paramParcel.readString();
    mMvnoMatchData = paramParcel.readString();
    mModemCognitive = paramParcel.readBoolean();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof DataProfile;
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    if ((paramObject != this) && (!toString().equals(paramObject.toString()))) {
      return bool2;
    }
    bool2 = true;
    return bool2;
  }
  
  public String getApn()
  {
    return mApn;
  }
  
  public int getAuthType()
  {
    return mAuthType;
  }
  
  public int getBearerBitmap()
  {
    return mBearerBitmap;
  }
  
  public int getMaxConns()
  {
    return mMaxConns;
  }
  
  public int getMaxConnsTime()
  {
    return mMaxConnsTime;
  }
  
  public int getMtu()
  {
    return mMtu;
  }
  
  public String getMvnoMatchData()
  {
    return mMvnoMatchData;
  }
  
  public String getMvnoType()
  {
    return mMvnoType;
  }
  
  public String getPassword()
  {
    return mPassword;
  }
  
  public int getProfileId()
  {
    return mProfileId;
  }
  
  public String getProtocol()
  {
    return mProtocol;
  }
  
  public String getRoamingProtocol()
  {
    return mRoamingProtocol;
  }
  
  public int getSupportedApnTypesBitmap()
  {
    return mSupportedApnTypesBitmap;
  }
  
  public int getType()
  {
    return mType;
  }
  
  public String getUserName()
  {
    return mUserName;
  }
  
  public int getWaitTime()
  {
    return mWaitTime;
  }
  
  public boolean isEnabled()
  {
    return mEnabled;
  }
  
  public boolean isModemCognitive()
  {
    return mModemCognitive;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("DataProfile=");
    localStringBuilder.append(mProfileId);
    localStringBuilder.append("/");
    localStringBuilder.append(mProtocol);
    localStringBuilder.append("/");
    localStringBuilder.append(mAuthType);
    localStringBuilder.append("/");
    Object localObject;
    if (Build.IS_USER)
    {
      localObject = "***/***/***";
    }
    else
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(mApn);
      ((StringBuilder)localObject).append("/");
      ((StringBuilder)localObject).append(mUserName);
      ((StringBuilder)localObject).append("/");
      ((StringBuilder)localObject).append(mPassword);
      localObject = ((StringBuilder)localObject).toString();
    }
    localStringBuilder.append((String)localObject);
    localStringBuilder.append("/");
    localStringBuilder.append(mType);
    localStringBuilder.append("/");
    localStringBuilder.append(mMaxConnsTime);
    localStringBuilder.append("/");
    localStringBuilder.append(mMaxConns);
    localStringBuilder.append("/");
    localStringBuilder.append(mWaitTime);
    localStringBuilder.append("/");
    localStringBuilder.append(mEnabled);
    localStringBuilder.append("/");
    localStringBuilder.append(mSupportedApnTypesBitmap);
    localStringBuilder.append("/");
    localStringBuilder.append(mRoamingProtocol);
    localStringBuilder.append("/");
    localStringBuilder.append(mBearerBitmap);
    localStringBuilder.append("/");
    localStringBuilder.append(mMtu);
    localStringBuilder.append("/");
    localStringBuilder.append(mMvnoType);
    localStringBuilder.append("/");
    localStringBuilder.append(mMvnoMatchData);
    localStringBuilder.append("/");
    localStringBuilder.append(mModemCognitive);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mProfileId);
    paramParcel.writeString(mApn);
    paramParcel.writeString(mProtocol);
    paramParcel.writeInt(mAuthType);
    paramParcel.writeString(mUserName);
    paramParcel.writeString(mPassword);
    paramParcel.writeInt(mType);
    paramParcel.writeInt(mMaxConnsTime);
    paramParcel.writeInt(mMaxConns);
    paramParcel.writeInt(mWaitTime);
    paramParcel.writeBoolean(mEnabled);
    paramParcel.writeInt(mSupportedApnTypesBitmap);
    paramParcel.writeString(mRoamingProtocol);
    paramParcel.writeInt(mBearerBitmap);
    paramParcel.writeInt(mMtu);
    paramParcel.writeString(mMvnoType);
    paramParcel.writeString(mMvnoMatchData);
    paramParcel.writeBoolean(mModemCognitive);
  }
}
