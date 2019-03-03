package android.net.wifi.rtt;

import android.annotation.SystemApi;
import android.net.MacAddress;
import android.net.wifi.aware.PeerHandle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Objects;

public final class RangingResult
  implements Parcelable
{
  public static final Parcelable.Creator<RangingResult> CREATOR = new Parcelable.Creator()
  {
    public RangingResult createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      boolean bool = paramAnonymousParcel.readBoolean();
      MacAddress localMacAddress = null;
      if (bool) {
        localMacAddress = (MacAddress)MacAddress.CREATOR.createFromParcel(paramAnonymousParcel);
      }
      bool = paramAnonymousParcel.readBoolean();
      PeerHandle localPeerHandle = null;
      if (bool) {
        localPeerHandle = new PeerHandle(paramAnonymousParcel.readInt());
      }
      int j = paramAnonymousParcel.readInt();
      int k = paramAnonymousParcel.readInt();
      int m = paramAnonymousParcel.readInt();
      int n = paramAnonymousParcel.readInt();
      int i1 = paramAnonymousParcel.readInt();
      byte[] arrayOfByte1 = paramAnonymousParcel.createByteArray();
      byte[] arrayOfByte2 = paramAnonymousParcel.createByteArray();
      long l = paramAnonymousParcel.readLong();
      if (bool) {
        return new RangingResult(i, localPeerHandle, j, k, m, n, i1, arrayOfByte1, arrayOfByte2, l);
      }
      return new RangingResult(i, localMacAddress, j, k, m, n, i1, arrayOfByte1, arrayOfByte2, l);
    }
    
    public RangingResult[] newArray(int paramAnonymousInt)
    {
      return new RangingResult[paramAnonymousInt];
    }
  };
  private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
  public static final int STATUS_FAIL = 1;
  public static final int STATUS_RESPONDER_DOES_NOT_SUPPORT_IEEE80211MC = 2;
  public static final int STATUS_SUCCESS = 0;
  private static final String TAG = "RangingResult";
  private final int mDistanceMm;
  private final int mDistanceStdDevMm;
  private final byte[] mLci;
  private final byte[] mLcr;
  private final MacAddress mMac;
  private final int mNumAttemptedMeasurements;
  private final int mNumSuccessfulMeasurements;
  private final PeerHandle mPeerHandle;
  private final int mRssi;
  private final int mStatus;
  private final long mTimestamp;
  
  public RangingResult(int paramInt1, MacAddress paramMacAddress, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long paramLong)
  {
    mStatus = paramInt1;
    mMac = paramMacAddress;
    mPeerHandle = null;
    mDistanceMm = paramInt2;
    mDistanceStdDevMm = paramInt3;
    mRssi = paramInt4;
    mNumAttemptedMeasurements = paramInt5;
    mNumSuccessfulMeasurements = paramInt6;
    if (paramArrayOfByte1 == null) {
      paramArrayOfByte1 = EMPTY_BYTE_ARRAY;
    }
    mLci = paramArrayOfByte1;
    if (paramArrayOfByte2 == null) {
      paramArrayOfByte2 = EMPTY_BYTE_ARRAY;
    }
    mLcr = paramArrayOfByte2;
    mTimestamp = paramLong;
  }
  
  public RangingResult(int paramInt1, PeerHandle paramPeerHandle, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, long paramLong)
  {
    mStatus = paramInt1;
    mMac = null;
    mPeerHandle = paramPeerHandle;
    mDistanceMm = paramInt2;
    mDistanceStdDevMm = paramInt3;
    mRssi = paramInt4;
    mNumAttemptedMeasurements = paramInt5;
    mNumSuccessfulMeasurements = paramInt6;
    if (paramArrayOfByte1 == null) {
      paramPeerHandle = EMPTY_BYTE_ARRAY;
    } else {
      paramPeerHandle = paramArrayOfByte1;
    }
    mLci = paramPeerHandle;
    if (paramArrayOfByte2 == null) {
      paramPeerHandle = EMPTY_BYTE_ARRAY;
    } else {
      paramPeerHandle = paramArrayOfByte2;
    }
    mLcr = paramPeerHandle;
    mTimestamp = paramLong;
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
    if (!(paramObject instanceof RangingResult)) {
      return false;
    }
    paramObject = (RangingResult)paramObject;
    if ((mStatus != mStatus) || (!Objects.equals(mMac, mMac)) || (!Objects.equals(mPeerHandle, mPeerHandle)) || (mDistanceMm != mDistanceMm) || (mDistanceStdDevMm != mDistanceStdDevMm) || (mRssi != mRssi) || (mNumAttemptedMeasurements != mNumAttemptedMeasurements) || (mNumSuccessfulMeasurements != mNumSuccessfulMeasurements) || (!Arrays.equals(mLci, mLci)) || (!Arrays.equals(mLcr, mLcr)) || (mTimestamp != mTimestamp)) {
      bool = false;
    }
    return bool;
  }
  
  public int getDistanceMm()
  {
    if (mStatus == 0) {
      return mDistanceMm;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("getDistanceMm(): invoked on an invalid result: getStatus()=");
    localStringBuilder.append(mStatus);
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  public int getDistanceStdDevMm()
  {
    if (mStatus == 0) {
      return mDistanceStdDevMm;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("getDistanceStdDevMm(): invoked on an invalid result: getStatus()=");
    localStringBuilder.append(mStatus);
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  @SystemApi
  public byte[] getLci()
  {
    if (mStatus == 0) {
      return mLci;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("getLci(): invoked on an invalid result: getStatus()=");
    localStringBuilder.append(mStatus);
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  @SystemApi
  public byte[] getLcr()
  {
    if (mStatus == 0) {
      return mLcr;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("getReportedLocationCivic(): invoked on an invalid result: getStatus()=");
    localStringBuilder.append(mStatus);
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  public MacAddress getMacAddress()
  {
    return mMac;
  }
  
  public int getNumAttemptedMeasurements()
  {
    if (mStatus == 0) {
      return mNumAttemptedMeasurements;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("getNumAttemptedMeasurements(): invoked on an invalid result: getStatus()=");
    localStringBuilder.append(mStatus);
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  public int getNumSuccessfulMeasurements()
  {
    if (mStatus == 0) {
      return mNumSuccessfulMeasurements;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("getNumSuccessfulMeasurements(): invoked on an invalid result: getStatus()=");
    localStringBuilder.append(mStatus);
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  public PeerHandle getPeerHandle()
  {
    return mPeerHandle;
  }
  
  public long getRangingTimestampMillis()
  {
    if (mStatus == 0) {
      return mTimestamp;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("getRangingTimestampMillis(): invoked on an invalid result: getStatus()=");
    localStringBuilder.append(mStatus);
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  public int getRssi()
  {
    if (mStatus == 0) {
      return mRssi;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("getRssi(): invoked on an invalid result: getStatus()=");
    localStringBuilder.append(mStatus);
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  public int getStatus()
  {
    return mStatus;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(mStatus), mMac, mPeerHandle, Integer.valueOf(mDistanceMm), Integer.valueOf(mDistanceStdDevMm), Integer.valueOf(mRssi), Integer.valueOf(mNumAttemptedMeasurements), Integer.valueOf(mNumSuccessfulMeasurements), mLci, mLcr, Long.valueOf(mTimestamp) });
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("RangingResult: [status=");
    localStringBuilder.append(mStatus);
    localStringBuilder.append(", mac=");
    localStringBuilder.append(mMac);
    localStringBuilder.append(", peerHandle=");
    Object localObject;
    if (mPeerHandle == null) {
      localObject = "<null>";
    } else {
      localObject = Integer.valueOf(mPeerHandle.peerId);
    }
    localStringBuilder.append(localObject);
    localStringBuilder.append(", distanceMm=");
    localStringBuilder.append(mDistanceMm);
    localStringBuilder.append(", distanceStdDevMm=");
    localStringBuilder.append(mDistanceStdDevMm);
    localStringBuilder.append(", rssi=");
    localStringBuilder.append(mRssi);
    localStringBuilder.append(", numAttemptedMeasurements=");
    localStringBuilder.append(mNumAttemptedMeasurements);
    localStringBuilder.append(", numSuccessfulMeasurements=");
    localStringBuilder.append(mNumSuccessfulMeasurements);
    localStringBuilder.append(", lci=");
    localStringBuilder.append(mLci);
    localStringBuilder.append(", lcr=");
    localStringBuilder.append(mLcr);
    localStringBuilder.append(", timestamp=");
    localStringBuilder.append(mTimestamp);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mStatus);
    if (mMac == null)
    {
      paramParcel.writeBoolean(false);
    }
    else
    {
      paramParcel.writeBoolean(true);
      mMac.writeToParcel(paramParcel, paramInt);
    }
    if (mPeerHandle == null)
    {
      paramParcel.writeBoolean(false);
    }
    else
    {
      paramParcel.writeBoolean(true);
      paramParcel.writeInt(mPeerHandle.peerId);
    }
    paramParcel.writeInt(mDistanceMm);
    paramParcel.writeInt(mDistanceStdDevMm);
    paramParcel.writeInt(mRssi);
    paramParcel.writeInt(mNumAttemptedMeasurements);
    paramParcel.writeInt(mNumSuccessfulMeasurements);
    paramParcel.writeByteArray(mLci);
    paramParcel.writeByteArray(mLcr);
    paramParcel.writeLong(mTimestamp);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface RangeResultStatus {}
}
