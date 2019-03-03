package android.net.wifi.p2p;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class WifiP2pDeviceList
  implements Parcelable
{
  public static final Parcelable.Creator<WifiP2pDeviceList> CREATOR = new Parcelable.Creator()
  {
    public WifiP2pDeviceList createFromParcel(Parcel paramAnonymousParcel)
    {
      WifiP2pDeviceList localWifiP2pDeviceList = new WifiP2pDeviceList();
      int i = paramAnonymousParcel.readInt();
      for (int j = 0; j < i; j++) {
        localWifiP2pDeviceList.update((WifiP2pDevice)paramAnonymousParcel.readParcelable(null));
      }
      return localWifiP2pDeviceList;
    }
    
    public WifiP2pDeviceList[] newArray(int paramAnonymousInt)
    {
      return new WifiP2pDeviceList[paramAnonymousInt];
    }
  };
  private final HashMap<String, WifiP2pDevice> mDevices = new HashMap();
  
  public WifiP2pDeviceList() {}
  
  public WifiP2pDeviceList(WifiP2pDeviceList paramWifiP2pDeviceList)
  {
    if (paramWifiP2pDeviceList != null)
    {
      paramWifiP2pDeviceList = paramWifiP2pDeviceList.getDeviceList().iterator();
      while (paramWifiP2pDeviceList.hasNext())
      {
        WifiP2pDevice localWifiP2pDevice = (WifiP2pDevice)paramWifiP2pDeviceList.next();
        mDevices.put(deviceAddress, new WifiP2pDevice(localWifiP2pDevice));
      }
    }
  }
  
  public WifiP2pDeviceList(ArrayList<WifiP2pDevice> paramArrayList)
  {
    Iterator localIterator = paramArrayList.iterator();
    while (localIterator.hasNext())
    {
      paramArrayList = (WifiP2pDevice)localIterator.next();
      if (deviceAddress != null) {
        mDevices.put(deviceAddress, new WifiP2pDevice(paramArrayList));
      }
    }
  }
  
  private void validateDevice(WifiP2pDevice paramWifiP2pDevice)
  {
    if (paramWifiP2pDevice != null)
    {
      if (!TextUtils.isEmpty(deviceAddress)) {
        return;
      }
      throw new IllegalArgumentException("Empty deviceAddress");
    }
    throw new IllegalArgumentException("Null device");
  }
  
  private void validateDeviceAddress(String paramString)
  {
    if (!TextUtils.isEmpty(paramString)) {
      return;
    }
    throw new IllegalArgumentException("Empty deviceAddress");
  }
  
  public boolean clear()
  {
    if (mDevices.isEmpty()) {
      return false;
    }
    mDevices.clear();
    return true;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public WifiP2pDevice get(String paramString)
  {
    validateDeviceAddress(paramString);
    return (WifiP2pDevice)mDevices.get(paramString);
  }
  
  public Collection<WifiP2pDevice> getDeviceList()
  {
    return Collections.unmodifiableCollection(mDevices.values());
  }
  
  public boolean isGroupOwner(String paramString)
  {
    validateDeviceAddress(paramString);
    Object localObject = (WifiP2pDevice)mDevices.get(paramString);
    if (localObject != null) {
      return ((WifiP2pDevice)localObject).isGroupOwner();
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Device not found ");
    ((StringBuilder)localObject).append(paramString);
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
  
  public WifiP2pDevice remove(String paramString)
  {
    validateDeviceAddress(paramString);
    return (WifiP2pDevice)mDevices.remove(paramString);
  }
  
  public boolean remove(WifiP2pDevice paramWifiP2pDevice)
  {
    validateDevice(paramWifiP2pDevice);
    boolean bool;
    if (mDevices.remove(deviceAddress) != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean remove(WifiP2pDeviceList paramWifiP2pDeviceList)
  {
    boolean bool = false;
    paramWifiP2pDeviceList = mDevices.values().iterator();
    while (paramWifiP2pDeviceList.hasNext()) {
      if (remove((WifiP2pDevice)paramWifiP2pDeviceList.next())) {
        bool = true;
      }
    }
    return bool;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    Iterator localIterator = mDevices.values().iterator();
    while (localIterator.hasNext())
    {
      WifiP2pDevice localWifiP2pDevice = (WifiP2pDevice)localIterator.next();
      localStringBuffer.append("\n");
      localStringBuffer.append(localWifiP2pDevice);
    }
    return localStringBuffer.toString();
  }
  
  public void update(WifiP2pDevice paramWifiP2pDevice)
  {
    updateSupplicantDetails(paramWifiP2pDevice);
    mDevices.get(deviceAddress)).status = status;
  }
  
  public void updateGroupCapability(String paramString, int paramInt)
  {
    validateDeviceAddress(paramString);
    paramString = (WifiP2pDevice)mDevices.get(paramString);
    if (paramString != null) {
      groupCapability = paramInt;
    }
  }
  
  public void updateStatus(String paramString, int paramInt)
  {
    validateDeviceAddress(paramString);
    paramString = (WifiP2pDevice)mDevices.get(paramString);
    if (paramString != null) {
      status = paramInt;
    }
  }
  
  public void updateSupplicantDetails(WifiP2pDevice paramWifiP2pDevice)
  {
    validateDevice(paramWifiP2pDevice);
    WifiP2pDevice localWifiP2pDevice = (WifiP2pDevice)mDevices.get(deviceAddress);
    if (localWifiP2pDevice != null)
    {
      deviceName = deviceName;
      primaryDeviceType = primaryDeviceType;
      secondaryDeviceType = secondaryDeviceType;
      wpsConfigMethodsSupported = wpsConfigMethodsSupported;
      deviceCapability = deviceCapability;
      groupCapability = groupCapability;
      wfdInfo = wfdInfo;
      return;
    }
    mDevices.put(deviceAddress, paramWifiP2pDevice);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mDevices.size());
    Iterator localIterator = mDevices.values().iterator();
    while (localIterator.hasNext()) {
      paramParcel.writeParcelable((WifiP2pDevice)localIterator.next(), paramInt);
    }
  }
}
