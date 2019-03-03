package android.nfc;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ApduList
  implements Parcelable
{
  public static final Parcelable.Creator<ApduList> CREATOR = new Parcelable.Creator()
  {
    public ApduList createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ApduList(paramAnonymousParcel, null);
    }
    
    public ApduList[] newArray(int paramAnonymousInt)
    {
      return new ApduList[paramAnonymousInt];
    }
  };
  private ArrayList<byte[]> commands = new ArrayList();
  
  public ApduList() {}
  
  private ApduList(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    for (int j = 0; j < i; j++)
    {
      byte[] arrayOfByte = new byte[paramParcel.readInt()];
      paramParcel.readByteArray(arrayOfByte);
      commands.add(arrayOfByte);
    }
  }
  
  public void add(byte[] paramArrayOfByte)
  {
    commands.add(paramArrayOfByte);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public List<byte[]> get()
  {
    return commands;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(commands.size());
    Iterator localIterator = commands.iterator();
    while (localIterator.hasNext())
    {
      byte[] arrayOfByte = (byte[])localIterator.next();
      paramParcel.writeInt(arrayOfByte.length);
      paramParcel.writeByteArray(arrayOfByte);
    }
  }
}
