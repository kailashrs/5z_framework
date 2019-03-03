package android.os;

import android.net.INetd;
import android.net.INetd.Stub;
import android.net.INetworkManagementEventObserver;
import android.net.INetworkManagementEventObserver.Stub;
import android.net.ITetheringStatsProvider;
import android.net.ITetheringStatsProvider.Stub;
import android.net.InterfaceConfiguration;
import android.net.Network;
import android.net.NetworkStats;
import android.net.RouteInfo;
import android.net.UidRange;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract interface INetworkManagementService
  extends IInterface
{
  public abstract void addIdleTimer(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void addInterfaceToLocalNetwork(String paramString, List<RouteInfo> paramList)
    throws RemoteException;
  
  public abstract void addInterfaceToNetwork(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void addLegacyRouteForNetId(int paramInt1, RouteInfo paramRouteInfo, int paramInt2)
    throws RemoteException;
  
  public abstract void addRoute(int paramInt, RouteInfo paramRouteInfo)
    throws RemoteException;
  
  public abstract void addVpnUidRanges(int paramInt, UidRange[] paramArrayOfUidRange)
    throws RemoteException;
  
  public abstract void allowProtect(int paramInt)
    throws RemoteException;
  
  public abstract void attachPppd(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
    throws RemoteException;
  
  public abstract void clearDefaultNetId()
    throws RemoteException;
  
  public abstract void clearInterfaceAddresses(String paramString)
    throws RemoteException;
  
  public abstract void clearPermission(int[] paramArrayOfInt)
    throws RemoteException;
  
  public abstract void createPhysicalNetwork(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void createVirtualNetwork(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
    throws RemoteException;
  
  public abstract void denyProtect(int paramInt)
    throws RemoteException;
  
  public abstract void detachPppd(String paramString)
    throws RemoteException;
  
  public abstract void disableIpv6(String paramString)
    throws RemoteException;
  
  public abstract void disableNat(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void enableIpv6(String paramString)
    throws RemoteException;
  
  public abstract void enableNat(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract String[] getDnsForwarders()
    throws RemoteException;
  
  public abstract InterfaceConfiguration getInterfaceConfig(String paramString)
    throws RemoteException;
  
  public abstract boolean getIpForwardingEnabled()
    throws RemoteException;
  
  public abstract INetd getNetdService()
    throws RemoteException;
  
  public abstract NetworkStats getNetworkStatsDetail()
    throws RemoteException;
  
  public abstract NetworkStats getNetworkStatsSummaryDev()
    throws RemoteException;
  
  public abstract NetworkStats getNetworkStatsSummaryXt()
    throws RemoteException;
  
  public abstract NetworkStats getNetworkStatsTethering(int paramInt)
    throws RemoteException;
  
  public abstract NetworkStats getNetworkStatsUidDetail(int paramInt, String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract Map getUidInterfaceRules()
    throws RemoteException;
  
  public abstract boolean isBandwidthControlEnabled()
    throws RemoteException;
  
  public abstract boolean isClatdStarted(String paramString)
    throws RemoteException;
  
  public abstract boolean isFirewallEnabled()
    throws RemoteException;
  
  public abstract boolean isNetworkActive()
    throws RemoteException;
  
  public abstract boolean isNetworkRestricted(int paramInt)
    throws RemoteException;
  
  public abstract boolean isTetheringStarted()
    throws RemoteException;
  
  public abstract String[] listInterfaces()
    throws RemoteException;
  
  public abstract String[] listTetheredInterfaces()
    throws RemoteException;
  
  public abstract String[] listTtys()
    throws RemoteException;
  
  public abstract void registerNetworkActivityListener(INetworkActivityListener paramINetworkActivityListener)
    throws RemoteException;
  
  public abstract void registerObserver(INetworkManagementEventObserver paramINetworkManagementEventObserver)
    throws RemoteException;
  
  public abstract void registerTetheringStatsProvider(ITetheringStatsProvider paramITetheringStatsProvider, String paramString)
    throws RemoteException;
  
  public abstract void removeIdleTimer(String paramString)
    throws RemoteException;
  
  public abstract void removeInterfaceAlert(String paramString)
    throws RemoteException;
  
  public abstract void removeInterfaceFromLocalNetwork(String paramString)
    throws RemoteException;
  
  public abstract void removeInterfaceFromNetwork(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void removeInterfaceQuota(String paramString)
    throws RemoteException;
  
  public abstract void removeNetwork(int paramInt)
    throws RemoteException;
  
  public abstract void removeRoute(int paramInt, RouteInfo paramRouteInfo)
    throws RemoteException;
  
  public abstract int removeRoutesFromLocalNetwork(List<RouteInfo> paramList)
    throws RemoteException;
  
  public abstract void removeVpnUidRanges(int paramInt, UidRange[] paramArrayOfUidRange)
    throws RemoteException;
  
  public abstract void setAllowOnlyVpnForUids(boolean paramBoolean, UidRange[] paramArrayOfUidRange)
    throws RemoteException;
  
  public abstract boolean setDataSaverModeEnabled(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setDefaultNetId(int paramInt)
    throws RemoteException;
  
  public abstract void setDnsConfigurationForNetwork(int paramInt, String[] paramArrayOfString1, String[] paramArrayOfString2, int[] paramArrayOfInt, String paramString, String[] paramArrayOfString3)
    throws RemoteException;
  
  public abstract void setDnsForwarders(Network paramNetwork, String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract void setFirewallChainEnabled(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setFirewallEnabled(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setFirewallInterfaceRule(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setFirewallUidRule(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void setFirewallUidRules(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
    throws RemoteException;
  
  public abstract void setGlobalAlert(long paramLong)
    throws RemoteException;
  
  public abstract void setIPv6AddrGenMode(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void setInterfaceAlert(String paramString, long paramLong)
    throws RemoteException;
  
  public abstract void setInterfaceConfig(String paramString, InterfaceConfiguration paramInterfaceConfiguration)
    throws RemoteException;
  
  public abstract void setInterfaceDown(String paramString)
    throws RemoteException;
  
  public abstract void setInterfaceIpv6PrivacyExtensions(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setInterfaceQuota(String paramString, long paramLong)
    throws RemoteException;
  
  public abstract void setInterfaceRestrict(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setInterfaceUp(String paramString)
    throws RemoteException;
  
  public abstract void setIpForwardingEnabled(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setMtu(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void setNetworkPermission(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void setPermission(String paramString, int[] paramArrayOfInt)
    throws RemoteException;
  
  public abstract void setUidCleartextNetworkPolicy(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setUidInterfaceRules(int paramInt, String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setUidMeteredNetworkBlacklist(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setUidMeteredNetworkWhitelist(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void shutdown()
    throws RemoteException;
  
  public abstract void startClatd(String paramString)
    throws RemoteException;
  
  public abstract void startInterfaceForwarding(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void startTethering(String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract void stopClatd(String paramString)
    throws RemoteException;
  
  public abstract void stopInterfaceForwarding(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void stopTethering()
    throws RemoteException;
  
  public abstract void tetherInterface(String paramString)
    throws RemoteException;
  
  public abstract void tetherLimitReached(ITetheringStatsProvider paramITetheringStatsProvider)
    throws RemoteException;
  
  public abstract void unregisterNetworkActivityListener(INetworkActivityListener paramINetworkActivityListener)
    throws RemoteException;
  
  public abstract void unregisterObserver(INetworkManagementEventObserver paramINetworkManagementEventObserver)
    throws RemoteException;
  
  public abstract void unregisterTetheringStatsProvider(ITetheringStatsProvider paramITetheringStatsProvider)
    throws RemoteException;
  
  public abstract void untetherInterface(String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements INetworkManagementService
  {
    private static final String DESCRIPTOR = "android.os.INetworkManagementService";
    static final int TRANSACTION_addIdleTimer = 56;
    static final int TRANSACTION_addInterfaceToLocalNetwork = 86;
    static final int TRANSACTION_addInterfaceToNetwork = 76;
    static final int TRANSACTION_addLegacyRouteForNetId = 78;
    static final int TRANSACTION_addRoute = 14;
    static final int TRANSACTION_addVpnUidRanges = 65;
    static final int TRANSACTION_allowProtect = 84;
    static final int TRANSACTION_attachPppd = 36;
    static final int TRANSACTION_clearDefaultNetId = 80;
    static final int TRANSACTION_clearInterfaceAddresses = 7;
    static final int TRANSACTION_clearPermission = 83;
    static final int TRANSACTION_createPhysicalNetwork = 73;
    static final int TRANSACTION_createVirtualNetwork = 74;
    static final int TRANSACTION_denyProtect = 85;
    static final int TRANSACTION_detachPppd = 37;
    static final int TRANSACTION_disableIpv6 = 11;
    static final int TRANSACTION_disableNat = 31;
    static final int TRANSACTION_enableIpv6 = 12;
    static final int TRANSACTION_enableNat = 30;
    static final int TRANSACTION_getDnsForwarders = 27;
    static final int TRANSACTION_getInterfaceConfig = 5;
    static final int TRANSACTION_getIpForwardingEnabled = 18;
    static final int TRANSACTION_getNetdService = 3;
    static final int TRANSACTION_getNetworkStatsDetail = 40;
    static final int TRANSACTION_getNetworkStatsSummaryDev = 38;
    static final int TRANSACTION_getNetworkStatsSummaryXt = 39;
    static final int TRANSACTION_getNetworkStatsTethering = 42;
    static final int TRANSACTION_getNetworkStatsUidDetail = 41;
    static final int TRANSACTION_getUidInterfaceRules = 54;
    static final int TRANSACTION_isBandwidthControlEnabled = 55;
    static final int TRANSACTION_isClatdStarted = 69;
    static final int TRANSACTION_isFirewallEnabled = 60;
    static final int TRANSACTION_isNetworkActive = 72;
    static final int TRANSACTION_isNetworkRestricted = 90;
    static final int TRANSACTION_isTetheringStarted = 22;
    static final int TRANSACTION_listInterfaces = 4;
    static final int TRANSACTION_listTetheredInterfaces = 25;
    static final int TRANSACTION_listTtys = 35;
    static final int TRANSACTION_registerNetworkActivityListener = 70;
    static final int TRANSACTION_registerObserver = 1;
    static final int TRANSACTION_registerTetheringStatsProvider = 32;
    static final int TRANSACTION_removeIdleTimer = 57;
    static final int TRANSACTION_removeInterfaceAlert = 46;
    static final int TRANSACTION_removeInterfaceFromLocalNetwork = 87;
    static final int TRANSACTION_removeInterfaceFromNetwork = 77;
    static final int TRANSACTION_removeInterfaceQuota = 44;
    static final int TRANSACTION_removeNetwork = 75;
    static final int TRANSACTION_removeRoute = 15;
    static final int TRANSACTION_removeRoutesFromLocalNetwork = 88;
    static final int TRANSACTION_removeVpnUidRanges = 66;
    static final int TRANSACTION_setAllowOnlyVpnForUids = 89;
    static final int TRANSACTION_setDataSaverModeEnabled = 50;
    static final int TRANSACTION_setDefaultNetId = 79;
    static final int TRANSACTION_setDnsConfigurationForNetwork = 58;
    static final int TRANSACTION_setDnsForwarders = 26;
    static final int TRANSACTION_setFirewallChainEnabled = 64;
    static final int TRANSACTION_setFirewallEnabled = 59;
    static final int TRANSACTION_setFirewallInterfaceRule = 61;
    static final int TRANSACTION_setFirewallUidRule = 62;
    static final int TRANSACTION_setFirewallUidRules = 63;
    static final int TRANSACTION_setGlobalAlert = 47;
    static final int TRANSACTION_setIPv6AddrGenMode = 13;
    static final int TRANSACTION_setInterfaceAlert = 45;
    static final int TRANSACTION_setInterfaceConfig = 6;
    static final int TRANSACTION_setInterfaceDown = 8;
    static final int TRANSACTION_setInterfaceIpv6PrivacyExtensions = 10;
    static final int TRANSACTION_setInterfaceQuota = 43;
    static final int TRANSACTION_setInterfaceRestrict = 53;
    static final int TRANSACTION_setInterfaceUp = 9;
    static final int TRANSACTION_setIpForwardingEnabled = 19;
    static final int TRANSACTION_setMtu = 16;
    static final int TRANSACTION_setNetworkPermission = 81;
    static final int TRANSACTION_setPermission = 82;
    static final int TRANSACTION_setUidCleartextNetworkPolicy = 51;
    static final int TRANSACTION_setUidInterfaceRules = 52;
    static final int TRANSACTION_setUidMeteredNetworkBlacklist = 48;
    static final int TRANSACTION_setUidMeteredNetworkWhitelist = 49;
    static final int TRANSACTION_shutdown = 17;
    static final int TRANSACTION_startClatd = 67;
    static final int TRANSACTION_startInterfaceForwarding = 28;
    static final int TRANSACTION_startTethering = 20;
    static final int TRANSACTION_stopClatd = 68;
    static final int TRANSACTION_stopInterfaceForwarding = 29;
    static final int TRANSACTION_stopTethering = 21;
    static final int TRANSACTION_tetherInterface = 23;
    static final int TRANSACTION_tetherLimitReached = 34;
    static final int TRANSACTION_unregisterNetworkActivityListener = 71;
    static final int TRANSACTION_unregisterObserver = 2;
    static final int TRANSACTION_unregisterTetheringStatsProvider = 33;
    static final int TRANSACTION_untetherInterface = 24;
    
    public Stub()
    {
      attachInterface(this, "android.os.INetworkManagementService");
    }
    
    public static INetworkManagementService asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.os.INetworkManagementService");
      if ((localIInterface != null) && ((localIInterface instanceof INetworkManagementService))) {
        return (INetworkManagementService)localIInterface;
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
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        String str = null;
        Object localObject5 = null;
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        boolean bool4 = false;
        boolean bool5 = false;
        boolean bool6 = false;
        boolean bool7 = false;
        boolean bool8 = false;
        boolean bool9 = false;
        boolean bool10 = false;
        boolean bool11 = false;
        boolean bool12 = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 90: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          paramInt1 = isNetworkRestricted(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 89: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          if (paramParcel1.readInt() != 0) {
            bool12 = true;
          }
          setAllowOnlyVpnForUids(bool12, (UidRange[])paramParcel1.createTypedArray(UidRange.CREATOR));
          paramParcel2.writeNoException();
          return true;
        case 88: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          paramInt1 = removeRoutesFromLocalNetwork(paramParcel1.createTypedArrayList(RouteInfo.CREATOR));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 87: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          removeInterfaceFromLocalNetwork(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 86: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          addInterfaceToLocalNetwork(paramParcel1.readString(), paramParcel1.createTypedArrayList(RouteInfo.CREATOR));
          paramParcel2.writeNoException();
          return true;
        case 85: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          denyProtect(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 84: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          allowProtect(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 83: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          clearPermission(paramParcel1.createIntArray());
          paramParcel2.writeNoException();
          return true;
        case 82: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          setPermission(paramParcel1.readString(), paramParcel1.createIntArray());
          paramParcel2.writeNoException();
          return true;
        case 81: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          setNetworkPermission(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 80: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          clearDefaultNetId();
          paramParcel2.writeNoException();
          return true;
        case 79: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          setDefaultNetId(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 78: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject4 = (RouteInfo)RouteInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject4 = localObject5;
          }
          addLegacyRouteForNetId(paramInt1, (RouteInfo)localObject4, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 77: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          removeInterfaceFromNetwork(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 76: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          addInterfaceToNetwork(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 75: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          removeNetwork(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 74: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool12 = true;
          } else {
            bool12 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          }
          createVirtualNetwork(paramInt1, bool12, bool1);
          paramParcel2.writeNoException();
          return true;
        case 73: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          createPhysicalNetwork(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 72: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          paramInt1 = isNetworkActive();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 71: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          unregisterNetworkActivityListener(INetworkActivityListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 70: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          registerNetworkActivityListener(INetworkActivityListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 69: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          paramInt1 = isClatdStarted(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 68: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          stopClatd(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 67: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          startClatd(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 66: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          removeVpnUidRanges(paramParcel1.readInt(), (UidRange[])paramParcel1.createTypedArray(UidRange.CREATOR));
          paramParcel2.writeNoException();
          return true;
        case 65: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          addVpnUidRanges(paramParcel1.readInt(), (UidRange[])paramParcel1.createTypedArray(UidRange.CREATOR));
          paramParcel2.writeNoException();
          return true;
        case 64: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          paramInt1 = paramParcel1.readInt();
          bool12 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool12 = true;
          }
          setFirewallChainEnabled(paramInt1, bool12);
          paramParcel2.writeNoException();
          return true;
        case 63: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          setFirewallUidRules(paramParcel1.readInt(), paramParcel1.createIntArray(), paramParcel1.createIntArray());
          paramParcel2.writeNoException();
          return true;
        case 62: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          setFirewallUidRule(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 61: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          localObject4 = paramParcel1.readString();
          bool12 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool12 = true;
          }
          setFirewallInterfaceRule((String)localObject4, bool12);
          paramParcel2.writeNoException();
          return true;
        case 60: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          paramInt1 = isFirewallEnabled();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 59: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          bool12 = bool4;
          if (paramParcel1.readInt() != 0) {
            bool12 = true;
          }
          setFirewallEnabled(bool12);
          paramParcel2.writeNoException();
          return true;
        case 58: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          setDnsConfigurationForNetwork(paramParcel1.readInt(), paramParcel1.createStringArray(), paramParcel1.createStringArray(), paramParcel1.createIntArray(), paramParcel1.readString(), paramParcel1.createStringArray());
          paramParcel2.writeNoException();
          return true;
        case 57: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          removeIdleTimer(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 56: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          addIdleTimer(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 55: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          paramInt1 = isBandwidthControlEnabled();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 54: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          paramParcel1 = getUidInterfaceRules();
          paramParcel2.writeNoException();
          paramParcel2.writeMap(paramParcel1);
          return true;
        case 53: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          localObject4 = paramParcel1.readString();
          bool12 = bool5;
          if (paramParcel1.readInt() != 0) {
            bool12 = true;
          }
          setInterfaceRestrict((String)localObject4, bool12);
          paramParcel2.writeNoException();
          return true;
        case 52: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          paramInt1 = paramParcel1.readInt();
          localObject4 = paramParcel1.readString();
          bool12 = bool6;
          if (paramParcel1.readInt() != 0) {
            bool12 = true;
          }
          setUidInterfaceRules(paramInt1, (String)localObject4, bool12);
          paramParcel2.writeNoException();
          return true;
        case 51: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          setUidCleartextNetworkPolicy(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 50: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          bool12 = bool7;
          if (paramParcel1.readInt() != 0) {
            bool12 = true;
          }
          paramInt1 = setDataSaverModeEnabled(bool12);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 49: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          paramInt1 = paramParcel1.readInt();
          bool12 = bool8;
          if (paramParcel1.readInt() != 0) {
            bool12 = true;
          }
          setUidMeteredNetworkWhitelist(paramInt1, bool12);
          paramParcel2.writeNoException();
          return true;
        case 48: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          paramInt1 = paramParcel1.readInt();
          bool12 = bool9;
          if (paramParcel1.readInt() != 0) {
            bool12 = true;
          }
          setUidMeteredNetworkBlacklist(paramInt1, bool12);
          paramParcel2.writeNoException();
          return true;
        case 47: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          setGlobalAlert(paramParcel1.readLong());
          paramParcel2.writeNoException();
          return true;
        case 46: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          removeInterfaceAlert(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 45: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          setInterfaceAlert(paramParcel1.readString(), paramParcel1.readLong());
          paramParcel2.writeNoException();
          return true;
        case 44: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          removeInterfaceQuota(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 43: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          setInterfaceQuota(paramParcel1.readString(), paramParcel1.readLong());
          paramParcel2.writeNoException();
          return true;
        case 42: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          paramParcel1 = getNetworkStatsTethering(paramParcel1.readInt());
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
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          paramParcel1 = getNetworkStatsUidDetail(paramParcel1.readInt(), paramParcel1.createStringArray());
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
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          paramParcel1 = getNetworkStatsDetail();
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
        case 39: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          paramParcel1 = getNetworkStatsSummaryXt();
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
        case 38: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          paramParcel1 = getNetworkStatsSummaryDev();
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
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          detachPppd(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 36: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          attachPppd(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 35: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          paramParcel1 = listTtys();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 34: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          tetherLimitReached(ITetheringStatsProvider.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 33: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          unregisterTetheringStatsProvider(ITetheringStatsProvider.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 32: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          registerTetheringStatsProvider(ITetheringStatsProvider.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 31: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          disableNat(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 30: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          enableNat(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 29: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          stopInterfaceForwarding(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 28: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          startInterfaceForwarding(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 27: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          paramParcel1 = getDnsForwarders();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 26: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          if (paramParcel1.readInt() != 0) {
            localObject4 = (Network)Network.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject4 = localObject1;
          }
          setDnsForwarders((Network)localObject4, paramParcel1.createStringArray());
          paramParcel2.writeNoException();
          return true;
        case 25: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          paramParcel1 = listTetheredInterfaces();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          untetherInterface(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          tetherInterface(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          paramInt1 = isTetheringStarted();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          stopTethering();
          paramParcel2.writeNoException();
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          startTethering(paramParcel1.createStringArray());
          paramParcel2.writeNoException();
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          bool12 = bool10;
          if (paramParcel1.readInt() != 0) {
            bool12 = true;
          }
          setIpForwardingEnabled(bool12);
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          paramInt1 = getIpForwardingEnabled();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          shutdown();
          paramParcel2.writeNoException();
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          setMtu(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (RouteInfo)RouteInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          removeRoute(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (RouteInfo)RouteInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          addRoute(paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          setIPv6AddrGenMode(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          enableIpv6(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          disableIpv6(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          localObject4 = paramParcel1.readString();
          bool12 = bool11;
          if (paramParcel1.readInt() != 0) {
            bool12 = true;
          }
          setInterfaceIpv6PrivacyExtensions((String)localObject4, bool12);
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          setInterfaceUp(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          setInterfaceDown(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          clearInterfaceAddresses(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          str = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (InterfaceConfiguration)InterfaceConfiguration.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject4;
          }
          setInterfaceConfig(str, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          paramParcel1 = getInterfaceConfig(paramParcel1.readString());
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
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          paramParcel1 = listInterfaces();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          localObject4 = getNetdService();
          paramParcel2.writeNoException();
          paramParcel1 = str;
          if (localObject4 != null) {
            paramParcel1 = ((INetd)localObject4).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.os.INetworkManagementService");
          unregisterObserver(INetworkManagementEventObserver.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("android.os.INetworkManagementService");
        registerObserver(INetworkManagementEventObserver.Stub.asInterface(paramParcel1.readStrongBinder()));
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.os.INetworkManagementService");
      return true;
    }
    
    private static class Proxy
      implements INetworkManagementService
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addIdleTimer(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public void addInterfaceToLocalNetwork(String paramString, List<RouteInfo> paramList)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString);
          localParcel1.writeTypedList(paramList);
          mRemote.transact(86, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void addInterfaceToNetwork(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(76, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void addLegacyRouteForNetId(int paramInt1, RouteInfo paramRouteInfo, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeInt(paramInt1);
          if (paramRouteInfo != null)
          {
            localParcel1.writeInt(1);
            paramRouteInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt2);
          mRemote.transact(78, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void addRoute(int paramInt, RouteInfo paramRouteInfo)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeInt(paramInt);
          if (paramRouteInfo != null)
          {
            localParcel1.writeInt(1);
            paramRouteInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void addVpnUidRanges(int paramInt, UidRange[] paramArrayOfUidRange)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeTypedArray(paramArrayOfUidRange, 0);
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
      
      public void allowProtect(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(84, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
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
      
      public void attachPppd(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          localParcel1.writeString(paramString4);
          localParcel1.writeString(paramString5);
          mRemote.transact(36, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void clearDefaultNetId()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          mRemote.transact(80, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void clearInterfaceAddresses(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void clearPermission(int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeIntArray(paramArrayOfInt);
          mRemote.transact(83, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void createPhysicalNetwork(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
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
      
      public void createVirtualNetwork(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeInt(paramBoolean2);
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
      
      public void denyProtect(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(85, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void detachPppd(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString);
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
      
      public void disableIpv6(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString);
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void disableNat(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(31, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void enableIpv6(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString);
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void enableNat(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(30, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] getDnsForwarders()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
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
      
      public InterfaceConfiguration getInterfaceConfig(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (InterfaceConfiguration)InterfaceConfiguration.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.os.INetworkManagementService";
      }
      
      public boolean getIpForwardingEnabled()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
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
      
      public INetd getNetdService()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          INetd localINetd = INetd.Stub.asInterface(localParcel2.readStrongBinder());
          return localINetd;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NetworkStats getNetworkStatsDetail()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          mRemote.transact(40, localParcel1, localParcel2, 0);
          localParcel2.readException();
          NetworkStats localNetworkStats;
          if (localParcel2.readInt() != 0) {
            localNetworkStats = (NetworkStats)NetworkStats.CREATOR.createFromParcel(localParcel2);
          } else {
            localNetworkStats = null;
          }
          return localNetworkStats;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NetworkStats getNetworkStatsSummaryDev()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          mRemote.transact(38, localParcel1, localParcel2, 0);
          localParcel2.readException();
          NetworkStats localNetworkStats;
          if (localParcel2.readInt() != 0) {
            localNetworkStats = (NetworkStats)NetworkStats.CREATOR.createFromParcel(localParcel2);
          } else {
            localNetworkStats = null;
          }
          return localNetworkStats;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NetworkStats getNetworkStatsSummaryXt()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          mRemote.transact(39, localParcel1, localParcel2, 0);
          localParcel2.readException();
          NetworkStats localNetworkStats;
          if (localParcel2.readInt() != 0) {
            localNetworkStats = (NetworkStats)NetworkStats.CREATOR.createFromParcel(localParcel2);
          } else {
            localNetworkStats = null;
          }
          return localNetworkStats;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NetworkStats getNetworkStatsTethering(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(42, localParcel1, localParcel2, 0);
          localParcel2.readException();
          NetworkStats localNetworkStats;
          if (localParcel2.readInt() != 0) {
            localNetworkStats = (NetworkStats)NetworkStats.CREATOR.createFromParcel(localParcel2);
          } else {
            localNetworkStats = null;
          }
          return localNetworkStats;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public NetworkStats getNetworkStatsUidDetail(int paramInt, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeStringArray(paramArrayOfString);
          mRemote.transact(41, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramArrayOfString = (NetworkStats)NetworkStats.CREATOR.createFromParcel(localParcel2);
          } else {
            paramArrayOfString = null;
          }
          return paramArrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Map getUidInterfaceRules()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          mRemote.transact(54, localParcel1, localParcel2, 0);
          localParcel2.readException();
          HashMap localHashMap = localParcel2.readHashMap(getClass().getClassLoader());
          return localHashMap;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isBandwidthControlEnabled()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(55, localParcel1, localParcel2, 0);
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
      
      public boolean isClatdStarted(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(69, localParcel1, localParcel2, 0);
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
      
      public boolean isFirewallEnabled()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(60, localParcel1, localParcel2, 0);
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
      
      public boolean isNetworkActive()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(72, localParcel1, localParcel2, 0);
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
      
      public boolean isNetworkRestricted(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(90, localParcel1, localParcel2, 0);
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
      
      public boolean isTetheringStarted()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(22, localParcel1, localParcel2, 0);
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
      
      public String[] listInterfaces()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          mRemote.transact(4, localParcel1, localParcel2, 0);
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
      
      public String[] listTetheredInterfaces()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          mRemote.transact(25, localParcel1, localParcel2, 0);
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
      
      public String[] listTtys()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          mRemote.transact(35, localParcel1, localParcel2, 0);
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
      
      public void registerNetworkActivityListener(INetworkActivityListener paramINetworkActivityListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          if (paramINetworkActivityListener != null) {
            paramINetworkActivityListener = paramINetworkActivityListener.asBinder();
          } else {
            paramINetworkActivityListener = null;
          }
          localParcel1.writeStrongBinder(paramINetworkActivityListener);
          mRemote.transact(70, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerObserver(INetworkManagementEventObserver paramINetworkManagementEventObserver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          if (paramINetworkManagementEventObserver != null) {
            paramINetworkManagementEventObserver = paramINetworkManagementEventObserver.asBinder();
          } else {
            paramINetworkManagementEventObserver = null;
          }
          localParcel1.writeStrongBinder(paramINetworkManagementEventObserver);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerTetheringStatsProvider(ITetheringStatsProvider paramITetheringStatsProvider, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          if (paramITetheringStatsProvider != null) {
            paramITetheringStatsProvider = paramITetheringStatsProvider.asBinder();
          } else {
            paramITetheringStatsProvider = null;
          }
          localParcel1.writeStrongBinder(paramITetheringStatsProvider);
          localParcel1.writeString(paramString);
          mRemote.transact(32, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeIdleTimer(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString);
          mRemote.transact(57, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeInterfaceAlert(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString);
          mRemote.transact(46, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeInterfaceFromLocalNetwork(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString);
          mRemote.transact(87, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeInterfaceFromNetwork(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(77, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeInterfaceQuota(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString);
          mRemote.transact(44, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeNetwork(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(75, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeRoute(int paramInt, RouteInfo paramRouteInfo)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeInt(paramInt);
          if (paramRouteInfo != null)
          {
            localParcel1.writeInt(1);
            paramRouteInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int removeRoutesFromLocalNetwork(List<RouteInfo> paramList)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeTypedList(paramList);
          mRemote.transact(88, localParcel1, localParcel2, 0);
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
      
      public void removeVpnUidRanges(int paramInt, UidRange[] paramArrayOfUidRange)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeTypedArray(paramArrayOfUidRange, 0);
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
      
      public void setAllowOnlyVpnForUids(boolean paramBoolean, UidRange[] paramArrayOfUidRange)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeTypedArray(paramArrayOfUidRange, 0);
          mRemote.transact(89, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setDataSaverModeEnabled(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeInt(paramBoolean);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(50, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (paramBoolean) {
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
      
      public void setDefaultNetId(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeInt(paramInt);
          mRemote.transact(79, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setDnsConfigurationForNetwork(int paramInt, String[] paramArrayOfString1, String[] paramArrayOfString2, int[] paramArrayOfInt, String paramString, String[] paramArrayOfString3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeStringArray(paramArrayOfString1);
          localParcel1.writeStringArray(paramArrayOfString2);
          localParcel1.writeIntArray(paramArrayOfInt);
          localParcel1.writeString(paramString);
          localParcel1.writeStringArray(paramArrayOfString3);
          mRemote.transact(58, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setDnsForwarders(Network paramNetwork, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          if (paramNetwork != null)
          {
            localParcel1.writeInt(1);
            paramNetwork.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeStringArray(paramArrayOfString);
          mRemote.transact(26, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setFirewallChainEnabled(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
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
      
      public void setFirewallEnabled(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(59, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setFirewallInterfaceRule(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(61, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setFirewallUidRule(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
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
      
      public void setFirewallUidRules(int paramInt, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeIntArray(paramArrayOfInt1);
          localParcel1.writeIntArray(paramArrayOfInt2);
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
      
      public void setGlobalAlert(long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeLong(paramLong);
          mRemote.transact(47, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setIPv6AddrGenMode(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setInterfaceAlert(String paramString, long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString);
          localParcel1.writeLong(paramLong);
          mRemote.transact(45, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setInterfaceConfig(String paramString, InterfaceConfiguration paramInterfaceConfiguration)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString);
          if (paramInterfaceConfiguration != null)
          {
            localParcel1.writeInt(1);
            paramInterfaceConfiguration.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setInterfaceDown(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setInterfaceIpv6PrivacyExtensions(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setInterfaceQuota(String paramString, long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString);
          localParcel1.writeLong(paramLong);
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
      
      public void setInterfaceRestrict(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString);
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
      
      public void setInterfaceUp(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setIpForwardingEnabled(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setMtu(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setNetworkPermission(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(81, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setPermission(String paramString, int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString);
          localParcel1.writeIntArray(paramArrayOfInt);
          mRemote.transact(82, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setUidCleartextNetworkPolicy(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(51, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setUidInterfaceRules(int paramInt, String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
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
      
      public void setUidMeteredNetworkBlacklist(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(48, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setUidMeteredNetworkWhitelist(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(49, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void shutdown()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void startClatd(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString);
          mRemote.transact(67, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void startInterfaceForwarding(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(28, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void startTethering(String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeStringArray(paramArrayOfString);
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void stopClatd(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString);
          mRemote.transact(68, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void stopInterfaceForwarding(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(29, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void stopTethering()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          mRemote.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void tetherInterface(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          localParcel1.writeString(paramString);
          mRemote.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void tetherLimitReached(ITetheringStatsProvider paramITetheringStatsProvider)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          if (paramITetheringStatsProvider != null) {
            paramITetheringStatsProvider = paramITetheringStatsProvider.asBinder();
          } else {
            paramITetheringStatsProvider = null;
          }
          localParcel1.writeStrongBinder(paramITetheringStatsProvider);
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
      
      public void unregisterNetworkActivityListener(INetworkActivityListener paramINetworkActivityListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          if (paramINetworkActivityListener != null) {
            paramINetworkActivityListener = paramINetworkActivityListener.asBinder();
          } else {
            paramINetworkActivityListener = null;
          }
          localParcel1.writeStrongBinder(paramINetworkActivityListener);
          mRemote.transact(71, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unregisterObserver(INetworkManagementEventObserver paramINetworkManagementEventObserver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          if (paramINetworkManagementEventObserver != null) {
            paramINetworkManagementEventObserver = paramINetworkManagementEventObserver.asBinder();
          } else {
            paramINetworkManagementEventObserver = null;
          }
          localParcel1.writeStrongBinder(paramINetworkManagementEventObserver);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void unregisterTetheringStatsProvider(ITetheringStatsProvider paramITetheringStatsProvider)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
          if (paramITetheringStatsProvider != null) {
            paramITetheringStatsProvider = paramITetheringStatsProvider.asBinder();
          } else {
            paramITetheringStatsProvider = null;
          }
          localParcel1.writeStrongBinder(paramITetheringStatsProvider);
          mRemote.transact(33, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void untetherInterface(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.os.INetworkManagementService");
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
    }
  }
}
