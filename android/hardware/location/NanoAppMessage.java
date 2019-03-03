package android.hardware.location;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public final class NanoAppMessage
  implements Parcelable
{
  public static final Parcelable.Creator<NanoAppMessage> CREATOR = new Parcelable.Creator()
  {
    public NanoAppMessage createFromParcel(Parcel paramAnonymousParcel)
    {
      return new NanoAppMessage(paramAnonymousParcel, null);
    }
    
    public NanoAppMessage[] newArray(int paramAnonymousInt)
    {
      return new NanoAppMessage[paramAnonymousInt];
    }
  };
  private static final int DEBUG_LOG_NUM_BYTES = 16;
  private boolean mIsBroadcasted;
  private byte[] mMessageBody;
  private int mMessageType;
  private long mNanoAppId;
  
  private NanoAppMessage(long paramLong, int paramInt, byte[] paramArrayOfByte, boolean paramBoolean)
  {
    mNanoAppId = paramLong;
    mMessageType = paramInt;
    mMessageBody = paramArrayOfByte;
    mIsBroadcasted = paramBoolean;
  }
  
  private NanoAppMessage(Parcel paramParcel)
  {
    mNanoAppId = paramParcel.readLong();
    int i = paramParcel.readInt();
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    mIsBroadcasted = bool;
    mMessageType = paramParcel.readInt();
    mMessageBody = new byte[paramParcel.readInt()];
    paramParcel.readByteArray(mMessageBody);
  }
  
  public static NanoAppMessage createMessageFromNanoApp(long paramLong, int paramInt, byte[] paramArrayOfByte, boolean paramBoolean)
  {
    return new NanoAppMessage(paramLong, paramInt, paramArrayOfByte, paramBoolean);
  }
  
  public static NanoAppMessage createMessageToNanoApp(long paramLong, int paramInt, byte[] paramArrayOfByte)
  {
    return new NanoAppMessage(paramLong, paramInt, paramArrayOfByte, false);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public byte[] getMessageBody()
  {
    return mMessageBody;
  }
  
  public int getMessageType()
  {
    return mMessageType;
  }
  
  public long getNanoAppId()
  {
    return mNanoAppId;
  }
  
  public boolean isBroadcastMessage()
  {
    return mIsBroadcasted;
  }
  
  public String toString()
  {
    int i = mMessageBody.length;
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("NanoAppMessage[type = ");
    ((StringBuilder)localObject1).append(mMessageType);
    ((StringBuilder)localObject1).append(", length = ");
    ((StringBuilder)localObject1).append(mMessageBody.length);
    ((StringBuilder)localObject1).append(" bytes, ");
    if (mIsBroadcasted) {
      localObject2 = "broadcast";
    } else {
      localObject2 = "unicast";
    }
    ((StringBuilder)localObject1).append((String)localObject2);
    ((StringBuilder)localObject1).append(", nanoapp = 0x");
    ((StringBuilder)localObject1).append(Long.toHexString(mNanoAppId));
    ((StringBuilder)localObject1).append("](");
    localObject1 = ((StringBuilder)localObject1).toString();
    Object localObject2 = localObject1;
    if (i > 0)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append("data = 0x");
      localObject2 = ((StringBuilder)localObject2).toString();
    }
    for (int j = 0; j < Math.min(i, 16); j++)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append((String)localObject2);
      ((StringBuilder)localObject1).append(Byte.toHexString(mMessageBody[j], true));
      localObject1 = ((StringBuilder)localObject1).toString();
      localObject2 = localObject1;
      if ((j + 1) % 4 == 0)
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append((String)localObject1);
        ((StringBuilder)localObject2).append(" ");
        localObject2 = ((StringBuilder)localObject2).toString();
      }
    }
    localObject1 = localObject2;
    if (i > 16)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append((String)localObject2);
      ((StringBuilder)localObject1).append("...");
      localObject1 = ((StringBuilder)localObject1).toString();
    }
    localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append((String)localObject1);
    ((StringBuilder)localObject2).append(")");
    return ((StringBuilder)localObject2).toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(mNanoAppId);
    paramParcel.writeInt(mIsBroadcasted);
    paramParcel.writeInt(mMessageType);
    paramParcel.writeInt(mMessageBody.length);
    paramParcel.writeByteArray(mMessageBody);
  }
}
