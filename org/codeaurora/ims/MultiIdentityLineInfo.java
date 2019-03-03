package org.codeaurora.ims;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class MultiIdentityLineInfo
  implements Parcelable
{
  public static final Parcelable.Creator<MultiIdentityLineInfo> CREATOR = new Parcelable.Creator()
  {
    public MultiIdentityLineInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new MultiIdentityLineInfo(paramAnonymousParcel);
    }
    
    public MultiIdentityLineInfo[] newArray(int paramAnonymousInt)
    {
      return new MultiIdentityLineInfo[paramAnonymousInt];
    }
  };
  public static final int LINE_STATUS_ACTIVE = 2;
  public static final int LINE_STATUS_INACTIVE = 1;
  public static final int LINE_STATUS_UNKNOWN = 0;
  public static final String LINE_TYPE = "lineType";
  public static final int LINE_TYPE_PRIMARY = 1;
  public static final int LINE_TYPE_SECONDARY = 2;
  public static final String ORIGINATING_NUMBER = "originatingNumber";
  public static final String TERMINATING_NUMBER = "terminatingNumber";
  public static final MultiIdentityLineInfo defaultLine = new MultiIdentityLineInfo("", 1);
  private int mLineStatus;
  private int mLineType;
  private String mMsisdn;
  
  public MultiIdentityLineInfo(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public MultiIdentityLineInfo(String paramString, int paramInt)
  {
    this(paramString, paramInt, 0);
  }
  
  public MultiIdentityLineInfo(String paramString, int paramInt1, int paramInt2)
  {
    mMsisdn = paramString;
    mLineType = paramInt1;
    mLineStatus = paramInt2;
  }
  
  public MultiIdentityLineInfo(MultiIdentityLineInfo paramMultiIdentityLineInfo)
  {
    mMsisdn = paramMultiIdentityLineInfo.getMsisdn();
    mLineType = paramMultiIdentityLineInfo.getLineType();
    mLineStatus = paramMultiIdentityLineInfo.getLineStatus();
  }
  
  public static MultiIdentityLineInfo getDefaultLine()
  {
    return defaultLine;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getLineStatus()
  {
    return mLineStatus;
  }
  
  public int getLineType()
  {
    return mLineType;
  }
  
  public String getMsisdn()
  {
    return mMsisdn;
  }
  
  public boolean isLineSecondary()
  {
    boolean bool;
    if (mLineType == 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    mMsisdn = paramParcel.readString();
    mLineType = paramParcel.readInt();
    mLineStatus = paramParcel.readInt();
  }
  
  public void setLineStatus(int paramInt)
  {
    mLineStatus = paramInt;
  }
  
  public void setLineType(int paramInt)
  {
    mLineType = paramInt;
  }
  
  public void setMsisdn(String paramString)
  {
    mMsisdn = paramString;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{MultiIdentity Line Info : msisdn = ");
    localStringBuilder.append(mMsisdn);
    localStringBuilder.append(" , line type = ");
    localStringBuilder.append(mLineType);
    localStringBuilder.append(" , line status = ");
    localStringBuilder.append(mLineStatus);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mMsisdn);
    paramParcel.writeInt(mLineType);
    paramParcel.writeInt(mLineStatus);
  }
}
