package android.net.wifi;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ScanResult
  implements Parcelable
{
  public static final int CHANNEL_WIDTH_160MHZ = 3;
  public static final int CHANNEL_WIDTH_20MHZ = 0;
  public static final int CHANNEL_WIDTH_40MHZ = 1;
  public static final int CHANNEL_WIDTH_80MHZ = 2;
  public static final int CHANNEL_WIDTH_80MHZ_PLUS_MHZ = 4;
  public static final int CIPHER_CCMP = 3;
  public static final int CIPHER_GCMP = 4;
  public static final int CIPHER_NONE = 0;
  public static final int CIPHER_NO_GROUP_ADDRESSED = 1;
  public static final int CIPHER_TKIP = 2;
  public static final Parcelable.Creator<ScanResult> CREATOR = new Parcelable.Creator()
  {
    public ScanResult createFromParcel(Parcel paramAnonymousParcel)
    {
      Object localObject = null;
      int i = paramAnonymousParcel.readInt();
      boolean bool1 = true;
      if (i == 1) {
        localObject = (WifiSsid)WifiSsid.CREATOR.createFromParcel(paramAnonymousParcel);
      }
      ScanResult localScanResult = new ScanResult((WifiSsid)localObject, paramAnonymousParcel.readString(), paramAnonymousParcel.readString(), paramAnonymousParcel.readLong(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readString(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readLong(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), false);
      seen = paramAnonymousParcel.readLong();
      i = paramAnonymousParcel.readInt();
      int j = 0;
      boolean bool2;
      if (i != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      untrusted = bool2;
      numUsage = paramAnonymousParcel.readInt();
      venueName = paramAnonymousParcel.readString();
      operatorFriendlyName = paramAnonymousParcel.readString();
      flags = paramAnonymousParcel.readLong();
      int k = paramAnonymousParcel.readInt();
      int m;
      if (k != 0)
      {
        informationElements = new ScanResult.InformationElement[k];
        for (i = 0; i < k; i++)
        {
          informationElements[i] = new ScanResult.InformationElement();
          informationElements[i].id = paramAnonymousParcel.readInt();
          m = paramAnonymousParcel.readInt();
          informationElements[i].bytes = new byte[m];
          paramAnonymousParcel.readByteArray(informationElements[i].bytes);
        }
      }
      k = paramAnonymousParcel.readInt();
      if (k != 0)
      {
        anqpLines = new ArrayList();
        for (i = 0; i < k; i++) {
          anqpLines.add(paramAnonymousParcel.readString());
        }
      }
      k = paramAnonymousParcel.readInt();
      if (k != 0)
      {
        anqpElements = new AnqpInformationElement[k];
        for (i = 0; i < k; i++)
        {
          m = paramAnonymousParcel.readInt();
          int n = paramAnonymousParcel.readInt();
          localObject = new byte[paramAnonymousParcel.readInt()];
          paramAnonymousParcel.readByteArray((byte[])localObject);
          anqpElements[i] = new AnqpInformationElement(m, n, (byte[])localObject);
        }
      }
      if (paramAnonymousParcel.readInt() != 0) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
      isCarrierAp = bool2;
      carrierApEapType = paramAnonymousParcel.readInt();
      carrierName = paramAnonymousParcel.readString();
      k = paramAnonymousParcel.readInt();
      if (k != 0)
      {
        radioChainInfos = new ScanResult.RadioChainInfo[k];
        for (i = j; i < k; i++)
        {
          radioChainInfos[i] = new ScanResult.RadioChainInfo();
          radioChainInfos[i].id = paramAnonymousParcel.readInt();
          radioChainInfos[i].level = paramAnonymousParcel.readInt();
        }
      }
      return localScanResult;
    }
    
    public ScanResult[] newArray(int paramAnonymousInt)
    {
      return new ScanResult[paramAnonymousInt];
    }
  };
  public static final long FLAG_80211mc_RESPONDER = 2L;
  public static final long FLAG_PASSPOINT_NETWORK = 1L;
  public static final int KEY_MGMT_DPP = 10;
  public static final int KEY_MGMT_EAP = 2;
  public static final int KEY_MGMT_EAP_SHA256 = 6;
  public static final int KEY_MGMT_EAP_SUITE_B_192 = 13;
  public static final int KEY_MGMT_FILS_SHA256 = 8;
  public static final int KEY_MGMT_FILS_SHA384 = 9;
  public static final int KEY_MGMT_FT_EAP = 4;
  public static final int KEY_MGMT_FT_PSK = 3;
  public static final int KEY_MGMT_NONE = 0;
  public static final int KEY_MGMT_OSEN = 7;
  public static final int KEY_MGMT_OWE = 12;
  public static final int KEY_MGMT_PSK = 1;
  public static final int KEY_MGMT_PSK_SHA256 = 5;
  public static final int KEY_MGMT_SAE = 11;
  public static final int PROTOCOL_NONE = 0;
  public static final int PROTOCOL_OSEN = 3;
  public static final int PROTOCOL_WPA = 1;
  public static final int PROTOCOL_WPA2 = 2;
  public static final int UNSPECIFIED = -1;
  public String BSSID;
  public String SSID;
  public int anqpDomainId;
  public AnqpInformationElement[] anqpElements;
  public List<String> anqpLines;
  public String capabilities;
  public int carrierApEapType;
  public String carrierName;
  public int centerFreq0;
  public int centerFreq1;
  public int channelWidth;
  public int distanceCm;
  public int distanceSdCm;
  public long flags;
  public int frequency;
  public long hessid;
  public InformationElement[] informationElements;
  public boolean is80211McRTTResponder;
  public boolean isCarrierAp;
  public int level;
  public int numUsage;
  public CharSequence operatorFriendlyName;
  public RadioChainInfo[] radioChainInfos;
  public long seen;
  public long timestamp;
  @SystemApi
  public boolean untrusted;
  public CharSequence venueName;
  public WifiSsid wifiSsid;
  
  public ScanResult() {}
  
  public ScanResult(ScanResult paramScanResult)
  {
    if (paramScanResult != null)
    {
      wifiSsid = wifiSsid;
      SSID = SSID;
      BSSID = BSSID;
      hessid = hessid;
      anqpDomainId = anqpDomainId;
      informationElements = informationElements;
      anqpElements = anqpElements;
      capabilities = capabilities;
      level = level;
      frequency = frequency;
      channelWidth = channelWidth;
      centerFreq0 = centerFreq0;
      centerFreq1 = centerFreq1;
      timestamp = timestamp;
      distanceCm = distanceCm;
      distanceSdCm = distanceSdCm;
      seen = seen;
      untrusted = untrusted;
      numUsage = numUsage;
      venueName = venueName;
      operatorFriendlyName = operatorFriendlyName;
      flags = flags;
      isCarrierAp = isCarrierAp;
      carrierApEapType = carrierApEapType;
      carrierName = carrierName;
      radioChainInfos = radioChainInfos;
    }
  }
  
  public ScanResult(WifiSsid paramWifiSsid, String paramString1, long paramLong1, int paramInt1, byte[] paramArrayOfByte, String paramString2, int paramInt2, int paramInt3, long paramLong2)
  {
    wifiSsid = paramWifiSsid;
    if (paramWifiSsid != null) {
      paramWifiSsid = paramWifiSsid.toString();
    } else {
      paramWifiSsid = "<unknown ssid>";
    }
    SSID = paramWifiSsid;
    BSSID = paramString1;
    hessid = paramLong1;
    anqpDomainId = paramInt1;
    if (paramArrayOfByte != null)
    {
      anqpElements = new AnqpInformationElement[1];
      anqpElements[0] = new AnqpInformationElement(5271450, 8, paramArrayOfByte);
    }
    capabilities = paramString2;
    level = paramInt2;
    frequency = paramInt3;
    timestamp = paramLong2;
    distanceCm = -1;
    distanceSdCm = -1;
    channelWidth = -1;
    centerFreq0 = -1;
    centerFreq1 = -1;
    flags = 0L;
    isCarrierAp = false;
    carrierApEapType = -1;
    carrierName = null;
    radioChainInfos = null;
  }
  
  public ScanResult(WifiSsid paramWifiSsid, String paramString1, String paramString2, int paramInt1, int paramInt2, long paramLong, int paramInt3, int paramInt4)
  {
    wifiSsid = paramWifiSsid;
    if (paramWifiSsid != null) {
      paramWifiSsid = paramWifiSsid.toString();
    } else {
      paramWifiSsid = "<unknown ssid>";
    }
    SSID = paramWifiSsid;
    BSSID = paramString1;
    capabilities = paramString2;
    level = paramInt1;
    frequency = paramInt2;
    timestamp = paramLong;
    distanceCm = paramInt3;
    distanceSdCm = paramInt4;
    channelWidth = -1;
    centerFreq0 = -1;
    centerFreq1 = -1;
    flags = 0L;
    isCarrierAp = false;
    carrierApEapType = -1;
    carrierName = null;
    radioChainInfos = null;
  }
  
  public ScanResult(WifiSsid paramWifiSsid, String paramString1, String paramString2, long paramLong1, int paramInt1, String paramString3, int paramInt2, int paramInt3, long paramLong2, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, boolean paramBoolean)
  {
    this(paramString1, paramString2, paramLong1, paramInt1, paramString3, paramInt2, paramInt3, paramLong2, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramBoolean);
    wifiSsid = paramWifiSsid;
  }
  
  public ScanResult(String paramString1, String paramString2, long paramLong1, int paramInt1, String paramString3, int paramInt2, int paramInt3, long paramLong2, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, boolean paramBoolean)
  {
    SSID = paramString1;
    BSSID = paramString2;
    hessid = paramLong1;
    anqpDomainId = paramInt1;
    capabilities = paramString3;
    level = paramInt2;
    frequency = paramInt3;
    timestamp = paramLong2;
    distanceCm = paramInt4;
    distanceSdCm = paramInt5;
    channelWidth = paramInt6;
    centerFreq0 = paramInt7;
    centerFreq1 = paramInt8;
    if (paramBoolean) {
      flags = 2L;
    } else {
      flags = 0L;
    }
    isCarrierAp = false;
    carrierApEapType = -1;
    carrierName = null;
    radioChainInfos = null;
  }
  
  public static boolean is24GHz(int paramInt)
  {
    boolean bool;
    if ((paramInt > 2400) && (paramInt < 2500)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean is5GHz(int paramInt)
  {
    boolean bool;
    if ((paramInt > 4900) && (paramInt < 5900)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void clearFlag(long paramLong)
  {
    flags &= paramLong;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean is24GHz()
  {
    return is24GHz(frequency);
  }
  
  public boolean is5GHz()
  {
    return is5GHz(frequency);
  }
  
  public boolean is80211mcResponder()
  {
    boolean bool;
    if ((flags & 0x2) != 0L) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isPasspointNetwork()
  {
    boolean bool;
    if ((flags & 1L) != 0L) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void setFlag(long paramLong)
  {
    flags |= paramLong;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("SSID: ");
    Object localObject;
    if (wifiSsid == null) {
      localObject = "<unknown ssid>";
    } else {
      localObject = wifiSsid;
    }
    localStringBuffer.append(localObject);
    localStringBuffer.append(", BSSID: ");
    if (BSSID == null) {
      localObject = "<none>";
    } else {
      localObject = BSSID;
    }
    localStringBuffer.append((String)localObject);
    localStringBuffer.append(", capabilities: ");
    if (capabilities == null) {
      localObject = "<none>";
    } else {
      localObject = capabilities;
    }
    localStringBuffer.append((String)localObject);
    localStringBuffer.append(", level: ");
    localStringBuffer.append(level);
    localStringBuffer.append(", frequency: ");
    localStringBuffer.append(frequency);
    localStringBuffer.append(", timestamp: ");
    localStringBuffer.append(timestamp);
    localStringBuffer.append(", distance: ");
    if (distanceCm != -1) {
      localObject = Integer.valueOf(distanceCm);
    } else {
      localObject = "?";
    }
    localStringBuffer.append(localObject);
    localStringBuffer.append("(cm)");
    localStringBuffer.append(", distanceSd: ");
    if (distanceSdCm != -1) {
      localObject = Integer.valueOf(distanceSdCm);
    } else {
      localObject = "?";
    }
    localStringBuffer.append(localObject);
    localStringBuffer.append("(cm)");
    localStringBuffer.append(", passpoint: ");
    if ((flags & 1L) != 0L) {
      localObject = "yes";
    } else {
      localObject = "no";
    }
    localStringBuffer.append((String)localObject);
    localStringBuffer.append(", ChannelBandwidth: ");
    localStringBuffer.append(channelWidth);
    localStringBuffer.append(", centerFreq0: ");
    localStringBuffer.append(centerFreq0);
    localStringBuffer.append(", centerFreq1: ");
    localStringBuffer.append(centerFreq1);
    localStringBuffer.append(", 80211mcResponder: ");
    if ((flags & 0x2) != 0L) {
      localObject = "is supported";
    } else {
      localObject = "is not supported";
    }
    localStringBuffer.append((String)localObject);
    localStringBuffer.append(", Carrier AP: ");
    if (isCarrierAp) {
      localObject = "yes";
    } else {
      localObject = "no";
    }
    localStringBuffer.append((String)localObject);
    localStringBuffer.append(", Carrier AP EAP Type: ");
    localStringBuffer.append(carrierApEapType);
    localStringBuffer.append(", Carrier name: ");
    localStringBuffer.append(carrierName);
    localStringBuffer.append(", Radio Chain Infos: ");
    localStringBuffer.append(Arrays.toString(radioChainInfos));
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    Object localObject1 = wifiSsid;
    int i = 0;
    if (localObject1 != null)
    {
      paramParcel.writeInt(1);
      wifiSsid.writeToParcel(paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeString(SSID);
    paramParcel.writeString(BSSID);
    paramParcel.writeLong(hessid);
    paramParcel.writeInt(anqpDomainId);
    paramParcel.writeString(capabilities);
    paramParcel.writeInt(level);
    paramParcel.writeInt(frequency);
    paramParcel.writeLong(timestamp);
    paramParcel.writeInt(distanceCm);
    paramParcel.writeInt(distanceSdCm);
    paramParcel.writeInt(channelWidth);
    paramParcel.writeInt(centerFreq0);
    paramParcel.writeInt(centerFreq1);
    paramParcel.writeLong(seen);
    paramParcel.writeInt(untrusted);
    paramParcel.writeInt(numUsage);
    if (venueName != null) {
      localObject1 = venueName.toString();
    } else {
      localObject1 = "";
    }
    paramParcel.writeString((String)localObject1);
    if (operatorFriendlyName != null) {
      localObject1 = operatorFriendlyName.toString();
    } else {
      localObject1 = "";
    }
    paramParcel.writeString((String)localObject1);
    paramParcel.writeLong(flags);
    if (informationElements != null)
    {
      paramParcel.writeInt(informationElements.length);
      for (paramInt = 0; paramInt < informationElements.length; paramInt++)
      {
        paramParcel.writeInt(informationElements[paramInt].id);
        paramParcel.writeInt(informationElements[paramInt].bytes.length);
        paramParcel.writeByteArray(informationElements[paramInt].bytes);
      }
    }
    paramParcel.writeInt(0);
    if (anqpLines != null)
    {
      paramParcel.writeInt(anqpLines.size());
      for (paramInt = 0; paramInt < anqpLines.size(); paramInt++) {
        paramParcel.writeString((String)anqpLines.get(paramInt));
      }
    }
    paramParcel.writeInt(0);
    if (anqpElements != null)
    {
      paramParcel.writeInt(anqpElements.length);
      for (Object localObject2 : anqpElements)
      {
        paramParcel.writeInt(localObject2.getVendorId());
        paramParcel.writeInt(localObject2.getElementId());
        paramParcel.writeInt(localObject2.getPayload().length);
        paramParcel.writeByteArray(localObject2.getPayload());
      }
    }
    paramParcel.writeInt(0);
    paramParcel.writeInt(isCarrierAp);
    paramParcel.writeInt(carrierApEapType);
    paramParcel.writeString(carrierName);
    if (radioChainInfos != null)
    {
      paramParcel.writeInt(radioChainInfos.length);
      for (paramInt = i; paramInt < radioChainInfos.length; paramInt++)
      {
        paramParcel.writeInt(radioChainInfos[paramInt].id);
        paramParcel.writeInt(radioChainInfos[paramInt].level);
      }
    }
    paramParcel.writeInt(0);
  }
  
  public static class InformationElement
  {
    public static final int EID_BSS_LOAD = 11;
    public static final int EID_ERP = 42;
    public static final int EID_EXTENDED_CAPS = 127;
    public static final int EID_EXTENDED_SUPPORTED_RATES = 50;
    public static final int EID_HT_CAPABILITIES = 45;
    public static final int EID_HT_OPERATION = 61;
    public static final int EID_INTERWORKING = 107;
    public static final int EID_ROAMING_CONSORTIUM = 111;
    public static final int EID_RSN = 48;
    public static final int EID_SSID = 0;
    public static final int EID_SUPPORTED_RATES = 1;
    public static final int EID_TIM = 5;
    public static final int EID_VHT_CAPABILITIES = 191;
    public static final int EID_VHT_OPERATION = 192;
    public static final int EID_VSA = 221;
    public byte[] bytes;
    public int id;
    
    public InformationElement() {}
    
    public InformationElement(InformationElement paramInformationElement)
    {
      id = id;
      bytes = ((byte[])bytes.clone());
    }
  }
  
  public static class RadioChainInfo
  {
    public int id;
    public int level;
    
    public RadioChainInfo() {}
    
    public boolean equals(Object paramObject)
    {
      boolean bool = true;
      if (this == paramObject) {
        return true;
      }
      if (!(paramObject instanceof RadioChainInfo)) {
        return false;
      }
      paramObject = (RadioChainInfo)paramObject;
      if ((id != id) || (level != level)) {
        bool = false;
      }
      return bool;
    }
    
    public int hashCode()
    {
      return Objects.hash(new Object[] { Integer.valueOf(id), Integer.valueOf(level) });
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("RadioChainInfo: id=");
      localStringBuilder.append(id);
      localStringBuilder.append(", level=");
      localStringBuilder.append(level);
      return localStringBuilder.toString();
    }
  }
}
