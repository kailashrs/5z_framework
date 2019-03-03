package android.hardware.usb;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;

public class UsbInterface
  implements Parcelable
{
  public static final Parcelable.Creator<UsbInterface> CREATOR = new Parcelable.Creator()
  {
    public UsbInterface createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      int j = paramAnonymousParcel.readInt();
      Object localObject = paramAnonymousParcel.readString();
      int k = paramAnonymousParcel.readInt();
      int m = paramAnonymousParcel.readInt();
      int n = paramAnonymousParcel.readInt();
      paramAnonymousParcel = paramAnonymousParcel.readParcelableArray(UsbEndpoint.class.getClassLoader());
      localObject = new UsbInterface(i, j, (String)localObject, k, m, n);
      ((UsbInterface)localObject).setEndpoints(paramAnonymousParcel);
      return localObject;
    }
    
    public UsbInterface[] newArray(int paramAnonymousInt)
    {
      return new UsbInterface[paramAnonymousInt];
    }
  };
  private final int mAlternateSetting;
  private final int mClass;
  private Parcelable[] mEndpoints;
  private final int mId;
  private final String mName;
  private final int mProtocol;
  private final int mSubclass;
  
  public UsbInterface(int paramInt1, int paramInt2, String paramString, int paramInt3, int paramInt4, int paramInt5)
  {
    mId = paramInt1;
    mAlternateSetting = paramInt2;
    mName = paramString;
    mClass = paramInt3;
    mSubclass = paramInt4;
    mProtocol = paramInt5;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getAlternateSetting()
  {
    return mAlternateSetting;
  }
  
  public UsbEndpoint getEndpoint(int paramInt)
  {
    return (UsbEndpoint)mEndpoints[paramInt];
  }
  
  public int getEndpointCount()
  {
    return mEndpoints.length;
  }
  
  public int getId()
  {
    return mId;
  }
  
  public int getInterfaceClass()
  {
    return mClass;
  }
  
  public int getInterfaceProtocol()
  {
    return mProtocol;
  }
  
  public int getInterfaceSubclass()
  {
    return mSubclass;
  }
  
  public String getName()
  {
    return mName;
  }
  
  public void setEndpoints(Parcelable[] paramArrayOfParcelable)
  {
    mEndpoints = ((Parcelable[])Preconditions.checkArrayElementsNotNull(paramArrayOfParcelable, "endpoints"));
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("UsbInterface[mId=");
    localStringBuilder.append(mId);
    localStringBuilder.append(",mAlternateSetting=");
    localStringBuilder.append(mAlternateSetting);
    localStringBuilder.append(",mName=");
    localStringBuilder.append(mName);
    localStringBuilder.append(",mClass=");
    localStringBuilder.append(mClass);
    localStringBuilder.append(",mSubclass=");
    localStringBuilder.append(mSubclass);
    localStringBuilder.append(",mProtocol=");
    localStringBuilder.append(mProtocol);
    localStringBuilder.append(",mEndpoints=[");
    localStringBuilder = new StringBuilder(localStringBuilder.toString());
    for (int i = 0; i < mEndpoints.length; i++)
    {
      localStringBuilder.append("\n");
      localStringBuilder.append(mEndpoints[i].toString());
    }
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mId);
    paramParcel.writeInt(mAlternateSetting);
    paramParcel.writeString(mName);
    paramParcel.writeInt(mClass);
    paramParcel.writeInt(mSubclass);
    paramParcel.writeInt(mProtocol);
    paramParcel.writeParcelableArray(mEndpoints, 0);
  }
}
