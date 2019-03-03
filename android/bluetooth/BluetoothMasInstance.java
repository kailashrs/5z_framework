package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class BluetoothMasInstance
  implements Parcelable
{
  public static final Parcelable.Creator<BluetoothMasInstance> CREATOR = new Parcelable.Creator()
  {
    public BluetoothMasInstance createFromParcel(Parcel paramAnonymousParcel)
    {
      return new BluetoothMasInstance(paramAnonymousParcel.readInt(), paramAnonymousParcel.readString(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt());
    }
    
    public BluetoothMasInstance[] newArray(int paramAnonymousInt)
    {
      return new BluetoothMasInstance[paramAnonymousInt];
    }
  };
  private final int mChannel;
  private final int mId;
  private final int mMsgTypes;
  private final String mName;
  
  public BluetoothMasInstance(int paramInt1, String paramString, int paramInt2, int paramInt3)
  {
    mId = paramInt1;
    mName = paramString;
    mChannel = paramInt2;
    mMsgTypes = paramInt3;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof BluetoothMasInstance;
    boolean bool2 = false;
    if (bool1)
    {
      if (mId == mId) {
        bool2 = true;
      }
      return bool2;
    }
    return false;
  }
  
  public int getChannel()
  {
    return mChannel;
  }
  
  public int getId()
  {
    return mId;
  }
  
  public int getMsgTypes()
  {
    return mMsgTypes;
  }
  
  public String getName()
  {
    return mName;
  }
  
  public int hashCode()
  {
    return mId + (mChannel << 8) + (mMsgTypes << 16);
  }
  
  public boolean msgSupported(int paramInt)
  {
    boolean bool;
    if ((mMsgTypes & paramInt) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(Integer.toString(mId));
    localStringBuilder.append(":");
    localStringBuilder.append(mName);
    localStringBuilder.append(":");
    localStringBuilder.append(mChannel);
    localStringBuilder.append(":");
    localStringBuilder.append(Integer.toHexString(mMsgTypes));
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mId);
    paramParcel.writeString(mName);
    paramParcel.writeInt(mChannel);
    paramParcel.writeInt(mMsgTypes);
  }
  
  public static final class MessageType
  {
    public static final int EMAIL = 1;
    public static final int MMS = 8;
    public static final int SMS_CDMA = 4;
    public static final int SMS_GSM = 2;
    
    public MessageType() {}
  }
}
