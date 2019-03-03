package com.android.internal.os;

import android.os.Trace;
import android.system.ErrnoException;
import android.system.Os;
import dalvik.system.ZygoteHooks;

public final class Zygote
{
  public static final int API_ENFORCEMENT_POLICY_MASK = 12288;
  public static final int API_ENFORCEMENT_POLICY_SHIFT = Integer.numberOfTrailingZeros(12288);
  public static final String CHILD_ZYGOTE_SOCKET_NAME_ARG = "--zygote-socket=";
  public static final int DEBUG_ALWAYS_JIT = 64;
  public static final int DEBUG_ENABLE_ASSERT = 4;
  public static final int DEBUG_ENABLE_CHECKJNI = 2;
  public static final int DEBUG_ENABLE_JDWP = 1;
  public static final int DEBUG_ENABLE_JNI_LOGGING = 16;
  public static final int DEBUG_ENABLE_SAFEMODE = 8;
  public static final int DEBUG_GENERATE_DEBUG_INFO = 32;
  public static final int DEBUG_GENERATE_MINI_DEBUG_INFO = 2048;
  public static final int DEBUG_JAVA_DEBUGGABLE = 256;
  public static final int DEBUG_NATIVE_DEBUGGABLE = 128;
  public static final int DISABLE_VERIFIER = 512;
  public static final int MOUNT_EXTERNAL_DEFAULT = 1;
  public static final int MOUNT_EXTERNAL_NONE = 0;
  public static final int MOUNT_EXTERNAL_READ = 2;
  public static final int MOUNT_EXTERNAL_WRITE = 3;
  public static final int ONLY_USE_SYSTEM_OAT_FILES = 1024;
  public static final int PROFILE_SYSTEM_SERVER = 16384;
  private static final ZygoteHooks VM_HOOKS = new ZygoteHooks();
  
  private Zygote() {}
  
  public static void appendQuotedShellArgs(StringBuilder paramStringBuilder, String[] paramArrayOfString)
  {
    int i = paramArrayOfString.length;
    for (int j = 0; j < i; j++)
    {
      String str = paramArrayOfString[j];
      paramStringBuilder.append(" '");
      paramStringBuilder.append(str.replace("'", "'\\''"));
      paramStringBuilder.append("'");
    }
  }
  
  private static void callPostForkChildHooks(int paramInt, boolean paramBoolean1, boolean paramBoolean2, String paramString)
  {
    VM_HOOKS.postForkChild(paramInt, paramBoolean1, paramBoolean2, paramString);
  }
  
  public static void execShell(String paramString)
  {
    String[] arrayOfString = new String[3];
    arrayOfString[0] = "/system/bin/sh";
    arrayOfString[1] = "-c";
    arrayOfString[2] = paramString;
    try
    {
      Os.execv(arrayOfString[0], arrayOfString);
      return;
    }
    catch (ErrnoException paramString)
    {
      throw new RuntimeException(paramString);
    }
  }
  
  public static int forkAndSpecialize(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int paramInt3, int[][] paramArrayOfInt, int paramInt4, String paramString1, String paramString2, int[] paramArrayOfInt2, int[] paramArrayOfInt3, boolean paramBoolean, String paramString3, String paramString4)
  {
    VM_HOOKS.preFork();
    resetNicePriority();
    paramInt1 = nativeForkAndSpecialize(paramInt1, paramInt2, paramArrayOfInt1, paramInt3, paramArrayOfInt, paramInt4, paramString1, paramString2, paramArrayOfInt2, paramArrayOfInt3, paramBoolean, paramString3, paramString4);
    if (paramInt1 == 0)
    {
      Trace.setTracingEnabled(true, paramInt3);
      Trace.traceBegin(64L, "PostFork");
    }
    VM_HOOKS.postForkCommon();
    return paramInt1;
  }
  
  public static int forkSystemServer(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int[][] paramArrayOfInt1, long paramLong1, long paramLong2)
  {
    VM_HOOKS.preFork();
    resetNicePriority();
    paramInt1 = nativeForkSystemServer(paramInt1, paramInt2, paramArrayOfInt, paramInt3, paramArrayOfInt1, paramLong1, paramLong2);
    if (paramInt1 == 0) {
      Trace.setTracingEnabled(true, paramInt3);
    }
    VM_HOOKS.postForkCommon();
    return paramInt1;
  }
  
  protected static native void nativeAllowFileAcrossFork(String paramString);
  
  private static native int nativeForkAndSpecialize(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int paramInt3, int[][] paramArrayOfInt, int paramInt4, String paramString1, String paramString2, int[] paramArrayOfInt2, int[] paramArrayOfInt3, boolean paramBoolean, String paramString3, String paramString4);
  
  private static native int nativeForkSystemServer(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int[][] paramArrayOfInt1, long paramLong1, long paramLong2);
  
  static native void nativePreApplicationInit();
  
  static native void nativeSecurityInit();
  
  protected static native void nativeUnmountStorageOnInit();
  
  static void resetNicePriority()
  {
    Thread.currentThread().setPriority(5);
  }
}
