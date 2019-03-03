package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PcoData
  implements Parcelable
{
  public static final Parcelable.Creator<PcoData> CREATOR = new Parcelable.Creator()
  {
    public PcoData createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PcoData(paramAnonymousParcel);
    }
    
    public PcoData[] newArray(int paramAnonymousInt)
    {
      return new PcoData[paramAnonymousInt];
    }
  };
  public final String bearerProto;
  public final int cid;
  public final byte[] contents;
  public final int pcoId;
  
  public PcoData(int paramInt1, String paramString, int paramInt2, byte[] paramArrayOfByte)
  {
    cid = paramInt1;
    bearerProto = paramString;
    pcoId = paramInt2;
    contents = paramArrayOfByte;
  }
  
  public PcoData(Parcel paramParcel)
  {
    cid = paramParcel.readInt();
    bearerProto = paramParcel.readString();
    pcoId = paramParcel.readInt();
    contents = paramParcel.createByteArray();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("PcoData(");
    localStringBuilder.append(cid);
    localStringBuilder.append(", ");
    localStringBuilder.append(bearerProto);
    localStringBuilder.append(", ");
    localStringBuilder.append(pcoId);
    localStringBuilder.append(", contents[");
    localStringBuilder.append(contents.length);
    localStringBuilder.append("])");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(cid);
    paramParcel.writeString(bearerProto);
    paramParcel.writeInt(pcoId);
    paramParcel.writeByteArray(contents);
  }
}
