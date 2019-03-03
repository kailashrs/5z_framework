package android.security.keymaster;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KeyCharacteristics
  implements Parcelable
{
  public static final Parcelable.Creator<KeyCharacteristics> CREATOR = new Parcelable.Creator()
  {
    public KeyCharacteristics createFromParcel(Parcel paramAnonymousParcel)
    {
      return new KeyCharacteristics(paramAnonymousParcel);
    }
    
    public KeyCharacteristics[] newArray(int paramAnonymousInt)
    {
      return new KeyCharacteristics[paramAnonymousInt];
    }
  };
  public KeymasterArguments hwEnforced;
  public KeymasterArguments swEnforced;
  
  public KeyCharacteristics() {}
  
  protected KeyCharacteristics(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean getBoolean(int paramInt)
  {
    if (hwEnforced.containsTag(paramInt)) {
      return hwEnforced.getBoolean(paramInt);
    }
    return swEnforced.getBoolean(paramInt);
  }
  
  public Date getDate(int paramInt)
  {
    Date localDate = swEnforced.getDate(paramInt, null);
    if (localDate != null) {
      return localDate;
    }
    return hwEnforced.getDate(paramInt, null);
  }
  
  public Integer getEnum(int paramInt)
  {
    if (hwEnforced.containsTag(paramInt)) {
      return Integer.valueOf(hwEnforced.getEnum(paramInt, -1));
    }
    if (swEnforced.containsTag(paramInt)) {
      return Integer.valueOf(swEnforced.getEnum(paramInt, -1));
    }
    return null;
  }
  
  public List<Integer> getEnums(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.addAll(hwEnforced.getEnums(paramInt));
    localArrayList.addAll(swEnforced.getEnums(paramInt));
    return localArrayList;
  }
  
  public long getUnsignedInt(int paramInt, long paramLong)
  {
    if (hwEnforced.containsTag(paramInt)) {
      return hwEnforced.getUnsignedInt(paramInt, paramLong);
    }
    return swEnforced.getUnsignedInt(paramInt, paramLong);
  }
  
  public List<BigInteger> getUnsignedLongs(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.addAll(hwEnforced.getUnsignedLongs(paramInt));
    localArrayList.addAll(swEnforced.getUnsignedLongs(paramInt));
    return localArrayList;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    swEnforced = ((KeymasterArguments)KeymasterArguments.CREATOR.createFromParcel(paramParcel));
    hwEnforced = ((KeymasterArguments)KeymasterArguments.CREATOR.createFromParcel(paramParcel));
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    swEnforced.writeToParcel(paramParcel, paramInt);
    hwEnforced.writeToParcel(paramParcel, paramInt);
  }
}
