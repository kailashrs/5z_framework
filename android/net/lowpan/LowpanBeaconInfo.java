package android.net.lowpan;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.HexDump;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.TreeSet;

public class LowpanBeaconInfo
  implements Parcelable
{
  public static final Parcelable.Creator<LowpanBeaconInfo> CREATOR = new Parcelable.Creator()
  {
    public LowpanBeaconInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      LowpanBeaconInfo.Builder localBuilder = new LowpanBeaconInfo.Builder();
      localBuilder.setLowpanIdentity((LowpanIdentity)LowpanIdentity.CREATOR.createFromParcel(paramAnonymousParcel));
      localBuilder.setRssi(paramAnonymousParcel.readInt());
      localBuilder.setLqi(paramAnonymousParcel.readInt());
      localBuilder.setBeaconAddress(paramAnonymousParcel.createByteArray());
      for (int i = paramAnonymousParcel.readInt(); i > 0; i--) {
        localBuilder.setFlag(paramAnonymousParcel.readInt());
      }
      return localBuilder.build();
    }
    
    public LowpanBeaconInfo[] newArray(int paramAnonymousInt)
    {
      return new LowpanBeaconInfo[paramAnonymousInt];
    }
  };
  public static final int FLAG_CAN_ASSIST = 1;
  public static final int UNKNOWN_LQI = 0;
  public static final int UNKNOWN_RSSI = Integer.MAX_VALUE;
  private byte[] mBeaconAddress = null;
  private final TreeSet<Integer> mFlags = new TreeSet();
  private LowpanIdentity mIdentity;
  private int mLqi = 0;
  private int mRssi = Integer.MAX_VALUE;
  
  private LowpanBeaconInfo() {}
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof LowpanBeaconInfo;
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    paramObject = (LowpanBeaconInfo)paramObject;
    bool1 = bool2;
    if (mIdentity.equals(mIdentity))
    {
      bool1 = bool2;
      if (Arrays.equals(mBeaconAddress, mBeaconAddress))
      {
        bool1 = bool2;
        if (mRssi == mRssi)
        {
          bool1 = bool2;
          if (mLqi == mLqi)
          {
            bool1 = bool2;
            if (mFlags.equals(mFlags)) {
              bool1 = true;
            }
          }
        }
      }
    }
    return bool1;
  }
  
  public byte[] getBeaconAddress()
  {
    return (byte[])mBeaconAddress.clone();
  }
  
  public Collection<Integer> getFlags()
  {
    return (Collection)mFlags.clone();
  }
  
  public LowpanIdentity getLowpanIdentity()
  {
    return mIdentity;
  }
  
  public int getLqi()
  {
    return mLqi;
  }
  
  public int getRssi()
  {
    return mRssi;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { mIdentity, Integer.valueOf(mRssi), Integer.valueOf(mLqi), Integer.valueOf(Arrays.hashCode(mBeaconAddress)), mFlags });
  }
  
  public boolean isFlagSet(int paramInt)
  {
    return mFlags.contains(Integer.valueOf(paramInt));
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(mIdentity.toString());
    if (mRssi != Integer.MAX_VALUE)
    {
      localStringBuffer.append(", RSSI:");
      localStringBuffer.append(mRssi);
      localStringBuffer.append("dBm");
    }
    if (mLqi != 0)
    {
      localStringBuffer.append(", LQI:");
      localStringBuffer.append(mLqi);
    }
    if (mBeaconAddress.length > 0)
    {
      localStringBuffer.append(", BeaconAddress:");
      localStringBuffer.append(HexDump.toHexString(mBeaconAddress));
    }
    Iterator localIterator = mFlags.iterator();
    while (localIterator.hasNext())
    {
      Integer localInteger = (Integer)localIterator.next();
      if (localInteger.intValue() != 1)
      {
        localStringBuffer.append(", FLAG_");
        localStringBuffer.append(Integer.toHexString(localInteger.intValue()));
      }
      else
      {
        localStringBuffer.append(", CAN_ASSIST");
      }
    }
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    mIdentity.writeToParcel(paramParcel, paramInt);
    paramParcel.writeInt(mRssi);
    paramParcel.writeInt(mLqi);
    paramParcel.writeByteArray(mBeaconAddress);
    paramParcel.writeInt(mFlags.size());
    Iterator localIterator = mFlags.iterator();
    while (localIterator.hasNext()) {
      paramParcel.writeInt(((Integer)localIterator.next()).intValue());
    }
  }
  
  public static class Builder
  {
    final LowpanBeaconInfo mBeaconInfo = new LowpanBeaconInfo(null);
    final LowpanIdentity.Builder mIdentityBuilder = new LowpanIdentity.Builder();
    
    public Builder() {}
    
    public LowpanBeaconInfo build()
    {
      LowpanBeaconInfo.access$502(mBeaconInfo, mIdentityBuilder.build());
      if (mBeaconInfo.mBeaconAddress == null) {
        LowpanBeaconInfo.access$302(mBeaconInfo, new byte[0]);
      }
      return mBeaconInfo;
    }
    
    public Builder setBeaconAddress(byte[] paramArrayOfByte)
    {
      LowpanBeaconInfo localLowpanBeaconInfo = mBeaconInfo;
      if (paramArrayOfByte != null) {
        paramArrayOfByte = (byte[])paramArrayOfByte.clone();
      } else {
        paramArrayOfByte = null;
      }
      LowpanBeaconInfo.access$302(localLowpanBeaconInfo, paramArrayOfByte);
      return this;
    }
    
    public Builder setChannel(int paramInt)
    {
      mIdentityBuilder.setChannel(paramInt);
      return this;
    }
    
    public Builder setFlag(int paramInt)
    {
      mBeaconInfo.mFlags.add(Integer.valueOf(paramInt));
      return this;
    }
    
    public Builder setFlags(Collection<Integer> paramCollection)
    {
      mBeaconInfo.mFlags.addAll(paramCollection);
      return this;
    }
    
    public Builder setLowpanIdentity(LowpanIdentity paramLowpanIdentity)
    {
      mIdentityBuilder.setLowpanIdentity(paramLowpanIdentity);
      return this;
    }
    
    public Builder setLqi(int paramInt)
    {
      LowpanBeaconInfo.access$202(mBeaconInfo, paramInt);
      return this;
    }
    
    public Builder setName(String paramString)
    {
      mIdentityBuilder.setName(paramString);
      return this;
    }
    
    public Builder setPanid(int paramInt)
    {
      mIdentityBuilder.setPanid(paramInt);
      return this;
    }
    
    public Builder setRssi(int paramInt)
    {
      LowpanBeaconInfo.access$102(mBeaconInfo, paramInt);
      return this;
    }
    
    public Builder setType(String paramString)
    {
      mIdentityBuilder.setType(paramString);
      return this;
    }
    
    public Builder setXpanid(byte[] paramArrayOfByte)
    {
      mIdentityBuilder.setXpanid(paramArrayOfByte);
      return this;
    }
  }
}
