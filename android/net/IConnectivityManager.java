package android.net;

import android.app.PendingIntent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Messenger;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ResultReceiver;
import com.android.internal.net.LegacyVpnInfo;
import com.android.internal.net.VpnConfig;
import com.android.internal.net.VpnInfo;
import com.android.internal.net.VpnProfile;

public abstract interface IConnectivityManager
  extends IInterface
{
  public abstract boolean addVpnAddress(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract int checkMobileProvisioning(int paramInt)
    throws RemoteException;
  
  public abstract ParcelFileDescriptor establishVpn(VpnConfig paramVpnConfig)
    throws RemoteException;
  
  public abstract void factoryReset()
    throws RemoteException;
  
  public abstract LinkProperties getActiveLinkProperties()
    throws RemoteException;
  
  public abstract Network getActiveNetwork()
    throws RemoteException;
  
  public abstract Network getActiveNetworkForUid(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract NetworkInfo getActiveNetworkInfo()
    throws RemoteException;
  
  public abstract NetworkInfo getActiveNetworkInfoForUid(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract NetworkQuotaInfo getActiveNetworkQuotaInfo()
    throws RemoteException;
  
  public abstract NetworkInfo[] getAllNetworkInfo()
    throws RemoteException;
  
  public abstract NetworkState[] getAllNetworkState()
    throws RemoteException;
  
  public abstract Network[] getAllNetworks()
    throws RemoteException;
  
  public abstract VpnInfo[] getAllVpnInfo()
    throws RemoteException;
  
  public abstract String getAlwaysOnVpnPackage(int paramInt)
    throws RemoteException;
  
  public abstract String getCaptivePortalServerUrl()
    throws RemoteException;
  
  public abstract NetworkCapabilities[] getDefaultNetworkCapabilitiesForUser(int paramInt)
    throws RemoteException;
  
  public abstract ProxyInfo getGlobalProxy()
    throws RemoteException;
  
  public abstract int getLastTetherError(String paramString)
    throws RemoteException;
  
  public abstract LegacyVpnInfo getLegacyVpnInfo(int paramInt)
    throws RemoteException;
  
  public abstract LinkProperties getLinkProperties(Network paramNetwork)
    throws RemoteException;
  
  public abstract LinkProperties getLinkPropertiesForType(int paramInt)
    throws RemoteException;
  
  public abstract String getMobileProvisioningUrl()
    throws RemoteException;
  
  public abstract int getMultipathPreference(Network paramNetwork)
    throws RemoteException;
  
  public abstract NetworkCapabilities getNetworkCapabilities(Network paramNetwork)
    throws RemoteException;
  
  public abstract Network getNetworkForType(int paramInt)
    throws RemoteException;
  
  public abstract NetworkInfo getNetworkInfo(int paramInt)
    throws RemoteException;
  
  public abstract NetworkInfo getNetworkInfoForUid(Network paramNetwork, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract byte[] getNetworkWatchlistConfigHash()
    throws RemoteException;
  
  public abstract ProxyInfo getProxyForNetwork(Network paramNetwork)
    throws RemoteException;
  
  public abstract int getRestoreDefaultNetworkDelay(int paramInt)
    throws RemoteException;
  
  public abstract String[] getTetherableBluetoothRegexs()
    throws RemoteException;
  
  public abstract String[] getTetherableIfaces()
    throws RemoteException;
  
  public abstract String[] getTetherableUsbRegexs()
    throws RemoteException;
  
  public abstract String[] getTetherableWifiRegexs()
    throws RemoteException;
  
  public abstract String[] getTetheredDhcpRanges()
    throws RemoteException;
  
  public abstract String[] getTetheredIfaces()
    throws RemoteException;
  
  public abstract String[] getTetheringErroredIfaces()
    throws RemoteException;
  
  public abstract VpnConfig getVpnConfig(int paramInt)
    throws RemoteException;
  
  public abstract boolean isActiveNetworkMetered()
    throws RemoteException;
  
  public abstract boolean isAlwaysOnVpnPackageSupported(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract boolean isNetworkSupported(int paramInt)
    throws RemoteException;
  
  public abstract boolean isTetheringSupported(String paramString)
    throws RemoteException;
  
  public abstract NetworkRequest listenForNetwork(NetworkCapabilities paramNetworkCapabilities, Messenger paramMessenger, IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void pendingListenForNetwork(NetworkCapabilities paramNetworkCapabilities, PendingIntent paramPendingIntent)
    throws RemoteException;
  
  public abstract NetworkRequest pendingRequestForNetwork(NetworkCapabilities paramNetworkCapabilities, PendingIntent paramPendingIntent)
    throws RemoteException;
  
  public abstract boolean prepareVpn(String paramString1, String paramString2, int paramInt)
    throws RemoteException;
  
  public abstract int registerNetworkAgent(Messenger paramMessenger, NetworkInfo paramNetworkInfo, LinkProperties paramLinkProperties, NetworkCapabilities paramNetworkCapabilities, int paramInt, NetworkMisc paramNetworkMisc)
    throws RemoteException;
  
  public abstract void registerNetworkFactory(Messenger paramMessenger, String paramString)
    throws RemoteException;
  
  public abstract void releaseNetworkRequest(NetworkRequest paramNetworkRequest)
    throws RemoteException;
  
  public abstract void releasePendingNetworkRequest(PendingIntent paramPendingIntent)
    throws RemoteException;
  
  public abstract boolean removeVpnAddress(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void reportInetCondition(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void reportNetworkConnectivity(Network paramNetwork, boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean requestBandwidthUpdate(Network paramNetwork)
    throws RemoteException;
  
  public abstract NetworkRequest requestNetwork(NetworkCapabilities paramNetworkCapabilities, Messenger paramMessenger, int paramInt1, IBinder paramIBinder, int paramInt2)
    throws RemoteException;
  
  public abstract boolean requestRouteToHostAddress(int paramInt, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void setAcceptUnvalidated(Network paramNetwork, boolean paramBoolean1, boolean paramBoolean2)
    throws RemoteException;
  
  public abstract void setAirplaneMode(boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean setAlwaysOnVpnPackage(int paramInt, String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setAvoidUnvalidated(Network paramNetwork)
    throws RemoteException;
  
  public abstract void setGlobalProxy(ProxyInfo paramProxyInfo)
    throws RemoteException;
  
  public abstract void setProvisioningNotificationVisible(boolean paramBoolean, int paramInt, String paramString)
    throws RemoteException;
  
  public abstract boolean setUnderlyingNetworksForVpn(Network[] paramArrayOfNetwork)
    throws RemoteException;
  
  public abstract int setUsbTethering(boolean paramBoolean, String paramString)
    throws RemoteException;
  
  public abstract void setVpnPackageAuthorization(String paramString, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void startCaptivePortalApp(Network paramNetwork)
    throws RemoteException;
  
  public abstract void startLegacyVpn(VpnProfile paramVpnProfile)
    throws RemoteException;
  
  public abstract void startNattKeepalive(Network paramNetwork, int paramInt1, Messenger paramMessenger, IBinder paramIBinder, String paramString1, int paramInt2, String paramString2)
    throws RemoteException;
  
  public abstract void startTethering(int paramInt, ResultReceiver paramResultReceiver, boolean paramBoolean, String paramString)
    throws RemoteException;
  
  public abstract void stopKeepalive(Network paramNetwork, int paramInt)
    throws RemoteException;
  
  public abstract void stopTethering(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract int tether(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void unregisterNetworkFactory(Messenger paramMessenger)
    throws RemoteException;
  
  public abstract int untether(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract boolean updateLockdownVpn()
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IConnectivityManager
  {
    private static final String DESCRIPTOR = "android.net.IConnectivityManager";
    static final int TRANSACTION_addVpnAddress = 69;
    static final int TRANSACTION_checkMobileProvisioning = 50;
    static final int TRANSACTION_establishVpn = 41;
    static final int TRANSACTION_factoryReset = 72;
    static final int TRANSACTION_getActiveLinkProperties = 12;
    static final int TRANSACTION_getActiveNetwork = 1;
    static final int TRANSACTION_getActiveNetworkForUid = 2;
    static final int TRANSACTION_getActiveNetworkInfo = 3;
    static final int TRANSACTION_getActiveNetworkInfoForUid = 4;
    static final int TRANSACTION_getActiveNetworkQuotaInfo = 17;
    static final int TRANSACTION_getAllNetworkInfo = 7;
    static final int TRANSACTION_getAllNetworkState = 16;
    static final int TRANSACTION_getAllNetworks = 9;
    static final int TRANSACTION_getAllVpnInfo = 45;
    static final int TRANSACTION_getAlwaysOnVpnPackage = 49;
    static final int TRANSACTION_getCaptivePortalServerUrl = 75;
    static final int TRANSACTION_getDefaultNetworkCapabilitiesForUser = 10;
    static final int TRANSACTION_getGlobalProxy = 36;
    static final int TRANSACTION_getLastTetherError = 22;
    static final int TRANSACTION_getLegacyVpnInfo = 44;
    static final int TRANSACTION_getLinkProperties = 14;
    static final int TRANSACTION_getLinkPropertiesForType = 13;
    static final int TRANSACTION_getMobileProvisioningUrl = 51;
    static final int TRANSACTION_getMultipathPreference = 67;
    static final int TRANSACTION_getNetworkCapabilities = 15;
    static final int TRANSACTION_getNetworkForType = 8;
    static final int TRANSACTION_getNetworkInfo = 5;
    static final int TRANSACTION_getNetworkInfoForUid = 6;
    static final int TRANSACTION_getNetworkWatchlistConfigHash = 76;
    static final int TRANSACTION_getProxyForNetwork = 38;
    static final int TRANSACTION_getRestoreDefaultNetworkDelay = 68;
    static final int TRANSACTION_getTetherableBluetoothRegexs = 32;
    static final int TRANSACTION_getTetherableIfaces = 26;
    static final int TRANSACTION_getTetherableUsbRegexs = 30;
    static final int TRANSACTION_getTetherableWifiRegexs = 31;
    static final int TRANSACTION_getTetheredDhcpRanges = 29;
    static final int TRANSACTION_getTetheredIfaces = 27;
    static final int TRANSACTION_getTetheringErroredIfaces = 28;
    static final int TRANSACTION_getVpnConfig = 42;
    static final int TRANSACTION_isActiveNetworkMetered = 18;
    static final int TRANSACTION_isAlwaysOnVpnPackageSupported = 47;
    static final int TRANSACTION_isNetworkSupported = 11;
    static final int TRANSACTION_isTetheringSupported = 23;
    static final int TRANSACTION_listenForNetwork = 61;
    static final int TRANSACTION_pendingListenForNetwork = 62;
    static final int TRANSACTION_pendingRequestForNetwork = 59;
    static final int TRANSACTION_prepareVpn = 39;
    static final int TRANSACTION_registerNetworkAgent = 57;
    static final int TRANSACTION_registerNetworkFactory = 54;
    static final int TRANSACTION_releaseNetworkRequest = 63;
    static final int TRANSACTION_releasePendingNetworkRequest = 60;
    static final int TRANSACTION_removeVpnAddress = 70;
    static final int TRANSACTION_reportInetCondition = 34;
    static final int TRANSACTION_reportNetworkConnectivity = 35;
    static final int TRANSACTION_requestBandwidthUpdate = 55;
    static final int TRANSACTION_requestNetwork = 58;
    static final int TRANSACTION_requestRouteToHostAddress = 19;
    static final int TRANSACTION_setAcceptUnvalidated = 64;
    static final int TRANSACTION_setAirplaneMode = 53;
    static final int TRANSACTION_setAlwaysOnVpnPackage = 48;
    static final int TRANSACTION_setAvoidUnvalidated = 65;
    static final int TRANSACTION_setGlobalProxy = 37;
    static final int TRANSACTION_setProvisioningNotificationVisible = 52;
    static final int TRANSACTION_setUnderlyingNetworksForVpn = 71;
    static final int TRANSACTION_setUsbTethering = 33;
    static final int TRANSACTION_setVpnPackageAuthorization = 40;
    static final int TRANSACTION_startCaptivePortalApp = 66;
    static final int TRANSACTION_startLegacyVpn = 43;
    static final int TRANSACTION_startNattKeepalive = 73;
    static final int TRANSACTION_startTethering = 24;
    static final int TRANSACTION_stopKeepalive = 74;
    static final int TRANSACTION_stopTethering = 25;
    static final int TRANSACTION_tether = 20;
    static final int TRANSACTION_unregisterNetworkFactory = 56;
    static final int TRANSACTION_untether = 21;
    static final int TRANSACTION_updateLockdownVpn = 46;
    
    public Stub()
    {
      attachInterface(this, "android.net.IConnectivityManager");
    }
    
    public static IConnectivityManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.IConnectivityManager");
      if ((localIInterface != null) && ((localIInterface instanceof IConnectivityManager))) {
        return (IConnectivityManager)localIInterface;
      }
      return new Proxy(paramIBinder);
    }
    
    public IBinder asBinder()
    {
      return this;
    }
    
    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      if (paramInt1 != 1598968902)
      {
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        boolean bool4 = false;
        boolean bool5 = false;
        boolean bool6 = false;
        boolean bool7 = false;
        boolean bool8 = false;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        LinkProperties localLinkProperties = null;
        NetworkCapabilities localNetworkCapabilities = null;
        Object localObject7 = null;
        Object localObject8 = null;
        Object localObject9 = null;
        Object localObject10 = null;
        Object localObject11 = null;
        Object localObject12 = null;
        Object localObject13 = null;
        Object localObject14 = null;
        Object localObject15 = null;
        Object localObject16 = null;
        Object localObject17 = null;
        Object localObject18 = null;
        Object localObject19 = null;
        Object localObject20 = null;
        Object localObject21 = null;
        Object localObject22 = null;
        Object localObject23 = null;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 76: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramParcel1 = getNetworkWatchlistConfigHash();
          paramParcel2.writeNoException();
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        case 75: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramParcel1 = getCaptivePortalServerUrl();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 74: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          if (paramParcel1.readInt() != 0) {
            localObject23 = (Network)Network.CREATOR.createFromParcel(paramParcel1);
          }
          stopKeepalive((Network)localObject23, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 73: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          if (paramParcel1.readInt() != 0) {
            localObject23 = (Network)Network.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject23 = null;
          }
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Messenger)Messenger.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          startNattKeepalive((Network)localObject23, paramInt1, (Messenger)localObject1, paramParcel1.readStrongBinder(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 72: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          factoryReset();
          paramParcel2.writeNoException();
          return true;
        case 71: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramInt1 = setUnderlyingNetworksForVpn((Network[])paramParcel1.createTypedArray(Network.CREATOR));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 70: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramInt1 = removeVpnAddress(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 69: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramInt1 = addVpnAddress(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 68: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramInt1 = getRestoreDefaultNetworkDelay(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 67: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Network)Network.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          paramInt1 = getMultipathPreference(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 66: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Network)Network.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          startCaptivePortalApp(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 65: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Network)Network.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          setAvoidUnvalidated(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 64: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          if (paramParcel1.readInt() != 0) {
            localObject23 = (Network)Network.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject23 = localObject5;
          }
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool8 = true;
          }
          setAcceptUnvalidated((Network)localObject23, bool1, bool8);
          paramParcel2.writeNoException();
          return true;
        case 63: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (NetworkRequest)NetworkRequest.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject6;
          }
          releaseNetworkRequest(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 62: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          if (paramParcel1.readInt() != 0) {
            localObject23 = (NetworkCapabilities)NetworkCapabilities.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject23 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localLinkProperties;
          }
          pendingListenForNetwork((NetworkCapabilities)localObject23, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 61: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          if (paramParcel1.readInt() != 0) {
            localObject23 = (NetworkCapabilities)NetworkCapabilities.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject23 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Messenger)Messenger.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localNetworkCapabilities;
          }
          paramParcel1 = listenForNetwork((NetworkCapabilities)localObject23, (Messenger)localObject1, paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 60: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject7;
          }
          releasePendingNetworkRequest(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 59: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          if (paramParcel1.readInt() != 0) {
            localObject23 = (NetworkCapabilities)NetworkCapabilities.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject23 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PendingIntent)PendingIntent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject8;
          }
          paramParcel1 = pendingRequestForNetwork((NetworkCapabilities)localObject23, paramParcel1);
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 58: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          if (paramParcel1.readInt() != 0) {
            localObject23 = (NetworkCapabilities)NetworkCapabilities.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject23 = null;
          }
          if (paramParcel1.readInt() != 0) {}
          for (localObject1 = (Messenger)Messenger.CREATOR.createFromParcel(paramParcel1);; localObject1 = localObject9) {
            break;
          }
          paramParcel1 = requestNetwork((NetworkCapabilities)localObject23, (Messenger)localObject1, paramParcel1.readInt(), paramParcel1.readStrongBinder(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 57: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          if (paramParcel1.readInt() != 0) {
            localObject23 = (Messenger)Messenger.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject23 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject1 = (NetworkInfo)NetworkInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localLinkProperties = (LinkProperties)LinkProperties.CREATOR.createFromParcel(paramParcel1);
          } else {
            localLinkProperties = null;
          }
          if (paramParcel1.readInt() != 0) {
            localNetworkCapabilities = (NetworkCapabilities)NetworkCapabilities.CREATOR.createFromParcel(paramParcel1);
          } else {
            localNetworkCapabilities = null;
          }
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (NetworkMisc)NetworkMisc.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = localObject10) {
            break;
          }
          paramInt1 = registerNetworkAgent((Messenger)localObject23, (NetworkInfo)localObject1, localLinkProperties, localNetworkCapabilities, paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 56: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Messenger)Messenger.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject11;
          }
          unregisterNetworkFactory(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 55: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Network)Network.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject12;
          }
          paramInt1 = requestBandwidthUpdate(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 54: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          if (paramParcel1.readInt() != 0) {
            localObject23 = (Messenger)Messenger.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject23 = localObject13;
          }
          registerNetworkFactory((Messenger)localObject23, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 53: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          }
          setAirplaneMode(bool1);
          paramParcel2.writeNoException();
          return true;
        case 52: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          bool1 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          }
          setProvisioningNotificationVisible(bool1, paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 51: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramParcel1 = getMobileProvisioningUrl();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 50: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramInt1 = checkMobileProvisioning(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 49: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramParcel1 = getAlwaysOnVpnPackage(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 48: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramInt1 = paramParcel1.readInt();
          localObject23 = paramParcel1.readString();
          bool1 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          }
          paramInt1 = setAlwaysOnVpnPackage(paramInt1, (String)localObject23, bool1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 47: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramInt1 = isAlwaysOnVpnPackageSupported(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 46: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramInt1 = updateLockdownVpn();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 45: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramParcel1 = getAllVpnInfo();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 44: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramParcel1 = getLegacyVpnInfo(paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 43: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (VpnProfile)VpnProfile.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject14;
          }
          startLegacyVpn(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 42: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramParcel1 = getVpnConfig(paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 41: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (VpnConfig)VpnConfig.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject15;
          }
          paramParcel1 = establishVpn(paramParcel1);
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 40: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          localObject23 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          bool1 = bool4;
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          }
          setVpnPackageAuthorization((String)localObject23, paramInt1, bool1);
          paramParcel2.writeNoException();
          return true;
        case 39: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramInt1 = prepareVpn(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 38: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Network)Network.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject16;
          }
          paramParcel1 = getProxyForNetwork(paramParcel1);
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 37: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ProxyInfo)ProxyInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject17;
          }
          setGlobalProxy(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 36: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramParcel1 = getGlobalProxy();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 35: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          if (paramParcel1.readInt() != 0) {
            localObject23 = (Network)Network.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject23 = localObject18;
          }
          bool1 = bool5;
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          }
          reportNetworkConnectivity((Network)localObject23, bool1);
          paramParcel2.writeNoException();
          return true;
        case 34: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          reportInetCondition(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 33: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          bool1 = bool6;
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          }
          paramInt1 = setUsbTethering(bool1, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 32: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramParcel1 = getTetherableBluetoothRegexs();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 31: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramParcel1 = getTetherableWifiRegexs();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 30: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramParcel1 = getTetherableUsbRegexs();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 29: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramParcel1 = getTetheredDhcpRanges();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 28: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramParcel1 = getTetheringErroredIfaces();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 27: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramParcel1 = getTetheredIfaces();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 26: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramParcel1 = getTetherableIfaces();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 25: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          stopTethering(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject23 = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject23 = localObject19;
          }
          bool1 = bool7;
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          }
          startTethering(paramInt1, (ResultReceiver)localObject23, bool1, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramInt1 = isTetheringSupported(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramInt1 = getLastTetherError(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramInt1 = untether(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramInt1 = tether(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramInt1 = requestRouteToHostAddress(paramParcel1.readInt(), paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramInt1 = isActiveNetworkMetered();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramParcel1 = getActiveNetworkQuotaInfo();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramParcel1 = getAllNetworkState();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Network)Network.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject20;
          }
          paramParcel1 = getNetworkCapabilities(paramParcel1);
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Network)Network.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject21;
          }
          paramParcel1 = getLinkProperties(paramParcel1);
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramParcel1 = getLinkPropertiesForType(paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramParcel1 = getActiveLinkProperties();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramInt1 = isNetworkSupported(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramParcel1 = getDefaultNetworkCapabilitiesForUser(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramParcel1 = getAllNetworks();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramParcel1 = getNetworkForType(paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramParcel1 = getAllNetworkInfo();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          if (paramParcel1.readInt() != 0) {
            localObject23 = (Network)Network.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject23 = localObject22;
          }
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          paramParcel1 = getNetworkInfoForUid((Network)localObject23, paramInt1, bool1);
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramParcel1 = getNetworkInfo(paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          paramParcel1 = getActiveNetworkInfoForUid(paramInt1, bool1);
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramParcel1 = getActiveNetworkInfo();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.net.IConnectivityManager");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          paramParcel1 = getActiveNetworkForUid(paramInt1, bool1);
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        }
        paramParcel1.enforceInterface("android.net.IConnectivityManager");
        paramParcel1 = getActiveNetwork();
        paramParcel2.writeNoException();
        if (paramParcel1 != null)
        {
          paramParcel2.writeInt(1);
          paramParcel1.writeToParcel(paramParcel2, 1);
        }
        else
        {
          paramParcel2.writeInt(0);
        }
        return true;
      }
      paramParcel2.writeString("android.net.IConnectivityManager");
      return true;
    }
    
    private static class Proxy
      implements IConnectivityManager
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public boolean addVpnAddress(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(69, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public int checkMobileProvisioning(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(50, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParcelFileDescriptor establishVpn(VpnConfig paramVpnConfig)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          if (paramVpnConfig != null)
          {
            localParcel1.writeInt(1);
            paramVpnConfig.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(41, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramVpnConfig = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(localParcel2);
          } else {
            paramVpnConfig = null;
          }
          return paramVpnConfig;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void factoryReset()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          mRemote.transact(72, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public LinkProperties getActiveLinkProperties()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          LinkProperties localLinkProperties;
          if (localParcel2.readInt() != 0) {
            localLinkProperties = (LinkProperties)LinkProperties.CREATOR.createFromParcel(localParcel2);
          } else {
            localLinkProperties = null;
          }
          return localLinkProperties;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Network getActiveNetwork()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Network localNetwork;
          if (localParcel2.readInt() != 0) {
            localNetwork = (Network)Network.CREATOR.createFromParcel(localParcel2);
          } else {
            localNetwork = null;
          }
          return localNetwork;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Network getActiveNetworkForUid(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Network localNetwork;
          if (localParcel2.readInt() != 0) {
            localNetwork = (Network)Network.CREATOR.createFromParcel(localParcel2);
          } else {
            localNetwork = null;
          }
          return localNetwork;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NetworkInfo getActiveNetworkInfo()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          NetworkInfo localNetworkInfo;
          if (localParcel2.readInt() != 0) {
            localNetworkInfo = (NetworkInfo)NetworkInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localNetworkInfo = null;
          }
          return localNetworkInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NetworkInfo getActiveNetworkInfoForUid(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          NetworkInfo localNetworkInfo;
          if (localParcel2.readInt() != 0) {
            localNetworkInfo = (NetworkInfo)NetworkInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localNetworkInfo = null;
          }
          return localNetworkInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NetworkQuotaInfo getActiveNetworkQuotaInfo()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          NetworkQuotaInfo localNetworkQuotaInfo;
          if (localParcel2.readInt() != 0) {
            localNetworkQuotaInfo = (NetworkQuotaInfo)NetworkQuotaInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localNetworkQuotaInfo = null;
          }
          return localNetworkQuotaInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NetworkInfo[] getAllNetworkInfo()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          NetworkInfo[] arrayOfNetworkInfo = (NetworkInfo[])localParcel2.createTypedArray(NetworkInfo.CREATOR);
          return arrayOfNetworkInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NetworkState[] getAllNetworkState()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          NetworkState[] arrayOfNetworkState = (NetworkState[])localParcel2.createTypedArray(NetworkState.CREATOR);
          return arrayOfNetworkState;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Network[] getAllNetworks()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Network[] arrayOfNetwork = (Network[])localParcel2.createTypedArray(Network.CREATOR);
          return arrayOfNetwork;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public VpnInfo[] getAllVpnInfo()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          mRemote.transact(45, localParcel1, localParcel2, 0);
          localParcel2.readException();
          VpnInfo[] arrayOfVpnInfo = (VpnInfo[])localParcel2.createTypedArray(VpnInfo.CREATOR);
          return arrayOfVpnInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getAlwaysOnVpnPackage(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(49, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getCaptivePortalServerUrl()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          mRemote.transact(75, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NetworkCapabilities[] getDefaultNetworkCapabilitiesForUser(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          NetworkCapabilities[] arrayOfNetworkCapabilities = (NetworkCapabilities[])localParcel2.createTypedArray(NetworkCapabilities.CREATOR);
          return arrayOfNetworkCapabilities;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ProxyInfo getGlobalProxy()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          mRemote.transact(36, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ProxyInfo localProxyInfo;
          if (localParcel2.readInt() != 0) {
            localProxyInfo = (ProxyInfo)ProxyInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localProxyInfo = null;
          }
          return localProxyInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.net.IConnectivityManager";
      }
      
      public int getLastTetherError(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          localParcel1.writeString(paramString);
          mRemote.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public LegacyVpnInfo getLegacyVpnInfo(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(44, localParcel1, localParcel2, 0);
          localParcel2.readException();
          LegacyVpnInfo localLegacyVpnInfo;
          if (localParcel2.readInt() != 0) {
            localLegacyVpnInfo = (LegacyVpnInfo)LegacyVpnInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localLegacyVpnInfo = null;
          }
          return localLegacyVpnInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public LinkProperties getLinkProperties(Network paramNetwork)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          if (paramNetwork != null)
          {
            localParcel1.writeInt(1);
            paramNetwork.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramNetwork = (LinkProperties)LinkProperties.CREATOR.createFromParcel(localParcel2);
          } else {
            paramNetwork = null;
          }
          return paramNetwork;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public LinkProperties getLinkPropertiesForType(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          LinkProperties localLinkProperties;
          if (localParcel2.readInt() != 0) {
            localLinkProperties = (LinkProperties)LinkProperties.CREATOR.createFromParcel(localParcel2);
          } else {
            localLinkProperties = null;
          }
          return localLinkProperties;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getMobileProvisioningUrl()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          mRemote.transact(51, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getMultipathPreference(Network paramNetwork)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          if (paramNetwork != null)
          {
            localParcel1.writeInt(1);
            paramNetwork.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(67, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NetworkCapabilities getNetworkCapabilities(Network paramNetwork)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          if (paramNetwork != null)
          {
            localParcel1.writeInt(1);
            paramNetwork.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramNetwork = (NetworkCapabilities)NetworkCapabilities.CREATOR.createFromParcel(localParcel2);
          } else {
            paramNetwork = null;
          }
          return paramNetwork;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Network getNetworkForType(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Network localNetwork;
          if (localParcel2.readInt() != 0) {
            localNetwork = (Network)Network.CREATOR.createFromParcel(localParcel2);
          } else {
            localNetwork = null;
          }
          return localNetwork;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NetworkInfo getNetworkInfo(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          NetworkInfo localNetworkInfo;
          if (localParcel2.readInt() != 0) {
            localNetworkInfo = (NetworkInfo)NetworkInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localNetworkInfo = null;
          }
          return localNetworkInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NetworkInfo getNetworkInfoForUid(Network paramNetwork, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          if (paramNetwork != null)
          {
            localParcel1.writeInt(1);
            paramNetwork.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramNetwork = (NetworkInfo)NetworkInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            paramNetwork = null;
          }
          return paramNetwork;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public byte[] getNetworkWatchlistConfigHash()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          mRemote.transact(76, localParcel1, localParcel2, 0);
          localParcel2.readException();
          byte[] arrayOfByte = localParcel2.createByteArray();
          return arrayOfByte;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ProxyInfo getProxyForNetwork(Network paramNetwork)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          if (paramNetwork != null)
          {
            localParcel1.writeInt(1);
            paramNetwork.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(38, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramNetwork = (ProxyInfo)ProxyInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            paramNetwork = null;
          }
          return paramNetwork;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getRestoreDefaultNetworkDelay(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(68, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] getTetherableBluetoothRegexs()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          mRemote.transact(32, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] getTetherableIfaces()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          mRemote.transact(26, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] getTetherableUsbRegexs()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          mRemote.transact(30, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] getTetherableWifiRegexs()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          mRemote.transact(31, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] getTetheredDhcpRanges()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          mRemote.transact(29, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] getTetheredIfaces()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          mRemote.transact(27, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] getTetheringErroredIfaces()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          mRemote.transact(28, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public VpnConfig getVpnConfig(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(42, localParcel1, localParcel2, 0);
          localParcel2.readException();
          VpnConfig localVpnConfig;
          if (localParcel2.readInt() != 0) {
            localVpnConfig = (VpnConfig)VpnConfig.CREATOR.createFromParcel(localParcel2);
          } else {
            localVpnConfig = null;
          }
          return localVpnConfig;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isActiveNetworkMetered()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isAlwaysOnVpnPackageSupported(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(47, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isNetworkSupported(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isTetheringSupported(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NetworkRequest listenForNetwork(NetworkCapabilities paramNetworkCapabilities, Messenger paramMessenger, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          if (paramNetworkCapabilities != null)
          {
            localParcel1.writeInt(1);
            paramNetworkCapabilities.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramMessenger != null)
          {
            localParcel1.writeInt(1);
            paramMessenger.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(61, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramNetworkCapabilities = (NetworkRequest)NetworkRequest.CREATOR.createFromParcel(localParcel2);
          } else {
            paramNetworkCapabilities = null;
          }
          return paramNetworkCapabilities;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void pendingListenForNetwork(NetworkCapabilities paramNetworkCapabilities, PendingIntent paramPendingIntent)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          if (paramNetworkCapabilities != null)
          {
            localParcel1.writeInt(1);
            paramNetworkCapabilities.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramPendingIntent != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(62, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NetworkRequest pendingRequestForNetwork(NetworkCapabilities paramNetworkCapabilities, PendingIntent paramPendingIntent)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          if (paramNetworkCapabilities != null)
          {
            localParcel1.writeInt(1);
            paramNetworkCapabilities.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramPendingIntent != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(59, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramNetworkCapabilities = (NetworkRequest)NetworkRequest.CREATOR.createFromParcel(localParcel2);
          } else {
            paramNetworkCapabilities = null;
          }
          return paramNetworkCapabilities;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean prepareVpn(String paramString1, String paramString2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
          paramString1 = mRemote;
          boolean bool = false;
          paramString1.transact(39, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int registerNetworkAgent(Messenger paramMessenger, NetworkInfo paramNetworkInfo, LinkProperties paramLinkProperties, NetworkCapabilities paramNetworkCapabilities, int paramInt, NetworkMisc paramNetworkMisc)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          if (paramMessenger != null)
          {
            localParcel1.writeInt(1);
            paramMessenger.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramNetworkInfo != null)
          {
            localParcel1.writeInt(1);
            paramNetworkInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramLinkProperties != null)
          {
            localParcel1.writeInt(1);
            paramLinkProperties.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramNetworkCapabilities != null)
          {
            localParcel1.writeInt(1);
            paramNetworkCapabilities.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          if (paramNetworkMisc != null)
          {
            localParcel1.writeInt(1);
            paramNetworkMisc.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(57, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerNetworkFactory(Messenger paramMessenger, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          if (paramMessenger != null)
          {
            localParcel1.writeInt(1);
            paramMessenger.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(54, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void releaseNetworkRequest(NetworkRequest paramNetworkRequest)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          if (paramNetworkRequest != null)
          {
            localParcel1.writeInt(1);
            paramNetworkRequest.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(63, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void releasePendingNetworkRequest(PendingIntent paramPendingIntent)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          if (paramPendingIntent != null)
          {
            localParcel1.writeInt(1);
            paramPendingIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(60, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean removeVpnAddress(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(70, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void reportInetCondition(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(34, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void reportNetworkConnectivity(Network paramNetwork, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          if (paramNetwork != null)
          {
            localParcel1.writeInt(1);
            paramNetwork.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(35, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean requestBandwidthUpdate(Network paramNetwork)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          boolean bool = true;
          if (paramNetwork != null)
          {
            localParcel1.writeInt(1);
            paramNetwork.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(55, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NetworkRequest requestNetwork(NetworkCapabilities paramNetworkCapabilities, Messenger paramMessenger, int paramInt1, IBinder paramIBinder, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          if (paramNetworkCapabilities != null)
          {
            localParcel1.writeInt(1);
            paramNetworkCapabilities.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramMessenger != null)
          {
            localParcel1.writeInt(1);
            paramMessenger.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(58, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramNetworkCapabilities = (NetworkRequest)NetworkRequest.CREATOR.createFromParcel(localParcel2);
          } else {
            paramNetworkCapabilities = null;
          }
          return paramNetworkCapabilities;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean requestRouteToHostAddress(int paramInt, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeByteArray(paramArrayOfByte);
          paramArrayOfByte = mRemote;
          boolean bool = false;
          paramArrayOfByte.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setAcceptUnvalidated(Network paramNetwork, boolean paramBoolean1, boolean paramBoolean2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          if (paramNetwork != null)
          {
            localParcel1.writeInt(1);
            paramNetwork.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeInt(paramBoolean2);
          mRemote.transact(64, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setAirplaneMode(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(53, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setAlwaysOnVpnPackage(int paramInt, String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(48, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setAvoidUnvalidated(Network paramNetwork)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          if (paramNetwork != null)
          {
            localParcel1.writeInt(1);
            paramNetwork.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(65, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setGlobalProxy(ProxyInfo paramProxyInfo)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          if (paramProxyInfo != null)
          {
            localParcel1.writeInt(1);
            paramProxyInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(37, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setProvisioningNotificationVisible(boolean paramBoolean, int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(52, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setUnderlyingNetworksForVpn(Network[] paramArrayOfNetwork)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          boolean bool = false;
          localParcel1.writeTypedArray(paramArrayOfNetwork, 0);
          mRemote.transact(71, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int setUsbTethering(boolean paramBoolean, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeString(paramString);
          mRemote.transact(33, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          return paramBoolean;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setVpnPackageAuthorization(String paramString, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(40, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void startCaptivePortalApp(Network paramNetwork)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          if (paramNetwork != null)
          {
            localParcel1.writeInt(1);
            paramNetwork.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(66, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void startLegacyVpn(VpnProfile paramVpnProfile)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          if (paramVpnProfile != null)
          {
            localParcel1.writeInt(1);
            paramVpnProfile.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(43, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void startNattKeepalive(Network paramNetwork, int paramInt1, Messenger paramMessenger, IBinder paramIBinder, String paramString1, int paramInt2, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          if (paramNetwork != null)
          {
            localParcel1.writeInt(1);
            paramNetwork.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          if (paramMessenger != null)
          {
            localParcel1.writeInt(1);
            paramMessenger.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString2);
          mRemote.transact(73, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void startTethering(int paramInt, ResultReceiver paramResultReceiver, boolean paramBoolean, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          localParcel1.writeInt(paramInt);
          if (paramResultReceiver != null)
          {
            localParcel1.writeInt(1);
            paramResultReceiver.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeString(paramString);
          mRemote.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void stopKeepalive(Network paramNetwork, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          if (paramNetwork != null)
          {
            localParcel1.writeInt(1);
            paramNetwork.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(74, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void stopTethering(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int tether(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unregisterNetworkFactory(Messenger paramMessenger)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          if (paramMessenger != null)
          {
            localParcel1.writeInt(1);
            paramMessenger.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(56, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int untether(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean updateLockdownVpn()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.IConnectivityManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(46, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
    }
  }
}
