package android.security;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class KeystoreArguments
  implements Parcelable
{
  public static final Parcelable.Creator<KeystoreArguments> CREATOR = new Parcelable.Creator()
  {
    public KeystoreArguments createFromParcel(Parcel paramAnonymousParcel)
    {
      return new KeystoreArguments(paramAnonymousParcel, null);
    }
    
    public KeystoreArguments[] newArray(int paramAnonymousInt)
    {
      return new KeystoreArguments[paramAnonymousInt];
    }
  };
  public byte[][] args;
  
  public KeystoreArguments()
  {
    args = null;
  }
  
  private KeystoreArguments(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public KeystoreArguments(byte[][] paramArrayOfByte)
  {
    args = paramArrayOfByte;
  }
  
  private void readFromParcel(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    args = new byte[i][];
    for (int j = 0; j < i; j++) {
      args[j] = paramParcel.createByteArray();
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    byte[][] arrayOfByte = args;
    paramInt = 0;
    if (arrayOfByte == null)
    {
      paramParcel.writeInt(0);
    }
    else
    {
      paramParcel.writeInt(args.length);
      arrayOfByte = args;
      int i = arrayOfByte.length;
      while (paramInt < i)
      {
        paramParcel.writeByteArray(arrayOfByte[paramInt]);
        paramInt++;
      }
    }
  }
}
