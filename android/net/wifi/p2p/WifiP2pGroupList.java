package android.net.wifi.p2p;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.LruCache;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class WifiP2pGroupList
  implements Parcelable
{
  public static final Parcelable.Creator<WifiP2pGroupList> CREATOR = new Parcelable.Creator()
  {
    public WifiP2pGroupList createFromParcel(Parcel paramAnonymousParcel)
    {
      WifiP2pGroupList localWifiP2pGroupList = new WifiP2pGroupList();
      int i = paramAnonymousParcel.readInt();
      for (int j = 0; j < i; j++) {
        localWifiP2pGroupList.add((WifiP2pGroup)paramAnonymousParcel.readParcelable(null));
      }
      return localWifiP2pGroupList;
    }
    
    public WifiP2pGroupList[] newArray(int paramAnonymousInt)
    {
      return new WifiP2pGroupList[paramAnonymousInt];
    }
  };
  private static final int CREDENTIAL_MAX_NUM = 32;
  private boolean isClearCalled = false;
  private final LruCache<Integer, WifiP2pGroup> mGroups;
  private final GroupDeleteListener mListener;
  
  public WifiP2pGroupList()
  {
    this(null, null);
  }
  
  public WifiP2pGroupList(WifiP2pGroupList paramWifiP2pGroupList, GroupDeleteListener paramGroupDeleteListener)
  {
    mListener = paramGroupDeleteListener;
    mGroups = new LruCache(32)
    {
      protected void entryRemoved(boolean paramAnonymousBoolean, Integer paramAnonymousInteger, WifiP2pGroup paramAnonymousWifiP2pGroup1, WifiP2pGroup paramAnonymousWifiP2pGroup2)
      {
        if ((mListener != null) && (!isClearCalled)) {
          mListener.onDeleteGroup(paramAnonymousWifiP2pGroup1.getNetworkId());
        }
      }
    };
    if (paramWifiP2pGroupList != null)
    {
      paramGroupDeleteListener = mGroups.snapshot().entrySet().iterator();
      while (paramGroupDeleteListener.hasNext())
      {
        paramWifiP2pGroupList = (Map.Entry)paramGroupDeleteListener.next();
        mGroups.put((Integer)paramWifiP2pGroupList.getKey(), (WifiP2pGroup)paramWifiP2pGroupList.getValue());
      }
    }
  }
  
  public void add(WifiP2pGroup paramWifiP2pGroup)
  {
    mGroups.put(Integer.valueOf(paramWifiP2pGroup.getNetworkId()), paramWifiP2pGroup);
  }
  
  public boolean clear()
  {
    if (mGroups.size() == 0) {
      return false;
    }
    isClearCalled = true;
    mGroups.evictAll();
    isClearCalled = false;
    return true;
  }
  
  public boolean contains(int paramInt)
  {
    Iterator localIterator = mGroups.snapshot().values().iterator();
    while (localIterator.hasNext()) {
      if (paramInt == ((WifiP2pGroup)localIterator.next()).getNetworkId()) {
        return true;
      }
    }
    return false;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public Collection<WifiP2pGroup> getGroupList()
  {
    return mGroups.snapshot().values();
  }
  
  public int getNetworkId(String paramString)
  {
    if (paramString == null) {
      return -1;
    }
    Iterator localIterator = mGroups.snapshot().values().iterator();
    while (localIterator.hasNext())
    {
      WifiP2pGroup localWifiP2pGroup = (WifiP2pGroup)localIterator.next();
      if (paramString.equalsIgnoreCase(getOwnerdeviceAddress))
      {
        mGroups.get(Integer.valueOf(localWifiP2pGroup.getNetworkId()));
        return localWifiP2pGroup.getNetworkId();
      }
    }
    return -1;
  }
  
  public int getNetworkId(String paramString1, String paramString2)
  {
    if ((paramString1 != null) && (paramString2 != null))
    {
      Iterator localIterator = mGroups.snapshot().values().iterator();
      while (localIterator.hasNext())
      {
        WifiP2pGroup localWifiP2pGroup = (WifiP2pGroup)localIterator.next();
        if ((paramString1.equalsIgnoreCase(getOwnerdeviceAddress)) && (paramString2.equals(localWifiP2pGroup.getNetworkName())))
        {
          mGroups.get(Integer.valueOf(localWifiP2pGroup.getNetworkId()));
          return localWifiP2pGroup.getNetworkId();
        }
      }
      return -1;
    }
    return -1;
  }
  
  public String getOwnerAddr(int paramInt)
  {
    WifiP2pGroup localWifiP2pGroup = (WifiP2pGroup)mGroups.get(Integer.valueOf(paramInt));
    if (localWifiP2pGroup != null) {
      return getOwnerdeviceAddress;
    }
    return null;
  }
  
  public void remove(int paramInt)
  {
    mGroups.remove(Integer.valueOf(paramInt));
  }
  
  void remove(String paramString)
  {
    remove(getNetworkId(paramString));
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    Iterator localIterator = mGroups.snapshot().values().iterator();
    while (localIterator.hasNext())
    {
      localStringBuffer.append((WifiP2pGroup)localIterator.next());
      localStringBuffer.append("\n");
    }
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    Object localObject = mGroups.snapshot().values();
    paramParcel.writeInt(((Collection)localObject).size());
    localObject = ((Collection)localObject).iterator();
    while (((Iterator)localObject).hasNext()) {
      paramParcel.writeParcelable((WifiP2pGroup)((Iterator)localObject).next(), paramInt);
    }
  }
  
  public static abstract interface GroupDeleteListener
  {
    public abstract void onDeleteGroup(int paramInt);
  }
}
