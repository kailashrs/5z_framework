package android.net.wifi;

import android.annotation.SystemApi;
import android.net.IpConfiguration;
import android.net.IpConfiguration.IpAssignment;
import android.net.IpConfiguration.ProxySettings;
import android.net.MacAddress;
import android.net.ProxyInfo;
import android.net.StaticIpConfiguration;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.BackupUtils;
import android.util.BackupUtils.BadVersionException;
import android.util.Log;
import android.util.TimeUtils;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class WifiConfiguration
  implements Parcelable
{
  public static final int AP_BAND_2GHZ = 0;
  public static final int AP_BAND_5GHZ = 1;
  public static final int AP_BAND_ANY = -1;
  public static final int AP_BAND_DUAL = 2;
  private static final int BACKUP_VERSION = 2;
  public static final Parcelable.Creator<WifiConfiguration> CREATOR = new Parcelable.Creator()
  {
    public WifiConfiguration createFromParcel(Parcel paramAnonymousParcel)
    {
      WifiConfiguration localWifiConfiguration = new WifiConfiguration();
      networkId = paramAnonymousParcel.readInt();
      status = paramAnonymousParcel.readInt();
      mNetworkSelectionStatus.readFromParcel(paramAnonymousParcel);
      SSID = paramAnonymousParcel.readString();
      BSSID = paramAnonymousParcel.readString();
      int i = paramAnonymousParcel.readInt();
      boolean bool1 = false;
      if (i != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      shareThisAp = bool2;
      apBand = paramAnonymousParcel.readInt();
      apChannel = paramAnonymousParcel.readInt();
      FQDN = paramAnonymousParcel.readString();
      providerFriendlyName = paramAnonymousParcel.readString();
      if (paramAnonymousParcel.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      isHomeProviderNetwork = bool2;
      int j = paramAnonymousParcel.readInt();
      roamingConsortiumIds = new long[j];
      for (i = 0; i < j; i++) {
        roamingConsortiumIds[i] = paramAnonymousParcel.readLong();
      }
      preSharedKey = paramAnonymousParcel.readString();
      for (i = 0; i < wepKeys.length; i++) {
        wepKeys[i] = paramAnonymousParcel.readString();
      }
      wepTxKeyIndex = paramAnonymousParcel.readInt();
      priority = paramAnonymousParcel.readInt();
      if (paramAnonymousParcel.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      hiddenSSID = bool2;
      if (paramAnonymousParcel.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      requirePMF = bool2;
      updateIdentifier = paramAnonymousParcel.readString();
      allowedKeyManagement = WifiConfiguration.readBitSet(paramAnonymousParcel);
      allowedProtocols = WifiConfiguration.readBitSet(paramAnonymousParcel);
      allowedAuthAlgorithms = WifiConfiguration.readBitSet(paramAnonymousParcel);
      allowedPairwiseCiphers = WifiConfiguration.readBitSet(paramAnonymousParcel);
      allowedGroupCiphers = WifiConfiguration.readBitSet(paramAnonymousParcel);
      allowedGroupMgmtCiphers = WifiConfiguration.readBitSet(paramAnonymousParcel);
      allowedSuiteBCiphers = WifiConfiguration.readBitSet(paramAnonymousParcel);
      enterpriseConfig = ((WifiEnterpriseConfig)paramAnonymousParcel.readParcelable(null));
      localWifiConfiguration.setIpConfiguration((IpConfiguration)paramAnonymousParcel.readParcelable(null));
      dhcpServer = paramAnonymousParcel.readString();
      defaultGwMacAddress = paramAnonymousParcel.readString();
      if (paramAnonymousParcel.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      selfAdded = bool2;
      if (paramAnonymousParcel.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      didSelfAdd = bool2;
      if (paramAnonymousParcel.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      validatedInternetAccess = bool2;
      if (paramAnonymousParcel.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      isLegacyPasspointConfig = bool2;
      if (paramAnonymousParcel.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      ephemeral = bool2;
      if (paramAnonymousParcel.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      meteredHint = bool2;
      meteredOverride = paramAnonymousParcel.readInt();
      if (paramAnonymousParcel.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      useExternalScores = bool2;
      creatorUid = paramAnonymousParcel.readInt();
      lastConnectUid = paramAnonymousParcel.readInt();
      lastUpdateUid = paramAnonymousParcel.readInt();
      creatorName = paramAnonymousParcel.readString();
      lastUpdateName = paramAnonymousParcel.readString();
      numScorerOverride = paramAnonymousParcel.readInt();
      numScorerOverrideAndSwitchedNetwork = paramAnonymousParcel.readInt();
      numAssociation = paramAnonymousParcel.readInt();
      userApproved = paramAnonymousParcel.readInt();
      numNoInternetAccessReports = paramAnonymousParcel.readInt();
      if (paramAnonymousParcel.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      noInternetAccessExpected = bool2;
      boolean bool2 = bool1;
      if (paramAnonymousParcel.readInt() != 0) {
        bool2 = true;
      }
      shared = bool2;
      WifiConfiguration.access$202(localWifiConfiguration, paramAnonymousParcel.readString());
      recentFailure.setAssociationStatus(paramAnonymousParcel.readInt());
      WifiConfiguration.access$302(localWifiConfiguration, (MacAddress)paramAnonymousParcel.readParcelable(null));
      dppConnector = paramAnonymousParcel.readString();
      dppNetAccessKey = paramAnonymousParcel.readString();
      dppNetAccessKeyExpiry = paramAnonymousParcel.readInt();
      dppCsign = paramAnonymousParcel.readString();
      return localWifiConfiguration;
    }
    
    public WifiConfiguration[] newArray(int paramAnonymousInt)
    {
      return new WifiConfiguration[paramAnonymousInt];
    }
  };
  public static final int HOME_NETWORK_RSSI_BOOST = 5;
  public static final int INVALID_NETWORK_ID = -1;
  public static int INVALID_RSSI = 0;
  public static final int LOCAL_ONLY_NETWORK_ID = -2;
  private static final int MAXIMUM_RANDOM_MAC_GENERATION_RETRY = 3;
  public static final int METERED_OVERRIDE_METERED = 1;
  public static final int METERED_OVERRIDE_NONE = 0;
  public static final int METERED_OVERRIDE_NOT_METERED = 2;
  private static final String TAG = "WifiConfiguration";
  public static final int UNKNOWN_UID = -1;
  public static final int USER_APPROVED = 1;
  public static final int USER_BANNED = 2;
  public static final int USER_PENDING = 3;
  public static final int USER_UNSPECIFIED = 0;
  public static final String bssidVarName = "bssid";
  public static final String erpVarName = "erp";
  public static final String hiddenSSIDVarName = "scan_ssid";
  public static final String pmfVarName = "ieee80211w";
  public static final String priorityVarName = "priority";
  public static final String pskVarName = "psk";
  public static final String shareThisApVarName = "share_this_ap";
  public static final String ssidVarName = "ssid";
  public static final String updateIdentiferVarName = "update_identifier";
  @Deprecated
  public static final String[] wepKeyVarNames = { "wep_key0", "wep_key1", "wep_key2", "wep_key3" };
  @Deprecated
  public static final String wepTxKeyIdxVarName = "wep_tx_keyidx";
  public String BSSID;
  public String FQDN;
  public String SSID;
  public BitSet allowedAuthAlgorithms;
  public BitSet allowedGroupCiphers;
  public BitSet allowedGroupMgmtCiphers;
  public BitSet allowedKeyManagement;
  public BitSet allowedPairwiseCiphers;
  public BitSet allowedProtocols;
  public BitSet allowedSuiteBCiphers;
  public int apBand;
  public int apChannel;
  public String creationTime;
  @SystemApi
  public String creatorName;
  @SystemApi
  public int creatorUid;
  public String defaultGwMacAddress;
  public String dhcpServer;
  public boolean didSelfAdd;
  public String dppConnector;
  public String dppCsign;
  public String dppNetAccessKey;
  public int dppNetAccessKeyExpiry;
  public int dtimInterval;
  public WifiEnterpriseConfig enterpriseConfig;
  public boolean ephemeral;
  public boolean hiddenSSID;
  public boolean isHomeProviderNetwork;
  public boolean isLegacyPasspointConfig;
  public int lastConnectUid;
  public long lastConnected;
  public long lastDisconnected;
  @SystemApi
  public String lastUpdateName;
  @SystemApi
  public int lastUpdateUid;
  public HashMap<String, Integer> linkedConfigurations;
  String mCachedConfigKey;
  private IpConfiguration mIpConfiguration;
  private NetworkSelectionStatus mNetworkSelectionStatus;
  private String mPasspointManagementObjectTree;
  private MacAddress mRandomizedMacAddress;
  @SystemApi
  public boolean meteredHint;
  public int meteredOverride;
  public int networkId;
  public boolean noInternetAccessExpected;
  @SystemApi
  public int numAssociation;
  public int numNoInternetAccessReports;
  @SystemApi
  public int numScorerOverride;
  @SystemApi
  public int numScorerOverrideAndSwitchedNetwork;
  public String peerWifiConfiguration;
  public String preSharedKey;
  @Deprecated
  public int priority;
  public String providerFriendlyName;
  public final RecentFailure recentFailure;
  public boolean requirePMF;
  public long[] roamingConsortiumIds;
  public boolean selfAdded;
  public boolean shareThisAp;
  public boolean shared;
  public int status;
  public String updateIdentifier;
  public String updateTime;
  @SystemApi
  public boolean useExternalScores;
  public int userApproved;
  public boolean validatedInternetAccess;
  @Deprecated
  public String[] wepKeys;
  @Deprecated
  public int wepTxKeyIndex;
  
  static
  {
    INVALID_RSSI = -127;
  }
  
  public WifiConfiguration()
  {
    apBand = 0;
    apChannel = 0;
    dtimInterval = 0;
    isLegacyPasspointConfig = false;
    userApproved = 0;
    meteredOverride = 0;
    mNetworkSelectionStatus = new NetworkSelectionStatus();
    recentFailure = new RecentFailure();
    networkId = -1;
    SSID = null;
    BSSID = null;
    FQDN = null;
    roamingConsortiumIds = new long[0];
    priority = 0;
    hiddenSSID = false;
    shareThisAp = false;
    allowedKeyManagement = new BitSet();
    allowedProtocols = new BitSet();
    allowedAuthAlgorithms = new BitSet();
    allowedPairwiseCiphers = new BitSet();
    allowedGroupCiphers = new BitSet();
    allowedGroupMgmtCiphers = new BitSet();
    allowedSuiteBCiphers = new BitSet();
    wepKeys = new String[4];
    for (int i = 0; i < wepKeys.length; i++) {
      wepKeys[i] = null;
    }
    enterpriseConfig = new WifiEnterpriseConfig();
    selfAdded = false;
    didSelfAdd = false;
    ephemeral = false;
    meteredHint = false;
    meteredOverride = 0;
    useExternalScores = false;
    validatedInternetAccess = false;
    mIpConfiguration = new IpConfiguration();
    lastUpdateUid = -1;
    creatorUid = -1;
    shared = true;
    dtimInterval = 0;
    mRandomizedMacAddress = MacAddress.fromString("02:00:00:00:00:00");
    dppConnector = null;
    dppNetAccessKey = null;
    dppNetAccessKeyExpiry = -1;
    dppCsign = null;
  }
  
  public WifiConfiguration(WifiConfiguration paramWifiConfiguration)
  {
    int i = 0;
    apBand = 0;
    apChannel = 0;
    dtimInterval = 0;
    isLegacyPasspointConfig = false;
    userApproved = 0;
    meteredOverride = 0;
    mNetworkSelectionStatus = new NetworkSelectionStatus();
    recentFailure = new RecentFailure();
    if (paramWifiConfiguration != null)
    {
      networkId = networkId;
      status = status;
      SSID = SSID;
      BSSID = BSSID;
      FQDN = FQDN;
      shareThisAp = shareThisAp;
      roamingConsortiumIds = ((long[])roamingConsortiumIds.clone());
      providerFriendlyName = providerFriendlyName;
      isHomeProviderNetwork = isHomeProviderNetwork;
      preSharedKey = preSharedKey;
      mNetworkSelectionStatus.copy(paramWifiConfiguration.getNetworkSelectionStatus());
      apBand = apBand;
      apChannel = apChannel;
      wepKeys = new String[4];
      while (i < wepKeys.length)
      {
        wepKeys[i] = wepKeys[i];
        i++;
      }
      wepTxKeyIndex = wepTxKeyIndex;
      priority = priority;
      hiddenSSID = hiddenSSID;
      allowedKeyManagement = ((BitSet)allowedKeyManagement.clone());
      allowedProtocols = ((BitSet)allowedProtocols.clone());
      allowedAuthAlgorithms = ((BitSet)allowedAuthAlgorithms.clone());
      allowedPairwiseCiphers = ((BitSet)allowedPairwiseCiphers.clone());
      allowedGroupCiphers = ((BitSet)allowedGroupCiphers.clone());
      allowedGroupMgmtCiphers = ((BitSet)allowedGroupMgmtCiphers.clone());
      allowedSuiteBCiphers = ((BitSet)allowedSuiteBCiphers.clone());
      enterpriseConfig = new WifiEnterpriseConfig(enterpriseConfig);
      defaultGwMacAddress = defaultGwMacAddress;
      mIpConfiguration = new IpConfiguration(mIpConfiguration);
      if ((linkedConfigurations != null) && (linkedConfigurations.size() > 0))
      {
        linkedConfigurations = new HashMap();
        linkedConfigurations.putAll(linkedConfigurations);
      }
      mCachedConfigKey = null;
      selfAdded = selfAdded;
      validatedInternetAccess = validatedInternetAccess;
      isLegacyPasspointConfig = isLegacyPasspointConfig;
      ephemeral = ephemeral;
      meteredHint = meteredHint;
      meteredOverride = meteredOverride;
      useExternalScores = useExternalScores;
      didSelfAdd = didSelfAdd;
      lastConnectUid = lastConnectUid;
      lastUpdateUid = lastUpdateUid;
      creatorUid = creatorUid;
      creatorName = creatorName;
      lastUpdateName = lastUpdateName;
      peerWifiConfiguration = peerWifiConfiguration;
      lastConnected = lastConnected;
      lastDisconnected = lastDisconnected;
      numScorerOverride = numScorerOverride;
      numScorerOverrideAndSwitchedNetwork = numScorerOverrideAndSwitchedNetwork;
      numAssociation = numAssociation;
      userApproved = userApproved;
      numNoInternetAccessReports = numNoInternetAccessReports;
      noInternetAccessExpected = noInternetAccessExpected;
      creationTime = creationTime;
      updateTime = updateTime;
      shared = shared;
      recentFailure.setAssociationStatus(recentFailure.getAssociationStatus());
      mRandomizedMacAddress = mRandomizedMacAddress;
      dppConnector = dppConnector;
      dppNetAccessKey = dppNetAccessKey;
      dppNetAccessKeyExpiry = dppNetAccessKeyExpiry;
      dppCsign = dppCsign;
      requirePMF = requirePMF;
    }
  }
  
  public static WifiConfiguration getWifiConfigFromBackup(DataInputStream paramDataInputStream)
    throws IOException, BackupUtils.BadVersionException
  {
    WifiConfiguration localWifiConfiguration = new WifiConfiguration();
    int i = paramDataInputStream.readInt();
    if ((i >= 1) && (i <= 2))
    {
      if (i == 1) {
        return null;
      }
      SSID = BackupUtils.readString(paramDataInputStream);
      apBand = paramDataInputStream.readInt();
      apChannel = paramDataInputStream.readInt();
      preSharedKey = BackupUtils.readString(paramDataInputStream);
      allowedKeyManagement.set(paramDataInputStream.readInt());
      return localWifiConfiguration;
    }
    throw new BackupUtils.BadVersionException("Unknown Backup Serialization Version");
  }
  
  public static boolean isMetered(WifiConfiguration paramWifiConfiguration, WifiInfo paramWifiInfo)
  {
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (paramWifiInfo != null)
    {
      bool2 = bool1;
      if (paramWifiInfo.getMeteredHint()) {
        bool2 = true;
      }
    }
    bool1 = bool2;
    if (paramWifiConfiguration != null)
    {
      bool1 = bool2;
      if (meteredHint) {
        bool1 = true;
      }
    }
    bool2 = bool1;
    if (paramWifiConfiguration != null)
    {
      bool2 = bool1;
      if (meteredOverride == 1) {
        bool2 = true;
      }
    }
    bool1 = bool2;
    if (paramWifiConfiguration != null)
    {
      bool1 = bool2;
      if (meteredOverride == 2) {
        bool1 = false;
      }
    }
    return bool1;
  }
  
  public static boolean isValidMacAddressForRandomization(MacAddress paramMacAddress)
  {
    boolean bool;
    if ((paramMacAddress != null) && (!paramMacAddress.isMulticastAddress()) && (paramMacAddress.isLocallyAssigned()) && (!MacAddress.fromString("02:00:00:00:00:00").equals(paramMacAddress))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static BitSet readBitSet(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    BitSet localBitSet = new BitSet();
    for (int j = 0; j < i; j++) {
      localBitSet.set(paramParcel.readInt());
    }
    return localBitSet;
  }
  
  private String trimStringForKeyId(String paramString)
  {
    return paramString.replace("\"", "").replace(" ", "");
  }
  
  public static String userApprovedAsString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return "INVALID";
    case 2: 
      return "USER_BANNED";
    case 1: 
      return "USER_APPROVED";
    }
    return "USER_UNSPECIFIED";
  }
  
  private static void writeBitSet(Parcel paramParcel, BitSet paramBitSet)
  {
    int i = -1;
    paramParcel.writeInt(paramBitSet.cardinality());
    for (;;)
    {
      int j = paramBitSet.nextSetBit(i + 1);
      i = j;
      if (j == -1) {
        break;
      }
      paramParcel.writeInt(i);
    }
  }
  
  public String configKey()
  {
    return configKey(false);
  }
  
  public String configKey(boolean paramBoolean)
  {
    Object localObject1;
    if ((paramBoolean) && (mCachedConfigKey != null))
    {
      localObject1 = mCachedConfigKey;
    }
    else
    {
      Object localObject2;
      if (providerFriendlyName != null)
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append(FQDN);
        ((StringBuilder)localObject1).append(KeyMgmt.strings[2]);
        localObject2 = ((StringBuilder)localObject1).toString();
        localObject1 = localObject2;
        if (!shared)
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append((String)localObject2);
          ((StringBuilder)localObject1).append("-");
          ((StringBuilder)localObject1).append(Integer.toString(UserHandle.getUserId(creatorUid)));
          localObject1 = ((StringBuilder)localObject1).toString();
        }
      }
      else
      {
        if (allowedKeyManagement.get(1))
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append(SSID);
          ((StringBuilder)localObject1).append(KeyMgmt.strings[1]);
          localObject1 = ((StringBuilder)localObject1).toString();
        }
        for (;;)
        {
          break;
          if ((!allowedKeyManagement.get(2)) && (!allowedKeyManagement.get(3)))
          {
            if (wepKeys[0] != null)
            {
              localObject1 = new StringBuilder();
              ((StringBuilder)localObject1).append(SSID);
              ((StringBuilder)localObject1).append("WEP");
              localObject1 = ((StringBuilder)localObject1).toString();
            }
            else if (allowedKeyManagement.get(10))
            {
              localObject1 = new StringBuilder();
              ((StringBuilder)localObject1).append(SSID);
              ((StringBuilder)localObject1).append(KeyMgmt.strings[10]);
              localObject1 = ((StringBuilder)localObject1).toString();
            }
            else if (allowedKeyManagement.get(12))
            {
              localObject1 = new StringBuilder();
              ((StringBuilder)localObject1).append(SSID);
              ((StringBuilder)localObject1).append(KeyMgmt.strings[12]);
              localObject1 = ((StringBuilder)localObject1).toString();
            }
            else if (allowedKeyManagement.get(11))
            {
              localObject1 = new StringBuilder();
              ((StringBuilder)localObject1).append(SSID);
              ((StringBuilder)localObject1).append(KeyMgmt.strings[11]);
              localObject1 = ((StringBuilder)localObject1).toString();
            }
            else if (allowedKeyManagement.get(13))
            {
              localObject1 = new StringBuilder();
              ((StringBuilder)localObject1).append(SSID);
              ((StringBuilder)localObject1).append(KeyMgmt.strings[13]);
              localObject1 = ((StringBuilder)localObject1).toString();
            }
            else
            {
              localObject1 = new StringBuilder();
              ((StringBuilder)localObject1).append(SSID);
              ((StringBuilder)localObject1).append(KeyMgmt.strings[0]);
              localObject1 = ((StringBuilder)localObject1).toString();
              break;
            }
          }
          else
          {
            localObject1 = new StringBuilder();
            ((StringBuilder)localObject1).append(SSID);
            ((StringBuilder)localObject1).append(KeyMgmt.strings[2]);
            localObject1 = ((StringBuilder)localObject1).toString();
          }
        }
        localObject2 = localObject1;
        if (!shared)
        {
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append((String)localObject1);
          ((StringBuilder)localObject2).append("-");
          ((StringBuilder)localObject2).append(Integer.toString(UserHandle.getUserId(creatorUid)));
          localObject2 = ((StringBuilder)localObject2).toString();
        }
        mCachedConfigKey = ((String)localObject2);
        localObject1 = localObject2;
      }
    }
    return localObject1;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getAuthType()
  {
    if (allowedKeyManagement.cardinality() <= 1)
    {
      if (allowedKeyManagement.get(1)) {
        return 1;
      }
      if (allowedKeyManagement.get(4)) {
        return 4;
      }
      if (allowedKeyManagement.get(2)) {
        return 2;
      }
      if (allowedKeyManagement.get(3)) {
        return 3;
      }
      if (allowedKeyManagement.get(10)) {
        return 10;
      }
      if (allowedKeyManagement.get(11)) {
        return 11;
      }
      if (allowedKeyManagement.get(12)) {
        return 12;
      }
      if (allowedKeyManagement.get(13)) {
        return 13;
      }
      return 0;
    }
    throw new IllegalStateException("More than one auth type set");
  }
  
  public byte[] getBytesForBackup()
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    DataOutputStream localDataOutputStream = new DataOutputStream(localByteArrayOutputStream);
    localDataOutputStream.writeInt(2);
    BackupUtils.writeString(localDataOutputStream, SSID);
    localDataOutputStream.writeInt(apBand);
    localDataOutputStream.writeInt(apChannel);
    BackupUtils.writeString(localDataOutputStream, preSharedKey);
    localDataOutputStream.writeInt(getAuthType());
    return localByteArrayOutputStream.toByteArray();
  }
  
  public ProxyInfo getHttpProxy()
  {
    if (mIpConfiguration.proxySettings == IpConfiguration.ProxySettings.NONE) {
      return null;
    }
    return new ProxyInfo(mIpConfiguration.httpProxy);
  }
  
  public IpConfiguration.IpAssignment getIpAssignment()
  {
    return mIpConfiguration.ipAssignment;
  }
  
  public IpConfiguration getIpConfiguration()
  {
    return mIpConfiguration;
  }
  
  public String getKeyIdForCredentials(WifiConfiguration paramWifiConfiguration)
  {
    Object localObject1 = null;
    Object localObject2 = null;
    try
    {
      if (TextUtils.isEmpty(SSID)) {
        SSID = SSID;
      }
      if (allowedKeyManagement.cardinality() == 0) {
        allowedKeyManagement = allowedKeyManagement;
      }
      if (allowedKeyManagement.get(2)) {
        localObject2 = KeyMgmt.strings[2];
      }
      if (allowedKeyManagement.get(5)) {
        localObject2 = KeyMgmt.strings[5];
      }
      Object localObject3 = localObject2;
      if (allowedKeyManagement.get(3))
      {
        localObject3 = new java/lang/StringBuilder;
        ((StringBuilder)localObject3).<init>();
        ((StringBuilder)localObject3).append((String)localObject2);
        ((StringBuilder)localObject3).append(KeyMgmt.strings[3]);
        localObject3 = ((StringBuilder)localObject3).toString();
      }
      if (!TextUtils.isEmpty((CharSequence)localObject3))
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append(trimStringForKeyId(SSID));
        localStringBuilder.append("_");
        localStringBuilder.append((String)localObject3);
        localStringBuilder.append("_");
        localObject3 = enterpriseConfig;
        localObject2 = localObject1;
        if (paramWifiConfiguration != null) {
          localObject2 = enterpriseConfig;
        }
        localStringBuilder.append(trimStringForKeyId(((WifiEnterpriseConfig)localObject3).getKeyId((WifiEnterpriseConfig)localObject2)));
        return localStringBuilder.toString();
      }
      paramWifiConfiguration = new java/lang/IllegalStateException;
      paramWifiConfiguration.<init>("Not an EAP network");
      throw paramWifiConfiguration;
    }
    catch (NullPointerException paramWifiConfiguration)
    {
      throw new IllegalStateException("Invalid config details");
    }
  }
  
  public String getMoTree()
  {
    return mPasspointManagementObjectTree;
  }
  
  public NetworkSelectionStatus getNetworkSelectionStatus()
  {
    return mNetworkSelectionStatus;
  }
  
  public MacAddress getOrCreateRandomizedMacAddress()
  {
    for (int i = 0; (!isValidMacAddressForRandomization(mRandomizedMacAddress)) && (i < 3); i++) {
      mRandomizedMacAddress = MacAddress.createRandomUnicastAddress();
    }
    if (!isValidMacAddressForRandomization(mRandomizedMacAddress)) {
      mRandomizedMacAddress = MacAddress.fromString("02:00:00:00:00:00");
    }
    return mRandomizedMacAddress;
  }
  
  public String getPrintableSsid()
  {
    if (SSID == null) {
      return "";
    }
    int i = SSID.length();
    if ((i > 2) && (SSID.charAt(0) == '"') && (SSID.charAt(i - 1) == '"')) {
      return SSID.substring(1, i - 1);
    }
    if ((i > 3) && (SSID.charAt(0) == 'P') && (SSID.charAt(1) == '"') && (SSID.charAt(i - 1) == '"')) {
      return WifiSsid.createFromAsciiEncoded(SSID.substring(2, i - 1)).toString();
    }
    return SSID;
  }
  
  public IpConfiguration.ProxySettings getProxySettings()
  {
    return mIpConfiguration.proxySettings;
  }
  
  public MacAddress getRandomizedMacAddress()
  {
    return mRandomizedMacAddress;
  }
  
  public StaticIpConfiguration getStaticIpConfiguration()
  {
    return mIpConfiguration.getStaticIpConfiguration();
  }
  
  @SystemApi
  public boolean hasNoInternetAccess()
  {
    boolean bool;
    if ((numNoInternetAccessReports > 0) && (!validatedInternetAccess)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isEnterprise()
  {
    boolean bool;
    if (((allowedKeyManagement.get(2)) || (allowedKeyManagement.get(3))) && (enterpriseConfig != null) && (enterpriseConfig.getEapMethod() != -1)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @SystemApi
  public boolean isEphemeral()
  {
    return ephemeral;
  }
  
  public boolean isLinked(WifiConfiguration paramWifiConfiguration)
  {
    return (paramWifiConfiguration != null) && (linkedConfigurations != null) && (linkedConfigurations != null) && (linkedConfigurations.get(configKey()) != null) && (linkedConfigurations.get(paramWifiConfiguration.configKey()) != null);
  }
  
  @SystemApi
  public boolean isNoInternetAccessExpected()
  {
    return noInternetAccessExpected;
  }
  
  public boolean isOpenNetwork()
  {
    int i = allowedKeyManagement.cardinality();
    boolean bool1 = false;
    if ((i != 0) && ((i != 1) || (!allowedKeyManagement.get(0)))) {
      i = 0;
    } else {
      i = 1;
    }
    int j = 1;
    int k = j;
    if (wepKeys != null) {
      for (int m = 0;; m++)
      {
        k = j;
        if (m >= wepKeys.length) {
          break;
        }
        if (wepKeys[m] != null)
        {
          k = 0;
          break;
        }
      }
    }
    boolean bool2 = bool1;
    if (i != 0)
    {
      bool2 = bool1;
      if (k != 0) {
        bool2 = true;
      }
    }
    return bool2;
  }
  
  public boolean isPasspoint()
  {
    boolean bool;
    if ((!TextUtils.isEmpty(FQDN)) && (!TextUtils.isEmpty(providerFriendlyName)) && (enterpriseConfig != null) && (enterpriseConfig.getEapMethod() != -1)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void setHttpProxy(ProxyInfo paramProxyInfo)
  {
    if (paramProxyInfo == null)
    {
      mIpConfiguration.setProxySettings(IpConfiguration.ProxySettings.NONE);
      mIpConfiguration.setHttpProxy(null);
      return;
    }
    if (!Uri.EMPTY.equals(paramProxyInfo.getPacFileUrl()))
    {
      localObject = IpConfiguration.ProxySettings.PAC;
      paramProxyInfo = new ProxyInfo(paramProxyInfo.getPacFileUrl(), paramProxyInfo.getPort());
    }
    else
    {
      localObject = IpConfiguration.ProxySettings.STATIC;
      paramProxyInfo = new ProxyInfo(paramProxyInfo.getHost(), paramProxyInfo.getPort(), paramProxyInfo.getExclusionListAsString());
    }
    if (paramProxyInfo.isValid())
    {
      mIpConfiguration.setProxySettings((IpConfiguration.ProxySettings)localObject);
      mIpConfiguration.setHttpProxy(paramProxyInfo);
      return;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Invalid ProxyInfo: ");
    ((StringBuilder)localObject).append(paramProxyInfo.toString());
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
  
  public void setIpAssignment(IpConfiguration.IpAssignment paramIpAssignment)
  {
    mIpConfiguration.ipAssignment = paramIpAssignment;
  }
  
  public void setIpConfiguration(IpConfiguration paramIpConfiguration)
  {
    IpConfiguration localIpConfiguration = paramIpConfiguration;
    if (paramIpConfiguration == null) {
      localIpConfiguration = new IpConfiguration();
    }
    mIpConfiguration = localIpConfiguration;
  }
  
  public void setNetworkSelectionStatus(NetworkSelectionStatus paramNetworkSelectionStatus)
  {
    mNetworkSelectionStatus = paramNetworkSelectionStatus;
  }
  
  public void setPasspointManagementObjectTree(String paramString)
  {
    mPasspointManagementObjectTree = paramString;
  }
  
  public void setProxy(IpConfiguration.ProxySettings paramProxySettings, ProxyInfo paramProxyInfo)
  {
    mIpConfiguration.proxySettings = paramProxySettings;
    mIpConfiguration.httpProxy = paramProxyInfo;
  }
  
  public void setProxySettings(IpConfiguration.ProxySettings paramProxySettings)
  {
    mIpConfiguration.proxySettings = paramProxySettings;
  }
  
  public void setRandomizedMacAddress(MacAddress paramMacAddress)
  {
    if (paramMacAddress == null)
    {
      Log.e("WifiConfiguration", "setRandomizedMacAddress received null MacAddress.");
      return;
    }
    mRandomizedMacAddress = paramMacAddress;
  }
  
  public void setStaticIpConfiguration(StaticIpConfiguration paramStaticIpConfiguration)
  {
    mIpConfiguration.setStaticIpConfiguration(paramStaticIpConfiguration);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (status == 0) {
      localStringBuilder.append("* ");
    } else if (status == 1) {
      localStringBuilder.append("- DSBLE ");
    }
    localStringBuilder.append("ID: ");
    localStringBuilder.append(networkId);
    localStringBuilder.append(" SSID: ");
    localStringBuilder.append(SSID);
    localStringBuilder.append(" PROVIDER-NAME: ");
    localStringBuilder.append(providerFriendlyName);
    localStringBuilder.append(" BSSID: ");
    localStringBuilder.append(BSSID);
    localStringBuilder.append(" FQDN: ");
    localStringBuilder.append(FQDN);
    localStringBuilder.append(" PRIO: ");
    localStringBuilder.append(priority);
    localStringBuilder.append(" HIDDEN: ");
    localStringBuilder.append(hiddenSSID);
    localStringBuilder.append('\n');
    localStringBuilder.append(" NetworkSelectionStatus ");
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(mNetworkSelectionStatus.getNetworkStatusString());
    ((StringBuilder)localObject).append("\n");
    localStringBuilder.append(((StringBuilder)localObject).toString());
    int i = mNetworkSelectionStatus.getNetworkSelectionDisableReason();
    int j = 0;
    if (i > 0)
    {
      localStringBuilder.append(" mNetworkSelectionDisableReason ");
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(mNetworkSelectionStatus.getNetworkDisableReasonString());
      ((StringBuilder)localObject).append("\n");
      localStringBuilder.append(((StringBuilder)localObject).toString());
      localObject = mNetworkSelectionStatus;
      for (i = 0;; i++)
      {
        localObject = mNetworkSelectionStatus;
        if (i >= 14) {
          break;
        }
        if (mNetworkSelectionStatus.getDisableReasonCounter(i) != 0)
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append(NetworkSelectionStatus.getNetworkDisableReasonString(i));
          ((StringBuilder)localObject).append(" counter:");
          ((StringBuilder)localObject).append(mNetworkSelectionStatus.getDisableReasonCounter(i));
          ((StringBuilder)localObject).append("\n");
          localStringBuilder.append(((StringBuilder)localObject).toString());
        }
      }
    }
    if (mNetworkSelectionStatus.getConnectChoice() != null)
    {
      localStringBuilder.append(" connect choice: ");
      localStringBuilder.append(mNetworkSelectionStatus.getConnectChoice());
      localStringBuilder.append(" connect choice set time: ");
      localStringBuilder.append(TimeUtils.logTimeOfDay(mNetworkSelectionStatus.getConnectChoiceTimestamp()));
    }
    localStringBuilder.append(" hasEverConnected: ");
    localStringBuilder.append(mNetworkSelectionStatus.getHasEverConnected());
    localStringBuilder.append("\n");
    if (numAssociation > 0)
    {
      localStringBuilder.append(" numAssociation ");
      localStringBuilder.append(numAssociation);
      localStringBuilder.append("\n");
    }
    if (numNoInternetAccessReports > 0)
    {
      localStringBuilder.append(" numNoInternetAccessReports ");
      localStringBuilder.append(numNoInternetAccessReports);
      localStringBuilder.append("\n");
    }
    if (updateTime != null)
    {
      localStringBuilder.append(" update ");
      localStringBuilder.append(updateTime);
      localStringBuilder.append("\n");
    }
    if (creationTime != null)
    {
      localStringBuilder.append(" creation ");
      localStringBuilder.append(creationTime);
      localStringBuilder.append("\n");
    }
    if (didSelfAdd) {
      localStringBuilder.append(" didSelfAdd");
    }
    if (selfAdded) {
      localStringBuilder.append(" selfAdded");
    }
    if (validatedInternetAccess) {
      localStringBuilder.append(" validatedInternetAccess");
    }
    if (ephemeral) {
      localStringBuilder.append(" ephemeral");
    }
    if (meteredHint) {
      localStringBuilder.append(" meteredHint");
    }
    if (useExternalScores) {
      localStringBuilder.append(" useExternalScores");
    }
    if ((didSelfAdd) || (selfAdded) || (validatedInternetAccess) || (ephemeral) || (meteredHint) || (useExternalScores)) {
      localStringBuilder.append("\n");
    }
    if (meteredOverride != 0)
    {
      localStringBuilder.append(" meteredOverride ");
      localStringBuilder.append(meteredOverride);
      localStringBuilder.append("\n");
    }
    localStringBuilder.append(" KeyMgmt:");
    for (i = 0; i < allowedKeyManagement.size(); i++) {
      if (allowedKeyManagement.get(i))
      {
        localStringBuilder.append(" ");
        if (i < KeyMgmt.strings.length) {
          localStringBuilder.append(KeyMgmt.strings[i]);
        } else {
          localStringBuilder.append("??");
        }
      }
    }
    localStringBuilder.append(" Protocols:");
    for (i = 0; i < allowedProtocols.size(); i++) {
      if (allowedProtocols.get(i))
      {
        localStringBuilder.append(" ");
        if (i < Protocol.strings.length) {
          localStringBuilder.append(Protocol.strings[i]);
        } else {
          localStringBuilder.append("??");
        }
      }
    }
    localStringBuilder.append('\n');
    localStringBuilder.append(" AuthAlgorithms:");
    for (i = 0; i < allowedAuthAlgorithms.size(); i++) {
      if (allowedAuthAlgorithms.get(i))
      {
        localStringBuilder.append(" ");
        if (i < AuthAlgorithm.strings.length) {
          localStringBuilder.append(AuthAlgorithm.strings[i]);
        } else {
          localStringBuilder.append("??");
        }
      }
    }
    localStringBuilder.append('\n');
    localStringBuilder.append(" PairwiseCiphers:");
    for (i = 0; i < allowedPairwiseCiphers.size(); i++) {
      if (allowedPairwiseCiphers.get(i))
      {
        localStringBuilder.append(" ");
        if (i < PairwiseCipher.strings.length) {
          localStringBuilder.append(PairwiseCipher.strings[i]);
        } else {
          localStringBuilder.append("??");
        }
      }
    }
    localStringBuilder.append('\n');
    localStringBuilder.append(" GroupCiphers:");
    for (i = 0; i < allowedGroupCiphers.size(); i++) {
      if (allowedGroupCiphers.get(i))
      {
        localStringBuilder.append(" ");
        if (i < GroupCipher.strings.length) {
          localStringBuilder.append(GroupCipher.strings[i]);
        } else {
          localStringBuilder.append("??");
        }
      }
    }
    localStringBuilder.append('\n');
    localStringBuilder.append(" GroupMgmtCiphers:");
    for (i = 0; i < allowedGroupMgmtCiphers.size(); i++) {
      if (allowedGroupMgmtCiphers.get(i))
      {
        localStringBuilder.append(" ");
        if (i < GroupMgmtCipher.strings.length) {
          localStringBuilder.append(GroupMgmtCipher.strings[i]);
        } else {
          localStringBuilder.append("??");
        }
      }
    }
    localStringBuilder.append('\n');
    localStringBuilder.append(" SuiteBCiphers:");
    for (i = j; i < allowedSuiteBCiphers.size(); i++) {
      if (allowedSuiteBCiphers.get(i))
      {
        localStringBuilder.append(" ");
        if (i < SuiteBCipher.strings.length) {
          localStringBuilder.append(SuiteBCipher.strings[i]);
        } else {
          localStringBuilder.append("??");
        }
      }
    }
    localStringBuilder.append('\n');
    localStringBuilder.append(" PSK: ");
    if (preSharedKey != null) {
      localStringBuilder.append('*');
    }
    localStringBuilder.append("\nEnterprise config:\n");
    localStringBuilder.append(enterpriseConfig);
    localStringBuilder.append("\nDPP config:\n");
    if (dppConnector != null) {
      localStringBuilder.append(" Dpp Connector: *\n");
    }
    if (dppNetAccessKey != null) {
      localStringBuilder.append(" Dpp NetAccessKey: *\n");
    }
    if (dppCsign != null) {
      localStringBuilder.append(" Dpp Csign: *\n");
    }
    localStringBuilder.append("IP config:\n");
    localStringBuilder.append(mIpConfiguration.toString());
    if (mNetworkSelectionStatus.getNetworkSelectionBSSID() != null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(" networkSelectionBSSID=");
      ((StringBuilder)localObject).append(mNetworkSelectionStatus.getNetworkSelectionBSSID());
      localStringBuilder.append(((StringBuilder)localObject).toString());
    }
    long l = SystemClock.elapsedRealtime();
    if (mNetworkSelectionStatus.getDisableTime() != -1L)
    {
      localStringBuilder.append('\n');
      l -= mNetworkSelectionStatus.getDisableTime();
      if (l <= 0L)
      {
        localStringBuilder.append(" blackListed since <incorrect>");
      }
      else
      {
        localStringBuilder.append(" blackListed: ");
        localStringBuilder.append(Long.toString(l / 1000L));
        localStringBuilder.append("sec ");
      }
    }
    if (creatorUid != 0)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(" cuid=");
      ((StringBuilder)localObject).append(creatorUid);
      localStringBuilder.append(((StringBuilder)localObject).toString());
    }
    if (creatorName != null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(" cname=");
      ((StringBuilder)localObject).append(creatorName);
      localStringBuilder.append(((StringBuilder)localObject).toString());
    }
    if (lastUpdateUid != 0)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(" luid=");
      ((StringBuilder)localObject).append(lastUpdateUid);
      localStringBuilder.append(((StringBuilder)localObject).toString());
    }
    if (lastUpdateName != null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(" lname=");
      ((StringBuilder)localObject).append(lastUpdateName);
      localStringBuilder.append(((StringBuilder)localObject).toString());
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" lcuid=");
    ((StringBuilder)localObject).append(lastConnectUid);
    localStringBuilder.append(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" userApproved=");
    ((StringBuilder)localObject).append(userApprovedAsString(userApproved));
    localStringBuilder.append(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" noInternetAccessExpected=");
    ((StringBuilder)localObject).append(noInternetAccessExpected);
    localStringBuilder.append(((StringBuilder)localObject).toString());
    localStringBuilder.append(" ");
    if (lastConnected != 0L)
    {
      localStringBuilder.append('\n');
      localStringBuilder.append("lastConnected: ");
      localStringBuilder.append(TimeUtils.logTimeOfDay(lastConnected));
      localStringBuilder.append(" ");
    }
    localStringBuilder.append('\n');
    if (linkedConfigurations != null)
    {
      localObject = linkedConfigurations.keySet().iterator();
      while (((Iterator)localObject).hasNext())
      {
        String str = (String)((Iterator)localObject).next();
        localStringBuilder.append(" linked: ");
        localStringBuilder.append(str);
        localStringBuilder.append('\n');
      }
    }
    localStringBuilder.append("recentFailure: ");
    localStringBuilder.append("Association Rejection code: ");
    localStringBuilder.append(recentFailure.getAssociationStatus());
    localStringBuilder.append("\n");
    localStringBuilder.append("ShareThisAp: ");
    localStringBuilder.append(shareThisAp);
    localStringBuilder.append('\n');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(networkId);
    paramParcel.writeInt(status);
    mNetworkSelectionStatus.writeToParcel(paramParcel);
    paramParcel.writeString(SSID);
    paramParcel.writeString(BSSID);
    paramParcel.writeInt(shareThisAp);
    paramParcel.writeInt(apBand);
    paramParcel.writeInt(apChannel);
    paramParcel.writeString(FQDN);
    paramParcel.writeString(providerFriendlyName);
    paramParcel.writeInt(isHomeProviderNetwork);
    paramParcel.writeInt(roamingConsortiumIds.length);
    Object localObject = roamingConsortiumIds;
    int i = localObject.length;
    int j = 0;
    for (int k = 0; k < i; k++) {
      paramParcel.writeLong(localObject[k]);
    }
    paramParcel.writeString(preSharedKey);
    localObject = wepKeys;
    i = localObject.length;
    for (k = j; k < i; k++) {
      paramParcel.writeString(localObject[k]);
    }
    paramParcel.writeInt(wepTxKeyIndex);
    paramParcel.writeInt(priority);
    paramParcel.writeInt(hiddenSSID);
    paramParcel.writeInt(requirePMF);
    paramParcel.writeString(updateIdentifier);
    writeBitSet(paramParcel, allowedKeyManagement);
    writeBitSet(paramParcel, allowedProtocols);
    writeBitSet(paramParcel, allowedAuthAlgorithms);
    writeBitSet(paramParcel, allowedPairwiseCiphers);
    writeBitSet(paramParcel, allowedGroupCiphers);
    writeBitSet(paramParcel, allowedGroupMgmtCiphers);
    writeBitSet(paramParcel, allowedSuiteBCiphers);
    paramParcel.writeParcelable(enterpriseConfig, paramInt);
    paramParcel.writeParcelable(mIpConfiguration, paramInt);
    paramParcel.writeString(dhcpServer);
    paramParcel.writeString(defaultGwMacAddress);
    paramParcel.writeInt(selfAdded);
    paramParcel.writeInt(didSelfAdd);
    paramParcel.writeInt(validatedInternetAccess);
    paramParcel.writeInt(isLegacyPasspointConfig);
    paramParcel.writeInt(ephemeral);
    paramParcel.writeInt(meteredHint);
    paramParcel.writeInt(meteredOverride);
    paramParcel.writeInt(useExternalScores);
    paramParcel.writeInt(creatorUid);
    paramParcel.writeInt(lastConnectUid);
    paramParcel.writeInt(lastUpdateUid);
    paramParcel.writeString(creatorName);
    paramParcel.writeString(lastUpdateName);
    paramParcel.writeInt(numScorerOverride);
    paramParcel.writeInt(numScorerOverrideAndSwitchedNetwork);
    paramParcel.writeInt(numAssociation);
    paramParcel.writeInt(userApproved);
    paramParcel.writeInt(numNoInternetAccessReports);
    paramParcel.writeInt(noInternetAccessExpected);
    paramParcel.writeInt(shared);
    paramParcel.writeString(mPasspointManagementObjectTree);
    paramParcel.writeInt(recentFailure.getAssociationStatus());
    paramParcel.writeParcelable(mRandomizedMacAddress, paramInt);
    paramParcel.writeString(dppConnector);
    paramParcel.writeString(dppNetAccessKey);
    paramParcel.writeInt(dppNetAccessKeyExpiry);
    paramParcel.writeString(dppCsign);
  }
  
  public static class AuthAlgorithm
  {
    public static final int LEAP = 2;
    public static final int OPEN = 0;
    @Deprecated
    public static final int SHARED = 1;
    public static final String[] strings = { "OPEN", "SHARED", "LEAP" };
    public static final String varName = "auth_alg";
    
    private AuthAlgorithm() {}
  }
  
  public static class GroupCipher
  {
    public static final int CCMP = 3;
    public static final int GCMP = 5;
    public static final int GTK_NOT_USED = 4;
    public static final int TKIP = 2;
    @Deprecated
    public static final int WEP104 = 1;
    @Deprecated
    public static final int WEP40 = 0;
    public static final String[] strings = { "WEP40", "WEP104", "TKIP", "CCMP", "GTK_NOT_USED", "GCMP" };
    public static final String varName = "group";
    
    private GroupCipher() {}
  }
  
  public static class GroupMgmtCipher
  {
    public static final int CMAC = 0;
    public static final int GMAC = 1;
    public static final String[] strings = { "CMAC", "GMAC" };
    public static final String varName = "groupMgmt";
    
    private GroupMgmtCipher() {}
  }
  
  public static class KeyMgmt
  {
    public static final int DPP = 10;
    public static final int FILS_SHA256 = 8;
    public static final int FILS_SHA384 = 9;
    public static final int FT_EAP = 7;
    public static final int FT_PSK = 6;
    public static final int IEEE8021X = 3;
    public static final int NONE = 0;
    public static final int OSEN = 5;
    public static final int OWE = 12;
    public static final int SAE = 11;
    public static final int SUITE_B_192 = 13;
    @SystemApi
    public static final int WPA2_PSK = 4;
    public static final int WPA_EAP = 2;
    public static final int WPA_PSK = 1;
    public static final String[] strings = { "NONE", "WPA_PSK", "WPA_EAP", "IEEE8021X", "WPA2_PSK", "OSEN", "FT_PSK", "FT_EAP", "FILS_SHA256", "FILS_SHA384", "DPP", "SAE", "OWE", "SUITE_B_192" };
    public static final String varName = "key_mgmt";
    
    private KeyMgmt() {}
  }
  
  public static class NetworkSelectionStatus
  {
    private static final int CONNECT_CHOICE_EXISTS = 1;
    private static final int CONNECT_CHOICE_NOT_EXISTS = -1;
    public static final int DISABLED_ASSOCIATION_REJECTION = 2;
    public static final int DISABLED_AUTHENTICATION_FAILURE = 3;
    public static final int DISABLED_AUTHENTICATION_NO_CREDENTIALS = 9;
    public static final int DISABLED_BAD_LINK = 1;
    public static final int DISABLED_BY_WIFI_MANAGER = 11;
    public static final int DISABLED_BY_WRONG_PASSWORD = 13;
    public static final int DISABLED_DHCP_FAILURE = 4;
    public static final int DISABLED_DNS_FAILURE = 5;
    public static final int DISABLED_DUE_TO_USER_SWITCH = 12;
    public static final int DISABLED_NO_INTERNET_PERMANENT = 10;
    public static final int DISABLED_NO_INTERNET_TEMPORARY = 6;
    public static final int DISABLED_TLS_VERSION_MISMATCH = 8;
    public static final int DISABLED_WPS_START = 7;
    public static final long INVALID_NETWORK_SELECTION_DISABLE_TIMESTAMP = -1L;
    public static final int NETWORK_SELECTION_DISABLED_MAX = 14;
    public static final int NETWORK_SELECTION_DISABLED_STARTING_INDEX = 1;
    public static final int NETWORK_SELECTION_ENABLE = 0;
    public static final int NETWORK_SELECTION_ENABLED = 0;
    public static final int NETWORK_SELECTION_PERMANENTLY_DISABLED = 2;
    public static final int NETWORK_SELECTION_STATUS_MAX = 3;
    public static final int NETWORK_SELECTION_TEMPORARY_DISABLED = 1;
    public static final String[] QUALITY_NETWORK_SELECTION_DISABLE_REASON = { "NETWORK_SELECTION_ENABLE", "NETWORK_SELECTION_DISABLED_BAD_LINK", "NETWORK_SELECTION_DISABLED_ASSOCIATION_REJECTION ", "NETWORK_SELECTION_DISABLED_AUTHENTICATION_FAILURE", "NETWORK_SELECTION_DISABLED_DHCP_FAILURE", "NETWORK_SELECTION_DISABLED_DNS_FAILURE", "NETWORK_SELECTION_DISABLED_NO_INTERNET_TEMPORARY", "NETWORK_SELECTION_DISABLED_WPS_START", "NETWORK_SELECTION_DISABLED_TLS_VERSION", "NETWORK_SELECTION_DISABLED_AUTHENTICATION_NO_CREDENTIALS", "NETWORK_SELECTION_DISABLED_NO_INTERNET_PERMANENT", "NETWORK_SELECTION_DISABLED_BY_WIFI_MANAGER", "NETWORK_SELECTION_DISABLED_BY_USER_SWITCH", "NETWORK_SELECTION_DISABLED_BY_WRONG_PASSWORD" };
    public static final String[] QUALITY_NETWORK_SELECTION_STATUS = { "NETWORK_SELECTION_ENABLED", "NETWORK_SELECTION_TEMPORARY_DISABLED", "NETWORK_SELECTION_PERMANENTLY_DISABLED" };
    private ScanResult mCandidate;
    private int mCandidateScore;
    private String mConnectChoice;
    private long mConnectChoiceTimestamp = -1L;
    private boolean mHasEverConnected = false;
    private int[] mNetworkSeclectionDisableCounter = new int[14];
    private String mNetworkSelectionBSSID;
    private int mNetworkSelectionDisableReason;
    private boolean mNotRecommended;
    private boolean mSeenInLastQualifiedNetworkSelection;
    private int mStatus;
    private long mTemporarilyDisabledTimestamp = -1L;
    
    public NetworkSelectionStatus() {}
    
    public static String getNetworkDisableReasonString(int paramInt)
    {
      if ((paramInt >= 0) && (paramInt < 14)) {
        return QUALITY_NETWORK_SELECTION_DISABLE_REASON[paramInt];
      }
      return null;
    }
    
    public void clearDisableReasonCounter()
    {
      Arrays.fill(mNetworkSeclectionDisableCounter, 0);
    }
    
    public void clearDisableReasonCounter(int paramInt)
    {
      if ((paramInt >= 0) && (paramInt < 14))
      {
        mNetworkSeclectionDisableCounter[paramInt] = 0;
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Illegal reason value: ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public void copy(NetworkSelectionStatus paramNetworkSelectionStatus)
    {
      mStatus = mStatus;
      mNetworkSelectionDisableReason = mNetworkSelectionDisableReason;
      for (int i = 0; i < 14; i++) {
        mNetworkSeclectionDisableCounter[i] = mNetworkSeclectionDisableCounter[i];
      }
      mTemporarilyDisabledTimestamp = mTemporarilyDisabledTimestamp;
      mNetworkSelectionBSSID = mNetworkSelectionBSSID;
      setSeenInLastQualifiedNetworkSelection(paramNetworkSelectionStatus.getSeenInLastQualifiedNetworkSelection());
      setCandidate(paramNetworkSelectionStatus.getCandidate());
      setCandidateScore(paramNetworkSelectionStatus.getCandidateScore());
      setConnectChoice(paramNetworkSelectionStatus.getConnectChoice());
      setConnectChoiceTimestamp(paramNetworkSelectionStatus.getConnectChoiceTimestamp());
      setHasEverConnected(paramNetworkSelectionStatus.getHasEverConnected());
      setNotRecommended(paramNetworkSelectionStatus.isNotRecommended());
    }
    
    public ScanResult getCandidate()
    {
      return mCandidate;
    }
    
    public int getCandidateScore()
    {
      return mCandidateScore;
    }
    
    public String getConnectChoice()
    {
      return mConnectChoice;
    }
    
    public long getConnectChoiceTimestamp()
    {
      return mConnectChoiceTimestamp;
    }
    
    public int getDisableReasonCounter(int paramInt)
    {
      if ((paramInt >= 0) && (paramInt < 14)) {
        return mNetworkSeclectionDisableCounter[paramInt];
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Illegal reason value: ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public long getDisableTime()
    {
      return mTemporarilyDisabledTimestamp;
    }
    
    public boolean getHasEverConnected()
    {
      return mHasEverConnected;
    }
    
    public String getNetworkDisableReasonString()
    {
      return QUALITY_NETWORK_SELECTION_DISABLE_REASON[mNetworkSelectionDisableReason];
    }
    
    public String getNetworkSelectionBSSID()
    {
      return mNetworkSelectionBSSID;
    }
    
    public int getNetworkSelectionDisableReason()
    {
      return mNetworkSelectionDisableReason;
    }
    
    public int getNetworkSelectionStatus()
    {
      return mStatus;
    }
    
    public String getNetworkStatusString()
    {
      return QUALITY_NETWORK_SELECTION_STATUS[mStatus];
    }
    
    public boolean getSeenInLastQualifiedNetworkSelection()
    {
      return mSeenInLastQualifiedNetworkSelection;
    }
    
    public void incrementDisableReasonCounter(int paramInt)
    {
      if ((paramInt >= 0) && (paramInt < 14))
      {
        localObject = mNetworkSeclectionDisableCounter;
        localObject[paramInt] += 1;
        return;
      }
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Illegal reason value: ");
      ((StringBuilder)localObject).append(paramInt);
      throw new IllegalArgumentException(((StringBuilder)localObject).toString());
    }
    
    public boolean isDisabledByReason(int paramInt)
    {
      boolean bool;
      if (mNetworkSelectionDisableReason == paramInt) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isNetworkEnabled()
    {
      boolean bool;
      if (mStatus == 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isNetworkPermanentlyDisabled()
    {
      boolean bool;
      if (mStatus == 2) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isNetworkTemporaryDisabled()
    {
      int i = mStatus;
      boolean bool = true;
      if (i != 1) {
        bool = false;
      }
      return bool;
    }
    
    public boolean isNotRecommended()
    {
      return mNotRecommended;
    }
    
    public void readFromParcel(Parcel paramParcel)
    {
      setNetworkSelectionStatus(paramParcel.readInt());
      setNetworkSelectionDisableReason(paramParcel.readInt());
      boolean bool1 = false;
      for (int i = 0; i < 14; i++) {
        setDisableReasonCounter(i, paramParcel.readInt());
      }
      setDisableTime(paramParcel.readLong());
      setNetworkSelectionBSSID(paramParcel.readString());
      if (paramParcel.readInt() == 1)
      {
        setConnectChoice(paramParcel.readString());
        setConnectChoiceTimestamp(paramParcel.readLong());
      }
      else
      {
        setConnectChoice(null);
        setConnectChoiceTimestamp(-1L);
      }
      if (paramParcel.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      setHasEverConnected(bool2);
      boolean bool2 = bool1;
      if (paramParcel.readInt() != 0) {
        bool2 = true;
      }
      setNotRecommended(bool2);
    }
    
    public void setCandidate(ScanResult paramScanResult)
    {
      mCandidate = paramScanResult;
    }
    
    public void setCandidateScore(int paramInt)
    {
      mCandidateScore = paramInt;
    }
    
    public void setConnectChoice(String paramString)
    {
      mConnectChoice = paramString;
    }
    
    public void setConnectChoiceTimestamp(long paramLong)
    {
      mConnectChoiceTimestamp = paramLong;
    }
    
    public void setDisableReasonCounter(int paramInt1, int paramInt2)
    {
      if ((paramInt1 >= 0) && (paramInt1 < 14))
      {
        mNetworkSeclectionDisableCounter[paramInt1] = paramInt2;
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Illegal reason value: ");
      localStringBuilder.append(paramInt1);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public void setDisableTime(long paramLong)
    {
      mTemporarilyDisabledTimestamp = paramLong;
    }
    
    public void setHasEverConnected(boolean paramBoolean)
    {
      mHasEverConnected = paramBoolean;
    }
    
    public void setNetworkSelectionBSSID(String paramString)
    {
      mNetworkSelectionBSSID = paramString;
    }
    
    public void setNetworkSelectionDisableReason(int paramInt)
    {
      if ((paramInt >= 0) && (paramInt < 14))
      {
        mNetworkSelectionDisableReason = paramInt;
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Illegal reason value: ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public void setNetworkSelectionStatus(int paramInt)
    {
      if ((paramInt >= 0) && (paramInt < 3)) {
        mStatus = paramInt;
      }
    }
    
    public void setNotRecommended(boolean paramBoolean)
    {
      mNotRecommended = paramBoolean;
    }
    
    public void setSeenInLastQualifiedNetworkSelection(boolean paramBoolean)
    {
      mSeenInLastQualifiedNetworkSelection = paramBoolean;
    }
    
    public void writeToParcel(Parcel paramParcel)
    {
      paramParcel.writeInt(getNetworkSelectionStatus());
      paramParcel.writeInt(getNetworkSelectionDisableReason());
      for (int i = 0; i < 14; i++) {
        paramParcel.writeInt(getDisableReasonCounter(i));
      }
      paramParcel.writeLong(getDisableTime());
      paramParcel.writeString(getNetworkSelectionBSSID());
      if (getConnectChoice() != null)
      {
        paramParcel.writeInt(1);
        paramParcel.writeString(getConnectChoice());
        paramParcel.writeLong(getConnectChoiceTimestamp());
      }
      else
      {
        paramParcel.writeInt(-1);
      }
      paramParcel.writeInt(getHasEverConnected());
      paramParcel.writeInt(isNotRecommended());
    }
  }
  
  public static class PairwiseCipher
  {
    public static final int CCMP = 2;
    public static final int GCMP = 3;
    public static final int NONE = 0;
    @Deprecated
    public static final int TKIP = 1;
    public static final String[] strings = { "NONE", "TKIP", "CCMP", "GCMP" };
    public static final String varName = "pairwise";
    
    private PairwiseCipher() {}
  }
  
  public static class Protocol
  {
    public static final int OSEN = 2;
    public static final int RSN = 1;
    public static final int WAPI = 3;
    @Deprecated
    public static final int WPA = 0;
    public static final String[] strings = { "WPA", "RSN", "OSEN", "WAPI" };
    public static final String varName = "proto";
    
    private Protocol() {}
  }
  
  public static class RecentFailure
  {
    public static final int NONE = 0;
    public static final int STATUS_AP_UNABLE_TO_HANDLE_NEW_STA = 17;
    private int mAssociationStatus = 0;
    
    public RecentFailure() {}
    
    public void clear()
    {
      mAssociationStatus = 0;
    }
    
    public int getAssociationStatus()
    {
      return mAssociationStatus;
    }
    
    public void setAssociationStatus(int paramInt)
    {
      mAssociationStatus = paramInt;
    }
  }
  
  public static class Status
  {
    public static final int CURRENT = 0;
    public static final int DISABLED = 1;
    public static final int ENABLED = 2;
    public static final String[] strings = { "current", "disabled", "enabled" };
    
    private Status() {}
  }
  
  public static class SuiteBCipher
  {
    public static final int ECDHE_ECDSA = 0;
    public static final int ECDHE_RSA = 1;
    public static final String[] strings = { "ECDHE_ECDSA", "ECDHE_RSA" };
    public static final String varName = "SuiteB";
    
    private SuiteBCipher() {}
  }
}
