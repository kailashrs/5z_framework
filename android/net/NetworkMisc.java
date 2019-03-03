package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class NetworkMisc
  implements Parcelable
{
  public static final Parcelable.Creator<NetworkMisc> CREATOR = new Parcelable.Creator()
  {
    public NetworkMisc createFromParcel(Parcel paramAnonymousParcel)
    {
      NetworkMisc localNetworkMisc = new NetworkMisc();
      int i = paramAnonymousParcel.readInt();
      boolean bool1 = false;
      if (i != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      allowBypass = bool2;
      if (paramAnonymousParcel.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      explicitlySelected = bool2;
      if (paramAnonymousParcel.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      acceptUnvalidated = bool2;
      subscriberId = paramAnonymousParcel.readString();
      boolean bool2 = bool1;
      if (paramAnonymousParcel.readInt() != 0) {
        bool2 = true;
      }
      provisioningNotificationDisabled = bool2;
      return localNetworkMisc;
    }
    
    public NetworkMisc[] newArray(int paramAnonymousInt)
    {
      return new NetworkMisc[paramAnonymousInt];
    }
  };
  public boolean acceptUnvalidated;
  public boolean allowBypass;
  public boolean explicitlySelected;
  public boolean provisioningNotificationDisabled;
  public String subscriberId;
  
  public NetworkMisc() {}
  
  public NetworkMisc(NetworkMisc paramNetworkMisc)
  {
    if (paramNetworkMisc != null)
    {
      allowBypass = allowBypass;
      explicitlySelected = explicitlySelected;
      acceptUnvalidated = acceptUnvalidated;
      subscriberId = subscriberId;
      provisioningNotificationDisabled = provisioningNotificationDisabled;
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(allowBypass);
    paramParcel.writeInt(explicitlySelected);
    paramParcel.writeInt(acceptUnvalidated);
    paramParcel.writeString(subscriberId);
    paramParcel.writeInt(provisioningNotificationDisabled);
  }
}
