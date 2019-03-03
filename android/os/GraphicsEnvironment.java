package android.os;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.provider.Settings.Global;
import android.util.Log;
import dalvik.system.VMRuntime;
import java.io.File;

public class GraphicsEnvironment
{
  private static final boolean DEBUG = false;
  private static final String PROPERTY_GFX_DRIVER = "ro.gfx.driver.0";
  private static final String TAG = "GraphicsEnvironment";
  private static final GraphicsEnvironment sInstance = new GraphicsEnvironment();
  private ClassLoader mClassLoader;
  private String mDebugLayerPath;
  private String mLayerPath;
  
  public GraphicsEnvironment() {}
  
  private static String chooseAbi(ApplicationInfo paramApplicationInfo)
  {
    String str = VMRuntime.getCurrentInstructionSet();
    if ((primaryCpuAbi != null) && (str.equals(VMRuntime.getInstructionSet(primaryCpuAbi)))) {
      return primaryCpuAbi;
    }
    if ((secondaryCpuAbi != null) && (str.equals(VMRuntime.getInstructionSet(secondaryCpuAbi)))) {
      return secondaryCpuAbi;
    }
    return null;
  }
  
  private static void chooseDriver(Context paramContext)
  {
    String str = SystemProperties.get("ro.gfx.driver.0");
    if ((str != null) && (!str.isEmpty()))
    {
      Object localObject = paramContext.getApplicationInfo();
      if ((!((ApplicationInfo)localObject).isPrivilegedApp()) && ((!((ApplicationInfo)localObject).isSystemApp()) || (((ApplicationInfo)localObject).isUpdatedSystemApp()))) {
        try
        {
          paramContext = paramContext.getPackageManager().getApplicationInfo(str, 1048576);
          str = chooseAbi(paramContext);
          if (str == null) {
            return;
          }
          if (targetSdkVersion < 26)
          {
            Log.w("GraphicsEnvironment", "updated driver package is not known to be compatible with O");
            return;
          }
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append(nativeLibraryDir);
          ((StringBuilder)localObject).append(File.pathSeparator);
          ((StringBuilder)localObject).append(sourceDir);
          ((StringBuilder)localObject).append("!/lib/");
          ((StringBuilder)localObject).append(str);
          setDriverPath(((StringBuilder)localObject).toString());
          return;
        }
        catch (PackageManager.NameNotFoundException paramContext)
        {
          paramContext = new StringBuilder();
          paramContext.append("driver package '");
          paramContext.append(str);
          paramContext.append("' not installed");
          Log.w("GraphicsEnvironment", paramContext.toString());
          return;
        }
      }
      return;
    }
  }
  
  public static void earlyInitEGL()
  {
    new Thread(_..Lambda.GraphicsEnvironment.U4RqBlx5_Js31_71IFOgvpvoAFg.INSTANCE, "EGL Init").start();
  }
  
  public static GraphicsEnvironment getInstance()
  {
    return sInstance;
  }
  
  private static boolean isDebuggable(Context paramContext)
  {
    boolean bool;
    if ((getApplicationInfoflags & 0x2) > 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static native void setDebugLayers(String paramString);
  
  private static native void setDriverPath(String paramString);
  
  private static native void setLayerPaths(ClassLoader paramClassLoader, String paramString);
  
  private void setupGpuLayers(Context paramContext)
  {
    String str1 = "";
    Object localObject = str1;
    if (isDebuggable(paramContext))
    {
      localObject = str1;
      if (Settings.Global.getInt(paramContext.getContentResolver(), "enable_gpu_debug_layers", 0) != 0)
      {
        String str2 = Settings.Global.getString(paramContext.getContentResolver(), "gpu_debug_app");
        String str3 = paramContext.getPackageName();
        localObject = str1;
        if (str2 != null)
        {
          localObject = str1;
          if (str3 != null)
          {
            localObject = str1;
            if (!str2.isEmpty())
            {
              localObject = str1;
              if (!str3.isEmpty())
              {
                localObject = str1;
                if (str2.equals(str3))
                {
                  localObject = new StringBuilder();
                  ((StringBuilder)localObject).append("GPU debug layers enabled for ");
                  ((StringBuilder)localObject).append(str3);
                  Log.i("GraphicsEnvironment", ((StringBuilder)localObject).toString());
                  localObject = new StringBuilder();
                  ((StringBuilder)localObject).append(mDebugLayerPath);
                  ((StringBuilder)localObject).append(":");
                  str1 = ((StringBuilder)localObject).toString();
                  paramContext = Settings.Global.getString(paramContext.getContentResolver(), "gpu_debug_layers");
                  localObject = new StringBuilder();
                  ((StringBuilder)localObject).append("Debug layer list: ");
                  ((StringBuilder)localObject).append(paramContext);
                  Log.i("GraphicsEnvironment", ((StringBuilder)localObject).toString());
                  localObject = str1;
                  if (paramContext != null)
                  {
                    localObject = str1;
                    if (!paramContext.isEmpty())
                    {
                      setDebugLayers(paramContext);
                      localObject = str1;
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    paramContext = new StringBuilder();
    paramContext.append((String)localObject);
    paramContext.append(mLayerPath);
    paramContext = paramContext.toString();
    setLayerPaths(mClassLoader, paramContext);
  }
  
  public void setLayerPaths(ClassLoader paramClassLoader, String paramString1, String paramString2)
  {
    mClassLoader = paramClassLoader;
    mLayerPath = paramString1;
    mDebugLayerPath = paramString2;
  }
  
  public void setup(Context paramContext)
  {
    setupGpuLayers(paramContext);
    chooseDriver(paramContext);
  }
}
