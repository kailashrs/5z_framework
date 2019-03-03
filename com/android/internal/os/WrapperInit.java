package com.android.internal.os;

import android.os.Process;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructCapUserData;
import android.system.StructCapUserHeader;
import android.util.Slog;
import android.util.TimingsTraceLog;
import dalvik.system.VMRuntime;
import java.io.DataOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import libcore.io.IoUtils;

public class WrapperInit
{
  private static final String TAG = "AndroidRuntime";
  
  private WrapperInit() {}
  
  public static void execApplication(String paramString1, String paramString2, int paramInt, String paramString3, FileDescriptor paramFileDescriptor, String[] paramArrayOfString)
  {
    StringBuilder localStringBuilder = new StringBuilder(paramString1);
    if (VMRuntime.is64BitInstructionSet(paramString3)) {
      paramString1 = "/system/bin/app_process64";
    } else {
      paramString1 = "/system/bin/app_process32";
    }
    localStringBuilder.append(' ');
    localStringBuilder.append(paramString1);
    localStringBuilder.append(" -Xcompiler-option --generate-mini-debug-info");
    localStringBuilder.append(" /system/bin --application");
    if (paramString2 != null)
    {
      localStringBuilder.append(" '--nice-name=");
      localStringBuilder.append(paramString2);
      localStringBuilder.append("'");
    }
    localStringBuilder.append(" com.android.internal.os.WrapperInit ");
    int i;
    if (paramFileDescriptor != null) {
      i = paramFileDescriptor.getInt$();
    } else {
      i = 0;
    }
    localStringBuilder.append(i);
    localStringBuilder.append(' ');
    localStringBuilder.append(paramInt);
    Zygote.appendQuotedShellArgs(localStringBuilder, paramArrayOfString);
    preserveCapabilities();
    Zygote.execShell(localStringBuilder.toString());
  }
  
  public static void main(String[] paramArrayOfString)
  {
    int i = Integer.parseInt(paramArrayOfString[0], 10);
    int j = Integer.parseInt(paramArrayOfString[1], 10);
    if (i != 0) {
      try
      {
        FileDescriptor localFileDescriptor = new java/io/FileDescriptor;
        localFileDescriptor.<init>();
        localFileDescriptor.setInt$(i);
        DataOutputStream localDataOutputStream = new java/io/DataOutputStream;
        FileOutputStream localFileOutputStream = new java/io/FileOutputStream;
        localFileOutputStream.<init>(localFileDescriptor);
        localDataOutputStream.<init>(localFileOutputStream);
        localDataOutputStream.writeInt(Process.myPid());
        localDataOutputStream.close();
        IoUtils.closeQuietly(localFileDescriptor);
      }
      catch (IOException localIOException)
      {
        Slog.d("AndroidRuntime", "Could not write pid of wrapped process to Zygote pipe.", localIOException);
      }
    }
    ZygoteInit.preload(new TimingsTraceLog("WrapperInitTiming", 16384L));
    String[] arrayOfString = new String[paramArrayOfString.length - 2];
    System.arraycopy(paramArrayOfString, 2, arrayOfString, 0, arrayOfString.length);
    wrapperInit(j, arrayOfString).run();
  }
  
  private static void preserveCapabilities()
  {
    int i = OsConstants._LINUX_CAPABILITY_VERSION_3;
    int j = 0;
    Object localObject = new StructCapUserHeader(i, 0);
    try
    {
      StructCapUserData[] arrayOfStructCapUserData = Os.capget((StructCapUserHeader)localObject);
      if ((0permitted != 0inheritable) || (1permitted != 1inheritable))
      {
        arrayOfStructCapUserData[0] = new StructCapUserData(0effective, 0permitted, 0permitted);
        arrayOfStructCapUserData[1] = new StructCapUserData(1effective, 1permitted, 1permitted);
      }
      try
      {
        Os.capset((StructCapUserHeader)localObject, arrayOfStructCapUserData);
        while (j < 64)
        {
          int k = OsConstants.CAP_TO_INDEX(j);
          i = OsConstants.CAP_TO_MASK(j);
          if ((inheritable & i) != 0) {
            try
            {
              Os.prctl(OsConstants.PR_CAP_AMBIENT, OsConstants.PR_CAP_AMBIENT_RAISE, j, 0L, 0L);
            }
            catch (ErrnoException localErrnoException3)
            {
              localObject = new StringBuilder();
              ((StringBuilder)localObject).append("RuntimeInit: Failed to raise ambient capability ");
              ((StringBuilder)localObject).append(j);
              Slog.e("AndroidRuntime", ((StringBuilder)localObject).toString(), localErrnoException3);
            }
          }
          j++;
        }
        return;
      }
      catch (ErrnoException localErrnoException1)
      {
        Slog.e("AndroidRuntime", "RuntimeInit: Failed capset", localErrnoException1);
        return;
      }
      return;
    }
    catch (ErrnoException localErrnoException2)
    {
      Slog.e("AndroidRuntime", "RuntimeInit: Failed capget", localErrnoException2);
    }
  }
  
  private static Runnable wrapperInit(int paramInt, String[] paramArrayOfString)
  {
    Object localObject1 = null;
    Object localObject2 = localObject1;
    String[] arrayOfString = paramArrayOfString;
    if (paramArrayOfString != null)
    {
      localObject2 = localObject1;
      arrayOfString = paramArrayOfString;
      if (paramArrayOfString.length > 2)
      {
        localObject2 = localObject1;
        arrayOfString = paramArrayOfString;
        if (paramArrayOfString[0].equals("-cp"))
        {
          localObject2 = ZygoteInit.createPathClassLoader(paramArrayOfString[1], paramInt);
          Thread.currentThread().setContextClassLoader((ClassLoader)localObject2);
          arrayOfString = new String[paramArrayOfString.length - 2];
          System.arraycopy(paramArrayOfString, 2, arrayOfString, 0, paramArrayOfString.length - 2);
        }
      }
    }
    Zygote.nativePreApplicationInit();
    return RuntimeInit.applicationInit(paramInt, arrayOfString, (ClassLoader)localObject2);
  }
}
