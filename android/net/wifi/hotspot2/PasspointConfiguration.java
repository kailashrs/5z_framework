package android.net.wifi.hotspot2;

import android.net.wifi.hotspot2.pps.Credential;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.net.wifi.hotspot2.pps.Policy;
import android.net.wifi.hotspot2.pps.UpdateParameter;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

public final class PasspointConfiguration
  implements Parcelable
{
  private static final int CERTIFICATE_SHA256_BYTES = 32;
  public static final Parcelable.Creator<PasspointConfiguration> CREATOR = new Parcelable.Creator()
  {
    private Map<String, byte[]> readTrustRootCerts(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      if (i == -1) {
        return null;
      }
      HashMap localHashMap = new HashMap(i);
      for (int j = 0; j < i; j++) {
        localHashMap.put(paramAnonymousParcel.readString(), paramAnonymousParcel.createByteArray());
      }
      return localHashMap;
    }
    
    public PasspointConfiguration createFromParcel(Parcel paramAnonymousParcel)
    {
      PasspointConfiguration localPasspointConfiguration = new PasspointConfiguration();
      localPasspointConfiguration.setHomeSp((HomeSp)paramAnonymousParcel.readParcelable(null));
      localPasspointConfiguration.setCredential((Credential)paramAnonymousParcel.readParcelable(null));
      localPasspointConfiguration.setPolicy((Policy)paramAnonymousParcel.readParcelable(null));
      localPasspointConfiguration.setSubscriptionUpdate((UpdateParameter)paramAnonymousParcel.readParcelable(null));
      localPasspointConfiguration.setTrustRootCertList(readTrustRootCerts(paramAnonymousParcel));
      localPasspointConfiguration.setUpdateIdentifier(paramAnonymousParcel.readInt());
      localPasspointConfiguration.setCredentialPriority(paramAnonymousParcel.readInt());
      localPasspointConfiguration.setSubscriptionCreationTimeInMillis(paramAnonymousParcel.readLong());
      localPasspointConfiguration.setSubscriptionExpirationTimeInMillis(paramAnonymousParcel.readLong());
      localPasspointConfiguration.setSubscriptionType(paramAnonymousParcel.readString());
      localPasspointConfiguration.setUsageLimitUsageTimePeriodInMinutes(paramAnonymousParcel.readLong());
      localPasspointConfiguration.setUsageLimitStartTimeInMillis(paramAnonymousParcel.readLong());
      localPasspointConfiguration.setUsageLimitDataLimit(paramAnonymousParcel.readLong());
      localPasspointConfiguration.setUsageLimitTimeLimitInMinutes(paramAnonymousParcel.readLong());
      return localPasspointConfiguration;
    }
    
    public PasspointConfiguration[] newArray(int paramAnonymousInt)
    {
      return new PasspointConfiguration[paramAnonymousInt];
    }
  };
  private static final int MAX_URL_BYTES = 1023;
  private static final int NULL_VALUE = -1;
  private static final String TAG = "PasspointConfiguration";
  private Credential mCredential = null;
  private int mCredentialPriority = Integer.MIN_VALUE;
  private HomeSp mHomeSp = null;
  private Policy mPolicy = null;
  private long mSubscriptionCreationTimeInMillis = Long.MIN_VALUE;
  private long mSubscriptionExpirationTimeInMillis = Long.MIN_VALUE;
  private String mSubscriptionType = null;
  private UpdateParameter mSubscriptionUpdate = null;
  private Map<String, byte[]> mTrustRootCertList = null;
  private int mUpdateIdentifier = Integer.MIN_VALUE;
  private long mUsageLimitDataLimit = Long.MIN_VALUE;
  private long mUsageLimitStartTimeInMillis = Long.MIN_VALUE;
  private long mUsageLimitTimeLimitInMinutes = Long.MIN_VALUE;
  private long mUsageLimitUsageTimePeriodInMinutes = Long.MIN_VALUE;
  
  public PasspointConfiguration() {}
  
  public PasspointConfiguration(PasspointConfiguration paramPasspointConfiguration)
  {
    if (paramPasspointConfiguration == null) {
      return;
    }
    if (mHomeSp != null) {
      mHomeSp = new HomeSp(mHomeSp);
    }
    if (mCredential != null) {
      mCredential = new Credential(mCredential);
    }
    if (mPolicy != null) {
      mPolicy = new Policy(mPolicy);
    }
    if (mTrustRootCertList != null) {
      mTrustRootCertList = Collections.unmodifiableMap(mTrustRootCertList);
    }
    if (mSubscriptionUpdate != null) {
      mSubscriptionUpdate = new UpdateParameter(mSubscriptionUpdate);
    }
    mUpdateIdentifier = mUpdateIdentifier;
    mCredentialPriority = mCredentialPriority;
    mSubscriptionCreationTimeInMillis = mSubscriptionCreationTimeInMillis;
    mSubscriptionExpirationTimeInMillis = mSubscriptionExpirationTimeInMillis;
    mSubscriptionType = mSubscriptionType;
    mUsageLimitDataLimit = mUsageLimitDataLimit;
    mUsageLimitStartTimeInMillis = mUsageLimitStartTimeInMillis;
    mUsageLimitTimeLimitInMinutes = mUsageLimitTimeLimitInMinutes;
    mUsageLimitUsageTimePeriodInMinutes = mUsageLimitUsageTimePeriodInMinutes;
  }
  
  private static boolean isTrustRootCertListEquals(Map<String, byte[]> paramMap1, Map<String, byte[]> paramMap2)
  {
    boolean bool = true;
    if ((paramMap1 != null) && (paramMap2 != null))
    {
      if (paramMap1.size() != paramMap2.size()) {
        return false;
      }
      paramMap1 = paramMap1.entrySet().iterator();
      while (paramMap1.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)paramMap1.next();
        if (!Arrays.equals((byte[])localEntry.getValue(), (byte[])paramMap2.get(localEntry.getKey()))) {
          return false;
        }
      }
      return true;
    }
    if (paramMap1 != paramMap2) {
      bool = false;
    }
    return bool;
  }
  
  private static void writeTrustRootCerts(Parcel paramParcel, Map<String, byte[]> paramMap)
  {
    if (paramMap == null)
    {
      paramParcel.writeInt(-1);
      return;
    }
    paramParcel.writeInt(paramMap.size());
    Iterator localIterator = paramMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      paramMap = (Map.Entry)localIterator.next();
      paramParcel.writeString((String)paramMap.getKey());
      paramParcel.writeByteArray((byte[])paramMap.getValue());
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
    if (!(paramObject instanceof PasspointConfiguration)) {
      return false;
    }
    paramObject = (PasspointConfiguration)paramObject;
    if ((mHomeSp == null ? mHomeSp != null : !mHomeSp.equals(mHomeSp)) || (mCredential == null ? mCredential != null : !mCredential.equals(mCredential)) || (mPolicy == null ? mPolicy != null : !mPolicy.equals(mPolicy)) || (mSubscriptionUpdate == null ? mSubscriptionUpdate != null : !mSubscriptionUpdate.equals(mSubscriptionUpdate)) || (!isTrustRootCertListEquals(mTrustRootCertList, mTrustRootCertList)) || (mUpdateIdentifier != mUpdateIdentifier) || (mCredentialPriority != mCredentialPriority) || (mSubscriptionCreationTimeInMillis != mSubscriptionCreationTimeInMillis) || (mSubscriptionExpirationTimeInMillis != mSubscriptionExpirationTimeInMillis) || (!TextUtils.equals(mSubscriptionType, mSubscriptionType)) || (mUsageLimitUsageTimePeriodInMinutes != mUsageLimitUsageTimePeriodInMinutes) || (mUsageLimitStartTimeInMillis != mUsageLimitStartTimeInMillis) || (mUsageLimitDataLimit != mUsageLimitDataLimit) || (mUsageLimitTimeLimitInMinutes != mUsageLimitTimeLimitInMinutes)) {
      bool = false;
    }
    return bool;
  }
  
  public Credential getCredential()
  {
    return mCredential;
  }
  
  public int getCredentialPriority()
  {
    return mCredentialPriority;
  }
  
  public HomeSp getHomeSp()
  {
    return mHomeSp;
  }
  
  public Policy getPolicy()
  {
    return mPolicy;
  }
  
  public long getSubscriptionCreationTimeInMillis()
  {
    return mSubscriptionCreationTimeInMillis;
  }
  
  public long getSubscriptionExpirationTimeInMillis()
  {
    return mSubscriptionExpirationTimeInMillis;
  }
  
  public String getSubscriptionType()
  {
    return mSubscriptionType;
  }
  
  public UpdateParameter getSubscriptionUpdate()
  {
    return mSubscriptionUpdate;
  }
  
  public Map<String, byte[]> getTrustRootCertList()
  {
    return mTrustRootCertList;
  }
  
  public int getUpdateIdentifier()
  {
    return mUpdateIdentifier;
  }
  
  public long getUsageLimitDataLimit()
  {
    return mUsageLimitDataLimit;
  }
  
  public long getUsageLimitStartTimeInMillis()
  {
    return mUsageLimitStartTimeInMillis;
  }
  
  public long getUsageLimitTimeLimitInMinutes()
  {
    return mUsageLimitTimeLimitInMinutes;
  }
  
  public long getUsageLimitUsageTimePeriodInMinutes()
  {
    return mUsageLimitUsageTimePeriodInMinutes;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { mHomeSp, mCredential, mPolicy, mSubscriptionUpdate, mTrustRootCertList, Integer.valueOf(mUpdateIdentifier), Integer.valueOf(mCredentialPriority), Long.valueOf(mSubscriptionCreationTimeInMillis), Long.valueOf(mSubscriptionExpirationTimeInMillis), Long.valueOf(mUsageLimitUsageTimePeriodInMinutes), Long.valueOf(mUsageLimitStartTimeInMillis), Long.valueOf(mUsageLimitDataLimit), Long.valueOf(mUsageLimitTimeLimitInMinutes) });
  }
  
  public void setCredential(Credential paramCredential)
  {
    mCredential = paramCredential;
  }
  
  public void setCredentialPriority(int paramInt)
  {
    mCredentialPriority = paramInt;
  }
  
  public void setHomeSp(HomeSp paramHomeSp)
  {
    mHomeSp = paramHomeSp;
  }
  
  public void setPolicy(Policy paramPolicy)
  {
    mPolicy = paramPolicy;
  }
  
  public void setSubscriptionCreationTimeInMillis(long paramLong)
  {
    mSubscriptionCreationTimeInMillis = paramLong;
  }
  
  public void setSubscriptionExpirationTimeInMillis(long paramLong)
  {
    mSubscriptionExpirationTimeInMillis = paramLong;
  }
  
  public void setSubscriptionType(String paramString)
  {
    mSubscriptionType = paramString;
  }
  
  public void setSubscriptionUpdate(UpdateParameter paramUpdateParameter)
  {
    mSubscriptionUpdate = paramUpdateParameter;
  }
  
  public void setTrustRootCertList(Map<String, byte[]> paramMap)
  {
    mTrustRootCertList = paramMap;
  }
  
  public void setUpdateIdentifier(int paramInt)
  {
    mUpdateIdentifier = paramInt;
  }
  
  public void setUsageLimitDataLimit(long paramLong)
  {
    mUsageLimitDataLimit = paramLong;
  }
  
  public void setUsageLimitStartTimeInMillis(long paramLong)
  {
    mUsageLimitStartTimeInMillis = paramLong;
  }
  
  public void setUsageLimitTimeLimitInMinutes(long paramLong)
  {
    mUsageLimitTimeLimitInMinutes = paramLong;
  }
  
  public void setUsageLimitUsageTimePeriodInMinutes(long paramLong)
  {
    mUsageLimitUsageTimePeriodInMinutes = paramLong;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("UpdateIdentifier: ");
    localStringBuilder.append(mUpdateIdentifier);
    localStringBuilder.append("\n");
    localStringBuilder.append("CredentialPriority: ");
    localStringBuilder.append(mCredentialPriority);
    localStringBuilder.append("\n");
    localStringBuilder.append("SubscriptionCreationTime: ");
    Object localObject;
    if (mSubscriptionCreationTimeInMillis != Long.MIN_VALUE) {
      localObject = new Date(mSubscriptionCreationTimeInMillis);
    } else {
      localObject = "Not specified";
    }
    localStringBuilder.append(localObject);
    localStringBuilder.append("\n");
    localStringBuilder.append("SubscriptionExpirationTime: ");
    if (mSubscriptionExpirationTimeInMillis != Long.MIN_VALUE) {
      localObject = new Date(mSubscriptionExpirationTimeInMillis);
    } else {
      localObject = "Not specified";
    }
    localStringBuilder.append(localObject);
    localStringBuilder.append("\n");
    localStringBuilder.append("UsageLimitStartTime: ");
    if (mUsageLimitStartTimeInMillis != Long.MIN_VALUE) {
      localObject = new Date(mUsageLimitStartTimeInMillis);
    } else {
      localObject = "Not specified";
    }
    localStringBuilder.append(localObject);
    localStringBuilder.append("\n");
    localStringBuilder.append("UsageTimePeriod: ");
    localStringBuilder.append(mUsageLimitUsageTimePeriodInMinutes);
    localStringBuilder.append("\n");
    localStringBuilder.append("UsageLimitDataLimit: ");
    localStringBuilder.append(mUsageLimitDataLimit);
    localStringBuilder.append("\n");
    localStringBuilder.append("UsageLimitTimeLimit: ");
    localStringBuilder.append(mUsageLimitTimeLimitInMinutes);
    localStringBuilder.append("\n");
    if (mHomeSp != null)
    {
      localStringBuilder.append("HomeSP Begin ---\n");
      localStringBuilder.append(mHomeSp);
      localStringBuilder.append("HomeSP End ---\n");
    }
    if (mCredential != null)
    {
      localStringBuilder.append("Credential Begin ---\n");
      localStringBuilder.append(mCredential);
      localStringBuilder.append("Credential End ---\n");
    }
    if (mPolicy != null)
    {
      localStringBuilder.append("Policy Begin ---\n");
      localStringBuilder.append(mPolicy);
      localStringBuilder.append("Policy End ---\n");
    }
    if (mSubscriptionUpdate != null)
    {
      localStringBuilder.append("SubscriptionUpdate Begin ---\n");
      localStringBuilder.append(mSubscriptionUpdate);
      localStringBuilder.append("SubscriptionUpdate End ---\n");
    }
    if (mTrustRootCertList != null)
    {
      localStringBuilder.append("TrustRootCertServers: ");
      localStringBuilder.append(mTrustRootCertList.keySet());
      localStringBuilder.append("\n");
    }
    return localStringBuilder.toString();
  }
  
  public boolean validate()
  {
    if ((mHomeSp != null) && (mHomeSp.validate()))
    {
      if ((mCredential != null) && (mCredential.validate()))
      {
        if ((mPolicy != null) && (!mPolicy.validate())) {
          return false;
        }
        if ((mSubscriptionUpdate != null) && (!mSubscriptionUpdate.validate())) {
          return false;
        }
        if (mTrustRootCertList != null)
        {
          Object localObject1 = mTrustRootCertList.entrySet().iterator();
          while (((Iterator)localObject1).hasNext())
          {
            Object localObject2 = (Map.Entry)((Iterator)localObject1).next();
            Object localObject3 = (String)((Map.Entry)localObject2).getKey();
            localObject2 = (byte[])((Map.Entry)localObject2).getValue();
            if (TextUtils.isEmpty((CharSequence)localObject3))
            {
              Log.d("PasspointConfiguration", "Empty URL");
              return false;
            }
            if (((String)localObject3).getBytes(StandardCharsets.UTF_8).length > 1023)
            {
              localObject1 = new StringBuilder();
              ((StringBuilder)localObject1).append("URL bytes exceeded the max: ");
              ((StringBuilder)localObject1).append(((String)localObject3).getBytes(StandardCharsets.UTF_8).length);
              Log.d("PasspointConfiguration", ((StringBuilder)localObject1).toString());
              return false;
            }
            if (localObject2 == null)
            {
              Log.d("PasspointConfiguration", "Fingerprint not specified");
              return false;
            }
            if (localObject2.length != 32)
            {
              localObject3 = new StringBuilder();
              ((StringBuilder)localObject3).append("Incorrect size of trust root certificate SHA-256 fingerprint: ");
              ((StringBuilder)localObject3).append(localObject2.length);
              Log.d("PasspointConfiguration", ((StringBuilder)localObject3).toString());
              return false;
            }
          }
        }
        return true;
      }
      return false;
    }
    return false;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(mHomeSp, paramInt);
    paramParcel.writeParcelable(mCredential, paramInt);
    paramParcel.writeParcelable(mPolicy, paramInt);
    paramParcel.writeParcelable(mSubscriptionUpdate, paramInt);
    writeTrustRootCerts(paramParcel, mTrustRootCertList);
    paramParcel.writeInt(mUpdateIdentifier);
    paramParcel.writeInt(mCredentialPriority);
    paramParcel.writeLong(mSubscriptionCreationTimeInMillis);
    paramParcel.writeLong(mSubscriptionExpirationTimeInMillis);
    paramParcel.writeString(mSubscriptionType);
    paramParcel.writeLong(mUsageLimitUsageTimePeriodInMinutes);
    paramParcel.writeLong(mUsageLimitStartTimeInMillis);
    paramParcel.writeLong(mUsageLimitDataLimit);
    paramParcel.writeLong(mUsageLimitTimeLimitInMinutes);
  }
}
