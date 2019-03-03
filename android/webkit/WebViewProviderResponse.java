package android.webkit;

import android.content.pm.PackageInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class WebViewProviderResponse
  implements Parcelable
{
  public static final Parcelable.Creator<WebViewProviderResponse> CREATOR = new Parcelable.Creator()
  {
    public WebViewProviderResponse createFromParcel(Parcel paramAnonymousParcel)
    {
      return new WebViewProviderResponse(paramAnonymousParcel, null);
    }
    
    public WebViewProviderResponse[] newArray(int paramAnonymousInt)
    {
      return new WebViewProviderResponse[paramAnonymousInt];
    }
  };
  public final PackageInfo packageInfo;
  public final int status;
  
  public WebViewProviderResponse(PackageInfo paramPackageInfo, int paramInt)
  {
    packageInfo = paramPackageInfo;
    status = paramInt;
  }
  
  private WebViewProviderResponse(Parcel paramParcel)
  {
    packageInfo = ((PackageInfo)paramParcel.readTypedObject(PackageInfo.CREATOR));
    status = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeTypedObject(packageInfo, paramInt);
    paramParcel.writeInt(status);
  }
}
