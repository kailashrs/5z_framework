package com.android.internal.util.dump;

import android.content.ComponentName;

public class DumpUtils
{
  public DumpUtils() {}
  
  public static void writeComponentName(DualDumpOutputStream paramDualDumpOutputStream, String paramString, long paramLong, ComponentName paramComponentName)
  {
    paramLong = paramDualDumpOutputStream.start(paramString, paramLong);
    paramDualDumpOutputStream.write("package_name", 1138166333441L, paramComponentName.getPackageName());
    paramDualDumpOutputStream.write("class_name", 1138166333442L, paramComponentName.getClassName());
    paramDualDumpOutputStream.end(paramLong);
  }
  
  public static void writeStringIfNotNull(DualDumpOutputStream paramDualDumpOutputStream, String paramString1, long paramLong, String paramString2)
  {
    if (paramString2 != null) {
      paramDualDumpOutputStream.write(paramString1, paramLong, paramString2);
    }
  }
}
