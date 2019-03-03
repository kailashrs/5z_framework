package android.util;

import android.content.Context;
import android.os.SystemProperties;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class BoostFramework
{
  private static final String PERFORMANCE_CLASS = "com.qualcomm.qti.Performance";
  private static final String PERFORMANCE_JAR = "/system/framework/QPerformance.jar";
  private static final String TAG = "BoostFramework";
  public static final int UXE_EVENT_BINDAPP = 2;
  public static final int UXE_EVENT_DISPLAYED_ACT = 3;
  public static final int UXE_EVENT_GAME = 5;
  public static final int UXE_EVENT_KILL = 4;
  public static final int UXE_EVENT_PKG_INSTALL = 8;
  public static final int UXE_EVENT_PKG_UNINSTALL = 7;
  public static final int UXE_EVENT_SUB_LAUNCH = 6;
  public static final int UXE_TRIGGER = 1;
  private static final String UXPERFORMANCE_CLASS = "com.qualcomm.qti.UxPerformance";
  private static final String UXPERFORMANCE_JAR = "/system/framework/UxPerformance.jar";
  public static final int VENDOR_HINT_ACTIVITY_BOOST = 4228;
  public static final int VENDOR_HINT_ANIM_BOOST = 4227;
  public static final int VENDOR_HINT_DRAG_BOOST = 4231;
  public static final int VENDOR_HINT_FINGERPRINT_UNLOCK_BOOST = 4352;
  public static final int VENDOR_HINT_FIRST_DRAW = 4162;
  public static final int VENDOR_HINT_FIRST_LAUNCH_BOOST = 4225;
  public static final int VENDOR_HINT_MTP_BOOST = 4230;
  public static final int VENDOR_HINT_PACKAGE_INSTALL_BOOST = 4232;
  public static final int VENDOR_HINT_ROTATION_ANIM_BOOST = 4240;
  public static final int VENDOR_HINT_ROTATION_LATENCY_BOOST = 4233;
  public static final int VENDOR_HINT_SCROLL_BOOST = 4224;
  public static final int VENDOR_HINT_SUBSEQ_LAUNCH_BOOST = 4226;
  public static final int VENDOR_HINT_TAP_EVENT = 4163;
  public static final int VENDOR_HINT_TOUCH_BOOST = 4229;
  private static Method sAcquireFunc;
  private static Method sIOPStart;
  private static Method sIOPStop;
  private static int sIopv2;
  private static boolean sIsLoaded = false;
  private static Class<?> sPerfClass = null;
  private static Method sPerfHintFunc;
  private static Method sReleaseFunc;
  private static Method sReleaseHandlerFunc;
  private static Method sUXEngineEvents;
  private static Method sUXEngineTrigger;
  private static Method sUxIOPStart = null;
  private static boolean sUxIsLoaded;
  private static Class<?> sUxPerfClass;
  private Object mPerf = null;
  private Object mUxPerf = null;
  
  static
  {
    sAcquireFunc = null;
    sPerfHintFunc = null;
    sReleaseFunc = null;
    sReleaseHandlerFunc = null;
    sIopv2 = -1;
    sIOPStart = null;
    sIOPStop = null;
    sUXEngineEvents = null;
    sUXEngineTrigger = null;
    sUxIsLoaded = false;
    sUxPerfClass = null;
  }
  
  public BoostFramework()
  {
    initFunctions();
    try
    {
      if (sPerfClass != null) {
        mPerf = sPerfClass.newInstance();
      }
      if (sUxPerfClass != null) {
        mUxPerf = sUxPerfClass.newInstance();
      }
    }
    catch (Exception localException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("BoostFramework() : Exception_2 = ");
      localStringBuilder.append(localException);
      Log.e("BoostFramework", localStringBuilder.toString());
    }
  }
  
  public BoostFramework(Context paramContext)
  {
    initFunctions();
    try
    {
      if (sPerfClass != null)
      {
        Constructor localConstructor = sPerfClass.getConstructor(new Class[] { Context.class });
        if (localConstructor != null) {
          mPerf = localConstructor.newInstance(new Object[] { paramContext });
        }
      }
      if (sUxPerfClass != null) {
        mUxPerf = sUxPerfClass.newInstance();
      }
    }
    catch (Exception localException)
    {
      paramContext = new StringBuilder();
      paramContext.append("BoostFramework() : Exception_3 = ");
      paramContext.append(localException);
      Log.e("BoostFramework", paramContext.toString());
    }
  }
  
  private void initFunctions()
  {
    try
    {
      boolean bool = sIsLoaded;
      if (!bool)
      {
        Object localObject1;
        try
        {
          sPerfClass = Class.forName("com.qualcomm.qti.Performance");
          Class localClass1 = Integer.TYPE;
          sAcquireFunc = sPerfClass.getMethod("perfLockAcquire", new Class[] { localClass1, [I.class });
          localClass1 = Integer.TYPE;
          Class localClass2 = Integer.TYPE;
          Class localClass3 = Integer.TYPE;
          sPerfHintFunc = sPerfClass.getMethod("perfHint", new Class[] { localClass1, String.class, localClass2, localClass3 });
          sReleaseFunc = sPerfClass.getMethod("perfLockRelease", new Class[0]);
          localClass1 = Integer.TYPE;
          sReleaseHandlerFunc = sPerfClass.getDeclaredMethod("perfLockReleaseHandler", new Class[] { localClass1 });
          localClass1 = Integer.TYPE;
          sIOPStart = sPerfClass.getDeclaredMethod("perfIOPrefetchStart", new Class[] { localClass1, String.class, String.class });
          sIOPStop = sPerfClass.getDeclaredMethod("perfIOPrefetchStop", new Class[0]);
          try
          {
            localClass1 = Integer.TYPE;
            localClass2 = Integer.TYPE;
            localClass3 = Integer.TYPE;
            sUXEngineEvents = sPerfClass.getDeclaredMethod("perfUXEngine_events", new Class[] { localClass1, localClass2, String.class, localClass3 });
            localClass1 = Integer.TYPE;
            sUXEngineTrigger = sPerfClass.getDeclaredMethod("perfUXEngine_trigger", new Class[] { localClass1 });
          }
          catch (Exception localException1)
          {
            Log.i("BoostFramework", "BoostFramework() : Exception_4 = PreferredApps not supported");
          }
          sIsLoaded = true;
        }
        catch (Exception localException2)
        {
          localObject1 = new java/lang/StringBuilder;
          ((StringBuilder)localObject1).<init>();
          ((StringBuilder)localObject1).append("BoostFramework() : Exception_1 = ");
          ((StringBuilder)localObject1).append(localException2);
          Log.e("BoostFramework", ((StringBuilder)localObject1).toString());
        }
        try
        {
          sUxPerfClass = Class.forName("com.qualcomm.qti.UxPerformance");
          localObject1 = Integer.TYPE;
          sUxIOPStart = sUxPerfClass.getDeclaredMethod("perfIOPrefetchStart", new Class[] { localObject1, String.class, String.class });
          sUxIsLoaded = true;
        }
        catch (Exception localException3)
        {
          localObject1 = new java/lang/StringBuilder;
          ((StringBuilder)localObject1).<init>();
          ((StringBuilder)localObject1).append("BoostFramework() Ux Perf: Exception = ");
          ((StringBuilder)localObject1).append(localException3);
          Log.e("BoostFramework", ((StringBuilder)localObject1).toString());
        }
      }
      return;
    }
    finally {}
  }
  
  public int perfHint(int paramInt, String paramString)
  {
    return perfHint(paramInt, paramString, -1, -1);
  }
  
  public int perfHint(int paramInt1, String paramString, int paramInt2)
  {
    return perfHint(paramInt1, paramString, paramInt2, -1);
  }
  
  public int perfHint(int paramInt1, String paramString, int paramInt2, int paramInt3)
  {
    int i = -1;
    int j = i;
    try
    {
      if (sPerfHintFunc != null) {
        j = ((Integer)sPerfHintFunc.invoke(mPerf, new Object[] { Integer.valueOf(paramInt1), paramString, Integer.valueOf(paramInt2), Integer.valueOf(paramInt3) })).intValue();
      }
    }
    catch (Exception paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Exception ");
      localStringBuilder.append(paramString);
      Log.e("BoostFramework", localStringBuilder.toString());
      j = i;
    }
    return j;
  }
  
  public int perfIOPrefetchStart(int paramInt, String paramString1, String paramString2)
  {
    int i = -1;
    int j;
    try
    {
      j = ((Integer)sIOPStart.invoke(mPerf, new Object[] { Integer.valueOf(paramInt), paramString1, paramString2 })).intValue();
    }
    catch (Exception localException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Exception ");
      localStringBuilder.append(localException);
      Log.e("BoostFramework", localStringBuilder.toString());
      j = i;
    }
    try
    {
      paramInt = ((Integer)sUxIOPStart.invoke(mUxPerf, new Object[] { Integer.valueOf(paramInt), paramString1, paramString2 })).intValue();
      j = paramInt;
    }
    catch (Exception paramString2)
    {
      paramString1 = new StringBuilder();
      paramString1.append("Ux Perf Exception ");
      paramString1.append(paramString2);
      Log.e("BoostFramework", paramString1.toString());
    }
    return j;
  }
  
  public int perfIOPrefetchStop()
  {
    int i = -1;
    try
    {
      int j = ((Integer)sIOPStop.invoke(mPerf, new Object[0])).intValue();
      i = j;
    }
    catch (Exception localException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Exception ");
      localStringBuilder.append(localException);
      Log.e("BoostFramework", localStringBuilder.toString());
    }
    return i;
  }
  
  public int perfLockAcquire(int paramInt, int... paramVarArgs)
  {
    int i = -1;
    int j = i;
    try
    {
      if (sAcquireFunc != null) {
        j = ((Integer)sAcquireFunc.invoke(mPerf, new Object[] { Integer.valueOf(paramInt), paramVarArgs })).intValue();
      }
    }
    catch (Exception localException)
    {
      paramVarArgs = new StringBuilder();
      paramVarArgs.append("Exception ");
      paramVarArgs.append(localException);
      Log.e("BoostFramework", paramVarArgs.toString());
      j = i;
    }
    return j;
  }
  
  public int perfLockRelease()
  {
    int i = -1;
    int j = i;
    try
    {
      if (sReleaseFunc != null) {
        j = ((Integer)sReleaseFunc.invoke(mPerf, new Object[0])).intValue();
      }
    }
    catch (Exception localException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Exception ");
      localStringBuilder.append(localException);
      Log.e("BoostFramework", localStringBuilder.toString());
      j = i;
    }
    return j;
  }
  
  public int perfLockReleaseHandler(int paramInt)
  {
    int i = -1;
    int j = i;
    try
    {
      if (sReleaseHandlerFunc != null) {
        j = ((Integer)sReleaseHandlerFunc.invoke(mPerf, new Object[] { Integer.valueOf(paramInt) })).intValue();
      }
    }
    catch (Exception localException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Exception ");
      localStringBuilder.append(localException);
      Log.e("BoostFramework", localStringBuilder.toString());
      j = i;
    }
    return j;
  }
  
  public int perfUXEngine_events(int paramInt1, int paramInt2, String paramString, int paramInt3)
  {
    int i = -1;
    if (sIopv2 == -1) {
      sIopv2 = SystemProperties.getInt("vendor.iop.enable_uxe", 0);
    }
    try
    {
      if ((sIopv2 != 0) && (sUXEngineEvents != null)) {
        paramInt1 = ((Integer)sUXEngineEvents.invoke(mPerf, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), paramString, Integer.valueOf(paramInt3) })).intValue();
      } else {
        return -1;
      }
    }
    catch (Exception localException)
    {
      paramString = new StringBuilder();
      paramString.append("Exception ");
      paramString.append(localException);
      Log.e("BoostFramework", paramString.toString());
      paramInt1 = i;
    }
    return paramInt1;
  }
  
  public String perfUXEngine_trigger(int paramInt)
  {
    Object localObject = null;
    if (sIopv2 == -1) {
      sIopv2 = SystemProperties.getInt("vendor.iop.enable_uxe", 0);
    }
    try
    {
      if ((sIopv2 != 0) && (sUXEngineTrigger != null))
      {
        String str = (String)sUXEngineTrigger.invoke(mPerf, new Object[] { Integer.valueOf(paramInt) });
        localObject = str;
      }
      else
      {
        return null;
      }
    }
    catch (Exception localException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Exception ");
      localStringBuilder.append(localException);
      Log.e("BoostFramework", localStringBuilder.toString());
    }
    return localObject;
  }
  
  public class Draw
  {
    public static final int EVENT_TYPE_V1 = 1;
    
    public Draw() {}
  }
  
  public class Launch
  {
    public static final int BOOST_V1 = 1;
    public static final int BOOST_V2 = 2;
    public static final int BOOST_V3 = 3;
    public static final int TYPE_SERVICE_START = 100;
    
    public Launch() {}
  }
  
  public class Scroll
  {
    public static final int HORIZONTAL = 2;
    public static final int PANEL_VIEW = 3;
    public static final int PREFILING = 4;
    public static final int VERTICAL = 1;
    
    public Scroll() {}
  }
}
