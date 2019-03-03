package android.hardware.camera2.params;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

public final class VendorTagDescriptor
  implements Parcelable
{
  public static final Parcelable.Creator<VendorTagDescriptor> CREATOR = new Parcelable.Creator()
  {
    public VendorTagDescriptor createFromParcel(Parcel paramAnonymousParcel)
    {
      try
      {
        paramAnonymousParcel = new VendorTagDescriptor(paramAnonymousParcel, null);
        return paramAnonymousParcel;
      }
      catch (Exception paramAnonymousParcel)
      {
        Log.e("VendorTagDescriptor", "Exception creating VendorTagDescriptor from parcel", paramAnonymousParcel);
      }
      return null;
    }
    
    public VendorTagDescriptor[] newArray(int paramAnonymousInt)
    {
      return new VendorTagDescriptor[paramAnonymousInt];
    }
  };
  private static final String TAG = "VendorTagDescriptor";
  
  private VendorTagDescriptor(Parcel paramParcel) {}
  
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
