package android.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;
import com.android.internal.R.styleable;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public abstract class LayoutInflater
{
  private static final int[] ATTRS_THEME = { 16842752 };
  private static final String ATTR_LAYOUT = "layout";
  private static final ClassLoader BOOT_CLASS_LOADER = LayoutInflater.class.getClassLoader();
  private static final boolean DEBUG = false;
  private static final StackTraceElement[] EMPTY_STACK_TRACE;
  private static final String TAG = LayoutInflater.class.getSimpleName();
  private static final String TAG_1995 = "blink";
  private static final String TAG_INCLUDE = "include";
  private static final String TAG_MERGE = "merge";
  private static final String TAG_REQUEST_FOCUS = "requestFocus";
  private static final String TAG_TAG = "tag";
  static final Class<?>[] mConstructorSignature;
  private static final HashMap<String, Constructor<? extends View>> sConstructorMap;
  final Object[] mConstructorArgs = new Object[2];
  protected final Context mContext;
  private Factory mFactory;
  private Factory2 mFactory2;
  private boolean mFactorySet;
  private Filter mFilter;
  private HashMap<String, Boolean> mFilterMap;
  private Factory2 mPrivateFactory;
  private TypedValue mTempValue;
  
  static
  {
    EMPTY_STACK_TRACE = new StackTraceElement[0];
    mConstructorSignature = new Class[] { Context.class, AttributeSet.class };
    sConstructorMap = new HashMap();
  }
  
  protected LayoutInflater(Context paramContext)
  {
    mContext = paramContext;
  }
  
  protected LayoutInflater(LayoutInflater paramLayoutInflater, Context paramContext)
  {
    mContext = paramContext;
    mFactory = mFactory;
    mFactory2 = mFactory2;
    mPrivateFactory = mPrivateFactory;
    setFilter(mFilter);
  }
  
  static final void consumeChildElements(XmlPullParser paramXmlPullParser)
    throws XmlPullParserException, IOException
  {
    int i = paramXmlPullParser.getDepth();
    int j;
    do
    {
      j = paramXmlPullParser.next();
    } while (((j != 3) || (paramXmlPullParser.getDepth() > i)) && (j != 1));
  }
  
  private View createViewFromTag(View paramView, String paramString, Context paramContext, AttributeSet paramAttributeSet)
  {
    return createViewFromTag(paramView, paramString, paramContext, paramAttributeSet, false);
  }
  
  private void failNotAllowed(String paramString1, String paramString2, AttributeSet paramAttributeSet)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramAttributeSet.getPositionDescription());
    localStringBuilder.append(": Class not allowed to be inflated ");
    if (paramString2 != null)
    {
      paramAttributeSet = new StringBuilder();
      paramAttributeSet.append(paramString2);
      paramAttributeSet.append(paramString1);
      paramString1 = paramAttributeSet.toString();
    }
    localStringBuilder.append(paramString1);
    throw new InflateException(localStringBuilder.toString());
  }
  
  public static LayoutInflater from(Context paramContext)
  {
    paramContext = (LayoutInflater)paramContext.getSystemService("layout_inflater");
    if (paramContext != null) {
      return paramContext;
    }
    throw new AssertionError("LayoutInflater not found.");
  }
  
  /* Error */
  private void parseInclude(XmlPullParser paramXmlPullParser, Context paramContext, View paramView, AttributeSet paramAttributeSet)
    throws XmlPullParserException, IOException
  {
    // Byte code:
    //   0: aload_2
    //   1: astore 5
    //   3: aload_3
    //   4: instanceof 192
    //   7: ifeq +601 -> 608
    //   10: aload 5
    //   12: aload 4
    //   14: getstatic 102	android/view/LayoutInflater:ATTRS_THEME	[I
    //   17: invokevirtual 196	android/content/Context:obtainStyledAttributes	(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;
    //   20: astore 6
    //   22: aload 6
    //   24: iconst_0
    //   25: iconst_0
    //   26: invokevirtual 202	android/content/res/TypedArray:getResourceId	(II)I
    //   29: istore 7
    //   31: iload 7
    //   33: ifeq +9 -> 42
    //   36: iconst_1
    //   37: istore 8
    //   39: goto +6 -> 45
    //   42: iconst_0
    //   43: istore 8
    //   45: aload 5
    //   47: astore_2
    //   48: iload 8
    //   50: ifeq +15 -> 65
    //   53: new 204	android/view/ContextThemeWrapper
    //   56: dup
    //   57: aload 5
    //   59: iload 7
    //   61: invokespecial 207	android/view/ContextThemeWrapper:<init>	(Landroid/content/Context;I)V
    //   64: astore_2
    //   65: aload 6
    //   67: invokevirtual 210	android/content/res/TypedArray:recycle	()V
    //   70: aload 4
    //   72: aconst_null
    //   73: ldc 27
    //   75: iconst_0
    //   76: invokeinterface 214 4 0
    //   81: istore 9
    //   83: iload 9
    //   85: istore 7
    //   87: iload 9
    //   89: ifne +62 -> 151
    //   92: aload 4
    //   94: aconst_null
    //   95: ldc 27
    //   97: invokeinterface 218 3 0
    //   102: astore 5
    //   104: aload 5
    //   106: ifnull +35 -> 141
    //   109: aload 5
    //   111: invokevirtual 223	java/lang/String:length	()I
    //   114: ifle +27 -> 141
    //   117: aload_2
    //   118: invokevirtual 227	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   121: aload 5
    //   123: iconst_1
    //   124: invokevirtual 231	java/lang/String:substring	(I)Ljava/lang/String;
    //   127: ldc -23
    //   129: aload_2
    //   130: invokevirtual 236	android/content/Context:getPackageName	()Ljava/lang/String;
    //   133: invokevirtual 242	android/content/res/Resources:getIdentifier	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)I
    //   136: istore 7
    //   138: goto +13 -> 151
    //   141: new 168	android/view/InflateException
    //   144: dup
    //   145: ldc -12
    //   147: invokespecial 171	android/view/InflateException:<init>	(Ljava/lang/String;)V
    //   150: athrow
    //   151: aload_0
    //   152: getfield 246	android/view/LayoutInflater:mTempValue	Landroid/util/TypedValue;
    //   155: ifnonnull +14 -> 169
    //   158: aload_0
    //   159: new 248	android/util/TypedValue
    //   162: dup
    //   163: invokespecial 249	android/util/TypedValue:<init>	()V
    //   166: putfield 246	android/view/LayoutInflater:mTempValue	Landroid/util/TypedValue;
    //   169: iload 7
    //   171: istore 9
    //   173: iload 7
    //   175: ifeq +33 -> 208
    //   178: iload 7
    //   180: istore 9
    //   182: aload_2
    //   183: invokevirtual 253	android/content/Context:getTheme	()Landroid/content/res/Resources$Theme;
    //   186: iload 7
    //   188: aload_0
    //   189: getfield 246	android/view/LayoutInflater:mTempValue	Landroid/util/TypedValue;
    //   192: iconst_1
    //   193: invokevirtual 259	android/content/res/Resources$Theme:resolveAttribute	(ILandroid/util/TypedValue;Z)Z
    //   196: ifeq +12 -> 208
    //   199: aload_0
    //   200: getfield 246	android/view/LayoutInflater:mTempValue	Landroid/util/TypedValue;
    //   203: getfield 263	android/util/TypedValue:resourceId	I
    //   206: istore 9
    //   208: iload 9
    //   210: ifeq +345 -> 555
    //   213: aload_2
    //   214: invokevirtual 227	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   217: iload 9
    //   219: invokevirtual 267	android/content/res/Resources:getLayout	(I)Landroid/content/res/XmlResourceParser;
    //   222: astore 6
    //   224: aload 6
    //   226: astore 10
    //   228: aload 10
    //   230: invokestatic 273	android/util/Xml:asAttributeSet	(Lorg/xmlpull/v1/XmlPullParser;)Landroid/util/AttributeSet;
    //   233: astore 5
    //   235: aload 10
    //   237: invokeinterface 276 1 0
    //   242: istore 7
    //   244: iload 7
    //   246: iconst_2
    //   247: if_icmpeq +12 -> 259
    //   250: iload 7
    //   252: iconst_1
    //   253: if_icmpeq +6 -> 259
    //   256: goto -21 -> 235
    //   259: iload 7
    //   261: iconst_2
    //   262: if_icmpne +237 -> 499
    //   265: aload 10
    //   267: invokeinterface 279 1 0
    //   272: astore 11
    //   274: ldc 44
    //   276: aload 11
    //   278: invokevirtual 283	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   281: istore 12
    //   283: iload 12
    //   285: ifeq +21 -> 306
    //   288: aload_0
    //   289: aload 10
    //   291: aload_3
    //   292: aload_2
    //   293: aload 5
    //   295: iconst_0
    //   296: invokevirtual 287	android/view/LayoutInflater:rInflate	(Lorg/xmlpull/v1/XmlPullParser;Landroid/view/View;Landroid/content/Context;Landroid/util/AttributeSet;Z)V
    //   299: goto +184 -> 483
    //   302: astore_1
    //   303: goto +243 -> 546
    //   306: aload_0
    //   307: aload_3
    //   308: aload 11
    //   310: aload_2
    //   311: aload 5
    //   313: iload 8
    //   315: invokevirtual 149	android/view/LayoutInflater:createViewFromTag	(Landroid/view/View;Ljava/lang/String;Landroid/content/Context;Landroid/util/AttributeSet;Z)Landroid/view/View;
    //   318: astore 11
    //   320: aload_3
    //   321: checkcast 192	android/view/ViewGroup
    //   324: astore 13
    //   326: aload_2
    //   327: aload 4
    //   329: getstatic 292	com/android/internal/R$styleable:Include	[I
    //   332: invokevirtual 196	android/content/Context:obtainStyledAttributes	(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;
    //   335: astore_2
    //   336: aload_2
    //   337: iconst_0
    //   338: iconst_m1
    //   339: invokevirtual 202	android/content/res/TypedArray:getResourceId	(II)I
    //   342: istore 9
    //   344: aload_2
    //   345: iconst_1
    //   346: iconst_m1
    //   347: invokevirtual 295	android/content/res/TypedArray:getInt	(II)I
    //   350: istore 7
    //   352: aload_2
    //   353: invokevirtual 210	android/content/res/TypedArray:recycle	()V
    //   356: aconst_null
    //   357: astore_2
    //   358: aload 13
    //   360: aload 4
    //   362: invokevirtual 299	android/view/ViewGroup:generateLayoutParams	(Landroid/util/AttributeSet;)Landroid/view/ViewGroup$LayoutParams;
    //   365: astore_3
    //   366: aload_3
    //   367: astore_2
    //   368: goto +8 -> 376
    //   371: astore_1
    //   372: goto +174 -> 546
    //   375: astore_3
    //   376: aload_2
    //   377: ifnonnull +14 -> 391
    //   380: aload 13
    //   382: aload 5
    //   384: invokevirtual 299	android/view/ViewGroup:generateLayoutParams	(Landroid/util/AttributeSet;)Landroid/view/ViewGroup$LayoutParams;
    //   387: astore_2
    //   388: goto +3 -> 391
    //   391: aload 11
    //   393: aload_2
    //   394: invokevirtual 305	android/view/View:setLayoutParams	(Landroid/view/ViewGroup$LayoutParams;)V
    //   397: aload_0
    //   398: aload 10
    //   400: aload 11
    //   402: aload 5
    //   404: iconst_1
    //   405: invokevirtual 309	android/view/LayoutInflater:rInflateChildren	(Lorg/xmlpull/v1/XmlPullParser;Landroid/view/View;Landroid/util/AttributeSet;Z)V
    //   408: iload 9
    //   410: iconst_m1
    //   411: if_icmpeq +10 -> 421
    //   414: aload 11
    //   416: iload 9
    //   418: invokevirtual 313	android/view/View:setId	(I)V
    //   421: iload 7
    //   423: tableswitch	default:+25->448, 0:+47->470, 1:+38->461, 2:+28->451
    //   448: goto +28 -> 476
    //   451: aload 11
    //   453: bipush 8
    //   455: invokevirtual 316	android/view/View:setVisibility	(I)V
    //   458: goto +18 -> 476
    //   461: aload 11
    //   463: iconst_4
    //   464: invokevirtual 316	android/view/View:setVisibility	(I)V
    //   467: goto +9 -> 476
    //   470: aload 11
    //   472: iconst_0
    //   473: invokevirtual 316	android/view/View:setVisibility	(I)V
    //   476: aload 13
    //   478: aload 11
    //   480: invokevirtual 320	android/view/ViewGroup:addView	(Landroid/view/View;)V
    //   483: aload 6
    //   485: invokeinterface 323 1 0
    //   490: aload_1
    //   491: invokestatic 325	android/view/LayoutInflater:consumeChildElements	(Lorg/xmlpull/v1/XmlPullParser;)V
    //   494: return
    //   495: astore_1
    //   496: goto +50 -> 546
    //   499: new 168	android/view/InflateException
    //   502: astore_2
    //   503: new 153	java/lang/StringBuilder
    //   506: astore_1
    //   507: aload_1
    //   508: invokespecial 154	java/lang/StringBuilder:<init>	()V
    //   511: aload_1
    //   512: aload 10
    //   514: invokeinterface 326 1 0
    //   519: invokevirtual 161	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   522: pop
    //   523: aload_1
    //   524: ldc_w 328
    //   527: invokevirtual 161	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   530: pop
    //   531: aload_2
    //   532: aload_1
    //   533: invokevirtual 166	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   536: invokespecial 171	android/view/InflateException:<init>	(Ljava/lang/String;)V
    //   539: aload_2
    //   540: athrow
    //   541: astore_1
    //   542: goto +4 -> 546
    //   545: astore_1
    //   546: aload 6
    //   548: invokeinterface 323 1 0
    //   553: aload_1
    //   554: athrow
    //   555: aload 4
    //   557: aconst_null
    //   558: ldc 27
    //   560: invokeinterface 218 3 0
    //   565: astore_1
    //   566: new 153	java/lang/StringBuilder
    //   569: dup
    //   570: invokespecial 154	java/lang/StringBuilder:<init>	()V
    //   573: astore_2
    //   574: aload_2
    //   575: ldc_w 330
    //   578: invokevirtual 161	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   581: pop
    //   582: aload_2
    //   583: aload_1
    //   584: invokevirtual 161	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   587: pop
    //   588: aload_2
    //   589: ldc_w 332
    //   592: invokevirtual 161	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   595: pop
    //   596: new 168	android/view/InflateException
    //   599: dup
    //   600: aload_2
    //   601: invokevirtual 166	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   604: invokespecial 171	android/view/InflateException:<init>	(Ljava/lang/String;)V
    //   607: athrow
    //   608: new 168	android/view/InflateException
    //   611: dup
    //   612: ldc_w 334
    //   615: invokespecial 171	android/view/InflateException:<init>	(Ljava/lang/String;)V
    //   618: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	619	0	this	LayoutInflater
    //   0	619	1	paramXmlPullParser	XmlPullParser
    //   0	619	2	paramContext	Context
    //   0	619	3	paramView	View
    //   0	619	4	paramAttributeSet	AttributeSet
    //   1	402	5	localObject1	Object
    //   20	527	6	localObject2	Object
    //   29	393	7	i	int
    //   37	277	8	bool1	boolean
    //   81	336	9	j	int
    //   226	287	10	localObject3	Object
    //   272	207	11	localObject4	Object
    //   281	3	12	bool2	boolean
    //   324	153	13	localViewGroup	ViewGroup
    // Exception table:
    //   from	to	target	type
    //   288	299	302	finally
    //   358	366	371	finally
    //   380	388	371	finally
    //   358	366	375	java/lang/RuntimeException
    //   306	356	495	finally
    //   391	397	495	finally
    //   397	408	541	finally
    //   414	421	541	finally
    //   451	458	541	finally
    //   461	467	541	finally
    //   470	476	541	finally
    //   476	483	541	finally
    //   499	541	541	finally
    //   228	235	545	finally
    //   235	244	545	finally
    //   265	283	545	finally
  }
  
  private void parseViewTag(XmlPullParser paramXmlPullParser, View paramView, AttributeSet paramAttributeSet)
    throws XmlPullParserException, IOException
  {
    paramAttributeSet = paramView.getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.ViewTag);
    paramView.setTag(paramAttributeSet.getResourceId(1, 0), paramAttributeSet.getText(0));
    paramAttributeSet.recycle();
    consumeChildElements(paramXmlPullParser);
  }
  
  private final boolean verifyClassLoader(Constructor<? extends View> paramConstructor)
  {
    ClassLoader localClassLoader1 = paramConstructor.getDeclaringClass().getClassLoader();
    if (localClassLoader1 == BOOT_CLASS_LOADER) {
      return true;
    }
    paramConstructor = mContext.getClassLoader();
    ClassLoader localClassLoader2;
    do
    {
      if (localClassLoader1 == paramConstructor) {
        return true;
      }
      localClassLoader2 = paramConstructor.getParent();
      paramConstructor = localClassLoader2;
    } while (localClassLoader2 != null);
    return false;
  }
  
  public abstract LayoutInflater cloneInContext(Context paramContext);
  
  /* Error */
  public final View createView(String paramString1, String paramString2, AttributeSet paramAttributeSet)
    throws ClassNotFoundException, InflateException
  {
    // Byte code:
    //   0: getstatic 99	android/view/LayoutInflater:sConstructorMap	Ljava/util/HashMap;
    //   3: aload_1
    //   4: invokevirtual 382	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   7: checkcast 355	java/lang/reflect/Constructor
    //   10: astore 4
    //   12: aload 4
    //   14: astore 5
    //   16: aload 4
    //   18: ifnull +27 -> 45
    //   21: aload 4
    //   23: astore 5
    //   25: aload_0
    //   26: aload 4
    //   28: invokespecial 384	android/view/LayoutInflater:verifyClassLoader	(Ljava/lang/reflect/Constructor;)Z
    //   31: ifne +14 -> 45
    //   34: aconst_null
    //   35: astore 5
    //   37: getstatic 99	android/view/LayoutInflater:sConstructorMap	Ljava/util/HashMap;
    //   40: aload_1
    //   41: invokevirtual 387	java/util/HashMap:remove	(Ljava/lang/Object;)Ljava/lang/Object;
    //   44: pop
    //   45: aconst_null
    //   46: astore 6
    //   48: aconst_null
    //   49: astore 7
    //   51: aload 6
    //   53: astore 4
    //   55: ldc2_w 388
    //   58: aload_1
    //   59: invokestatic 395	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   62: aload 5
    //   64: ifnonnull +182 -> 246
    //   67: aload 6
    //   69: astore 4
    //   71: aload_0
    //   72: getfield 115	android/view/LayoutInflater:mContext	Landroid/content/Context;
    //   75: invokevirtual 360	android/content/Context:getClassLoader	()Ljava/lang/ClassLoader;
    //   78: astore 5
    //   80: aload_2
    //   81: ifnull +57 -> 138
    //   84: aload 6
    //   86: astore 4
    //   88: new 153	java/lang/StringBuilder
    //   91: astore 8
    //   93: aload 6
    //   95: astore 4
    //   97: aload 8
    //   99: invokespecial 154	java/lang/StringBuilder:<init>	()V
    //   102: aload 6
    //   104: astore 4
    //   106: aload 8
    //   108: aload_2
    //   109: invokevirtual 161	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   112: pop
    //   113: aload 6
    //   115: astore 4
    //   117: aload 8
    //   119: aload_1
    //   120: invokevirtual 161	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   123: pop
    //   124: aload 6
    //   126: astore 4
    //   128: aload 8
    //   130: invokevirtual 166	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   133: astore 8
    //   135: goto +6 -> 141
    //   138: aload_1
    //   139: astore 8
    //   141: aload 6
    //   143: astore 4
    //   145: aload 5
    //   147: aload 8
    //   149: invokevirtual 399	java/lang/ClassLoader:loadClass	(Ljava/lang/String;)Ljava/lang/Class;
    //   152: ldc_w 301
    //   155: invokevirtual 403	java/lang/Class:asSubclass	(Ljava/lang/Class;)Ljava/lang/Class;
    //   158: astore 8
    //   160: aload 8
    //   162: astore 4
    //   164: aload_0
    //   165: getfield 124	android/view/LayoutInflater:mFilter	Landroid/view/LayoutInflater$Filter;
    //   168: ifnull +37 -> 205
    //   171: aload 8
    //   173: ifnull +32 -> 205
    //   176: aload 8
    //   178: astore 4
    //   180: aload_0
    //   181: getfield 124	android/view/LayoutInflater:mFilter	Landroid/view/LayoutInflater$Filter;
    //   184: aload 8
    //   186: invokeinterface 407 2 0
    //   191: ifne +14 -> 205
    //   194: aload 8
    //   196: astore 4
    //   198: aload_0
    //   199: aload_1
    //   200: aload_2
    //   201: aload_3
    //   202: invokespecial 409	android/view/LayoutInflater:failNotAllowed	(Ljava/lang/String;Ljava/lang/String;Landroid/util/AttributeSet;)V
    //   205: aload 8
    //   207: astore 4
    //   209: aload 8
    //   211: getstatic 92	android/view/LayoutInflater:mConstructorSignature	[Ljava/lang/Class;
    //   214: invokevirtual 413	java/lang/Class:getConstructor	([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;
    //   217: astore 9
    //   219: aload 8
    //   221: astore 4
    //   223: aload 9
    //   225: iconst_1
    //   226: invokevirtual 417	java/lang/reflect/Constructor:setAccessible	(Z)V
    //   229: aload 8
    //   231: astore 4
    //   233: getstatic 99	android/view/LayoutInflater:sConstructorMap	Ljava/util/HashMap;
    //   236: aload_1
    //   237: aload 9
    //   239: invokevirtual 421	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   242: pop
    //   243: goto +252 -> 495
    //   246: aload 5
    //   248: astore 9
    //   250: aload 7
    //   252: astore 8
    //   254: aload 6
    //   256: astore 4
    //   258: aload_0
    //   259: getfield 124	android/view/LayoutInflater:mFilter	Landroid/view/LayoutInflater$Filter;
    //   262: ifnull +233 -> 495
    //   265: aload 6
    //   267: astore 4
    //   269: aload_0
    //   270: getfield 423	android/view/LayoutInflater:mFilterMap	Ljava/util/HashMap;
    //   273: aload_1
    //   274: invokevirtual 382	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   277: checkcast 425	java/lang/Boolean
    //   280: astore 10
    //   282: aload 10
    //   284: ifnonnull +169 -> 453
    //   287: aload 6
    //   289: astore 4
    //   291: aload_0
    //   292: getfield 115	android/view/LayoutInflater:mContext	Landroid/content/Context;
    //   295: invokevirtual 360	android/content/Context:getClassLoader	()Ljava/lang/ClassLoader;
    //   298: astore 9
    //   300: aload_2
    //   301: ifnull +57 -> 358
    //   304: aload 6
    //   306: astore 4
    //   308: new 153	java/lang/StringBuilder
    //   311: astore 8
    //   313: aload 6
    //   315: astore 4
    //   317: aload 8
    //   319: invokespecial 154	java/lang/StringBuilder:<init>	()V
    //   322: aload 6
    //   324: astore 4
    //   326: aload 8
    //   328: aload_2
    //   329: invokevirtual 161	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   332: pop
    //   333: aload 6
    //   335: astore 4
    //   337: aload 8
    //   339: aload_1
    //   340: invokevirtual 161	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   343: pop
    //   344: aload 6
    //   346: astore 4
    //   348: aload 8
    //   350: invokevirtual 166	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   353: astore 8
    //   355: goto +6 -> 361
    //   358: aload_1
    //   359: astore 8
    //   361: aload 6
    //   363: astore 4
    //   365: aload 9
    //   367: aload 8
    //   369: invokevirtual 399	java/lang/ClassLoader:loadClass	(Ljava/lang/String;)Ljava/lang/Class;
    //   372: ldc_w 301
    //   375: invokevirtual 403	java/lang/Class:asSubclass	(Ljava/lang/Class;)Ljava/lang/Class;
    //   378: astore 8
    //   380: aload 8
    //   382: ifnull +27 -> 409
    //   385: aload 8
    //   387: astore 4
    //   389: aload_0
    //   390: getfield 124	android/view/LayoutInflater:mFilter	Landroid/view/LayoutInflater$Filter;
    //   393: aload 8
    //   395: invokeinterface 407 2 0
    //   400: ifeq +9 -> 409
    //   403: iconst_1
    //   404: istore 11
    //   406: goto +6 -> 412
    //   409: iconst_0
    //   410: istore 11
    //   412: aload 8
    //   414: astore 4
    //   416: aload_0
    //   417: getfield 423	android/view/LayoutInflater:mFilterMap	Ljava/util/HashMap;
    //   420: aload_1
    //   421: iload 11
    //   423: invokestatic 429	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   426: invokevirtual 421	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   429: pop
    //   430: iload 11
    //   432: ifne +14 -> 446
    //   435: aload 8
    //   437: astore 4
    //   439: aload_0
    //   440: aload_1
    //   441: aload_2
    //   442: aload_3
    //   443: invokespecial 409	android/view/LayoutInflater:failNotAllowed	(Ljava/lang/String;Ljava/lang/String;Landroid/util/AttributeSet;)V
    //   446: aload 5
    //   448: astore 9
    //   450: goto +45 -> 495
    //   453: aload 5
    //   455: astore 9
    //   457: aload 7
    //   459: astore 8
    //   461: aload 6
    //   463: astore 4
    //   465: aload 10
    //   467: getstatic 433	java/lang/Boolean:FALSE	Ljava/lang/Boolean;
    //   470: invokevirtual 434	java/lang/Boolean:equals	(Ljava/lang/Object;)Z
    //   473: ifeq +22 -> 495
    //   476: aload 6
    //   478: astore 4
    //   480: aload_0
    //   481: aload_1
    //   482: aload_2
    //   483: aload_3
    //   484: invokespecial 409	android/view/LayoutInflater:failNotAllowed	(Ljava/lang/String;Ljava/lang/String;Landroid/util/AttributeSet;)V
    //   487: aload 7
    //   489: astore 8
    //   491: aload 5
    //   493: astore 9
    //   495: aload 8
    //   497: astore 4
    //   499: aload_0
    //   500: getfield 113	android/view/LayoutInflater:mConstructorArgs	[Ljava/lang/Object;
    //   503: iconst_0
    //   504: aaload
    //   505: astore 6
    //   507: aload 8
    //   509: astore 4
    //   511: aload_0
    //   512: getfield 113	android/view/LayoutInflater:mConstructorArgs	[Ljava/lang/Object;
    //   515: iconst_0
    //   516: aaload
    //   517: ifnonnull +17 -> 534
    //   520: aload 8
    //   522: astore 4
    //   524: aload_0
    //   525: getfield 113	android/view/LayoutInflater:mConstructorArgs	[Ljava/lang/Object;
    //   528: iconst_0
    //   529: aload_0
    //   530: getfield 115	android/view/LayoutInflater:mContext	Landroid/content/Context;
    //   533: aastore
    //   534: aload 8
    //   536: astore 4
    //   538: aload_0
    //   539: getfield 113	android/view/LayoutInflater:mConstructorArgs	[Ljava/lang/Object;
    //   542: astore 5
    //   544: aload 5
    //   546: iconst_1
    //   547: aload_3
    //   548: aastore
    //   549: aload 8
    //   551: astore 4
    //   553: aload 9
    //   555: aload 5
    //   557: invokevirtual 438	java/lang/reflect/Constructor:newInstance	([Ljava/lang/Object;)Ljava/lang/Object;
    //   560: checkcast 301	android/view/View
    //   563: astore 9
    //   565: aload 8
    //   567: astore 4
    //   569: aload 9
    //   571: instanceof 440
    //   574: ifeq +26 -> 600
    //   577: aload 8
    //   579: astore 4
    //   581: aload 9
    //   583: checkcast 440	android/view/ViewStub
    //   586: aload_0
    //   587: aload 5
    //   589: iconst_0
    //   590: aaload
    //   591: checkcast 88	android/content/Context
    //   594: invokevirtual 442	android/view/LayoutInflater:cloneInContext	(Landroid/content/Context;)Landroid/view/LayoutInflater;
    //   597: invokevirtual 446	android/view/ViewStub:setLayoutInflater	(Landroid/view/LayoutInflater;)V
    //   600: aload 8
    //   602: astore 4
    //   604: aload_0
    //   605: getfield 113	android/view/LayoutInflater:mConstructorArgs	[Ljava/lang/Object;
    //   608: iconst_0
    //   609: aload 6
    //   611: aastore
    //   612: ldc2_w 388
    //   615: invokestatic 450	android/os/Trace:traceEnd	(J)V
    //   618: aload 9
    //   620: areturn
    //   621: astore_1
    //   622: goto +286 -> 908
    //   625: astore 5
    //   627: new 168	android/view/InflateException
    //   630: astore 8
    //   632: new 153	java/lang/StringBuilder
    //   635: astore_2
    //   636: aload_2
    //   637: invokespecial 154	java/lang/StringBuilder:<init>	()V
    //   640: aload_2
    //   641: aload_3
    //   642: invokeinterface 157 1 0
    //   647: invokevirtual 161	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   650: pop
    //   651: aload_2
    //   652: ldc_w 452
    //   655: invokevirtual 161	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   658: pop
    //   659: aload 4
    //   661: ifnonnull +10 -> 671
    //   664: ldc_w 454
    //   667: astore_1
    //   668: goto +9 -> 677
    //   671: aload 4
    //   673: invokevirtual 455	java/lang/Class:getName	()Ljava/lang/String;
    //   676: astore_1
    //   677: aload_2
    //   678: aload_1
    //   679: invokevirtual 161	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   682: pop
    //   683: aload 8
    //   685: aload_2
    //   686: invokevirtual 166	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   689: aload 5
    //   691: invokespecial 458	android/view/InflateException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   694: aload 8
    //   696: getstatic 86	android/view/LayoutInflater:EMPTY_STACK_TRACE	[Ljava/lang/StackTraceElement;
    //   699: invokevirtual 462	android/view/InflateException:setStackTrace	([Ljava/lang/StackTraceElement;)V
    //   702: aload 8
    //   704: athrow
    //   705: astore_1
    //   706: aload_1
    //   707: athrow
    //   708: astore 5
    //   710: new 168	android/view/InflateException
    //   713: astore 4
    //   715: new 153	java/lang/StringBuilder
    //   718: astore 8
    //   720: aload 8
    //   722: invokespecial 154	java/lang/StringBuilder:<init>	()V
    //   725: aload 8
    //   727: aload_3
    //   728: invokeinterface 157 1 0
    //   733: invokevirtual 161	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   736: pop
    //   737: aload 8
    //   739: ldc_w 464
    //   742: invokevirtual 161	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   745: pop
    //   746: aload_2
    //   747: ifnull +31 -> 778
    //   750: new 153	java/lang/StringBuilder
    //   753: astore_3
    //   754: aload_3
    //   755: invokespecial 154	java/lang/StringBuilder:<init>	()V
    //   758: aload_3
    //   759: aload_2
    //   760: invokevirtual 161	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   763: pop
    //   764: aload_3
    //   765: aload_1
    //   766: invokevirtual 161	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   769: pop
    //   770: aload_3
    //   771: invokevirtual 166	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   774: astore_1
    //   775: goto +3 -> 778
    //   778: aload 8
    //   780: aload_1
    //   781: invokevirtual 161	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   784: pop
    //   785: aload 4
    //   787: aload 8
    //   789: invokevirtual 166	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   792: aload 5
    //   794: invokespecial 458	android/view/InflateException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   797: aload 4
    //   799: getstatic 86	android/view/LayoutInflater:EMPTY_STACK_TRACE	[Ljava/lang/StackTraceElement;
    //   802: invokevirtual 462	android/view/InflateException:setStackTrace	([Ljava/lang/StackTraceElement;)V
    //   805: aload 4
    //   807: athrow
    //   808: astore 8
    //   810: new 168	android/view/InflateException
    //   813: astore 4
    //   815: new 153	java/lang/StringBuilder
    //   818: astore 5
    //   820: aload 5
    //   822: invokespecial 154	java/lang/StringBuilder:<init>	()V
    //   825: aload 5
    //   827: aload_3
    //   828: invokeinterface 157 1 0
    //   833: invokevirtual 161	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   836: pop
    //   837: aload 5
    //   839: ldc_w 452
    //   842: invokevirtual 161	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   845: pop
    //   846: aload_2
    //   847: ifnull +31 -> 878
    //   850: new 153	java/lang/StringBuilder
    //   853: astore_3
    //   854: aload_3
    //   855: invokespecial 154	java/lang/StringBuilder:<init>	()V
    //   858: aload_3
    //   859: aload_2
    //   860: invokevirtual 161	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   863: pop
    //   864: aload_3
    //   865: aload_1
    //   866: invokevirtual 161	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   869: pop
    //   870: aload_3
    //   871: invokevirtual 166	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   874: astore_1
    //   875: goto +3 -> 878
    //   878: aload 5
    //   880: aload_1
    //   881: invokevirtual 161	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   884: pop
    //   885: aload 4
    //   887: aload 5
    //   889: invokevirtual 166	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   892: aload 8
    //   894: invokespecial 458	android/view/InflateException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   897: aload 4
    //   899: getstatic 86	android/view/LayoutInflater:EMPTY_STACK_TRACE	[Ljava/lang/StackTraceElement;
    //   902: invokevirtual 462	android/view/InflateException:setStackTrace	([Ljava/lang/StackTraceElement;)V
    //   905: aload 4
    //   907: athrow
    //   908: ldc2_w 388
    //   911: invokestatic 450	android/os/Trace:traceEnd	(J)V
    //   914: aload_1
    //   915: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	916	0	this	LayoutInflater
    //   0	916	1	paramString1	String
    //   0	916	2	paramString2	String
    //   0	916	3	paramAttributeSet	AttributeSet
    //   10	896	4	localObject1	Object
    //   14	574	5	localObject2	Object
    //   625	65	5	localException	Exception
    //   708	85	5	localClassCastException	ClassCastException
    //   818	70	5	localStringBuilder	StringBuilder
    //   46	564	6	localObject3	Object
    //   49	439	7	localObject4	Object
    //   91	697	8	localObject5	Object
    //   808	85	8	localNoSuchMethodException	NoSuchMethodException
    //   217	402	9	localObject6	Object
    //   280	186	10	localBoolean	Boolean
    //   404	27	11	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   55	62	621	finally
    //   71	80	621	finally
    //   88	93	621	finally
    //   97	102	621	finally
    //   106	113	621	finally
    //   117	124	621	finally
    //   128	135	621	finally
    //   145	160	621	finally
    //   164	171	621	finally
    //   180	194	621	finally
    //   198	205	621	finally
    //   209	219	621	finally
    //   223	229	621	finally
    //   233	243	621	finally
    //   258	265	621	finally
    //   269	282	621	finally
    //   291	300	621	finally
    //   308	313	621	finally
    //   317	322	621	finally
    //   326	333	621	finally
    //   337	344	621	finally
    //   348	355	621	finally
    //   365	380	621	finally
    //   389	403	621	finally
    //   416	430	621	finally
    //   439	446	621	finally
    //   465	476	621	finally
    //   480	487	621	finally
    //   499	507	621	finally
    //   511	520	621	finally
    //   524	534	621	finally
    //   538	544	621	finally
    //   553	565	621	finally
    //   569	577	621	finally
    //   581	600	621	finally
    //   604	612	621	finally
    //   627	659	621	finally
    //   671	677	621	finally
    //   677	705	621	finally
    //   706	708	621	finally
    //   710	746	621	finally
    //   750	775	621	finally
    //   778	808	621	finally
    //   810	846	621	finally
    //   850	875	621	finally
    //   878	908	621	finally
    //   55	62	625	java/lang/Exception
    //   71	80	625	java/lang/Exception
    //   88	93	625	java/lang/Exception
    //   97	102	625	java/lang/Exception
    //   106	113	625	java/lang/Exception
    //   117	124	625	java/lang/Exception
    //   128	135	625	java/lang/Exception
    //   145	160	625	java/lang/Exception
    //   164	171	625	java/lang/Exception
    //   180	194	625	java/lang/Exception
    //   198	205	625	java/lang/Exception
    //   209	219	625	java/lang/Exception
    //   223	229	625	java/lang/Exception
    //   233	243	625	java/lang/Exception
    //   258	265	625	java/lang/Exception
    //   269	282	625	java/lang/Exception
    //   291	300	625	java/lang/Exception
    //   308	313	625	java/lang/Exception
    //   317	322	625	java/lang/Exception
    //   326	333	625	java/lang/Exception
    //   337	344	625	java/lang/Exception
    //   348	355	625	java/lang/Exception
    //   365	380	625	java/lang/Exception
    //   389	403	625	java/lang/Exception
    //   416	430	625	java/lang/Exception
    //   439	446	625	java/lang/Exception
    //   465	476	625	java/lang/Exception
    //   480	487	625	java/lang/Exception
    //   499	507	625	java/lang/Exception
    //   511	520	625	java/lang/Exception
    //   524	534	625	java/lang/Exception
    //   538	544	625	java/lang/Exception
    //   553	565	625	java/lang/Exception
    //   569	577	625	java/lang/Exception
    //   581	600	625	java/lang/Exception
    //   604	612	625	java/lang/Exception
    //   55	62	705	java/lang/ClassNotFoundException
    //   71	80	705	java/lang/ClassNotFoundException
    //   88	93	705	java/lang/ClassNotFoundException
    //   97	102	705	java/lang/ClassNotFoundException
    //   106	113	705	java/lang/ClassNotFoundException
    //   117	124	705	java/lang/ClassNotFoundException
    //   128	135	705	java/lang/ClassNotFoundException
    //   145	160	705	java/lang/ClassNotFoundException
    //   164	171	705	java/lang/ClassNotFoundException
    //   180	194	705	java/lang/ClassNotFoundException
    //   198	205	705	java/lang/ClassNotFoundException
    //   209	219	705	java/lang/ClassNotFoundException
    //   223	229	705	java/lang/ClassNotFoundException
    //   233	243	705	java/lang/ClassNotFoundException
    //   258	265	705	java/lang/ClassNotFoundException
    //   269	282	705	java/lang/ClassNotFoundException
    //   291	300	705	java/lang/ClassNotFoundException
    //   308	313	705	java/lang/ClassNotFoundException
    //   317	322	705	java/lang/ClassNotFoundException
    //   326	333	705	java/lang/ClassNotFoundException
    //   337	344	705	java/lang/ClassNotFoundException
    //   348	355	705	java/lang/ClassNotFoundException
    //   365	380	705	java/lang/ClassNotFoundException
    //   389	403	705	java/lang/ClassNotFoundException
    //   416	430	705	java/lang/ClassNotFoundException
    //   439	446	705	java/lang/ClassNotFoundException
    //   465	476	705	java/lang/ClassNotFoundException
    //   480	487	705	java/lang/ClassNotFoundException
    //   499	507	705	java/lang/ClassNotFoundException
    //   511	520	705	java/lang/ClassNotFoundException
    //   524	534	705	java/lang/ClassNotFoundException
    //   538	544	705	java/lang/ClassNotFoundException
    //   553	565	705	java/lang/ClassNotFoundException
    //   569	577	705	java/lang/ClassNotFoundException
    //   581	600	705	java/lang/ClassNotFoundException
    //   604	612	705	java/lang/ClassNotFoundException
    //   55	62	708	java/lang/ClassCastException
    //   71	80	708	java/lang/ClassCastException
    //   88	93	708	java/lang/ClassCastException
    //   97	102	708	java/lang/ClassCastException
    //   106	113	708	java/lang/ClassCastException
    //   117	124	708	java/lang/ClassCastException
    //   128	135	708	java/lang/ClassCastException
    //   145	160	708	java/lang/ClassCastException
    //   164	171	708	java/lang/ClassCastException
    //   180	194	708	java/lang/ClassCastException
    //   198	205	708	java/lang/ClassCastException
    //   209	219	708	java/lang/ClassCastException
    //   223	229	708	java/lang/ClassCastException
    //   233	243	708	java/lang/ClassCastException
    //   258	265	708	java/lang/ClassCastException
    //   269	282	708	java/lang/ClassCastException
    //   291	300	708	java/lang/ClassCastException
    //   308	313	708	java/lang/ClassCastException
    //   317	322	708	java/lang/ClassCastException
    //   326	333	708	java/lang/ClassCastException
    //   337	344	708	java/lang/ClassCastException
    //   348	355	708	java/lang/ClassCastException
    //   365	380	708	java/lang/ClassCastException
    //   389	403	708	java/lang/ClassCastException
    //   416	430	708	java/lang/ClassCastException
    //   439	446	708	java/lang/ClassCastException
    //   465	476	708	java/lang/ClassCastException
    //   480	487	708	java/lang/ClassCastException
    //   499	507	708	java/lang/ClassCastException
    //   511	520	708	java/lang/ClassCastException
    //   524	534	708	java/lang/ClassCastException
    //   538	544	708	java/lang/ClassCastException
    //   553	565	708	java/lang/ClassCastException
    //   569	577	708	java/lang/ClassCastException
    //   581	600	708	java/lang/ClassCastException
    //   604	612	708	java/lang/ClassCastException
    //   55	62	808	java/lang/NoSuchMethodException
    //   71	80	808	java/lang/NoSuchMethodException
    //   88	93	808	java/lang/NoSuchMethodException
    //   97	102	808	java/lang/NoSuchMethodException
    //   106	113	808	java/lang/NoSuchMethodException
    //   117	124	808	java/lang/NoSuchMethodException
    //   128	135	808	java/lang/NoSuchMethodException
    //   145	160	808	java/lang/NoSuchMethodException
    //   164	171	808	java/lang/NoSuchMethodException
    //   180	194	808	java/lang/NoSuchMethodException
    //   198	205	808	java/lang/NoSuchMethodException
    //   209	219	808	java/lang/NoSuchMethodException
    //   223	229	808	java/lang/NoSuchMethodException
    //   233	243	808	java/lang/NoSuchMethodException
    //   258	265	808	java/lang/NoSuchMethodException
    //   269	282	808	java/lang/NoSuchMethodException
    //   291	300	808	java/lang/NoSuchMethodException
    //   308	313	808	java/lang/NoSuchMethodException
    //   317	322	808	java/lang/NoSuchMethodException
    //   326	333	808	java/lang/NoSuchMethodException
    //   337	344	808	java/lang/NoSuchMethodException
    //   348	355	808	java/lang/NoSuchMethodException
    //   365	380	808	java/lang/NoSuchMethodException
    //   389	403	808	java/lang/NoSuchMethodException
    //   416	430	808	java/lang/NoSuchMethodException
    //   439	446	808	java/lang/NoSuchMethodException
    //   465	476	808	java/lang/NoSuchMethodException
    //   480	487	808	java/lang/NoSuchMethodException
    //   499	507	808	java/lang/NoSuchMethodException
    //   511	520	808	java/lang/NoSuchMethodException
    //   524	534	808	java/lang/NoSuchMethodException
    //   538	544	808	java/lang/NoSuchMethodException
    //   553	565	808	java/lang/NoSuchMethodException
    //   569	577	808	java/lang/NoSuchMethodException
    //   581	600	808	java/lang/NoSuchMethodException
    //   604	612	808	java/lang/NoSuchMethodException
  }
  
  View createViewFromTag(View paramView, String paramString, Context paramContext, AttributeSet paramAttributeSet, boolean paramBoolean)
  {
    String str = paramString;
    if (paramString.equals("view")) {
      str = paramAttributeSet.getAttributeValue(null, "class");
    }
    Object localObject = paramContext;
    if (!paramBoolean)
    {
      localObject = paramContext.obtainStyledAttributes(paramAttributeSet, ATTRS_THEME);
      int i = ((TypedArray)localObject).getResourceId(0, 0);
      paramString = paramContext;
      if (i != 0) {
        paramString = new ContextThemeWrapper(paramContext, i);
      }
      ((TypedArray)localObject).recycle();
      localObject = paramString;
    }
    if (str.equals("blink")) {
      return new BlinkLayout((Context)localObject, paramAttributeSet);
    }
    try
    {
      if (mFactory2 != null) {
        paramContext = mFactory2.onCreateView(paramView, str, (Context)localObject, paramAttributeSet);
      }
      for (;;)
      {
        break;
        if (mFactory != null) {
          paramContext = mFactory.onCreateView(str, (Context)localObject, paramAttributeSet);
        } else {
          paramContext = null;
        }
      }
      paramString = paramContext;
      if (paramContext == null)
      {
        paramString = paramContext;
        if (mPrivateFactory != null) {
          paramString = mPrivateFactory.onCreateView(paramView, str, (Context)localObject, paramAttributeSet);
        }
      }
      paramContext = paramString;
      if (paramString == null)
      {
        paramString = mConstructorArgs[0];
        mConstructorArgs[0] = localObject;
        try
        {
          if (-1 == str.indexOf('.')) {}
          for (paramView = onCreateView(paramView, str, paramAttributeSet);; paramView = createView(str, null, paramAttributeSet)) {
            break;
          }
          mConstructorArgs[0] = paramString;
          paramContext = paramView;
        }
        finally
        {
          mConstructorArgs[0] = paramString;
        }
      }
      return paramContext;
    }
    catch (Exception paramView)
    {
      paramString = new StringBuilder();
      paramString.append(paramAttributeSet.getPositionDescription());
      paramString.append(": Error inflating class ");
      paramString.append(str);
      paramView = new InflateException(paramString.toString(), paramView);
      paramView.setStackTrace(EMPTY_STACK_TRACE);
      throw paramView;
    }
    catch (ClassNotFoundException paramString)
    {
      paramView = new StringBuilder();
      paramView.append(paramAttributeSet.getPositionDescription());
      paramView.append(": Error inflating class ");
      paramView.append(str);
      paramView = new InflateException(paramView.toString(), paramString);
      paramView.setStackTrace(EMPTY_STACK_TRACE);
      throw paramView;
    }
    catch (InflateException paramView)
    {
      throw paramView;
    }
  }
  
  public Context getContext()
  {
    return mContext;
  }
  
  public final Factory getFactory()
  {
    return mFactory;
  }
  
  public final Factory2 getFactory2()
  {
    return mFactory2;
  }
  
  public Filter getFilter()
  {
    return mFilter;
  }
  
  public View inflate(int paramInt, ViewGroup paramViewGroup)
  {
    boolean bool;
    if (paramViewGroup != null) {
      bool = true;
    } else {
      bool = false;
    }
    return inflate(paramInt, paramViewGroup, bool);
  }
  
  public View inflate(int paramInt, ViewGroup paramViewGroup, boolean paramBoolean)
  {
    XmlResourceParser localXmlResourceParser = getContext().getResources().getLayout(paramInt);
    try
    {
      paramViewGroup = inflate(localXmlResourceParser, paramViewGroup, paramBoolean);
      return paramViewGroup;
    }
    finally
    {
      localXmlResourceParser.close();
    }
  }
  
  public View inflate(XmlPullParser paramXmlPullParser, ViewGroup paramViewGroup)
  {
    boolean bool;
    if (paramViewGroup != null) {
      bool = true;
    } else {
      bool = false;
    }
    return inflate(paramXmlPullParser, paramViewGroup, bool);
  }
  
  /* Error */
  public View inflate(XmlPullParser paramXmlPullParser, ViewGroup paramViewGroup, boolean paramBoolean)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 113	android/view/LayoutInflater:mConstructorArgs	[Ljava/lang/Object;
    //   4: astore 4
    //   6: aload 4
    //   8: monitorenter
    //   9: ldc2_w 388
    //   12: ldc_w 503
    //   15: invokestatic 395	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   18: aload_0
    //   19: getfield 115	android/view/LayoutInflater:mContext	Landroid/content/Context;
    //   22: astore 5
    //   24: aload_1
    //   25: invokestatic 273	android/util/Xml:asAttributeSet	(Lorg/xmlpull/v1/XmlPullParser;)Landroid/util/AttributeSet;
    //   28: astore 6
    //   30: aload_0
    //   31: getfield 113	android/view/LayoutInflater:mConstructorArgs	[Ljava/lang/Object;
    //   34: iconst_0
    //   35: aaload
    //   36: checkcast 88	android/content/Context
    //   39: astore 7
    //   41: aload_0
    //   42: getfield 113	android/view/LayoutInflater:mConstructorArgs	[Ljava/lang/Object;
    //   45: iconst_0
    //   46: aload 5
    //   48: aastore
    //   49: aload_2
    //   50: astore 8
    //   52: aload_1
    //   53: invokeinterface 143 1 0
    //   58: istore 9
    //   60: iload 9
    //   62: iconst_2
    //   63: if_icmpeq +12 -> 75
    //   66: iload 9
    //   68: iconst_1
    //   69: if_icmpeq +6 -> 75
    //   72: goto -20 -> 52
    //   75: iload 9
    //   77: iconst_2
    //   78: if_icmpne +184 -> 262
    //   81: aload_1
    //   82: invokeinterface 504 1 0
    //   87: astore 10
    //   89: ldc 44
    //   91: aload 10
    //   93: invokevirtual 283	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   96: istore 11
    //   98: iload 11
    //   100: ifeq +38 -> 138
    //   103: aload_2
    //   104: ifnull +21 -> 125
    //   107: iload_3
    //   108: ifeq +17 -> 125
    //   111: aload_0
    //   112: aload_1
    //   113: aload_2
    //   114: aload 5
    //   116: aload 6
    //   118: iconst_0
    //   119: invokevirtual 287	android/view/LayoutInflater:rInflate	(Lorg/xmlpull/v1/XmlPullParser;Landroid/view/View;Landroid/content/Context;Landroid/util/AttributeSet;Z)V
    //   122: goto +101 -> 223
    //   125: new 168	android/view/InflateException
    //   128: astore_2
    //   129: aload_2
    //   130: ldc_w 506
    //   133: invokespecial 171	android/view/InflateException:<init>	(Ljava/lang/String;)V
    //   136: aload_2
    //   137: athrow
    //   138: aload_0
    //   139: aload_2
    //   140: aload 10
    //   142: aload 5
    //   144: aload 6
    //   146: invokespecial 508	android/view/LayoutInflater:createViewFromTag	(Landroid/view/View;Ljava/lang/String;Landroid/content/Context;Landroid/util/AttributeSet;)Landroid/view/View;
    //   149: astore 10
    //   151: aconst_null
    //   152: astore 5
    //   154: aload_2
    //   155: ifnull +30 -> 185
    //   158: aload_2
    //   159: aload 6
    //   161: invokevirtual 299	android/view/ViewGroup:generateLayoutParams	(Landroid/util/AttributeSet;)Landroid/view/ViewGroup$LayoutParams;
    //   164: astore 12
    //   166: aload 12
    //   168: astore 5
    //   170: iload_3
    //   171: ifne +14 -> 185
    //   174: aload 10
    //   176: aload 12
    //   178: invokevirtual 305	android/view/View:setLayoutParams	(Landroid/view/ViewGroup$LayoutParams;)V
    //   181: aload 12
    //   183: astore 5
    //   185: aload_0
    //   186: aload_1
    //   187: aload 10
    //   189: aload 6
    //   191: iconst_1
    //   192: invokevirtual 309	android/view/LayoutInflater:rInflateChildren	(Lorg/xmlpull/v1/XmlPullParser;Landroid/view/View;Landroid/util/AttributeSet;Z)V
    //   195: aload_2
    //   196: ifnull +15 -> 211
    //   199: iload_3
    //   200: ifeq +11 -> 211
    //   203: aload_2
    //   204: aload 10
    //   206: aload 5
    //   208: invokevirtual 511	android/view/ViewGroup:addView	(Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V
    //   211: aload_2
    //   212: ifnull +7 -> 219
    //   215: iload_3
    //   216: ifne +7 -> 223
    //   219: aload 10
    //   221: astore 8
    //   223: aload_0
    //   224: getfield 113	android/view/LayoutInflater:mConstructorArgs	[Ljava/lang/Object;
    //   227: iconst_0
    //   228: aload 7
    //   230: aastore
    //   231: aload_0
    //   232: getfield 113	android/view/LayoutInflater:mConstructorArgs	[Ljava/lang/Object;
    //   235: iconst_1
    //   236: aconst_null
    //   237: aastore
    //   238: ldc2_w 388
    //   241: invokestatic 450	android/os/Trace:traceEnd	(J)V
    //   244: aload 4
    //   246: monitorexit
    //   247: aload 8
    //   249: areturn
    //   250: astore_1
    //   251: goto +160 -> 411
    //   254: astore_2
    //   255: goto +64 -> 319
    //   258: astore_1
    //   259: goto +129 -> 388
    //   262: new 168	android/view/InflateException
    //   265: astore 8
    //   267: new 153	java/lang/StringBuilder
    //   270: astore_2
    //   271: aload_2
    //   272: invokespecial 154	java/lang/StringBuilder:<init>	()V
    //   275: aload_2
    //   276: aload_1
    //   277: invokeinterface 512 1 0
    //   282: invokevirtual 161	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   285: pop
    //   286: aload_2
    //   287: ldc_w 328
    //   290: invokevirtual 161	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   293: pop
    //   294: aload 8
    //   296: aload_2
    //   297: invokevirtual 166	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   300: invokespecial 171	android/view/InflateException:<init>	(Ljava/lang/String;)V
    //   303: aload 8
    //   305: athrow
    //   306: astore_2
    //   307: goto +12 -> 319
    //   310: astore_1
    //   311: goto +77 -> 388
    //   314: astore_1
    //   315: goto +96 -> 411
    //   318: astore_2
    //   319: new 168	android/view/InflateException
    //   322: astore 8
    //   324: new 153	java/lang/StringBuilder
    //   327: astore 5
    //   329: aload 5
    //   331: invokespecial 154	java/lang/StringBuilder:<init>	()V
    //   334: aload 5
    //   336: aload_1
    //   337: invokeinterface 512 1 0
    //   342: invokevirtual 161	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   345: pop
    //   346: aload 5
    //   348: ldc_w 514
    //   351: invokevirtual 161	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   354: pop
    //   355: aload 5
    //   357: aload_2
    //   358: invokevirtual 517	java/lang/Exception:getMessage	()Ljava/lang/String;
    //   361: invokevirtual 161	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   364: pop
    //   365: aload 8
    //   367: aload 5
    //   369: invokevirtual 166	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   372: aload_2
    //   373: invokespecial 458	android/view/InflateException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   376: aload 8
    //   378: getstatic 86	android/view/LayoutInflater:EMPTY_STACK_TRACE	[Ljava/lang/StackTraceElement;
    //   381: invokevirtual 462	android/view/InflateException:setStackTrace	([Ljava/lang/StackTraceElement;)V
    //   384: aload 8
    //   386: athrow
    //   387: astore_1
    //   388: new 168	android/view/InflateException
    //   391: astore_2
    //   392: aload_2
    //   393: aload_1
    //   394: invokevirtual 518	org/xmlpull/v1/XmlPullParserException:getMessage	()Ljava/lang/String;
    //   397: aload_1
    //   398: invokespecial 458	android/view/InflateException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   401: aload_2
    //   402: getstatic 86	android/view/LayoutInflater:EMPTY_STACK_TRACE	[Ljava/lang/StackTraceElement;
    //   405: invokevirtual 462	android/view/InflateException:setStackTrace	([Ljava/lang/StackTraceElement;)V
    //   408: aload_2
    //   409: athrow
    //   410: astore_1
    //   411: aload_0
    //   412: getfield 113	android/view/LayoutInflater:mConstructorArgs	[Ljava/lang/Object;
    //   415: iconst_0
    //   416: aload 7
    //   418: aastore
    //   419: aload_0
    //   420: getfield 113	android/view/LayoutInflater:mConstructorArgs	[Ljava/lang/Object;
    //   423: iconst_1
    //   424: aconst_null
    //   425: aastore
    //   426: ldc2_w 388
    //   429: invokestatic 450	android/os/Trace:traceEnd	(J)V
    //   432: aload_1
    //   433: athrow
    //   434: astore_1
    //   435: aload 4
    //   437: monitorexit
    //   438: aload_1
    //   439: athrow
    //   440: astore_1
    //   441: goto -6 -> 435
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	444	0	this	LayoutInflater
    //   0	444	1	paramXmlPullParser	XmlPullParser
    //   0	444	2	paramViewGroup	ViewGroup
    //   0	444	3	paramBoolean	boolean
    //   4	432	4	arrayOfObject	Object[]
    //   22	346	5	localObject1	Object
    //   28	162	6	localAttributeSet	AttributeSet
    //   39	378	7	localContext	Context
    //   50	335	8	localObject2	Object
    //   58	21	9	i	int
    //   87	133	10	localObject3	Object
    //   96	3	11	bool	boolean
    //   164	18	12	localLayoutParams	ViewGroup.LayoutParams
    // Exception table:
    //   from	to	target	type
    //   111	122	250	finally
    //   125	138	250	finally
    //   138	151	250	finally
    //   158	166	250	finally
    //   174	181	250	finally
    //   111	122	254	java/lang/Exception
    //   125	138	254	java/lang/Exception
    //   138	151	254	java/lang/Exception
    //   158	166	254	java/lang/Exception
    //   174	181	254	java/lang/Exception
    //   111	122	258	org/xmlpull/v1/XmlPullParserException
    //   125	138	258	org/xmlpull/v1/XmlPullParserException
    //   138	151	258	org/xmlpull/v1/XmlPullParserException
    //   158	166	258	org/xmlpull/v1/XmlPullParserException
    //   174	181	258	org/xmlpull/v1/XmlPullParserException
    //   185	195	306	java/lang/Exception
    //   203	211	306	java/lang/Exception
    //   262	306	306	java/lang/Exception
    //   185	195	310	org/xmlpull/v1/XmlPullParserException
    //   203	211	310	org/xmlpull/v1/XmlPullParserException
    //   262	306	310	org/xmlpull/v1/XmlPullParserException
    //   52	60	314	finally
    //   81	98	314	finally
    //   52	60	318	java/lang/Exception
    //   81	98	318	java/lang/Exception
    //   52	60	387	org/xmlpull/v1/XmlPullParserException
    //   81	98	387	org/xmlpull/v1/XmlPullParserException
    //   185	195	410	finally
    //   203	211	410	finally
    //   262	306	410	finally
    //   319	387	410	finally
    //   388	410	410	finally
    //   9	49	434	finally
    //   223	244	440	finally
    //   244	247	440	finally
    //   411	434	440	finally
    //   435	438	440	finally
  }
  
  protected View onCreateView(View paramView, String paramString, AttributeSet paramAttributeSet)
    throws ClassNotFoundException
  {
    return onCreateView(paramString, paramAttributeSet);
  }
  
  protected View onCreateView(String paramString, AttributeSet paramAttributeSet)
    throws ClassNotFoundException
  {
    return createView(paramString, "android.view.", paramAttributeSet);
  }
  
  void rInflate(XmlPullParser paramXmlPullParser, View paramView, Context paramContext, AttributeSet paramAttributeSet, boolean paramBoolean)
    throws XmlPullParserException, IOException
  {
    int i = paramXmlPullParser.getDepth();
    int j = 0;
    for (;;)
    {
      int k = paramXmlPullParser.next();
      if (((k == 3) && (paramXmlPullParser.getDepth() <= i)) || (k == 1)) {
        break label212;
      }
      if (k == 2)
      {
        Object localObject = paramXmlPullParser.getName();
        if ("requestFocus".equals(localObject))
        {
          j = 1;
          consumeChildElements(paramXmlPullParser);
        }
        else if ("tag".equals(localObject))
        {
          parseViewTag(paramXmlPullParser, paramView, paramAttributeSet);
        }
        else if ("include".equals(localObject))
        {
          if (paramXmlPullParser.getDepth() != 0) {
            parseInclude(paramXmlPullParser, paramContext, paramView, paramAttributeSet);
          } else {
            throw new InflateException("<include /> cannot be the root element");
          }
        }
        else
        {
          if ("merge".equals(localObject)) {
            break;
          }
          localObject = createViewFromTag(paramView, (String)localObject, paramContext, paramAttributeSet);
          ViewGroup localViewGroup = (ViewGroup)paramView;
          ViewGroup.LayoutParams localLayoutParams = localViewGroup.generateLayoutParams(paramAttributeSet);
          rInflateChildren(paramXmlPullParser, (View)localObject, paramAttributeSet, true);
          localViewGroup.addView((View)localObject, localLayoutParams);
        }
      }
    }
    throw new InflateException("<merge /> must be the root element");
    label212:
    if (j != 0) {
      paramView.restoreDefaultFocus();
    }
    if (paramBoolean) {
      paramView.onFinishInflate();
    }
  }
  
  final void rInflateChildren(XmlPullParser paramXmlPullParser, View paramView, AttributeSet paramAttributeSet, boolean paramBoolean)
    throws XmlPullParserException, IOException
  {
    rInflate(paramXmlPullParser, paramView, paramView.getContext(), paramAttributeSet, paramBoolean);
  }
  
  public void setFactory(Factory paramFactory)
  {
    if (!mFactorySet)
    {
      if (paramFactory != null)
      {
        mFactorySet = true;
        if (mFactory == null) {
          mFactory = paramFactory;
        } else {
          mFactory = new FactoryMerger(paramFactory, null, mFactory, mFactory2);
        }
        return;
      }
      throw new NullPointerException("Given factory can not be null");
    }
    throw new IllegalStateException("A factory has already been set on this LayoutInflater");
  }
  
  public void setFactory2(Factory2 paramFactory2)
  {
    if (!mFactorySet)
    {
      if (paramFactory2 != null)
      {
        mFactorySet = true;
        if (mFactory == null)
        {
          mFactory2 = paramFactory2;
          mFactory = paramFactory2;
        }
        else
        {
          paramFactory2 = new FactoryMerger(paramFactory2, paramFactory2, mFactory, mFactory2);
          mFactory2 = paramFactory2;
          mFactory = paramFactory2;
        }
        return;
      }
      throw new NullPointerException("Given factory can not be null");
    }
    throw new IllegalStateException("A factory has already been set on this LayoutInflater");
  }
  
  public void setFilter(Filter paramFilter)
  {
    mFilter = paramFilter;
    if (paramFilter != null) {
      mFilterMap = new HashMap();
    }
  }
  
  public void setPrivateFactory(Factory2 paramFactory2)
  {
    if (mPrivateFactory == null) {
      mPrivateFactory = paramFactory2;
    } else {
      mPrivateFactory = new FactoryMerger(paramFactory2, paramFactory2, mPrivateFactory, mPrivateFactory);
    }
  }
  
  private static class BlinkLayout
    extends FrameLayout
  {
    private static final int BLINK_DELAY = 500;
    private static final int MESSAGE_BLINK = 66;
    private boolean mBlink;
    private boolean mBlinkState;
    private final Handler mHandler = new Handler(new Handler.Callback()
    {
      public boolean handleMessage(Message paramAnonymousMessage)
      {
        if (what == 66)
        {
          if (mBlink)
          {
            LayoutInflater.BlinkLayout.access$102(LayoutInflater.BlinkLayout.this, mBlinkState ^ true);
            LayoutInflater.BlinkLayout.this.makeBlink();
          }
          invalidate();
          return true;
        }
        return false;
      }
    });
    
    public BlinkLayout(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
    }
    
    private void makeBlink()
    {
      Message localMessage = mHandler.obtainMessage(66);
      mHandler.sendMessageDelayed(localMessage, 500L);
    }
    
    protected void dispatchDraw(Canvas paramCanvas)
    {
      if (mBlinkState) {
        super.dispatchDraw(paramCanvas);
      }
    }
    
    protected void onAttachedToWindow()
    {
      super.onAttachedToWindow();
      mBlink = true;
      mBlinkState = true;
      makeBlink();
    }
    
    protected void onDetachedFromWindow()
    {
      super.onDetachedFromWindow();
      mBlink = false;
      mBlinkState = true;
      mHandler.removeMessages(66);
    }
  }
  
  public static abstract interface Factory
  {
    public abstract View onCreateView(String paramString, Context paramContext, AttributeSet paramAttributeSet);
  }
  
  public static abstract interface Factory2
    extends LayoutInflater.Factory
  {
    public abstract View onCreateView(View paramView, String paramString, Context paramContext, AttributeSet paramAttributeSet);
  }
  
  private static class FactoryMerger
    implements LayoutInflater.Factory2
  {
    private final LayoutInflater.Factory mF1;
    private final LayoutInflater.Factory2 mF12;
    private final LayoutInflater.Factory mF2;
    private final LayoutInflater.Factory2 mF22;
    
    FactoryMerger(LayoutInflater.Factory paramFactory1, LayoutInflater.Factory2 paramFactory21, LayoutInflater.Factory paramFactory2, LayoutInflater.Factory2 paramFactory22)
    {
      mF1 = paramFactory1;
      mF2 = paramFactory2;
      mF12 = paramFactory21;
      mF22 = paramFactory22;
    }
    
    public View onCreateView(View paramView, String paramString, Context paramContext, AttributeSet paramAttributeSet)
    {
      View localView;
      if (mF12 != null) {
        localView = mF12.onCreateView(paramView, paramString, paramContext, paramAttributeSet);
      } else {
        localView = mF1.onCreateView(paramString, paramContext, paramAttributeSet);
      }
      if (localView != null) {
        return localView;
      }
      if (mF22 != null) {
        paramView = mF22.onCreateView(paramView, paramString, paramContext, paramAttributeSet);
      } else {
        paramView = mF2.onCreateView(paramString, paramContext, paramAttributeSet);
      }
      return paramView;
    }
    
    public View onCreateView(String paramString, Context paramContext, AttributeSet paramAttributeSet)
    {
      View localView = mF1.onCreateView(paramString, paramContext, paramAttributeSet);
      if (localView != null) {
        return localView;
      }
      return mF2.onCreateView(paramString, paramContext, paramAttributeSet);
    }
  }
  
  public static abstract interface Filter
  {
    public abstract boolean onLoadClass(Class paramClass);
  }
}
