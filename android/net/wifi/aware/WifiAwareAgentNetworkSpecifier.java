package android.net.wifi.aware;

import android.net.NetworkSpecifier;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringJoiner;
import libcore.util.HexEncoding;

public class WifiAwareAgentNetworkSpecifier
  extends NetworkSpecifier
  implements Parcelable
{
  public static final Parcelable.Creator<WifiAwareAgentNetworkSpecifier> CREATOR = new Parcelable.Creator()
  {
    public WifiAwareAgentNetworkSpecifier createFromParcel(Parcel paramAnonymousParcel)
    {
      WifiAwareAgentNetworkSpecifier localWifiAwareAgentNetworkSpecifier = new WifiAwareAgentNetworkSpecifier();
      for (paramAnonymousParcel : paramAnonymousParcel.readArray(null)) {
        mNetworkSpecifiers.add((WifiAwareAgentNetworkSpecifier.ByteArrayWrapper)paramAnonymousParcel);
      }
      return localWifiAwareAgentNetworkSpecifier;
    }
    
    public WifiAwareAgentNetworkSpecifier[] newArray(int paramAnonymousInt)
    {
      return new WifiAwareAgentNetworkSpecifier[paramAnonymousInt];
    }
  };
  private static final String TAG = "WifiAwareAgentNs";
  private static final boolean VDBG = false;
  private MessageDigest mDigester;
  private Set<ByteArrayWrapper> mNetworkSpecifiers = new HashSet();
  
  public WifiAwareAgentNetworkSpecifier() {}
  
  public WifiAwareAgentNetworkSpecifier(WifiAwareNetworkSpecifier paramWifiAwareNetworkSpecifier)
  {
    initialize();
    mNetworkSpecifiers.add(convert(paramWifiAwareNetworkSpecifier));
  }
  
  public WifiAwareAgentNetworkSpecifier(WifiAwareNetworkSpecifier[] paramArrayOfWifiAwareNetworkSpecifier)
  {
    initialize();
    int i = paramArrayOfWifiAwareNetworkSpecifier.length;
    for (int j = 0; j < i; j++)
    {
      WifiAwareNetworkSpecifier localWifiAwareNetworkSpecifier = paramArrayOfWifiAwareNetworkSpecifier[j];
      mNetworkSpecifiers.add(convert(localWifiAwareNetworkSpecifier));
    }
  }
  
  private ByteArrayWrapper convert(WifiAwareNetworkSpecifier paramWifiAwareNetworkSpecifier)
  {
    if (mDigester == null) {
      return null;
    }
    Parcel localParcel = Parcel.obtain();
    paramWifiAwareNetworkSpecifier.writeToParcel(localParcel, 0);
    paramWifiAwareNetworkSpecifier = localParcel.marshall();
    mDigester.reset();
    mDigester.update(paramWifiAwareNetworkSpecifier);
    return new ByteArrayWrapper(mDigester.digest());
  }
  
  private void initialize()
  {
    try
    {
      mDigester = MessageDigest.getInstance("SHA-256");
      return;
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      Log.e("WifiAwareAgentNs", "Can not instantiate a SHA-256 digester!? Will match nothing.");
    }
  }
  
  public void assertValidFromUid(int paramInt)
  {
    throw new SecurityException("WifiAwareAgentNetworkSpecifier should not be used in network requests");
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == this) {
      return true;
    }
    if (!(paramObject instanceof WifiAwareAgentNetworkSpecifier)) {
      return false;
    }
    return mNetworkSpecifiers.equals(mNetworkSpecifiers);
  }
  
  public int hashCode()
  {
    return mNetworkSpecifiers.hashCode();
  }
  
  public boolean isEmpty()
  {
    return mNetworkSpecifiers.isEmpty();
  }
  
  public boolean satisfiedBy(NetworkSpecifier paramNetworkSpecifier)
  {
    if (!(paramNetworkSpecifier instanceof WifiAwareAgentNetworkSpecifier)) {
      return false;
    }
    paramNetworkSpecifier = (WifiAwareAgentNetworkSpecifier)paramNetworkSpecifier;
    Iterator localIterator = mNetworkSpecifiers.iterator();
    while (localIterator.hasNext())
    {
      ByteArrayWrapper localByteArrayWrapper = (ByteArrayWrapper)localIterator.next();
      if (!mNetworkSpecifiers.contains(localByteArrayWrapper)) {
        return false;
      }
    }
    return true;
  }
  
  public boolean satisfiesAwareNetworkSpecifier(WifiAwareNetworkSpecifier paramWifiAwareNetworkSpecifier)
  {
    paramWifiAwareNetworkSpecifier = convert(paramWifiAwareNetworkSpecifier);
    return mNetworkSpecifiers.contains(paramWifiAwareNetworkSpecifier);
  }
  
  public String toString()
  {
    StringJoiner localStringJoiner = new StringJoiner(",");
    Iterator localIterator = mNetworkSpecifiers.iterator();
    while (localIterator.hasNext()) {
      localStringJoiner.add(((ByteArrayWrapper)localIterator.next()).toString());
    }
    return localStringJoiner.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeArray(mNetworkSpecifiers.toArray());
  }
  
  private static class ByteArrayWrapper
    implements Parcelable
  {
    public static final Parcelable.Creator<ByteArrayWrapper> CREATOR = new Parcelable.Creator()
    {
      public WifiAwareAgentNetworkSpecifier.ByteArrayWrapper createFromParcel(Parcel paramAnonymousParcel)
      {
        return new WifiAwareAgentNetworkSpecifier.ByteArrayWrapper(paramAnonymousParcel.readBlob());
      }
      
      public WifiAwareAgentNetworkSpecifier.ByteArrayWrapper[] newArray(int paramAnonymousInt)
      {
        return new WifiAwareAgentNetworkSpecifier.ByteArrayWrapper[paramAnonymousInt];
      }
    };
    private byte[] mData;
    
    ByteArrayWrapper(byte[] paramArrayOfByte)
    {
      mData = paramArrayOfByte;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      if (paramObject == this) {
        return true;
      }
      if (!(paramObject instanceof ByteArrayWrapper)) {
        return false;
      }
      return Arrays.equals(mData, mData);
    }
    
    public int hashCode()
    {
      return Arrays.hashCode(mData);
    }
    
    public String toString()
    {
      return new String(HexEncoding.encode(mData));
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeBlob(mData);
    }
  }
}
