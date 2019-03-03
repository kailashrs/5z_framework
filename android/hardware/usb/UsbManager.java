package android.hardware.usb;

import android.annotation.SystemApi;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.util.Preconditions;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

public class UsbManager
{
  public static final String ACTION_USB_ACCESSORY_ATTACHED = "android.hardware.usb.action.USB_ACCESSORY_ATTACHED";
  public static final String ACTION_USB_ACCESSORY_DETACHED = "android.hardware.usb.action.USB_ACCESSORY_DETACHED";
  public static final String ACTION_USB_DEVICE_ATTACHED = "android.hardware.usb.action.USB_DEVICE_ATTACHED";
  public static final String ACTION_USB_DEVICE_DETACHED = "android.hardware.usb.action.USB_DEVICE_DETACHED";
  public static final String ACTION_USB_PORT_CHANGED = "android.hardware.usb.action.USB_PORT_CHANGED";
  public static final String ACTION_USB_STATE = "android.hardware.usb.action.USB_STATE";
  public static final String EXTRA_ACCESSORY = "accessory";
  public static final String EXTRA_DEVICE = "device";
  public static final String EXTRA_PERMISSION_GRANTED = "permission";
  public static final String EXTRA_PORT = "port";
  public static final String EXTRA_PORT_STATUS = "portStatus";
  public static final long FUNCTION_ACCESSORY = 2L;
  public static final long FUNCTION_ADB = 1L;
  public static final long FUNCTION_AUDIO_SOURCE = 64L;
  public static final long FUNCTION_MIDI = 8L;
  public static final long FUNCTION_MTP = 4L;
  private static final Map<String, Long> FUNCTION_NAME_TO_CODE = new HashMap();
  public static final long FUNCTION_NONE = 0L;
  public static final long FUNCTION_PTP = 16L;
  public static final long FUNCTION_RNDIS = 32L;
  private static final long SETTABLE_FUNCTIONS = 60L;
  private static final String TAG = "UsbManager";
  public static final String USB_CONFIGURED = "configured";
  public static final String USB_CONNECTED = "connected";
  public static final String USB_DATA_UNLOCKED = "unlocked";
  public static final String USB_FUNCTION_ACCESSORY = "accessory";
  public static final String USB_FUNCTION_ADB = "adb";
  public static final String USB_FUNCTION_AUDIO_SOURCE = "audio_source";
  public static final String USB_FUNCTION_MIDI = "midi";
  public static final String USB_FUNCTION_MTP = "mtp";
  public static final String USB_FUNCTION_NONE = "none";
  public static final String USB_FUNCTION_PTP = "ptp";
  public static final String USB_FUNCTION_RNDIS = "rndis";
  public static final String USB_HOST_CONNECTED = "host_connected";
  private final Context mContext;
  private final IUsbManager mService;
  
  static
  {
    FUNCTION_NAME_TO_CODE.put("mtp", Long.valueOf(4L));
    FUNCTION_NAME_TO_CODE.put("ptp", Long.valueOf(16L));
    FUNCTION_NAME_TO_CODE.put("rndis", Long.valueOf(32L));
    FUNCTION_NAME_TO_CODE.put("midi", Long.valueOf(8L));
    FUNCTION_NAME_TO_CODE.put("accessory", Long.valueOf(2L));
    FUNCTION_NAME_TO_CODE.put("audio_source", Long.valueOf(64L));
    FUNCTION_NAME_TO_CODE.put("adb", Long.valueOf(1L));
  }
  
  public UsbManager(Context paramContext, IUsbManager paramIUsbManager)
  {
    mContext = paramContext;
    mService = paramIUsbManager;
  }
  
  public static boolean areSettableFunctions(long paramLong)
  {
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (paramLong != 0L) {
      if (((0xFFFFFFFFFFFFFFC3 & paramLong) == 0L) && (Long.bitCount(paramLong) == 1)) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
    }
    return bool2;
  }
  
  public static long usbFunctionsFromString(String paramString)
  {
    if ((paramString != null) && (!paramString.equals("none")))
    {
      long l = 0L;
      Object localObject1 = paramString.split(",");
      int i = localObject1.length;
      int j = 0;
      while (j < i)
      {
        Object localObject2 = localObject1[j];
        if (FUNCTION_NAME_TO_CODE.containsKey(localObject2)) {
          l |= ((Long)FUNCTION_NAME_TO_CODE.get(localObject2)).longValue();
        } else {
          if (localObject2.length() > 0) {
            break label94;
          }
        }
        j++;
        continue;
        label94:
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("Invalid usb function ");
        ((StringBuilder)localObject1).append(paramString);
        throw new IllegalArgumentException(((StringBuilder)localObject1).toString());
      }
      return l;
    }
    return 0L;
  }
  
  public static String usbFunctionsToString(long paramLong)
  {
    StringJoiner localStringJoiner = new StringJoiner(",");
    if ((0x4 & paramLong) != 0L) {
      localStringJoiner.add("mtp");
    }
    if ((0x10 & paramLong) != 0L) {
      localStringJoiner.add("ptp");
    }
    if ((0x20 & paramLong) != 0L) {
      localStringJoiner.add("rndis");
    }
    if ((0x8 & paramLong) != 0L) {
      localStringJoiner.add("midi");
    }
    if ((0x2 & paramLong) != 0L) {
      localStringJoiner.add("accessory");
    }
    if ((0x40 & paramLong) != 0L) {
      localStringJoiner.add("audio_source");
    }
    if ((1L & paramLong) != 0L) {
      localStringJoiner.add("adb");
    }
    return localStringJoiner.toString();
  }
  
  public UsbAccessory[] getAccessoryList()
  {
    if (mService == null) {
      return null;
    }
    try
    {
      UsbAccessory localUsbAccessory = mService.getCurrentAccessory();
      if (localUsbAccessory == null) {
        return null;
      }
      return new UsbAccessory[] { localUsbAccessory };
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public ParcelFileDescriptor getControlFd(long paramLong)
  {
    try
    {
      ParcelFileDescriptor localParcelFileDescriptor = mService.getControlFd(paramLong);
      return localParcelFileDescriptor;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public long getCurrentFunctions()
  {
    try
    {
      long l = mService.getCurrentFunctions();
      return l;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public HashMap<String, UsbDevice> getDeviceList()
  {
    HashMap localHashMap = new HashMap();
    if (mService == null) {
      return localHashMap;
    }
    Bundle localBundle = new Bundle();
    try
    {
      mService.getDeviceList(localBundle);
      Iterator localIterator = localBundle.keySet().iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        localHashMap.put(str, (UsbDevice)localBundle.get(str));
      }
      return localHashMap;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public UsbPortStatus getPortStatus(UsbPort paramUsbPort)
  {
    Preconditions.checkNotNull(paramUsbPort, "port must not be null");
    try
    {
      paramUsbPort = mService.getPortStatus(paramUsbPort.getId());
      return paramUsbPort;
    }
    catch (RemoteException paramUsbPort)
    {
      throw paramUsbPort.rethrowFromSystemServer();
    }
  }
  
  public UsbPort[] getPorts()
  {
    if (mService == null) {
      return null;
    }
    try
    {
      UsbPort[] arrayOfUsbPort = mService.getPorts();
      return arrayOfUsbPort;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public long getScreenUnlockedFunctions()
  {
    try
    {
      long l = mService.getScreenUnlockedFunctions();
      return l;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void grantPermission(UsbDevice paramUsbDevice)
  {
    grantPermission(paramUsbDevice, Process.myUid());
  }
  
  public void grantPermission(UsbDevice paramUsbDevice, int paramInt)
  {
    try
    {
      mService.grantDevicePermission(paramUsbDevice, paramInt);
      return;
    }
    catch (RemoteException paramUsbDevice)
    {
      throw paramUsbDevice.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void grantPermission(UsbDevice paramUsbDevice, String paramString)
  {
    try
    {
      grantPermission(paramUsbDevice, mContext.getPackageManager().getPackageUidAsUser(paramString, mContext.getUserId()));
    }
    catch (PackageManager.NameNotFoundException paramUsbDevice)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Package ");
      localStringBuilder.append(paramString);
      localStringBuilder.append(" not found.");
      Log.e("UsbManager", localStringBuilder.toString(), paramUsbDevice);
    }
  }
  
  public boolean hasPermission(UsbAccessory paramUsbAccessory)
  {
    if (mService == null) {
      return false;
    }
    try
    {
      boolean bool = mService.hasAccessoryPermission(paramUsbAccessory);
      return bool;
    }
    catch (RemoteException paramUsbAccessory)
    {
      throw paramUsbAccessory.rethrowFromSystemServer();
    }
  }
  
  public boolean hasPermission(UsbDevice paramUsbDevice)
  {
    if (mService == null) {
      return false;
    }
    try
    {
      boolean bool = mService.hasDevicePermission(paramUsbDevice, mContext.getPackageName());
      return bool;
    }
    catch (RemoteException paramUsbDevice)
    {
      throw paramUsbDevice.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public boolean isFunctionEnabled(String paramString)
  {
    try
    {
      boolean bool = mService.isFunctionEnabled(paramString);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public ParcelFileDescriptor openAccessory(UsbAccessory paramUsbAccessory)
  {
    try
    {
      paramUsbAccessory = mService.openAccessory(paramUsbAccessory);
      return paramUsbAccessory;
    }
    catch (RemoteException paramUsbAccessory)
    {
      throw paramUsbAccessory.rethrowFromSystemServer();
    }
  }
  
  public UsbDeviceConnection openDevice(UsbDevice paramUsbDevice)
  {
    try
    {
      String str = paramUsbDevice.getDeviceName();
      ParcelFileDescriptor localParcelFileDescriptor = mService.openDevice(str, mContext.getPackageName());
      if (localParcelFileDescriptor != null)
      {
        UsbDeviceConnection localUsbDeviceConnection = new android/hardware/usb/UsbDeviceConnection;
        localUsbDeviceConnection.<init>(paramUsbDevice);
        boolean bool = localUsbDeviceConnection.open(str, localParcelFileDescriptor, mContext);
        localParcelFileDescriptor.close();
        if (bool) {
          return localUsbDeviceConnection;
        }
      }
    }
    catch (Exception paramUsbDevice)
    {
      Log.e("UsbManager", "exception in UsbManager.openDevice", paramUsbDevice);
    }
    return null;
  }
  
  public void requestPermission(UsbAccessory paramUsbAccessory, PendingIntent paramPendingIntent)
  {
    try
    {
      mService.requestAccessoryPermission(paramUsbAccessory, mContext.getPackageName(), paramPendingIntent);
      return;
    }
    catch (RemoteException paramUsbAccessory)
    {
      throw paramUsbAccessory.rethrowFromSystemServer();
    }
  }
  
  public void requestPermission(UsbDevice paramUsbDevice, PendingIntent paramPendingIntent)
  {
    try
    {
      mService.requestDevicePermission(paramUsbDevice, mContext.getPackageName(), paramPendingIntent);
      return;
    }
    catch (RemoteException paramUsbDevice)
    {
      throw paramUsbDevice.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public void setCurrentFunction(String paramString, boolean paramBoolean)
  {
    try
    {
      mService.setCurrentFunction(paramString, paramBoolean);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void setCurrentFunctions(long paramLong)
  {
    try
    {
      mService.setCurrentFunctions(paramLong);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void setPortRoles(UsbPort paramUsbPort, int paramInt1, int paramInt2)
  {
    Preconditions.checkNotNull(paramUsbPort, "port must not be null");
    UsbPort.checkRoles(paramInt1, paramInt2);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("setPortRoles Package:");
    localStringBuilder.append(mContext.getPackageName());
    Log.d("UsbManager", localStringBuilder.toString());
    try
    {
      mService.setPortRoles(paramUsbPort.getId(), paramInt1, paramInt2);
      return;
    }
    catch (RemoteException paramUsbPort)
    {
      throw paramUsbPort.rethrowFromSystemServer();
    }
  }
  
  public void setScreenUnlockedFunctions(long paramLong)
  {
    try
    {
      mService.setScreenUnlockedFunctions(paramLong);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void setUsbDeviceConnectionHandler(ComponentName paramComponentName)
  {
    try
    {
      mService.setUsbDeviceConnectionHandler(paramComponentName);
      return;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
}
