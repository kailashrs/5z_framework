package android.security.keymaster;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ExportResult
  implements Parcelable
{
  public static final Parcelable.Creator<ExportResult> CREATOR = new Parcelable.Creator()
  {
    public ExportResult createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ExportResult(paramAnonymousParcel);
    }
    
    public ExportResult[] newArray(int paramAnonymousInt)
    {
      return new ExportResult[paramAnonymousInt];
    }
  };
  public final byte[] exportData;
  public final int resultCode;
  
  protected ExportResult(Parcel paramParcel)
  {
    resultCode = paramParcel.readInt();
    exportData = paramParcel.createByteArray();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(resultCode);
    paramParcel.writeByteArray(exportData);
  }
}
