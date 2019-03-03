package com.android.internal.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class VpnProfile
  implements Cloneable, Parcelable
{
  public static final Parcelable.Creator<VpnProfile> CREATOR = new Parcelable.Creator()
  {
    public VpnProfile createFromParcel(Parcel paramAnonymousParcel)
    {
      return new VpnProfile(paramAnonymousParcel);
    }
    
    public VpnProfile[] newArray(int paramAnonymousInt)
    {
      return new VpnProfile[paramAnonymousInt];
    }
  };
  private static final String TAG = "VpnProfile";
  public static final int TYPE_IPSEC_HYBRID_RSA = 5;
  public static final int TYPE_IPSEC_XAUTH_PSK = 3;
  public static final int TYPE_IPSEC_XAUTH_RSA = 4;
  public static final int TYPE_L2TP_IPSEC_PSK = 1;
  public static final int TYPE_L2TP_IPSEC_RSA = 2;
  public static final int TYPE_MAX = 5;
  public static final int TYPE_PPTP = 0;
  public String dnsServers;
  public String ipsecCaCert;
  public String ipsecIdentifier;
  public String ipsecSecret;
  public String ipsecServerCert;
  public String ipsecUserCert;
  public final String key;
  public String l2tpSecret;
  public boolean mppe;
  public String name = "";
  public String password;
  public String routes;
  public boolean saveLogin;
  public String searchDomains;
  public String server;
  public int type;
  public String username;
  
  public VpnProfile(Parcel paramParcel)
  {
    boolean bool1 = false;
    type = 0;
    server = "";
    username = "";
    password = "";
    dnsServers = "";
    searchDomains = "";
    routes = "";
    mppe = true;
    l2tpSecret = "";
    ipsecIdentifier = "";
    ipsecSecret = "";
    ipsecUserCert = "";
    ipsecCaCert = "";
    ipsecServerCert = "";
    saveLogin = false;
    key = paramParcel.readString();
    name = paramParcel.readString();
    type = paramParcel.readInt();
    server = paramParcel.readString();
    username = paramParcel.readString();
    password = paramParcel.readString();
    dnsServers = paramParcel.readString();
    searchDomains = paramParcel.readString();
    routes = paramParcel.readString();
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mppe = bool2;
    l2tpSecret = paramParcel.readString();
    ipsecIdentifier = paramParcel.readString();
    ipsecSecret = paramParcel.readString();
    ipsecUserCert = paramParcel.readString();
    ipsecCaCert = paramParcel.readString();
    ipsecServerCert = paramParcel.readString();
    boolean bool2 = bool1;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    }
    saveLogin = bool2;
  }
  
  public VpnProfile(String paramString)
  {
    type = 0;
    server = "";
    username = "";
    password = "";
    dnsServers = "";
    searchDomains = "";
    routes = "";
    mppe = true;
    l2tpSecret = "";
    ipsecIdentifier = "";
    ipsecSecret = "";
    ipsecUserCert = "";
    ipsecCaCert = "";
    ipsecServerCert = "";
    saveLogin = false;
    key = paramString;
  }
  
  public static VpnProfile decode(String paramString, byte[] paramArrayOfByte)
  {
    if (paramString == null) {
      return null;
    }
    try
    {
      Object localObject = new java/lang/String;
      ((String)localObject).<init>(paramArrayOfByte, StandardCharsets.UTF_8);
      localObject = ((String)localObject).split("\000", -1);
      if ((localObject.length >= 14) && (localObject.length <= 15))
      {
        paramArrayOfByte = new com/android/internal/net/VpnProfile;
        paramArrayOfByte.<init>(paramString);
        boolean bool = false;
        name = localObject[0];
        type = Integer.parseInt(localObject[1]);
        if ((type >= 0) && (type <= 5))
        {
          server = localObject[2];
          username = localObject[3];
          password = localObject[4];
          dnsServers = localObject[5];
          searchDomains = localObject[6];
          routes = localObject[7];
          mppe = Boolean.parseBoolean(localObject[8]);
          l2tpSecret = localObject[9];
          ipsecIdentifier = localObject[10];
          ipsecSecret = localObject[11];
          ipsecUserCert = localObject[12];
          ipsecCaCert = localObject[13];
          if (localObject.length > 14) {
            paramString = localObject[14];
          } else {
            paramString = "";
          }
          ipsecServerCert = paramString;
          if ((username.isEmpty()) && (password.isEmpty())) {
            break label235;
          }
          bool = true;
          label235:
          saveLogin = bool;
          return paramArrayOfByte;
        }
        return null;
      }
      return null;
    }
    catch (Exception paramString) {}
    return null;
  }
  
  public boolean areDnsAddressesNumeric()
  {
    try
    {
      String[] arrayOfString = dnsServers.split(" +");
      int i = arrayOfString.length;
      for (int j = 0; j < i; j++) {
        InetAddress.parseNumericAddress(arrayOfString[j]);
      }
      return true;
    }
    catch (IllegalArgumentException localIllegalArgumentException) {}
    return false;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public byte[] encode()
  {
    StringBuilder localStringBuilder = new StringBuilder(name);
    localStringBuilder.append('\000');
    localStringBuilder.append(type);
    localStringBuilder.append('\000');
    localStringBuilder.append(server);
    localStringBuilder.append('\000');
    String str;
    if (saveLogin) {
      str = username;
    } else {
      str = "";
    }
    localStringBuilder.append(str);
    localStringBuilder.append('\000');
    if (saveLogin) {
      str = password;
    } else {
      str = "";
    }
    localStringBuilder.append(str);
    localStringBuilder.append('\000');
    localStringBuilder.append(dnsServers);
    localStringBuilder.append('\000');
    localStringBuilder.append(searchDomains);
    localStringBuilder.append('\000');
    localStringBuilder.append(routes);
    localStringBuilder.append('\000');
    localStringBuilder.append(mppe);
    localStringBuilder.append('\000');
    localStringBuilder.append(l2tpSecret);
    localStringBuilder.append('\000');
    localStringBuilder.append(ipsecIdentifier);
    localStringBuilder.append('\000');
    localStringBuilder.append(ipsecSecret);
    localStringBuilder.append('\000');
    localStringBuilder.append(ipsecUserCert);
    localStringBuilder.append('\000');
    localStringBuilder.append(ipsecCaCert);
    localStringBuilder.append('\000');
    localStringBuilder.append(ipsecServerCert);
    return localStringBuilder.toString().getBytes(StandardCharsets.UTF_8);
  }
  
  public boolean hasDns()
  {
    return TextUtils.isEmpty(dnsServers) ^ true;
  }
  
  public boolean isServerAddressNumeric()
  {
    try
    {
      InetAddress.parseNumericAddress(server);
      return true;
    }
    catch (IllegalArgumentException localIllegalArgumentException) {}
    return false;
  }
  
  public boolean isTypeValidForLockdown()
  {
    boolean bool;
    if (type != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isValidLockdownProfile()
  {
    boolean bool;
    if ((isTypeValidForLockdown()) && (isServerAddressNumeric()) && (hasDns()) && (areDnsAddressesNumeric())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(key);
    paramParcel.writeString(name);
    paramParcel.writeInt(type);
    paramParcel.writeString(server);
    paramParcel.writeString(username);
    paramParcel.writeString(password);
    paramParcel.writeString(dnsServers);
    paramParcel.writeString(searchDomains);
    paramParcel.writeString(routes);
    paramParcel.writeInt(mppe);
    paramParcel.writeString(l2tpSecret);
    paramParcel.writeString(ipsecIdentifier);
    paramParcel.writeString(ipsecSecret);
    paramParcel.writeString(ipsecUserCert);
    paramParcel.writeString(ipsecCaCert);
    paramParcel.writeString(ipsecServerCert);
    paramParcel.writeInt(saveLogin);
  }
}
