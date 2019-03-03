package android.net.wifi.rtt;

import android.annotation.SystemApi;
import android.net.MacAddress;
import android.net.wifi.ScanResult;
import android.net.wifi.aware.PeerHandle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;

public final class RangingRequest
  implements Parcelable
{
  public static final Parcelable.Creator<RangingRequest> CREATOR = new Parcelable.Creator()
  {
    public RangingRequest createFromParcel(Parcel paramAnonymousParcel)
    {
      return new RangingRequest(paramAnonymousParcel.readArrayList(null), null);
    }
    
    public RangingRequest[] newArray(int paramAnonymousInt)
    {
      return new RangingRequest[paramAnonymousInt];
    }
  };
  private static final int MAX_PEERS = 10;
  public final List<ResponderConfig> mRttPeers;
  
  private RangingRequest(List<ResponderConfig> paramList)
  {
    mRttPeers = paramList;
  }
  
  public static int getMaxPeers()
  {
    return 10;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void enforceValidity(boolean paramBoolean)
  {
    if (mRttPeers.size() <= 10)
    {
      Iterator localIterator = mRttPeers.iterator();
      while (localIterator.hasNext()) {
        if (!((ResponderConfig)localIterator.next()).isValid(paramBoolean)) {
          throw new IllegalArgumentException("Invalid Responder specification");
        }
      }
      return;
    }
    throw new IllegalArgumentException("Ranging to too many peers requested. Use getMaxPeers() API to get limit.");
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof RangingRequest)) {
      return false;
    }
    paramObject = (RangingRequest)paramObject;
    if ((mRttPeers.size() != mRttPeers.size()) || (!mRttPeers.containsAll(mRttPeers))) {
      bool = false;
    }
    return bool;
  }
  
  public int hashCode()
  {
    return mRttPeers.hashCode();
  }
  
  public String toString()
  {
    StringJoiner localStringJoiner = new StringJoiner(", ", "RangingRequest: mRttPeers=[", "]");
    Iterator localIterator = mRttPeers.iterator();
    while (localIterator.hasNext()) {
      localStringJoiner.add(((ResponderConfig)localIterator.next()).toString());
    }
    return localStringJoiner.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeList(mRttPeers);
  }
  
  public static final class Builder
  {
    private List<ResponderConfig> mRttPeers = new ArrayList();
    
    public Builder() {}
    
    public Builder addAccessPoint(ScanResult paramScanResult)
    {
      if (paramScanResult != null) {
        return addResponder(ResponderConfig.fromScanResult(paramScanResult));
      }
      throw new IllegalArgumentException("Null ScanResult!");
    }
    
    public Builder addAccessPoints(List<ScanResult> paramList)
    {
      if (paramList != null)
      {
        paramList = paramList.iterator();
        while (paramList.hasNext()) {
          addAccessPoint((ScanResult)paramList.next());
        }
        return this;
      }
      throw new IllegalArgumentException("Null list of ScanResults!");
    }
    
    @SystemApi
    public Builder addResponder(ResponderConfig paramResponderConfig)
    {
      if (paramResponderConfig != null)
      {
        mRttPeers.add(paramResponderConfig);
        return this;
      }
      throw new IllegalArgumentException("Null Responder!");
    }
    
    public Builder addWifiAwarePeer(MacAddress paramMacAddress)
    {
      if (paramMacAddress != null) {
        return addResponder(ResponderConfig.fromWifiAwarePeerMacAddressWithDefaults(paramMacAddress));
      }
      throw new IllegalArgumentException("Null peer MAC address");
    }
    
    public Builder addWifiAwarePeer(PeerHandle paramPeerHandle)
    {
      if (paramPeerHandle != null) {
        return addResponder(ResponderConfig.fromWifiAwarePeerHandleWithDefaults(paramPeerHandle));
      }
      throw new IllegalArgumentException("Null peer handler (identifier)");
    }
    
    public RangingRequest build()
    {
      return new RangingRequest(mRttPeers, null);
    }
  }
}
