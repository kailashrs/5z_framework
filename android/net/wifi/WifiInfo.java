package android.net.wifi;

import android.net.NetworkInfo.DetailedState;
import android.net.NetworkUtils;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.EnumMap;
import java.util.Locale;

public class WifiInfo
  implements Parcelable
{
  public static final Parcelable.Creator<WifiInfo> CREATOR = new Parcelable.Creator()
  {
    public WifiInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      WifiInfo localWifiInfo = new WifiInfo();
      localWifiInfo.setNetworkId(paramAnonymousParcel.readInt());
      localWifiInfo.setRssi(paramAnonymousParcel.readInt());
      localWifiInfo.setLinkSpeed(paramAnonymousParcel.readInt());
      localWifiInfo.setFrequency(paramAnonymousParcel.readInt());
      int i = paramAnonymousParcel.readByte();
      boolean bool1 = true;
      if (i == 1) {
        try
        {
          localWifiInfo.setInetAddress(InetAddress.getByAddress(paramAnonymousParcel.createByteArray()));
        }
        catch (UnknownHostException localUnknownHostException) {}
      }
      if (paramAnonymousParcel.readInt() == 1) {
        WifiInfo.access$002(localWifiInfo, (WifiSsid)WifiSsid.CREATOR.createFromParcel(paramAnonymousParcel));
      }
      WifiInfo.access$102(localWifiInfo, paramAnonymousParcel.readString());
      WifiInfo.access$202(localWifiInfo, paramAnonymousParcel.readString());
      boolean bool2;
      if (paramAnonymousParcel.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      WifiInfo.access$302(localWifiInfo, bool2);
      if (paramAnonymousParcel.readInt() != 0) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
      WifiInfo.access$402(localWifiInfo, bool2);
      score = paramAnonymousParcel.readInt();
      txSuccessRate = paramAnonymousParcel.readDouble();
      txRetriesRate = paramAnonymousParcel.readDouble();
      txBadRate = paramAnonymousParcel.readDouble();
      rxSuccessRate = paramAnonymousParcel.readDouble();
      WifiInfo.access$502(localWifiInfo, (SupplicantState)SupplicantState.CREATOR.createFromParcel(paramAnonymousParcel));
      return localWifiInfo;
    }
    
    public WifiInfo[] newArray(int paramAnonymousInt)
    {
      return new WifiInfo[paramAnonymousInt];
    }
  };
  public static final String DEFAULT_MAC_ADDRESS = "02:00:00:00:00:00";
  public static final String FREQUENCY_UNITS = "MHz";
  public static final int INVALID_RSSI = -127;
  public static final String LINK_SPEED_UNITS = "Mbps";
  public static final int MAX_RSSI = 200;
  public static final int MIN_RSSI = -126;
  private static final String TAG = "WifiInfo";
  private static final EnumMap<SupplicantState, NetworkInfo.DetailedState> stateMap = new EnumMap(SupplicantState.class);
  private String mBSSID;
  private boolean mEphemeral;
  private int mFrequency;
  private InetAddress mIpAddress;
  private int mLinkSpeed;
  private String mMacAddress = "02:00:00:00:00:00";
  private boolean mMeteredHint;
  private int mNetworkId;
  private int mRssi;
  private SupplicantState mSupplicantState;
  private WifiSsid mWifiSsid;
  public long rxSuccess;
  public double rxSuccessRate;
  public int score;
  public long txBad;
  public double txBadRate;
  public long txRetries;
  public double txRetriesRate;
  public long txSuccess;
  public double txSuccessRate;
  
  static
  {
    stateMap.put(SupplicantState.DISCONNECTED, NetworkInfo.DetailedState.DISCONNECTED);
    stateMap.put(SupplicantState.INTERFACE_DISABLED, NetworkInfo.DetailedState.DISCONNECTED);
    stateMap.put(SupplicantState.INACTIVE, NetworkInfo.DetailedState.IDLE);
    stateMap.put(SupplicantState.SCANNING, NetworkInfo.DetailedState.SCANNING);
    stateMap.put(SupplicantState.AUTHENTICATING, NetworkInfo.DetailedState.CONNECTING);
    stateMap.put(SupplicantState.ASSOCIATING, NetworkInfo.DetailedState.CONNECTING);
    stateMap.put(SupplicantState.ASSOCIATED, NetworkInfo.DetailedState.CONNECTING);
    stateMap.put(SupplicantState.FOUR_WAY_HANDSHAKE, NetworkInfo.DetailedState.AUTHENTICATING);
    stateMap.put(SupplicantState.GROUP_HANDSHAKE, NetworkInfo.DetailedState.AUTHENTICATING);
    stateMap.put(SupplicantState.COMPLETED, NetworkInfo.DetailedState.OBTAINING_IPADDR);
    stateMap.put(SupplicantState.DORMANT, NetworkInfo.DetailedState.DISCONNECTED);
    stateMap.put(SupplicantState.UNINITIALIZED, NetworkInfo.DetailedState.IDLE);
    stateMap.put(SupplicantState.INVALID, NetworkInfo.DetailedState.FAILED);
  }
  
  public WifiInfo()
  {
    mWifiSsid = null;
    mBSSID = null;
    mNetworkId = -1;
    mSupplicantState = SupplicantState.UNINITIALIZED;
    mRssi = -127;
    mLinkSpeed = -1;
    mFrequency = -1;
  }
  
  public WifiInfo(WifiInfo paramWifiInfo)
  {
    if (paramWifiInfo != null)
    {
      mSupplicantState = mSupplicantState;
      mBSSID = mBSSID;
      mWifiSsid = mWifiSsid;
      mNetworkId = mNetworkId;
      mRssi = mRssi;
      mLinkSpeed = mLinkSpeed;
      mFrequency = mFrequency;
      mIpAddress = mIpAddress;
      mMacAddress = mMacAddress;
      mMeteredHint = mMeteredHint;
      mEphemeral = mEphemeral;
      txBad = txBad;
      txRetries = txRetries;
      txSuccess = txSuccess;
      rxSuccess = rxSuccess;
      txBadRate = txBadRate;
      txRetriesRate = txRetriesRate;
      txSuccessRate = txSuccessRate;
      rxSuccessRate = rxSuccessRate;
      score = score;
    }
  }
  
  public static NetworkInfo.DetailedState getDetailedStateOf(SupplicantState paramSupplicantState)
  {
    return (NetworkInfo.DetailedState)stateMap.get(paramSupplicantState);
  }
  
  public static String removeDoubleQuotes(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    int i = paramString.length();
    if ((i > 1) && (paramString.charAt(0) == '"') && (paramString.charAt(i - 1) == '"')) {
      return paramString.substring(1, i - 1);
    }
    return paramString;
  }
  
  static SupplicantState valueOf(String paramString)
  {
    if ("4WAY_HANDSHAKE".equalsIgnoreCase(paramString)) {
      return SupplicantState.FOUR_WAY_HANDSHAKE;
    }
    try
    {
      paramString = SupplicantState.valueOf(paramString.toUpperCase(Locale.ROOT));
      return paramString;
    }
    catch (IllegalArgumentException paramString) {}
    return SupplicantState.INVALID;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getBSSID()
  {
    return mBSSID;
  }
  
  public int getFrequency()
  {
    return mFrequency;
  }
  
  public boolean getHiddenSSID()
  {
    if (mWifiSsid == null) {
      return false;
    }
    return mWifiSsid.isHidden();
  }
  
  public int getIpAddress()
  {
    int i = 0;
    if ((mIpAddress instanceof Inet4Address)) {
      i = NetworkUtils.inetAddressToInt((Inet4Address)mIpAddress);
    }
    return i;
  }
  
  public int getLinkSpeed()
  {
    return mLinkSpeed;
  }
  
  public String getMacAddress()
  {
    return mMacAddress;
  }
  
  public boolean getMeteredHint()
  {
    return mMeteredHint;
  }
  
  public int getNetworkId()
  {
    return mNetworkId;
  }
  
  public int getRssi()
  {
    return mRssi;
  }
  
  public String getSSID()
  {
    if (mWifiSsid != null)
    {
      String str = mWifiSsid.toString();
      if (!TextUtils.isEmpty(str))
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("\"");
        ((StringBuilder)localObject).append(str);
        ((StringBuilder)localObject).append("\"");
        return ((StringBuilder)localObject).toString();
      }
      Object localObject = mWifiSsid.getHexString();
      if (localObject == null) {
        localObject = "<unknown ssid>";
      }
      return localObject;
    }
    return "<unknown ssid>";
  }
  
  public SupplicantState getSupplicantState()
  {
    return mSupplicantState;
  }
  
  public WifiSsid getWifiSsid()
  {
    return mWifiSsid;
  }
  
  public boolean hasRealMacAddress()
  {
    boolean bool;
    if ((mMacAddress != null) && (!"02:00:00:00:00:00".equals(mMacAddress))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean is24GHz()
  {
    return ScanResult.is24GHz(mFrequency);
  }
  
  public boolean is5GHz()
  {
    return ScanResult.is5GHz(mFrequency);
  }
  
  public boolean isEphemeral()
  {
    return mEphemeral;
  }
  
  public void reset()
  {
    setInetAddress(null);
    setBSSID(null);
    setSSID(null);
    setNetworkId(-1);
    setRssi(-127);
    setLinkSpeed(-1);
    setFrequency(-1);
    setMeteredHint(false);
    setEphemeral(false);
    txBad = 0L;
    txSuccess = 0L;
    rxSuccess = 0L;
    txRetries = 0L;
    txBadRate = 0.0D;
    txSuccessRate = 0.0D;
    rxSuccessRate = 0.0D;
    txRetriesRate = 0.0D;
    score = 0;
  }
  
  public void setBSSID(String paramString)
  {
    mBSSID = paramString;
  }
  
  public void setEphemeral(boolean paramBoolean)
  {
    mEphemeral = paramBoolean;
  }
  
  public void setFrequency(int paramInt)
  {
    mFrequency = paramInt;
  }
  
  public void setInetAddress(InetAddress paramInetAddress)
  {
    mIpAddress = paramInetAddress;
  }
  
  public void setLinkSpeed(int paramInt)
  {
    mLinkSpeed = paramInt;
  }
  
  public void setMacAddress(String paramString)
  {
    mMacAddress = paramString;
  }
  
  public void setMeteredHint(boolean paramBoolean)
  {
    mMeteredHint = paramBoolean;
  }
  
  public void setNetworkId(int paramInt)
  {
    mNetworkId = paramInt;
  }
  
  public void setRssi(int paramInt)
  {
    int i = paramInt;
    if (paramInt < -127) {
      i = -127;
    }
    paramInt = i;
    if (i > 200) {
      paramInt = 200;
    }
    mRssi = paramInt;
  }
  
  public void setSSID(WifiSsid paramWifiSsid)
  {
    mWifiSsid = paramWifiSsid;
  }
  
  public void setSupplicantState(SupplicantState paramSupplicantState)
  {
    mSupplicantState = paramSupplicantState;
  }
  
  void setSupplicantState(String paramString)
  {
    mSupplicantState = valueOf(paramString);
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("SSID: ");
    Object localObject;
    if (mWifiSsid == null) {
      localObject = "<unknown ssid>";
    } else {
      localObject = mWifiSsid;
    }
    localStringBuffer.append(localObject);
    localStringBuffer.append(", BSSID: ");
    if (mBSSID == null) {
      localObject = "<none>";
    } else {
      localObject = mBSSID;
    }
    localStringBuffer.append((String)localObject);
    localStringBuffer.append(", MAC: ");
    if (mMacAddress == null) {
      localObject = "<none>";
    } else {
      localObject = mMacAddress;
    }
    localStringBuffer.append((String)localObject);
    localStringBuffer.append(", Supplicant state: ");
    if (mSupplicantState == null) {
      localObject = "<none>";
    } else {
      localObject = mSupplicantState;
    }
    localStringBuffer.append(localObject);
    localStringBuffer.append(", RSSI: ");
    localStringBuffer.append(mRssi);
    localStringBuffer.append(", Link speed: ");
    localStringBuffer.append(mLinkSpeed);
    localStringBuffer.append("Mbps");
    localStringBuffer.append(", Frequency: ");
    localStringBuffer.append(mFrequency);
    localStringBuffer.append("MHz");
    localStringBuffer.append(", Net ID: ");
    localStringBuffer.append(mNetworkId);
    localStringBuffer.append(", Metered hint: ");
    localStringBuffer.append(mMeteredHint);
    localStringBuffer.append(", score: ");
    localStringBuffer.append(Integer.toString(score));
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mNetworkId);
    paramParcel.writeInt(mRssi);
    paramParcel.writeInt(mLinkSpeed);
    paramParcel.writeInt(mFrequency);
    if (mIpAddress != null)
    {
      paramParcel.writeByte((byte)1);
      paramParcel.writeByteArray(mIpAddress.getAddress());
    }
    else
    {
      paramParcel.writeByte((byte)0);
    }
    if (mWifiSsid != null)
    {
      paramParcel.writeInt(1);
      mWifiSsid.writeToParcel(paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeString(mBSSID);
    paramParcel.writeString(mMacAddress);
    paramParcel.writeInt(mMeteredHint);
    paramParcel.writeInt(mEphemeral);
    paramParcel.writeInt(score);
    paramParcel.writeDouble(txSuccessRate);
    paramParcel.writeDouble(txRetriesRate);
    paramParcel.writeDouble(txBadRate);
    paramParcel.writeDouble(rxSuccessRate);
    mSupplicantState.writeToParcel(paramParcel, paramInt);
  }
}
