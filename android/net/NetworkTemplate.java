package android.net;

import android.net.wifi.WifiInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.BackupUtils;
import android.util.BackupUtils.BadVersionException;
import android.util.Log;
import com.android.internal.util.ArrayUtils;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class NetworkTemplate
  implements Parcelable
{
  private static final int BACKUP_VERSION = 1;
  public static final Parcelable.Creator<NetworkTemplate> CREATOR = new Parcelable.Creator()
  {
    public NetworkTemplate createFromParcel(Parcel paramAnonymousParcel)
    {
      return new NetworkTemplate(paramAnonymousParcel, null);
    }
    
    public NetworkTemplate[] newArray(int paramAnonymousInt)
    {
      return new NetworkTemplate[paramAnonymousInt];
    }
  };
  public static final int MATCH_BLUETOOTH = 8;
  public static final int MATCH_ETHERNET = 5;
  public static final int MATCH_MOBILE = 1;
  public static final int MATCH_MOBILE_WILDCARD = 6;
  public static final int MATCH_PROXY = 9;
  public static final int MATCH_WIFI = 4;
  public static final int MATCH_WIFI_WILDCARD = 7;
  private static final String TAG = "NetworkTemplate";
  private static boolean sForceAllNetworkTypes = false;
  private final int mDefaultNetwork;
  private final int mMatchRule;
  private final String[] mMatchSubscriberIds;
  private final int mMetered;
  private final String mNetworkId;
  private final int mRoaming;
  private final String mSubscriberId;
  
  public NetworkTemplate(int paramInt, String paramString1, String paramString2)
  {
    this(paramInt, paramString1, new String[] { paramString1 }, paramString2);
  }
  
  public NetworkTemplate(int paramInt, String paramString1, String[] paramArrayOfString, String paramString2)
  {
    this(paramInt, paramString1, paramArrayOfString, paramString2, -1, -1, -1);
  }
  
  public NetworkTemplate(int paramInt1, String paramString1, String[] paramArrayOfString, String paramString2, int paramInt2, int paramInt3, int paramInt4)
  {
    mMatchRule = paramInt1;
    mSubscriberId = paramString1;
    mMatchSubscriberIds = paramArrayOfString;
    mNetworkId = paramString2;
    mMetered = paramInt2;
    mRoaming = paramInt3;
    mDefaultNetwork = paramInt4;
    if (!isKnownMatchRule(paramInt1))
    {
      paramString1 = new StringBuilder();
      paramString1.append("Unknown network template rule ");
      paramString1.append(paramInt1);
      paramString1.append(" will not match any identity.");
      Log.e("NetworkTemplate", paramString1.toString());
    }
  }
  
  private NetworkTemplate(Parcel paramParcel)
  {
    mMatchRule = paramParcel.readInt();
    mSubscriberId = paramParcel.readString();
    mMatchSubscriberIds = paramParcel.createStringArray();
    mNetworkId = paramParcel.readString();
    mMetered = paramParcel.readInt();
    mRoaming = paramParcel.readInt();
    mDefaultNetwork = paramParcel.readInt();
  }
  
  public static NetworkTemplate buildTemplateBluetooth()
  {
    return new NetworkTemplate(8, null, null);
  }
  
  public static NetworkTemplate buildTemplateEthernet()
  {
    return new NetworkTemplate(5, null, null);
  }
  
  public static NetworkTemplate buildTemplateMobileAll(String paramString)
  {
    return new NetworkTemplate(1, paramString, null);
  }
  
  public static NetworkTemplate buildTemplateMobileWildcard()
  {
    return new NetworkTemplate(6, null, null);
  }
  
  public static NetworkTemplate buildTemplateProxy()
  {
    return new NetworkTemplate(9, null, null);
  }
  
  @Deprecated
  public static NetworkTemplate buildTemplateWifi()
  {
    return buildTemplateWifiWildcard();
  }
  
  public static NetworkTemplate buildTemplateWifi(String paramString)
  {
    return new NetworkTemplate(4, null, paramString);
  }
  
  public static NetworkTemplate buildTemplateWifiWildcard()
  {
    return new NetworkTemplate(7, null, null);
  }
  
  public static void forceAllNetworkTypes()
  {
    sForceAllNetworkTypes = true;
  }
  
  private static String getMatchRuleName(int paramInt)
  {
    if (paramInt != 1)
    {
      switch (paramInt)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("UNKNOWN(");
        localStringBuilder.append(paramInt);
        localStringBuilder.append(")");
        return localStringBuilder.toString();
      case 9: 
        return "PROXY";
      case 8: 
        return "BLUETOOTH";
      case 7: 
        return "WIFI_WILDCARD";
      case 6: 
        return "MOBILE_WILDCARD";
      case 5: 
        return "ETHERNET";
      }
      return "WIFI";
    }
    return "MOBILE";
  }
  
  public static NetworkTemplate getNetworkTemplateFromBackup(DataInputStream paramDataInputStream)
    throws IOException, BackupUtils.BadVersionException
  {
    int i = paramDataInputStream.readInt();
    if ((i >= 1) && (i <= 1))
    {
      i = paramDataInputStream.readInt();
      String str = BackupUtils.readString(paramDataInputStream);
      paramDataInputStream = BackupUtils.readString(paramDataInputStream);
      if (isKnownMatchRule(i)) {
        return new NetworkTemplate(i, str, paramDataInputStream);
      }
      paramDataInputStream = new StringBuilder();
      paramDataInputStream.append("Restored network template contains unknown match rule ");
      paramDataInputStream.append(i);
      throw new BackupUtils.BadVersionException(paramDataInputStream.toString());
    }
    throw new BackupUtils.BadVersionException("Unknown Backup Serialization Version");
  }
  
  private static boolean isKnownMatchRule(int paramInt)
  {
    if (paramInt != 1) {
      switch (paramInt)
      {
      default: 
        return false;
      }
    }
    return true;
  }
  
  private boolean matchesBluetooth(NetworkIdentity paramNetworkIdentity)
  {
    return mType == 7;
  }
  
  private boolean matchesDefaultNetwork(NetworkIdentity paramNetworkIdentity)
  {
    int i = mDefaultNetwork;
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (i != -1) {
      if (mDefaultNetwork == 1)
      {
        bool2 = bool1;
        if (mDefaultNetwork) {}
      }
      else if ((mDefaultNetwork == 0) && (!mDefaultNetwork))
      {
        bool2 = bool1;
      }
      else
      {
        bool2 = false;
      }
    }
    return bool2;
  }
  
  private boolean matchesEthernet(NetworkIdentity paramNetworkIdentity)
  {
    return mType == 9;
  }
  
  private boolean matchesMetered(NetworkIdentity paramNetworkIdentity)
  {
    int i = mMetered;
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (i != -1) {
      if (mMetered == 1)
      {
        bool2 = bool1;
        if (mMetered) {}
      }
      else if ((mMetered == 0) && (!mMetered))
      {
        bool2 = bool1;
      }
      else
      {
        bool2 = false;
      }
    }
    return bool2;
  }
  
  private boolean matchesMobile(NetworkIdentity paramNetworkIdentity)
  {
    int i = mType;
    boolean bool = true;
    if (i == 6) {
      return true;
    }
    if (((!sForceAllNetworkTypes) && ((mType != 0) || (!mMetered))) || (ArrayUtils.isEmpty(mMatchSubscriberIds)) || (!ArrayUtils.contains(mMatchSubscriberIds, mSubscriberId))) {
      bool = false;
    }
    return bool;
  }
  
  private boolean matchesMobileWildcard(NetworkIdentity paramNetworkIdentity)
  {
    int i = mType;
    boolean bool1 = true;
    if (i == 6) {
      return true;
    }
    boolean bool2 = bool1;
    if (!sForceAllNetworkTypes) {
      if ((mType == 0) && (mMetered)) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
    }
    return bool2;
  }
  
  private boolean matchesProxy(NetworkIdentity paramNetworkIdentity)
  {
    boolean bool;
    if (mType == 16) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean matchesRoaming(NetworkIdentity paramNetworkIdentity)
  {
    int i = mRoaming;
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (i != -1) {
      if (mRoaming == 1)
      {
        bool2 = bool1;
        if (mRoaming) {}
      }
      else if ((mRoaming == 0) && (!mRoaming))
      {
        bool2 = bool1;
      }
      else
      {
        bool2 = false;
      }
    }
    return bool2;
  }
  
  private boolean matchesWifi(NetworkIdentity paramNetworkIdentity)
  {
    if (mType != 1) {
      return false;
    }
    return Objects.equals(WifiInfo.removeDoubleQuotes(mNetworkId), WifiInfo.removeDoubleQuotes(mNetworkId));
  }
  
  private boolean matchesWifiWildcard(NetworkIdentity paramNetworkIdentity)
  {
    int i = mType;
    return (i == 1) || (i == 13);
  }
  
  public static NetworkTemplate normalize(NetworkTemplate paramNetworkTemplate, String[] paramArrayOfString)
  {
    if ((paramNetworkTemplate.isMatchRuleMobile()) && (ArrayUtils.contains(paramArrayOfString, mSubscriberId))) {
      return new NetworkTemplate(mMatchRule, paramArrayOfString[0], paramArrayOfString, mNetworkId);
    }
    return paramNetworkTemplate;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof NetworkTemplate;
    boolean bool2 = false;
    if (bool1)
    {
      paramObject = (NetworkTemplate)paramObject;
      bool1 = bool2;
      if (mMatchRule == mMatchRule)
      {
        bool1 = bool2;
        if (Objects.equals(mSubscriberId, mSubscriberId))
        {
          bool1 = bool2;
          if (Objects.equals(mNetworkId, mNetworkId))
          {
            bool1 = bool2;
            if (mMetered == mMetered)
            {
              bool1 = bool2;
              if (mRoaming == mRoaming)
              {
                bool1 = bool2;
                if (mDefaultNetwork == mDefaultNetwork) {
                  bool1 = true;
                }
              }
            }
          }
        }
      }
      return bool1;
    }
    return false;
  }
  
  public byte[] getBytesForBackup()
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    DataOutputStream localDataOutputStream = new DataOutputStream(localByteArrayOutputStream);
    localDataOutputStream.writeInt(1);
    localDataOutputStream.writeInt(mMatchRule);
    BackupUtils.writeString(localDataOutputStream, mSubscriberId);
    BackupUtils.writeString(localDataOutputStream, mNetworkId);
    return localByteArrayOutputStream.toByteArray();
  }
  
  public int getMatchRule()
  {
    return mMatchRule;
  }
  
  public String getNetworkId()
  {
    return mNetworkId;
  }
  
  public String getSubscriberId()
  {
    return mSubscriberId;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(mMatchRule), mSubscriberId, mNetworkId, Integer.valueOf(mMetered), Integer.valueOf(mRoaming), Integer.valueOf(mDefaultNetwork) });
  }
  
  public boolean isMatchRuleMobile()
  {
    int i = mMatchRule;
    return (i == 1) || (i == 6);
  }
  
  public boolean isPersistable()
  {
    switch (mMatchRule)
    {
    default: 
      return true;
    }
    return false;
  }
  
  public boolean matches(NetworkIdentity paramNetworkIdentity)
  {
    if (!matchesMetered(paramNetworkIdentity)) {
      return false;
    }
    if (!matchesRoaming(paramNetworkIdentity)) {
      return false;
    }
    if (!matchesDefaultNetwork(paramNetworkIdentity)) {
      return false;
    }
    int i = mMatchRule;
    if (i != 1)
    {
      switch (i)
      {
      default: 
        return false;
      case 9: 
        return matchesProxy(paramNetworkIdentity);
      case 8: 
        return matchesBluetooth(paramNetworkIdentity);
      case 7: 
        return matchesWifiWildcard(paramNetworkIdentity);
      case 6: 
        return matchesMobileWildcard(paramNetworkIdentity);
      case 5: 
        return matchesEthernet(paramNetworkIdentity);
      }
      return matchesWifi(paramNetworkIdentity);
    }
    return matchesMobile(paramNetworkIdentity);
  }
  
  public boolean matchesSubscriberId(String paramString)
  {
    return ArrayUtils.contains(mMatchSubscriberIds, paramString);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("NetworkTemplate: ");
    localStringBuilder.append("matchRule=");
    localStringBuilder.append(getMatchRuleName(mMatchRule));
    if (mSubscriberId != null)
    {
      localStringBuilder.append(", subscriberId=");
      localStringBuilder.append(NetworkIdentity.scrubSubscriberId(mSubscriberId));
    }
    if (mMatchSubscriberIds != null)
    {
      localStringBuilder.append(", matchSubscriberIds=");
      localStringBuilder.append(Arrays.toString(NetworkIdentity.scrubSubscriberId(mMatchSubscriberIds)));
    }
    if (mNetworkId != null)
    {
      localStringBuilder.append(", networkId=");
      localStringBuilder.append(mNetworkId);
    }
    if (mMetered != -1)
    {
      localStringBuilder.append(", metered=");
      localStringBuilder.append(NetworkStats.meteredToString(mMetered));
    }
    if (mRoaming != -1)
    {
      localStringBuilder.append(", roaming=");
      localStringBuilder.append(NetworkStats.roamingToString(mRoaming));
    }
    if (mDefaultNetwork != -1)
    {
      localStringBuilder.append(", defaultNetwork=");
      localStringBuilder.append(NetworkStats.defaultNetworkToString(mDefaultNetwork));
    }
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mMatchRule);
    paramParcel.writeString(mSubscriberId);
    paramParcel.writeStringArray(mMatchSubscriberIds);
    paramParcel.writeString(mNetworkId);
    paramParcel.writeInt(mMetered);
    paramParcel.writeInt(mRoaming);
    paramParcel.writeInt(mDefaultNetwork);
  }
}
