package android.net.wifi.rtt;

import android.annotation.SystemApi;
import android.net.MacAddress;
import android.net.wifi.ScanResult;
import android.net.wifi.ScanResult.InformationElement;
import android.net.wifi.aware.PeerHandle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

@SystemApi
public final class ResponderConfig
  implements Parcelable
{
  private static final int AWARE_BAND_2_DISCOVERY_CHANNEL = 2437;
  public static final int CHANNEL_WIDTH_160MHZ = 3;
  public static final int CHANNEL_WIDTH_20MHZ = 0;
  public static final int CHANNEL_WIDTH_40MHZ = 1;
  public static final int CHANNEL_WIDTH_80MHZ = 2;
  public static final int CHANNEL_WIDTH_80MHZ_PLUS_MHZ = 4;
  public static final Parcelable.Creator<ResponderConfig> CREATOR = new Parcelable.Creator()
  {
    public ResponderConfig createFromParcel(Parcel paramAnonymousParcel)
    {
      boolean bool = paramAnonymousParcel.readBoolean();
      MacAddress localMacAddress = null;
      if (bool) {
        localMacAddress = (MacAddress)MacAddress.CREATOR.createFromParcel(paramAnonymousParcel);
      }
      bool = paramAnonymousParcel.readBoolean();
      PeerHandle localPeerHandle = null;
      if (bool) {
        localPeerHandle = new PeerHandle(paramAnonymousParcel.readInt());
      }
      int i = paramAnonymousParcel.readInt();
      if (paramAnonymousParcel.readInt() == 1) {
        bool = true;
      } else {
        bool = false;
      }
      int j = paramAnonymousParcel.readInt();
      int k = paramAnonymousParcel.readInt();
      int m = paramAnonymousParcel.readInt();
      int n = paramAnonymousParcel.readInt();
      int i1 = paramAnonymousParcel.readInt();
      if (localPeerHandle == null) {
        return new ResponderConfig(localMacAddress, i, bool, j, k, m, n, i1);
      }
      return new ResponderConfig(localPeerHandle, i, bool, j, k, m, n, i1);
    }
    
    public ResponderConfig[] newArray(int paramAnonymousInt)
    {
      return new ResponderConfig[paramAnonymousInt];
    }
  };
  public static final int PREAMBLE_HT = 1;
  public static final int PREAMBLE_LEGACY = 0;
  public static final int PREAMBLE_VHT = 2;
  public static final int RESPONDER_AP = 0;
  public static final int RESPONDER_AWARE = 4;
  public static final int RESPONDER_P2P_CLIENT = 3;
  public static final int RESPONDER_P2P_GO = 2;
  public static final int RESPONDER_STA = 1;
  private static final String TAG = "ResponderConfig";
  public final int centerFreq0;
  public final int centerFreq1;
  public final int channelWidth;
  public final int frequency;
  public final MacAddress macAddress;
  public final PeerHandle peerHandle;
  public final int preamble;
  public final int responderType;
  public final boolean supports80211mc;
  
  public ResponderConfig(MacAddress paramMacAddress, int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    if (paramMacAddress != null)
    {
      macAddress = paramMacAddress;
      peerHandle = null;
      responderType = paramInt1;
      supports80211mc = paramBoolean;
      channelWidth = paramInt2;
      frequency = paramInt3;
      centerFreq0 = paramInt4;
      centerFreq1 = paramInt5;
      preamble = paramInt6;
      return;
    }
    throw new IllegalArgumentException("Invalid ResponderConfig - must specify a MAC address");
  }
  
  public ResponderConfig(MacAddress paramMacAddress, PeerHandle paramPeerHandle, int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    macAddress = paramMacAddress;
    peerHandle = paramPeerHandle;
    responderType = paramInt1;
    supports80211mc = paramBoolean;
    channelWidth = paramInt2;
    frequency = paramInt3;
    centerFreq0 = paramInt4;
    centerFreq1 = paramInt5;
    preamble = paramInt6;
  }
  
  public ResponderConfig(PeerHandle paramPeerHandle, int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
  {
    macAddress = null;
    peerHandle = paramPeerHandle;
    responderType = paramInt1;
    supports80211mc = paramBoolean;
    channelWidth = paramInt2;
    frequency = paramInt3;
    centerFreq0 = paramInt4;
    centerFreq1 = paramInt5;
    preamble = paramInt6;
  }
  
  public static ResponderConfig fromScanResult(ScanResult paramScanResult)
  {
    MacAddress localMacAddress = MacAddress.fromString(BSSID);
    boolean bool = paramScanResult.is80211mcResponder();
    int i = translateScanResultChannelWidth(channelWidth);
    int j = frequency;
    int k = centerFreq0;
    int m = centerFreq1;
    int i2;
    if ((informationElements != null) && (informationElements.length != 0))
    {
      int n = 0;
      ScanResult.InformationElement[] arrayOfInformationElement = informationElements;
      int i1 = arrayOfInformationElement.length;
      i2 = 0;
      int i3 = 0;
      while (i3 < i1)
      {
        paramScanResult = arrayOfInformationElement[i3];
        int i4;
        if (id == 45)
        {
          i4 = 1;
        }
        else
        {
          i4 = i2;
          if (id == 191)
          {
            n = 1;
            i4 = i2;
          }
        }
        i3++;
        i2 = i4;
      }
      if (n != 0) {
        i2 = 2;
      }
      for (;;)
      {
        break;
        if (i2 != 0) {
          i2 = 1;
        } else {
          i2 = 0;
        }
      }
    }
    else
    {
      Log.e("ResponderConfig", "Scan Results do not contain IEs - using backup method to select preamble");
      if ((i != 2) && (i != 3)) {
        i2 = 1;
      } else {
        i2 = 2;
      }
    }
    return new ResponderConfig(localMacAddress, 0, bool, i, j, k, m, i2);
  }
  
  public static ResponderConfig fromWifiAwarePeerHandleWithDefaults(PeerHandle paramPeerHandle)
  {
    return new ResponderConfig(paramPeerHandle, 4, true, 0, 2437, 0, 0, 1);
  }
  
  public static ResponderConfig fromWifiAwarePeerMacAddressWithDefaults(MacAddress paramMacAddress)
  {
    return new ResponderConfig(paramMacAddress, 4, true, 0, 2437, 0, 0, 1);
  }
  
  static int translateScanResultChannelWidth(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("translateScanResultChannelWidth: bad ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    case 4: 
      return 4;
    case 3: 
      return 3;
    case 2: 
      return 2;
    case 1: 
      return 1;
    }
    return 0;
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
    if (!(paramObject instanceof ResponderConfig)) {
      return false;
    }
    paramObject = (ResponderConfig)paramObject;
    if ((!Objects.equals(macAddress, macAddress)) || (!Objects.equals(peerHandle, peerHandle)) || (responderType != responderType) || (supports80211mc != supports80211mc) || (channelWidth != channelWidth) || (frequency != frequency) || (centerFreq0 != centerFreq0) || (centerFreq1 != centerFreq1) || (preamble != preamble)) {
      bool = false;
    }
    return bool;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { macAddress, peerHandle, Integer.valueOf(responderType), Boolean.valueOf(supports80211mc), Integer.valueOf(channelWidth), Integer.valueOf(frequency), Integer.valueOf(centerFreq0), Integer.valueOf(centerFreq1), Integer.valueOf(preamble) });
  }
  
  public boolean isValid(boolean paramBoolean)
  {
    if (((macAddress == null) && (peerHandle == null)) || ((macAddress != null) && (peerHandle != null))) {
      return false;
    }
    return (paramBoolean) || (responderType != 4);
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer("ResponderConfig: macAddress=");
    localStringBuffer.append(macAddress);
    localStringBuffer.append(", peerHandle=");
    Object localObject;
    if (peerHandle == null) {
      localObject = "<null>";
    } else {
      localObject = Integer.valueOf(peerHandle.peerId);
    }
    localStringBuffer.append(localObject);
    localStringBuffer.append(", responderType=");
    localStringBuffer.append(responderType);
    localStringBuffer.append(", supports80211mc=");
    localStringBuffer.append(supports80211mc);
    localStringBuffer.append(", channelWidth=");
    localStringBuffer.append(channelWidth);
    localStringBuffer.append(", frequency=");
    localStringBuffer.append(frequency);
    localStringBuffer.append(", centerFreq0=");
    localStringBuffer.append(centerFreq0);
    localStringBuffer.append(", centerFreq1=");
    localStringBuffer.append(centerFreq1);
    localStringBuffer.append(", preamble=");
    localStringBuffer.append(preamble);
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (macAddress == null)
    {
      paramParcel.writeBoolean(false);
    }
    else
    {
      paramParcel.writeBoolean(true);
      macAddress.writeToParcel(paramParcel, paramInt);
    }
    if (peerHandle == null)
    {
      paramParcel.writeBoolean(false);
    }
    else
    {
      paramParcel.writeBoolean(true);
      paramParcel.writeInt(peerHandle.peerId);
    }
    paramParcel.writeInt(responderType);
    paramParcel.writeInt(supports80211mc);
    paramParcel.writeInt(channelWidth);
    paramParcel.writeInt(frequency);
    paramParcel.writeInt(centerFreq0);
    paramParcel.writeInt(centerFreq1);
    paramParcel.writeInt(preamble);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ChannelWidth {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface PreambleType {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ResponderType {}
}
