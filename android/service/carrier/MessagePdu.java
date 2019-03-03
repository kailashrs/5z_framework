package android.service.carrier;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class MessagePdu
  implements Parcelable
{
  public static final Parcelable.Creator<MessagePdu> CREATOR = new Parcelable.Creator()
  {
    public MessagePdu createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      Object localObject;
      if (i == -1)
      {
        localObject = null;
      }
      else
      {
        ArrayList localArrayList = new ArrayList(i);
        for (int j = 0;; j++)
        {
          localObject = localArrayList;
          if (j >= i) {
            break;
          }
          localArrayList.add(paramAnonymousParcel.createByteArray());
        }
      }
      return new MessagePdu(localObject);
    }
    
    public MessagePdu[] newArray(int paramAnonymousInt)
    {
      return new MessagePdu[paramAnonymousInt];
    }
  };
  private static final int NULL_LENGTH = -1;
  private final List<byte[]> mPduList;
  
  public MessagePdu(List<byte[]> paramList)
  {
    if ((paramList != null) && (!paramList.contains(null)))
    {
      mPduList = paramList;
      return;
    }
    throw new IllegalArgumentException("pduList must not be null or contain nulls");
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public List<byte[]> getPdus()
  {
    return mPduList;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (mPduList == null)
    {
      paramParcel.writeInt(-1);
    }
    else
    {
      paramParcel.writeInt(mPduList.size());
      Iterator localIterator = mPduList.iterator();
      while (localIterator.hasNext()) {
        paramParcel.writeByteArray((byte[])localIterator.next());
      }
    }
  }
}
