package android.location;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.InvalidParameterException;

public final class GnssNavigationMessage
  implements Parcelable
{
  public static final Parcelable.Creator<GnssNavigationMessage> CREATOR = new Parcelable.Creator()
  {
    public GnssNavigationMessage createFromParcel(Parcel paramAnonymousParcel)
    {
      GnssNavigationMessage localGnssNavigationMessage = new GnssNavigationMessage();
      localGnssNavigationMessage.setType(paramAnonymousParcel.readInt());
      localGnssNavigationMessage.setSvid(paramAnonymousParcel.readInt());
      localGnssNavigationMessage.setMessageId(paramAnonymousParcel.readInt());
      localGnssNavigationMessage.setSubmessageId(paramAnonymousParcel.readInt());
      byte[] arrayOfByte = new byte[paramAnonymousParcel.readInt()];
      paramAnonymousParcel.readByteArray(arrayOfByte);
      localGnssNavigationMessage.setData(arrayOfByte);
      localGnssNavigationMessage.setStatus(paramAnonymousParcel.readInt());
      return localGnssNavigationMessage;
    }
    
    public GnssNavigationMessage[] newArray(int paramAnonymousInt)
    {
      return new GnssNavigationMessage[paramAnonymousInt];
    }
  };
  private static final byte[] EMPTY_ARRAY = new byte[0];
  public static final int STATUS_PARITY_PASSED = 1;
  public static final int STATUS_PARITY_REBUILT = 2;
  public static final int STATUS_UNKNOWN = 0;
  public static final int TYPE_BDS_D1 = 1281;
  public static final int TYPE_BDS_D2 = 1282;
  public static final int TYPE_GAL_F = 1538;
  public static final int TYPE_GAL_I = 1537;
  public static final int TYPE_GLO_L1CA = 769;
  public static final int TYPE_GPS_CNAV2 = 260;
  public static final int TYPE_GPS_L1CA = 257;
  public static final int TYPE_GPS_L2CNAV = 258;
  public static final int TYPE_GPS_L5CNAV = 259;
  public static final int TYPE_UNKNOWN = 0;
  private byte[] mData;
  private int mMessageId;
  private int mStatus;
  private int mSubmessageId;
  private int mSvid;
  private int mType;
  
  public GnssNavigationMessage()
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
    int i = mType;
    if (i != 0)
    {
      if (i != 769)
      {
        switch (i)
        {
        default: 
          switch (i)
          {
          default: 
            switch (i)
            {
            default: 
              StringBuilder localStringBuilder = new StringBuilder();
              localStringBuilder.append("<Invalid:");
              localStringBuilder.append(mType);
              localStringBuilder.append(">");
              return localStringBuilder.toString();
            case 1538: 
              return "Galileo F";
            }
            return "Galileo I";
          case 1282: 
            return "Beidou D2";
          }
          return "Beidou D1";
        case 260: 
          return "GPS CNAV2";
        case 259: 
          return "GPS L5-CNAV";
        case 258: 
          return "GPS L2-CNAV";
        }
        return "GPS L1 C/A";
      }
      return "Glonass L1 C/A";
    }
    return "Unknown";
  }
  
  private void initialize()
  {
    mType = 0;
    mSvid = 0;
    mMessageId = -1;
    mSubmessageId = -1;
    mData = EMPTY_ARRAY;
    mStatus = 0;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public byte[] getData()
  {
    return mData;
  }
  
  public int getMessageId()
  {
    return mMessageId;
  }
  
  public int getStatus()
  {
    return mStatus;
  }
  
  public int getSubmessageId()
  {
    return mSubmessageId;
  }
  
  public int getSvid()
  {
    return mSvid;
  }
  
  public int getType()
  {
    return mType;
  }
  
  public void reset()
  {
    initialize();
  }
  
  public void set(GnssNavigationMessage paramGnssNavigationMessage)
  {
    mType = mType;
    mSvid = mSvid;
    mMessageId = mMessageId;
    mSubmessageId = mSubmessageId;
    mData = mData;
    mStatus = mStatus;
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
  
  public void setMessageId(int paramInt)
  {
    mMessageId = paramInt;
  }
  
  public void setStatus(int paramInt)
  {
    mStatus = paramInt;
  }
  
  public void setSubmessageId(int paramInt)
  {
    mSubmessageId = paramInt;
  }
  
  public void setSvid(int paramInt)
  {
    mSvid = paramInt;
  }
  
  public void setType(int paramInt)
  {
    mType = paramInt;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("GnssNavigationMessage:\n");
    int i = 0;
    localStringBuilder.append(String.format("   %-15s = %s\n", new Object[] { "Type", getTypeString() }));
    localStringBuilder.append(String.format("   %-15s = %s\n", new Object[] { "Svid", Integer.valueOf(mSvid) }));
    localStringBuilder.append(String.format("   %-15s = %s\n", new Object[] { "Status", getStatusString() }));
    localStringBuilder.append(String.format("   %-15s = %s\n", new Object[] { "MessageId", Integer.valueOf(mMessageId) }));
    localStringBuilder.append(String.format("   %-15s = %s\n", new Object[] { "SubmessageId", Integer.valueOf(mSubmessageId) }));
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
    paramParcel.writeInt(mType);
    paramParcel.writeInt(mSvid);
    paramParcel.writeInt(mMessageId);
    paramParcel.writeInt(mSubmessageId);
    paramParcel.writeInt(mData.length);
    paramParcel.writeByteArray(mData);
    paramParcel.writeInt(mStatus);
  }
  
  public static abstract class Callback
  {
    public static final int STATUS_LOCATION_DISABLED = 2;
    public static final int STATUS_NOT_SUPPORTED = 0;
    public static final int STATUS_READY = 1;
    
    public Callback() {}
    
    public void onGnssNavigationMessageReceived(GnssNavigationMessage paramGnssNavigationMessage) {}
    
    public void onStatusChanged(int paramInt) {}
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface GnssNavigationMessageStatus {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface GnssNavigationMessageType {}
}
