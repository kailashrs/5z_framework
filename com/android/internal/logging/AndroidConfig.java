package com.android.internal.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AndroidConfig
{
  public AndroidConfig()
  {
    try
    {
      Logger localLogger = Logger.getLogger("");
      AndroidHandler localAndroidHandler = new com/android/internal/logging/AndroidHandler;
      localAndroidHandler.<init>();
      localLogger.addHandler(localAndroidHandler);
      localLogger.setLevel(Level.INFO);
      Logger.getLogger("org.apache").setLevel(Level.WARNING);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
}
