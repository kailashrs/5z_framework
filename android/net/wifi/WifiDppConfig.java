package android.net.wifi;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class WifiDppConfig
  implements Parcelable
{
  public static final Parcelable.Creator<WifiDppConfig> CREATOR = new Parcelable.Creator()
  {
    public WifiDppConfig createFromParcel(Parcel paramAnonymousParcel)
    {
      WifiDppConfig localWifiDppConfig = new WifiDppConfig();
      peer_bootstrap_id = paramAnonymousParcel.readInt();
      own_bootstrap_id = paramAnonymousParcel.readInt();
      dpp_role = paramAnonymousParcel.readInt();
      ssid = paramAnonymousParcel.readString();
      passphrase = paramAnonymousParcel.readString();
      isAp = paramAnonymousParcel.readInt();
      isDpp = paramAnonymousParcel.readInt();
      conf_id = paramAnonymousParcel.readInt();
      bootstrap_type = paramAnonymousParcel.readInt();
      chan_list = paramAnonymousParcel.readString();
      mac_addr = paramAnonymousParcel.readString();
      info = paramAnonymousParcel.readString();
      curve = paramAnonymousParcel.readString();
      expiry = paramAnonymousParcel.readInt();
      key = paramAnonymousParcel.readString();
      mEventResult.readFromParcel(paramAnonymousParcel);
      return localWifiDppConfig;
    }
    
    public WifiDppConfig[] newArray(int paramAnonymousInt)
    {
      return new WifiDppConfig[paramAnonymousInt];
    }
  };
  public static final int DPP_INVALID_CONFIG_ID = -1;
  public static final int DPP_ROLE_CONFIGURATOR = 0;
  public static final int DPP_ROLE_ENROLLEE = 1;
  public static final int DPP_TYPE_NAN_BOOTSTRAP = 1;
  public static final int DPP_TYPE_QR_CODE = 0;
  private static final String TAG = "WifiDppConfig";
  public int bootstrap_type = -1;
  public String chan_list = null;
  public int conf_id = -1;
  public String curve = null;
  public int dpp_role = -1;
  public int expiry = 0;
  public String info = null;
  public int isAp = -1;
  public int isDpp = -1;
  public String key = null;
  private DppResult mEventResult = new DppResult();
  public String mac_addr = null;
  public int own_bootstrap_id = -1;
  public String passphrase = null;
  public int peer_bootstrap_id = -1;
  public String ssid = null;
  
  public WifiDppConfig() {}
  
  public int describeContents()
  {
    return 0;
  }
  
  public DppResult getDppResult()
  {
    return mEventResult;
  }
  
  public void readFromParcel(Parcel paramParcel) {}
  
  public void setDppResult(DppResult paramDppResult)
  {
    mEventResult = paramDppResult;
  }
  
  public void writeToParcel(Parcel paramParcel) {}
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(peer_bootstrap_id);
    paramParcel.writeInt(own_bootstrap_id);
    paramParcel.writeInt(dpp_role);
    paramParcel.writeString(ssid);
    paramParcel.writeString(passphrase);
    paramParcel.writeInt(isAp);
    paramParcel.writeInt(isDpp);
    paramParcel.writeInt(conf_id);
    paramParcel.writeInt(bootstrap_type);
    paramParcel.writeString(chan_list);
    paramParcel.writeString(mac_addr);
    paramParcel.writeString(info);
    paramParcel.writeString(curve);
    paramParcel.writeInt(expiry);
    paramParcel.writeString(key);
    mEventResult.writeToParcel(paramParcel);
  }
  
  public static class DppResult
  {
    public static final int DPP_CONF_EVENT_TYPE_FAILED = 0;
    public static final int DPP_CONF_EVENT_TYPE_RECEIVED = 2;
    public static final int DPP_CONF_EVENT_TYPE_SENT = 1;
    public static final int DPP_EVENT_AUTH_SUCCESS = 0;
    public static final int DPP_EVENT_CONF = 4;
    public static final int DPP_EVENT_MISSING_AUTH = 5;
    public static final int DPP_EVENT_NETWORK_ID = 6;
    public static final int DPP_EVENT_NOT_COMPATIBLE = 1;
    public static final int DPP_EVENT_RESPONSE_PENDING = 2;
    public static final int DPP_EVENT_SCAN_PEER_QRCODE = 3;
    public byte authMissingParam = (byte)0;
    public String cSignKey = null;
    public byte capab = (byte)0;
    public byte configEventType = (byte)0;
    public String connector = null;
    public String iBootstrapData = null;
    public boolean initiator = false;
    public String netAccessKey = null;
    public int netAccessKeyExpiry = 0;
    public int netID = -1;
    public String passphrase = null;
    public String psk = null;
    public String ssid = null;
    
    public DppResult() {}
    
    public void readFromParcel(Parcel paramParcel)
    {
      boolean bool;
      if (paramParcel.readInt() > 0) {
        bool = true;
      } else {
        bool = false;
      }
      initiator = bool;
      netID = paramParcel.readInt();
      capab = paramParcel.readByte();
      authMissingParam = paramParcel.readByte();
      configEventType = paramParcel.readByte();
      iBootstrapData = paramParcel.readString();
      ssid = paramParcel.readString();
      connector = paramParcel.readString();
      cSignKey = paramParcel.readString();
      netAccessKey = paramParcel.readString();
      netAccessKeyExpiry = paramParcel.readInt();
      passphrase = paramParcel.readString();
      psk = paramParcel.readString();
    }
    
    public void writeToParcel(Parcel paramParcel)
    {
      paramParcel.writeInt(initiator);
      paramParcel.writeInt(netID);
      paramParcel.writeByte(capab);
      paramParcel.writeByte(authMissingParam);
      paramParcel.writeByte(configEventType);
      paramParcel.writeString(iBootstrapData);
      paramParcel.writeString(ssid);
      paramParcel.writeString(connector);
      paramParcel.writeString(cSignKey);
      paramParcel.writeString(netAccessKey);
      paramParcel.writeInt(netAccessKeyExpiry);
      paramParcel.writeString(passphrase);
      paramParcel.writeString(psk);
    }
  }
}
