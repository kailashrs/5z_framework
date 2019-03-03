package android.net.wifi;

import android.content.pm.ParceledListSlice;
import android.net.DhcpInfo;
import android.net.Network;
import android.net.wifi.hotspot2.IProvisioningCallback;
import android.net.wifi.hotspot2.IProvisioningCallback.Stub;
import android.net.wifi.hotspot2.OsuProvider;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.WorkSource;
import java.util.ArrayList;
import java.util.List;

public abstract interface IWifiManager
  extends IInterface
{
  public abstract void acquireMulticastLock(IBinder paramIBinder, String paramString)
    throws RemoteException;
  
  public abstract boolean acquireWifiLock(IBinder paramIBinder, int paramInt, String paramString, WorkSource paramWorkSource)
    throws RemoteException;
  
  public abstract boolean addOrUpdateApLocalAllowList(WifiConfiguration paramWifiConfiguration)
    throws RemoteException;
  
  public abstract int addOrUpdateNetwork(WifiConfiguration paramWifiConfiguration, String paramString)
    throws RemoteException;
  
  public abstract boolean addOrUpdatePasspointConfiguration(PasspointConfiguration paramPasspointConfiguration, String paramString)
    throws RemoteException;
  
  public abstract void deauthenticateNetwork(long paramLong, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void disableEphemeralNetwork(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract boolean disableNetwork(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void disconnect(String paramString)
    throws RemoteException;
  
  public abstract boolean disconnectClient(WifiConfiguration paramWifiConfiguration)
    throws RemoteException;
  
  public abstract int dppAddBootstrapQrCode(String paramString)
    throws RemoteException;
  
  public abstract int dppBootstrapGenerate(WifiDppConfig paramWifiDppConfig)
    throws RemoteException;
  
  public abstract int dppBootstrapRemove(int paramInt)
    throws RemoteException;
  
  public abstract int dppConfiguratorAdd(String paramString1, String paramString2, int paramInt)
    throws RemoteException;
  
  public abstract String dppConfiguratorGetKey(int paramInt)
    throws RemoteException;
  
  public abstract int dppConfiguratorRemove(int paramInt)
    throws RemoteException;
  
  public abstract String dppGetUri(int paramInt)
    throws RemoteException;
  
  public abstract int dppListen(String paramString, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
    throws RemoteException;
  
  public abstract int dppStartAuth(WifiDppConfig paramWifiDppConfig)
    throws RemoteException;
  
  public abstract void dppStopListen()
    throws RemoteException;
  
  public abstract boolean enableNetwork(int paramInt, boolean paramBoolean, String paramString)
    throws RemoteException;
  
  public abstract void enableTdls(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void enableTdlsWithMacAddress(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void enableVerboseLogging(int paramInt)
    throws RemoteException;
  
  public abstract void enableWifiConnectivityManager(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void enableWifiCoverageExtendFeature(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void enable_hotspotWhiteList(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void factoryReset(String paramString)
    throws RemoteException;
  
  public abstract List<WifiConfiguration> getAllMatchingWifiConfigs(ScanResult paramScanResult)
    throws RemoteException;
  
  public abstract List<WifiConfiguration> getApAllowList()
    throws RemoteException;
  
  public abstract String getCapabilities(String paramString)
    throws RemoteException;
  
  public abstract ParceledListSlice getConfiguredNetworks()
    throws RemoteException;
  
  public abstract WifiInfo getConnectionInfo(String paramString)
    throws RemoteException;
  
  public abstract String getCountryCode()
    throws RemoteException;
  
  public abstract Network getCurrentNetwork()
    throws RemoteException;
  
  public abstract String getCurrentNetworkWpsNfcConfigurationToken()
    throws RemoteException;
  
  public abstract DhcpInfo getDhcpInfo()
    throws RemoteException;
  
  public abstract List<OsuProvider> getMatchingOsuProviders(ScanResult paramScanResult)
    throws RemoteException;
  
  public abstract WifiConfiguration getMatchingWifiConfig(ScanResult paramScanResult)
    throws RemoteException;
  
  public abstract List<PasspointConfiguration> getPasspointConfigurations()
    throws RemoteException;
  
  public abstract ParceledListSlice getPrivilegedConfiguredNetworks()
    throws RemoteException;
  
  public abstract List<ScanResult> getScanResults(String paramString)
    throws RemoteException;
  
  public abstract int getSupportedFeatures()
    throws RemoteException;
  
  public abstract int getVerboseLoggingLevel()
    throws RemoteException;
  
  public abstract WifiConfiguration getWifiApConfiguration()
    throws RemoteException;
  
  public abstract int getWifiApEnabledState()
    throws RemoteException;
  
  public abstract int getWifiEnabledState()
    throws RemoteException;
  
  public abstract Messenger getWifiServiceMessenger(String paramString)
    throws RemoteException;
  
  public abstract void initializeMulticastFiltering()
    throws RemoteException;
  
  public abstract boolean isDualBandSupported()
    throws RemoteException;
  
  public abstract boolean isEnableWhiteList()
    throws RemoteException;
  
  public abstract boolean isExtendingWifi()
    throws RemoteException;
  
  public abstract boolean isMulticastEnabled()
    throws RemoteException;
  
  public abstract boolean isScanAlwaysAvailable()
    throws RemoteException;
  
  public abstract boolean isWifiCoverageExtendFeatureEnabled()
    throws RemoteException;
  
  public abstract int matchProviderWithCurrentNetwork(String paramString)
    throws RemoteException;
  
  public abstract boolean needs5GHzToAnyApBandConversion()
    throws RemoteException;
  
  public abstract void queryPasspointIcon(long paramLong, String paramString)
    throws RemoteException;
  
  public abstract void reassociate(String paramString)
    throws RemoteException;
  
  public abstract void reconnect(String paramString)
    throws RemoteException;
  
  public abstract void registerSoftApCallback(IBinder paramIBinder, ISoftApCallback paramISoftApCallback, int paramInt)
    throws RemoteException;
  
  public abstract void releaseMulticastLock()
    throws RemoteException;
  
  public abstract boolean releaseWifiLock(IBinder paramIBinder)
    throws RemoteException;
  
  public abstract boolean removeApLocalAllowList(WifiConfiguration paramWifiConfiguration)
    throws RemoteException;
  
  public abstract boolean removeNetwork(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract boolean removePasspointConfiguration(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract WifiActivityEnergyInfo reportActivityInfo()
    throws RemoteException;
  
  public abstract void requestActivityInfo(ResultReceiver paramResultReceiver)
    throws RemoteException;
  
  public abstract void restoreBackupData(byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void restoreSupplicantBackupData(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws RemoteException;
  
  public abstract byte[] retrieveBackupData()
    throws RemoteException;
  
  public abstract void setCountryCode(String paramString)
    throws RemoteException;
  
  public abstract boolean setWifiApConfiguration(WifiConfiguration paramWifiConfiguration, String paramString)
    throws RemoteException;
  
  public abstract boolean setWifiEnabled(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract int startLocalOnlyHotspot(Messenger paramMessenger, IBinder paramIBinder, String paramString)
    throws RemoteException;
  
  public abstract boolean startScan(String paramString)
    throws RemoteException;
  
  public abstract boolean startSoftAp(WifiConfiguration paramWifiConfiguration)
    throws RemoteException;
  
  public abstract void startSubscriptionProvisioning(OsuProvider paramOsuProvider, IProvisioningCallback paramIProvisioningCallback)
    throws RemoteException;
  
  public abstract void startWatchLocalOnlyHotspot(Messenger paramMessenger, IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void stopLocalOnlyHotspot()
    throws RemoteException;
  
  public abstract boolean stopSoftAp()
    throws RemoteException;
  
  public abstract void stopWatchLocalOnlyHotspot()
    throws RemoteException;
  
  public abstract void unregisterSoftApCallback(int paramInt)
    throws RemoteException;
  
  public abstract void updateInterfaceIpState(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void updateWifiLockWorkSource(IBinder paramIBinder, WorkSource paramWorkSource)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IWifiManager
  {
    private static final String DESCRIPTOR = "android.net.wifi.IWifiManager";
    static final int TRANSACTION_acquireMulticastLock = 38;
    static final int TRANSACTION_acquireWifiLock = 33;
    static final int TRANSACTION_addOrUpdateApLocalAllowList = 77;
    static final int TRANSACTION_addOrUpdateNetwork = 9;
    static final int TRANSACTION_addOrUpdatePasspointConfiguration = 10;
    static final int TRANSACTION_deauthenticateNetwork = 15;
    static final int TRANSACTION_disableEphemeralNetwork = 57;
    static final int TRANSACTION_disableNetwork = 18;
    static final int TRANSACTION_disconnect = 21;
    static final int TRANSACTION_disconnectClient = 79;
    static final int TRANSACTION_dppAddBootstrapQrCode = 67;
    static final int TRANSACTION_dppBootstrapGenerate = 68;
    static final int TRANSACTION_dppBootstrapRemove = 70;
    static final int TRANSACTION_dppConfiguratorAdd = 73;
    static final int TRANSACTION_dppConfiguratorGetKey = 76;
    static final int TRANSACTION_dppConfiguratorRemove = 74;
    static final int TRANSACTION_dppGetUri = 69;
    static final int TRANSACTION_dppListen = 71;
    static final int TRANSACTION_dppStartAuth = 75;
    static final int TRANSACTION_dppStopListen = 72;
    static final int TRANSACTION_enableNetwork = 17;
    static final int TRANSACTION_enableTdls = 51;
    static final int TRANSACTION_enableTdlsWithMacAddress = 52;
    static final int TRANSACTION_enableVerboseLogging = 54;
    static final int TRANSACTION_enableWifiConnectivityManager = 56;
    static final int TRANSACTION_enableWifiCoverageExtendFeature = 85;
    static final int TRANSACTION_enable_hotspotWhiteList = 81;
    static final int TRANSACTION_factoryReset = 58;
    static final int TRANSACTION_getAllMatchingWifiConfigs = 7;
    static final int TRANSACTION_getApAllowList = 80;
    static final int TRANSACTION_getCapabilities = 66;
    static final int TRANSACTION_getConfiguredNetworks = 4;
    static final int TRANSACTION_getConnectionInfo = 24;
    static final int TRANSACTION_getCountryCode = 28;
    static final int TRANSACTION_getCurrentNetwork = 59;
    static final int TRANSACTION_getCurrentNetworkWpsNfcConfigurationToken = 53;
    static final int TRANSACTION_getDhcpInfo = 31;
    static final int TRANSACTION_getMatchingOsuProviders = 8;
    static final int TRANSACTION_getMatchingWifiConfig = 6;
    static final int TRANSACTION_getPasspointConfigurations = 12;
    static final int TRANSACTION_getPrivilegedConfiguredNetworks = 5;
    static final int TRANSACTION_getScanResults = 20;
    static final int TRANSACTION_getSupportedFeatures = 1;
    static final int TRANSACTION_getVerboseLoggingLevel = 55;
    static final int TRANSACTION_getWifiApConfiguration = 48;
    static final int TRANSACTION_getWifiApEnabledState = 47;
    static final int TRANSACTION_getWifiEnabledState = 26;
    static final int TRANSACTION_getWifiServiceMessenger = 50;
    static final int TRANSACTION_initializeMulticastFiltering = 36;
    static final int TRANSACTION_isDualBandSupported = 29;
    static final int TRANSACTION_isEnableWhiteList = 82;
    static final int TRANSACTION_isExtendingWifi = 83;
    static final int TRANSACTION_isMulticastEnabled = 37;
    static final int TRANSACTION_isScanAlwaysAvailable = 32;
    static final int TRANSACTION_isWifiCoverageExtendFeatureEnabled = 84;
    static final int TRANSACTION_matchProviderWithCurrentNetwork = 14;
    static final int TRANSACTION_needs5GHzToAnyApBandConversion = 30;
    static final int TRANSACTION_queryPasspointIcon = 13;
    static final int TRANSACTION_reassociate = 23;
    static final int TRANSACTION_reconnect = 22;
    static final int TRANSACTION_registerSoftApCallback = 64;
    static final int TRANSACTION_releaseMulticastLock = 39;
    static final int TRANSACTION_releaseWifiLock = 35;
    static final int TRANSACTION_removeApLocalAllowList = 78;
    static final int TRANSACTION_removeNetwork = 16;
    static final int TRANSACTION_removePasspointConfiguration = 11;
    static final int TRANSACTION_reportActivityInfo = 2;
    static final int TRANSACTION_requestActivityInfo = 3;
    static final int TRANSACTION_restoreBackupData = 61;
    static final int TRANSACTION_restoreSupplicantBackupData = 62;
    static final int TRANSACTION_retrieveBackupData = 60;
    static final int TRANSACTION_setCountryCode = 27;
    static final int TRANSACTION_setWifiApConfiguration = 49;
    static final int TRANSACTION_setWifiEnabled = 25;
    static final int TRANSACTION_startLocalOnlyHotspot = 43;
    static final int TRANSACTION_startScan = 19;
    static final int TRANSACTION_startSoftAp = 41;
    static final int TRANSACTION_startSubscriptionProvisioning = 63;
    static final int TRANSACTION_startWatchLocalOnlyHotspot = 45;
    static final int TRANSACTION_stopLocalOnlyHotspot = 44;
    static final int TRANSACTION_stopSoftAp = 42;
    static final int TRANSACTION_stopWatchLocalOnlyHotspot = 46;
    static final int TRANSACTION_unregisterSoftApCallback = 65;
    static final int TRANSACTION_updateInterfaceIpState = 40;
    static final int TRANSACTION_updateWifiLockWorkSource = 34;
    
    public Stub()
    {
      attachInterface(this, "android.net.wifi.IWifiManager");
    }
    
    public static IWifiManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.net.wifi.IWifiManager");
      if ((localIInterface != null) && ((localIInterface instanceof IWifiManager))) {
        return (IWifiManager)localIInterface;
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
        Object localObject5 = null;
        Object localObject6 = null;
        Object localObject7 = null;
        Object localObject8 = null;
        Object localObject9 = null;
        String str = null;
        Object localObject10 = null;
        Object localObject11 = null;
        Object localObject12 = null;
        Object localObject13 = null;
        Object localObject14 = null;
        Object localObject15 = null;
        Object localObject16 = null;
        IBinder localIBinder = null;
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        boolean bool4 = false;
        boolean bool5 = false;
        boolean bool6 = false;
        boolean bool7 = false;
        boolean bool8 = false;
        boolean bool9 = false;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 85: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          if (paramParcel1.readInt() != 0) {
            bool9 = true;
          }
          enableWifiCoverageExtendFeature(bool9);
          paramParcel2.writeNoException();
          return true;
        case 84: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramInt1 = isWifiCoverageExtendFeatureEnabled();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 83: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramInt1 = isExtendingWifi();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 82: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramInt1 = isEnableWhiteList();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 81: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          bool9 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool9 = true;
          }
          enable_hotspotWhiteList(bool9);
          paramParcel2.writeNoException();
          return true;
        case 80: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramParcel1 = getApAllowList();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 79: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (WifiConfiguration)WifiConfiguration.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localIBinder;
          }
          paramInt1 = disconnectClient(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 78: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (WifiConfiguration)WifiConfiguration.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          paramInt1 = removeApLocalAllowList(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 77: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (WifiConfiguration)WifiConfiguration.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          paramInt1 = addOrUpdateApLocalAllowList(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 76: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramParcel1 = dppConfiguratorGetKey(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 75: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (WifiDppConfig)WifiDppConfig.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          paramInt1 = dppStartAuth(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 74: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramInt1 = dppConfiguratorRemove(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 73: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramInt1 = dppConfiguratorAdd(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 72: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          dppStopListen();
          paramParcel2.writeNoException();
          return true;
        case 71: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          localObject10 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool9 = true;
          } else {
            bool9 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          }
          paramInt1 = dppListen((String)localObject10, paramInt1, bool9, bool2);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 70: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramInt1 = dppBootstrapRemove(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 69: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramParcel1 = dppGetUri(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 68: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (WifiDppConfig)WifiDppConfig.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          paramInt1 = dppBootstrapGenerate(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 67: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramInt1 = dppAddBootstrapQrCode(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 66: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramParcel1 = getCapabilities(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 65: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          unregisterSoftApCallback(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 64: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          registerSoftApCallback(paramParcel1.readStrongBinder(), ISoftApCallback.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 63: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          if (paramParcel1.readInt() != 0) {
            localObject10 = (OsuProvider)OsuProvider.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject10 = localObject5;
          }
          startSubscriptionProvisioning((OsuProvider)localObject10, IProvisioningCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 62: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          restoreSupplicantBackupData(paramParcel1.createByteArray(), paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          return true;
        case 61: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          restoreBackupData(paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          return true;
        case 60: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramParcel1 = retrieveBackupData();
          paramParcel2.writeNoException();
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        case 59: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramParcel1 = getCurrentNetwork();
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
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          factoryReset(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 57: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          disableEphemeralNetwork(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 56: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          bool9 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool9 = true;
          }
          enableWifiConnectivityManager(bool9);
          paramParcel2.writeNoException();
          return true;
        case 55: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramInt1 = getVerboseLoggingLevel();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 54: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          enableVerboseLogging(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 53: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramParcel1 = getCurrentNetworkWpsNfcConfigurationToken();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 52: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          localObject10 = paramParcel1.readString();
          bool9 = bool4;
          if (paramParcel1.readInt() != 0) {
            bool9 = true;
          }
          enableTdlsWithMacAddress((String)localObject10, bool9);
          paramParcel2.writeNoException();
          return true;
        case 51: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          localObject10 = paramParcel1.readString();
          bool9 = bool5;
          if (paramParcel1.readInt() != 0) {
            bool9 = true;
          }
          enableTdls((String)localObject10, bool9);
          paramParcel2.writeNoException();
          return true;
        case 50: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramParcel1 = getWifiServiceMessenger(paramParcel1.readString());
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
        case 49: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          if (paramParcel1.readInt() != 0) {
            localObject10 = (WifiConfiguration)WifiConfiguration.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject10 = localObject6;
          }
          paramInt1 = setWifiApConfiguration((WifiConfiguration)localObject10, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 48: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramParcel1 = getWifiApConfiguration();
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
        case 47: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramInt1 = getWifiApEnabledState();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 46: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          stopWatchLocalOnlyHotspot();
          paramParcel2.writeNoException();
          return true;
        case 45: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          if (paramParcel1.readInt() != 0) {
            localObject10 = (Messenger)Messenger.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject10 = localObject7;
          }
          startWatchLocalOnlyHotspot((Messenger)localObject10, paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 44: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          stopLocalOnlyHotspot();
          paramParcel2.writeNoException();
          return true;
        case 43: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          if (paramParcel1.readInt() != 0) {
            localObject10 = (Messenger)Messenger.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject10 = localObject8;
          }
          paramInt1 = startLocalOnlyHotspot((Messenger)localObject10, paramParcel1.readStrongBinder(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 42: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramInt1 = stopSoftAp();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 41: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (WifiConfiguration)WifiConfiguration.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject9;
          }
          paramInt1 = startSoftAp(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 40: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          updateInterfaceIpState(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 39: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          releaseMulticastLock();
          paramParcel2.writeNoException();
          return true;
        case 38: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          acquireMulticastLock(paramParcel1.readStrongBinder(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 37: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramInt1 = isMulticastEnabled();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 36: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          initializeMulticastFiltering();
          paramParcel2.writeNoException();
          return true;
        case 35: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramInt1 = releaseWifiLock(paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 34: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          localObject10 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str;
          }
          updateWifiLockWorkSource((IBinder)localObject10, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 33: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          localIBinder = paramParcel1.readStrongBinder();
          paramInt1 = paramParcel1.readInt();
          str = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject10;
          }
          paramInt1 = acquireWifiLock(localIBinder, paramInt1, str, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 32: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramInt1 = isScanAlwaysAvailable();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 31: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramParcel1 = getDhcpInfo();
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
        case 30: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramInt1 = needs5GHzToAnyApBandConversion();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 29: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramInt1 = isDualBandSupported();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 28: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramParcel1 = getCountryCode();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 27: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          setCountryCode(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 26: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramInt1 = getWifiEnabledState();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 25: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          localObject10 = paramParcel1.readString();
          bool9 = bool6;
          if (paramParcel1.readInt() != 0) {
            bool9 = true;
          }
          paramInt1 = setWifiEnabled((String)localObject10, bool9);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramParcel1 = getConnectionInfo(paramParcel1.readString());
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
        case 23: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          reassociate(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          reconnect(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          disconnect(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramParcel1 = getScanResults(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramInt1 = startScan(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramInt1 = disableNetwork(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramInt1 = paramParcel1.readInt();
          bool9 = bool7;
          if (paramParcel1.readInt() != 0) {
            bool9 = true;
          }
          paramInt1 = enableNetwork(paramInt1, bool9, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramInt1 = removeNetwork(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          long l = paramParcel1.readLong();
          bool9 = bool8;
          if (paramParcel1.readInt() != 0) {
            bool9 = true;
          }
          deauthenticateNetwork(l, bool9);
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramInt1 = matchProviderWithCurrentNetwork(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          queryPasspointIcon(paramParcel1.readLong(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramParcel1 = getPasspointConfigurations();
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramInt1 = removePasspointConfiguration(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          if (paramParcel1.readInt() != 0) {
            localObject10 = (PasspointConfiguration)PasspointConfiguration.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject10 = localObject11;
          }
          paramInt1 = addOrUpdatePasspointConfiguration((PasspointConfiguration)localObject10, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          if (paramParcel1.readInt() != 0) {
            localObject10 = (WifiConfiguration)WifiConfiguration.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject10 = localObject12;
          }
          paramInt1 = addOrUpdateNetwork((WifiConfiguration)localObject10, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ScanResult)ScanResult.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject13;
          }
          paramParcel1 = getMatchingOsuProviders(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ScanResult)ScanResult.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject14;
          }
          paramParcel1 = getAllMatchingWifiConfigs(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ScanResult)ScanResult.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject15;
          }
          paramParcel1 = getMatchingWifiConfig(paramParcel1);
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
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramParcel1 = getPrivilegedConfiguredNetworks();
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
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramParcel1 = getConfiguredNetworks();
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
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject16;
          }
          requestActivityInfo(paramParcel1);
          return true;
        case 2: 
          paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
          paramParcel1 = reportActivityInfo();
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
        paramParcel1.enforceInterface("android.net.wifi.IWifiManager");
        paramInt1 = getSupportedFeatures();
        paramParcel2.writeNoException();
        paramParcel2.writeInt(paramInt1);
        return true;
      }
      paramParcel2.writeString("android.net.wifi.IWifiManager");
      return true;
    }
    
    private static class Proxy
      implements IWifiManager
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void acquireMulticastLock(IBinder paramIBinder, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeString(paramString);
          mRemote.transact(38, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean acquireWifiLock(IBinder paramIBinder, int paramInt, String paramString, WorkSource paramWorkSource)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          boolean bool = true;
          if (paramWorkSource != null)
          {
            localParcel1.writeInt(1);
            paramWorkSource.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(33, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
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
      
      public boolean addOrUpdateApLocalAllowList(WifiConfiguration paramWifiConfiguration)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          boolean bool = true;
          if (paramWifiConfiguration != null)
          {
            localParcel1.writeInt(1);
            paramWifiConfiguration.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(77, localParcel1, localParcel2, 0);
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
      
      public int addOrUpdateNetwork(WifiConfiguration paramWifiConfiguration, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          if (paramWifiConfiguration != null)
          {
            localParcel1.writeInt(1);
            paramWifiConfiguration.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(9, localParcel1, localParcel2, 0);
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
      
      public boolean addOrUpdatePasspointConfiguration(PasspointConfiguration paramPasspointConfiguration, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          boolean bool = true;
          if (paramPasspointConfiguration != null)
          {
            localParcel1.writeInt(1);
            paramPasspointConfiguration.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(10, localParcel1, localParcel2, 0);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public void deauthenticateNetwork(long paramLong, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeLong(paramLong);
          localParcel1.writeInt(paramBoolean);
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
      
      public void disableEphemeralNetwork(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
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
      
      public boolean disableNetwork(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(18, localParcel1, localParcel2, 0);
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
      
      public void disconnect(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeString(paramString);
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
      
      public boolean disconnectClient(WifiConfiguration paramWifiConfiguration)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          boolean bool = true;
          if (paramWifiConfiguration != null)
          {
            localParcel1.writeInt(1);
            paramWifiConfiguration.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(79, localParcel1, localParcel2, 0);
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
      
      public int dppAddBootstrapQrCode(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeString(paramString);
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
      
      public int dppBootstrapGenerate(WifiDppConfig paramWifiDppConfig)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          if (paramWifiDppConfig != null)
          {
            localParcel1.writeInt(1);
            paramWifiDppConfig.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(68, localParcel1, localParcel2, 0);
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
      
      public int dppBootstrapRemove(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(70, localParcel1, localParcel2, 0);
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
      
      public int dppConfiguratorAdd(String paramString1, String paramString2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
          mRemote.transact(73, localParcel1, localParcel2, 0);
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
      
      public String dppConfiguratorGetKey(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(76, localParcel1, localParcel2, 0);
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
      
      public int dppConfiguratorRemove(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(74, localParcel1, localParcel2, 0);
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
      
      public String dppGetUri(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(69, localParcel1, localParcel2, 0);
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
      
      public int dppListen(String paramString, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeInt(paramBoolean2);
          mRemote.transact(71, localParcel1, localParcel2, 0);
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
      
      public int dppStartAuth(WifiDppConfig paramWifiDppConfig)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          if (paramWifiDppConfig != null)
          {
            localParcel1.writeInt(1);
            paramWifiDppConfig.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(75, localParcel1, localParcel2, 0);
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
      
      public void dppStopListen()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
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
      
      public boolean enableNetwork(int paramInt, boolean paramBoolean, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(17, localParcel1, localParcel2, 0);
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
      
      public void enableTdls(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
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
      
      public void enableTdlsWithMacAddress(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
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
      
      public void enableVerboseLogging(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeInt(paramInt);
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
      
      public void enableWifiConnectivityManager(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeInt(paramBoolean);
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
      
      public void enableWifiCoverageExtendFeature(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeInt(paramBoolean);
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
      
      public void enable_hotspotWhiteList(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeInt(paramBoolean);
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
      
      public void factoryReset(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeString(paramString);
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
      
      public List<WifiConfiguration> getAllMatchingWifiConfigs(ScanResult paramScanResult)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          if (paramScanResult != null)
          {
            localParcel1.writeInt(1);
            paramScanResult.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramScanResult = localParcel2.createTypedArrayList(WifiConfiguration.CREATOR);
          return paramScanResult;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<WifiConfiguration> getApAllowList()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          mRemote.transact(80, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(WifiConfiguration.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getCapabilities(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeString(paramString);
          mRemote.transact(66, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParceledListSlice getConfiguredNetworks()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ParceledListSlice localParceledListSlice;
          if (localParcel2.readInt() != 0) {
            localParceledListSlice = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            localParceledListSlice = null;
          }
          return localParceledListSlice;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public WifiInfo getConnectionInfo(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeString(paramString);
          mRemote.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (WifiInfo)WifiInfo.CREATOR.createFromParcel(localParcel2);
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
      
      public String getCountryCode()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          mRemote.transact(28, localParcel1, localParcel2, 0);
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
      
      public Network getCurrentNetwork()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          mRemote.transact(59, localParcel1, localParcel2, 0);
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
      
      public String getCurrentNetworkWpsNfcConfigurationToken()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          mRemote.transact(53, localParcel1, localParcel2, 0);
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
      
      public DhcpInfo getDhcpInfo()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          mRemote.transact(31, localParcel1, localParcel2, 0);
          localParcel2.readException();
          DhcpInfo localDhcpInfo;
          if (localParcel2.readInt() != 0) {
            localDhcpInfo = (DhcpInfo)DhcpInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localDhcpInfo = null;
          }
          return localDhcpInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.net.wifi.IWifiManager";
      }
      
      public List<OsuProvider> getMatchingOsuProviders(ScanResult paramScanResult)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          if (paramScanResult != null)
          {
            localParcel1.writeInt(1);
            paramScanResult.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramScanResult = localParcel2.createTypedArrayList(OsuProvider.CREATOR);
          return paramScanResult;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public WifiConfiguration getMatchingWifiConfig(ScanResult paramScanResult)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          if (paramScanResult != null)
          {
            localParcel1.writeInt(1);
            paramScanResult.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramScanResult = (WifiConfiguration)WifiConfiguration.CREATOR.createFromParcel(localParcel2);
          } else {
            paramScanResult = null;
          }
          return paramScanResult;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<PasspointConfiguration> getPasspointConfigurations()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(PasspointConfiguration.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParceledListSlice getPrivilegedConfiguredNetworks()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ParceledListSlice localParceledListSlice;
          if (localParcel2.readInt() != 0) {
            localParceledListSlice = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            localParceledListSlice = null;
          }
          return localParceledListSlice;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<ScanResult> getScanResults(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeString(paramString);
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createTypedArrayList(ScanResult.CREATOR);
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getSupportedFeatures()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          mRemote.transact(1, localParcel1, localParcel2, 0);
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
      
      public int getVerboseLoggingLevel()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          mRemote.transact(55, localParcel1, localParcel2, 0);
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
      
      public WifiConfiguration getWifiApConfiguration()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          mRemote.transact(48, localParcel1, localParcel2, 0);
          localParcel2.readException();
          WifiConfiguration localWifiConfiguration;
          if (localParcel2.readInt() != 0) {
            localWifiConfiguration = (WifiConfiguration)WifiConfiguration.CREATOR.createFromParcel(localParcel2);
          } else {
            localWifiConfiguration = null;
          }
          return localWifiConfiguration;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getWifiApEnabledState()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          mRemote.transact(47, localParcel1, localParcel2, 0);
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
      
      public int getWifiEnabledState()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          mRemote.transact(26, localParcel1, localParcel2, 0);
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
      
      public Messenger getWifiServiceMessenger(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeString(paramString);
          mRemote.transact(50, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (Messenger)Messenger.CREATOR.createFromParcel(localParcel2);
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
      
      public void initializeMulticastFiltering()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
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
      
      public boolean isDualBandSupported()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(29, localParcel1, localParcel2, 0);
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
      
      public boolean isEnableWhiteList()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(82, localParcel1, localParcel2, 0);
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
      
      public boolean isExtendingWifi()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(83, localParcel1, localParcel2, 0);
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
      
      public boolean isMulticastEnabled()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(37, localParcel1, localParcel2, 0);
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
      
      public boolean isScanAlwaysAvailable()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(32, localParcel1, localParcel2, 0);
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
      
      public boolean isWifiCoverageExtendFeatureEnabled()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(84, localParcel1, localParcel2, 0);
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
      
      public int matchProviderWithCurrentNetwork(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeString(paramString);
          mRemote.transact(14, localParcel1, localParcel2, 0);
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
      
      public boolean needs5GHzToAnyApBandConversion()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(30, localParcel1, localParcel2, 0);
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
      
      public void queryPasspointIcon(long paramLong, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeLong(paramLong);
          localParcel1.writeString(paramString);
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
      
      public void reassociate(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
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
      
      public void reconnect(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeString(paramString);
          mRemote.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerSoftApCallback(IBinder paramIBinder, ISoftApCallback paramISoftApCallback, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramISoftApCallback != null) {
            paramIBinder = paramISoftApCallback.asBinder();
          } else {
            paramIBinder = null;
          }
          localParcel1.writeStrongBinder(paramIBinder);
          localParcel1.writeInt(paramInt);
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
      
      public void releaseMulticastLock()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          mRemote.transact(39, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean releaseWifiLock(IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeStrongBinder(paramIBinder);
          paramIBinder = mRemote;
          boolean bool = false;
          paramIBinder.transact(35, localParcel1, localParcel2, 0);
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
      
      public boolean removeApLocalAllowList(WifiConfiguration paramWifiConfiguration)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          boolean bool = true;
          if (paramWifiConfiguration != null)
          {
            localParcel1.writeInt(1);
            paramWifiConfiguration.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(78, localParcel1, localParcel2, 0);
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
      
      public boolean removeNetwork(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(16, localParcel1, localParcel2, 0);
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
      
      public boolean removePasspointConfiguration(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          paramString1 = mRemote;
          boolean bool = false;
          paramString1.transact(11, localParcel1, localParcel2, 0);
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
      
      public WifiActivityEnergyInfo reportActivityInfo()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          WifiActivityEnergyInfo localWifiActivityEnergyInfo;
          if (localParcel2.readInt() != 0) {
            localWifiActivityEnergyInfo = (WifiActivityEnergyInfo)WifiActivityEnergyInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            localWifiActivityEnergyInfo = null;
          }
          return localWifiActivityEnergyInfo;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void requestActivityInfo(ResultReceiver paramResultReceiver)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.net.wifi.IWifiManager");
          if (paramResultReceiver != null)
          {
            localParcel.writeInt(1);
            paramResultReceiver.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(3, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void restoreBackupData(byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeByteArray(paramArrayOfByte);
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
      
      public void restoreSupplicantBackupData(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeByteArray(paramArrayOfByte1);
          localParcel1.writeByteArray(paramArrayOfByte2);
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
      
      public byte[] retrieveBackupData()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          mRemote.transact(60, localParcel1, localParcel2, 0);
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
      
      public void setCountryCode(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeString(paramString);
          mRemote.transact(27, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setWifiApConfiguration(WifiConfiguration paramWifiConfiguration, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          boolean bool = true;
          if (paramWifiConfiguration != null)
          {
            localParcel1.writeInt(1);
            paramWifiConfiguration.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(49, localParcel1, localParcel2, 0);
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
      
      public boolean setWifiEnabled(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(25, localParcel1, localParcel2, 0);
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
      
      public int startLocalOnlyHotspot(Messenger paramMessenger, IBinder paramIBinder, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
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
          localParcel1.writeString(paramString);
          mRemote.transact(43, localParcel1, localParcel2, 0);
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
      
      public boolean startScan(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(19, localParcel1, localParcel2, 0);
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
      
      public boolean startSoftAp(WifiConfiguration paramWifiConfiguration)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          boolean bool = true;
          if (paramWifiConfiguration != null)
          {
            localParcel1.writeInt(1);
            paramWifiConfiguration.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(41, localParcel1, localParcel2, 0);
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
      
      public void startSubscriptionProvisioning(OsuProvider paramOsuProvider, IProvisioningCallback paramIProvisioningCallback)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          if (paramOsuProvider != null)
          {
            localParcel1.writeInt(1);
            paramOsuProvider.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIProvisioningCallback != null) {
            paramOsuProvider = paramIProvisioningCallback.asBinder();
          } else {
            paramOsuProvider = null;
          }
          localParcel1.writeStrongBinder(paramOsuProvider);
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
      
      public void startWatchLocalOnlyHotspot(Messenger paramMessenger, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
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
      
      public void stopLocalOnlyHotspot()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
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
      
      public boolean stopSoftAp()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(42, localParcel1, localParcel2, 0);
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
      
      public void stopWatchLocalOnlyHotspot()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
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
      
      public void unregisterSoftApCallback(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeInt(paramInt);
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
      
      public void updateInterfaceIpState(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
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
      
      public void updateWifiLockWorkSource(IBinder paramIBinder, WorkSource paramWorkSource)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.net.wifi.IWifiManager");
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramWorkSource != null)
          {
            localParcel1.writeInt(1);
            paramWorkSource.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
    }
  }
}
