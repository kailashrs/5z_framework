package android.telecom;

import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ParcelableRttCall
  implements Parcelable
{
  public static final Parcelable.Creator<ParcelableRttCall> CREATOR = new Parcelable.Creator()
  {
    public ParcelableRttCall createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ParcelableRttCall(paramAnonymousParcel);
    }
    
    public ParcelableRttCall[] newArray(int paramAnonymousInt)
    {
      return new ParcelableRttCall[paramAnonymousInt];
    }
  };
  private final ParcelFileDescriptor mReceiveStream;
  private final int mRttMode;
  private final ParcelFileDescriptor mTransmitStream;
  
  public ParcelableRttCall(int paramInt, ParcelFileDescriptor paramParcelFileDescriptor1, ParcelFileDescriptor paramParcelFileDescriptor2)
  {
    mRttMode = paramInt;
    mTransmitStream = paramParcelFileDescriptor1;
    mReceiveStream = paramParcelFileDescriptor2;
  }
  
  protected ParcelableRttCall(Parcel paramParcel)
  {
    mRttMode = paramParcel.readInt();
    mTransmitStream = ((ParcelFileDescriptor)paramParcel.readParcelable(ParcelFileDescriptor.class.getClassLoader()));
    mReceiveStream = ((ParcelFileDescriptor)paramParcel.readParcelable(ParcelFileDescriptor.class.getClassLoader()));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public ParcelFileDescriptor getReceiveStream()
  {
    return mReceiveStream;
  }
  
  public int getRttMode()
  {
    return mRttMode;
  }
  
  public ParcelFileDescriptor getTransmitStream()
  {
    return mTransmitStream;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mRttMode);
    paramParcel.writeParcelable(mTransmitStream, paramInt);
    paramParcel.writeParcelable(mReceiveStream, paramInt);
  }
}
