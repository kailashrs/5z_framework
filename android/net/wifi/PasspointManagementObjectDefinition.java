package android.net.wifi;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PasspointManagementObjectDefinition
  implements Parcelable
{
  public static final Parcelable.Creator<PasspointManagementObjectDefinition> CREATOR = new Parcelable.Creator()
  {
    public PasspointManagementObjectDefinition createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PasspointManagementObjectDefinition(paramAnonymousParcel.readString(), paramAnonymousParcel.readString(), paramAnonymousParcel.readString());
    }
    
    public PasspointManagementObjectDefinition[] newArray(int paramAnonymousInt)
    {
      return new PasspointManagementObjectDefinition[paramAnonymousInt];
    }
  };
  private final String mBaseUri;
  private final String mMoTree;
  private final String mUrn;
  
  public PasspointManagementObjectDefinition(String paramString1, String paramString2, String paramString3)
  {
    mBaseUri = paramString1;
    mUrn = paramString2;
    mMoTree = paramString3;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getBaseUri()
  {
    return mBaseUri;
  }
  
  public String getMoTree()
  {
    return mMoTree;
  }
  
  public String getUrn()
  {
    return mUrn;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mBaseUri);
    paramParcel.writeString(mUrn);
    paramParcel.writeString(mMoTree);
  }
}
