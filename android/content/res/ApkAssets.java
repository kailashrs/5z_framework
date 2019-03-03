package android.content.res;

import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.Preconditions;
import java.io.FileDescriptor;
import java.io.IOException;

public final class ApkAssets
{
  @GuardedBy("this")
  private final long mNativePtr;
  @GuardedBy("this")
  private StringBlock mStringBlock;
  
  private ApkAssets(FileDescriptor paramFileDescriptor, String paramString, boolean paramBoolean1, boolean paramBoolean2)
    throws IOException
  {
    Preconditions.checkNotNull(paramFileDescriptor, "fd");
    Preconditions.checkNotNull(paramString, "friendlyName");
    mNativePtr = nativeLoadFromFd(paramFileDescriptor, paramString, paramBoolean1, paramBoolean2);
    mStringBlock = new StringBlock(nativeGetStringBlock(mNativePtr), true);
  }
  
  private ApkAssets(String paramString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
    throws IOException
  {
    Preconditions.checkNotNull(paramString, "path");
    mNativePtr = nativeLoad(paramString, paramBoolean1, paramBoolean2, paramBoolean3);
    mStringBlock = new StringBlock(nativeGetStringBlock(mNativePtr), true);
  }
  
  public static ApkAssets loadFromFd(FileDescriptor paramFileDescriptor, String paramString, boolean paramBoolean1, boolean paramBoolean2)
    throws IOException
  {
    return new ApkAssets(paramFileDescriptor, paramString, paramBoolean1, paramBoolean2);
  }
  
  public static ApkAssets loadFromPath(String paramString)
    throws IOException
  {
    return new ApkAssets(paramString, false, false, false);
  }
  
  public static ApkAssets loadFromPath(String paramString, boolean paramBoolean)
    throws IOException
  {
    return new ApkAssets(paramString, paramBoolean, false, false);
  }
  
  public static ApkAssets loadFromPath(String paramString, boolean paramBoolean1, boolean paramBoolean2)
    throws IOException
  {
    return new ApkAssets(paramString, paramBoolean1, paramBoolean2, false);
  }
  
  public static ApkAssets loadOverlayFromPath(String paramString, boolean paramBoolean)
    throws IOException
  {
    return new ApkAssets(paramString, paramBoolean, false, true);
  }
  
  private static native void nativeDestroy(long paramLong);
  
  private static native String nativeGetAssetPath(long paramLong);
  
  private static native long nativeGetStringBlock(long paramLong);
  
  private static native boolean nativeIsUpToDate(long paramLong);
  
  private static native long nativeLoad(String paramString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
    throws IOException;
  
  private static native long nativeLoadFromFd(FileDescriptor paramFileDescriptor, String paramString, boolean paramBoolean1, boolean paramBoolean2)
    throws IOException;
  
  private static native long nativeOpenXml(long paramLong, String paramString)
    throws IOException;
  
  protected void finalize()
    throws Throwable
  {
    nativeDestroy(mNativePtr);
  }
  
  public String getAssetPath()
  {
    try
    {
      String str = nativeGetAssetPath(mNativePtr);
      return str;
    }
    finally {}
  }
  
  CharSequence getStringFromPool(int paramInt)
  {
    try
    {
      CharSequence localCharSequence = mStringBlock.get(paramInt);
      return localCharSequence;
    }
    finally {}
  }
  
  public boolean isUpToDate()
  {
    try
    {
      boolean bool = nativeIsUpToDate(mNativePtr);
      return bool;
    }
    finally {}
  }
  
  /* Error */
  public XmlResourceParser openXml(String paramString)
    throws IOException
  {
    // Byte code:
    //   0: aload_1
    //   1: ldc 95
    //   3: invokestatic 26	com/android/internal/util/Preconditions:checkNotNull	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   6: pop
    //   7: aload_0
    //   8: monitorenter
    //   9: aload_0
    //   10: getfield 34	android/content/res/ApkAssets:mNativePtr	J
    //   13: aload_1
    //   14: invokestatic 97	android/content/res/ApkAssets:nativeOpenXml	(JLjava/lang/String;)J
    //   17: lstore_2
    //   18: new 99	android/content/res/XmlBlock
    //   21: astore 4
    //   23: aconst_null
    //   24: astore 5
    //   26: aload 4
    //   28: aconst_null
    //   29: lload_2
    //   30: invokespecial 102	android/content/res/XmlBlock:<init>	(Landroid/content/res/AssetManager;J)V
    //   33: aload 5
    //   35: astore_1
    //   36: aload 4
    //   38: invokevirtual 106	android/content/res/XmlBlock:newParser	()Landroid/content/res/XmlResourceParser;
    //   41: astore 6
    //   43: aload 6
    //   45: ifnull +13 -> 58
    //   48: aload 4
    //   50: invokevirtual 109	android/content/res/XmlBlock:close	()V
    //   53: aload_0
    //   54: monitorexit
    //   55: aload 6
    //   57: areturn
    //   58: aload 5
    //   60: astore_1
    //   61: new 111	java/lang/AssertionError
    //   64: astore 6
    //   66: aload 5
    //   68: astore_1
    //   69: aload 6
    //   71: ldc 113
    //   73: invokespecial 116	java/lang/AssertionError:<init>	(Ljava/lang/Object;)V
    //   76: aload 5
    //   78: astore_1
    //   79: aload 6
    //   81: athrow
    //   82: astore 5
    //   84: goto +11 -> 95
    //   87: astore 5
    //   89: aload 5
    //   91: astore_1
    //   92: aload 5
    //   94: athrow
    //   95: aload_1
    //   96: ifnull +22 -> 118
    //   99: aload 4
    //   101: invokevirtual 109	android/content/res/XmlBlock:close	()V
    //   104: goto +19 -> 123
    //   107: astore 4
    //   109: aload_1
    //   110: aload 4
    //   112: invokevirtual 120	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   115: goto +8 -> 123
    //   118: aload 4
    //   120: invokevirtual 109	android/content/res/XmlBlock:close	()V
    //   123: aload 5
    //   125: athrow
    //   126: astore_1
    //   127: aload_0
    //   128: monitorexit
    //   129: aload_1
    //   130: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	131	0	this	ApkAssets
    //   0	131	1	paramString	String
    //   17	13	2	l	long
    //   21	79	4	localXmlBlock	XmlBlock
    //   107	12	4	localThrowable1	Throwable
    //   24	53	5	localObject1	Object
    //   82	1	5	localObject2	Object
    //   87	37	5	localThrowable2	Throwable
    //   41	39	6	localObject3	Object
    // Exception table:
    //   from	to	target	type
    //   36	43	82	finally
    //   61	66	82	finally
    //   69	76	82	finally
    //   79	82	82	finally
    //   92	95	82	finally
    //   36	43	87	java/lang/Throwable
    //   61	66	87	java/lang/Throwable
    //   69	76	87	java/lang/Throwable
    //   79	82	87	java/lang/Throwable
    //   99	104	107	java/lang/Throwable
    //   9	23	126	finally
    //   26	33	126	finally
    //   48	55	126	finally
    //   99	104	126	finally
    //   109	115	126	finally
    //   118	123	126	finally
    //   123	126	126	finally
    //   127	129	126	finally
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ApkAssets{path=");
    localStringBuilder.append(getAssetPath());
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
}
