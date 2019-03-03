package android.security.keymaster;

import android.os.Parcel;
import java.util.Date;

class KeymasterDateArgument
  extends KeymasterArgument
{
  public final Date date;
  
  public KeymasterDateArgument(int paramInt, Parcel paramParcel)
  {
    super(paramInt);
    date = new Date(paramParcel.readLong());
  }
  
  public KeymasterDateArgument(int paramInt, Date paramDate)
  {
    super(paramInt);
    if (KeymasterDefs.getTagType(paramInt) == 1610612736)
    {
      date = paramDate;
      return;
    }
    paramDate = new StringBuilder();
    paramDate.append("Bad date tag ");
    paramDate.append(paramInt);
    throw new IllegalArgumentException(paramDate.toString());
  }
  
  public void writeValue(Parcel paramParcel)
  {
    paramParcel.writeLong(date.getTime());
  }
}
