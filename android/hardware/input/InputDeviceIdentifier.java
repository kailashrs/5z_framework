package android.hardware.input;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.util.Objects;

public final class InputDeviceIdentifier
  implements Parcelable
{
  public static final Parcelable.Creator<InputDeviceIdentifier> CREATOR = new Parcelable.Creator()
  {
    public InputDeviceIdentifier createFromParcel(Parcel paramAnonymousParcel)
    {
      return new InputDeviceIdentifier(paramAnonymousParcel, null);
    }
    
    public InputDeviceIdentifier[] newArray(int paramAnonymousInt)
    {
      return new InputDeviceIdentifier[paramAnonymousInt];
    }
  };
  private final String mDescriptor;
  private final int mProductId;
  private final int mVendorId;
  
  private InputDeviceIdentifier(Parcel paramParcel)
  {
    mDescriptor = paramParcel.readString();
    mVendorId = paramParcel.readInt();
    mProductId = paramParcel.readInt();
  }
  
  public InputDeviceIdentifier(String paramString, int paramInt1, int paramInt2)
  {
    mDescriptor = paramString;
    mVendorId = paramInt1;
    mProductId = paramInt2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && ((paramObject instanceof InputDeviceIdentifier)))
    {
      paramObject = (InputDeviceIdentifier)paramObject;
      if ((mVendorId != mVendorId) || (mProductId != mProductId) || (!TextUtils.equals(mDescriptor, mDescriptor))) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public String getDescriptor()
  {
    return mDescriptor;
  }
  
  public int getProductId()
  {
    return mProductId;
  }
  
  public int getVendorId()
  {
    return mVendorId;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { mDescriptor, Integer.valueOf(mVendorId), Integer.valueOf(mProductId) });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mDescriptor);
    paramParcel.writeInt(mVendorId);
    paramParcel.writeInt(mProductId);
  }
}
