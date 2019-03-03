package android.hardware.usb;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;

public class UsbAccessory
  implements Parcelable
{
  public static final Parcelable.Creator<UsbAccessory> CREATOR = new Parcelable.Creator()
  {
    public UsbAccessory createFromParcel(Parcel paramAnonymousParcel)
    {
      return new UsbAccessory(paramAnonymousParcel.readString(), paramAnonymousParcel.readString(), paramAnonymousParcel.readString(), paramAnonymousParcel.readString(), paramAnonymousParcel.readString(), paramAnonymousParcel.readString());
    }
    
    public UsbAccessory[] newArray(int paramAnonymousInt)
    {
      return new UsbAccessory[paramAnonymousInt];
    }
  };
  public static final int DESCRIPTION_STRING = 2;
  public static final int MANUFACTURER_STRING = 0;
  public static final int MODEL_STRING = 1;
  public static final int SERIAL_STRING = 5;
  private static final String TAG = "UsbAccessory";
  public static final int URI_STRING = 4;
  public static final int VERSION_STRING = 3;
  private final String mDescription;
  private final String mManufacturer;
  private final String mModel;
  private final String mSerial;
  private final String mUri;
  private final String mVersion;
  
  public UsbAccessory(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6)
  {
    mManufacturer = ((String)Preconditions.checkNotNull(paramString1));
    mModel = ((String)Preconditions.checkNotNull(paramString2));
    mDescription = paramString3;
    mVersion = paramString4;
    mUri = paramString5;
    mSerial = paramString6;
  }
  
  public UsbAccessory(String[] paramArrayOfString)
  {
    this(paramArrayOfString[0], paramArrayOfString[1], paramArrayOfString[2], paramArrayOfString[3], paramArrayOfString[4], paramArrayOfString[5]);
  }
  
  private static boolean compare(String paramString1, String paramString2)
  {
    if (paramString1 == null)
    {
      boolean bool;
      if (paramString2 == null) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    return paramString1.equals(paramString2);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof UsbAccessory;
    boolean bool2 = false;
    if (bool1)
    {
      paramObject = (UsbAccessory)paramObject;
      bool1 = bool2;
      if (compare(mManufacturer, paramObject.getManufacturer()))
      {
        bool1 = bool2;
        if (compare(mModel, paramObject.getModel()))
        {
          bool1 = bool2;
          if (compare(mDescription, paramObject.getDescription()))
          {
            bool1 = bool2;
            if (compare(mVersion, paramObject.getVersion()))
            {
              bool1 = bool2;
              if (compare(mUri, paramObject.getUri()))
              {
                bool1 = bool2;
                if (compare(mSerial, paramObject.getSerial())) {
                  bool1 = true;
                }
              }
            }
          }
        }
      }
      return bool1;
    }
    return false;
  }
  
  public String getDescription()
  {
    return mDescription;
  }
  
  public String getManufacturer()
  {
    return mManufacturer;
  }
  
  public String getModel()
  {
    return mModel;
  }
  
  public String getSerial()
  {
    return mSerial;
  }
  
  public String getUri()
  {
    return mUri;
  }
  
  public String getVersion()
  {
    return mVersion;
  }
  
  public int hashCode()
  {
    int i = mManufacturer.hashCode();
    int j = mModel.hashCode();
    String str = mDescription;
    int k = 0;
    int m;
    if (str == null) {
      m = 0;
    } else {
      m = mDescription.hashCode();
    }
    int n;
    if (mVersion == null) {
      n = 0;
    } else {
      n = mVersion.hashCode();
    }
    int i1;
    if (mUri == null) {
      i1 = 0;
    } else {
      i1 = mUri.hashCode();
    }
    if (mSerial != null) {
      k = mSerial.hashCode();
    }
    return i ^ j ^ m ^ n ^ i1 ^ k;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("UsbAccessory[mManufacturer=");
    localStringBuilder.append(mManufacturer);
    localStringBuilder.append(", mModel=");
    localStringBuilder.append(mModel);
    localStringBuilder.append(", mDescription=");
    localStringBuilder.append(mDescription);
    localStringBuilder.append(", mVersion=");
    localStringBuilder.append(mVersion);
    localStringBuilder.append(", mUri=");
    localStringBuilder.append(mUri);
    localStringBuilder.append(", mSerial=");
    localStringBuilder.append(mSerial);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mManufacturer);
    paramParcel.writeString(mModel);
    paramParcel.writeString(mDescription);
    paramParcel.writeString(mVersion);
    paramParcel.writeString(mUri);
    paramParcel.writeString(mSerial);
  }
}
