package com.android.internal.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class VpnInfo
  implements Parcelable
{
  public static final Parcelable.Creator<VpnInfo> CREATOR = new Parcelable.Creator()
  {
    public VpnInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      VpnInfo localVpnInfo = new VpnInfo();
      ownerUid = paramAnonymousParcel.readInt();
      vpnIface = paramAnonymousParcel.readString();
      primaryUnderlyingIface = paramAnonymousParcel.readString();
      return localVpnInfo;
    }
    
    public VpnInfo[] newArray(int paramAnonymousInt)
    {
      return new VpnInfo[paramAnonymousInt];
    }
  };
  public int ownerUid;
  public String primaryUnderlyingIface;
  public String vpnIface;
  
  public VpnInfo() {}
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("VpnInfo{ownerUid=");
    localStringBuilder.append(ownerUid);
    localStringBuilder.append(", vpnIface='");
    localStringBuilder.append(vpnIface);
    localStringBuilder.append('\'');
    localStringBuilder.append(", primaryUnderlyingIface='");
    localStringBuilder.append(primaryUnderlyingIface);
    localStringBuilder.append('\'');
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(ownerUid);
    paramParcel.writeString(vpnIface);
    paramParcel.writeString(primaryUnderlyingIface);
  }
}
