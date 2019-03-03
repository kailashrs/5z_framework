package android.net;

import android.annotation.SystemApi;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiSsid;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import java.util.Objects;

@SystemApi
public class NetworkKey
  implements Parcelable
{
  public static final Parcelable.Creator<NetworkKey> CREATOR = new Parcelable.Creator()
  {
    public NetworkKey createFromParcel(Parcel paramAnonymousParcel)
    {
      return new NetworkKey(paramAnonymousParcel, null);
    }
    
    public NetworkKey[] newArray(int paramAnonymousInt)
    {
      return new NetworkKey[paramAnonymousInt];
    }
  };
  private static final String TAG = "NetworkKey";
  public static final int TYPE_WIFI = 1;
  public final int type;
  public final WifiKey wifiKey;
  
  public NetworkKey(WifiKey paramWifiKey)
  {
    type = 1;
    wifiKey = paramWifiKey;
  }
  
  private NetworkKey(Parcel paramParcel)
  {
    type = paramParcel.readInt();
    if (type == 1)
    {
      wifiKey = ((WifiKey)WifiKey.CREATOR.createFromParcel(paramParcel));
      return;
    }
    paramParcel = new StringBuilder();
    paramParcel.append("Parcel has unknown type: ");
    paramParcel.append(type);
    throw new IllegalArgumentException(paramParcel.toString());
  }
  
  public static NetworkKey createFromScanResult(ScanResult paramScanResult)
  {
    if ((paramScanResult != null) && (wifiSsid != null))
    {
      String str = wifiSsid.toString();
      paramScanResult = BSSID;
      if ((!TextUtils.isEmpty(str)) && (!str.equals("<unknown ssid>")) && (!TextUtils.isEmpty(paramScanResult))) {
        try
        {
          paramScanResult = new WifiKey(String.format("\"%s\"", new Object[] { str }), paramScanResult);
          return new NetworkKey(paramScanResult);
        }
        catch (IllegalArgumentException paramScanResult)
        {
          Log.e("NetworkKey", "Unable to create WifiKey.", paramScanResult);
          return null;
        }
      }
    }
    return null;
  }
  
  public static NetworkKey createFromWifiInfo(WifiInfo paramWifiInfo)
  {
    if (paramWifiInfo != null)
    {
      String str = paramWifiInfo.getSSID();
      paramWifiInfo = paramWifiInfo.getBSSID();
      if ((!TextUtils.isEmpty(str)) && (!str.equals("<unknown ssid>")) && (!TextUtils.isEmpty(paramWifiInfo))) {
        try
        {
          paramWifiInfo = new WifiKey(str, paramWifiInfo);
          return new NetworkKey(paramWifiInfo);
        }
        catch (IllegalArgumentException paramWifiInfo)
        {
          Log.e("NetworkKey", "Unable to create WifiKey.", paramWifiInfo);
          return null;
        }
      }
    }
    return null;
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
      paramObject = (NetworkKey)paramObject;
      if ((type != type) || (!Objects.equals(wifiKey, wifiKey))) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(type), wifiKey });
  }
  
  public String toString()
  {
    if (type != 1) {
      return "InvalidKey";
    }
    return wifiKey.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(type);
    if (type == 1)
    {
      wifiKey.writeToParcel(paramParcel, paramInt);
      return;
    }
    paramParcel = new StringBuilder();
    paramParcel.append("NetworkKey has unknown type ");
    paramParcel.append(type);
    throw new IllegalStateException(paramParcel.toString());
  }
}
