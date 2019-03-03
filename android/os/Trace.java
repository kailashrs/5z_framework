package android.os;

import dalvik.annotation.optimization.FastNative;

public final class Trace
{
  private static final int MAX_SECTION_NAME_LEN = 127;
  private static final String TAG = "Trace";
  public static final long TRACE_TAG_ACTIVITY_MANAGER = 64L;
  public static final long TRACE_TAG_ADB = 4194304L;
  public static final long TRACE_TAG_AIDL = 16777216L;
  public static final long TRACE_TAG_ALWAYS = 1L;
  public static final long TRACE_TAG_APP = 4096L;
  public static final long TRACE_TAG_AUDIO = 256L;
  public static final long TRACE_TAG_BIONIC = 65536L;
  public static final long TRACE_TAG_CAMERA = 1024L;
  public static final long TRACE_TAG_DALVIK = 16384L;
  public static final long TRACE_TAG_DATABASE = 1048576L;
  public static final long TRACE_TAG_GRAPHICS = 2L;
  public static final long TRACE_TAG_HAL = 2048L;
  public static final long TRACE_TAG_INPUT = 4L;
  public static final long TRACE_TAG_NETWORK = 2097152L;
  public static final long TRACE_TAG_NEVER = 0L;
  private static final long TRACE_TAG_NOT_READY = Long.MIN_VALUE;
  public static final long TRACE_TAG_PACKAGE_MANAGER = 262144L;
  public static final long TRACE_TAG_POWER = 131072L;
  public static final long TRACE_TAG_RESOURCES = 8192L;
  public static final long TRACE_TAG_RS = 32768L;
  public static final long TRACE_TAG_SYNC_MANAGER = 128L;
  public static final long TRACE_TAG_SYSTEM_SERVER = 524288L;
  public static final long TRACE_TAG_VIBRATOR = 8388608L;
  public static final long TRACE_TAG_VIDEO = 512L;
  public static final long TRACE_TAG_VIEW = 8L;
  public static final long TRACE_TAG_WEBVIEW = 16L;
  public static final long TRACE_TAG_WINDOW_MANAGER = 32L;
  private static volatile long sEnabledTags = Long.MIN_VALUE;
  private static int sZygoteDebugFlags = 0;
  
  static
  {
    SystemProperties.addChangeCallback(_..Lambda.Trace.2zLZ_Lc2kAXsVjw_nLYeNhqmGq0.INSTANCE);
  }
  
  private Trace() {}
  
  public static void asyncTraceBegin(long paramLong, String paramString, int paramInt)
  {
    if (isTagEnabled(paramLong)) {
      nativeAsyncTraceBegin(paramLong, paramString, paramInt);
    }
  }
  
  public static void asyncTraceEnd(long paramLong, String paramString, int paramInt)
  {
    if (isTagEnabled(paramLong)) {
      nativeAsyncTraceEnd(paramLong, paramString, paramInt);
    }
  }
  
  public static void beginSection(String paramString)
  {
    if (isTagEnabled(4096L)) {
      if (paramString.length() <= 127) {
        nativeTraceBegin(4096L, paramString);
      } else {
        throw new IllegalArgumentException("sectionName is too long");
      }
    }
  }
  
  private static long cacheEnabledTags()
  {
    long l = nativeGetEnabledTags();
    sEnabledTags = l;
    return l;
  }
  
  public static void endSection()
  {
    if (isTagEnabled(4096L)) {
      nativeTraceEnd(4096L);
    }
  }
  
  public static boolean isTagEnabled(long paramLong)
  {
    long l1 = sEnabledTags;
    long l2 = l1;
    if (l1 == Long.MIN_VALUE) {
      l2 = cacheEnabledTags();
    }
    boolean bool;
    if ((l2 & paramLong) != 0L) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @FastNative
  private static native void nativeAsyncTraceBegin(long paramLong, String paramString, int paramInt);
  
  @FastNative
  private static native void nativeAsyncTraceEnd(long paramLong, String paramString, int paramInt);
  
  private static native long nativeGetEnabledTags();
  
  private static native void nativeSetAppTracingAllowed(boolean paramBoolean);
  
  private static native void nativeSetTracingEnabled(boolean paramBoolean);
  
  @FastNative
  private static native void nativeTraceBegin(long paramLong, String paramString);
  
  @FastNative
  private static native void nativeTraceCounter(long paramLong, String paramString, int paramInt);
  
  @FastNative
  private static native void nativeTraceEnd(long paramLong);
  
  public static void setAppTracingAllowed(boolean paramBoolean)
  {
    nativeSetAppTracingAllowed(paramBoolean);
    cacheEnabledTags();
  }
  
  public static void setTracingEnabled(boolean paramBoolean, int paramInt)
  {
    nativeSetTracingEnabled(paramBoolean);
    sZygoteDebugFlags = paramInt;
    cacheEnabledTags();
  }
  
  public static void traceBegin(long paramLong, String paramString)
  {
    if (isTagEnabled(paramLong)) {
      nativeTraceBegin(paramLong, paramString);
    }
  }
  
  public static void traceCounter(long paramLong, String paramString, int paramInt)
  {
    if (isTagEnabled(paramLong)) {
      nativeTraceCounter(paramLong, paramString, paramInt);
    }
  }
  
  public static void traceEnd(long paramLong)
  {
    if (isTagEnabled(paramLong)) {
      nativeTraceEnd(paramLong);
    }
  }
}
