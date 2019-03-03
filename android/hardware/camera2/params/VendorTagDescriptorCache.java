package android.hardware.camera2.params;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

public final class VendorTagDescriptorCache
  implements Parcelable
{
  public static final Parcelable.Creator<VendorTagDescriptorCache> CREATOR = new Parcelable.Creator()
  {
    public VendorTagDescriptorCache createFromParcel(Parcel paramAnonymousParcel)
    {
      try
      {
        paramAnonymousParcel = new VendorTagDescriptorCache(paramAnonymousParcel, null);
        return paramAnonymousParcel;
      }
      catch (Exception paramAnonymousParcel)
      {
        Log.e("VendorTagDescriptorCache", "Exception creating VendorTagDescriptorCache from parcel", paramAnonymousParcel);
      }
      return null;
    }
    
    public VendorTagDescriptorCache[] newArray(int paramAnonymousInt)
    {
      return new VendorTagDescriptorCache[paramAnonymousInt];
    }
  };
  private static final String TAG = "VendorTagDescriptorCache";
  
  private VendorTagDescriptorCache(Parcel paramParcel) {}
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (paramParcel != null) {
      return;
    }
    throw new IllegalArgumentException("dest must not be null");
  }
}
