package android.net.wifi.hotspot2.pps;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

public final class UpdateParameter
  implements Parcelable
{
  private static final int CERTIFICATE_SHA256_BYTES = 32;
  public static final Parcelable.Creator<UpdateParameter> CREATOR = new Parcelable.Creator()
  {
    public UpdateParameter createFromParcel(Parcel paramAnonymousParcel)
    {
      UpdateParameter localUpdateParameter = new UpdateParameter();
      localUpdateParameter.setUpdateIntervalInMinutes(paramAnonymousParcel.readLong());
      localUpdateParameter.setUpdateMethod(paramAnonymousParcel.readString());
      localUpdateParameter.setRestriction(paramAnonymousParcel.readString());
      localUpdateParameter.setServerUri(paramAnonymousParcel.readString());
      localUpdateParameter.setUsername(paramAnonymousParcel.readString());
      localUpdateParameter.setBase64EncodedPassword(paramAnonymousParcel.readString());
      localUpdateParameter.setTrustRootCertUrl(paramAnonymousParcel.readString());
      localUpdateParameter.setTrustRootCertSha256Fingerprint(paramAnonymousParcel.createByteArray());
      return localUpdateParameter;
    }
    
    public UpdateParameter[] newArray(int paramAnonymousInt)
    {
      return new UpdateParameter[paramAnonymousInt];
    }
  };
  private static final int MAX_PASSWORD_BYTES = 255;
  private static final int MAX_URI_BYTES = 1023;
  private static final int MAX_URL_BYTES = 1023;
  private static final int MAX_USERNAME_BYTES = 63;
  private static final String TAG = "UpdateParameter";
  public static final long UPDATE_CHECK_INTERVAL_NEVER = 4294967295L;
  public static final String UPDATE_METHOD_OMADM = "OMA-DM-ClientInitiated";
  public static final String UPDATE_METHOD_SSP = "SSP-ClientInitiated";
  public static final String UPDATE_RESTRICTION_HOMESP = "HomeSP";
  public static final String UPDATE_RESTRICTION_ROAMING_PARTNER = "RoamingPartner";
  public static final String UPDATE_RESTRICTION_UNRESTRICTED = "Unrestricted";
  private String mBase64EncodedPassword = null;
  private String mRestriction = null;
  private String mServerUri = null;
  private byte[] mTrustRootCertSha256Fingerprint = null;
  private String mTrustRootCertUrl = null;
  private long mUpdateIntervalInMinutes = Long.MIN_VALUE;
  private String mUpdateMethod = null;
  private String mUsername = null;
  
  public UpdateParameter() {}
  
  public UpdateParameter(UpdateParameter paramUpdateParameter)
  {
    if (paramUpdateParameter == null) {
      return;
    }
    mUpdateIntervalInMinutes = mUpdateIntervalInMinutes;
    mUpdateMethod = mUpdateMethod;
    mRestriction = mRestriction;
    mServerUri = mServerUri;
    mUsername = mUsername;
    mBase64EncodedPassword = mBase64EncodedPassword;
    mTrustRootCertUrl = mTrustRootCertUrl;
    if (mTrustRootCertSha256Fingerprint != null) {
      mTrustRootCertSha256Fingerprint = Arrays.copyOf(mTrustRootCertSha256Fingerprint, mTrustRootCertSha256Fingerprint.length);
    }
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
    if (!(paramObject instanceof UpdateParameter)) {
      return false;
    }
    paramObject = (UpdateParameter)paramObject;
    if ((mUpdateIntervalInMinutes != mUpdateIntervalInMinutes) || (!TextUtils.equals(mUpdateMethod, mUpdateMethod)) || (!TextUtils.equals(mRestriction, mRestriction)) || (!TextUtils.equals(mServerUri, mServerUri)) || (!TextUtils.equals(mUsername, mUsername)) || (!TextUtils.equals(mBase64EncodedPassword, mBase64EncodedPassword)) || (!TextUtils.equals(mTrustRootCertUrl, mTrustRootCertUrl)) || (!Arrays.equals(mTrustRootCertSha256Fingerprint, mTrustRootCertSha256Fingerprint))) {
      bool = false;
    }
    return bool;
  }
  
  public String getBase64EncodedPassword()
  {
    return mBase64EncodedPassword;
  }
  
  public String getRestriction()
  {
    return mRestriction;
  }
  
  public String getServerUri()
  {
    return mServerUri;
  }
  
  public byte[] getTrustRootCertSha256Fingerprint()
  {
    return mTrustRootCertSha256Fingerprint;
  }
  
  public String getTrustRootCertUrl()
  {
    return mTrustRootCertUrl;
  }
  
  public long getUpdateIntervalInMinutes()
  {
    return mUpdateIntervalInMinutes;
  }
  
  public String getUpdateMethod()
  {
    return mUpdateMethod;
  }
  
  public String getUsername()
  {
    return mUsername;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Long.valueOf(mUpdateIntervalInMinutes), mUpdateMethod, mRestriction, mServerUri, mUsername, mBase64EncodedPassword, mTrustRootCertUrl, mTrustRootCertSha256Fingerprint });
  }
  
  public void setBase64EncodedPassword(String paramString)
  {
    mBase64EncodedPassword = paramString;
  }
  
  public void setRestriction(String paramString)
  {
    mRestriction = paramString;
  }
  
  public void setServerUri(String paramString)
  {
    mServerUri = paramString;
  }
  
  public void setTrustRootCertSha256Fingerprint(byte[] paramArrayOfByte)
  {
    mTrustRootCertSha256Fingerprint = paramArrayOfByte;
  }
  
  public void setTrustRootCertUrl(String paramString)
  {
    mTrustRootCertUrl = paramString;
  }
  
  public void setUpdateIntervalInMinutes(long paramLong)
  {
    mUpdateIntervalInMinutes = paramLong;
  }
  
  public void setUpdateMethod(String paramString)
  {
    mUpdateMethod = paramString;
  }
  
  public void setUsername(String paramString)
  {
    mUsername = paramString;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("UpdateInterval: ");
    localStringBuilder.append(mUpdateIntervalInMinutes);
    localStringBuilder.append("\n");
    localStringBuilder.append("UpdateMethod: ");
    localStringBuilder.append(mUpdateMethod);
    localStringBuilder.append("\n");
    localStringBuilder.append("Restriction: ");
    localStringBuilder.append(mRestriction);
    localStringBuilder.append("\n");
    localStringBuilder.append("ServerURI: ");
    localStringBuilder.append(mServerUri);
    localStringBuilder.append("\n");
    localStringBuilder.append("Username: ");
    localStringBuilder.append(mUsername);
    localStringBuilder.append("\n");
    localStringBuilder.append("TrustRootCertURL: ");
    localStringBuilder.append(mTrustRootCertUrl);
    localStringBuilder.append("\n");
    return localStringBuilder.toString();
  }
  
  public boolean validate()
  {
    if (mUpdateIntervalInMinutes == Long.MIN_VALUE)
    {
      Log.d("UpdateParameter", "Update interval not specified");
      return false;
    }
    if (mUpdateIntervalInMinutes == 4294967295L) {
      return true;
    }
    StringBuilder localStringBuilder1;
    if ((!TextUtils.equals(mUpdateMethod, "OMA-DM-ClientInitiated")) && (!TextUtils.equals(mUpdateMethod, "SSP-ClientInitiated")))
    {
      localStringBuilder1 = new StringBuilder();
      localStringBuilder1.append("Unknown update method: ");
      localStringBuilder1.append(mUpdateMethod);
      Log.d("UpdateParameter", localStringBuilder1.toString());
      return false;
    }
    if ((!TextUtils.equals(mRestriction, "HomeSP")) && (!TextUtils.equals(mRestriction, "RoamingPartner")) && (!TextUtils.equals(mRestriction, "Unrestricted")))
    {
      localStringBuilder1 = new StringBuilder();
      localStringBuilder1.append("Unknown restriction: ");
      localStringBuilder1.append(mRestriction);
      Log.d("UpdateParameter", localStringBuilder1.toString());
      return false;
    }
    if (TextUtils.isEmpty(mServerUri))
    {
      Log.d("UpdateParameter", "Missing update server URI");
      return false;
    }
    if (mServerUri.getBytes(StandardCharsets.UTF_8).length > 1023)
    {
      localStringBuilder1 = new StringBuilder();
      localStringBuilder1.append("URI bytes exceeded the max: ");
      localStringBuilder1.append(mServerUri.getBytes(StandardCharsets.UTF_8).length);
      Log.d("UpdateParameter", localStringBuilder1.toString());
      return false;
    }
    if (TextUtils.isEmpty(mUsername))
    {
      Log.d("UpdateParameter", "Missing username");
      return false;
    }
    if (mUsername.getBytes(StandardCharsets.UTF_8).length > 63)
    {
      localStringBuilder1 = new StringBuilder();
      localStringBuilder1.append("Username bytes exceeded the max: ");
      localStringBuilder1.append(mUsername.getBytes(StandardCharsets.UTF_8).length);
      Log.d("UpdateParameter", localStringBuilder1.toString());
      return false;
    }
    if (TextUtils.isEmpty(mBase64EncodedPassword))
    {
      Log.d("UpdateParameter", "Missing username");
      return false;
    }
    if (mBase64EncodedPassword.getBytes(StandardCharsets.UTF_8).length > 255)
    {
      localStringBuilder1 = new StringBuilder();
      localStringBuilder1.append("Password bytes exceeded the max: ");
      localStringBuilder1.append(mBase64EncodedPassword.getBytes(StandardCharsets.UTF_8).length);
      Log.d("UpdateParameter", localStringBuilder1.toString());
      return false;
    }
    try
    {
      Base64.decode(mBase64EncodedPassword, 0);
      if (TextUtils.isEmpty(mTrustRootCertUrl))
      {
        Log.d("UpdateParameter", "Missing trust root certificate URL");
        return false;
      }
      if (mTrustRootCertUrl.getBytes(StandardCharsets.UTF_8).length > 1023)
      {
        localStringBuilder1 = new StringBuilder();
        localStringBuilder1.append("Trust root cert URL bytes exceeded the max: ");
        localStringBuilder1.append(mTrustRootCertUrl.getBytes(StandardCharsets.UTF_8).length);
        Log.d("UpdateParameter", localStringBuilder1.toString());
        return false;
      }
      if (mTrustRootCertSha256Fingerprint == null)
      {
        Log.d("UpdateParameter", "Missing trust root certificate SHA-256 fingerprint");
        return false;
      }
      if (mTrustRootCertSha256Fingerprint.length != 32)
      {
        localStringBuilder1 = new StringBuilder();
        localStringBuilder1.append("Incorrect size of trust root certificate SHA-256 fingerprint: ");
        localStringBuilder1.append(mTrustRootCertSha256Fingerprint.length);
        Log.d("UpdateParameter", localStringBuilder1.toString());
        return false;
      }
      return true;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      StringBuilder localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append("Invalid encoding for password: ");
      localStringBuilder2.append(mBase64EncodedPassword);
      Log.d("UpdateParameter", localStringBuilder2.toString());
    }
    return false;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(mUpdateIntervalInMinutes);
    paramParcel.writeString(mUpdateMethod);
    paramParcel.writeString(mRestriction);
    paramParcel.writeString(mServerUri);
    paramParcel.writeString(mUsername);
    paramParcel.writeString(mBase64EncodedPassword);
    paramParcel.writeString(mTrustRootCertUrl);
    paramParcel.writeByteArray(mTrustRootCertSha256Fingerprint);
  }
}
