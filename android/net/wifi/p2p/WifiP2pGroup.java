package android.net.wifi.p2p;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WifiP2pGroup
  implements Parcelable
{
  public static final Parcelable.Creator<WifiP2pGroup> CREATOR = new Parcelable.Creator()
  {
    public WifiP2pGroup createFromParcel(Parcel paramAnonymousParcel)
    {
      WifiP2pGroup localWifiP2pGroup = new WifiP2pGroup();
      localWifiP2pGroup.setNetworkName(paramAnonymousParcel.readString());
      localWifiP2pGroup.setOwner((WifiP2pDevice)paramAnonymousParcel.readParcelable(null));
      int i = paramAnonymousParcel.readByte();
      int j = 0;
      boolean bool = true;
      if (i != 1) {
        bool = false;
      }
      localWifiP2pGroup.setIsGroupOwner(bool);
      i = paramAnonymousParcel.readInt();
      while (j < i)
      {
        localWifiP2pGroup.addClient((WifiP2pDevice)paramAnonymousParcel.readParcelable(null));
        j++;
      }
      localWifiP2pGroup.setPassphrase(paramAnonymousParcel.readString());
      localWifiP2pGroup.setInterface(paramAnonymousParcel.readString());
      localWifiP2pGroup.setNetworkId(paramAnonymousParcel.readInt());
      return localWifiP2pGroup;
    }
    
    public WifiP2pGroup[] newArray(int paramAnonymousInt)
    {
      return new WifiP2pGroup[paramAnonymousInt];
    }
  };
  public static final int PERSISTENT_NET_ID = -2;
  public static final int TEMPORARY_NET_ID = -1;
  private static final Pattern groupStartedPattern = Pattern.compile("ssid=\"(.+)\" freq=(\\d+) (?:psk=)?([0-9a-fA-F]{64})?(?:passphrase=)?(?:\"(.{0,63})\")? go_dev_addr=((?:[0-9a-f]{2}:){5}[0-9a-f]{2}) ?(\\[PERSISTENT\\])?");
  private List<WifiP2pDevice> mClients = new ArrayList();
  private String mInterface;
  private boolean mIsGroupOwner;
  private int mNetId;
  private String mNetworkName;
  private WifiP2pDevice mOwner;
  private String mPassphrase;
  
  public WifiP2pGroup() {}
  
  public WifiP2pGroup(WifiP2pGroup paramWifiP2pGroup)
  {
    if (paramWifiP2pGroup != null)
    {
      mNetworkName = paramWifiP2pGroup.getNetworkName();
      mOwner = new WifiP2pDevice(paramWifiP2pGroup.getOwner());
      mIsGroupOwner = mIsGroupOwner;
      Iterator localIterator = paramWifiP2pGroup.getClientList().iterator();
      while (localIterator.hasNext())
      {
        WifiP2pDevice localWifiP2pDevice = (WifiP2pDevice)localIterator.next();
        mClients.add(localWifiP2pDevice);
      }
      mPassphrase = paramWifiP2pGroup.getPassphrase();
      mInterface = paramWifiP2pGroup.getInterface();
      mNetId = paramWifiP2pGroup.getNetworkId();
    }
  }
  
  public WifiP2pGroup(String paramString)
    throws IllegalArgumentException
  {
    String[] arrayOfString = paramString.split(" ");
    if (arrayOfString.length >= 3)
    {
      if (arrayOfString[0].startsWith("P2P-GROUP"))
      {
        mInterface = arrayOfString[1];
        mIsGroupOwner = arrayOfString[2].equals("GO");
        paramString = groupStartedPattern.matcher(paramString);
        if (!paramString.find()) {
          return;
        }
        mNetworkName = paramString.group(1);
        mPassphrase = paramString.group(4);
        mOwner = new WifiP2pDevice(paramString.group(5));
        if (paramString.group(6) != null) {
          mNetId = -2;
        } else {
          mNetId = -1;
        }
      }
      else
      {
        if (!arrayOfString[0].equals("P2P-INVITATION-RECEIVED")) {
          break label286;
        }
        mNetId = -2;
        int i = arrayOfString.length;
        for (int j = 0; j < i; j++)
        {
          paramString = arrayOfString[j].split("=");
          if (paramString.length == 2) {
            if (paramString[0].equals("sa"))
            {
              WifiP2pDevice localWifiP2pDevice = paramString[1];
              localWifiP2pDevice = new WifiP2pDevice();
              deviceAddress = paramString[1];
              mClients.add(localWifiP2pDevice);
            }
            else if (paramString[0].equals("go_dev_addr"))
            {
              mOwner = new WifiP2pDevice(paramString[1]);
            }
            else if (paramString[0].equals("persistent"))
            {
              mNetId = Integer.parseInt(paramString[1]);
            }
          }
        }
      }
      return;
      label286:
      throw new IllegalArgumentException("Malformed supplicant event");
    }
    throw new IllegalArgumentException("Malformed supplicant event");
  }
  
  public void addClient(WifiP2pDevice paramWifiP2pDevice)
  {
    Iterator localIterator = mClients.iterator();
    while (localIterator.hasNext()) {
      if (((WifiP2pDevice)localIterator.next()).equals(paramWifiP2pDevice)) {
        return;
      }
    }
    mClients.add(paramWifiP2pDevice);
  }
  
  public void addClient(String paramString)
  {
    addClient(new WifiP2pDevice(paramString));
  }
  
  public boolean contains(WifiP2pDevice paramWifiP2pDevice)
  {
    return (mOwner.equals(paramWifiP2pDevice)) || (mClients.contains(paramWifiP2pDevice));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public Collection<WifiP2pDevice> getClientList()
  {
    return Collections.unmodifiableCollection(mClients);
  }
  
  public String getInterface()
  {
    return mInterface;
  }
  
  public int getNetworkId()
  {
    return mNetId;
  }
  
  public String getNetworkName()
  {
    return mNetworkName;
  }
  
  public WifiP2pDevice getOwner()
  {
    return mOwner;
  }
  
  public String getPassphrase()
  {
    return mPassphrase;
  }
  
  public boolean isClientListEmpty()
  {
    boolean bool;
    if (mClients.size() == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isGroupOwner()
  {
    return mIsGroupOwner;
  }
  
  public boolean removeClient(WifiP2pDevice paramWifiP2pDevice)
  {
    return mClients.remove(paramWifiP2pDevice);
  }
  
  public boolean removeClient(String paramString)
  {
    return mClients.remove(new WifiP2pDevice(paramString));
  }
  
  public void setInterface(String paramString)
  {
    mInterface = paramString;
  }
  
  public void setIsGroupOwner(boolean paramBoolean)
  {
    mIsGroupOwner = paramBoolean;
  }
  
  public void setNetworkId(int paramInt)
  {
    mNetId = paramInt;
  }
  
  public void setNetworkName(String paramString)
  {
    mNetworkName = paramString;
  }
  
  public void setOwner(WifiP2pDevice paramWifiP2pDevice)
  {
    mOwner = paramWifiP2pDevice;
  }
  
  public void setPassphrase(String paramString)
  {
    mPassphrase = paramString;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("network: ");
    localStringBuffer.append(mNetworkName);
    localStringBuffer.append("\n isGO: ");
    localStringBuffer.append(mIsGroupOwner);
    localStringBuffer.append("\n GO: ");
    localStringBuffer.append(mOwner);
    Iterator localIterator = mClients.iterator();
    while (localIterator.hasNext())
    {
      WifiP2pDevice localWifiP2pDevice = (WifiP2pDevice)localIterator.next();
      localStringBuffer.append("\n Client: ");
      localStringBuffer.append(localWifiP2pDevice);
    }
    localStringBuffer.append("\n interface: ");
    localStringBuffer.append(mInterface);
    localStringBuffer.append("\n networkId: ");
    localStringBuffer.append(mNetId);
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mNetworkName);
    paramParcel.writeParcelable(mOwner, paramInt);
    paramParcel.writeByte(mIsGroupOwner);
    paramParcel.writeInt(mClients.size());
    Iterator localIterator = mClients.iterator();
    while (localIterator.hasNext()) {
      paramParcel.writeParcelable((WifiP2pDevice)localIterator.next(), paramInt);
    }
    paramParcel.writeString(mPassphrase);
    paramParcel.writeString(mInterface);
    paramParcel.writeInt(mNetId);
  }
}
