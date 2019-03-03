package android.hardware.location;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

@SystemApi
@Deprecated
public class ContextHubMessage
  implements Parcelable
{
  public static final Parcelable.Creator<ContextHubMessage> CREATOR = new Parcelable.Creator()
  {
    public ContextHubMessage createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ContextHubMessage(paramAnonymousParcel, null);
    }
    
    public ContextHubMessage[] newArray(int paramAnonymousInt)
    {
      return new ContextHubMessage[paramAnonymousInt];
    }
  };
  private static final int DEBUG_LOG_NUM_BYTES = 16;
  private byte[] mData;
  private int mType;
  private int mVersion;
  
  public ContextHubMessage(int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    mType = paramInt1;
    mVersion = paramInt2;
    mData = Arrays.copyOf(paramArrayOfByte, paramArrayOfByte.length);
  }
  
  private ContextHubMessage(Parcel paramParcel)
  {
    mType = paramParcel.readInt();
    mVersion = paramParcel.readInt();
    mData = new byte[paramParcel.readInt()];
    paramParcel.readByteArray(mData);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public byte[] getData()
  {
    return Arrays.copyOf(mData, mData.length);
  }
  
  public int getMsgType()
  {
    return mType;
  }
  
  public int getVersion()
  {
    return mVersion;
  }
  
  public void setMsgData(byte[] paramArrayOfByte)
  {
    mData = Arrays.copyOf(paramArrayOfByte, paramArrayOfByte.length);
  }
  
  public void setMsgType(int paramInt)
  {
    mType = paramInt;
  }
  
  public void setVersion(int paramInt)
  {
    mVersion = paramInt;
  }
  
  public String toString()
  {
    int i = mData.length;
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("ContextHubMessage[type = ");
    ((StringBuilder)localObject1).append(mType);
    ((StringBuilder)localObject1).append(", length = ");
    ((StringBuilder)localObject1).append(mData.length);
    ((StringBuilder)localObject1).append(" bytes](");
    Object localObject2 = ((StringBuilder)localObject1).toString();
    localObject1 = localObject2;
    if (i > 0)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append((String)localObject2);
      ((StringBuilder)localObject1).append("data = 0x");
      localObject1 = ((StringBuilder)localObject1).toString();
    }
    for (int j = 0; j < Math.min(i, 16); j++)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append(Byte.toHexString(mData[j], true));
      localObject2 = ((StringBuilder)localObject2).toString();
      localObject1 = localObject2;
      if ((j + 1) % 4 == 0)
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append((String)localObject2);
        ((StringBuilder)localObject1).append(" ");
        localObject1 = ((StringBuilder)localObject1).toString();
      }
    }
    localObject2 = localObject1;
    if (i > 16)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append("...");
      localObject2 = ((StringBuilder)localObject2).toString();
    }
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append((String)localObject2);
    ((StringBuilder)localObject1).append(")");
    return ((StringBuilder)localObject1).toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mType);
    paramParcel.writeInt(mVersion);
    paramParcel.writeInt(mData.length);
    paramParcel.writeByteArray(mData);
  }
}
