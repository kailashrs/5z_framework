package android.net.lowpan;

import android.icu.text.StringPrep;
import android.icu.text.StringPrepParseException;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.android.internal.util.HexDump;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

public class LowpanIdentity
  implements Parcelable
{
  public static final Parcelable.Creator<LowpanIdentity> CREATOR = new Parcelable.Creator()
  {
    public LowpanIdentity createFromParcel(Parcel paramAnonymousParcel)
    {
      LowpanIdentity.Builder localBuilder = new LowpanIdentity.Builder();
      localBuilder.setRawName(paramAnonymousParcel.createByteArray());
      localBuilder.setType(paramAnonymousParcel.readString());
      localBuilder.setXpanid(paramAnonymousParcel.createByteArray());
      localBuilder.setPanid(paramAnonymousParcel.readInt());
      localBuilder.setChannel(paramAnonymousParcel.readInt());
      return localBuilder.build();
    }
    
    public LowpanIdentity[] newArray(int paramAnonymousInt)
    {
      return new LowpanIdentity[paramAnonymousInt];
    }
  };
  private static final String TAG = LowpanIdentity.class.getSimpleName();
  public static final int UNSPECIFIED_CHANNEL = -1;
  public static final int UNSPECIFIED_PANID = -1;
  private int mChannel = -1;
  private boolean mIsNameValid = true;
  private String mName = "";
  private int mPanid = -1;
  private byte[] mRawName = new byte[0];
  private String mType = "";
  private byte[] mXpanid = new byte[0];
  
  LowpanIdentity() {}
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof LowpanIdentity;
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    paramObject = (LowpanIdentity)paramObject;
    bool1 = bool2;
    if (Arrays.equals(mRawName, mRawName))
    {
      bool1 = bool2;
      if (Arrays.equals(mXpanid, mXpanid))
      {
        bool1 = bool2;
        if (mType.equals(mType))
        {
          bool1 = bool2;
          if (mPanid == mPanid)
          {
            bool1 = bool2;
            if (mChannel == mChannel) {
              bool1 = true;
            }
          }
        }
      }
    }
    return bool1;
  }
  
  public int getChannel()
  {
    return mChannel;
  }
  
  public String getName()
  {
    return mName;
  }
  
  public int getPanid()
  {
    return mPanid;
  }
  
  public byte[] getRawName()
  {
    return (byte[])mRawName.clone();
  }
  
  public String getType()
  {
    return mType;
  }
  
  public byte[] getXpanid()
  {
    return (byte[])mXpanid.clone();
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(Arrays.hashCode(mRawName)), mType, Integer.valueOf(Arrays.hashCode(mXpanid)), Integer.valueOf(mPanid), Integer.valueOf(mChannel) });
  }
  
  public boolean isNameValid()
  {
    return mIsNameValid;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("Name:");
    localStringBuffer.append(getName());
    if (mType.length() > 0)
    {
      localStringBuffer.append(", Type:");
      localStringBuffer.append(mType);
    }
    if (mXpanid.length > 0)
    {
      localStringBuffer.append(", XPANID:");
      localStringBuffer.append(HexDump.toHexString(mXpanid));
    }
    if (mPanid != -1)
    {
      localStringBuffer.append(", PANID:");
      localStringBuffer.append(String.format("0x%04X", new Object[] { Integer.valueOf(mPanid) }));
    }
    if (mChannel != -1)
    {
      localStringBuffer.append(", Channel:");
      localStringBuffer.append(mChannel);
    }
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeByteArray(mRawName);
    paramParcel.writeString(mType);
    paramParcel.writeByteArray(mXpanid);
    paramParcel.writeInt(mPanid);
    paramParcel.writeInt(mChannel);
  }
  
  public static class Builder
  {
    private static final StringPrep stringPrep = StringPrep.getInstance(8);
    final LowpanIdentity mIdentity = new LowpanIdentity();
    
    public Builder() {}
    
    private static String escape(byte[] paramArrayOfByte)
    {
      StringBuffer localStringBuffer = new StringBuffer();
      int i = paramArrayOfByte.length;
      for (int j = 0; j < i; j++)
      {
        int k = paramArrayOfByte[j];
        if ((k >= 32) && (k <= 126)) {
          localStringBuffer.append((char)k);
        } else {
          localStringBuffer.append(String.format("\\0x%02x", new Object[] { Integer.valueOf(k & 0xFF) }));
        }
      }
      return localStringBuffer.toString();
    }
    
    public LowpanIdentity build()
    {
      return mIdentity;
    }
    
    public Builder setChannel(int paramInt)
    {
      LowpanIdentity.access$702(mIdentity, paramInt);
      return this;
    }
    
    public Builder setLowpanIdentity(LowpanIdentity paramLowpanIdentity)
    {
      Objects.requireNonNull(paramLowpanIdentity);
      setRawName(paramLowpanIdentity.getRawName());
      setXpanid(paramLowpanIdentity.getXpanid());
      setPanid(paramLowpanIdentity.getPanid());
      setChannel(paramLowpanIdentity.getChannel());
      setType(paramLowpanIdentity.getType());
      return this;
    }
    
    public Builder setName(String paramString)
    {
      Objects.requireNonNull(paramString);
      try
      {
        LowpanIdentity.access$002(mIdentity, stringPrep.prepare(paramString, 0));
        LowpanIdentity.access$102(mIdentity, mIdentity.mName.getBytes(StandardCharsets.UTF_8));
        LowpanIdentity.access$202(mIdentity, true);
      }
      catch (StringPrepParseException localStringPrepParseException)
      {
        Log.w(LowpanIdentity.TAG, localStringPrepParseException.toString());
        setRawName(paramString.getBytes(StandardCharsets.UTF_8));
      }
      return this;
    }
    
    public Builder setPanid(int paramInt)
    {
      LowpanIdentity.access$502(mIdentity, paramInt);
      return this;
    }
    
    public Builder setRawName(byte[] paramArrayOfByte)
    {
      Objects.requireNonNull(paramArrayOfByte);
      LowpanIdentity.access$102(mIdentity, (byte[])paramArrayOfByte.clone());
      LowpanIdentity.access$002(mIdentity, new String(paramArrayOfByte, StandardCharsets.UTF_8));
      try
      {
        String str = stringPrep.prepare(mIdentity.mName, 0);
        LowpanIdentity.access$202(mIdentity, Arrays.equals(str.getBytes(StandardCharsets.UTF_8), paramArrayOfByte));
      }
      catch (StringPrepParseException localStringPrepParseException)
      {
        Log.w(LowpanIdentity.TAG, localStringPrepParseException.toString());
        LowpanIdentity.access$202(mIdentity, false);
      }
      if (!mIdentity.mIsNameValid)
      {
        LowpanIdentity localLowpanIdentity = mIdentity;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("«");
        localStringBuilder.append(escape(paramArrayOfByte));
        localStringBuilder.append("»");
        LowpanIdentity.access$002(localLowpanIdentity, localStringBuilder.toString());
      }
      return this;
    }
    
    public Builder setType(String paramString)
    {
      LowpanIdentity.access$602(mIdentity, paramString);
      return this;
    }
    
    public Builder setXpanid(byte[] paramArrayOfByte)
    {
      LowpanIdentity localLowpanIdentity = mIdentity;
      if (paramArrayOfByte != null) {
        paramArrayOfByte = (byte[])paramArrayOfByte.clone();
      } else {
        paramArrayOfByte = null;
      }
      LowpanIdentity.access$402(localLowpanIdentity, paramArrayOfByte);
      return this;
    }
  }
}
