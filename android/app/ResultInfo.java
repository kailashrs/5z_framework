package android.app;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public class ResultInfo
  implements Parcelable
{
  public static final Parcelable.Creator<ResultInfo> CREATOR = new Parcelable.Creator()
  {
    public ResultInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ResultInfo(paramAnonymousParcel);
    }
    
    public ResultInfo[] newArray(int paramAnonymousInt)
    {
      return new ResultInfo[paramAnonymousInt];
    }
  };
  public final Intent mData;
  public final int mRequestCode;
  public final int mResultCode;
  public final String mResultWho;
  
  public ResultInfo(Parcel paramParcel)
  {
    mResultWho = paramParcel.readString();
    mRequestCode = paramParcel.readInt();
    mResultCode = paramParcel.readInt();
    if (paramParcel.readInt() != 0) {
      mData = ((Intent)Intent.CREATOR.createFromParcel(paramParcel));
    } else {
      mData = null;
    }
  }
  
  public ResultInfo(String paramString, int paramInt1, int paramInt2, Intent paramIntent)
  {
    mResultWho = paramString;
    mRequestCode = paramInt1;
    mResultCode = paramInt2;
    mData = paramIntent;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = false;
    if ((paramObject != null) && ((paramObject instanceof ResultInfo)))
    {
      paramObject = (ResultInfo)paramObject;
      boolean bool2;
      if (mData == null)
      {
        if (mData == null) {
          bool2 = true;
        } else {
          bool2 = false;
        }
      }
      else {
        bool2 = mData.filterEquals(mData);
      }
      boolean bool3 = bool1;
      if (bool2)
      {
        bool3 = bool1;
        if (Objects.equals(mResultWho, mResultWho))
        {
          bool3 = bool1;
          if (mResultCode == mResultCode)
          {
            bool3 = bool1;
            if (mRequestCode == mRequestCode) {
              bool3 = true;
            }
          }
        }
      }
      return bool3;
    }
    return false;
  }
  
  public int hashCode()
  {
    int i = 31 * (31 * (31 * 17 + mRequestCode) + mResultCode) + Objects.hashCode(mResultWho);
    int j = i;
    if (mData != null) {
      j = 31 * i + mData.filterHashCode();
    }
    return j;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ResultInfo{who=");
    localStringBuilder.append(mResultWho);
    localStringBuilder.append(", request=");
    localStringBuilder.append(mRequestCode);
    localStringBuilder.append(", result=");
    localStringBuilder.append(mResultCode);
    localStringBuilder.append(", data=");
    localStringBuilder.append(mData);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mResultWho);
    paramParcel.writeInt(mRequestCode);
    paramParcel.writeInt(mResultCode);
    if (mData != null)
    {
      paramParcel.writeInt(1);
      mData.writeToParcel(paramParcel, 0);
    }
    else
    {
      paramParcel.writeInt(0);
    }
  }
}
