package android.content.res;

import android.animation.Animator;
import android.animation.StateListAnimator;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.ImageDecoder.AssetInputStreamSource;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.graphics.drawable.DrawableContainer;
import android.icu.text.PluralRules;
import android.os.Build.VERSION;
import android.os.LocaleList;
import android.os.Process;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.TypedValue;
import android.util.Xml;
import android.view.DisplayAdjustments;
import com.android.internal.util.GrowingArrayUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Locale;
import org.xmlpull.v1.XmlPullParserException;

public class ResourcesImpl
{
  private static final boolean DEBUG_CONFIG = false;
  private static final boolean DEBUG_LOAD = false;
  private static final int ID_OTHER = 16777220;
  static final String TAG = "Resources";
  static final String TAG_PRELOAD = "Resources.preload";
  public static final boolean TRACE_FOR_DETAILED_PRELOAD = SystemProperties.getBoolean("debug.trace_resource_preload", false);
  private static final boolean TRACE_FOR_MISS_PRELOAD = false;
  private static final boolean TRACE_FOR_PRELOAD = false;
  private static final int XML_BLOCK_CACHE_SIZE = 4;
  private static int sPreloadTracingNumLoadedDrawables;
  private static boolean sPreloaded;
  private static final LongSparseArray<Drawable.ConstantState> sPreloadedColorDrawables;
  private static final LongSparseArray<ConstantState<ComplexColor>> sPreloadedComplexColors;
  private static final LongSparseArray<Drawable.ConstantState>[] sPreloadedDrawables;
  private static final Object sSync = new Object();
  private final Object mAccessLock = new Object();
  private final ConfigurationBoundResourceCache<Animator> mAnimatorCache = new ConfigurationBoundResourceCache();
  final AssetManager mAssets;
  private final int[] mCachedXmlBlockCookies = new int[4];
  private final String[] mCachedXmlBlockFiles = new String[4];
  private final XmlBlock[] mCachedXmlBlocks = new XmlBlock[4];
  private final DrawableCache mColorDrawableCache = new DrawableCache();
  private final ConfigurationBoundResourceCache<ComplexColor> mComplexColorCache = new ConfigurationBoundResourceCache();
  private final Configuration mConfiguration = new Configuration();
  private final DisplayAdjustments mDisplayAdjustments;
  private final DrawableCache mDrawableCache = new DrawableCache();
  private int mLastCachedXmlBlockIndex = -1;
  private final ThreadLocal<LookupStack> mLookupStack = ThreadLocal.withInitial(_..Lambda.ResourcesImpl.h3PTRX185BeQl8SVC2_w9arp5Og.INSTANCE);
  private final DisplayMetrics mMetrics = new DisplayMetrics();
  private PluralRules mPluralRule;
  private long mPreloadTracingPreloadStartTime;
  private long mPreloadTracingStartBitmapCount;
  private long mPreloadTracingStartBitmapSize;
  private boolean mPreloading;
  private final ConfigurationBoundResourceCache<StateListAnimator> mStateListAnimatorCache = new ConfigurationBoundResourceCache();
  private final Configuration mTmpConfig = new Configuration();
  
  static
  {
    sPreloadedColorDrawables = new LongSparseArray();
    sPreloadedComplexColors = new LongSparseArray();
    sPreloadedDrawables = new LongSparseArray[2];
    sPreloadedDrawables[0] = new LongSparseArray();
    sPreloadedDrawables[1] = new LongSparseArray();
  }
  
  public ResourcesImpl(AssetManager paramAssetManager, DisplayMetrics paramDisplayMetrics, Configuration paramConfiguration, DisplayAdjustments paramDisplayAdjustments)
  {
    mAssets = paramAssetManager;
    mMetrics.setToDefaults();
    mDisplayAdjustments = paramDisplayAdjustments;
    mConfiguration.setToDefaults();
    updateConfiguration(paramConfiguration, paramDisplayMetrics, paramDisplayAdjustments.getCompatibilityInfo());
  }
  
  private static String adjustLanguageTag(String paramString)
  {
    int i = paramString.indexOf('-');
    String str;
    if (i == -1)
    {
      str = "";
    }
    else
    {
      localObject = paramString.substring(0, i);
      str = paramString.substring(i);
      paramString = (String)localObject;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(Locale.adjustLanguageCode(paramString));
    ((StringBuilder)localObject).append(str);
    return ((StringBuilder)localObject).toString();
  }
  
  private static int attrForQuantityCode(String paramString)
  {
    switch (paramString.hashCode())
    {
    default: 
      break;
    case 3735208: 
      if (paramString.equals("zero")) {
        i = 0;
      }
      break;
    case 3343967: 
      if (paramString.equals("many")) {
        i = 4;
      }
      break;
    case 115276: 
      if (paramString.equals("two")) {
        i = 2;
      }
      break;
    case 110182: 
      if (paramString.equals("one")) {
        i = 1;
      }
      break;
    case 101272: 
      if (paramString.equals("few")) {
        i = 3;
      }
      break;
    }
    int i = -1;
    switch (i)
    {
    default: 
      return 16777220;
    case 4: 
      return 16777225;
    case 3: 
      return 16777224;
    case 2: 
      return 16777223;
    case 1: 
      return 16777222;
    }
    return 16777221;
  }
  
  private void cacheDrawable(TypedValue arg1, boolean paramBoolean1, DrawableCache paramDrawableCache, Resources.Theme paramTheme, boolean paramBoolean2, long paramLong, Drawable paramDrawable)
  {
    paramDrawable = paramDrawable.getConstantState();
    if (paramDrawable == null) {
      return;
    }
    if (mPreloading)
    {
      int i = paramDrawable.getChangingConfigurations();
      if (paramBoolean1)
      {
        if (verifyPreloadConfig(i, 0, resourceId, "drawable")) {
          sPreloadedColorDrawables.put(paramLong, paramDrawable);
        }
      }
      else if (verifyPreloadConfig(i, 8192, resourceId, "drawable")) {
        if ((i & 0x2000) == 0)
        {
          sPreloadedDrawables[0].put(paramLong, paramDrawable);
          sPreloadedDrawables[1].put(paramLong, paramDrawable);
        }
        else
        {
          sPreloadedDrawables[mConfiguration.getLayoutDirection()].put(paramLong, paramDrawable);
        }
      }
    }
    synchronized (mAccessLock)
    {
      paramDrawableCache.put(paramLong, paramTheme, paramDrawable, paramBoolean2);
      return;
    }
  }
  
  private Drawable decodeImageDrawable(AssetManager.AssetInputStream paramAssetInputStream, Resources paramResources, TypedValue paramTypedValue)
  {
    paramAssetInputStream = new ImageDecoder.AssetInputStreamSource(paramAssetInputStream, paramResources, paramTypedValue);
    try
    {
      paramAssetInputStream = ImageDecoder.decodeDrawable(paramAssetInputStream, _..Lambda.ResourcesImpl.99dm2ENnzo9b0SIUjUj2Kl3pi90.INSTANCE);
      return paramAssetInputStream;
    }
    catch (IOException paramAssetInputStream) {}
    return null;
  }
  
  private ColorStateList getColorStateListFromInt(TypedValue paramTypedValue, long paramLong)
  {
    Object localObject = (ConstantState)sPreloadedComplexColors.get(paramLong);
    if (localObject != null) {
      return (ColorStateList)((ConstantState)localObject).newInstance();
    }
    localObject = ColorStateList.valueOf(data);
    if ((mPreloading) && (verifyPreloadConfig(changingConfigurations, 0, resourceId, "color"))) {
      sPreloadedComplexColors.put(paramLong, ((ColorStateList)localObject).getConstantState());
    }
    return localObject;
  }
  
  private PluralRules getPluralRule()
  {
    synchronized (sSync)
    {
      if (mPluralRule == null) {
        mPluralRule = PluralRules.forLocale(mConfiguration.getLocales().get(0));
      }
      PluralRules localPluralRules = mPluralRule;
      return localPluralRules;
    }
  }
  
  private ComplexColor loadComplexColorForCookie(Resources paramResources, TypedValue paramTypedValue, int paramInt, Resources.Theme paramTheme)
  {
    if (string != null)
    {
      String str1 = string.toString();
      Object localObject = null;
      Trace.traceBegin(8192L, str1);
      if (str1.endsWith(".xml")) {
        try
        {
          XmlResourceParser localXmlResourceParser = loadXmlResourceParser(str1, paramInt, assetCookie, "ComplexColor");
          AttributeSet localAttributeSet = Xml.asAttributeSet(localXmlResourceParser);
          int i;
          do
          {
            i = localXmlResourceParser.next();
          } while ((i != 2) && (i != 1));
          if (i == 2)
          {
            String str2 = localXmlResourceParser.getName();
            if (str2.equals("gradient"))
            {
              paramTypedValue = GradientColor.createFromXmlInner(paramResources, localXmlResourceParser, localAttributeSet, paramTheme);
            }
            else
            {
              paramTypedValue = localObject;
              if (str2.equals("selector")) {
                paramTypedValue = ColorStateList.createFromXmlInner(paramResources, localXmlResourceParser, localAttributeSet, paramTheme);
              }
            }
            localXmlResourceParser.close();
            Trace.traceEnd(8192L);
            return paramTypedValue;
          }
          paramResources = new org/xmlpull/v1/XmlPullParserException;
          paramResources.<init>("No start tag found");
          throw paramResources;
        }
        catch (Exception paramResources)
        {
          Trace.traceEnd(8192L);
          paramTypedValue = new StringBuilder();
          paramTypedValue.append("File ");
          paramTypedValue.append(str1);
          paramTypedValue.append(" from ComplexColor resource ID #0x");
          paramTypedValue.append(Integer.toHexString(paramInt));
          paramTypedValue = new Resources.NotFoundException(paramTypedValue.toString());
          paramTypedValue.initCause(paramResources);
          throw paramTypedValue;
        }
      }
      Trace.traceEnd(8192L);
      paramResources = new StringBuilder();
      paramResources.append("File ");
      paramResources.append(str1);
      paramResources.append(" from drawable resource ID #0x");
      paramResources.append(Integer.toHexString(paramInt));
      paramResources.append(": .xml extension required");
      throw new Resources.NotFoundException(paramResources.toString());
    }
    paramResources = new StringBuilder();
    paramResources.append("Can't convert to ComplexColor: type=0x");
    paramResources.append(type);
    throw new UnsupportedOperationException(paramResources.toString());
  }
  
  private ComplexColor loadComplexColorFromName(Resources paramResources, Resources.Theme paramTheme, TypedValue paramTypedValue, int paramInt)
  {
    long l = assetCookie << 32 | data;
    ConfigurationBoundResourceCache localConfigurationBoundResourceCache = mComplexColorCache;
    ComplexColor localComplexColor = (ComplexColor)localConfigurationBoundResourceCache.getInstance(l, paramResources, paramTheme);
    if (localComplexColor != null) {
      return localComplexColor;
    }
    Object localObject = (ConstantState)sPreloadedComplexColors.get(l);
    if (localObject != null) {
      localComplexColor = (ComplexColor)((ConstantState)localObject).newInstance(paramResources, paramTheme);
    }
    localObject = localComplexColor;
    if (localComplexColor == null) {
      localObject = loadComplexColorForCookie(paramResources, paramTypedValue, paramInt, paramTheme);
    }
    if (localObject != null)
    {
      ((ComplexColor)localObject).setBaseChangingConfigurations(changingConfigurations);
      if (mPreloading)
      {
        if (verifyPreloadConfig(((ComplexColor)localObject).getChangingConfigurations(), 0, resourceId, "color")) {
          sPreloadedComplexColors.put(l, ((ComplexColor)localObject).getConstantState());
        }
      }
      else {
        localConfigurationBoundResourceCache.put(l, paramTheme, ((ComplexColor)localObject).getConstantState());
      }
    }
    return localObject;
  }
  
  /* Error */
  private Drawable loadDrawableForCookie(Resources paramResources, TypedValue paramTypedValue, int paramInt1, int paramInt2)
  {
    // Byte code:
    //   0: aload_2
    //   1: getfield 361	android/util/TypedValue:string	Ljava/lang/CharSequence;
    //   4: ifnull +536 -> 540
    //   7: aload_2
    //   8: getfield 361	android/util/TypedValue:string	Ljava/lang/CharSequence;
    //   11: invokeinterface 364 1 0
    //   16: astore 5
    //   18: lconst_0
    //   19: lstore 6
    //   21: iconst_0
    //   22: istore 8
    //   24: lconst_0
    //   25: lstore 9
    //   27: iconst_0
    //   28: istore 11
    //   30: getstatic 92	android/content/res/ResourcesImpl:TRACE_FOR_DETAILED_PRELOAD	Z
    //   33: ifeq +23 -> 56
    //   36: invokestatic 490	java/lang/System:nanoTime	()J
    //   39: lstore 6
    //   41: getstatic 495	android/graphics/Bitmap:sPreloadTracingNumInstantiatedBitmaps	I
    //   44: istore 8
    //   46: getstatic 498	android/graphics/Bitmap:sPreloadTracingTotalBitmapsSize	J
    //   49: lstore 9
    //   51: getstatic 500	android/content/res/ResourcesImpl:sPreloadTracingNumLoadedDrawables	I
    //   54: istore 11
    //   56: ldc2_w 365
    //   59: aload 5
    //   61: invokestatic 372	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   64: aload_0
    //   65: getfield 145	android/content/res/ResourcesImpl:mLookupStack	Ljava/lang/ThreadLocal;
    //   68: invokevirtual 502	java/lang/ThreadLocal:get	()Ljava/lang/Object;
    //   71: checkcast 8	android/content/res/ResourcesImpl$LookupStack
    //   74: astore 12
    //   76: aload 12
    //   78: iload_3
    //   79: invokevirtual 506	android/content/res/ResourcesImpl$LookupStack:contains	(I)Z
    //   82: ifne +374 -> 456
    //   85: aload 12
    //   87: iload_3
    //   88: invokevirtual 509	android/content/res/ResourcesImpl$LookupStack:push	(I)V
    //   91: aload 5
    //   93: ldc_w 374
    //   96: invokevirtual 378	java/lang/String:endsWith	(Ljava/lang/String;)Z
    //   99: istore 13
    //   101: iload 13
    //   103: ifeq +40 -> 143
    //   106: aload_0
    //   107: aload 5
    //   109: iload_3
    //   110: aload_2
    //   111: getfield 381	android/util/TypedValue:assetCookie	I
    //   114: ldc_w 260
    //   117: invokevirtual 387	android/content/res/ResourcesImpl:loadXmlResourceParser	(Ljava/lang/String;IILjava/lang/String;)Landroid/content/res/XmlResourceParser;
    //   120: astore_2
    //   121: aload_1
    //   122: aload_2
    //   123: iload 4
    //   125: aconst_null
    //   126: invokestatic 513	android/graphics/drawable/Drawable:createFromXmlForDensity	(Landroid/content/res/Resources;Lorg/xmlpull/v1/XmlPullParser;ILandroid/content/res/Resources$Theme;)Landroid/graphics/drawable/Drawable;
    //   129: astore_1
    //   130: aload_2
    //   131: invokeinterface 417 1 0
    //   136: goto +31 -> 167
    //   139: astore_1
    //   140: goto +309 -> 449
    //   143: aload_0
    //   144: aload_0
    //   145: getfield 166	android/content/res/ResourcesImpl:mAssets	Landroid/content/res/AssetManager;
    //   148: aload_2
    //   149: getfield 381	android/util/TypedValue:assetCookie	I
    //   152: aload 5
    //   154: iconst_2
    //   155: invokevirtual 519	android/content/res/AssetManager:openNonAsset	(ILjava/lang/String;I)Ljava/io/InputStream;
    //   158: checkcast 521	android/content/res/AssetManager$AssetInputStream
    //   161: aload_1
    //   162: aload_2
    //   163: invokespecial 523	android/content/res/ResourcesImpl:decodeImageDrawable	(Landroid/content/res/AssetManager$AssetInputStream;Landroid/content/res/Resources;Landroid/util/TypedValue;)Landroid/graphics/drawable/Drawable;
    //   166: astore_1
    //   167: aload 12
    //   169: invokevirtual 526	android/content/res/ResourcesImpl$LookupStack:pop	()V
    //   172: ldc2_w 365
    //   175: invokestatic 421	android/os/Trace:traceEnd	(J)V
    //   178: getstatic 92	android/content/res/ResourcesImpl:TRACE_FOR_DETAILED_PRELOAD	Z
    //   181: ifeq +265 -> 446
    //   184: iload_3
    //   185: bipush 24
    //   187: iushr
    //   188: iconst_1
    //   189: if_icmpne +257 -> 446
    //   192: aload_0
    //   193: iload_3
    //   194: invokevirtual 529	android/content/res/ResourcesImpl:getResourceName	(I)Ljava/lang/String;
    //   197: astore 12
    //   199: aload 12
    //   201: ifnull +245 -> 446
    //   204: invokestatic 490	java/lang/System:nanoTime	()J
    //   207: lstore 14
    //   209: getstatic 495	android/graphics/Bitmap:sPreloadTracingNumInstantiatedBitmaps	I
    //   212: istore 16
    //   214: getstatic 498	android/graphics/Bitmap:sPreloadTracingTotalBitmapsSize	J
    //   217: lstore 17
    //   219: getstatic 500	android/content/res/ResourcesImpl:sPreloadTracingNumLoadedDrawables	I
    //   222: istore 19
    //   224: getstatic 500	android/content/res/ResourcesImpl:sPreloadTracingNumLoadedDrawables	I
    //   227: iconst_1
    //   228: iadd
    //   229: putstatic 500	android/content/res/ResourcesImpl:sPreloadTracingNumLoadedDrawables	I
    //   232: invokestatic 534	android/os/Process:myUid	()I
    //   235: ifne +9 -> 244
    //   238: iconst_1
    //   239: istore 4
    //   241: goto +6 -> 247
    //   244: iconst_0
    //   245: istore 4
    //   247: new 199	java/lang/StringBuilder
    //   250: dup
    //   251: invokespecial 200	java/lang/StringBuilder:<init>	()V
    //   254: astore 20
    //   256: iload 4
    //   258: ifeq +10 -> 268
    //   261: ldc_w 536
    //   264: astore_2
    //   265: goto +10 -> 275
    //   268: ldc_w 538
    //   271: astore_2
    //   272: goto -7 -> 265
    //   275: aload 20
    //   277: aload_2
    //   278: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   281: pop
    //   282: aload 20
    //   284: iload_3
    //   285: invokestatic 437	java/lang/Integer:toHexString	(I)Ljava/lang/String;
    //   288: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   291: pop
    //   292: aload 20
    //   294: ldc_w 540
    //   297: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   300: pop
    //   301: aload 20
    //   303: aload 12
    //   305: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   308: pop
    //   309: aload 20
    //   311: ldc_w 540
    //   314: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   317: pop
    //   318: aload 20
    //   320: aload 5
    //   322: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   325: pop
    //   326: aload 20
    //   328: ldc_w 540
    //   331: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   334: pop
    //   335: aload 20
    //   337: aload_1
    //   338: invokevirtual 544	java/lang/Object:getClass	()Ljava/lang/Class;
    //   341: invokevirtual 549	java/lang/Class:getCanonicalName	()Ljava/lang/String;
    //   344: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   347: pop
    //   348: aload 20
    //   350: ldc_w 551
    //   353: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   356: pop
    //   357: aload 20
    //   359: iload 19
    //   361: iload 11
    //   363: isub
    //   364: invokevirtual 456	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   367: pop
    //   368: aload 20
    //   370: ldc_w 553
    //   373: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   376: pop
    //   377: aload 20
    //   379: iload 16
    //   381: iload 8
    //   383: isub
    //   384: invokevirtual 456	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   387: pop
    //   388: aload 20
    //   390: ldc_w 555
    //   393: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   396: pop
    //   397: aload 20
    //   399: lload 17
    //   401: lload 9
    //   403: lsub
    //   404: invokevirtual 558	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   407: pop
    //   408: aload 20
    //   410: ldc_w 560
    //   413: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   416: pop
    //   417: aload 20
    //   419: lload 14
    //   421: lload 6
    //   423: lsub
    //   424: ldc2_w 561
    //   427: ldiv
    //   428: invokevirtual 558	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   431: pop
    //   432: ldc 26
    //   434: aload 20
    //   436: invokevirtual 213	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   439: invokestatic 568	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   442: pop
    //   443: goto +3 -> 446
    //   446: aload_1
    //   447: areturn
    //   448: astore_1
    //   449: aload 12
    //   451: invokevirtual 526	android/content/res/ResourcesImpl$LookupStack:pop	()V
    //   454: aload_1
    //   455: athrow
    //   456: new 357	java/lang/Exception
    //   459: astore_1
    //   460: aload_1
    //   461: ldc_w 570
    //   464: invokespecial 571	java/lang/Exception:<init>	(Ljava/lang/String;)V
    //   467: aload_1
    //   468: athrow
    //   469: astore_1
    //   470: goto +4 -> 474
    //   473: astore_1
    //   474: ldc2_w 365
    //   477: invokestatic 421	android/os/Trace:traceEnd	(J)V
    //   480: new 199	java/lang/StringBuilder
    //   483: dup
    //   484: invokespecial 200	java/lang/StringBuilder:<init>	()V
    //   487: astore_2
    //   488: aload_2
    //   489: ldc_w 430
    //   492: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   495: pop
    //   496: aload_2
    //   497: aload 5
    //   499: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   502: pop
    //   503: aload_2
    //   504: ldc_w 446
    //   507: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   510: pop
    //   511: aload_2
    //   512: iload_3
    //   513: invokestatic 437	java/lang/Integer:toHexString	(I)Ljava/lang/String;
    //   516: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   519: pop
    //   520: new 439	android/content/res/Resources$NotFoundException
    //   523: dup
    //   524: aload_2
    //   525: invokevirtual 213	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   528: invokespecial 440	android/content/res/Resources$NotFoundException:<init>	(Ljava/lang/String;)V
    //   531: astore_2
    //   532: aload_2
    //   533: aload_1
    //   534: invokevirtual 444	android/content/res/Resources$NotFoundException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   537: pop
    //   538: aload_2
    //   539: athrow
    //   540: new 199	java/lang/StringBuilder
    //   543: dup
    //   544: invokespecial 200	java/lang/StringBuilder:<init>	()V
    //   547: astore_1
    //   548: aload_1
    //   549: ldc_w 573
    //   552: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   555: pop
    //   556: aload_1
    //   557: aload_0
    //   558: iload_3
    //   559: invokevirtual 529	android/content/res/ResourcesImpl:getResourceName	(I)Ljava/lang/String;
    //   562: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   565: pop
    //   566: aload_1
    //   567: ldc_w 575
    //   570: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   573: pop
    //   574: aload_1
    //   575: iload_3
    //   576: invokestatic 437	java/lang/Integer:toHexString	(I)Ljava/lang/String;
    //   579: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   582: pop
    //   583: aload_1
    //   584: ldc_w 577
    //   587: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   590: pop
    //   591: aload_1
    //   592: aload_2
    //   593: invokevirtual 580	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   596: pop
    //   597: new 439	android/content/res/Resources$NotFoundException
    //   600: dup
    //   601: aload_1
    //   602: invokevirtual 213	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   605: invokespecial 440	android/content/res/Resources$NotFoundException:<init>	(Ljava/lang/String;)V
    //   608: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	609	0	this	ResourcesImpl
    //   0	609	1	paramResources	Resources
    //   0	609	2	paramTypedValue	TypedValue
    //   0	609	3	paramInt1	int
    //   0	609	4	paramInt2	int
    //   16	482	5	str	String
    //   19	403	6	l1	long
    //   22	362	8	i	int
    //   25	377	9	l2	long
    //   28	336	11	j	int
    //   74	376	12	localObject	Object
    //   99	3	13	bool	boolean
    //   207	213	14	l3	long
    //   212	172	16	k	int
    //   217	183	17	l4	long
    //   222	142	19	m	int
    //   254	181	20	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   106	136	139	finally
    //   91	101	448	finally
    //   143	167	448	finally
    //   449	456	469	java/lang/Exception
    //   449	456	469	java/lang/StackOverflowError
    //   456	469	469	java/lang/Exception
    //   456	469	469	java/lang/StackOverflowError
    //   76	91	473	java/lang/Exception
    //   76	91	473	java/lang/StackOverflowError
    //   167	172	473	java/lang/Exception
    //   167	172	473	java/lang/StackOverflowError
  }
  
  private boolean verifyPreloadConfig(int paramInt1, int paramInt2, int paramInt3, String paramString)
  {
    if ((0xBFFFEFFF & paramInt1 & paramInt2) != 0)
    {
      String str2;
      try
      {
        String str1 = getResourceName(paramInt3);
      }
      catch (Resources.NotFoundException localNotFoundException)
      {
        str2 = "?";
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Preloaded ");
      localStringBuilder.append(paramString);
      localStringBuilder.append(" resource #0x");
      localStringBuilder.append(Integer.toHexString(paramInt3));
      localStringBuilder.append(" (");
      localStringBuilder.append(str2);
      localStringBuilder.append(") that varies with configuration!!");
      Log.w("Resources", localStringBuilder.toString());
      return false;
    }
    return true;
  }
  
  public int calcConfigChanges(Configuration paramConfiguration)
  {
    if (paramConfiguration == null) {
      return -1;
    }
    mTmpConfig.setTo(paramConfiguration);
    int i = densityDpi;
    int j = i;
    if (i == 0) {
      j = mMetrics.noncompatDensityDpi;
    }
    mDisplayAdjustments.getCompatibilityInfo().applyToConfiguration(j, mTmpConfig);
    if (mTmpConfig.getLocales().isEmpty()) {
      mTmpConfig.setLocales(LocaleList.getDefault());
    }
    return mConfiguration.updateFrom(mTmpConfig);
  }
  
  void finishPreloading()
  {
    if (mPreloading)
    {
      if (TRACE_FOR_DETAILED_PRELOAD)
      {
        long l1 = SystemClock.uptimeMillis();
        long l2 = mPreloadTracingPreloadStartTime;
        long l3 = Bitmap.sPreloadTracingTotalBitmapsSize;
        long l4 = mPreloadTracingStartBitmapSize;
        long l5 = Bitmap.sPreloadTracingNumInstantiatedBitmaps;
        long l6 = mPreloadTracingStartBitmapCount;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Preload finished, ");
        localStringBuilder.append(l5 - l6);
        localStringBuilder.append(" bitmaps of ");
        localStringBuilder.append(l3 - l4);
        localStringBuilder.append(" bytes in ");
        localStringBuilder.append(l1 - l2);
        localStringBuilder.append(" ms");
        Log.d("Resources.preload", localStringBuilder.toString());
      }
      mPreloading = false;
      flushLayoutCache();
    }
  }
  
  public void flushLayoutCache()
  {
    synchronized (mCachedXmlBlocks)
    {
      Object localObject1 = mCachedXmlBlockCookies;
      int i = 0;
      Arrays.fill((int[])localObject1, 0);
      Arrays.fill(mCachedXmlBlockFiles, null);
      localObject1 = mCachedXmlBlocks;
      while (i < 4)
      {
        Object localObject3 = localObject1[i];
        if (localObject3 != null) {
          localObject3.close();
        }
        i++;
      }
      Arrays.fill((Object[])localObject1, null);
      return;
    }
  }
  
  ConfigurationBoundResourceCache<Animator> getAnimatorCache()
  {
    return mAnimatorCache;
  }
  
  public AssetManager getAssets()
  {
    return mAssets;
  }
  
  CompatibilityInfo getCompatibilityInfo()
  {
    return mDisplayAdjustments.getCompatibilityInfo();
  }
  
  Configuration getConfiguration()
  {
    return mConfiguration;
  }
  
  public DisplayAdjustments getDisplayAdjustments()
  {
    return mDisplayAdjustments;
  }
  
  DisplayMetrics getDisplayMetrics()
  {
    return mMetrics;
  }
  
  int getIdentifier(String paramString1, String paramString2, String paramString3)
  {
    if (paramString1 != null) {
      try
      {
        int i = Integer.parseInt(paramString1);
        return i;
      }
      catch (Exception localException)
      {
        return mAssets.getResourceIdentifier(paramString1, paramString2, paramString3);
      }
    }
    throw new NullPointerException("name is null");
  }
  
  LongSparseArray<Drawable.ConstantState> getPreloadedDrawables()
  {
    return sPreloadedDrawables[0];
  }
  
  CharSequence getQuantityText(int paramInt1, int paramInt2)
    throws Resources.NotFoundException
  {
    PluralRules localPluralRules = getPluralRule();
    Object localObject = mAssets.getResourceBagText(paramInt1, attrForQuantityCode(localPluralRules.select(paramInt2)));
    if (localObject != null) {
      return localObject;
    }
    localObject = mAssets.getResourceBagText(paramInt1, 16777220);
    if (localObject != null) {
      return localObject;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Plural resource ID #0x");
    ((StringBuilder)localObject).append(Integer.toHexString(paramInt1));
    ((StringBuilder)localObject).append(" quantity=");
    ((StringBuilder)localObject).append(paramInt2);
    ((StringBuilder)localObject).append(" item=");
    ((StringBuilder)localObject).append(localPluralRules.select(paramInt2));
    throw new Resources.NotFoundException(((StringBuilder)localObject).toString());
  }
  
  String getResourceEntryName(int paramInt)
    throws Resources.NotFoundException
  {
    Object localObject = mAssets.getResourceEntryName(paramInt);
    if (localObject != null) {
      return localObject;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Unable to find resource ID #0x");
    ((StringBuilder)localObject).append(Integer.toHexString(paramInt));
    throw new Resources.NotFoundException(((StringBuilder)localObject).toString());
  }
  
  String getResourceName(int paramInt)
    throws Resources.NotFoundException
  {
    Object localObject = mAssets.getResourceName(paramInt);
    if (localObject != null) {
      return localObject;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Unable to find resource ID #0x");
    ((StringBuilder)localObject).append(Integer.toHexString(paramInt));
    throw new Resources.NotFoundException(((StringBuilder)localObject).toString());
  }
  
  String getResourcePackageName(int paramInt)
    throws Resources.NotFoundException
  {
    Object localObject = mAssets.getResourcePackageName(paramInt);
    if (localObject != null) {
      return localObject;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Unable to find resource ID #0x");
    ((StringBuilder)localObject).append(Integer.toHexString(paramInt));
    throw new Resources.NotFoundException(((StringBuilder)localObject).toString());
  }
  
  String getResourceTypeName(int paramInt)
    throws Resources.NotFoundException
  {
    Object localObject = mAssets.getResourceTypeName(paramInt);
    if (localObject != null) {
      return localObject;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Unable to find resource ID #0x");
    ((StringBuilder)localObject).append(Integer.toHexString(paramInt));
    throw new Resources.NotFoundException(((StringBuilder)localObject).toString());
  }
  
  Configuration[] getSizeConfigurations()
  {
    return mAssets.getSizeConfigurations();
  }
  
  ConfigurationBoundResourceCache<StateListAnimator> getStateListAnimatorCache()
  {
    return mStateListAnimatorCache;
  }
  
  void getValue(int paramInt, TypedValue paramTypedValue, boolean paramBoolean)
    throws Resources.NotFoundException
  {
    if (mAssets.getResourceValue(paramInt, 0, paramTypedValue, paramBoolean)) {
      return;
    }
    paramTypedValue = new StringBuilder();
    paramTypedValue.append("Resource ID #0x");
    paramTypedValue.append(Integer.toHexString(paramInt));
    throw new Resources.NotFoundException(paramTypedValue.toString());
  }
  
  void getValue(String paramString, TypedValue paramTypedValue, boolean paramBoolean)
    throws Resources.NotFoundException
  {
    int i = getIdentifier(paramString, "string", null);
    if (i != 0)
    {
      getValue(i, paramTypedValue, paramBoolean);
      return;
    }
    paramTypedValue = new StringBuilder();
    paramTypedValue.append("String resource name ");
    paramTypedValue.append(paramString);
    throw new Resources.NotFoundException(paramTypedValue.toString());
  }
  
  void getValueForDensity(int paramInt1, int paramInt2, TypedValue paramTypedValue, boolean paramBoolean)
    throws Resources.NotFoundException
  {
    if (mAssets.getResourceValue(paramInt1, paramInt2, paramTypedValue, paramBoolean)) {
      return;
    }
    paramTypedValue = new StringBuilder();
    paramTypedValue.append("Resource ID #0x");
    paramTypedValue.append(Integer.toHexString(paramInt1));
    throw new Resources.NotFoundException(paramTypedValue.toString());
  }
  
  ColorStateList loadColorStateList(Resources paramResources, TypedValue paramTypedValue, int paramInt, Resources.Theme paramTheme)
    throws Resources.NotFoundException
  {
    long l1 = assetCookie;
    long l2 = data;
    if ((type >= 28) && (type <= 31)) {
      return getColorStateListFromInt(paramTypedValue, l1 << 32 | l2);
    }
    paramResources = loadComplexColorFromName(paramResources, paramTheme, paramTypedValue, paramInt);
    if ((paramResources != null) && ((paramResources instanceof ColorStateList))) {
      return (ColorStateList)paramResources;
    }
    paramResources = new StringBuilder();
    paramResources.append("Can't find ColorStateList from drawable resource ID #0x");
    paramResources.append(Integer.toHexString(paramInt));
    throw new Resources.NotFoundException(paramResources.toString());
  }
  
  ComplexColor loadComplexColor(Resources paramResources, TypedValue paramTypedValue, int paramInt, Resources.Theme paramTheme)
  {
    long l1 = assetCookie;
    long l2 = data;
    if ((type >= 28) && (type <= 31)) {
      return getColorStateListFromInt(paramTypedValue, l1 << 32 | l2);
    }
    String str = string.toString();
    if (str.endsWith(".xml")) {
      try
      {
        paramResources = loadComplexColorFromName(paramResources, paramTheme, paramTypedValue, paramInt);
        return paramResources;
      }
      catch (Exception paramResources)
      {
        paramTypedValue = new StringBuilder();
        paramTypedValue.append("File ");
        paramTypedValue.append(str);
        paramTypedValue.append(" from complex color resource ID #0x");
        paramTypedValue.append(Integer.toHexString(paramInt));
        paramTypedValue = new Resources.NotFoundException(paramTypedValue.toString());
        paramTypedValue.initCause(paramResources);
        throw paramTypedValue;
      }
    }
    paramResources = new StringBuilder();
    paramResources.append("File ");
    paramResources.append(str);
    paramResources.append(" from drawable resource ID #0x");
    paramResources.append(Integer.toHexString(paramInt));
    paramResources.append(": .xml extension required");
    throw new Resources.NotFoundException(paramResources.toString());
  }
  
  Drawable loadDrawable(Resources paramResources, TypedValue paramTypedValue, int paramInt1, int paramInt2, Resources.Theme paramTheme)
    throws Resources.NotFoundException
  {
    int i;
    if ((paramInt2 != 0) && (density != mMetrics.densityDpi)) {
      i = 0;
    } else {
      i = 1;
    }
    if ((paramInt2 > 0) && (density > 0) && (density != 65535)) {
      if (density == paramInt2) {
        density = mMetrics.densityDpi;
      } else {
        density = (density * mMetrics.densityDpi / paramInt2);
      }
    }
    try
    {
      boolean bool1;
      DrawableCache localDrawableCache;
      long l;
      if ((type >= 28) && (type <= 31))
      {
        bool1 = true;
        localDrawableCache = mColorDrawableCache;
        l = data;
      }
      else
      {
        bool1 = false;
        localDrawableCache = mDrawableCache;
        l = assetCookie << 32 | data;
      }
      if ((!mPreloading) && (i != 0))
      {
        localObject1 = localDrawableCache.getInstance(l, paramResources, paramTheme);
        if (localObject1 != null)
        {
          ((Drawable)localObject1).setChangingConfigurations(changingConfigurations);
          return localObject1;
        }
      }
      if (bool1) {
        localObject1 = (Drawable.ConstantState)sPreloadedColorDrawables.get(l);
      } else {
        localObject1 = (Drawable.ConstantState)sPreloadedDrawables[mConfiguration.getLayoutDirection()].get(l);
      }
      int j = 0;
      Object localObject2;
      if (localObject1 != null)
      {
        if ((TRACE_FOR_DETAILED_PRELOAD) && (paramInt1 >>> 24 == 1) && (Process.myUid() != 0))
        {
          String str = getResourceName(paramInt1);
          if (str != null)
          {
            localObject2 = new java/lang/StringBuilder;
            ((StringBuilder)localObject2).<init>();
            ((StringBuilder)localObject2).append("Hit preloaded FW drawable #");
            ((StringBuilder)localObject2).append(Integer.toHexString(paramInt1));
            ((StringBuilder)localObject2).append(" ");
            ((StringBuilder)localObject2).append(str);
            Log.d("Resources.preload", ((StringBuilder)localObject2).toString());
          }
        }
        localObject2 = ((Drawable.ConstantState)localObject1).newDrawable(paramResources);
      }
      for (;;)
      {
        break;
        if (bool1) {
          localObject2 = new ColorDrawable(data);
        } else {
          localObject2 = loadDrawableForCookie(paramResources, paramTypedValue, paramInt1, paramInt2);
        }
      }
      paramInt2 = j;
      if ((localObject2 instanceof DrawableContainer)) {
        paramInt2 = 1;
      }
      boolean bool2;
      if ((localObject2 != null) && (((Drawable)localObject2).canApplyTheme())) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      Object localObject1 = localObject2;
      if (bool2)
      {
        localObject1 = localObject2;
        if (paramTheme != null)
        {
          localObject1 = ((Drawable)localObject2).mutate();
          ((Drawable)localObject1).applyTheme(paramTheme);
          ((Drawable)localObject1).clearMutated();
        }
      }
      if (localObject1 != null)
      {
        ((Drawable)localObject1).setChangingConfigurations(changingConfigurations);
        if (i != 0) {
          try
          {
            cacheDrawable(paramTypedValue, bool1, localDrawableCache, paramTheme, bool2, l, (Drawable)localObject1);
            paramTypedValue = (TypedValue)localObject1;
            if (paramInt2 == 0) {
              break label530;
            }
            paramTheme = ((Drawable)localObject1).getConstantState();
            paramTypedValue = (TypedValue)localObject1;
            if (paramTheme == null) {
              break label530;
            }
            paramTypedValue = paramTheme.newDrawable(paramResources);
          }
          catch (Exception paramResources)
          {
            break label533;
          }
        }
      }
      paramTypedValue = (TypedValue)localObject1;
      label530:
      return paramTypedValue;
    }
    catch (Exception paramResources)
    {
      try
      {
        label533:
        paramTypedValue = getResourceName(paramInt1);
      }
      catch (Resources.NotFoundException paramTypedValue)
      {
        paramTypedValue = "(missing name)";
      }
      paramTheme = new StringBuilder();
      paramTheme.append("Drawable ");
      paramTheme.append(paramTypedValue);
      paramTheme.append(" with resource ID #0x");
      paramTheme.append(Integer.toHexString(paramInt1));
      paramResources = new Resources.NotFoundException(paramTheme.toString(), paramResources);
      paramResources.setStackTrace(new StackTraceElement[0]);
      throw paramResources;
    }
  }
  
  /* Error */
  public android.graphics.Typeface loadFont(Resources paramResources, TypedValue paramTypedValue, int paramInt)
  {
    // Byte code:
    //   0: aload_2
    //   1: getfield 361	android/util/TypedValue:string	Ljava/lang/CharSequence;
    //   4: ifnull +232 -> 236
    //   7: aload_2
    //   8: getfield 361	android/util/TypedValue:string	Ljava/lang/CharSequence;
    //   11: invokeinterface 364 1 0
    //   16: astore 4
    //   18: aload 4
    //   20: ldc_w 816
    //   23: invokevirtual 819	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   26: ifne +5 -> 31
    //   29: aconst_null
    //   30: areturn
    //   31: aload_0
    //   32: getfield 166	android/content/res/ResourcesImpl:mAssets	Landroid/content/res/AssetManager;
    //   35: aload 4
    //   37: invokestatic 825	android/graphics/Typeface:findFromCache	(Landroid/content/res/AssetManager;Ljava/lang/String;)Landroid/graphics/Typeface;
    //   40: astore 5
    //   42: aload 5
    //   44: ifnull +6 -> 50
    //   47: aload 5
    //   49: areturn
    //   50: ldc2_w 365
    //   53: aload 4
    //   55: invokestatic 372	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   58: aload 4
    //   60: ldc_w 827
    //   63: invokevirtual 378	java/lang/String:endsWith	(Ljava/lang/String;)Z
    //   66: ifeq +55 -> 121
    //   69: aload_0
    //   70: aload 4
    //   72: iload_3
    //   73: aload_2
    //   74: getfield 381	android/util/TypedValue:assetCookie	I
    //   77: ldc_w 829
    //   80: invokevirtual 387	android/content/res/ResourcesImpl:loadXmlResourceParser	(Ljava/lang/String;IILjava/lang/String;)Landroid/content/res/XmlResourceParser;
    //   83: astore_2
    //   84: aload_2
    //   85: aload_1
    //   86: invokestatic 835	android/content/res/FontResourcesParser:parse	(Lorg/xmlpull/v1/XmlPullParser;Landroid/content/res/Resources;)Landroid/content/res/FontResourcesParser$FamilyResourceEntry;
    //   89: astore_1
    //   90: aload_1
    //   91: ifnonnull +11 -> 102
    //   94: ldc2_w 365
    //   97: invokestatic 421	android/os/Trace:traceEnd	(J)V
    //   100: aconst_null
    //   101: areturn
    //   102: aload_1
    //   103: aload_0
    //   104: getfield 166	android/content/res/ResourcesImpl:mAssets	Landroid/content/res/AssetManager;
    //   107: aload 4
    //   109: invokestatic 839	android/graphics/Typeface:createFromResources	(Landroid/content/res/FontResourcesParser$FamilyResourceEntry;Landroid/content/res/AssetManager;Ljava/lang/String;)Landroid/graphics/Typeface;
    //   112: astore_1
    //   113: ldc2_w 365
    //   116: invokestatic 421	android/os/Trace:traceEnd	(J)V
    //   119: aload_1
    //   120: areturn
    //   121: aload_0
    //   122: getfield 166	android/content/res/ResourcesImpl:mAssets	Landroid/content/res/AssetManager;
    //   125: aload 4
    //   127: aload_2
    //   128: getfield 381	android/util/TypedValue:assetCookie	I
    //   131: invokestatic 842	android/graphics/Typeface:createFromResources	(Landroid/content/res/AssetManager;Ljava/lang/String;I)Landroid/graphics/Typeface;
    //   134: astore_1
    //   135: ldc2_w 365
    //   138: invokestatic 421	android/os/Trace:traceEnd	(J)V
    //   141: aload_1
    //   142: areturn
    //   143: astore_1
    //   144: goto +84 -> 228
    //   147: astore_1
    //   148: new 199	java/lang/StringBuilder
    //   151: astore_2
    //   152: aload_2
    //   153: invokespecial 200	java/lang/StringBuilder:<init>	()V
    //   156: aload_2
    //   157: ldc_w 844
    //   160: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   163: pop
    //   164: aload_2
    //   165: aload 4
    //   167: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   170: pop
    //   171: ldc 23
    //   173: aload_2
    //   174: invokevirtual 213	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   177: aload_1
    //   178: invokestatic 848	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   181: pop
    //   182: goto +38 -> 220
    //   185: astore_1
    //   186: new 199	java/lang/StringBuilder
    //   189: astore_2
    //   190: aload_2
    //   191: invokespecial 200	java/lang/StringBuilder:<init>	()V
    //   194: aload_2
    //   195: ldc_w 850
    //   198: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   201: pop
    //   202: aload_2
    //   203: aload 4
    //   205: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   208: pop
    //   209: ldc 23
    //   211: aload_2
    //   212: invokevirtual 213	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   215: aload_1
    //   216: invokestatic 848	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   219: pop
    //   220: ldc2_w 365
    //   223: invokestatic 421	android/os/Trace:traceEnd	(J)V
    //   226: aconst_null
    //   227: areturn
    //   228: ldc2_w 365
    //   231: invokestatic 421	android/os/Trace:traceEnd	(J)V
    //   234: aload_1
    //   235: athrow
    //   236: new 199	java/lang/StringBuilder
    //   239: dup
    //   240: invokespecial 200	java/lang/StringBuilder:<init>	()V
    //   243: astore_1
    //   244: aload_1
    //   245: ldc_w 573
    //   248: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   251: pop
    //   252: aload_1
    //   253: aload_0
    //   254: iload_3
    //   255: invokevirtual 529	android/content/res/ResourcesImpl:getResourceName	(I)Ljava/lang/String;
    //   258: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   261: pop
    //   262: aload_1
    //   263: ldc_w 575
    //   266: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   269: pop
    //   270: aload_1
    //   271: iload_3
    //   272: invokestatic 437	java/lang/Integer:toHexString	(I)Ljava/lang/String;
    //   275: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   278: pop
    //   279: aload_1
    //   280: ldc_w 852
    //   283: invokevirtual 209	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   286: pop
    //   287: aload_1
    //   288: aload_2
    //   289: invokevirtual 580	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   292: pop
    //   293: new 439	android/content/res/Resources$NotFoundException
    //   296: dup
    //   297: aload_1
    //   298: invokevirtual 213	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   301: invokespecial 440	android/content/res/Resources$NotFoundException:<init>	(Ljava/lang/String;)V
    //   304: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	305	0	this	ResourcesImpl
    //   0	305	1	paramResources	Resources
    //   0	305	2	paramTypedValue	TypedValue
    //   0	305	3	paramInt	int
    //   16	188	4	str	String
    //   40	8	5	localTypeface	android.graphics.Typeface
    // Exception table:
    //   from	to	target	type
    //   58	84	143	finally
    //   84	90	143	finally
    //   102	113	143	finally
    //   121	135	143	finally
    //   148	182	143	finally
    //   186	220	143	finally
    //   58	84	147	java/io/IOException
    //   84	90	147	java/io/IOException
    //   102	113	147	java/io/IOException
    //   121	135	147	java/io/IOException
    //   58	84	185	org/xmlpull/v1/XmlPullParserException
    //   84	90	185	org/xmlpull/v1/XmlPullParserException
    //   102	113	185	org/xmlpull/v1/XmlPullParserException
    //   121	135	185	org/xmlpull/v1/XmlPullParserException
  }
  
  XmlResourceParser loadXmlResourceParser(String paramString1, int paramInt1, int paramInt2, String paramString2)
    throws Resources.NotFoundException
  {
    if (paramInt1 != 0) {
      try
      {
        synchronized (mCachedXmlBlocks)
        {
          int[] arrayOfInt = mCachedXmlBlockCookies;
          Object localObject1 = mCachedXmlBlockFiles;
          XmlBlock[] arrayOfXmlBlock2 = mCachedXmlBlocks;
          int i = localObject1.length;
          for (int j = 0; j < i; j++) {
            if ((arrayOfInt[j] == paramInt2) && (localObject1[j] != null) && (localObject1[j].equals(paramString1)))
            {
              localObject1 = arrayOfXmlBlock2[j].newParser();
              return localObject1;
            }
          }
          XmlBlock localXmlBlock1 = mAssets.openXmlBlockAsset(paramInt2, paramString1);
          if (localXmlBlock1 != null)
          {
            j = (mLastCachedXmlBlockIndex + 1) % i;
            mLastCachedXmlBlockIndex = j;
            XmlBlock localXmlBlock2 = arrayOfXmlBlock2[j];
            if (localXmlBlock2 != null) {
              localXmlBlock2.close();
            }
            arrayOfInt[j] = paramInt2;
            localObject1[j] = paramString1;
            arrayOfXmlBlock2[j] = localXmlBlock1;
            localObject1 = localXmlBlock1.newParser();
            return localObject1;
          }
        }
        StringBuilder localStringBuilder2;
        localStringBuilder1 = new StringBuilder();
      }
      catch (Exception localException)
      {
        localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append("File ");
        localStringBuilder2.append(paramString1);
        localStringBuilder2.append(" from xml type ");
        localStringBuilder2.append(paramString2);
        localStringBuilder2.append(" resource ID #0x");
        localStringBuilder2.append(Integer.toHexString(paramInt1));
        paramString1 = new Resources.NotFoundException(localStringBuilder2.toString());
        paramString1.initCause(localException);
        throw paramString1;
      }
    }
    StringBuilder localStringBuilder1;
    localStringBuilder1.append("File ");
    localStringBuilder1.append(paramString1);
    localStringBuilder1.append(" from xml type ");
    localStringBuilder1.append(paramString2);
    localStringBuilder1.append(" resource ID #0x");
    localStringBuilder1.append(Integer.toHexString(paramInt1));
    throw new Resources.NotFoundException(localStringBuilder1.toString());
  }
  
  ThemeImpl newThemeImpl()
  {
    return new ThemeImpl();
  }
  
  ThemeImpl newThemeImpl(Resources.ThemeKey paramThemeKey)
  {
    ThemeImpl localThemeImpl = new ThemeImpl();
    mKey.setTo(paramThemeKey);
    localThemeImpl.rebase();
    return localThemeImpl;
  }
  
  InputStream openRawResource(int paramInt, TypedValue paramTypedValue)
    throws Resources.NotFoundException
  {
    getValue(paramInt, paramTypedValue, true);
    try
    {
      InputStream localInputStream = mAssets.openNonAsset(assetCookie, string.toString(), 2);
      return localInputStream;
    }
    catch (Exception localException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("File ");
      if (string == null) {
        paramTypedValue = "(null)";
      } else {
        paramTypedValue = string.toString();
      }
      localStringBuilder.append(paramTypedValue);
      localStringBuilder.append(" from drawable resource ID #0x");
      localStringBuilder.append(Integer.toHexString(paramInt));
      paramTypedValue = new Resources.NotFoundException(localStringBuilder.toString());
      paramTypedValue.initCause(localException);
      throw paramTypedValue;
    }
  }
  
  AssetFileDescriptor openRawResourceFd(int paramInt, TypedValue paramTypedValue)
    throws Resources.NotFoundException
  {
    getValue(paramInt, paramTypedValue, true);
    try
    {
      AssetFileDescriptor localAssetFileDescriptor = mAssets.openNonAssetFd(assetCookie, string.toString());
      return localAssetFileDescriptor;
    }
    catch (Exception localException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("File ");
      localStringBuilder.append(string.toString());
      localStringBuilder.append(" from drawable resource ID #0x");
      localStringBuilder.append(Integer.toHexString(paramInt));
      throw new Resources.NotFoundException(localStringBuilder.toString(), localException);
    }
  }
  
  public final void startPreloading()
  {
    synchronized (sSync)
    {
      if (!sPreloaded)
      {
        sPreloaded = true;
        mPreloading = true;
        mConfiguration.densityDpi = DisplayMetrics.DENSITY_DEVICE;
        updateConfiguration(null, null, null);
        if (TRACE_FOR_DETAILED_PRELOAD)
        {
          mPreloadTracingPreloadStartTime = SystemClock.uptimeMillis();
          mPreloadTracingStartBitmapSize = Bitmap.sPreloadTracingTotalBitmapsSize;
          mPreloadTracingStartBitmapCount = Bitmap.sPreloadTracingNumInstantiatedBitmaps;
          Log.d("Resources.preload", "Preload starting");
        }
        return;
      }
      IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
      localIllegalStateException.<init>("Resources already preloaded");
      throw localIllegalStateException;
    }
  }
  
  public void updateConfiguration(Configuration arg1, DisplayMetrics paramDisplayMetrics, CompatibilityInfo paramCompatibilityInfo)
  {
    Trace.traceBegin(8192L, "ResourcesImpl#updateConfiguration");
    try
    {
      Object localObject = mAccessLock;
      if (paramCompatibilityInfo != null) {
        try
        {
          mDisplayAdjustments.setCompatibilityInfo(paramCompatibilityInfo);
        }
        finally
        {
          break label606;
        }
      }
      if (paramDisplayMetrics != null) {
        mMetrics.setTo(paramDisplayMetrics);
      }
      mDisplayAdjustments.getCompatibilityInfo().applyToDisplayMetrics(mMetrics);
      int i = calcConfigChanges(???);
      ??? = mConfiguration.getLocales();
      paramDisplayMetrics = ???;
      if (???.isEmpty())
      {
        paramDisplayMetrics = LocaleList.getDefault();
        mConfiguration.setLocales(paramDisplayMetrics);
      }
      if (((i & 0x4) != 0) && (paramDisplayMetrics.size() > 1))
      {
        paramCompatibilityInfo = mAssets.getNonSystemLocales();
        ??? = paramCompatibilityInfo;
        if (LocaleList.isPseudoLocalesOnly(paramCompatibilityInfo))
        {
          paramCompatibilityInfo = mAssets.getLocales();
          ??? = paramCompatibilityInfo;
          if (LocaleList.isPseudoLocalesOnly(paramCompatibilityInfo)) {
            ??? = null;
          }
        }
        if (??? != null)
        {
          ??? = paramDisplayMetrics.getFirstMatchWithEnglishSupported(???);
          if ((??? != null) && (??? != paramDisplayMetrics.get(0)))
          {
            paramCompatibilityInfo = mConfiguration;
            LocaleList localLocaleList = new android/os/LocaleList;
            localLocaleList.<init>(???, paramDisplayMetrics);
            paramCompatibilityInfo.setLocales(localLocaleList);
          }
        }
      }
      if (mConfiguration.densityDpi != 0)
      {
        mMetrics.densityDpi = mConfiguration.densityDpi;
        mMetrics.density = (mConfiguration.densityDpi * 0.00625F);
      }
      ??? = mMetrics;
      float f1 = mMetrics.density;
      float f2;
      if (mConfiguration.fontScale != 0.0F) {
        f2 = mConfiguration.fontScale;
      } else {
        f2 = 1.0F;
      }
      scaledDensity = (f1 * f2);
      int j;
      if (mMetrics.widthPixels >= mMetrics.heightPixels) {
        j = mMetrics.widthPixels;
      }
      for (int k = mMetrics.heightPixels;; k = mMetrics.widthPixels)
      {
        break;
        j = mMetrics.heightPixels;
      }
      int m;
      if ((mConfiguration.keyboardHidden == 1) && (mConfiguration.hardKeyboardHidden == 2)) {
        m = 3;
      } else {
        m = mConfiguration.keyboardHidden;
      }
      mAssets.setConfiguration(mConfiguration.mcc, mConfiguration.mnc, adjustLanguageTag(mConfiguration.getLocales().get(0).toLanguageTag()), mConfiguration.orientation, mConfiguration.touchscreen, mConfiguration.densityDpi, mConfiguration.keyboard, m, mConfiguration.navigation, j, k, mConfiguration.smallestScreenWidthDp, mConfiguration.screenWidthDp, mConfiguration.screenHeightDp, mConfiguration.screenLayout, mConfiguration.uiMode, mConfiguration.colorMode, Build.VERSION.RESOURCES_SDK_INT);
      mDrawableCache.onConfigurationChange(i);
      mColorDrawableCache.onConfigurationChange(i);
      mComplexColorCache.onConfigurationChange(i);
      mAnimatorCache.onConfigurationChange(i);
      mStateListAnimatorCache.onConfigurationChange(i);
      flushLayoutCache();
      synchronized (sSync)
      {
        if (mPluralRule != null) {
          mPluralRule = PluralRules.forLocale(mConfiguration.getLocales().get(0));
        }
        return;
      }
      label606:
      throw ???;
    }
    finally
    {
      Trace.traceEnd(8192L);
    }
  }
  
  private static class LookupStack
  {
    private int[] mIds = new int[4];
    private int mSize = 0;
    
    private LookupStack() {}
    
    public boolean contains(int paramInt)
    {
      for (int i = 0; i < mSize; i++) {
        if (mIds[i] == paramInt) {
          return true;
        }
      }
      return false;
    }
    
    public void pop()
    {
      mSize -= 1;
    }
    
    public void push(int paramInt)
    {
      mIds = GrowingArrayUtils.append(mIds, mSize, paramInt);
      mSize += 1;
    }
  }
  
  public class ThemeImpl
  {
    private final AssetManager mAssets = mAssets;
    private final Resources.ThemeKey mKey = new Resources.ThemeKey();
    private final long mTheme = mAssets.createTheme();
    private int mThemeResId = 0;
    
    ThemeImpl() {}
    
    void applyStyle(int paramInt, boolean paramBoolean)
    {
      synchronized (mKey)
      {
        mAssets.applyStyleToTheme(mTheme, paramInt, paramBoolean);
        mThemeResId = paramInt;
        mKey.append(paramInt, paramBoolean);
        return;
      }
    }
    
    public void dump(int paramInt, String paramString1, String paramString2)
    {
      synchronized (mKey)
      {
        mAssets.dumpTheme(mTheme, paramInt, paramString1, paramString2);
        return;
      }
    }
    
    protected void finalize()
      throws Throwable
    {
      super.finalize();
      mAssets.releaseTheme(mTheme);
    }
    
    int[] getAllAttributes()
    {
      return mAssets.getStyleAttributes(getAppliedStyleResId());
    }
    
    int getAppliedStyleResId()
    {
      return mThemeResId;
    }
    
    int getChangingConfigurations()
    {
      synchronized (mKey)
      {
        int i = ActivityInfo.activityInfoConfigNativeToJava(AssetManager.nativeThemeGetChangingConfigurations(mTheme));
        return i;
      }
    }
    
    Resources.ThemeKey getKey()
    {
      return mKey;
    }
    
    long getNativeTheme()
    {
      return mTheme;
    }
    
    String[] getTheme()
    {
      synchronized (mKey)
      {
        int i = mKey.mCount;
        String[] arrayOfString = new String[i * 2];
        int j = 0;
        i--;
        while (j < arrayOfString.length)
        {
          int k = mKey.mResId[i];
          int m = mKey.mForce[i];
          try
          {
            arrayOfString[j] = getResourceName(k);
          }
          catch (Resources.NotFoundException localNotFoundException)
          {
            arrayOfString[j] = Integer.toHexString(j);
          }
          String str;
          if (m != 0) {
            str = "forced";
          } else {
            str = "not forced";
          }
          arrayOfString[(j + 1)] = str;
          j += 2;
          i--;
        }
        return arrayOfString;
      }
    }
    
    TypedArray obtainStyledAttributes(Resources.Theme paramTheme, AttributeSet paramAttributeSet, int[] paramArrayOfInt, int paramInt1, int paramInt2)
    {
      TypedArray localTypedArray;
      synchronized (mKey)
      {
        int i = paramArrayOfInt.length;
        localTypedArray = TypedArray.obtain(paramTheme.getResources(), i);
        paramAttributeSet = (XmlBlock.Parser)paramAttributeSet;
        mAssets.applyStyle(mTheme, paramInt1, paramInt2, paramAttributeSet, paramArrayOfInt, mDataAddress, mIndicesAddress);
      }
      throw paramTheme;
    }
    
    void rebase()
    {
      synchronized (mKey)
      {
        AssetManager.nativeThemeClear(mTheme);
        for (int i = 0; i < mKey.mCount; i++)
        {
          int j = mKey.mResId[i];
          int k = mKey.mForce[i];
          mAssets.applyStyleToTheme(mTheme, j, k);
        }
        return;
      }
    }
    
    boolean resolveAttribute(int paramInt, TypedValue paramTypedValue, boolean paramBoolean)
    {
      synchronized (mKey)
      {
        paramBoolean = mAssets.getThemeValue(mTheme, paramInt, paramTypedValue, paramBoolean);
        return paramBoolean;
      }
    }
    
    TypedArray resolveAttributes(Resources.Theme paramTheme, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
    {
      synchronized (mKey)
      {
        int i = paramArrayOfInt2.length;
        if ((paramArrayOfInt1 != null) && (i == paramArrayOfInt1.length))
        {
          TypedArray localTypedArray = TypedArray.obtain(paramTheme.getResources(), i);
          mAssets.resolveAttrs(mTheme, 0, 0, paramArrayOfInt1, paramArrayOfInt2, mData, mIndices);
          mTheme = paramTheme;
          mXml = null;
          return localTypedArray;
        }
        paramTheme = new java/lang/IllegalArgumentException;
        paramTheme.<init>("Base attribute values must the same length as attrs");
        throw paramTheme;
      }
    }
    
    void setTo(ThemeImpl paramThemeImpl)
    {
      synchronized (mKey)
      {
        synchronized (mKey)
        {
          AssetManager.nativeThemeCopy(mTheme, mTheme);
          mThemeResId = mThemeResId;
          mKey.setTo(paramThemeImpl.getKey());
          return;
        }
      }
    }
  }
}
