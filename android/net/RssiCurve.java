package android.net;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;
import java.util.Objects;

@SystemApi
public class RssiCurve
  implements Parcelable
{
  public static final Parcelable.Creator<RssiCurve> CREATOR = new Parcelable.Creator()
  {
    public RssiCurve createFromParcel(Parcel paramAnonymousParcel)
    {
      return new RssiCurve(paramAnonymousParcel, null);
    }
    
    public RssiCurve[] newArray(int paramAnonymousInt)
    {
      return new RssiCurve[paramAnonymousInt];
    }
  };
  private static final int DEFAULT_ACTIVE_NETWORK_RSSI_BOOST = 25;
  public final int activeNetworkRssiBoost;
  public final int bucketWidth;
  public final byte[] rssiBuckets;
  public final int start;
  
  public RssiCurve(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    this(paramInt1, paramInt2, paramArrayOfByte, 25);
  }
  
  public RssiCurve(int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
  {
    start = paramInt1;
    bucketWidth = paramInt2;
    if ((paramArrayOfByte != null) && (paramArrayOfByte.length != 0))
    {
      rssiBuckets = paramArrayOfByte;
      activeNetworkRssiBoost = paramInt3;
      return;
    }
    throw new IllegalArgumentException("rssiBuckets must be at least one element large.");
  }
  
  private RssiCurve(Parcel paramParcel)
  {
    start = paramParcel.readInt();
    bucketWidth = paramParcel.readInt();
    rssiBuckets = new byte[paramParcel.readInt()];
    paramParcel.readByteArray(rssiBuckets);
    activeNetworkRssiBoost = paramParcel.readInt();
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
      paramObject = (RssiCurve)paramObject;
      if ((start != start) || (bucketWidth != bucketWidth) || (!Arrays.equals(rssiBuckets, rssiBuckets)) || (activeNetworkRssiBoost != activeNetworkRssiBoost)) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(start), Integer.valueOf(bucketWidth), Integer.valueOf(activeNetworkRssiBoost) }) ^ Arrays.hashCode(rssiBuckets);
  }
  
  public byte lookupScore(int paramInt)
  {
    return lookupScore(paramInt, false);
  }
  
  public byte lookupScore(int paramInt, boolean paramBoolean)
  {
    int i = paramInt;
    if (paramBoolean) {
      i = paramInt + activeNetworkRssiBoost;
    }
    i = (i - start) / bucketWidth;
    if (i < 0)
    {
      paramInt = 0;
    }
    else
    {
      paramInt = i;
      if (i > rssiBuckets.length - 1) {
        paramInt = rssiBuckets.length - 1;
      }
    }
    return rssiBuckets[paramInt];
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("RssiCurve[start=");
    localStringBuilder.append(start);
    localStringBuilder.append(",bucketWidth=");
    localStringBuilder.append(bucketWidth);
    localStringBuilder.append(",activeNetworkRssiBoost=");
    localStringBuilder.append(activeNetworkRssiBoost);
    localStringBuilder.append(",buckets=");
    for (int i = 0; i < rssiBuckets.length; i++)
    {
      localStringBuilder.append(rssiBuckets[i]);
      if (i < rssiBuckets.length - 1) {
        localStringBuilder.append(",");
      }
    }
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(start);
    paramParcel.writeInt(bucketWidth);
    paramParcel.writeInt(rssiBuckets.length);
    paramParcel.writeByteArray(rssiBuckets);
    paramParcel.writeInt(activeNetworkRssiBoost);
  }
}
