package android.net.wifi.aware;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class Characteristics
  implements Parcelable
{
  public static final Parcelable.Creator<Characteristics> CREATOR = new Parcelable.Creator()
  {
    public Characteristics createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Characteristics(paramAnonymousParcel.readBundle());
    }
    
    public Characteristics[] newArray(int paramAnonymousInt)
    {
      return new Characteristics[paramAnonymousInt];
    }
  };
  public static final String KEY_MAX_MATCH_FILTER_LENGTH = "key_max_match_filter_length";
  public static final String KEY_MAX_SERVICE_NAME_LENGTH = "key_max_service_name_length";
  public static final String KEY_MAX_SERVICE_SPECIFIC_INFO_LENGTH = "key_max_service_specific_info_length";
  private Bundle mCharacteristics = new Bundle();
  
  public Characteristics(Bundle paramBundle)
  {
    mCharacteristics = paramBundle;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getMaxMatchFilterLength()
  {
    return mCharacteristics.getInt("key_max_match_filter_length");
  }
  
  public int getMaxServiceNameLength()
  {
    return mCharacteristics.getInt("key_max_service_name_length");
  }
  
  public int getMaxServiceSpecificInfoLength()
  {
    return mCharacteristics.getInt("key_max_service_specific_info_length");
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeBundle(mCharacteristics);
  }
}
