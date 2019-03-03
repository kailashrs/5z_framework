package android.security.keymaster;

import android.os.Parcel;
import android.os.ParcelFormatException;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

abstract class KeymasterArgument
  implements Parcelable
{
  public static final Parcelable.Creator<KeymasterArgument> CREATOR = new Parcelable.Creator()
  {
    public KeymasterArgument createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.dataPosition();
      int j = paramAnonymousParcel.readInt();
      int k = KeymasterDefs.getTagType(j);
      if ((k != Integer.MIN_VALUE) && (k != -1879048192))
      {
        if (k != -1610612736) {
          if ((k != 268435456) && (k != 536870912) && (k != 805306368) && (k != 1073741824))
          {
            if (k != 1342177280)
            {
              if (k != 1610612736)
              {
                if (k == 1879048192) {
                  return new KeymasterBooleanArgument(j, paramAnonymousParcel);
                }
                paramAnonymousParcel = new StringBuilder();
                paramAnonymousParcel.append("Bad tag: ");
                paramAnonymousParcel.append(j);
                paramAnonymousParcel.append(" at ");
                paramAnonymousParcel.append(i);
                throw new ParcelFormatException(paramAnonymousParcel.toString());
              }
              return new KeymasterDateArgument(j, paramAnonymousParcel);
            }
          }
          else {
            return new KeymasterIntArgument(j, paramAnonymousParcel);
          }
        }
        return new KeymasterLongArgument(j, paramAnonymousParcel);
      }
      return new KeymasterBlobArgument(j, paramAnonymousParcel);
    }
    
    public KeymasterArgument[] newArray(int paramAnonymousInt)
    {
      return new KeymasterArgument[paramAnonymousInt];
    }
  };
  public final int tag;
  
  protected KeymasterArgument(int paramInt)
  {
    tag = paramInt;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(tag);
    writeValue(paramParcel);
  }
  
  public abstract void writeValue(Parcel paramParcel);
}
