package android.webkit;

import android.annotation.SystemApi;
import android.content.pm.Signature;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Base64;

@SystemApi
public final class WebViewProviderInfo
  implements Parcelable
{
  public static final Parcelable.Creator<WebViewProviderInfo> CREATOR = new Parcelable.Creator()
  {
    public WebViewProviderInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new WebViewProviderInfo(paramAnonymousParcel, null);
    }
    
    public WebViewProviderInfo[] newArray(int paramAnonymousInt)
    {
      return new WebViewProviderInfo[paramAnonymousInt];
    }
  };
  public final boolean availableByDefault;
  public final String description;
  public final boolean isFallback;
  public final String packageName;
  public final Signature[] signatures;
  
  private WebViewProviderInfo(Parcel paramParcel)
  {
    packageName = paramParcel.readString();
    description = paramParcel.readString();
    int i = paramParcel.readInt();
    boolean bool1 = false;
    if (i > 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    availableByDefault = bool2;
    boolean bool2 = bool1;
    if (paramParcel.readInt() > 0) {
      bool2 = true;
    }
    isFallback = bool2;
    signatures = ((Signature[])paramParcel.createTypedArray(Signature.CREATOR));
  }
  
  public WebViewProviderInfo(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2, String[] paramArrayOfString)
  {
    packageName = paramString1;
    description = paramString2;
    availableByDefault = paramBoolean1;
    isFallback = paramBoolean2;
    if (paramArrayOfString == null)
    {
      signatures = new Signature[0];
    }
    else
    {
      signatures = new Signature[paramArrayOfString.length];
      for (int i = 0; i < paramArrayOfString.length; i++) {
        signatures[i] = new Signature(Base64.decode(paramArrayOfString[i], 0));
      }
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(packageName);
    paramParcel.writeString(description);
    paramParcel.writeInt(availableByDefault);
    paramParcel.writeInt(isFallback);
    paramParcel.writeTypedArray(signatures, 0);
  }
}
