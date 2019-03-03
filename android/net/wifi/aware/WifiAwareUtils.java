package android.net.wifi.aware;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class WifiAwareUtils
{
  public WifiAwareUtils() {}
  
  public static boolean isLegacyVersion(Context paramContext, int paramInt)
  {
    try
    {
      int i = getPackageManagergetApplicationInfogetOpPackageName0targetSdkVersion;
      if (i < paramInt) {
        return true;
      }
    }
    catch (PackageManager.NameNotFoundException paramContext) {}
    return false;
  }
  
  public static boolean validatePassphrase(String paramString)
  {
    return (paramString != null) && (paramString.length() >= 8) && (paramString.length() <= 63);
  }
  
  public static boolean validatePmk(byte[] paramArrayOfByte)
  {
    return (paramArrayOfByte != null) && (paramArrayOfByte.length == 32);
  }
  
  public static void validateServiceName(byte[] paramArrayOfByte)
    throws IllegalArgumentException
  {
    if (paramArrayOfByte != null)
    {
      if ((paramArrayOfByte.length >= 1) && (paramArrayOfByte.length <= 255))
      {
        for (int i = 0; i < paramArrayOfByte.length; i++)
        {
          int j = paramArrayOfByte[i];
          if (((j & 0x80) == 0) && ((j < 48) || (j > 57)) && ((j < 97) || (j > 122)) && ((j < 65) || (j > 90)) && (j != 45) && (j != 46)) {
            throw new IllegalArgumentException("Invalid service name - illegal characters, allowed = (0-9, a-z,A-Z, -, .)");
          }
        }
        return;
      }
      throw new IllegalArgumentException("Invalid service name length - must be between 1 and 255 bytes (UTF-8 encoding)");
    }
    throw new IllegalArgumentException("Invalid service name - null");
  }
}
