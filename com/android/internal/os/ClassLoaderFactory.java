package com.android.internal.os;

import android.os.Trace;
import dalvik.system.DelegateLastClassLoader;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class ClassLoaderFactory
{
  private static final String DELEGATE_LAST_CLASS_LOADER_NAME = DelegateLastClassLoader.class.getName();
  private static final String DEX_CLASS_LOADER_NAME;
  private static final String PATH_CLASS_LOADER_NAME = PathClassLoader.class.getName();
  
  static
  {
    DEX_CLASS_LOADER_NAME = DexClassLoader.class.getName();
  }
  
  private ClassLoaderFactory() {}
  
  public static ClassLoader createClassLoader(String paramString1, String paramString2, ClassLoader paramClassLoader, String paramString3)
  {
    if (isPathClassLoaderName(paramString3)) {
      return new PathClassLoader(paramString1, paramString2, paramClassLoader);
    }
    if (isDelegateLastClassLoaderName(paramString3)) {
      return new DelegateLastClassLoader(paramString1, paramString2, paramClassLoader);
    }
    paramString1 = new StringBuilder();
    paramString1.append("Invalid classLoaderName: ");
    paramString1.append(paramString3);
    throw new AssertionError(paramString1.toString());
  }
  
  public static ClassLoader createClassLoader(String paramString1, String paramString2, String paramString3, ClassLoader paramClassLoader, int paramInt, boolean paramBoolean, String paramString4)
  {
    paramClassLoader = createClassLoader(paramString1, paramString2, paramClassLoader, paramString4);
    boolean bool1 = false;
    paramString1 = paramString1.split(":");
    int i = paramString1.length;
    boolean bool2;
    for (int j = 0;; j++)
    {
      bool2 = bool1;
      if (j >= i) {
        break;
      }
      if (paramString1[j].startsWith("/vendor/"))
      {
        bool2 = true;
        break;
      }
    }
    Trace.traceBegin(64L, "createClassloaderNamespace");
    paramString1 = createClassloaderNamespace(paramClassLoader, paramInt, paramString2, paramString3, paramBoolean, bool2);
    Trace.traceEnd(64L);
    if (paramString1 == null) {
      return paramClassLoader;
    }
    paramString2 = new StringBuilder();
    paramString2.append("Unable to create namespace for the classloader ");
    paramString2.append(paramClassLoader);
    paramString2.append(": ");
    paramString2.append(paramString1);
    throw new UnsatisfiedLinkError(paramString2.toString());
  }
  
  private static native String createClassloaderNamespace(ClassLoader paramClassLoader, int paramInt, String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2);
  
  public static boolean isDelegateLastClassLoaderName(String paramString)
  {
    return DELEGATE_LAST_CLASS_LOADER_NAME.equals(paramString);
  }
  
  public static boolean isPathClassLoaderName(String paramString)
  {
    boolean bool;
    if ((paramString != null) && (!PATH_CLASS_LOADER_NAME.equals(paramString)) && (!DEX_CLASS_LOADER_NAME.equals(paramString))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static boolean isValidClassLoaderName(String paramString)
  {
    boolean bool;
    if ((paramString != null) && ((isPathClassLoaderName(paramString)) || (isDelegateLastClassLoaderName(paramString)))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
}
