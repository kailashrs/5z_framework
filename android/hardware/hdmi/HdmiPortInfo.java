package android.hardware.hdmi;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public final class HdmiPortInfo
  implements Parcelable
{
  public static final Parcelable.Creator<HdmiPortInfo> CREATOR = new Parcelable.Creator()
  {
    public HdmiPortInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      int j = paramAnonymousParcel.readInt();
      int k = paramAnonymousParcel.readInt();
      boolean bool1;
      if (paramAnonymousParcel.readInt() == 1) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      boolean bool2;
      if (paramAnonymousParcel.readInt() == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      boolean bool3;
      if (paramAnonymousParcel.readInt() == 1) {
        bool3 = true;
      } else {
        bool3 = false;
      }
      return new HdmiPortInfo(i, j, k, bool1, bool3, bool2);
    }
    
    public HdmiPortInfo[] newArray(int paramAnonymousInt)
    {
      return new HdmiPortInfo[paramAnonymousInt];
    }
  };
  public static final int PORT_INPUT = 0;
  public static final int PORT_OUTPUT = 1;
  private final int mAddress;
  private final boolean mArcSupported;
  private final boolean mCecSupported;
  private final int mId;
  private final boolean mMhlSupported;
  private final int mType;
  
  public HdmiPortInfo(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    mId = paramInt1;
    mType = paramInt2;
    mAddress = paramInt3;
    mCecSupported = paramBoolean1;
    mArcSupported = paramBoolean3;
    mMhlSupported = paramBoolean2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof HdmiPortInfo;
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    paramObject = (HdmiPortInfo)paramObject;
    bool1 = bool2;
    if (mId == mId)
    {
      bool1 = bool2;
      if (mType == mType)
      {
        bool1 = bool2;
        if (mAddress == mAddress)
        {
          bool1 = bool2;
          if (mCecSupported == mCecSupported)
          {
            bool1 = bool2;
            if (mArcSupported == mArcSupported)
            {
              bool1 = bool2;
              if (mMhlSupported == mMhlSupported) {
                bool1 = true;
              }
            }
          }
        }
      }
    }
    return bool1;
  }
  
  public int getAddress()
  {
    return mAddress;
  }
  
  public int getId()
  {
    return mId;
  }
  
  public int getType()
  {
    return mType;
  }
  
  public boolean isArcSupported()
  {
    return mArcSupported;
  }
  
  public boolean isCecSupported()
  {
    return mCecSupported;
  }
  
  public boolean isMhlSupported()
  {
    return mMhlSupported;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("port_id: ");
    localStringBuffer.append(mId);
    localStringBuffer.append(", ");
    localStringBuffer.append("address: ");
    localStringBuffer.append(String.format("0x%04x", new Object[] { Integer.valueOf(mAddress) }));
    localStringBuffer.append(", ");
    localStringBuffer.append("cec: ");
    localStringBuffer.append(mCecSupported);
    localStringBuffer.append(", ");
    localStringBuffer.append("arc: ");
    localStringBuffer.append(mArcSupported);
    localStringBuffer.append(", ");
    localStringBuffer.append("mhl: ");
    localStringBuffer.append(mMhlSupported);
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mId);
    paramParcel.writeInt(mType);
    paramParcel.writeInt(mAddress);
    paramParcel.writeInt(mCecSupported);
    paramParcel.writeInt(mArcSupported);
    paramParcel.writeInt(mMhlSupported);
  }
}
