package com.android.ims.internal.uce.common;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CapInfo
  implements Parcelable
{
  public static final Parcelable.Creator<CapInfo> CREATOR = new Parcelable.Creator()
  {
    public CapInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new CapInfo(paramAnonymousParcel, null);
    }
    
    public CapInfo[] newArray(int paramAnonymousInt)
    {
      return new CapInfo[paramAnonymousInt];
    }
  };
  private long mCapTimestamp = 0L;
  private boolean mCdViaPresenceSupported = false;
  private String[] mExts = new String[10];
  private boolean mFtHttpSupported = false;
  private boolean mFtSnFSupported = false;
  private boolean mFtSupported = false;
  private boolean mFtThumbSupported = false;
  private boolean mFullSnFGroupChatSupported = false;
  private boolean mGeoPullFtSupported = false;
  private boolean mGeoPullSupported = false;
  private boolean mGeoPushSupported = false;
  private boolean mImSupported = false;
  private boolean mIpVideoSupported = false;
  private boolean mIpVoiceSupported = false;
  private boolean mIsSupported = false;
  private boolean mRcsIpVideoCallSupported = false;
  private boolean mRcsIpVideoOnlyCallSupported = false;
  private boolean mRcsIpVoiceCallSupported = false;
  private boolean mSmSupported = false;
  private boolean mSpSupported = false;
  private boolean mVsDuringCSSupported = false;
  private boolean mVsSupported = false;
  
  public CapInfo() {}
  
  private CapInfo(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getCapTimestamp()
  {
    return mCapTimestamp;
  }
  
  public String[] getExts()
  {
    return mExts;
  }
  
  public boolean isCdViaPresenceSupported()
  {
    return mCdViaPresenceSupported;
  }
  
  public boolean isFtHttpSupported()
  {
    return mFtHttpSupported;
  }
  
  public boolean isFtSnFSupported()
  {
    return mFtSnFSupported;
  }
  
  public boolean isFtSupported()
  {
    return mFtSupported;
  }
  
  public boolean isFtThumbSupported()
  {
    return mFtThumbSupported;
  }
  
  public boolean isFullSnFGroupChatSupported()
  {
    return mFullSnFGroupChatSupported;
  }
  
  public boolean isGeoPullFtSupported()
  {
    return mGeoPullFtSupported;
  }
  
  public boolean isGeoPullSupported()
  {
    return mGeoPullSupported;
  }
  
  public boolean isGeoPushSupported()
  {
    return mGeoPushSupported;
  }
  
  public boolean isImSupported()
  {
    return mImSupported;
  }
  
  public boolean isIpVideoSupported()
  {
    return mIpVideoSupported;
  }
  
  public boolean isIpVoiceSupported()
  {
    return mIpVoiceSupported;
  }
  
  public boolean isIsSupported()
  {
    return mIsSupported;
  }
  
  public boolean isRcsIpVideoCallSupported()
  {
    return mRcsIpVideoCallSupported;
  }
  
  public boolean isRcsIpVideoOnlyCallSupported()
  {
    return mRcsIpVideoOnlyCallSupported;
  }
  
  public boolean isRcsIpVoiceCallSupported()
  {
    return mRcsIpVoiceCallSupported;
  }
  
  public boolean isSmSupported()
  {
    return mSmSupported;
  }
  
  public boolean isSpSupported()
  {
    return mSpSupported;
  }
  
  public boolean isVsDuringCSSupported()
  {
    return mVsDuringCSSupported;
  }
  
  public boolean isVsSupported()
  {
    return mVsSupported;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    boolean bool1 = true;
    if (i == 0) {
      bool2 = false;
    } else {
      bool2 = true;
    }
    mImSupported = bool2;
    if (paramParcel.readInt() == 0) {
      bool2 = false;
    } else {
      bool2 = true;
    }
    mFtSupported = bool2;
    if (paramParcel.readInt() == 0) {
      bool2 = false;
    } else {
      bool2 = true;
    }
    mFtThumbSupported = bool2;
    if (paramParcel.readInt() == 0) {
      bool2 = false;
    } else {
      bool2 = true;
    }
    mFtSnFSupported = bool2;
    if (paramParcel.readInt() == 0) {
      bool2 = false;
    } else {
      bool2 = true;
    }
    mFtHttpSupported = bool2;
    if (paramParcel.readInt() == 0) {
      bool2 = false;
    } else {
      bool2 = true;
    }
    mIsSupported = bool2;
    if (paramParcel.readInt() == 0) {
      bool2 = false;
    } else {
      bool2 = true;
    }
    mVsDuringCSSupported = bool2;
    if (paramParcel.readInt() == 0) {
      bool2 = false;
    } else {
      bool2 = true;
    }
    mVsSupported = bool2;
    if (paramParcel.readInt() == 0) {
      bool2 = false;
    } else {
      bool2 = true;
    }
    mSpSupported = bool2;
    if (paramParcel.readInt() == 0) {
      bool2 = false;
    } else {
      bool2 = true;
    }
    mCdViaPresenceSupported = bool2;
    if (paramParcel.readInt() == 0) {
      bool2 = false;
    } else {
      bool2 = true;
    }
    mIpVoiceSupported = bool2;
    if (paramParcel.readInt() == 0) {
      bool2 = false;
    } else {
      bool2 = true;
    }
    mIpVideoSupported = bool2;
    if (paramParcel.readInt() == 0) {
      bool2 = false;
    } else {
      bool2 = true;
    }
    mGeoPullFtSupported = bool2;
    if (paramParcel.readInt() == 0) {
      bool2 = false;
    } else {
      bool2 = true;
    }
    mGeoPullSupported = bool2;
    if (paramParcel.readInt() == 0) {
      bool2 = false;
    } else {
      bool2 = true;
    }
    mGeoPushSupported = bool2;
    if (paramParcel.readInt() == 0) {
      bool2 = false;
    } else {
      bool2 = true;
    }
    mSmSupported = bool2;
    if (paramParcel.readInt() == 0) {
      bool2 = false;
    } else {
      bool2 = true;
    }
    mFullSnFGroupChatSupported = bool2;
    if (paramParcel.readInt() == 0) {
      bool2 = false;
    } else {
      bool2 = true;
    }
    mRcsIpVoiceCallSupported = bool2;
    if (paramParcel.readInt() == 0) {
      bool2 = false;
    } else {
      bool2 = true;
    }
    mRcsIpVideoCallSupported = bool2;
    boolean bool2 = bool1;
    if (paramParcel.readInt() == 0) {
      bool2 = false;
    }
    mRcsIpVideoOnlyCallSupported = bool2;
    mExts = paramParcel.createStringArray();
    mCapTimestamp = paramParcel.readLong();
  }
  
  public void setCapTimestamp(long paramLong)
  {
    mCapTimestamp = paramLong;
  }
  
  public void setCdViaPresenceSupported(boolean paramBoolean)
  {
    mCdViaPresenceSupported = paramBoolean;
  }
  
  public void setExts(String[] paramArrayOfString)
  {
    mExts = paramArrayOfString;
  }
  
  public void setFtHttpSupported(boolean paramBoolean)
  {
    mFtHttpSupported = paramBoolean;
  }
  
  public void setFtSnFSupported(boolean paramBoolean)
  {
    mFtSnFSupported = paramBoolean;
  }
  
  public void setFtSupported(boolean paramBoolean)
  {
    mFtSupported = paramBoolean;
  }
  
  public void setFtThumbSupported(boolean paramBoolean)
  {
    mFtThumbSupported = paramBoolean;
  }
  
  public void setFullSnFGroupChatSupported(boolean paramBoolean)
  {
    mFullSnFGroupChatSupported = paramBoolean;
  }
  
  public void setGeoPullFtSupported(boolean paramBoolean)
  {
    mGeoPullFtSupported = paramBoolean;
  }
  
  public void setGeoPullSupported(boolean paramBoolean)
  {
    mGeoPullSupported = paramBoolean;
  }
  
  public void setGeoPushSupported(boolean paramBoolean)
  {
    mGeoPushSupported = paramBoolean;
  }
  
  public void setImSupported(boolean paramBoolean)
  {
    mImSupported = paramBoolean;
  }
  
  public void setIpVideoSupported(boolean paramBoolean)
  {
    mIpVideoSupported = paramBoolean;
  }
  
  public void setIpVoiceSupported(boolean paramBoolean)
  {
    mIpVoiceSupported = paramBoolean;
  }
  
  public void setIsSupported(boolean paramBoolean)
  {
    mIsSupported = paramBoolean;
  }
  
  public void setRcsIpVideoCallSupported(boolean paramBoolean)
  {
    mRcsIpVideoCallSupported = paramBoolean;
  }
  
  public void setRcsIpVideoOnlyCallSupported(boolean paramBoolean)
  {
    mRcsIpVideoOnlyCallSupported = paramBoolean;
  }
  
  public void setRcsIpVoiceCallSupported(boolean paramBoolean)
  {
    mRcsIpVoiceCallSupported = paramBoolean;
  }
  
  public void setSmSupported(boolean paramBoolean)
  {
    mSmSupported = paramBoolean;
  }
  
  public void setSpSupported(boolean paramBoolean)
  {
    mSpSupported = paramBoolean;
  }
  
  public void setVsDuringCSSupported(boolean paramBoolean)
  {
    mVsDuringCSSupported = paramBoolean;
  }
  
  public void setVsSupported(boolean paramBoolean)
  {
    mVsSupported = paramBoolean;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mImSupported);
    paramParcel.writeInt(mFtSupported);
    paramParcel.writeInt(mFtThumbSupported);
    paramParcel.writeInt(mFtSnFSupported);
    paramParcel.writeInt(mFtHttpSupported);
    paramParcel.writeInt(mIsSupported);
    paramParcel.writeInt(mVsDuringCSSupported);
    paramParcel.writeInt(mVsSupported);
    paramParcel.writeInt(mSpSupported);
    paramParcel.writeInt(mCdViaPresenceSupported);
    paramParcel.writeInt(mIpVoiceSupported);
    paramParcel.writeInt(mIpVideoSupported);
    paramParcel.writeInt(mGeoPullFtSupported);
    paramParcel.writeInt(mGeoPullSupported);
    paramParcel.writeInt(mGeoPushSupported);
    paramParcel.writeInt(mSmSupported);
    paramParcel.writeInt(mFullSnFGroupChatSupported);
    paramParcel.writeInt(mRcsIpVoiceCallSupported);
    paramParcel.writeInt(mRcsIpVideoCallSupported);
    paramParcel.writeInt(mRcsIpVideoOnlyCallSupported);
    paramParcel.writeStringArray(mExts);
    paramParcel.writeLong(mCapTimestamp);
  }
}
