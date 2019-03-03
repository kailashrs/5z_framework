package android.location;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import java.util.Locale;

public class Country
  implements Parcelable
{
  public static final int COUNTRY_SOURCE_LOCALE = 3;
  public static final int COUNTRY_SOURCE_LOCATION = 1;
  public static final int COUNTRY_SOURCE_NETWORK = 0;
  public static final int COUNTRY_SOURCE_SIM = 2;
  public static final Parcelable.Creator<Country> CREATOR = new Parcelable.Creator()
  {
    public Country createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Country(paramAnonymousParcel.readString(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readLong(), null);
    }
    
    public Country[] newArray(int paramAnonymousInt)
    {
      return new Country[paramAnonymousInt];
    }
  };
  private final String mCountryIso;
  private int mHashCode;
  private final int mSource;
  private final long mTimestamp;
  
  public Country(Country paramCountry)
  {
    mCountryIso = mCountryIso;
    mSource = mSource;
    mTimestamp = mTimestamp;
  }
  
  public Country(String paramString, int paramInt)
  {
    if ((paramString != null) && (paramInt >= 0) && (paramInt <= 3))
    {
      mCountryIso = paramString.toUpperCase(Locale.US);
      mSource = paramInt;
      mTimestamp = SystemClock.elapsedRealtime();
      return;
    }
    throw new IllegalArgumentException();
  }
  
  private Country(String paramString, int paramInt, long paramLong)
  {
    if ((paramString != null) && (paramInt >= 0) && (paramInt <= 3))
    {
      mCountryIso = paramString.toUpperCase(Locale.US);
      mSource = paramInt;
      mTimestamp = paramLong;
      return;
    }
    throw new IllegalArgumentException();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (paramObject == this) {
      return true;
    }
    if ((paramObject instanceof Country))
    {
      paramObject = (Country)paramObject;
      if ((!mCountryIso.equals(paramObject.getCountryIso())) || (mSource != paramObject.getSource())) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public boolean equalsIgnoreSource(Country paramCountry)
  {
    boolean bool;
    if ((paramCountry != null) && (mCountryIso.equals(paramCountry.getCountryIso()))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final String getCountryIso()
  {
    return mCountryIso;
  }
  
  public final int getSource()
  {
    return mSource;
  }
  
  public final long getTimestamp()
  {
    return mTimestamp;
  }
  
  public int hashCode()
  {
    if (mHashCode == 0) {
      mHashCode = ((17 * 13 + mCountryIso.hashCode()) * 13 + mSource);
    }
    return mHashCode;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Country {ISO=");
    localStringBuilder.append(mCountryIso);
    localStringBuilder.append(", source=");
    localStringBuilder.append(mSource);
    localStringBuilder.append(", time=");
    localStringBuilder.append(mTimestamp);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mCountryIso);
    paramParcel.writeInt(mSource);
    paramParcel.writeLong(mTimestamp);
  }
}
