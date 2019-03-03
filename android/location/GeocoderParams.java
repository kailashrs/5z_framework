package android.location;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Locale;

public class GeocoderParams
  implements Parcelable
{
  public static final Parcelable.Creator<GeocoderParams> CREATOR = new Parcelable.Creator()
  {
    public GeocoderParams createFromParcel(Parcel paramAnonymousParcel)
    {
      GeocoderParams localGeocoderParams = new GeocoderParams(null);
      GeocoderParams.access$102(localGeocoderParams, new Locale(paramAnonymousParcel.readString(), paramAnonymousParcel.readString(), paramAnonymousParcel.readString()));
      GeocoderParams.access$202(localGeocoderParams, paramAnonymousParcel.readString());
      return localGeocoderParams;
    }
    
    public GeocoderParams[] newArray(int paramAnonymousInt)
    {
      return new GeocoderParams[paramAnonymousInt];
    }
  };
  private Locale mLocale;
  private String mPackageName;
  
  private GeocoderParams() {}
  
  public GeocoderParams(Context paramContext, Locale paramLocale)
  {
    mLocale = paramLocale;
    mPackageName = paramContext.getPackageName();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getClientPackage()
  {
    return mPackageName;
  }
  
  public Locale getLocale()
  {
    return mLocale;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mLocale.getLanguage());
    paramParcel.writeString(mLocale.getCountry());
    paramParcel.writeString(mLocale.getVariant());
    paramParcel.writeString(mPackageName);
  }
}
