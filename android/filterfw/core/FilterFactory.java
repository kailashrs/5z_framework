package android.filterfw.core;

import android.util.Log;
import dalvik.system.PathClassLoader;
import java.util.HashSet;
import java.util.Iterator;

public class FilterFactory
{
  private static final String TAG = "FilterFactory";
  private static Object mClassLoaderGuard = new Object();
  private static ClassLoader mCurrentClassLoader = Thread.currentThread().getContextClassLoader();
  private static HashSet<String> mLibraries = new HashSet();
  private static boolean mLogVerbose = Log.isLoggable("FilterFactory", 2);
  private static FilterFactory mSharedFactory;
  private HashSet<String> mPackages = new HashSet();
  
  public FilterFactory() {}
  
  public static void addFilterLibrary(String paramString)
  {
    if (mLogVerbose)
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("Adding filter library ");
      ((StringBuilder)???).append(paramString);
      Log.v("FilterFactory", ((StringBuilder)???).toString());
    }
    synchronized (mClassLoaderGuard)
    {
      if (mLibraries.contains(paramString))
      {
        if (mLogVerbose) {
          Log.v("FilterFactory", "Library already added");
        }
        return;
      }
      mLibraries.add(paramString);
      PathClassLoader localPathClassLoader = new dalvik/system/PathClassLoader;
      localPathClassLoader.<init>(paramString, mCurrentClassLoader);
      mCurrentClassLoader = localPathClassLoader;
      return;
    }
  }
  
  public static FilterFactory sharedFactory()
  {
    if (mSharedFactory == null) {
      mSharedFactory = new FilterFactory();
    }
    return mSharedFactory;
  }
  
  public void addPackage(String paramString)
  {
    if (mLogVerbose)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Adding package ");
      localStringBuilder.append(paramString);
      Log.v("FilterFactory", localStringBuilder.toString());
    }
    mPackages.add(paramString);
  }
  
  /* Error */
  public Filter createFilterByClass(Class paramClass, String paramString)
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc 106
    //   3: invokevirtual 112	java/lang/Class:asSubclass	(Ljava/lang/Class;)Ljava/lang/Class;
    //   6: pop
    //   7: aconst_null
    //   8: astore_3
    //   9: aload_1
    //   10: iconst_1
    //   11: anewarray 108	java/lang/Class
    //   14: dup
    //   15: iconst_0
    //   16: ldc 114
    //   18: aastore
    //   19: invokevirtual 118	java/lang/Class:getConstructor	([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;
    //   22: astore 4
    //   24: aload 4
    //   26: iconst_1
    //   27: anewarray 4	java/lang/Object
    //   30: dup
    //   31: iconst_0
    //   32: aload_2
    //   33: aastore
    //   34: invokevirtual 124	java/lang/reflect/Constructor:newInstance	([Ljava/lang/Object;)Ljava/lang/Object;
    //   37: checkcast 106	android/filterfw/core/Filter
    //   40: astore_1
    //   41: goto +6 -> 47
    //   44: astore_1
    //   45: aload_3
    //   46: astore_1
    //   47: aload_1
    //   48: ifnull +5 -> 53
    //   51: aload_1
    //   52: areturn
    //   53: new 59	java/lang/StringBuilder
    //   56: dup
    //   57: invokespecial 60	java/lang/StringBuilder:<init>	()V
    //   60: astore_1
    //   61: aload_1
    //   62: ldc 126
    //   64: invokevirtual 66	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   67: pop
    //   68: aload_1
    //   69: aload_2
    //   70: invokevirtual 66	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   73: pop
    //   74: aload_1
    //   75: ldc -128
    //   77: invokevirtual 66	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   80: pop
    //   81: new 130	java/lang/IllegalArgumentException
    //   84: dup
    //   85: aload_1
    //   86: invokevirtual 70	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   89: invokespecial 132	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   92: athrow
    //   93: astore_2
    //   94: new 59	java/lang/StringBuilder
    //   97: dup
    //   98: invokespecial 60	java/lang/StringBuilder:<init>	()V
    //   101: astore_2
    //   102: aload_2
    //   103: ldc -122
    //   105: invokevirtual 66	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   108: pop
    //   109: aload_2
    //   110: aload_1
    //   111: invokevirtual 137	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   114: pop
    //   115: aload_2
    //   116: ldc -117
    //   118: invokevirtual 66	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   121: pop
    //   122: new 130	java/lang/IllegalArgumentException
    //   125: dup
    //   126: aload_2
    //   127: invokevirtual 70	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   130: invokespecial 132	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   133: athrow
    //   134: astore_2
    //   135: new 59	java/lang/StringBuilder
    //   138: dup
    //   139: invokespecial 60	java/lang/StringBuilder:<init>	()V
    //   142: astore_2
    //   143: aload_2
    //   144: ldc -115
    //   146: invokevirtual 66	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   149: pop
    //   150: aload_2
    //   151: aload_1
    //   152: invokevirtual 137	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   155: pop
    //   156: aload_2
    //   157: ldc -113
    //   159: invokevirtual 66	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   162: pop
    //   163: new 130	java/lang/IllegalArgumentException
    //   166: dup
    //   167: aload_2
    //   168: invokevirtual 70	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   171: invokespecial 132	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   174: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	175	0	this	FilterFactory
    //   0	175	1	paramClass	Class
    //   0	175	2	paramString	String
    //   8	38	3	localObject	Object
    //   22	3	4	localConstructor	java.lang.reflect.Constructor
    // Exception table:
    //   from	to	target	type
    //   24	41	44	java/lang/Throwable
    //   9	24	93	java/lang/NoSuchMethodException
    //   0	7	134	java/lang/ClassCastException
  }
  
  public Filter createFilterByClassName(String paramString1, String paramString2)
  {
    if (mLogVerbose)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("Looking up class ");
      ((StringBuilder)localObject1).append(paramString1);
      Log.v("FilterFactory", ((StringBuilder)localObject1).toString());
    }
    Object localObject1 = null;
    Iterator localIterator = mPackages.iterator();
    Object localObject4;
    for (;;)
    {
      localObject4 = localObject1;
      if (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        localObject4 = localObject1;
        try
        {
          if (mLogVerbose)
          {
            localObject4 = localObject1;
            localObject5 = new java/lang/StringBuilder;
            localObject4 = localObject1;
            ((StringBuilder)localObject5).<init>();
            localObject4 = localObject1;
            ((StringBuilder)localObject5).append("Trying ");
            localObject4 = localObject1;
            ((StringBuilder)localObject5).append(str);
            localObject4 = localObject1;
            ((StringBuilder)localObject5).append(".");
            localObject4 = localObject1;
            ((StringBuilder)localObject5).append(paramString1);
            localObject4 = localObject1;
            Log.v("FilterFactory", ((StringBuilder)localObject5).toString());
          }
          localObject4 = localObject1;
          Object localObject5 = mClassLoaderGuard;
          localObject4 = localObject1;
          localObject4 = localObject1;
          try
          {
            ClassLoader localClassLoader = mCurrentClassLoader;
            localObject4 = localObject1;
            StringBuilder localStringBuilder = new java/lang/StringBuilder;
            localObject4 = localObject1;
            localStringBuilder.<init>();
            localObject4 = localObject1;
            localStringBuilder.append(str);
            localObject4 = localObject1;
            localStringBuilder.append(".");
            localObject4 = localObject1;
            localStringBuilder.append(paramString1);
            localObject4 = localObject1;
            localObject1 = localClassLoader.loadClass(localStringBuilder.toString());
            localObject4 = localObject1;
            if (localObject1 != null) {
              localObject4 = localObject1;
            } else {
              continue;
            }
          }
          finally {}
        }
        catch (ClassNotFoundException localClassNotFoundException)
        {
          Object localObject3 = localObject4;
        }
      }
    }
    if (localObject4 != null) {
      return createFilterByClass(localObject4, paramString2);
    }
    paramString2 = new StringBuilder();
    paramString2.append("Unknown filter class '");
    paramString2.append(paramString1);
    paramString2.append("'!");
    throw new IllegalArgumentException(paramString2.toString());
  }
}
