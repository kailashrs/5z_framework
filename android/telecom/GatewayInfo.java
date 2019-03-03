package android.telecom;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;

public class GatewayInfo
  implements Parcelable
{
  public static final Parcelable.Creator<GatewayInfo> CREATOR = new Parcelable.Creator()
  {
    public GatewayInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new GatewayInfo(paramAnonymousParcel.readString(), (Uri)Uri.CREATOR.createFromParcel(paramAnonymousParcel), (Uri)Uri.CREATOR.createFromParcel(paramAnonymousParcel));
    }
    
    public GatewayInfo[] newArray(int paramAnonymousInt)
    {
      return new GatewayInfo[paramAnonymousInt];
    }
  };
  private final Uri mGatewayAddress;
  private final String mGatewayProviderPackageName;
  private final Uri mOriginalAddress;
  
  public GatewayInfo(String paramString, Uri paramUri1, Uri paramUri2)
  {
    mGatewayProviderPackageName = paramString;
    mGatewayAddress = paramUri1;
    mOriginalAddress = paramUri2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public Uri getGatewayAddress()
  {
    return mGatewayAddress;
  }
  
  public String getGatewayProviderPackageName()
  {
    return mGatewayProviderPackageName;
  }
  
  public Uri getOriginalAddress()
  {
    return mOriginalAddress;
  }
  
  public boolean isEmpty()
  {
    boolean bool;
    if ((!TextUtils.isEmpty(mGatewayProviderPackageName)) && (mGatewayAddress != null)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mGatewayProviderPackageName);
    mGatewayAddress.writeToParcel(paramParcel, 0);
    mOriginalAddress.writeToParcel(paramParcel, 0);
  }
}
