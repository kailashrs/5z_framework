package android.companion;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanFilter;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BluetoothDeviceFilterUtils
{
  private static final boolean DEBUG = false;
  private static final String LOG_TAG = "BluetoothDeviceFilterUtils";
  
  private BluetoothDeviceFilterUtils() {}
  
  private static void debugLogMatchResult(boolean paramBoolean, BluetoothDevice paramBluetoothDevice, Object paramObject)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(getDeviceDisplayNameInternal(paramBluetoothDevice));
    if (paramBoolean) {
      paramBluetoothDevice = " ~ ";
    } else {
      paramBluetoothDevice = " !~ ";
    }
    localStringBuilder.append(paramBluetoothDevice);
    localStringBuilder.append(paramObject);
    Log.i("BluetoothDeviceFilterUtils", localStringBuilder.toString());
  }
  
  private static void debugLogMatchResult(boolean paramBoolean, android.net.wifi.ScanResult paramScanResult, Object paramObject)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(getDeviceDisplayNameInternal(paramScanResult));
    if (paramBoolean) {
      paramScanResult = " ~ ";
    } else {
      paramScanResult = " !~ ";
    }
    localStringBuilder.append(paramScanResult);
    localStringBuilder.append(paramObject);
    Log.i("BluetoothDeviceFilterUtils", localStringBuilder.toString());
  }
  
  public static String getDeviceDisplayNameInternal(BluetoothDevice paramBluetoothDevice)
  {
    return TextUtils.firstNotEmpty(paramBluetoothDevice.getAliasName(), paramBluetoothDevice.getAddress());
  }
  
  public static String getDeviceDisplayNameInternal(android.net.wifi.ScanResult paramScanResult)
  {
    return TextUtils.firstNotEmpty(SSID, BSSID);
  }
  
  public static String getDeviceMacAddress(Parcelable paramParcelable)
  {
    if ((paramParcelable instanceof BluetoothDevice)) {
      return ((BluetoothDevice)paramParcelable).getAddress();
    }
    if ((paramParcelable instanceof android.net.wifi.ScanResult)) {
      return BSSID;
    }
    if ((paramParcelable instanceof android.bluetooth.le.ScanResult)) {
      return getDeviceMacAddress(((android.bluetooth.le.ScanResult)paramParcelable).getDevice());
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unknown device type: ");
    localStringBuilder.append(paramParcelable);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  static boolean matches(ScanFilter paramScanFilter, BluetoothDevice paramBluetoothDevice)
  {
    boolean bool;
    if ((matchesAddress(paramScanFilter.getDeviceAddress(), paramBluetoothDevice)) && (matchesServiceUuid(paramScanFilter.getServiceUuid(), paramScanFilter.getServiceUuidMask(), paramBluetoothDevice))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  static boolean matchesAddress(String paramString, BluetoothDevice paramBluetoothDevice)
  {
    boolean bool;
    if ((paramString != null) && ((paramBluetoothDevice == null) || (!paramString.equals(paramBluetoothDevice.getAddress())))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  static boolean matchesName(Pattern paramPattern, BluetoothDevice paramBluetoothDevice)
  {
    boolean bool;
    if (paramPattern == null) {
      bool = true;
    }
    for (;;)
    {
      break;
      if (paramBluetoothDevice == null)
      {
        bool = false;
      }
      else
      {
        paramBluetoothDevice = paramBluetoothDevice.getName();
        if ((paramBluetoothDevice != null) && (paramPattern.matcher(paramBluetoothDevice).find())) {
          bool = true;
        } else {
          bool = false;
        }
      }
    }
    return bool;
  }
  
  static boolean matchesName(Pattern paramPattern, android.net.wifi.ScanResult paramScanResult)
  {
    boolean bool;
    if (paramPattern == null) {
      bool = true;
    }
    for (;;)
    {
      break;
      if (paramScanResult == null)
      {
        bool = false;
      }
      else
      {
        paramScanResult = SSID;
        if ((paramScanResult != null) && (paramPattern.matcher(paramScanResult).find())) {
          bool = true;
        } else {
          bool = false;
        }
      }
    }
    return bool;
  }
  
  static boolean matchesServiceUuid(ParcelUuid paramParcelUuid1, ParcelUuid paramParcelUuid2, BluetoothDevice paramBluetoothDevice)
  {
    boolean bool;
    if ((paramParcelUuid1 != null) && (!ScanFilter.matchesServiceUuids(paramParcelUuid1, paramParcelUuid2, Arrays.asList(paramBluetoothDevice.getUuids())))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  static boolean matchesServiceUuids(List<ParcelUuid> paramList1, List<ParcelUuid> paramList2, BluetoothDevice paramBluetoothDevice)
  {
    for (int i = 0; i < paramList1.size(); i++) {
      if (!matchesServiceUuid((ParcelUuid)paramList1.get(i), (ParcelUuid)paramList2.get(i), paramBluetoothDevice)) {
        return false;
      }
    }
    return true;
  }
  
  static Pattern patternFromString(String paramString)
  {
    if (paramString == null) {
      paramString = null;
    } else {
      paramString = Pattern.compile(paramString);
    }
    return paramString;
  }
  
  static String patternToString(Pattern paramPattern)
  {
    if (paramPattern == null) {
      paramPattern = null;
    } else {
      paramPattern = paramPattern.pattern();
    }
    return paramPattern;
  }
}
