package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.net.InetSocketAddress;
import java.net.Proxy.Type;
import java.util.List;
import java.util.Locale;

public class ProxyInfo
  implements Parcelable
{
  public static final Parcelable.Creator<ProxyInfo> CREATOR = new Parcelable.Creator()
  {
    public ProxyInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      String str = null;
      int i = 0;
      if (paramAnonymousParcel.readByte() != 0) {
        return new ProxyInfo((Uri)Uri.CREATOR.createFromParcel(paramAnonymousParcel), paramAnonymousParcel.readInt());
      }
      if (paramAnonymousParcel.readByte() != 0)
      {
        str = paramAnonymousParcel.readString();
        i = paramAnonymousParcel.readInt();
      }
      return new ProxyInfo(str, i, paramAnonymousParcel.readString(), paramAnonymousParcel.readStringArray(), null);
    }
    
    public ProxyInfo[] newArray(int paramAnonymousInt)
    {
      return new ProxyInfo[paramAnonymousInt];
    }
  };
  public static final String LOCAL_EXCL_LIST = "";
  public static final String LOCAL_HOST = "localhost";
  public static final int LOCAL_PORT = -1;
  private String mExclusionList;
  private String mHost;
  private Uri mPacFileUrl;
  private String[] mParsedExclusionList;
  private int mPort;
  
  public ProxyInfo(ProxyInfo paramProxyInfo)
  {
    if (paramProxyInfo != null)
    {
      mHost = paramProxyInfo.getHost();
      mPort = paramProxyInfo.getPort();
      mPacFileUrl = mPacFileUrl;
      mExclusionList = paramProxyInfo.getExclusionListAsString();
      mParsedExclusionList = mParsedExclusionList;
    }
    else
    {
      mPacFileUrl = Uri.EMPTY;
    }
  }
  
  public ProxyInfo(Uri paramUri)
  {
    mHost = "localhost";
    mPort = -1;
    setExclusionList("");
    if (paramUri != null)
    {
      mPacFileUrl = paramUri;
      return;
    }
    throw new NullPointerException();
  }
  
  public ProxyInfo(Uri paramUri, int paramInt)
  {
    mHost = "localhost";
    mPort = paramInt;
    setExclusionList("");
    if (paramUri != null)
    {
      mPacFileUrl = paramUri;
      return;
    }
    throw new NullPointerException();
  }
  
  public ProxyInfo(String paramString)
  {
    mHost = "localhost";
    mPort = -1;
    setExclusionList("");
    mPacFileUrl = Uri.parse(paramString);
  }
  
  public ProxyInfo(String paramString1, int paramInt, String paramString2)
  {
    mHost = paramString1;
    mPort = paramInt;
    setExclusionList(paramString2);
    mPacFileUrl = Uri.EMPTY;
  }
  
  private ProxyInfo(String paramString1, int paramInt, String paramString2, String[] paramArrayOfString)
  {
    mHost = paramString1;
    mPort = paramInt;
    mExclusionList = paramString2;
    mParsedExclusionList = paramArrayOfString;
    mPacFileUrl = Uri.EMPTY;
  }
  
  public static ProxyInfo buildDirectProxy(String paramString, int paramInt)
  {
    return new ProxyInfo(paramString, paramInt, null);
  }
  
  public static ProxyInfo buildDirectProxy(String paramString, int paramInt, List<String> paramList)
  {
    paramList = (String[])paramList.toArray(new String[paramList.size()]);
    return new ProxyInfo(paramString, paramInt, TextUtils.join(",", paramList), paramList);
  }
  
  public static ProxyInfo buildPacProxy(Uri paramUri)
  {
    return new ProxyInfo(paramUri);
  }
  
  private void setExclusionList(String paramString)
  {
    mExclusionList = paramString;
    if (mExclusionList == null) {
      mParsedExclusionList = new String[0];
    } else {
      mParsedExclusionList = paramString.toLowerCase(Locale.ROOT).split(",");
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof ProxyInfo;
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    paramObject = (ProxyInfo)paramObject;
    if (!Uri.EMPTY.equals(mPacFileUrl))
    {
      bool1 = bool2;
      if (mPacFileUrl.equals(paramObject.getPacFileUrl()))
      {
        bool1 = bool2;
        if (mPort == mPort) {
          bool1 = true;
        }
      }
      return bool1;
    }
    if (!Uri.EMPTY.equals(mPacFileUrl)) {
      return false;
    }
    if ((mExclusionList != null) && (!mExclusionList.equals(paramObject.getExclusionListAsString()))) {
      return false;
    }
    if ((mHost != null) && (paramObject.getHost() != null) && (!mHost.equals(paramObject.getHost()))) {
      return false;
    }
    if ((mHost != null) && (mHost == null)) {
      return false;
    }
    if ((mHost == null) && (mHost != null)) {
      return false;
    }
    return mPort == mPort;
  }
  
  public String[] getExclusionList()
  {
    return mParsedExclusionList;
  }
  
  public String getExclusionListAsString()
  {
    return mExclusionList;
  }
  
  public String getHost()
  {
    return mHost;
  }
  
  public Uri getPacFileUrl()
  {
    return mPacFileUrl;
  }
  
  public int getPort()
  {
    return mPort;
  }
  
  public InetSocketAddress getSocketAddress()
  {
    Object localObject = null;
    try
    {
      InetSocketAddress localInetSocketAddress = new java/net/InetSocketAddress;
      localInetSocketAddress.<init>(mHost, mPort);
      localObject = localInetSocketAddress;
    }
    catch (IllegalArgumentException localIllegalArgumentException) {}
    return localObject;
  }
  
  public int hashCode()
  {
    String str = mHost;
    int i = 0;
    int j;
    if (str == null) {
      j = 0;
    } else {
      j = mHost.hashCode();
    }
    if (mExclusionList != null) {
      i = mExclusionList.hashCode();
    }
    return j + i + mPort;
  }
  
  public boolean isValid()
  {
    boolean bool1 = Uri.EMPTY.equals(mPacFileUrl);
    boolean bool2 = true;
    if (!bool1) {
      return true;
    }
    String str1;
    if (mHost == null) {
      str1 = "";
    } else {
      str1 = mHost;
    }
    String str2;
    if (mPort == 0) {
      str2 = "";
    } else {
      str2 = Integer.toString(mPort);
    }
    String str3;
    if (mExclusionList == null) {
      str3 = "";
    } else {
      str3 = mExclusionList;
    }
    if (Proxy.validate(str1, str2, str3) != 0) {
      bool2 = false;
    }
    return bool2;
  }
  
  public java.net.Proxy makeProxy()
  {
    java.net.Proxy localProxy1 = java.net.Proxy.NO_PROXY;
    java.net.Proxy localProxy2 = localProxy1;
    java.net.Proxy localProxy3;
    if (mHost != null) {
      try
      {
        InetSocketAddress localInetSocketAddress = new java/net/InetSocketAddress;
        localInetSocketAddress.<init>(mHost, mPort);
        localProxy2 = new java/net/Proxy;
        localProxy2.<init>(Proxy.Type.HTTP, localInetSocketAddress);
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        localProxy3 = localProxy1;
      }
    }
    return localProxy3;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (!Uri.EMPTY.equals(mPacFileUrl))
    {
      localStringBuilder.append("PAC Script: ");
      localStringBuilder.append(mPacFileUrl);
    }
    if (mHost != null)
    {
      localStringBuilder.append("[");
      localStringBuilder.append(mHost);
      localStringBuilder.append("] ");
      localStringBuilder.append(Integer.toString(mPort));
      if (mExclusionList != null)
      {
        localStringBuilder.append(" xl=");
        localStringBuilder.append(mExclusionList);
      }
    }
    else
    {
      localStringBuilder.append("[ProxyProperties.mHost == null]");
    }
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (!Uri.EMPTY.equals(mPacFileUrl))
    {
      paramParcel.writeByte((byte)1);
      mPacFileUrl.writeToParcel(paramParcel, 0);
      paramParcel.writeInt(mPort);
      return;
    }
    paramParcel.writeByte((byte)0);
    if (mHost != null)
    {
      paramParcel.writeByte((byte)1);
      paramParcel.writeString(mHost);
      paramParcel.writeInt(mPort);
    }
    else
    {
      paramParcel.writeByte((byte)0);
    }
    paramParcel.writeString(mExclusionList);
    paramParcel.writeStringArray(mParsedExclusionList);
  }
}
