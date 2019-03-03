package com.android.internal.os;

import android.os.SystemProperties;

public class RoSystemProperties
{
  public static final boolean CONFIG_AVOID_GFX_ACCEL;
  public static final boolean CONFIG_LOW_RAM;
  public static final boolean CONFIG_SMALL_BATTERY;
  public static final String CONTROL_PRIVAPP_PERMISSIONS;
  public static final boolean CONTROL_PRIVAPP_PERMISSIONS_DISABLE;
  public static final boolean CONTROL_PRIVAPP_PERMISSIONS_ENFORCE;
  public static final boolean CONTROL_PRIVAPP_PERMISSIONS_LOG;
  public static final boolean CRYPTO_BLOCK_ENCRYPTED;
  public static final boolean CRYPTO_ENCRYPTABLE;
  public static final boolean CRYPTO_ENCRYPTED;
  public static final boolean CRYPTO_FILE_ENCRYPTED;
  public static final String CRYPTO_STATE;
  public static final String CRYPTO_TYPE;
  public static final boolean DEBUGGABLE;
  public static final int FACTORYTEST;
  public static final boolean FW_SYSTEM_USER_SPLIT;
  
  static
  {
    boolean bool1 = false;
    if (SystemProperties.getInt("ro.debuggable", 0) == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    DEBUGGABLE = bool2;
    FACTORYTEST = SystemProperties.getInt("ro.factorytest", 0);
    CONTROL_PRIVAPP_PERMISSIONS = SystemProperties.get("ro.control_privapp_permissions");
    CONFIG_AVOID_GFX_ACCEL = SystemProperties.getBoolean("ro.config.avoid_gfx_accel", false);
    CONFIG_LOW_RAM = SystemProperties.getBoolean("ro.config.low_ram", false);
    CONFIG_SMALL_BATTERY = SystemProperties.getBoolean("ro.config.small_battery", false);
    FW_SYSTEM_USER_SPLIT = SystemProperties.getBoolean("ro.fw.system_user_split", false);
    CRYPTO_STATE = SystemProperties.get("ro.crypto.state");
    CRYPTO_TYPE = SystemProperties.get("ro.crypto.type");
    if ((!CRYPTO_STATE.isEmpty()) && (!"unsupported".equals(CRYPTO_STATE))) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    CRYPTO_ENCRYPTABLE = bool2;
    CRYPTO_ENCRYPTED = "encrypted".equalsIgnoreCase(CRYPTO_STATE);
    CRYPTO_FILE_ENCRYPTED = "file".equalsIgnoreCase(CRYPTO_TYPE);
    CRYPTO_BLOCK_ENCRYPTED = "block".equalsIgnoreCase(CRYPTO_TYPE);
    CONTROL_PRIVAPP_PERMISSIONS_LOG = "log".equalsIgnoreCase(CONTROL_PRIVAPP_PERMISSIONS);
    CONTROL_PRIVAPP_PERMISSIONS_ENFORCE = "enforce".equalsIgnoreCase(CONTROL_PRIVAPP_PERMISSIONS);
    boolean bool2 = bool1;
    if (!CONTROL_PRIVAPP_PERMISSIONS_LOG)
    {
      bool2 = bool1;
      if (!CONTROL_PRIVAPP_PERMISSIONS_ENFORCE) {
        bool2 = true;
      }
    }
    CONTROL_PRIVAPP_PERMISSIONS_DISABLE = bool2;
  }
  
  public RoSystemProperties() {}
}
