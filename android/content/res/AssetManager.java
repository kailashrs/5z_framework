package android.content.res;

import android.content.pm.ActivityInfo;
import android.os.ParcelFileDescriptor;
import android.util.ArraySet;
import android.util.SparseArray;
import android.util.TypedValue;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.Preconditions;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public final class AssetManager
  implements AutoCloseable
{
  public static final int ACCESS_BUFFER = 3;
  public static final int ACCESS_RANDOM = 1;
  public static final int ACCESS_STREAMING = 2;
  public static final int ACCESS_UNKNOWN = 0;
  private static final boolean DEBUG_REFS = false;
  private static final String FRAMEWORK_APK_PATH = "/system/framework/framework-res.apk";
  private static final String TAG = "AssetManager";
  private static final ApkAssets[] sEmptyApkAssets = new ApkAssets[0];
  private static final Object sSync = new Object();
  @GuardedBy("sSync")
  static AssetManager sSystem = null;
  @GuardedBy("sSync")
  private static ApkAssets[] sSystemApkAssets = new ApkAssets[0];
  @GuardedBy("sSync")
  private static ArraySet<ApkAssets> sSystemApkAssetsSet;
  @GuardedBy("this")
  private ApkAssets[] mApkAssets;
  @GuardedBy("this")
  private int mNumRefs = 1;
  @GuardedBy("this")
  private long mObject;
  @GuardedBy("this")
  private final long[] mOffsets = new long[2];
  @GuardedBy("this")
  private boolean mOpen = true;
  @GuardedBy("this")
  private HashMap<Long, RuntimeException> mRefStacks;
  @GuardedBy("this")
  private final TypedValue mValue = new TypedValue();
  
  public AssetManager()
  {
    synchronized (sSync)
    {
      createSystemAssetsInZygoteLocked();
      ApkAssets[] arrayOfApkAssets = sSystemApkAssets;
      mObject = nativeCreate();
      setApkAssets(arrayOfApkAssets, false);
      return;
    }
  }
  
  private AssetManager(boolean paramBoolean)
  {
    mObject = nativeCreate();
  }
  
  private int addAssetPathInternal(String paramString, boolean paramBoolean1, boolean paramBoolean2)
  {
    Preconditions.checkNotNull(paramString, "path");
    label182:
    try
    {
      ensureOpenLocked();
      int i = mApkAssets.length;
      for (int j = 0; j < i; j++) {
        if (mApkAssets[j].getAssetPath().equals(paramString)) {
          return j + 1;
        }
      }
      if (paramBoolean1) {
        try
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("/data/resource-cache/");
          localStringBuilder.append(paramString.substring(1).replace('/', '@'));
          localStringBuilder.append("@idmap");
          paramString = ApkAssets.loadOverlayFromPath(localStringBuilder.toString(), false);
        }
        catch (IOException paramString)
        {
          break label182;
        }
      } else {
        paramString = ApkAssets.loadFromPath(paramString, false, paramBoolean2);
      }
      mApkAssets = ((ApkAssets[])Arrays.copyOf(mApkAssets, i + 1));
      mApkAssets[i] = paramString;
      nativeSetApkAssets(mObject, mApkAssets, true);
      invalidateCachesLocked(-1);
      return i + 1;
    }
    finally {}
    return 0;
  }
  
  @GuardedBy("sSync")
  private static void createSystemAssetsInZygoteLocked()
  {
    if (sSystem != null) {
      return;
    }
    nativeVerifySystemIdmaps();
    try
    {
      Object localObject = new java/util/ArrayList;
      ((ArrayList)localObject).<init>();
      ((ArrayList)localObject).add(ApkAssets.loadFromPath("/system/framework/framework-res.apk", true));
      loadStaticRuntimeOverlays((ArrayList)localObject);
      ArraySet localArraySet = new android/util/ArraySet;
      localArraySet.<init>((Collection)localObject);
      sSystemApkAssetsSet = localArraySet;
      sSystemApkAssets = (ApkAssets[])((ArrayList)localObject).toArray(new ApkAssets[((ArrayList)localObject).size()]);
      localObject = new android/content/res/AssetManager;
      ((AssetManager)localObject).<init>(true);
      sSystem = (AssetManager)localObject;
      sSystem.setApkAssets(sSystemApkAssets, false);
      return;
    }
    catch (IOException localIOException)
    {
      throw new IllegalStateException("Failed to create system AssetManager", localIOException);
    }
  }
  
  @GuardedBy("this")
  private void decRefsLocked(long paramLong)
  {
    mNumRefs -= 1;
    if ((mNumRefs == 0) && (mObject != 0L))
    {
      nativeDestroy(mObject);
      mObject = 0L;
      mApkAssets = sEmptyApkAssets;
    }
  }
  
  @GuardedBy("this")
  private void ensureOpenLocked()
  {
    if (mOpen) {
      return;
    }
    throw new RuntimeException("AssetManager has been closed");
  }
  
  @GuardedBy("this")
  private void ensureValidLocked()
  {
    if (mObject != 0L) {
      return;
    }
    throw new RuntimeException("AssetManager has been destroyed");
  }
  
  public static native String getAssetAllocations();
  
  public static native int getGlobalAssetCount();
  
  public static native int getGlobalAssetManagerCount();
  
  public static AssetManager getSystem()
  {
    synchronized (sSync)
    {
      createSystemAssetsInZygoteLocked();
      AssetManager localAssetManager = sSystem;
      return localAssetManager;
    }
  }
  
  @GuardedBy("this")
  private void incRefsLocked(long paramLong)
  {
    mNumRefs += 1;
  }
  
  private void invalidateCachesLocked(int paramInt) {}
  
  /* Error */
  private static void loadStaticRuntimeOverlays(ArrayList<ApkAssets> paramArrayList)
    throws IOException
  {
    // Byte code:
    //   0: new 284	java/io/FileInputStream
    //   3: dup
    //   4: ldc_w 286
    //   7: invokespecial 287	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
    //   10: astore_1
    //   11: new 289	java/io/BufferedReader
    //   14: astore_2
    //   15: new 291	java/io/InputStreamReader
    //   18: astore_3
    //   19: aload_3
    //   20: aload_1
    //   21: invokespecial 294	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;)V
    //   24: aload_2
    //   25: aload_3
    //   26: invokespecial 297	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   29: aconst_null
    //   30: astore 4
    //   32: aload 4
    //   34: astore_3
    //   35: aload_1
    //   36: invokevirtual 301	java/io/FileInputStream:getChannel	()Ljava/nio/channels/FileChannel;
    //   39: lconst_0
    //   40: ldc2_w 302
    //   43: iconst_1
    //   44: invokevirtual 309	java/nio/channels/FileChannel:lock	(JJZ)Ljava/nio/channels/FileLock;
    //   47: astore 5
    //   49: aload_2
    //   50: invokevirtual 312	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   53: astore_3
    //   54: aload_3
    //   55: ifnull +24 -> 79
    //   58: aload_0
    //   59: aload_3
    //   60: ldc_w 314
    //   63: invokevirtual 318	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   66: iconst_1
    //   67: aaload
    //   68: iconst_1
    //   69: invokestatic 208	android/content/res/ApkAssets:loadOverlayFromPath	(Ljava/lang/String;Z)Landroid/content/res/ApkAssets;
    //   72: invokevirtual 234	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   75: pop
    //   76: goto -27 -> 49
    //   79: aload 5
    //   81: ifnull +12 -> 93
    //   84: aload 4
    //   86: astore_3
    //   87: aconst_null
    //   88: aload 5
    //   90: invokestatic 320	android/content/res/AssetManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   93: aconst_null
    //   94: aload_2
    //   95: invokestatic 320	android/content/res/AssetManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   98: aload_1
    //   99: invokestatic 326	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   102: return
    //   103: astore 6
    //   105: aconst_null
    //   106: astore_0
    //   107: goto +8 -> 115
    //   110: astore_0
    //   111: aload_0
    //   112: athrow
    //   113: astore 6
    //   115: aload 5
    //   117: ifnull +12 -> 129
    //   120: aload 4
    //   122: astore_3
    //   123: aload_0
    //   124: aload 5
    //   126: invokestatic 320	android/content/res/AssetManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   129: aload 4
    //   131: astore_3
    //   132: aload 6
    //   134: athrow
    //   135: astore_0
    //   136: goto +8 -> 144
    //   139: astore_0
    //   140: aload_0
    //   141: astore_3
    //   142: aload_0
    //   143: athrow
    //   144: aload_3
    //   145: aload_2
    //   146: invokestatic 320	android/content/res/AssetManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   149: aload_0
    //   150: athrow
    //   151: astore_0
    //   152: aload_1
    //   153: invokestatic 326	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   156: aload_0
    //   157: athrow
    //   158: astore_0
    //   159: ldc 32
    //   161: ldc_w 328
    //   164: invokestatic 334	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   167: pop
    //   168: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	169	0	paramArrayList	ArrayList<ApkAssets>
    //   10	143	1	localFileInputStream	java.io.FileInputStream
    //   14	132	2	localBufferedReader	java.io.BufferedReader
    //   18	127	3	localObject1	Object
    //   30	100	4	localObject2	Object
    //   47	78	5	localFileLock	java.nio.channels.FileLock
    //   103	1	6	localObject3	Object
    //   113	20	6	localObject4	Object
    // Exception table:
    //   from	to	target	type
    //   49	54	103	finally
    //   58	76	103	finally
    //   49	54	110	java/lang/Throwable
    //   58	76	110	java/lang/Throwable
    //   111	113	113	finally
    //   35	49	135	finally
    //   87	93	135	finally
    //   123	129	135	finally
    //   132	135	135	finally
    //   142	144	135	finally
    //   35	49	139	java/lang/Throwable
    //   87	93	139	java/lang/Throwable
    //   123	129	139	java/lang/Throwable
    //   132	135	139	java/lang/Throwable
    //   11	29	151	finally
    //   93	98	151	finally
    //   144	151	151	finally
    //   0	11	158	java/io/FileNotFoundException
  }
  
  private static native void nativeApplyStyle(long paramLong1, long paramLong2, int paramInt1, int paramInt2, long paramLong3, int[] paramArrayOfInt, long paramLong4, long paramLong5);
  
  private static native void nativeAssetDestroy(long paramLong);
  
  private static native long nativeAssetGetLength(long paramLong);
  
  private static native long nativeAssetGetRemainingLength(long paramLong);
  
  private static native int nativeAssetRead(long paramLong, byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  private static native int nativeAssetReadChar(long paramLong);
  
  private static native long nativeAssetSeek(long paramLong1, long paramLong2, int paramInt);
  
  private static native long nativeCreate();
  
  private static native void nativeDestroy(long paramLong);
  
  private static native SparseArray<String> nativeGetAssignedPackageIdentifiers(long paramLong);
  
  private static native String[] nativeGetLocales(long paramLong, boolean paramBoolean);
  
  private static native int nativeGetResourceArray(long paramLong, int paramInt, int[] paramArrayOfInt);
  
  private static native int nativeGetResourceArraySize(long paramLong, int paramInt);
  
  private static native int nativeGetResourceBagValue(long paramLong, int paramInt1, int paramInt2, TypedValue paramTypedValue);
  
  private static native String nativeGetResourceEntryName(long paramLong, int paramInt);
  
  private static native int nativeGetResourceIdentifier(long paramLong, String paramString1, String paramString2, String paramString3);
  
  private static native int[] nativeGetResourceIntArray(long paramLong, int paramInt);
  
  private static native String nativeGetResourceName(long paramLong, int paramInt);
  
  private static native String nativeGetResourcePackageName(long paramLong, int paramInt);
  
  private static native String[] nativeGetResourceStringArray(long paramLong, int paramInt);
  
  private static native int[] nativeGetResourceStringArrayInfo(long paramLong, int paramInt);
  
  private static native String nativeGetResourceTypeName(long paramLong, int paramInt);
  
  private static native int nativeGetResourceValue(long paramLong, int paramInt, short paramShort, TypedValue paramTypedValue, boolean paramBoolean);
  
  private static native Configuration[] nativeGetSizeConfigurations(long paramLong);
  
  private static native int[] nativeGetStyleAttributes(long paramLong, int paramInt);
  
  private static native String[] nativeList(long paramLong, String paramString)
    throws IOException;
  
  private static native long nativeOpenAsset(long paramLong, String paramString, int paramInt);
  
  private static native ParcelFileDescriptor nativeOpenAssetFd(long paramLong, String paramString, long[] paramArrayOfLong)
    throws IOException;
  
  private static native long nativeOpenNonAsset(long paramLong, int paramInt1, String paramString, int paramInt2);
  
  private static native ParcelFileDescriptor nativeOpenNonAssetFd(long paramLong, int paramInt, String paramString, long[] paramArrayOfLong)
    throws IOException;
  
  private static native long nativeOpenXmlAsset(long paramLong, int paramInt, String paramString);
  
  private static native boolean nativeResolveAttrs(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int[] paramArrayOfInt4);
  
  private static native boolean nativeRetrieveAttributes(long paramLong1, long paramLong2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3);
  
  private static native void nativeSetApkAssets(long paramLong, ApkAssets[] paramArrayOfApkAssets, boolean paramBoolean);
  
  private static native void nativeSetConfiguration(long paramLong, int paramInt1, int paramInt2, String paramString, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12, int paramInt13, int paramInt14, int paramInt15, int paramInt16, int paramInt17);
  
  private static native void nativeThemeApplyStyle(long paramLong1, long paramLong2, int paramInt, boolean paramBoolean);
  
  static native void nativeThemeClear(long paramLong);
  
  static native void nativeThemeCopy(long paramLong1, long paramLong2);
  
  private static native long nativeThemeCreate(long paramLong);
  
  private static native void nativeThemeDestroy(long paramLong);
  
  private static native void nativeThemeDump(long paramLong1, long paramLong2, int paramInt, String paramString1, String paramString2);
  
  private static native int nativeThemeGetAttributeValue(long paramLong1, long paramLong2, int paramInt, TypedValue paramTypedValue, boolean paramBoolean);
  
  static native int nativeThemeGetChangingConfigurations(long paramLong);
  
  private static native void nativeVerifySystemIdmaps();
  
  @Deprecated
  public int addAssetPath(String paramString)
  {
    return addAssetPathInternal(paramString, false, false);
  }
  
  @Deprecated
  public int addAssetPathAsSharedLibrary(String paramString)
  {
    return addAssetPathInternal(paramString, false, true);
  }
  
  @Deprecated
  public int addOverlayPath(String paramString)
  {
    return addAssetPathInternal(paramString, true, false);
  }
  
  void applyStyle(long paramLong1, int paramInt1, int paramInt2, XmlBlock.Parser paramParser, int[] paramArrayOfInt, long paramLong2, long paramLong3)
  {
    Preconditions.checkNotNull(paramArrayOfInt, "inAttrs");
    try
    {
      ensureValidLocked();
      long l1 = mObject;
      long l2;
      if (paramParser != null) {
        l2 = mParseState;
      } else {
        l2 = 0L;
      }
      nativeApplyStyle(l1, paramLong1, paramInt1, paramInt2, l2, paramArrayOfInt, paramLong2, paramLong3);
      return;
    }
    finally {}
  }
  
  void applyStyleToTheme(long paramLong, int paramInt, boolean paramBoolean)
  {
    try
    {
      ensureValidLocked();
      nativeThemeApplyStyle(mObject, paramLong, paramInt, paramBoolean);
      return;
    }
    finally {}
  }
  
  public void close()
  {
    try
    {
      if (!mOpen) {
        return;
      }
      mOpen = false;
      decRefsLocked(hashCode());
      return;
    }
    finally {}
  }
  
  long createTheme()
  {
    try
    {
      ensureValidLocked();
      long l = nativeThemeCreate(mObject);
      incRefsLocked(l);
      return l;
    }
    finally {}
  }
  
  void dumpTheme(long paramLong, int paramInt, String paramString1, String paramString2)
  {
    try
    {
      ensureValidLocked();
      nativeThemeDump(mObject, paramLong, paramInt, paramString1, paramString2);
      return;
    }
    finally {}
  }
  
  protected void finalize()
    throws Throwable
  {
    if (mObject != 0L) {
      nativeDestroy(mObject);
    }
  }
  
  public int findCookieForPath(String paramString)
  {
    Preconditions.checkNotNull(paramString, "path");
    try
    {
      ensureValidLocked();
      int i = mApkAssets.length;
      for (int j = 0; j < i; j++) {
        if (paramString.equals(mApkAssets[j].getAssetPath())) {
          return j + 1;
        }
      }
      return 0;
    }
    finally {}
  }
  
  public ApkAssets[] getApkAssets()
  {
    try
    {
      if (mOpen)
      {
        ApkAssets[] arrayOfApkAssets = mApkAssets;
        return arrayOfApkAssets;
      }
      return sEmptyApkAssets;
    }
    finally {}
  }
  
  public SparseArray<String> getAssignedPackageIdentifiers()
  {
    try
    {
      ensureValidLocked();
      SparseArray localSparseArray = nativeGetAssignedPackageIdentifiers(mObject);
      return localSparseArray;
    }
    finally {}
  }
  
  public String[] getLocales()
  {
    try
    {
      ensureValidLocked();
      String[] arrayOfString = nativeGetLocales(mObject, false);
      return arrayOfString;
    }
    finally {}
  }
  
  public String[] getNonSystemLocales()
  {
    try
    {
      ensureValidLocked();
      String[] arrayOfString = nativeGetLocales(mObject, true);
      return arrayOfString;
    }
    finally {}
  }
  
  CharSequence getPooledStringForCookie(int paramInt1, int paramInt2)
  {
    return getApkAssets()[(paramInt1 - 1)].getStringFromPool(paramInt2);
  }
  
  int getResourceArray(int paramInt, int[] paramArrayOfInt)
  {
    Preconditions.checkNotNull(paramArrayOfInt, "outData");
    try
    {
      ensureValidLocked();
      paramInt = nativeGetResourceArray(mObject, paramInt, paramArrayOfInt);
      return paramInt;
    }
    finally {}
  }
  
  int getResourceArraySize(int paramInt)
  {
    try
    {
      ensureValidLocked();
      paramInt = nativeGetResourceArraySize(mObject, paramInt);
      return paramInt;
    }
    finally {}
  }
  
  CharSequence getResourceBagText(int paramInt1, int paramInt2)
  {
    try
    {
      ensureValidLocked();
      Object localObject1 = mValue;
      paramInt1 = nativeGetResourceBagValue(mObject, paramInt1, paramInt2, (TypedValue)localObject1);
      if (paramInt1 <= 0) {
        return null;
      }
      changingConfigurations = ActivityInfo.activityInfoConfigNativeToJava(changingConfigurations);
      if (type == 3)
      {
        localObject1 = mApkAssets[(paramInt1 - 1)].getStringFromPool(data);
        return localObject1;
      }
      localObject1 = ((TypedValue)localObject1).coerceToString();
      return localObject1;
    }
    finally {}
  }
  
  String getResourceEntryName(int paramInt)
  {
    try
    {
      ensureValidLocked();
      String str = nativeGetResourceEntryName(mObject, paramInt);
      return str;
    }
    finally {}
  }
  
  int getResourceIdentifier(String paramString1, String paramString2, String paramString3)
  {
    try
    {
      ensureValidLocked();
      int i = nativeGetResourceIdentifier(mObject, paramString1, paramString2, paramString3);
      return i;
    }
    finally {}
  }
  
  int[] getResourceIntArray(int paramInt)
  {
    try
    {
      ensureValidLocked();
      int[] arrayOfInt = nativeGetResourceIntArray(mObject, paramInt);
      return arrayOfInt;
    }
    finally {}
  }
  
  String getResourceName(int paramInt)
  {
    try
    {
      ensureValidLocked();
      String str = nativeGetResourceName(mObject, paramInt);
      return str;
    }
    finally {}
  }
  
  String getResourcePackageName(int paramInt)
  {
    try
    {
      ensureValidLocked();
      String str = nativeGetResourcePackageName(mObject, paramInt);
      return str;
    }
    finally {}
  }
  
  String[] getResourceStringArray(int paramInt)
  {
    try
    {
      ensureValidLocked();
      String[] arrayOfString = nativeGetResourceStringArray(mObject, paramInt);
      return arrayOfString;
    }
    finally {}
  }
  
  CharSequence getResourceText(int paramInt)
  {
    try
    {
      Object localObject1 = mValue;
      if (getResourceValue(paramInt, 0, (TypedValue)localObject1, true))
      {
        localObject1 = ((TypedValue)localObject1).coerceToString();
        return localObject1;
      }
      return null;
    }
    finally {}
  }
  
  CharSequence[] getResourceTextArray(int paramInt)
  {
    try
    {
      ensureValidLocked();
      int[] arrayOfInt = nativeGetResourceStringArrayInfo(mObject, paramInt);
      if (arrayOfInt == null) {
        return null;
      }
      int i = arrayOfInt.length;
      CharSequence[] arrayOfCharSequence = new CharSequence[i / 2];
      int j = 0;
      for (paramInt = 0; j < i; paramInt++)
      {
        int k = arrayOfInt[j];
        int m = arrayOfInt[(j + 1)];
        CharSequence localCharSequence;
        if ((m >= 0) && (k > 0)) {
          localCharSequence = mApkAssets[(k - 1)].getStringFromPool(m);
        } else {
          localCharSequence = null;
        }
        arrayOfCharSequence[paramInt] = localCharSequence;
        j += 2;
      }
      return arrayOfCharSequence;
    }
    finally {}
  }
  
  String getResourceTypeName(int paramInt)
  {
    try
    {
      ensureValidLocked();
      String str = nativeGetResourceTypeName(mObject, paramInt);
      return str;
    }
    finally {}
  }
  
  boolean getResourceValue(int paramInt1, int paramInt2, TypedValue paramTypedValue, boolean paramBoolean)
  {
    Preconditions.checkNotNull(paramTypedValue, "outValue");
    try
    {
      ensureValidLocked();
      paramInt1 = nativeGetResourceValue(mObject, paramInt1, (short)paramInt2, paramTypedValue, paramBoolean);
      if (paramInt1 <= 0) {
        return false;
      }
      changingConfigurations = ActivityInfo.activityInfoConfigNativeToJava(changingConfigurations);
      if (type == 3) {
        string = mApkAssets[(paramInt1 - 1)].getStringFromPool(data);
      }
      return true;
    }
    finally {}
  }
  
  Configuration[] getSizeConfigurations()
  {
    try
    {
      ensureValidLocked();
      Configuration[] arrayOfConfiguration = nativeGetSizeConfigurations(mObject);
      return arrayOfConfiguration;
    }
    finally {}
  }
  
  int[] getStyleAttributes(int paramInt)
  {
    try
    {
      ensureValidLocked();
      int[] arrayOfInt = nativeGetStyleAttributes(mObject, paramInt);
      return arrayOfInt;
    }
    finally {}
  }
  
  boolean getThemeValue(long paramLong, int paramInt, TypedValue paramTypedValue, boolean paramBoolean)
  {
    Preconditions.checkNotNull(paramTypedValue, "outValue");
    try
    {
      ensureValidLocked();
      paramInt = nativeThemeGetAttributeValue(mObject, paramLong, paramInt, paramTypedValue, paramBoolean);
      if (paramInt <= 0) {
        return false;
      }
      changingConfigurations = ActivityInfo.activityInfoConfigNativeToJava(changingConfigurations);
      if (type == 3) {
        string = mApkAssets[(paramInt - 1)].getStringFromPool(data);
      }
      return true;
    }
    finally {}
  }
  
  public boolean isUpToDate()
  {
    ApkAssets[] arrayOfApkAssets = getApkAssets();
    int i = arrayOfApkAssets.length;
    for (int j = 0; j < i; j++) {
      if (!arrayOfApkAssets[j].isUpToDate()) {
        return false;
      }
    }
    return true;
  }
  
  public String[] list(String paramString)
    throws IOException
  {
    Preconditions.checkNotNull(paramString, "path");
    try
    {
      ensureValidLocked();
      paramString = nativeList(mObject, paramString);
      return paramString;
    }
    finally {}
  }
  
  public InputStream open(String paramString)
    throws IOException
  {
    return open(paramString, 2);
  }
  
  public InputStream open(String paramString, int paramInt)
    throws IOException
  {
    Preconditions.checkNotNull(paramString, "fileName");
    try
    {
      ensureOpenLocked();
      long l = nativeOpenAsset(mObject, paramString, paramInt);
      if (l != 0L)
      {
        paramString = new android/content/res/AssetManager$AssetInputStream;
        paramString.<init>(this, l, null);
        incRefsLocked(paramString.hashCode());
        return paramString;
      }
      FileNotFoundException localFileNotFoundException = new java/io/FileNotFoundException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Asset file: ");
      localStringBuilder.append(paramString);
      localFileNotFoundException.<init>(localStringBuilder.toString());
      throw localFileNotFoundException;
    }
    finally {}
  }
  
  public AssetFileDescriptor openFd(String paramString)
    throws IOException
  {
    Preconditions.checkNotNull(paramString, "fileName");
    try
    {
      ensureOpenLocked();
      Object localObject = nativeOpenAssetFd(mObject, paramString, mOffsets);
      if (localObject != null)
      {
        paramString = new android/content/res/AssetFileDescriptor;
        paramString.<init>((ParcelFileDescriptor)localObject, mOffsets[0], mOffsets[1]);
        return paramString;
      }
      FileNotFoundException localFileNotFoundException = new java/io/FileNotFoundException;
      localObject = new java/lang/StringBuilder;
      ((StringBuilder)localObject).<init>();
      ((StringBuilder)localObject).append("Asset file: ");
      ((StringBuilder)localObject).append(paramString);
      localFileNotFoundException.<init>(((StringBuilder)localObject).toString());
      throw localFileNotFoundException;
    }
    finally {}
  }
  
  public InputStream openNonAsset(int paramInt, String paramString)
    throws IOException
  {
    return openNonAsset(paramInt, paramString, 2);
  }
  
  public InputStream openNonAsset(int paramInt1, String paramString, int paramInt2)
    throws IOException
  {
    Preconditions.checkNotNull(paramString, "fileName");
    try
    {
      ensureOpenLocked();
      long l = nativeOpenNonAsset(mObject, paramInt1, paramString, paramInt2);
      if (l != 0L)
      {
        paramString = new android/content/res/AssetManager$AssetInputStream;
        paramString.<init>(this, l, null);
        incRefsLocked(paramString.hashCode());
        return paramString;
      }
      FileNotFoundException localFileNotFoundException = new java/io/FileNotFoundException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Asset absolute file: ");
      localStringBuilder.append(paramString);
      localFileNotFoundException.<init>(localStringBuilder.toString());
      throw localFileNotFoundException;
    }
    finally {}
  }
  
  public InputStream openNonAsset(String paramString)
    throws IOException
  {
    return openNonAsset(0, paramString, 2);
  }
  
  public InputStream openNonAsset(String paramString, int paramInt)
    throws IOException
  {
    return openNonAsset(0, paramString, paramInt);
  }
  
  public AssetFileDescriptor openNonAssetFd(int paramInt, String paramString)
    throws IOException
  {
    Preconditions.checkNotNull(paramString, "fileName");
    try
    {
      ensureOpenLocked();
      Object localObject = nativeOpenNonAssetFd(mObject, paramInt, paramString, mOffsets);
      if (localObject != null)
      {
        paramString = new android/content/res/AssetFileDescriptor;
        paramString.<init>((ParcelFileDescriptor)localObject, mOffsets[0], mOffsets[1]);
        return paramString;
      }
      FileNotFoundException localFileNotFoundException = new java/io/FileNotFoundException;
      localObject = new java/lang/StringBuilder;
      ((StringBuilder)localObject).<init>();
      ((StringBuilder)localObject).append("Asset absolute file: ");
      ((StringBuilder)localObject).append(paramString);
      localFileNotFoundException.<init>(((StringBuilder)localObject).toString());
      throw localFileNotFoundException;
    }
    finally {}
  }
  
  public AssetFileDescriptor openNonAssetFd(String paramString)
    throws IOException
  {
    return openNonAssetFd(0, paramString);
  }
  
  XmlBlock openXmlBlockAsset(int paramInt, String paramString)
    throws IOException
  {
    Preconditions.checkNotNull(paramString, "fileName");
    try
    {
      ensureOpenLocked();
      long l = nativeOpenXmlAsset(mObject, paramInt, paramString);
      if (l != 0L)
      {
        paramString = new android/content/res/XmlBlock;
        paramString.<init>(this, l);
        incRefsLocked(paramString.hashCode());
        return paramString;
      }
      FileNotFoundException localFileNotFoundException = new java/io/FileNotFoundException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Asset XML file: ");
      localStringBuilder.append(paramString);
      localFileNotFoundException.<init>(localStringBuilder.toString());
      throw localFileNotFoundException;
    }
    finally {}
  }
  
  XmlBlock openXmlBlockAsset(String paramString)
    throws IOException
  {
    return openXmlBlockAsset(0, paramString);
  }
  
  /* Error */
  public XmlResourceParser openXmlResourceParser(int paramInt, String paramString)
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: iload_1
    //   2: aload_2
    //   3: invokevirtual 599	android/content/res/AssetManager:openXmlBlockAsset	(ILjava/lang/String;)Landroid/content/res/XmlBlock;
    //   6: astore_3
    //   7: aconst_null
    //   8: astore 4
    //   10: aload 4
    //   12: astore_2
    //   13: aload_3
    //   14: invokevirtual 605	android/content/res/XmlBlock:newParser	()Landroid/content/res/XmlResourceParser;
    //   17: astore 5
    //   19: aload 5
    //   21: ifnull +15 -> 36
    //   24: aload_3
    //   25: ifnull +8 -> 33
    //   28: aconst_null
    //   29: aload_3
    //   30: invokestatic 320	android/content/res/AssetManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   33: aload 5
    //   35: areturn
    //   36: aload 4
    //   38: astore_2
    //   39: new 607	java/lang/AssertionError
    //   42: astore 5
    //   44: aload 4
    //   46: astore_2
    //   47: aload 5
    //   49: ldc_w 609
    //   52: invokespecial 612	java/lang/AssertionError:<init>	(Ljava/lang/Object;)V
    //   55: aload 4
    //   57: astore_2
    //   58: aload 5
    //   60: athrow
    //   61: astore 4
    //   63: goto +11 -> 74
    //   66: astore 4
    //   68: aload 4
    //   70: astore_2
    //   71: aload 4
    //   73: athrow
    //   74: aload_3
    //   75: ifnull +8 -> 83
    //   78: aload_2
    //   79: aload_3
    //   80: invokestatic 320	android/content/res/AssetManager:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   83: aload 4
    //   85: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	86	0	this	AssetManager
    //   0	86	1	paramInt	int
    //   0	86	2	paramString	String
    //   6	74	3	localXmlBlock	XmlBlock
    //   8	48	4	localObject1	Object
    //   61	1	4	localObject2	Object
    //   66	18	4	localThrowable	Throwable
    //   17	42	5	localObject3	Object
    // Exception table:
    //   from	to	target	type
    //   13	19	61	finally
    //   39	44	61	finally
    //   47	55	61	finally
    //   58	61	61	finally
    //   71	74	61	finally
    //   13	19	66	java/lang/Throwable
    //   39	44	66	java/lang/Throwable
    //   47	55	66	java/lang/Throwable
    //   58	61	66	java/lang/Throwable
  }
  
  public XmlResourceParser openXmlResourceParser(String paramString)
    throws IOException
  {
    return openXmlResourceParser(0, paramString);
  }
  
  void releaseTheme(long paramLong)
  {
    try
    {
      nativeThemeDestroy(paramLong);
      decRefsLocked(paramLong);
      return;
    }
    finally {}
  }
  
  boolean resolveAttrs(long paramLong, int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int[] paramArrayOfInt4)
  {
    Preconditions.checkNotNull(paramArrayOfInt2, "inAttrs");
    Preconditions.checkNotNull(paramArrayOfInt3, "outValues");
    Preconditions.checkNotNull(paramArrayOfInt4, "outIndices");
    try
    {
      ensureValidLocked();
      boolean bool = nativeResolveAttrs(mObject, paramLong, paramInt1, paramInt2, paramArrayOfInt1, paramArrayOfInt2, paramArrayOfInt3, paramArrayOfInt4);
      return bool;
    }
    finally {}
  }
  
  boolean retrieveAttributes(XmlBlock.Parser paramParser, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3)
  {
    Preconditions.checkNotNull(paramParser, "parser");
    Preconditions.checkNotNull(paramArrayOfInt1, "inAttrs");
    Preconditions.checkNotNull(paramArrayOfInt2, "outValues");
    Preconditions.checkNotNull(paramArrayOfInt3, "outIndices");
    try
    {
      ensureValidLocked();
      boolean bool = nativeRetrieveAttributes(mObject, mParseState, paramArrayOfInt1, paramArrayOfInt2, paramArrayOfInt3);
      return bool;
    }
    finally {}
  }
  
  public void setApkAssets(ApkAssets[] paramArrayOfApkAssets, boolean paramBoolean)
  {
    Preconditions.checkNotNull(paramArrayOfApkAssets, "apkAssets");
    ApkAssets[] arrayOfApkAssets = new ApkAssets[sSystemApkAssets.length + paramArrayOfApkAssets.length];
    Object localObject = sSystemApkAssets;
    int i = sSystemApkAssets.length;
    int j = 0;
    System.arraycopy(localObject, 0, arrayOfApkAssets, 0, i);
    int k = sSystemApkAssets.length;
    int m = paramArrayOfApkAssets.length;
    while (j < m)
    {
      localObject = paramArrayOfApkAssets[j];
      i = k;
      if (!sSystemApkAssetsSet.contains(localObject))
      {
        arrayOfApkAssets[k] = localObject;
        i = k + 1;
      }
      j++;
      k = i;
    }
    paramArrayOfApkAssets = arrayOfApkAssets;
    if (k != arrayOfApkAssets.length) {
      paramArrayOfApkAssets = (ApkAssets[])Arrays.copyOf(arrayOfApkAssets, k);
    }
    try
    {
      ensureOpenLocked();
      mApkAssets = paramArrayOfApkAssets;
      nativeSetApkAssets(mObject, mApkAssets, paramBoolean);
      if (paramBoolean) {
        invalidateCachesLocked(-1);
      }
      return;
    }
    finally {}
  }
  
  public void setConfiguration(int paramInt1, int paramInt2, String paramString, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10, int paramInt11, int paramInt12, int paramInt13, int paramInt14, int paramInt15, int paramInt16, int paramInt17)
  {
    try
    {
      ensureValidLocked();
      nativeSetConfiguration(mObject, paramInt1, paramInt2, paramString, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramInt9, paramInt10, paramInt11, paramInt12, paramInt13, paramInt14, paramInt15, paramInt16, paramInt17);
      return;
    }
    finally {}
  }
  
  void xmlBlockGone(int paramInt)
  {
    long l = paramInt;
    try
    {
      decRefsLocked(l);
      return;
    }
    finally {}
  }
  
  public final class AssetInputStream
    extends InputStream
  {
    private long mAssetNativePtr;
    private long mLength;
    private long mMarkPos;
    
    private AssetInputStream(long paramLong)
    {
      mAssetNativePtr = paramLong;
      mLength = AssetManager.nativeAssetGetLength(paramLong);
    }
    
    private void ensureOpen()
    {
      if (mAssetNativePtr != 0L) {
        return;
      }
      throw new IllegalStateException("AssetInputStream is closed");
    }
    
    public final int available()
      throws IOException
    {
      ensureOpen();
      long l = AssetManager.nativeAssetGetRemainingLength(mAssetNativePtr);
      int i;
      if (l > 2147483647L) {
        i = Integer.MAX_VALUE;
      } else {
        i = (int)l;
      }
      return i;
    }
    
    public final void close()
      throws IOException
    {
      if (mAssetNativePtr != 0L)
      {
        AssetManager.nativeAssetDestroy(mAssetNativePtr);
        mAssetNativePtr = 0L;
        synchronized (AssetManager.this)
        {
          AssetManager.this.decRefsLocked(hashCode());
        }
      }
    }
    
    protected void finalize()
      throws Throwable
    {
      close();
    }
    
    public final int getAssetInt()
    {
      throw new UnsupportedOperationException();
    }
    
    public final long getNativeAsset()
    {
      return mAssetNativePtr;
    }
    
    public final void mark(int paramInt)
    {
      ensureOpen();
      mMarkPos = AssetManager.nativeAssetSeek(mAssetNativePtr, 0L, 0);
    }
    
    public final boolean markSupported()
    {
      return true;
    }
    
    public final int read()
      throws IOException
    {
      ensureOpen();
      return AssetManager.nativeAssetReadChar(mAssetNativePtr);
    }
    
    public final int read(byte[] paramArrayOfByte)
      throws IOException
    {
      ensureOpen();
      Preconditions.checkNotNull(paramArrayOfByte, "b");
      return AssetManager.nativeAssetRead(mAssetNativePtr, paramArrayOfByte, 0, paramArrayOfByte.length);
    }
    
    public final int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
      throws IOException
    {
      ensureOpen();
      Preconditions.checkNotNull(paramArrayOfByte, "b");
      return AssetManager.nativeAssetRead(mAssetNativePtr, paramArrayOfByte, paramInt1, paramInt2);
    }
    
    public final void reset()
      throws IOException
    {
      ensureOpen();
      AssetManager.nativeAssetSeek(mAssetNativePtr, mMarkPos, -1);
    }
    
    public final long skip(long paramLong)
      throws IOException
    {
      ensureOpen();
      long l1 = AssetManager.nativeAssetSeek(mAssetNativePtr, 0L, 0);
      long l2 = paramLong;
      if (l1 + paramLong > mLength) {
        l2 = mLength - l1;
      }
      if (l2 > 0L) {
        AssetManager.nativeAssetSeek(mAssetNativePtr, l2, 0);
      }
      return l2;
    }
  }
  
  public static class Builder
  {
    private ArrayList<ApkAssets> mUserApkAssets = new ArrayList();
    
    public Builder() {}
    
    public Builder addApkAssets(ApkAssets paramApkAssets)
    {
      mUserApkAssets.add(paramApkAssets);
      return this;
    }
    
    public AssetManager build()
    {
      Object localObject = AssetManager.getSystem().getApkAssets();
      ApkAssets[] arrayOfApkAssets = new ApkAssets[localObject.length + mUserApkAssets.size()];
      System.arraycopy(localObject, 0, arrayOfApkAssets, 0, localObject.length);
      int i = mUserApkAssets.size();
      for (int j = 0; j < i; j++) {
        arrayOfApkAssets[(localObject.length + j)] = ((ApkAssets)mUserApkAssets.get(j));
      }
      localObject = new AssetManager(false, null);
      AssetManager.access$102((AssetManager)localObject, arrayOfApkAssets);
      AssetManager.nativeSetApkAssets(mObject, arrayOfApkAssets, false);
      return localObject;
    }
  }
}
