package android.os;

import android.annotation.SystemApi;
import android.util.Log;
import android.util.MutableInt;
import com.android.internal.annotations.GuardedBy;
import java.util.ArrayList;
import java.util.HashMap;

@SystemApi
public class SystemProperties
{
  public static final int PROP_NAME_MAX = Integer.MAX_VALUE;
  public static final int PROP_VALUE_MAX = 91;
  private static final String TAG = "SystemProperties";
  private static final boolean TRACK_KEY_ACCESS = false;
  @GuardedBy("sChangeCallbacks")
  private static final ArrayList<Runnable> sChangeCallbacks = new ArrayList();
  @GuardedBy("sRoReads")
  private static final HashMap<String, MutableInt> sRoReads = null;
  
  private SystemProperties() {}
  
  public static void addChangeCallback(Runnable paramRunnable)
  {
    synchronized (sChangeCallbacks)
    {
      if (sChangeCallbacks.size() == 0) {
        native_add_change_callback();
      }
      sChangeCallbacks.add(paramRunnable);
      return;
    }
  }
  
  private static void callChangeCallbacks()
  {
    synchronized (sChangeCallbacks)
    {
      if (sChangeCallbacks.size() == 0) {
        return;
      }
      ArrayList localArrayList2 = new java/util/ArrayList;
      localArrayList2.<init>(sChangeCallbacks);
      for (int i = 0;; i++)
      {
        int j = localArrayList2.size();
        if (i >= j) {
          break;
        }
        try
        {
          ((Runnable)localArrayList2.get(i)).run();
        }
        catch (Throwable localThrowable)
        {
          Log.wtf("SystemProperties", "Exception in SystemProperties change callback", localThrowable);
        }
      }
      return;
    }
  }
  
  @SystemApi
  public static String get(String paramString)
  {
    return native_get(paramString);
  }
  
  @SystemApi
  public static String get(String paramString1, String paramString2)
  {
    return native_get(paramString1, paramString2);
  }
  
  @SystemApi
  public static boolean getBoolean(String paramString, boolean paramBoolean)
  {
    return native_get_boolean(paramString, paramBoolean);
  }
  
  @SystemApi
  public static int getInt(String paramString, int paramInt)
  {
    return native_get_int(paramString, paramInt);
  }
  
  @SystemApi
  public static long getLong(String paramString, long paramLong)
  {
    return native_get_long(paramString, paramLong);
  }
  
  private static native void native_add_change_callback();
  
  private static native String native_get(String paramString);
  
  private static native String native_get(String paramString1, String paramString2);
  
  private static native boolean native_get_boolean(String paramString, boolean paramBoolean);
  
  private static native int native_get_int(String paramString, int paramInt);
  
  private static native long native_get_long(String paramString, long paramLong);
  
  private static native void native_report_sysprop_change();
  
  private static native void native_set(String paramString1, String paramString2);
  
  private static void onKeyAccess(String paramString) {}
  
  public static void reportSyspropChanged() {}
  
  public static void set(String paramString1, String paramString2)
  {
    if ((paramString2 != null) && (!paramString2.startsWith("ro.")) && (paramString2.length() > 91))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("value of system property '");
      localStringBuilder.append(paramString1);
      localStringBuilder.append("' is longer than ");
      localStringBuilder.append(91);
      localStringBuilder.append(" characters: ");
      localStringBuilder.append(paramString2);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    native_set(paramString1, paramString2);
  }
}
