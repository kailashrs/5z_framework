package android.app;

import android.os.Build.VERSION;
import android.os.GraphicsEnvironment;
import android.os.Trace;
import android.util.ArrayMap;
import com.android.internal.os.ClassLoaderFactory;
import dalvik.system.PathClassLoader;
import java.util.Collection;

public class ApplicationLoaders
{
  private static final ApplicationLoaders gApplicationLoaders = new ApplicationLoaders();
  private final ArrayMap<String, ClassLoader> mLoaders = new ArrayMap();
  
  public ApplicationLoaders() {}
  
  private ClassLoader getClassLoader(String paramString1, int paramInt, boolean paramBoolean, String paramString2, String paramString3, ClassLoader paramClassLoader, String paramString4, String paramString5)
  {
    ClassLoader localClassLoader = ClassLoader.getSystemClassLoader().getParent();
    ArrayMap localArrayMap = mLoaders;
    if (paramClassLoader == null) {
      paramClassLoader = localClassLoader;
    }
    if (paramClassLoader == localClassLoader)
    {
      try
      {
        localClassLoader = (ClassLoader)mLoaders.get(paramString4);
        if (localClassLoader != null) {
          return localClassLoader;
        }
        Trace.traceBegin(64L, paramString1);
        paramString1 = ClassLoaderFactory.createClassLoader(paramString1, paramString2, paramString3, paramClassLoader, paramInt, paramBoolean, paramString5);
        Trace.traceEnd(64L);
        Trace.traceBegin(64L, "setLayerPaths");
        paramClassLoader = GraphicsEnvironment.getInstance();
      }
      finally
      {
        try
        {
          paramClassLoader.setLayerPaths(paramString1, paramString2, paramString3);
          Trace.traceEnd(64L);
          mLoaders.put(paramString4, paramString1);
          return paramString1;
        }
        finally
        {
          for (;;) {}
        }
        paramString1 = finally;
      }
    }
    else
    {
      Trace.traceBegin(64L, paramString1);
      try
      {
        paramString1 = ClassLoaderFactory.createClassLoader(paramString1, null, paramClassLoader, paramString5);
        Trace.traceEnd(64L);
        return paramString1;
      }
      finally {}
    }
    throw paramString1;
  }
  
  public static ApplicationLoaders getDefault()
  {
    return gApplicationLoaders;
  }
  
  void addNative(ClassLoader paramClassLoader, Collection<String> paramCollection)
  {
    if ((paramClassLoader instanceof PathClassLoader))
    {
      ((PathClassLoader)paramClassLoader).addNativePath(paramCollection);
      return;
    }
    throw new IllegalStateException("class loader is not a PathClassLoader");
  }
  
  void addPath(ClassLoader paramClassLoader, String paramString)
  {
    if ((paramClassLoader instanceof PathClassLoader))
    {
      ((PathClassLoader)paramClassLoader).addDexPath(paramString);
      return;
    }
    throw new IllegalStateException("class loader is not a PathClassLoader");
  }
  
  public ClassLoader createAndCacheWebViewClassLoader(String paramString1, String paramString2, String paramString3)
  {
    return getClassLoader(paramString1, Build.VERSION.SDK_INT, false, paramString2, null, null, paramString3, null);
  }
  
  ClassLoader getClassLoader(String paramString1, int paramInt, boolean paramBoolean, String paramString2, String paramString3, ClassLoader paramClassLoader, String paramString4)
  {
    return getClassLoader(paramString1, paramInt, paramBoolean, paramString2, paramString3, paramClassLoader, paramString1, paramString4);
  }
}
