package android.graphics;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.FontResourcesParser.FamilyResourceEntry;
import android.content.res.FontResourcesParser.FontFamilyFilesResourceEntry;
import android.content.res.FontResourcesParser.FontFileResourceEntry;
import android.content.res.FontResourcesParser.ProviderResourceEntry;
import android.graphics.fonts.FontVariationAxis;
import android.net.Uri;
import android.os.SELinux;
import android.provider.FontRequest;
import android.provider.FontsContract;
import android.provider.FontsContract.FontInfo;
import android.provider.Settings.Global;
import android.text.FontConfig;
import android.text.FontConfig.Alias;
import android.text.FontConfig.Family;
import android.text.FontConfig.Font;
import android.util.ArrayMap;
import android.util.Base64;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.LruCache;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
import dalvik.annotation.optimization.CriticalNative;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import libcore.util.NativeAllocationRegistry;
import org.xmlpull.v1.XmlPullParserException;

public class Typeface
{
  public static final int BOLD = 1;
  public static final int BOLD_ITALIC = 3;
  private static final boolean DEBUG = false;
  private static final boolean DEBUGMAIN = true;
  public static final Typeface DEFAULT;
  public static final Typeface DEFAULT_BOLD;
  private static final String DEFAULT_FAMILY = "sans-serif";
  private static final String DROIDSANS = "DroidSans.ttf";
  private static final String DROIDSANS_BOLD = "DroidSans-Bold.ttf";
  static Typeface DefaultFlipfont;
  private static final int[] EMPTY_AXES;
  private static final boolean FLIP_ALL_APPS = true;
  private static final String[] FlipFontAppList;
  private static String FlipFontPath;
  private static final String[] FontsLikeDefault;
  public static final int ITALIC = 2;
  public static final int MAX_WEIGHT = 1000;
  public static final Typeface MONOSPACE;
  private static final int MONOSPACE_INDEX = 3;
  public static final int NORMAL = 0;
  public static final int RESOLVE_BY_FONT_TABLE = -1;
  private static final int SANS_INDEX = 1;
  public static final Typeface SANS_SERIF;
  public static final Typeface SERIF;
  private static final int SERIF_INDEX = 2;
  private static final int STYLE_ITALIC = 1;
  public static final int STYLE_MASK = 3;
  private static final int STYLE_NORMAL = 0;
  private static String TAG = "Typeface";
  private static final String TAG_MONOTYPE = "Monotype";
  public static final Typeface WEATHERFONTREG;
  public static final Typeface WEATHERFONTREG2;
  public static final Typeface WEATHERFONTZENUI5;
  public static boolean isFlipFontUsed;
  private static boolean ismtFontsDirectoryExists;
  private static boolean lastAppInList;
  private static String lastAppNameString;
  static Typeface sDefaultTypeface;
  static Typeface[] sDefaults;
  private static final Object sDynamicCacheLock;
  @GuardedBy("sDynamicCacheLock")
  private static final LruCache<String, Typeface> sDynamicTypefaceCache;
  private static final NativeAllocationRegistry sRegistry = new NativeAllocationRegistry(Typeface.class.getClassLoader(), nativeGetReleaseFunc(), 64L);
  private static final Object sStyledCacheLock;
  @GuardedBy("sStyledCacheLock")
  private static final LongSparseArray<SparseArray<Typeface>> sStyledTypefaceCache = new LongSparseArray(3);
  static final Map<String, FontFamily[]> sSystemFallbackMap;
  static final Map<String, Typeface> sSystemFontMap;
  private static final Object sWeightCacheLock;
  @GuardedBy("sWeightCacheLock")
  private static final LongSparseArray<SparseArray<Typeface>> sWeightTypefaceCache;
  public boolean isLikeDefault = false;
  private int mStyle = 0;
  private int[] mSupportedAxes;
  private int mWeight = 0;
  public long native_instance;
  
  static
  {
    sStyledCacheLock = new Object();
    sWeightTypefaceCache = new LongSparseArray(3);
    sWeightCacheLock = new Object();
    sDynamicTypefaceCache = new LruCache(16);
    sDynamicCacheLock = new Object();
    EMPTY_AXES = new int[0];
    FlipFontPath = "";
    ismtFontsDirectoryExists = false;
    lastAppNameString = null;
    lastAppInList = false;
    isFlipFontUsed = false;
    FlipFontAppList = new String[] { "android", "com.android.*", "com.cooliris.media", "jp.co.omronsoft.openwnn", "com.monotypeimaging.*" };
    FontsLikeDefault = new String[] { "sans-serif", "sans-serif-condensed" };
    Object localObject = new ArrayMap();
    ArrayMap localArrayMap = new ArrayMap();
    buildSystemFallback("/system/etc/fonts.xml", "/system/fonts/", (ArrayMap)localObject, localArrayMap);
    sSystemFontMap = Collections.unmodifiableMap((Map)localObject);
    sSystemFallbackMap = Collections.unmodifiableMap(localArrayMap);
    setDefault((Typeface)sSystemFontMap.get("sans-serif"));
    localObject = (String)null;
    DEFAULT = create((String)localObject, 0);
    DEFAULT_BOLD = create((String)localObject, 1);
    SANS_SERIF = create("sans-serif", 0);
    SERIF = create("serif", 0);
    MONOSPACE = create("monospace", 0);
    WEATHERFONTREG = create("weatherfontReg", 0);
    WEATHERFONTREG2 = create("weatherfontReg2", 0);
    WEATHERFONTZENUI5 = create("weatherfontZenUI5", 0);
    sDefaults = new Typeface[] { DEFAULT, DEFAULT_BOLD, create((String)localObject, 2), create((String)localObject, 3) };
  }
  
  private Typeface(long paramLong)
  {
    if (paramLong != 0L)
    {
      native_instance = paramLong;
      sRegistry.registerNativeAllocation(this, native_instance);
      mStyle = nativeGetStyle(paramLong);
      mWeight = nativeGetWeight(paramLong);
      return;
    }
    throw new RuntimeException("native typeface cannot be made");
  }
  
  public static void SetAppTypeFace(Context paramContext, String paramString)
  {
    if (!ismtFontsDirectoryExists)
    {
      makeMtFontsDirectory();
      ismtFontsDirectoryExists = true;
    }
    if (isAppInFlipList(paramString))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("SetAppTypeFace- try to flip, app = ");
      localStringBuilder.append(paramString);
      Log.v("Monotype", localStringBuilder.toString());
      SetFlipFonts(paramContext);
    }
  }
  
  private static void SetFlipFonts(Context paramContext)
  {
    Object localObject = "";
    String str = getFontPathFlipFont(paramContext, 1);
    if (str.endsWith("default"))
    {
      isFlipFontUsed = false;
      paramContext = (Context)localObject;
    }
    else
    {
      isFlipFontUsed = true;
      paramContext = new StringBuilder();
      paramContext.append(str);
      paramContext.append("/");
      paramContext.append("DroidSans-Bold.ttf");
      paramContext = paramContext.toString();
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(str);
      ((StringBuilder)localObject).append("/");
      ((StringBuilder)localObject).append("DroidSans.ttf");
      str = ((StringBuilder)localObject).toString();
    }
    if (str.equals(FlipFontPath)) {
      return;
    }
    FlipFontPath = str;
    long l = DEFAULTnative_instance;
    if ((isFlipFontUsed) && (!str.isEmpty()))
    {
      try
      {
        DEFAULTnative_instance = createFromFilenative_instance;
      }
      catch (RuntimeException localRuntimeException)
      {
        DEFAULTnative_instance = create0native_instance;
      }
      if (DEFAULTnative_instance == 0L) {
        DEFAULTnative_instance = create0native_instance;
      }
    }
    else
    {
      nativeSetDefault(DefaultFlipfontnative_instance);
      DEFAULTnative_instance = nativeCreateFromTypeface(0L, 0);
    }
    DEFAULTmStyle = nativeGetStyle(DEFAULTnative_instance);
    l = DEFAULT_BOLDnative_instance;
    if ((isFlipFontUsed) && (!paramContext.isEmpty()))
    {
      try
      {
        DEFAULT_BOLDnative_instance = createFromFilenative_instance;
      }
      catch (RuntimeException paramContext)
      {
        DEFAULT_BOLDnative_instance = create1native_instance;
      }
      if (DEFAULT_BOLDnative_instance == 0L) {
        DEFAULT_BOLDnative_instance = create1native_instance;
      }
    }
    else
    {
      DEFAULT_BOLDnative_instance = nativeCreateFromTypeface(0L, 1);
    }
    DEFAULT_BOLDmStyle = nativeGetStyle(DEFAULT_BOLDnative_instance);
    l = sDefaults0native_instance;
    sDefaults0native_instance = nativeCreateFromTypefaceWithExactStyle(DEFAULTnative_instance, 400, false);
    sDefaults0mStyle = nativeGetStyle(sDefaults0native_instance);
    l = sDefaults1native_instance;
    sDefaults1native_instance = nativeCreateFromTypefaceWithExactStyle(DEFAULT_BOLDnative_instance, 700, false);
    sDefaults1mStyle = nativeGetStyle(sDefaults1native_instance);
    l = sDefaults2native_instance;
    sDefaults2native_instance = nativeCreateFromTypefaceWithExactStyle(DEFAULTnative_instance, 400, true);
    sDefaults2mStyle = nativeGetStyle(sDefaults2native_instance);
    l = sDefaults3native_instance;
    sDefaults3native_instance = nativeCreateFromTypefaceWithExactStyle(DEFAULT_BOLDnative_instance, 700, true);
    sDefaults3mStyle = nativeGetStyle(sDefaults3native_instance);
    if (isFlipFontUsed) {
      nativeSetDefault(sDefaultTypefacenative_instance);
    }
  }
  
  @VisibleForTesting
  public static void buildSystemFallback(String paramString1, String paramString2, ArrayMap<String, Typeface> paramArrayMap, ArrayMap<String, FontFamily[]> paramArrayMap1)
  {
    try
    {
      Object localObject1 = new java/io/FileInputStream;
      ((FileInputStream)localObject1).<init>(paramString1);
      localObject1 = FontListParser.parse((InputStream)localObject1);
      Object localObject2 = new java/util/HashMap;
      ((HashMap)localObject2).<init>();
      Object localObject3 = ((FontConfig)localObject1).getFamilies();
      Object localObject4 = new android/util/ArrayMap;
      ((ArrayMap)localObject4).<init>();
      int i = localObject3.length;
      Object localObject5;
      for (int j = 0; j < i; j++)
      {
        FontFamily localFontFamily = localObject3[j];
        localObject5 = localFontFamily.getName();
        if (localObject5 != null)
        {
          localFontFamily = createFontFamily(localFontFamily.getName(), Arrays.asList(localFontFamily.getFonts()), localFontFamily.getLanguages(), localFontFamily.getVariant(), (Map)localObject2, paramString2);
          if (localFontFamily != null)
          {
            ArrayList localArrayList = new java/util/ArrayList;
            localArrayList.<init>();
            localArrayList.add(localFontFamily);
            ((ArrayMap)localObject4).put(localObject5, localArrayList);
          }
        }
      }
      for (j = 0; j < localObject3.length; j++)
      {
        localObject5 = localObject3[j];
        if ((j != 0) && (((FontConfig.Family)localObject5).getName() != null)) {
          continue;
        }
        pushFamilyToFallback((FontConfig.Family)localObject5, (ArrayMap)localObject4, (Map)localObject2, paramString2);
      }
      j = 0;
      for (;;)
      {
        if (j < ((ArrayMap)localObject4).size())
        {
          paramString2 = (String)((ArrayMap)localObject4).keyAt(j);
          localObject3 = (List)((ArrayMap)localObject4).valueAt(j);
          localObject5 = (FontFamily[])((List)localObject3).toArray(new FontFamily[((List)localObject3).size()]);
        }
        try
        {
          paramArrayMap1.put(paramString2, localObject5);
          localObject3 = new long[localObject5.length];
          for (i = 0; i < localObject5.length; i++) {
            localObject3[i] = mNativePtr;
          }
          localObject5 = new android/graphics/Typeface;
          ((Typeface)localObject5).<init>(nativeCreateFromArray((long[])localObject3, -1, -1));
          for (i = 0; i < FontsLikeDefault.length; i++) {
            if (paramString2.equals(FontsLikeDefault[i]))
            {
              isLikeDefault = true;
              break;
            }
          }
          paramArrayMap.put(paramString2, localObject5);
          j++;
        }
        catch (XmlPullParserException paramString2)
        {
          int k;
          break label552;
        }
        catch (IOException paramString2)
        {
          break label592;
        }
        catch (FileNotFoundException paramString2)
        {
          break label632;
        }
        catch (RuntimeException paramString1)
        {
          break label672;
        }
      }
      localObject4 = ((FontConfig)localObject1).getAliases();
      k = localObject4.length;
      j = 0;
      paramString2 = (String)localObject2;
      paramArrayMap1 = (ArrayMap<String, FontFamily[]>)localObject1;
      while (j < k)
      {
        localObject3 = localObject4[j];
        localObject2 = (Typeface)paramArrayMap.get(((FontConfig.Alias)localObject3).getToName());
        localObject1 = localObject2;
        i = ((FontConfig.Alias)localObject3).getWeight();
        if (i != 400)
        {
          localObject1 = new android/graphics/Typeface;
          ((Typeface)localObject1).<init>(nativeCreateWeightAlias(native_instance, i));
          for (i = 0; i < FontsLikeDefault.length; i++) {
            if (((FontConfig.Alias)localObject3).getToName().equals(FontsLikeDefault[i]))
            {
              isLikeDefault = true;
              break;
            }
          }
        }
        paramArrayMap.put(((FontConfig.Alias)localObject3).getName(), localObject1);
        j++;
        continue;
        label552:
        label592:
        label632:
        Log.w(TAG, "Didn't create default family (most likely, non-Minikin build)", paramString1);
      }
    }
    catch (XmlPullParserException paramString2)
    {
      paramArrayMap = TAG;
      paramArrayMap1 = new StringBuilder();
      paramArrayMap1.append("XML parse exception for ");
      paramArrayMap1.append(paramString1);
      Log.e(paramArrayMap, paramArrayMap1.toString(), paramString2);
    }
    catch (IOException paramString2)
    {
      paramArrayMap = TAG;
      paramArrayMap1 = new StringBuilder();
      paramArrayMap1.append("Error reading ");
      paramArrayMap1.append(paramString1);
      Log.e(paramArrayMap, paramArrayMap1.toString(), paramString2);
    }
    catch (FileNotFoundException paramString2)
    {
      paramArrayMap = TAG;
      paramArrayMap1 = new StringBuilder();
      paramArrayMap1.append("Error opening ");
      paramArrayMap1.append(paramString1);
      Log.e(paramArrayMap, paramArrayMap1.toString(), paramString2);
    }
    catch (RuntimeException paramString1) {}
    label672:
  }
  
  public static Typeface create(Typeface paramTypeface, int paramInt)
  {
    int i = paramInt;
    if ((paramInt & 0xFFFFFFFC) != 0) {
      i = 0;
    }
    Typeface localTypeface1 = paramTypeface;
    if (paramTypeface == null) {
      localTypeface1 = sDefaultTypeface;
    }
    if (mStyle == i) {
      return localTypeface1;
    }
    if ((isFlipFontUsed) && (isLikeDefault)) {
      return sDefaults[i];
    }
    long l = native_instance;
    synchronized (sStyledCacheLock)
    {
      paramTypeface = (SparseArray)sStyledTypefaceCache.get(l);
      if (paramTypeface == null)
      {
        paramTypeface = new android/util/SparseArray;
        paramTypeface.<init>(4);
        sStyledTypefaceCache.put(l, paramTypeface);
      }
      else
      {
        localTypeface2 = (Typeface)paramTypeface.get(i);
        if (localTypeface2 != null) {
          return localTypeface2;
        }
      }
      Typeface localTypeface2 = new android/graphics/Typeface;
      localTypeface2.<init>(nativeCreateFromTypeface(l, i));
      if ((localTypeface1 != null) && (mStyle == i)) {
        isLikeDefault = isLikeDefault;
      }
      paramTypeface.put(i, localTypeface2);
      return localTypeface2;
    }
  }
  
  public static Typeface create(Typeface paramTypeface, int paramInt, boolean paramBoolean)
  {
    Preconditions.checkArgumentInRange(paramInt, 0, 1000, "weight");
    Typeface localTypeface = paramTypeface;
    if (paramTypeface == null) {
      localTypeface = sDefaultTypeface;
    }
    return createWeightStyle(localTypeface, paramInt, paramBoolean);
  }
  
  public static Typeface create(String paramString, int paramInt)
  {
    return create((Typeface)sSystemFontMap.get(paramString), paramInt);
  }
  
  private static FontFamily createFontFamily(String paramString1, List<FontConfig.Font> paramList, String[] paramArrayOfString, int paramInt, Map<String, ByteBuffer> paramMap, String paramString2)
  {
    FontFamily localFontFamily = new FontFamily(paramArrayOfString, paramInt);
    for (paramInt = 0; paramInt < paramList.size(); paramInt++)
    {
      FontConfig.Font localFont = (FontConfig.Font)paramList.get(paramInt);
      Object localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(paramString2);
      ((StringBuilder)localObject1).append(localFont.getFontName());
      String str = ((StringBuilder)localObject1).toString();
      Object localObject2 = (ByteBuffer)paramMap.get(str);
      localObject1 = localObject2;
      if (localObject2 == null)
      {
        if (!paramMap.containsKey(str))
        {
          localObject2 = mmap(str);
          paramMap.put(str, localObject2);
          localObject1 = localObject2;
          if (localObject2 == null) {}
        }
      }
      else if (!localFontFamily.addFontFromBuffer((ByteBuffer)localObject1, localFont.getTtcIndex(), localFont.getAxes(), localFont.getWeight(), localFont.isItalic()))
      {
        localObject1 = TAG;
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("Error creating font ");
        ((StringBuilder)localObject2).append(str);
        ((StringBuilder)localObject2).append("#");
        ((StringBuilder)localObject2).append(localFont.getTtcIndex());
        Log.e((String)localObject1, ((StringBuilder)localObject2).toString());
      }
    }
    if (!localFontFamily.freeze())
    {
      paramMap = TAG;
      paramList = new StringBuilder();
      paramList.append("Unable to load Family: ");
      paramList.append(paramString1);
      paramList.append(" : ");
      paramList.append(Arrays.toString(paramArrayOfString));
      Log.e(paramMap, paramList.toString());
      return null;
    }
    return localFontFamily;
  }
  
  public static Typeface createFromAsset(AssetManager paramAssetManager, String paramString)
  {
    Preconditions.checkNotNull(paramString);
    Preconditions.checkNotNull(paramAssetManager);
    Typeface localTypeface = new Builder(paramAssetManager, paramString).build();
    if (localTypeface != null) {
      return localTypeface;
    }
    try
    {
      paramAssetManager = paramAssetManager.open(paramString);
      if (paramAssetManager != null) {
        paramAssetManager.close();
      }
      return DEFAULT;
    }
    catch (IOException paramAssetManager)
    {
      paramAssetManager = new StringBuilder();
      paramAssetManager.append("Font asset not found ");
      paramAssetManager.append(paramString);
      throw new RuntimeException(paramAssetManager.toString());
    }
  }
  
  private static Typeface createFromFamilies(FontFamily[] paramArrayOfFontFamily)
  {
    long[] arrayOfLong = new long[paramArrayOfFontFamily.length];
    for (int i = 0; i < paramArrayOfFontFamily.length; i++) {
      arrayOfLong[i] = mNativePtr;
    }
    return new Typeface(nativeCreateFromArray(arrayOfLong, -1, -1));
  }
  
  private static Typeface createFromFamiliesWithDefault(FontFamily[] paramArrayOfFontFamily, int paramInt1, int paramInt2)
  {
    return createFromFamiliesWithDefault(paramArrayOfFontFamily, "sans-serif", paramInt1, paramInt2);
  }
  
  private static Typeface createFromFamiliesWithDefault(FontFamily[] paramArrayOfFontFamily, String paramString, int paramInt1, int paramInt2)
  {
    Object localObject = (FontFamily[])sSystemFallbackMap.get(paramString);
    paramString = (String)localObject;
    if (localObject == null) {
      paramString = (FontFamily[])sSystemFallbackMap.get("sans-serif");
    }
    localObject = new long[paramArrayOfFontFamily.length + paramString.length];
    int i = 0;
    for (int j = 0; j < paramArrayOfFontFamily.length; j++) {
      localObject[j] = mNativePtr;
    }
    for (j = i; j < paramString.length; j++) {
      localObject[(paramArrayOfFontFamily.length + j)] = mNativePtr;
    }
    return new Typeface(nativeCreateFromArray((long[])localObject, paramInt1, paramInt2));
  }
  
  public static Typeface createFromFile(File paramFile)
  {
    Object localObject = new Builder(paramFile).build();
    if (localObject != null) {
      return localObject;
    }
    if (paramFile.exists()) {
      return DEFAULT;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Font asset not found ");
    ((StringBuilder)localObject).append(paramFile.getAbsolutePath());
    throw new RuntimeException(((StringBuilder)localObject).toString());
  }
  
  public static Typeface createFromFile(String paramString)
  {
    Preconditions.checkNotNull(paramString);
    return createFromFile(new File(paramString));
  }
  
  public static Typeface createFromResources(AssetManager paramAssetManager, String paramString, int paramInt)
  {
    synchronized (sDynamicCacheLock)
    {
      String str = Builder.createAssetUid(paramAssetManager, paramString, 0, null, -1, -1, "sans-serif");
      Object localObject2 = (Typeface)sDynamicTypefaceCache.get(str);
      if (localObject2 != null) {
        return localObject2;
      }
      localObject2 = new android/graphics/FontFamily;
      ((FontFamily)localObject2).<init>();
      if (((FontFamily)localObject2).addFontFromAssetManager(paramAssetManager, paramString, paramInt, false, 0, -1, -1, null))
      {
        if (!((FontFamily)localObject2).freeze()) {
          return null;
        }
        paramAssetManager = createFromFamiliesWithDefault(new FontFamily[] { localObject2 }, "sans-serif", -1, -1);
        sDynamicTypefaceCache.put(str, paramAssetManager);
        return paramAssetManager;
      }
      return null;
    }
  }
  
  public static Typeface createFromResources(FontResourcesParser.FamilyResourceEntry arg0, AssetManager paramAssetManager, String paramString)
  {
    Object localObject2;
    int i;
    int j;
    if ((??? instanceof FontResourcesParser.ProviderResourceEntry))
    {
      localObject1 = (FontResourcesParser.ProviderResourceEntry)???;
      paramAssetManager = ((FontResourcesParser.ProviderResourceEntry)localObject1).getCerts();
      localObject2 = new ArrayList();
      if (paramAssetManager != null) {
        for (i = 0; i < paramAssetManager.size(); i++)
        {
          paramString = (List)paramAssetManager.get(i);
          ??? = new ArrayList();
          for (j = 0; j < paramString.size(); j++) {
            ???.add(Base64.decode((String)paramString.get(j), 0));
          }
          ((List)localObject2).add(???);
        }
      }
      ??? = FontsContract.getFontSync(new FontRequest(((FontResourcesParser.ProviderResourceEntry)localObject1).getAuthority(), ((FontResourcesParser.ProviderResourceEntry)localObject1).getPackage(), ((FontResourcesParser.ProviderResourceEntry)localObject1).getQuery(), (List)localObject2));
      if (??? == null) {
        ??? = DEFAULT;
      }
      return ???;
    }
    Object localObject1 = findFromCache(paramAssetManager, paramString);
    if (localObject1 != null) {
      return localObject1;
    }
    localObject1 = (FontResourcesParser.FontFamilyFilesResourceEntry)???;
    ??? = new FontFamily();
    for (localObject1 : ((FontResourcesParser.FontFamilyFilesResourceEntry)localObject1).getEntries()) {
      if (!???.addFontFromAssetManager(paramAssetManager, ((FontResourcesParser.FontFileResourceEntry)localObject1).getFileName(), 0, false, ((FontResourcesParser.FontFileResourceEntry)localObject1).getTtcIndex(), ((FontResourcesParser.FontFileResourceEntry)localObject1).getWeight(), ((FontResourcesParser.FontFileResourceEntry)localObject1).getItalic(), FontVariationAxis.fromFontVariationSettings(((FontResourcesParser.FontFileResourceEntry)localObject1).getVariationSettings()))) {
        return null;
      }
    }
    if (!???.freeze()) {
      return null;
    }
    localObject1 = createFromFamiliesWithDefault(new FontFamily[] { ??? }, "sans-serif", -1, -1);
    synchronized (sDynamicCacheLock)
    {
      paramAssetManager = Builder.createAssetUid(paramAssetManager, paramString, 0, null, -1, -1, "sans-serif");
      sDynamicTypefaceCache.put(paramAssetManager, localObject1);
      return localObject1;
    }
  }
  
  public static Typeface createFromTypefaceWithVariation(Typeface paramTypeface, List<FontVariationAxis> paramList)
  {
    long l;
    if (paramTypeface == null) {
      l = 0L;
    } else {
      l = native_instance;
    }
    return new Typeface(nativeCreateFromTypefaceWithVariation(l, paramList));
  }
  
  private static String createProviderUid(String paramString1, String paramString2)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("provider:");
    localStringBuilder.append(paramString1);
    localStringBuilder.append("-");
    localStringBuilder.append(paramString2);
    return localStringBuilder.toString();
  }
  
  private static Typeface createWeightStyle(Typeface paramTypeface, int paramInt, boolean paramBoolean)
  {
    int i = paramInt << 1 | paramBoolean;
    synchronized (sWeightCacheLock)
    {
      SparseArray localSparseArray = (SparseArray)sWeightTypefaceCache.get(native_instance);
      if (localSparseArray == null)
      {
        localSparseArray = new android/util/SparseArray;
        localSparseArray.<init>(4);
        sWeightTypefaceCache.put(native_instance, localSparseArray);
      }
      else
      {
        localTypeface = (Typeface)localSparseArray.get(i);
        if (localTypeface != null) {
          return localTypeface;
        }
      }
      Typeface localTypeface = new android/graphics/Typeface;
      localTypeface.<init>(nativeCreateFromTypefaceWithExactStyle(native_instance, paramInt, paramBoolean));
      localSparseArray.put(i, localTypeface);
      return localTypeface;
    }
  }
  
  public static Typeface defaultFromStyle(int paramInt)
  {
    int i;
    if (paramInt >= 0)
    {
      i = paramInt;
      if (paramInt <= 3) {}
    }
    else
    {
      i = 0;
    }
    return sDefaults[i];
  }
  
  public static Typeface findFromCache(AssetManager paramAssetManager, String paramString)
  {
    synchronized (sDynamicCacheLock)
    {
      paramAssetManager = Builder.createAssetUid(paramAssetManager, paramString, 0, null, -1, -1, "sans-serif");
      paramAssetManager = (Typeface)sDynamicTypefaceCache.get(paramAssetManager);
      if (paramAssetManager != null) {
        return paramAssetManager;
      }
      return null;
    }
  }
  
  public static String getFontNameFlipFont(Context paramContext, int paramInt)
  {
    paramContext = getFullFlipFont(paramContext, paramInt).split("#");
    if (paramContext.length < 2)
    {
      if (paramContext[0].endsWith("default")) {
        return "default";
      }
      return null;
    }
    return paramContext[1];
  }
  
  public static String getFontPathFlipFont(Context paramContext, int paramInt)
  {
    return getFullFlipFont(paramContext, paramInt).split("#")[0];
  }
  
  private static String getFullFlipFont(Context paramContext, int paramInt)
  {
    Object localObject1 = "default#default";
    switch (paramInt)
    {
    default: 
      paramContext = (Context)localObject1;
      break;
    case 3: 
      paramContext = (Context)localObject1;
      break;
    case 2: 
      paramContext = (Context)localObject1;
      break;
    case 1: 
      try
      {
        String str = Settings.Global.getString(paramContext.getContentResolver(), "flipfont_settings_path");
        localObject2 = Settings.Global.getString(paramContext.getContentResolver(), "flipfont_settings_name");
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        if (str == null) {
          paramContext = "default";
        } else {
          paramContext = str;
        }
        localStringBuilder.append(paramContext);
        localStringBuilder.append("#");
        if (localObject2 == null) {
          paramContext = "default";
        } else {
          paramContext = (Context)localObject2;
        }
        localStringBuilder.append(paramContext);
        paramContext = localStringBuilder.toString();
      }
      catch (SecurityException paramContext)
      {
        Object localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("getFullFlipFont has SecurityException while Settings.Global.getString:");
        ((StringBuilder)localObject2).append(paramContext);
        Log.v("Monotype", ((StringBuilder)localObject2).toString());
        paramContext = (Context)localObject1;
      }
      catch (NullPointerException localNullPointerException)
      {
        for (;;)
        {
          paramContext = new StringBuilder();
          paramContext.append("getFullFlipFont has NullPointerException while Settings.Global.getString:");
          paramContext.append(localNullPointerException);
          Log.v("Monotype", paramContext.toString());
          paramContext = (Context)localObject1;
        }
      }
    }
    localObject1 = paramContext;
    if (paramContext == null) {
      localObject1 = "default#default";
    }
    paramContext = new StringBuilder();
    paramContext.append("    Typeface getFontPathFlipFont - systemFont = ");
    paramContext.append((String)localObject1);
    Log.v("Monotype", paramContext.toString());
    return localObject1;
  }
  
  private static boolean isAppInFlipList(String paramString)
  {
    return true;
  }
  
  private static String[] loadAppList()
  {
    return FlipFontAppList;
  }
  
  private static void makeMtFontsDirectory()
  {
    File localFile = new File("/data/mt_fonts/");
    if (!localFile.exists())
    {
      localFile.mkdir();
      if (!SELinux.restorecon(localFile)) {
        Log.e("Monotype", "Restorecons failed. Directory will have wrong label.");
      }
      localFile.setExecutable(true, false);
      localFile.setReadable(true, false);
      localFile.setWritable(true, false);
      Log.v("Monotype", "makeMtFontsDirectory()");
    }
  }
  
  /* Error */
  private static ByteBuffer mmap(String paramString)
  {
    // Byte code:
    //   0: new 363	java/io/FileInputStream
    //   3: astore_1
    //   4: aload_1
    //   5: aload_0
    //   6: invokespecial 364	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
    //   9: aload_1
    //   10: invokevirtual 768	java/io/FileInputStream:getChannel	()Ljava/nio/channels/FileChannel;
    //   13: astore_2
    //   14: aload_2
    //   15: invokevirtual 772	java/nio/channels/FileChannel:size	()J
    //   18: lstore_3
    //   19: aload_2
    //   20: getstatic 778	java/nio/channels/FileChannel$MapMode:READ_ONLY	Ljava/nio/channels/FileChannel$MapMode;
    //   23: lconst_0
    //   24: lload_3
    //   25: invokevirtual 782	java/nio/channels/FileChannel:map	(Ljava/nio/channels/FileChannel$MapMode;JJ)Ljava/nio/MappedByteBuffer;
    //   28: astore_2
    //   29: aload_1
    //   30: invokevirtual 783	java/io/FileInputStream:close	()V
    //   33: aload_2
    //   34: areturn
    //   35: astore_2
    //   36: aconst_null
    //   37: astore 5
    //   39: goto +9 -> 48
    //   42: astore 5
    //   44: aload 5
    //   46: athrow
    //   47: astore_2
    //   48: aload 5
    //   50: ifnull +20 -> 70
    //   53: aload_1
    //   54: invokevirtual 783	java/io/FileInputStream:close	()V
    //   57: goto +17 -> 74
    //   60: astore_1
    //   61: aload 5
    //   63: aload_1
    //   64: invokevirtual 787	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   67: goto +7 -> 74
    //   70: aload_1
    //   71: invokevirtual 783	java/io/FileInputStream:close	()V
    //   74: aload_2
    //   75: athrow
    //   76: astore_2
    //   77: getstatic 102	android/graphics/Typeface:TAG	Ljava/lang/String;
    //   80: astore 5
    //   82: new 278	java/lang/StringBuilder
    //   85: dup
    //   86: invokespecial 279	java/lang/StringBuilder:<init>	()V
    //   89: astore_2
    //   90: aload_2
    //   91: ldc_w 789
    //   94: invokevirtual 285	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   97: pop
    //   98: aload_2
    //   99: aload_0
    //   100: invokevirtual 285	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   103: pop
    //   104: aload 5
    //   106: aload_2
    //   107: invokevirtual 289	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   110: invokestatic 553	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   113: pop
    //   114: aconst_null
    //   115: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	116	0	paramString	String
    //   3	51	1	localFileInputStream	FileInputStream
    //   60	11	1	localThrowable1	Throwable
    //   13	21	2	localObject1	Object
    //   35	1	2	localObject2	Object
    //   47	28	2	localObject3	Object
    //   76	1	2	localIOException	IOException
    //   89	18	2	localStringBuilder	StringBuilder
    //   18	7	3	l	long
    //   37	1	5	localObject4	Object
    //   42	20	5	localThrowable2	Throwable
    //   80	25	5	str	String
    // Exception table:
    //   from	to	target	type
    //   9	29	35	finally
    //   9	29	42	java/lang/Throwable
    //   44	47	47	finally
    //   53	57	60	java/lang/Throwable
    //   0	9	76	java/io/IOException
    //   29	33	76	java/io/IOException
    //   53	57	76	java/io/IOException
    //   61	67	76	java/io/IOException
    //   70	74	76	java/io/IOException
    //   74	76	76	java/io/IOException
  }
  
  private static native long nativeCreateFromArray(long[] paramArrayOfLong, int paramInt1, int paramInt2);
  
  private static native long nativeCreateFromTypeface(long paramLong, int paramInt);
  
  private static native long nativeCreateFromTypefaceWithExactStyle(long paramLong, int paramInt, boolean paramBoolean);
  
  private static native long nativeCreateFromTypefaceWithVariation(long paramLong, List<FontVariationAxis> paramList);
  
  private static native long nativeCreateWeightAlias(long paramLong, int paramInt);
  
  @CriticalNative
  private static native long nativeGetReleaseFunc();
  
  @CriticalNative
  private static native int nativeGetStyle(long paramLong);
  
  private static native int[] nativeGetSupportedAxes(long paramLong);
  
  @CriticalNative
  private static native int nativeGetWeight(long paramLong);
  
  @CriticalNative
  private static native void nativeSetDefault(long paramLong);
  
  private static void pushFamilyToFallback(FontConfig.Family paramFamily, ArrayMap<String, ArrayList<FontFamily>> paramArrayMap, Map<String, ByteBuffer> paramMap, String paramString)
  {
    String[] arrayOfString = paramFamily.getLanguages();
    int i = paramFamily.getVariant();
    ArrayList localArrayList = new ArrayList();
    ArrayMap localArrayMap = new ArrayMap();
    FontConfig.Font[] arrayOfFont = paramFamily.getFonts();
    int j = arrayOfFont.length;
    int k = 0;
    Object localObject1;
    Object localObject2;
    for (int m = 0; m < j; m++)
    {
      FontConfig.Font localFont = arrayOfFont[m];
      String str = localFont.getFallbackFor();
      if (str == null)
      {
        localArrayList.add(localFont);
      }
      else
      {
        localObject1 = (ArrayList)localArrayMap.get(str);
        localObject2 = localObject1;
        if (localObject1 == null)
        {
          localObject2 = new ArrayList();
          localArrayMap.put(str, localObject2);
        }
        ((ArrayList)localObject2).add(localFont);
      }
    }
    if (localArrayList.isEmpty()) {
      localObject2 = null;
    } else {
      localObject2 = createFontFamily(paramFamily.getName(), localArrayList, arrayOfString, i, paramMap, paramString);
    }
    for (m = k; m < paramArrayMap.size(); m++)
    {
      localObject1 = (ArrayList)localArrayMap.get(paramArrayMap.keyAt(m));
      if (localObject1 == null)
      {
        if (localObject2 != null) {
          ((ArrayList)paramArrayMap.valueAt(m)).add(localObject2);
        }
      }
      else
      {
        localObject1 = createFontFamily(paramFamily.getName(), (List)localObject1, arrayOfString, i, paramMap, paramString);
        if (localObject1 != null) {
          ((ArrayList)paramArrayMap.valueAt(m)).add(localObject1);
        } else if (localObject2 != null) {
          ((ArrayList)paramArrayMap.valueAt(m)).add(localObject2);
        }
      }
    }
  }
  
  private static void setDefault(Typeface paramTypeface)
  {
    DefaultFlipfont = new Typeface(nativeCreateFromTypeface(native_instance, mStyle));
    sDefaultTypeface = paramTypeface;
    nativeSetDefault(native_instance);
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (Typeface)paramObject;
      if ((mStyle != mStyle) || (native_instance != native_instance)) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public int getStyle()
  {
    return mStyle;
  }
  
  public int getWeight()
  {
    return mWeight;
  }
  
  public int hashCode()
  {
    return 31 * (31 * 17 + (int)(native_instance ^ native_instance >>> 32)) + mStyle;
  }
  
  public final boolean isBold()
  {
    int i = mStyle;
    boolean bool = true;
    if ((i & 0x1) == 0) {
      bool = false;
    }
    return bool;
  }
  
  public final boolean isItalic()
  {
    boolean bool;
    if ((mStyle & 0x2) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isSupportedAxes(int paramInt)
  {
    if (mSupportedAxes == null) {
      try
      {
        if (mSupportedAxes == null)
        {
          mSupportedAxes = nativeGetSupportedAxes(native_instance);
          if (mSupportedAxes == null) {
            mSupportedAxes = EMPTY_AXES;
          }
        }
      }
      finally {}
    }
    boolean bool;
    if (Arrays.binarySearch(mSupportedAxes, paramInt) >= 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static final class Builder
  {
    public static final int BOLD_WEIGHT = 700;
    public static final int NORMAL_WEIGHT = 400;
    private AssetManager mAssetManager;
    private FontVariationAxis[] mAxes;
    private String mFallbackFamilyName;
    private FileDescriptor mFd;
    private Map<Uri, ByteBuffer> mFontBuffers;
    private FontsContract.FontInfo[] mFonts;
    private int mItalic = -1;
    private String mPath;
    private int mTtcIndex;
    private int mWeight = -1;
    
    public Builder(AssetManager paramAssetManager, String paramString)
    {
      mAssetManager = ((AssetManager)Preconditions.checkNotNull(paramAssetManager));
      mPath = ((String)Preconditions.checkStringNotEmpty(paramString));
    }
    
    public Builder(File paramFile)
    {
      mPath = paramFile.getAbsolutePath();
    }
    
    public Builder(FileDescriptor paramFileDescriptor)
    {
      mFd = paramFileDescriptor;
    }
    
    public Builder(String paramString)
    {
      mPath = paramString;
    }
    
    public Builder(FontsContract.FontInfo[] paramArrayOfFontInfo, Map<Uri, ByteBuffer> paramMap)
    {
      mFonts = paramArrayOfFontInfo;
      mFontBuffers = paramMap;
    }
    
    private static String createAssetUid(AssetManager paramAssetManager, String paramString1, int paramInt1, FontVariationAxis[] paramArrayOfFontVariationAxis, int paramInt2, int paramInt3, String paramString2)
    {
      SparseArray localSparseArray = paramAssetManager.getAssignedPackageIdentifiers();
      paramAssetManager = new StringBuilder();
      int i = localSparseArray.size();
      int j = 0;
      for (int k = 0; k < i; k++)
      {
        paramAssetManager.append((String)localSparseArray.valueAt(k));
        paramAssetManager.append("-");
      }
      paramAssetManager.append(paramString1);
      paramAssetManager.append("-");
      paramAssetManager.append(Integer.toString(paramInt1));
      paramAssetManager.append("-");
      paramAssetManager.append(Integer.toString(paramInt2));
      paramAssetManager.append("-");
      paramAssetManager.append(Integer.toString(paramInt3));
      paramAssetManager.append("--");
      paramAssetManager.append(paramString2);
      paramAssetManager.append("--");
      if (paramArrayOfFontVariationAxis != null)
      {
        paramInt2 = paramArrayOfFontVariationAxis.length;
        for (paramInt1 = j; paramInt1 < paramInt2; paramInt1++)
        {
          paramString1 = paramArrayOfFontVariationAxis[paramInt1];
          paramAssetManager.append(paramString1.getTag());
          paramAssetManager.append("-");
          paramAssetManager.append(Float.toString(paramString1.getStyleValue()));
        }
      }
      return paramAssetManager.toString();
    }
    
    private Typeface resolveFallbackTypeface()
    {
      if (mFallbackFamilyName == null) {
        return null;
      }
      Typeface localTypeface1 = (Typeface)Typeface.sSystemFontMap.get(mFallbackFamilyName);
      Typeface localTypeface2 = localTypeface1;
      if (localTypeface1 == null) {
        localTypeface2 = Typeface.sDefaultTypeface;
      }
      if ((mWeight == -1) && (mItalic == -1)) {
        return localTypeface2;
      }
      int i;
      if (mWeight == -1) {
        i = mWeight;
      } else {
        i = mWeight;
      }
      int j = mItalic;
      boolean bool = false;
      if (j == -1) {
        if ((mStyle & 0x2) == 0) {}
      }
      for (;;)
      {
        bool = true;
        break;
        do
        {
          break;
        } while (mItalic != 1);
      }
      return Typeface.createWeightStyle(localTypeface2, i, bool);
    }
    
    /* Error */
    public Typeface build()
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 67	android/graphics/Typeface$Builder:mFd	Ljava/io/FileDescriptor;
      //   4: astore_1
      //   5: aconst_null
      //   6: astore_2
      //   7: aload_1
      //   8: ifnull +224 -> 232
      //   11: new 161	java/io/FileInputStream
      //   14: astore_3
      //   15: aload_3
      //   16: aload_0
      //   17: getfield 67	android/graphics/Typeface$Builder:mFd	Ljava/io/FileDescriptor;
      //   20: invokespecial 163	java/io/FileInputStream:<init>	(Ljava/io/FileDescriptor;)V
      //   23: aload_2
      //   24: astore_1
      //   25: aload_3
      //   26: invokevirtual 167	java/io/FileInputStream:getChannel	()Ljava/nio/channels/FileChannel;
      //   29: astore 4
      //   31: aload_2
      //   32: astore_1
      //   33: aload 4
      //   35: invokevirtual 172	java/nio/channels/FileChannel:size	()J
      //   38: lstore 5
      //   40: aload_2
      //   41: astore_1
      //   42: aload 4
      //   44: getstatic 178	java/nio/channels/FileChannel$MapMode:READ_ONLY	Ljava/nio/channels/FileChannel$MapMode;
      //   47: lconst_0
      //   48: lload 5
      //   50: invokevirtual 182	java/nio/channels/FileChannel:map	(Ljava/nio/channels/FileChannel$MapMode;JJ)Ljava/nio/MappedByteBuffer;
      //   53: astore 7
      //   55: aload_2
      //   56: astore_1
      //   57: new 184	android/graphics/FontFamily
      //   60: astore 4
      //   62: aload_2
      //   63: astore_1
      //   64: aload 4
      //   66: invokespecial 185	android/graphics/FontFamily:<init>	()V
      //   69: aload_2
      //   70: astore_1
      //   71: aload 4
      //   73: aload 7
      //   75: aload_0
      //   76: getfield 187	android/graphics/Typeface$Builder:mTtcIndex	I
      //   79: aload_0
      //   80: getfield 189	android/graphics/Typeface$Builder:mAxes	[Landroid/graphics/fonts/FontVariationAxis;
      //   83: aload_0
      //   84: getfield 36	android/graphics/Typeface$Builder:mWeight	I
      //   87: aload_0
      //   88: getfield 38	android/graphics/Typeface$Builder:mItalic	I
      //   91: invokevirtual 193	android/graphics/FontFamily:addFontFromBuffer	(Ljava/nio/ByteBuffer;I[Landroid/graphics/fonts/FontVariationAxis;II)Z
      //   94: ifne +23 -> 117
      //   97: aload_2
      //   98: astore_1
      //   99: aload 4
      //   101: invokevirtual 196	android/graphics/FontFamily:abortCreation	()V
      //   104: aload_2
      //   105: astore_1
      //   106: aload_0
      //   107: invokespecial 198	android/graphics/Typeface$Builder:resolveFallbackTypeface	()Landroid/graphics/Typeface;
      //   110: astore_2
      //   111: aload_3
      //   112: invokevirtual 201	java/io/FileInputStream:close	()V
      //   115: aload_2
      //   116: areturn
      //   117: aload_2
      //   118: astore_1
      //   119: aload 4
      //   121: invokevirtual 205	android/graphics/FontFamily:freeze	()Z
      //   124: ifne +16 -> 140
      //   127: aload_2
      //   128: astore_1
      //   129: aload_0
      //   130: invokespecial 198	android/graphics/Typeface$Builder:resolveFallbackTypeface	()Landroid/graphics/Typeface;
      //   133: astore_2
      //   134: aload_3
      //   135: invokevirtual 201	java/io/FileInputStream:close	()V
      //   138: aload_2
      //   139: areturn
      //   140: aload_2
      //   141: astore_1
      //   142: aload_0
      //   143: getfield 131	android/graphics/Typeface$Builder:mFallbackFamilyName	Ljava/lang/String;
      //   146: astore 7
      //   148: aload_2
      //   149: astore_1
      //   150: aload_0
      //   151: getfield 36	android/graphics/Typeface$Builder:mWeight	I
      //   154: istore 8
      //   156: aload_2
      //   157: astore_1
      //   158: aload_0
      //   159: getfield 38	android/graphics/Typeface$Builder:mItalic	I
      //   162: istore 9
      //   164: aload_2
      //   165: astore_1
      //   166: iconst_1
      //   167: anewarray 184	android/graphics/FontFamily
      //   170: dup
      //   171: iconst_0
      //   172: aload 4
      //   174: aastore
      //   175: aload 7
      //   177: iload 8
      //   179: iload 9
      //   181: invokestatic 209	android/graphics/Typeface:access$400	([Landroid/graphics/FontFamily;Ljava/lang/String;II)Landroid/graphics/Typeface;
      //   184: astore_2
      //   185: aload_3
      //   186: invokevirtual 201	java/io/FileInputStream:close	()V
      //   189: aload_2
      //   190: areturn
      //   191: astore_2
      //   192: goto +8 -> 200
      //   195: astore_2
      //   196: aload_2
      //   197: astore_1
      //   198: aload_2
      //   199: athrow
      //   200: aload_1
      //   201: ifnull +19 -> 220
      //   204: aload_3
      //   205: invokevirtual 201	java/io/FileInputStream:close	()V
      //   208: goto +16 -> 224
      //   211: astore_3
      //   212: aload_1
      //   213: aload_3
      //   214: invokevirtual 213	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
      //   217: goto +7 -> 224
      //   220: aload_3
      //   221: invokevirtual 201	java/io/FileInputStream:close	()V
      //   224: aload_2
      //   225: athrow
      //   226: astore_1
      //   227: aload_0
      //   228: invokespecial 198	android/graphics/Typeface$Builder:resolveFallbackTypeface	()Landroid/graphics/Typeface;
      //   231: areturn
      //   232: aload_0
      //   233: getfield 48	android/graphics/Typeface$Builder:mAssetManager	Landroid/content/res/AssetManager;
      //   236: ifnull +187 -> 423
      //   239: aload_0
      //   240: getfield 48	android/graphics/Typeface$Builder:mAssetManager	Landroid/content/res/AssetManager;
      //   243: aload_0
      //   244: getfield 56	android/graphics/Typeface$Builder:mPath	Ljava/lang/String;
      //   247: aload_0
      //   248: getfield 187	android/graphics/Typeface$Builder:mTtcIndex	I
      //   251: aload_0
      //   252: getfield 189	android/graphics/Typeface$Builder:mAxes	[Landroid/graphics/fonts/FontVariationAxis;
      //   255: aload_0
      //   256: getfield 36	android/graphics/Typeface$Builder:mWeight	I
      //   259: aload_0
      //   260: getfield 38	android/graphics/Typeface$Builder:mItalic	I
      //   263: aload_0
      //   264: getfield 131	android/graphics/Typeface$Builder:mFallbackFamilyName	Ljava/lang/String;
      //   267: invokestatic 80	android/graphics/Typeface$Builder:createAssetUid	(Landroid/content/res/AssetManager;Ljava/lang/String;I[Landroid/graphics/fonts/FontVariationAxis;IILjava/lang/String;)Ljava/lang/String;
      //   270: astore_2
      //   271: invokestatic 217	android/graphics/Typeface:access$500	()Ljava/lang/Object;
      //   274: astore_1
      //   275: aload_1
      //   276: monitorenter
      //   277: invokestatic 221	android/graphics/Typeface:access$600	()Landroid/util/LruCache;
      //   280: aload_2
      //   281: invokevirtual 224	android/util/LruCache:get	(Ljava/lang/Object;)Ljava/lang/Object;
      //   284: checkcast 6	android/graphics/Typeface
      //   287: astore_3
      //   288: aload_3
      //   289: ifnull +7 -> 296
      //   292: aload_1
      //   293: monitorexit
      //   294: aload_3
      //   295: areturn
      //   296: new 184	android/graphics/FontFamily
      //   299: astore_3
      //   300: aload_3
      //   301: invokespecial 185	android/graphics/FontFamily:<init>	()V
      //   304: aload_3
      //   305: aload_0
      //   306: getfield 48	android/graphics/Typeface$Builder:mAssetManager	Landroid/content/res/AssetManager;
      //   309: aload_0
      //   310: getfield 56	android/graphics/Typeface$Builder:mPath	Ljava/lang/String;
      //   313: aload_0
      //   314: getfield 187	android/graphics/Typeface$Builder:mTtcIndex	I
      //   317: iconst_1
      //   318: aload_0
      //   319: getfield 187	android/graphics/Typeface$Builder:mTtcIndex	I
      //   322: aload_0
      //   323: getfield 36	android/graphics/Typeface$Builder:mWeight	I
      //   326: aload_0
      //   327: getfield 38	android/graphics/Typeface$Builder:mItalic	I
      //   330: aload_0
      //   331: getfield 189	android/graphics/Typeface$Builder:mAxes	[Landroid/graphics/fonts/FontVariationAxis;
      //   334: invokevirtual 228	android/graphics/FontFamily:addFontFromAssetManager	(Landroid/content/res/AssetManager;Ljava/lang/String;IZIII[Landroid/graphics/fonts/FontVariationAxis;)Z
      //   337: ifne +16 -> 353
      //   340: aload_3
      //   341: invokevirtual 196	android/graphics/FontFamily:abortCreation	()V
      //   344: aload_0
      //   345: invokespecial 198	android/graphics/Typeface$Builder:resolveFallbackTypeface	()Landroid/graphics/Typeface;
      //   348: astore_2
      //   349: aload_1
      //   350: monitorexit
      //   351: aload_2
      //   352: areturn
      //   353: aload_3
      //   354: invokevirtual 205	android/graphics/FontFamily:freeze	()Z
      //   357: ifne +12 -> 369
      //   360: aload_0
      //   361: invokespecial 198	android/graphics/Typeface$Builder:resolveFallbackTypeface	()Landroid/graphics/Typeface;
      //   364: astore_2
      //   365: aload_1
      //   366: monitorexit
      //   367: aload_2
      //   368: areturn
      //   369: aload_0
      //   370: getfield 131	android/graphics/Typeface$Builder:mFallbackFamilyName	Ljava/lang/String;
      //   373: astore 4
      //   375: aload_0
      //   376: getfield 36	android/graphics/Typeface$Builder:mWeight	I
      //   379: istore 9
      //   381: aload_0
      //   382: getfield 38	android/graphics/Typeface$Builder:mItalic	I
      //   385: istore 8
      //   387: iconst_1
      //   388: anewarray 184	android/graphics/FontFamily
      //   391: dup
      //   392: iconst_0
      //   393: aload_3
      //   394: aastore
      //   395: aload 4
      //   397: iload 9
      //   399: iload 8
      //   401: invokestatic 209	android/graphics/Typeface:access$400	([Landroid/graphics/FontFamily;Ljava/lang/String;II)Landroid/graphics/Typeface;
      //   404: astore_3
      //   405: invokestatic 221	android/graphics/Typeface:access$600	()Landroid/util/LruCache;
      //   408: aload_2
      //   409: aload_3
      //   410: invokevirtual 232	android/util/LruCache:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
      //   413: pop
      //   414: aload_1
      //   415: monitorexit
      //   416: aload_3
      //   417: areturn
      //   418: astore_2
      //   419: aload_1
      //   420: monitorexit
      //   421: aload_2
      //   422: athrow
      //   423: aload_0
      //   424: getfield 56	android/graphics/Typeface$Builder:mPath	Ljava/lang/String;
      //   427: ifnull +93 -> 520
      //   430: new 184	android/graphics/FontFamily
      //   433: dup
      //   434: invokespecial 185	android/graphics/FontFamily:<init>	()V
      //   437: astore_1
      //   438: aload_1
      //   439: aload_0
      //   440: getfield 56	android/graphics/Typeface$Builder:mPath	Ljava/lang/String;
      //   443: aload_0
      //   444: getfield 187	android/graphics/Typeface$Builder:mTtcIndex	I
      //   447: aload_0
      //   448: getfield 189	android/graphics/Typeface$Builder:mAxes	[Landroid/graphics/fonts/FontVariationAxis;
      //   451: aload_0
      //   452: getfield 36	android/graphics/Typeface$Builder:mWeight	I
      //   455: aload_0
      //   456: getfield 38	android/graphics/Typeface$Builder:mItalic	I
      //   459: invokevirtual 236	android/graphics/FontFamily:addFont	(Ljava/lang/String;I[Landroid/graphics/fonts/FontVariationAxis;II)Z
      //   462: ifne +12 -> 474
      //   465: aload_1
      //   466: invokevirtual 196	android/graphics/FontFamily:abortCreation	()V
      //   469: aload_0
      //   470: invokespecial 198	android/graphics/Typeface$Builder:resolveFallbackTypeface	()Landroid/graphics/Typeface;
      //   473: areturn
      //   474: aload_1
      //   475: invokevirtual 205	android/graphics/FontFamily:freeze	()Z
      //   478: ifne +8 -> 486
      //   481: aload_0
      //   482: invokespecial 198	android/graphics/Typeface$Builder:resolveFallbackTypeface	()Landroid/graphics/Typeface;
      //   485: areturn
      //   486: aload_0
      //   487: getfield 131	android/graphics/Typeface$Builder:mFallbackFamilyName	Ljava/lang/String;
      //   490: astore_2
      //   491: aload_0
      //   492: getfield 36	android/graphics/Typeface$Builder:mWeight	I
      //   495: istore 9
      //   497: aload_0
      //   498: getfield 38	android/graphics/Typeface$Builder:mItalic	I
      //   501: istore 8
      //   503: iconst_1
      //   504: anewarray 184	android/graphics/FontFamily
      //   507: dup
      //   508: iconst_0
      //   509: aload_1
      //   510: aastore
      //   511: aload_2
      //   512: iload 9
      //   514: iload 8
      //   516: invokestatic 209	android/graphics/Typeface:access$400	([Landroid/graphics/FontFamily;Ljava/lang/String;II)Landroid/graphics/Typeface;
      //   519: areturn
      //   520: aload_0
      //   521: getfield 71	android/graphics/Typeface$Builder:mFonts	[Landroid/provider/FontsContract$FontInfo;
      //   524: ifnull +157 -> 681
      //   527: new 184	android/graphics/FontFamily
      //   530: dup
      //   531: invokespecial 185	android/graphics/FontFamily:<init>	()V
      //   534: astore_1
      //   535: aload_0
      //   536: getfield 71	android/graphics/Typeface$Builder:mFonts	[Landroid/provider/FontsContract$FontInfo;
      //   539: astore_3
      //   540: aload_3
      //   541: arraylength
      //   542: istore 10
      //   544: iconst_0
      //   545: istore 8
      //   547: iconst_0
      //   548: istore 9
      //   550: iload 8
      //   552: iload 10
      //   554: if_icmpge +77 -> 631
      //   557: aload_3
      //   558: iload 8
      //   560: aaload
      //   561: astore 4
      //   563: aload_0
      //   564: getfield 73	android/graphics/Typeface$Builder:mFontBuffers	Ljava/util/Map;
      //   567: aload 4
      //   569: invokevirtual 242	android/provider/FontsContract$FontInfo:getUri	()Landroid/net/Uri;
      //   572: invokeinterface 139 2 0
      //   577: checkcast 244	java/nio/ByteBuffer
      //   580: astore_2
      //   581: aload_2
      //   582: ifnonnull +6 -> 588
      //   585: goto +40 -> 625
      //   588: aload_1
      //   589: aload_2
      //   590: aload 4
      //   592: invokevirtual 247	android/provider/FontsContract$FontInfo:getTtcIndex	()I
      //   595: aload 4
      //   597: invokevirtual 251	android/provider/FontsContract$FontInfo:getAxes	()[Landroid/graphics/fonts/FontVariationAxis;
      //   600: aload 4
      //   602: invokevirtual 254	android/provider/FontsContract$FontInfo:getWeight	()I
      //   605: aload 4
      //   607: invokevirtual 257	android/provider/FontsContract$FontInfo:isItalic	()Z
      //   610: invokevirtual 193	android/graphics/FontFamily:addFontFromBuffer	(Ljava/nio/ByteBuffer;I[Landroid/graphics/fonts/FontVariationAxis;II)Z
      //   613: ifne +9 -> 622
      //   616: aload_1
      //   617: invokevirtual 196	android/graphics/FontFamily:abortCreation	()V
      //   620: aconst_null
      //   621: areturn
      //   622: iconst_1
      //   623: istore 9
      //   625: iinc 8 1
      //   628: goto -78 -> 550
      //   631: iload 9
      //   633: ifne +9 -> 642
      //   636: aload_1
      //   637: invokevirtual 196	android/graphics/FontFamily:abortCreation	()V
      //   640: aconst_null
      //   641: areturn
      //   642: aload_1
      //   643: invokevirtual 205	android/graphics/FontFamily:freeze	()Z
      //   646: pop
      //   647: aload_0
      //   648: getfield 131	android/graphics/Typeface$Builder:mFallbackFamilyName	Ljava/lang/String;
      //   651: astore_2
      //   652: aload_0
      //   653: getfield 36	android/graphics/Typeface$Builder:mWeight	I
      //   656: istore 8
      //   658: aload_0
      //   659: getfield 38	android/graphics/Typeface$Builder:mItalic	I
      //   662: istore 9
      //   664: iconst_1
      //   665: anewarray 184	android/graphics/FontFamily
      //   668: dup
      //   669: iconst_0
      //   670: aload_1
      //   671: aastore
      //   672: aload_2
      //   673: iload 8
      //   675: iload 9
      //   677: invokestatic 209	android/graphics/Typeface:access$400	([Landroid/graphics/FontFamily;Ljava/lang/String;II)Landroid/graphics/Typeface;
      //   680: areturn
      //   681: new 259	java/lang/IllegalArgumentException
      //   684: dup
      //   685: ldc_w 261
      //   688: invokespecial 263	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
      //   691: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	692	0	this	Builder
      //   4	209	1	localObject1	Object
      //   226	1	1	localIOException	IOException
      //   6	184	2	localTypeface	Typeface
      //   191	1	2	localObject3	Object
      //   195	30	2	localThrowable1	Throwable
      //   270	139	2	localObject4	Object
      //   418	4	2	localObject5	Object
      //   490	183	2	localObject6	Object
      //   14	191	3	localFileInputStream	FileInputStream
      //   211	10	3	localThrowable2	Throwable
      //   287	271	3	localObject7	Object
      //   29	577	4	localObject8	Object
      //   38	11	5	l	long
      //   53	123	7	localObject9	Object
      //   154	520	8	i	int
      //   162	514	9	j	int
      //   542	13	10	k	int
      // Exception table:
      //   from	to	target	type
      //   25	31	191	finally
      //   33	40	191	finally
      //   42	55	191	finally
      //   57	62	191	finally
      //   64	69	191	finally
      //   71	97	191	finally
      //   99	104	191	finally
      //   106	111	191	finally
      //   119	127	191	finally
      //   129	134	191	finally
      //   142	148	191	finally
      //   150	156	191	finally
      //   158	164	191	finally
      //   166	185	191	finally
      //   198	200	191	finally
      //   25	31	195	java/lang/Throwable
      //   33	40	195	java/lang/Throwable
      //   42	55	195	java/lang/Throwable
      //   57	62	195	java/lang/Throwable
      //   64	69	195	java/lang/Throwable
      //   71	97	195	java/lang/Throwable
      //   99	104	195	java/lang/Throwable
      //   106	111	195	java/lang/Throwable
      //   119	127	195	java/lang/Throwable
      //   129	134	195	java/lang/Throwable
      //   142	148	195	java/lang/Throwable
      //   150	156	195	java/lang/Throwable
      //   158	164	195	java/lang/Throwable
      //   166	185	195	java/lang/Throwable
      //   204	208	211	java/lang/Throwable
      //   11	23	226	java/io/IOException
      //   111	115	226	java/io/IOException
      //   134	138	226	java/io/IOException
      //   185	189	226	java/io/IOException
      //   204	208	226	java/io/IOException
      //   212	217	226	java/io/IOException
      //   220	224	226	java/io/IOException
      //   224	226	226	java/io/IOException
      //   277	288	418	finally
      //   292	294	418	finally
      //   296	351	418	finally
      //   353	367	418	finally
      //   369	416	418	finally
      //   419	421	418	finally
    }
    
    public Builder setFallback(String paramString)
    {
      mFallbackFamilyName = paramString;
      return this;
    }
    
    public Builder setFontVariationSettings(String paramString)
    {
      if (mFonts == null)
      {
        if (mAxes == null)
        {
          mAxes = FontVariationAxis.fromFontVariationSettings(paramString);
          return this;
        }
        throw new IllegalStateException("Font variation settings are already set.");
      }
      throw new IllegalArgumentException("Font variation settings can not be specified for FontResult source.");
    }
    
    public Builder setFontVariationSettings(FontVariationAxis[] paramArrayOfFontVariationAxis)
    {
      if (mFonts == null)
      {
        if (mAxes == null)
        {
          mAxes = paramArrayOfFontVariationAxis;
          return this;
        }
        throw new IllegalStateException("Font variation settings are already set.");
      }
      throw new IllegalArgumentException("Font variation settings can not be specified for FontResult source.");
    }
    
    public Builder setItalic(boolean paramBoolean)
    {
      mItalic = paramBoolean;
      return this;
    }
    
    public Builder setTtcIndex(int paramInt)
    {
      if (mFonts == null)
      {
        mTtcIndex = paramInt;
        return this;
      }
      throw new IllegalArgumentException("TTC index can not be specified for FontResult source.");
    }
    
    public Builder setWeight(int paramInt)
    {
      mWeight = paramInt;
      return this;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Style {}
}
