package com.android.internal.content;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageParser;
import android.content.pm.PackageParser.Package;
import android.content.pm.PackageParser.PackageLite;
import android.content.pm.PackageParser.PackageParserException;
import android.os.Build;
import android.os.SELinux;
import android.os.SystemProperties;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Slog;
import dalvik.system.CloseGuard;
import dalvik.system.VMRuntime;
import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.List;

public class NativeLibraryHelper
{
  private static final int BITCODE_PRESENT = 1;
  public static final String CLEAR_ABI_OVERRIDE = "-";
  private static final boolean DEBUG_NATIVE = false;
  private static final boolean HAS_NATIVE_BRIDGE = "0".equals(SystemProperties.get("ro.dalvik.vm.native.bridge", "0")) ^ true;
  public static final String LIB64_DIR_NAME = "lib64";
  public static final String LIB_DIR_NAME = "lib";
  private static final String TAG = "NativeHelper";
  
  public NativeLibraryHelper() {}
  
  public static int copyNativeBinaries(Handle paramHandle, File paramFile, String paramString)
  {
    long[] arrayOfLong = apkHandles;
    int i = arrayOfLong.length;
    for (int j = 0; j < i; j++)
    {
      int k = nativeCopyNativeBinaries(arrayOfLong[j], paramFile.getPath(), paramString, extractNativeLibs, HAS_NATIVE_BRIDGE, debuggable);
      if (k != 1) {
        return k;
      }
    }
    return 1;
  }
  
  public static int copyNativeBinariesForSupportedAbi(Handle paramHandle, File paramFile, String[] paramArrayOfString, boolean paramBoolean)
    throws IOException
  {
    createNativeLibrarySubdir(paramFile);
    int i = findSupportedAbi(paramHandle, paramArrayOfString);
    if (i >= 0)
    {
      String str = VMRuntime.getInstructionSet(paramArrayOfString[i]);
      if (paramBoolean)
      {
        paramFile = new File(paramFile, str);
        createNativeLibrarySubdir(paramFile);
      }
      int j = copyNativeBinaries(paramHandle, paramFile, paramArrayOfString[i]);
      if (j != 1) {
        return j;
      }
    }
    return i;
  }
  
  public static int copyNativeBinariesWithOverride(Handle paramHandle, File paramFile, String paramString)
  {
    try
    {
      int i;
      if (multiArch)
      {
        if ((paramString != null) && (!"-".equals(paramString))) {
          Slog.w("NativeHelper", "Ignoring abiOverride for multi arch application.");
        }
        if (Build.SUPPORTED_32_BIT_ABIS.length > 0)
        {
          i = copyNativeBinariesForSupportedAbi(paramHandle, paramFile, Build.SUPPORTED_32_BIT_ABIS, true);
          if ((i < 0) && (i != -114) && (i != -113))
          {
            paramHandle = new java/lang/StringBuilder;
            paramHandle.<init>();
            paramHandle.append("Failure copying 32 bit native libraries; copyRet=");
            paramHandle.append(i);
            Slog.w("NativeHelper", paramHandle.toString());
            return i;
          }
        }
        if (Build.SUPPORTED_64_BIT_ABIS.length > 0)
        {
          i = copyNativeBinariesForSupportedAbi(paramHandle, paramFile, Build.SUPPORTED_64_BIT_ABIS, true);
          if ((i < 0) && (i != -114) && (i != -113))
          {
            paramHandle = new java/lang/StringBuilder;
            paramHandle.<init>();
            paramHandle.append("Failure copying 64 bit native libraries; copyRet=");
            paramHandle.append(i);
            Slog.w("NativeHelper", paramHandle.toString());
            return i;
          }
        }
      }
      else
      {
        String str = null;
        if ("-".equals(paramString)) {
          str = null;
        } else if (paramString != null) {
          str = paramString;
        }
        if (str != null)
        {
          paramString = new String[1];
          paramString[0] = str;
        }
        else
        {
          paramString = Build.SUPPORTED_ABIS;
        }
        Object localObject = paramString;
        if (Build.SUPPORTED_64_BIT_ABIS.length > 0)
        {
          localObject = paramString;
          if (str == null)
          {
            localObject = paramString;
            if (hasRenderscriptBitcode(paramHandle)) {
              localObject = Build.SUPPORTED_32_BIT_ABIS;
            }
          }
        }
        i = copyNativeBinariesForSupportedAbi(paramHandle, paramFile, (String[])localObject, true);
        if ((i < 0) && (i != -114))
        {
          paramHandle = new java/lang/StringBuilder;
          paramHandle.<init>();
          paramHandle.append("Failure copying native libraries [errorCode=");
          paramHandle.append(i);
          paramHandle.append("]");
          Slog.w("NativeHelper", paramHandle.toString());
          return i;
        }
      }
      return 1;
    }
    catch (IOException paramHandle)
    {
      Slog.e("NativeHelper", "Copying native libraries failed", paramHandle);
    }
    return -110;
  }
  
  public static void createNativeLibrarySubdir(File paramFile)
    throws IOException
  {
    if (!paramFile.isDirectory())
    {
      paramFile.delete();
      if (paramFile.mkdir())
      {
        try
        {
          Os.chmod(paramFile.getPath(), OsConstants.S_IRWXU | OsConstants.S_IRGRP | OsConstants.S_IXGRP | OsConstants.S_IROTH | OsConstants.S_IXOTH);
        }
        catch (ErrnoException localErrnoException)
        {
          StringBuilder localStringBuilder2 = new StringBuilder();
          localStringBuilder2.append("Cannot chmod native library directory ");
          localStringBuilder2.append(paramFile.getPath());
          throw new IOException(localStringBuilder2.toString(), localErrnoException);
        }
      }
      else
      {
        localStringBuilder1 = new StringBuilder();
        localStringBuilder1.append("Cannot create ");
        localStringBuilder1.append(paramFile.getPath());
        throw new IOException(localStringBuilder1.toString());
      }
    }
    else
    {
      if (!SELinux.restorecon(paramFile)) {
        break label130;
      }
    }
    return;
    label130:
    StringBuilder localStringBuilder1 = new StringBuilder();
    localStringBuilder1.append("Cannot set SELinux context for ");
    localStringBuilder1.append(paramFile.getPath());
    throw new IOException(localStringBuilder1.toString());
  }
  
  public static int findSupportedAbi(Handle paramHandle, String[] paramArrayOfString)
  {
    int i = -114;
    long[] arrayOfLong = apkHandles;
    int j = arrayOfLong.length;
    int k = 0;
    while (k < j)
    {
      int m = nativeFindSupportedAbi(arrayOfLong[k], paramArrayOfString, debuggable);
      int n;
      if (m == -114)
      {
        n = i;
      }
      else if (m == -113)
      {
        n = i;
        if (i < 0) {
          n = -113;
        }
      }
      else
      {
        if (m < 0) {
          break label101;
        }
        if (i >= 0)
        {
          n = i;
          if (m >= i) {}
        }
        else
        {
          n = m;
        }
      }
      k++;
      i = n;
      continue;
      label101:
      return m;
    }
    return i;
  }
  
  private static native int hasRenderscriptBitcode(long paramLong);
  
  public static boolean hasRenderscriptBitcode(Handle paramHandle)
    throws IOException
  {
    paramHandle = apkHandles;
    int i = paramHandle.length;
    int j = 0;
    while (j < i)
    {
      int k = hasRenderscriptBitcode(paramHandle[j]);
      if (k >= 0)
      {
        if (k == 1) {
          return true;
        }
        j++;
      }
      else
      {
        paramHandle = new StringBuilder();
        paramHandle.append("Error scanning APK, code: ");
        paramHandle.append(k);
        throw new IOException(paramHandle.toString());
      }
    }
    return false;
  }
  
  private static native void nativeClose(long paramLong);
  
  private static native int nativeCopyNativeBinaries(long paramLong, String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3);
  
  private static native int nativeFindSupportedAbi(long paramLong, String[] paramArrayOfString, boolean paramBoolean);
  
  private static native long nativeOpenApk(String paramString);
  
  private static native long nativeOpenApkFd(FileDescriptor paramFileDescriptor, String paramString);
  
  private static native long nativeSumNativeBinaries(long paramLong, String paramString, boolean paramBoolean);
  
  public static void removeNativeBinariesFromDirLI(File paramFile, boolean paramBoolean)
  {
    if (paramFile.exists())
    {
      Object localObject = paramFile.listFiles();
      if (localObject != null) {
        for (int i = 0; i < localObject.length; i++) {
          if (localObject[i].isDirectory())
          {
            removeNativeBinariesFromDirLI(localObject[i], true);
          }
          else if (!localObject[i].delete())
          {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("Could not delete native binary: ");
            localStringBuilder.append(localObject[i].getPath());
            Slog.w("NativeHelper", localStringBuilder.toString());
          }
        }
      }
      if ((paramBoolean) && (!paramFile.delete()))
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Could not delete native binary directory: ");
        ((StringBuilder)localObject).append(paramFile.getPath());
        Slog.w("NativeHelper", ((StringBuilder)localObject).toString());
      }
    }
  }
  
  public static void removeNativeBinariesLI(String paramString)
  {
    if (paramString == null) {
      return;
    }
    removeNativeBinariesFromDirLI(new File(paramString), false);
  }
  
  private static long sumNativeBinaries(Handle paramHandle, String paramString)
  {
    long l = 0L;
    long[] arrayOfLong = apkHandles;
    int i = arrayOfLong.length;
    for (int j = 0; j < i; j++) {
      l += nativeSumNativeBinaries(arrayOfLong[j], paramString, debuggable);
    }
    return l;
  }
  
  private static long sumNativeBinariesForSupportedAbi(Handle paramHandle, String[] paramArrayOfString)
  {
    int i = findSupportedAbi(paramHandle, paramArrayOfString);
    if (i >= 0) {
      return sumNativeBinaries(paramHandle, paramArrayOfString[i]);
    }
    return 0L;
  }
  
  public static long sumNativeBinariesWithOverride(Handle paramHandle, String paramString)
    throws IOException
  {
    long l1 = 0L;
    long l2;
    if (multiArch)
    {
      if ((paramString != null) && (!"-".equals(paramString))) {
        Slog.w("NativeHelper", "Ignoring abiOverride for multi arch application.");
      }
      if (Build.SUPPORTED_32_BIT_ABIS.length > 0) {
        l1 = 0L + sumNativeBinariesForSupportedAbi(paramHandle, Build.SUPPORTED_32_BIT_ABIS);
      }
      l2 = l1;
      if (Build.SUPPORTED_64_BIT_ABIS.length > 0) {
        l2 = l1 + sumNativeBinariesForSupportedAbi(paramHandle, Build.SUPPORTED_64_BIT_ABIS);
      }
    }
    else
    {
      String str = null;
      if ("-".equals(paramString)) {
        str = null;
      } else if (paramString != null) {
        str = paramString;
      }
      if (str != null)
      {
        paramString = new String[1];
        paramString[0] = str;
      }
      else
      {
        paramString = Build.SUPPORTED_ABIS;
      }
      Object localObject = paramString;
      if (Build.SUPPORTED_64_BIT_ABIS.length > 0)
      {
        localObject = paramString;
        if (str == null)
        {
          localObject = paramString;
          if (hasRenderscriptBitcode(paramHandle)) {
            localObject = Build.SUPPORTED_32_BIT_ABIS;
          }
        }
      }
      l2 = 0L + sumNativeBinariesForSupportedAbi(paramHandle, (String[])localObject);
    }
    return l2;
  }
  
  public static class Handle
    implements Closeable
  {
    final long[] apkHandles;
    final boolean debuggable;
    final boolean extractNativeLibs;
    private volatile boolean mClosed;
    private final CloseGuard mGuard = CloseGuard.get();
    final boolean multiArch;
    
    Handle(long[] paramArrayOfLong, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
    {
      apkHandles = paramArrayOfLong;
      multiArch = paramBoolean1;
      extractNativeLibs = paramBoolean2;
      debuggable = paramBoolean3;
      mGuard.open("close");
    }
    
    public static Handle create(PackageParser.Package paramPackage)
      throws IOException
    {
      List localList = paramPackage.getAllCodePaths();
      int i = applicationInfo.flags;
      boolean bool1 = false;
      boolean bool2;
      if ((i & 0x80000000) != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      boolean bool3;
      if ((applicationInfo.flags & 0x10000000) != 0) {
        bool3 = true;
      } else {
        bool3 = false;
      }
      if ((applicationInfo.flags & 0x2) != 0) {
        bool1 = true;
      }
      return create(localList, bool2, bool3, bool1);
    }
    
    public static Handle create(PackageParser.PackageLite paramPackageLite)
      throws IOException
    {
      return create(paramPackageLite.getAllCodePaths(), multiArch, extractNativeLibs, debuggable);
    }
    
    public static Handle create(File paramFile)
      throws IOException
    {
      try
      {
        localObject = create(PackageParser.parsePackageLite(paramFile, 0));
        return localObject;
      }
      catch (PackageParser.PackageParserException localPackageParserException)
      {
        Object localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Failed to parse package: ");
        ((StringBuilder)localObject).append(paramFile);
        throw new IOException(((StringBuilder)localObject).toString(), localPackageParserException);
      }
    }
    
    private static Handle create(List<String> paramList, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
      throws IOException
    {
      int i = paramList.size();
      long[] arrayOfLong = new long[i];
      int j = 0;
      for (int k = 0; k < i; k++)
      {
        String str = (String)paramList.get(k);
        arrayOfLong[k] = NativeLibraryHelper.nativeOpenApk(str);
        if (arrayOfLong[k] == 0L)
        {
          while (j < k)
          {
            NativeLibraryHelper.nativeClose(arrayOfLong[j]);
            j++;
          }
          paramList = new StringBuilder();
          paramList.append("Unable to open APK: ");
          paramList.append(str);
          throw new IOException(paramList.toString());
        }
      }
      return new Handle(arrayOfLong, paramBoolean1, paramBoolean2, paramBoolean3);
    }
    
    public static Handle createFd(PackageParser.PackageLite paramPackageLite, FileDescriptor paramFileDescriptor)
      throws IOException
    {
      long[] arrayOfLong = new long[1];
      String str = baseCodePath;
      arrayOfLong[0] = NativeLibraryHelper.nativeOpenApkFd(paramFileDescriptor, str);
      if (arrayOfLong[0] != 0L) {
        return new Handle(arrayOfLong, multiArch, extractNativeLibs, debuggable);
      }
      paramPackageLite = new StringBuilder();
      paramPackageLite.append("Unable to open APK ");
      paramPackageLite.append(str);
      paramPackageLite.append(" from fd ");
      paramPackageLite.append(paramFileDescriptor);
      throw new IOException(paramPackageLite.toString());
    }
    
    public void close()
    {
      long[] arrayOfLong = apkHandles;
      int i = arrayOfLong.length;
      for (int j = 0; j < i; j++) {
        NativeLibraryHelper.nativeClose(arrayOfLong[j]);
      }
      mGuard.close();
      mClosed = true;
    }
    
    protected void finalize()
      throws Throwable
    {
      if (mGuard != null) {
        mGuard.warnIfOpen();
      }
      try
      {
        if (!mClosed) {
          close();
        }
        return;
      }
      finally
      {
        super.finalize();
      }
    }
  }
}
