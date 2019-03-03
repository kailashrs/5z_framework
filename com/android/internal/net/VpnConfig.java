package com.android.internal.net;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.IpPrefix;
import android.net.LinkAddress;
import android.net.Network;
import android.net.RouteInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.UserHandle;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class VpnConfig
  implements Parcelable
{
  public static final Parcelable.Creator<VpnConfig> CREATOR = new Parcelable.Creator()
  {
    public VpnConfig createFromParcel(Parcel paramAnonymousParcel)
    {
      VpnConfig localVpnConfig = new VpnConfig();
      user = paramAnonymousParcel.readString();
      interfaze = paramAnonymousParcel.readString();
      session = paramAnonymousParcel.readString();
      mtu = paramAnonymousParcel.readInt();
      paramAnonymousParcel.readTypedList(addresses, LinkAddress.CREATOR);
      paramAnonymousParcel.readTypedList(routes, RouteInfo.CREATOR);
      dnsServers = paramAnonymousParcel.createStringArrayList();
      searchDomains = paramAnonymousParcel.createStringArrayList();
      allowedApplications = paramAnonymousParcel.createStringArrayList();
      disallowedApplications = paramAnonymousParcel.createStringArrayList();
      configureIntent = ((PendingIntent)paramAnonymousParcel.readParcelable(null));
      startTime = paramAnonymousParcel.readLong();
      int i = paramAnonymousParcel.readInt();
      boolean bool1 = false;
      if (i != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      legacy = bool2;
      if (paramAnonymousParcel.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      blocking = bool2;
      if (paramAnonymousParcel.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      allowBypass = bool2;
      if (paramAnonymousParcel.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      allowIPv4 = bool2;
      boolean bool2 = bool1;
      if (paramAnonymousParcel.readInt() != 0) {
        bool2 = true;
      }
      allowIPv6 = bool2;
      underlyingNetworks = ((Network[])paramAnonymousParcel.createTypedArray(Network.CREATOR));
      return localVpnConfig;
    }
    
    public VpnConfig[] newArray(int paramAnonymousInt)
    {
      return new VpnConfig[paramAnonymousInt];
    }
  };
  public static final String DIALOGS_PACKAGE = "com.android.vpndialogs";
  public static final String LEGACY_VPN = "[Legacy VPN]";
  public static final String SERVICE_INTERFACE = "android.net.VpnService";
  public List<LinkAddress> addresses = new ArrayList();
  public boolean allowBypass;
  public boolean allowIPv4;
  public boolean allowIPv6;
  public List<String> allowedApplications;
  public boolean blocking;
  public PendingIntent configureIntent;
  public List<String> disallowedApplications;
  public List<String> dnsServers;
  public String interfaze;
  public boolean legacy;
  public int mtu = -1;
  public List<RouteInfo> routes = new ArrayList();
  public List<String> searchDomains;
  public String session;
  public long startTime = -1L;
  public Network[] underlyingNetworks;
  public String user;
  
  public VpnConfig() {}
  
  public static Intent getIntentForConfirmation()
  {
    Intent localIntent = new Intent();
    ComponentName localComponentName = ComponentName.unflattenFromString(Resources.getSystem().getString(17039681));
    localIntent.setClassName(localComponentName.getPackageName(), localComponentName.getClassName());
    return localIntent;
  }
  
  public static PendingIntent getIntentForStatusPanel(Context paramContext)
  {
    Intent localIntent = new Intent();
    localIntent.setClassName("com.android.vpndialogs", "com.android.vpndialogs.ManageDialog");
    localIntent.addFlags(1350565888);
    return PendingIntent.getActivityAsUser(paramContext, 0, localIntent, 0, null, UserHandle.CURRENT);
  }
  
  public static CharSequence getVpnLabel(Context paramContext, String paramString)
    throws PackageManager.NameNotFoundException
  {
    paramContext = paramContext.getPackageManager();
    Object localObject = new Intent("android.net.VpnService");
    ((Intent)localObject).setPackage(paramString);
    localObject = paramContext.queryIntentServices((Intent)localObject, 0);
    if ((localObject != null) && (((List)localObject).size() == 1)) {
      return ((ResolveInfo)((List)localObject).get(0)).loadLabel(paramContext);
    }
    return paramContext.getApplicationInfo(paramString, 0).loadLabel(paramContext);
  }
  
  public void addLegacyAddresses(String paramString)
  {
    if (paramString.trim().equals("")) {
      return;
    }
    String[] arrayOfString = paramString.trim().split(" ");
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++)
    {
      paramString = new LinkAddress(arrayOfString[j]);
      addresses.add(paramString);
      updateAllowedFamilies(paramString.getAddress());
    }
  }
  
  public void addLegacyRoutes(String paramString)
  {
    if (paramString.trim().equals("")) {
      return;
    }
    paramString = paramString.trim().split(" ");
    int i = paramString.length;
    for (int j = 0; j < i; j++)
    {
      RouteInfo localRouteInfo = new RouteInfo(new IpPrefix(paramString[j]), null);
      routes.add(localRouteInfo);
      updateAllowedFamilies(localRouteInfo.getDestination().getAddress());
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void updateAllowedFamilies(InetAddress paramInetAddress)
  {
    if ((paramInetAddress instanceof Inet4Address)) {
      allowIPv4 = true;
    } else {
      allowIPv6 = true;
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(user);
    paramParcel.writeString(interfaze);
    paramParcel.writeString(session);
    paramParcel.writeInt(mtu);
    paramParcel.writeTypedList(addresses);
    paramParcel.writeTypedList(routes);
    paramParcel.writeStringList(dnsServers);
    paramParcel.writeStringList(searchDomains);
    paramParcel.writeStringList(allowedApplications);
    paramParcel.writeStringList(disallowedApplications);
    paramParcel.writeParcelable(configureIntent, paramInt);
    paramParcel.writeLong(startTime);
    paramParcel.writeInt(legacy);
    paramParcel.writeInt(blocking);
    paramParcel.writeInt(allowBypass);
    paramParcel.writeInt(allowIPv4);
    paramParcel.writeInt(allowIPv6);
    paramParcel.writeTypedArray(underlyingNetworks, paramInt);
  }
}
