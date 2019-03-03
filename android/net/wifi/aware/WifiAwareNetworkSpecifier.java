package android.net.wifi.aware;

import android.net.NetworkSpecifier;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;
import java.util.Objects;

public final class WifiAwareNetworkSpecifier
  extends NetworkSpecifier
  implements Parcelable
{
  public static final Parcelable.Creator<WifiAwareNetworkSpecifier> CREATOR = new Parcelable.Creator()
  {
    public WifiAwareNetworkSpecifier createFromParcel(Parcel paramAnonymousParcel)
    {
      return new WifiAwareNetworkSpecifier(paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.createByteArray(), paramAnonymousParcel.createByteArray(), paramAnonymousParcel.readString(), paramAnonymousParcel.readInt());
    }
    
    public WifiAwareNetworkSpecifier[] newArray(int paramAnonymousInt)
    {
      return new WifiAwareNetworkSpecifier[paramAnonymousInt];
    }
  };
  public static final int NETWORK_SPECIFIER_TYPE_IB = 0;
  public static final int NETWORK_SPECIFIER_TYPE_IB_ANY_PEER = 1;
  public static final int NETWORK_SPECIFIER_TYPE_MAX_VALID = 3;
  public static final int NETWORK_SPECIFIER_TYPE_OOB = 2;
  public static final int NETWORK_SPECIFIER_TYPE_OOB_ANY_PEER = 3;
  public final int clientId;
  public final String passphrase;
  public final int peerId;
  public final byte[] peerMac;
  public final byte[] pmk;
  public final int requestorUid;
  public final int role;
  public final int sessionId;
  public final int type;
  
  public WifiAwareNetworkSpecifier(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, String paramString, int paramInt6)
  {
    type = paramInt1;
    role = paramInt2;
    clientId = paramInt3;
    sessionId = paramInt4;
    peerId = paramInt5;
    peerMac = paramArrayOfByte1;
    pmk = paramArrayOfByte2;
    passphrase = paramString;
    requestorUid = paramInt6;
  }
  
  public void assertValidFromUid(int paramInt)
  {
    if (requestorUid == paramInt) {
      return;
    }
    throw new SecurityException("mismatched UIDs");
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
    if (!(paramObject instanceof WifiAwareNetworkSpecifier)) {
      return false;
    }
    paramObject = (WifiAwareNetworkSpecifier)paramObject;
    if ((type != type) || (role != role) || (clientId != clientId) || (sessionId != sessionId) || (peerId != peerId) || (!Arrays.equals(peerMac, peerMac)) || (!Arrays.equals(pmk, pmk)) || (!Objects.equals(passphrase, passphrase)) || (requestorUid != requestorUid)) {
      bool = false;
    }
    return bool;
  }
  
  public int hashCode()
  {
    return 31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * 17 + type) + role) + clientId) + sessionId) + peerId) + Arrays.hashCode(peerMac)) + Arrays.hashCode(pmk)) + Objects.hashCode(passphrase)) + requestorUid;
  }
  
  public boolean isOutOfBand()
  {
    boolean bool;
    if ((type != 2) && (type != 3)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean satisfiedBy(NetworkSpecifier paramNetworkSpecifier)
  {
    if ((paramNetworkSpecifier instanceof WifiAwareAgentNetworkSpecifier)) {
      return ((WifiAwareAgentNetworkSpecifier)paramNetworkSpecifier).satisfiesAwareNetworkSpecifier(this);
    }
    return equals(paramNetworkSpecifier);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("WifiAwareNetworkSpecifier [");
    localStringBuilder.append("type=");
    localStringBuilder.append(type);
    localStringBuilder.append(", role=");
    localStringBuilder.append(role);
    localStringBuilder.append(", clientId=");
    localStringBuilder.append(clientId);
    localStringBuilder.append(", sessionId=");
    localStringBuilder.append(sessionId);
    localStringBuilder.append(", peerId=");
    localStringBuilder.append(peerId);
    localStringBuilder.append(", peerMac=");
    String str;
    if (peerMac == null) {
      str = "<null>";
    } else {
      str = "<non-null>";
    }
    localStringBuilder.append(str);
    localStringBuilder.append(", pmk=");
    if (pmk == null) {
      str = "<null>";
    } else {
      str = "<non-null>";
    }
    localStringBuilder.append(str);
    localStringBuilder.append(", passphrase=");
    if (passphrase == null) {
      str = "<null>";
    } else {
      str = "<non-null>";
    }
    localStringBuilder.append(str);
    localStringBuilder.append(", requestorUid=");
    localStringBuilder.append(requestorUid);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(type);
    paramParcel.writeInt(role);
    paramParcel.writeInt(clientId);
    paramParcel.writeInt(sessionId);
    paramParcel.writeInt(peerId);
    paramParcel.writeByteArray(peerMac);
    paramParcel.writeByteArray(pmk);
    paramParcel.writeString(passphrase);
    paramParcel.writeInt(requestorUid);
  }
}
