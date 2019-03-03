package android.net.wifi.hotspot2;

import android.graphics.drawable.Icon;
import android.net.Uri;
import android.net.wifi.WifiSsid;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class OsuProvider
  implements Parcelable
{
  public static final Parcelable.Creator<OsuProvider> CREATOR = new Parcelable.Creator()
  {
    public OsuProvider createFromParcel(Parcel paramAnonymousParcel)
    {
      WifiSsid localWifiSsid = (WifiSsid)paramAnonymousParcel.readParcelable(null);
      String str1 = paramAnonymousParcel.readString();
      String str2 = paramAnonymousParcel.readString();
      Uri localUri = (Uri)paramAnonymousParcel.readParcelable(null);
      String str3 = paramAnonymousParcel.readString();
      ArrayList localArrayList = new ArrayList();
      paramAnonymousParcel.readList(localArrayList, null);
      return new OsuProvider(localWifiSsid, str1, str2, localUri, str3, localArrayList, (Icon)paramAnonymousParcel.readParcelable(null));
    }
    
    public OsuProvider[] newArray(int paramAnonymousInt)
    {
      return new OsuProvider[paramAnonymousInt];
    }
  };
  public static final int METHOD_OMA_DM = 0;
  public static final int METHOD_SOAP_XML_SPP = 1;
  private final String mFriendlyName;
  private final Icon mIcon;
  private final List<Integer> mMethodList;
  private final String mNetworkAccessIdentifier;
  private final WifiSsid mOsuSsid;
  private final Uri mServerUri;
  private final String mServiceDescription;
  
  public OsuProvider(WifiSsid paramWifiSsid, String paramString1, String paramString2, Uri paramUri, String paramString3, List<Integer> paramList, Icon paramIcon)
  {
    mOsuSsid = paramWifiSsid;
    mFriendlyName = paramString1;
    mServiceDescription = paramString2;
    mServerUri = paramUri;
    mNetworkAccessIdentifier = paramString3;
    if (paramList == null) {
      mMethodList = new ArrayList();
    } else {
      mMethodList = new ArrayList(paramList);
    }
    mIcon = paramIcon;
  }
  
  public OsuProvider(OsuProvider paramOsuProvider)
  {
    if (paramOsuProvider == null)
    {
      mOsuSsid = null;
      mFriendlyName = null;
      mServiceDescription = null;
      mServerUri = null;
      mNetworkAccessIdentifier = null;
      mMethodList = new ArrayList();
      mIcon = null;
      return;
    }
    mOsuSsid = mOsuSsid;
    mFriendlyName = mFriendlyName;
    mServiceDescription = mServiceDescription;
    mServerUri = mServerUri;
    mNetworkAccessIdentifier = mNetworkAccessIdentifier;
    if (mMethodList == null) {
      mMethodList = new ArrayList();
    } else {
      mMethodList = new ArrayList(mMethodList);
    }
    mIcon = mIcon;
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
    if (!(paramObject instanceof OsuProvider)) {
      return false;
    }
    paramObject = (OsuProvider)paramObject;
    if ((mOsuSsid == null ? mOsuSsid == null : mOsuSsid.equals(mOsuSsid)) && (TextUtils.equals(mFriendlyName, mFriendlyName)) && (TextUtils.equals(mServiceDescription, mServiceDescription)) && (mServerUri == null ? mServerUri == null : mServerUri.equals(mServerUri)) && (TextUtils.equals(mNetworkAccessIdentifier, mNetworkAccessIdentifier)) && (mMethodList == null ? mMethodList == null : mMethodList.equals(mMethodList))) {
      if (mIcon == null) {
        if (mIcon != null) {
          break label194;
        }
      } else if (mIcon.sameAs(mIcon)) {
        return bool;
      }
    }
    label194:
    bool = false;
    return bool;
  }
  
  public String getFriendlyName()
  {
    return mFriendlyName;
  }
  
  public Icon getIcon()
  {
    return mIcon;
  }
  
  public List<Integer> getMethodList()
  {
    return Collections.unmodifiableList(mMethodList);
  }
  
  public String getNetworkAccessIdentifier()
  {
    return mNetworkAccessIdentifier;
  }
  
  public WifiSsid getOsuSsid()
  {
    return mOsuSsid;
  }
  
  public Uri getServerUri()
  {
    return mServerUri;
  }
  
  public String getServiceDescription()
  {
    return mServiceDescription;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { mOsuSsid, mFriendlyName, mServiceDescription, mServerUri, mNetworkAccessIdentifier, mMethodList, mIcon });
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("OsuProvider{mOsuSsid=");
    localStringBuilder.append(mOsuSsid);
    localStringBuilder.append(" mFriendlyName=");
    localStringBuilder.append(mFriendlyName);
    localStringBuilder.append(" mServiceDescription=");
    localStringBuilder.append(mServiceDescription);
    localStringBuilder.append(" mServerUri=");
    localStringBuilder.append(mServerUri);
    localStringBuilder.append(" mNetworkAccessIdentifier=");
    localStringBuilder.append(mNetworkAccessIdentifier);
    localStringBuilder.append(" mMethodList=");
    localStringBuilder.append(mMethodList);
    localStringBuilder.append(" mIcon=");
    localStringBuilder.append(mIcon);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(mOsuSsid, paramInt);
    paramParcel.writeString(mFriendlyName);
    paramParcel.writeString(mServiceDescription);
    paramParcel.writeParcelable(mServerUri, paramInt);
    paramParcel.writeString(mNetworkAccessIdentifier);
    paramParcel.writeList(mMethodList);
    paramParcel.writeParcelable(mIcon, paramInt);
  }
}
