package android.net;

import android.annotation.SystemApi;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageManager;
import android.content.pm.IPackageManager.Stub;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.system.OsConstants;
import com.android.internal.net.VpnConfig;
import java.io.FileDescriptor;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class VpnService
  extends Service
{
  public static final String SERVICE_INTERFACE = "android.net.VpnService";
  public static final String SERVICE_META_DATA_SUPPORTS_ALWAYS_ON = "android.net.VpnService.SUPPORTS_ALWAYS_ON";
  
  public VpnService() {}
  
  private static void check(InetAddress paramInetAddress, int paramInt)
  {
    if (!paramInetAddress.isLoopbackAddress())
    {
      if ((paramInetAddress instanceof Inet4Address))
      {
        if ((paramInt < 0) || (paramInt > 32)) {
          throw new IllegalArgumentException("Bad prefixLength");
        }
      }
      else
      {
        if (!(paramInetAddress instanceof Inet6Address)) {
          break label66;
        }
        if ((paramInt < 0) || (paramInt > 128)) {
          break label56;
        }
      }
      return;
      label56:
      throw new IllegalArgumentException("Bad prefixLength");
      label66:
      throw new IllegalArgumentException("Unsupported family");
    }
    throw new IllegalArgumentException("Bad address");
  }
  
  private static IConnectivityManager getService()
  {
    return IConnectivityManager.Stub.asInterface(ServiceManager.getService("connectivity"));
  }
  
  public static Intent prepare(Context paramContext)
  {
    try
    {
      boolean bool = getService().prepareVpn(paramContext.getPackageName(), null, paramContext.getUserId());
      if (bool) {
        return null;
      }
    }
    catch (RemoteException paramContext) {}
    return VpnConfig.getIntentForConfirmation();
  }
  
  @SystemApi
  public static void prepareAndAuthorize(Context paramContext)
  {
    IConnectivityManager localIConnectivityManager = getService();
    String str = paramContext.getPackageName();
    try
    {
      int i = paramContext.getUserId();
      if (!localIConnectivityManager.prepareVpn(str, null, i)) {
        localIConnectivityManager.prepareVpn(null, str, i);
      }
      localIConnectivityManager.setVpnPackageAuthorization(str, i, true);
    }
    catch (RemoteException paramContext) {}
  }
  
  public boolean addAddress(InetAddress paramInetAddress, int paramInt)
  {
    check(paramInetAddress, paramInt);
    try
    {
      boolean bool = getService().addVpnAddress(paramInetAddress.getHostAddress(), paramInt);
      return bool;
    }
    catch (RemoteException paramInetAddress)
    {
      throw new IllegalStateException(paramInetAddress);
    }
  }
  
  public IBinder onBind(Intent paramIntent)
  {
    if ((paramIntent != null) && ("android.net.VpnService".equals(paramIntent.getAction()))) {
      return new Callback(null);
    }
    return null;
  }
  
  public void onRevoke()
  {
    stopSelf();
  }
  
  public boolean protect(int paramInt)
  {
    return NetworkUtils.protectFromVpn(paramInt);
  }
  
  public boolean protect(DatagramSocket paramDatagramSocket)
  {
    return protect(paramDatagramSocket.getFileDescriptor$().getInt$());
  }
  
  public boolean protect(Socket paramSocket)
  {
    return protect(paramSocket.getFileDescriptor$().getInt$());
  }
  
  public boolean removeAddress(InetAddress paramInetAddress, int paramInt)
  {
    check(paramInetAddress, paramInt);
    try
    {
      boolean bool = getService().removeVpnAddress(paramInetAddress.getHostAddress(), paramInt);
      return bool;
    }
    catch (RemoteException paramInetAddress)
    {
      throw new IllegalStateException(paramInetAddress);
    }
  }
  
  public boolean setUnderlyingNetworks(Network[] paramArrayOfNetwork)
  {
    try
    {
      boolean bool = getService().setUnderlyingNetworksForVpn(paramArrayOfNetwork);
      return bool;
    }
    catch (RemoteException paramArrayOfNetwork)
    {
      throw new IllegalStateException(paramArrayOfNetwork);
    }
  }
  
  public class Builder
  {
    private final List<LinkAddress> mAddresses = new ArrayList();
    private final VpnConfig mConfig = new VpnConfig();
    private final List<RouteInfo> mRoutes = new ArrayList();
    
    public Builder()
    {
      mConfig.user = getClass().getName();
    }
    
    private void verifyApp(String paramString)
      throws PackageManager.NameNotFoundException
    {
      IPackageManager localIPackageManager = IPackageManager.Stub.asInterface(ServiceManager.getService("package"));
      try
      {
        localIPackageManager.getApplicationInfo(paramString, 0, UserHandle.getCallingUserId());
        return;
      }
      catch (RemoteException paramString)
      {
        throw new IllegalStateException(paramString);
      }
    }
    
    public Builder addAddress(String paramString, int paramInt)
    {
      return addAddress(InetAddress.parseNumericAddress(paramString), paramInt);
    }
    
    public Builder addAddress(InetAddress paramInetAddress, int paramInt)
    {
      VpnService.check(paramInetAddress, paramInt);
      if (!paramInetAddress.isAnyLocalAddress())
      {
        mAddresses.add(new LinkAddress(paramInetAddress, paramInt));
        mConfig.updateAllowedFamilies(paramInetAddress);
        return this;
      }
      throw new IllegalArgumentException("Bad address");
    }
    
    public Builder addAllowedApplication(String paramString)
      throws PackageManager.NameNotFoundException
    {
      if (mConfig.disallowedApplications == null)
      {
        verifyApp(paramString);
        if (mConfig.allowedApplications == null) {
          mConfig.allowedApplications = new ArrayList();
        }
        mConfig.allowedApplications.add(paramString);
        return this;
      }
      throw new UnsupportedOperationException("addDisallowedApplication already called");
    }
    
    public Builder addDisallowedApplication(String paramString)
      throws PackageManager.NameNotFoundException
    {
      if (mConfig.allowedApplications == null)
      {
        verifyApp(paramString);
        if (mConfig.disallowedApplications == null) {
          mConfig.disallowedApplications = new ArrayList();
        }
        mConfig.disallowedApplications.add(paramString);
        return this;
      }
      throw new UnsupportedOperationException("addAllowedApplication already called");
    }
    
    public Builder addDnsServer(String paramString)
    {
      return addDnsServer(InetAddress.parseNumericAddress(paramString));
    }
    
    public Builder addDnsServer(InetAddress paramInetAddress)
    {
      if ((!paramInetAddress.isLoopbackAddress()) && (!paramInetAddress.isAnyLocalAddress()))
      {
        if (mConfig.dnsServers == null) {
          mConfig.dnsServers = new ArrayList();
        }
        mConfig.dnsServers.add(paramInetAddress.getHostAddress());
        return this;
      }
      throw new IllegalArgumentException("Bad address");
    }
    
    public Builder addRoute(String paramString, int paramInt)
    {
      return addRoute(InetAddress.parseNumericAddress(paramString), paramInt);
    }
    
    public Builder addRoute(InetAddress paramInetAddress, int paramInt)
    {
      VpnService.check(paramInetAddress, paramInt);
      int i = paramInt / 8;
      byte[] arrayOfByte = paramInetAddress.getAddress();
      if (i < arrayOfByte.length)
      {
        arrayOfByte[i] = ((byte)(byte)(arrayOfByte[i] << paramInt % 8));
        while (i < arrayOfByte.length) {
          if (arrayOfByte[i] == 0) {
            i++;
          } else {
            throw new IllegalArgumentException("Bad address");
          }
        }
      }
      mRoutes.add(new RouteInfo(new IpPrefix(paramInetAddress, paramInt), null));
      mConfig.updateAllowedFamilies(paramInetAddress);
      return this;
    }
    
    public Builder addSearchDomain(String paramString)
    {
      if (mConfig.searchDomains == null) {
        mConfig.searchDomains = new ArrayList();
      }
      mConfig.searchDomains.add(paramString);
      return this;
    }
    
    public Builder allowBypass()
    {
      mConfig.allowBypass = true;
      return this;
    }
    
    public Builder allowFamily(int paramInt)
    {
      if (paramInt == OsConstants.AF_INET)
      {
        mConfig.allowIPv4 = true;
      }
      else
      {
        if (paramInt != OsConstants.AF_INET6) {
          break label35;
        }
        mConfig.allowIPv6 = true;
      }
      return this;
      label35:
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramInt);
      localStringBuilder.append(" is neither ");
      localStringBuilder.append(OsConstants.AF_INET);
      localStringBuilder.append(" nor ");
      localStringBuilder.append(OsConstants.AF_INET6);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public ParcelFileDescriptor establish()
    {
      mConfig.addresses = mAddresses;
      mConfig.routes = mRoutes;
      try
      {
        ParcelFileDescriptor localParcelFileDescriptor = VpnService.access$200().establishVpn(mConfig);
        return localParcelFileDescriptor;
      }
      catch (RemoteException localRemoteException)
      {
        throw new IllegalStateException(localRemoteException);
      }
    }
    
    public Builder setBlocking(boolean paramBoolean)
    {
      mConfig.blocking = paramBoolean;
      return this;
    }
    
    public Builder setConfigureIntent(PendingIntent paramPendingIntent)
    {
      mConfig.configureIntent = paramPendingIntent;
      return this;
    }
    
    public Builder setMtu(int paramInt)
    {
      if (paramInt > 0)
      {
        mConfig.mtu = paramInt;
        return this;
      }
      throw new IllegalArgumentException("Bad mtu");
    }
    
    public Builder setSession(String paramString)
    {
      mConfig.session = paramString;
      return this;
    }
    
    public Builder setUnderlyingNetworks(Network[] paramArrayOfNetwork)
    {
      VpnConfig localVpnConfig = mConfig;
      if (paramArrayOfNetwork != null) {
        paramArrayOfNetwork = (Network[])paramArrayOfNetwork.clone();
      } else {
        paramArrayOfNetwork = null;
      }
      underlyingNetworks = paramArrayOfNetwork;
      return this;
    }
  }
  
  private class Callback
    extends Binder
  {
    private Callback() {}
    
    protected boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
    {
      if (paramInt1 == 16777215)
      {
        onRevoke();
        return true;
      }
      return false;
    }
  }
}
