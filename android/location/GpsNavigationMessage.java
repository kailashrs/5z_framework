package android.location;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.security.InvalidParameterException;

@SystemApi
public class GpsNavigationMessage
  implements Parcelable
{
  public static final Parcelable.Creator<GpsNavigationMessage> CREATOR = new Parcelable.Creator()
  {
    public GpsNavigationMessage createFromParcel(Parcel paramAnonymousParcel)
    {
      GpsNavigationMessage localGpsNavigationMessage = new GpsNavigationMessage();
      localGpsNavigationMessage.setType(paramAnonymousParcel.readByte());
      localGpsNavigationMessage.setPrn(paramAnonymousParcel.readByte());
      localGpsNavigationMessage.setMessageId((short)paramAnonymousParcel.readInt());
      localGpsNavigationMessage.setSubmessageId((short)paramAnonymousParcel.readInt());
      byte[] arrayOfByte = new byte[paramAnonymousParcel.readInt()];
      paramAnonymousParcel.readByteArray(arrayOfByte);
      localGpsNavigationMessage.setData(arrayOfByte);
      if (paramAnonymousParcel.dataAvail() >= 32) {
        localGpsNavigationMessage.setStatus((short)paramAnonymousParcel.readInt());
      } else {
        localGpsNavigationMessage.setStatus((short)0);
      }
      return localGpsNavigationMessage;
    }
    
    public GpsNavigationMessage[] newArray(int paramAnonymousInt)
    {
      return new GpsNavigationMessage[paramAnonymousInt];
    }
  };
  private static final byte[] EMPTY_ARRAY = new byte[0];
  public static final short STATUS_PARITY_PASSED = 1;
  public static final short STATUS_PARITY_REBUILT = 2;
  public static final short STATUS_UNKNOWN = 0;
  public static final byte TYPE_CNAV2 = 4;
  public static final byte TYPE_L1CA = 1;
  public static final byte TYPE_L2CNAV = 2;
  public static final byte TYPE_L5CNAV = 3;
  public static final byte TYPE_UNKNOWN = 0;
  private byte[] mData;
  private short mMessageId;
  private byte mPrn;
  private short mStatus;
  private short mSubmessageId;
  private byte mType;
  
  GpsNavigationMessage()
  {
    initialize();
  }
  
  private String getStatusString()
  {
    switch (mStatus)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<Invalid:");
      localStringBuilder.append(mStatus);
      localStringBuilder.append(">");
      return localStringBuilder.toString();
    case 2: 
      return "ParityRebuilt";
    case 1: 
      return "ParityPassed";
    }
    return "Unknown";
  }
  
  private String getTypeString()
  {
    switch (mType)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<Invalid:");
      localStringBuilder.append(mType);
      localStringBuilder.append(">");
      return localStringBuilder.toString();
    case 4: 
      return "CNAV-2";
    case 3: 
      return "L5-CNAV";
    case 2: 
      return "L2-CNAV";
    case 1: 
      return "L1 C/A";
    }
    return "Unknown";
  }
  
  private void initialize()
  {
    mType = ((byte)0);
    mPrn = ((byte)0);
    mMessageId = ((short)-1);
    mSubmessageId = ((short)-1);
    mData = EMPTY_ARRAY;
    mStatus = ((short)0);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public byte[] getData()
  {
    return mData;
  }
  
  public short getMessageId()
  {
    return mMessageId;
  }
  
  public byte getPrn()
  {
    return mPrn;
  }
  
  public short getStatus()
  {
    return mStatus;
  }
  
  public short getSubmessageId()
  {
    return mSubmessageId;
  }
  
  public byte getType()
  {
    return mType;
  }
  
  public void reset()
  {
    initialize();
  }
  
  public void set(GpsNavigationMessage paramGpsNavigationMessage)
  {
    mType = ((byte)mType);
    mPrn = ((byte)mPrn);
    mMessageId = ((short)mMessageId);
    mSubmessageId = ((short)mSubmessageId);
    mData = mData;
    mStatus = ((short)mStatus);
  }
  
  public void setData(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte != null)
    {
      mData = paramArrayOfByte;
      return;
    }
    throw new InvalidParameterException("Data must be a non-null array");
  }
  
  public void setMessageId(short paramShort)
  {
    mMessageId = ((short)paramShort);
  }
  
  public void setPrn(byte paramByte)
  {
    mPrn = ((byte)paramByte);
  }
  
  public void setStatus(short paramShort)
  {
    mStatus = ((short)paramShort);
  }
  
  public void setSubmessageId(short paramShort)
  {
    mSubmessageId = ((short)paramShort);
  }
  
  public void setType(byte paramByte)
  {
    mType = ((byte)paramByte);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("GpsNavigationMessage:\n");
    int i = 0;
    localStringBuilder.append(String.format("   %-15s = %s\n", new Object[] { "Type", getTypeString() }));
    localStringBuilder.append(String.format("   %-15s = %s\n", new Object[] { "Prn", Byte.valueOf(mPrn) }));
    localStringBuilder.append(String.format("   %-15s = %s\n", new Object[] { "Status", getStatusString() }));
    localStringBuilder.append(String.format("   %-15s = %s\n", new Object[] { "MessageId", Short.valueOf(mMessageId) }));
    localStringBuilder.append(String.format("   %-15s = %s\n", new Object[] { "SubmessageId", Short.valueOf(mSubmessageId) }));
    localStringBuilder.append(String.format("   %-15s = %s\n", new Object[] { "Data", "{" }));
    String str = "        ";
    byte[] arrayOfByte = mData;
    int j = arrayOfByte.length;
    while (i < j)
    {
      int k = arrayOfByte[i];
      localStringBuilder.append(str);
      localStringBuilder.append(k);
      str = ", ";
      i++;
    }
    localStringBuilder.append(" }");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeByte(mType);
    paramParcel.writeByte(mPrn);
    paramParcel.writeInt(mMessageId);
    paramParcel.writeInt(mSubmessageId);
    paramParcel.writeInt(mData.length);
    paramParcel.writeByteArray(mData);
    paramParcel.writeInt(mStatus);
  }
}
