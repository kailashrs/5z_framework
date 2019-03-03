package android.app;

import android.accounts.AccountManager;
import android.accounts.IAccountManager.Stub;
import android.app.admin.DevicePolicyManager;
import android.app.admin.IDevicePolicyManager.Stub;
import android.app.job.IJobScheduler.Stub;
import android.app.job.JobScheduler;
import android.app.slice.SliceManager;
import android.app.timezone.RulesManager;
import android.app.trust.TrustManager;
import android.app.usage.IStorageStatsManager.Stub;
import android.app.usage.IUsageStatsManager;
import android.app.usage.IUsageStatsManager.Stub;
import android.app.usage.NetworkStatsManager;
import android.app.usage.StorageStatsManager;
import android.app.usage.UsageStatsManager;
import android.appwidget.AppWidgetManager;
import android.bluetooth.BluetoothManager;
import android.companion.CompanionDeviceManager;
import android.companion.ICompanionDeviceManager;
import android.companion.ICompanionDeviceManager.Stub;
import android.content.Context;
import android.content.IRestrictionsManager.Stub;
import android.content.RestrictionsManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.CrossProfileApps;
import android.content.pm.ICrossProfileApps.Stub;
import android.content.pm.IShortcutService.Stub;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutManager;
import android.content.res.Resources;
import android.hardware.ConsumerIrManager;
import android.hardware.ISerialManager.Stub;
import android.hardware.SensorManager;
import android.hardware.SerialManager;
import android.hardware.SystemSensorManager;
import android.hardware.camera2.CameraManager;
import android.hardware.display.DisplayManager;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.IFingerprintService;
import android.hardware.fingerprint.IFingerprintService.Stub;
import android.hardware.hdmi.HdmiControlManager;
import android.hardware.hdmi.IHdmiControlService.Stub;
import android.hardware.input.InputManager;
import android.hardware.location.ContextHubManager;
import android.hardware.radio.RadioManager;
import android.hardware.usb.IUsbManager.Stub;
import android.hardware.usb.UsbManager;
import android.location.CountryDetector;
import android.location.ICountryDetector.Stub;
import android.location.ILocationManager.Stub;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaRouter;
import android.media.midi.IMidiManager.Stub;
import android.media.midi.MidiManager;
import android.media.projection.MediaProjectionManager;
import android.media.session.MediaSessionManager;
import android.media.soundtrigger.SoundTriggerManager;
import android.media.tv.ITvInputManager.Stub;
import android.media.tv.TvInputManager;
import android.net.ConnectivityManager;
import android.net.ConnectivityThread;
import android.net.EthernetManager;
import android.net.IConnectivityManager.Stub;
import android.net.IEthernetManager;
import android.net.IEthernetManager.Stub;
import android.net.IIpSecService.Stub;
import android.net.INetworkPolicyManager.Stub;
import android.net.IpSecManager;
import android.net.NetworkPolicyManager;
import android.net.NetworkScoreManager;
import android.net.NetworkWatchlistManager;
import android.net.lowpan.ILowpanManager;
import android.net.lowpan.ILowpanManager.Stub;
import android.net.lowpan.LowpanManager;
import android.net.nsd.INsdManager;
import android.net.nsd.INsdManager.Stub;
import android.net.nsd.NsdManager;
import android.net.wifi.IWifiManager;
import android.net.wifi.IWifiManager.Stub;
import android.net.wifi.IWifiScanner;
import android.net.wifi.IWifiScanner.Stub;
import android.net.wifi.RttManager;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiScanner;
import android.net.wifi.aware.IWifiAwareManager;
import android.net.wifi.aware.IWifiAwareManager.Stub;
import android.net.wifi.aware.WifiAwareManager;
import android.net.wifi.p2p.IWifiP2pManager.Stub;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.rtt.IWifiRttManager;
import android.net.wifi.rtt.IWifiRttManager.Stub;
import android.net.wifi.rtt.WifiRttManager;
import android.nfc.NfcManager;
import android.os.BatteryManager;
import android.os.DeviceIdleManager;
import android.os.DropBoxManager;
import android.os.Handler;
import android.os.HardwarePropertiesManager;
import android.os.IBatteryPropertiesRegistrar.Stub;
import android.os.IBinder;
import android.os.IDeviceIdleController;
import android.os.IDeviceIdleController.Stub;
import android.os.IHardwarePropertiesManager.Stub;
import android.os.IPowerManager;
import android.os.IPowerManager.Stub;
import android.os.IRecoverySystem.Stub;
import android.os.ISystemUpdateManager.Stub;
import android.os.IUserManager.Stub;
import android.os.IncidentManager;
import android.os.PowerManager;
import android.os.Process;
import android.os.RecoverySystem;
import android.os.ServiceManager;
import android.os.ServiceManager.ServiceNotFoundException;
import android.os.SystemUpdateManager;
import android.os.SystemVibrator;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.Vibrator;
import android.os.health.SystemHealthManager;
import android.os.storage.StorageManager;
import android.print.IPrintManager;
import android.print.IPrintManager.Stub;
import android.print.PrintManager;
import android.service.oemlock.IOemLockService;
import android.service.oemlock.IOemLockService.Stub;
import android.service.oemlock.OemLockManager;
import android.service.persistentdata.IPersistentDataBlockService;
import android.service.persistentdata.IPersistentDataBlockService.Stub;
import android.service.persistentdata.PersistentDataBlockManager;
import android.service.vr.IVrManager.Stub;
import android.telecom.TelecomManager;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.euicc.EuiccCardManager;
import android.telephony.euicc.EuiccManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.WindowManagerImpl;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.CaptioningManager;
import android.view.autofill.AutofillManager;
import android.view.autofill.IAutoFillManager;
import android.view.autofill.IAutoFillManager.Stub;
import android.view.inputmethod.InputMethodManager;
import android.view.textclassifier.TextClassificationManager;
import android.view.textservice.TextServicesManager;
import com.android.internal.app.IAppOpsService.Stub;
import com.android.internal.app.IBatteryStats.Stub;
import com.android.internal.app.ISoundTriggerService.Stub;
import com.android.internal.appwidget.IAppWidgetService.Stub;
import com.android.internal.net.INetworkWatchlistManager.Stub;
import com.android.internal.os.IDropBoxManagerService.Stub;
import com.android.internal.policy.PhoneLayoutInflater;
import java.util.HashMap;

final class SystemServiceRegistry
{
  private static final HashMap<String, ServiceFetcher<?>> SYSTEM_SERVICE_FETCHERS;
  private static final HashMap<Class<?>, String> SYSTEM_SERVICE_NAMES = new HashMap();
  private static final String TAG = "SystemServiceRegistry";
  private static int sServiceCacheSize;
  
  static
  {
    SYSTEM_SERVICE_FETCHERS = new HashMap();
    registerService("accessibility", AccessibilityManager.class, new CachedServiceFetcher()
    {
      public AccessibilityManager createService(ContextImpl paramAnonymousContextImpl)
      {
        return AccessibilityManager.getInstance(paramAnonymousContextImpl);
      }
    });
    registerService("captioning", CaptioningManager.class, new CachedServiceFetcher()
    {
      public CaptioningManager createService(ContextImpl paramAnonymousContextImpl)
      {
        return new CaptioningManager(paramAnonymousContextImpl);
      }
    });
    registerService("account", AccountManager.class, new CachedServiceFetcher()
    {
      public AccountManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new AccountManager(paramAnonymousContextImpl, IAccountManager.Stub.asInterface(ServiceManager.getServiceOrThrow("account")));
      }
    });
    registerService("activity", ActivityManager.class, new CachedServiceFetcher()
    {
      public ActivityManager createService(ContextImpl paramAnonymousContextImpl)
      {
        return new ActivityManager(paramAnonymousContextImpl.getOuterContext(), mMainThread.getHandler());
      }
    });
    registerService("alarm", AlarmManager.class, new CachedServiceFetcher()
    {
      public AlarmManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new AlarmManager(IAlarmManager.Stub.asInterface(ServiceManager.getServiceOrThrow("alarm")), paramAnonymousContextImpl);
      }
    });
    registerService("audio", AudioManager.class, new CachedServiceFetcher()
    {
      public AudioManager createService(ContextImpl paramAnonymousContextImpl)
      {
        return new AudioManager(paramAnonymousContextImpl);
      }
    });
    registerService("media_router", MediaRouter.class, new CachedServiceFetcher()
    {
      public MediaRouter createService(ContextImpl paramAnonymousContextImpl)
      {
        return new MediaRouter(paramAnonymousContextImpl);
      }
    });
    registerService("bluetooth", BluetoothManager.class, new CachedServiceFetcher()
    {
      public BluetoothManager createService(ContextImpl paramAnonymousContextImpl)
      {
        return new BluetoothManager(paramAnonymousContextImpl);
      }
    });
    registerService("hdmi_control", HdmiControlManager.class, new StaticServiceFetcher()
    {
      public HdmiControlManager createService()
        throws ServiceManager.ServiceNotFoundException
      {
        return new HdmiControlManager(IHdmiControlService.Stub.asInterface(ServiceManager.getServiceOrThrow("hdmi_control")));
      }
    });
    registerService("textclassification", TextClassificationManager.class, new CachedServiceFetcher()
    {
      public TextClassificationManager createService(ContextImpl paramAnonymousContextImpl)
      {
        return new TextClassificationManager(paramAnonymousContextImpl);
      }
    });
    registerService("clipboard", android.content.ClipboardManager.class, new CachedServiceFetcher()
    {
      public android.content.ClipboardManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new android.content.ClipboardManager(paramAnonymousContextImpl.getOuterContext(), mMainThread.getHandler());
      }
    });
    SYSTEM_SERVICE_NAMES.put(android.text.ClipboardManager.class, "clipboard");
    registerService("connectivity", ConnectivityManager.class, new StaticApplicationContextServiceFetcher()
    {
      public ConnectivityManager createService(Context paramAnonymousContext)
        throws ServiceManager.ServiceNotFoundException
      {
        return new ConnectivityManager(paramAnonymousContext, IConnectivityManager.Stub.asInterface(ServiceManager.getServiceOrThrow("connectivity")));
      }
    });
    registerService("ipsec", IpSecManager.class, new CachedServiceFetcher()
    {
      public IpSecManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new IpSecManager(paramAnonymousContextImpl, IIpSecService.Stub.asInterface(ServiceManager.getService("ipsec")));
      }
    });
    registerService("country_detector", CountryDetector.class, new StaticServiceFetcher()
    {
      public CountryDetector createService()
        throws ServiceManager.ServiceNotFoundException
      {
        return new CountryDetector(ICountryDetector.Stub.asInterface(ServiceManager.getServiceOrThrow("country_detector")));
      }
    });
    registerService("device_policy", DevicePolicyManager.class, new CachedServiceFetcher()
    {
      public DevicePolicyManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new DevicePolicyManager(paramAnonymousContextImpl, IDevicePolicyManager.Stub.asInterface(ServiceManager.getServiceOrThrow("device_policy")));
      }
    });
    registerService("download", DownloadManager.class, new CachedServiceFetcher()
    {
      public DownloadManager createService(ContextImpl paramAnonymousContextImpl)
      {
        return new DownloadManager(paramAnonymousContextImpl);
      }
    });
    registerService("batterymanager", BatteryManager.class, new CachedServiceFetcher()
    {
      public BatteryManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new BatteryManager(paramAnonymousContextImpl, IBatteryStats.Stub.asInterface(ServiceManager.getServiceOrThrow("batterystats")), IBatteryPropertiesRegistrar.Stub.asInterface(ServiceManager.getServiceOrThrow("batteryproperties")));
      }
    });
    registerService("nfc", NfcManager.class, new CachedServiceFetcher()
    {
      public NfcManager createService(ContextImpl paramAnonymousContextImpl)
      {
        return new NfcManager(paramAnonymousContextImpl);
      }
    });
    registerService("dropbox", DropBoxManager.class, new CachedServiceFetcher()
    {
      public DropBoxManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new DropBoxManager(paramAnonymousContextImpl, IDropBoxManagerService.Stub.asInterface(ServiceManager.getServiceOrThrow("dropbox")));
      }
    });
    registerService("input", InputManager.class, new StaticServiceFetcher()
    {
      public InputManager createService()
      {
        return InputManager.getInstance();
      }
    });
    registerService("display", DisplayManager.class, new CachedServiceFetcher()
    {
      public DisplayManager createService(ContextImpl paramAnonymousContextImpl)
      {
        return new DisplayManager(paramAnonymousContextImpl.getOuterContext());
      }
    });
    registerService("input_method", InputMethodManager.class, new StaticServiceFetcher()
    {
      public InputMethodManager createService()
      {
        return InputMethodManager.getInstance();
      }
    });
    registerService("textservices", TextServicesManager.class, new StaticServiceFetcher()
    {
      public TextServicesManager createService()
      {
        return TextServicesManager.getInstance();
      }
    });
    registerService("keyguard", KeyguardManager.class, new CachedServiceFetcher()
    {
      public KeyguardManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new KeyguardManager(paramAnonymousContextImpl);
      }
    });
    registerService("layout_inflater", LayoutInflater.class, new CachedServiceFetcher()
    {
      public LayoutInflater createService(ContextImpl paramAnonymousContextImpl)
      {
        return new PhoneLayoutInflater(paramAnonymousContextImpl.getOuterContext());
      }
    });
    registerService("location", LocationManager.class, new CachedServiceFetcher()
    {
      public LocationManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new LocationManager(paramAnonymousContextImpl, ILocationManager.Stub.asInterface(ServiceManager.getServiceOrThrow("location")));
      }
    });
    registerService("netpolicy", NetworkPolicyManager.class, new CachedServiceFetcher()
    {
      public NetworkPolicyManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new NetworkPolicyManager(paramAnonymousContextImpl, INetworkPolicyManager.Stub.asInterface(ServiceManager.getServiceOrThrow("netpolicy")));
      }
    });
    registerService("notification", NotificationManager.class, new CachedServiceFetcher()
    {
      public NotificationManager createService(ContextImpl paramAnonymousContextImpl)
      {
        Context localContext = paramAnonymousContextImpl.getOuterContext();
        return new NotificationManager(new ContextThemeWrapper(localContext, Resources.selectSystemTheme(0, getApplicationInfotargetSdkVersion, 16973835, 16973935, 16974126, 16974130)), mMainThread.getHandler());
      }
    });
    registerService("servicediscovery", NsdManager.class, new CachedServiceFetcher()
    {
      public NsdManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        INsdManager localINsdManager = INsdManager.Stub.asInterface(ServiceManager.getServiceOrThrow("servicediscovery"));
        return new NsdManager(paramAnonymousContextImpl.getOuterContext(), localINsdManager);
      }
    });
    registerService("power", PowerManager.class, new CachedServiceFetcher()
    {
      public PowerManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        IPowerManager localIPowerManager = IPowerManager.Stub.asInterface(ServiceManager.getServiceOrThrow("power"));
        return new PowerManager(paramAnonymousContextImpl.getOuterContext(), localIPowerManager, mMainThread.getHandler());
      }
    });
    registerService("recovery", RecoverySystem.class, new CachedServiceFetcher()
    {
      public RecoverySystem createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new RecoverySystem(IRecoverySystem.Stub.asInterface(ServiceManager.getServiceOrThrow("recovery")));
      }
    });
    registerService("search", SearchManager.class, new CachedServiceFetcher()
    {
      public SearchManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new SearchManager(paramAnonymousContextImpl.getOuterContext(), mMainThread.getHandler());
      }
    });
    registerService("sensor", SensorManager.class, new CachedServiceFetcher()
    {
      public SensorManager createService(ContextImpl paramAnonymousContextImpl)
      {
        return new SystemSensorManager(paramAnonymousContextImpl.getOuterContext(), mMainThread.getHandler().getLooper());
      }
    });
    registerService("stats", StatsManager.class, new CachedServiceFetcher()
    {
      public StatsManager createService(ContextImpl paramAnonymousContextImpl)
      {
        return new StatsManager(paramAnonymousContextImpl.getOuterContext());
      }
    });
    registerService("statusbar", StatusBarManager.class, new CachedServiceFetcher()
    {
      public StatusBarManager createService(ContextImpl paramAnonymousContextImpl)
      {
        return new StatusBarManager(paramAnonymousContextImpl.getOuterContext());
      }
    });
    registerService("storage", StorageManager.class, new CachedServiceFetcher()
    {
      public StorageManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new StorageManager(paramAnonymousContextImpl, mMainThread.getHandler().getLooper());
      }
    });
    registerService("storagestats", StorageStatsManager.class, new CachedServiceFetcher()
    {
      public StorageStatsManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new StorageStatsManager(paramAnonymousContextImpl, IStorageStatsManager.Stub.asInterface(ServiceManager.getServiceOrThrow("storagestats")));
      }
    });
    registerService("system_update", SystemUpdateManager.class, new CachedServiceFetcher()
    {
      public SystemUpdateManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new SystemUpdateManager(ISystemUpdateManager.Stub.asInterface(ServiceManager.getServiceOrThrow("system_update")));
      }
    });
    registerService("phone", TelephonyManager.class, new CachedServiceFetcher()
    {
      public TelephonyManager createService(ContextImpl paramAnonymousContextImpl)
      {
        return new TelephonyManager(paramAnonymousContextImpl.getOuterContext());
      }
    });
    registerService("telephony_subscription_service", SubscriptionManager.class, new CachedServiceFetcher()
    {
      public SubscriptionManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new SubscriptionManager(paramAnonymousContextImpl.getOuterContext());
      }
    });
    registerService("carrier_config", CarrierConfigManager.class, new CachedServiceFetcher()
    {
      public CarrierConfigManager createService(ContextImpl paramAnonymousContextImpl)
      {
        return new CarrierConfigManager();
      }
    });
    registerService("telecom", TelecomManager.class, new CachedServiceFetcher()
    {
      public TelecomManager createService(ContextImpl paramAnonymousContextImpl)
      {
        return new TelecomManager(paramAnonymousContextImpl.getOuterContext());
      }
    });
    registerService("euicc", EuiccManager.class, new CachedServiceFetcher()
    {
      public EuiccManager createService(ContextImpl paramAnonymousContextImpl)
      {
        return new EuiccManager(paramAnonymousContextImpl.getOuterContext());
      }
    });
    registerService("euicc_card", EuiccCardManager.class, new CachedServiceFetcher()
    {
      public EuiccCardManager createService(ContextImpl paramAnonymousContextImpl)
      {
        return new EuiccCardManager(paramAnonymousContextImpl.getOuterContext());
      }
    });
    registerService("uimode", UiModeManager.class, new CachedServiceFetcher()
    {
      public UiModeManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new UiModeManager();
      }
    });
    registerService("usb", UsbManager.class, new CachedServiceFetcher()
    {
      public UsbManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new UsbManager(paramAnonymousContextImpl, IUsbManager.Stub.asInterface(ServiceManager.getServiceOrThrow("usb")));
      }
    });
    registerService("serial", SerialManager.class, new CachedServiceFetcher()
    {
      public SerialManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new SerialManager(paramAnonymousContextImpl, ISerialManager.Stub.asInterface(ServiceManager.getServiceOrThrow("serial")));
      }
    });
    registerService("vibrator", Vibrator.class, new CachedServiceFetcher()
    {
      public Vibrator createService(ContextImpl paramAnonymousContextImpl)
      {
        return new SystemVibrator(paramAnonymousContextImpl);
      }
    });
    registerService("wallpaper", WallpaperManager.class, new CachedServiceFetcher()
    {
      public WallpaperManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        IBinder localIBinder;
        if (getApplicationInfotargetSdkVersion >= 28) {
          localIBinder = ServiceManager.getServiceOrThrow("wallpaper");
        } else {
          localIBinder = ServiceManager.getService("wallpaper");
        }
        return new WallpaperManager(IWallpaperManager.Stub.asInterface(localIBinder), paramAnonymousContextImpl.getOuterContext(), mMainThread.getHandler());
      }
    });
    registerService("lowpan", LowpanManager.class, new CachedServiceFetcher()
    {
      public LowpanManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        ILowpanManager localILowpanManager = ILowpanManager.Stub.asInterface(ServiceManager.getServiceOrThrow("lowpan"));
        return new LowpanManager(paramAnonymousContextImpl.getOuterContext(), localILowpanManager, ConnectivityThread.getInstanceLooper());
      }
    });
    registerService("wifi", WifiManager.class, new CachedServiceFetcher()
    {
      public WifiManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        IWifiManager localIWifiManager = IWifiManager.Stub.asInterface(ServiceManager.getServiceOrThrow("wifi"));
        return new WifiManager(paramAnonymousContextImpl.getOuterContext(), localIWifiManager, ConnectivityThread.getInstanceLooper());
      }
    });
    registerService("wifip2p", WifiP2pManager.class, new StaticServiceFetcher()
    {
      public WifiP2pManager createService()
        throws ServiceManager.ServiceNotFoundException
      {
        return new WifiP2pManager(IWifiP2pManager.Stub.asInterface(ServiceManager.getServiceOrThrow("wifip2p")));
      }
    });
    registerService("wifiaware", WifiAwareManager.class, new CachedServiceFetcher()
    {
      public WifiAwareManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        IWifiAwareManager localIWifiAwareManager = IWifiAwareManager.Stub.asInterface(ServiceManager.getServiceOrThrow("wifiaware"));
        if (localIWifiAwareManager == null) {
          return null;
        }
        return new WifiAwareManager(paramAnonymousContextImpl.getOuterContext(), localIWifiAwareManager);
      }
    });
    registerService("wifiscanner", WifiScanner.class, new CachedServiceFetcher()
    {
      public WifiScanner createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        IWifiScanner localIWifiScanner = IWifiScanner.Stub.asInterface(ServiceManager.getServiceOrThrow("wifiscanner"));
        return new WifiScanner(paramAnonymousContextImpl.getOuterContext(), localIWifiScanner, ConnectivityThread.getInstanceLooper());
      }
    });
    registerService("rttmanager", RttManager.class, new CachedServiceFetcher()
    {
      public RttManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        IWifiRttManager localIWifiRttManager = IWifiRttManager.Stub.asInterface(ServiceManager.getServiceOrThrow("wifirtt"));
        return new RttManager(paramAnonymousContextImpl.getOuterContext(), new WifiRttManager(paramAnonymousContextImpl.getOuterContext(), localIWifiRttManager));
      }
    });
    registerService("wifirtt", WifiRttManager.class, new CachedServiceFetcher()
    {
      public WifiRttManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        IWifiRttManager localIWifiRttManager = IWifiRttManager.Stub.asInterface(ServiceManager.getServiceOrThrow("wifirtt"));
        return new WifiRttManager(paramAnonymousContextImpl.getOuterContext(), localIWifiRttManager);
      }
    });
    registerService("ethernet", EthernetManager.class, new CachedServiceFetcher()
    {
      public EthernetManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        IEthernetManager localIEthernetManager = IEthernetManager.Stub.asInterface(ServiceManager.getServiceOrThrow("ethernet"));
        return new EthernetManager(paramAnonymousContextImpl.getOuterContext(), localIEthernetManager);
      }
    });
    registerService("window", WindowManager.class, new CachedServiceFetcher()
    {
      public WindowManager createService(ContextImpl paramAnonymousContextImpl)
      {
        return new WindowManagerImpl(paramAnonymousContextImpl);
      }
    });
    registerService("user", UserManager.class, new CachedServiceFetcher()
    {
      public UserManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new UserManager(paramAnonymousContextImpl, IUserManager.Stub.asInterface(ServiceManager.getServiceOrThrow("user")));
      }
    });
    registerService("appops", AppOpsManager.class, new CachedServiceFetcher()
    {
      public AppOpsManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new AppOpsManager(paramAnonymousContextImpl, IAppOpsService.Stub.asInterface(ServiceManager.getServiceOrThrow("appops")));
      }
    });
    registerService("camera", CameraManager.class, new CachedServiceFetcher()
    {
      public CameraManager createService(ContextImpl paramAnonymousContextImpl)
      {
        return new CameraManager(paramAnonymousContextImpl);
      }
    });
    registerService("launcherapps", LauncherApps.class, new CachedServiceFetcher()
    {
      public LauncherApps createService(ContextImpl paramAnonymousContextImpl)
      {
        return new LauncherApps(paramAnonymousContextImpl);
      }
    });
    registerService("restrictions", RestrictionsManager.class, new CachedServiceFetcher()
    {
      public RestrictionsManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new RestrictionsManager(paramAnonymousContextImpl, IRestrictionsManager.Stub.asInterface(ServiceManager.getServiceOrThrow("restrictions")));
      }
    });
    registerService("print", PrintManager.class, new CachedServiceFetcher()
    {
      public PrintManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        IPrintManager localIPrintManager = null;
        if (paramAnonymousContextImpl.getPackageManager().hasSystemFeature("android.software.print")) {
          localIPrintManager = IPrintManager.Stub.asInterface(ServiceManager.getServiceOrThrow("print"));
        }
        int i = paramAnonymousContextImpl.getUserId();
        int j = UserHandle.getAppId(getApplicationInfouid);
        return new PrintManager(paramAnonymousContextImpl.getOuterContext(), localIPrintManager, i, j);
      }
    });
    registerService("companiondevice", CompanionDeviceManager.class, new CachedServiceFetcher()
    {
      public CompanionDeviceManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        ICompanionDeviceManager localICompanionDeviceManager = null;
        if (paramAnonymousContextImpl.getPackageManager().hasSystemFeature("android.software.companion_device_setup")) {
          localICompanionDeviceManager = ICompanionDeviceManager.Stub.asInterface(ServiceManager.getServiceOrThrow("companiondevice"));
        }
        return new CompanionDeviceManager(localICompanionDeviceManager, paramAnonymousContextImpl.getOuterContext());
      }
    });
    registerService("consumer_ir", ConsumerIrManager.class, new CachedServiceFetcher()
    {
      public ConsumerIrManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new ConsumerIrManager(paramAnonymousContextImpl);
      }
    });
    registerService("media_session", MediaSessionManager.class, new CachedServiceFetcher()
    {
      public MediaSessionManager createService(ContextImpl paramAnonymousContextImpl)
      {
        return new MediaSessionManager(paramAnonymousContextImpl);
      }
    });
    registerService("trust", TrustManager.class, new StaticServiceFetcher()
    {
      public TrustManager createService()
        throws ServiceManager.ServiceNotFoundException
      {
        return new TrustManager(ServiceManager.getServiceOrThrow("trust"));
      }
    });
    registerService("fingerprint", FingerprintManager.class, new CachedServiceFetcher()
    {
      public FingerprintManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        if (getApplicationInfotargetSdkVersion >= 26) {
          localObject = ServiceManager.getServiceOrThrow("fingerprint");
        } else {
          localObject = ServiceManager.getService("fingerprint");
        }
        Object localObject = IFingerprintService.Stub.asInterface((IBinder)localObject);
        return new FingerprintManager(paramAnonymousContextImpl.getOuterContext(), (IFingerprintService)localObject);
      }
    });
    registerService("tv_input", TvInputManager.class, new CachedServiceFetcher()
    {
      public TvInputManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new TvInputManager(ITvInputManager.Stub.asInterface(ServiceManager.getServiceOrThrow("tv_input")), paramAnonymousContextImpl.getUserId());
      }
    });
    registerService("network_score", NetworkScoreManager.class, new CachedServiceFetcher()
    {
      public NetworkScoreManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new NetworkScoreManager(paramAnonymousContextImpl);
      }
    });
    registerService("usagestats", UsageStatsManager.class, new CachedServiceFetcher()
    {
      public UsageStatsManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        IUsageStatsManager localIUsageStatsManager = IUsageStatsManager.Stub.asInterface(ServiceManager.getServiceOrThrow("usagestats"));
        return new UsageStatsManager(paramAnonymousContextImpl.getOuterContext(), localIUsageStatsManager);
      }
    });
    registerService("netstats", NetworkStatsManager.class, new CachedServiceFetcher()
    {
      public NetworkStatsManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new NetworkStatsManager(paramAnonymousContextImpl.getOuterContext());
      }
    });
    registerService("jobscheduler", JobScheduler.class, new StaticServiceFetcher()
    {
      public JobScheduler createService()
        throws ServiceManager.ServiceNotFoundException
      {
        return new JobSchedulerImpl(IJobScheduler.Stub.asInterface(ServiceManager.getServiceOrThrow("jobscheduler")));
      }
    });
    registerService("persistent_data_block", PersistentDataBlockManager.class, new StaticServiceFetcher()
    {
      public PersistentDataBlockManager createService()
        throws ServiceManager.ServiceNotFoundException
      {
        Object localObject = ServiceManager.getServiceOrThrow("persistent_data_block");
        localObject = IPersistentDataBlockService.Stub.asInterface((IBinder)localObject);
        if (localObject != null) {
          return new PersistentDataBlockManager((IPersistentDataBlockService)localObject);
        }
        return null;
      }
    });
    registerService("oem_lock", OemLockManager.class, new StaticServiceFetcher()
    {
      public OemLockManager createService()
        throws ServiceManager.ServiceNotFoundException
      {
        IOemLockService localIOemLockService = IOemLockService.Stub.asInterface(ServiceManager.getServiceOrThrow("oem_lock"));
        if (localIOemLockService != null) {
          return new OemLockManager(localIOemLockService);
        }
        return null;
      }
    });
    registerService("media_projection", MediaProjectionManager.class, new CachedServiceFetcher()
    {
      public MediaProjectionManager createService(ContextImpl paramAnonymousContextImpl)
      {
        return new MediaProjectionManager(paramAnonymousContextImpl);
      }
    });
    registerService("appwidget", AppWidgetManager.class, new CachedServiceFetcher()
    {
      public AppWidgetManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new AppWidgetManager(paramAnonymousContextImpl, IAppWidgetService.Stub.asInterface(ServiceManager.getServiceOrThrow("appwidget")));
      }
    });
    registerService("midi", MidiManager.class, new CachedServiceFetcher()
    {
      public MidiManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new MidiManager(IMidiManager.Stub.asInterface(ServiceManager.getServiceOrThrow("midi")));
      }
    });
    registerService("broadcastradio", RadioManager.class, new CachedServiceFetcher()
    {
      public RadioManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new RadioManager(paramAnonymousContextImpl);
      }
    });
    registerService("hardware_properties", HardwarePropertiesManager.class, new CachedServiceFetcher()
    {
      public HardwarePropertiesManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        IBinder localIBinder = ServiceManager.getServiceOrThrow("hardware_properties");
        return new HardwarePropertiesManager(paramAnonymousContextImpl, IHardwarePropertiesManager.Stub.asInterface(localIBinder));
      }
    });
    registerService("soundtrigger", SoundTriggerManager.class, new CachedServiceFetcher()
    {
      public SoundTriggerManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new SoundTriggerManager(paramAnonymousContextImpl, ISoundTriggerService.Stub.asInterface(ServiceManager.getServiceOrThrow("soundtrigger")));
      }
    });
    registerService("shortcut", ShortcutManager.class, new CachedServiceFetcher()
    {
      public ShortcutManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new ShortcutManager(paramAnonymousContextImpl, IShortcutService.Stub.asInterface(ServiceManager.getServiceOrThrow("shortcut")));
      }
    });
    registerService("network_watchlist", NetworkWatchlistManager.class, new CachedServiceFetcher()
    {
      public NetworkWatchlistManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new NetworkWatchlistManager(paramAnonymousContextImpl, INetworkWatchlistManager.Stub.asInterface(ServiceManager.getServiceOrThrow("network_watchlist")));
      }
    });
    registerService("systemhealth", SystemHealthManager.class, new CachedServiceFetcher()
    {
      public SystemHealthManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new SystemHealthManager(IBatteryStats.Stub.asInterface(ServiceManager.getServiceOrThrow("batterystats")));
      }
    });
    registerService("contexthub", ContextHubManager.class, new CachedServiceFetcher()
    {
      public ContextHubManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new ContextHubManager(paramAnonymousContextImpl.getOuterContext(), mMainThread.getHandler().getLooper());
      }
    });
    registerService("incident", IncidentManager.class, new CachedServiceFetcher()
    {
      public IncidentManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new IncidentManager(paramAnonymousContextImpl);
      }
    });
    registerService("autofill", AutofillManager.class, new CachedServiceFetcher()
    {
      public AutofillManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        IAutoFillManager localIAutoFillManager = IAutoFillManager.Stub.asInterface(ServiceManager.getService("autofill"));
        return new AutofillManager(paramAnonymousContextImpl.getOuterContext(), localIAutoFillManager);
      }
    });
    registerService("vrmanager", VrManager.class, new CachedServiceFetcher()
    {
      public VrManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new VrManager(IVrManager.Stub.asInterface(ServiceManager.getServiceOrThrow("vrmanager")));
      }
    });
    registerService("timezone", RulesManager.class, new CachedServiceFetcher()
    {
      public RulesManager createService(ContextImpl paramAnonymousContextImpl)
      {
        return new RulesManager(paramAnonymousContextImpl.getOuterContext());
      }
    });
    registerService("crossprofileapps", CrossProfileApps.class, new CachedServiceFetcher()
    {
      public CrossProfileApps createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        IBinder localIBinder = ServiceManager.getServiceOrThrow("crossprofileapps");
        return new CrossProfileApps(paramAnonymousContextImpl.getOuterContext(), ICrossProfileApps.Stub.asInterface(localIBinder));
      }
    });
    registerService("slice", SliceManager.class, new CachedServiceFetcher()
    {
      public SliceManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        return new SliceManager(paramAnonymousContextImpl.getOuterContext(), mMainThread.getHandler());
      }
    });
    registerService("deviceidle", DeviceIdleManager.class, new CachedServiceFetcher()
    {
      public DeviceIdleManager createService(ContextImpl paramAnonymousContextImpl)
        throws ServiceManager.ServiceNotFoundException
      {
        IDeviceIdleController localIDeviceIdleController = IDeviceIdleController.Stub.asInterface(ServiceManager.getServiceOrThrow("deviceidle"));
        return new DeviceIdleManager(paramAnonymousContextImpl.getOuterContext(), localIDeviceIdleController);
      }
    });
  }
  
  private SystemServiceRegistry() {}
  
  public static Object[] createServiceCache()
  {
    return new Object[sServiceCacheSize];
  }
  
  public static Object getSystemService(ContextImpl paramContextImpl, String paramString)
  {
    paramString = (ServiceFetcher)SYSTEM_SERVICE_FETCHERS.get(paramString);
    if (paramString != null) {
      paramContextImpl = paramString.getService(paramContextImpl);
    } else {
      paramContextImpl = null;
    }
    return paramContextImpl;
  }
  
  public static String getSystemServiceName(Class<?> paramClass)
  {
    return (String)SYSTEM_SERVICE_NAMES.get(paramClass);
  }
  
  public static void onServiceNotFound(ServiceManager.ServiceNotFoundException paramServiceNotFoundException)
  {
    if (Process.myUid() < 10000) {
      Log.wtf("SystemServiceRegistry", paramServiceNotFoundException.getMessage(), paramServiceNotFoundException);
    } else {
      Log.w("SystemServiceRegistry", paramServiceNotFoundException.getMessage());
    }
  }
  
  private static <T> void registerService(String paramString, Class<T> paramClass, ServiceFetcher<T> paramServiceFetcher)
  {
    SYSTEM_SERVICE_NAMES.put(paramClass, paramString);
    SYSTEM_SERVICE_FETCHERS.put(paramString, paramServiceFetcher);
  }
  
  static abstract class CachedServiceFetcher<T>
    implements SystemServiceRegistry.ServiceFetcher<T>
  {
    private final int mCacheIndex = SystemServiceRegistry.access$008();
    
    CachedServiceFetcher() {}
    
    public abstract T createService(ContextImpl paramContextImpl)
      throws ServiceManager.ServiceNotFoundException;
    
    /* Error */
    public final T getService(ContextImpl paramContextImpl)
    {
      // Byte code:
      //   0: aload_1
      //   1: getfield 36	android/app/ContextImpl:mServiceCache	[Ljava/lang/Object;
      //   4: astore_2
      //   5: aload_1
      //   6: getfield 40	android/app/ContextImpl:mServiceInitializationStateArray	[I
      //   9: astore_3
      //   10: iconst_0
      //   11: istore 4
      //   13: aload_2
      //   14: monitorenter
      //   15: aload_2
      //   16: aload_0
      //   17: getfield 22	android/app/SystemServiceRegistry$CachedServiceFetcher:mCacheIndex	I
      //   20: aaload
      //   21: astore 5
      //   23: aload 5
      //   25: ifnonnull +220 -> 245
      //   28: aload_3
      //   29: aload_0
      //   30: getfield 22	android/app/SystemServiceRegistry$CachedServiceFetcher:mCacheIndex	I
      //   33: iaload
      //   34: iconst_3
      //   35: if_icmpne +6 -> 41
      //   38: goto +207 -> 245
      //   41: aload_3
      //   42: aload_0
      //   43: getfield 22	android/app/SystemServiceRegistry$CachedServiceFetcher:mCacheIndex	I
      //   46: iaload
      //   47: iconst_2
      //   48: if_icmpne +10 -> 58
      //   51: aload_3
      //   52: aload_0
      //   53: getfield 22	android/app/SystemServiceRegistry$CachedServiceFetcher:mCacheIndex	I
      //   56: iconst_0
      //   57: iastore
      //   58: aload_3
      //   59: aload_0
      //   60: getfield 22	android/app/SystemServiceRegistry$CachedServiceFetcher:mCacheIndex	I
      //   63: iaload
      //   64: ifne +13 -> 77
      //   67: iconst_1
      //   68: istore 4
      //   70: aload_3
      //   71: aload_0
      //   72: getfield 22	android/app/SystemServiceRegistry$CachedServiceFetcher:mCacheIndex	I
      //   75: iconst_1
      //   76: iastore
      //   77: aload_2
      //   78: monitorexit
      //   79: iload 4
      //   81: ifeq +112 -> 193
      //   84: aconst_null
      //   85: astore 5
      //   87: aload_0
      //   88: aload_1
      //   89: invokevirtual 42	android/app/SystemServiceRegistry$CachedServiceFetcher:createService	(Landroid/app/ContextImpl;)Ljava/lang/Object;
      //   92: astore_1
      //   93: aload_2
      //   94: monitorenter
      //   95: aload_2
      //   96: aload_0
      //   97: getfield 22	android/app/SystemServiceRegistry$CachedServiceFetcher:mCacheIndex	I
      //   100: aload_1
      //   101: aastore
      //   102: aload_3
      //   103: aload_0
      //   104: getfield 22	android/app/SystemServiceRegistry$CachedServiceFetcher:mCacheIndex	I
      //   107: iconst_2
      //   108: iastore
      //   109: aload_2
      //   110: invokevirtual 45	java/lang/Object:notifyAll	()V
      //   113: aload_2
      //   114: monitorexit
      //   115: goto +42 -> 157
      //   118: astore_1
      //   119: aload_2
      //   120: monitorexit
      //   121: aload_1
      //   122: athrow
      //   123: astore_1
      //   124: goto +40 -> 164
      //   127: astore_1
      //   128: aload_1
      //   129: invokestatic 49	android/app/SystemServiceRegistry:onServiceNotFound	(Landroid/os/ServiceManager$ServiceNotFoundException;)V
      //   132: aload_2
      //   133: monitorenter
      //   134: aload_2
      //   135: aload_0
      //   136: getfield 22	android/app/SystemServiceRegistry$CachedServiceFetcher:mCacheIndex	I
      //   139: aconst_null
      //   140: aastore
      //   141: aload_3
      //   142: aload_0
      //   143: getfield 22	android/app/SystemServiceRegistry$CachedServiceFetcher:mCacheIndex	I
      //   146: iconst_3
      //   147: iastore
      //   148: aload_2
      //   149: invokevirtual 45	java/lang/Object:notifyAll	()V
      //   152: aload_2
      //   153: monitorexit
      //   154: aload 5
      //   156: astore_1
      //   157: aload_1
      //   158: areturn
      //   159: astore_1
      //   160: aload_2
      //   161: monitorexit
      //   162: aload_1
      //   163: athrow
      //   164: aload_2
      //   165: monitorenter
      //   166: aload_2
      //   167: aload_0
      //   168: getfield 22	android/app/SystemServiceRegistry$CachedServiceFetcher:mCacheIndex	I
      //   171: aconst_null
      //   172: aastore
      //   173: aload_3
      //   174: aload_0
      //   175: getfield 22	android/app/SystemServiceRegistry$CachedServiceFetcher:mCacheIndex	I
      //   178: iconst_3
      //   179: iastore
      //   180: aload_2
      //   181: invokevirtual 45	java/lang/Object:notifyAll	()V
      //   184: aload_2
      //   185: monitorexit
      //   186: aload_1
      //   187: athrow
      //   188: astore_1
      //   189: aload_2
      //   190: monitorexit
      //   191: aload_1
      //   192: athrow
      //   193: aload_2
      //   194: monitorenter
      //   195: aload_3
      //   196: aload_0
      //   197: getfield 22	android/app/SystemServiceRegistry$CachedServiceFetcher:mCacheIndex	I
      //   200: iaload
      //   201: istore 4
      //   203: iload 4
      //   205: iconst_2
      //   206: if_icmpge +29 -> 235
      //   209: aload_2
      //   210: invokevirtual 52	java/lang/Object:wait	()V
      //   213: goto -18 -> 195
      //   216: astore_1
      //   217: ldc 54
      //   219: ldc 56
      //   221: invokestatic 62	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
      //   224: pop
      //   225: invokestatic 68	java/lang/Thread:currentThread	()Ljava/lang/Thread;
      //   228: invokevirtual 71	java/lang/Thread:interrupt	()V
      //   231: aload_2
      //   232: monitorexit
      //   233: aconst_null
      //   234: areturn
      //   235: aload_2
      //   236: monitorexit
      //   237: goto -227 -> 10
      //   240: astore_1
      //   241: aload_2
      //   242: monitorexit
      //   243: aload_1
      //   244: athrow
      //   245: aload_2
      //   246: monitorexit
      //   247: aload 5
      //   249: areturn
      //   250: astore_1
      //   251: aload_2
      //   252: monitorexit
      //   253: aload_1
      //   254: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	255	0	this	CachedServiceFetcher
      //   0	255	1	paramContextImpl	ContextImpl
      //   4	248	2	arrayOfObject	Object[]
      //   9	187	3	arrayOfInt	int[]
      //   11	196	4	i	int
      //   21	227	5	localObject	Object
      // Exception table:
      //   from	to	target	type
      //   95	115	118	finally
      //   119	121	118	finally
      //   87	93	123	finally
      //   128	132	123	finally
      //   87	93	127	android/os/ServiceManager$ServiceNotFoundException
      //   134	154	159	finally
      //   160	162	159	finally
      //   166	186	188	finally
      //   189	191	188	finally
      //   209	213	216	java/lang/InterruptedException
      //   195	203	240	finally
      //   209	213	240	finally
      //   217	233	240	finally
      //   235	237	240	finally
      //   241	243	240	finally
      //   15	23	250	finally
      //   28	38	250	finally
      //   41	58	250	finally
      //   58	67	250	finally
      //   70	77	250	finally
      //   77	79	250	finally
      //   245	247	250	finally
      //   251	253	250	finally
    }
  }
  
  static abstract interface ServiceFetcher<T>
  {
    public abstract T getService(ContextImpl paramContextImpl);
  }
  
  static abstract class StaticApplicationContextServiceFetcher<T>
    implements SystemServiceRegistry.ServiceFetcher<T>
  {
    private T mCachedInstance;
    
    StaticApplicationContextServiceFetcher() {}
    
    public abstract T createService(Context paramContext)
      throws ServiceManager.ServiceNotFoundException;
    
    public final T getService(ContextImpl paramContextImpl)
    {
      try
      {
        if (mCachedInstance == null)
        {
          Context localContext = paramContextImpl.getApplicationContext();
          if (localContext != null) {
            paramContextImpl = localContext;
          }
          try
          {
            mCachedInstance = createService(paramContextImpl);
          }
          catch (ServiceManager.ServiceNotFoundException paramContextImpl)
          {
            SystemServiceRegistry.onServiceNotFound(paramContextImpl);
          }
        }
        paramContextImpl = mCachedInstance;
        return paramContextImpl;
      }
      finally {}
    }
  }
  
  static abstract class StaticServiceFetcher<T>
    implements SystemServiceRegistry.ServiceFetcher<T>
  {
    private T mCachedInstance;
    
    StaticServiceFetcher() {}
    
    public abstract T createService()
      throws ServiceManager.ServiceNotFoundException;
    
    public final T getService(ContextImpl paramContextImpl)
    {
      try
      {
        paramContextImpl = mCachedInstance;
        if (paramContextImpl == null) {
          try
          {
            mCachedInstance = createService();
          }
          catch (ServiceManager.ServiceNotFoundException paramContextImpl)
          {
            SystemServiceRegistry.onServiceNotFound(paramContextImpl);
          }
        }
        paramContextImpl = mCachedInstance;
        return paramContextImpl;
      }
      finally {}
    }
  }
}
