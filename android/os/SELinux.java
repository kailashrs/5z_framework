package android.os;

import android.util.Slog;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

public class SELinux
{
  private static final int SELINUX_ANDROID_RESTORECON_DATADATA = 16;
  private static final int SELINUX_ANDROID_RESTORECON_FORCE = 8;
  private static final int SELINUX_ANDROID_RESTORECON_NOCHANGE = 1;
  private static final int SELINUX_ANDROID_RESTORECON_RECURSE = 4;
  private static final int SELINUX_ANDROID_RESTORECON_VERBOSE = 2;
  private static final String TAG = "SELinux";
  
  public SELinux() {}
  
  public static final native boolean checkSELinuxAccess(String paramString1, String paramString2, String paramString3, String paramString4);
  
  public static final native String getContext();
  
  public static final native String getFileContext(String paramString);
  
  public static final native String getPeerContext(FileDescriptor paramFileDescriptor);
  
  public static final native String getPidContext(int paramInt);
  
  public static final native boolean isSELinuxEnabled();
  
  public static final native boolean isSELinuxEnforced();
  
  private static native boolean native_restorecon(String paramString, int paramInt);
  
  public static boolean restorecon(File paramFile)
    throws NullPointerException
  {
    try
    {
      boolean bool = native_restorecon(paramFile.getCanonicalPath(), 0);
      return bool;
    }
    catch (IOException localIOException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Error getting canonical path. Restorecon failed for ");
      localStringBuilder.append(paramFile.getPath());
      Slog.e("SELinux", localStringBuilder.toString(), localIOException);
    }
    return false;
  }
  
  public static boolean restorecon(String paramString)
    throws NullPointerException
  {
    if (paramString != null) {
      return native_restorecon(paramString, 0);
    }
    throw new NullPointerException();
  }
  
  public static boolean restoreconRecursive(File paramFile)
  {
    try
    {
      boolean bool = native_restorecon(paramFile.getCanonicalPath(), 4);
      return bool;
    }
    catch (IOException localIOException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Error getting canonical path. Restorecon failed for ");
      localStringBuilder.append(paramFile.getPath());
      Slog.e("SELinux", localStringBuilder.toString(), localIOException);
    }
    return false;
  }
  
  public static final native boolean setFSCreateContext(String paramString);
  
  public static final native boolean setFileContext(String paramString1, String paramString2);
}
