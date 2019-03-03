package android.bluetooth.le;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public final class ResultStorageDescriptor
  implements Parcelable
{
  public static final Parcelable.Creator<ResultStorageDescriptor> CREATOR = new Parcelable.Creator()
  {
    public ResultStorageDescriptor createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ResultStorageDescriptor(paramAnonymousParcel, null);
    }
    
    public ResultStorageDescriptor[] newArray(int paramAnonymousInt)
    {
      return new ResultStorageDescriptor[paramAnonymousInt];
    }
  };
  private int mLength;
  private int mOffset;
  private int mType;
  
  public ResultStorageDescriptor(int paramInt1, int paramInt2, int paramInt3)
  {
    mType = paramInt1;
    mOffset = paramInt2;
    mLength = paramInt3;
  }
  
  private ResultStorageDescriptor(Parcel paramParcel)
  {
    ReadFromParcel(paramParcel);
  }
  
  private void ReadFromParcel(Parcel paramParcel)
  {
    mType = paramParcel.readInt();
    mOffset = paramParcel.readInt();
    mLength = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getLength()
  {
    return mLength;
  }
  
  public int getOffset()
  {
    return mOffset;
  }
  
  public int getType()
  {
    return mType;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mType);
    paramParcel.writeInt(mOffset);
    paramParcel.writeInt(mLength);
  }
}
